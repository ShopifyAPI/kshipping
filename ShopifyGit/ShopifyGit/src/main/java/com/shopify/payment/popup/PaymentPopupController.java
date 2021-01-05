package com.shopify.payment.popup;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.api.LotteLogisApiService;
import com.shopify.common.SpConstants;
import com.shopify.common.ZXingHelper;
import com.shopify.common.util.UtilFn;
import com.shopify.payinfo.PayinfoData;
import com.shopify.payinfo.PayinfoService;
import com.shopify.shipment.ShipmentData;
import com.shopify.payment.PaymentService;
import com.shopify.shipment.popup.LocalDeliveryPaymentData;
import com.shopify.shipment.popup.ShipmentPopupData;
import com.shopify.shipment.popup.ShipmentPopupDataLocalData;
import com.shopify.shop.ShopData;


@Controller
@RequestMapping("/payment/popup")
public class PaymentPopupController{
	@Value("${file.upload-dir}")			String fileDir;
	
    private Logger LOGGER = LoggerFactory.getLogger(PaymentPopupController.class);
    @Autowired private PaymentService paymentService;
    @Autowired private PaymentPopupService paymentPopupService;
    @Autowired private UtilFn util;
    @Autowired private LotteLogisApiService lotteLogisApiService;
    @Autowired private PayinfoService payinfoService;
    
    //바코드 popup
    @RequestMapping(value="/popBacordPrint")
    public ModelAndView popBacordPrint(@ModelAttribute ShipmentPopupData shipPopData, Model model, HttpServletRequest req, HttpSession sess) {
        ModelAndView mav = new ModelAndView("payment/popup/popBacordPrint");
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        if(sessionData != null) {
        	shipPopData.setLocale(sessionData.getLocale());
        	shipPopData.setShopIdx(sessionData.getShopIdx());
            shipPopData.setEmail(sessionData.getEmail());
        }
        
        List<ShipmentPopupData> invoiceList = new ArrayList<ShipmentPopupData>();
        if(shipPopData != null) {
            String masterCode = shipPopData.getMasterCode();
            
            if(masterCode != null) {
                if(masterCode.indexOf(",") > 0) {
                    String[] codeAry = masterCode.split(",");
                    //LOGGER.debug("###############1aaaaaaaaaaaa=orderIdx=>/"+orderIdxChk);
                    shipPopData.setMasterCode("");
                    shipPopData.setMasterCodeList(codeAry);
                    invoiceList = paymentPopupService.selectPopPaymentDeliveryArrayList(shipPopData, sess);
                    LOGGER.debug("###############1aaaaaaaaaaaa=orderIdxList=>/"+invoiceList);
                    model.addAttribute("status", "array");
                }else {
                    invoiceList = paymentPopupService.selectPopPaymentDeliveryArrayList(shipPopData, sess);
                    model.addAttribute("status", "single");
                }
                model.addAttribute("invoiceList", invoiceList);
                
            }else {
                model.addAttribute("status", "non");
            }
        }else {
            model.addAttribute("status", "non");
        }
        return mav;
    }


    //배송-상세 popup
    @RequestMapping(value="/popDetailPayment")
    public ModelAndView popDetailPayment(@ModelAttribute ShipmentData ship, Model model,HttpServletRequest req, HttpSession sess) {
        ShipmentPopupData shipPopData = new ShipmentPopupData();
        String locale = util.getUserLocale(sess); // 세션 다국어 받아 오기 
        @SuppressWarnings("unchecked")
        List<ShipmentPopupData> shipPopDataList = new ArrayList<ShipmentPopupData>();
        List<ShipmentPopupData> shipPopDataListReturn = new ArrayList<ShipmentPopupData>();
        ModelAndView mav = new ModelAndView("payment/popup/popDetailPayment");
        // 1. 배송과 관련된 출고지 저장 여부 확인
        // 2. 기본 출고지 저장 여부 확인 
        LOGGER.debug("###############1aaaaaaaaaaaa=ship=>/"+ship);
        if(ship != null) {
            String masterCode = ship.getMasterCode();
            
            if(masterCode != null) {
                ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
                ship.setLocale(sessionData.getLocale());
                ship.setEmail(sessionData.getEmail());
                int cnt = paymentService.selectPaymentCount(ship, sess);
                if(cnt > 0) {
                    ship.setMasterCode(masterCode);
                    ship.setCurrentPage(0);
                    ship.setPageSize(10);
                    ship.setLocale(locale);
                    shipPopData.setLocale(locale);
                    shipPopData.setMasterCode(masterCode);
                    shipPopData.setEmail(sessionData.getEmail());
                    int cntDetail = 0;
                    
                    LOGGER.debug("#########popDetailPayment######selectPaymentDetail=ship=>/"+ship);
                    ShipmentData sData = paymentService.selectPaymentDetail(ship);
                    LOGGER.debug("#########popDetailPayment#####111#selectPaymentDetail=sData=>/"+sData);
                    
                  //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                    if(sData != null) {
                        String sgetBuyerFirstname = sData.getBuyerFirstname();
                        if(sgetBuyerFirstname != null)    sgetBuyerFirstname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerFirstname);
                        else sgetBuyerFirstname = "";
                        sData.setBuyerFirstname(util.nullToEmpty(sgetBuyerFirstname));

                        String sgetBuyerLastname = sData.getBuyerLastname();
                        if(sgetBuyerLastname != null)    sgetBuyerLastname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerLastname);
                        else sgetBuyerLastname = "";
                        sData.setBuyerLastname(util.nullToEmpty(sgetBuyerLastname));
                        
                        String sgetBuyerPhone = sData.getBuyerPhone();
                        if(sgetBuyerPhone != null)         sgetBuyerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerPhone) ;
                        else sgetBuyerPhone = "";
                        sData.setBuyerPhone(util.nullToEmpty(sgetBuyerPhone));
                        
                        
                        String sgetSellerName = sData.getSellerName();
                        if(sgetSellerName != null)    sgetSellerName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetSellerName);
                        else sgetSellerName = "";
                        sData.setSellerName(util.nullToEmpty(sgetSellerName));
                        
                        String sgetSellerPhone = sData.getSellerPhone();
                        if(sgetSellerPhone != null)         sgetSellerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetSellerPhone) ;
                        else sgetSellerPhone = "";
                        sData.setSellerPhone(util.nullToEmpty(sgetSellerPhone));
                        
                        String sgetCustomerName = sData.getCustomerName();
                        if(sgetCustomerName != null) sgetCustomerName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetCustomerName) ;
                        else sgetCustomerName = "";
                        sData.setCustomerName(util.nullToEmpty(sgetCustomerName));
                        
                        int payment = sData.getPayment();
                        int paymentTotal = sData.getPaymentTotal();
                        int rankPrice = sData.getRankPrice();
                        int quantity = sData.getQuantity();
                        double orderPrice = sData.getOrderPrice();
                        double weight = sData.getWeight();
                        double unitCost = 0;
                        String unitCostStr="";
                        
                        DecimalFormat formatter = new DecimalFormat("###,###");
                        String paymentStr = formatter.format(payment);
                        sData.setPaymentStr(paymentStr);
                        String paymentToStringStr = formatter.format(paymentTotal);
                        sData.setPaymentTotalStr(paymentToStringStr);
                        String rankPriceStr = formatter.format(rankPrice);
                        sData.setRankPriceStr(rankPriceStr);
                        String quantityStr = formatter.format(quantity);
                        sData.setQuantityStr(quantityStr);
                        String orderPriceStr = formatter.format(orderPrice);
                        sData.setOrderPriceStr(orderPriceStr);
                        String weightStr = formatter.format(weight);
                        sData.setWeightStr(weightStr);
                        
                        
                        LOGGER.debug("###############popDetailPayment=sData=>/"+sData);
                        //cntDetail = paymentPopupService.selectPopPaymentSkuCount(shipPopData);
                        String combineCode = sData.getCombineCode();
                        
                        LOGGER.debug("###############popDetailPayment=combineCode=>/"+combineCode);
                       	cntDetail = paymentPopupService.selectPopPaymentCount(shipPopData);
                        if(cntDetail > 0) {
                            //shipPopDataList = paymentPopupService.selectPopPaymentSkuList(shipPopData, sess);
                        	if("Y".equals(combineCode)) {	//합배송시 별도 쿼리
                        		shipPopDataList = paymentPopupService.selectPopPaymentCombineList(shipPopData, sess);
                            }else {
                            	shipPopDataList = paymentPopupService.selectPopPaymentList(shipPopData, sess);
                            }
                        	
                        	LOGGER.debug("###############popDetailPayment=shipPopDataList=>/"+shipPopDataList);
                        	
                            //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                            for(ShipmentPopupData list : shipPopDataList) {
                                String getBuyerFirstname       = list.getBuyerFirstname();
                                if(getBuyerFirstname != null)    getBuyerFirstname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerFirstname);
                                else getBuyerFirstname = "";
                                list.setBuyerFirstname(util.nullToEmpty(getBuyerFirstname));
                                
                                String getBuyerLastname       = list.getBuyerLastname();
                                if(getBuyerLastname != null)    getBuyerLastname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerLastname);
                                else getBuyerLastname = "";
                                list.setBuyerLastname(util.nullToEmpty(getBuyerLastname));
                                
                                String getBuyerPhone            = list.getBuyerPhone();
                                if(getBuyerPhone != null)         getBuyerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerPhone) ;
                                else getBuyerPhone = "";
                                list.setBuyerLastname(util.nullToEmpty(getBuyerPhone));
                                
                                payment = list.getPayment();
                                paymentTotal = list.getPaymentTotal();
                                rankPrice = list.getRankPrice();
                                quantity = list.getQuantity();
                                unitCost = list.getUnitCost();
                                weight = list.getWeight();
                                
                                paymentStr = formatter.format(payment);
                                paymentStr = paymentStr + util.getLocaleChangeStr(sess,"원","won");
                                list.setPaymentStr(paymentStr);
                                paymentToStringStr = formatter.format(paymentTotal);
                                list.setPaymentTotalStr(paymentToStringStr);
                                rankPriceStr = formatter.format(rankPrice);
                                list.setRankPriceStr(rankPriceStr);
                                quantityStr = formatter.format(quantity);
                               	quantityStr = quantityStr + util.getLocaleChangeStr(sess,"개","ea");
                               	list.setQuantityStr(quantityStr);
                               	weightStr = formatter.format(weight);
                               	weightStr = weightStr + list.getWeightUnit();
                               	list.setWeightStr(weightStr);                                
                                unitCostStr=formatter.format(unitCost);
                                list.setUnitCostStr(unitCostStr);
                                
                                shipPopDataListReturn.add(list);
                            }
                            
                            LOGGER.debug("###############1aaaaaaaaaaaa=selectPaymentDetail=shipPopDataListReturn==>>/"+shipPopDataListReturn);
                        }
                        
                    }
                    model.addAttribute("listCnt", cntDetail);                   //목록의 데이터개수
                    model.addAttribute("list", shipPopDataListReturn);     //목록
                    model.addAttribute("detail", sData);                        //상세데이터
                    model.addAttribute("status", "ok");
                }else {
                
                    model.addAttribute("status", "non");
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
     * 배송 > 팝업 > 결제페이지
     * @return ModelAndView(jsonView)
     */
    @RequestMapping(value="/popPaymentPayment")
    public ModelAndView popPaymentPayment(@RequestParam(name = "masterCode") String masterCode, 
    									  @RequestParam(name = "krCnt") int krCnt, 
    										Model model,HttpServletRequest req, HttpSession sess) {
        ModelAndView mav = new ModelAndView("payment/popup/popPaymentPayment");
        // 1. 배송과 관련된 출고지 저장 여부 확인
        // 2. 기본 출고지 저장 여부 확인 
        ShipmentData ship = new ShipmentData();
        
        //LOGGER.debug("###############popPaymentPayment=ship=>/"+ship.getMasterCodeList());
        if(masterCode != null) {
            if(masterCode.indexOf(",") > 0) {
            	String[] masterCodeList = masterCode.split(",");
            	ship.setMasterCodeList(masterCodeList);
            	ship.setMasterCode("");
            } else {
            	ship.setMasterCode(masterCode);
            }
            model = getPaymentPopupDetailMasterCode(ship, krCnt, model, req, sess);
        }else {
            model.addAttribute("status", "no");
        }
        
        return mav;
    }
    /**
     * 배송 > 팝업 > 결제페이지내 사용하는 method
     * @param krCnt 
     * @return ModelAndView(jsonView)
     */
    private Model getPaymentPopupDetailMasterCode(ShipmentData ship, int krCnt, Model model,  HttpServletRequest req, HttpSession sess) {
    	
    	LOGGER.debug("<<<<<<<<<<<<<<<<<<getPaymentPopupDetailMasterCode>>>>>>>>>>>>>>>>>>>>>>>" + ship);
    	ShipmentPopupData shipPopData = new ShipmentPopupData();
        @SuppressWarnings("unchecked")
        List<ShipmentPopupData> shipPopDataList = new ArrayList<ShipmentPopupData>();
        List<ShipmentPopupData> shipPopDataListReturn = new ArrayList<ShipmentPopupData>();
        String masterCode = ship.getMasterCode();
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        ship.setLocale(sessionData.getLocale());
        ship.setEmail(sessionData.getEmail());
        
        int cnt = paymentService.selectPaymentCount(ship, sess);
        
        if(cnt > 0) {
			ship.setCurrentPage(0);
			ship.setPageSize(10);
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
            LOGGER.debug("#########getPaymentPopupDetailMasterCode######ship=>/"+ship);
            ShipmentData sData = paymentService.selectPaymentDetail(ship);
            LOGGER.debug("###############popPaymentPayment=sData=>/"+sData);
            LOGGER.debug("## getDeliveryAmount: " + sData.getDeliveryAmount());
            LOGGER.debug("## getRankPrice: " + sData.getRankPrice());
            // 조한두 테스트 
            if(sData.getMasterCode() != null && shipPopData.getMasterCode() == null)
            {
            	shipPopData.setMasterCode(sData.getMasterCode());
            	LOGGER.debug("## ERROR MASTER_CODE CASE ##");
            }
            String combineCode = sData.getCombineCode();
            //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
            if(sData != null) {
                String sgetBuyerFirstname = sData.getBuyerFirstname();
                if(sgetBuyerFirstname != null)    sgetBuyerFirstname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerFirstname);
                else sgetBuyerFirstname = "";
                sData.setBuyerFirstname(util.nullToEmpty(sgetBuyerFirstname));

                String sgetBuyerLastname = sData.getBuyerLastname();
                if(sgetBuyerLastname != null)    sgetBuyerLastname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerLastname);
                else sgetBuyerLastname = "";
                sData.setBuyerLastname(util.nullToEmpty(sgetBuyerLastname));

                String sgetBuyerPhone = sData.getBuyerPhone();
                if(sgetBuyerPhone != null)         sgetBuyerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sgetBuyerPhone) ;
                else sgetBuyerPhone = "";
                sData.setBuyerPhone(util.nullToEmpty(sgetBuyerPhone));
                
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
                
                //cntDetail = paymentPopupService.selectPopPaymentSkuCount(shipPopData);
                cntDetail = paymentPopupService.selectPopPaymentCount(shipPopData);
                if(cntDetail > 0) {                	
                	 if("Y".equals(combineCode)) { //합배송시 별도 쿼리 
					  shipPopDataList = paymentPopupService.selectPopPaymentCombineList(shipPopData, sess); }
					  else {
					  shipPopDataList = paymentPopupService.selectPopPaymentList(shipPopData,
					  sess); }
					 //shipPopDataList = paymentPopupService.selectPopPaymentSkuList(shipPopData, sess);
                    LOGGER.debug("###############popPaymentPayment=selectPopPaymentSkuList=>/"+shipPopDataList);
                    
                  //데이터 호출하여 가공처리(ex 복호화, 숫자 콤마처리)
                    for(ShipmentPopupData list : shipPopDataList) {
                        String getBuyerFirstname       = list.getBuyerFirstname();
                        if(getBuyerFirstname != null)    getBuyerFirstname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerFirstname);
                        else getBuyerFirstname = "";
                        list.setBuyerFirstname(util.nullToEmpty(getBuyerFirstname));
                        
                        String getBuyerLastname       = list.getBuyerLastname();
                        if(getBuyerLastname != null)    getBuyerLastname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerLastname);
                        else getBuyerLastname = "";
                        list.setBuyerLastname(util.nullToEmpty(getBuyerLastname));
                        
                        String getBuyerPhone            = list.getBuyerPhone();
                        if(getBuyerPhone != null)         getBuyerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, getBuyerPhone) ;
                        else getBuyerPhone = "";
                        list.setBuyerPhone(util.nullToEmpty(getBuyerPhone));
                        
                        rankPrice = list.getRankPrice();
                        payment = list.getPayment() - rankPrice;
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
            
            
            LocalDeliveryPaymentData pikData = new LocalDeliveryPaymentData();
            pikData.setLocale(sessionData.getLocale());
            
            if ( krCnt == 0 ) {
            	// 국내 택배 요금 리스트(롯데)
            	pikData.setId("B010020");
            	pikData.setCode("L_PICKUP");
            	List<LocalDeliveryPaymentData> lglPickup = paymentPopupService.selectLocalDeliveryPaymentList(pikData, sess);
            	// 우체국 
            	pikData.setId("B010010");
            	pikData.setCode("P_PICKUP");
            	List<LocalDeliveryPaymentData> postPickup = paymentPopupService.selectLocalDeliveryPaymentList(pikData, sess);
            	//판토스 
            	pikData.setId("B010030");
            	pikData.setCode("T_PICKUP");
            	List<LocalDeliveryPaymentData> pantosPickup = paymentPopupService.selectLocalDeliveryPaymentList(pikData, sess);
            	
            	model.addAttribute("lglPickup", lglPickup);            // 롯데 로직스 국내 픽업
            	model.addAttribute("postPickup", postPickup);          // 우체국 국내 픽업
            	model.addAttribute("pantosPickup", pantosPickup);     // 판토스 국내 픽업
            }
            
            // 배송비 관리자 셋팅 값 (조한두)
            String payMethodSetting = paymentPopupService.selectAdminPayMethod(sessionData.getEmail());
            sData.setPayMethodSetting(payMethodSetting);
            
            model.addAttribute("listCnt", cntDetail);                   //목록의 데이터개수
            model.addAttribute("list", shipPopDataListReturn);     //목록
            model.addAttribute("detail", sData);                        //상세데이터
            model.addAttribute("krCnt", krCnt);
            model.addAttribute("status", "ok");
        }else {
        
            model.addAttribute("status", "non");
        }
        return model;
    }
    
    
    /**
     * 배송 > 팝업 > 결제처리
     * @return ModelAndView(jsonView)
     */
    @PostMapping(value="/popPaymentPaymentProc")
    public ModelAndView popPaymentPaymentProc(@RequestBody ShipmentPopupData shipPopData, Model model, HttpServletRequest req, HttpServletResponse res, HttpSession sess) {
        ModelAndView mav = new ModelAndView("jsonView");
        LOGGER.debug("###############popPaymentPaymentProc=shipPopData=>/"+shipPopData);
        ModelAndView spData = paymentPopupService.popPaymentPaymentProc(shipPopData, model, req, res, sess);
		/*LOGGER.debug("###############1aaaaaaaaaaaa=confirmUrl=>/"+spData.getModel().get("confirmUrl"));
		LOGGER.debug("###############1aaaaaaaaaaaa=result=>/"+spData.getModel().get("result"));*/
        model.addAttribute("confirmUrl", spData.getModel().get("confirmUrl"));
        model.addAttribute("result", spData.getModel().get("result"));
        model.addAttribute("errCode", spData.getModel().get("errCode"));
        model.addAttribute("errMsg", spData.getModel().get("errMsg"));

        return mav;
    }
    
    
    /**
     * 배송 > 팝업 > 결제승인 팝업
     * @return ModelAndView(jsonView)
     */
    @PostMapping(value="/popPaymentPopOpen")
    public ModelAndView popPaymentPopOpen(@RequestBody ShipmentPopupData shipPopData, Model model, HttpServletRequest req, HttpServletResponse res, HttpSession sess) {
        ModelAndView mav = new ModelAndView("jsonView");
        ModelAndView spData = paymentPopupService.popPaymentPopOpen(shipPopData, model, req, res, sess);
		/*LOGGER.debug("###############1aaaaaaaaaaaa=confirmUrl=>/"+spData.getModel().get("confirmUrl"));
		LOGGER.debug("###############1aaaaaaaaaaaa=result=>/"+spData.getModel().get("result"));*/
        model.addAttribute("confirmUrl", spData.getModel().get("confirmUrl"));
        model.addAttribute("result", spData.getModel().get("result"));
        model.addAttribute("errCode", spData.getModel().get("errCode"));
        model.addAttribute("errMsg", spData.getModel().get("errMsg"));

        return mav;
    }
    
   

    
    

    @RequestMapping("/popCreateBarcode/{code}")
    public void popCreateBarcode(@PathVariable("code") String code, HttpServletResponse response) throws Exception {
        if(code == null) code = "";
        //LOGGER.debug("###############1aaaaaaaaaaaa=code=>/"+code);
		/* if(!"".equals(code)){
		    int codeCnt = code.length();
		    if(codeCnt < 16) {
		        String codeStr = "";
		        for(int c=0; c<codeCnt; c++) {
		            codeStr += "0";
		        }
		        code = codeStr + code;
		    }else if(codeCnt > 16) {
		        code = code.substring(0,15);
		    }
		}else {
		    code = "0000000000000000";
		}*/
        String codeStr = "";
        String textStr = "";
        LOGGER.debug("###############2aaaaaaaaaaaa=code.indexOf(\",\")=>/"+code.indexOf(","));
        if(code.indexOf(",") > 0) {
        	String[] codeAry = code.split(",");
        	codeStr = codeAry[0];
        	textStr = codeAry[1];
        	if(textStr.length() > 10 ) {
        		textStr = textStr.substring(0,10);
        	}
        	LOGGER.debug("###############2aaaaaaaaaaaa=codeStr=>/"+codeStr);
        	LOGGER.debug("###############2aaaaaaaaaaaa=textStr=>/"+textStr);
        }else {
        	codeStr = code;
        }
        //codeStr = codeStr.substring(0,7);
        //codeStr = "*"+codeStr+"*";
        
        LOGGER.debug("###############2aaaaaaaaaaaa=code=>/"+code);
        response.setContentType("image/png");
        
        /* 바코드 타입 
    	 * "codabar", "code39", "postnet", "intl2of5", "ean-128"
    	 * "royal-mail-cbc", "ean-13", "itf-14", "datamatrix", "code128"
    	 * "pdf417", "upc-a", "upc-e", "usps4cb", "ean-8", "ean-13" */
        String barcodeType = "code39";
    	/* 바코드 데이터 */
    	String barcodeData = codeStr;
    	
    	/* 이미지의 dpi */
    	final int dpi = 200;
    	
    	/* 이미지 파일 포맷 
    	 * SVG, EPS, TIFF, JPEG, PNG, GIF, BMP */
    	String fileFormat = "png";
    	
    	/* 출력될 파일 */
    	String dir = fileDir;
    	String fileName = "barcode_"+barcodeType;
    	String outputFile = dir +"/"+ fileName+"."+fileFormat;
    	
    	/* anti-aliasing */
    	boolean isAntiAliasing = false;
    	
    	/* 이미지 생성 */
    	ZXingHelper.getBarCodeImg(barcodeData, textStr, barcodeType, fileFormat, isAntiAliasing, dpi, outputFile, 290, 90, response);
    	//createBarcode(barcodeType, barcodeData, fileFormat, isAntiAliasing, dpi, outputFile, response);
    	
        //ZXingHelper.getBarCodeImage(codeStr, textStr, 290, 90,response);
//      OutputStream outputStream = response.getOutputStream();
//      outputStream.write(ZXingHelper.getBarCodeImage("0123456789123456", 200, 20));
//      outputStream.flush();
//      outputStream.close();
    }
    
    @GetMapping("/popPaymentChange")
    public ModelAndView popPaymentChange(Model model, HttpSession sess) throws Exception {
        ModelAndView mav = new ModelAndView("payment/popup/popPaymentChange");
        
        
        
        return mav;
    }
    
    @PostMapping("/popPaymentChangeProc")
    public ModelAndView popPaymentChangeProc(@RequestBody ShipmentPopupData shipPopData, Model model, HttpSession sess) throws Exception {
        ModelAndView mav = new ModelAndView("jsonView");
        
        int result = paymentPopupService.updatePopPaymentStat(shipPopData);
        
        LOGGER.debug("popPaymentChangeProc : " + result);
        
        mav.addObject("result", result);
        mav.addObject("errCode", true);
        mav.addObject("errMsg", "성공");
        
        return mav;
    }

}