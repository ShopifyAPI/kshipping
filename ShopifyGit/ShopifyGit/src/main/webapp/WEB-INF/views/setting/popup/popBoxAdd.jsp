<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_ADM_532_POP 
기능설명 : 설정관리 > 배송관리 > 포장재 popup
Author   Date         Description
jwh      2019-12-23   First Draft
**************************************************
 -->
                <div class="pop_head">
                    <h3><spring:message code="settings.menuBox" text="포장재" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                    <form name="submitform" id="submitform">
                    <div class="form_sect">
                        <dl>
                            <dt>
                                <c:set var="shop"><spring:message code="settings.shop" text="쇼핑몰" /></c:set>
                                <p class="required">${shop}</p>
                            </dt>
                            <dd>
                                <select class="cper100p" name="shopIdx" id="shopIdx" data-required="Y" data-label="${shop}">
                                <c:forEach items="${list}" var="item" varStatus="status">
                                    <option value="${item.shopIdx}" <c:if test="${box.shopIdx eq item.shopIdx}">selected</c:if>>${item.shopName}</option>
                                </c:forEach>
                                </select>
                            </dd>
                            
                            <dt>
                                <c:set var="boxType"><spring:message code="settings.boxType" text="포장재타입" /></c:set>
                                <p class="required">${boxType}</p>
                            </dt>
                            <dd>
                                <select class="cper100p select-ajax" name="boxType" id="boxType" 
	                                data-codetype="etc" data-code="${box.boxType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H010000" }' 
	                                data-required="Y" data-label="${boxType}" ></select>
                            </dd>
                            <dt>
                                <c:set var="boxTitle"><spring:message code="settings.boxTitle" text="포장재 명" /></c:set>
                                <p class="required">${boxTitle}</p>
                            </dt>
                            <dd>
                                <div>
                                    <input type="text" class="cper100p"  name="boxTitle" id="boxTitle" value="${box.boxTitle}" 
                                        maxlength="30" data-required="Y" data-label="${boxTitle}" placeholder="${boxTitle}">
                                </div>
                            </dd>
                            <dt><p class="required"><spring:message code="settings.boxSize" text="박스크기" /></p></dt>
                            <dd>
	                            <div class="row">
	                               <c:set var="boxLength"><spring:message code="settings.boxLength" text="길이" /></c:set>
                                   <input type="text" class="cper2500" name="boxLength" id="boxLength" value="<c:if test="${box.boxLength != ''}">${box.boxLength}</c:if>" 
                                       maxlength="4" data-required="Y" data-label="${boxLength}" data-format="number" placeholder="${boxLength}">
                                   
                                   <c:set var="boxWidth"><spring:message code="settings.boxWidth" text="폭" /></c:set>
                                   <input type="text" class="cper2500" name="boxWidth" id="boxWidth" value="<c:if test="${box.boxWidth != '' }">${box.boxWidth}</c:if>" 
                                       maxlength="4" data-required="Y" data-label="${boxWidth}" data-format="number" placeholder="${boxWidth}">
                                   
                                   <c:set var="boxHeight"><spring:message code="settings.boxHeight" text="높이" /></c:set>
                                   <input type="text" class="cper2500" name="boxHeight" id="boxHeight" value="<c:if test="${box.boxHeight != ''}">${box.boxHeight}</c:if>" 
                                       maxlength="4" data-required="Y" data-label="${boxHeight}" data-format="number" placeholder="${boxHeight}">
                                       
                                   <select class="cper2500 select-ajax" name="boxUnit" id="boxUnit" 
	                                   data-codetype="etc" data-code="${box.boxUnit}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H020000" }' 
	                                   data-required="Y" data-label="<spring:message code="settings.boxUnit" text="박스단위" />" ></select>
	                            </div>
                            </dd>
                            <dt>
                                <c:set var="weight"><spring:message code="settings.weight" text="무게" /></c:set>
                                <p class="required">${weight}</p>
                            </dt>
                            <dd>
                                <div class="row">
                                    <input type="text" class="cper7000" name="boxWeight" id="boxWeight" value="<c:if test="${box.boxWeight != ''}">${box.boxWeight}</c:if>" 
                                        maxlength="4" data-required="Y" data-label="${weight}" placeholder="${weight}">
                                        
                                    <select class="cper3000 select-ajax" name="weightUnit" id="weightUnit" 
                                        data-codetype="etc" data-code="${box.weightUnit}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H030000" }' 
                                        data-required="Y" data-label="<spring:message code="weightUnit" text="무게단위" />" ></select>
                                </div>
                            </dd>
                        </dl>
                    </div>
                    <input type="checkbox" name=useDefault id="useDefault" value="Y" <c:if test="${box.useDefault eq 'Y'}">checked</c:if>><label for="useDefault" class="mt5"><span class="icon_ck" ></span>
                    <span class="label_txt"><spring:message code="settings.pop.boxDefaultText" text="기본 포장재로 등록" /></span></label>
                    
                    <div class="pop_btn">
                    <c:choose>
                        <c:when test="${box.boxIdx > 0}">
                            <button type="button" class="btn_type2 cper100p edit"><spring:message code="button.edit" text="수정" /></button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn_type2 cper100p save"><spring:message code="button.insert" text="등록" /></button>
                        </c:otherwise>
                    </c:choose> 
                    </div>
                    <input type="hidden" name="boxIdx" id="boxIdx" value="${box.boxIdx}">
                    </form>
                </div>