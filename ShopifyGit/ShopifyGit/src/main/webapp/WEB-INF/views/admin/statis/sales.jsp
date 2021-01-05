<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
 
<!-- 
**************************************************
화면코드 :UI_SYS_622_BAS 
기능설명 : 관리자 목록 조회
           내려 받기 기능은 매입작업 시 반영 
Author   Date      Description
 김윤홍     2020-01-06  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
<head>
<%// header include %>
<%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%>

<title>관리자 매출 원장</title>
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
        	var $submitform = $('#submitform');
            bindSelectAjax($submitform);
            
            var shipCompany = $("select[name='searchCompany']").data("code");
            setShipService(shipCompany);
        };
    
        var bindEvent = function() {
        	// 검색 > 셀러 input box clear
            $(document).on("click","#btn_clear",function(){
                $(".searchbox input[name='searchSeller']").val('');
            });
        	// 검색 > 셀러 검색 팝업 호출
            $(document).on("click",".ic_search",function(){
                searchPopSeller();
            });
        	
            // 검색 > 셀러 검색 팝업 호출
            $(document).on("keyup",".searchbox input[name='searchSeller']",function(e){
                if(e.keyCode == 13){
                	searchPopSeller();
                }
            });
            
        	//검색 > 배송사 select Box 변경시 배송서비스 select Box data set 
            $("#searchCompany").on("change",function(){
                //배송사 코드
                onChange = true;
                var shipCompany = this.value;
                setShipService(shipCompany);    
            });
        	
        	// 검색 클릭
            $(document).on("click","#btnSerch",function(){
            	search();
            });
        	
            // 셀러검색 셀러 선택 
            $(document).on("click",".popSeller",function(){
            	var email = $(this).data("email");
                $(".searchbox input[name='searchSeller']").val(email);
                $(this).closest(".popup").find(".pop_head .btn_close").trigger("click");
            });
            
            // 셀러검색 팝업 > 셀러 검색
            $(document).on("click",".pop_body .ic_search",function(){
            	searchSellerProc("ckick");
            });
            
            //다운로드
            $(document).on("click","#down",function(){   
                down();
            });
        };
        
        var searchPopSeller = function() {
        	var searchWord = $(".searchbox input[name='searchSeller']").val();
        	openPop("/admin/statis/sellerSearch?searchWord=" + searchWord, "", 700, "", searchSellerProc, "");
        }
        
        // 셀러 검색 
        var searchSellerProc = function(mode){
        	var searchWord = $(".pop_body input[name='searchWord']").val();
        	var searchType = $(".pop_body input[name='searchType']").val();
        	
        	if(mode == "ckick" && searchWord == ""){
        		alert("검색어를 입력해 주세요.");
        		alert(getMessage("common.searchWord") + getMessage("alert.inputCheck"));  //오류가 발생했습니다.
        		return;
        	} 
        	
        	// 샐러 검색
        	if(searchWord != "") {
        		var param = {"searchWord":searchWord, "searchType":searchType};
        		ajaxCall("post", "/admin/statis/sellerSearchProc", param, sendsellerBinding, false);
        	}
        }
        
        // 검색 리스트 바인딩
        var sendsellerBinding = function(data) {
        	var tempHtml = "";
        	var html = "";
        	
        	if (data.errCode == true) {
        		$(".pop_body #popSearchArea tbody").html(""); // 초기화
        		
                if(data.list.length <= 0) {
                	tempHtml = "<tr>" + $(".pop_body #popSearchArea #sellerListNone").html() + "</tr>";
                	$(".pop_body #popSearchArea tbody").html(tempHtml);
                } else {
                	tempHtml = $(".pop_body #popSearchArea #sellerList").html();
                	
                	$.each(data.list, function (i, item) {        
                		name = item.firstName + " " + item.lastName;
                		html = tempHtml.replace(/{email}/gi, item.email);
                		html = html.replace(/{name}/gi, name);
                		html = html.replace(/{company}/gi, item.company);
                		
                		$(".pop_body #popSearchArea tbody").append("<tr>" + html + "</tr>");
                    });
                }
            } else {
                alert(getMessage("alert.proc.err", ["button.search"]));  //오류가 발생했습니다.
            }
        }
        
        // 검색
        var search = function(){
        	submitform.submit();
        }
        
        // 검색 > 배송 서비스 세팅
        var setShipService = function(shipCompany){
            var codeGroup = "";
            
            if(shipCompany == "B010010"){
                codeGroup = "B020000";
            }else if(shipCompany == "B010020"){
                codeGroup = "B040000";
            }else if(shipCompany == "B010030"){
                codeGroup = "B140000";
            }
            else{
                //콤보박스 초기화
                $("#searchCourier").empty();
                var option = $("<option value = ''>== 배송 서비스 ==</option>");
                $('#searchCourier').append(option);
                return;
            }
            //$("#partDeliveryService").empty();
            sendServer(codeGroup);
        }
        
        // 검색 > 배송사 서비스 ajax 호출
        var sendServer = function(codeGroup){
            
            var paramList = new Array() ;
            var data = new Object() ;
            data.codeGroup = codeGroup
            data.locale = _USER_lang; 
            paramList.push(data) ;
            
            $.ajax({
                type       : "POST"
                ,async     : true  // true: 비동기, false: 동기
                ,url       : "/common/componentDataSet"
                ,cache     : false
                ,data      : JSON.stringify(paramList) 
                ,contentType: "application/json"
                ,dataType  : "json"
                ,beforeSend : function(xhr) {
                    xhr.setRequestHeader(header, token);
                }
                ,success   : function(data) {
                    setSelectBox(data)
                }
                ,error     : function(error) {
                    returnData = "error";
                    console.log(error);
                }
                ,complete: function (jqXHR, textStatus, errorThrown) {
                }
            }); 
        }
        
        // 검색 > 배송사 서비스 option 세팅
        var setSelectBox = function(data){
            //콤보박스 초기화
            $("#searchCourier").empty();
            var code = $("select[name='searchCourier']").data("code");
            
            for(var i = 0; i < data.length; i++){
                var option = "";
                
                if(i == 0) {
                    option = $("<option value = ''>== "+data[i].codeKname+" ==</option>");
                } else {
                    if(code == data[i].codeId) {
                        option = $("<option value = '"+data[i].codeId+"' selected>"+data[i].codeKname+"</option>");    
                    } else {
                        option = $("<option value = '"+data[i].codeId+"'>"+data[i].codeKname+"</option>");
                    }
                }
                
                $('#searchCourier').append(option);
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
        
       // 엑셀 다운 로드 
        var down = function(){
         var param = {
                    "searchDateStartValue" : $("input[name=searchDateStart]").val(),
                    "searchDateEndValue"   : $("input[name=searchDateEnd]").val(),
                    "searchCompany"       : $("input[name=searchCompany]").val(),
                    "searchCourier"       : $("input[name=searchCourier]").val(),
                    "searchSeller"        : $("input[name=searchSeller]").val(),
                    "searchMode"          : $("input[name=searchMode]").val() 
         };
        var url_="/admin/statis/sales/salesExcel?" + $.param(param);
        console.log("searchDateStart:" + $("input[name=searchDateStart]").val());
        console.log("searchDateEndValue:" + $("input[name=searchDateEnd]").val());
        console.log("searchCompany:" + $("input[name=searchCompany]").val());
        console.log("searchCourier:" + $("input[name=searchCourier]").val());
        console.log("searchSeller:" + $("input[name=searchSeller]").val());
        console.log("searchMode:" + $("input[name=searchMode]").val());
        location.replace(url_);
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
                    <h2><spring:message code="admin.statis.menu.title2" text="매출 원장 조회" /></h2>
                    <ul class="tab mt20">
                        <li class="<c:if test="${mode eq 'NA'}">on</c:if> tab_delivery"><a href="/admin/statis/sales"><spring:message code="admin.statis.sales.delivery" text="해외 특송" /></a></li>
                        <li class="<c:if test="${mode eq 'ND'}">on</c:if> tab_local"><a href="/admin/statis/salesLocal"><spring:message code="admin.statis.sales.local" text="국내 택배" /></a></li>
                        <li class="<c:if test="${mode eq 'EW'}">on</c:if> tab_charge"><a href="/admin/statis/salesCharge"><spring:message code="admin.statis.sales.charge" text="초과 요금" /></a></li>
                    </ul>
                    <ul class="tab_conts">
                        <li>
                            <form name="submitform" id="submitform" method="get">
                            <div class="search">
                                <table>
                                    <colgroup>
                                        <col class="cper1250">
                                        <col class="cperauto">
                                        <col class="cper1250">
                                        <col class="cperauto">
                                    </colgroup>
                                    <tbody>
                                        <tr>
                                            <th><spring:message code="admin.statis.search.title1" text="결재 일자" /></th>
                                            <td>
                                                <div  class="month">
                                                    <input class="date" type="text" name="searchDateStart" value="${search.searchDateStart}" maxlength="10" 
					                                    data-label="<spring:message code="order.seaech.date.start" text="검색일(시작일)" />"/>
					                                <span>~</span>
					                                <input class="date" type="text" name="searchDateEnd" value="${search.searchDateEnd}" maxlength="10" 
					                                    data-label="<spring:message code="order.seaech.date.end" text="검색일(종료일)" />"/>
                                                </div>
                                            </td>
                                            <th><spring:message code="admin.statis.search.title2" text="셀러" /></th>
                                            <td>
                                                <div class="searchbox">
	                                                <input name="searchSeller" id="searchSeller" type="text" class="wp250" value="${search.searchSeller}" placeholder="검색을 통해 셀러를 선택해주세요.">
	                                                <button type="button" class="ic_search ml5 valign_t"></button>
                                                </div>
                                                <button type="button" id="btn_clear"><spring:message code="admin.statis.search.clear" /></button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th><spring:message code="admin.statis.search.title3" text="배송 서비스" /></th>
                                            <td colspan="3">
                                                <div class="module_group">
					                                <div class="action">
					                                   <select class="select-ajax" id="searchCompany" name="searchCompany"
		                                                    data-codetype="" data-code="${search.searchCompany}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B010000" }'>
		                                                </select>
		                                                <select class="" id="searchCourier" name="searchCourier"
		                                                    data-codetype="" data-code="${search.searchCourier}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B040000" }'>
		                                                </select>
					                                </div>
					                                <div class="btn_group">
					                                    <button type="button" id="btnSerch" class="btn_type3"><spring:message code="button.search" text="조회" /></button>
					                                    <button type="button" class="btn_type1 down" id="down"><span><spring:message code="button.down.all" text="내려받기" /></span></button>
					                                </div>
					                            </div>
                                                
                                                
                                            </td>
                                            
                                        </tr>
                                    </tbody>
                                </table>

                                
                            </div> <!--search end-->
                 
                            <div class="module_group mt20">
                                <div class="action">
                                   <%@ include file="/WEB-INF/views/common/incPageSize.jsp"%>
                                </div>
                                
                                <div class="btn_group">
                                    <%-- 
                                        내려 받기는 향후 세금계산서를 위한 버튼 
                                    --%>
                                    <%-- <button type="button" class="btn_type1 down"><span>내려받기</span></button> --%>
                                </div>
                            </div>
                            
                            <div class="scr_x">

                                 <%--// #### include table start ############### --%>

	                    <c:choose>
	                        <c:when test="${mode eq 'NA'}"> <%--// 국외 --%>
	                            <%@ include file="/WEB-INF/views/admin/statis/inc/incSalesDelivery.jsp"%>
	                        </c:when>
	                        <c:when test="${mode eq 'ND'}"> <%--// 국내 --%>
	                            <%@ include file="/WEB-INF/views/admin/statis/inc/incSalesLocal.jsp"%>
	                        </c:when>
	                        <c:when test="${mode eq 'EW'}"> <%--// 추가요금 --%>
	                            <%@ include file="/WEB-INF/views/admin/statis/inc/incSalesCharge.jsp"%>
	                        </c:when>
	                    </c:choose>
                    
                                 <%--//  #### include table end ############### --%>
  
                            </div>
                            </form>
                            
                            <div class="pager">
                                <c:if test="${fn:length(list) > 0}">
                                <%@ include file="/WEB-INF/views/common/incPaging2.jsp"%>
                                </c:if>
                                <form name="defaultSearchForm" id="defaultSearchForm" method="get">
                                <input type="hidden" name="searchDateStart" value="${search.searchDateStart}">
                                <input type="hidden" name="searchDateEnd" value="${search.searchDateEnd}">
                                <input type="hidden" name="searchSeller" value="${search.searchSeller}">
                                <input type="hidden" name="searchCompany" value="${search.searchCompany}">
                                <input type="hidden" name="searchCourier" value="${search.searchCourier}">
                                <input type="hidden" name="searchMode" value="${mode}">
                                <input type="hidden" name="pageSize" value="${search.pageSize}">
                                <input type="hidden" name="currentPage" value="">
                                </form>
                            </div>
                            
                        </li>
                    </ul>
                    
                    
               </article>
               
           </div>
        </div>
           
        <!--// #### Content area ############### -->

    </div>

	<%// footer include%>
	<%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
	
</body>
</html>