package com.shopify.schedule;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shopify.admin.statis.AdminStatisService;
import com.shopify.api.LotteLogisApiService;
import com.shopify.api.lotte.delivery.LotteDeliveryService;
import com.shopify.api.pantos.PantosService;
import com.shopify.common.EmailData;
import com.shopify.common.MailService;
import com.shopify.common.RestService;
import com.shopify.common.util.UtilFn;
import com.shopify.common.util.UtilFunc;
import com.shopify.mapper.EmailMapper;
import com.shopify.shipment.ShipmentData;

/**
* 배치 설정 
* @author : jwh
* @since  : 2020-02-13
* @desc   : 배치 프로세스 설정
*/
@Component
public class CronTable {

	private Logger LOGGER = LoggerFactory.getLogger(CronTable.class);
	
	@Autowired private LotteLogisApiService lotteLogis;
	@Autowired private AdminStatisService adminStatisService;
	@Autowired private LotteDeliveryService lotteDeliveryService;
	@Autowired private UtilFn util;
	@Autowired	private RestService restService;
	@Autowired 	private EmailMapper emailMapper;
	@Autowired 	private MailService mailService;
	@Value("${spring.profiles.active}") String active;
	@Value("${spring.profiles.activeIp}") String activeIp; // 10.0.10.177
	
	/*
	 *  매 5분 주기로 이메일 테이블 내용을 읽어서 발송 처리 : send_result
	 */
	@Scheduled(cron = "0 */5 * * * *")
	public void sendEmailJob() {
		
		if(util.getHostIpAddress().equals(activeIp)) { //10.0.10.177 서버에서반 실행
			LOGGER.debug("=======================[START-sendEmailJob]===================");
			int id;
			String to_email="";
			String subject = "";
			String contents = "";
			int[] resultId;
			int ret;
			boolean success = false;
			List<EmailData> sendList = emailMapper.selectEmailList();
			if(sendList != null && sendList.size() > 0)
			{
				LOGGER.debug("sendEmailList Size:" + sendList.size());
				for(EmailData email : sendList)
				{
					id = email.getId();
					to_email = email.getToEmail();
					subject = email.getSubject();
					contents = email.getContent();
					try {
						success = mailService.sendMail(to_email, subject, contents);
						LOGGER.debug(">to_email:" + to_email + " result:" + success);
					}catch(Exception ee) {
						ee.printStackTrace();
						success = false;
					}
					email.setResult(success);
					ret = emailMapper.updateEmailList(email);
					LOGGER.debug(">to_email:" + to_email + " ret:" + ret);
				}
			}
			LOGGER.debug("=======================[END]===================");
		}
	}
	
	/**
	 * 배치 > 롯데 로지스 배송 정보
	 * @param ShipmentPopupData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	// 매일 6,12,16,20시 실행
	@Scheduled(cron = "0 0 6,12,16,20 * * *")
	//매일 6시 실행
//	@Scheduled(cron = "0 0 6 * * *")
	// 조한두 수정: 배송 정보 테스트 : 30분마다 한번씩 
	//	@Scheduled(cron = "30 * * * * *")
	//@Scheduled(cron = "0 */48 * * * *")
	public void responseLotteTracking() {
        //LOGGER.debug("## CronTable >>> responseLotteTracking : Start");
        
        try {
        	//if("prod".equals(active)) {
        	if(!"".equals(active)) {
				if(util.getHostIpAddress().equals(activeIp)) { //10.0.10.177 서버에서반 실행
					lotteLogis.scheduledLotteTracking();
				}
        	} else {
        		lotteLogis.scheduledLotteTracking();
        	}
		} catch (JSONException e) {
			e.printStackTrace();
			//LOGGER.debug("## CronTable >>> responseLotteTracking[Error] : " + e.toString());
		}
        
        //LOGGER.debug("## CronTable >>> responseLotteTracking : End");
    }
	
	@Scheduled(cron = "0 5,35 * * * *")
	public void responseLotteHomeTracking() {
		
//		this.activeIp = "192.168.0.22";
//		LOGGER.debug("active=" + active);
//		LOGGER.debug("activeIp=" + activeIp);
		
		CronJobData cronData = new CronJobData("LotteHomeTracking");
		
		HttpHeaders headers = restService.composeLotteHeaders(false);
		int seq = 0;
		
		if( this.activeIp.equals( UtilFunc.getHostIpAddress() )  ) { //10.0.10.177 서버에서반 실행
				List<ShipmentData> list = lotteDeliveryService.listLotteHomeTracking();
				for( ShipmentData data : list ) {
					try {
						// 1. master record 1건 단위로 transacation 을 분리함
						cronData.initData();
						lotteDeliveryService.scheduledLotteHomeTracking(cronData, headers, data, seq++);
						
					} catch ( Exception e ) {
			    		e.printStackTrace();
			    		cronData.setData("F", data.getMasterCode() + " : exception = " + e.getMessage() );
			    	} 
					
					// 2. transaction 에러에 상관없이, master record 1건 단위로 cron log 를 기록함
			    	restService.insertCronJobData(cronData);
				}
		}
		
	}
	
	/**
	 * 배치 > 롯데 로지스 실패한 배송정보 다시 전송 8
	 * @param ShipmentPopupData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	// 매일 20시 실행
	@Scheduled(cron = "0 0 20 * * *")
	//@Scheduled(cron = "0 */34 * * * *")
	public void requestLotteReOrder() {
		//LOGGER.debug("## CronTable >>> requestLotteReOrder : Start");
        
		try {
			if("prod".equals(active)) {
				if(util.getHostIpAddress().equals(activeIp)) { //10.0.10.177 서버에서반 실행
					lotteLogis.scheduledLotteReOrder();
				}
        	} else {
        		lotteLogis.scheduledLotteReOrder();
        	}
		} catch (Exception e) {
			e.printStackTrace();
			//LOGGER.debug("## CronTable >>> requestLotteReOrder[Error] : " + e.toString());
		}

        //LOGGER.debug("## CronTable >>> requestLotteReOrder : End");
    }
	
	/**
	 * 배치 > 관리자 > 매출원장/손익통계 생성
	 * @param String
	 * @return void
	 */
	// 새벽 2시 실행
	@Scheduled(cron = "0 0 2 * * *")
	// 조한두 수정: 통계 정보 테스트  
	//@Scheduled(cron = "0 0 16 * * *")
	public void makeStatisSales() {
		//LOGGER.debug("## CronTable >>> makeStatisSales : Start");
		String nowDate = util.getDateElement("dd-1");
		
		try {
			//if("prod".equals(active)) {
			if(!"".equals(active)) {
				if(util.getHostIpAddress().equals(activeIp)) { //10.0.10.177 서버에서반 실행
					adminStatisService.insertStatisSales(nowDate); // 매출원장/손익통계
				}
	    	} else {
	    		adminStatisService.insertStatisSales(nowDate); // 매출원장/손익통계
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 배치 > 판토스 스케줄
	 * @param HttpSession
	 */
	@Scheduled(cron = "0 * * * * *")
	//@Scheduled(cron = "0 */34 * * * *")
	public void requestPantosTracking() {
		/*
		LOGGER.debug("## CronTable >>> requestPantosTracking : Start");
       
		try {
			if("prod".equals(active)) {
				if(util.getHostIpAddress().equals(activeIp)) { //10.0.10.177 서버에서반 실행
					pantosApi.updateTracking();
				}
        	} else {
        		pantosApi.updateTracking();
        	}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug("## CronTable >>> requestPantosTracking[Error] : " + e.toString());
		}

        LOGGER.debug("## CronTable >>> requestPantosTracking : End");
        */
    }
}
