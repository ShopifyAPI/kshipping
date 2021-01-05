<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>

<!-- 
**************************************************
화면코드 : UI_SYS_112_POP
기능설명 : 관리자 > 셀러관리 > 셀러 등급 변경(팝업)
Author   Date      Description
 jwh     2020-01-22  First Draft
**************************************************
 -->
 
                <c:set var="rank"><spring:message code="rank.rank" text="등급" /></c:set>
                <div class="pop_head">
                    <h3>${rank}</h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <form name="submitform" id="submitform">
                <div class="pop_body">
                    <p class="t_center bold"><spring:message code="admin.pop.rankText" text="등급을 선택해 주세요." /></p>
                    
                    <select class="cper100p select-ajax" name="rankId" id="rankId" 
	                    data-codetype="" data-code="${seller.rankId}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "E010000" }' 
	                    data-required="Y" data-label="${rank}" ></select>
	                    
                    <br>
                    <br>
                    <p class="t_center bold"><spring:message code="admin.pop.satusText" text="셀러 상태를 선택해 주세요." /></p>
                    
                    <select class="cper100p select-ajax" name="shopStatus" id="shopStatus" 
	                    data-codetype="" data-code="${seller.shopStatus}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "E020000" }' 
	                    data-required="Y" data-label="${shopStatus}" ></select>	                    
                    
                    <br>
                    <br>
                    <p class="t_center bold"><spring:message code="admin.pop.paymentText" text="결제 상태를 선택해 주세요." /></p>
                    
                    <select class="cper100p select-ajax" name="paymentMethod" id="paymentMethod" 
	                    data-codetype="" data-code="${seller.paymentMethod}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "E030000" }' 
	                    data-required="Y" data-label="${paymentMethod}" ></select>
	                    	                    
	                <input type="hidden" name="email" id="email" value="${seller.email}">
              
                    <div class="pop_btn">
                        <button type="button" class="btn_type2 edit"><spring:message code="button.edit" text="수정" /></button>
                        <button type="button" class="btn_type6 cancel"><spring:message code="button.cancel" text="취소" /></button>
                    </div>
                    
                </div>
                </form>