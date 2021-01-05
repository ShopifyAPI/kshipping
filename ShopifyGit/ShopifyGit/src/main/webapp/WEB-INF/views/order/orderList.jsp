<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>  
<!-- 
**************************************************
화면코드 : UI_ADM_532_POP 
기능설명 : 설정관리 > 배송관리 > 포장재 popup
Author   Date         Description
jwh      2020-01-29   First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>
    <title><spring:message code="incgnb.order" text="주문" /></title>
    <link type="text/css" rel="stylesheet" href="/style/order.css" />
    <link type="text/css" rel="stylesheet" href="/style/courier.css" />
    <script type="text/javascript" src="/js/order/order.js"></script>
    <script type="text/javascript" src="/js/order/orderPopup.js"></script>
<%--     <script type="text/javascript" src="/js/order/order.js?v=<%=System.currentTimeMillis() %>"></script> --%>
<%--     <script type="text/javascript" src="/js/order/orderPopup.js?v=<%=System.currentTimeMillis() %>"></script> --%>
</head>
<body>
<div class="frame">
<div class="frame_wrap">
<%// gnb include %>
<%@ include file="/WEB-INF/views/common/incGnb.jsp"%> 
<div class="sub_conts">

<!-- #### Content area ############### -->
<article>

	<form name="frm" method="get" id="ajax-form" action="/order">

		<div class="tit_wrap">
			<h3><spring:message code="incgnb.order" text="주문" /></h3>
			<div class="sect_right">
				<div class="month">
					<input type="text" name="searchDateStart" class="date" maxlength="10" data-label="<spring:message code="order.seaech.date.start" text="검색일(시작일)" />" value="${search.searchDateStart}" data-required="Y" /><span>~</span>
					<input type="text" name="searchDateEnd" class="date" maxlength="10" data-label="<spring:message code="order.seaech.date.end" text="검색일(종료일)" />" value="${search.searchDateEnd}" data-required="Y" />
				</div>
				<select name="searchOrderStatus" id="searchOrderStatus">
					<option value="0">== 배송정보 ==</option>
					<option value="1">입력필요</option>
					<option value="2">입력완료</option>
				</select>
				<select name="searchOrder" class="select-ajax" id="searchOrder" data-code="${search.searchOrder}" data-codetype="etc" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A100000" }'></select>
				<div class="searchbox">
					<input type="text" name="searchWord" placeholder="<spring:message code="order.seaech.orderWord.label" text="검색어를 입력해주세요." />" value="${search.searchWord}">
					<button type="button" class="ic_search" ></button>
				</div>
			</div>
			<script>
				var _searchOrder = "${search.searchOrder}" ;
				if (_searchOrder > 0) {
					$("select#searchOrder").val(_searchOrder) ;
				}
				var _searchOrderStatus = "${search.searchOrderStatus}" ;
				if (_searchOrderStatus > 0) {
					$("select#searchOrderStatus").val(_searchOrderStatus) ;
				}
				var _searchWord = "${search.searchWord}" ;
				if (_searchWord && _searchWord.length > 0) {
					$("input#searchWord").val(_searchWord) ;
				}
			</script>
		</div>
		
		<div class="module_group">
			<div class="btn_group">
				<button type="button" class="btn_type1 bundle"><span><spring:message code="button.deliverySum" text="합배송" /></span></button>
				<button type="button" class="btn_type1 new"><span><spring:message code="button.deliveryAdd" text="배송생성" /></span></button>
				<button type="button" class="btn_type1 refresh"><span><spring:message code="button.reset" text="새로고침" /></span></button>
				<button type="button" class="btn_type1 down"><span><spring:message code="button.down" text="내려받기" /></span></button>
				<button type="button" class="btn_type1 del"><span><spring:message code="button.del" text="삭제" /></span></button>
			</div>
			<div class="action">
               <%-- 페이지 사이즈 와 국내/외 여부 선택 --%>
               <%@ include file="/WEB-INF/views/common/incPageDest.jsp"%>
			</div>
		</div>
	</form>
	
	<form name="sendForm" method="post" id="sendForm">
	<table class="tbtype">

		<colgroup>
			<col class="wp35">
			<col class="wp80">
			<col class="wp80">
			<col class="">
			<col class="wp50">
			<col class="wp100">
			<col class="wp100">
			<col class="wp60">
			<col class="wp80">
			<col class="wp120">
			<col class="wp80">
			<col class="wp100">
			<col class="wp100">
		</colgroup>

		<thead>
			<tr>
				<th><input type="checkbox" id="ind01" name="allCheck"><label for="ind01"><span class="icon_ck"></span></label></th>
				<th data-sort-name="shopName"><div><spring:message code="order.popup.address.shopName" text="쇼핑몰명" /></div></th>
				<th data-sort-name="orderNo"><div><spring:message code="order.orderNo" text="주문번호" /></div></th>
				<th data-sort-name="productName"><div><spring:message code="order.productName" text="제품명" /></div></th>
				<th data-sort-name="detailCnt"><div><spring:message code="order.itemnum" text="수량" /></div></th>
				<th><div><spring:message code="order.paymentAmount" text="결제금액" /></div></th> 
				<th data-sort-name="orderInfo"><div><spring:message code="order.orderInfo" text="주문자정보" /></div></th>
				<th data-sort-name="country"><div><spring:message code="order.country" text="배송국가" /></div></th> 
				<th data-sort-name="zipCode"><div><spring:message code="order.zipCode" text="우편번호" /></div></th> 
				<th data-sort-name="courier"><div><spring:message code="order.deliveryType" text="배송유형" /></div></th> <%-- <spring:message code="order.delivery" text="배송사" /> --%>
				<th><spring:message code="admin.statis.sales.title4" text="배송사" /></th>
				<th data-sort-name="orderDate"><div><spring:message code="order.orderDateTime" text="주문일시" /></div></th> 
				<th><div><spring:message code="order.orderStatus" text="배송정보" /></div></th>
			</tr>
		</thead>
		<tbody>
		<c:choose>
		
			<c:when test="${fn:length(list) == 0}">
				<tr>
					<td colspan="11" class="t_center">
						<spring:message code="common.title.notData" text="검색된 데이터가 없습니다." />
					</td>
				</tr>
			</c:when>
			
			<c:otherwise>
				<c:forEach items="${list}" var="list" varStatus="status">
					<c:set var="label" value="btn_state order_ing" />
					<c:set var="labelText"><spring:message code="order.label.ing" text="입력필요" /></c:set>
					<c:if test="${list.weight != 0 and list.payment != 0}">
						<c:set var="label" value="btn_state order_end" />
						<c:set var="labelText"><spring:message code="order.label.end" text="입력완료" /></c:set>
					</c:if>
					
					<tr data-code="${list.orderCode}" data-orderidx="${list.orderIdx}" data-shopidx="${list.shopIdx}" data-country="${list.buyerCountryCode}">
						<td>
							<input class="${label}" type="checkbox" id="ind02_${status.count}" name="orderIdxChk" value="${list.orderCode}" 
								data-ordername="${list.orderName}" data-shopidx="${list.shopIdx}" data-zipcode="${list.orderZipCode}"
								data-weight="${list.orderWeight}" data-country="${list.buyerCountryCode}" data-courier="${list.shippingLineName}">
							<label for="ind02_${status.count}"><span class="icon_ck" ></span></label>
						</td>
						<td class="t_center"><p>${list.shopName}</p></td>
						<td class="t_center" title="${list.orderCode}"><p>${list.orderName}</p></td>
						<c:set var="goodsText">${list.goods} <c:if test="${list.detailCnt > 1}">외 ${list.detailCnt-1}건</c:if></c:set>
						<td title="${goodsText}">
							<p class="el">${goodsText}</p>
						</td>
						<td class="t_center">
							<p>${list.totalQuantity}<c:if test="${list.detailCnt > 1}">(${list.detailCnt})</c:if></p>
						</td>
						<td class="t_right"><p><fmt:formatNumber value="${list.totalPrice}" pattern="#,###.##" /> <span class="tpu">${list.totalPriceCurrency}</span></p></td>
						<td class="t_center" title="${list.buyerLastname} ${list.buyerFirstname}"><p class="el">${list.buyerLastname} ${list.buyerFirstname}</p></td>
						<td class="t_center"><p>${list.buyerCountryCode}</p></td>
						<td class="t_center"><p>${list.buyerZipCode}</p></td>
						<td class="t_center" title="${list.shippingLineName}"><p class="el">${list.shippingLineName}</p></td>
						<td class="t_center" title="${list.companyName}"><p class="el">${list.companyName}</p></td>
						<td class="t_center" title="${list.orderDate}"><p>${fn:substring(list.orderDate, 0, 10)}</p></td>
						<td class="t_center" data-zipcode="${list.buyerZipCode}" data-weight="${list.weight}" data-payment="${list.payment}">
							<div class="btn_label ${label}">${labelText}</div>
						</td>
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
			<input type="hidden" name="searchDestType" value="${search.searchDestType}">
            <input type="hidden" name="searchCompany" value="${search.searchCompany}">
			<input type="hidden" name="searchDateStart" value="${search.searchDateStart}">
			<input type="hidden" name="searchDateEnd" value="${search.searchDateEnd}">
			<input type="hidden" name="searchType" value="${search.searchType}">
			<input type="hidden" name="searchWord" value="${search.searchWord}">
			<input type="hidden" name="searchPay" value="${search.searchPay}">
			<input type="hidden" name="pageSize" value="${search.pageSize}">
			<input type="hidden" name="currentPage" value="">
			<input type="hidden" name="sortOrder" value="${search.sortOrder}">
		</form>
	</div>
    
</article>
<!-- #### Content area ############### -->

</div> 
</div>
</div> 
<%// footer include %>
<%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
</body>
</html>