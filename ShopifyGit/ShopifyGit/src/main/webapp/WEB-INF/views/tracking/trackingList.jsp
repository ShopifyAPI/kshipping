<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 

<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%> 
    <title>Tracking</title>
    
    <script type="text/javascript">
    
    // READY
    $(function () {
        var param_masterCode = "";
        initialize();
    });
 
    var initialize = function () {
        initControls();
        bindEvent();
    };
    var initControls = function () {
        setDate();
        var $submitform = $('#searchform');
        bindSelectAjax($submitform); // 검색 select box 세팅
    };
    var bindEvent = function () {
        //상세보기
        $(document).on("click",".list-detail",function(){
            var masterCode = $(this).data("code");
            var combineCode = $(this).data("combinecode");
            var url = "/cs/popup/backShowPop?masterCode=" + masterCode + "&combineCode=" + combineCode;
            openPop(url, "", 1200, "", "", "");
        });
        //검색
        $(document).on("click",".ic_search",function(){
            search();
        });
        $(document).on("keyup","#searchWord",function(e){
            if(e.keyCode == 13){
                search();
            }
        });
    };
    
    // 검색일자
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
    
    // 전체 검색
    var search = function(){
    	var _searchType = $("#searchType").val() ;
    	//// 검색조건은 없고 검색어만 존재하는 경우, 검색어를 초기화한다. 
    	if (_searchType != undefined && _searchType.trim() == "") {
    		$("#searchWord").val("") ;
    	}
        document.searchform.submit();
    }
    </script>
    
    <style>
    	.st {border-radius: 5px; padding-left: 5px; padding-right: 5px;}
    	.temp {float: right; width: 80px; height: 35px; line-height: 33px; text-align: center; margin-left:3px; margin-right:3px;}
    	.st.deliver2 {border: 2px solid #d16915;}
    	.st.paym2 {border: 2px solid #5d3d78;}
    </style>
</head>

<body>
<div class="frame">
	<div class="frame_wrap">
		<%// gnb include %>
		<%@ include file="/WEB-INF/views/common/incGnb.jsp"%> 
		<div class="sub_conts">
			<!-- #### Content area ############### -->               
			<article>
				<!-- tit_wrap -->
				<div class="tit_wrap">
					<h3><spring:message code="incgnb.tracking" text="배송추적" /></h3>
					<!-- 기본 검색 영역 -->
					<div class="sect_right">
						<form name="searchform" id="searchform" method="get" action="/tracking">
						
							<%--
								//// 2020.05.26, 이지중 ////
								
								-	tb_use_code 테이블의 데이터와 화면에 표시하려는 항목이 다르기 때문에 하드코딩으로 임시 대체합니다.
								-	추후 동적인 사용을 원하실 경우 tb_use_code 테이블에 추가 코드를 발급하거나 전용 쿼리를 맵핑하는 등의 작업을 진행해야 합니다.
								
								//// 배송상태의 원본코드 ////
								
								<select
									class="select-ajax"
									name="searchState"
									id="searchState"
									data-code="${search.searchState}"
									data-url="/common/componentDataSet"
									data-param='{ "codeGroup" : "A020000" }'>
								</select>
							--%>
							
							<%-- ////	검색조건 : 배송상태	//// --%>
							<select name="searchState" id="searchState">
								<option value="">== 배송상태 ==</option>
								<option value="A020040" ext="10">집하(국내)</option>
								<option value="A020045" ext="30">배송중(국내)</option>
								<option value="A020049" ext="41">배송완료(국내)</option>
								<option value="A020050" ext="50">해외발송준비</option>
								<option value="A020060" ext="60">접수</option>
								<option value="A020070" ext="70">현지도착</option>
								<option value="A020080" ext="80">현지통관 중</option>
								<option value="A020090" ext="90">현지배송 중</option>
								<option value="A020099" ext="99">배송완료</option>
							</select>
							
							<%-- ////	검색조건 : 접수기간	//// --%>
							<div  class="month">
								<input class="date" type="text" name="searchDateStart" value="${search.searchDateStart}" maxlength="10" data-label="<spring:message code="order.seaech.date.start" text="검색일(시작일)" />"/>
								<span>~</span>
								<input class="date" type="text" name="searchDateEnd" value="${search.searchDateEnd}" maxlength="10" data-label="<spring:message code="order.seaech.date.end" text="검색일(종료일)" />"/>
							</div>
							
							<%--
								//// 2020.05.26, 이지중 ////
								
								-	tb_use_code 테이블의 데이터와 화면에 표시하려는 항목이 다르기 때문에 하드코딩으로 임시 대체합니다.
								-	추후 동적인 사용을 원하실 경우 tb_use_code 테이블에 추가 코드를 발급하거나 전용 쿼리를 맵핑하는 등의 작업을 진행해야 합니다.
								
								//// 검색유형의 원본코드 ////
								
								<select
									class="select-ajax"
									name="searchType"
									id="searchType"
									data-codetype="etc"
									data-code="${search.searchType}"
									data-url="/common/componentDataSet"
									data-param='{ "codeGroup" : "A150000" }'>
								</select>
							--%>

							<%-- ////	검색조건 : 검색유형	//// --%>
							<select name="searchType" id="searchType">
								<option value="">== 검색조건 ==</option>
								<option value="orderName">주문번호</option>
								<option value="goods">상품명</option>
								<option value="name">주문자명</option>
								<option value="phone">주문자 HP</option>  
							</select>
							
							<%-- ////	검색조건 : 검색어	//// --%>
		                    <div class="searchbox">
		                    	<button type="button" class="ic_search"></button>
		                    	<input type="text" name="searchWord" id="searchWord" value="${search.searchWord}" placeholder="<spring:message code="order.seaech.orderWord.label" text="검색어를 입력해주세요." />">
		                   	</div>
						</form>
						
						<script>
						$(document).ready(function() {
							//// 일반적으로 검색조건이 설정되어 있는 경우, 검색결과에서 기존 조건을 그대로 보여줘야 한다.
							var searchStateValue = '${search.searchState}' ;
							if (searchStateValue != undefined && searchStateValue.trim().length > 0) {
								var _select = $("#searchState > option[value="+searchStateValue+"]") ;
								if (_select) { _select.attr("selected", "true") ; }
							}
							var searchWordValue = '${search.searchWord}' ;
							if (searchWordValue != undefined && searchWordValue.trim().length > 0) {
								var searchTypeValue = '${search.searchType}' ;
								if (searchTypeValue != undefined && searchTypeValue.trim().length > 0) {
									var _select = $("#searchType > option[value="+searchTypeValue+"]") ;
									if (_select) { _select.attr("selected", "true") ; }
								}
							}
						}) ;
						</script>
					</div>
				</div>
		
				<ul class="tab">                        
					<li><a href="/payment"><spring:message code="incgnb.payment" text="결제" /></a></li>
					<li><a href="/shipment"><spring:message code="incgnb.shipment" text="배송" /></a></li>
					<li class='on'><a href="/tracking"><spring:message code="incgnb.tracking" text="배송추적" /></a></li>
				</ul>
				    
				<ul class="tab_conts">
					<li>
						<div class="module_group">
							<div class="btn_group">
								<div class="st deliver2 temp"><spring:message code="button.tracking.international" text="해외 " /></div>
								<div class="st paym2 temp"><spring:message code="button.tracking.domestic" text="국내" /></div>
							</div>
							<div class="action">
								<%-- 페이지 사이즈 --%>
								<%@ include file="/WEB-INF/views/common/incPageSize.jsp"%>
								<span style="font-color:#FF0000;!important">
									<%-- <spring:message code="shipment.Precautions" text="* 반드시 개별 특송상품박스에 아래 배송바코드가 부착되어야 합니다!"/> --%> 
									&nbsp; 집하된 이후의 배송상태를 조회 할 수 있습니다.
								</span>  
							</div>
						</div>
						<form name="listform" id="listform" method="get">
							<c:choose>
								<c:when test="${fn:length(list) == 0}">
									<div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
								</c:when>
								<c:otherwise>
									<table class="tbtype">
									    <colgroup>
									        <col class="cper1100"/>
									        <col class="cper1300"/>
									        <col class="cper0780"/>
									        <col class="cperauto"/>
									        <col class="cper1100"/>
									        <col class="cper0780"/>
									        <col class="cper0950"/>
									        <col class="cper1300"/>
									    </colgroup>
									    <thead>
									        <tr>                                    
									            <th><spring:message code="settings.shopName" text="쇼핑몰명" /></th>
												<th><%-- <spring:message code="shipment.shipmentCode" text="주문자 HP" /> --%>주문자 HP</th>
												<th><spring:message code="order.orderNo" text="주문번호" /></th>
												<th><spring:message code="settings.itemName" text="상품명" /></th>
												<th><spring:message code="settings.orderInfo" text="주문자 정보" /></th>
												<th><spring:message code="settings.shippingCountry" text="배송국가" /></th>
												<th><spring:message code="settings.shipmentDate" text="배송 접수일" /></th>
												<th><%-- <spring:message code="settings.status" text="상태" /> --%>배송 트래킹</th>
									    	</tr>
										</thead>
										<tbody>
											<c:forEach items="${list}" var="item" varStatus="status">
											<tr>
											    <td class="t_center"><p>${item.shopName}</p></td>
												<td class="t_center" title="${item.buyerPhone}"><p>${item.buyerPhone}</p></td>
												<td class="t_center"><p>${item.orderName}</p></td>
												<td><p style="cursor : pointer" class="list-detail" data-code="${item.masterCode}" data-combinecode="${item.combineCode}">${item.goods} <c:if test="${item.goodsCnt > 0}">외 ${item.goodsCnt} 개</c:if></p></td>
												<td class="t_center"><p>${item.buyerFirstname}  ${item.buyerLastname}</p></td>
												<td class="t_center"><p>${item.buyerCountryCode}</p></td>
												<td class="t_center"><p>${item.orderDate}</p></td>
												<td class="t_center"><div class="st ${item.stateStrCss}"><p><b>${item.stateStr}</b></p><p>${item.stateDate}</p></div></td>
											</tr>
											</c:forEach>
										</tbody>    
									</table>
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
			<!--// #### Content area ############### -->        
        </div>    
    </div>
</div>    
<%// footer include %>
<%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
</body>
</html>