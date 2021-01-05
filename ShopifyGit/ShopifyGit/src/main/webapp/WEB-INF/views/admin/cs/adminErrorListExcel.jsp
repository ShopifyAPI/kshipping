<%@ page language="java"
    contentType="application/vnd.ms-excel; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
    int row = 1;
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
    String today = format.format(new Date());
    String days = request.getParameter("days");
    String fileName = today + ".에러";
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
    <title><spring:message code="cs.error" text="에러" /></title>
</head>
<body>
    <div class="frame"> <!--프레임 영역 이 div 영역부터 붙여서 넣어주세요.-->
        <div class="frame_wrap">
            
            <%// gnb include %>

            <div class="sub_conts">
            <form name="frm" method="get" id="ajax-form" action="/order">
                <!-- #### Content area ############### -->
                <article>
                    <div class="tit_wrap">
                        <h3><spring:message code="cs.error" text="에러" /></h3>
                    </div> <!--tit_wrap end-->
                    <table class="tbtype">
                        <colgroup>
                            <col class="wp80"/>
                            <col class="wp120"/>
                            <col class="wp120"/>
                            <col class="wp120"/>
                            <col class="cperauto"/>
                            <col class="wp90"/>
                            <col class="wp90"/>
                            <col class="wp110"/>
                            <col class="wp100"/>
                            <col class="wp120"/>
                        </colgroup>
                        <thead>
                            <tr class=".list-detail">
                                <th><spring:message code="common.no" text="번호" /></th>
                                <th><spring:message code="settings.shopName" text="쇼핑몰명" /></th>
                                <th><spring:message code="shipment.masterCode" text="송장번호" /></th>
                                <th><spring:message code="order.orderNo" text="주문번호" /></th>
                                <th><spring:message code="shipment.productName" text="상품명" /></th>
                                <th><spring:message code="shipment.orderInfo" text="주문자정보" /></th>
                                <th><spring:message code="cs.deliveryCountry" text="배송국가" /></th>
                                <th><spring:message code="cs.globalCourier" text="해외특송사" /></th>
                                <th><spring:message code="shipment.shipmentDate" text="배송 접수일" /></th>
                                <th><spring:message code="order.orderStatus" text="상태" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            
                        <c:forEach items="${list}" var="list" varStatus="status">
                            
                            <tr>
                                <td class="t_center"><p><%=row++%></p></td>
                                <td class="t_center"><p>${list.shopName}</p></td>
                                <td class="t_center"><p>${list.masterCode}</p></td>
                                <td class="t_center"><p>${list.orderName}</p></td>
                                <td class="list-edit"><p>${list.goods}</p></td>
                                <td class="t_center list-edit"><p>${list.buyerFirstname}  ${list.buyerLastname}</p></td>
                                <td class="t_center list-edit"><p>${list.buyerCountryCode}</p></td>
                                <td class="t_center list-edit"><p>${list.courier}</p></td>
                                <td class="t_center list-edit"><p>${list.orderDate}</p></td>
                                <c:if test="${list.stateGroup != 'A030000'}">
                                    <td class="t_center list-edit"><div class="btn_state ${list.stateStrCss}">${list.stateStr}</div></td>
                                </c:if>
                                <c:if test="${list.stateGroup == 'A030000'}">       
                                    <td class="t_center list-edit"><div class="btn_state ${list.reasonStrCss}">${list.reasonStr}</div></td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        </tbody>    
                    </table>
                </article>
            </form>
            </div> 
        </div>
    </div> 
   
</body>
</html>
