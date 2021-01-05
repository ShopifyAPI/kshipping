<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_SYS_111_BAS
기능설명 : 관리자 > 셀러정보관리 > 리스트
Author   Date      Description
 jwh     2020-01-22  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 
	<link type="text/css" rel="stylesheet" href="/style/order.css?v=<%=System.currentTimeMillis() %>" />
	
    <title>ADM FAQ 리스트 조회</title>
    <script type="text/javascript">
    
    // READY
    $(function () {
        initialize();
    });
 
    var initialize = function () {
        initControls();
        bindEvent();
    };
    
    var initControls = function () {
        var $submitform = $('#searchform');
        bindSelectAjax($submitform); // 검색 select box 세팅
    	setSort();
    };

    var bindEvent = function () {
        // 상세보기
        $(document).on("click",".list-view",function(){  
            var email = $(this).closest("tr").data("email");
            var param = $("#defaultSearchForm").serialize();
            
            param = encodeURI("email=" + email + "&" + param);
            location.href = "/admin/seller/viewSeller?" + param;
        });

        //검색
        $(document).on("click",".ic_search",function(){
            search();
        });
        
        $(document).on("keyup",".ic_search",function(){
            if(e.keyCode == 13){
                search();
            }
        });
    	//정렬 처리 하기
    	$(document).on("click", "table.tbtype thead tr th", function () {

    		if($(this).data("sort-name") != null)  {
    			var sortName = $(this).data("sort-name");

    			var oldSortName = $("input[name='sortOrder']").val();
    			if(oldSortName=="DEC"+sortName) {
    				$("input[name='sortOrder']").val("ASC"+sortName);
    			} else {
    				$("input[name='sortOrder']").val("DEC"+sortName);
    			}

    			gotoPage(1);

    		}
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

    
    var setSort = function() {

    	var sortOrder = $("input[name='sortOrder']").val();
    	var preFix =sortOrder.substring(0,3);
    	var strSort =sortOrder.substring(3);
    	$("table.tbtype thead tr th").each(function() {
    		var sortName = $(this).data("sort-name");
    		if(sortName == strSort && preFix == "DEC")  {
    			$(this).find("div").attr("class","desc-sort");
    		} else if(sortName == strSort && preFix == "ASC")  {
    			$(this).find("div").attr("class","asc-sort");
    		} else if(sortName != undefined) {
    			$(this).find("div").attr("class","sort-default");
    		}
    	});
    }
    
    // 전체 검색
    var search = function(){
        searchform.submit();
    }
    </script>
    
</head>
<body>
    <div class="wrap">
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incAdmGnb.jsp"%> 

        <!-- #### Content area ############### -->

        <div class="contents">
           <div class="cont_body">
               <article>
                    <h2><spring:message code="admin.seller" text="셀러 정보 관리" /></h2>
                    <div class="module_group mt20">
                        <div class="action">
                            <form name="searchform" id="searchform" method="get">
                            
                            <select class="select-ajax" name="searchRankId"  id="searchRankId" 
                               data-codetype="etc" data-code="${search.searchRankId}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "E010000" }'></select>
                            
                            <select class="select-ajax" name="searchType"  id="searchType" 
                               data-codetype="etc" data-code="${search.searchType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A140000" }'></select>
                            
                            <div class="searchbox">
                                <input type="text" name="searchWord" id="searchWord" value="${search.searchWord}" placeholder="<spring:message code="common.searchWord" text="검색어" />">
                                <button type="button" class="ic_search"></button>
                            </div>
                                            
                            <%-- 페이지 사이즈 --%>
                            <%@ include file="/WEB-INF/views/common/incPageSize.jsp"%>
                            
                            </form>
                        </div>
                    </div>
                    
                    <form name="listform" id="listform" method="get">       
                                     
                <c:choose>
                    <c:when test="${fn:length(list) == 0}">
                        
                        <div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
                    
                    </c:when>
                    <c:otherwise>
                    
                    <table class="tbtype">
                        <colgroup>
                            <col class="wp50">
                            <col class="cperauto">
                            <col class="cperauto">
                            <col class="cper1100">
                            <col class="cper0780">
                            <col class="cper0780">
                            <col class="cper1100">
                        </colgroup>
                        <thead>
                            <tr>
                                <th><spring:message code="settings.idx" text="번호" /></th>
                                <th><spring:message code="settings.email" text="이메일" /></th>
                                <th><spring:message code="settings.shopName" text="쇼핑몰명" /></th>
                                <th><spring:message code="settings.name" text="이름" /></th>
                                <th><spring:message code="settings.rank" text="등급" /></th>
                                <th><spring:message code="settings.shopStatus" text="활동상태" /></th>
                                <th data-sort-name="regDate"><div><spring:message code="board.regDate" text="등록일" /></div></th>
                                
                            </tr>
                        </thead>
                        <tbody>
                        
                        <c:forEach items="${list}" var="item" varStatus="status">
                            <tr data-email="${item.email}">
                                <td class="t_center"><p>${paging.countNum - status.index}</p></td>
                                <td class="list-view"><p>${item.email}</p></td>
                                <td class="list-view"><a href="/admin/seller/viewSeller?email=${item.email}" onclick="return false">${item.shopName}</a></td>
                                <td class="t_center list-view"><p>${item.firstName} ${item.lastName}</p></td>
                                <td class="t_center list-view" ><p>${item.rankName}</p></td>
                                <td class="t_center"><p>${item.shopStatus}</p></td>
                                <td class="t_center"><p>${item.regDate}</p></td>
                                
                            </tr>
                         </c:forEach>
                            
                        </tbody>
                    </table>
                    
                    </c:otherwise>
                </c:choose>
                                      
                    </form> 
                    
                    <div class="pager">
                        <c:if test="${fn:length(list) > 0}">
                        <%@ include file="/WEB-INF/views/common/incPaging2.jsp"%>
                        </c:if>
                        <form name="defaultSearchForm" id="defaultSearchForm" method="get">
                        <input type="hidden" name="searchRankId" value="${search.searchRankId}"> 
                        <input type="hidden" name="searchType" value="${search.searchType}">
                        <input type="hidden" name="searchWord" value="${search.searchWord}">
                        
                        <input type="hidden" name="pageSize" value="${search.pageSize}">
                        <input type="hidden" name="currentPage" value="${search.currentPage}">
                        <input type="hidden" name="sortOrder" value="${search.sortOrder}">
                        </form>
                    </div>
               </article>
           </div>
        </div>
           
        <!--// #### Content area ############### -->

    </div>
                
   <%// footer include %>
   <%@ include file="/WEB-INF/views/common/incAdmFooter.jsp"%>
    
</body>
</html>
