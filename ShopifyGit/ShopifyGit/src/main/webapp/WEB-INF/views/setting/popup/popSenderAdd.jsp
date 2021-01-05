<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_ADM_522_POP 
기능설명 : 설정관리 > 배송관리 > 출고지 popup
Author   Date         Description
jwh      2019-12-23   First Draft
**************************************************
 -->
                <div class="pop_head">
                    <h3><spring:message code="settings.menuSender" text="출고지" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                    <form name="submitform" id="submitform" action="/shopifyInstall">
                    <div class="form_side">
                        <dl>
                            <dt>
                                <c:set var="shop"><spring:message code="settings.shop" text="쇼핑몰" /></c:set>
                                <p class="required">${shop}</p>
                            </dt>
                            <dd>
                                <select class="cper100p" name="shopIdx" id="shopIdx" data-required="Y" data-label="${shop}">
                                <c:forEach items="${list}" var="item" varStatus="status">
                                    <option value="${item.shopIdx}" <c:if test="${sender.shopIdx eq item.shopIdx}">selected</c:if>>${item.shopName}</option>
                                </c:forEach>
                                </select>
                            </dd>
                            <dt>
                                <c:set var="title"><spring:message code="settings.sender.name" text="출고지명" /></c:set>
                                <p class="required">${title}</p>
                            </dt>
                            <dd>
                                <div>
                                    <input type="text" class="cper100p" name="senderTitle" id="senderTitle" value="${sender.senderTitle}" 
                                        maxlength="20" data-required="Y" data-label="${title}" placeholder="${title}">
                                </div>
                            </dd>
                            <dt>
                                <c:set var="phoneNumber"><spring:message code="settings.phoneNumber" text="전화번호" /></c:set>
                                <p class="required">
                                	${phoneNumber}
                                	<c:if test="${sender.phoneNumber04 == '' or sender.phoneNumber04 == null}" >
                                	   <c:if test="${sender.phoneNumber != '' and sender.phoneNumber != null}" >[${sender.phoneNumber}]</c:if>
                                	</c:if>
                                </p>
                            </dt>
                            <dd>
                                <div class="phone4">
                                	<c:set var="phoneText01"><spring:message code="common.phone.01" text="국가번호" /></c:set>
                                	<c:set var="phoneText02"><spring:message code="common.phone.02" text="국번" /></c:set>
                                	<c:set var="phoneText03"><spring:message code="common.phone.03" text="중간자리" /></c:set>
                                	<c:set var="phoneText04"><spring:message code="common.phone.04" text="끝자리" /></c:set>

                                    <input type="text" name="phoneNumber01" id="phoneNumber01" maxlength="4" value="${sender.phoneNumber01}"
                                        data-format="number"  data-label="${phoneText01}" placeholder="${phoneText01}">
                                    <input type="text" name="phoneNumber02" id="phoneNumber02" maxlength="5" value="${sender.phoneNumber02}"
                                        data-format="number" data-required="Y" data-label="${phoneText02}" placeholder="${phoneText02}">
                                    <input type="text" name="phoneNumber03" id="phoneNumber03" maxlength="5" value="${sender.phoneNumber03}"
                                        data-format="number" data-required="Y" data-label="${phoneText03}" placeholder="${phoneText03}">
                                    <input type="text" name="phoneNumber04" id="phoneNumber04" maxlength="5" value="${sender.phoneNumber04}"
                                        data-format="number" data-required="Y" data-label="${phoneText04}" placeholder="${phoneText04}">
                                </div>
                            </dd>
                            
                            <dt>
                                <c:set var="zipCode"><spring:message code="settings.zipCode" text="우편번호" /></c:set>
                                <c:set var="addr1"><spring:message code="settings.addr1" text="주소" /></c:set>
                                <p class="required">${zipCode}</p>
                            </dt>
                            
                            <dd>
                                <div class="zipcode">
                                    <input type="text" name="zipCode" id="zipCode" value="${sender.zipCode}" 
                                        maxlength="20" readonly data-required="Y" data-label="${zipCode}" placeholder="${zipCode}">
                                    <a href="#" onclick="return false;" class="btn_search zipcode-pop"></a>
                                </div>
                                <input type="text" class="cper100p mt0 readonly" name="addr1" id="addr1" value="${sender.addr1}" 
                                    maxlength="50" readonly data-required="Y" data-label="${addr1}" placeholder="${addr1}">
                            </dd>
                            
                            <dt>
                                <c:set var="addr2"><spring:message code="settings.addr2" text="상세주소" /></c:set>
                                <p class="required">${addr2}</p>
                            </dt>
                            
                            <dd>
                                <input type="text" class="cper100p mt0" name="addr2" id="addr2" value="${sender.addr2}" 
                                    maxlength="50" data-required="Y" data-label="${addr2}" placeholder="${addr2}">
                            </dd>
                            
                            <dt>
                                <c:set var="addr1Ename"><spring:message code="settings.addr1Ename" text="영문 주소" /></c:set>
                                <p>${addr1Ename}</p>
                            </dt>
                            <dd>
	                            <div>
	                                <input type="text" class="cper100p mt5 readonly" name="addr1Ename" id="addr1Ename" value="${sender.addr1Ename}" 
	                                    maxlength="50" readonly >
	                            </div>
                            </dd>
                            
                            <dt>
                                <c:set var="addr2Ename"><spring:message code="settings.addr2Ename" text="영문 상세주소" /></c:set>
                                <p class="required">${addr2Ename}</p>
                            </dt>
                            <dd>
	                            <div>
	                                <input type="text" class="cper100p mt0" name="addr2Ename" id="addr2Ename" value="${sender.addr2Ename}" 
	                                    maxlength="50" data-format="english" data-required="Y" data-label="${addr2Ename}" placeholder="english detail address">
	                            </div>
                            </dd>
                            
                            <dt>
                                <c:set var="province"><spring:message code="settings.province" text="Province" /></c:set>
                                <p>${province}</p>
                            </dt>
                            <dd>
                                <input type="text" class="cper100p mt0 readonly" name="province" id="province" value="${sender.province}" 
                                    maxlength="50" readonly >
                            </dd>
                            
                            <dt>
                                <c:set var="city"><spring:message code="settings.city" text="City" /></c:set>
                                <p>${city}</p>
                            </dt>
                            <dd>
                                <input type="text" class="cper100p mt0 readonly" name="city" id="city" value="${sender.city}" 
                                    maxlength="50" readonly ">
                            </dd>
                        </dl>
                    </div>
                    
                    <input type="checkbox" name=useDefault id="useDefault" value="Y" <c:if test="${sender.useDefault eq 'Y'}">checked</c:if>><label for="useDefault" class="mt5"><span class="icon_ck" ></span>
                    <span class="label_txt"><spring:message code="settings.pop.senderDefaultText" text="기본 출고지로 등록" /></span></label>
                    
                    <div class="pop_btn">
                    <c:choose>
                        <c:when test="${sender.senderIdx > 0}">
                            <button type="button" class="btn_type2 cper100p edit"><spring:message code="button.edit" text="수정" /></button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn_type2 cper100p save"><spring:message code="button.insert" text="등록" /></button>
                        </c:otherwise>
                    </c:choose>
                    </div>
                    
                    <input type="hidden" name="senderIdx" id="senderIdx" value="${sender.senderIdx}">
                    <input type="hidden" name="senderProvince" id="senderProvince" value="${sender.province}">
                    <input type="hidden" name="senderCity" id="senderCity" value="${sender.city}">
                    </form>
                </div>
