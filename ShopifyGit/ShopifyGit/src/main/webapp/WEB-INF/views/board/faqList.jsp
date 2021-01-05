<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_ADM_621_TAB ~ UI_ADM_625_TAB
기능설명 : 셀러 FAQ 리스트 조회
Author   Date      Description
 김윤홍     2019-12-23  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>
    
    <title>셀러 FAQ 리스트 조회</title>
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
		
		var searchType = $("#defaultSearchForm input[name='searchType']").val();
		//subTab 클릭시 css변경
		if(searchType=="D020000" || searchType=="" || searchType==null){
			$("a[name=sub_tab_a]").removeClass();
			$("#D020000").addClass("on");
		}else if(searchType=="D020010"){
			$("a[name=sub_tab_a]").removeClass();
			$("#D020010").addClass("on");
		}else if(searchType=="D020020"){
			$("a[name=sub_tab_a]").removeClass();
			$("#D020020").addClass("on");
		}else if(searchType=="D020030"){
			$("a[name=sub_tab_a]").removeClass();
			$("#D020030").addClass("on");
		}else if(searchType=="D020040"){
			$("a[name=sub_tab_a]").removeClass();
			$("#D020040").addClass("on");
		}else{
			$("a[name=sub_tab_a]").removeClass();
			$("#D020000").addClass("on");
		}
    
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
    
	var selectPart = function(searchType){
		$("#searchType").val(searchType);
		
		$("#defaultSearchForm input[name='searchType']").val(searchType);
		$("#defaultSearchForm input[name='currentPage']").val("1");
        
		defaultSearchForm.submit();
        
	}
	
	var displayControl = function(idx){
		
		var classChk = $("#"+idx).hasClass("on");
		
		if(classChk == true){
			$("#"+idx).removeClass("on");
		}else{
			$("li[name='content']").removeClass("on");
			$("#"+idx).addClass("on");
		}
		
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
		                        <li id="qna" ><a href="/board/selectQna" ><spring:message code="board.menuQna" text="Q&A" /></a></li>
		                        <li id="faq" class="on"><a href="/board/selectFaq"><spring:message code="board.menuFaq" text="FAQ" /></a></li>
                    		</ul>
                    		
                    		<ul class="tab_conts">
                    			<li>
                    				<div class="sub_tab">
	                  					<a href="#" id="D020000" name="sub_tab_a" onclick="selectPart('D020000');"><spring:message code="button.all" text="전체" /></a>
										<a href="#" id="D020010" name="sub_tab_a" onclick="selectPart('D020010');"><spring:message code="button.order" text="주문" /></a>
										<a href="#" id="D020020" name="sub_tab_a" onclick="selectPart('D020020');"><spring:message code="button.ship" text="배송" /></a>
										<a href="#" id="D020030" name="sub_tab_a" onclick="selectPart('D020030');"><spring:message code="button.site" text="사이트" /></a>
										<a href="#" id="D020040" name="sub_tab_a" onclick="selectPart('D020040');"><spring:message code="button.etc" text="기타" /></a>
                    				</div>
                                
                                	<div>
                                	<form name="submitform" id="submitform" method="get" action="/board/selectFaq">
									<input type="hidden" id="division" name="division" value="D010001">
									<input type="hidden" id="searchType" name="searchType" value="">
	                                <ul class="faq">
	                               		 <c:choose>
											 <c:when test="${fn:length(list) == 0}">
												 <tr>
												 	<div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
												 </tr>
		
											 </c:when>
											 <c:otherwise>
												 <c:forEach items="${list}" var="list" varStatus="status">
												 	
													<c:if test="${status.index == 0}">
														<li class="" name="content" id="${list.idx}" onclick="displayControl(${list.idx})">
					                                        <a href="#" class="faq_head">Q. ${list.content}</a>
					                                        <div class="faq_answer">
					                                            <p><span>A.</span>
					                                              	 ${list.answer}
					                                             </p>
					                                        </div>
				                                    	</li>
													</c:if>
													<c:if test="${status.index > 0}">
														<li name="content" id="${list.idx}" onclick="displayControl(${list.idx})">
					                                        <a href="#"  class="faq_head">Q. ${list.content}</a>
					                                        <div class="faq_answer">
					                                            <p><span>A.</span>
					                                             ${list.answer}
					                                             </p>
					                                        </div>
					                                    </li>
													</c:if>
														
														
													 
												 </c:forEach>
											 </c:otherwise>
										 </c:choose>
	                                </ul>
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
                           			</div>
                    			</li>
                    		</ul>
				</article>
			</div>
		</div>
	</div>
    
    <%// footer include %>
    <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
	
</body>
</html>