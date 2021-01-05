<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_ADM_613_BAS 
기능설명 : Q&A 상세보기
Author   Date      Description
 김윤홍     2019-12-23  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>

    <title>셀러 Q&A 상세조회</title>
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

	};

	var bindEvent = function() {
		
		$("#part").attr("readonly",true);
		$("#title").attr("readonly",true); 
		$("#content").attr("readonly",true); 
		$("#answer").attr("readonly",true); 
		
		
		$("#btn_list").on('click',function(){
			
			//목록화면이로 이동
		 	location.href = "/board/selectQna";
		
		});	
		
	};
	
</script>
</head>
<body>
    <div class="frame">
		<div class="frame_wrap">
			<%// gnb include%>
			<%@ include file="/WEB-INF/views/common/incGnb.jsp"%>
			
			
			<div class="sub_conts">
                <article>
                    <div class="tit_wrap">
                        <h3><spring:message code="board.menuServicecenter" text="고객센터" /></h3>
                    </div> <!--tit_wrap end-->
                    <ul class="tab">
	                    <li class="on"><a href="/board/selectQna"><spring:message code="board.menuQna" text="Q&A" /></a></li>
	                    <li><a href="/board/selectFaq"><spring:message code="board.menuFaq" text="FAQ" /></a></li>
               		</ul>
                    <ul class="tab_conts">
                        <li>
                            <div class="tit_wrap mt0">
                                <h4 class="subtit"><spring:message code="board.qnashow" text="문의 상세" /></h4>
                                
                            </div>
                            <form name="submitform" id="submitform" method="post">
                            <table class="tbtype">         
                                <colgroup>
                                    <col class="cper1500">
                                    <col class="cperauto">
                                </colgroup>                       
                                <tbody>
                                    <tr>
                                        <th class="t_center">[ ${boardData.partName} ]</th>
                                        <td>
                                           <p> ${boardData.title}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <div class="editor">
                                               <p>${boardData.content}</p>
                                            </div>
                                            <div class="reple">
                                                <p>${boardData.answer}</p>                                            </div>
                                        </td>
                                    </tr>
                                 
                                </tbody>
                            </table>
                            </form>
                           
                            <div class="button">
                                <button type="button" class="btn_type5" id="btn_list"><span><spring:message code="button.list" text="목록" /></span></button>
                            </div>
                        </li>
                    </ul>
                </article>
            </div>
            
			<%// footer include%>
			<%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
		</div>
	</div>
	
</body>
</html>