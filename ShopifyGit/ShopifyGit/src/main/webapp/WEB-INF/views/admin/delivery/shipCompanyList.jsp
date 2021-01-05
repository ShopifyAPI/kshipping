<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
<!-- 
**************************************************
화면코드 :UI_SYS_311_BAS 
기능설명 : AMD 배송사 목록
Author   Date      Description
 김윤홍     2019-12-30  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 

    <title>배송사 리스트 조회</title>
    <script type="text/javascript">
    
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
		//등록버튼 클릭시
		$(document).on("click","#btn_write",function(){   	
	    	openPop("/admin/delivery/popup/newShipCompany", "", "510", "", popupSelectBind, "");
	    });
		
		//등록 > 팝업 > 배송비 변경
		$(document).on("change","select[name='shipId']",function(){   	
	    	getShipService($(this).val());
	    });

		
		//서비스 저장
		$(document).on("click","#btn_write_pop",function(){   	
			var $submitform = $('#shipCompany');
            
			// select type Validation
	        if(!fnValidationCheckForSelectbox($submitform)){
	            return false;
	        }
			
	        // input type Validation
            if(!fnValidationCheckForInput($submitform)){
                return false;
            }

			// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
	        var param = $submitform.serializeObject(); 
	        param.codeName = $("select[name='code'] option:selected").text();
	        	
			ajaxCall("post", "/admin/delivery/insertShipCompany", param, sendFromNew); 
	    });
		
		//서비스 수정
		$(document).on("click","#btn_write_pop_edit",function(){   	
			var $submitform = $('#shipCompany');
            
			// select type Validation
	        if(!fnValidationCheckForSelectbox($submitform)){
	            return false;
	        }
			
	        // input type Validation
            if(!fnValidationCheckForInput($submitform)){
                return false;
            }

			// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
	        var param = $submitform.serializeObject(); 
	        param.codeName = $("select[name='code'] option:selected").text();
	        param.shipId = $("select[name='shipId']").data("code"); 	
	        param.code = $("select[name='code']").data("code");
			ajaxCall("post", "/admin/delivery/editShipCompanyProc", param, sendFromEdit); 
	    });
				
		// check box 컨트롤
        $(document).on("click","input[name='allCheck']",function(){
        	allCheck("ckBox", this);
        });
		
		//배송사 삭제
		$("#btn_delete").on('click',function(){
			
			var $submitform = $('#submitform');
			
			if(!$("input[name='ckBox']").is(":checked")){
                alert( getMessage("alert.deleteChk") ); // 삭제할 대상을 선택해주세요
                return;
            }
			
			// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
	        var param = $submitform.serializeObject();

			if (confirm(getMessage("alert.deleteConfirm"))) { //선택 항목을 삭제 하시겠습니까?
				// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
				ajaxCall("post", "/admin/delivery/deleteShipCompany", param, sendFromDelete);
		
			} else if (confirm_1 == false) {
				return;
			}
			
		});

     	// 사용여부 수정 처리
        $(document).on("click",".list-edit",function(){
        	$(this).addClass("change");
        	editUseYnSend($(this));
        });
     	
        // 수정 팝업
        $(document).on("click",".list-view",function(){ 
        	var $obj = $(this).closest("tr")
        	var shipId = $obj.data("id");
            var code = $obj.data("code");

            var param = "shipId=" + shipId + "&code=" + code + "";
            openPop("/admin/delivery/popup/editShipCompany?" + param, "", "510", "", popupSelectBindEdit, "");
        });
	};
	
	// 사용여부 처리 
	var editUseYnSend = function (obj) {
		var shipId = obj.data("id");
		var code = obj.data("code");
		var useYn = obj.data("useyn");

		var param = {"shipId":shipId, "code":code, "useYn":useYn};
		ajaxCall("post", "/admin/delivery/editShipServiceUseYn", param, sendUseYn);
    }
	
	// 등록 팝업 select box 세팅
    var popupSelectBind = function (proc) {
        var $submitform = $('#shipCompany');
        bindSelectAjax($submitform);
    }
	
 	// 수정 팝업 select box 세팅
    var popupSelectBindEdit = function (proc) {
        var $submitform = $('#shipCompany');
        bindSelectAjax($submitform);

        getShipService($(".pop_body select[name='shipId']").data("code")); 
    }
	
    // 배송사 서비스 콜
    var getShipService = function(val) {
        // 서비스 초기화
        $("select[name='code'] option").remove();

        var param = {"codeEtc":val}
        ajaxCall("post", "/admin/delivery/popup/newShipService", param, makeService, false);   
    }

    // 배송사 서비스 생성
    var makeService = function(data){
    	var code = $(".pop_body select[name='code']").data("code");
    	
        if(data["delivery"].length > 0) {
            $.each(data["delivery"], function(index, item) {
                var option = "";
                
                if(index == 0){
                    option = $("<option value=''>== " + item.codeName + " ==</option>");
                } else {
                    if(code == item.codeEtc) {
                    	option = $("<option value='" + item.codeEtc + "' selected>" + item.codeName + "</option>");
                    } else {
                    	option = $("<option value='" + item.codeEtc + "'>" + item.codeName + "</option>");	
                    }
                }
                
                $("select[name='code']").append(option);
            });
        } else {
            console.log(data)
            alert(getMessage("alert.proc.err", ["button.insert"]));  // 등록 중 오류가 발생했습니다.
        }
    }
	

    // 사용여부 수정 완료
    var sendUseYn = function(data){
    	if(data.errCode == true) {
        	$(".change").data("useyn", data.useYn).find("a").text(data.useYn);
		} else {
			alert(getMessage("alert.proc.err", ["button.edit"]))
		}
        $(".change").removeClass("change");
    }
    
	// 배송사 저장
	var sendFromNew = function(data){
		if(data.errCode == true) {
			alert(getMessage("alert.proc.end", ["button.insert"]));//등록이 완료되었습니다.
		    location.href = "/admin/delivery/listShipCompany";
		} else {
			if(data.result < 0) {
				alert(data.errMsg); // 중복 오류
			} else {
				alert(getMessage("alert.proc.err", ["button.insert"]));  // 등록 중 오류가 발생했습니다.
			}
		}
	}
	
	var sendFromEdit = function(data){
		if(data.errCode == true) {
		    location.href = "/admin/delivery/listShipCompany";
		} else {
			alert(getMessage("alert.proc.err", ["button.eidt"]));  // 등록 중 오류가 발생했습니다.
		}
	}
	
	var sendFromDelete = function(data){
		if(data.errCode == true && data.result > 0) {
			alert(getMessage("alert.proc.end", ["button.delete"]));//삭제가 완료되었습니다.
		    location.href = "/admin/delivery/listShipCompany";
		} else {
			alert(getMessage("alert.proc.err", ["button.delete"]));  // 삭제 중 오류가 발생했습니다.
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
               		<h2><spring:message code="delivery.shipcompanylist" text="배송사 관리" /></h2>
                    <form name="submitform" id="submitform" method="post" action="/admin/delivery/listShipCompany">
                    	<table class="tbtype mt20">
                        <colgroup>
                            <col class="wp35">
                            <col class="cper2000">
                            <col class="cper0900">
                            <col class="cperauto">
                            <col class="cper1100">
                            <col class="cper0900">
                            <col class="cper0900">
                            <col class="cper0900">
                            <col class="cper0900">
                        </colgroup>
                        <thead>
                            <tr>
                                <th><input type="checkbox" id="b_i1" name="allCheck"><label for="b_i1"><span class="icon_ck"></span></label></th>
                                <th><spring:message code="delivery.shipcompanyName" text="배송사명" /></th>
                                <th><spring:message code="delivery.shipcompanyId" text="배송사 아이디" /></th>
								<th><spring:message code="delivery.shipcompanyCodeName" text="배송비명" /></th>
								<th><spring:message code="delivery.shipcompanyCodeId" text="배송비아이디" /></th>
								<th><spring:message code="delivery.shipcompanyCode" text="배송비코드" /></th>
								<th><spring:message code="delivery.shipcompany.mindate" text="최소배송일" /></th>
								<th><spring:message code="delivery.shipcompany.maxdate" text="최대배송일" /></th>
								<th><spring:message code="delivery.shipcompanyYn" text="사용여부" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(list) == 0}">
                                
                                <tr>
                                    <td colspan="9" class="t_center">
                                        <spring:message code="settings.nolist" text="등록된 정보가 없습니다." />
                                    </td>
                                </tr>
                                
                                </c:when>
                                <c:otherwise>
									<c:forEach items="${list}" var="item" varStatus="status">
										<tr data-id="${item.id}" data-code="${item.code}" data-useyn="${item.useYn}">
											<td class="t_center"><input type="checkbox" name="ckBox" value="${item.id}@${item.code}" data-code="${item.code}" data-id="${item.id}" id="ind02_${status.count}"><label for="ind02_${status.count}"><span class="icon_ck" ></span></label></td>
											<td class="t_center">${item.idName}</td>
											<td class="t_center">${item.id}</td>
											<td class="list-view"><a href="" onclick="return false;">${item.codeName}</a></td>
											<td class="t_center list-view"><a href="" onclick="return false;">${item.code}</a></td>
											<td class="t_center list-view"><a href="" onclick="return false;">${item.codeId}</a></td>
											<td class="t_center">${item.minDeliveryDate}</td>
											<td class="t_center">${item.maxDeliveryDate}</td>
											<td class="t_center list-edit" data-id="${item.id}" data-code="${item.code}" data-useyn="${item.useYn}"><a href="" onclick="return false;">${item.useYn}</a></td>
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
                        
                        <input type="hidden" name="pageSize" value="${search.pageSize}">
                        <input type="hidden" name="currentPage" value="">
                        </form>
                    </div>
                    
                    <div class="foot_btn">
                        <button type="button" class="btn_type2" id="btn_write"><span><spring:message code="button.insert" text="등록" /></span></button>
                        <button type="button" class="btn_type6" id="btn_delete" ><span><spring:message code="button.delete" text="삭제" /></span></button>
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