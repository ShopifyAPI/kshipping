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
 <div class="module_group">
                                                    <div class="btn_group">
                                                        <button type="button" class="btn_type2" id="btnCompanyUpdate"><spring:message code="admin.statis.button.compay.update" text="택배사정산" /></button>
                                                        <button type="button" class="btn_type2" id="btnSellerUpdate"><spring:message code="admin.statis.button.seller.update" text="셀러정산" /></button>
                                                    </div>
 </div> 
                                <table class="tbtype">
                                    <colgroup>
                                        <col class="wp35">
                                        <col class="wp100">
                                        <col class="wp150">
                                        <col class="wp200">
                                        <col class="wp150">
                                        <col class="wp100">
                                        <col class="wp70">
                                        <col class="wp70">
                                        <col class="wp90">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                    </colgroup>
                                    <thead> 
                                        <tr>
                                            <th rowspan="2"><input type="checkbox" id="ind01" name="allCheck"><label for="ind01"><span class="icon_ck" ></span></label></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title1" text="결재 일자" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title2" text="쇼핑몰 명" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title3" text="해외 송장번호" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title4" text="배송사" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title5" text="특송 서비스" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title6" text="배송국가" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title7" text="총무게" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title8" text="결제수단" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.note.state"   text="배송상태" /></th>
                                            <th style="background-color:#00cc00" rowspan="2"><spring:message code="admin.statis.note.comcalcul" text="택배사정산" /></th>
                                            <th style="background-color:#00ff00" rowspan="2"><spring:message code="admin.statis.note.sellercul" text="셀러정산" /></th>
                                            <th style="background-color:#66ccff"><strong><spring:message code="admin.statis.note.feestitle" text="공급단가" /></strong></th>
                                            <th style="background-color:#66ccff"><strong><spring:message code="admin.statis.total.subTitle4" text="부가세" /></strong></th>
                                            <th style="background-color:#66ccff"><strong><spring:message code="admin.statis.note.addtitle" text="추가요금" /></strong></th>
                                            <th style="background-color:#66ccff"><strong><spring:message code="admin.statis.note.feestotal" text="매출합계" /></strong></th>
                                            <th style="background-color:#ffcc00"><strong><spring:message code="admin.statis.note.salestitle" text="매입단가" /></strong></th>
                                            <th style="background-color:#ffcc00"><strong><spring:message code="admin.statis.note.salevat" text="매입부가세" /></strong></th>
                                            <th style="background-color:#ffcc00"><strong><spring:message code="admin.statis.note.addsalevat" text="매입추가요금" /></strong></th>
                                            <th style="background-color:#ffcc00"><strong><spring:message code="admin.statis.note.saletotal" text="매입합계" /></strong></th>
                                            <th style="background-color:#4dffa6"><strong><spring:message code="admin.statis.note.profit" text="이익금액" /></strong></th>
                                        </tr>
                                        <tr>
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumPayment}" pattern="#,###" />&nbsp;</p></th> <!-- 공급단가 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumPickupPrice}" pattern="#,###" />&nbsp;</p></th> <!-- 부가세/픽업금액 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumAddFeesPrice}" pattern="#,###" />&nbsp;</p></th> <!-- 추가요금 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumTotalFeesPrice}" pattern="#,###" />&nbsp;</p></th> <!-- 매출합계 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumSalePrice}" pattern="#,###" />&nbsp;</p></th> <!-- 매입단가 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumPaymentVatPrice}" pattern="#,###" />&nbsp;</p></th> <!-- 매입부가세 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumAddSalePrice}" pattern="#,###" />&nbsp;</p></th> <!-- 매입추가요금 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumTotalSalePrice}" pattern="#,###" />&nbsp;</p></th> <!-- 매입합계 -->
                                            <th class="t_right"><p class="total"><fmt:formatNumber value="${sum.sumTotalFeesPrice- sum.sumTotalSalePrice}" pattern="#,###" />&nbsp;</p></th> <!-- 이익금액 -->
                                        </tr>
                                    </thead>
                                    <tbody>
                            <c:choose>
                                <c:when test="${fn:length(list) == 0}">
                                        <tr>
                                            <td colspan="21" class="t_center"><p><spring:message code="common.title.notData" text="검색된 데이터가 없습니다." /></p></td>
                                        </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${list}" var="item" varStatus="status">
                                        <tr data-idx="${item.masterCode}">  
                                            <td><input type="checkbox" name="masterCode"  value="${item.masterCode}" id="ind02_${status.count}"><label for="ind02_${status.count}"><span class="icon_ck" ></span></label></td>
                                            <td class="t_center"><p>${item.paymentDate}</p></td>
                                            <td><p>${item.shopName}</p></td>
                                            <td class="t_center"><p>${item.masterCode}</p></td>
                                            <td class="t_center"><p>${item.courierCompanyName}</p></td>
                                            <td class="t_center"><p>${item.courierId}</p></td>
                                            <td class="t_center"><p>${item.buyerCountryCode}</p></td>
                                            <td class="t_right"><p>${item.totalWeight/1000}kg</p></td>  
                                            <td class="t_center"><p>${item.payType}</p></td>
                                            <td class="t_center"><p>${item.state}</p></td>
                                            <td class="t_center"><p>${item.companyCalculDesc}</p></td> <!-- 택배사정산여부 -->
                                            <td class="t_center"><p>${item.sellerCalculDesc}</p></td> <!-- 셀러정산여부 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.payment}" pattern="#,###" /></p></td> <!-- 공급단가 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.pickupPrice}" pattern="#,###" /></p></td> <!-- 부가세/픽업금액 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.addFeesPrice}" pattern="#,###" /></p></td> <!-- 추가요금 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.feesTotal}" pattern="#,###" /></p></td> <!-- 매출합계 -->
                                            <td class="t_right"><p><strong><fmt:formatNumber value="${item.salePrice}" pattern="#,###" /></strong></p></td> <!-- 매입단가 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.paymentVat}" pattern="#,###" /></p></td> <!-- 매입부가세 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.addSalePrice}" pattern="#,###" /></p></td> <!-- 매입추가요금 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.saleTotal}" pattern="#,###" /></p></td> <!-- 매입합계 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.feesTotal-item.saleTotal}" pattern="#,###" /></p></td> <!-- 이익금액 -->
                                        </tr>
                                                                            
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>                                    
                                    

                                    </tbody>
                                </table>