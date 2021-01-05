package com.shopify.order.popup;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.common.util.UtilFunc;
import com.shopify.shipment.ShipmentData;


@Controller
@RequestMapping("/order/popup")
public class OrderPopupController{
    private Logger LOGGER = LoggerFactory.getLogger(OrderPopupController.class);
    
    @Autowired private OrderPopupService orderPopupService;
    @Autowired private ObjectMapper mapper; // jackson's objectmapper
    
    /**
	 * 주문 > 팝업 > 주소 설정
	 * @return ModelAndView(model)
	 */
    @GetMapping(value="/popupAddressChange")
    public ModelAndView popupAddressChange() {
    	
    	ModelAndView mav = new ModelAndView("order/popup/popupAddressChange");
    	    	
    	return mav;
    }
    
//    @GetMapping(value="/popOrderCreateAddress")
//    public ModelAndView popOrderCreateAddress(Model model, ShipmentData setting, HttpSession sess
//            , @RequestParam(value = "code", required = false, defaultValue = "") String code
//            , @RequestParam(value = "idx", required = false, defaultValue = "0") int idx
//        ) {
//    	
//        ModelAndView mav = new ModelAndView("order/popup/popOrderCreateAddress");
//        model.addAttribute("code", code);
//        model.addAttribute("idx", idx);
//        
//        if("".equals(code) || idx == 0) {
//        	model.addAttribute("address", "");
//        	model.addAttribute("sellerAddrList", "");
//        	model.addAttribute("buyerCountryCode", "");
//        	model.addAttribute("status", "no");
//        } else {
//        	setting.setOrderCode(code);
//        	setting.setShopIdx(idx);
//        	Map<String, Object> map = orderPopupService.selectAddress(setting, sess);
//
//        	model.addAttribute("address", map.get("address"));
//        	model.addAttribute("sellerAddrList", map.get("seller"));
//        	model.addAttribute("buyerCountryCode", ((ShipmentData)map.get("address")).getBuyerCountryCode());
//        	model.addAttribute("status", "yes");
//        }
//        
//        return mav;
//    }
    
    /**
	 * 주문 > 팝업 > 배송 주소 저장
	 * @return ModelAndView(jsonView)
	 */
    @PostMapping(value="/popOrderCreateAddressProc")
    public ModelAndView popOrderCreateAddressProc(Model model, HttpSession sess, @RequestBody OrderDeliveryData delivery) {
        ModelAndView mav = new ModelAndView("jsonView");
        LOGGER.debug("DOMESTIC-popOrderCreateAddressProc:" + delivery);
        int chk = orderPopupService.insertDelivery(delivery, sess);
        if (chk == 0) {
            model.addAttribute("result", chk);
            model.addAttribute("errCode", false);
            model.addAttribute("errMsg", "실패");
        } else {
            model.addAttribute("result", chk);
            model.addAttribute("errCode", true);
            model.addAttribute("errMsg", "성공");
        }
        return mav;
    }

    /**
	 * 주문 > 팝업 > 관세 popup
	 * @return ModelAndView(model)
	 */
    @RequestMapping(value="/popOrderCreateExpress")
    public ModelAndView popOrderCreateExpress(Model model, ShipmentData setting, HttpSession sess
    		, @RequestParam(value = "code", required = false, defaultValue = "") String code
            , @RequestParam(value = "idx", required = false, defaultValue = "0") int idx
        ) {
    	
        ModelAndView mav = new ModelAndView("order/popup/popOrderCreateExpress");
        if("".equals(code) || idx == 0) {
    		model.addAttribute("code", code);
        	model.addAttribute("idx", idx);
        	model.addAttribute("address", "");
        	model.addAttribute("skuList", "");
        	model.addAttribute("boxList", "");
        	model.addAttribute("status", "no");
        } else {
        	setting.setOrderCode(code);
        	setting.setShopIdx(idx);
        	
        	Map<String, Object> map = orderPopupService.selectAddress(setting, sess);

        	ShipmentData address = (ShipmentData) map.get("address");
        	
        	model.addAttribute("address", address);
        	model.addAttribute("sellerAddrList", map.get("seller"));
        	
        	
        	map = orderPopupService.selectExpress(setting, sess, address);

        	model.addAttribute("code", code);
        	model.addAttribute("idx", idx);
//        	model.addAttribute("address", map.get("address"));
        	model.addAttribute("skuList", map.get("skuList"));
        	model.addAttribute("boxList", map.get("boxList"));
        	model.addAttribute("courier", map.get("courier"));
        	model.addAttribute("status", "yes");
        	
//        	ShipmentData address = (ShipmentData) map.get("address");
//        	Map<String, Object> mapX = orderPopupService.selectExpressCustoms(setting, sess);

//        	model.addAttribute("list", mapX.get("list"));
//        	model.addAttribute("courier",  orderPopupService.selectPayment(address));
//        	model.addAttribute("address", mapX.get("address"));
//          	model.addAttribute("buyerCountryCode", address.getBuyerCountryCode());
//        	model.addAttribute("status", mapX.get("status"));
        }
        
        return mav;
    }
    
    /**
	 * 주문 > 팝업 > 관세 popup > 저장
	 * @return ModelAndView(jsonView)
	 */
    @PostMapping(value="/popOrderCreateExpressProc" , produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView popOrderCreateExpressProc(Model model, HttpSession sess, HttpServletRequest req
    		,@RequestBody OrderDeliveryData delivery
    	) {
    	
    	 LOGGER.debug("DOMESTIC-popOrderCreateExpressProc:" + delivery);
        ModelAndView mav = new ModelAndView("jsonView");
        
        Map<String, Object> map = orderPopupService.insertDeliveryExpress(delivery, sess);
        
        
//        if(chk == 0) {
//            model.addAttribute("result", chk);
//            model.addAttribute("errCode", false);
//            model.addAttribute("errMsg", "실패");
//        }else {
//            model.addAttribute("result", chk);
//            model.addAttribute("errCode", true);
//            model.addAttribute("errMsg", "성공");
//        }

        model.addAttribute("errCode",map.get("errCode"));
        
        return mav;
    }
    
    /**
	 * 주문 > 팝업 > 해외특송 popup
	 * @return ModelAndView(model)
	 */
    @GetMapping(value="/popOrderCreateCustoms")
    public ModelAndView popOrderCreateCustoms(Model model, ShipmentData setting, HttpSession sess
    		, @RequestParam(value = "code", required = false, defaultValue = "") String code
            , @RequestParam(value = "idx", required = false, defaultValue = "0") int idx
        ) {
        
    	ModelAndView mav = new ModelAndView("order/popup/popOrderCreateCustoms");
    	model.addAttribute("code", code);
    	model.addAttribute("idx", idx);
    	
    	if("".equals(code) || idx == 0) {
        	model.addAttribute("list", "");
        	model.addAttribute("courier", "");
        	model.addAttribute("address", "");
        	model.addAttribute("buyerCountryCode", "");
        	model.addAttribute("status", "no");
        } else {
        	setting.setOrderCode(code);
        	setting.setShopIdx(idx);
        	Map<String, Object> map = orderPopupService.selectOrderCustoms(setting, sess);

        	model.addAttribute("list", map.get("list"));
        	model.addAttribute("courier", map.get("courier"));
        	model.addAttribute("address", map.get("address"));
          	model.addAttribute("buyerCountryCode", ((ShipmentData)map.get("address")).getBuyerCountryCode());
        	model.addAttribute("status", map.get("status"));
        }
    	
        return mav;
    }
    
    /**
	 * 주문 > 팝업 > 해외특송 popup > 저장
	 * @return ModelAndView(jsonView)
	 */
    @PostMapping(value="/popOrderCreateCustomsProc")
    public ModelAndView popOrderCreateCustomsProc(Model model, HttpSession sess, @RequestBody Map<String, String> input) {
    	ModelAndView mav = new ModelAndView("jsonView");
    	LOGGER.debug("DOMESTIC-popOrderCreateCustomsProc:" + input);
    	
    	UtilFunc.unCommaMap(input, "weight", "totalWeight", "volumeticWeight");

    	OrderDeliveryData delivery = mapper.convertValue(input, OrderDeliveryData.class);
    	OrderCourierData courier = mapper.convertValue(input, OrderCourierData.class);
    	String orderCode = courier.getOrderCode();
    	
    	// transaction 을 하나로 하기 위하여, facade service method 를 호출함
    	Map<String, Object> map = orderPopupService.insertFacadeExpressCustoms(delivery, courier, sess);
    	
        model.addAttribute("errCode",map.get("errCode"));
        model.addAttribute("proc", courier.getProc());
        model.addAttribute("currPosition", orderCode);
        
        return mav;
    }
    
    /**
	 * 주문 > 팝업 > 주소 설정 ( 국내택배)
	 * @return ModelAndView(model)
	 */
    @GetMapping(value="/popOrderCreateDomestic")
    public ModelAndView popOrderCreateDomestic(Model model, ShipmentData setting, HttpSession sess
            , @RequestParam(value = "code", required = false, defaultValue = "") String code
            , @RequestParam(value = "idx", required = false, defaultValue = "0") String idx
        ) {
    	
        ModelAndView mav = new ModelAndView("order/popup/popOrderCreateDomestic");
        model.addAttribute("code", code);
        model.addAttribute("idx", idx);

        String ckOrderCode = code.replaceAll(",", "").trim();
        
        if("".equals(ckOrderCode)) {
        	model.addAttribute("address", "");
        	model.addAttribute("sellerAddrList", "");
        	model.addAttribute("status", "no");
        }else {

        	setting.setOrderCode(code);
        	setting.setArrShopIdx(idx);
        	Map<String, Object> map;
            if(idx.indexOf(",")>0)
            {
            	map = orderPopupService.selectDomesticAddress(-1, setting, sess);
            }
            else{
            	int ShopIdx = Integer.parseInt(idx);
            	map = orderPopupService.selectDomesticAddress(ShopIdx, setting, sess);
            }
        	model.addAttribute("address", map.get("address"));
        	model.addAttribute("sellerAddrList", map.get("seller"));
        	model.addAttribute("defaultCourier", map.get("defaultCourier"));
        	
        	setting.setOrderCode(code);
        	setting.setArrShopIdx(idx);        	        	
        	
        	map = orderPopupService.selectOrderExpress(setting, sess);
            
        	model.addAttribute("courierList", map.get("courierList"));
        	model.addAttribute("skuList", map.get("skuList"));
        	model.addAttribute("boxList", map.get("boxList"));
        	model.addAttribute("status", map.get("status"));
        	model.addAttribute("boxTypeList",map.get("boxTypeList"));
        	
        	
        	model.addAttribute("status", "yes");
        } 
        return mav;
    }
    
    /**
  	 * 주문 > 팝업 > 해외특송 popup > 저장
  	 * @return ModelAndView(jsonView)
  	 */
      @PostMapping(value="/popOrderCreateDomesticProc")
      public ModelAndView popOrderCreateDomesticProc(Model model, HttpSession sess
                            ,@RequestBody OrderInputData inputData
      	) {
    	  LOGGER.debug("DOMESTIC-popOrderCreateDomesticProc:" + inputData);
    	  
          ModelAndView mav = new ModelAndView("jsonView");
          
          String orderCode = inputData.getOrderCode();
          int chk = orderPopupService.orderDeliveryFacade(inputData, sess);
          
          model.addAttribute("proc", inputData.getProc());
          model.addAttribute("result", chk);
          model.addAttribute("currPosition", orderCode);
          
          if(chk == 0) {
              model.addAttribute("errCode", false);
              model.addAttribute("errMsg", "실패");
          }else {
              model.addAttribute("errCode", true);
              model.addAttribute("errMsg", "성공");
          }

          return mav;
      }
}