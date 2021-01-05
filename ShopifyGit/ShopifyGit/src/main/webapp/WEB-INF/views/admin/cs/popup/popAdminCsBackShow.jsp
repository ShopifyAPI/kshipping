<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>
<script>
</script>
<!-- 
**************************************************
화면코드 : 
기능설명 : 관리자CS관리 반송 상세보기(팝업)
Author   Date      Description
 김윤홍     2020-02-05  First Draft
**************************************************
 -->
<div class="pop_head">
    <h3><spring:message code="order.popup.delivary" text="배송정보" /></h3>
    <button type="button" class="btn_close"></button>
</div>
<div class="pop_body">
                    <h4 class="sub_h4 mt20"><spring:message code="shipment.orderInfo" text="주문정보" /></h4>
                    <table class="tbtype2 mt10">
                        <colgroup>
                            <col class="cper1100">
                            <col class="cperauto">
                            <col class="cper1100">
                            <col class="cperauto">
                            <col class="cper1100">
                            <col class="cper1200">
                        </colgroup>
                        <tbody>
                            <tr>
                            	<th><p><spring:message code="shipment.buyerName" text="주문자명" /></p></th>
                            	<td><p>${detail.customerName}</p></td>
                            	<th><p><spring:message code="admin.pop.contact" text="연락처" /></p></th>
                            	<td><p>${detail.sellerPhone}</p></td>
                            	<th><p><spring:message code="order.paymentAmount" text="결제금액" /></p></th>
                            	<td class="t_right"><p>${detail.paymentTotalStr}원</p></td>
                            </tr>
                            <tr>
                                <th><p><spring:message code="shipment.shipmentCode" text="배송번호" /></p></th>
                                <td>
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
                    
                    <%-- <table class="tbtype mt10">
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
                                <th><p><spring:message code="settings.shopName" text="쇼핑몰명" /></p></th>
                                <td><p>${detail.shopName}</p></td>
                                <th><p><spring:message code="shipment.buyerName" text="주문자명" /></p></th>
                                <td><p>${detail.customerName}</p></td>
                                <th><p><spring:message code="admin.pop.contact" text="연락처" /></p></th>
                                <td><p>${detail.sellerPhone}</p></td>
                                <th><p><spring:message code="order.paymentAmount" text="결제금액" /></p></th>
                                <td class="t_right"><p>${detail.orderPrice}원</p></td>
                            </tr>
                        </tbody>
                    </table> --%>
                    
                    <table class="tbtype2 mt20">
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
                                <th><p><spring:message code="cs.buyerName" text="수취인 명" /></p></th>
                                <td colspan="3"><p>${detail.buyerFirstname} ${detail.buyerLastname}</p></td>
                                <th><p><spring:message code="admin.pop.contact" text="연락처" /></p></th>
                                <td colspan="3"><p>${detail.buyerPhone}</p></td>
                                <%-- <th><p><spring:message code="order.popup.express.box.totalWeight" text="총무게" /></p></th>
                                <td><p>${detail.payWeight} ${detail.weightUnit}</p></td>
                                <th><p><spring:message code="shipment.popup.delivery.price" text="배송비" /></p></th>
                                <td class="t_right"><p>${detail.payment}원</p></td> --%>
                            </tr>
                            <tr>
                                <th><p><spring:message code="order.popup.express.box.totalWeight" text="총무게" /></p></th>
                                <td><p>${detail.payWeight} ${detail.weightUnit}</p></td>
                                <th><p><spring:message code="shipment.popup.delivery.price" text="배송비" /></p></th>
                                <td class="t_right"><p>${detail.paymentStr}원</p></td>
                                <th><p><spring:message code="shipment.shipmentDate" text="배송접수일" /></p></th>
                                <td><p>${detail.regDateStr}</p></td>
                                <th><p><spring:message code="board.status" text="상태" /></p></th>
                                <!-- 반송사유 -->
                                <td class="t_center"><div class="btn_state ${detail.reasonStrCss}">${detail.reasonStr}</div></td>
                                <!-- 반송사유 -->
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
                    </table>
                    
                   <h4 class="sub_h4 mt20"><spring:message code="order.popup.itemInfo" text="상품정보" /></h4>
                   <div class="scr_x mt10">
                   
                        <table class="tbtype">
                            <colgroup>
                                <col class="wp120">
                                <col class="wp120">
                                <col class="wp390">
                                <col class="wp100">                                
                                <col class="wp60">
                                <col class="wp60">
                                <col class="wp80">
                                <col class="wp120">
                                <col class="wp80">
                                <col class="wp120">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th><spring:message code="order.orderNo" text="주문번호" /></th>
                                    <th><spring:message code="settings.itemCode" text="상품코드" /></th>
                                    <th><spring:message code="settings.itemName" text="상품명" /></th>
                                    <th><spring:message code="settings.itemSku" text="SKU" /></th>
                                    <th><spring:message code="settings.weight" text="무게" /></th>
                                    <th><spring:message code="settings.itemQty" text="개수" /></th>
                                    <th><spring:message code="settings.itemOrigin" text="생산지" /></th>
                                    <th><spring:message code="settings.hscode" text="HS CODE" /></th>
                                    <th><spring:message code="settings.itemType" text="상품타입" /></th>
                                    <th><spring:message code="settings.itemPrice" text="가격" /></th>
                                </tr>
                            </thead>
                            
                            <tbody>
                        <c:choose>
		                   <c:when test="${fn:length(list) == 0}">
		                       
		                        <tr>
                                    <td colspan="10" class="t_center"><p><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></p></td>
                                </tr>
		                   
		                   </c:when>
		                   <c:otherwise>
                            	<c:forEach items="${list}" var="item" varStatus="status">
                            	
                                <tr>
                                    <td class="t_center"><p>${item.orderCode}</p></td>
                                    <td class="t_center"><p>${item.goodsCode}</p></td>
                                    <td><p>${item.goods}</p></td>
                                    <td class="t_center"><p>${item.goodsSku}</p></td>
                                    <td class="t_center"><p>${item.weight}${item.weightUnit}</p></td>
                                    <td class="t_center"><p>${item.quantityStr}</p></td>
                                    <td class="t_center"><p>${item.origin}</p></td>
                                    <td class="t_center"><p>${item.hscode}</p></td>
                                    <td class="t_center"><p>${item.goodsTypeStr}</p></td>
                                    <td class="t_right"><p>${item.unitCost}</p></td>
                                </tr>
                                
                            </c:forEach>
                         </c:otherwise>
                    </c:choose>                           
                            
                            </tbody>
                        </table>

                    </div>
                    
                </div><!--pop_body end-->