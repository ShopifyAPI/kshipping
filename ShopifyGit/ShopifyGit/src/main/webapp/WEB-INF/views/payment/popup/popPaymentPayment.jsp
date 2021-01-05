<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 

                <div class="pop_head">
                    <h3><spring:message code="order.popup.delivary" text="배송정보" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                        <div class="tit_wrap mt0">
                            <h4 class="sub_h4"><spring:message code="incgnb.shipment" text="배송" /></h4>
                            <div class="sect_right">
                                <p class="bd_count"><spring:message code="common.unit.total" text="총" /> ${listCnt}<spring:message code="common.unit.case" text="건" /></p>
                            </div>
                        </div>
                        <div class="pick_service" id="deliveryform">
                            <input type="checkbox" id="pick_service" name="pickupService"><label for="pick_service"><span class="icon_ck"></span><span class="label_txt"><spring:message code="shipment.popup.payment.pickup" text="택배 픽업 서비스" /></span></label>
                            <div class="right pickupService" style="display:none">
                                <div id="boxSize" style="display:none">
                                    <select class="select-ajax box_size" name="box_size" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A170000" }' ></select>
                                </div>
                                <select class="select-ajax" name="courier" id="courier" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B010000" }'></select>
                                <button type="button" class="btn_type2 addPickup"><spring:message code="button.add" text="추가"/></button>
                            </div>
                        </div>
                        <div class="h_scroll" style="height:auto">
                        <c:choose>
                        <c:when test="${fn:length(list) == 0}">
                           <div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
                        </c:when>
                        <c:otherwise>
                            <table class="tbtype">
                                <colgroup>
                                    <col class="wp35">
                                    <col class="wp150">
                                    <col class="wp100">
                                    <col class="cperauto">
                                    <col class="wp100">
                                    <col class="wp150">
                                    <col class="wp100">
                                    <col class="wp80">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th><input type="checkbox" id="pickup" name="pickup"><label for="pickup"><span class="icon_ck"></span></label></th>
                                        <th><spring:message code="shipment.shipmentCode" text="배송번호"/></th>
                                        <th><spring:message code="order.orderDate" text="주문일자"/></th>
                                        <th><spring:message code="order.productName" text="상품명"/></th>
                                        <th><spring:message code="order.orderInfo" text="주문자 정보"/></th>
                                        <th><spring:message code="admin.statis.sales.title5" text="특송서비스"/></th>
                                        <th><spring:message code="order.paymentAmount" text="결제 금액"/></th>
                                        <th><spring:message code="settings.priceCurrency" text="화폐단위" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${list}" var="item" varStatus="status">
                                    <tr>
                                        <td class="t_center">
                                            <input type="checkbox" name="pickChk" class="pickChk" 
                                                data-origin="${item.origin}" 
                                                data-couriercom="${item.courierCompany}" 
                                                data-courier="${item.courier}" 
                                                data-mcode="${item.masterCode}" 
                                                data-amount="${item.payment}"
                                                data-goods="${item.goods}"
                                                data-orderdate="${item.orderDate}"
                                                data-rankprice="${item.rankPrice}" 
                                                data-country="${item.buyerCountryCode}" 
                                                id="pickup_${item.masterCode}">
                                            <label for="pickup_${item.masterCode}"><span class="icon_ck" ></span></label>
                                        </td>
                                        <td class="t_center"><p>${item.masterCode}</p></td>
                                        <td class="t_center"><p>${item.orderDate}</p></td>
                                        <td><p>${item.goods}</p></td>
                                        <td class="t_center"><p>${item.buyerLastname} ${item.buyerFirstname}</p></td>
                                        <td class="t_center"><p>${item.courier}</p></td>
                                        <td class="t_right"><p>${item.paymentStr}</p></td>
                                        <td class="t_center"><p>KRW</p></td>
                                    </tr>
                                </c:forEach>   
                                </tbody>
                            </table>
                            </c:otherwise>
                        </c:choose>
                        </div>
                        <p id="blankLine">&nbsp;</p>
                        <ul class="pick_list pickupService" id="pickupService" style="display:none"></ul> <!-- 픽업 박스 보이는 곳 -->
                        <div id="tempLocalHtml" style="display:none">
                            <li data-localcode="#localService#">
                                <h4 class="sub_h4">#localServiceName#</h4>
                                <div class="h_scroll subDiv">
                                    <c:set var="boxsize"><spring:message code="shipment.popup.payment.boxsize" text="박스사이즈" /></c:set>
                                    <b>${boxsize} :</b> 
                                    <c:if test="${not empty lglPickup}">
	                                    <select class="box_size" name="box_size" id="B010020" style="display:none">
	                                        <option value="" data-price="0" data-comcode="">== ${boxsize} ==</option>
		                                    <c:forEach items="${lglPickup}" var="item" varStatus="status" >
		                                        <option value="${item.zone}" data-price="${item.price}" data-comcode="${item.id}">${item.codeName}</option>
		                                    </c:forEach>
	                                    </select>
	                                </c:if>
	                                <c:if test="${not empty postPickup}">
	                                    <select class="box_size" name="box_size" id="B010010" style="display:none">
	                                        <option value="" data-price="0" data-comcode="">== ${boxsize} ==</option>
		                                    <c:forEach items="${postPickup}" var="item" varStatus="status">
		                                        <option value="${item.zone}" data-price="${item.price}" data-comcode="${item.id}">${item.codeName}</option>
		                                    </c:forEach>
	                                    </select>
	                                </c:if>
	                                <c:if test="${not empty pantosPickup}">
                                        <select class="box_size" name="box_size" id="B010030" style="display:none">
                                            <option value="" data-price="0" data-comcode="">== ${boxsize} ==</option>
                                            <c:forEach items="${pantosPickup}" var="item" varStatus="status">
                                                <option value="${item.zone}" data-price="${item.price}" data-comcode="${item.id}">${item.codeName}</option>
                                            </c:forEach>
                                        </select>
                                    </c:if>
                                    <div class="deliveryAreaZone">
                                    </div>
                                </div>
                            </li>
                        </div>
                        <div id="tempDeliveryHtml" style="display:none">
                                <div class="pickon" data-mastercode="#masterCode#">
                                    <table class="tbtype">
                                    <colgroup>
                                        <col class="cper2000">
                                        <col class="cperauto">
                                        <col class="cper2000">
                                        <col class="cperauto">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th><spring:message code="shipment.shipmentCode" text="배송번호" /></th>
                                            <td colspan="3" onclick="viewDetail(this)" class="list-edit"><a href="#" onclick="return false;">#masterCode#</a></td>
                                        </tr>
                                    <tbody class="off pick-off">
                                        <tr>
                                            <th><spring:message code="shipment.productName" text="상품명" /></th>
                                            <td colspan="3"><p>#goods#</p></td>
                                        </tr>
                                        <tr>
                                            <th><spring:message code="shipment.popup.delivery.price" text="배송비" /></th>
                                            <td><p>#price#</p></td>
                                            <th><spring:message code="order.orderDate" text="주문일자" /></th>
                                            <td><p>#orderDate#</p></td>
                                        </tr>
                                    </tbody>
                                    </thead>
                                    </table>
                                </div>
                            </div>
                            
                        <form name="payfrm" id="payfrm" method="post">
                        <input type="hidden" id="masterCode" name="masterCode" value="${detail.masterCode}"> <!-- 222,2222 -->
                        <input type="hidden" id="masterCodeList" name="masterCodeList" value="${detail.masterCode}"> <!-- 222,2222 -->
                        <input type="hidden" id="payment" name="payment" value="${detail.payment}">  <!-- payment -->
                        <input type="hidden" id="deliveryAmount" name=deliveryAmount value="${detail.deliveryAmount}">   <!-- localPayment -->
                        <input type="hidden" id="paymentVat" name="paymentVat">                 <!-- popLocalPaymentVat -->
                        <input type="hidden" id="rankPrice" name="rankPrice" value="${detail.rankPrice}">  <!--salePayment-->
                        <input type="hidden" id="paymentTotal" name="paymentTotal" value="${detail.paymentTotal}"> <!--totalPayment-->
                        <input type="hidden" id="deliveryCompany" name="deliveryCompany">    <!-- 택배 111,1111 -->
                        <input type="hidden" id="deliveryCompanyCode" name="deliveryCompanyCode">  <!-- 택배 111,1111 -->
                        <input type="hidden" id="boxSize" name="boxSize">                      <!-- 택배 A,B -->
                        <input type="hidden" id="paySelect" name="paySelect" value="bank">
                        <input type="hidden" id="krCnt" value="${krCnt}">
                        <div class="wrap_col cper100p mt45">
                            <div class="colA">
                                <h4 class="sub_h4"><spring:message code="shipment.list.payment.type" text="결제수단" /></h4>
                                <div class="paybox hg">
                                    <c:choose>
                                      <c:when test="${detail.payMethodSetting == '3'}">
                                       <div class="boxbg">
                                            <input type="radio" id="payPaypal" name="payMethod" value="paypal" checked ><label for="credit"><span class="icon_ra"></span><span class="label_txt bold"><spring:message code="shipment.popup.payment.method.paypal" text="페이팔" /></span></label>
                                            <input type="radio" id="payCash" name="payMethod" value="cash"  checked="checked"><label for="bank"  ><span class="icon_ra"></span><span class="label_txt bold"><spring:message code="shipment.popup.payment.method.cash" text="무통장 입금" /></span></label>
                                            <span style="display:none">
                                            <input type="radio" id="payEtc" name="payMethod"><label for="paypal" ><span class="icon_ra"></span><span class="label_txt bold"><spring:message code="shipment.popup.payment.method.etc" text="기타" /></span></label>
                                            </span>
                                       </div>
                                       <dl class="t_center ">
                                                 <img src="/img/common/paypal.png" id="payChoice"  />
                                       </dl>    
                                      </c:when>
                                      <c:when test="${detail.payMethodSetting == '1'}">
                                        <div class="boxbg">
                                            <input type="radio" id="payPaypal" name="payMethod" value="paypal" ><label for="credit"><span class="icon_ra"></span><span class="label_txt bold"><spring:message code="shipment.popup.payment.method.paypal" text="페이팔" /></span></label>
                                        </div>
                                        <dl class="t_center ">
                                                <img src="/img/common/paypal.png"  />
                                        </dl>
                                      </c:when>
                                      <c:when test="${detail.payMethodSetting == '2'}">
                                         <div class="boxbg">
                                            <input type="radio" id="payCash" name="payMethod" value="cash" checked="checked"><label for="bank"  ><span class="icon_ra"></span><span class="label_txt bold"><spring:message code="shipment.popup.payment.method.cash" text="무통장 입금" /></span></label>
                                         </div>
                                         <dl class="t_center">
                                                <img src="/img/common/firm_banking.png"  />
                                         </dl>
                                      </c:when>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="colB">
                                <h4 class="sub_h4"><spring:message code="shipment.popup.payment.total.price" text="결제 내역" /></h4>
                                <table class="tbtype mt10">
                                    <colgroup>
                                        <col class="cper2500">
                                        <col class="cper2500">
                                        <col class="cper2500">
                                        <col class="cper2500">
                                    </colgroup>
                                <thead>
                                    <tr>
                                        <th><strong><spring:message code="shipment.payment.part" text="구분" /></strong></th>
                                        <th><strong><spring:message code="shipment.payment.price" text="금액" /></strong></th>
                                        <th><strong><spring:message code="admin.statis.total.subTitle4" text="부가세" /></strong></th>
                                        <th><strong><spring:message code="shipment.payment.total" text="합계" /></strong></th>
                                    </tr>
                                </thead>
                                <tbody class="paybox amountBox">
                                    <tr id="trOverseas" style="display:none">
							            <td class="t_center"><spring:message code="shipment.popup.payment.total.price.delivery" text="할인 배송금액" /></td>
                                        <td class="t_right"><span class="cost amount1">0</span></td>
                                        <td class="t_right"><span class="cost">0</span></td>
                                        <td class="t_right"><span class="cost amount1">0</span></td>
                                    </tr>
                                    <tr id="trLocal" class="pickupService" style="display:none">
                                        <td class="t_center"> <spring:message code="shipment.popup.payment.total.price.home" text="국내 택배요금" /> </td>
                                        <td class="t_right"><span class="cost amount4">0</span></td>
                                        <td class="t_right"><span class="cost amount4_vat">0</span></td>
                                        <td class="t_right"><span class="cost amount4_tot">0</span></td>
                                    </tr>
                                    <tr class="totalServicePayment" >
                                        <td class="t_center"><strong><spring:message code="shipment.popup.payment.total.price.plan" text="총 결제금액" /></strong></td>
                                        <td class="t_right"><strong><span class="cost amount3_sum">0</span></strong></td>
                                        <td class="t_right"><strong><span class="cost amount3_vat">0</span></strong></td>
                                        <td class="t_right"><strong><span class="cost amount3">0</span></strong></td>
                                    </tr>
                                </tbody>
                                </table>
                                
                            </div>
                        </div>
                        </form>
                    <div class="pop_btn">
                        <!-- <button type="button" class="btn_type2 btnPaymentProc sendPayment">결제</button> -->
                        <button type="button" class="btn_type2 sendPayment"><spring:message code="shipment.popup.button.payment" text="결제" /></button>
                    </div>
                </div>