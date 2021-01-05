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
                    <div class="wrap_col w75_25">
                        <div class="colA">
                            <table class="tbtype">
                                <colgroup>
                                    <col class="cper1200">
                                    <col class="cperauto">
                                    <col class="cper1400">
                                    <col class="cperauto">
                                    <col class="cper1400">
                                    <col class="cper1500">
                                </colgroup>
                                <tbody>
                                    <tr>
                                        <th><p><spring:message code="order.popup.delivary" text="송장번호" /></p></th>
                                        <td><p>${detail.invoice}</p></td>
                                        <th><p><spring:message code="shipment.orderInfo" text="주문자 정보" /></p></th>
                                        <td><p>${detail.buyerFirstname} ${detail.buyerLastname}</p></td>
                                        <th><p><spring:message code="shipment.delivery" text="서비스" /></p></th>
                                        <td><p>${detail.courier}</p></td>
                                    </tr>
                                    <tr>
                                        <th rowspan="2"><p><spring:message code="order.popup.delivary" text="주문 정보" /></p></th>
                                        <td><p>(${detail.buyerCountryCode})</p></td>
                                        <td colspan="2"><p>${detail.buyerAddr1} </p></td>
                                        <th><p><spring:message code="board.regDate" text="등록일" /></p></th>
                                        <td><p>${detail.regDateStr}</p></td>
                                    </tr>
                                    <tr>
                                        <td><p>${detail.buyerZipCode}</p></td>
                                        <td colspan="2"><p>${detail.buyerAddr2}</p></td>
                                        <th><p><spring:message code="board.status" text="상태" /></p></th>
                                        <td><div class="btn_state paym">${detail.stateStr}</div></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="colB">
                            <table class="tbtype">
                                <colgroup>
                                    <col class="cper4500">
                                    <col class="cper5500">
                                </colgroup>
                                <tbody>
                                    <tr>
                                        <th><p><spring:message code="order.popup.express.box.totalWeight" text="총 무게 "/></p></th>
                                        <td class="t_right"><p>${detail.payWeight} ${detail.weightUnit}</p></td>
                                    </tr>
                                    <tr>
                                        <th><p><spring:message code="order.paymentAmount" text="결제금액"/></p></th>
                                        <td class="t_right"><p>${detail.payment}원</p></td>
                                    </tr>
                                    <tr>
                                        <th><p><spring:message code="shipment.popup.delivery.price" text="배송비"/></p></th>
                                        <td class="t_right"><p>${detail.payment}원</p></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                   <table class="tbtype">
                       <colgroup></colgroup>
                   </table>
                   <h4 class="sub_h4 mt20"><spring:message code="order.popup.address.title2" text="관세정보 "/></h4>
                   <div class="scr_x mt10">
                   <c:choose>
	                   <c:when test="${fn:length(list) == 0}">
	                       
	                       <div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
	                   
	                   </c:when>
	                   <c:otherwise>
                        <table class="tbtype">
                            <colgroup>
                                <col class="wp120">
                                <col class="wp300">
                                <col class="wp50">
                                <col class="wp120">
                                <col class="wp60">
                                <col class="wp60">
                                <col class="wp60">
                                <col class="wp60">
                                <col class="wp60">
                                <col class="wp60">
                                <col class="wp90">
                                <col class="wp100">
                                <col class="wp80">
                                <col class="wp120">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th><spring:message code="order.orderNo" text="주문번호 "/></th>
                                    <th><spring:message code="order.productName" text="상품명"/></th>
                                    <th>SKU</th>
                                    <th><spring:message code="settings.itemCode" text="상품코드"/></th>
                                    <th><spring:message code="settings.weight" text="무게"/></th>
                                    <th><spring:message code="shipment.popup.unit" text="단위"/></th>
                                    <th><spring:message code="order.popup.express.tax.horizontal" text="가로"/></th>
                                    <th><spring:message code="order.popup.express.tax.Vertical" text="세로"/></th>
                                    <th><spring:message code="settings.boxHeight" text="높이"/></th>
                                    <th><spring:message code="shipment.popup.unit" text="단위"/></th>
                                    <th><spring:message code="order.popup.express.tax.production" text="생산지"/></th>
                                    <th>HS CODE</th>
                                    <th><spring:message code="settings.itemType" text="상품타입"/></th>
                                    <th><spring:message code="settings.itemPrice" text="가격"/></th>
                                </tr>
                            </thead>
                            <tbody>
                            
		                        <c:forEach items="${list}" var="item" varStatus="status">
                                <tr>
                                    <td class="t_center"><p>${item.orderCode}</p></td>
                                    <td><p>${item.goods}</p></td>
                                    <td><p>${item.goodsSku}</p></td>
                                    <td><p>${item.goodsCode}</p></td>
                                    <td class="t_center"><p>${item.weight}</p></td>
                                    <td class="t_center"><p>${item.weightUnit}</p></td>
                                    <td class="t_center"><p>${item.boxLength}</p></td>
                                    <td class="t_center"><p>${item.boxWidth}</p></td>
                                    <td class="t_center"><p>${item.boxHeight}</p></td>
                                    <td class="t_center"><p>${item.boxUnit}</p></td>
                                    <td class="t_center"><p>${item.origin}</p></td>
                                    <td class="t_center"><p>${item.hscode}</p></td>
                                    <td class="t_center"><p>${item.goodsType}</p></td>
                                    <td class="t_right"><p>${item.price}</p></td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        </c:otherwise>
                    </c:choose>
                    </div>
                    <c:if test="${detail.payState != 'Y'}">
                    <div class="pop_btn">
                        <button type="button" class="btn_type2 btnPayment"><spring:message code="button.payment" text="결제하기"/></button>
                    </div>
                    </c:if>
                </div>