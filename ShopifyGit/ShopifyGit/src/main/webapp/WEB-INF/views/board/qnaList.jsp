<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_ADM_611_TAB 
기능설명 : AMD Q&A 리스트 조회
Author   Date      Description
 김윤홍     2019-12-23  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>

    <title>셀러 Q&A 리스트 조회</title>
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
		var $submitform = $('#searchform');
    	bindSelectAjax($submitform);

	};

	var bindEvent = function() {
		
		//Q&A 조회
		$("#btn_search").on('click',function(){
			search();
		});
		
		$(document).on("keyup","#btn_search",function(){
            if(e.keyCode == 13){
                search();
            }
        });
		
		// 상세보기
        $(document).on("click",".list-edit",function(){  
            var idx = $(this).closest("tr").data("idx");
            var url = "/board/showQna?idx=" + idx;
            
            location.href = url;
        });
		
		//Q&A 등록화면으로 이동
		$("#btn_write").on('click',function(){
			location.href = "/board/newQna";
			
		});	
		
	};
	
	// 페이징 페이지 이동
    var gotoPage = function(page) {
        $("#defaultSearchForm input[name='currentPage']").val(page);
        defaultSearchForm.submit();
    }
    
    // 페이지 사이즈 변경
    var changPageSize = function(val){
        $("#defaultSearchForm input[name='pageSize']").val(val);
        $("#defaultSearchForm input[name='currentPage']").val("1");
        
        defaultSearchForm.submit();
    }
    
    // 전체 검색
    var search = function(){
        searchform.submit();
    }
	
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
						</div>
						
						<ul class="tab">
	                        <li class="on"><a href="/board/selectQna"><spring:message code="board.menuQna" text="Q&A" /></a></li>
	                        <li><a href="/board/selectFaq"><spring:message code="board.menuFaq" text="FAQ" /></a></li>
                   		</ul>
                    		
                    		<ul class="tab_conts">
                    			<li>
                    				<div class="tit_wrap mt0">
                               		<h4 class="subtit"><spring:message code="board.qnalist" text="나의 문의내역" /></h4>
										<div class="sect_right">
										<form name="searchform" id="searchform" method="get">
											
											<select class="select-ajax" id="searchType" name=searchType
											 data-codetype="" data-code="${search.searchType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "D020000" }'></select>
		                                    
		                                    <div class="searchbox"> <!--인풋박스에 클릭시 active 클래스 추가-->
		                                        <button type="button" class="ic_search" id="btn_search"></button>
		                                        <input type="text" id="searchWord" value="${search.searchWord}" name="searchWord" placeholder="<spring:message code="order.seaech.orderWord.label" text="검색어를 입력해주세요." />">
		                                    </div>
		                                    
		                                    <%-- 페이지 사이즈 --%>
                                            <%@ include file="/WEB-INF/views/common/incPageSize.jsp"%>
										
										</form>
		                                    
                                		</div>
                            		</div>
									<form name="submitform" id="submitform" method="get" action="/board/selectQna">
									<input type="hidden" id="division" name="division" value="D010002">
									<c:choose>
										<c:when test="${fn:length(list) == 0}">
											<tr>
												<div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
											</tr>
										</c:when>
									<c:otherwise>                            		
                            		<table class="tbtype">
		                                <colgroup>
		                                    <col class="cper0780">
		                                    <col class="cper1250">
		                                    <col class="cperauto">
		                                    <col class="cper1100">
		                                    <col class="cper1100">
		                                </colgroup>
                                	<thead>
	                                    <tr>
	                                        <th><spring:message code="board.idx" text="번호" /></th>
											<th><spring:message code="board.part" text="구분" /></th>
											<th><spring:message code="board.title" text="제목" /></th>
											<th><spring:message code="board.regDate" text="등록일" /></th>
											<th><spring:message code="board.status" text="상태" /></th>
	                                    </tr>
                                	</thead>
	                                <tbody>
												<c:forEach items="${list}" var="list" varStatus="status">
													<tr data-idx="${list.idx}">
														<td class="t_center list-edit"><p>${paging.countNum - status.index}</p></td>
														<td class="t_center list-edit">${list.partName}</td>
														<td class="list-edit">${list.title}</td>
														<td class="t_center list-edit">${list.regDate}</td>
														<td class="t_center list-edit">
														      <c:choose>
                                                                <c:when test="${empty list.answer or list.answer eq ''}">
                                                                    <div class="btn_state cancel"><spring:message code="board.answer.no" text="미답변" /></div>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <div class="btn_state pay"><spring:message code="board.answer.yes" text="답변완료" /></div>
                                                                </c:otherwise>
                                                              </c:choose>
														</td>
													</tr>
		
												</c:forEach>
											</c:otherwise>
										</c:choose>
	                                </tbody>
		                            </table>
		                            </form>
		                            
		                            <div class="pager">
				                        <c:if test="${fn:length(list) > 0}">
				                        <%@ include file="/WEB-INF/views/common/incPaging2.jsp"%>
				                        </c:if>
				                        <form name="defaultSearchForm" id="defaultSearchForm" method="get">
				                        <input type="hidden" name="searchType" value="${search.searchType}">
				                        <input type="hidden" name="searchWord" value="${search.searchWord}">
				                        <input type="hidden" name="pageSize" value="${search.pageSize}">
				                        <input type="hidden" name="currentPage" value="${search.currentPage}">
				                        </form>
				                    </div>
		                            
		                            <div class="button">
		                                <button type="button" class="btn_type3" id="btn_write"><span><spring:message code="button.write" text="작성" /></span></button>
		                            </div>		
                           		</li>
                           </ul>
						<!--// tit_wrap end -->
						
					<!--// #### Content area ############### -->

				</article>
			</div>
		
		</div>
	</div>
    
    <%// footer include %>
    <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
</body>
</html>