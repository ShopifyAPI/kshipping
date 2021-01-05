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
		test board
	</div>
    
    <%// footer include %>
    <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
	
</body>
</html>