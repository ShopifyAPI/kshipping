<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_SYS_411_BAS
기능설명 : ADM FAQ 목록조회 화면
Author   Date      Description
 김윤홍     2019-12-23  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 

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
    	bindSelectAjax($submitform);
    };
    
    var bindEvent = function () {
    	
    	$(".list-edit").each(function(){
            var length = 65; //표시할 글자수 정하기
            $(this).each(function(){
                if( $(this).text().length >= length ){
    				console.log($(this).text().length)
                    $(this).text( $(this).text().substr(0,length)+'...'); 
                    //지정할 글자수 이후 표시할 텍스트
                }
            });
        });
    	
   	// 상세보기
    $(document).on("click",".list-edit",function(){  
        var idx = $(this).closest("tr").data("idx");
        var url = "/admin/board/admEditFaq?idx=" + idx;
        location.href = url;
    });
    	
   	// check box 컨트롤
    $(document).on("click","input[name='allCheck']",function(){
    	allCheck("ckBox", this);
    });
    	
    //FAQ 조회
	$("#btn_search").on('click',function(){
		search();
	});
		
	$(document).on("keyup","#btn_search",function(){
	    if(e.keyCode == 13){
	        search();
	    }
    });
    	
   	//FAQ 등록화면으로 이동
   	$("#btn_write").on('click',function(){
   		location.href = "/admin/board/admNewFaq";
   	});
   	
   	//FAQ 삭제
   	$("#btn_delete").on('click',function(){
   		
   		var $submitform = $('#submitform');
           
		var param = $submitform.serializeObject();
		
		var chk = param.ckBox;
		
		if (chk == null) {
			
			alert(getMessage("alert.deleteChk"));//삭제할 대상을 선택해주세요.
			return;
		}	    	

		var confirm_1 = confirm(getMessage("alert.deleteConfirm"));//선택 항목을 삭제 하시겠습니까?
				
		if (confirm_1 == true) {
			
			var type = "post";
			var url = "/admin/board/deleteBoard";

			// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
	        
			ajaxCall(type, url, param, sendFrom);
	
		} else if (confirm_1 == false) {

			return;

		}
   		
   	});
    	
    };
    
    var sendFrom = function(data){
		if(data.errCode == true) {
		    alert(getMessage("alert.proc.end", ["button.delete"]));//삭제가 완료되었습니다.
		    //목록화면으로 이동	 		
		    location.href = "/admin/board/selectFaq";
		} else {
			alert(getMessage("alert.proc.err", ["button.delete"]));  // 삭제 중 오류가 발생했습니다.
		}
	}
	
    //ROW 체크박스 선택시
	var chkClick = function(idx){
		
		if($("#"+idx).is(':checked') ){
	        $("#"+idx).prop("checked", false);
        }else{
	        $("#"+idx).prop("checked", true);
	    }
	}
    
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
    <div class="wrap">
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incAdmGnb.jsp"%> 

        <!-- #### Content area ############### -->

        <div class="contents">
           <div class="cont_body">
               <article>
                    <h2><spring:message code="board.faqmanage" text="FAQ 관리"/></h2>
					
                    <div class="module_group mt20">
                    <form name="searchform" id="searchform" method="get">
                    	<div class="action">
	                    		<%-- 페이지 사이즈 --%>
                                <%@ include file="/WEB-INF/views/common/incPageSize.jsp"%>
                    	</div>
                    	<div class="btn_group">
                    		<input type="hidden" id="division" name="division" value="D010002">
	                   		<select class="select-ajax" id="searchType" name="searchType"
	                            data-codetype="" data-code="${search.searchType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "D020000" }'></select>
		                    <div class="searchbox"> <!--인풋박스에 클릭시 active 클래스 추가-->
	                            <button type="button" class="ic_search" id="btn_search"></button>
	                            <input type="text" id="searchWord" value="${search.searchWord}" name="searchWord" placeholder="<spring:message code="common.searchWord" text="검색어" />">
	                        </div>
                    	</div>
                    	</form>
                    </div>
				    
				    <form name="submitform" id="submitform" method="get" action="/admin/board/selectFaq">
				    <c:choose>
						<c:when test="${fn:length(list) == 0}">
							<tr>
								<div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
							</tr>
						</c:when>
					<c:otherwise>
                    <table class="tbtype">
							<colgroup>
								<col class="wp50">
	                            <col class="wp50">
	                            <col class="cper1100">
	                            <col class="cperauto">
	                            <col class="cperauto">
							</colgroup>
							<thead>
								<tr>
									<th><input type="checkbox" name="allCheck" id="ind01"><label for="ind01"><span class="icon_ck"></span></label></th>
									<th><spring:message code="button.idx" text="번호" /></th>
									<th><spring:message code="button.part" text="구분" /></th>
									<th><spring:message code="button.questions" text="질문" /></th>
									<th><spring:message code="button.answer" text="답변" /></th>
								</tr>
							</thead>
							<tbody>
										<c:forEach items="${list}" var="list" varStatus="status">
											<tr data-idx="${list.idx}">
												<td class="t_center"><input type="checkbox" id="${list.idx}" name="ckBox" value="${list.idx}"><label for="ind02"><span onclick="chkClick('${list.idx}')" class="icon_ck"></span></label></td>
                                                <td class="t_center list-edit"><p>${paging.countNum - status.index}</p></td>
												<td class="t_center list-edit">${list.partName}</td>
												<td class="list-edit content" >${list.content}</td>
												<td class="list-edit content" >${list.answer}</td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						</form>
						
						<div class="foot_btn">
                        	<button type="button" class="btn_type6" id="btn_delete"><span><spring:message code="button.delete" text="삭제" /></span></button>
  							<button type="button" class="btn_type2" id="btn_write"><span><spring:message code="button.write" text="작성" /></span></button>
                    	</div>
                    
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
               </article>
           </div>
        </div>
           
        <!--// #### Content area ############### -->

    </div>
                
   <%// footer include %>
   <%@ include file="/WEB-INF/views/common/incAdmFooter.jsp"%>
    
</body>
</html>
