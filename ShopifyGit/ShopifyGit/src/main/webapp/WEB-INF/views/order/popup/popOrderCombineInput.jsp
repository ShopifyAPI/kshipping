<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>  
<!-- 
**************************************************
화면코드 : UI_ADM_222_POP
기능설명 : 주문 > 팝업 > 출고지&배송지 선택/등록
Author   Date         Description
jwh      2020-01-29   First Draft
**************************************************
 -->
<c:choose>
	<c:when test="${status == 'no'}">
	
	<script type="text/javascript">
	callPopupError();
    </script>
	
	</c:when>
	<c:otherwise>
                                                    

                <div class="pop_head">
                    <h3><spring:message code="order.popup.address.title" text="배송정보" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                    
	                <div id="popCombineStep01" class="combine-view combine-view-on">
	                
	                    <form name="frmCombineStep01" method="get" id="frmCombineStep01">
		                <ul class="step2 col3">
	                        <li class="title on"><spring:message code="order.popup.address.title1" text="주소등록" /></li>
	                        <li class="title"><spring:message code="order.popup.address.title2" text="관세정보" /></li> 
	                        <li class="title"><spring:message code="order.popup.address.title3" text="배송정보" /></li>
	                    </ul>
	                    <ul class="tab_conts">
	                    
	                        <li>
	                            <div class="wrap_col">
	                                <div class="colA">
	                                    <h4 class="sub_h4"><spring:message code="order.popup.address.sender" text="보내는 사람" /></h4>
	                                    <div class="form_sect">
	                                        <dl>
	                                            <dt>
	                                                <c:set var="sender"><spring:message code="settings.sender.name" text="출고지명" /></c:set>
	                                                <p class="required">${sender}</p>
	                                            </dt>
	                                            <dd>
	                                                <select name="selectSender" id="selectSender" class="cper100p" data-label="${sender}" data-code="">
	                                                    <option data-name="" 
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
	                                                        value="">==${sender}==</option>
	                                                    <c:if test="${fn:length(sellerAddrList) > 0}">
	                                                        <c:forEach items="${sellerAddrList}" var="list">
	                                                            <option data-name="${list.name}" 
	                                                                data-phonenumber01="${list.phoneNumber01}"
	                                                                data-phonenumber02="${list.phoneNumber02}" 
	                                                                data-phonenumber03="${list.phoneNumber03}" 
	                                                                data-phonenumber04="${list.phoneNumber04}"  
	                                                                data-zipcode="${list.zipCode}" 
	                                                                data-addr1="${list.addr1}"
	                                                                data-addr2="${list.addr2}"
	                                                                data-shopidx="${list.shopIdx}"
	                                                                data-addr1ename="${list.addr1Ename}"
                                                                	data-addr2ename="${list.addr2Ename}"
                                                                	data-province="${list.province}"
                                                                	data-city="${list.city}"
	                                                                data-shopidx="${list.shopIdx}"
	                                                                value="${list.senderIdx}"
	                                                                <c:if test="${list.senderIdx == address.selectSender}"> selected</c:if>>${list.senderTitle}</option>
	                                                        </c:forEach>
	                                                    </c:if>
	                                                </select>                                                
	                                            </dd>
	                                            <dt>
	                                                <c:set var="phone"><spring:message code="order.popup.address.phone" text="전화번호" /></c:set>
	                                                <p class="required">
	                                                    ${phone}
	                                                    <c:if test="${address.sellerPhone04 == '' or address.sellerPhone04 == null}" >
	                                                        <c:if test="${address.sellerPhone != '' and address.sellerPhone != null}" >[${address.sellerPhone}]</c:if>
	                                                    </c:if>
	                                                </p>
	                                            </dt>
	                                            <dd>
	                                                <div class="phone4">
	                                                    <c:set var="phoneText01"><spring:message code="common.phone.01" text="국가번호" /></c:set>
	                                                    <c:set var="phoneText02"><spring:message code="common.phone.02" text="국번" /></c:set>
	                                                    <c:set var="phoneText03"><spring:message code="common.phone.03" text="중간자리" /></c:set>
	                                                    <c:set var="phoneText04"><spring:message code="common.phone.04" text="끝자리" /></c:set>
	                                                    <input type="text" name="sellerPhone01" id="sellerPhone01" maxlength="4" value="${address.sellerPhone01}"
	                                                        data-format="number" data-required="Y" data-label="${phoneText01}" placeholder="${phoneText01}">
	                                                    <input type="text" name="sellerPhone02" id="sellerPhone02" maxlength="5" value="${address.sellerPhone02}"
	                                                        data-format="number" data-required="Y" data-label="${phoneText02}" placeholder="${phoneText02}">
	                                                    <input type="text" name="sellerPhone03" id="sellerPhone03" maxlength="5" value="${address.sellerPhone03}"
	                                                        data-format="number" data-required="Y" data-label="${phoneText03}" placeholder="${phoneText03}">
	                                                    <input type="text" name="sellerPhone04" id="sellerPhone04" maxlength="5" value="${address.sellerPhone04}"
	                                                        data-format="number" data-required="Y" data-label="${phoneText04}" placeholder="${phoneText04}">
	                                                </div>
	                                            </dd>
                                            <dt>
                                                <p class="required"><spring:message code="order.popup.address.address4" text="출고지 주소" /></p>
                                            </dt>
                                            <dd>
                                                <div class="zipcode">
                                                    <c:set var="postcode"><spring:message code="order.popup.address.address1" text="우편번호" /></c:set>
                                                    <input type="text" class="postcode" name="sellerZipCode" id="postcode" value="${address.sellerZipCode}" readOnly placeholder="${postcode}"  
                                                        data-label="${postcode}" data-required="Y">
                                                    <a href="#" class="btn_search postcode" onclick="return false;"></a>
                                                </div>
                                                <c:set var="sellerAddr1"><spring:message code="order.popup.address.address2" text="주소" /></c:set>
                                                <input type="text" class="cper100p mt5 readonly" name="sellerAddr1" id="sellerAddr1" value="${address.sellerAddr1}" readOnly placeholder="${sellerAddr1}" text="${sellerAddr1}" 
                                                    data-label="${sellerAddr1}" data-required="Y">
                                                <c:set var="sellerAddr2"><spring:message code="order.popup.address.address3" text="상세주소" /></c:set>
                                                <input type="text" class="cper100p mt5" name="sellerAddr2" id="detailAddress" value="${address.sellerAddr2}"  placeholder="${sellerAddr2}"  text="${sellerAddr2}"
                                                    data-label="${sellerAddr2}" >
                                            </dd>
                                            
                                            <dt>
                                                <p class="required"><spring:message code="order.popup.address.address4.ename" text="영문주소" /></p>
                                            </dt>
                                            <dd>
                                                <c:set var="sellerAddr1Ename"><spring:message code="order.popup.address.address2.ename" text="주소" /></c:set>
                                                <input type="text" class="cper100p mt5 readonly" name="sellerAddr1Ename" id="sellerAddr1Ename" value="${address.sellerAddr1Ename}" readOnly placeholder="${sellerAddr1Ename}" text="${sellerAddr1Ename}" 
                                                    data-label="${sellerAddr1Ename}" data-required="Y">
                                                <c:set var="sellerAddr2Ename"><spring:message code="order.popup.address.address3.ename" text="상세주소" /></c:set>
                                                <input type="text" class="cper100p mt5" name="sellerAddr2Ename" id="sellerAddr2Ename" value="${address.sellerAddr2Ename}"  placeholder="${sellerAddr2Ename}"  text="${sellerAddr2Ename}"
                                                    data-label="${sellerAddr2Ename}" data-required="Y" data-format="english">
                                                <c:set var="sellerProvince"><spring:message code="order.popup.address.address.province" text="Province" /></c:set>
                                                <input type="text" class="cper100p mt5 readonly" name="sellerProvince" id="sellerProvince" value="${address.sellerProvince}" readOnly placeholder="${sellerProvince}" text="${sellerProvince}" 
                                                    data-label="${sellerProvince}" >
                                                <c:set var="sellerCity"><spring:message code="order.popup.address.address.city" text="City" /></c:set>
                                                <input type="text" class="cper100p mt5 readonly" name="sellerCity" id="sellerCity" value="${address.sellerCity}" readOnly placeholder="${sellerCity}"  text="${sellerCity}"
                                                    data-label="${sellerCity}" >
                                            </dd>
                                            
                                        </dl>
                                    </div>
                                </div>	                                
                                <div class="colB">
	                                    <h4 class="sub_h4"><spring:message code="order.popup.address.receiver" text="받는 사람" /></h4>
	                                    <div class="form_sect">
	                                        <dl>
	                                            <dt><p class="required"><spring:message code="order.popup.address.firstName" text="이름" /></p></dt>
	                                            <dd>
	                                                <div class="row">
	                                                    <c:set var="buyerFirstname"><spring:message code="order.popup.address.firstName" text="이름" /></c:set>
	                                                    <input type="text" class="cper5000" name="buyerFirstname" value="${address.buyerFirstname}" placeholder="${buyerFirstname}" 
	                                                        data-label="${buyerFirstname}" data-required="Y">
	                                                    <c:set var="buyerLastname"><spring:message code="order.popup.address.lastName" text="이름(성)" /></c:set>    
	                                                    <input type="text" class="cper5000" name="buyerLastname" value="${address.buyerLastname}" placeholder="${buyerLastname}" 
	                                                        data-label="${buyerLastname}">
	                                                </div>
	                                            </dd>
	                                            <dt>
	                                                <c:set var="buyerPhone"><spring:message code="order.popup.address.phone" text="전화번호" /></c:set>
	                                                <p class="required">
	                                                    ${buyerPhone}
	                                                    <c:if test="${address.buyerPhone04 == '' or address.buyerPhone04 == null}" >
	                                                        <c:if test="${address.buyerPhone != '' and address.buyerPhone != null}" >[${address.buyerPhone}]</c:if>
	                                                    </c:if>
	                                                </p>
	                                            </dt>
	                                            <dd>
                                                <input type="text" class="cper100p" name="buyerPhone" value="${address.buyerPhone}" Placeholder="${buyerPhone}" 
                                                    data-format="worldPhone" data-label="${buyerPhone}" data-required="Y"">
                                            	</dd>
<%-- 	                                                <div class="phone4">
	                                                    <input type="text" name="buyerPhone01" id="buyerPhone01" maxlength="4" value="${address.buyerPhone01}"
	                                                        data-format="num" data-required="Y" data-label="${phoneText01}" placeholder="${phoneText01}">
	                                                    <input type="text" name="buyerPhone02" id="buyerPhone02" maxlength="5" value="${address.buyerPhone02}"
	                                                        data-format="num" data-required="Y" data-label="${phoneText02}" placeholder="${phoneText02}">
	                                                    <input type="text" name="buyerPhone03" id="buyerPhone03" maxlength="5" value="${address.buyerPhone03}"
	                                                        data-format="num" data-required="Y" data-label="${phoneText03}" placeholder="${phoneText03}">
	                                                    <input type="text" name="buyerPhone04" id="buyerPhone04" maxlength="5" value="${address.buyerPhone04}"
	                                                        data-format="num" data-required="Y" data-label="${phoneText04}" placeholder="${phoneText04}">
	                                                </div> --%>
	                                                <%-- <input type="text" class="cper100p" name="buyerPhone" value="${address.buyerPhone}" Placeholder="${buyerPhone}" 
	                                                    data-format="phone" data-label="${buyerPhone}" data-required="Y"> --%>
	                                            </dd>
	                                            <dt>
	                                                <c:set var="buyerEmail"><spring:message code="settings.email" text="이메일" /></c:set>
	                                                <p class="">${buyerEmail}</p>
	                                            </dt>
	                                            <dd>
	                                                <input type="email" class="cper100p" name="buyerEmail" value="${address.buyerEmail}" data-format="email" placeholder="${buyerEmail}">
	                                            </dd>
	                                            <dt><p class="required"><spring:message code="order.popup.address.title4" text="주소" /></p></dt>
	                                            <dd>
	                                                <select class="cper100p select-ajax" name="buyerCountryCode" id="county"  
	                                                    data-code="${address.buyerCountryCode}"  data-codetype="etc" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "F010000" }' 
	                                                    data-label="<spring:message code="order.popup.address.country" text="국가" />" data-required="Y">
	                                                </select>
	                                                
	                                                <c:set var="buyerAddr2"><spring:message code="order.popup.address.address3" text="상세주소" /></c:set>    
	                                                <input type="text" name="buyerAddr2" class="cper100p mt5" value="${address.buyerAddr2}" placeholder="${buyerAddr2}" 
	                                                    data-label="${buyerAddr2}" data-required="Y">
	                                                
	                                                <c:set var="buyerAddr1"><spring:message code="order.popup.address.address2" text="주소" /></c:set>
	                                                <input type="text" name="buyerAddr1" class="cper100p mt5" value="${address.buyerAddr1}" placeholder="${buyerAddr1}" 
	                                                    data-label="${buyerAddr1}" data-required="Y">
	                                                
	                                                <input type="hidden" id="jibunAddress">
	                                                <div class="row mt5">
	                                                    <c:set var="buyerCity"><spring:message code="order.popup.address.city" text="시" /></c:set>   
	                                                    <input type="text" name="buyerCity" class="cper5000" value="${address.buyerCity}" placeholder="${buyerCity}" data-label="${buyerCity}" >
	                                                    <c:set var="buyerProvince"><spring:message code="order.popup.address.state" text="시/군/구" /></c:set>
	                                                    <input type="text" name="buyerProvince" class="cper5000" value="${address.buyerProvince}" placeholder="${buyerProvince}" data-label="${buyerProvince}" >
	                                                </div>
	                                            </dd>
	                                            <dt>
	                                                <c:set var="buyerZipCode"><spring:message code="order.popup.address.post" text="우편번호" /></c:set>
	                                                <p class="required">${buyerZipCode}</p>
	                                            </dt>
	                                            <dd><input type="text" name="buyerZipCode" class="cper100p mt5" value="${address.buyerZipCode}" Placeholder="${buyerZipCode}" 
	                                                data-label="${buyerZipCode}" data-required="Y"></dd>
	                                        </dl>
	                                    </div>
	                                </div>
	                            </div>
	                        </li>
	
	                        
	                        
	                    </ul>
	                    <div class="pop_btn">
	                        <%-- <button type="button" class="btn_type3" id="prevBtn01" onclick="return false;"><spring:message code="button.prev" text="이전" /></button> --%>
	                        <button type="button" data-area="popCombineStep02" class="btn_type5 btnCombineProc"  name="next" onclick="return false;"><spring:message code="button.next" text="다음" /></button>
	                    </div>
	                    </form>

	                </div>
	                
	                <div id="popCombineStep02" class="combine-view combine-view-off">
	                    <form name="frmCombineStep02" method="get" id="frmCombineStep02">
		                <ul class="step2 col3">
	                        <li class="title"><spring:message code="order.popup.address.title1" text="주소등록" /></li>
	                        <li class="title on"><spring:message code="order.popup.address.title2" text="관세정보" /></li>
	                        <li class="title"><spring:message code="order.popup.address.title3" text="배송정보" /></li>
	                    </ul>
	                    <ul class="tab_conts">
	                        <li>
	                            <h4 class="sub_h4"><spring:message code="order.popup.express.box.title" text="BOX 정보" /></h4>
	                            <div class="form_sect">
	                                <dl class="col2">
	                                    <dt>
	                                        <c:set var="boxSelect"><spring:message code="settings.boxSelect" text="포장재선택" /></c:set>
	                                        <p class="required">${boxSelect}</p>
	                                    </dt>
	                                    <dd>
	                                        <select class="cper100p" name="selectBox" id="boxSelect" data-required="" data-label="">
	                                        <option value="">==<spring:message code="settings.boxSelect" text="포장재선택" />==</option>
	                                        <c:forEach items="${boxList}" var="item" varStatus="status">
	                                            <option data-boxtype="${item.boxType}"
	                                                data-boxlength="${item.boxLength}"
	                                                data-boxwidth="${item.boxWidth}"
	                                                data-boxheight="${item.boxHeight}"
	                                                data-boxunit="${item.boxUnit}"
	                                                data-boxweight="${item.boxWeight}"
	                                                data-weightunit="${item.weightUnit}"
	                                                value="${item.boxIdx}"
	                                                <c:if test="${item.boxIdx == address.selectBox}"> selected</c:if>>${item.boxTitle}</option>
	                                        </c:forEach>
	                                        </select>
	                                    </dd>
	                                    <dt><p class="required"><spring:message code="order.popup.express.box.totalWeight" text="총 무게" /></p></dt>
	                                    <dd>
	                                        <div class="row">
	                                            <input type="text" readonly name="totalWeight" id="totalWeight" class="cper7000 t_right">
	                                            <input type="text" readonly name="totalWeightUnit" id="totalWeightUnit" value="kg" class="cper3000">
	                                        </div>
	                                        <div class="mt20">
	                                            <spring:message code="order.popup.combine.note" text="* 총 무게가 28kg 이상일 경우 배송 거부 사유가 될수 있습니다." />
	                                        </div>
	                                        
	                                        
	                                    </dd>
	                                </dl>
	                                <dl class="col2">
	                                    <dt>
	                                        <c:set var="boxType"><spring:message code="settings.boxType" text="박스타입" /></c:set>
	                                        <p class="required">${boxType}</p>
	                                    </dt>
	                                    <dd>
	                                        <select class="cper100p select-ajax" name="boxType" id="boxType" 
	                                            data-codetype="etc" data-code="${address.boxType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H010000" }' 
	                                            data-required="Y" data-label="${boxType}" ></select>
	                                    </dd>
	                                    <dt>
	                                        <p><p class="required"><spring:message code="order.popup.express.box.size" text="크기" /></p></p>
	                                    </dt>
	                                    <dd>
	                                        <div class="row">
	                                            <c:set var="boxLength"><spring:message code="settings.boxLength" text="길이" /></c:set>
	                                            <input type="text" class="cper2500" name="boxLength" id="boxLength" value="<c:if test="${address.boxLength != ''}">${address.boxLength}</c:if>" 
	                                                maxlength="4" data-required="Y" data-label="${boxLength}" data-format="number" placeholder="${boxLength}">
	                                           
	                                            <c:set var="boxWidth"><spring:message code="settings.boxWidth" text="폭" /></c:set>
	                                            <input type="text" class="cper2500" name="boxWidth" id="boxWidth" value="<c:if test="${address.boxWidth != ''}">${address.boxWidth}</c:if>" 
	                                                maxlength="4" data-required="Y" data-label="${boxWidth}" data-format="number" placeholder="${boxWidth}">
	                                           
	                                            <c:set var="boxHeight"><spring:message code="settings.boxHeight" text="높이" /></c:set>
	                                            <input type="text" class="cper2500" name="boxHeight" id="boxHeight" value="<c:if test="${address.boxHeight != ''}">${address.boxHeight}</c:if>" 
	                                                maxlength="4" data-required="Y" data-label="${boxHeight}" data-format="number" placeholder="${boxHeight}">
	                                               
	                                            <select class="cper2500 select-ajax" name="boxUnit" id="boxUnit" 
	                                                data-codetype="etc" data-code="${address.boxUnit}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H020000" }' 
	                                                data-required="Y" data-label="<spring:message code="settings.boxUnit" text="박스단위" />" ></select>
	                                        </div>
	                                        <div class="row mt5">
	                                            <c:set var="weight"><spring:message code="settings.weight" text="무게" /></c:set>
	                                            <input type="text" class="cper2500" name="weight" id="weight" value="<c:if test="${address.boxWeight != ''}">${address.boxWeight}</c:if>" 
	                                                maxlength="4" data-required="Y" data-label="${weight}" data-format="num" placeholder="${weight}"onchange="setTotalWeight()">
	                                            
	                                            <select class="cper2500 select-ajax" name="weightUnit" id="weightUnit" 
	                                                data-codetype="etc" data-code="${address.weightUnit}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H030000" }' 
	                                                data-required="Y" data-label="<spring:message code="settings.weightUnit" text="무게단위" />" onchange="setTotalWeight()" ></select>
	                                        </div>
	                                    </dd>
	                                </dl>
	                            </div>
	                            <div class="tit_wrap mt20">
	                                <h4 class="sub_h4"><spring:message code="order.popup.address.title2" text="관세정보" /></h4>
	                                <div class="sect_right">
	                                </div>
	                            </div>
	                            <div class="scr_x">
	                                <table class="tbtype boxlist">
	                                    <colgroup>
	                                        <col class="wp150">
	                                        <col class="wp200">
	                                        <col class="wp150">
	                                        <col class="wp150">
	                                        <col class="wp100">
	                                        <col class="wp100">
	                                        <col class="wp90">
	                                        <col class="wp90">
                                            <col class="wp90">
                                            <col class="wp90">
	                                    </colgroup>
	                                    <thead>
	                                        <tr>
	                                            <th><span class="required"><spring:message code="settings.itemCode" text="상품코드" /></span></th>
	                                            <th><spring:message code="settings.itemName" text="상품명" /></th>
	                                            <th><span class="required"><spring:message code="settings.itemSku" text="SKU" /></span></th>
	                                            <th><span class="required"><spring:message code="settings.itemOrigin" text="생산지" /></span></th>
	                                            <th><span class="required"><spring:message code="settings.hscode" text="HS CODE" /></span></th>
	                                            <th><span class="required"><spring:message code="settings.itemType" text="상품타입" /></span></th>
	                                            <th colspan="2"><span class="required"><spring:message code="settings.weight" text="무게" /></span></th>
	                                            <%-- <th colspan="4">${boxLength}*${boxWidth}*${boxHeight}</th> --%>
	                                            <th colspan="2"><span class="required"><spring:message code="settings.itemPrice" text="가격" /></span></th>
	                                            <th><span class="required"><spring:message code="order.popup.express.box.quantity" text="수량" /></span></th>
	                                        </tr>
	                                    </thead>
	                                    <tbody>
	                                    
	                                    
	                                <c:choose>
	                                    <c:when test="${fn:length(skuList) == 0}">
	                                        <tr>
	                                            <td colspan="9" class="t_center">
	                                                <spring:message code="common.title.notData" text="검색된 데이터가 없습니다." />
	                                            </td>
	                                        </tr>
	                                    </c:when>
	                                    <c:otherwise>
	                                        <c:forEach items="${skuList}" var="item" varStatus="status">
	                                    
	                                        <tr>
	                                            <td>
	                                                <input type="text" class="" name="arrGoodsCode" id="goodsCode_${status.count}" value="${item.goodsCode}" 
	                                                    maxlength="20" data-required="Y" data-label="<spring:message code="settings.itemCode" text="상품코드" />" 
	                                                    placeholder="<spring:message code="settings.itemCode" text="상품코드" />">
	                                                <input type="hidden" name="arrSkuIdx" id="skuIdx_${status.count}" value="${item.skuIdx}"> 
	                                                <input type="hidden" name="arrGoodsItemId" id="goodsItemId_${status.count}" value="${item.goodsItemId}"> 
	                                            </td>
	                                            <td>
	                                                <input type="text" class="" name="arrGoods" id="goods_${status.count}" value="${item.goods}" 
	                                                    maxlength="100" data-required="Y" data-label="<spring:message code="settings.itemName" text="상품명" />" 
	                                                    placeholder="<spring:message code="settings.itemName" text="상품명" />">
	                                            </td>
	                                            <td>
	                                                <input type="text" class="" name="arrGoodsSku" id="goodsSku_${status.count}" value="${item.goodsSku}" 
	                                                    maxlength="100" data-required="Y" data-label="<spring:message code="settings.itemSku" text="SKU" />" 
	                                                    placeholder="<spring:message code="settings.itemSku" text="SKU" />">
	                                            </td>
	                                            <td>
	                                                <select class="select-ajax" name="arrOrigin" id="origin_${status.count}" 
	                                                    data-codetype="etc" data-code="${item.origin}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "F010000" }' 
	                                                    data-required="Y" data-label="<spring:message code="settings.itemOrigin" text="생산지" />" ></select>
	                                            </td>
	                                            <td>
	                                                <input type="text" class="" name="arrHscode" id="hscode_${status.count}" value="${item.hscode}" 
	                                                    maxlength="8" data-required="Y" data-label="<spring:message code="settings.hscode" text="HS CODE" />" 
	                                                    placeholder="<spring:message code="settings.hscode" text="HS CODE" />">
	                                                <a href="#" class="btn_search"></a>
	                                            </td>
	                                            <td>
	                                                <select class="select-ajax" name="arrGoodsType" id="goodsType_${status.count}" 
	                                                    data-codetype="etc" data-code="${item.goodsType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H040000" }' 
	                                                    data-required="Y" data-label="<spring:message code="settings.itemType" text="상품타입" />" ></select>
	                                            </td>
	                                            <td class="weight-data" data-weight="${item.weight}" data-unit="${item.weightUnit}" data-quantity="${item.quantity}">
	                                                <input type="text" class="t_right" name="arrWeight" id="weight_${status.count}" value="<c:if test="${item.weight != ''}">${item.weight}</c:if>" 
	                                                    maxlength="4" data-required="Y" data-format="num" data-label="<spring:message code="settings.weight" text="무게" />" 
	                                                    placeholder="<spring:message code="settings.weight" text="무게" />" onchange="setTotalWeight()">
	                                            </td>
	                                            <td>
	                                                <select class="select-ajax" name="arrWeightUnit" id="weightUnit_${status.count}" 
	                                                    data-codetype="etc" data-code="${item.weightUnit}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H030000" }' 
	                                                    data-required="Y" data-label="<spring:message code="settings.weightUnit" text="무게단위"  />" onchange="setTotalWeight()"></select>
	                                            </td>
	                                            <td>
	                                                <input type="text" class="t_right" name="arrUnitCost" id="unitCost_${status.count}" value="<c:if test="${item.unitCost gt 0.0}">${item.unitCost}</c:if>" 
	                                                    maxlength="7" data-required="Y" data-format="num" data-label="<spring:message code="settings.itemPrice" text="가격" />" 
	                                                    placeholder="<spring:message code="settings.itemPrice" text="가격" />">
	                                            </td>
	                                            <td>
	                                                <input type="text" class="t_center" name="arrPriceCurrency" id="priceCurrency_${status.count}" value="${item.priceCurrency}" 
	                                                    maxlength="3" data-required="Y" data-format="" data-label="<spring:message code="settings.priceCurrency" text="화폐단위" />" 
	                                                    placeholder="<spring:message code="settings.priceCurrency" text="화폐단위" />">
	                                            </td>
	                                            <td>
	                                                <input type="text" class="t_right" name="arrQuantity" id="quantity_${status.count}" value="<c:if test="${item.quantity > 0}">${item.quantity}</c:if>" 
	                                                    maxlength="5" data-required="Y" data-format="num" data-label="<spring:message code="order.popup.express.box.quantity" text="수량" />" 
	                                                    placeholder="<spring:message code="order.popup.express.box.quantity" text="수량" />" onchange="setTotalWeight()">
	                                            </td>
	                                        </tr>
	                                        
	                                        </c:forEach>  
	                                    </c:otherwise>
	                                </c:choose>   
	                                        
	                                        
	                                    </tbody>
	                                </table>
	                                
	                            </div>
	                            <div class="mt10 required"><a href="https://unipass.customs.go.kr/clip/index.do?opnurl=/hsinfosrch/openULS0201001Q.do" target="_blank">HSCODE GUIDE</a></div>
	                        </li>
	                    </ul>
	                    <div class="pop_btn">
	                        <button type="button" data-area="popCombineStep01" class="btn_type3 btnCombineProc" name="prev"><spring:message code="button.prev" text="이전" /></button> 
	                        <button type="button" data-area="popCombineStep03" class="btn_type5 btnCombineProc" name="next"><spring:message code="button.next" text="다음" /></button>
	                    </div>
	                    </form>
	                </div>
	                
	                
	                
	                <div id="popCombineStep03" class="combine-view combine-view-off">
		                <form name="frmCombineStep03" method="get" id="frmCombineStep03">
		                
		                </form>
		           <div class="pop_btn">
                            <button type="button" data-area="popCombineStep02" class="btn_type3 btnCombineProc" name="prev"><spring:message code="button.prev" text="이전" /></button> 
                            <button type="button" class="btn_type4" id="addDeliveryBtn"><spring:message code="button.deliveryAdd" text="배송생성" /></button>
                          	<input type="text" name="arrOrderCode" id="arrOrderCode" value="${arrOrderCode}">
                    		<input type="text" name="arrShopIdx" id="arrShopIdx" value="${arrShopIdx}"> 
                        </div>
	                </div>
                
                
                    <form name="frmCombineDefalut" method="get" id="frmCombineDefalut">
                    <input type="hidden" name="sellerCountry" value="KOREA">
                    <input type="hidden" name="sellerCountryCode" value="KR">
                    <input type="hidden" name="sellerName" value="${address.sellerName}">
                    <input type="hidden" name="orderIdx" value="${address.orderIdx}">
                    <input type="hidden" name="orderDate" value="${address.orderDate}">
                    <input type="hidden" name="arrOrderCode" id="arrOrderCode" value="${arrOrderCode}">
                    <input type="hidden" name="arrShopIdx" id="arrShopIdx" value="${arrShopIdx}"> 
                    </form>
                </div>
                
                
                
    </c:otherwise>
 </c:choose>   