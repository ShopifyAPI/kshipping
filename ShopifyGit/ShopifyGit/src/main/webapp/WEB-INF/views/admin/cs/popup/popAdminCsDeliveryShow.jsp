<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>
<script>
</script>
<!-- 
**************************************************
화면코드 : 
기능설명 : 관리자CS관리 배송 상세보기(팝업)
Author   Date      Description
 김윤홍     2020-02-05  First Draft
**************************************************
 -->
				<div class="pop_head">
				    <h3><spring:message code="order.popup.delivary" text="배송정보" /></h3>
				    <button type="button" class="btn_close"></button>
				</div>
				<div class="pop_body">
                    <h4 class="sub_h4 mt20"><spring:message code="admin.order.orderInfo" text="주문/배송정보" /></h4>
                    
                    <!-- 주문/배송 정보 -->
                    <table class="tbtype2 mt10">
                    	<colgroup>
                            <col class="cper1300">
                            <col class="cperauto">
                            <col class="cper1300">
                            <col class="cperauto">
                        </colgroup>
                        <tbody>
                        	<tr>
                            	<th><p>주문번호</p></th>
                            	<td><p>${detail.orderName} [${detail.orderCode}]</p></td>
                            	<th><p>주문자명</p></th>
                            	<td><p>${detail.customerName}</p></td>
                            </tr>
                            <tr>
                            	<th><p>배송바코드</p></th>
                            	<td><p>${detail.masterCode}</p></td>
                            	<th><p>해외특송번호</p></th>
                            	<td><p>${detail.invoice}</p></td>
                            </tr>
                          
                          <c:if test="${detail.localCode != ''}">  
                            <tr>
                            	<th><p>국내픽업코드</p></th>
                            	<td><p>${detail.localCode}</p></td>
                            	<th><p>국내택배번호</p></th>
                            	<td><p>${detail.localInvoice}</p></td>
                            </tr>
                          </c:if>  
                            
                            <tr>
                            	<th><p>배송국가</p></th>
                            	<td><p>${detail.buyerCountryCode}</p></td>
                            	<th><p>배송서비스</p></th>
                            	<td><p>${detail.courierCode} [${detail.courierCompanyName}]</p></td>
                            </tr>
                            <tr>
                            	<th><p>배송비</p></th>
                            	<td><p><fmt:formatNumber value="${detail.totalPrice}" pattern="#,###" /></p></td>
                            	<th><p>상태</p></th>
                            	<td class=""><div class="btn_state ${detail.stateStrCss}">${detail.stateStr}</div></td>
                            </tr>
                            <tr>
                            	<th><p>배송접수일</p></th>
                            	<td><p>${detail.orderDate}</p></td>
                            	<th><p>결제일자</p></th>
                            	<td><p>${detail.paymentDate}</p></td>
                            </tr>
                       	</tbody>
                    </table>
                    <table class="tbtype2 mt10">
                    	<colgroup>
                            <col class="cper1300">
                            <col class="cperauto">
                            <col class="cper1300">
                            <col class="cperauto">
                        </colgroup>
                        <tbody>
                            <tr>
                            	<th><p>쇼핑몰</p></th>
                            	<td><p>${detail.shopName}</p></td>
                            	<th><p>사업자명</p></th>
                            	<td><p>${detail.company}</p></td>
                            </tr>
                            <tr>
                            	<th><p>판매자</p></th>
                            	<td><p>${detail.sellerName}</p></td>
                            	<th><p>연락처</p></th>
                            	<td><p>${detail.sellerPhone}</p></td>
                            </tr>
                            <tr>
                            	<th><p>판매자주소</p></th>
                            	<td colspan="3"><p>[${detail.sellerZipCode}] ${detail.sellerAddr1} ${detail.sellerAddr2}</p></td>              
                            </tr>
                        </tbody>
                    </table>
                    <table class="tbtype2 mt10">
                    	<colgroup>
                            <col class="cper1300">
                            <col class="cperauto">
                            <col class="cper1300">
                            <col class="cperauto">
                        </colgroup>
                        <tbody>
                            <tr>
                            	<th><p>수취인명</p></th>
                            	<td><p>${detail.buyerFirstname} ${detail.buyerLastname}</p></td>
                            	<th><p>연락처</p></th>
                            	<td><p>${detail.buyerPhone}</p></td>
                            </tr>
                            <tr>
                            	<th><p>수취인주소</p></th>
                            	<td colspan="3"><p>[${detail.buyerZipCode}] ${detail.buyerAddr1} ${detail.buyerAddr2}</p></td>                 
                            </tr>
                        </tbody>
                    </table>
                    
                   <h4 class="sub_h4 mt20"><spring:message code="order.popup.itemInfo" text="상품정보" /></h4>
                   <div class="scr_x mt10">

                        <table class="tbtype">
                            <colgroup>
                                <col class="wp120">
                                <col class="cperauto">
                                <col class="cperauto">                                
                                <col class="wp120">
                                <col class="wp80">
                                <col class="wp120">
                                <col class="wp120">
                                <col class="wp80">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th><spring:message code="settings.itemCode" text="상품코드" /></th>
                                    <th><spring:message code="settings.itemName" text="상품명" /></th>
                                    <th><spring:message code="settings.itemSku" text="SKU" /></th>
                                    <th><spring:message code="settings.weight" text="무게" /></th>
                                    <th><spring:message code="settings.itemQty" text="개수" /></th>
                                    <th><spring:message code="settings.hscode" text="HS CODE" /></th>
                                    <th><spring:message code="settings.itemPrice" text="가격" /></th>
                                    <th><spring:message code="settings.priceCurrency" text="화폐단위" /></th>
                                </tr>
                            </thead>
                            
                            <tbody>
                            <c:choose>
			                   <c:when test="${fn:length(list) == 0}">
			                       <tr>
                                    <td colspan="8" class="t_center"><p><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></p></td>
                                   </tr>			                   
			                   </c:when>
			                   <c:otherwise>
			                   
                            	<c:forEach items="${list}" var="item" varStatus="status">
                                <tr>
                                    <td class="t_center"><p>${item.goodsCode}</p></td>
                                    <td><p>${item.goods}</p></td>
                                    <td class="t_center"><p>${item.goodsSku}</p></td>
                                    <td class="t_center"><p><fmt:formatNumber value="${item.weight}" pattern="#,###" />${item.weightUnit}</p></td>
                                    <td class="t_center"><p>${item.quantity}</p></td>
                                    <td class="t_center"><p>${item.hscode}</p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.unitCost}" pattern="#,###" /></p></td>
                                    <td class="t_center"><p>${item.priceCurrency}</p></td>
                                </tr>
                            	</c:forEach>
                            
                            	</c:otherwise>
                    		</c:choose>
                            </tbody>
                        </table>
                        
                    </div>
                    
          		</div>