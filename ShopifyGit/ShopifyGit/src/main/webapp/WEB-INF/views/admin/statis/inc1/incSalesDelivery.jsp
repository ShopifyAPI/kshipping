<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
 
<!-- 
**************************************************
화면코드 :UI_SYS_541_BAS 
기능설명 : 관리자 목록 조회
Author   Date      Description
 조한두     2020-09-15  First Draft
**************************************************
 -->
  <form name="weightform" id="weightform" method="get"  >
 <div class="module_group">
                                                    <div class="btn_group">
                                                        <button type="button" class="btn_type1" id="btnCompanyAllSearch"><spring:message code="admin.statis.button.compay.true" text="택배사전체조회" /></button>
                                                        <button type="button" class="btn_type1" id="btnSellerAllSearch"><spring:message code="admin.statis.button.seller.true" text="셀러전체조회" /></button>
                                                        <button type="button" class="btn_type1" id="btnCompanySearch"><spring:message code="admin.statis.button.compay.false" text="택배사미정산조회" /></button>
                                                        <button type="button" class="btn_type1" id="btnSellerSearch"><spring:message code="admin.statis.button.seller.false" text="셀러미정산조회" /></button>
                                                        <button type="button" class="btn_type2" id="btnCompanyUpdate"><spring:message code="admin.statis.button.compay.update" text="택배사정산" /></button>
                                                        <button type="button" class="btn_type2" id="btnSellerUpdate"><spring:message code="admin.statis.button.seller.update" text="셀러정산" /></button>
                                                        <button type="button" class="btn_type4" id="btnCalculWeightUpload"><spring:message code="admin.statis.button.calculweight.upload" text="부피업로드" /></button>
                                                        <button type="button" class="btn_type4" id="btnCalculWeightUpdate"><spring:message code="admin.statis.button.calculweight.update" text="부피재계산" /></button>
                                                        <button type="button" class="btn_type4" id="btnCalculAll"><spring:message code="admin.statis.button.calculall.update" text="매출원장재정산" /></button>
                                                        <button type="button" class="btn_type4" id="btnCalculPayment"><spring:message code="admin.statis.button.calculpayment.update=" text="손익통계재정산" /></button>
                                                    </div>
 </div>
 
                                <table class="tbtype">
                                    <colgroup>
                                        <col class="wp35"> <!-- 체크박스 -->
                                        <col class="wp100"> <!-- 결제일자  -->
                                        <col class="wp150"> <!-- 쇼핑몰명  -->
                                        <col class="wp200"> <!-- 해외송장번호  -->
                                        <col class="wp150"> <!-- 배송사  -->
                                        <col class="wp100"> <!-- 특송서비스  -->
                                        <col class="wp70"> <!-- 배송국가  -->
                                        <col class="wp70"> <!-- 결제수단  -->
                                        <col class="wp90"> <!-- 배송상태  -->
                                        <col class="wp100"> <!-- 총무게  -->
                                        <col class="wp80"> <!-- 부피무게  -->
                                        <col class="wp100"> <!-- 택배사정산  -->
                                        <col class="wp100"> <!-- 셀러정산  -->
                                        <col class="wp100"> <!-- 공급단가  -->
                                        <col class="wp100"> <!-- 할인금액  -->
                                        <col class="wp100"> <!-- 국내픽업  -->
                                        <col class="wp100"> <!-- 부가세  -->
                                        <col class="wp100"> <!-- 실배송금액  -->
                                        <col class="wp100"> <!-- 추가요금  -->
                                        <col class="wp100"> <!-- 매출합계  -->
                                        <col class="wp100"> <!-- 매입단가  -->
                                        <col class="wp100"> <!-- 매입부가세  -->
                                        <col class="wp100"> <!-- 매입추가요금  -->
                                        <col class="wp100"> <!-- 매입합계  -->
                                        <col class="wp100"> <!-- 이익금액  -->
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
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title8" text="결제수단" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.note.state" text="배송상태" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.sales.title7" text="총무게" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.note.weight" text="부피무게" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.note.comcalcul" text="택배사정산" /></th>
                                            <th rowspan="2"><spring:message code="admin.statis.note.sellercul" text="셀러정산" /></th>
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
                                        
                                        <tr data-idx="${item.masterCode}">  
                                            <td><input type="checkbox" name="masterCode"  value="${item.masterCode}" id="ind02_${status.count}"><label for="ind02_${status.count}"><span class="icon_ck" ></span></label></td>
                                       
                                            <td class="t_center"><p>${item.paymentDate}</p></td>
                                            <td><p>${item.shopName}</p></td>
                                            <td class="t_center"><p>${item.masterCode}</p></td>
                                            <td class="t_center"><p>${item.courierCompanyName}</p></td>
                                            <td class="t_center"><p>${item.courierId}</p></td>
                                            <td class="t_center"><p>${item.buyerCountryCode}</p></td>
                                            <td class="t_center"><p>${item.payType}</p></td>
                                            <td class="t_center"><p>${item.state}</p></td>
                                            <td class="t_right"><input type="text" class="cper100p" name="totalweight" id="totalweight_${status.count}" value="${item.totalWeight/1000}" data-format="number" readOnly></td> <!-- 실중량 -->
                                            <td class="t_right"><input type="text" class="cper100p" name="volumeweight" id="weight_${status.count}" value="${item.weight}" data-format="number" numberOnly></td> <!-- 부피무게 -->  
                                            <td class="t_center"><p>${item.companyCalculDesc}</p></td> <!-- 택배사정산여부 -->
                                            <td class="t_center"><p>${item.sellerCalculDesc}</p></td> <!-- 셀러정산여부 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.payment}" pattern="#,###" /></p></td>                        <!-- 공급단가 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.rankPrice}" pattern="#,###" /></p></td>                      <!-- 할인금액 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.pickupPrice}" pattern="#,###" /></p></td>                    <!-- 픽업금액 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.paymentVat}" pattern="#,###" /></p></td>                     <!-- 부가세 -->
                                            <td class="t_right"><p><strong><fmt:formatNumber value="${item.totalPayment}" pattern="#,###" /></strong></p></td>  <!-- 실배송금액 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.addFeesPrice}" pattern="#,###" /></p></td>                   <!-- 추가요금 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.feesTotal}" pattern="#,###" /></p></td>                      <!-- 매출합계 -->
                                            <td class="t_right"><p><strong><fmt:formatNumber value="${item.salePrice}" pattern="#,###" /></strong></p></td>     <!-- 매입단가 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.saleVat}" pattern="#,###" /></p></td>                     <!-- 매입부가세 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.addSalePrice}" pattern="#,###" /></p></td>                   <!-- 매입추가요금 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.saleTotal}" pattern="#,###" /></p></td>                      <!-- 매입합계 -->
                                            <td class="t_right"><p><fmt:formatNumber value="${item.feesTotal-item.saleTotal}" pattern="#,###" /></p></td>       <!-- 이익금액 -->
                                             
                                            
 
                                        </tr>
                                                                            
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>

                                    </tbody>
                                </table>
</form>                             