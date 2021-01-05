<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>  
<!-- 
**************************************************
화면코드 : UI_ADM_532_POP 
기능설명 : 설정관리 > 배송관리 > 포장재 popup
Author   Date         Description
jwh      2020-01-29   First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeaderAddress.jsp"%>
    <title><spring:message code="incgnb.order" text="주문" /></title>
    <link type="text/css" rel="stylesheet" href="/style/order.css" />
    <link type="text/css" rel="stylesheet" href="/style/courier.css" />
    
<!--     <script type="text/javascript" src="/js/order/order.js"></script> -->
<!--     <script type="text/javascript" src="/js/order/orderPopup.js"></script> -->
    
    <script type="text/javascript" src="/js/order/addressPopup.js"></script>
               
    
</head>
<body class="popup modal_cv">

	<div class="pop_head">
	    <h3><spring:message code="order.popup.address.title1" text="주소등록" /></h3>
	</div>
	<div class="pop_body">
	    <form name="addressForm" id="addressForm">
	    <div class="form_side">
	        <dl>
	            <dt>
	                <c:set var="addr1"><spring:message code="settings.addr1" text="주소" /></c:set>
	                <p class="required">${addr1}</p>
	            </dt>
	            <dd>
	                <input type="text" class="cper100p" name="addr1" id="addr1" value="" 
	                     maxlength="100" data-required="Y" data-label="${addr1}" placeholder="${addr1}">
	            </dd>                            
	            <dt>
	                <c:set var="addr2"><spring:message code="settings.addr2" text="상세주소" /></c:set>
	                <p>${addr2}</p>
	            </dt>
	            <dd>
	                <input type="text" class="cper100p" name="addr2" id="addr2" value="" 
	                     maxlength="100" data-label="${addr2}" placeholder="${addr2}">
	            </dd>

	        </dl>
	    </div>
	    
	    <div class="pop_btn mgct">
	            <button type="button" id="addressChangeSave" class="btn_type2 cper100p edit"><spring:message code="button.save" text="저장" /></button>
	            <button type="button" id="addressChangeCancel" class="btn_type3 cper100p save"><spring:message code="button.cancel" text="취소" /></button>
	    </div>
	    </form>
	</div>
                
</body>
</html>                