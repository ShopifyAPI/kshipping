<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : 
기능설명 : 약관
Author   Date      Description
 김윤홍     2020-02-18  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>

    <title>약관</title>
    <script type="text/javascript">
    
    // READY
	$(function() {
		initialize();
	});

	var initialize = function() {
		initControls();
		bindEvent();
	};

	var initControls = function() {
		$(".gnb_menu ul").hide();
		
// 		var userLocale = _USER_lang;
		var userLocale = 'ko';
		
		if(userLocale == "ko"){
			console.log("1")
			document.getElementById("iframe_agree1").src = "policy_ko.html";
			document.getElementById("iframe_agree2").src = "privacy_ko.html";
			document.getElementById("iframe_agree3").src = "agree3_ko.html";
			
		}else if(userLocale =="en"){
			console.log("2")
			document.getElementById("iframe_agree1").src = "policy_en.html";
			document.getElementById("iframe_agree2").src = "privacy_en.html";
			document.getElementById("iframe_agree3").src = "agree3_en.html";
			
		}else{
			console.log("3")
			document.getElementById("iframe_agree1").src = "policy_ko.html";
			document.getElementById("iframe_agree2").src = "privacy_ko.html";
			document.getElementById("iframe_agree3").src = "agree3_ko.html";
			
		}
	};

	var bindEvent = function() {
	
		// 약관동의 
        $(document).on("click","#btn_save",function(){  
            
        	var agree1 = $("#agree1").is(":checked");
        	var agree2 = $("#agree2").is(":checked");
        	var eventchk;       	        	
        	if($("#agree3").is(":checked")){
        		eventchk = "Y";

        	}else{
        		eventchk = "N";
        	} 
        	
        	//체크박스 체크여부확인
        	if(!$("#agree1").is(":checked") ||
        	   !$("#agree2").is(":checked")){
        		
        		if(agree1==false){
        			alert(getMessage("alert.terms.agree", ["terms.termsofUse"]));//이용약관에 동의해주세요.
        			$("#agree1").focus();
        		}else if(agree2==false){
        			alert(getMessage("alert.terms.agree", ["terms.privacyPolicy"]));//개인정보 처리방침에 동의해주세요.
        			$("#agree2").focus();
        		}else{
        			return;
        		}
        		return;
        	}
        	
            var type = "post";
            var url = "/terms/updateTerms";
			// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
			var param = {"eventchk":eventchk}
			
			ajaxCall(type, url, param, sendFrom);
            
        });
		
        // 약관미동의 
        $(document).on("click","#btn_cancel",function(){  
            
        	/*자바스크립트  히스토리백 , 히스토리 홈 -1 또는 -1*/
        	
//         	history.back();
        	history.go(-1);
            
        });
        
    }
	
	//Ajax CallBack
	var sendFrom = function(){
    	alert("약관동의 성공");
    	location.replace("/")
    	/* location.replace("/") */
    	
    }
	
	</script>
</head>
<body>
    <div class="frame"> <!--프레임 영역 이 div 영역부터 붙여서 넣어주세요.-->
        <div class="frame_wrap">
        
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incGnb.jsp"%> 
        
        
            <div class="sub_conts">
                <article> <!--간격이 필요하시면  class="mt45" 추가해주세요-->
                    <div class="tit_wrap">
                        <h3 class="logo">Kshipping</h3>
                    </div> <!--tit_wrap end-->
                    <p class="bold txt16"><spring:message code="terms.terms1" text="Kshipping에서" /> <span class="color_lb">${ShopData.email}</span><spring:message code="terms.terms2" text="님의 개인정보에 동의하십니까?" /></p>
                    <P class="mt20"><spring:message code="terms.terms3" text="제공된 정보는 이용자 식별,통계,계정 연동 및 CS 등을 위해 서비스 이용기간 동안 활용/보관됩니다." /></P>
                    <P><spring:message code="terms.terms4" text="기본정보 및 필수 제공항목은 K shipping 서비스를 이용하기 위해 반드시 제공되어야 할 정보입니다." /></P>

                    <div class="term_tit">
                        <h4><font color ="red"><spring:message code="terms.termsofUse" text="이용약관" /></font></h4>
                        <input type="checkbox" id="agree1" name="required"><label for="agree1"><span class="icon_ck"></span><span class="label_txt"><spring:message code="terms.agree" text="동의합니다." /></span></label> 
                    </div>
                    
                    <!-- 약관1 -->
                    <iframe id="iframe_agree1" height="100%" width="100%" src="policy_ko.html" style="border-width:1px; border-color:CCC; border-style:solid; padding:10px"></iframe> 
                    

                    <div class="term_tit mt20">
                        <h4><font color ="red"><spring:message code="terms.privacyPolicy" text="개인정보 처리방침" /></font></h4>
                        <input type="checkbox" id="agree2" name="required"><label for="agree2"><span class="icon_ck"></span><span class="label_txt"><spring:message code="terms.agree" text="동의합니다." /></span></label>
                    </div>
                    <!-- 약관2 -->
					<iframe id="iframe_agree2" height="100%" width="100%" src="privacy_ko.html" style="border-width:1px; border-color:CCC; border-style:solid; padding:10px""></iframe>
                   
                   <div class="term_tit mt20">
                        <h4><spring:message code="terms.promotion" text="이벤트 프로모션 동의" /></h4>                     
                        <input type="checkbox" id="agree3" name="required"><label for="agree3"><span class="icon_ck"></span><span class="label_txt"><spring:message code="terms.agree" text="동의합니다." /></span></label>                        
                    </div>
                    <!-- 약관3 -->
					<iframe id="iframe_agree3" height="100%" width="100%" src="agree3_ko.html" style="border-width:1px; border-color:CCC; border-style:solid; padding:10px""></iframe>
                    
                    <p class="mt10"><spring:message code="terms.terms5" text="동의 후에는 해당서비스의 이용갹관 및 개인정보 처리방침에 따라 정보가 관리됩니다." /></p>
                    <div class="button t_center">
                        <button type="button" class="btn_type1" id="btn_cancel"><spring:message code="button.cancel" text="취소" /></button>
                        <button type="button" class="btn_type2" id="btn_save"><spring:message code="button.agree" text="동의" /></button>
                    </div>
                    
                    
                </article>
            </div>
        </div>

        

    </div> <!--frame end -->
   <%// footer include %>
   <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
</body>
</html>
