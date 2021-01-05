<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <meta charset="UTF-8" />
    <meta name="_csrf_parameter" content="${_csrf.parameterName}" />
    <meta name="_csrf_header" content="${_csrf.headerName}" />
    <meta name="_csrf" content="${_csrf.token}" />
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="initial-scale=1.0, width=device-width">
    
  <!--    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
    <link rel="icon" href="/favicon.ico" type="image/x-icon">  -->
    
   <link rel="shortcut icon" href="//cdn.shopify.com/s/files/1/0278/0738/3690/files/ms-icon-310x310_32x32.png?v=1599026309" type="image/png">
    
    <link rel="stylesheet" href="https://unpkg.com/@shopify/polaris@4.10.2/styles.min.css"/>
    <link type="text/css" rel="stylesheet" href="/style/reset.css?v=<%=System.currentTimeMillis() %>" />
    <link type="text/css" rel="stylesheet" href="/style/admin.css?v=<%=System.currentTimeMillis() %>" />

    <script src="//js.stripe.com/v3/"></script>
    <script src="//widget.cloudinary.com/v2.0/global/all.js" type="text/javascript"></script>  
    <script src="//cdn.shopify.com/s/assets/external/app.js"></script>
    <script src="//code.jquery.com/jquery-1.12.4.js"></script>
	<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	
    <link type="text/css" rel="stylesheet" href="/style/spinner.css?v=<%=System.currentTimeMillis() %>" />

    <script src="/js/jquery.i18n.properties.js"></script>	
<!--     <script src="/js/jquery.i18n.properties.min.js"></script>	 -->
    <script src="/js/jquery.bpopup.min.js"></script>
    <script src="/js/jquery.form.js"></script>
    <script src="/ckEditor/ckEditor/ckeditor.js"></script>
    <script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>
    <script src="/js/util.js?v=<%=System.currentTimeMillis() %>"></script>
    <script type="text/javascript">
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
      
    var _USER_lang = "${SHOPIFY_LOCALE_SESSION.locale}";
	var _MESSAGE;
	
	//세션 메뉴권한
	var authority = "${SHOPIFY_ADMIN_SESSION.authority}";
	//세션권한 	split
	if(authority){
		authority = authority.split("|");
		//권한 배열의 첫번째, 마지막은 제외시킨다 (공백임)
		if(authority != ""){
			authority.shift();
			authority.pop();
		}
	}
	//세션권한에 따라 메뉴 show, hide
	$(document).ready(function(){
    	//gnb.jsp menu의 length를 구함
    	var num = $(".menu").children().length;
    	//메뉴의 length 만큼 반복
    	for(var i=0; i<=num; i++){
    		//gnb.jsp 메뉴 class 명
    		var temp = $(".menu").children().eq(i).attr('class');
    		//세션 권한에 gnb class가 포함되어 있는지 판단 (-1이면 없는거임)
    		if(authority.indexOf(temp)!== -1){
    			//메뉴보이기
    			jQuery("."+temp).show();
    		}else{
    			//메뉴 숨기기
    			if(temp != "show") {
    				jQuery("."+temp).hide();	
    			}
    		}
    	}
	});
	
	</script>
