<%@ page language="java"
    contentType="application/vnd.ms-excel; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.text.SimpleDateFormat"%>
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
                        <h3><spring:message code="admin.seller.discount" text="셀러별 할인율" /></h3>
                    </div> <!--tit_wrap end-->
                    <table class="tbtype" border="1">
                        <colgroup>
                            <col class="wp200">
                            <col class="wp100">
                            <col class="wp200">
                            <c:forEach items="${courierList}" var="list">
                                <col class="wp60">
                            </c:forEach>
                        </colgroup>
                        <thead>
                             <tr>
                                <th rowspan="2"><spring:message code="admin.pop.email" text="이메일" /></th>
                                <th rowspan="2"><spring:message code="admin.seller.shop.name" text="쇼핑몰 이름" /></th>
                                <th rowspan="2"><spring:message code="admin.seller.shop.domain=" text="도메인" /></th>
                                 <c:forEach items="${courierList}" var="courier">
                                     <c:if test="${courier.carrierCnt != 0}">
                                         <th colspan="${courier.carrierCnt}">${courier.comName}</th>
                                     </c:if>
                                 </c:forEach>
                             </tr>
                             <tr>
                             <c:forEach items="${courierList}" var="courier">
                                     <th>${courier.codeName}</th>
                             </c:forEach>
                             </tr>
                        </thead>
                        <tbody>
	                        <c:choose>
	                            <c:when test="${fn:length(sellerDiscountList) == 0}">
	                                <c:set var="colsize" scope="session" value="${fn:length(courierList) + 3}"/>  
			                        <tr>
			                            <td colspan="${colsize}"  class="t_center"><spring:message code="common.title.notData" text="검색된 데이터가 없습니다." /></td>
			                        </tr>
	                            </c:when>
	                            <c:otherwise>
	                                <c:forEach items="${sellerDiscountList}" var="seller">
	                                     <tr>
	                                         <td class="t_left">${seller.email}</td>
	                                         <td class="t_left">${seller.allShopNames}</td>
	                                         <td class="t_left">${seller.allDomains}</td>
	                        
	                                         <c:forEach items="${seller.array}" var="array">
	                                            <td class="t_right"> 
	                                                ${array}
	                                            </td>                                                   
	                                         </c:forEach>
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
