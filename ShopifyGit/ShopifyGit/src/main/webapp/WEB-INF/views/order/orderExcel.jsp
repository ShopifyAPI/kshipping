<%@ page language="java"
    contentType="application/vnd.ms-excel; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
    String today = format.format(new Date());
    String days = request.getParameter("days");
    String fileName = today + ".주문";
      response.setHeader("Content-Type", "application/vnd.ms-xls");
    response.setHeader("Content-Disposition",
            "attachment; filename=" + new String((fileName).getBytes("KSC5601"), "8859_1") + ".xls");
%>
<%@ include file="/WEB-INF/views/common/incSysExcel.jsp"%>  
<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeaderExcel.jsp"%>
    <title><spring:message code="incgnb.order" text="주문" /></title>
</head>
<body>
    <div class="frame"> <!--프레임 영역 이 div 영역부터 붙여서 넣어주세요.-->
        <div class="frame_wrap">
            
            <%// gnb include %>

            <div class="sub_conts">
                <!-- #### Content area ############### -->
				<article>
					<div class="tit_wrap">
						<h3><spring:message code="incgnb.order" text="주문" /></h3>
					</div> <!--tit_wrap end-->
					<table class="tbtype" border="1">
						<colgroup>
                            <col class="cper1100"/>
                            <col class="cper1300"/>
                            <col class="cper1500"/>
                            <col class="cper0900"/>
                            <col class="wp60"/>
                            <col class="cper0900"/>
                            <col class="cper0780"/>
                            <col class="wp150"/>
						</colgroup>
						<thead>
							<tr>
                                <th><spring:message code="order.popup.address.shopName" text="쇼핑몰 명" /></th>
                                <th><spring:message code="order.orderNo" text="주문번호" /></th>
                                <th>[<spring:message code="order.list.title.shopname" text="상점명"  />] <spring:message code="order.productName" text="제품명" /></th>
                                <th><spring:message code="order.orderInfo" text="주문자정보" /></th>
                                <th><spring:message code="cs.deliveryCountry" text="배송국가" /></th>  
                                <th><spring:message code="order.paymentAmount" text="결제금액" /></th> 
                                <th><spring:message code="settings.priceCurrency" text="화폐단위" /></th>   
                                <th><spring:message code="order.orderDate" text="주문일자" /></th>  
							</tr>
						</thead>
						<tbody>
						<c:choose>
							<c:when test="${fn:length(list) == 0}">
						<tr>
							<td colspan="9"  class="t_center"><spring:message code="common.title.notData" text="검색된 데이터가 없습니다." /></td>
						</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${list}" var="list">
									<tr>
			                            <td class="t_center"><p>${list.shopName}</p></td>
			                            <td style="mso-number-format:'\@'"><p>${list.orderName}</p></td>
			                            <td><p>${list.goods}</p></td>
			                            <td class="t_center"><p>${list.buyerLastname} ${list.buyerFirstname}</p></td>
			                            <td class="t_center"><p>${list.buyerCountryCode}</p></td>
			                            <td class="t_right"><p><fmt:formatNumber value="${list.price}" pattern="#,###" /></p></td>
			                            <td class="t_center"><p>${list.totalPriceCurrency}</p></td>
			                            <td class="t_center"><p>${list.orderDate}</p></td>
			                        </tr>
								</c:forEach>  
							</c:otherwise>
						</c:choose>
						
						</tbody>
					</table>
				</article>
			</div> 
		</div>
	</div> 
   
</body>
</html>
