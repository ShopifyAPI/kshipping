<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>  

<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%>
    
    <title>Title</title>
    <script type="text/javascript">
	
	$(document).ready(function(){
		$("input[name]").keyup(function(e){
            if(e.keyCode == 13){
                fnLogin();
            }
        });
		
		var parameter  = {
		    	email : ''
		    	, passwd : ''
			};
		$form = $("#ajax-form");
    	$("#loginBtn").on('click', function(){
    		if(fnValidationCheckForInput($form)){
    			parameter.email=$("#email").val();
    			parameter.passwd=$("#password").val();
    			fnLogin();
    		}
    	});
	    
    	var fnLogin  = function(){
    		if(fnValidationCheckForInput($form)){
                parameter.email=$("#email").val();
                parameter.passwd=$("#password").val();
            }
    		
            var url = "/admin/loginProc";
            ajaxCall("post", url, parameter, setPageView);
    	}
        
    	var setPageView = function(data){
        	data.errCode ? location.replace(data.redirect) : alert(data.errMsg);
        }
        
        var key = getCookie("ShopifyAdminSaveIdKey");
    	$("#email").val(key); 
    	 
    	if($("#email").val() != ""){ // 그 전에 ID를 저장해서 처음 페이지 로딩 시, 입력 칸에 저장된 ID가 표시된 상태라면,
    		$("#saveId").attr("checked", true); // ID 저장하기를 체크 상태로 두기.
    	}
    	 
    	$("#saveId").change(function(){ // 체크박스에 변화가 있다면,
    		if($("#saveId").is(":checked")){ // ID 저장하기 체크했을 때,
    			setCookie("ShopifyAdminSaveIdKey", $("#email").val(), 7); // 7일 동안 쿠키 보관
    		}else{ // ID 저장하기 체크 해제 시,
    			deleteCookie("ShopifyAdminSaveIdKey");
    		}
    	});
    	 
    	// ID 저장하기를 체크한 상태에서 ID를 입력하는 경우, 이럴 때도 쿠키 저장.
    	$("#email").keyup(function(){ // ID 입력 칸에 ID를 입력할 때,
    		if($("#saveId").is(":checked")){ // ID 저장하기를 체크한 상태라면,
    			setCookie("ShopifyAdminSaveIdKey", $("#email").val(), 7); // 7일 동안 쿠키 보관
    		}
    	});
	    
    });
	
	</script>
</head>
<body>

    <div class="wrap">
        <div class="login">
            <div class="form_login">
                <h3><spring:message code="common.login" text="로그인" /></h3>
                <div class="idbox" id="ajax-form">
                    <input value="" type="text" id="email" name="email" maxlength="100" data-label="Email" data-required="Y" placeholder="Email"/>
                    <input value="" type="password" id="password" name="password" maxlength="50" data-label="Password" data-required="Y" placeholder="Password" />

                    <button type="button" class="btn_type2" id="loginBtn"><spring:message code="common.login" text="로그인" /></button>
                    <div class="mt30">
                        <input type="checkbox" id="saveId"><label for="saveId"><span class="icon_ck"></span><span class="label_txt"><spring:message code="common.login.idsave" text="아이디 저장" /></span></label>
                        <!-- <a href="#" class="find_pw">비밀번호 찾기</a> -->
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <%// footer include %>
	<%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
    
</body>
</html>