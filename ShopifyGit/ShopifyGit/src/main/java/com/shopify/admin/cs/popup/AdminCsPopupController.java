package com.shopify.admin.cs.popup;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.admin.AdminData;
import com.shopify.admin.cs.AdminCsData;
import com.shopify.admin.cs.AdminCsService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.cs.CsData;
import com.shopify.cs.popup.CsPopupData;
import com.shopify.shop.ShopData;

/**
 * 관리자 팝업 컨트롤러
 *
 */
@Controller
public class AdminCsPopupController {
	
	@Autowired private AdminCsService adminCsService;
	@Autowired private AdminCsPopupService adminCsPopupService;
	@Autowired private UtilFn util;
	
	private Logger LOGGER = LoggerFactory.getLogger(AdminCsPopupController.class);
	
	/**
	 * CS괸리 > 배송 상세보기 Popup 
	 * @return
	 */
	@GetMapping(value = "/admin/cs/popup/adminDeliveryShowPop")
	public ModelAndView adminDeliveryShowPop(@ModelAttribute AdminCsData csData, Model model,HttpServletRequest req, HttpSession sess) {
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String locale = sd.getLocale(); // 세션 다국어 받아 오기 
		csData.setLocale(locale);
		csData.setPaymentCode(SpConstants.DELIVERY_EXTERNAL); // 국외
		
		ModelAndView mav = new ModelAndView("admin/cs/popup/popAdminCsDeliveryShow");
		
		Map<String, Object> map = adminCsService.selectAdminCsDeliveryView(csData);
        
        model.addAttribute("list", map.get("list"));     //목록
        model.addAttribute("detail", map.get("detail")); //상세데이터
        model.addAttribute("status", "ok");
        
        return mav;
    }
	
	/**
	 * CS괸리 > 추가요금 상세보기 Popup 
	 * @return
	 */
	@GetMapping(value = "/admin/cs/popup/adminPaymentShowPop")
	public ModelAndView adminPaymentShowPop(@ModelAttribute AdminCsData csData, Model model,HttpServletRequest req, HttpSession sess) {
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String locale = sd.getLocale(); // 세션 다국어 받아 오기 
		csData.setLocale(locale);
		csData.setPaymentCode(SpConstants.DELIVERY_CHARGE); // 추가
		
		ModelAndView mav = new ModelAndView("admin/cs/popup/popAdminCsDeliveryShow");
		
		Map<String, Object> map = adminCsService.selectAdminCsDeliveryView(csData);
        
        model.addAttribute("list", map.get("list"));     //목록
        model.addAttribute("detail", map.get("detail")); //상세데이터
        model.addAttribute("status", "ok");
        
        return mav;
    }	
	
	
	
	
	
	/**
	 * CS괸리 > 반송 상세보기 Popup 
	 * @return
	 */
	@GetMapping(value = "/admin/cs/popup/adminBackShowPop")
	public ModelAndView adminBackShowPop(@ModelAttribute AdminCsData csData, Model model,HttpServletRequest req, HttpSession sess) {
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminCsPopupData csPopData = new AdminCsPopupData();
        String locale = sd.getLocale(); // 세션 다국어 받아 오기 
        @SuppressWarnings("unchecked")
        List<AdminCsPopupData> csPopDataList = new ArrayList<AdminCsPopupData>();
        List<AdminCsPopupData> csPopDataListReturn = new ArrayList<AdminCsPopupData>();
        ModelAndView mav = new ModelAndView("admin/cs/popup/popAdminCsBackShow");
        // 1. 배송과 관련된 출고지 저장 여부 확인
        // 2. 기본 출고지 저장 여부 확인 
        LOGGER.debug("###############1aaaaaaaaaaaa=csData=>/"+csData);
        if(csData != null) {
            String masterCode = csData.getMasterCode();
            if(masterCode != null) {
                csData.setLocale(sd.getLocale());
                int cnt = adminCsService.selectAdminCsBackCount(csData, sess);
                if(cnt > 0) {
                	csData.setMasterCode(masterCode);
                	csData.setCurrentPage(0);
                	csData.setPageSize(10);
                	csData.setLocale(locale);
                	csPopData.setMasterCode(masterCode);
                    int cntDetail = 0;
                    
                    AdminCsData sData = adminCsService.selectAdminCsBackDetail(csData);
                    
                    //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                    if(sData != null) {
                        String sgetBuyerFirstname = sData.getBuyerFirstname();
                        if(sgetBuyerFirstname != null)    sgetBuyerFirstname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerFirstname);
                        else sgetBuyerFirstname = "";
                        sData.setBuyerFirstname(sgetBuyerFirstname);

                        String sgetBuyerLastname = sData.getBuyerLastname();
                        if(sgetBuyerLastname != null)    sgetBuyerLastname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerLastname);
                        else sgetBuyerLastname = "";
                        sData.setBuyerLastname(sgetBuyerLastname);
                        
                        String sgetBuyerPhone = sData.getBuyerPhone();
                        if(sgetBuyerPhone != null)         sgetBuyerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerPhone) ;
                        else sgetBuyerPhone = "";
                        sData.setBuyerPhone(sgetBuyerPhone);
                        
                        
                        String sgetSellerName = sData.getSellerName();
                        if(sgetSellerName != null)    sgetSellerName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetSellerName);
                        else sgetSellerName = "";
                        sData.setSellerName(sgetSellerName);
                        
                        String sgetSellerPhone = sData.getSellerPhone();
                        if(sgetSellerPhone != null)         sgetSellerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetSellerPhone) ;
                        else sgetSellerPhone = "";
                        sData.setSellerPhone(sgetSellerPhone);
                        
                        String sgetCustomerName = sData.getCustomerName();
                        if(sgetCustomerName != null) sgetCustomerName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetCustomerName) ;
                        else sgetCustomerName = "";
                        sData.setCustomerName(sgetCustomerName);
                        
                        int payment = sData.getPayment();
                        int paymentTotal = sData.getPaymentTotal();
                        int rankPrice = sData.getRankPrice();
                        int quantity = sData.getQuantity();
                        int deliveryAmount = sData.getDeliveryAmount(); 
                        
                        DecimalFormat formatter = new DecimalFormat("###,###");
                        String paymentStr = formatter.format(payment);
                        sData.setPaymentStr(paymentStr);
                        String paymentToStringStr = formatter.format(paymentTotal);
                        sData.setPaymentTotalStr(paymentToStringStr);
                        String rankPriceStr = formatter.format(rankPrice);
                        sData.setRankPriceStr(rankPriceStr);
                        String quantityStr = formatter.format(quantity);
                        sData.setQuantityStr(quantityStr);
                        String deliveryAmountStr = formatter.format(deliveryAmount);
                        sData.setDeliveryAmountStr(deliveryAmountStr);
                        
                        cntDetail = adminCsPopupService.selectPopAdminCsBackSkuCount(csPopData);
                        if(cntDetail > 0) {
                        	//반송 관세정보 목록
                            csPopDataList = adminCsPopupService.selectPopAdminCsBackSkuList(csPopData, sess);
                            
                            //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                            for(AdminCsPopupData list : csPopDataList) {
                                String getBuyerFirstname       = list.getBuyerFirstname();
                                if(getBuyerFirstname != null)    getBuyerFirstname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerFirstname);
                                else getBuyerFirstname = "";
                                list.setBuyerFirstname(getBuyerFirstname);
                                
                                String getBuyerLastname       = list.getBuyerLastname();
                                if(getBuyerLastname != null)    getBuyerLastname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerLastname);
                                else getBuyerLastname = "";
                                list.setBuyerLastname(getBuyerLastname);
                                
                                String getBuyerPhone            = list.getBuyerPhone();
                                if(getBuyerPhone != null)         getBuyerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerPhone) ;
                                else getBuyerPhone = "";
                                list.setBuyerLastname(getBuyerPhone);
                                
                                payment = list.getPayment();
                                paymentTotal = list.getPaymentTotal();
                                rankPrice = list.getRankPrice();
                                
                                paymentStr = formatter.format(payment);
                                list.setPaymentStr(paymentStr);
                                paymentToStringStr = formatter.format(paymentTotal);
                                list.setPaymentTotalStr(paymentToStringStr);
                                rankPriceStr = formatter.format(rankPrice);
                                list.setRankPriceStr(rankPriceStr);
                                quantityStr = formatter.format(quantity);
                                list.setQuantityStr(quantityStr);
                                int unitCost = list.getUnitCost();
                                String unitCostStr = formatter.format(unitCost);
                                list.setUnitCostStr(unitCostStr);
                                
                                csPopDataListReturn.add(list);
                            }
                            
                            LOGGER.debug("###############1aaaaaaaaaaaa=selectShipmentDetail=>/"+sData);
                        }
                        
                    }
                    model.addAttribute("listCnt", cntDetail);                   //목록의 데이터개수
                    model.addAttribute("list", csPopDataListReturn);     //목록
                    model.addAttribute("detail", sData);                        //상세데이터
                    model.addAttribute("status", "ok");
                }else {
                
                    model.addAttribute("status", "non");
                }
                
            }else {
                model.addAttribute("status", "no");
            }
        }else {
            model.addAttribute("status", "no");
        }
        return mav;
    }
		
		
	
	/**
	 * CS괸리 > 교환 상세보기 Popup 
	 * @return
	 */
	@GetMapping(value = "/admin/cs/popup/adminExchangeShowPop")
	public ModelAndView adminExchangeShowPop(@ModelAttribute AdminCsData csData, Model model,HttpServletRequest req, HttpSession sess) {
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminCsPopupData csPopData = new AdminCsPopupData();
        String locale = sd.getLocale(); // 세션 다국어 받아 오기 
        @SuppressWarnings("unchecked")
        List<AdminCsPopupData> csPopDataList = new ArrayList<AdminCsPopupData>();
        List<AdminCsPopupData> csPopDataListReturn = new ArrayList<AdminCsPopupData>();
        ModelAndView mav = new ModelAndView("admin/cs/popup/popAdminCsExchangeShow");
        // 1. 배송과 관련된 출고지 저장 여부 확인
        // 2. 기본 출고지 저장 여부 확인 
        LOGGER.debug("###############1aaaaaaaaaaaa=csData=>/"+csData);
        if(csData != null) {
            String masterCode = csData.getMasterCode();
            if(masterCode != null) {
                csData.setLocale(sd.getLocale());
                int cnt = adminCsService.selectAdminCsExchangeCount(csData, sess);
                if(cnt > 0) {
                	csData.setMasterCode(masterCode);
                	csData.setCurrentPage(0);
                	csData.setPageSize(10);
                	csData.setLocale(locale);
                	csPopData.setMasterCode(masterCode);
                    int cntDetail = 0;
                    
                    AdminCsData sData = adminCsService.selectAdminCsExchangeDetail(csData);
                    
                    //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                    if(sData != null) {
                        String sgetBuyerFirstname = sData.getBuyerFirstname();
                        if(sgetBuyerFirstname != null)    sgetBuyerFirstname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerFirstname);
                        else sgetBuyerFirstname = "";
                        sData.setBuyerFirstname(sgetBuyerFirstname);

                        String sgetBuyerLastname = sData.getBuyerLastname();
                        if(sgetBuyerLastname != null)    sgetBuyerLastname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerLastname);
                        else sgetBuyerLastname = "";
                        sData.setBuyerLastname(sgetBuyerLastname);
                        
                        String sgetBuyerPhone = sData.getBuyerPhone();
                        if(sgetBuyerPhone != null)         sgetBuyerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerPhone) ;
                        else sgetBuyerPhone = "";
                        sData.setBuyerPhone(sgetBuyerPhone);
                        
                        
                        String sgetSellerName = sData.getSellerName();
                        if(sgetSellerName != null)    sgetSellerName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetSellerName);
                        else sgetSellerName = "";
                        sData.setSellerName(sgetSellerName);
                        
                        String sgetSellerPhone = sData.getSellerPhone();
                        if(sgetSellerPhone != null)         sgetSellerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetSellerPhone) ;
                        else sgetSellerPhone = "";
                        sData.setSellerPhone(sgetSellerPhone);
                        
                        String sgetCustomerName = sData.getCustomerName();
                        if(sgetCustomerName != null) sgetCustomerName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetCustomerName) ;
                        else sgetCustomerName = "";
                        sData.setCustomerName(sgetCustomerName);
                        
                        int payment = sData.getPayment();
                        int paymentTotal = sData.getPaymentTotal();
                        int rankPrice = sData.getRankPrice();
                        int quantity = sData.getQuantity();
                        int deliveryAmount = sData.getDeliveryAmount(); 
                        
                        DecimalFormat formatter = new DecimalFormat("###,###");
                        String paymentStr = formatter.format(payment);
                        sData.setPaymentStr(paymentStr);
                        String paymentToStringStr = formatter.format(paymentTotal);
                        sData.setPaymentTotalStr(paymentToStringStr);
                        String rankPriceStr = formatter.format(rankPrice);
                        sData.setRankPriceStr(rankPriceStr);
                        String quantityStr = formatter.format(quantity);
                        sData.setQuantityStr(quantityStr);
                        String deliveryAmountStr = formatter.format(deliveryAmount);
                        sData.setDeliveryAmountStr(deliveryAmountStr);
                        
                        cntDetail = adminCsPopupService.selectPopAdminCsExchangeSkuCount(csPopData);
                        if(cntDetail > 0) {
                        	//반송 관세정보 목록
                            csPopDataList = adminCsPopupService.selectPopAdminCsExchangeSkuList(csPopData, sess);
                            
                            //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                            for(AdminCsPopupData list : csPopDataList) {
                                String getBuyerFirstname       = list.getBuyerFirstname();
                                if(getBuyerFirstname != null)    getBuyerFirstname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerFirstname);
                                else getBuyerFirstname = "";
                                list.setBuyerFirstname(getBuyerFirstname);
                                
                                String getBuyerLastname       = list.getBuyerLastname();
                                if(getBuyerLastname != null)    getBuyerLastname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerLastname);
                                else getBuyerLastname = "";
                                list.setBuyerLastname(getBuyerLastname);
                                
                                String getBuyerPhone            = list.getBuyerPhone();
                                if(getBuyerPhone != null)         getBuyerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerPhone) ;
                                else getBuyerPhone = "";
                                list.setBuyerLastname(getBuyerPhone);
                                
                                payment = list.getPayment();
                                paymentTotal = list.getPaymentTotal();
                                rankPrice = list.getRankPrice();
                                
                                paymentStr = formatter.format(payment);
                                list.setPaymentStr(paymentStr);
                                paymentToStringStr = formatter.format(paymentTotal);
                                list.setPaymentTotalStr(paymentToStringStr);
                                rankPriceStr = formatter.format(rankPrice);
                                list.setRankPriceStr(rankPriceStr);
                                quantityStr = formatter.format(quantity);
                                list.setQuantityStr(quantityStr);
                                int unitCost = list.getUnitCost();
                                String unitCostStr = formatter.format(unitCost);
                                list.setUnitCostStr(unitCostStr);
                                
                                csPopDataListReturn.add(list);
                            }
                            
                        }
                        
                    }
                    model.addAttribute("listCnt", cntDetail);                   //목록의 데이터개수
                    model.addAttribute("list", csPopDataListReturn);     //목록
                    model.addAttribute("detail", sData);                        //상세데이터
                    model.addAttribute("status", "ok");
                }else {
                
                    model.addAttribute("status", "non");
                }
                
            }else {
                model.addAttribute("status", "no");
            }
        }else {
            model.addAttribute("status", "no");
        }
        return mav;
    }
		
	
	/**
	 * CS괸리 > 반품 상세보기 Popup 
	 * @return
	 */
	@GetMapping(value = "/admin/cs/popup/adminReturnShowPop")
	public ModelAndView adminReturnShowPop(@ModelAttribute AdminCsData csData, Model model,HttpServletRequest req, HttpSession sess) {
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminCsPopupData csPopData = new AdminCsPopupData();
        String locale = sd.getLocale(); // 세션 다국어 받아 오기 
        @SuppressWarnings("unchecked")
        List<AdminCsPopupData> csPopDataList = new ArrayList<AdminCsPopupData>();
        List<AdminCsPopupData> csPopDataListReturn = new ArrayList<AdminCsPopupData>();
        ModelAndView mav = new ModelAndView("admin/cs/popup/popAdminCsReturnShow");
        // 1. 배송과 관련된 출고지 저장 여부 확인
        // 2. 기본 출고지 저장 여부 확인 
        LOGGER.debug("###############1aaaaaaaaaaaa=csData=>/"+csData);
        if(csData != null) {
            String masterCode = csData.getMasterCode();
            if(masterCode != null) {
                csData.setLocale(sd.getLocale());
                int cnt = adminCsService.selectAdminCsReturnCount(csData, sess);
                if(cnt > 0) {
                	csData.setMasterCode(masterCode);
                	csData.setCurrentPage(0);
                	csData.setPageSize(10);
                	csData.setLocale(locale);
                	csPopData.setMasterCode(masterCode);
                    int cntDetail = 0;
                    
                    AdminCsData sData = adminCsService.selectAdminCsReturnDetail(csData);
                    
                    //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                    if(sData != null) {
                        String sgetBuyerFirstname = sData.getBuyerFirstname();
                        if(sgetBuyerFirstname != null)    sgetBuyerFirstname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerFirstname);
                        else sgetBuyerFirstname = "";
                        sData.setBuyerFirstname(sgetBuyerFirstname);

                        String sgetBuyerLastname = sData.getBuyerLastname();
                        if(sgetBuyerLastname != null)    sgetBuyerLastname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerLastname);
                        else sgetBuyerLastname = "";
                        sData.setBuyerLastname(sgetBuyerLastname);
                        
                        String sgetBuyerPhone = sData.getBuyerPhone();
                        if(sgetBuyerPhone != null)         sgetBuyerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerPhone) ;
                        else sgetBuyerPhone = "";
                        sData.setBuyerPhone(sgetBuyerPhone);
                        
                        
                        String sgetSellerName = sData.getSellerName();
                        if(sgetSellerName != null)    sgetSellerName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetSellerName);
                        else sgetSellerName = "";
                        sData.setSellerName(sgetSellerName);
                        
                        String sgetSellerPhone = sData.getSellerPhone();
                        if(sgetSellerPhone != null)         sgetSellerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetSellerPhone) ;
                        else sgetSellerPhone = "";
                        sData.setSellerPhone(sgetSellerPhone);
                        
                        String sgetCustomerName = sData.getCustomerName();
                        if(sgetCustomerName != null) sgetCustomerName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetCustomerName) ;
                        else sgetCustomerName = "";
                        sData.setCustomerName(sgetCustomerName);
                        
                        int payment = sData.getPayment();
                        int paymentTotal = sData.getPaymentTotal();
                        int rankPrice = sData.getRankPrice();
                        int quantity = sData.getQuantity();
                        int deliveryAmount = sData.getDeliveryAmount(); 
                        
                        DecimalFormat formatter = new DecimalFormat("###,###");
                        String paymentStr = formatter.format(payment);
                        sData.setPaymentStr(paymentStr);
                        String paymentToStringStr = formatter.format(paymentTotal);
                        sData.setPaymentTotalStr(paymentToStringStr);
                        String rankPriceStr = formatter.format(rankPrice);
                        sData.setRankPriceStr(rankPriceStr);
                        String quantityStr = formatter.format(quantity);
                        sData.setQuantityStr(quantityStr);
                        String deliveryAmountStr = formatter.format(deliveryAmount);
                        sData.setDeliveryAmountStr(deliveryAmountStr);
                        
                        cntDetail = adminCsPopupService.selectPopAdminCsReturnSkuCount(csPopData);
                        if(cntDetail > 0) {
                        	//반송 관세정보 목록
                            csPopDataList = adminCsPopupService.selectPopAdminCsReturnSkuList(csPopData, sess);
                            
                            //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                            for(AdminCsPopupData list : csPopDataList) {
                                String getBuyerFirstname       = list.getBuyerFirstname();
                                if(getBuyerFirstname != null)    getBuyerFirstname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerFirstname);
                                else getBuyerFirstname = "";
                                list.setBuyerFirstname(getBuyerFirstname);
                                
                                String getBuyerLastname       = list.getBuyerLastname();
                                if(getBuyerLastname != null)    getBuyerLastname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerLastname);
                                else getBuyerLastname = "";
                                list.setBuyerLastname(getBuyerLastname);
                                
                                String getBuyerPhone            = list.getBuyerPhone();
                                if(getBuyerPhone != null)         getBuyerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerPhone) ;
                                else getBuyerPhone = "";
                                list.setBuyerLastname(getBuyerPhone);
                                
                                payment = list.getPayment();
                                paymentTotal = list.getPaymentTotal();
                                rankPrice = list.getRankPrice();
                                
                                paymentStr = formatter.format(payment);
                                list.setPaymentStr(paymentStr);
                                paymentToStringStr = formatter.format(paymentTotal);
                                list.setPaymentTotalStr(paymentToStringStr);
                                rankPriceStr = formatter.format(rankPrice);
                                list.setRankPriceStr(rankPriceStr);
                                quantityStr = formatter.format(quantity);
                                list.setQuantityStr(quantityStr);
                                int unitCost = list.getUnitCost();
                                String unitCostStr = formatter.format(unitCost);
                                list.setUnitCostStr(unitCostStr);
                                
                                csPopDataListReturn.add(list);
                            }
                            
                        }
                        
                    }
                    model.addAttribute("listCnt", cntDetail);                   //목록의 데이터개수
                    model.addAttribute("list", csPopDataListReturn);     //목록
                    model.addAttribute("detail", sData);                        //상세데이터
                    model.addAttribute("status", "ok");
                }else {
                
                    model.addAttribute("status", "non");
                }
                
            }else {
                model.addAttribute("status", "no");
            }
        }else {
            model.addAttribute("status", "no");
        }
        return mav;
    }
		
	
	
	
}
