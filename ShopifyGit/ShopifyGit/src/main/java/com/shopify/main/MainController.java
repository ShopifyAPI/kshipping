package com.shopify.main;
 
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.admin.AdminData;
import com.shopify.admin.AdminScopeData;
import com.shopify.admin.AdminService;
import com.shopify.api.ShopifyApiService;
import com.shopify.common.MailService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.seller.SellerData;
import com.shopify.seller.SellerService;
import com.shopify.shop.ShopData;
import com.shopify.shop.ShopService;

@Controller
public class MainController {
	private Logger LOGGER = LoggerFactory.getLogger(MainController.class);
	
	@Autowired private SellerService sellerService;
	@Autowired private ShopService shopService;
	@Autowired private AdminService adminService;
	@Autowired private UtilFn util;
	@Autowired private MailService mailService;
	@Autowired private MainService mainService;
	@Autowired private ShopifyApiService spApiService;
	
	@GetMapping("/")
	public String selectNotice(Model model, HttpSession sess, MainData mainData, HttpServletRequest req
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
			, @RequestParam(value = "code", required = false, defaultValue = "") String code
		) {
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		//LOGGER.debug("###############1aaaaaaaaaaaa=MainController=>sd:"+util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sd.getAccessToken()));
		//주문목록 조회전..api를통하여 목록 갱신처리..
//        Map<String,Object> apiMap = orderService.shopifyGetOrder(model, req, sess);
        Map<String,Object> apiMap = spApiService.shopifyGetOrder(req, sess);
        LOGGER.debug("###############1aaaaaaaaaaaa=MainController=>apiMap:"+apiMap);
        
		MainData orderData = mainService.orderData(sess); 	
    	model.addAttribute("orderData", orderData);
		
		mainData.setCurrentPage(currentPage);
		mainData.setSearchType(searchType);
		mainData.setSearchWord(searchWord);
		mainData.setPageSize(pageSize);
		
		Map<String, Object> map = mainService.selectNotice(mainData, sess);
		
		model.addAttribute("search", mainData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		model.addAttribute("flagCount", map.get("flagCount"));
		
		return "/noticeList";
		
	}
	
	@GetMapping("/dashboard")
	public String selectNotice(Model model, HttpSession sess, MainData mainData, HttpServletRequest req
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
		) {
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		//주문목록 조회전..api를통하여 목록 갱신처리..
//        Map<String,Object> apiMap = orderService.shopifyGetOrder(model, req, sess);
        Map<String,Object> apiMap = spApiService.shopifyGetOrder(req, sess);
		
		MainData orderData = mainService.orderData(sess); 	
    	model.addAttribute("orderData", orderData);
		
		mainData.setCurrentPage(currentPage);
		mainData.setSearchType(searchType);
		mainData.setSearchWord(searchWord);
		mainData.setPageSize(pageSize);
		
		Map<String, Object> map = mainService.selectNotice(mainData, sess);
		
		model.addAttribute("search", mainData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		model.addAttribute("flagCount", map.get("flagCount"));
		
		return "/noticeList";
	}

	/*
	*	 현황 내용
	*/
	@RequestMapping(value="/selectPost")
	public String index(Model model, HttpSession sess,@ModelAttribute MainData post) throws Exception {

		MainData orderData = mainService.orderData(sess);
	 	// 우체국 API호출
		if(post.getCurrentPage() == 0) {
			post.setCurrentPage(1);
		}
		
		if(post.getPageSize() == 0) {
			post.setPageSize(10);
		}
		// 우체국 API호출
		Map<String, Object> postNoticeData = mainService.postNoticeData(post);
		
		model.addAttribute("list", postNoticeData.get("list"));
		model.addAttribute("paging", postNoticeData.get("paging"));
		model.addAttribute("errCode", postNoticeData.get("errCode"));
		model.addAttribute("errMsg", postNoticeData.get("errMsg"));
		model.addAttribute("orderData", orderData);
		
	    return "index";
	
	
   }	
	
/*	
    @RequestMapping(value="/selectPost")
    public String index(Model model, HttpSession sess, MainData mainData) throws Exception {
    	// 현황 내용
    	MainData orderData = mainService.orderData(sess);
    	
    	// 우체국 API호출
    	Map<String, Object> postNoticeData = mainService.postNoticeData(orderData);
    	
    	model.addAttribute("list", postNoticeData.get("list"));
    	model.addAttribute("paging", postNoticeData.get("paging"));
    	model.addAttribute("errCode", postNoticeData.get("errCode"));
    	model.addAttribute("errMsg", postNoticeData.get("errMsg"));
    	model.addAttribute("orderData", orderData);
    	
    	return "index";
    }  */  
    
	/**
	 * NOTICE 상세 조회
	 * @return
	 */
	@GetMapping("/showNotice")
	public String showNotice(Model model, MainData mainData, HttpSession sess) throws Exception {
	    MainData data = mainService.showNotice(mainData, sess);
	    List<MainData> fileData = mainService.showNoticeFile(mainData, sess);
		model.addAttribute("mainData", data);
		model.addAttribute("mainFileData", fileData);
		return "/noticeShow";
	}
	
/*
 * YR
 * 
 *     @RequestMapping(value="/")
    public String index(Model model, HttpSession sess, @ModelAttribute MainData post) throws Exception {
    	// 현황 내용
    	MainData orderData = mainService.orderData(sess);
    	
    	LOGGER.debug("getCurrentPage : " + post.getCurrentPage());
    	LOGGER.debug("getPageSize : " + post.getPageSize());
    	
    	if(post.getCurrentPage() == 0) {
    		post.setCurrentPage(1);
    	}
    	
    	if(post.getPageSize() == 0) {
    		post.setPageSize(10);
    	}
    	// 우체국 API호출
    	Map<String, Object> postNoticeData = mainService.postNoticeData(post);
    	
    	model.addAttribute("list", postNoticeData.get("list"));
    	model.addAttribute("paging", postNoticeData.get("paging"));
    	model.addAttribute("errCode", postNoticeData.get("errCode"));
    	model.addAttribute("errMsg", postNoticeData.get("errMsg"));
    	model.addAttribute("orderData", orderData);
    	
        return "index";
    }
*
* YR
*/
	@RequestMapping(value="/popup/postNotice")
    public String popPostNotice(Model model, HttpSession sess) throws Exception {
    	
        return "popPostNotice";
    }

	/*
	*	alert 팝업
	*/
	@RequestMapping(value="/common/popAlert", method=RequestMethod.GET)
    public Map<String,Object> commonPopAlert(Model model, HttpServletRequest req, HttpSession sess) throws Exception {
        ObjectMapper objectMapper =new ObjectMapper();
        Map<String,Object> resultMap = new HashMap<String,Object>();

        String str = "";
        Map<String,String[]> map = req.getParameterMap();
        for(Iterator<String> n  = map.keySet().iterator(); n.hasNext();) {
            str += n.next();
        }
        str = str.replaceAll("\\\"", "");
        str = "{"+str+"}";
        JSONObject popup = new JSONObject(str);
        //MainPopupData popup = (MainPopupData) objectMapper.readValue(str, MainPopupData.class);
        
        //LOGGER.debug("###############1aaaaaaaaaaaa=sessionData_login=>2:"+popup.get("title"));
        
        String message = popup.get("message").toString();
        String title = popup.get("title").toString();
        resultMap.put("message",message);
        resultMap.put("title",title);
        
        return resultMap;
    }

	/*
	*	confirm 팝업
	*/
	@RequestMapping(value="/common/popConfirm", method=RequestMethod.GET)
    public Map<String,Object> commonPopConfirm(Model model, HttpServletRequest req, HttpSession sess) throws Exception {
        ObjectMapper objectMapper =new ObjectMapper();
        Map<String,Object> resultMap = new HashMap<String,Object>();

        String str = "";
        Map<String,String[]> map = req.getParameterMap();
        for(Iterator<String> n  = map.keySet().iterator(); n.hasNext();) {
            str += n.next();
        }
        str = str.replaceAll("\\\"", "");
        str = "{"+str+"}";
        JSONObject popup = new JSONObject(str);
        //MainPopupData popup = (MainPopupData) objectMapper.readValue(str, MainPopupData.class);
        
        //LOGGER.debug("###############1aaaaaaaaaaaa=sessionData_login=>2:"+popup.get("title"));
        
        String message = popup.get("message").toString();
        String title = popup.get("title").toString();
        resultMap.put("message",message);
        resultMap.put("title",title);
        
        return resultMap;
    }
    
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/*
	*	로그인페이지
	*/    
    @GetMapping("/login")
	public ModelAndView login(HttpServletRequest req, HttpSession sess) {
		ModelAndView mav = new ModelAndView("common/login/login");
		
		System.out.println("#### login > Start");
		
		InetAddress local; 
		try { 
			local = InetAddress.getLocalHost(); 
			String ip = local.getHostAddress(); 
			//System.out.println("local ip : " + ip); 
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		
		//System.out.println("#### login > End");
		
		return mav;
	}
    
    /*
	*	로그인페이지
	*/    
    @GetMapping("/loginOri")
	public ModelAndView loginOri(HttpServletRequest req, HttpSession sess) {
		ModelAndView mav = new ModelAndView("common/login/loginOri");
		
		System.out.println("#### login > Start");
		
		InetAddress local; 
		try { 
			local = InetAddress.getLocalHost(); 
			String ip = local.getHostAddress(); 
			//System.out.println("local ip : " + ip); 
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		
		//System.out.println("#### login > End");
		
		return mav;
	}

	/*
	*	로그인처리
	*/	
	@RequestMapping(value="/loginProc", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> loginProc(@RequestBody String reqBody, Model model, HttpServletRequest req, HttpSession sess) {
		
		//System.out.println("#### loginProc > Start");
		
		ObjectMapper objectMapper =new ObjectMapper();
		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			SellerData seller = (SellerData) objectMapper.readValue(reqBody, SellerData.class);
			
			//seller.setPasswd(util.getBCryptDecrypt(seller.getPasswd()));
			//셀러정보 조회 세션처리
			int cnt = sellerService.selectSellerCount(seller);
			ShopData loSd = (ShopData) sess.getAttribute(SpConstants.LOCALE_SESSION_KEY);    //언어세션처리
			if(cnt != 0) {
				SellerData sellerData = (SellerData) sellerService.selectSeller(seller);
				LOGGER.debug("###############loginProc=seller.getPasswd()=>/"+util.getBCryptDecrypt("1"));
				if(passwordEncoder.matches(seller.getPasswd(), sellerData.getPasswd())) {
				    LOGGER.debug("###############loginProc=1=>/"+cnt);
				    
				    String locale = (loSd == null) ? "ko" : loSd.getLocale();
				    
				    if(sessionData == null) {
						sessionData = new ShopData();
						sessionData.setEmail(sellerData.getEmail());
						sessionData.setEncryptKey(SpConstants.ENCRYPT_KEY);
						sessionData.setLocale(locale);
						sessionData.setRankId(sellerData.getRankId());
						sessionData.setRankRate(sellerData.getRankRate());
						sessionData.setPrivatechk(sellerData.getPrivatechk());
						sess.setAttribute(SpConstants.HTTP_SESSION_KEY, sessionData);
						resultMap.put("statusSeller","OK");
					}else {
						sessionData.setEmail(sellerData.getEmail());
						sessionData.setEncryptKey(SpConstants.ENCRYPT_KEY);
						sessionData.setLocale(locale);
						sessionData.setRankId(sellerData.getRankId());
                        sessionData.setRankRate(sellerData.getRankRate());
						sessionData.setPrivatechk(sellerData.getPrivatechk());
                        /*                        sessionData.setPrivateChk(sellerData.getPrivateChk());*/
						sess.setAttribute(SpConstants.HTTP_SESSION_KEY, sessionData);
						resultMap.put("statusSeller","OK");
					}
				    LOGGER.debug("###############loginProc=1locale=>/"+sessionData.getLocale());
					//샵정보 조회하여 세션처리
					ShopData shop = new ShopData();
					shop.setEmail(sellerData.getEmail());
					shop.setEncryptKey(SpConstants.ENCRYPT_KEY);
					int cntShop = shopService.selectOneShopCount(shop);
					if(cntShop == 0) {
						resultMap.put("statusShop",HttpStatus.BAD_REQUEST);
					}else {
						String redirectUrl = "";
						if(sess.getAttribute(SpConstants.REFFRER_SESSION_KEY) != null) {
							redirectUrl = sess.getAttribute(SpConstants.REFFRER_SESSION_KEY).toString();
						}
						
						if("/error".equals(redirectUrl) || "/robots.txt".equals(redirectUrl)) {
							redirectUrl = "";
						}
						
						ShopData sd = shopService.selectOneShop(shop);
						sessionData.setEmail(sellerData.getEmail());
						sessionData.setAccessToken(sd.getAccessToken());
						sessionData.setId(sd.getId());
						sessionData.setShopId(sd.getShopId());
						sessionData.setShopIdx(sd.getShopIdx());
						sessionData.setEncryptKey(SpConstants.ENCRYPT_KEY);
						sessionData.setLocale(locale);
						sessionData.setRankId(sd.getRankId());
                        sessionData.setRankRate(sd.getRankRate());
                        sessionData.setShopName(sd.getShopName());
                        sessionData.setDomain(sd.getDomain());
						sess.setAttribute(SpConstants.HTTP_SESSION_KEY, sessionData);
						sess.removeAttribute(SpConstants.REFFRER_SESSION_KEY);
						
						LOGGER.debug("## loginProc > redirectUrl : "+redirectUrl);
						
						resultMap.put("redirectUrl",redirectUrl);
						resultMap.put("statusShop","OK");
						
						
						//LOGGER.debug("###############1aaaaaaaaaaaa=sessionData_login=>/"+sessionData);
						//LOGGER.debug("###############1aaaaaaaaaaaa=sessionData_login=>/"+HttpStatus.OK);
					}
					
					//resultMap.put("chk","ok");
				}else {
				    LOGGER.debug("###############loginProc=2=>/"+cnt);
					resultMap.put("statusSeller",HttpStatus.BAD_REQUEST);
					resultMap.put("statusShop",HttpStatus.FORBIDDEN);
				}
			}else {
				//resultMap.put("orderList","ok");
				resultMap.put("statusSeller",HttpStatus.FORBIDDEN);
				resultMap.put("statusShop",HttpStatus.BAD_REQUEST);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultMap.put("statusSeller",HttpStatus.FORBIDDEN);
			resultMap.put("statusShop",HttpStatus.FORBIDDEN);
		}
		
		InetAddress local; 
		try { 
			local = InetAddress.getLocalHost(); 
			String ip = local.getHostAddress(); 
			//System.out.println("local ip : " + ip); 
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}

		
		//System.out.println("#### loginProc > End");
		
		return resultMap;
	}

	/*
	*	관리자 로그인페이지
	*/    
	@GetMapping("/admin/login")
	public ModelAndView adminLogin(HttpServletRequest req, HttpSession sess) {
		ModelAndView mav = new ModelAndView("admin/common/login");
		
		return mav;
	}

	/*
	*	관리자 로그인처리
	*/
	@PostMapping(value="/admin/loginProc")
	public ModelAndView adminLoginProc(Model model, HttpSession sess, @RequestBody AdminData admin) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		AdminData sessionData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);		
		ShopData loSd = (ShopData) sess.getAttribute(SpConstants.LOCALE_SESSION_KEY);
		//LOGGER.debug("###############1aaaaaaaaaaaa=loSd=>/"+loSd);
		
		String locale = (loSd == null) ? "ko" : loSd.getLocale();
		
		try {
			AdminData adminData = adminService.selectAdminLogin(admin);
			
			if(adminData != null) {
				
				// 비밀번호 비교
				if(passwordEncoder.matches(admin.getPasswd(), adminData.getPasswd())) {
					
					String useSdate = adminData.getUseSdate();
					String useEdate = adminData.getUseEdate();
					String scopeId = adminData.getScopeId();
					
					LOGGER.debug("today2 [useSdate]: " + useSdate.compareTo(util.getDateElement("today")));
					LOGGER.debug("today2 [useEdate]: " + useEdate.compareTo(util.getDateElement("today")));
					
					// 사용 기간 확인
					if(useSdate.compareTo(util.getDateElement("today")) <= 0 && useEdate.compareTo(util.getDateElement("today")) >= 0) {
						
						String redirect = "";
						String authority = "|";

						// 권한 체크 하기
						AdminScopeData scope = new AdminScopeData();
						scope.setScopeId(adminData.getScopeId());
						
						LOGGER.debug("adminData.getScopeId() : " + adminData.getScopeId());
						
						List<AdminScopeData> list = adminService.selectAdminScope(scope);
						
						// 권한 설정 체크 
						if(list.size() > 0) {
							int index = 0;
							
							for (AdminScopeData item : list) {
								if(index == 0) {
									redirect = item.getRedirect(); // 첫페이지 설정
								}
								
								//LOGGER.debug("login proc [redirect] : " + item.getRedirect());
								
								authority += item.getMenuId() + "|";
								
								index++;
							}
							
							if(sessionData == null) {
								sessionData = new AdminData();
								sessionData.setEmail(admin.getEmail());
								sessionData.setEncryptKey(SpConstants.ENCRYPT_KEY);
								sessionData.setLocale(locale);
								sessionData.setAuthority(authority);
								sessionData.setRedirect(redirect);
							} else {
								sessionData.setEmail(admin.getEmail());
								sessionData.setEncryptKey(SpConstants.ENCRYPT_KEY);
								sessionData.setLocale(locale);
								sessionData.setAuthority(authority);
								sessionData.setRedirect(redirect);
							}
							
							sess.setAttribute(SpConstants.ADMIN_SESSION_KEY, sessionData);
							
							model.addAttribute("redirect", redirect);
							model.addAttribute("errCode", true);
							model.addAttribute("errMsg", "성공.");
							
							
						} else {
							model.addAttribute("redirect", "");
							model.addAttribute("errCode", false);
							model.addAttribute("errMsg", "사용 권한이 없습니다.");
						}
						
					} else {
						model.addAttribute("redirect", "");
						model.addAttribute("errCode", false);
						model.addAttribute("errMsg", "사용 기간을 확인해 주십시오.");
					}
					
				} else {
					model.addAttribute("redirect", "");
					model.addAttribute("errCode", false);
					model.addAttribute("errMsg", "정보가 일치 하지 않습니다.");
				}
				
			} else {
				model.addAttribute("redirect", "");
				model.addAttribute("errCode", false);
				model.addAttribute("errMsg", "정보가 일치 하지 않습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			
			model.addAttribute("redirect", "");
			model.addAttribute("errCode", false);
			model.addAttribute("errMsg", "로그인 중 오류가 발생했습니다.");
		}
		
		LOGGER.debug("loginProc : " + model.toString());
		
		return mav;
	}

	/*
	*	로그아웃
	*/
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String loout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		return "redirect:/login";
	}
	
	/*
	*	관리자 로그아웃
	*/
	@GetMapping(value = "/admin/logout")
	public String adminLogOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		return "redirect:/admin/login";
	}

	/*
	*	비밀번호 찾기페이지
	*/
    @GetMapping("/admin/findPasswd")
	public String adminFindPasswd(HttpServletRequest req, HttpSession sess) {
		return "admin/common/findPasswd";
	}
	
	
	/*
	*	비밀번호 찾기
	*/	
    @RequestMapping(value="/admin/findPasswdSend", method=RequestMethod.POST)
	@ResponseBody
	public String adminFindPasswdSend(@RequestBody String reqBody, Model model, HttpServletRequest req, HttpSession sess) {
		ObjectMapper objectMapper =new ObjectMapper();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			SellerData seller = (SellerData) objectMapper.readValue(reqBody, SellerData.class);
			
			//셀러정보 조회 세션처리
			int cnt = sellerService.selectSellerCount(seller);
			if(cnt != 0) {
				SellerData sellerData = (SellerData) sellerService.selectSeller(seller);
				
				String email = sellerData.getEmail();
				//이메일 인증 url 생성 (날짜+시간+랜덤인증키)
				String toYear = util.getDateElement("yy");
				String toMonth = util.getDateElement("mm");
				String toDay = util.getDateElement("dd");
				String toHour = util.getDateElement("hh");
				String toMin = util.getDateElement("mi");
				String toRandom = util.getRandomString(20);	//
				String authEmailKey = toYear+toMonth+toDay+toHour+toMin+"@"+toRandom+"@"+email;
				//LOGGER.debug("###############444444444442authEmailKey=>/"+authEmailKey);
				String encryptKey = util.getAESEncrypt(SpConstants.FIND_PASSWD_KEY, authEmailKey); 
				String mailContens = "<a href='https://localhost/admin/findPasswdProc?authKey="+encryptKey+"'>";
				seller.setFindAuth(toRandom);
				boolean isSend = false;
				int resultCnt = sellerService.updateSellerPasswd(seller);
				if(resultCnt > 0)
					isSend = mailService.sendMail(email, "shopify 비번찾기 메일", mailContens);

				model.addAttribute("status", HttpStatus.OK);
			}else {
				model.addAttribute("status", HttpStatus.BAD_REQUEST);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("status", HttpStatus.FORBIDDEN);;
		}
		
		return "admin/common/findPasswdSend";
	}
	
	/*
	*	비밀번호 찾기 링크
	*/
	@RequestMapping(value="/admin/findPasswdProc", method=RequestMethod.POST)
	@ResponseBody
	public String adminFindPasswdProc(@RequestBody String reqBody, Model model, HttpServletRequest req, HttpSession sess) {
		ObjectMapper objectMapper =new ObjectMapper();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String emailKey = req.getParameter("authKey");
		String decryptKey = "";
		String[] arryKey = null;
		boolean authChk = false;
		int resultCnt = 0;
		SellerData seller = null;
		try {
			seller = (SellerData) objectMapper.readValue(reqBody, SellerData.class);
			LOGGER.debug("###############444444444442 seller=>/"+seller);
			LOGGER.debug("###############444444444442 emailKey=>/"+emailKey);
			if(emailKey != null && seller != null) {
				//받은키 복호화
				decryptKey = util.getAESDecrypt(SpConstants.FIND_PASSWD_KEY, emailKey);
				if(decryptKey != null) {
					//복호화키 배열로 끊기
					if(decryptKey.indexOf("@") > 0) {
						arryKey = decryptKey.split("@");
						if(arryKey[0] != "") {
							String toYear = util.getDateElement("yy");
							String toMonth = util.getDateElement("mm");
							String toDay = util.getDateElement("dd");
							String toHour = util.getDateElement("hh");
							String toMin = util.getDateElement("mi");
							long authTime = Long.parseLong(toYear+toMonth+toDay+toHour+toMin);
							long authParam = Long.parseLong(arryKey[0]);
							if(authTime - authParam <= 60 ) authChk = true;	//1시간이내만 인정
						}
	
						if(authChk) {
							if(arryKey[2] != "") {
								seller.setEmail(arryKey[2]);
							}
						}
					}
					
				}
			}
			
			
			//셀러정보 조회 세션처리
			int cnt = sellerService.selectSellerCount(seller);
			if(cnt != 0) {
				SellerData sellerData = (SellerData) sellerService.selectSeller(seller);
				
				String email = sellerData.getEmail();
				String find_auth = sellerData.getFindAuth();
				//이메일 인증 url 생성 (날짜+시간+랜덤인증키)
				if(arryKey[1] != "") {
					if(!arryKey[1].equals(find_auth)) {
						authChk = false;
					}
				}
				if(authChk) {	//실제 패스워드 변경처리
					resultCnt = sellerService.updateSellerPasswd(seller);
				}
				
				
				model.addAttribute("status", HttpStatus.OK);
			}else {
				model.addAttribute("status", HttpStatus.BAD_REQUEST);
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "admin/common/findPasswdProc";
	}
}
