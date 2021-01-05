package com.shopify.order.popup;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
* 주문 합배송 컨트롤러
* @author : 심윤
* @since  : 2020-02-27
* @desc   : 주문 합배송  컨트롤러
*/
@Controller
@RequestMapping("/order/popup")
public class OrderCombineController{
    private Logger LOGGER = LoggerFactory.getLogger(OrderCombineController.class);
    
    @Autowired private OrderCombineService orderCombineService;
    //@Autowired private OrderService orderService;
    //@Autowired private OrderPopupService orderPopupService;
    //@Autowired private UtilFn util;
    
    /**
	 * 주문 > 합배송 팝업 > 주문번호 확인 Step1
	 * @return ModelAndView(model)
	 */
    @GetMapping(value="/popOrderCombine")
    public ModelAndView popOrderCombine(Model model, @ModelAttribute OrderPopupData orderPopupData, HttpSession sess) {
    	
    	ModelAndView mav = new ModelAndView("order/popup/popOrderCombine");
        
    	Map<String, Object> map = orderCombineService.orderCombineCheck(orderPopupData, sess);
    	
    	model.addAttribute("status", map.get("checkCode"));
    	model.addAttribute("orderCodeList", map.get("list"));
    	model.addAttribute("arrOrderCode", map.get("arrOrderCode"));
    	model.addAttribute("arrShopIdx", map.get("arrShopIdx"));

    	return mav;
    }
    
    /**
	 * 주문 > 합배송 팝업 > 관세정보 Step2
	 * @return ModelAndView(model)
	 */
    @GetMapping(value="/popOrderCombineInput")
    public ModelAndView popOrderCombineInput(Model model, @ModelAttribute OrderPopupData orderPopupData, HttpSession sess) {
    	ModelAndView mav = new ModelAndView("order/popup/popOrderCombineInput");

    	Map<String, Object> map = orderCombineService.orderCombineAddr(orderPopupData, sess);
  
    	model.addAttribute("arrOrderCode", map.get("arrOrderCode"));
    	model.addAttribute("arrShopIdx", map.get("arrShopIdx"));
    	model.addAttribute("address", map.get("address"));
    	model.addAttribute("sellerAddrList", map.get("seller")); 
    	model.addAttribute("boxList", map.get("boxList")); 
    	model.addAttribute("skuList", map.get("skuList")); 
    	model.addAttribute("status", map.get("status"));

    	return mav;
    }
    
    /**
	 * 주문 > 합배송 팝업 > 배송서비스 선택 Step3
	 * @return ModelAndView(model)
	 */
    @GetMapping(value="/popOrderCombineDelivery")
    public ModelAndView popOrderCombinedelivery(Model model, @ModelAttribute OrderPopupData orderPopupData, HttpSession sess) {
    	
    	Map<String, Object> map = orderCombineService.orderCombineDeliveryService(orderPopupData, sess);
        
    	ModelAndView mav = new ModelAndView("order/popup/popOrderCombineDelivery");
    	model.addAttribute("list", map.get("list"));
    	model.addAttribute("courier", map.get("courier"));
    	model.addAttribute("status", map.get("status"));
    	model.addAttribute("address", map.get("address"));  //yr추가[2020.05.26]
    	
        return mav;
    }
    
    
    /**
	 * 주문 > 합배송 팝업 > 합배송 저장 proc Step4
	 * @return ModelAndView(jsonView)
	 */
    @PostMapping(value="/popOrderCombineProc")
    public ModelAndView popOrderCombineProc(Model model, @RequestBody Map<String, String> request, HttpSession sess) {
    	 
    	int chk = orderCombineService.orderCombineDeliveryProc(request, sess);
    	
    	ModelAndView mav = new ModelAndView("jsonView");
    	
        if(chk == 0) {
            model.addAttribute("result", chk);
            model.addAttribute("errCode", false);
            model.addAttribute("errMsg", "실패");
        }else {
            model.addAttribute("result", chk);
            model.addAttribute("errCode", true);
            model.addAttribute("errMsg", "성공");
        }
    	
        return mav;
        

//        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
//        
//        if(orderIdxChk != null) {
//            //합배송 주문번호 임의생성(오늘날짜+임의수4자리)
//            String orderCode = util.getDateElement("full").substring(2,12)+util.randomNumber(4,4);
//            //합배송 주문일자 생성
//            String orderDate = util.getDateElement("today");
//            OrderData combineOrder = new OrderData();
//            combineOrder.setShopIdx(sessionData.getShopIdx());
//            combineOrder.setOrderCode(orderCode);
//            combineOrder.setHideYn("N");
//            combineOrder.setOrderDate(orderDate);
//            combineOrder.setCombineYn("Y");
//            combineOrder.setEmail(sessionData.getEmail());
//            
//            OrderDeliveryData delivery = new OrderDeliveryData();
//            delivery.setShopIdx(sessionData.getShopIdx());
//            delivery.setOrderCode(orderCode);
//            delivery.setOrderDate(orderDate);
//
//            //합배송용 주문생성
//            //orderService.insertOrder(combineOrder);
//            int chk = 0;
//            Map<String, Object> map = orderPopupService.createDelivery(delivery, sess);
//            if(map != null) {
//            	chk = (int) map.get("chk");
//            	masterCode = (String) map.get("masterCode");
//            }
//            LOGGER.debug("###############popOrderCombineProc=chk=>/"+chk);
//            
//            if(chk == 1) {
//            	//생성된 합배송idx 구하기
//                OrderData combinedChkOrder = orderService.selectOneOrder(combineOrder);
//                
//                //받아온 주문 합배송처리
//                String[] orderAry = orderIdxChk.split(",");
//                //LOGGER.debug("###############1aaaaaaaaaaaa=orderIdx=>/"+orderIdxChk);
//                List<OrderData> orderIdxList = new ArrayList<OrderData>();
//                OrderData od = new OrderData();
//                for(String el : orderAry) {
//                	int orderIdx = orderPopupService.selectOrderIdx(el);
//                	if(el != "" && el != null) {
//						/**/
//                        //orderIdxList.add(od);
//                        //orderPopupService.updateOrderCodeMerge(orderIdxList);
//                		od.setOrderCode(el);
//						od.setOrderIdx(orderIdx);
//						od.setShopIdx(sessionData.getShopIdx());
//						od.setCombineYn("Y");
//						//od.setCombineOrderIdx(combinedChkOrder.getOrderIdx());
//						od.setCombineOrderCode(orderIdxChk);
//                		od.setParentCode(masterCode);
//                		od.setChildCode(orderIdx+"");
//                		LOGGER.debug("###############popOrderCombineProc=masterCode=>/"+masterCode);
//                		LOGGER.debug("###############popOrderCombineProc=orderIdx=>/"+orderIdx);
//                		LOGGER.debug("###############popOrderCombineProc=od=>/"+od);
//                		int cnt= orderPopupService.selectDeliveryCombineCount(od);
//                		LOGGER.debug("###############popOrderCombineProc=cnt=>/"+cnt);
//                		if(cnt == 0) {
//                			cnt = orderPopupService.insertDeliveryCombine(od);
//                			LOGGER.debug("###############popOrderCombineProc=chkMap=>/"+cnt);
//                		}
//                    }
//                	
//                }
//                
//                LOGGER.debug("###############1aaaaaaaaaaaa=orderIdxList=>/"+orderIdxList);
//                
////                model.addAttribute("status", "ok");
//                mav.addObject("status", "ok");
//            }else {
//            	mav.addObject("status", "fail");
//            }
//            
//        }else {
////            model.addAttribute("status", "no");
//        	mav.addObject("status", "no");
//        	
//        }
        
    }

}