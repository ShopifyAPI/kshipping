<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
<script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>

<!-- 
**************************************************
화면코드 :UI_SYS_132_POP 
기능설명 : ADM 할인율 등록(팝업)
Author   Date      Description
 김윤홍     2019-12-30  First Draft
**************************************************
 -->
 
 <div class="pop_head">
    <h3> <spring:message code="rank.rankinsert" text="할인율 등록"/></h3>
    <button type="button" class="btn_close"></button>
</div>

<div class="pop_body">
	<form name="insertRank" id="insertRank" method="post">
	<div>
	   <strong> * 신규 생성은 코드관리에서 먼저 설정해 주세요.</strong>
	</div>
    <div class="bg_gray wp380 mt10">
        
        <dl>
            <dt>
            <c:set var="fromDate"><spring:message code="rank.effectiveFromDate" text="적용시작일" /></c:set>
           	${fromDate}
            </dt>
            <dd>
                <div class="month"><input type="text" class="date" id="startDate" name="startDate" 
				 placeholder="${fromDate}" data-label="${fromDate}" data-required='Y'></div>
            </dd>
        </dl>
        <dl>
            <dt>
            	<c:set var="rank"><spring:message code="rank.rank" text="등급" /></c:set>
                ${rank}
            </dt>
            <dd>
                <select class="select-ajax" name="rankId" id="rankId"
				data-codetype="" data-code="" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "E010000" }'>
				</select>
            </dd>
        </dl>
        <dl>
            <dt><c:set var="discount"><spring:message code="rank.discount" text="할인율" /></c:set>
            	${discount}
           	</dt>
            <dd>
                <select class="select-ajax" id="discount" name="discount"
				data-codetype="" data-code="" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "J010000" }'>
				</select>
            </dd>
        </dl>
     
	</div>
	</form>
    <div class="pop_btn">
        <button type="button" class="btn_type2" id="btn_write_pop" ><span><spring:message code="button.save" text="저장" /></span></button>
    </div>
    
</div><!--pop_body end-->