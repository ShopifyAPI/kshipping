<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>  
<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>
    
    <title>Title</title>
    <script type="text/javascript">
    var titleMsg = "${ title }";
    var messageMsg = "${ message }";
    
    $(function () {
        $("#popTitle").text(getMessage(titleMsg));
        $("#popBody").text(getMessage(messageMsg));
        
        $(document).on("click","#btnOk", function(){
        	$(".btn_close").click();
            procOk();
        });
        
        $(document).on("click","#btnCancel", function(){
        	$(".btn_close").click();
        });
        
        var procOk = function(){
            var param = "title:'incgnb.order',message:'alert.deleteMessage'";// ***중요=> 공백 없어야함
            openPop("/common/popAlert", param, openf, funcf, closef);
        }
    });
    </script>
</head>
<body>
   
    <div class="frame"> <!--프레임 영역 /// 이 div 영역부터 붙여서 넣어주세요.-->
        <div> <!-- 팝업-->
            <div class="popup">
                <div class="pop_head">
                    <h3 id="popTitle"></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                   <P class="t_center" id="popBody"></P>
                   <P class="t_center">&nbsp;</P>
                   <p class="t_center">
                   <button type="button" class="btn_type1" id="btnOk"><span><spring:message code="button.confirm" text="확인" /></span></button>
                    <button type="button" class="btn_type1" id="btnCancel"><span><spring:message code="button.cancel" text="취소" /></span></button>
                   </p>
                </div><!--pop_body end-->
            </div><!-- popupp end-->
            <div class="dimmed"></div>
        </div>
        <div class="frame_wrap"></div>
    </div> <!--frame end -->


</body>
</html>
