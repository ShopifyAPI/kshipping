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
                    <ul class="step col3">
                        <li class="on"><a href="#" onclick="return false;"><spring:message code="order.popup.address.title1" text="주소등록" /></a></li>
                        <li><a id="popupExpress" href="#" onclick="popupExpress('${code}','${idx}'); return false;"><spring:message code="order.popup.address.title2" text="관세정보" /></a></li> 
                        <li id="tab_custom"><a href="#" onclick="popupCustoms('${code}','${idx}'); return true"><spring:message code="order.popup.address.title3" text="배송정보" /></a></li>
                    </ul>
                    <ul class="tab_conts">
                    <form name="frm" method="get" id="ajax-popForm">
                        <li>
                            <div class="wrap_col">
                                <div class="colA">
                                    <h4 class="sub_h4"><spring:message code="order.popup.address.sender" text="보내는 사람" /></h4>
                                    <div class="form_side">
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
				                                        data-format="number" data-required="N" data-label="${phoneText02}" placeholder="${phoneText02}">
				                                    <input type="text" name="sellerPhone03" id="sellerPhone03" maxlength="5" value="${address.sellerPhone03}"
				                                        data-format="number" data-required="N" data-label="${phoneText03}" placeholder="${phoneText03}">
				                                    <input type="text" name="sellerPhone04" id="sellerPhone04" maxlength="5" value="${address.sellerPhone04}"
				                                        data-format="number" data-required="N" data-label="${phoneText04}" placeholder="${phoneText04}">
				                                </div>
                                                <%-- <div>
                                                    <input type="text" class="cper100p" name="sellerPhone" id="sellerPhone" maxlength="15" value="${address.sellerPhone}" placeholder="${phone}"  
                                                        data-label="${phone}" data-format="phone" data-required="Y" ${phone}>
                                                </div> --%>
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
                                                    data-label="${sellerAddr2}" data-required="Y">
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
                                                    data-label="${sellerProvince}" data-required="Y">
                                                <c:set var="sellerCity"><spring:message code="order.popup.address.address.city" text="City" /></c:set>
                                                <input type="text" class="cper100p mt5 readonly" name="sellerCity" id="sellerCity" value="${address.sellerCity}" readOnly placeholder="${sellerCity}"  text="${sellerCity}"
                                                    data-label="${sellerCity}" data-required="Y">
                                            </dd>
                                            
                                        </dl>
                                    </div>
                                </div>
                                <div class="colB">
                                    <h4 class="sub_h4"><spring:message code="order.popup.address.receiver" text="받는 사람" /></h4>
                                    <div class="form_side">
                                        <dl>
                                            <dt><p class="required"><spring:message code="order.popup.address.firstName" text="이름" /></p></dt>
                                            <dd>
                                                <div class="row">
                                                    <c:set var="buyerFirstname"><spring:message code="order.popup.address.firstName" text="이름" /></c:set>
                                                    <input type="text" class="cper5000" name="buyerFirstname" value="${address.buyerFirstname}" placeholder="${buyerFirstname}" 
                                                        data-label="${buyerFirstname}" data-required="Y" readonly="readonly" style="color:gray">
                                                    <c:set var="buyerLastname"><spring:message code="order.popup.address.lastName" text="이름(성)" /></c:set>    
                                                    <input type="text" class="cper5000" name="buyerLastname" value="${address.buyerLastname}" placeholder="${buyerLastname}" 
                                                        data-label="${buyerLastname}" readonly="readonly" style="color:gray">
                                                </div>
                                            </dd>
                                            
                                            
                                            <dt>
                                                <c:set var="buyerPhone"><spring:message code="order.popup.address.phone" text="전화번호" /></c:set>
                                                <p class="required"> ${buyerPhone} </p>
                                            </dt>
                                            <dd>
                                                <input type="text" class="cper100p" name="buyerPhone" value="${address.buyerPhone}" Placeholder="${buyerPhone}" 
                                                    data-format="worldPhone" data-label="${buyerPhone}" data-required="Y"">
                                            </dd>
                                            <!-- 
                                            <dd>
                                            	<div class="phone4">
				                                    <input type="text" name="buyerPhone01" id="buyerPhone01" maxlength="4" value="${address.buyerPhone01}"
				                                        data-format="num" data-required="Y" data-label="${phoneText01}" placeholder="${phoneText01}">
				                                    <input type="text" name="buyerPhone02" id="buyerPhone02" maxlength="5" value="${address.buyerPhone02}"
				                                        data-format="num" data-required="Y" data-label="${phoneText02}" placeholder="${phoneText02}">
				                                    <input type="text" name="buyerPhone03" id="buyerPhone03" maxlength="5" value="${address.buyerPhone03}"
				                                        data-format="num" data-required="Y" data-label="${phoneText03}" placeholder="${phoneText03}">
				                                    <input type="text" name="buyerPhone04" id="buyerPhone04" maxlength="5" value="${address.buyerPhone04}"
				                                        data-format="num" data-required="Y" data-label="${phoneText04}" placeholder="${phoneText04}">
				                                </div>
                                             -->
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
                                                    data-label="<spring:message code="order.popup.address.country" text="국가" />" data-required="Y"
                                                    readonly onFocus="this.initialSelect = this.selectedIndex;" onChange="this.selectedIndex = this.initialSelect;" style="color:gray">
                                                </select>
                                                
                                                <c:set var="buyerAddr2"><spring:message code="order.popup.address.address3" text="상세주소" /></c:set>    
                                                <input type="text" name="buyerAddr2" class="cper100p mt5" value="${address.buyerAddr2}" placeholder="${buyerAddr2}" 
                                                    data-label="${buyerAddr2}" data-required="N">
                                                
                                                <c:set var="buyerAddr1"><spring:message code="order.popup.address.address2" text="주소" /></c:set>
                                                <input type="text" name="buyerAddr1" class="cper100p mt5" value="${address.buyerAddr1}" placeholder="${buyerAddr1}" 
                                                    data-label="${buyerAddr1}" data-required="Y" style="color:gray">
                                                
                                                <input type="hidden" id="jibunAddress">
                                                <div class="row mt5">
                                                    <c:set var="buyerCity"><spring:message code="order.popup.address.city" text="시" /></c:set>   
                                                    <input type="text" name="buyerCity" class="cper5000" value="${address.buyerCity}" placeholder="${buyerCity}" 
                                                    data-label="${buyerCity}" data-required="Y"  style="color:gray">
                                                    <c:set var="buyerProvince"><spring:message code="order.popup.address.state" text="시/군/구" /></c:set>
                                                    <input type="text" name="buyerProvince" class="cper5000" value="${address.buyerProvince}" placeholder="${buyerProvince}"
                                                     data-label="${buyerProvince}" data-required="N"  style="color:gray">
                                                </div>
                                            </dd>
                                            <dt>
                                                <c:set var="buyerZipCode"><spring:message code="order.popup.address.post" text="우편번호" /></c:set>
                                                <p class="">${buyerZipCode}</p>
                                            </dt>
                                            <dd><input type="text" name="buyerZipCode" class="cper100p mt5" value="${address.buyerZipCode}" Placeholder="${buyerZipCode}" 
                                                data-label="${buyerZipCode}" data-required="N" readonly="readonly" style="color:gray"></dd>
                                        </dl>
                                    </div>
                                    <div class="pop_adjacent">
				                        <button type="button" class="btn_type2" id="saveAddrBtn"><spring:message code="button.save" text="저장" /></button>
				                    </div>
                                </div>
                            </div>
                        </li>

                        <input type="hidden" name="sellerCountry" value="KOREA">
                        <input type="hidden" name="sellerCountryCode" value="KR">
                        <input type="hidden" name="sellerName" value="${address.sellerName}">
                        <input type="hidden" name="masterCode" value="${address.masterCode}">
                        <input type="hidden" name="orderCode" value="${address.orderCode}">
                        <input type="hidden" name="shopIdx" value="${address.shopIdx}">
                        <input type="hidden" name="orderIdx" value="${address.orderIdx}">
                        <input type="hidden" name="orderDate" value="${address.orderDate}">
                        </form>
                    </ul>

                </div>
                
    </c:otherwise>
 </c:choose>   