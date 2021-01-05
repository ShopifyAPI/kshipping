<%@ page language="java" contentType="application/vnd.ms-excel; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
    int row = 1;
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
    String today = format.format(new Date());
    String days = request.getParameter("days");
    String fileName = today + ".매출원장(해외))";
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
    <title><spring:message code="admin.statis.sales" text="매출 원장(해외)" /></title>
</head>
<body>
    <div class="frame"> <!--프레임 영역 이 div 영역부터 붙여서 넣어주세요.-->
        <div class="frame_wrap">
            <%// gnb include %>
            
            <div class="sub_conts">
                <!-- #### Content area ############### -->
                <article>
                    <div class="tit_wrap">
                        <h3><spring:message code="admin.statis.sales.delivery" text="해외특송" /></h3>
                    </div> <!--tit_wrap end-->
                    <table class="tbtype" border="1">
                        <colgroup>
                                        <col class="wp100"> <!--번호 -->
                                        <col class="wp100"> <!--결재일자 -->
                                        <col class="wp150"> <!--쇼핑몰명 -->
                                        <col class="wp200"> <!--해외송장번호 -->
                                        <col class="wp150"> <!--배송사 -->
                                        <col class="wp100"> <!--특송서비스ㅡ -->
                                        <col class="wp70"> <!--배송국가 -->
                                        <col class="wp70"> <!--결제수단 -->
                                        <col class="wp90"> <!-- 배송상태  -->
                                        <col class="wp90"> <!--총무게 -->
                                        <col class="wp100"> <!--부피무게 -->
                                        <col class="wp80"> <!--택배사정산 -->
                                        <col class="wp100"> <!--셀러정산 -->
                                        <col class="wp100"> <!--공급단가 -->
                                        <col class="wp100"> <!--할인금액 -->
                                        <col class="wp100"> <!-- 국내픽업  -->
                                        <col class="wp100"> <!--부가세 -->
                                        <col class="wp100"> <!--실배송금액 -->
                                        <col class="wp100"> <!--추가요금 -->
                                        <col class="wp150"> <!--매출합계 -->
                                        <col class="wp150"> <!--매입단가 -->
                                        <col class="wp150"> <!--매입부가세 -->
                                        <col class="wp100"> <!--매입추가요금 -->
                                        <col class="wp100"> <!--매입합계 -->
                                        <col class="wp100"> <!--이익금액 -->
                        </colgroup>
                        <thead> 
                             <tr>
                                            <th rowspan="2"><spring:message code="common.no" text="번호" /></th> 
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title1" text="결재 일자" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title2" text="쇼핑몰 명" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title3" text="해외 송장번호" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title4" text="배송사" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title5" text="특송 서비스" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title6" text="배송국가" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title8" text="결제수단" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.note.state" text="배송상태" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title7" text="총무게" /></th>
                                            <th rowspan="2" style="background-color:#FF3333" ><spring:message code="admin.statis.note.weight" text="부피무게" /></th>
                                            <th rowspan="2" style="background-color:#00cc00" ><spring:message code="admin.statis.note.comcalcul" text="택배사정산" /></th>
                                            <th rowspan="2" style="background-color:#00ff00" ><spring:message code="admin.statis.note.sellercul" text="셀러정산" /></th>
                                            <th style="background-color:#66ccff"><strong><spring:message code="admin.statis.note.feestitle" text="공급단가" /></strong></th>
                                            <th style="background-color:#66ccff"><strong><spring:message code="admin.statis.total.subTitle3" text="할인금액" /></strong></th>
                                            <th style="background-color:#66ccff"><strong><spring:message code="admin.statis.note.pickup" text="국내픽업" /></strong></th>
                                            <th style="background-color:#66ccff"><strong><spring:message code="admin.statis.note.vat" text="부가세" /></strong></th>
                                            <th style="background-color:#66ccff"><strong><spring:message code="admin.statis.total.subTitle7" text="실배송금액" /></strong></th>
                                            <th style="background-color:#66ccff"><strong><spring:message code="admin.statis.note.addtitle" text="추가요금" /></strong></th>
                                            <th style="background-color:#66ccff"><strong><spring:message code="admin.statis.note.feestotal" text="매출합계" /></strong></th>
                                            <th style="background-color:#ffcc00"><strong><spring:message code="admin.statis.note.salestotal" text="매입단가" /></strong></th>
                                            <th style="background-color:#ffcc00"><strong><spring:message code="admin.statis.note.salevat" text="매입부가세" /></strong></th>
                                            <th style="background-color:#ffcc00"><strong><spring:message code="admin.statis.note.addsalevat" text="매입추가요금" /></strong></th>
                                            <th style="background-color:#ffcc00"><strong><spring:message code="admin.statis.note.saletotal" text="매입합계" /></strong></th>
                                            <th style="background-color:#4dffa6"><strong><spring:message code="admin.statis.note.profit" text="이익금액" /></strong></th>
                            </tr>
                            <tr>
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumPayment}" pattern="#,###" />&nbsp;</p></th> <!-- 공급단가 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumRankPrice}" pattern="#,###" />&nbsp;</p></th> <!-- 할인금액 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumPickupPrice}" pattern="#,###" />&nbsp;</p></th> <!-- 픽업금액 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumPaymentVatPrice}" pattern="#,###" />&nbsp;</p></th> <!-- 부가세 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumTotalPayment}" pattern="#,###" />&nbsp;</p></th> <!-- 실배송금액 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumAddFeesPrice}" pattern="#,###" />&nbsp;</p></th> <!-- 추가요금 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumTotalFeesPrice}" pattern="#,###" />&nbsp;</p></th> <!-- 매출합계 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumSalePrice}" pattern="#,###" />&nbsp;</p></th> <!-- 매입단가 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumSaleVat}" pattern="#,###" />&nbsp;</p></th> <!-- 매입부가세 -->
                                             <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumAddSalePrice}" pattern="#,###" />&nbsp;</p></th> <!-- 매입추가요금 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumTotalSalePrice}" pattern="#,###" />&nbsp;</p></th> <!-- 매입합계 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumTotalFeesPrice- sum.sumTotalSalePrice}" pattern="#,###" />&nbsp;</p></th> <!-- 이익금액 -->

                           </tr>
                       </thead>
                       <tbody>
                            <c:choose>
                                <c:when test="${fn:length(list) == 0}">
                                    <tr>
                                           <td colspan="25" class="t_center"><p><spring:message code="common.title.notData" text="검색된 데이터가 없습니다." /></p></td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${list}" var="item" varStatus="status">
                                        <tr>
                                            <td class="t_center"><p><%=row++%></p></td>
                                            <td class="t_center"><p>${item.paymentDate}</p></td>
                                            <td><p>${item.shopName}</p></td>
                                            <td class="t_center" style='mso-number-format:"\@";'><p>${item.masterCode}</p></td>
                                            <td class="t_center"><p>${item.courierCompanyName}</p></td>
                                            <td class="t_center"><p>${item.courierId}</p></td>
                                            <td class="t_center"><p>${item.buyerCountryCode}</p></td>
                                            <td class="t_center"><p>${item.payType}</p></td>
                                            <td class="t_center"><p>${item.state}</p></td>
                                            <td class="t_right"><p><fmt:formatNumber value="${item.totalWeight}" pattern="#,###" /></p></td><!-- 실중량 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.weight*1000}" pattern="#,###" /></p></td> <!-- 부피무게 -->  
                                            <td class="t_center"><p>${item.companyCalculDesc}</p></td> <!-- 택배사정산여부 -->
                                            <td class="t_center"><p>${item.sellerCalculDesc}</p></td> <!-- 셀러정산여부 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.payment}" pattern="#,###" /></p></td> <!-- 공급단가 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.rankPrice}" pattern="#,###" /></p></td> <!-- 할인금액 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.pickupPrice}" pattern="#,###" /></p></td>                    <!-- 픽업금액 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.paymentVat}" pattern="#,###" /></p></td>                     <!-- 부가세 -->
                                            <td class="t_right"><p><strong><fmt:formatNumber value="${item.totalPayment}" pattern="#,###" /></strong></p></td>  <!-- 실배송금액 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.addFeesPrice}" pattern="#,###" /></p></td> <!-- 추가요금 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.feesTotal}" pattern="#,###" /></p></td> <!-- 매출합계 -->
                                            <td class="t_right"><p><strong><fmt:formatNumber value="${item.salePrice}" pattern="#,###" /></strong></p></td> <!-- 매입단가 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.saleVat}" pattern="#,###" /></p></td> <!-- 매입부가세 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.addSalePrice}" pattern="#,###" /></p></td> <!-- 매입추가요금 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.saleTotal}" pattern="#,###" /></p></td> <!-- 매입합계 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.feesTotal-item.saleTotal}" pattern="#,###" /></p></td> <!-- 이익금액 -->
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