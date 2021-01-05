package com.shopify.payment.popup;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.admin.delivery.DeliveryService;
import com.shopify.common.SpConstants;
import com.shopify.common.ZXingHelper;
import com.shopify.common.util.UtilFn;
import com.shopify.order.OrderData;
import com.shopify.seller.SellerData;
import com.shopify.seller.SellerService;
import com.shopify.shipment.popup.ShipmentPopupData;
import com.shopify.shipment.ShipmentData;
import com.shopify.payment.PaymentService;
import com.shopify.shop.ShopData;
import com.shopify.shop.ShopService;


@RestController
@RequestMapping("/payment/popup")
public class PaymentPopupApiController{
    private Logger LOGGER = LoggerFactory.getLogger(PaymentPopupApiController.class);
    @Autowired private PaymentService paymentService;
    @Autowired private PaymentPopupService paymentPopupService;
    @Autowired private UtilFn util;
    
    /**
     * 배송 > 팝업 > api용 결제페이지내 사용하는 method
     * @return ModelAndView(jsonView)
     */
    @PostMapping(value="/apiPaymentPayment", produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView apiPaymentPayment(@RequestBody ShipmentData ship, Model model,HttpServletRequest req, HttpSession sess) {
    	ModelAndView mav = new ModelAndView("jsonView");
        // 1. 배송과 관련된 출고지 저장 여부 확인
        // 2. 기본 출고지 저장 여부 확인 
        LOGGER.debug("###############popPaymentPayment=ship=>/"+ship);
        //LOGGER.debug("###############popPaymentPayment=ship=>/"+ship.getMasterCodeList());
        if(ship != null) {
        	String masterCode = ship.getMasterCode();
            if(masterCode != null) {
                if(masterCode.indexOf(",") > 0) {
                	String[] masterCodeList = masterCode.split(",");
                	ship.setMasterCodeList(masterCodeList);
                	ship.setMasterCode("");
                	//LOGGER.debug("###############popPaymentPayment=ship.getMasterCodeList()=>/"+ship.getMasterCodeList().toString());
                	LOGGER.debug("###############popPaymentPayment=ship.getMasterCodeList()=>/"+ship.getMasterCodeList().length);
                	LOGGER.debug("###############popPaymentPayment=ship.getMasterCode()=>/"+ship.getMasterCode());
                	model = getPaymentPopupDetailMasterCode(ship, model, req, sess);
                }else {
                	model = getPaymentPopupDetailMasterCode(ship, model, req, sess);
                } 
                
                
//                LOGGER.debug("###############1aaaaaaaaaaaa=shop=>/"+shop);
            }else {
                model.addAttribute("status", "no");
            }
        }else {
            model.addAttribute("status", "no");
        }
        return mav;
    }
    
    /**
     * 배송 > 팝업 > 결제페이지내 사용하는 method
     * @return ModelAndView(jsonView)
     */
    private Model getPaymentPopupDetailMasterCode(ShipmentData ship, Model model,  HttpServletRequest req, HttpSession sess) {
    	ShipmentPopupData shipPopData = new ShipmentPopupData();
        @SuppressWarnings("unchecked")
        List<ShipmentPopupData> shipPopDataList = new ArrayList<ShipmentPopupData>();
        List<ShipmentPopupData> shipPopDataListReturn = new ArrayList<ShipmentPopupData>();
        String masterCode = ship.getMasterCode();
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        int cnt = paymentService.selectPaymentCount(ship, sess);
        if(cnt > 0) {
			ship.setCurrentPage(0);
			ship.setPageSize(10);
			ship.setEmail(sessionData.getEmail());
			ship.setLocale(sessionData.getLocale());
			shipPopData.setEmail(sessionData.getEmail());
			shipPopData.setLocale(sessionData.getLocale());
			if(ship.getMasterCodeList() != null && ship.getMasterCodeList().length > 0) {
				shipPopData.setMasterCodeList(ship.getMasterCodeList());
				shipPopData.setMasterCode("");
            	//LOGGER.debug("#####getPaymentPopupDetailMasterCode=shipPopData.getMasterCodeList()=>/"+shipPopData.getMasterCodeList());
            	LOGGER.debug("#####getPaymentPopupDetailMasterCode=shipPopData.getMasterCode()=>/"+shipPopData.getMasterCode());

			}else {
				shipPopData.setMasterCode(masterCode);
			}
			shipPopData.setEmail(sessionData.getEmail());
            
            int cntDetail = 0;
            
            ShipmentData sData = paymentService.selectPaymentDetail(ship);
            LOGGER.debug("###############popPaymentPayment=sData=>/"+sData);
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
                
                int payment = sData.getPayment();
                int paymentTotal = sData.getPaymentTotal();
                int rankPrice = sData.getRankPrice();
                
                DecimalFormat formatter = new DecimalFormat("###,###");
                String paymentStr = formatter.format(payment);
                sData.setPaymentStr(paymentStr);
                String paymentToStringStr = formatter.format(paymentTotal);
                sData.setPaymentTotalStr(paymentToStringStr);
                String rankPriceStr = formatter.format(rankPrice);
                sData.setRankPriceStr(rankPriceStr);
                
                
                cntDetail = paymentPopupService.selectPopPaymentCount(shipPopData);
                if(cntDetail > 0) {
                    shipPopDataList = paymentPopupService.selectPopPaymentList(shipPopData, sess);
                    LOGGER.debug("###############popPaymentPayment=shipPopDataList=>/"+shipPopDataList);
                    
                  //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                    for(ShipmentPopupData list : shipPopDataList) {
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
                        
                        rankPrice = list.getRankPrice();
                        payment = list.getPayment();
                        paymentTotal = list.getPaymentTotal();
                        
                        rankPriceStr = formatter.format(rankPrice);
                        list.setRankPriceStr(rankPriceStr);
                        paymentStr = formatter.format(payment);
                        list.setPaymentStr(paymentStr);
                        paymentToStringStr = formatter.format(paymentTotal);
                        list.setPaymentTotalStr(paymentToStringStr);
                        
                        shipPopDataListReturn.add(list);
                    }
                    
                    LOGGER.debug("###############popPaymentPayment=shipPopDataListReturn=>/"+shipPopDataListReturn);
                }
                
            }
            model.addAttribute("listCnt", cntDetail);                   //목록의 데이터개수
            model.addAttribute("list", shipPopDataListReturn);     //목록
            model.addAttribute("detail", sData);                        //상세데이터
            model.addAttribute("status", "ok");
        }else {
        
            model.addAttribute("status", "non");
        }
        return model;
    }
    
    
    /**
     * 배송 > 배송비 호출
     * @return int
     */
    @PostMapping(value="/apiPaymentDeliveryAmount", produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView apiPaymentDeliveryAmount(@RequestBody ShipmentPopupData shipPopData, Model model,HttpServletRequest req, HttpSession sess) {
    	ModelAndView mav = new ModelAndView("jsonView");
    	/*shipPopData.setWeight(weightTotal);
		shipPopData.setCourier(sData.getCourier());
		shipPopData.setCourierCompany(sData.getCourierCompany());
		shipPopData.setOrigin(sData.getOrigin());*/
    	if(shipPopData != null) {
    		int deliveryAmount = paymentPopupService.selectPaymentPrice(shipPopData);
        	int deliveryAmountVat = (int)Math.floor(deliveryAmount * 0.1);
        
        	model.addAttribute("deliveryAmount", deliveryAmount);
        	model.addAttribute("deliveryAmountVat", deliveryAmountVat);
    	}else {
    		model.addAttribute("deliveryAmount", 0);
        	model.addAttribute("deliveryAmountVat", 0);
    	}
    	    
	   
	    return mav;
    }
    

}