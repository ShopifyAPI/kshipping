package com.shopify.tracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.TrackingMapper;

@Service
@Transactional
public class TrackingService {
	private Logger LOGGER = LoggerFactory.getLogger(TrackingService.class);
	@Autowired 
	private TrackingMapper trackingMapper;

	@Autowired	
	private UtilFn util;

	@Value("${lotteLogis.acper.name}") 		String lotteAcperNm;
	@Value("${lotteLogis.acper.tel}") 		String lotteAcperTel;
	@Value("${lotteLogis.acper.cpno}") 		String lotteAcperCpno;
	@Value("${lotteLogis.acper.zipcode}") 	String lotteAcperZipcd;
	@Value("${lotteLogis.acper.addr}") 		String lotteAcperAddr;

	/**
	 * 배송 > 배송목록
	 * @return
	 */
	public Map<String, Object> selectTrackingList(TrackingData ship, HttpSession sess) {
		String email = util.getUserEmail(sess);   // 세션 이메일 받아 오기
		String locale = util.getUserLocale(sess); // 세션 다국어 받아 오기 
		TrackingData sData = new TrackingData();
		if(ship == null) {
			sData.setCurrentPage(1);
			sData.setPageSize(10);
			ship = sData;
		}
		ship.setEmail(email);
		ship.setLocale(locale);

		boolean bSearchWordEncrypt = false ;
		String searchType = "" ;
		String searchWord = "" ;
		if (true) { // 검색조건별 파라미터 가공
			searchType = ship.getSearchType() ; // 검색조건
			if (!(searchType != null && searchType.trim().length() > 0)) { searchType = "" ; }
			searchWord = ship.getSearchWord() ; // 검색어
			if (!(searchWord != null && searchWord.trim().length() > 0)) { searchWord = "" ; }
			// 검색조건 및 검색어가 모두 유효한 값을 가졌는지 확인한다.
			if (searchType.trim().length() > 0 && searchType.trim().length() > 0) {
				// 검색조건이 암호화 대상 필드인 경우, 검색어를 일시적으로 암호화한다.
				// 암호화된 검색조건은 질의가 끝난 후 다시 입력 문자로 치환된다.
				if (searchType.equals("name")) {
					// TODO : '주문자 이름' 필드의 경우 쿼리에 보면 성 또는 이름, 전체가 같은지를 확인하도록 되어 있는데 대소문자가 틀린 경우는 어찌 할건지 고민해봐야 한다. 
					ship.setSearchWord(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord)) ;
					bSearchWordEncrypt = true ;
				} else if (searchType.equals("phone")) {
					ship.setSearchWord(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord)) ;
					bSearchWordEncrypt = true ;
				}
			}
		}
		
		int dataCount = trackingMapper.selectTrackingCount(ship); // 전체 건수 조회
		int currentPage = ship.getCurrentPage(); //현제 페이지
		int pageSize = ship.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<TrackingData> list = new ArrayList<TrackingData>(); //데이터 리스트 변수 선언 
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			ship.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			ship.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			System.out.println("################ship=>"+ship);
			list = trackingMapper.selectTracking(ship);
			//암호화풀기어 리스트 재생성
			int intA = 0;
			for(TrackingData item : list) {
				String fName = item.getBuyerFirstname();
				String lName = item.getBuyerLastname();
				String phone = item.getBuyerPhone();
				String sellerName = item.getSellerName();
				String customerName = item.getCustomerName();
				String buyerPhone = item.getBuyerPhone() ;
				
				if(fName != null) item.setBuyerFirstname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, fName));
				if(lName != null) item.setBuyerLastname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, lName));
				if(phone != null) item.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, phone));
				if(sellerName != null) item.setSellerName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sellerName));
				if(customerName != null) item.setCustomerName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, customerName));
				if(buyerPhone != null) item.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, buyerPhone));
				// 엑셀 저장 처리 
				if(currentPage == 0) {
					String courierCompany = item.getCourierCompany();
					if(courierCompany != null) {
						if(courierCompany.equals("B010020")) { // 롯데 정보
							item.setConsigneeName(lotteAcperNm);
							item.setConsigneeTel(lotteAcperTel);
							item.setConsigneeCpno(lotteAcperCpno);
							item.setConsigneeZipcode(lotteAcperZipcd);
							item.setConsigneeAddr1(lotteAcperAddr);
							item.setConsigneeAddr2("");
						} else if(courierCompany.equals("B010010")) { // 우체국 정보

						}
					}
				}
				list.set(intA, item);
				intA++;
			}
		}
		
		if (bSearchWordEncrypt) {
			// 암호화된 검색조건은 질의가 끝난 후 다시 입력 문자로 치환된다.
			ship.setSearchWord(searchWord) ;
		}

		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("paging", paging);
		return map;
	}
}