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
		};

	var bindEvent = function() {
 
        $(document).on("click","#btn_cancel",function(){  
            
        	/*자바스크립트  히스토리백 , 히스토리 홈 -1 또는 -1*/        	
//         	history.back();
        	history.go(-1);
            
        });
        
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
                    <div class="tit_wrap mt45">
                        <h3 class="logo">! 공지</h3>
                        
                    </div> <!--tit_wrap end-->
                    <p class="txt20 mt45"><spring:message code="common.request" text="Kshipping에서" /> </p>
					
					<p class="txt20"><spring:message code="common.request1" text="Kshipping에서" /> </p>
					<p class="txt20"><spring:message code="common.request2" text="Kshipping에서" /> </p>

                    <p class="bold txt20 color_lb mt45"><spring:message code="common.request3" text="shopigate@solugate.com" /></p>
                    <div class="button t_center">
                        <button type="button" class="btn_type1" id="btn_cancel"><spring:message code="button.prev" text="뒤로" /></button>                      
                    </div>
                    
                    
                </article>
            </div>
        </div>

        

    </div> <!--frame end -->
   <%// footer include %>
   <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
</body>
</html>
