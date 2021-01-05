<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
 
<!-- 
**************************************************
화면코드 :UI_SYS_541_BAS 
기능설명 : 관리자 목록 조회
Author   Date      Description
 김윤홍     2020-01-06  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
<head>
<%// header include %>
<%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%>

<title>관리자 목록 조회</title>
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
			
			//관리자 목록 조회
			$("#btn_search").on('click', function() {
				search();
			});
			
			//등록버튼 클릭시
			$(document).on("click","#btn_write",function(){   	
		    	openPop("/admin/admin/popup/adminNewPop", "", "475" ,openf, funcf, closef);
		    });
			//팝업 보내기 버튼 클릭시 
			$(document).on("click","#btn_send_pop",function(){   	
				
				var $submitform = $('#insertAdmin');
				
				 // input type Validation & submit
		         if(!fnValidationCheckForInput($submitform)){
		        	 return;
		         }
		        
		        // select type Validation
		        if(!fnValidationCheckForSelectbox($submitform)){
		            return false;
		        }
		        
		        //사용여부 선택 체크
		        var adminUseYn = $("#adminUseYn").val();
		        if(adminUseYn == "K010000" || adminUseYn == "" || adminUseYn == null ){
		        	
		        	alert(getMessage("delivery.shipcompanyYn")+getMessage("alert.selectCheck"));
	            	return;
		        	
		        }
		        
		        //관리자 구분 선택 체크
		        var adminScopeId = $("#adminScopeId").val();
		        if(adminScopeId == "I010000" || adminScopeId == "" || adminScopeId == null ){
		        	
		        	alert(getMessage("board.part")+getMessage("alert.selectCheck"));
	            	return;
		        	
		        }
		        
				var param = $submitform.serializeObject();
				
				var confirm_1 = confirm(getMessage("alert.proc", ["admin.admin","button.insert"]));//관리자를 등록 하시겠습니까?
						
				if (confirm_1 == true) {
					
					var type = "post";
					var url = "/admin/admin/insertAdminListPop";

					// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
			        
					ajaxCall(type, url, param, sendFromInsert);
			
				} else if (confirm_1 == false) {
	
					return;
	
				}	
		    });
			
			$(document).on("click","#btn_cancle_pop",function(){   	
				closePop($(this))
		    });
			
			// check box 컨트롤
	        $(document).on("click","input[name='allCheck']",function(){
	        	allCheck("ckBox", this);
	        });
	
			//관리자 삭제
			$("#btn_delete").on('click', function() {
	
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
					var url = "/admin/admin/deleteAdminList";

					// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
			        
					ajaxCall(type, url, param, sendFromDelete);
			
				} else if (confirm_1 == false) {
	
					return;
	
				}
	
			});
	
		};
		/*팝업관련*/
		var openf = function(){
	        //alert("open");
	    }
	    
	    var funcf = function(){
	    	var $submitform = $('#insertAdmin');
	    	bindSelectAjax($submitform);
	    }
	    
	    var closef = function(){
	        //alert("close");
	    }
	    /*팝업관련*/
	    
		
		var sendFromInsert = function(data) {
			
			if (data.errCode == true) {
				alert(getMessage("alert.proc.end", ["button.insert"]));//등록이 완료되었습니다.
				location.reload();
			} else {
				alert(getMessage("alert.proc.err", ["button.insert"]));  // 등록 중 오류가 발생했습니다.
			}
		}
	    
	    var sendFromDelete = function(data) {
	
			if (data.errCode == true) {
				alert(getMessage("alert.proc.end", ["button.delete"]));//삭제가 완료되었습니다.
				location.reload();
			} else {
				alert(getMessage("alert.proc.err", ["button.delete"]));  // 삭제 중 오류가 발생했습니다.
			}
		}
	
		// 상세보기 
        $(document).on("click",".list-edit",function(){  
            var adminId = $(this).closest("tr").data("idx");
            location.href = '/admin/admin/showAdmin?adminId=' + adminId + '';
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
               		<h2><spring:message code="admin.adminmanage" text="관리자 관리" /></h2>
               		
               		<form name="searchform" id="searchform" method="get" action="/admin/admin/adminList">
                    <div class="module_group mt20">
                        <div class="action">
                            <select class="select-ajax" id="searchType" name="searchType" data-required="Y" data-label="구분"
                                        data-codetype="etc" data-code="${search.searchType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A180000" }'></select>
                            <div class="searchbox"> <!--인풋박스에 클릭시 active 클래스 추가-->
                                <input type="text" id="searchWord" name="searchWord" value="${search.searchWord}" placeholder="<spring:message code="common.searchWord" text="검색어" />">
                                <button type="button" id="btn_search" class="ic_search"></button>
                            </div>
                            <%-- 페이지 사이즈 --%>
                            <%@ include file="/WEB-INF/views/common/incPageSize.jsp"%>
                        </div>
                    </div>
                    </form>
                    <form name="submitform" id="submitform" method="get" action="/admin/admin/adminList">
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
                            <col class="cper1250">
                            <col class="cperauto">
                            <col class="cper1250">
                            <col class="cper1250">
                        </colgroup>
                        <thead>
                            <tr>
                                <th><input type="checkbox" id="manage_all" name="allCheck"><label for="manage_all"><span class="icon_ck"></span></label></th>
								<th><spring:message code="button.idx" text="번호" /></th>
								<th><spring:message code="button.part" text="구분" /></th>
								<th><spring:message code="button.email" text="이메일" /></th>
								<th><spring:message code="button.name" text="이름" /></th>
								<th><spring:message code="button.regDate" text="가입일" /></th>
                            </tr>
                        </thead>
                        <tbody>
									<c:forEach items="${list}" var="list" varStatus="status">
										<tr data-idx="${list.adminId}">
											<td class="t_center"><input type="checkbox" name="ckBox" value="${list.adminId}" id="ind02_${status.count}"><label for="ind02_${status.count}"><span class="icon_ck"></span></label></td>
											<td class="t_center list-edit"><p>${paging.countNum - status.index}</p></td>
											<td class="t_center list-edit">${list.adminDepart}</td>
											<td class="list-edit">${list.adminId}</td>
											<td class="t_center"><p>${list.adminName}</p></td>
											<td class="t_center list-edit">${list.adminRegDate}</td>
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
	                    <div class="foot_btn">
	                            <button type="button" class="btn_type2" id="btn_write"><span><spring:message code="button.insert" text="등록" /></span></button>
	                            <button type="button" class="btn_type6" id="btn_delete"><span><spring:message code="button.delete" text="삭제" /></span></button>
	                    </div>
               </article>
           </div>
        </div>
           
        <!--// #### Content area ############### -->

    </div>

	<%// footer include%>
	<%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
	
</body>
</html>