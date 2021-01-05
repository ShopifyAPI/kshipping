<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_ADM_613_BAS 
기능설명 : NOTICE 상세보기
Author   Date      Description
 YR     2020-02-20  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>

    <title>셀러 NOTICE 상세조회</title>
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
		 	location.href = "/";
		
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
                        <h3><spring:message code="notice.noticeText" text="공지사항 내용" /></h3>
                    </div> <!--tit_wrap end-->
                    <div class="tit_wrap mt30">
                    </div>
                    <div class="tit_wrap mt0">
                    </div>
                    <div class="tit_wrap mt10">
                        <ul class="tab">
                            <li id="kshipping" class="on"><a href="/" ><spring:message code="notice.menuKshipping" text="K-Shipping" /></a></li>
                            <li id="post"><a href="/selectPost"><spring:message code="notice.menuPost" text="배송사" /></a></li>
                        </ul>                
                    </div> <!--tit_wrap end--> 
                                       
                    <ul class="tab_conts">
                        <li>
                            <div class="tit_wrap mt0">
                                <h4 class="subtit"><spring:message code="notice.noticeshow" text="공지 상세" /></h4>
                                
                            </div>
                            <form name="submitform" id="submitform" method="post">
                            <table class="tbtype">         
                                <colgroup>
                                    <col class="cper1500">
                                    <col class="cperauto">
                                </colgroup>                       
                                <tbody>
                                    <tr>
                                        <th class="t_center">[ ${mainData.type} ]</th>
                                        <td>
                                           <p> ${mainData.title}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
								            <div class="noticeeditor">
                                                <p>${mainData.content}</p>
                                            </div>
<%--                                             <div class="reple">
                                                <p>${mainData.writer}</p>                                            </div> --%>
                                        </td>
                                    </tr>
								<c:if test="${not empty mainFileData}">
								<c:forEach items="${mainFileData}" var="list" varStatus="status">
								<tr> <a href="path_to_file" download="proposed_file_name">
									<td colspan="2">
										${status.count}. ${list.fileName} (${list.size}bytes) [<a href="${list.fileDownloadUri}" download="${list.fileName}">다운로드</a>]
									</td>
								</tr>
								</c:forEach>								
								</c:if>                                 
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