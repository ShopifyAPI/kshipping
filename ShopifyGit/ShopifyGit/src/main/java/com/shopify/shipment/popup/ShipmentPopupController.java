package com.shopify.shipment.popup;

import java.io.ByteArrayInputStream;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.api.lotte.delivery.LotteDeliveryService;
import com.shopify.api.pantos.PantosService;
import com.shopify.common.RestService;
import com.shopify.common.SpConstants;
import com.shopify.common.ZXingHelper;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.ShipmentMapper;
import com.shopify.shipment.ShipmentData;
import com.shopify.shipment.ShipmentService;
import com.shopify.shop.ShopData;


@Controller
@RequestMapping("/shipment/popup")
public class ShipmentPopupController{
	@Value("${file.upload-dir}")			String fileDir;
	
	String COURIER_COMPANY_PANTOS = "B010030";			//판토스
	String COURIER_COMPANY_LOTTELOGIS = "B010020";			//롯데 로지스
	
	
    private Logger LOGGER = LoggerFactory.getLogger(ShipmentPopupController.class);
    @Autowired private ShipmentService shipmentService;
    @Autowired private ShipmentPopupService shipmentPopupService;
    @Autowired private UtilFn util;
    @Autowired private PantosService pantosApiService;
    @Autowired private LotteDeliveryService lotteDeliveryService;
	@Autowired	private RestService restService;
	@Autowired private ShipmentMapper shipmentMapper;
    
	
    @RequestMapping(value = "/popBacordPdfPrint", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> popBacordPdfPrint(@ModelAttribute ShipmentPopupData shipPopData, Model model, HttpServletRequest req, HttpSession sess) {

        ByteArrayInputStream bis = lotteDeliveryService.generateLotteWaybill(shipPopData.getMasterCode());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=barcode.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    
    
    
    //바코드 popup
    @RequestMapping(value="/popBacordPrint")
    public ModelAndView popBacordPrint(@ModelAttribute ShipmentPopupData shipPopData, Model model, HttpServletRequest req, HttpSession sess) {
        ModelAndView mav = new ModelAndView("shipment/popup/popBacordPrint");
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        if(sessionData != null) {
        	shipPopData.setLocale(sessionData.getLocale());
        	shipPopData.setShopIdx(sessionData.getShopIdx());
            shipPopData.setEmail(sessionData.getEmail());
        }
        
        List<ShipmentPopupData> invoiceList = new ArrayList<ShipmentPopupData>();
        int nCount = 0;
        if(shipPopData != null) {
            String masterCode = shipPopData.getMasterCode();
            
            if(masterCode != null) {
                if(masterCode.indexOf(",") > 0) {
                    String[] codeAry = masterCode.split(",");
                    //LOGGER.debug("###############1aaaaaaaaaaaa=orderIdx=>/"+orderIdxChk);
                    shipPopData.setMasterCode("");
                    shipPopData.setMasterCodeList(codeAry);
                    invoiceList = shipmentPopupService.selectPopShipmentDeliveryArrayList(shipPopData, sess);
                    LOGGER.debug("###############1aaaaaaaaaaaa=orderIdxList=>/"+invoiceList);
                    model.addAttribute("status", "array");
                }else {
                    invoiceList = shipmentPopupService.selectPopShipmentDeliveryArrayList(shipPopData, sess);
                    model.addAttribute("status", "single");
                }
                if(invoiceList != null)
                	nCount = invoiceList.size();
                model.addAttribute("invoiceList", invoiceList);
                model.addAttribute("invoicesize", nCount);
            }else {
                model.addAttribute("status", "non");
            }
        }else {
            model.addAttribute("status", "non");
        }
        return mav;
    }
    
    //바코드 popup : 관리자용 (조한두)
    @RequestMapping(value="/popBacordPrintAdmin")
    public ModelAndView popBacordPrintAdmin(@ModelAttribute ShipmentPopupData shipPopData, Model model, HttpServletRequest req, HttpSession sess) {
        ModelAndView mav = new ModelAndView("payment/popup/popBacordPrint");
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
//        if(sessionData != null) {
//        	shipPopData.setLocale(sessionData.getLocale());
//        	shipPopData.setShopIdx(sessionData.getShopIdx());
//            shipPopData.setEmail(sessionData.getEmail());
//        }
        LOGGER.debug(">>>popBacordPrintAdmin  ShipmentPopupData: " + shipPopData);
        List<ShipmentPopupData> invoiceList = new ArrayList<ShipmentPopupData>();
        int nCount = 0;
        if(shipPopData != null) {
            String masterCode = shipPopData.getMasterCode();
            
            if(masterCode != null) {
                if(masterCode.indexOf(",") > 0) {
                    String[] codeAry = masterCode.split(",");
                    //LOGGER.debug("###############1aaaaaaaaaaaa=orderIdx=>/"+orderIdxChk);
                    shipPopData.setMasterCode("");
                    shipPopData.setMasterCodeList(codeAry);
                    invoiceList = shipmentPopupService.selectPopShipmentDeliveryArrayList(shipPopData, sess);
                    LOGGER.debug("###############1aaaaaaaaaaaa=orderIdxList=>/"+invoiceList);
                    model.addAttribute("status", "array");
                }else {
                	 invoiceList = shipmentPopupService.selectPopShipmentDeliveryArrayList(shipPopData, sess);
                    model.addAttribute("status", "single");
                }
                if(invoiceList != null)
                	nCount = invoiceList.size();
                model.addAttribute("invoiceList", invoiceList);
                model.addAttribute("invoicesize", nCount);
                
            }else {
                model.addAttribute("status", "non");
            }
        }else {
            model.addAttribute("status", "non");
        }
        return mav;
    }


    //배송-상세 popup
    @RequestMapping(value="/popDetailShipment")
    public ModelAndView popDetailShipment(@ModelAttribute ShipmentData ship, Model model,HttpServletRequest req, HttpSession sess) {
        ShipmentPopupData shipPopData = new ShipmentPopupData();
        String locale = util.getUserLocale(sess); // 세션 다국어 받아 오기 
        @SuppressWarnings("unchecked")
        List<ShipmentPopupData> shipPopDataList = new ArrayList<ShipmentPopupData>();
        List<ShipmentPopupData> shipPopDataListReturn = new ArrayList<ShipmentPopupData>();
        ModelAndView mav = new ModelAndView("shipment/popup/popDetailShipment");
        // 1. 배송과 관련된 출고지 저장 여부 확인
        // 2. 기본 출고지 저장 여부 확인 
        LOGGER.debug("###############1aaaaaaaaaaaa=ship=>/"+ship);
        if(ship != null) {
            String masterCode = ship.getMasterCode();
            
            if(masterCode != null) {
                ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
                ship.setLocale(sessionData.getLocale());
                ship.setEmail(sessionData.getEmail());
                int cnt = shipmentService.selectShipmentCount(ship, sess);
                if(cnt > 0) {
                    ship.setMasterCode(masterCode);
                    ship.setCurrentPage(0);
                    ship.setPageSize(10);
                    ship.setLocale(locale);
                    shipPopData.setLocale(locale);
                    shipPopData.setMasterCode(masterCode);
                    shipPopData.setEmail(sessionData.getEmail());
                    int cntDetail = 0;
                    
                    LOGGER.debug("#########popDetailShipment######selectShipmentDetail=ship=>/"+ship);
                    ShipmentData sData = shipmentService.selectShipmentDetail(ship);
                    LOGGER.debug("#########popDetailShipment#####111#selectShipmentDetail=sData=>/"+sData);
                    
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
                        
                        
                        LOGGER.debug("###############popDetailShipment=sData=>/"+sData);
                        //cntDetail = shipmentPopupService.selectPopShipmentSkuCount(shipPopData);
                        String combineCode = sData.getCombineCode();
                        
                        LOGGER.debug("###############popDetailShipment=combineCode=>/"+combineCode);
                       	cntDetail = shipmentPopupService.selectPopShipmentCount(shipPopData);
                        if(cntDetail > 0) {
                            //shipPopDataList = shipmentPopupService.selectPopShipmentSkuList(shipPopData, sess);
                        	if("Y".equals(combineCode)) {	//합배송시 별도 쿼리
                        		shipPopDataList = shipmentPopupService.selectPopShipmentCombineList(shipPopData, sess);
                            }else {
                            	shipPopDataList = shipmentPopupService.selectPopShipmentList(shipPopData, sess);
                            }
                        	
                        	LOGGER.debug("###############popDetailShipment=shipPopDataList=>/"+shipPopDataList);
                        	
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
                            
                            LOGGER.debug("###############1aaaaaaaaaaaa=selectShipmentDetail=shipPopDataListReturn==>>/"+shipPopDataListReturn);
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
    @RequestMapping(value="/popPaymentShipment")
    public ModelAndView popPaymentShipment(@ModelAttribute ShipmentData ship, Model model,HttpServletRequest req, HttpSession sess) {
        ModelAndView mav = new ModelAndView("shipment/popup/popPaymentShipment");
        // 1. 배송과 관련된 출고지 저장 여부 확인
        // 2. 기본 출고지 저장 여부 확인 
        LOGGER.debug("###############popPaymentShipment=ship=>/"+ship);
        //LOGGER.debug("###############popPaymentShipment=ship=>/"+ship.getMasterCodeList());
        if(ship != null) {
        	String masterCode = ship.getMasterCode();
            if(masterCode != null) {
                if(masterCode.indexOf(",") > 0) {
                	String[] masterCodeList = masterCode.split(",");
                	ship.setMasterCodeList(masterCodeList);
                	ship.setMasterCode("");
                	//LOGGER.debug("###############popPaymentShipment=ship.getMasterCodeList()=>/"+ship.getMasterCodeList().toString());
                	LOGGER.debug("###############popPaymentShipment=ship.getMasterCodeList()=>/"+ship.getMasterCodeList().length);
                	LOGGER.debug("###############popPaymentShipment=ship.getMasterCode()=>/"+ship.getMasterCode());
                	model = getShipmentPopupDetailMasterCode(ship, model, req, sess);
                }else {
                	model = getShipmentPopupDetailMasterCode(ship, model, req, sess);
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
    private Model getShipmentPopupDetailMasterCode(ShipmentData ship, Model model,  HttpServletRequest req, HttpSession sess) {
    	ShipmentPopupData shipPopData = new ShipmentPopupData();
        @SuppressWarnings("unchecked")
        List<ShipmentPopupData> shipPopDataList = new ArrayList<ShipmentPopupData>();
        List<ShipmentPopupData> shipPopDataListReturn = new ArrayList<ShipmentPopupData>();
        String masterCode = ship.getMasterCode();
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        ship.setLocale(sessionData.getLocale());
        ship.setEmail(sessionData.getEmail());
        int cnt = shipmentService.selectShipmentCount(ship, sess);
        if(cnt > 0) {
			ship.setCurrentPage(0);
			ship.setPageSize(10);
			shipPopData.setEmail(sessionData.getEmail());
			shipPopData.setLocale(sessionData.getLocale());
			if(ship.getMasterCodeList() != null && ship.getMasterCodeList().length > 0) {
				shipPopData.setMasterCodeList(ship.getMasterCodeList());
				shipPopData.setMasterCode("");
            	//LOGGER.debug("#####getShipmentPopupDetailMasterCode=shipPopData.getMasterCodeList()=>/"+shipPopData.getMasterCodeList());
            	LOGGER.debug("#####getShipmentPopupDetailMasterCode=shipPopData.getMasterCode()=>/"+shipPopData.getMasterCode());

			}else {
				shipPopData.setMasterCode(masterCode);
			}
			shipPopData.setEmail(sessionData.getEmail());
            
            int cntDetail = 0;
            LOGGER.debug("#########getShipmentPopupDetailMasterCode######ship=>/"+ship);
            ShipmentData sData = shipmentService.selectShipmentDetail(ship);
            LOGGER.debug("###############popPaymentShipment=sData=>/"+sData);
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
                
                //cntDetail = shipmentPopupService.selectPopShipmentSkuCount(shipPopData);
                cntDetail = shipmentPopupService.selectPopShipmentCount(shipPopData);
                if(cntDetail > 0) {
                	if("Y".equals(combineCode)) {	//합배송시 별도 쿼리
                		shipPopDataList = shipmentPopupService.selectPopShipmentCombineList(shipPopData, sess);
                    }else {
                    	shipPopDataList = shipmentPopupService.selectPopShipmentList(shipPopData, sess);
                    }
                    //shipPopDataList = shipmentPopupService.selectPopShipmentSkuList(shipPopData, sess);
                    LOGGER.debug("###############popPaymentShipment=selectPopShipmentSkuList=>/"+shipPopDataList);
                    
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
                    
                    LOGGER.debug("###############popPaymentShipment=shipPopDataListReturn=>/"+shipPopDataListReturn);
                }
                
            }
            
            // 국내 택배 요금 리스트(롯데)
            LocalDeliveryPaymentData pikData = new LocalDeliveryPaymentData();
            pikData.setId("B010020");
            pikData.setLocale(sessionData.getLocale());
            pikData.setCode("L_PICKUP");
            List<LocalDeliveryPaymentData> lglPickup = shipmentPopupService.selectLocalDeliveryPaymentList(pikData, sess);
            
            pikData.setId("B010010");
            pikData.setCode("P_PICKUP");
            List<LocalDeliveryPaymentData> postPickup = shipmentPopupService.selectLocalDeliveryPaymentList(pikData, sess);
            
            model.addAttribute("listCnt", cntDetail);                   //목록의 데이터개수
            model.addAttribute("list", shipPopDataListReturn);     //목록
            model.addAttribute("detail", sData);                        //상세데이터
            model.addAttribute("lglPickup", lglPickup);            // 롯데 로직스 국내 픽업
            model.addAttribute("postPickup", postPickup);          // 우체국 국내 픽업
            model.addAttribute("status", "ok");
        }else {
        
            model.addAttribute("status", "non");
        }
        return model;
    }
    
    
    /**
     * 배송 > 팝업 > 배송처리
     * @return ModelAndView(jsonView)
     */
    @PostMapping(value="/popPaymentShipmentProc")
    public ModelAndView popPaymentShipmentProc(@RequestBody List<ShipmentPopupData> shipPopDataList, Model model, HttpServletRequest req, HttpSession sess) {
     
    	ModelAndView mav = new ModelAndView("jsonView");
    	
    	Map dataMap = shipmentPopupService.getDeliveryByCompanys(shipPopDataList);
    	

    	
    	if(dataMap.get(COURIER_COMPANY_PANTOS) != null ) {
    		List<ShipmentPopupData> list = (List<ShipmentPopupData>) dataMap.get(COURIER_COMPANY_PANTOS);
    		Map rsMap =pantosApiService.popPaymentShipmentProc(list, sess);
    		model.addAttribute("result", rsMap.get("result"));
    		model.addAttribute("errCode", rsMap.get("errCode"));
    		model.addAttribute("errMsg", rsMap.get("errMsg"));
    	}
    	
    	if(dataMap.get(COURIER_COMPANY_LOTTELOGIS) != null ) {
    		List<ShipmentPopupData> list = (List<ShipmentPopupData>) dataMap.get(COURIER_COMPANY_LOTTELOGIS);
    		ModelAndView spData = null;
    		String countryCode = null;
    		int size = list.size();
    		if ( size > 0 ) {
    			ShipmentPopupData data = list.get(0);
    			countryCode = ((ShipmentPopupData)list.get(0)).getBuyerCountryCode();
    		}
    		
    		if(countryCode.equals("KR")) {
    			LOGGER.debug("국내택배 배송신청*****");
    			
    			Map rsMap = popPaymentShipmentProcCaller(list);
//    			Map rsMap = lotteDeliveryApiService.popPaymentShipmentProc(list,sess);
//    			model.addAttribute("result", rsMap.get("result"));
        		model.addAttribute("errCode", rsMap.get("errCode"));
        		model.addAttribute("errMsg", rsMap.get("errMsg"));

    		} else { 
    			spData = shipmentPopupService.popPaymentShipmentProc(list, model, req, sess);
    			model.addAttribute("result", spData.getModelMap().get("result"));
        		model.addAttribute("errCode", spData.getModelMap().get("errCode"));
        		model.addAttribute("errMsg", spData.getModelMap().get("errMsg"));
    		}
    		
    		
    	}

    	List<String> masterCodeList = new ArrayList<>();
    	for(ShipmentPopupData shipPopupData : shipPopDataList) {
    		masterCodeList.add(shipPopupData.getMasterCode());
    	}
    	
    	int date = shipmentMapper.updateShipmentData(masterCodeList);
    	
//        ModelAndView spData = shipmentPopupService.popPaymentShipmentProc(shipPopDataList, model, req, sess);
//        model.addAttribute("result", spData.getModelMap().get("result"));
//        model.addAttribute("errCode", spData.getModelMap().get("errCode"));
//        model.addAttribute("errMsg", spData.getModelMap().get("errMsg"));
        
        return mav;
    }
    
    
	public Map<String, Object> popPaymentShipmentProcCaller(List<ShipmentPopupData> list) {
		
//		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
//		String locale = sessionData.getLocale();
//		String email = sessionData.getEmail();
		HttpHeaders headers = restService.composeLotteHeaders(true);
		
		StringBuilder buffer = new StringBuilder();
		String result;
		
		//----------------------------------------------
		// master code 1개씩 transaction 을 독립시키기 위하여, 
		// 여기 controller 에서 loop 돌면서 service 를 master code 단위로 호출함
		//----------------------------------------------
		for( ShipmentPopupData shipmentData : list ) {
			result = lotteDeliveryService.popPaymentShipmentProc(headers, shipmentData);
			if ( result != null ) {
				buffer.append(result);
				buffer.append("\n");
			} 
		}
		
		Map<String, Object> rsMap = new HashMap<>();
		
		if ( buffer.length() > 0  ) {
			rsMap.put("errCode", false );
			rsMap.put("errMsg", buffer.toString());
		} else {
			rsMap.put("errCode", true);
			rsMap.put("errMsg", "");
		}
		
		return rsMap;
	}

    
    /**
     * 배송 > 팝업 > 배송처리 :  관리자용 (조한두)
     * @return ModelAndView(jsonView)
     */
    @PostMapping(value="/popPaymentShipmentProcAdmin")
    public ModelAndView popPaymentShipmentProcAdmin(@RequestBody List<ShipmentPopupData> shipPopDataList, Model model, HttpServletRequest req, HttpSession sess) {
     
    	ModelAndView mav = new ModelAndView("jsonView");
    	
    	
        ModelAndView spData = shipmentPopupService.popPaymentShipmentProcAdmin(shipPopDataList, model, req, sess);
        model.addAttribute("result", spData.getModelMap().get("result"));
        model.addAttribute("errCode", spData.getModelMap().get("errCode"));
        model.addAttribute("errMsg", spData.getModelMap().get("errMsg"));
        
    	List<String> masterCodeList = new ArrayList<>();
    	for(ShipmentPopupData shipPopupData : shipPopDataList) {
    		masterCodeList.add(shipPopupData.getMasterCode());
    	}
    	
    	int date = shipmentMapper.updateShipmentData(masterCodeList);
        
        return mav;
    }
  
    /*
    
    //바코드 popup
    @RequestMapping(value="/popBacordPrint")
    public ModelAndView popBacordPrint(@ModelAttribute ShipmentPopupData shipPopData, Model model, HttpServletRequest req, HttpSession sess) {
        ModelAndView mav = new ModelAndView("payment/popup/popBacordPrint");
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
  
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
*/
    
   

    
    

    @RequestMapping("/popCreateBarcode/{code}")
    public void popCreateBarcode(@PathVariable("code") String code, HttpServletResponse response) throws Exception {
        if(code == null) code = "";
      
        String orderStr ="";
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
    	final int dpi = 400;
    	
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
    
    @GetMapping("/popShipmentChange")
    public ModelAndView popShipmentChange(Model model, ShipmentPopupData shipPopData, HttpSession sess
    		,@RequestParam(value = "changeReason", defaultValue = "") String changeReason) throws Exception {
        ModelAndView mav = new ModelAndView("shipment/popup/popShipmentChange");
       
        LOGGER.debug("popShipmentChange------------------------------------");        
        
        return mav;
    }
    
    @PostMapping("/popShipmentChangeProc")
    public ModelAndView popShipmentChangeProc(@RequestBody ShipmentPopupData shipPopData, Model model, HttpSession sess) throws Exception {
        ModelAndView mav = new ModelAndView("jsonView");
        
        int result = shipmentPopupService.updatePopShipmentStat(shipPopData, sess);
        
        LOGGER.debug("popShipmentChangeProc : " + result);
        
        mav.addObject("result", result);
        mav.addObject("errCode", true);
        mav.addObject("errMsg", "성공");
        
        return mav;
    }

}