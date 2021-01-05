package com.shopify.api;

import java.io.UnsupportedEncodingException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.api.lotte.delivery.LotteUtil;
import com.shopify.common.RestService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.Hmac;
import com.shopify.common.util.UtilFn;
import com.shopify.common.util.UtilFunc;
import com.shopify.config.RestfulConfig;
import com.shopify.order.OrderData;
import com.shopify.order.OrderService;
import com.shopify.order.popup.OrderPopupRestService;
import com.shopify.seller.SellerData;
import com.shopify.seller.SellerService;
import com.shopify.shipment.ShipmentService;
import com.shopify.shipment.popup.ShipmentPopupData;
import com.shopify.shipment.popup.ShipmentPopupService;
import com.shopify.shop.ShopData;
import com.shopify.shop.ShopService;

@Service
@Transactional
public class ShopifyApiService {
	private Logger LOGGER = LoggerFactory.getLogger(ShopifyApiService.class);
	
	@Autowired	RestTemplate restTemplate;
	@Autowired	RestfulConfig restConfig;
	@Autowired	UtilFn util;
	@Autowired	Hmac hmac;
	@Autowired	OrderService orderService;
	@Autowired	ShopService shopService;
	@Autowired	SellerService sellerService;
	@Autowired	ShipmentService shipmentService;
	@Autowired	ShipmentPopupService shipmentPopupService;
	@Autowired	ShopifyApiService spApiService;
	@Autowired	ShopifyOutApiService spApiOutService;
	@Autowired private ExchangeApiService exchange;  // 조한두 
	@Autowired private OrderPopupRestService orderPopupRestService;
	@Autowired RestService restService;
	
	//properties/shopifyApi.properties 에 설정된 api 호출
	@Value("${shopify.AppId}")						       			String AppId;
	@Value("${shopify.AppPw}")						       			String AppPw;
	@Value("${shopify.ShopUrl}")					       			String ShopUrl;
	@Value("${shopify.App.callbackUrl}")					       	String AppCallbackUrl;
	@Value("${shopify.App.carrierName}")					    String AppCarrierName;
	@Value("${shopify.applicationCharge.returnUrl}")	    String AppReturnUrl;
	@Value("${shopify.App.name}")								String AppName;
	
		
	@Value("${Auth.Get.Shop}")						       			String authShop;
	@Value("${Auth.Get.OAuth}")					       			String oauth;
	@Value("${Order.Get.Orders}")					       			String getOrder;
	@Value("${Order.Get.OrderId}")				       			String getOrderIdUrl;
	@Value("${AccessToken.Get.Search}")		       			String getAccessToken;
	@Value("${Fulfillment.Get.Event.Create.Step1}")      	String getFulfillmentCreateStep1Url;
	@Value("${Fulfillment.Get.Event.Create.Step2}")      	String getFulfillmentCreateStep2Url;
	@Value("${Fulfillment.Get.Event.Create.Step3}")      	String getFulfillmentCreateStep3Url;
//	@Value("${Fulfillment.Post.Event.Create.Step4}")      	String getFulfillmentCreateStep4;
	
	@Value("${Fulfillment.Get.OrderId.Fullist}")      			String getFulfillmentListUrl;
	@Value("${Billing.Post.ApplicationCharge}")      			String procBilling;
//	@Value("${Billing.Activate.ApplicationCharge}")      	String procBillingActivateUrl;
	@Value("${Billing.Get.ApplicationCharge}")      			String getBillingUrl;

	@Value("${Carrier.Post.Create}")								String postCarrier;
//	@Value("${Carrier.Put.Edit}")									String EditCarrier;
	@Value("${Carrier.Get.List}")									String ListCarrier;
	
	@Value("${shopify.scope}")						       			String scope;
	@Value("${shopify.redirectUri}")				       			String redirectUri;
	@Value("${shopify.authUri}")					       			String authUri;
	
	public ModelAndView shopifyShopAuth(HttpSession sess, HttpServletResponse res) {
		
		LOGGER.debug("### shopifyShopAuth START");
		
		ModelAndView mav = new ModelAndView("index");
		SellerData ssd = new SellerData();
		String result = "";
		try {
			ObjectMapper objectMapper =new ObjectMapper();
			//파라미터설정
			MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
			//parameters.add("api", "thisistest");
			parameters = null;
			
			ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
			if(sd != null) {
				
				LOGGER.debug("### shopifyShopAuth [sd] :" + sd.toString());
				
				//헤더설정-> 기본인증처리
				HttpHeaders headers = new HttpHeaders();
				headers.add("X-Shopify-Access-Token", util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sd.getAccessToken()));
				HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
				
				String url = "https://" + sd.getShop() + authShop;

				LOGGER.debug("### shopifyShopAuth [url] :" + url.toString());
				
				//api 호출
				RestTemplate rest = new RestTemplate();
				//result = rest.postForObject(new URI(uri+authShop), request, String.class);						//post방식
				//Object rs = rest.exchange((url), HttpMethod.GET, request, String.class);		//get방식
				ResponseEntity<String> respones = rest.exchange((url), HttpMethod.GET, request, String.class);
				
				LOGGER.debug("### shopifyShopAuth [sd] :" + respones.toString());
				LOGGER.debug("### shopifyShopAuth [getStatusCode] :" + respones.getStatusCode());
				
				JSONObject responseJson = null;
				
				//LOGGER.debug("###############444444444441/responseJson=>"+responseJson);
				
				if (respones.getStatusCode() == HttpStatus.OK) {
					
					LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody] :" + respones.getBody().toString());
					
					//responseJson = new JSONObject(respones.getBody().toString().replaceAll("\\\\", ""));
					responseJson = new JSONObject(respones.getBody().toString());
					//JSONArray array = responseJson.getJSONArray("shop");
					JSONObject jo = (JSONObject) responseJson.get("shop");
					
					
					LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody : jo] :" + jo.toString());
					
					//responseJson = new JSONObject(respones.getBody());
//						LOGGER.debug("###############444444444441/"+array.get(i));
//						LOGGER.debug("###############444444444442/"+i);
//						LOGGER.debug("###############444444444443/"+array.length());
					
					if(jo != null) {
						sd.setId(jo.get("id").toString());
						sd.setShopId(jo.get("id").toString());
						sd.setEmail(jo.get("email").toString());
						sd.setLocale(jo.get("primary_locale").toString());
						sd.setDomain(jo.get("myshopify_domain").toString());
						sd.setEcommerce(SpConstants.ECOMMERCE_KEY); 
						String name = jo.get("shop_owner").toString();
						
						// 쇼핑몰 이름 설정
						if(jo.get("name").toString() != null) {
							sd.setShopName(jo.get("name").toString());
						} else {
							try {
								String domain = jo.get("myshopify_domain").toString().replaceAll(".", "/");
								String[] arrShopName = domain.split("/");
								sd.setShopName(arrShopName[0]);
							} catch (Exception e) {
								sd.setShopName(jo.get("myshopify_domain").toString());
							}
						}
						
						if(name != null) {
							String[] nameAry = name.split(" ");
							ssd.setFirstName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, nameAry[1]));
							ssd.setLastName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, nameAry[0]));
							ssd.setEmail(jo.get("email").toString());
							ssd.setEmailVerified("N");
							ssd.setPhoneNumber(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, jo.get("phone").toString()));
							ssd.setCompany(jo.get("name").toString());
							ssd.setCompanyEname(jo.get("name").toString());
							ssd.setCompanyEname(jo.get("myshopify_domain").toString());
							ssd.setUseYn("Y");
							ssd.setPasswd(util.getBCryptDecrypt(jo.get("id").toString()));	//상점아이디로 임시패스워드 저장
							//LOGGER.debug("###############4444444444411111111/ssd==>"+ssd);
							
							LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody : ssd] :" + ssd.toString());
						}
					}
					
					sd.setEncryptKey(SpConstants.ENCRYPT_KEY);
					sd.setDelYn("N");
					sd.setUseYn("N");
					sd.setCombineYn("Y");
					
					//LOGGER.debug("###############4444444444411111111/"+sd);
					int shopCnt = shopService.selectOneShopCount(sd);
					//LOGGER.debug("###############44444444444000000/shopCnt=>"+shopCnt);
					
					LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody : shopCnt] :" + shopCnt);
					
					if(shopCnt == 0 ) {
						int chk = 0;
						try {
							chk = shopService.insertShop(sd);
							LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody : insertShop ok] :" + chk);
						} catch(Exception e) {
							LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody : insertShop error] :" + e.getMessage());
						}
						
						ShopData sdata = shopService.selectOneShop(sd);
						sdata.setShopId(sd.getShopId());
						sdata.setId(sd.getShopId());
						sdata.setAccessToken(sd.getAccessToken());
						sdata.setShop(sd.getShop());
						sdata.setPrivatechk(sd.getPrivatechk());
						sdata.setAccessKey(AppId);
						sdata.setScope(scope);
						sdata.setAppName(AppName);
						LOGGER.debug("###############4444444444411111111/"+sd.getShopName());
						sess.setAttribute(SpConstants.HTTP_SESSION_KEY, sdata);
						
						LOGGER.debug("### shopifyShopAuth HttpStatus.HTTP_SESSION_KEY [updateShop] :" + sdata.toString());
						
						//LOGGER.debug("###############444444444441222222finally111==>/"+sdata);
					}else {
						ShopData sdata = shopService.selectOneShop(sd);
						LOGGER.debug("###############444444444441222222finally111==>/"+sdata.getActiveYn());
						sdata.setShopId(sd.getShopId());
						sdata.setId(sd.getShopId());
						sdata.setAccessToken(sd.getAccessToken());
						sdata.setShop(sd.getShop());
						sdata.setDelYn("N");
						sdata.setUseYn("Y"); 
						sdata.setPrivatechk("Y");
						sdata.setAccessKey(AppId);
						sdata.setScope(scope);
						sdata.setAppName(AppName);
						//LOGGER.debug("###############444444444441222222/"+sdata);
						LOGGER.debug("###############4444444444411111111222/"+sd.getShopName());
						sess.setAttribute(SpConstants.HTTP_SESSION_KEY, sdata);
						//LOGGER.debug("###############444444444441222222finally222==>/"+sdata);
						
						LOGGER.debug("### shopifyShopAuth HttpStatus.HTTP_SESSION_KEY [updateShop] :" + sdata.toString());
						
						int chk = shopService.updateShop(sdata);
					}
					if(sd != null) {
						String loginShopData = util.getAESEncrypt(SpConstants.FIND_PASSWD_KEY, sd+"");
						java.net.URLEncoder.encode(loginShopData, "UTF-8");
						javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(SpConstants.HTTP_SESSION_KEY, loginShopData);
						cookie.setMaxAge(60*60*24);
						cookie.setPath("/"); 
					    res.addCookie(cookie);
					}

					
					LOGGER.debug("###############4444444444411111111/ssd==>"+ssd);
					int sellerCnt = 0;
					try {
						sellerCnt = sellerService.selectSellerCount(ssd);
						LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody : selectSellerCount ok] :" + sellerCnt);
					} catch(Exception e) {
						LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody : selectSellerCount error] :" + e.getMessage());
					}
					//LOGGER.debug("###############44444444444000000/"+sellerCnt);
					if(sellerCnt == 0) {
						ShopData sdata = shopService.selectOneShop(sd);
						sdata.setShopId(sd.getShopId());
						sdata.setId(sd.getShopId());
						sdata.setAccessToken(sd.getAccessToken());
						sdata.setShop(sd.getShop());
						sdata.setDomain(sd.getDomain());
						
						sellerService.insertSeller(ssd);
						shopifyCarrierCreate(sdata);
					}else {
						SellerData seller = sellerService.selectSeller(ssd);
						//LOGGER.debug("###############4444444444411111111/ssd==>"+ssd);
						//LOGGER.debug("###############444444444441222222/seller==>"+seller);
						//sellerService.updateSeller(ssd);
						//LOGGER.debug("###############444444444441222222/seller==>"+seller);
					}
					
					LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody : sellerCnt] :" + sellerCnt);
					LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody : respones body] :" + respones.getBody().toString());
					
					mav.addObject("access_token", respones);
					mav.addObject("status", "ok");
					
					LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody : mav] ");
					LOGGER.debug("### shopifyShopAuth HttpStatus.OK [getBody : mav] :" + mav.toString());
					
					//LOGGER.debug("#########################");
					//LOGGER.debug(respones.toString());
				} else if (respones.getStatusCode() == HttpStatus.UNAUTHORIZED) {

					LOGGER.debug("### shopifyShopAuth HttpStatus.UNAUTHORIZED : " + responseJson.toString());
					
					mav.addObject("access_token", responseJson);
					mav.addObject("status", HttpStatus.UNAUTHORIZED);
				}

				
			}else {
				mav.addObject("status", "no");
				LOGGER.error("### 로그인필요 ###");
			}
			
		}catch(HttpStatusCodeException e){
		     String errorpayload = e.getResponseBodyAsString();
		     mav.addObject("status", "err");
		     LOGGER.debug("######shopifyShopAuth###message==>"+e.getMessage().toString());
		     LOGGER.debug("######shopifyShopAuth###errorpayload==>"+errorpayload);
		     //do whatever you want
		} catch (RestClientException e) {
			e.printStackTrace();
			mav.addObject("status", "err");
			LOGGER.error("### API 연동중 오류 ###"+e.getMessage());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			LOGGER.error("### JSONException ###");			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("### UnsupportedEncodingException ###"+e.getMessage());
		}

		LOGGER.debug("######   result End #######");
		
		return mav;
	}
	
	//상점설치시 호출됨(권한신청)
	public String shopifyInstall(HttpServletRequest req) {
		LOGGER.debug("############shopifyInstall##############"); 
		String uri = restConfig.makeURI();
		
		String rnd = util.randomNumber(20);
		HttpSession httpSession = req.getSession(true);
		httpSession.setAttribute("shopifyState", rnd);
		
		String shop = req.getParameter("shop");
		
		// TODO URL 변경 필요
		if(shop == null) { // APP에서 연동 
			shop = req.getParameter("shopName") + "." + authUri + oauth;
		} else { // 쇼피파이에서 설치 클릭 
			shop = shop + oauth;
		}
		
		String shopifyAuthUri = "https://"+shop+"?client_id="+AppId+"&scope="+scope+"&redirect_uri="+redirectUri+"&state="+rnd;
		return shopifyAuthUri;
	}

	//상점호출시 install이후 토근발행
	public String shopifyGenToken(HttpServletRequest req, HttpServletResponse res, HttpSession sess) {
		LOGGER.debug("############shopifyGenToken##############");
		
		ObjectMapper objectMapper =new ObjectMapper();
		ModelAndView mav = new ModelAndView("index");

		String param_code = req.getParameter("code");
		String param_hmac = req.getParameter("hmac");
		String param_shop = req.getParameter("shop");
		String param_state = req.getParameter("state");
		String param_timestamp = req.getParameter("timestamp");
		LOGGER.debug("############shopifyGenToken#111111111111111#############");
		try {
			if(param_code != null && param_hmac != null && param_shop != null && param_state != null && param_timestamp != null) {
	
				List<NameValuePair> params = new ArrayList<NameValuePair>();	//파라미터 add는 키이름순으로 해야함
				params.add(new BasicNameValuePair("code", param_code));
				params.add(new BasicNameValuePair("shop", param_shop));
				params.add(new BasicNameValuePair("state", param_state));
				params.add(new BasicNameValuePair("timestamp", param_timestamp));
				String param_string = util.httpBuildQuery(params, "UTF-8");
				//LOGGER.debug("############shopifyGenToken#222222222222222#############");
				
				String computed_param = hmac.hget(param_string);
				//LOGGER.debug("########param_hmac==>"+param_hmac);
				//LOGGER.debug("########computed_param==>"+computed_param);
				//String computed_hmac = hmac.hget(param_hmac);
				if(computed_param.equals(param_hmac)) {
					params = null;
					LOGGER.debug("############shopifyGenToken#33333333333333#############");
					
					//파라미터설정
					MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
					parameters.add("client_id", AppId);
					parameters.add("client_secret", AppPw);
					parameters.add("code", param_code);
					
					//헤더설정-> 기본인증처리
					HttpHeaders headers = new HttpHeaders();
					headers.add("X-Shopify-Access-Token", AppPw);
		
					HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
					String access_token_url = "https://" + param_shop + "/admin/oauth/access_token";
					//LOGGER.debug("############shopifyGenToken#444444444444444#############param_shop=>"+param_shop);
					//api 호출 
					RestTemplate rest = new RestTemplate();
					String respones = rest.postForObject((access_token_url), request, String.class);		//post방식
					//LOGGER.debug("############shopifyGenToken#444444444444444#############respones=>"+respones);
					ShopData sd = objectMapper.readValue(respones, ShopData.class);   // String to Object로 변환
					//LOGGER.debug("############shopifyGenToken#444444444444444#############sd=>"+sd);
					if(sd != null) {
						if(sd.getShop() == null ) sd.setShop(param_shop);
						if(sd.getAccess_token() != null)
							sd.setAccessToken(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sd.getAccess_token()));
//						if(sd.getAccessToken() != null)
//							sd.setAccessToken(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sd.getAccess_token()));
						 
						LOGGER.debug("############origin=>"+sd.getAccess_token()+"##############");
						LOGGER.debug("############encode=>"+util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sd.getAccess_token())+"##############");
						LOGGER.debug("############decode=>"+util.getAESDecrypt(SpConstants.ENCRYPT_KEY, util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sd.getAccess_token()))+"##############");
						LOGGER.debug(sd.toString());
						sess.setAttribute(SpConstants.HTTP_SESSION_KEY, sd);
						
						////return "redirect:/shopifyShopAuth";
					}
					
					shopifyShopAuth(sess, res);	//인스톨이후 로그인처리
					mav.addObject("access_token", respones);
					mav.addObject("status", "ok");
					
				}else {
					LOGGER.debug("############shopifyGenToken#5555555555555#############");
					mav.addObject("status", "no");	//보내준 암호화코드로 파라미터의 유효성체크
				}
			}else {
				LOGGER.debug("############shopifyGenToken#666666666666#############");
				mav.addObject("status", "err");		//파라미터의 값이 null인경우 체크
			}
		}catch(HttpStatusCodeException e){
		     String errorpayload = e.getResponseBodyAsString();
		     mav.addObject("status", "err2");
				mav.addObject("message", e.getMessage().toString());
		     LOGGER.debug("######shopifyGenToken###message==>"+e.getMessage().toString());
		     LOGGER.debug("######shopifyGenToken###errorpayload==>"+errorpayload);
		     //do whatever you want
		}catch(Exception e) {		
			LOGGER.debug("############shopifyGenToken#7777777777#############");//그외 오류체크
			mav.addObject("status", "err2");
			mav.addObject("message", e.getMessage().toString());
		}
		//LOGGER.debug("############shopifyGenToken#888888888888888#############");
		//return mav;
		
		LOGGER.debug("shopifyGenToken : RedirectView ");
		return "/";
	}
	
	//AccessToken 조회
	public ModelAndView shopifyGetAccessToken(HttpServletRequest req, HttpServletResponse res, HttpSession sess) {
		ModelAndView mav = new ModelAndView("common/access_token");
		try {
			
					MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
					//parameters.add("client_id", AppId);
					
					//헤더설정-> 기본인증처리
					ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
					String accessToken = sessionData.getAccessToken();
					accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);
					HttpHeaders headers = new HttpHeaders();
					headers.add("X-Shopify-Access-Token", accessToken);
					HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
					String uri = restConfig.makeURI();
					
					//api 호출 
					RestTemplate rest = new RestTemplate();
					//Object respones = rest.exchange((uri+getAccessToken), HttpMethod.GET, request, String.class);		//get방식
					ResponseEntity<String> respones = rest.exchange((uri+getAccessToken), HttpMethod.GET, request, String.class);
					JSONObject responseJson = null;
					//String respones = rest.postForObject((uri+getOrder), request, String.class);		//post방식
					//LOGGER.debug("######==>"+respones);
					if (respones.getStatusCode() == HttpStatus.OK) {
						//responseJson = new JSONObject(respones.getBody().toString().replaceAll("\\\\", ""));
						responseJson = new JSONObject(respones.getBody().toString());
						String access_token = responseJson.get("access_token").toString();
						access_token = util.getAESEncrypt(SpConstants.ENCRYPT_KEY, access_token);
//						LOGGER.debug("############2shopifyGenToken##############"); 
//						LOGGER.debug("############2origin=>"+access_token+"##############");
//						LOGGER.debug("############2encode=>"+util.getAESEncrypt(SpConstants.ENCRYPT_KEY, access_token)+"##############");
//						LOGGER.debug("############2decode=>"+util.getAESDecrypt(SpConstants.ENCRYPT_KEY, util.getAESEncrypt(SpConstants.ENCRYPT_KEY, access_token))+"##############");
//						LOGGER.debug(respones.toString());
						
						//ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
						sessionData.setAccessToken(access_token);
						sess.setAttribute(SpConstants.HTTP_SESSION_KEY, sessionData);
						
						mav.addObject("access_token", respones);
						mav.addObject("status", "ok");
						
						//LOGGER.debug("############==>sess");
						shopifyShopAuth(sess, res);	//현재 인증받은 셀러의 정보 호출
					} else if (respones.getStatusCode() == HttpStatus.UNAUTHORIZED) {

						mav.addObject("access_token", responseJson);
						mav.addObject("status", HttpStatus.UNAUTHORIZED);
					}
					
					

		}catch(HttpStatusCodeException e){
		     String errorpayload = e.getResponseBodyAsString();
		     mav.addObject("status", "err2");
				mav.addObject("message", e.getMessage().toString());
		     LOGGER.debug("######shopifyGetAccessToken###message==>"+e.getMessage().toString());
		     LOGGER.debug("######shopifyGetAccessToken###errorpayload==>"+errorpayload);
		     //do whatever you want
		}catch(Exception e) {
			mav.addObject("status", "err2");
			mav.addObject("message", e.getMessage().toString());
		}
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
	public Map<String,Object> shopifyGetOrder(HttpServletRequest req, HttpSession sess) {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		ObjectMapper objectMapper =new ObjectMapper();
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);		//json 대소문자 구분 안하게 설정
		objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);							//따옴표를 포함한 문자
		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		try {

			
			// 헤더설정-> 기본인증처리
			List<ShopData> shopList = shopService.selectAllShop(sessionData);

			// String accessToken = sessionData.getAccessToken();
			String ShopName = "";
			int ShopIdx = 0;

			LOGGER.debug("############### shopifyGetOrder > sd.size : " + shopList.size());
			
			if(shopList == null || shopList.isEmpty()) return null;
					
			MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
			// parameters.add("client_id", AppId);

			for(int intSd=0; intSd<shopList.size(); intSd++) {
				ShopData shopData = shopList.get(intSd);

				String accessToken = shopData.getAccessToken();
				String maxOrderCode = shopData.getOrderIdx();
				String domain = shopData.getDomain();
				String callAPIUrl = "https://" + domain+getOrder;
				
				if(maxOrderCode != null && !"".equals(maxOrderCode) ) {
					callAPIUrl+="&since_id="+maxOrderCode;
					LOGGER.debug("############### yryr TEST######## shopifyGetOrder[" + callAPIUrl + "] ");
				}
				
				ResponseEntity<String> respones =  getShopAPIOrder(parameters, callAPIUrl, accessToken);
				if (respones.getStatusCode() == HttpStatus.OK) {
					resultMap.put("status",HttpStatus.OK);
				} else if (respones.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					resultMap.put("status",HttpStatus.UNAUTHORIZED);
					continue;
				} else {
					continue;
				}

//				LOGGER.debug("############### shopifyGetOrder[" + intSd + "] : " + respones.getBody().toString()); 
				JSONObject responseJson = new JSONObject(respones.getBody().toString());
				if(respones == null)  {
					LOGGER.debug("############### shopifyGetOrder[respones] : null "); 
					continue;
				}
				
				JSONArray array = responseJson.getJSONArray("orders");
				if(array == null || array.length() ==0) {
					LOGGER.debug("############### shopifyGetOrder[array orders] : "+array.toString()); 
					continue;
				}
				
				ShopIdx = shopData.getShopIdx();
				ShopName = shopData.getShopName();
//				domain = shopData.getDomain();
				
				for(int i=0; i<array.length(); i++) {
					JSONObject jo =  (JSONObject) array.get(i);
					if(jo ==null) {
						continue ;
					}
					String paymentStatus=jo.getString("financial_status");
					
					if(!"paid".equals(paymentStatus) )
						continue;

					OrderData od  =  new OrderData();
					
					boolean rs = setJson2OrderData(jo, od, sessionData.getEmail());
					od.setShopIdx(ShopIdx);
					
					//주문 정보 저장
					int rsInt = saveShopifyOrder(od);
					int orderIdx = od.getOrderIdx();
					if(rsInt != 1) {
						LOGGER.error(">>>>> 주문상세저장[saveShopifyOrder]  : " + orderIdx);
						continue;
					}
					
					
					if(!jo.isNull("line_items") && jo.getJSONArray("line_items").length()  > 0) {
						JSONArray arrayItem = jo.getJSONArray("line_items");
						for(int k=0; k<arrayItem.length(); k++) {
							
							JSONObject joItem =  (JSONObject) arrayItem.get(k);
							if(joItem.getString("product_id").equals("null") || joItem.getString("title").equals("Tip")) 	// Tip 제외
								continue;
							
							int mergeCount = saveOrderDetail(jo, od, orderIdx, joItem);
						
						}
						
						LOGGER.debug(">>>>> 주문상세저장[updateOrder] subidx : " + orderIdx);
					} else {
						LOGGER.debug(">>>>> 주문상세저장[updateOrder else] arrayItem is null : ");
					}
				}

			}
					
			//LOGGER.debug(">>>>> 주문저장 End");
					
					
		}catch(HttpStatusCodeException e){
 		     String errorpayload = e.getResponseBodyAsString();
 		    resultMap.put("status",HttpStatus.BAD_REQUEST);
			resultMap.put("message", e.getMessage().toString());
		     LOGGER.debug("######shopifyGetOrder###message==>"+e.getMessage().toString());
		     LOGGER.debug("######shopifyGetOrder###errorpayload==>"+errorpayload);
		     //do whatever you want
		}catch(Exception e) {
			LOGGER.debug(">>>>> 주문저장 Exception : " + e.toString());
			resultMap.put("status",HttpStatus.BAD_REQUEST);
			resultMap.put("message", e.getMessage().toString());
		}
		return resultMap;
	}

public Map<String,Object> shopifyAllGetOrder(HttpServletRequest req, HttpSession sess) {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		ObjectMapper objectMapper =new ObjectMapper();
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);		//json 대소문자 구분 안하게 설정
		objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);							//따옴표를 포함한 문자
		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		try {

			
			// 헤더설정-> 기본인증처리
			List<ShopData> shopList = shopService.selectAllShop(sessionData);

			// String accessToken = sessionData.getAccessToken();
			String ShopName = "";
			int ShopIdx = 0;

			LOGGER.debug("############### shopifyGetOrder > sd.size : " + shopList.size());
			
			if(shopList == null || shopList.isEmpty()) return null;
					
			MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
			// parameters.add("client_id", AppId);

			for(int intSd=0; intSd<shopList.size(); intSd++) {
				ShopData shopData = shopList.get(intSd);

				String accessToken = shopData.getAccessToken();
				String maxOrderCode = shopData.getOrderIdx();
				String domain = shopData.getDomain();
				String callAPIUrl = "https://" + domain+getOrder;
				
//				if(maxOrderCode != null && !"".equals(maxOrderCode) ) {
//					callAPIUrl+="&since_id="+maxOrderCode;
//					LOGGER.debug("############### yryr TEST######## shopifyGetOrder[" + callAPIUrl + "] ");
//				}
				
				ResponseEntity<String> respones =  getShopAPIOrder(parameters, callAPIUrl, accessToken);
				if (respones.getStatusCode() == HttpStatus.OK) {
					resultMap.put("status",HttpStatus.OK);
				} else if (respones.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					resultMap.put("status",HttpStatus.UNAUTHORIZED);
					continue;
				} else {
					continue;
		
				}

//				LOGGER.debug("############### shopifyGetOrder[" + intSd + "] : " + respones.getBody().toString()); 
				JSONObject responseJson = new JSONObject(respones.getBody().toString());
				if(respones == null)  {
					LOGGER.debug("############### shopifyGetOrder[respones] : null "); 
					continue;
				}
				
				JSONArray array = responseJson.getJSONArray("orders");
				if(array == null || array.length() ==0) {
					LOGGER.debug("############### shopifyGetOrder[array orders] : "+array.toString()); 
					continue;
				}
				
				ShopIdx = shopData.getShopIdx();
				ShopName = shopData.getShopName();
//				domain = shopData.getDomain();
				
				for(int i=0; i<array.length(); i++) {
					JSONObject jo =  (JSONObject) array.get(i);
					if(jo ==null) {
						continue ;
					}
					String paymentStatus=jo.getString("financial_status");
					
					if(!"paid".equals(paymentStatus) )
						continue;

					OrderData od  =  new OrderData();
					
					boolean rs = setJson2OrderData(jo, od, sessionData.getEmail());
					od.setShopIdx(ShopIdx);
					
					//주문 정보 저장
					int rsInt = saveShopifyOrder(od);
					int orderIdx = od.getOrderIdx();
					if(rsInt != 1) {
						LOGGER.error(">>>>> 주문상세저장[saveShopifyOrder]  : " + orderIdx);
						continue;
					}
					
					
					if(!jo.isNull("line_items") && jo.getJSONArray("line_items").length()  > 0) {
						JSONArray arrayItem = jo.getJSONArray("line_items");
						for(int k=0; k<arrayItem.length(); k++) {
							
							JSONObject joItem =  (JSONObject) arrayItem.get(k);
							if(joItem.getString("product_id").equals("null") || joItem.getString("title").equals("Tip"))	
								continue;
							int mergeCount = saveOrderDetail(jo, od, orderIdx, joItem);
						
						}
						
						LOGGER.debug(">>>>> 주문상세저장[updateOrder] subidx : " + orderIdx);
					} else {
						LOGGER.debug(">>>>> 주문상세저장[updateOrder else] arrayItem is null : ");
					}
				}

			}
					
			//LOGGER.debug(">>>>> 주문저장 End");
					
					
		}catch(HttpStatusCodeException e){
 		     String errorpayload = e.getResponseBodyAsString();
 		    resultMap.put("status",HttpStatus.BAD_REQUEST);
			resultMap.put("message", e.getMessage().toString());
		     LOGGER.debug("######shopifyGetOrder###message==>"+e.getMessage().toString());
		     LOGGER.debug("######shopifyGetOrder###errorpayload==>"+errorpayload);
		     //do whatever you want
		}catch(Exception e) {
			LOGGER.debug(">>>>> 주문저장 Exception : " + e.toString());
			resultMap.put("status",HttpStatus.BAD_REQUEST);
			resultMap.put("message", e.getMessage().toString());
		}
		return resultMap;
	}
	
	
	
	private boolean setJson2OrderData(JSONObject jo, OrderData od, String sessionEmail) throws JSONException {
//		String status = "";
//		String error_code = "";
		
		od.setOrderCode(jo.getString("id"));
		od.setOrderName(jo.getString("name"));
		//od.setUpdated_at(util.dateFormat(jo.get("updated_at").toString(),"Y"));						//날짜 포멧 변경...Y인경우는 뒤 -5시간 처리
		od.setOrderDate(util.dateFormat(jo.getString("created_at"),"Y"));									//주문생성일자
		
		od.setLocationId(util.nullToEmpty(jo.getString("location_id"))); 
		od.setTotalPriceCurrency(jo.getString("currency"));
		String totalprice = jo.getString("total_price");																//주문총금액
		if(totalprice != null) {
			od.setPrice(Double.parseDouble(totalprice));
		}
		
		od.setOrderStatusUrl(jo.getString("order_status_url"));  		//상품 url
		
		// 2020-03-26 추가 컬럼
		od.setFinancialStatus(util.nullToEmpty(jo.getString("financial_status")));  // 결제 정보
		od.setOrderCourier(util.nullToEmpty(jo.getString("processing_method")));  // 배송서비스 정보
		od.setFulfillmentStatus(util.nullToEmpty(jo.getString("fulfillment_status")));  // 배송서비스 정보
		
		String email = "";
		
		if(!jo.isNull("customer")) {
			JSONObject joCustomer = jo.getJSONObject("customer");
			 od.setCustomerId(joCustomer.getString("id"));                               //고객ID
             String customerName = joCustomer.getString("first_name");                      //고객명           
             if(customerName == null || "null".equals(customerName)) {
             	customerName = "";
             }
             customerName = customerName + " " + joCustomer.getString("last_name");      //고객명
             if("null".equals(customerName) || " null".equals(customerName))
            	 customerName = "GUEST";
             od.setCustomerName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, customerName));
             //LOGGER.debug("##########@@@@@customer777777777777711111111111555od.getCustomerName()==>"+od.getCustomerName());
             email = joCustomer.getString("email");
             od.setEmail(email);
             od.setCombineYn("N");
		}
		
	
		if(!jo.isNull("shipping_lines")) {
			JSONArray joShippingLines = jo.getJSONArray("shipping_lines");
			//LOGGER.debug("### joShippingLines : " + joShippingAddress);
			for(int k=0; k<joShippingLines.length(); k++) {
				//OrderData subData = new OrderData();
				
				JSONObject joItem = new JSONObject();
				joItem =  (JSONObject) joShippingLines.get(k);
				
				od.setShippingLineName(joItem.getString("title"));
				od.setShippingLineCode(joItem.getString("code"));
			}
		}
		
		if(email == null || "".equals(email)) {
		    email =sessionEmail;
		}
		
//		if(!jo.isNull("line_items")) {
//			JSONArray joLineItems = jo.getJSONArray("line_items");
//			for(int k=0; k<joLineItems.length();k++) {
//				JSONObject joLI = new JSONObject();
//				joLI = (JSONObject)joLineItems.get(k);
//				
//				od.setVendor(joLI.getString("vendor"));
//			}
//			
//		}
		
		if(!jo.isNull("shipping_address")) {
			JSONObject joShippingAddress = jo.getJSONObject("shipping_address");
	        //LOGGER.debug("### joShippingAddress : " + joShippingAddress);
            String first_name = joShippingAddress.getString("first_name");
            if(first_name == null) first_name = " ";
            String last_name = joShippingAddress.getString("last_name");
            if(last_name == null) last_name = " ";
            String phone = joShippingAddress.getString("phone");
            if(phone == null) phone = " ";
            
            od.setBuyerFirstname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, first_name));
            od.setBuyerLastname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, last_name));
            od.setBuyerAddr1(joShippingAddress.getString("address1"));
            od.setBuyerAddr2(joShippingAddress.getString("address2"));
            od.setBuyerCity(joShippingAddress.getString("city"));
            od.setBuyerCountry(joShippingAddress.getString("country"));
            od.setBuyerCountryCode(joShippingAddress.getString("country_code"));
            od.setBuyerPhone(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, phone));
             
            od.setBuyerProvince(UtilFunc.getJsonString(joShippingAddress.getString("province")));
            LotteUtil.convertProvince(od);
            
//              od.setBuyerProvince(joShippingAddress.getString("province"));  
            od.setBuyerProvinceCode(joShippingAddress.getString("province_code"));
            od.setBuyerEmail(email);
//              od.setBuyerZipCode(joShippingAddress.getString("zip"));  //postal code
            od.setBuyerZipCode(UtilFunc.getJsonString(joShippingAddress.getString("zip")));
            od.setEmail(email);
		}
						
						
		if(od.getCombineYn() == null) {
			od.setCombineYn("N");
		}
		
		return true;
	}

	private int  saveOrderDetail(JSONObject jo, OrderData od, int orderIdx, JSONObject joItem)
			throws JSONException {
		int mergeCount;
		OrderData subData = new OrderData();
		
		LOGGER.debug(">>>>> 주문상세저장 joItem : " + joItem.toString());
		
		// 특수 문자 치환
		String sku = util.strReplace(joItem.getString("sku"));
		//String goods = util.strReplace(joItem.getString("name"));
		String goods = util.strReplace(joItem.getString("title"));
		String variants = util.strReplace(joItem.getString("variant_title"));
		
		subData.setOrderIdx(orderIdx);
		subData.setGoodsCode(util.nullToEmpty(joItem.getString("product_id")));
		subData.setGoods(goods);
		subData.setVariants(variants);
		subData.setPrice(joItem.getDouble("price")*joItem.getInt("quantity"));
		subData.setTaxable(joItem.getString("taxable"));
		//subData.setBarcode(joItem.getString("product_id"));
		//subData.setOrigin(joItem.getString("product_id"));
		//subData.setHscode(joItem.getString("product_id"));
		subData.setWeight(joItem.getInt("grams"));
		subData.setWeightUnit("g");
		subData.setQuantity(joItem.getInt("quantity"));
		subData.setGoodsItemId(joItem.getString("id"));
		subData.setLocationId(util.nullToEmpty(jo.getString("location_id")));
		subData.setUnitCost((joItem.getDouble("price")));
		subData.setCustomerName(od.getCustomerName());
		subData.setCustomerId(od.getCustomerId());
		subData.setSku(sku);
		subData.setOrderStatusUrl(od.getOrderStatusUrl());
		subData.setVendor(joItem.getString("vendor"));
		
		//상세정보 화폐단위 
		JSONObject itemPrice = new JSONObject();
		itemPrice = joItem.getJSONObject("price_set").getJSONObject("shop_money");
		
		subData.setPriceCurrency(itemPrice.getString("currency_code"));

		String productUrl = orderPopupRestService.getProductHandleFullUrl(Integer.toString(od.getShopIdx()),subData.getGoodsCode());		
		subData.setItemLink(productUrl);
		//LOGGER.debug("####################################################");
		//LOGGER.debug("################ price : " + joItem.getDouble("price"));
		//LOGGER.debug("################ price : " + joItem.getInt("quantity"));
		
		//주문상세저장
		mergeCount = orderService.mergeOrderDetail(subData);
		LOGGER.debug(">>>>> 주문상세저장[mergeOrderDetail ] mergeCount={},  order_idx={} : ", mergeCount, subData.getOrderIdx());
		return mergeCount;
	}
	
	private int saveShopifyOrder(OrderData od) {
		// TODO Auto-generated method stub
		
		int rsInt = 0;
		
		List<OrderData> oData = orderService.selectOneOrderList(od);
		int orderIdx = 0;
		
		if( oData.size() == 0) {
			LOGGER.debug(">>>>> 주문저장[insertOrder] : " + od.toString());
			od.setHideYn("N");
			od.setDelYn("N");
			rsInt = orderService.insertOrderOne(od);
			orderIdx = od.getOrderIdx();
			LOGGER.debug("orderIdx from orderService.insertOrderOne() : " + orderIdx);
				
		}else if (oData.size() == 1 ) {
			LOGGER.debug(">>>>> 주문저장[updateOrder] : " + od.toString());
			od.setDelYn("N");
			od.setHideYn("N");
			orderIdx = oData.get(0).getOrderIdx();
			rsInt = orderService.updateOrderOne(od);
		} else {
			LOGGER.error(">>>>> 주문저장[updateOrder] ERR : tb_order_list={}, shop_idx={}, order_code={}" , oData.size(), od.getShopIdx(), od.getOrderCode());
//			String text = String.format("주문저장[updateOrder] ERR : tb_order_list=%d, shop_dx=%s, order_code=%s" , oData.size(), od.getShopIdx(), od.getOrderCode());
			//throw new RuntimeException(text);
		}
						
		od.setOrderIdx(orderIdx);
		
		
		return rsInt;
	}

	private ResponseEntity<String> getShopAPIOrder(MultiValueMap<String, String> parameters, String callAPIURL, String accessToken) {
		// TODO Auto-generated method stub
		
		accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);
		
		LOGGER.debug("############### getShopAPIOrder[] > accessToken : " + accessToken);

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Shopify-Access-Token", accessToken);		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
		//api 호출 
		RestTemplate rest = new RestTemplate();

		//String respones = rest.postForObject((uri+getOrder), request, String.class);		//post방식
		return rest.exchange(callAPIURL, HttpMethod.GET, request, String.class);
	}

	//특정 주문조회
	public ModelAndView shopifyGetOrderId(ShipmentPopupData shipPopData,HttpServletRequest req, HttpSession sess) {
		ModelAndView mav = new ModelAndView("common/access_token");

		String param_orderId = req.getParameter("id");
		if(param_orderId != null){
			try {
					String orderId = shipPopData.getOrderCode();
					MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
					//parameters.add("client_id", AppId);
					
					//헤더설정-> 기본인증처리
					ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
					String accessToken = sessionData.getAccessToken();
					accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);

					HttpHeaders headers = new HttpHeaders();
					headers.add("X-Shopify-Access-Token", accessToken);
					HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
					String uri = restConfig.makeURI();
					String getOrderId = uri + getOrderIdUrl + orderId + ".json";
					//LOGGER.debug("######getOrderId==>"+getOrderId);
					//api 호출 
					RestTemplate rest = new RestTemplate();
					ResponseEntity<String> respones = rest.exchange(getOrderId, HttpMethod.GET, request, String.class);
					
					if (respones.getStatusCode() == HttpStatus.OK) {
						//JSONObject responseJson = new JSONObject(respones.getBody().toString().replaceAll("\\\\", ""));
                    	JSONObject responseJson = new JSONObject(respones.getBody().toString());
						LOGGER.debug("######==>"+respones);
					}
					//String respones = rest.postForObject((uri+getOrder), request, String.class);		//post방식
					//LOGGER.debug("######==>"+respones);
					
					mav.addObject("access_token", respones);
					mav.addObject("status", "ok");
	
			}catch(HttpStatusCodeException e){
      		     String errorpayload = e.getResponseBodyAsString();
      		   mav.addObject("status", "err2");
				mav.addObject("message", e.getMessage().toString());
    		     LOGGER.debug("######shopifyGetOrderId###message==>"+e.getMessage().toString());
    		     LOGGER.debug("######shopifyGetOrderId###errorpayload==>"+errorpayload);
    		     //do whatever you want
    		}catch(Exception e) {
				mav.addObject("status", "err2");
				mav.addObject("message", e.getMessage().toString());
			}
		}else{
			mav.addObject("status", "no");
			mav.addObject("message", "No Data");
		}
		return mav;
	}
	
	/**
     * 이행 이벤트 처리 
     * @return
     */
    @PostMapping("/shopifyPostEventOrderId")
    public Map<String,Object> shopifyPostEventOrderId(ShipmentPopupData shipPopData, HttpServletRequest req, HttpSession sess) {
        ModelAndView mav = new ModelAndView("jsonview");
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(shipPopData != null){
            String orderId = shipPopData.getOrderCode();
            String getFulId = shipPopData.getFulId();
            if(orderId != null && getFulId != null) {
                try {
                    
                    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
                    //parameters.add("order_id", orderId);
                    
                    //헤더설정-> 기본인증처리
                    ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
                    String accessToken = sessionData.getAccessToken();
                    accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("X-Shopify-Access-Token", accessToken);
                    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
                    String uri = restConfig.makeURI();
                    String getFulfillmentCreateStep4Uri = uri+getFulfillmentCreateStep1Url+orderId+"/fulfillments.json";
                    String getFulfillmentCreateStep5Uri = uri+getFulfillmentCreateStep1Url+orderId+"/fulfillments/"+getFulId+".json";
                    //getFulfillmentCreateStep4 = getFulfillmentCreateStep4.replace("{order_id}",orderId+"");
                    //getFulfillmentCreateStep4 = getFulfillmentCreateStep4.replace("{fulfillment_id}",getFulId+"");
                    //LOGGER.debug("######getOrderId==>"+getOrderId);
                    //api 호출 
                    RestTemplate rest = new RestTemplate();
                    ResponseEntity<String> respones = rest.exchange((uri+getFulfillmentCreateStep4Uri), HttpMethod.POST, request, String.class);
                    
                    if (respones.getStatusCode() == HttpStatus.OK) {
                    	//JSONObject responseJson = new JSONObject(respones.getBody().toString().replaceAll("\\\\", ""));
                    	JSONObject responseJson = new JSONObject(respones.getBody().toString());
                        LOGGER.debug("######==>"+respones);
                    }
                    LOGGER.debug("######==>"+respones);

                    
                    resultMap.put("respones", respones);
                    resultMap.put("status", "ok");
        
                }catch(HttpStatusCodeException e){
	       		     String errorpayload = e.getResponseBodyAsString();
		       		  resultMap.put("status", "err2");
	                  resultMap.put("message", e.getMessage().toString());
	     		     LOGGER.debug("######getFulfillmentCreateStep4###message==>"+e.getMessage().toString());
	     		     LOGGER.debug("######getFulfillmentCreateStep4###errorpayload==>"+errorpayload);
	     		     //do whatever you want
	     		}catch(RestClientException e){
	     		     //no response payload, tell the user sth else
	     			LOGGER.debug("######getFulfillmentCreateStep4###RestClientException==>"+e.getMessage());
	     		}catch(Exception e) {
                    resultMap.put("status", "err2");
                    resultMap.put("message", e.getMessage().toString());
                }
            }else{
                resultMap.put("status", "no2");
                resultMap.put("message", "No Data2");
            }
            
        }else{
            resultMap.put("status", "no");
            resultMap.put("message", "No Data");
        }
        return resultMap;
    }
    
    /**
     * 이행 등록
     * @return
     */
    @PostMapping("/shopifyPostOrderIdFullist")
	public Map<String, Object> shopifyPostOrderIdFullist(ShipmentPopupData shipPopData, HttpServletRequest req,
			HttpSession sess) {
		ModelAndView mav = new ModelAndView("jsonview");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		LOGGER.debug("shopifyPostOrderIdFullist######shipPopData==>" + shipPopData);
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		if (shipPopData == null) {
			resultMap.put("status", "no");
			resultMap.put("message", "No Data");
			return resultMap;
		}

		String ShopName = shipPopData.getShopName();
		String orderId = shipPopData.getOrderCode();
		String goodsCode = shipPopData.getGoodsCode();
		String locationId = "";// shipPopData.getLocationId();
		String getFulId = shipPopData.getFulId();

		if (orderId == null) {
			resultMap.put("status", "no2");
			resultMap.put("message", "No Data2");
			return resultMap;
		}

		try {
				JSONObject parameters = new JSONObject();
				String accessToken = sd.getAccessToken();
				accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);
				ShopName = util.getUserVoDomain(sd);


				String uri = "https://" + ShopName + "." + authUri;
				String getFulfillmentCreateStep1 = uri + getFulfillmentCreateStep1Url + orderId + ".json";

				HttpHeaders headers = new HttpHeaders();
				headers.add("X-Shopify-Access-Token", accessToken);
				headers.add("Authorization", accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				JsonNode node = restService.getRestTemplate(headers, getFulfillmentCreateStep1);
				node.toString();
				JSONObject nodObj = new JSONObject(node.toString());
				if (nodObj.isNull("order")) {
					resultMap.put("status", "fail");
					return resultMap;
				}
				JSONObject jo = nodObj.getJSONObject("order");

				if (jo.isNull("line_items")) {
					resultMap.put("status", "fail");
					return resultMap;
				}
				JSONArray arrayItem = jo.getJSONArray("line_items");
				OrderData subData = new OrderData();
				String variant_id = "";
				String itemId = "";
				for (int k = 0; k < arrayItem.length(); k++) {
					JSONObject joItem = (JSONObject) arrayItem.get(k);
					variant_id = joItem.getString("variant_id");
					itemId = joItem.getString("id");
					if (variant_id == null)
						continue;
				}

				String getFulfillmentCreateStep2 = uri + getFulfillmentCreateStep2Url + variant_id + ".json";
				node = restService.getRestTemplate(headers, getFulfillmentCreateStep2);
				nodObj = new JSONObject(node.toString());

				if (nodObj.isNull("variant")) {
					resultMap.put("status", "fail");
					return resultMap;
				}
				JSONObject variant_jo = nodObj.getJSONObject("variant");
				if (variant_jo.isNull("inventory_item_id")) {
					resultMap.put("status", "fail");
					return resultMap;
				}
				String inventory_item_id = variant_jo.getString("inventory_item_id");
				if (inventory_item_id == null) {
					resultMap.put("status", "fail");
					return resultMap;
				}

				String getFulfillmentCreateStep3 = uri + getFulfillmentCreateStep3Url + inventory_item_id;
				node = restService.getRestTemplate(headers, getFulfillmentCreateStep3);
				nodObj = new JSONObject(node.toString());
				if (nodObj.isNull("inventory_levels")) {
					resultMap.put("status", "fail");
					return resultMap;					
				}

				JSONArray array3 = nodObj.getJSONArray("inventory_levels");
				for (int n = 0; n < array3.length(); n++) {
					JSONObject jo3 = new JSONObject();
					jo3 = (JSONObject) array3.get(n);
					if (jo3 == null)
						continue;

					locationId = jo3.getString("location_id");
					String tNumber = util.getRandomString(10);
					JSONObject parameters4 = new JSONObject();
					JSONObject fulfillment = new JSONObject();
					JSONObject fulfillmentInfo = new JSONObject();
					JSONArray lineItemsArray = new JSONArray();
					JSONArray trackingArray = new JSONArray();
					JSONObject trackingInfo = new JSONObject();
					JSONObject lineItemsInfo = new JSONObject();
					JSONArray tNumberArray = new JSONArray();

					/*
					 * if(goodsCode.indexOf(",") > 0) { String[] goodsCodeList =
					 * goodsCode.split(","); for(String gcode : goodsCodeList) {
					 * lineItemsInfo.put("id", gcode); } }else { lineItemsInfo.put("id", goodsCode);
					 * }
					 */

					lineItemsInfo.put("id", itemId);
					lineItemsArray.put(lineItemsInfo);
					tNumberArray.put(tNumber);

					fulfillment.put("location_id", locationId);
					// trackingArray.put("https://www.new-fedex-tracking.com/?number=123456789010");
					// fulfillment.put("tracking_urls", trackingArray);
					// fulfillment.put("tracking_number", tNumber);
					// fulfillment.put("tracking_urls",
					// "https://www.new-fedex-tracking.com/?number=123456789010");
					if (shipPopData.getTrackingNoList() != null) {
						fulfillment.put("tracking_number", shipPopData.getTrackingNoList());
					} else {
						fulfillment.put("tracking_number", tNumber);
					}
					// fulfillment.put("tracking_company", ShopName);
					// fulfillment.put("line_items", lineItemsArray);
					// fulfillmentInfo.put("line_items", lineItemsArray);
					// fulfillment.put("notify_customer", "true");
					fulfillment.put("notify_customer", "true");
					parameters4.put("fulfillment", fulfillment);
					String getFulfillmentCreateStep4Uri = uri + getFulfillmentCreateStep1Url + orderId
							+ "/fulfillments.json";
					try {
					node = restService.postRestTemplate(headers, getFulfillmentCreateStep4Uri, parameters4.toString());
					}
					catch(Exception e) {
							LOGGER.debug("##422Error##shopifyPostOrderIdFullist######error==>" + e.getMessage());
						}
				}
				// }

				resultMap.put("respones", node.toString());
//			} // for sDataList

			// LOGGER.debug("shopifyPostOrderIdFullist######22responseJson4==>"+responseJson4);
			// String respones = rest.postForObject((uri+getOrder), request, String.class);
			// //post방식
			// LOGGER.debug("######==>"+respones);

			resultMap.put("status", "ok");

		} catch (HttpStatusCodeException e) {
			String errorpayload = e.getResponseBodyAsString();
			resultMap.put("status", "err2");
			resultMap.put("message", e.getMessage().toString());
			LOGGER.debug("######shopifyPostOrderIdFullist###message==>" + e.getMessage().toString());
			LOGGER.debug("######shopifyPostOrderIdFullist###errorpayload==>" + errorpayload);
			// do whatever you want
		} catch (RestClientException e) {
			// no response payload, tell the user sth else
			LOGGER.debug("######shopifyPostOrderIdFullist###RestClientException==>" + e.getMessage());
		} catch (Exception e) {
			LOGGER.debug("shopifyPostOrderIdFullist######error==>" + e.getMessage());
			resultMap.put("status", "err2");
			resultMap.put("message", e.getMessage().toString());
		}

		return resultMap;
	}
    
    private void debugRestCall(String tag, String url, HttpMethod method, HttpEntity<String> httpEntity) {
		LOGGER.debug("REST CALL {} : url     = {} ", tag, url);
		LOGGER.debug("REST CALL {} : method  = {} ", tag, method.toString());
		LOGGER.debug("REST CALL {} : headers = {} ", tag, httpEntity.getHeaders());
		LOGGER.debug("REST CALL {} : body    = {} ", tag, httpEntity.getBody());
	}
    
    private void debugRestResult(String tag, ResponseEntity<String> result) {
    	LOGGER.debug("REST RESULLT {} : {} ", tag, result);
    }

	//특정 주문 이행목록
    @GetMapping("/shopifyGetOrderIdFullist")
    public ShipmentPopupData shopifyGetOrderIdFullist(ShipmentPopupData shipPopData,HttpServletRequest req, HttpSession sess) {
        ModelAndView mav = new ModelAndView("common/access_token");
        ShipmentPopupData returnSpd = new ShipmentPopupData();
        String orderId = shipPopData.getOrderCode();
        if(orderId != null){
            try {
                    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
                    //parameters.add("client_id", AppId);
                    
                    //헤더설정-> 기본인증처리
                    ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
                    String accessToken = sessionData.getAccessToken();
                    accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("X-Shopify-Access-Token", accessToken);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
                    String uri = restConfig.makeURI();
                    String getFulfillmentList = uri + getFulfillmentListUrl.replace("{order_id}",orderId);
                    //LOGGER.debug("######getOrderId==>"+getOrderId);
                    //api 호출 
                    RestTemplate rest = new RestTemplate();
                    ResponseEntity<String> respones = rest.exchange(getFulfillmentList, HttpMethod.GET, request, String.class);
                    
                    if (respones.getStatusCode() == HttpStatus.OK) {
                    	//JSONObject responseJson = new JSONObject(respones.getBody().toString().replaceAll("\\\\", ""));
                    	JSONObject responseJson = new JSONObject(respones.getBody().toString());
                        LOGGER.debug("shopifyGetOrderIdFullist######respones==>"+respones);
                    }
                    //String respones = rest.postForObject((uri+getOrder), request, String.class);      //post방식
                    //LOGGER.debug("######==>"+respones);
                    
                    mav.addObject("respones", respones);
                    mav.addObject("status", "ok");
    
            }catch(Exception e) {
                mav.addObject("status", "err2");
                mav.addObject("message", e.getMessage().toString());
            }
        }else{
            mav.addObject("status", "no");
            mav.addObject("message", "No Data");
        }
        return returnSpd;
    }
	
    //결제
  	public ShipmentPopupData shopifyProcBilling(ShipmentPopupData shipPopData,HttpServletRequest req, HttpSession sess) {
  		ShipmentPopupData returnVal = new ShipmentPopupData();
  		ModelAndView mav = new ModelAndView("jsonView");
  		try {
  			ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
  			String billingYn=sessionData.getBillingYn();
  			String testYN="";
  			if(billingYn.equals("N")){testYN = "true";}
		  			if(shipPopData.getGoods() == null) {
						shipPopData.setGoods("test");
					}
					if(shipPopData.getPaymentTotal() == 0){
						shipPopData.setPaymentTotal(1);
					}
					JSONObject parameters = new JSONObject();
					JSONObject applicationCharge = new JSONObject();
					// KRW -> USD
					String price_usd = exchange.getExchangePrice(shipPopData.getTotalAmountSum()+"");		// 조한두 : 국내택배 픽업시 총결제 금액 계산 ****************
					applicationCharge.put("id", shipPopData.getOrderCode());
					applicationCharge.put("name", shipPopData.getGoods());
					applicationCharge.put("price", price_usd); // 조한두 
					String decorated_return_url = "https://"+sessionData.getDomain()+"/admin/apps/"+sessionData.getAppName()+"/shipment";
					applicationCharge.put("return_url", decorated_return_url+"/confirm/");
					
					applicationCharge.put("test", testYN); // 테스트 목적으로 '테스트'를 포함 할 수 있습니다. 청구 작성시 true입니다. 이렇게하면 신용 카드가 청구되지 않습니다. 테스트 상점 및 데모 상점은 청구 할 수 없습니다.
					parameters.put("application_charge", applicationCharge);
					
  					//헤더설정-> 기본인증처리
					ShopData sd = new ShopData();
                	sd.setEmail(shipPopData.getEmail());
                	sd.setMasterCode(shipPopData.getMasterCode());
                	sd.setMasterCodeList(shipPopData.getMasterCodeList());
					List<ShopData> sDataList = shopService.selectShopOrderDomain(sd);
                	for(int intS = 0; intS < sDataList.size(); intS++) {
                		//ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
                        String accessToken = sDataList.get(intS).getAccessToken();
                        accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);
                        ShopData ssd = new ShopData();
                        ssd.setDomain(sDataList.get(intS).getDomain());
                        String ShopName = util.getUserVoDomain(ssd);
      					HttpHeaders headers = new HttpHeaders();
      					headers.add("X-Shopify-Access-Token", accessToken);
      					headers.add("Authorization", accessToken);
      					//headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
      					headers.setContentType(MediaType.APPLICATION_JSON);
      					HttpEntity<String> request = new HttpEntity<String>(parameters.toString(), headers);
      					LOGGER.debug("######shopifyProcBilling###accessToken==>"+accessToken);
      					LOGGER.debug("######shopifyProcBilling###parameters==>"+parameters);
      					LOGGER.debug("######shopifyProcBilling###request==>"+request);
      					//String uri = restConfig.makeURI();
      					String uri = "https://"+ShopName+"."+authUri;
      					LOGGER.debug("######shopifyProcBilling###uri+procBilling==>"+uri+procBilling);
      					
      					//api 호출 
      					RestTemplate rest = new RestTemplate();
      					ResponseEntity<String> respones = rest.exchange((uri+procBilling), HttpMethod.POST, request, String.class);
      					JSONObject responseJson = null;
      					
      					LOGGER.debug("######shopifyProcBilling###respones.getStatusCode()==>"+respones.getStatusCode());
                        if (respones.getStatusCode() == HttpStatus.OK || respones.getStatusCode() == HttpStatus.CREATED) {
                        	//responseJson = new JSONObject(respones.getBody().toString().replaceAll("\\\\", ""));
                        	responseJson = new JSONObject(respones.getBody().toString());
                            LOGGER.debug("shopifyGetOrderIdFullist######respones==>"+respones);
                            
    	  					LOGGER.debug("######shopifyProcBilling###respones==>"+respones);
    	  					JSONObject application_charge = responseJson.getJSONObject("application_charge");
		  					returnVal.setApiTest(testYN);
    	  					returnVal.setApiClientId(application_charge.getString("api_client_id"));
    	  					returnVal.setApiStatus(application_charge.getString("status"));
    	  					returnVal.setApiName(application_charge.getString("name"));
    	  					returnVal.setApiPrice(shipPopData.getTotalAmountSum()+"");		// 조한두 : KRW 금액
    	  					returnVal.setPriceUsd(application_charge.getString("price")); // 조한두 : USD 금액
    	  					returnVal.setApiReturnUrl(application_charge.getString("return_url"));
    	  					returnVal.setApiConfirmationUrl(application_charge.getString("confirmation_url"));
    	  					returnVal.setApiId(application_charge.getString("id"));
    	  					returnVal.setApiReturnJson(application_charge.toString());
    	  					returnVal.setApiReturnChk(true);
    	  					
    	  					LOGGER.debug("######shopifyProcBilling###application_charge==>"+application_charge);
    	  					LOGGER.debug("######shopifyProcBilling###application_charge.name==>"+application_charge.getString("name"));
                        }
                	} // for sDataList
  					
  		}catch(HttpStatusCodeException e){
  			 returnVal.setApiReturnChk(false);
 		     String errorpayload = e.getResponseBodyAsString();
 		     LOGGER.debug("######shopifyProcBilling###message==>"+e.getMessage().toString());
 		     LOGGER.debug("######shopifyProcBilling###errorpayload==>"+errorpayload);
 		     //do whatever you want
 		}catch(RestClientException e){
 		     //no response payload, tell the user sth else
 			LOGGER.debug("######shopifyProcBilling###RestClientException==>"+e.getMessage());
 		}catch(Exception e) {
  			LOGGER.debug("######shopifyProcBilling#222##returnVal==>"+returnVal);
  			returnVal.setApiReturnChk(false);
  			LOGGER.debug("######shopifyProcBilling###message==>"+e.getMessage().toString());
  			mav.addObject("message", e.getMessage().toString());
  		}
  		return returnVal;
  	}
  	
	/*
	* 결제 후 accept 처리
	* param ShipmentPopupData
	*/
  	public ShipmentPopupData shopifyProcBillingActivate(ShipmentPopupData shipPopData,HttpServletRequest req, HttpSession sess) {
  		ShipmentPopupData returnVal = new ShipmentPopupData();
//  		ModelAndView mav = new ModelAndView("jsonView");
  		try {
  			
			ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
			if(sessionData == null)  {
				returnVal.setApiReturnChk(false);
				return returnVal;
			}
			String decorated_return_url = "https://"+sessionData.getDomain()+"/admin/apps/"+sessionData.getAppName()+"/shipment";

			JSONObject applicationCharge = new JSONObject();
			
    		applicationCharge.put("id", shipPopData.getApiId());
			applicationCharge.put("name", shipPopData.getApiName());
			applicationCharge.put("api_client_id", shipPopData.getApiClientId());
			applicationCharge.put("price", shipPopData.getApiPrice());
			applicationCharge.put("status", "accepted");
			applicationCharge.put("test", "");

			applicationCharge.put("return_url", decorated_return_url);
			applicationCharge.put("decorated_return_url", decorated_return_url+"/confirm/");

			JSONObject sendParm = new JSONObject();
			sendParm.put("application_charge", applicationCharge);
			
			//헤더설정-> 기본인증처리
			ShopData sd = new ShopData();
        	sd.setEmail(shipPopData.getEmail());
        	sd.setShopId(shipPopData.getShopId());
        	sd.setMasterCode(shipPopData.getMasterCode());
        	sd.setMasterCodeList(shipPopData.getMasterCodeList());
			List<ShopData> sDataList = shopService.selectShopOrderDomain(sd);
			
        	for(int intS = 0; intS < sDataList.size(); intS++) {

                String accessToken = sDataList.get(intS).getAccessToken();
                accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);
                ShopData ssd = new ShopData();
                ssd.setDomain(sDataList.get(intS).getDomain());
                String ShopName = util.getUserVoDomain(ssd);

                
				HttpHeaders headers = new HttpHeaders();
				headers.add("X-Shopify-Access-Token", accessToken);
				headers.add("Authorization", accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				//String uri = restConfig.makeURI();
				String uri = "https://"+ShopName+"."+authUri;
				String procBillingActivate = uri + getBillingUrl+shipPopData.getApiId()+"/activate.json";
				LOGGER.debug("######shopifyProcBilling###uri+procBillingActivate==>"+procBillingActivate);
				
				JsonNode node = restService.postRestTemplate(headers, procBillingActivate, sendParm.toString());
				if(node.isNull())  {
					returnVal.setApiReturnChk(false);
					return returnVal;
				} else {
					returnVal.setApiReturnChk(true);
				}
				
                //LOGGER.debug("######shopifyProcBilling#111##returnVal==>"+returnVal);
        	} // for sDataList
  					
  		}catch(HttpStatusCodeException e){
  		     String errorpayload = e.getResponseBodyAsString();
  		   returnVal.setApiReturnChk(false);
  		     LOGGER.debug("######shopifyProcBillingActivate###message==>"+e.getMessage().toString());
  		     LOGGER.debug("######shopifyProcBillingActivate###errorpayload==>"+errorpayload);
  		     //do whatever you want
  		} catch(RestClientException e){
  		     //no response payload, tell the user sth else
  			LOGGER.debug("######shopifyProcBillingActivate###RestClientException==>"+e.getMessage());
  		} catch(Exception e) {
  			LOGGER.debug("######shopifyProcBillingActivate#222##returnVal==>"+returnVal);
  			returnVal.setApiReturnChk(false);
  			LOGGER.debug("######shopifyProcBillingActivate###message==>"+e.getMessage().toString());
//  			mav.addObject("message", e.getMessage().toString());
  		}
  		return returnVal;
  	}
  	
  	/**
  	 * 결제 후 accept 처리 판토스
  	 * @param shipPopData<ShipmentPopupData> 
  	 * @param domain
  	 * @param appName
  	 * @return
  	 */
  	public ShipmentPopupData shopifyProcBillingActivate(ShipmentPopupData shipPopData, String domain, String appName) {
  		ShipmentPopupData returnVal = new ShipmentPopupData();
//  		ModelAndView mav = new ModelAndView("jsonView");
  		try {
  			
//			ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
//			if(sessionData == null)  {
//				returnVal.setApiReturnChk(false);
//				return returnVal;
//			}

  			String decorated_return_url = "https://"+domain+"/admin/apps/"+appName+"/shipment";
  			
			JSONObject applicationCharge = new JSONObject();
			
    		applicationCharge.put("id", shipPopData.getApiId());
			applicationCharge.put("name", shipPopData.getApiName());
			applicationCharge.put("api_client_id", shipPopData.getApiClientId());
			applicationCharge.put("price", shipPopData.getApiPrice());
			applicationCharge.put("status", "accepted");
			applicationCharge.put("test", "");

			applicationCharge.put("return_url", decorated_return_url);
			applicationCharge.put("decorated_return_url", decorated_return_url+"/confirm/");

			JSONObject sendParm = new JSONObject();
			sendParm.put("application_charge", applicationCharge);
			
			//헤더설정-> 기본인증처리
			ShopData sd = new ShopData();
        	sd.setEmail(shipPopData.getEmail());
        	sd.setShopId(shipPopData.getShopId());
			List<ShopData> sDataList = shopService.selectShopOrderDomain(sd);
			
        	for(int intS = 0; intS < sDataList.size(); intS++) {

        		ShopData ssd = sDataList.get(intS);

        		String accessToken = ssd.getAccessToken();
                accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);
                
                String ShopName = util.getUserVoDomain(ssd);
                
				HttpHeaders headers = new HttpHeaders();
				headers.add("X-Shopify-Access-Token", accessToken);
				headers.add("Authorization", accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				String uri = "https://"+ShopName+"."+authUri;
				String procBillingActivate = uri + getBillingUrl+shipPopData.getApiId()+"/activate.json";
				
				JsonNode node = restService.postRestTemplate(headers, procBillingActivate, sendParm.toString());
				if(node.isNull())  {
					returnVal.setApiReturnChk(false);
					return returnVal;
				} else {
					returnVal.setApiReturnChk(true);
				}
				
                //LOGGER.debug("######shopifyProcBilling#111##returnVal==>"+returnVal);
        	} // for sDataList
  					
  		}catch(HttpStatusCodeException e){
  		     String errorpayload = e.getResponseBodyAsString();
  		   returnVal.setApiReturnChk(false);
  		     LOGGER.debug("######shopifyProcBillingActivate###message==>"+e.getMessage().toString());
  		     LOGGER.debug("######shopifyProcBillingActivate###errorpayload==>"+errorpayload);
  		     //do whatever you want
  		} catch(RestClientException e){
  		     //no response payload, tell the user sth else
  			LOGGER.debug("######shopifyProcBillingActivate###RestClientException==>"+e.getMessage());
  		} catch(Exception e) {
  			LOGGER.debug("######shopifyProcBillingActivate#222##returnVal==>"+returnVal);
  			returnVal.setApiReturnChk(false);
  			LOGGER.debug("######shopifyProcBillingActivate###message==>"+e.getMessage().toString());
//  			mav.addObject("message", e.getMessage().toString());
  		}
  		return returnVal;
  	}

	/*
	* 결제 조회
	* param ShipmentPopupData
	*/
  	public List<ShipmentPopupData> shopifyGetBilling(ShipmentPopupData shipPopData,HttpServletRequest req, HttpSession sess) {
  		List<ShipmentPopupData> returnArray = new ArrayList<ShipmentPopupData>();
  		ShipmentPopupData returnVal = new ShipmentPopupData();
  		try {

			//헤더설정-> 기본인증처리
			ShopData sd = new ShopData();
        	sd.setEmail(shipPopData.getEmail());
        	sd.setShopId(shipPopData.getShopId());
        	sd.setMasterCode(shipPopData.getMasterCode());
        	sd.setMasterCodeList(shipPopData.getMasterCodeList());
			List<ShopData> sDataList = shopService.selectShopOrderDomain(sd);
        	for(int intS = 0; intS < sDataList.size(); intS++) {
        		LOGGER.debug("######shopifyGetBilling###sDataList.size()==>"+sDataList.size()+"//=>"+intS);
        		//ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        		
        		ShopData ssd = sDataList.get(intS);
        		
                String accessToken = ssd.getAccessToken();
                accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);
                
                String ShopName = util.getUserVoDomain(ssd);
                
				HttpHeaders headers = new HttpHeaders();
				headers.add("X-Shopify-Access-Token", accessToken);
				headers.add("Authorization", accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				//String uri = restConfig.makeURI();
				String uri = "https://"+ShopName+"."+authUri;
				String getBilling = uri + getBillingUrl + shipPopData.getApiId() + ".json";
				
				//api 호출 
				JsonNode node = restService.getRestTemplate(headers, getBilling);
				if(node.isNull()) {
					returnVal.setApiReturnChk(false);
  					returnArray.add(returnVal);
					return  returnArray;
				}
				JSONObject responseJson = new JSONObject(node.toString());
				JSONObject application_charge = responseJson.getJSONObject("application_charge");
				returnVal.setApiStatus(application_charge.getString("status"));
				returnVal.setApiReturnChk(true);
				returnArray.add(returnVal);	
				
                //LOGGER.debug("######shopifyProcBilling#111##returnVal==>"+returnVal);
        	} // for sDataList
  					
  		}catch(HttpStatusCodeException e){
  		     String errorpayload = e.getResponseBodyAsString();
  		     returnVal.setApiReturnChk(false);
  		     returnArray.add(returnVal);
  		     LOGGER.debug("######shopifyGetBilling###message==>"+e.getMessage().toString());
  		     LOGGER.debug("######shopifyGetBilling###errorpayload==>"+errorpayload);
  		     //do whatever you want
  		} catch(RestClientException e){
  		     //no response payload, tell the user sth else
  			LOGGER.debug("######shopifyGetBilling###RestClientException==>"+e.getMessage());
  		} catch(Exception e) {
  			LOGGER.debug("######shopifyGetBilling#222##returnVal==>"+returnVal);
  			returnVal.setApiReturnChk(false);
  			returnArray.add(returnVal);
  			LOGGER.debug("######shopifyGetBilling###message==>"+e.getMessage().toString());
  		}
  		return returnArray;
  	}
  	
  	/*
	* 결제 조회  판토스용 처리 데이타 
	* param ShipmentPopupData
	*/
	public List<ShipmentPopupData> shopifyGetBilling(ShopData sd, String apiId) {
		List<ShipmentPopupData> returnArray = new ArrayList<ShipmentPopupData>();
		ShipmentPopupData returnVal = new ShipmentPopupData();
		try {
	
			//헤더설정-> 기본인증처리
//			ShopData sd = new ShopData();
//	    	sd.setEmail(shipPopData.getEmail());
//	    	sd.setShopId(shipPopData.getShopId());
			List<ShopData> sDataList = shopService.selectShopOrderDomain(sd);
	    	for(int intS = 0; intS < sDataList.size(); intS++) {
	    		LOGGER.debug("######shopifyGetBilling###sDataList.size()==>"+sDataList.size()+"//=>"+intS);
	    		//ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
	    		
	    		ShopData ssd = sDataList.get(intS);
        		
                String accessToken = ssd.getAccessToken();
	            accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);

	            String ShopName = util.getUserVoDomain(ssd);
	            
				HttpHeaders headers = new HttpHeaders();
				headers.add("X-Shopify-Access-Token", accessToken);
				headers.add("Authorization", accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				//String uri = restConfig.makeURI();
				String uri = "https://"+ShopName+"."+authUri;
				String getBilling = uri + getBillingUrl + apiId + ".json";
				
				//api 호출 
				JsonNode node = restService.getRestTemplate(headers, getBilling);
				if(node.isNull()) {
					returnVal.setApiReturnChk(false);
					returnArray.add(returnVal);
					return  returnArray;
				}
				JSONObject responseJson = new JSONObject(node.toString());
				JSONObject application_charge = responseJson.getJSONObject("application_charge");
				returnVal.setApiStatus(application_charge.getString("status"));
				returnVal.setApiReturnChk(true);
				returnArray.add(returnVal);	
				
	            //LOGGER.debug("######shopifyProcBilling#111##returnVal==>"+returnVal);
	    	} // for sDataList
					
		}catch(HttpStatusCodeException e){
		     String errorpayload = e.getResponseBodyAsString();
		     returnVal.setApiReturnChk(false);
		     returnArray.add(returnVal);
		     LOGGER.debug("######shopifyGetBilling###message==>"+e.getMessage().toString());
		     LOGGER.debug("######shopifyGetBilling###errorpayload==>"+errorpayload);
		     //do whatever you want
		} catch(RestClientException e){
		     //no response payload, tell the user sth else
			LOGGER.debug("######shopifyGetBilling###RestClientException==>"+e.getMessage());
		} catch(Exception e) {
			LOGGER.debug("######shopifyGetBilling#222##returnVal==>"+returnVal);
			returnVal.setApiReturnChk(false);
			returnArray.add(returnVal);
			LOGGER.debug("######shopifyGetBilling###message==>"+e.getMessage().toString());
		}
		return returnArray;
	}

	//캐리어서비스 등록
  	public ModelAndView shopifyCarrierCreate(ShopData sData) {
  		ModelAndView mav = new ModelAndView("index");
  		String accessToken = sData.getAccessToken();
  		String ShopName = util.getUserVoDomain(sData);
  		JSONObject parameters = new JSONObject();
		JSONObject carrierService = new JSONObject();
		ShopifyOutApiDataCarrier aData = new ShopifyOutApiDataCarrier(); 
		String carrierPname = AppCarrierName;
		String carrierPcallback_url = AppCallbackUrl;
		String carrierPservice_discovery = "true";
		String carrierPservice_type = "api";
		try {
			carrierService.put("name", carrierPname);
			carrierService.put("callback_url", carrierPcallback_url);
			//carrierService.put("carrier_service_type", carrierPservice_type);
			carrierService.put("service_discovery", carrierPservice_discovery);
			parameters.put("carrier_service", carrierService);

			
	  		accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);
	  		HttpHeaders headers = new HttpHeaders();
			headers.add("X-Shopify-Access-Token", accessToken);
			headers.setContentType(MediaType.APPLICATION_JSON);
			//HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
			//HttpEntity<JSONObject> request = new HttpEntity<>(parameters, headers);
			HttpEntity<String> request = new HttpEntity<String>(parameters.toString(), headers);
			
			String uri = "https://"+ShopName+"."+authUri;
	  		String createCarrier = uri+postCarrier;
			//LOGGER.debug("######getOrderId==>"+getOrderId);
	  		//api 호출 
	  		RestTemplate rest = new RestTemplate();
	  		ResponseEntity<String> respones = rest.exchange((createCarrier), HttpMethod.POST, request, String.class);
	  		LOGGER.debug("######shopifyCarrierCreate###respones==>"+respones);
	  		LOGGER.debug("######shopifyCarrierCreate###respones.getStatusCode()==>"+respones.getStatusCode());
	  		if (respones.getStatusCode() == HttpStatus.OK) {
	  			JSONObject responseJson = new JSONObject(respones.getBody().toString());
	  			LOGGER.debug("######shopifyCarrierCreate_OK==>"+respones);
	  			
	  			aData.setCallbackUrl(carrierPcallback_url);
  				aData.setName(carrierPname);
  				aData.setActive(carrierPservice_discovery);
  				aData.setFormat("json");
  				spApiOutService.insertCarrier(aData);
	  		}else if (respones.getStatusCode() == HttpStatus.CREATED) {
	  			JSONObject responseJson = new JSONObject(respones.getBody().toString());
	  			JSONObject rs = (JSONObject) responseJson.get("carrier_service");
	  			LOGGER.debug("######shopifyCarrierCreate_CREATED==>"+respones);
	  			
	  			String id = rs.get("id").toString();
	  			aData.setFormat("json");
	  			aData.setCallbackUrl(carrierPcallback_url);
	  			aData.setId(id);
  				aData.setName(carrierPname);
  				aData.setActive(carrierPservice_discovery);
	  			spApiOutService.updateCarrier(aData);
	  		}
		}catch(HttpStatusCodeException e){
  			 
 		     String errorpayload = e.getResponseBodyAsString();
 		     LOGGER.debug("######shopifyCarrierCreate###message==>"+e.getMessage().toString());
 		     LOGGER.debug("######shopifyCarrierCreate###errorpayload==>"+errorpayload);
 		     //do whatever you want
 		}catch(Exception e) {
			LOGGER.debug("######shopifyCarrierCreate###ShopName==>"+"https://"+ShopName+"."+authUri);
			LOGGER.debug("######shopifyCarrierCreate###accessToken==>"+accessToken);
			LOGGER.debug("######shopifyCarrierCreate###uri+CreateCarrier==>"+postCarrier);
			LOGGER.debug("######shopifyCarrierCreate###parameters==>"+parameters);
  			LOGGER.debug("######shopifyCarrierCreate###message==>"+e.getMessage().toString());
  		}
  		return mav;
  		
  	}
  	
  	//캐리어서비스 수정
  	public ModelAndView shopifyCarrierEdit(ShopifyOutApiDataCarrier carrierData, HttpServletRequest req, HttpSession sess) {
  		ModelAndView mav = new ModelAndView("index");
  		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
  		String accessToken = sessionData.getAccessToken();
  		String ShopName = util.getUserDomain(sess);
  		JSONObject parameters = new JSONObject();
		JSONObject carrierService = new JSONObject();
		ShopifyOutApiDataCarrier aData = new ShopifyOutApiDataCarrier(); 
		String carrierPname = AppCarrierName;
		String carrierPcallback_url = AppCallbackUrl;
		String carrierPservice_type = "api";
		String carrierPservice_discovery = "true";
		try {
			carrierService.put("name", carrierPname);
			carrierService.put("callback_url", carrierPcallback_url);
			//carrierService.put("carrier_service_type", carrierPservice_type);
			carrierService.put("service_discovery", carrierPservice_discovery);
			parameters.put("carrier_service", carrierService);

			
	  		accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);
	  		HttpHeaders headers = new HttpHeaders();
			headers.add("X-Shopify-Access-Token", accessToken);
			headers.setContentType(MediaType.APPLICATION_JSON);
			//HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
			//HttpEntity<JSONObject> request = new HttpEntity<>(parameters, headers);
			HttpEntity<String> request = new HttpEntity<String>(parameters.toString(), headers);
			
			String uri = "https://"+ShopName+"."+authUri;
	  		//LOGGER.debug("######getOrderId==>"+getOrderId);
			//EditCarrier = EditCarrier.replace("{carrier_service_id}", "32609796201");
			String editCarrier = uri+ListCarrier+carrierData.getId()+".json";
			
	  		//api 호출 
	  		RestTemplate rest = new RestTemplate();
	  		ResponseEntity<String> respones = rest.exchange(editCarrier, HttpMethod.PUT, request, String.class);
	  		LOGGER.debug("######shopifyCarrierCreate###respones==>"+respones);
	  		if (respones.getStatusCode() == HttpStatus.OK) {
	  			//JSONObject responseJson = new JSONObject(respones.getBody().toString().replaceAll("\\\\", ""));
	  			JSONObject responseJson = new JSONObject(respones.getBody().toString());
	  			LOGGER.debug("######shopifyCarrierCreate==>"+respones);
	  			JSONObject carrier_update = (JSONObject) responseJson.get("carrier_service");
	  			aData.setCallbackUrl(carrierPcallback_url);
	  			ShopifyOutApiDataCarrier rData = spApiOutService.selectCarrierCount(aData);
	  			int cnt = rData.getCarrierCnt();
	  			aData.setId(carrier_update.getString("id"));
  				aData.setName(carrierPname);
  				aData.setActive(carrierPservice_discovery);
  				spApiOutService.updateCarrier(aData);
	  		}
		}catch(HttpStatusCodeException e){
 			 
		     String errorpayload = e.getResponseBodyAsString();
		     LOGGER.debug("######shopifyCarrierEdit###message==>"+e.getMessage().toString());
		     LOGGER.debug("######shopifyCarrierEdit###errorpayload==>"+errorpayload);
		     //do whatever you want
		}catch(Exception e) {
			LOGGER.debug("######shopifyCarrierEdit###ShopName==>"+"https://"+ShopName+"."+authUri);
			LOGGER.debug("######shopifyCarrierEdit###accessToken==>"+accessToken);
			LOGGER.debug("######shopifyCarrierEdit###uri+CreateCarrier==>"+postCarrier);
			LOGGER.debug("######shopifyCarrierEdit###parameters==>"+parameters);
  			LOGGER.debug("######shopifyCarrierEdit###message==>"+e.getMessage().toString());
  		}
  		return mav;
  		
  	}
  	
  	//캐리어서비스 조회
  	public ModelAndView shopifyCarrierList(HttpServletRequest req, HttpSession sess) {
  		ModelAndView mav = new ModelAndView("index");
  		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
  		String accessToken = sessionData.getAccessToken();
  		String ShopName = sessionData.getShopName();
  		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		try {
	  		accessToken = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, accessToken);
	  		HttpHeaders headers = new HttpHeaders();
			headers.add("X-Shopify-Access-Token", accessToken);
			headers.setContentType(MediaType.APPLICATION_JSON);
			//HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
			//HttpEntity<JSONObject> request = new HttpEntity<>(parameters, headers);
			HttpEntity<String> request = new HttpEntity<String>(parameters.toString(), headers);

			String getCarrier = "https://"+ShopName+"."+authUri+postCarrier;
	  		//LOGGER.debug("######getOrderId==>"+getOrderId);
	  		//api 호출 
	  		RestTemplate rest = new RestTemplate();
	  		ResponseEntity<String> respones = rest.exchange(getCarrier, HttpMethod.GET, request, String.class);
	  		LOGGER.debug("######shopifyCarrierCreate###respones==>"+respones);
	  		
	  		if (respones.getStatusCode() == HttpStatus.OK) {
	  			//JSONObject responseJson = new JSONObject(respones.getBody().toString().replaceAll("\\\\", ""));
	  			JSONObject responseJson = new JSONObject(respones.getBody().toString());
	  			LOGGER.debug("######shopifyCarrierCreate==>"+respones);
	   		}
		}catch(HttpStatusCodeException e){
 			 
		     String errorpayload = e.getResponseBodyAsString();
		     LOGGER.debug("######shopifyCarrierList###message==>"+e.getMessage().toString());
		     LOGGER.debug("######shopifyCarrierList###errorpayload==>"+errorpayload);
		     //do whatever you want
		}catch(Exception e) {
			LOGGER.debug("######shopifyCarrierList###ShopName==>"+"https://"+ShopName+"."+authUri);
			LOGGER.debug("######shopifyCarrierList###accessToken==>"+accessToken);
			LOGGER.debug("######shopifyCarrierList###uri+CreateCarrier==>"+postCarrier);
			LOGGER.debug("######shopifyCarrierList###parameters==>"+parameters);
  			LOGGER.debug("######shopifyCarrierList###message==>"+e.getMessage().toString());
  		}
  		return mav;
  		
  	}
  	
  	//hmac로 로그인처리
  	public RedirectView shopifyHmac(HttpServletRequest req, HttpServletResponse res, HttpSession sess) {
		LOGGER.debug("############shopifyGenToken##############");
		
		ObjectMapper objectMapper =new ObjectMapper();
		ModelAndView mav = new ModelAndView("index");

		String param_code = req.getParameter("code");
		String param_hmac = req.getParameter("hmac");
		String param_shop = req.getParameter("shop");
		String param_state = req.getParameter("state");
		String param_timestamp = req.getParameter("timestamp");
		LOGGER.debug("############shopifyGenToken#111111111111111#############");
		try {
			if(param_code != null && param_hmac != null && param_shop != null && param_state != null && param_timestamp != null) {
	
				List<NameValuePair> params = new ArrayList<NameValuePair>();	//파라미터 add는 키이름순으로 해야함
				params.add(new BasicNameValuePair("code", param_code));
				params.add(new BasicNameValuePair("shop", param_shop));
				params.add(new BasicNameValuePair("state", param_state));
				params.add(new BasicNameValuePair("timestamp", param_timestamp));
				String param_string = util.httpBuildQuery(params, "UTF-8");
				//LOGGER.debug("############shopifyGenToken#222222222222222#############");
				
				String computed_param = hmac.hget(param_string);
				//LOGGER.debug("########param_hmac==>"+param_hmac);
				//LOGGER.debug("########computed_param==>"+computed_param);
				//String computed_hmac = hmac.hget(param_hmac);
				if(computed_param.equals(param_hmac)) {
					params = null;
					LOGGER.debug("############shopifyGenToken#33333333333333#############");
					
					//파라미터설정
					MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
					parameters.add("client_id", AppId);
					parameters.add("client_secret", AppPw);
					parameters.add("code", param_code);
					
					//헤더설정-> 기본인증처리
					HttpHeaders headers = new HttpHeaders();
					headers.add("X-Shopify-Access-Token", AppPw);
		
					HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
					String access_token_url = "https://" + param_shop + "/admin/oauth/access_token";
					//LOGGER.debug("############shopifyGenToken#444444444444444#############param_shop=>"+param_shop);
					//api 호출 
					RestTemplate rest = new RestTemplate();
					String respones = rest.postForObject((access_token_url), request, String.class);		//post방식
					//LOGGER.debug("############shopifyGenToken#444444444444444#############respones=>"+respones);
					ShopData sd = objectMapper.readValue(respones, ShopData.class);   // String to Object로 변환
					//LOGGER.debug("############shopifyGenToken#444444444444444#############sd=>"+sd);
					if(sd != null) {
						if(sd.getShop() == null ) sd.setShop(param_shop);
						if(sd.getAccess_token() != null)
							sd.setAccessToken(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sd.getAccess_token()));
//						if(sd.getAccessToken() != null)
//							sd.setAccessToken(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sd.getAccess_token()));
						 
						LOGGER.debug("############origin=>"+sd.getAccess_token()+"##############");
						LOGGER.debug("############encode=>"+util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sd.getAccess_token())+"##############");
						LOGGER.debug("############decode=>"+util.getAESDecrypt(SpConstants.ENCRYPT_KEY, util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sd.getAccess_token()))+"##############");
						LOGGER.debug(sd.toString());
						sess.setAttribute(SpConstants.HTTP_SESSION_KEY, sd);
						
						////return "redirect:/shopifyShopAuth";
					}
					
					shopifyShopAuth(sess, res);	//인스톨이후 로그인처리
					mav.addObject("access_token", respones);
					mav.addObject("status", "ok");
					
				}else {
					LOGGER.debug("############shopifyHmac#5555555555555#############");
					mav.addObject("status", "no");	//보내준 암호화코드로 파라미터의 유효성체크
				}
			}else {
				LOGGER.debug("############shopifyHmac#666666666666#############");
				mav.addObject("status", "err");		//파라미터의 값이 null인경우 체크
			}
		}catch(HttpStatusCodeException e){
			mav.addObject("status", "err2");
			mav.addObject("message", e.getMessage().toString());
		     String errorpayload = e.getResponseBodyAsString();
		     LOGGER.debug("######shopifyHmac###message==>"+e.getMessage().toString());
		     LOGGER.debug("######shopifyHmac###errorpayload==>"+errorpayload);
		     //do whatever you want
		}catch(Exception e) {		
			LOGGER.debug("############shopifyHmac#7777777777#############");//그외 오류체크
			mav.addObject("status", "err2");
			mav.addObject("message", e.getMessage().toString());
		}
		//LOGGER.debug("############shopifyGenToken#888888888888888#############");
		//return mav;
		
		LOGGER.debug("shopifyGenToken : RedirectView ");
		return new RedirectView("/");
	}

}