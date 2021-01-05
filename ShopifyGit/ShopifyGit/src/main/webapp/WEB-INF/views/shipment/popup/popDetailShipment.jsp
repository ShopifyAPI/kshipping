<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<script type="text/javascript">
    var masterCode = $("#masterCode").val();
	$(function () {
	    initialize();
	    $(document).on("click",".btnPayment",function(){
	    	masterCode = $("#masterCode").val();                                          
            payment(masterCode);
        });
	});
	
	var initialize = function () {
	    initControls();
	    bindEvent();
	};
	
	var initControls = function () {
	    /* setDate();
	    bindSelectAjax($('#ajax-form')); */
	};
	
	var bindEvent = function () {
        
    };
</script>
                <input type="hidden" id="masterCode" value="${detail.masterCode}">
                <div class="pop_head">
                    <h3><spring:message code="order.popup.delivary" text="배송정보" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                    <h4 class="sub_h4 mt20"><spring:message code="shipment.orderInfo" text="주문정보" /></h4>
                    <table class="tbtype mt10">
                        <colgroup>
                            <col class="cper1100">
                            <col class="cperauto">
                            <col class="cper1100">
                            <col class="cper0950">
                            <col class="cper1100">
                            <col class="cperauto">
                            <col class="cper1100">
                            <col class="cper1200">
                        </colgroup>
                        <tbody>
                            <tr>
                                <%-- <th><p><spring:message code="settings.shopName" text="쇼핑몰명" /></p></th>
                                <td><p>${detail.shopName}</p></td> --%>
                                <th><p><spring:message code="shipment.buyerName" text="주문자명" /></p></th>
                                <td colspan="3"><p>${detail.customerName}</p></td>
                                <th><p><spring:message code="admin.pop.contact" text="연락처" /></p></th>
                                <td><p>${detail.sellerPhone}</p></td>
                                <th><p><spring:message code="order.paymentAmount" text="결제금액" /></p></th>
                                <%-- <td class="t_right"><p><fmt:formatNumber value="${detail.orderPriceStr}" pattern="#,###" />원</p></td> --%>
                                <td class="t_right"><p>${detail.orderPriceStr}원</p></td>
                            </tr>
                            
                            <tr>
                                <th><p><spring:message code="shipment.shipmentCode" text="배송번호" /></p></th>
                                <td colspan="3">
                                    <p>${detail.masterCode}</p>
                                    <%-- 화물 추적 도메인 
                                        DHL : http://www.dhl.co.kr/content/kr/ko/express/tracking.shtml?AWB=307021770241&brand=DHL
                                        UPS : https://www.ups.com/track?loc=ko_KR&tracknum=307021770241&requester=WT/ 
                                    --%>
                                </td>
                                <th><p><spring:message code="shipment.delivery" text="배송서비스" /></p></th>
                                <td colspan="3"><p>${detail.courier}</p></td>
                            </tr>
                            
                        </tbody>
                    </table>
                    
                    <table class="tbtype mt20">
                        <colgroup>
                            <col class="cper1100">
                            <col class="cper2000">
                            <col class="cper1100">
                            <col class="cperauto">
                            <col class="cper1100">
                            <col class="cper1500">
                        </colgroup>
                        <tbody>
                            <tr>
                                <th><p><spring:message code="admin.pop.contact" text="연락처" /></p></th>
                                <td><p>${detail.buyerPhone}</p></td>
                                <th><p><spring:message code="cs.buyerName" text="수취인 명" /></p></th>
                                <td><p>${detail.buyerFirstname} ${detail.buyerLastname}</p></td>
                                <th><p><spring:message code="shipment.shipmentDate" text="배송접수일" /></p></th>
                                <td><p>${detail.regDateStr}</p></td>
                             </tr>
                             <tr>
                                <th><p><spring:message code="order.popup.express.box.totalWeight" text="총무게" /></p></th>
                                <td><p>${detail.payWeight} ${detail.weightUnit}</p></td>
                                <th><p><spring:message code="shipment.popup.delivery.price" text="배송비" /></p></th>
                                <%-- <td class="t_right"><p><fmt:formatNumber value="${detail.payment}" pattern="#,###" /><spring:message code="common.unit" text="원" /></p></td> --%>
                                <td class="t_right"><p><fmt:formatNumber value="${detail.payment}" pattern="#,###" /><spring:message code="common.unit" text="원" /></p></td>
                                <th><p><spring:message code="board.status" text="상태" /></p></th>
                                <td><div class="btn_state ${detail.stateStrCss}">${detail.stateStr}</div></td>
                             </tr>
                             <tr>
                                <th><p><spring:message code="cs.deliveryCountry" text="배송" /></p></th>
                                <td><p>${detail.buyerCountryCode}</p></td>
                                <th rowspan="2"><p><spring:message code="order.popup.address.address2" text="주소" /></p></td>
                                <td colspan="3">${detail.buyerCity} ${detail.buyerProvince}</td>
                             </tr>
                             <tr>
                                <th><p><spring:message code="order.popup.address.post" text="우편번호" /></p></th>
                                <td><p>${detail.buyerZipCode}</p></td>
                                <td colspan="3"><p>${detail.buyerAddr1} ${detail.buyerAddr2}</p></td>
                              </tr>
                        </tbody>
                    </table>

                  
                    <%-- <table class="tbtype mt20">
                        <colgroup>
                            <col class="cper1100">
                            <col class="cperauto">
                            <col class="cper1100">
                            <col class="cperauto">
                            <col class="cper1100">
                            <col class="cper1200">
                            <col class="cper1100">
                            <col class="cper1200">
                        </colgroup>
                        <tbody>
                            <tr>
                                <th><p><spring:message code="shipment.shipmentCode" text="배송번호" /></p></th>
                                <td><p>${detail.masterCode}</p></td>
                                <th><p><spring:message code="shipment.delivery" text="배송서비스" /></p></th>
                                <td><p>${detail.courier}</p></td>
                                <th><p><spring:message code="shipment.shipmentDate" text="배송접수일" /></p></th>
                                <td><p>${detail.regDateStr}</p></td>
                                <th><p><spring:message code="board.status" text="상태" /></p></th>
                                <td><div class="btn_state ${detail.stateStrCss}">${detail.stateStr}</div></td>
                            </tr>
                            <tr>
                                <th><p><spring:message code="cs.buyerName" text="수취인 명" /></p></th>
                                <td><p>${detail.buyerFirstname} ${detail.buyerLastname}</p></td>
                                <th><p><spring:message code="admin.pop.contact" text="연락처" /></p></th>
                                <td><p>${detail.buyerPhone}</p></td>
                                <th><p><spring:message code="order.popup.express.box.totalWeight" text="총무게" /></p></th>
                                <td><p>${detail.payWeight} ${detail.weightUnit}</p></td>
                                <th><p><spring:message code="shipment.popup.delivery.price" text="배송비" /></p></th>
                                <td class="t_right"><p>${detail.payment}원</p></td>
                            </tr>
                            <tr>
                                <th><p><spring:message code="cs.deliveryCountry" text="배송" /></p></th>
                                <td><p>${detail.buyerCountryCode}</p></td>
                                <th rowspan="2"><p><spring:message code="order.popup.address.address2" text="주소" /></p></td>
                                <td colspan="5">${detail.buyerCity} ${detail.buyerProvince}</td>
                            </tr>
                            <tr>
                                <th><p><spring:message code="order.popup.address.post" text="우편번호" /></p></th>
                                <td><p>${detail.buyerZipCode}</p></td>
                                <td colspan="5"><p>${detail.buyerAddr1} ${detail.buyerAddr2}</p></td>
                            </tr>
                        </tbody>
                    </table> --%>
                    
                   <h4 class="sub_h4 mt20"><spring:message code="order.popup.itemInfo" text="상품정보" /></h4>
                   <div class="scr_x mt10">
                   <c:choose>
	                   <c:when test="${fn:length(list) == 0}">
	                       
	                       <div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
	                   
	                   </c:when>
	                   <c:otherwise>
                   
                        <table class="tbtype">
                            <colgroup>
                                <!-- <col class="wp120"> -->
                                <col class="wp120">
                                <col class="wp390">
                                <!-- <col class="wp100"> -->                                
                                <col class="wp60">
                                <col class="wp60">
                                <col class="wp80">
                                <col class="wp120">
                                <col class="wp80">
                                <col class="wp120">
                            </colgroup>
                            <thead>
                                <tr>
                                    <%-- <th><spring:message code="order.orderNo" text="주문번호" /></th> --%>
                                    <th><spring:message code="settings.itemCode" text="상품코드" /></th>
                                    <th><spring:message code="settings.itemName" text="상품명" /></th>
                                    <%-- <th><spring:message code="settings.itemSku" text="SKU" /></th> --%>
                                    <th><spring:message code="settings.weight" text="무게" /></th>
                                    <th><spring:message code="settings.itemQty" text="개수" /></th>
                                    <th><spring:message code="settings.itemOrigin" text="생산지" /></th>
                                    <th><spring:message code="settings.hscode" text="HS CODE" /></th>
                                    <th><spring:message code="settings.itemType" text="상품타입" /></th>
                                    <th><spring:message code="settings.itemPrice" text="가격" /></th>
                                </tr>
                            </thead>
                            <c:forEach items="${list}" var="list" varStatus="status">
                            <tbody>
                                <tr>
                                    <%-- <td class="t_center"><p>${list.orderCode}</p></td> --%>
                                    <td class="t_center"><p>${list.goodsCode}</p></td>
                                    <td><p>${list.goods}</p></td>
                                    <%-- <td class="t_center"><p>${list.goodsSku}</p></td> --%>
                                    <td class="t_center"><p>${list.weightStr}</p></td>
                                    <td class="t_center"><p>${list.quantityStr}</p></td>
                                    <td class="t_center"><p>${list.originStr}</p></td>
                                    <td class="t_center"><p>${list.hscode}</p></td>
                                    <td class="t_center"><p>${list.goodsTypeStr}</p></td>
                                    <td class="t_right"><p>${list.paymentStr}</p></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        </c:otherwise>
                    </c:choose>
                    </div>
                    
<%--                     <c:if test="${detail.payState != 'Y'}"> --%>
<!--                     <div class="pop_btn"> -->
<%--                         <button type="button" class="btn_type2 btnPayment"><spring:message code="button.payment" text="결제하기"/></button> --%>
<!--                     </div> -->
<%--                     </c:if> --%>
                    
                </div><!--pop_body end-->