package com.shopify.main;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.MainMapper;
import com.shopify.shop.ShopData;

/**
* DashBoard 페이지 서비스 
* @author : jwh
* @since  : 2020-02-17
* @desc   : DashBoard 페이지 서비스 
*/

@Service
@PropertySource("classpath:properties/config.properties")
public class MainService {
	private Logger LOGGER = LoggerFactory.getLogger(MainService.class);
	
	@Autowired private UtilFn util;
	@Autowired private MainMapper mainMapper;
	
	@Value("${post.api.regkey}") 	String postRegKey;
	@Value("${post.api.url}") 		String postUrl;
	@Value("${post.api.target}") 	String postTarget;
	@Value("${post.api.group}") 	String postGroup;
	
	/**
	 * DashBoard > 주문 현황
	 * @param MainData
	 * @param HttpSession
	 * @return MainData
	 */
	public MainData orderData(HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String nowDate = util.getDateElement("today");
		
		//이번달
		String startDate = util.getDateElement("yymm") + "-01";
		
		//다음달
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		Calendar cal = Calendar.getInstance( );
		cal.add (cal.MONTH, +1); 
		String endDate = df.format(cal.getTime()) + "-01";
		
		MainData main = new MainData();
		main.setEmail(email);
		main.setNowDate(nowDate);
		main.setStartDate(startDate);
		main.setEndDate(endDate);
		
		MainData mainData = mainMapper.selectOrderTotal(main);
		
		return mainData;
	}
/* YR 추가 */	
	
    /**
	 * NOTICE 조회
	 * @return
	 */
    public Map<String,Object> selectNotice(MainData mainData, HttpSession sess){
        
    	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);

    	String eMail = sd.getEmail();
    	
    	LOGGER.debug("selectNotice [eMail] : " + eMail);
    	LOGGER.debug("selectNotice [getEmail] : " + sd.getEmail());
    	
    	mainData.setNoti_svc("D010003");
    	mainData.setWriter(eMail);
    	mainData.setLocale(sd.getLocale());
		
    	int flagCount = mainMapper.NoticeFlagCount(mainData);
    	int dataCount = mainMapper.selectAllNoticeCount(mainData);
		int currentPage = mainData.getCurrentPage(); //현제 페이지
		int pageSize = mainData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		
		List<MainData> noticeList = new ArrayList<MainData>(); //데이터 리스트
		
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			mainData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			mainData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			noticeList = mainMapper.selectAllNotice(mainData);//데이터 리스트
		}
		
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", noticeList);
		map.put("paging", paging);
		map.put("flagCount", flagCount);
		
		return map;
		
    }

    /**
	 * NOTICE 상세 조회
	 * @return
	 */
    public MainData showNotice(MainData mainData, HttpSession sess){
    	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
    	mainData.setLocale(sd.getLocale());
		return mainMapper.showNotice(mainData);
    }
    
    /**
	 * NOTICE 상세 조회
	 * @return
	 */
    public List<MainData> showNoticeFile(MainData mainData, HttpSession sess){
    	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
    	mainData.setLocale(sd.getLocale());
		return mainMapper.showNoticeFile(mainData);
    }	
    
	
	/**
	 * DashBoard > 공시사항 (우체국 API연동)
	 * @param MainData
	 * @param HttpSession
	 * @return MainData
	 */
	public Map<String, Object> postNoticeData(MainData post) {
		
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		// http://biz.epost.go.kr/KpostPortal/openapi?regkey=test&target=notice&group=2;3;4;5&countPerPage=20&currentPage=1
		UriComponents ParamBuilder = UriComponentsBuilder.fromHttpUrl(postUrl)
                .queryParam("regkey", postRegKey)
                .queryParam("target", postTarget)
                .queryParam("group", postGroup)
                .queryParam("countPerPage", post.getPageSize())
                .queryParam("currentPage", post.getCurrentPage())
                .build(false);   
		 
		 DocumentBuilder builder;
		 Document doc = null;
		 
		 List<MainData> list = new ArrayList<MainData>();
		 Map<String, Object> paging = new HashMap<String, Object>();
		 
		 boolean errCode = false;
		 String errMsg = "";

		 try {
			 ResponseEntity<String> response = rest.exchange(ParamBuilder.toUriString(), HttpMethod.GET, new HttpEntity<String>(headers), String.class);
			 String body = response.getBody();
			 
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			 InputSource is = new InputSource(new StringReader(body));
			 builder = factory.newDocumentBuilder();
			 doc = builder.parse(is);
			 XPathFactory xpathFactory = XPathFactory.newInstance();
			 XPath xpath = xpathFactory.newXPath();
			 
			 // 페이징 정보
			 XPathExpression pageinfoExpr = xpath.compile("//notice/pageinfo");
			 NodeList pageinfo = (NodeList) pageinfoExpr.evaluate(doc, XPathConstants.NODESET);
			 
			 // 리스트 데이터
			 XPathExpression itemExpr = xpath.compile("//notice/itemlist/item");
			 NodeList itemList = (NodeList) itemExpr.evaluate(doc, XPathConstants.NODESET);
			 
			 if(itemList.getLength() > 0) {
				 int dataCount = 0;
				 int pageSize = 20;
				 int pageBlockSize = 10;
				 int currentPage = 0;
				 for (int i = 0; i < pageinfo.getLength(); i++) {
	                NodeList child = pageinfo.item(i).getChildNodes();
	                for (int j = 0; j < child.getLength(); j++) {
	                    Node node = child.item(j);
	                    
	                    if(Node.TEXT_NODE !=child.item(j).getNodeType()){
	                    	switch (node.getNodeName()) { 
	                    	case "totalCount": 
	                    		dataCount = Integer.parseInt(node.getTextContent());
	                    		break; 
	                    	case "totalPage": 
	                    		//paging.setTotalPage(Integer.parseInt(node.getTextContent()));
	                    		break;  
	                    	case "countPerPage": 
	                    		//paging.setCountPerPage(Integer.parseInt(node.getTextContent()));
	                    		pageSize = Integer.parseInt(node.getTextContent());
	                    		break;  
	                    	case "currentPage": 
	                    		currentPage = Integer.parseInt(node.getTextContent());
		                    	break;
	                    	} // case end
	                    } // if end
	                } // for end
	             } // for end
				 
				 //페이징 데이터 생성
				 paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); 
				 
				 // 리스트 데이터
				 for (int i = 0; i < itemList.getLength(); i++) {
	                NodeList child = itemList.item(i).getChildNodes();
	                MainData mainData = new MainData();
	                for (int j = 0; j < child.getLength(); j++) {
	                    Node node = child.item(j);
	                    
	                    if(Node.TEXT_NODE !=child.item(j).getNodeType()){
	                    	switch (node.getNodeName()) { 
	                    	case "seq": 
	                    		mainData.setSeq(Integer.parseInt(node.getTextContent()));
	                    		break; 
	                    	case "title": 
	                    		mainData.setTitle(node.getTextContent());
	                    		break;  
	                    	case "content": 
	                    		mainData.setContent(node.getTextContent());
	                    		break;  
	                    	case "writeday": 
		                    	mainData.setWriteday(node.getTextContent());
		                    	break;  
		                    case "group_name": 
			                    mainData.setGroup_name(node.getTextContent());
			                    break;  
		                    case "attachfilenm": 
			                    mainData.setAttachfilenm(node.getTextContent());
			                    break;
	                    	} // case end
	                    } // if end
	                } // for end
	                
	                list.add(mainData);
	             } // for end
				 
				 //LOGGER.debug("####### list : " + list.toString());
				 
				 errCode = true;
				 errMsg = "성공";
			 } else { // if end
				 XPathExpression errExpr = xpath.compile("//error");
				 NodeList errList = (NodeList) itemExpr.evaluate(doc, XPathConstants.NODESET);
				 
				// 페이징 정보
				 for (int j = 0; j < pageinfo.getLength(); j++) {
					 Node node = pageinfo.item(j);
					 
					 String errorCode = "";
					 String errorMessage = "";
					 
					 if(Node.TEXT_NODE !=pageinfo.item(j).getNodeType()){
						 switch (node.getNodeName()) { 
	                 	case "error_code": 
	                 		errorCode = node.getTextContent();
	                 		break; 
	                 	case "message": 
	                 		errorMessage = node.getTextContent();
	                 		break;  
	                 	} // case end
					 }
					 
					 errMsg = "[" + errorCode + "] " + errorMessage;
				 }
				 
				 errCode = false;
				 errMsg = "성공";
			 } // else end

		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
			errCode = false;
			errMsg = e.toString();
		}
	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("errCode", errCode);
		map.put("errMsg", errMsg);
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
	}
}
