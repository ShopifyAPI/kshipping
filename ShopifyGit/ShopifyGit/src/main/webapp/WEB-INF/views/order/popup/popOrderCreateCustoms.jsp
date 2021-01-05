<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_ADM_224_POP
기능설명 : 주문 > 팝업 > 배송사 선택/등록
Author   Date         Description
jwh      2020-01-29   First Draft
**************************************************
 -->
<c:choose>
    <c:when test="${status == 'no'}">
    
    <script type="text/javascript">
    alert("test:" + courier.courier + "/" +  courier.courierCompany);
    callPopupError();
    </script>
    
    </c:when>
    <c:otherwise>

                <div class="pop_head">
                    <h3><spring:message code="order.popup.address.title" text="배송정보" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                    <form name="frm" method="get" id="ajax-popForm">
                    <ul class="step col3">
                        <li><a href="#" onclick="popupAddress('${code}','${idx}'); return false;"><spring:message code="order.popup.address.title1" text="주소등록" /></a></li>
                        <li><a href="#" onclick="popupExpress('${code}','${idx}'); return false;"><spring:message code="order.popup.address.title2" text="관세정보" /></a></li>
                        <li class="on"><a href="#" onclick="return false;"><spring:message code="order.popup.address.title3" text="배송정보" /></a></li>
                    </ul>
                    <ul class="tab_conts">
                        <li>
                            <h4 class="sub_h4"><spring:message code="order.popup.customer.title1" text="배송 서비스 선택" /></h4>
                            <table class="tbtype mt10">
                                <colgroup>
                                    <col class="cperauto">
                                    <col class="cper1250">
                                    <col class="cper0950">
                                    <col class="cper1250">
                                    
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th><spring:message code="order.popup.customer.title2" text="서비스" /></th>
                                        <th><spring:message code="order.popup.customer.title5" text="할인 배송료" /></th>
                                        <th><spring:message code="settings.priceCurrency" text="화폐단위" /></th>
                                        <th><spring:message code="order.popup.customer.title4" text="선택" /></th>
                                    </tr>
                                </thead>
                                <tbody class="courier-area">
                         <c:choose>
                             <c:when test="${fn:length(list) == 0}">
                                 <tr>
                                     <td colspan="5" class="t_center">
                                         <spring:message code="order.popup.customer.nolist" text="해당하는 국가의 국제 특송업체가 존재 하지 않습니다." />
                                     </td>
                                 </tr>
                             </c:when>
                             <c:otherwise>
                             	 <c:set var="pipe" value="|"></c:set>
                                 <c:forEach items="${list}" var="item" varStatus="status">
                  
                                 	
                                    <tr data-courier="${item.code}" data-courierid="${item.courierId}" data-id="${item.comCode}" data-price="${item.price}" data-rankprice="${item.rankPrice}">
                                        <td><p>${item.codeName}</p></td>
                                        <td class="t_right">
                                            <p><fmt:formatNumber value="${item.price - item.rankPrice}" pattern="#,###" /></p>
                                        </td>
                                        <td class="t_center">
                                            KRW
                                        </td>
                                        <td class="t_center">
                                        <c:choose>
                                            <c:when test="${item.courierId == address.shippingLineCode}">
                                                <button data-courier="${item.code}" data-id="${item.comCode}" data-price="${item.price}" data-courierid="${item.courierId}" data-rankprice="${item.rankPrice}"
                                                     type="button" class="btn_select on orderCourier"><spring:message code="button.select" text="선택" /></button>
                                            </c:when>
                                            <c:when test="${item.comCode == courier.courierCompany && item.courierId == courier.courier}">
                                                <button data-courier="${item.code}" data-id="${item.comCode}" data-price="${item.price}" data-courierid="${item.courierId}" data-rankprice="${item.rankPrice}"
                                                     type="button" class="btn_select on"><spring:message code="button.select" text="선택" /></button>
                                            </c:when>
                                            <c:otherwise>
                                                <button data-courier="${item.code}" data-id="${item.comCode}" data-price="${item.price}" data-courierid="${item.courierId}" data-rankprice="${item.rankPrice}"
                                                    type="button" class="btn_select"><spring:message code="button.select" text="선택" /></button>
                                            </c:otherwise>
                                        </c:choose>
                                        </td>
                                    </tr>
                 

 									<c:set var="tempCode">${tempCode}${item.code}${pipe}</c:set>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
 
                                </tbody>
                            </table>
                        
                        </li>
                    </ul>
                    <div class="pop_btn">
                    <c:if test="${fn:length(list) != 0}">
                        <button type="button" class="btn_type2" id="saveCustomsBtn"><spring:message code="button.save.and.next" text="저장한후 다음열기" /></button>
                        <button type="button" class="btn_type4" id="addDeliveryBtn"><spring:message code="button.deliveryAdd" text="배송생성" /></button>
                    </c:if>
                    </div>
                    <input type="hidden" name="proc" value="">
                    <input type="hidden" name="courierCompany" value="${courier.courierCompany}">
                    <input type="hidden" name="courier" value="${courier.courier}">
                    <input type="hidden" name="payment" value="${courier.payment}">
                    <input type="hidden" name="courierId" value="${courier.courierId}">
                    <input type="hidden" name="rankPrice" value="${courier.rankPrice}">
                    <input type="hidden" name="paymentCode" value="NA">
                    <input type="hidden" name="payState" value="N">
                    <input type="hidden" name="masterCode" value="${address.masterCode}">
                    <input type="hidden" name="orderCode" value="${address.orderCode}">
                    <input type="hidden" name="shopIdx" value="${address.shopIdx}">
                    <input type="hidden" name="orderIdx" value="${address.orderIdx}">
                    </form>
                </div>

    </c:otherwise>
</c:choose>     