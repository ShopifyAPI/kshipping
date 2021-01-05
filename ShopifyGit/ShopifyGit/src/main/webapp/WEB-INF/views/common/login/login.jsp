<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>  

<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>
    
    <title>Title</title>
    <script type="text/javascript">
	
	$(document).ready(function(){
		top.location.replace("https://accounts.shopify.com/store-login")
    	$("#loginBtn").on('click', function(){
    		top.location.replace("https://accounts.shopify.com/store-login")
    	});
	    
    });
	
	</script>
</head>
<body>

    <div class="wrap">
        <div class="login">
            <div class="form_login">
                <h3><spring:message code="common.login" text="로그인" /></h3>
                <div class="idbox">
                    <button type="button" class="btn_type2" id="loginBtn"><spring:message code="common.login" text="로그인" /></button>
                </div>
            </div>
        </div>
    </div>
    
    <%// footer include %>
	<%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
    
</body>
</html>