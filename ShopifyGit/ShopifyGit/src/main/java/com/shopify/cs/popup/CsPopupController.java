package com.shopify.cs.popup;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.admin.AdminData;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.cs.CsData;
import com.shopify.cs.CsService;
import com.shopify.mapper.CsMapper;
import com.shopify.shop.ShopData;

/**
 * 관리자 팝업 컨트롤러
 *
 */
@Controller
public class CsPopupController {
	
	@Autowired private CsService csService;
	@Autowired private CsPopupService csPopupService;
	@Autowired private UtilFn util;
	@Autowired private CsMapper csMapper;
	
	private Logger LOGGER = LoggerFactory.getLogger(CsPopupController.class);
	
	/**
	 * CS괸리 > 반송 상세보기 Popup 
	 * @return
	 */
	@GetMapping(value = "/cs/popup/backShowPop")
	public ModelAndView backShowPop(@ModelAttribute CsData csData, Model model,HttpServletRequest req, HttpSession sess) {
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		//csData.setPaymentCode(SpConstants.DELIVERY_EXTERNAL); // 국외
		String deliveryCode = csMapper.selectPaymentCode(csData); 	//국내(NA)/국외(ND)픽업 조회 [YR_2020.05.21]
		csData.setPaymentCode(deliveryCode);
		ModelAndView mav = new ModelAndView("cs/popup/popCsBackShow");
		
		Map<String, Object> map = csService.selectCsDeliveryView(csData, sess);
        
        model.addAttribute("list", map.get("list"));     //목록
        model.addAttribute("detail", map.get("detail")); //상세데이터
        model.addAttribute("status", "ok");
		
        return mav;
	}
	
	/**
	 * CS괸리 > 추가요금 상세보기 Popup 
	 * @return
	 */
	@GetMapping(value = "/cs/popup/paymentShowPop")
	public ModelAndView paymentShowPop(@ModelAttribute CsData csData, Model model,HttpServletRequest req, HttpSession sess) {
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		csData.setPaymentCode(SpConstants.DELIVERY_CHARGE); // 추가
		
		ModelAndView mav = new ModelAndView("cs/popup/popCsBackShow");
		
		Map<String, Object> map = csService.selectCsDeliveryView(csData, sess);
        
        model.addAttribute("list", map.get("list"));     //목록
        model.addAttribute("detail", map.get("detail")); //상세데이터
        model.addAttribute("status", "ok");
		
        return mav;
    }
	
	
	
	
	
	
	/**
	 * CS괸리 > 교환 상세보기 Popup 
	 * @return
	 */
	@GetMapping(value = "/cs/popup/exchangeShowPop")
	public ModelAndView exchangeShowPop(@ModelAttribute CsData csData, Model model,HttpServletRequest req, HttpSession sess) {
	 	CsPopupData csPopData = new CsPopupData();
        String locale = util.getUserLocale(sess); // 세션 다국어 받아 오기 
        @SuppressWarnings("unchecked")
        List<CsPopupData> csPopDataList = new ArrayList<CsPopupData>();
        List<CsPopupData> csPopDataListReturn = new ArrayList<CsPopupData>();
        ModelAndView mav = new ModelAndView("cs/popup/popCsExchangeShow");
        // 1. 배송과 관련된 출고지 저장 여부 확인
        // 2. 기본 출고지 저장 여부 확인 
        if(csData != null) {
            String masterCode = csData.getMasterCode();
            if(masterCode != null) {
                ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
                csData.setLocale(sessionData.getLocale());
                int cnt = csService.selectCsExchangeCount(csData, sess);
                if(cnt > 0) {
                	csData.setMasterCode(masterCode);
                	csData.setCurrentPage(0);
                	csData.setPageSize(10);
                	csData.setEmail(sessionData.getEmail());
                	csData.setLocale(locale);
                	csPopData.setMasterCode(masterCode);
                	csPopData.setEmail(sessionData.getEmail());
                    int cntDetail = 0;
                    
                    CsData sData = csService.selectCsExchangeDetail(csData);
                    
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
                        
                        DecimalFormat formatter = new DecimalFormat("###,###");
                        String paymentStr = formatter.format(payment);
                        sData.setPaymentStr(paymentStr);
                        String paymentToStringStr = formatter.format(paymentTotal);
                        sData.setPaymentTotalStr(paymentToStringStr);
                        String rankPriceStr = formatter.format(rankPrice);
                        sData.setRankPriceStr(rankPriceStr);
                        String quantityStr = formatter.format(quantity);
                        sData.setQuantityStr(quantityStr);
                        
                        cntDetail = csPopupService.selectPopCsExchangeSkuCount(csPopData);

                        if(cntDetail > 0) {
                        	//교환 관세정보 목록
                            csPopDataList = csPopupService.selectPopCsExchangeSkuList(csPopData, sess);
                            
                            //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                            for(CsPopupData list : csPopDataList) {
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
	@GetMapping(value = "/cs/popup/returnShowPop")
	public ModelAndView returnShowPop(@ModelAttribute CsData csData, Model model,HttpServletRequest req, HttpSession sess) {
	 	CsPopupData csPopData = new CsPopupData();
        String locale = util.getUserLocale(sess); // 세션 다국어 받아 오기 
        @SuppressWarnings("unchecked")
        List<CsPopupData> csPopDataList = new ArrayList<CsPopupData>();
        List<CsPopupData> csPopDataListReturn = new ArrayList<CsPopupData>();
        ModelAndView mav = new ModelAndView("cs/popup/popCsReturnShow");
        // 1. 배송과 관련된 출고지 저장 여부 확인
        // 2. 기본 출고지 저장 여부 확인 
        if(csData != null) {
            String masterCode = csData.getMasterCode();
            if(masterCode != null) {
                ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
                csData.setLocale(sessionData.getLocale());
                int cnt = csService.selectCsReturnCount(csData, sess);
                if(cnt > 0) {
                	csData.setMasterCode(masterCode);
                	csData.setCurrentPage(0);
                	csData.setPageSize(10);
                	csData.setEmail(sessionData.getEmail());
                	csData.setLocale(locale);
                	csPopData.setMasterCode(masterCode);
                	csPopData.setEmail(sessionData.getEmail());
                    int cntDetail = 0;
                    
                    CsData sData = csService.selectCsReturnDetail(csData);
                    
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
                        
                        DecimalFormat formatter = new DecimalFormat("###,###");
                        String paymentStr = formatter.format(payment);
                        sData.setPaymentStr(paymentStr);
                        String paymentToStringStr = formatter.format(paymentTotal);
                        sData.setPaymentTotalStr(paymentToStringStr);
                        String rankPriceStr = formatter.format(rankPrice);
                        sData.setRankPriceStr(rankPriceStr);
                        String quantityStr = formatter.format(quantity);
                        sData.setQuantityStr(quantityStr);
                        
                        cntDetail = csPopupService.selectPopCsReturnSkuCount(csPopData);

                        if(cntDetail > 0) {
                        	//반품 관세정보 목록
                            csPopDataList = csPopupService.selectPopCsReturnSkuList(csPopData, sess);
                            
                            //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                            for(CsPopupData list : csPopDataList) {
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
