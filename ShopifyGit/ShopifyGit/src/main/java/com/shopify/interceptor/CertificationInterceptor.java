package com.shopify.interceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.admin.AdminData;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.shop.ShopData;
/**
 * 수정 사항: 
 *  2020.04.10:  조한두 : preHandle 처리 로직 분기 처리: Admin, 셀러, 기타 (other)
 *  
 *   
 *   <기타>
 *   1)preHandle - Controller 실행 요청전       :    return false 면 Controller 로 요청을 보내지 않음, true 면 preHandle 처리 후 Controller 로 요청이 정상적으로 감 
 *   preHandle은 리턴값이 boolean형태다. 리턴값이 true가 아닐경우 해당 컨트롤러로 이어주지 않는다.
    			 if (session == null) {
   						// 처리를 끝냄 - 컨트롤로 요청이 가지 않음.
   						response.sendRedirect("/login.do");
   				 		return false;
  				 }
	  2)postHandle - view(jsp)로 forward되기 전에
      3)afterCompletion - 끝난뒤
 * 
 * 
 * @author 조한두 
 *
 */

/**
 * CertificationInterceptor 프로그램은 MyBatisConfig.java에서   "registry.addInterceptor(new CertificationInterceptor())" 처럼 "new()" 생성되었기 때문에,
 * "new()" 로 만들어질때는, @Autowired 나 @Value 와 같은 annotation 이 적용되지 않는다.
 *  1. 그래서 MytabisConfig.java 안에서 annotation 을 사용해서 만들어야 한다.
 *  2. 그러므로 spring은 "@Component" 라는 annotation 에 의하여 annotation 이 적용된 class 를 하나 만들어 놓지만, MybatisConfig 에서는 new() 로 새로운 instance 를 만들어 사용한다.
 *  3. 따라서 혼돈을 방지하기 위하여 @Component 라는 annotation 도 생략해야 하는데, util 이 사용하는 곳이 있어서, 아직은 그대로, error 발생하는 상태로 그대로 둔다.
 * @author CHOI
 *
 */
@Component
public class CertificationInterceptor extends HandlerInterceptorAdapter {
	
    private Logger LOGGER = LoggerFactory.getLogger(CertificationInterceptor.class);
    
	private	boolean certificateSkip;
	
	 @Autowired private UtilFn util;
    
//	@PostConstruct
//	public void postConstruct() {
//		System.out.println("crazy");
//	}
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
    	System.out.println("##### preHandle #############################################################");
       	/*
         * admin/delivery  admin/price admin/rank admin/uploadPriceExcelFile < 하나로 통일해야함
         * */
        HttpSession session = request.getSession();
        ShopData sessionData = (ShopData) session.getAttribute(SpConstants.HTTP_SESSION_KEY);
        AdminData sessionAdmData = (AdminData) session.getAttribute(SpConstants.ADMIN_SESSION_KEY);
               
        String referrer = request.getRequestURI();
        //셀러 약관동의 체크
        String privatechk = "N";
        
        if(sessionData != null) {
        	privatechk = sessionData.getPrivatechk();
        }
        if(privatechk == null) {
        	privatechk = "N";
        }
        
        
        //셀러 활성 여부 체크  [2020.05.15 YR]
        String activeYn = "N";
        
        if(sessionData != null) {
        	activeYn = sessionData.getActiveYn();
        }
        if(activeYn == null) {
        	activeYn = "N";
        } 
        
        
        String[] path = request.getServletPath().split("/");
        /*
        System.out.println("###############CertificationInterceptor=getRequestURI=>" + request.getRequestURI());   //  /admin
        System.out.println("###############CertificationInterceptor=getContextPath=>" + request.getContextPath());
        System.out.println("###############CertificationInterceptor=getRequestURL=>" + request.getRequestURL()); // https://localhost/admin
        System.out.println("###############CertificationInterceptor=getServletPath=>" + request.getServletPath());
        */
        if(referrer != null) {
            if("/favicon.ico".equals(referrer)) {
                referrer = "";
            }else {
            	session.setAttribute(SpConstants.REFFRER_SESSION_KEY, referrer);
            }
        }else {
            referrer = "";
        }
        //System.out.println("############### request.getServletPath()=>/"+request.getServletPath());
        String[] arrPath = request.getServletPath().split("/");
        //System.out.println("############### arrPath.length=>/"+arrPath.length);
        //System.out.println("############### <<>>=arrPath=>"+arrPath[1]);
        
        if(arrPath.length > 1) {
        	// Admin 처리 
        	if("admin".equals(arrPath[1])) 
        	{
        		return adminHandle(sessionAdmData, response, session, arrPath);
            } 
        	// end Admin
        	// 데시보드 
        	else 
        	{
        		return sellerHandle(sessionData, arrPath, session, request, response, privatechk, activeYn);
            }  
        	//end 데시보드 
        }
        // 기타 처리 
        else {
        	return otherHandle(sessionData, response, session, privatechk, activeYn);
        }
        
    }
    
    // [1] Admin 처리 
    private boolean adminHandle(AdminData sessionAdmData, HttpServletResponse response,  HttpSession session,  String[] arrPath) throws Exception
    {
    	System.out.println(">>>>>>>>>>>>>> [1] >>>>>>>>>>>>>>>>>>>>>");
    	System.out.println("#### adminHandle " +"####");
    	//String auth = sessionAdmData.getAuthority();
    	
        if(ObjectUtils.isEmpty(sessionAdmData)){
            response.sendRedirect("/admin/login");
            return false;
        } else {
        	String auth = sessionAdmData.getAuthority();
        	
        	//최고 관리자의 경우 체크하지 않음
        	if(auth.indexOf("I010020")!= -1) {
//        		session.setMaxInactiveInterval(60*60*4);
                return true;
			}
        	
            //관리자 페이지 권한 확인
        	String temp_path = arrPath[1] +"/"+ arrPath[2];
        	if(temp_path.equals("admin/userInfo") || temp_path.equals("admin/common")) {
        		System.out.println("temp_path : " + temp_path);
        		return true;
        	} else if(temp_path.equals("admin/seller")) {
    			if(auth.indexOf("I010030")== -1) {
    				response.sendRedirect("/admin/login");
        			return false;
    			}                			
    		} else if(temp_path.equals("admin/cs")) {
    			if(auth.indexOf("I010040")== -1) {
    				response.sendRedirect("/admin/login");
        			return false;
    			}                			
    		} else if(temp_path.equals("admin/board")) {
    			if(auth.indexOf("I010050")== -1) {
    				response.sendRedirect("/admin/login");
        			return false;
    			}
    		} else if(temp_path.equals("admin/delivery") || temp_path.equals("admin/price") || temp_path.equals("admin/rank") || temp_path.equals("/admin/uploadPriceExcelFile")) {
    			if(auth.indexOf("I010060")== -1) {
    				response.sendRedirect("/admin/login");
        			return false;
    			}
    		} else if(temp_path.equals("admin/code")) {
    			if(auth.indexOf("I010070")== -1) {
    				response.sendRedirect("/admin/login");
        			return false;
    			}                			
    		} else if(temp_path.equals("admin/admin")) {
    			
    			if(auth.indexOf("I010080")== -1) {
    				response.sendRedirect("/admin/login");
        			return false;
    			}
    		} else if(temp_path.equals("admin/statis")) {
    			
    			if(auth.indexOf("I010090")== -1) {
    				response.sendRedirect("/admin/login");
        			return false;
    			}	
    		} else {
    			response.sendRedirect("/admin/login");
    			return false;
    		}
//        	session.setMaxInactiveInterval(60*60*4);
        	return true;
        	
        }
    }
    // [2] 셀러 처리 
    private boolean sellerHandle(ShopData sessionData,  String[] arrPath, HttpSession session, HttpServletRequest request, HttpServletResponse response, String privatechk, String activeYn) throws Exception
    {
    	System.out.println(">>>>>>>>>>>>>> [2] >>>>>>>>>>>>>>>>>>>>>");
    	System.out.println("#### sellerHandle arrPath[1] : " + arrPath[1] +"####");
    	if(arrPath[1].indexOf("shopify") >0) {
//    		session.setMaxInactiveInterval(60*60*4);
            return true;
    	}else {
    		System.out.println("#### CertificationInterceptor [user login 01] : " + ObjectUtils.isEmpty(sessionData));
        	
            if(ObjectUtils.isEmpty(sessionData)){
            	System.out.println("----------Session is Empty --------------");
            	//LOGGER.debug("############decode=>"+util.getAESDecrypt(SpConstants.ENCRYPT_KEY, util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sessionData.getAccess_token()))+"##############");
				//LOGGER.debug(sessionData.toString());
				session.getAttribute(SpConstants.HTTP_SESSION_KEY);
				session.getAttribute(SpConstants.TOKEN_SESSION_KEY);
				Boolean loginBool = false;
				javax.servlet.http.Cookie [] cookies = request.getCookies();
				if(cookies != null) {
					for(int i = 0 ; i < cookies.length ; i++) {
						javax.servlet.http.Cookie cookie = cookies[i];
						if(cookie.getValue() != null) {
							
							LOGGER.debug("cookie.getName(): " +cookie.getName() + "   " + "############cookie.getValue()#==>"+cookie.getValue());
							
							if(cookie.getName().equals(SpConstants.HTTP_SESSION_KEY)) {
								System.out.println("############# Session & Cookie Exist  ###############");
								ObjectMapper objectMapper =new ObjectMapper();
								String cookieVal = cookie.getValue();
								if(cookieVal.indexOf(" ") > 0) {
									cookieVal = cookieVal.replace(" ", "+");
								}
								try {
									String loginString = util.getAESDecrypt(SpConstants.FIND_PASSWD_KEY, cookieVal);
									LOGGER.debug("############loginString  #==>"+loginString);
									ShopData sd = objectMapper.readValue(loginString, ShopData.class);
									if(sd != null) {
										System.out.println(">>>>>>>>>>>>>> ShopData Not Null  >>>>>>>>>>>>");
										session.setAttribute(SpConstants.HTTP_SESSION_KEY, sd);
										sessionData = (ShopData) session.getAttribute(SpConstants.HTTP_SESSION_KEY);
//										session.setMaxInactiveInterval(60*60*4);
										response.sendRedirect("/");
										loginBool = true;
										System.out.println(">>>>>>>>>>>>>> Cookie Read Session setAttribute , loginBool is True >>>>>>>>>>>>");
									}
									else 
										System.out.println(">>>>>>>>>>>>>> ShopData is Null  >>>>>>>>>>>>");
								}catch(Exception e) {
									LOGGER.debug("############ sellerHandle Exception #==>"+e.getMessage());
									response.sendRedirect("/login");
								}
							}
							else
								System.out.println("############# Session에 쿠키 미존재 ###############");
						}
					}
					if(loginBool == false) {
						System.out.println("############# loginBool is false ###############");
						//response.sendRedirect("/login");
						System.out.println(">>>>>>>>>>>>>> Cookie is Exist, But Session ShopData Wrong..... >>>>>>>>>>>>");
						return false;
					}
					return loginBool;
				}else {
					response.sendRedirect("/login");
                    return false;
				}
                
            }else if(!privatechk.equals("Y")) {
            	System.out.println("----------Session is Not Empty --------------");
            	response.sendRedirect("/terms/selectTerms");
            	return false;
            }else {
//                session.setMaxInactiveInterval(60*60*4);
            	if(certificateSkip == false && !activeYn.equals("Y")) {
                	System.out.println("----------Session is Not Empty444 --------------"+certificateSkip);
                	response.sendRedirect("/terms/requestContact");
                	return false;
            	}       	
            	else{
            		System.out.println("----------Session is Not Empty 333 --------------");
            	  	LOGGER.debug("############loginString  #==>"+sessionData);
            	    return true;
            	}
            }
    	}
    	
    }
    // [3] 기타 요청 처리 
    private boolean otherHandle(ShopData sessionData, HttpServletResponse response, HttpSession session, String privatechk, String activeYn) throws Exception
    {
    	System.out.println(">>>>>>>>>>>>>> [3] >>>>>>>>>>>>>>>>>>>>>");
    	System.out.println("#### otherHandle  ShopData is Empty: " + ObjectUtils.isEmpty(sessionData) +"####");
    	
        if(ObjectUtils.isEmpty(sessionData)){
            response.sendRedirect("/login");
            return false;
        }else if(!privatechk.equals("Y")) {
        	response.sendRedirect("/terms/selectTerms");
        	return false;
        }else {
//          session.setMaxInactiveInterval(60*60*4);
      	if( certificateSkip==false && ! activeYn.equals("Y")) {
          	System.out.println("----------Session is Not Empty444 --------------certificateSkip:"+certificateSkip);
          	response.sendRedirect("/terms/requestContact");
          	return false;}
      	else{

      		return true;
      	}
        }
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    	HttpSession session = request.getSession();
    	
    	//System.out.println("##### postHandle #############################################################");
    }
 
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    	//System.out.println("##### afterCompletion #############################################################");
    }

	public void setCertificateSkip(boolean certificateSkip) {
		this.certificateSkip = certificateSkip;
	}
 
}