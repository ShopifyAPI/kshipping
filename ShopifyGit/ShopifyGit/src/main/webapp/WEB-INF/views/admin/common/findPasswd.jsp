<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>  

<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>
    
    <title>Title</title>
    <script type="text/javascript">
    $form = $("#ajax-form");
    var parameter  = {
    		email : ''
    	};
    $(function() {
    	$("#findBtn").on('click', function(){
    		if(fnValidationCheckForInput($form)){
    			parameter.email=$("#email").val();
    			findPasswd();
    		}
    	});
    });
 
    var findPasswd  = function(){	
		var type ="post";
		var url = "/admin/findPasswdSend";
		console.log(parameter)
		ajaxCall(type, url, parameter, setPageView);
	}
	
	var setPageView = function(data){
		console.log(data);
	}

	</script>
</head>
<body>

    <%// gnb include %>
    <%@ include file="/WEB-INF/views/common/incGnb.jsp"%>

	<div id="ajax-form">
	<h1> <spring:message code="index.hello" text="hello text" />/admin/common/findPasswd.jsp!11</h1>
	<table border = "1" style="width:400px">
		<tr class="text-center">
			<th ><input type="text" id="email" name="email" maxlength="100" data-label="E-Mail" data-required="Y" placeholder="E-Mail"/></th>
		</tr>
		<tr class="text-center">
			<th><input type="button" id="findBtn" value="비밀번호 찾기" /> </th>
		</tr>
	</table>
	</div>
    
    <%// footer include %>
	<%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
    
</body>
</html>