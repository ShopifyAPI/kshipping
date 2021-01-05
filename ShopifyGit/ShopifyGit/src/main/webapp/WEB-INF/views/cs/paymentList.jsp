<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_ADM_441_BAS 
기능설명 : CS 관리 추가요금 목록
           추가 요
Author   Date      Description
 김윤홍     2020-02-05  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
<%// header include %>
<%@ include file="/WEB-INF/views/common/incHeader.jsp"%>

<title>CS 관리 추가요금 목록 조회</title>
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
		
        //결제확인
        $(document).on("click","#btn_paymentComplete",function(){   
        	var status = "A060020";
        	updateStatus(status);
        });
       
        // 상세보기 popup 
        $(document).on("click",".list-edit",function(){  
        	var masterCode = $(this).closest("tr").data("idx");
            var combineCode = $(this).closest("tr").data("combineCode");
            var url = "/cs/popup/paymentShowPop?masterCode=" + masterCode + "&combineCode=" + combineCode;
            openPop(url, "", 1200, "", "", "");
            
            /* var masterCode = $(this).closest("tr").data("idx");
            var url = "/cs/popup/paymentShowPop?masterCode=" + masterCode;
            openPop(url, "", 900, "", "", ""); */
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
        
	};
	
	var updateStatus = function(status){
		
		$("#updateStatus").val(status);
		
		var $submitform = $('#listform');
		
		// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
        var param = $submitform.serializeObject();
        var chk = param.ckBox;
        
        var test = param.status;
        
        if (chk == null) {
			alert(getMessage("alert.checkboxChk"));//삭제할 대상을 선택해주세요.
			$("#updateStatus").val("");
			return;
			
		}
		
        var confirm_1 = confirm(getMessage("alert.proc", ["common.target","button.edit"]));//선택 항목을 수정 하시겠습니까?
        
		if (confirm_1 == true) {
			
			var type = "post";
			var url = "/cs/updateStatus";

			// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
			ajaxCall(type, url, param, sendFromUpdate);
	
		} else if (confirm_1 == false) {
			$("#updateStatus").val("");
			return;

		}
	};
	
	var sendFromUpdate = function(data){
		
		if(data.errCode == true) {
		    alert(getMessage("alert.proc.end", ["button.edit"]));  // 수정이 완료 되었습니다.
            location.reload();
		} else {
			alert(getMessage("alert.proc.err", ["button.edit"]));  // 수정 중 오류가 발생했습니다.
		}
		
	};
	
	// 엑셀 다운 로드 
    var downExcel = function() {
        location.replace("/cs/paymentListExcel");
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
    <div class="frame"> <!--프레임 영역 이 div 영역부터 붙여서 넣어주세요.-->
        <div class="frame_wrap">
        
       	<%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incGnb.jsp"%> 
        
        <div class="sub_conts">
                <article>
                    <div class="tit_wrap">
                        <h3><spring:message code="incgnb.csmanage" text="CS 관리" /></h3>
                    </div> <!--tit_wrap end-->
                    <ul class="tab">
                        <li><a href="/cs/backList"><spring:message code="cs.back" text="반송" /></a></li>
                        <li><a href="/cs/exchangeList"><spring:message code="cs.exchange" text="교환" /></a></li>
                        <li><a href="/cs/returnList"><spring:message code="cs.return" text="반품" /></a></li>
                        <li class="on"><a href="/cs/paymentList"><spring:message code="cs.payment" text="추가요금" /></a></li>
                    </ul>
                    <ul class="tab_conts">
                        <li>
                            <div class="tit_wrap mt0">
                            	<form name="searchform" method="get" id="searchform" action="/cs/paymentList">
                            		<div class="sect_right">
	                                    <select class="select-ajax" name="searchPayment"  id="searchPayment" 
		                                		data-codetype="etc" data-code="${search.searchPayment}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A060000" }'>
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
	                                        <input type="text" name="searchWord" placeholder="<spring:message code="order.seaech.orderWord.label" text="검색어를 입력해주세요." />" value="${search.searchWord}">
	                                        <button type="button" class="ic_search"></button>
	                                    </div>
                                	</div>
                            	</form>
                                
                            </div> <!--tit_wrap end-->
                            <div class="module_group">
                                <div class="btn_group">
                                    <%--<button type="button" id="btn_paymentComplete" class="btn_type2"><spring:message code="button.paymentChk" text="결제확인" /></button> --%>
                                    <button type="button" class="btn_type2"><spring:message code="button.payment" text="결제하기" /></button>
                                    <%--<button type="button" class="btn_type1 send"><span><spring:message code="cs.sendGuide" text="안내 발송" /></span></button> --%>
                                    <%--<button type="button" class="btn_type1 down"><span><spring:message code="button.down" text="내려받기" /></span></button> --%>
                                </div>
                                <div class="action">
									 <%-- 페이지 사이즈 --%>
                                     <%@ include file="/WEB-INF/views/common/incPageSize.jsp"%>
								</div>
                            </div>
                            <form name="listform" id="listform" method="get">
                           	<input type="hidden" id="updateStatus" name="updateStatus" value=""/>
                           	<c:choose>
								<c:when test="${fn:length(list) == 0}">
									<tr>
										<div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
									</tr>
								</c:when>
							<c:otherwise>
                            <div class="scr_x">
                            	<table class="tbtype">
                                    <colgroup>
                                        <col class="wp35">
                                        <col class="wp150"/>
                                        <col class="wp160"/>
                                        <col class="wp80"/>
                                        <col class="cperauto"/>
                                        <col class="wp60"/>
                                        <col class="wp90"/>
                                        <col class="wp100"/>
                                        <col class="wp160"/>
                                        <col class="wp120"/>
                                    </colgroup>
                                    <thead>
                                        <tr> 
                                            <th><input type="checkbox" id="ind01" name="allCheck"><label for="ind01"><span class="icon_ck" ></span></label></th>
                                            <th><spring:message code="settings.shopName" text="쇼핑몰명" /></th>
                                            <th><spring:message code="admin.cs.masterCode" text="배송바코드" /></th>
                                            <th><spring:message code="order.orderNo" text="주문번호" /></th>
                                            <th><spring:message code="shipment.productName" text="상품명" /></th>
                                            <th><spring:message code="admin.cs.country" text="국가" /></th>
                                            <th><spring:message code="cs.globalCourier" text="해외특송사" /></th>
                                            <th><spring:message code="shipment.shipmentDate" text="배송 접수일" /></th>
                                            <th><spring:message code="admin.cs.etc" text="비고" /></th>
                                            <th><spring:message code="cs.paymentState" text="결제상태" /></th>
                                        </tr>
                                    </thead>
                                    <tbody>
									<c:forEach items="${list}" var="list" varStatus="status">
										<tr data-idx="${list.masterCode}" data-combine="${list.combineCode}">
											<td><input type="checkbox" id="ind02_${status.count}" name="ckBox" value="${list.masterCode}"><label for="ind02_${status.count}"><span class="icon_ck" ></span></label></td>
											<td class="t_center"><p>${list.shopName}</p></td>
	                                            <td class="t_center list-edit" title="${list.invoice}">
												    <p>${list.masterCode}<c:if test="${list.localCode != ''}"><strong style="color:blue">ⓟ</strong></c:if></p>
												</td>
	                                            <td class="t_center list-edit" title="${list.orderCode}"><p>${list.orderName}</p></td>
	                                            <td class="list-edit"><p>${list.goods}</p></td>
	                                            <td class="t_center"><p>${list.buyerCountryCode}</p></td>
	                                            <td class="t_center" title="${list.courier}"><p>${list.courierCode}</p></td>
	                                            <td class="t_center"><p>${list.orderDate}</p></td>
	                                            <td class="t_center"><p>${list.addChargeInfo}</p></td>
	                                            <td class="t_center">
	                                               <c:choose>  
                                                       <c:when test="${list.payState == 'Y'}">
                                                           <div class="btn_state paym2"><spring:message code="cs.payment.completed" text="결재완료" /></div>
                                                       </c:when>
                                                       <c:otherwise>
                                                           <div class="btn_state paym"><spring:message code="cs.payment.request" text="결재요청" /></div>
                                                       </c:otherwise>
                                                   </c:choose>
	                                            </td>
										</tr>
											</c:forEach>  
										
                                    </tbody>
                            	</table>
                            </div>
                            </c:otherwise>
                         </c:choose>
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
    </div> <!--frame end -->
</body>
</html>