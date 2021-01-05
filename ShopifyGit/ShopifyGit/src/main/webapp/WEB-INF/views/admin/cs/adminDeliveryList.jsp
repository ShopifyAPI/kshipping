<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_SYS_211_BAS
기능설명 :관리자 CS 관리 배송목록
Author   Date      Description
 김윤홍     2020-02-04  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
<%// header include %>
<%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 

<title>CS 관리 배송목록 조회</title>
<script type="text/javascript">

    //READY
    $(function () {
        initialize();
    });
    
    var initialize = function () {
        initControls();
        bindEvent();
    };
    
    var initControls = function () {
        bindSelectAjax($('#searchform'));//select Box 세팅
        setDate();
    };
    

     
    var bindEvent = function () {
        
        // check box 컨트롤
        $(document).on("click","input[name='allCheck']",function(){
            allCheck("ckBox", this);
        });
        
        //다운로드
        $(document).on("click",".down",function(){   
            downExcel();
        });
        
        // 상세보기 popup 
        $(document).on("click",".list-edit",function(){  
            var masterCode = $(this).closest("tr").data("idx");
            var combineCode = $(this).closest("tr").data("combineCode");
            var url = "/admin/cs/popup/adminDeliveryShowPop?masterCode=" + masterCode + "&combineCode=" + combineCode;
            openPop(url, "", 1200, "", "", "");
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
        
        
        $(document).on("click","#btnPayment",function(){
            popPaymentProc('');
        });
        
        // 바코드 출력
        $(document).on("click",".bacord",function(){  
            allClosePop();
            if(!$("input[name='ckBox']").is(":checked")){
                alert( getMessage("alert.checkboxChk") ); // 삭제할 대상을 선택해주세요
                return;
            }
            printBarcode();
        });
        
    };
    
     var printBarcode = function() {
            //allClosePop();
            var email = '';
            email = $("input[name='email']").val();
            if(email == '')
            {   
                alert("셀러의 이메일 주소를 입력해주세요.!!!!");
                return;
            }
            console.log("email:" + email);
            if(confirm( getMessage("alert.payment.barcode") )) {
                var $submitform = $('#listform');
                var param = $submitform.serializeObject();
                //param.email = email;
                console.log("ckBox:" + param.ckBox);
                console.log("param.email:" + param.email);
                openPop("/shipment/popup/popBacordPrintAdmin?masterCode="+param.ckBox+ "&email="+param.email+"&", "", 700);
            }
    }
    
    var popPaymentProc = function(masterCode){
        var masterCodeListArr = [];
        var payCnt = 0;
        var jsonDataList = [];
        //allClosePop();
        var email = '';
        email = $("input[name='email']").val();
        if(email == '')
        {   
            alert("셀러의 이메일 주소를 입력해주세요.!!!!");
            return;
        }
        console.log("email:" + email);
        if(masterCode == ""){
            if(!$("input[name='ckBox']").is(":checked")){
                 alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
                 return;
            }else{
                $('input:checkbox[name="ckBox"]').each(function(i,e) {
                        if(this.checked){
                        var jsonData = {};
                            if(jsonData.masterCode != "" && jsonData.masterCode != undefined) {
                                jsonData.masterCode = this.value;
                                jsonData.email = email;
                                console.log("1)masterCode : " + jsonData.masterCode);
                            }else{
                                jsonData.masterCode = this.value;
                                jsonData.email = email;
                                console.log("2)masterCode : " + jsonData.masterCode);
                            }
                             jsonDataList.push(jsonData);
                             console.log(jsonDataList);
                             payCnt++;   
                        }
                 });
            }
        }else{
            payCnt = 1;
        }
        var url = "/shipment/popup/popPaymentShipmentProcAdmin";
        console.log(jsonDataList);
        ajaxCall("post", url, jsonDataList, popPaymentPopOpenEnd);
    }
    
    var popPaymentPopOpenEnd = function(data) {
        if(data.errCode){
            alert( getMessage("alert.payment.shipping.faild") );
            search();
        }else{
            alert( getMessage("alert.payment.shipping.sucess") );
            location.reload();
        }
    }
    
    // 엑셀 다운 로드 
    var downExcel = function() {
        var _url = "/admin/cs/adminDeliveryListExcel";
        searchDateStartValue = $("input[name=searchDateStart]").val() ;
        searchDateEndValue = $("input[name=searchDateEnd]").val() ;
        searchType = $("input[name=searchType]").val() ;
        searchWord = $("input[name=searchWord]").val() ;
        if (searchDateStartValue && searchDateStartValue.length == 10) {
            if (searchDateEndValue && searchDateEndValue.length == 10) {
                _url += ("?searchDateStart="+searchDateStartValue) ;
                _url += ("&searchDateEnd="+searchDateEndValue) ;
                _url += ("&searchType="+searchType) ;
                _url += ("&searchWord="+searchword) ;
            }           
        }
        location.replace(_url);
    }
    
    var setDate = function(){
        var searchDateStartSet = "${search.searchDateStart}";
        var searchDateEndSet = "${search.searchDateEnd}";
        var todayStart = getMonthTodate();
        var todayEnd = getTodateLast();
        if(searchDateStartSet != "") todayStart = searchDateStartSet;
        if(searchDateEndSet != "") todayEnd = searchDateEndSet;
        $("input[name=searchDateStart]").val(todayStart);           
        $("input[name=searchDateEnd]").val(todayEnd);
    };

    // 전체 검색
    var search = function(){
        searchform.submit();
    }

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
                    <div class="tit_wrap">
                        <h3><spring:message code="incgnb.csmanage" text="CS 관리" /></h3>
                    </div> <!--tit_wrap end-->
                    <ul class="tab">
                        <li class="on"><a href="/admin/cs/adminDeliveryList"><spring:message code="incgnb.shipment" text="배송" /></a></li>
                        <li><a href="/admin/cs/adminErrorList"><spring:message code="cs.error" text="에러" /></a></li>
                        <li><a href="/admin/cs/adminBackList"><spring:message code="cs.back" text="반송" /></a></li>
                        <li><a href="/admin/cs/adminExchangeList"><spring:message code="cs.exchange" text="교환" /></a></li>
                        <li><a href="/admin/cs/adminReturnList"><spring:message code="cs.return" text="반품" /></a></li>
                        <li><a href="/admin/cs/adminPaymentList"><spring:message code="cs.payment" text="추가요금" /></a></li>
                    </ul>
                    
                    <ul class="tab_conts">
                        <li>
                            <div class="tit_wrap mt0">
                                <form name="searchform" method="get" id="searchform" action="/admin/cs/adminDeliveryList">
                                    <div class="sect_right">
                                        <select class="select-ajax" name="searchState"  id="searchState" 
                                                data-codetype="" data-code="${search.searchState}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A020000" }'>
                                        </select>
                                        <div  class="month"> <!--아이콘이 버튼으로 별도작업되야하면 말씀해주세요. 다시 전달해드리겠습니다.-->
                                            <input type="text" name="searchDateStart" class="date" maxlength="10" data-label="<spring:message code="order.seaech.date.start" text="검색일(시작일)" />" data-required="Y" />
                                            <span>~</span>
                                            <input type="text" name="searchDateEnd" class="date" maxlength="10" data-label="<spring:message code="order.seaech.date.end" text="검색일(종료일)" />" data-required="Y" />
                                        </div>
                                        <select class="select-ajax" name="searchType"  id="searchType" 
                                                data-codetype="etc" data-code="${search.searchType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A190000" }'>
                                        </select>
                                        <div class="searchbox"> <!--인풋박스에 클릭시 active 클래스 추가-->
                                            <input type="text" id="searchWord" name="searchWord" placeholder="<spring:message code="common.searchWord" text="검색어" />" value="${search.searchWord}">
                                            <button type="button" class="ic_search"></button>
                                        </div>
                                    </div>
                                </form>
                                
                            </div> <!--tit_wrap end-->
                            
                           
                            <form name="listform" id="listform" method="get">
                            
                            <div class="module_group">
                                <div class="btn_group">
                                    <!-- 관리자 긴급 처리용 : 조한두  -->
                                    <th><spring:message code="admin.seller.email" text="셀러이메일" /></th> 
                                    <input type="text" name="email" id="email" placeholder="shopigate@solugate.com" value=""/>
                                    <button type="button" class="btn_type2 payment" id="btnPayment"><spring:message code="button.delivery.proc" text="배송처리" /></button>
                                    <button type="button" class="btn_type2 state_change" id="btnStateChange"><spring:message code="button.state.change" text="상태변경" /></button>
                                    <button type="button" class="btn_type1 down" id="down"><span><spring:message code="button.down.all" text="내려받기" /></span></button>
                                    <button type="button" class="btn_type1 bacord"><span><spring:message code="button.barcode" text="바코드" /></span></button>
                                  
                                </div>
                                <div class="action">
                                     <%-- 페이지 사이즈 --%>
                                     <%@ include file="/WEB-INF/views/common/incPageSize.jsp"%>
                                </div>
                            </div>
                            
                            <input type="hidden" id="updateStatus" name="updateStatus" value=""/>
                            
                            <table class="tbtype">
                                <colgroup>
                                    <col class="wp35">
                                    <col class="wp200"/>
                                    <col class="wp200"/>
                                    <col class="wp200"/>
                                    <col class="wp140"/>
                                    <col class="cperauto"/>
                                    <col class="wp60"/>
                                    <col class="wp90"/>
                                    <col class="wp200"/>
                                    <col class="wp100"/>
                                    <col class="wp120"/>
                                </colgroup>
                                <thead>
                                    <tr> 
                                        <th><input type="checkbox" id="ind01" name="allCheck"><label for="ind01"><span class="icon_ck" ></span></label></th>
                                        <th><spring:message code="settings.shopName" text="쇼핑몰명" /></th>
                                        <th><spring:message code="admin.cs.masterCode" text="배송바코드" /></th>
                                        <th><spring:message code="admin.cs.hblNo" text="hbl번호"/></th>
                                        <th><spring:message code="order.orderNo" text="주문번호" /></th>
                                        <th><spring:message code="admin.pop.email" text="이메일" /></th>
                                        <th><spring:message code="admin.cs.country" text="국가" /></th>
                                        <th><spring:message code="cs.globalCourier" text="해외특송사" /></th>
                                        <th><spring:message code="admin.cs.tracking" text="트래킹 번호"/></th>
                                        <th><spring:message code="shipment.shipmentDate" text="배송 접수일" /></th>
                                        <th><spring:message code="order.orderStatus" text="상태" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${fn:length(list) == 0}">
                                        <tr>
                                            <td class="t_center" colspan="10"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></td>
                                        </tr>
                                    </c:when>
                                   <c:otherwise>
                                        <c:forEach items="${list}" var="list" varStatus="status">
                                            <tr data-idx="${list.masterCode}" data-combine="${list.combineCode}">
                                                <td><input type="checkbox" id="ind02_${status.count}" name="ckBox" value="${list.masterCode}"><label for="ind02_${status.count}"><span class="icon_ck" ></span></label></td>
                                                <td class="t_center"><p>${list.shopName}</p></td>
                                                <td class="t_center list-edit" title="${list.invoice}">
                                                    <p>${list.masterCode}<c:if test="${list.localCode != ''}"><strong style="color:blue">ⓟ</strong></c:if>
                                                        <c:if test="${list.combineCode =='Y'}"><strong style="color:red">ⓒ</strong></c:if></p>
                                                </td>
                                                <td class="t_center"><p>${list.hblNo}</p></td>
                                                <td class="t_center list-edit" title="${list.orderCode}"><p>${list.orderName}</p></td>
                                                <td class="t_center"><p>${list.email}</p></td>
                                                <td class="t_center"><p>${list.buyerCountryCode}</p></td>
                                                <td class="t_center" title="${list.courier}"><p>${list.courierCode}</p></td>
                                                <td class="t_center"><p>${list.trackingNo}</p></td>
                                                <td class="t_center"><p>${list.orderDate}</p></td>
                                                <c:if test="${list.stateGroup != 'A030000'}">
                                                    <td class="t_center"><div class="btn_state ${list.stateStrCss}">${list.stateStr}</div></td>
                                                </c:if>
                                                <c:if test="${list.stateGroup == 'A030000'}">       
                                                    <td class="t_center"><div class="btn_state ${list.reasonStrCss}">${list.reasonStr}</div></td>
                                                </c:if>
                                            </tr>
                                        </c:forEach>  
                                    </c:otherwise>
                                </c:choose>
                                
                                </tbody>
                            </table>
                            </form>
                        </li>
                    </ul>   
                    
                    <div class="pager">
                        <c:if test="${fn:length(list) > 0}">
                        <%@ include file="/WEB-INF/views/common/incPaging2.jsp"%>
                        </c:if>
                        <form name="defaultSearchForm" id="defaultSearchForm" method="get">                                
                        <input type="hidden" name="searchState" value="${search.searchState}">
                        <input type="hidden" name="searchDateStart" value="${search.searchDateStart}">
                        <input type="hidden" name="searchDateEnd" value="${search.searchDateEnd}">
                        <input type="hidden" name="searchType" value="${search.searchType}">
                        <input type="hidden" name="searchWord" value="${search.searchWord}">
                        <input type="hidden" name="pageSize" value="${search.pageSize}">
                        <input type="hidden" name="currentPage" value="">
                        
                        </form>
                    </div>
               </article>
           </div>
        </div>
    </div>
                
   <%// footer include %>
   <%@ include file="/WEB-INF/views/common/incAdmFooter.jsp"%>
</body>
</html>