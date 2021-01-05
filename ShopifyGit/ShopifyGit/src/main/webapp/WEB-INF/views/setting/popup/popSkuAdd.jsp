<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_ADM_542_POP 
기능설명 : 설정관리 > 배송관리 > 관세정보 popup
Author   Date         Description
jwh      2019-12-23   First Draft
**************************************************
 -->
                <div class="pop_head">
                    <h3><spring:message code="settings.pop.skuTitle" text="관세정보" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                    <form name="submitform" id="submitform">
                    <div class="form_side">
                        <dl>
                            <dt>
                                <c:set var="shop"><spring:message code="settings.shop" text="쇼핑몰" /></c:set>
                                <p class="required">${shop}</p>
                            </dt>
                            <dd>
                                <select class="cper100p" name="shopIdx" id="shopIdx" data-required="Y" data-label="${shop}">
                                <c:forEach items="${list}" var="item" varStatus="status">
                                    <option value="${item.shopIdx}" <c:if test="${sku.shopIdx eq item.shopIdx}">selected</c:if>>${item.shopName}</option>
                                </c:forEach>
                                </select>
                            </dd>
                            <dt>
                                <c:set var="itemSku"><spring:message code="settings.itemSku" text="SKU" /></c:set>
                                <p>${itemSku}</p>
                            </dt>
                            <dd>
                                <input type="text" class="cper100p" name="itemSku" id="itemSku" value="${sku.itemSku}" 
                                     data-label="${itemSku}" placeholder="<spring:message code="settings.itemSkuText" text="sku 를 입력해 주세요." />">
                            </dd>                            
                            <dt>
                                <c:set var="itemCode"><spring:message code="settings.itemCode" text="상품코드" /></c:set>
                                <p class="required">${itemCode}</p>
                            </dt>
                            <dd>
                                <input type="text" class="cper100p" name="itemCode" id="itemCode" value="${sku.itemCode}" 
                                    data-required="Y" data-label="${itemCode}" placeholder="<spring:message code="settings.itemCodeText" text="상품코드를 입력해주세요." />">
                            </dd>
                            <dt>
                                <c:set var="itemName"><spring:message code="settings.itemName" text="상품명" /></c:set>
                                <p class="required">${itemName}</p>
                            </dt>
                            <dd>
                                <input type="text" class="cper100p" name="itemName" id="itemName" value="${sku.itemName}" 
                                    data-required="Y" data-label="${itemName}" placeholder="<spring:message code="settings.itemNameText" text="구체적으로 기재해 주세요." />">
                            </dd>
                            <dt>
                                <c:set var="repreItemNm"><spring:message code="settings.repreItemNm" text="Representative product name(Russian)" /></c:set>
                                <p class="">${repreItemNm}</p>
                            </dt>
                            <dd>
                                <input type="text" class="cper100p" name="repreItemNm" id="repreItemNm" value="${sku.repreItemNm}" 
                                    data-label="${repreItemNm}" placeholder="<spring:message code="settings.itemNameText" text="구체적으로 기재해 주세요." />">
                            </dd>
                            <dt>
                                <c:set var="repreItemNmRu"><spring:message code="settings.repreItemNmRu" text="Representative product name" /></c:set>
                                <p class="">${repreItemNmRu}</p>
                            </dt>
                            <dd>
                                <input type="text" class="cper100p" name="repreItemNmRu" id="repreItemNmRu" value="${sku.repreItemNmRu}" 
                                     data-label="${repreItemNmRu}" placeholder="<spring:message code="settings.itemNameText" text="구체적으로 기재해 주세요." />">
                            </dd>                                                        
                            <dt>
                                <c:set var="weight"><spring:message code="settings.weight" text="무게" /></c:set>
                                <p class="required">${weight}</p>
                            </dt>
                            <dd>
                                <div class="row">
                                    <input type="text" class="cper7000" name="itemWeight" id="itemWeight" value="<c:if test="${sku.itemWeight > 0}">${sku.itemWeight}</c:if>" 
                                        maxlength="5" data-required="Y" data-format="num" data-label="${weight}" placeholder="${weight}">
                                        
                                    <select class="cper3000 select-ajax" name="weightUnit" id="weightUnit" 
                                        data-codetype="etc" data-code="${sku.weightUnit}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H030000" }' 
                                        data-required="Y" data-label="<spring:message code="weightUnit" text="무게단위" />" ></select>
                                </div>
                            </dd>
                            <!--   
                            <dt><p class="required"><spring:message code="settings.boxSize" text="박스크기" /></p></dt>
                            <dd>
                                <div class="row">
                                   <c:set var="boxLength"><spring:message code="settings.boxLength" text="길이" /></c:set>
                                   <input type="text" class="cper2500" name="boxLength" id="boxLength" value="${sku.boxLength}" 
                                       maxlength="4" data-required="Y" data-label="${boxLength}" placeholder="${boxLength}">
                                   
                                   <c:set var="boxWidth"><spring:message code="settings.boxWidth" text="폭" /></c:set>
                                   <input type="text" class="cper2500" name="boxWidth" id="boxWidth" value="${sku.boxWidth}" 
                                       maxlength="4" data-required="Y" data-label="${boxWidth}" placeholder="${boxWidth}">
                                   
                                   <c:set var="boxHeight"><spring:message code="settings.boxHeight" text="높이" /></c:set>
                                   <input type="text" class="cper2500" name="boxHeight" id="boxHeight" value="${sku.boxHeight}" 
                                       maxlength="4" data-required="Y" data-label="${boxHeight}" placeholder="${boxHeight}">
                                       
                                   <select class="cper2500 select-ajax" name="boxUnit" id="boxUnit" 
                                       data-codetype="etc" data-code="${sku.boxUnit}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H020000" }' 
                                       data-required="Y" data-label="<spring:message code="settings.boxUnit" text="박스단위" />" ></select>
                                </div>
                            </dd>
                             -->
                            
                            <!-- add start--> 
                            <dt>
                                        <c:set var="boxSelect"><spring:message code="settings.boxSelect" text="포장재선택" /></c:set>
                                        <p class="">${boxSelect}</p>
                            </dt>
                            <dd>
                                        <select class="cper100p" name="selectBox" id="boxSelect"  data-label="${boxSelect}">
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
                            <!--  
                            <dt>
                                        <c:set var="boxType"><spring:message code="settings.boxType" text="박스타입" /></c:set>
                                        <p class="required">${boxType}</p>
                            </dt>
                            <dd>
                                        <select class="cper100p select-ajax" name="boxType" id="boxType" 
                                            data-codetype="etc" data-code="${address.boxType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H010000" }' 
                                            data-required="Y" data-label="${boxType}" ></select>
                            </dd>
                            -->
                            <!-- add end--> 
                            <dt>
                                <c:set var="itemQty"><spring:message code="settings.itemQty" text="개수" /></c:set>
                                <p class="required">${itemQty}</p>
                            </dt>
                            <dd>
                                <input type="text" class="cper100p" name="itemQty" id="itemQty" value="<c:if test="${sku.itemQty > 0}">${sku.itemQty}</c:if>" 
                                    data-required="Y" data-format="num" data-label="${itemQty}" placeholder="${itemQty}">
                            </dd>
                            <dt>
                                <c:set var="itemPrice"><spring:message code="settings.itemPrice" text="가격" /></c:set>
                                <p class="required">${itemPrice}</p>
                            </dt>
                            <dd>
                                <div class="row">
                                    <input type="text" class="cper7000" name="itemPrice" id="itemPrice" value="<c:if test="${sku.itemPrice gt 0.0}">${sku.itemPrice}</c:if>" 
                                        data-required="Y" data-format="num" data-label="${itemPrice}" placeholder="${itemPrice}">
                                    <input type="text" class="cper3000" name="priceCurrency" id="priceCurrency" value="${sku.priceCurrency}" 
                                        data-required="Y" data-format="" data-label="<spring:message code="settings.priceCurrency" text="화폐단위" />" placeholder="<spring:message code="settings.priceCurrency" text="화폐단위" />">
                                </div>
                            </dd>
                            <dt>
                                <c:set var="hscode"><spring:message code="settings.hscode" text="HS CODE" /></c:set>
                                <p class="required">${hscode}</p>
                            </dt>
                            <dd>
                                <div class="zipcode">
                                    <input type="text" class="cper100p" name="hscode" id="hscode" value="${sku.hscode}" 
                                    data-required="Y" data-label="${hscode}" placeholder="<spring:message code="settings.hscodeText" text="8~11자 CODE를 입력해주세요." />">
                                    <a href="https://unipass.customs.go.kr/clip/index.do?opnurl=/hsinfosrch/openULS0201001Q.do" class="btn_search" target="_blank"></a>
                                </div>
                            </dd>
                            <dt>
                                <c:set var="itemOrigin"><spring:message code="settings.itemOrigin" text="생산지" /></c:set>
                                <p class="required">${itemOrigin}</p>
                            </dt>
                            <dd>
                                <select class="cper100p select-ajax" name="itemOrigin" id="itemOrigin" 
                                    data-codetype="etc" data-code="${sku.itemOrigin}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "F010000" }' 
                                    data-required="Y" data-label="${itemOrigin}" ></select>
                            </dd>
                            <dt>
                                <c:set var="itemType"><spring:message code="settings.itemType" text="상품타입" /></c:set>
                                <p class="required">${itemType}</p>
                            </dt>
                            <dd>
                               <select class="cper100p select-ajax" name="itemType" id="itemType" 
                                    data-codetype="etc" data-code="${sku.itemType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H040000" }' 
                                    data-required="Y" data-label="${itemType}" ></select>
                            </dd>
                        </dl>
                    </div>
                    
                    <div class="pop_btn">
                    <c:choose>
                        <c:when test="${sku.skuIdx > 0}">
                            <button type="button" class="btn_type2 cper100p edit"><spring:message code="button.edit" text="수정" /></button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn_type2 cper100p save"><spring:message code="button.insert" text="등록" /></button>
                        </c:otherwise>
                    </c:choose> 
                    </div>
                    <input type="hidden" name="skuIdx" id="skuIdx" value="${sku.skuIdx}">
                    </form>
                </div>