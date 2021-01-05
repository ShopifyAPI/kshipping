<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_SYS_511_BAS 
기능설명 : AMD 코드관리 리스트 조회
Author   Date      Description
 김윤홍     2020-01-31  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 

    <title>ADM 코드관리 리스트 조회</title>
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
    	$("#userlang").val(_USER_lang);
    	
    	
	};

	var bindEvent = function() {
		
		// 리스트 클릭시 상세보기,수정 팝업 open
        $(document).on("click",".list-view",function(){  
            var codeId = $(this).closest("tr").data("email");
            
            param = encodeURI("codeId=" + codeId );
              
            openPop("/admin/popup/adminCodeEditPop?"+param, "", "450px", openfEdit, popupSelectBindEdit, closefEdit);
        });
		
        //코드관리 수정버튼 클릭시
		$(document).on("click","#btn_edit_pop",function(){
			
			var $submitform = $('#editAdminCode');
	           
            // input type Validation & submit
            if(!fnValidationCheckForInput($submitform)){
            	return;
            }
            
            var codeUseYn = $("#codeUseYn").val();
            if(codeUseYn == "" || codeUseYn == null || codeUseYn == "K010000"){
            	alert(getMessage("code.useYn")+getMessage("alert.selectCheck"));
            	return;
            }
            
            var type = "post";
            var url = "/admin/code/editAdminCodeGroup";

            // 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
            var param = $submitform.serializeObject(); 
			
            ajaxCall(type, url, param, sendFromEdit);
		});
		
         //코드관리 신규팝업 OPEN FUNCTION
		 var popupSelectBindNew = function(){
		
			 var $submitform = $('#insertAdminCode');
			 
			 bindSelectAjax($submitform);
			 //대분류 SELECT BOX DATA SET			
			 selectBoxDataSet();
			 $("#codeGroup2").hide();//대분류
			 
		 };
		
		 //코드관리 수정팝업 OPEN FUNCTION
		 var popupSelectBindEdit = function(){
		 	 var codeId = $("#codeId").val();
			 var codeGroup = $("#codeGroup").val(); 
			
			 if(codeId==codeGroup){
		 		 $(".mainCategory").hide();
			 }
			 
		 	 var $submitform = $('#editAdminCode');
			 bindSelectAjax($submitform);
			 
			//수정불가 항목(대분류 , 코드)
			$("#codeId").attr("readonly",true);
			$("#codeGroup").attr("readonly",true);
			
			
			
		 };
		
		 //코드관리 등록 팝업 OPEN
		 $(document).on("click","#btn_write",function(){
		 	 openPop("/admin/popup/adminCodeNewPop", "", "450px", openf, popupSelectBindNew, closef);
		 	 
		 });
		
		 //코드관리 등록
		 $(document).on("click","#btn_write_pop",function(){
			
			 var $submitform = $('#insertAdminCode');
			 var gbn = $("#codeGbn").val();
			 
			 //대분류 등록시 codeId 필수값 벨리데이션 제외
			 if(gbn == "codeGroup"){
				$("#codeId").remove();
			 }
			 
			 // input type Validation & submit
	         if(!fnValidationCheckForInput($submitform)){
	        	 return;
	         }
	        
	        // select type Validation
	        if(!fnValidationCheckForSelectbox($submitform)){
	            return false;
	        }
	        
	        //selectBox Validation
	        var codeGroup = $("#codeGroup").val();
	          //var mainCategor?y = getMessage("code.MainCategory");
	  //      consol.log("4444444444  maincategory : "+ mainCategory);
            if(codeGroup == "J010000" || codeGroup == null || codeGroup == ""){
               
            	alert(getMessage("code.MainCategory")+getMessage("alert.selectCheck"));
            	return;
            }
            
	        
	        var type = "post";
	        var url = "";
	        //구분값에 따라 CodeGroup(대분류) , CodeId(중분류) 등록
	        if(gbn == "codeGroup"){
	        	url = "/admin/code/insertAdminCodeGroup";
	        }else{
	        	url = "/admin/code/insertAdminCodeId";
	        }

	        // 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
	        var param = $submitform.serializeObject();
	        
	        ajaxCall(type, url, param, sendFromNew);
		});
		
		// 코드관리 삭제
        $(document).on("click","#btn_delete",function(){
        	if(!$("input[name='ckBox']").is(":checked")){
                alert( getMessage("alert.deleteChk") ); // 삭제할 대상을 선택해주세요
                return;
            }
            
            if(confirm(getMessage("alert.deleteConfirm"))){  // 선택항목을 삭제 하시겠습니까?
                var $submitform = $('#listform');
                var type = "post";
                var url = "/admin/code/deleteAdminCode";
                
                var param = $submitform.serializeObject();
                
                ajaxCall(type, url, param, sendFrom);
            }
        });
		
		// check box 컨트롤
        $(document).on("click","input[name='allCheck']",function(){
        	allCheck("ckBox", this);
        });
		
		/*팝업관련이벤트*/
		var openf = function(){}
	    var closef = function(){}
		var openfEdit = function(){}
	    var closefEdit = function(){}
	    /*팝업관련이벤트*/
	};
	
	//검색
    $(document).on("click",".ic_search",function(){
        search();
    });
    
    $(document).on("keyup",".ic_search",function(){
        if(e.keyCode == 13){
            search();
        }
    });
	
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
	//삭제완료시
	var sendFrom = function(data){
		if(data.errCode == true) {
			alert(getMessage("alert.proc.end", ["button.delete"]));//삭제가 완료되었습니다.
		  //목록화면으로 이동
			location.href = "";
		} else {
			alert(getMessage("alert.proc.err", ["button.delete"]));  // 삭제 중 오류가 발생했습니다.
			location.href = "";
		}
	}
	
	// 전체 검색
    var search = function(){
        searchform.submit();
    }

	//ROW 체크박스 선택시
	var chkClick = function(idx){
		if($("#"+idx).is(':checked') ){
	        $("#"+idx).prop("checked", false);
        }else{
	        $("#"+idx).prop("checked", true);
	    }
	}
	
	//코드 등록완료 시
	var sendFromNew = function(data){
		
		var codeGroup = data.codeGroup;
		var searchType = "MainCategory";
		$("#searchWord").val(codeGroup);
		$("#searchType").val(searchType);
		
		if(data.errCode == true) {
			alert(getMessage("alert.proc.end", ["button.insert"]));//등록이 완료되었습니다.
			//등록한 codeGroup 재조회(searchType은 대분류로 설정)	 		
		    search();
		} else {
			alert(getMessage("alert.proc.err", ["button.insert"]));  // 등록 중 오류가 발생했습니다.
			
		}
	}
	
	//코드 수정완료시
	var sendFromEdit = function(data){
		
		var codeGroup = data.codeGroup;
		var searchType = "MainCategory";
		$("#searchWord").val(codeGroup);
		$("#searchType").val(searchType);
		
		if(data.errCode == true) {
			alert(getMessage("alert.proc.end", ["button.edit"]));//등록이 완료되었습니다.
		    //수정한 codeGroup 재조회(searchType은 대분류로 설정)	 		
		    search();
		} else {
			alert(getMessage("alert.proc.err", ["button.edit"]));  // 등록 중 오류가 발생했습니다.
			
		}
	}
	
	//등록팝업 대분류 체크박스 이벤트
	var categoryChkClick = function(){
		
		if($("#cate_state").is(':checked',false) ){
			$("#codeGbn").val("codeGroup");
			$("input[name='ckBoxPop']").prop('checked',true);
			
			//대분류 등록시(대분류 input box로 변경해야함, 중분류 input box 비활성화)
			var str = '<input type="text" id="codeGroup" name="codeGroup" data-label="${MainCategory}" data-required="Y"/>'
			
			$(".codeId").hide();
            jQuery('#MainCategorySelect').css("display", "none");
			$("#codeGroup").remove();
            jQuery('#MainCategoryInsert').css("display", "block");
			$("#MainCategoryInsert").append(str); //대분류 input box 태그 추가

			$("#codeGroup1").hide();//대분류 
			$("#codeGroup2").show();//코드
			

			//컴포넌트 값 초기화
			$("#codeGroup").val("");
			$("#codeId").val("");
			$("#codeKname").val("");
			$("#codeEname").val("");
			$("#codeEtc").val("");
			$("#codeSeq").val("");
			$("#codeDiscript").val("");
			$("#codeUseYn").val("Y");

		}else{
			$("#codeGbn").val("codeId");
			$("input[name='ckBoxPop']").prop('checked',false);
			//중분류 등록시(대분류 select box로 변경, 중분류 input box 활성화)
			var str = '<select id="codeGroup" name="codeGroup" data-label="${MainCategory}" data-required="Y"></select>'
			$(".codeId").show(); //중분류 input box 영역 보임
            jQuery('#MainCategoryInsert').css("display", "none");
			$("#codeGroup").remove();//input box 태그 제거
            jQuery('#MainCategorySelect').css("display", "block");
			$("#MainCategorySelect").append(str);//대분류 select box 태그 추가
			
			$("#codeGroup2").hide();//대분류 
			$("#codeGroup1").show();//코드
			
			//컴포넌트 값 초기화
			$("#codeGroup").val("");
			$("#codeId").val("");
			$("#codeKname").val("");
			$("#codeEname").val("");
			$("#codeEtc").val("");
			$("#codeSeq").val("");
			$("#codeDiscript").val("");
			$("#codeUseYn").val("Y");
			
			//코드그룹 SELECT BOX DATA SET
			selectBoxDataSet();
			
		}

	};
	
	//대분류 selectBox Data Set
    var selectBoxDataSet = function(){
    	
    	var type = "post";
         var url = "/common/listCodeGroup";
         var param = "";
         
 		$.ajax({
 	        type       : "POST"
 	        ,async     : true  // true: 비동기, false: 동기
 	        ,url       : url
 	        ,cache     : false
 	        ,data      : JSON.stringify(param) 
 	        ,contentType: "application/json"
 	        ,dataType  : "json"
 	        ,beforeSend : function(xhr) {
 	        	
 	        	xhr.setRequestHeader(header, token);
 	        }
 	        ,success   : function(data) {
 	        	
 	        	$.each(data, function(index, item) {
 	    			var option = "";
 	    			var MainCategory = getMessage("code.MainCategory");
 	    			
 	    			if(index == 0){
 	    				//option = $("<option value='"+MainCategory+"'>"+MainCategory+"</option>");
 	    			//	$("#codeGroup").append(option);
 	    				//option = $("<option value=''>== " + "대분류" + " ==</option>");
						//$select.append(option);
						
 	    			//	option = $("<option value='"+item.codeGroup+"'>"+item.codeName+"</option>");
 	    				
 	    			}else{
 	    			//	option = $("<option value='"+item.codeGroup+"'>"+item.codeName+"</option>");
 	    			}
 	    			$("#codeGroup").append(option);
 	    		});
 				
 	        }
 	        ,error     : function(error) {
 	        	returnData = "error";
 	        	console.log(error);
 	        }
 	        ,complete: function (jqXHR, textStatus, errorThrown) {
 			
 			}
 		});
    	
    }
	
	</script>
</head>
<body>
    <div class="wrap">
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incAdmGnb.jsp"%> 
        <div class="contents">
           <div class="cont_body">
               <article>
                    <h2><spring:message code="code.codeManage" text="코드 관리" /></h2>
                    <div class="module_group mt20">
                    	<form name="searchform" id="searchform" method="get" action="/admin/code/selectCode">
                    	<input type="hidden" id="userlang" name="userlang" />
	                    <div class="action">
	                    	<%-- 페이지 사이즈 --%>
	                    	<%@ include file="/WEB-INF/views/common/incPageSize.jsp"%>
	                    </div>
	                    <div class="btn_group">	
	                    <!-- 사용여부 -->    
	                        <select class="select-ajax" name="searchUseYn"  id="searchUseYn" 
	                                data-codetype="etc" data-code="${search.searchUseYn}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "K010000" }'></select>
	                     
	                     <!-- 대분류 코드  -->
	                        <select class="select-ajax" name="searchCodeGroup" id="searchCodeGroup"
	                        		data-codetype="" data-code="${search.searchCodeGroup }" data-url="/common/listCodeGroup" data-param=></select>
	                     
	                     <!-- 검색조건 -->   			                        
	                        <select class="select-ajax" name="searchType"  id="searchType" 
	                                data-codetype="etc" data-code="${search.searchType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A160000" }'></select>
	                        
	                     <!-- 검색어 입력박스 -->   
	                        <div class="searchbox">
		                        <input type="text" name="searchWord" id="searchWord" value="${search.searchWord}" placeholder="<spring:message code="common.searchWord" text="검색어" />">
		                        <button type="button" class="ic_search"></button>
	                    	</div>
	                    </div>
	                    </form>
                    </div>
                    <form name="listform" id="listform" method="get">
                        <c:choose>
                    		<c:when test="${fn:length(list) == 0}">
                    			<tr>
                    				<div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
                    			</tr>
                    		</c:when>
                   		<c:otherwise>
                        <table class="tbtype">
                            <colgroup>
                                <col class="wp35">
                                <col class="wp50">
                                <col class="wp95"> 
                                <col class="wp50">
                                <col class="wp95">
                                <col class="wp120">
                                <col class="wp120">
                                <col class="wp95">
                                <col class="cperauto">
                                <col class="wp50">
                                <col class="wp100">                           
                                
                              <!--  
                                <col class="wp35">
                                <col class="wp50">
                                <col class="wp95">
                                <col class="wp95">
                                <col class="wp95">
                                <col class="wp120">
                                <col class="wp120">
                                <col class="cperauto">
                                <col class="wp50">
                                <col class="wp50">
                                <col class="wp100">
                              --> 
                            </colgroup>
                            <thead>
                                <tr class="multi">
                                    <th rowspan="2"><input type="checkbox" name="allCheck" id="ind01"><label for="ind01"><span class="icon_ck"></span></label></th>
                                    <th rowspan="2"><spring:message code="board.idx" text="번호" /></th rowspan="2">
                                    <th rowspan="2"><spring:message code="code.MainCategory" text="대분류" /></th rowspan="2">
                                    <th rowspan="2"><spring:message code="code.seq" text="SEQ" /></th>
                                    <th rowspan="2"><spring:message code="code.Category" text="코드" /></th rowspan="2">
                                    <th colspan="2"><spring:message code="code.CodeName" text="코드명" /></th>
                                    <th rowspan="2"><spring:message code="code.EtcCode" text="기타코드" /></th rowspan="2">
                                    <th rowspan="2"><spring:message code="code.discript" text="설명" /></th>
                                    <th rowspan="2"><spring:message code="code.use" text="사용" /><br><spring:message code="code.yn" text="여부" /></th>
                                    <th rowspan="2"><spring:message code="board.regDate" text="등록일" /></t>
    
                                   <!-- 
                                    <th rowspan="2"><spring:message code="code.Category" text="코드" /></th rowspan="2">
                                    <th rowspan="2"><spring:message code="code.EtcCode" text="기타코드" /></th rowspan="2">
                                    <th rowspan="2"><spring:message code="code.MainCategory" text="대분류" /></th rowspan="2">
                                    <th colspan="2"><spring:message code="code.CodeName" text="코드명" /></th>
                                    <th rowspan="2"><spring:message code="code.discript" text="설명" /></th>
                                    <th rowspan="2"><spring:message code="code.seq" text="순서" /></th>
                                    <th rowspan="2"><spring:message code="code.use" text="사용" /><br><spring:message code="code.yn" text="여부" /></th>
                                    <th rowspan="2"><spring:message code="board.regDate" text="등록일" /></t>
                                 -->
  
                                </tr>
                                <tr class="multi"> 
                                    <th>한글</th>
                                    <th>영어</th>                             
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${list}" var="item" varStatus="status">
                               <tr data-email="${item.codeId}">
	                                <td class="t_center"><input type="checkbox" id="${item.codeId}" name="ckBox" value="${item.codeId}"><label for="ind02"><span onclick="chkClick('${item.codeId}')" class="icon_ck"></span></label></td>
	                                <td class="t_center"><p>${paging.countNum - status.index}</p></td>
	                                <td class="t_center list-view"><p>${item.codeGroup}</p></td>
	                                <td class="t_center list-view"><p>${item.codeSeq}</p></td>
	                                <td class="t_center list-view"><p>${item.codeId}</p></td>
	                                <td class="list-view"><p>${item.codeKname}</p></td>
	                                <td class="list-view"><p>${item.codeEname}</td>
	                                <td class="t_center list-view"><p>${item.codeEtc}</p></td>
	                                <td class="list-view"><p>${item.codeDiscript}</p></td>
	                                <td class="t_center list-view"><p>${item.codeUseYn}</p></td>
	                                <td class="t_center list-view"><p>${item.codeRegDate}</p></td>
                               
                               <!--  
                                <tr data-email="${item.codeId}">
	                                <td class="t_center"><input type="checkbox" id="${item.codeId}" name="ckBox" value="${item.codeId}"><label for="ind02"><span onclick="chkClick('${item.codeId}')" class="icon_ck"></span></label></td>
	                                <td class="t_center"><p>${paging.countNum - status.index}</p></td>
	                                <td class="t_center list-view"><p>${item.codeId}</p></td>
	                                <td class="t_center list-view"><p>${item.codeEtc}</p></td>
	                                <td class="t_center list-view"><p>${item.codeGroup}</p></td>
									<td class="list-view"><p>${item.codeKname}</p></td>
	                                <td class="list-view"><p>${item.codeEname}</td>
	                                <td class="list-view"><p>${item.codeDiscript}</p></td>
	                                <td class="t_center list-view"><p>${item.codeSeq}</p></td>
	                                <td class="t_center list-view"><p>${item.codeUseYn}</p></td>
	                                <td class="t_center list-view"><p>${item.codeRegDate}</p></td>
                            	-->
                            	
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
                        <input type="hidden" name="searchType" value="${search.searchType}">
                        
                        <input type="hidden" name="searchUseYn" value="${search.searchUseYn}">
                        <input type="hidden" name="searchCodeGroup" value="${search.searchCodeGroup}">
                        <input type="hidden" name="searchWord" value="${search.searchWord}">
                        <input type="hidden" name="pageSize" value="${search.pageSize}">
                        <input type="hidden" name="currentPage" value="${search.currentPage}">
                        </form>
                    </div>
                    <div class="foot_btn">
                        <button type="button" class="btn_type2" id="btn_write" ><spring:message code="button.insert" text="등록" /></button>
                        <button type="button" class="btn_type6" id="btn_delete"><spring:message code="button.delete" text="삭제" /></button>
                        
                    </div>
               </article>
           </div>
        </div>
    </div>
   <%// footer include %>
   <%@ include file="/WEB-INF/views/common/incAdmFooter.jsp"%>
</body>
</html>
