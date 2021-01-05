package com.shopify.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.common.SpConstants;
import com.shopify.common.util.Hmac;
import com.shopify.common.util.UtilFn;
import com.shopify.config.RestfulConfig;
import com.shopify.order.OrderData;
import com.shopify.order.OrderService;
import com.shopify.seller.SellerData;
import com.shopify.seller.SellerService;
import com.shopify.shipment.ShipmentService;
import com.shopify.shipment.popup.ShipmentPopupData;
import com.shopify.shop.ShopData;
import com.shopify.shop.ShopService;


@Controller
public class ShopifyApiController {
	private Logger LOGGER = LoggerFactory.getLogger(ShopifyApiController.class);
	
	@Autowired	RestTemplate restTemplate;
	@Autowired	RestfulConfig restConfig;
	@Autowired	UtilFn util;
	@Autowired	Hmac hmac;
	@Autowired	OrderService orderService;
	@Autowired	ShopService shopService;
	@Autowired	SellerService sellerService;
	@Autowired	ShipmentService shipmentService;
	@Autowired	ShopifyApiService spApiService;
	@Autowired	ShopifyOutApiService spApiOutService;
	
	//properties/shopifyApi.properties 에 설정된 api 호출
	@Value("${shopify.AppId}")						       			String AppId;
	@Value("${shopify.AppPw}")						       			String AppPw;
	@Value("${shopify.ShopUrl}")					       			String ShopUrl;
	@Value("${shopify.App.callbackUrl}")					       	String AppCallbackUrl;
	@Value("${shopify.App.carrierName}")					    String AppCarrierName;
	
		
	@Value("${Auth.Get.Shop}")						       			String authShop;
	@Value("${Auth.Get.OAuth}")					       			String oauth;
	@Value("${Order.Get.Orders}")					       			String getOrder;
	@Value("${Order.Get.OrderId}")				       			String getOrderId;
	@Value("${AccessToken.Get.Search}")		       			String getAccessToken;
	@Value("${Fulfillment.Get.Event.Create.Step1}")      	String getFulfillmentCreateStep1;
	@Value("${Fulfillment.Get.Event.Create.Step2}")      	String getFulfillmentCreateStep2;
	@Value("${Fulfillment.Get.Event.Create.Step3}")      	String getFulfillmentCreateStep3;
	@Value("${Fulfillment.Post.Event.Create.Step4}")      	String getFulfillmentCreateStep4;
	
	@Value("${Fulfillment.Get.OrderId.Fullist}")      			String getFulfillmentList;
	@Value("${Fulfillment.Post.OrderId.Fullist}")     			String setFulfillmentList;
	@Value("${Billing.Post.ApplicationCharge}")      			String procBilling;
	@Value("${Carrier.Post.Create}")								String CreateCarrier;
//	@Value("${Carrier.Put.Edit}")									String EditCarrier;
	@Value("${Carrier.Get.List}")									String ListCarrier;
	
	@Value("${shopify.scope}")						       			String scope;
	@Value("${shopify.redirectUri}")				       			String redirectUri;
	@Value("${shopify.authUri}")					       			String authUri;
	

	//상점 정보 호출
	@RequestMapping("/shopifyShopAuth")
	@ResponseBody
	public ModelAndView shopifyShopAuth(HttpSession sess, HttpServletResponse res) {
		
		ModelAndView mav = spApiService.shopifyShopAuth(sess, res);
		return mav;
	}
	
	//상점설치시 호출됨(권한신청)
	@GetMapping("/shopifyInstall")
	public RedirectView shopifyInstall(HttpServletRequest req) {
		LOGGER.debug("############shopifyInstall##############");
		
		String shopifyAuthUri = spApiService.shopifyInstall(req);
		return new RedirectView(shopifyAuthUri);
	}

	//상점호출시 install이후 토근발행
	@GetMapping("/shopifyGenToken")
	//public ResponseEntity<Object> shopifyGenToken(HttpServletRequest req, HttpServletResponse res, HttpSession sess) {
	public String shopifyGenToken(HttpServletRequest req, HttpServletResponse res, HttpSession sess) {
		LOGGER.debug("controller11############shopifyGenToken##############");
		HttpHeaders httpHeaders = new HttpHeaders();
		String shopifyAuthUri = "/";
		String aa = spApiService.shopifyGenToken(req, res, sess);
		if(aa == null ) {
			ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
			String domainNm = util.getUserDomain(sess);
			String domain = sessionData.getDomain();
			LOGGER.debug("############shopifyGenToken##############"+domain);
			String rnd = util.randomNumber(20);
			HttpSession httpSession = req.getSession(true);
			LOGGER.debug("############shopifyHttpSession11##############"+httpSession);
			httpSession.setAttribute("shopifyState", rnd);
			LOGGER.debug("############shopifyHttpSession222rnd##############"+rnd);
			String accessToken = sessionData.getAccessToken();
			accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);
			httpHeaders.add("X-Shopify-Access-Token", accessToken);
			String redirectUriAfter = redirectUri.replace("shopifyGenToken","dashboard");
			//redirectUriAfter = "https://"+domain+"admin/apps/"+domainNm+"/terms/selectTerms";
			shopifyAuthUri = "https://"+domain+oauth+"?client_id="+AppId+"&scope="+scope+"&state="+rnd+"&redirect_uri="+redirectUriAfter;
			//return new RedirectView(shopifyAuthUri);
			
			URI redirectUri = null;
			try {
				redirectUri = new URI(shopifyAuthUri);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			httpHeaders.setLocation(redirectUri);
		}
		//spApiService.shopifyHmac(req, res, sess);
		
		//return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		return "redirect:"+shopifyAuthUri;
		
	}
	
	//AccessToken 조회
	@GetMapping("/shopifyGetAccessToken")
	public ModelAndView shopifyGetAccessToken(HttpServletRequest req, HttpServletResponse res, HttpSession sess) {
		
		ModelAndView mav = new ModelAndView("common/access_token");
		mav = spApiService.shopifyGetAccessToken(req, res, sess);
		return mav;
	}
	
	//주문목록조회 list
//	@GetMapping("/shopifyOrderList")
//	public ModelAndView shopifyOrderList(HttpServletRequest req, HttpSession sess) {
//		ModelAndView mav = new ModelAndView("order/orderList");
//		
//		return mav;
//	}

	//주문 조회하여 저장처리
	@RequestMapping(value="/api/shopifyGetOrder", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> shopifyGetOrder(Model model, HttpServletRequest req, HttpSession sess) {
		Map<String,Object> resultMap = spApiService.shopifyGetOrder(req, sess);
		return resultMap;
	}
	
	//특정 주문조회
	@GetMapping("/shopifyGetOrderId")
	public ModelAndView shopifyGetOrderId(@RequestBody ShipmentPopupData shipPopData,HttpServletRequest req, HttpSession sess) {
		ModelAndView mav = new ModelAndView("common/access_token");
		mav = spApiService.shopifyGetOrderId(shipPopData,req, sess);
		return mav;
	}
	
	/**
     * 이행 이벤트 처리 
     * @return
     */
    @PostMapping("/shopifyPostEventOrderId")
    public Map<String,Object> shopifyPostEventOrderId(ShipmentPopupData shipPopData, HttpServletRequest req, HttpSession sess) {
        ModelAndView mav = new ModelAndView("jsonview");
        Map<String,Object> resultMap = spApiService.shopifyPostEventOrderId(shipPopData,req, sess);
        return resultMap;
    }
    
    /**
     * 이행 등록
     * @return
     */
    @PostMapping("/shopifyPostOrderIdFullist")
    public Map<String,Object> shopifyPostOrderIdFullist(ShipmentPopupData shipPopData, HttpServletRequest req, HttpSession sess) {
        ModelAndView mav = new ModelAndView("jsonview");
        Map<String,Object> resultMap = spApiService.shopifyPostOrderIdFullist(shipPopData,req, sess);
        return resultMap;
    }
    
    //특정 주문 이행목록
    @GetMapping("/shopifyGetOrderIdFullist")
    public ShipmentPopupData shopifyGetOrderIdFullist(ShipmentPopupData shipPopData,HttpServletRequest req, HttpSession sess) {
        ModelAndView mav = new ModelAndView("common/access_token");
        ShipmentPopupData returnSpd = spApiService.shopifyGetOrderIdFullist(shipPopData,req, sess);
        return returnSpd;
    }
	
    //결제
  	@GetMapping("/shopifyProcBilling")
  	public ShipmentPopupData shopifyProcBilling(@RequestBody ShipmentPopupData shipPopData,HttpServletRequest req, HttpSession sess) {
  		ModelAndView mav = new ModelAndView("jsonView");
  		ShipmentPopupData returnVal = spApiService.shopifyProcBilling(shipPopData,req, sess);
  		return returnVal;
  	}
  	
  	public ShipmentPopupData shopifyProcBillingActivate( ShipmentPopupData shipPopData,HttpServletRequest req, HttpSession sess) {
  		ModelAndView mav = new ModelAndView("jsonView");
  		ShipmentPopupData returnVal = spApiService.shopifyProcBillingActivate(shipPopData,req, sess);
  		return returnVal;
  	}
  	
  	
  	//캐리어서비스 등록
  	@GetMapping(value="/shopifyCarrierCreate")
  	public ModelAndView shopifyCarrierCreate(ShopData sData) {
  		ModelAndView mav = new ModelAndView("index");
  		mav = spApiService.shopifyCarrierCreate(sData);
  		
  		return mav;
  		
  	}
  	
  	//캐리어서비스 수정
  	@GetMapping(value="/shopifyCarrierEdit")
  	public ModelAndView shopifyCarrierEdit(Model model, ShopifyOutApiDataCarrier carrierData, HttpServletRequest req, HttpSession sess) {
  		ModelAndView mav = new ModelAndView("index");
  		mav = spApiService.shopifyCarrierEdit(carrierData, req, sess);
  		return mav;
  		
  	}
  	
  	//캐리어서비스 조회
  	@GetMapping(value="/shopifyCarrierList")
  	public ModelAndView shopifyCarrierList(Model model, HttpServletRequest req, HttpSession sess) {
  		ModelAndView mav = new ModelAndView("index");
  		mav = spApiService.shopifyCarrierList(req, sess);
  		return mav;
  		
  	}
	
}