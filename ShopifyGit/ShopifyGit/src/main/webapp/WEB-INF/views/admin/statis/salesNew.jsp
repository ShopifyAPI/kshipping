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
            
            setTotalPayment();
        };
    
        var bindEvent = function() {
            // 부피무게 입력: 숫자만 
            /*
            $("input:text[numberOnly]").on("keyup", function() {
                //$(this).val($(this).val().replace(/[^0-9]/g,""));
                $(this).val(addCommas($(this).val().replace(/[^0-9]/g,"")));
            });
            */
            // 부피무게 중량 입력 *********************************************************
            $("input:text[numberOnly]").on("focus", function() {
                var x = $(this).val();
                x = removeCommas(x);
                $(this).val(x);
            }).on("focusout", function() {
                var x = $(this).val();
                if(x && x.length > 0) {
                    if(!$.isNumeric(x)) {
                        x = x.replace(/[^.0-9]/g,"");
                    }
                    console.log("x:" + x);
                    if(x > 1)
                    x = addCommas(x);
                    $(this).val(x);
                }
            }).on("keyup", function() {
                $(this).val($(this).val().replace(/[^.0-9]/g,""));
            });
            // check box 컨트롤
            $(document).on("click","input[name='allCheck']",function(){
                allCheck("masterCode", this);
            });
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
            
            // 요약 ***************************************************************************
            $(document).on("click","#btnReport",function(){  
                var url = "/admin/statis/popup/salesReport" ;
                openPop(url, "", 1400, "", "", "");
            });
            // 택배사 정산/미정산 조회 *****************************************************************
            $(document).on("click","#btnCompanyAllSearch",function(){  
                var searchDateStart = $("input[name=searchDateStart]").val();
                var searchDateEnd = $("input[name=searchDateEnd]").val();
                location.href = "/admin/statis/salesNewAllCompany?searchDateStart=" + searchDateStart + "&searchDateEnd=" + searchDateEnd;
            });
            $(document).on("click","#btnCompanySearch",function(){  
                var searchDateStart = $("input[name=searchDateStart]").val();
                var searchDateEnd = $("input[name=searchDateEnd]").val();
                location.href = "/admin/statis/salesNewCompany?searchDateStart=" + searchDateStart + "&searchDateEnd=" + searchDateEnd;
            });
            // 셀러 정산/미정산 조회 *****************************************************************
            $(document).on("click","#btnSellerAllSearch",function(){  
                var searchDateStart = $("input[name=searchDateStart]").val();
                var searchDateEnd = $("input[name=searchDateEnd]").val();
                location.href = "/admin/statis/salesNewAllSeller?searchDateStart=" + searchDateStart + "&searchDateEnd=" + searchDateEnd;
            });
            $(document).on("click","#btnSellerSearch",function(){  
                var searchDateStart = $("input[name=searchDateStart]").val();
                var searchDateEnd = $("input[name=searchDateEnd]").val();
                location.href = "/admin/statis/salesNewSeller?searchDateStart=" + searchDateStart + "&searchDateEnd=" + searchDateEnd;
            });
            // 부피재계산 **********************************************************************
            $(document).on("click","#btnCalculWeightUpdate",function(){  
                updateCalculWeightProc();
            });
            // 택배사 정산 *********************************************************************
            $(document).on("click","#btnCompanyUpdate",function(){  
                updateCalculProc("COMPANY");
            });
            // 셀러 정산 **********************************************************************
            $(document).on("click","#btnSellerUpdate",function(){  
                updateCalculProc("SELLER");
            });
            // 매출원장재정산 
            $(document).on("click","#btnCalculAll",function(){  
                if(confirm(getMessage('admin.statis.button.calculall.alert'))) {
                    console.log("------calculAll START-----------");
                    var searchDateStart = $("input[name=searchDateStart]").val();
                    var searchDateEnd = $("input[name=searchDateEnd]").val();
                    var param = {
                            "searchDateStartValue" : searchDateStart,
                            "searchDateEndValue"   : searchDateEnd
                    };
                    var url_="/admin/statis/salesReCalcul?searchDateStart=" + searchDateStart + "&searchDateEnd=" + searchDateEnd;
                    ajaxCall("post", url_, null, updateCalculAllEnd);
                }
            });
            // 손익통계재정산 
            $(document).on("click","#btnCalculPayment",function(){  
                if(confirm(getMessage('admin.statis.button.calculpayment.alert'))) {
                    console.log("------calculAll START-----------");
                    var searchDateStart = $("input[name=searchDateStart]").val();
                    var searchDateEnd = $("input[name=searchDateEnd]").val();
                    var param = {
                            "searchDateStartValue" : searchDateStart,
                            "searchDateEndValue"   : searchDateEnd
                    };
                    var url_="/admin/statis/salesRePayment?searchDateStart=" + searchDateStart + "&searchDateEnd=" + searchDateEnd;
                    ajaxCall("post", url_, null, updateCalculAllEnd);
                }
            });
            // 부피무게 엑셀 업로드 ***************************************************************
             $(document).on("click","#btnCalculWeightUpload",function(){  
                 var url = "/admin/statis/popup/salesVolumeWeight";
                 openPop(url, "", "510", "", popupSelectBind, "");  
                 //openPop(url, "", 510, "", "", "");
            });
            // 상세보기 popup - 매출추가요금 클릭 
            $(document).on("click",".list-editEW",function(){  
                var masterCode = $(this).closest("tr").data("idx");
                console.log(">masterCode:" + masterCode);
                var url = "/admin/statis/popup/salesChargePopup?masterCode=" + masterCode+"&paymentCode=EW" ;
                openPop(url, "", 1200, "", "", "");
            });
            // 상세보기 popup - 매입추가요금 클릭 
            $(document).on("click",".list-editSW",function(){  
                var masterCode = $(this).closest("tr").data("idx");
                console.log(">masterCode:" + masterCode);
                var url = "/admin/statis/popup/salesChargePopup?masterCode=" + masterCode+"&paymentCode=SW" ;
                openPop(url, "", 1200, "", "", "");
            });
            
            $(document).on("change","#file_up",function(e){
                
                var name = $("#file_up")[0].files[0].name; //파일이름
                
                //확장자를 체크(xls xlsx 형식만 지원함)
                var extension = name.slice(name.lastIndexOf(".") + 1).toLowerCase(); //파일 확장자를 잘라내고, 비교를 위해 소문자로 만듬.
                if(extension != "xls" && extension != "xlsx"){ 
                    alert(getMessage("price.fileUploadExtension"));
                    $("#file_up").val("");
                    return false;
                };
                
                $("#upload_name").val(name);
                
            });
            
            
            
            //공시요금 팝업 저장버튼 클릭시
            $(document).on("click","#btn_write_pop_weight",function(){
                
                var $submitform = $('#volumeweight');
                   
                
                //파일
                var file_up = $("#file_up").val();
                if(file_up == null || file_up == ""){
                    
                    alert(getMessage("price.addFile")+getMessage("alert.selectCheck"));//첨부파일을 선택해주세요.
                    return;
                    
                }
                
                var options = {
                        dataType:"text",//결과
                        success:function(data){
                            console.log(data)
                            alert(getMessage("alert.proc.end", ["price.ExcelUpload"]));  // 엑셀업로드가 완료 되었습니다.
                            location.replace("/admin/statis/sales");
                        },beforeSend:function(){
                            $('.spinner').removeClass('spinner');
                            $('.spinner').addClass('spinner-dimmed');
                            $('.loading').addClass('loading');
                        },complete:function(){
                            $('.spinner').removeClass('spinner-dimmed');
                            $('.spinner').addClass('spinner-show');
                        },error:function(e){                    
                            console.log(e)
                            alert(getMessage("alert.proc.err", ["price.ExcelUpload"]));  // 등록 중 오류가 발생했습니다.
                            location.replace("/admin/statis/sales");
                        }
                    };
                
                $("#volumeweight").ajaxForm(options).submit();
            });
            // 엑셀업로드 팝업 select box 세팅
            var popupSelectBind = function () {
                 
                var $submitform = $('#volumeweight');
                bindSelectAjax($submitform);
                sendServerPop('');
            }
            // ****************************************************************************
        }; // end bindEvent
        
        var setTotalPayment = function() {
            
            $("#sumTotalArea th").each(function(){
                console.log($(this).attr("id"));
                num = $("input[name='"  + $(this).attr("id") + "']").val();
                $(this).text(num);
            });
        }
        
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
        // ************************************************************************* 
        var sendServerPop = function(codeGroup){
            
            var paramList = new Array() ;
            var data = new Object() ;
            data.codeGroup = codeGroup
            data.locale = _USER_lang; 
            paramList.push(data) ;
            
            //ajaxCall("POST", fData.url, paramList, setSelectView);
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
                     
                }
                ,error     : function(error) {
                    returnData = "error";
                    console.log(error);
                }
                ,complete: function (jqXHR, textStatus, errorThrown) {
                }
            }); 
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
        var url_="/admin/statis/sales/salesExcelNew?" + $.param(param);
        console.log("searchDateStart:" + $("input[name=searchDateStart]").val());
        console.log("searchDateEndValue:" + $("input[name=searchDateEnd]").val());
        console.log("searchCompany:" + $("input[name=searchCompany]").val());
        console.log("searchCourier:" + $("input[name=searchCourier]").val());
        console.log("searchSeller:" + $("input[name=searchSeller]").val());
        console.log("searchMode:" + $("input[name=searchMode]").val());
        location.replace(url_);
       }
       // 부피재계산 
       var updateCalculWeightProc = function() {
           var masterCodeList = new Array;
           var volumeWeightList = new Array;
           var volumeCheckList = new Array;
           var masterCode="";
           var tempStr;
           var index = 1;
           var volumeWeight;
           var totalWeight;
           var success = true;
           if(!$("input[name='masterCode']").is(":checked")){
               alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
               return;
           }
           else{
              $('input:checkbox[name="masterCode"]').each(function() {
                    console.log("index:" + index);
                    if(this.checked){
                         masterCode = this.value;
                         volumeWeight = $('#weightform input:text[id=weight_'+index+']').val();
                         totalWeight = $('#weightform input:text[id=totalweight_'+index+']').val();
                         console.log(">masterCode:" + masterCode);
                         console.log(">total weight:" + totalWeight);
                         console.log(">volume weight:" + volumeWeight);
                         if(volumeWeight == null || volumeWeight == '' )
                         {
                             console.log("error");
                             success = false;
                             alert("체크한 배송건의 부피무게를 g 단위로 넣어주세요");
                             return false;
                         }
                          // 실중량 < 부피중량 
                         if(totalWeight < volumeWeight || volumeWeight == 0)
                         {
                             masterCodeList.push(this.value);
                             volumeWeightList.push(volumeWeight);    
                             tempStr = masterCode + "@" + volumeWeight;
                             volumeCheckList.push(tempStr);
                         }
                         else
                         {
                             console.log("error");
                             success = false;
                             alert("입력한 부피무게가 총무게보다 작습니다. 총무게보다 부피무게가 큰 경우 계산됩니다.");
                             return false;
                         }   
                          
                    }
                    index++;
               });
           }
           if(success == true)
           {
               console.log(">masterCodeList:" + masterCodeList);
               console.log(">volumeWeightList:" + volumeWeightList);
               console.log(">volumeCheckList:" + volumeCheckList);
               var url = "/admin/statis/salesWeightCalculUpdate?volumeCheckList=" + volumeCheckList;
               ajaxCall("post", url, null, updateWeightCalculEnd);     
           }
        
       }
       
        
       // 택배사/셀러 정산 업데이트 처리 
        var updateCalculProc = function(type){
            var masterCodeList = new Array;
            var payCnt = 0;
            var jsonData = new Object();
           
            if(!$("input[name='masterCode']").is(":checked")){
                alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
                return;
            }else{
               $('input:checkbox[name="masterCode"]').each(function() {
                     if(this.checked){
                      masterCodeList.push(this.value);
                      payCnt++;
                      console.log(">input:" + $('input:text[name="volumeweight"]').val());
                      
                     }
                });
            }
            jsonData.masterCodeList = masterCodeList;
            jsonData.calculType = type;
            var url = "/admin/statis/salesCalculUpdate?masterCodeList=" + masterCodeList + "&calculType="+type;
            //var url = "/admin/statis/salesCalculUpdate";
            //console.log("<>"+jsonDataList);
            var jsonText = "";
            jsonText = jsonData ;
            console.log(JSON.stringify(jsonText))
            ajaxCall("post", url, null, updateCalculEnd);
            //ajaxCall("post", url, JSON.stringify(jsonText), updateCalculEnd);
        }
        var updateWeightCalculEnd = function(data) {
            if(data.errCode){
                alert( getMessage("admin.statis.button.weightcalcul.faild") );
            }else{
                alert( getMessage("admin.statis.button.weightcalcul.sucess") );
                location.reload();
            }
        }
        var updateCalculEnd = function(data) {
            if(data.errCode){
                alert( getMessage("admin.statis.button.calcul.faild") );
            }else{
                alert( getMessage("admin.statis.button.calcul.sucess") );
                location.reload();
            }
        }
        var updateCalculAllEnd = function(data) {
            if(data.errCode){
                alert( getMessage("admin.statis.button.calculall.faild") );
            }else{
                alert( getMessage("admin.statis.button.calculall.sucess") );
                location.reload();
            }
        }
        //3자리 단위마다 콤마 생성
        function addCommas(x) {
            return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
        //모든 콤마 제거
        function removeCommas(x) {
            if(!x || x.length == 0) return "";
            else return x.split(",").join("");
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
                        <li class="<c:if test="${mode eq 'ND'}">on</c:if> tab_delivery"><a href="/admin/statis/sales"><spring:message code="admin.statis.sales.delivery" text="해외 특송" /></a></li>
                        <li class="<c:if test="${mode eq 'DO'}">on</c:if> tab_local"><a href="/admin/statis/salesLocalNew"><spring:message code="admin.statis.sales.local" text="국내 택배" /></a></li>
                        <li class="<c:if test="${mode eq 'EW'}">on</c:if> tab_charge"><a href="/admin/statis/salesChargeNew"><spring:message code="admin.statis.sales.charge" text="초과 요금" /></a></li>
                    </ul>
                    <ul class="tab_conts">
                        <li>
                            <form name="submitform" id="submitform" method="get"  >
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
                                                        <select class="select-ajax" id="searchCourier" name="searchCourier"
                                                            data-codetype="" data-code="${search.searchCourier}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B040000" }'>
                                                        </select>
                                                    </div>
                                                    <div class="btn_group">
                                                        <button type="button" class="btn_type3" id="btnReport"><spring:message code="admin.statis.button.report" text="요약" /></button>
                                                        <button type="button" class="btn_type3" id="btnSerch"><spring:message code="button.search" text="조회" /></button>
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
                          </form>   
                        <c:choose>
                            <c:when test="${mode eq 'ND'}"> <%--// 국외 --%>
                                <%@ include file="/WEB-INF/views/admin/statis/inc1/incSalesDelivery.jsp"%>
                            </c:when>
                            <c:when test="${mode eq 'DO'}"> <%--// 국내 --%>
                                <%@ include file="/WEB-INF/views/admin/statis/inc1/incSalesLocal.jsp"%>
                            </c:when>
                            <c:when test="${mode eq 'EW'}"> <%--// 추가요금 --%>
                                <%@ include file="/WEB-INF/views/admin/statis/inc1/incSalesCharge.jsp"%>
                            </c:when>
                        </c:choose>
                    
                                 <%--//  #### include table end ############### --%>
  
                            </div>
                           
                            
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