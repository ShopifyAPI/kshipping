<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
<!-- 
**************************************************
화면코드 : UI_ADM_222_POP
기능설명 : 주문 > 팝업 > 출고지&배송지 선택/등록
Author   Date         Description
jwh      2020-01-29   First Draft
**************************************************
-->

<style type="text/css">
.form_side dl dt {
	margin-top: .5rem !important;
}
.mt10p {
	margin-top: 10px !important;
}
.likeForm {
	height: 35px;
	line-height: 34px;
	border: 1px solid #DDD;
	color: #000;
	background-color: #F4F5F7;
	font-family: arial;
	font-size: 15px;
	padding: 0px 13px;
}
</style>

<c:if test="${status == 'no'}">
	<script type="text/javascript">
		callPopupError();
	</script>
</c:if>

<div class="pop_head">
	<h3>
		<spring:message code="order.popup.address.title" text="배송정보" />
	</h3>
	<button type="button" class="btn_close"></button>
</div>

<div class="pop_body" style="max-height: 900px !important;">
	<form name="frm" method="get" id="ajax-popForm">
		<input type="hidden" name="sellerPhone01" id="sellerPhone01" value="${address.sellerPhone01}" />
		<input type="hidden" name="sellerPhone02" id="sellerPhone02" value="${address.sellerPhone02}" />
		<input type="hidden" name="sellerPhone03" id="sellerPhone03" value="${address.sellerPhone03}" />
		<input type="hidden" name="sellerPhone04" id="sellerPhone04" value="${address.sellerPhone04}" />
		<input type="hidden" name="sellerZipCode" id="postcode" value="${address.sellerZipCode}" data-label="<spring:message code="order.popup.address.address1" text="우편번호" />" data-required="Y" />
		<input type="hidden" name="sellerAddr1" id="sellerAddr1" value="${address.sellerAddr1}" data-label="<spring:message code="order.popup.address.address2" text="주소" />" data-required="Y" />
		<input type="hidden" name="sellerAddr2" id="detailAddress" value="${address.sellerAddr2}" data-label="<spring:message code="order.popup.address.address3" text="상세주소" />" data-required="Y" />
		<input type="hidden" name="sellerAddr1Ename" id="sellerAddr1Ename" value="${address.sellerAddr1Ename}" data-label="<spring:message code="order.popup.address.address2.ename" text="주소" />" data-required="Y" data-format="english" />
		<input type="hidden" name="sellerAddr2Ename" id="sellerAddr2Ename" value="${address.sellerAddr2Ename}" data-label="<spring:message code="order.popup.address.address3.ename" text="상세주소" />" data-required="Y" data-format="english" />
		<input type="hidden" name="sellerProvince" id="sellerProvince" value="${address.sellerProvince}" data-label="<spring:message code="order.popup.address.address.province" text="Province" />" data-required="Y" />
		<input type="hidden" name="sellerCity" id="sellerCity" value="${address.sellerCity}" data-label="<spring:message code="order.popup.address.address.city" text="City" />" data-required="Y" />
		<input type="hidden" name="buyerFirstname" value="${address.buyerFirstname}" data-label="<spring:message code="order.popup.address.firstName" text="이름" />" data-required="Y">
		<input type="hidden" name="buyerLastname" value="${address.buyerLastname}" data-label="<spring:message code="order.popup.address.lastName" text="이름(성)" />">
		<%-- <input type="hidden" name="buyerPhone" value="${address.buyerPhone}" data-format="worldPhone" data-label="<spring:message code="order.popup.address.phone" text="전화번호" />" data-required="Y" /> --%>
		<input type="hidden" name="buyerEmail" value="${address.buyerEmail}" data-label="<spring:message code="settings.email" text="이메일" />" data-format="email" />
		<input type="hidden" name="buyerCountryCode" value="${address.buyerCountryCode}" data-label="<spring:message code="order.popup.address.country" text="국가" />" data-required="Y" />
		<input type="hidden" name="buyerAddr1" value="${address.buyerAddr1}" data-label="<spring:message code="order.popup.address.address2" text="주소" />" data-required="Y" />
		<input type="hidden" name="buyerAddr2" value="${address.buyerAddr2}" />
		<input type="hidden" name="buyerCity" value="${address.buyerCity}" data-label="<spring:message code="order.popup.address.city" text="시" />" data-required="Y">
		<input type="hidden" name="buyerProvince" value="${address.buyerProvince}" />
		<input type="hidden" name="buyerZipCode" value="${address.buyerZipCode}" />
		<input type="hidden" name="proc" value="" />
		<input type="hidden" name="sellerCountry" value="KOREA" />
		<input type="hidden" name="sellerCountryCode" value="KR" />
		<input type="hidden" name="sellerName" value="${address.sellerName}" />
		<input type="hidden" name="masterCode" value="${address.masterCode}" />
		<input type="hidden" name="orderCode" value="${address.orderCode}" />
		<input type="hidden" name="shopIdx" value="${address.shopIdx}" />
		<input type="hidden" name="orderIdx" value="${address.orderIdx}" />
		<input type="hidden" name="orderDate" value="${address.orderDate}" />
		<input type="hidden" name="weight" id="weight" value="1" />
		<input type="hidden" name="weightUnit" id="weightUnit" value="g" />
		<input type="hidden" name="totalWeight" id="totalWeight" value="0.1" />
		<input type="hidden" name="totalWeightUnit" id="totalWeightUnit" value="g" />
		<input type="hidden" name="courierCompany" value="${courier.courierCompany}" />
		<input type="hidden" name="courier" value="${courier.courier}" />
		<input type="hidden" name="payment" value="${courier.payment}" />
		<input type="hidden" name="courierId" value="${courier.courierId}" />
		<input type="hidden" name="rankPrice" value="${courier.rankPrice}" />
		<input type="hidden" name="paymentCode" value="NA" />
		<input type="hidden" name="payState" value="N" />
		<input type="hidden" name="defCourierId" value="${defaultCourier.courierId}" />
		<input type="hidden" name="defBoxType" value="${defaultCourier.boxType}" />
		<input type="hidden" name="boxType" value="${defaultCourier.boxType}" />
		<input type="hidden" name="boxPrice" />

		<div class="wrap_col">
			<div class="colA">
				<h4 class="sub_h4">
						<spring:message code="order.popup.address.sender" text="보내는 사람" />
					</h4>
				<div class="form_side mt10p">
					<dl>
						<dt>
							<p class="required"><spring:message code="settings.sender.name" text="출고지명" /></p>
						</dt>
						<dd>
						    <select name="selectSender" id="selectSenderKr" class="cper100p" data-label="" data-code="">
								<option data-name="" 
									data-phonenumber=""
								    data-phonenumber01="" 
								    data-phonenumber02="" 
								    data-phonenumber03="" 
								    data-phonenumber04="" 
								    data-zipcode="" 
								    data-addr1=""
								    data-addr2=""
								    data-addr1ename=""
								    data-addr2ename=""
								    data-province=""
								    data-city=""
								    data-shopidx="" 
								    value="">==<spring:message code="settings.sender.name" text="출고지명" />==</option>
								<c:forEach items="${sellerAddrList}" var="list">
									<option data-name="${list.name}"
										data-phonenumber="${list.phoneNumber}"
										data-phonenumber01="${list.phoneNumber01}"
										data-phonenumber02="${list.phoneNumber02}" 
										data-phonenumber03="${list.phoneNumber03}" 
										data-phonenumber04="${list.phoneNumber04}"  
										data-zipcode="${list.zipCode}" 
										data-addr1="${list.addr1}"
										data-addr2="${list.addr2}"
										data-addr1ename="${list.addr1Ename}"
										data-addr2ename="${list.addr2Ename}"
										data-province="${list.province}"
										data-city="${list.city}"
										data-shopidx="${list.shopIdx}"
										value="${list.senderIdx}"
										<c:if test="${list.senderIdx == address.selectSender}"> selected</c:if>>
										${list.senderTitle}
									</option>
								</c:forEach>
						    </select>                                                
						</dd>
						<dt>
							<p><spring:message code="order.popup.address.phone" text="전화번호" /></p>
						</dt>
						<dd>
							<div class="likeForm cper100p" name="sellerPhone" id="sellerPhone">
								<c:if test="${empty address.sellerPhone}">
									${address.sellerPhone01}-${address.sellerPhone02}-${address.sellerPhone03}-${address.sellerPhone04}
								</c:if>
								<c:if test="${not empty address.sellerPhone}">
									${address.sellerPhone}
								</c:if>
							</div>
						</dd>
						<dt><p><spring:message code="order.popup.address.address4" text="출고지 주소" /></p></dt>
						<dd>
							<div class="likeForm cper100p" name="sellerAddr" id="sellerAddr">(${address.sellerZipCode}) ${address.sellerAddr1}</div>
							<div class="likeForm cper100p" name="sellerAddrDetail" id="sellerAddrDetail">${address.sellerAddr2}</div>
						</dd>
					</dl>
				</div>
			</div>
			<div class="colB">
				<h4 class="sub_h4">
					<spring:message code="order.popup.address.receiver" text="받는 사람" />
				</h4>
				<div class="form_side mt10p">
					<dl>
						<dt>
							<p><spring:message code="order.popup.address.firstName" text="이름" /> / <spring:message code="settings.email" text="이메일" /></p>
						</dt>
						<dd>
							<div class="row">
								<div class="likeForm cper5000">${address.buyerFirstname} ${address.buyerLastname}</div>
								<div class="likeForm cper5000">${address.buyerEmail}</div>
							</div>
						</dd>
						<dt>
						<c:set var="buyerPhone"><spring:message code="order.popup.address.phone" text="전화번호" /></c:set>
                            <p>${buyerPhone} </p>
						</dt>
						<dd>
							<input type="text" class="cper100p" name="buyerPhone" value="${address.buyerPhone}" Placeholder="${buyerPhone}" 
                                                    data-format="worldPhone" data-label="${buyerPhone}" data-required="Y">
						</dd>
						<dt>
							<p><spring:message code="order.popup.address.title4" text="주소" /></p>
						</dt>
						<dd>
							<div class="likeForm cper100p">(${address.buyerZipCode}) ${address.buyerProvince} ${address.buyerCity} ${address.buyerAddr1}</div>
							<div class="likeForm cper100p">${address.buyerAddr2}</div>
						</dd>
					</dl>
				</div>
			</div>
		</div>
		
		<div class="wrap_col">
			<div class="colA">
				<div class="mt20">
					<h4 class="sub_h4">
						<spring:message code="order.popup.customer.title1" text="배송 서비스 선택" />
					</h4>
				</div> 
				<div class="wrap_col">
					<table class="tbtype tbsmall mt10">
						<colgroup>
							<col class="cperauto">
							<col calss="cper100p">
							<col calss="cper100p">							
							<col class="cper1250">
						</colgroup>
						<thead>
							<tr>
								<th><spring:message code="order.popup.customer.title2" text="서비스" /></th>
								<th><c:set var="boxType"><spring:message code="shipment.popup.payment.boxsize" text="박스사이즈"/></c:set>
									<p calss="required">${boxType}</th>								
								<th><c:set var="boxPrice"><spring:message code="order.popup.customer.title3" text="배송료" /></c:set>
									<p calss="required">${boxPrice}</th>
								<th><spring:message code="order.popup.customer.title4" text="선택" /></th>
							</tr>
						</thead>
						<tbody class="courier-area">
							<c:if test="${fn:length(courierList) == 0}">
								<tr><td colspan="5" class="t_center"><spring:message code="order.popup.customer.nolist" text="해당하는 국가의 국제 특송업체가 존재 하지 않습니다." /></td></tr>
							</c:if>
							<c:if test="${fn:length(courierList) > 0}">
								<c:forEach items="${courierList}" var="item">
									<tr class="courier" data-courier="${item.code}" data-courierid="${item.courierId}" data-id="${item.comCode}" data-price="${item.price}" data-rankprice="${item.rankPrice}">
										<td>
											<p>${item.codeName}</p>
										</td>
										
										<td>																	
											<select name="selectBoxType">
												<option value="" data-courier-id="" data-price="">== ${boxType} ==</option>
												<c:forEach items="${boxTypeList}" var="box">
												    <c:if test="${item.courierId == box.courierId}">
												        <option value ="${box.zone}" data-courier-id="${box.courierId}" data-price="${box.price}">${box.boxType}</option>
												    </c:if>
												</c:forEach>
											</select>	
                                    	</td>
                                    	<td>
                                    		<input name="boxPrice" type="text" >
                                    	</td>
										<td class="t_center">
											<button type="button" class="btn_select"><spring:message code="button.select" text="선택" /></button>
										</td>	
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
		<div class="mt20">
			<h4 class="sub_h4">관세정보</h4>
			<div class="sect_right"></div>
		</div>
		<div class="scr_x mt10">
			<table class="tbtype boxlist">
				<colgroup>
					<col class="wp150" />
					<col class="cperauto" />
					<col class="wp90" />
					<col class="wp90" />
					<col class="wp90" />
					<col class="wp90" />
					<col class="wp90" />
				</colgroup>
				<thead>
					<tr>
						<th><span class="required">상품코드</span></th>
						<th>상품명</th>
						<th colspan="2"><span class="required">무게</span></th>
						<th colspan="2"><span class="required">가격</span></th>
						<th><span class="required">수량</span></th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty skuList}">
						<tr>
							<td colspan="99" class="t_center">
								관세 정보를 찾을 수 없습니다.
							</td>
						</tr>
					</c:if>
					<c:if test="${not empty skuList}">
						<c:forEach items="${skuList}" var="item" varStatus="status">
							<tr>
								<td>
									<input type="text" class="" name="arrGoodsCode" id="goodsCode_${status.count}" value="${item.goodsCode}" maxlength="20" data-required="Y" data-label="<spring:message code="settings.itemCode" text="상품코드" />" placeholder="<spring:message code="settings.itemCode" text="상품코드" />" />
									<input type="hidden" name="arrSkuIdx" id="skuIdx_${status.count}" value="${item.skuIdx}" />
									<input type="hidden" name="arrGoodsItemId" id="goodsItemId_${status.count}" value="${item.goodsItemId}" />
									<input type="hidden" name="arrGoodsSku" id="goodsSku_${status.count}" value="${item.goodsSku}" />
									<input type="hidden" name="arrOrigin" id="origin_${status.count}" value="${item.origin}" />
									<input type="hidden" name="arrHscode" id="hscode_${status.count}" value="${item.hsCode}" />
								</td>
								<td>
									<input type="text" class="" name="arrGoods" id="goods_${status.count}" value="${item.goods}" maxlength="100" data-required="Y" data-label="<spring:message code="settings.itemName" text="상품명" />" placeholder="<spring:message code="settings.itemName" text="상품명" />" />
									<input type="hidden"class="" name="arrGoodsType" id="goodsType_${status.count}" value="${item.goodsType}"  data-label="<spring:message code="settings.itemType" text="상품타입" />" />
								</td>
								<td class="weight-data" data-weight="${item.weight}" data-unit="${item.weightUnit}" data-quantity="${item.quantity}">
									<input type="text" class="t_right" name="arrWeight" id="weight_${status.count}" value="<c:if test="${item.weight != ''}">${item.weight}</c:if>" maxlength="4" data-required="Y" data-format="num" data-label="<spring:message code="settings.weight" text="무게" />" placeholder="<spring:message code="settings.weight" text="무게" />" onchange="setTotalWeight()">
								</td>
								<td>
									<input type="text" class="t_right" name="arrWeightUnit" id="weightUnit_${status.count}" value="${item.weightUnit}" data-required="Y" data-label="<spring:message code="settings.weightUnit" text="무게단위"  />" onchange="setTotalWeight()" />
								</td>
								<td>
									<input type="text" class="t_right" name="arrUnitCost" id="unitCost_${status.count}" value="<c:if test="${item.unitCost gt 0.0}">${item.unitCost}</c:if>" maxlength="7" data-required="Y" data-format="num" data-label="<spring:message code="settings.itemPrice" text="가격" />" placeholder="<spring:message code="settings.itemPrice" text="가격" />">
								</td>
								<td>
									<input type="text" class="t_center" name="arrPriceCurrency" id="priceCurrency_${status.count}" value="${item.priceCurrency}" maxlength="3" data-required="Y" data-format="" data-label="<spring:message code="settings.priceCurrency" text="화폐단위" />" placeholder="<spring:message code="settings.priceCurrency" text="화폐단위" />">
								</td>
								<td>
									<input type="text" class="t_center" name="arrQuantity" id="quantity_${status.count}" value="<c:if test="${item.quantity > 0}">${item.quantity}</c:if>" maxlength="5" data-required="Y" data-format="num" data-label="<spring:message code="order.popup.express.box.quantity" text="수량" />" placeholder="<spring:message code="order.popup.express.box.quantity" text="수량" />" onchange="setTotalWeight()">
								</td>
							</tr>
						</c:forEach>  
					</c:if>
				</tbody>
			</table>
		</div>

		<div class="wrap_col">
			<div class="pop_adjacent">
				<button type="button" class="btn_type2" id="saveDomesticBtn"><spring:message code="button.save" text="저장" /></button>
			</div>
		</div>

	</form>
</div>