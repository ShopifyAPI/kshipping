package com.shopify.pdf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.api.lotte.delivery.LotteAddressResultData;
import com.shopify.api.lotte.delivery.LotteDeliveryData;

@Controller
public class WaybillController {
	
	@Autowired private LotteWaybill3PService lotteWaybill3PService;

    @RequestMapping(value = "/pdfreport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> citiesReport() {
    	
    	String zipFile = "c:/temp/address.json";
    	String orderFile = "c:/temp/order.json";
    	
/**
 * [LotteAddressResultData(result=success, message=, ukey=null, cityGunGu=금천구, dong=가산동, areaNo=08506, zipNo=153803, shipFare=0, airFare=0, tmlCd=20, tmlNm=A4, filtCd=375, brnshpNm=구로중앙(대), brnshpCd=10525, empNm=김영중, bldAnnm=, lttd=37.478169, lgtd=126.881599), LotteAddressResultData(result=2success, message=2, ukey=null, cityGunGu=2금천구, dong=2가산동, areaNo=208506, zipNo=2153803, shipFare=20, airFare=20, tmlCd=220, tmlNm=2A4, filtCd=2375, brnshpNm=2구로중앙(대), brnshpCd=210525, empNm=2김영중, bldAnnm=2, lttd=237.478169, lgtd=2126.881599), LotteAddressResultData(result=3success, message=3, ukey=null, cityGunGu=3금천구, dong=3가산동, areaNo=308506, zipNo=3153803, shipFare=30, airFare=30, tmlCd=320, tmlNm=3A4, filtCd=3375, brnshpNm=3구로중앙(대), brnshpCd=310525, empNm=3김영중, bldAnnm=3, lttd=337.478169, lgtd=3126.881599)]
    [LotteDeliveryData(jobCustCd=jobCustCd거래처코드, ustRtgSctCd=ustRtgSctCd출고반품구분, ordSct=ordSct오더구분, fareSctCd=fareSctCd운임구분, ordNo=ordNo주문번호, invNo=1234-5678-9012, orgInvNo=orgInvNo원송장번호, snperNm=snperNm송하인명, snperTel=02-1234-5678, snperCpno=010-1234-5678, snperZipcd=snperZipcd송하인우편번호, snperAdr=서울특별시 금천구 가산디지털1로 145, 11층 1104호 (가산동, 에이스 하이엔드 타워 3차), acperNm=acperNm수하인명, acperTel=(02) 1234-1234, acperCpno=010-2340-4567, acperZipcd=acperZipcd수하인우편번호, acperAdr=경기도 성남시 경기도 성남시 경기도 성남시 분당구 판교역로 241번길 20 미래에셋 벤처타워 1층, boxTypCd=boxTypCd박스크기, gdsNm=gdsNm상품명1|gdsNm상품명2|gdsNm상품명3, dlvMsgCont=dlvMsgCont배달메세지내용, cusMsgCont=cusMsgCont고객메세지내용, pickReqYmd=pickReqYmd집하요청일, bdpkSctCd=bdpkSctCd합포장여부, bdpkKey=bdpkKey합포장KEY), LotteDeliveryData(jobCustCd=2jobCustCd거래처코드, ustRtgSctCd=2ustRtgSctCd출고반품구분, ordSct=2ordSct오더구분, fareSctCd=2fareSctCd운임구분, ordNo=2ordNo주문번호, invNo=21234-5678-9012, orgInvNo=2orgInvNo원송장번호, snperNm=2snperNm송하인명, snperTel=(031)234-3456, snperCpno=010-1234-5678, snperZipcd=2snperZipcd송하인우편번호, snperAdr=경기도 성남시 경기도 성남시  분당구 판교역로 240 (삼평동) 삼환하이팩스 A동 4층, acperNm=2acperNm수하인명, acperTel=(053) 1234-1234, acperCpno=010-2345-0987, acperZipcd=2acperZipcd수하인우편번호, acperAdr=경기도 성남시 경기도 성남시 경기도 성남시  분당구 판교역로 235 에이치스퀘어 N동 7층, boxTypCd=2boxTypCd박스크기, gdsNm=2gdsNm상품명1|gdsNm상품명2|gdsNm상품명3|gdsNm상품명4, dlvMsgCont=2dlvMsgCont배달메세지내용, cusMsgCont=2cusMsgCont고객메세지내용, pickReqYmd=2pickReqYmd집하요청일, bdpkSctCd=2bdpkSctCd합포장여부, bdpkKey=null), LotteDeliveryData(jobCustCd=3jobCustCd거래처코드, ustRtgSctCd=3ustRtgSctCd출고반품구분, ordSct=3ordSct오더구분, fareSctCd=3fareSctCd운임구분, ordNo=3ordNo주문번호, invNo=31234-5678-9012, orgInvNo=3orgInvNo원송장번호, snperNm=3snperNm송하인명, snperTel=052)456-2345, snperCpno=010-1234-5678, snperZipcd=3snperZipcd송하인우편번호, snperAdr=서울시 강남구 서울시 강남구 테헤란로 152 역삼동, 강남파이낸스센터 22층, acperNm=3acperNm수하인명, acperTel=(0412) 1234-1234, acperCpno=010-234-7890, acperZipcd=12345, acperAdr=경기도 성남시 경기도 성남시 경기도 성남시 분당구 정자동 불정로 6 NAVER 그린팩토리 1층, boxTypCd=3boxTypCd박스크기, gdsNm=gdsNm상품명1|gdsNm상품명2|gdsNm상품명3|gdsNm상품명4|gdsNm상품명5, dlvMsgCont=3dlvMsgCont배달메세지내용, cusMsgCont=3cusMsgCont고객메세지내용, pickReqYmd=3pickReqYmd집하요청일, bdpkSctCd=3bdpkSctCd합포장여부, bdpkKey=null)]
 */
//    	File pdfFile = null;
//		try {
//			pdfFile = File.createTempFile("prefix-", "-suffix");
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//    	pdfFile.deleteOnExit();
    	
    	List<LotteAddressResultData> addressList = null;
    	List<LotteDeliveryData> deliveryList = null;

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			
			addressList = objectMapper.readValue(new File(zipFile), new TypeReference<List<LotteAddressResultData>>() { });
			System.out.println(addressList);
			deliveryList = objectMapper.readValue(new File(orderFile), new TypeReference<List<LotteDeliveryData>>() {});
			System.out.println(deliveryList);
			
//			LotteWaybill3P.printPage(addressList, orderList, pdfFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		


		Map<String, LotteAddressResultData> addressMap = addressList.parallelStream().collect(Collectors.toMap(LotteAddressResultData::getMasterCode, Function.identity()));
		Map<String, LotteDeliveryData> deliveryMap = deliveryList.parallelStream().collect(Collectors.toMap(LotteDeliveryData::getMasterCode, Function.identity()));
			         
		Set<String> keySet = addressMap.keySet();
		keySet.addAll( deliveryMap.keySet() );
		
//		List<String> both = keySet.stream().filter( k -> addressMap.containsKey(k) && deliveryMap.containsKey(k) ).collect(Collectors.toList());
	         
		
		String[] array = keySet.stream().toArray(String[]::new);
	          
	          
        
        ByteArrayInputStream bis = lotteWaybill3PService.generate(array, addressMap, deliveryMap);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=solugate.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}