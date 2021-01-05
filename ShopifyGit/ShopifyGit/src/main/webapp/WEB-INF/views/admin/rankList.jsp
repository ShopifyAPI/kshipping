<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_SYS_131_BAS 
기능설명 : AMD 할인율 목록
Author   Date      Description
 김윤홍     2019-12-30  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 

    <title>할인율 리스트 조회</title>
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
	
	// 등록/수정 팝업 select box 세팅
    var popupSelectBind = function () {
    	var $submitform = $('#insertRank');
        bindSelectAjax($submitform);
    }
	
	var bindEvent = function() {
		
		// 수정 팝업 오픈 
        $(document).on("click",".list-edit",function(){  
			
        	var idx = $(this).closest("tr").data("idx");
	    	openPop("/admin/rank/popup/adminEditPop?idx="+idx+"","" ,"465", openfEdit, funcfEdit, closefEdit);
	    	
        });
		
		
		//팝업 수정버튼 클릭시
		$(document).on("click","#btn_edit_pop",function(){ 	
	    	
			var confirm_1 = confirm(getMessage("alert.proc", ["common.target","button.edit"]));//선택 항목을 수정 하시겠습니까?
	        
			if (confirm_1 == true) {
				var $submitform = $('#insertRank');
		           
	            // input type Validation & submit
	            if(!fnValidationCheckForInput($submitform)){
	            	return;
	            }
	            
	            var discount = $("#discount").val();
	            if(discount == "J010000" || discount == "" || discount == null){
	            	alert(getMessage("rank.discount")+getMessage("alert.selectCheck"));
	            	return;
	            }
	            
	            var rankId = $("#rankId").val();
	            if(rankId == "E010000" || rankId == "" || rankId == null){
	            	alert(getMessage("rank.rank")+getMessage("alert.selectCheck"));
	            	return;
	            }
	            
	            var type = "post";
	            var url = "/admin/rank/popup/editRank";

	            // 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
	            var param = $submitform.serializeObject(); 
	            
	            ajaxCall(type, url, param, sendFromEdit);	

		
			} else if (confirm_1 == false) {
				
				return;

			}
			
	    });
		
		//등록버튼 클릭시 팝업 오픈
		$(document).on("click","#btn_write",function(){   	
	    	openPop("/admin/rank/popup/newRankPop", "", "465", openf, popupSelectBind, closef);
	    });
		
		//팝업 등록버튼 클릭시
		$(document).on("click","#btn_write_pop",function(){
	    	
			var confirm_1 = confirm(getMessage("alert.proc", ["rank.discount","button.insert"]));//선택 항목을 등록 하시겠습니까?
					
			if (confirm_1 == true) {
				
				var $submitform = $('#insertRank');
		           
	            // input type Validation & submit
	            if(!fnValidationCheckForInput($submitform)){
	            	return;
	            }
	            
	            if(!fnValidationCheckForSelectbox($submitform)){
	            	return;
	            }
	            
	            var discount = $("#discount").val();
	            
	            if(discount == "J010000" || discount == "" || discount == null){
	            	alert(getMessage("rank.discount")+getMessage("alert.selectCheck"));
	            	return;
	            }
	            
	            var rankId = $("#rankId").val();
	            if(rankId == "E010000" || rankId == "" || rankId == null){
	            	alert(getMessage("rank.rank")+getMessage("alert.selectCheck"));
	            	return;
	            }
	            
	            var type = "post";
	            var url = "/admin/rank/insertRank";

	            // 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
	            var param = $submitform.serializeObject(); 
	            
	            ajaxCall(type, url, param, sendFromNew);
				
			}else if (confirm_1 == false) {
				
				return;

			}
			
			
            
	    });
		
		//할인율 삭제
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
				var url = "/admin/rank/deleteRank";

				// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
				ajaxCall(type, url, param, sendFromDelete);
		
			} else if (confirm_1 == false) {

				return;

			}
	});
		
		// check box 컨트롤
        $(document).on("click","input[name='allCheck']",function(){
        	allCheck("ckBox", this);
        });
		
	};
	
	/*팝업관련*/
	var openf = function(){
        //alert("open");
    }
    
    var closef = function(){
        //alert("close");
    }
    
    /*팝업관련*/
	var openfEdit = function(){
		
    }
    
    var funcfEdit = function(){
    	
    	var $submitform = $('#insertRank');
        bindSelectAjax($submitform);
    
        $("#rankId").attr("disabled",true);
   		$("#startDate").attr("disabled",true);
    }
    
    var closefEdit = function(){
        //alert("close");
    }
    /*팝업관련*/ 
    
	var sendFromNew = function(data){
		if(data.errCode == true) {
			alert(getMessage("alert.proc.end", ["button.insert"]));//등록이 완료되었습니다.
		    //목록화면으로 이동	 		
		    location.href = "/admin/rank/rankList";
		} else {
// 			alert(getMessage("alert.proc.err", ["button.insert"]));  // 등록 중 오류가 발생했습니다.
			alert(getMessage("rank.duplicateErr"));
		}
	}
    
	var sendFromEdit = function(data){
		if(data.errCode == true) {
			alert(getMessage("alert.proc.end", ["button.edit"]));//수정이 완료되었습니다.
		    //목록화면으로 이동	 		
		    location.href = "/admin/rank/rankList";
		} else {
// 			alert(getMessage("alert.proc.err", ["button.edit"]));  // 수정 중 오류가 발생했습니다.
			alert(getMessage("rank.duplicateErr"));
		}
	}
    
	var sendFromDelete = function(data){
    	
		if(data.errCode == true) {
			alert(getMessage("alert.proc.end", ["button.delete"]));//삭제가 완료되었습니다.
		    //목록화면으로 이동	 		
		    location.href = "/admin/rank/rankList";
		} else {
			alert(getMessage("alert.proc.err", ["button.delete"]));  // 삭제 중 오류가 발생했습니다.
		}
	}
	
	//ROW 체크박스 선택시
	var chkClick = function(rankCode){
		
		if($("#"+rankCode).is(':checked') ){
	        $("#"+rankCode).prop("checked", false);
        }else{
	        $("#"+rankCode).prop("checked", true);
	    }
		
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
               		<form name="submitform" id="submitform" method="get">
           			<h2><spring:message code="rank.ranklist" text="할인율 관리" /></h2>
           			<c:choose>
						<c:when test="${fn:length(list) == 0}">
							<tr>
								<div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
							</tr>
						</c:when>
					<c:otherwise>
                    <table class="tbtype mt20">
                        <colgroup>
                            <col class="wp50">
                            <col class="cperauto">
                            <col class="cperauto">
                            <col class="cperauto">
                        </colgroup>
                        <thead>
                            <tr>
                                <th><input type="checkbox" name="allCheck" id="ind01"><label for="ind01"><span class="icon_ck"></span></label></th>
								<th><spring:message code="rank.rank" text="등급" /></th>
								<th><spring:message code="rank.discount" text="할인율" /></th>
								<th><spring:message code="rank.effectiveDate" text="적용시작일" /></th>
                            </tr>
                        </thead>
                        <tbody>
                                    <%-- 
                                       // 정책상 수정/삭제 제외 
                                       // 수정 기능 할성화 는 list-edit-none => list-edit로 변경 해주면 됩니다.                
                                    --%>
									<c:forEach items="${list}" var="item" varStatus="status">
										<tr data-idx="${item.idx}">
											<td class="t_center"><input type="checkbox" id="ind02_${status.count}" name="ckBox" value="${item.idx}"><label for="ind02_${status.count}"><span class="icon_ck"></span></label></td>
											<td class="t_center list-edit-none">${item.rankId}</td>
											<td class="t_center list-edit-none">${item.discountName}</td>
											<td class="t_center list-edit-none">${item.startDate}</td>
										</tr>
									</c:forEach>
                        </tbody>
                    </table>
                    
                    </c:otherwise>
                </c:choose>
                   
                    <div class="foot_btn">
                        <button type="button" class="btn_type2" id="btn_write"><span><spring:message code="button.insert" text="등록" /></span></button>
                        <%-- // 정책상 수정/삭제 제외 
                        <button type="button" class="btn_type6" id="btn_delete"><span><spring:message code="button.delete" text="삭제" /></span></button> 
                        --%>
                    </div>
               		</form>
               		
               </article>
           </div>
        </div>
           
        <!--// #### Content area ############### -->

    </div>
		
   <%// footer include %>
   <%@ include file="/WEB-INF/views/common/incAdmFooter.jsp"%>
</body>
</html>