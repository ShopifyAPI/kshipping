<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
 
<!-- 
**************************************************
화면코드 :UI_SYS_541_BAS 
기능설명 : 관리자 목록 조회
Author   Date      Description
 김윤홍     2020-01-06  First Draft
**************************************************
 -->
                                <table class="tbtype">
                                    <colgroup>
                                        <col class="wp100">
                                        <col class="wp150">
                                        <col class="wp200">
                                        <col class="wp150">
                                        <col class="wp100">
                                        <col class="wp70">
                                        <col class="wp70">
                                        <col class="wp90">
                                        <col class="wp100">
                                        <col class="wp80">
                                        <col class="wp70">
                                        <col class="wp100">
                                    </colgroup>
                                    <thead> 
                                        <tr>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title1" text="결재 일자" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title2" text="쇼핑몰 명" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title3" text="해외 송장번호" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title4" text="배송사" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title5" text="특송 서비스" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title6" text="배송국가" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title7" text="총무게" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title8" text="결제수단" /></th>
                                            <!--  
                                            <th><strong><spring:message code="admin.statis.total.subTitle6" text="공시금액" /></strong></th>
                                            <th><strong><spring:message code="admin.statis.total.subTitle3" text="할인금액" /></strong></th>
                                            <th><strong><spring:message code="admin.statis.total.subTitle4" text="부가세" /></strong></th>
                                            -->
                                            <th><strong><spring:message code="admin.statis.total.subTitle5" text="매출금액" /></strong></th>
                                        </tr>
                                        <tr>
                                            <!-- <th class="t_right" colspan="8"><p class="total">총 합계&nbsp;</p></th> -->
                                            <!--  
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumPayment}" pattern="#,###" />&nbsp;</p></th>
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumRankPrice}" pattern="#,###" />&nbsp;</p></th>
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumPaymentVat}" pattern="#,###" />&nbsp;</p></th>
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumTotalPayment}" pattern="#,###" />&nbsp;</p></th>
                                            -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumPickupPrice}" pattern="#,###" />&nbsp;</p></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    
                            <c:choose>
                                <c:when test="${fn:length(list) == 0}">
                                        <tr>
                                            <td colspan="9" class="t_center"><p><spring:message code="common.title.notData" text="검색된 데이터가 없습니다." /></p></td>
                                        </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${list}" var="item" varStatus="status">
                                        
                                        <tr>  
                                            <td class="t_center"><p>${item.paymentDate}</p></td>
                                            <td><p>${item.shopName}</p></td>
                                            <td class="t_center"><p>${item.masterCode}</p></td>
                                            <td class="t_center"><p>${item.courierCompanyName}</p></td>
                                            <td class="t_center"><p>${item.courierId}</p></td>
                                            <td class="t_center"><p>${item.buyerCountryCode}</p></td>
                                            <td class="t_right"><p>${item.totalWeight/1000}kg</p></td>  
                                            <td class="t_center"><p>${item.payType}</p></td>
                                            <!--  
                                            <td class="t_right"><p><fmt:formatNumber value="${item.payment}" pattern="#,###" /></p></td>
                                            <td class="t_right"><p><fmt:formatNumber value="${item.rankPrice}" pattern="#,###" /></p></td>
                                            <td class="t_right"><p><fmt:formatNumber value="${item.paymentVat}" pattern="#,###" /></p></td>
                                            <td class="t_right"><p><strong><fmt:formatNumber value="${item.totalPayment}" pattern="#,###" /></strong></p></td>
                                            -->
                                            <td class="t_right"><p><strong><fmt:formatNumber value="${item.pickupPrice}" pattern="#,###" /></strong></p></td>
                                        </tr>
                                                                            
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>                                    
                                    

                                    </tbody>
                                </table>