<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 

<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%> 
    
    <title>Title</title>
	<script type="text/javascript" src="/js/paymentList.js"></script>
	<script type="text/javascript" src="/js/popPaymentPayment.js"></script>
  
</head>
<body>
    <div class="frame">
        <div class="frame_wrap">
        
        
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incGnb.jsp"%> 

    
            <div class="sub_conts">
                <article>
                
                <!-- #### Content area ############### -->
                
                    <!-- tit_wrap -->
                    <div class="tit_wrap">
                        <h3><spring:message code="incgnb.payment" text="결제" /></h3>
                         <div class="sect_right">
                        <!-- 기본 검색 영역 -->
                            <form name="searchform" id="searchform" method="get" action="/payment"> 
                                <select class="select-ajax" name="searchState" id="searchState" 
                                    data-code="${search.searchState}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A010000" }'></select>
                                <div  class="month">
                                    <input class="date" type="text" name="searchDateStart" value="${search.searchDateStart}" maxlength="10" 
                                        data-label="<spring:message code="order.seaech.date.start" text="검색일(시작일)" />"/>
                                    <span>~</span>
                                    <input class="date" type="text" name="searchDateEnd" value="${search.searchDateEnd}" maxlength="10" 
                                        data-label="<spring:message code="order.seaech.date.end" text="검색일(종료일)" />"/>
                                </div>
                                <select class="select-ajax" name="searchType" id="searchType" 
                                    data-codetype="etc" data-code="${search.searchType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A150000" }'></select>
                                <div class="searchbox">
                                    <button type="button" class="ic_search"></button>
                                    <input type="text" name="searchWord" id="searchWord" value="${search.searchWord}" placeholder="<spring:message code="order.seaech.orderWord.label" text="검색어를 입력해주세요." />">
                                </div>
                            </form>
                        <!--// 기본 검색 영역 -->
                        </div>
                    </div> 
                    
                    <ul class="tab">                        
                        <li class="on"><a href="/payment"><spring:message code="incgnb.payment" text="결제" /></a></li>
                        <li><a href="/shipment"><spring:message code="incgnb.shipment" text="배송" /></a></li>
                        <li><a href="/tracking"><spring:message code="incgnb.tracking" text="배송추적" /></a></li>
                    </ul> 

                    <ul class="tab_conts">
                        <li>                      

 
                    
                        <div class="module_group">
                            <div class="btn_group">
                                <button type="button" class="btn_type4 payment" id="btnPayment"><spring:message code="button.payment" text="결제하기" /></button>
                                <!--<button type="button" class="btn_type4 confirm"><span><spring:message code="button.payment.confirm" text="결제승인" /></span></button>-->
                                <button type="button" class="btn_type1 down"><span><spring:message code="button.down" text="내려받기" /></span></button>
                                <button type="button" class="btn_type1 del"><span><spring:message code="button.delete" text="삭제" /></span></button>
                            </div>
                        <div class="action">
                            <%-- 페이지 사이즈 와 국내/외 여부 선택 --%>
                            <%@ include file="/WEB-INF/views/common/incPageDest.jsp"%> 
                            
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
                                    <col class="wp35">
                                    <col class="cper0780"/>
                                    <col class="cper1300"/>
                                    <col class="cper0500"/>
                                    <col class="cperauto"/>
                                    <col class="cper1100"/>
                                    <col class="cper0500"/>
                                    <col class="cper0500"/>
                                    <col class="cper0780"/>
                                    <col class="cper1000"/>
                                </colgroup>
                                <thead>
                                    <tr>                                    
                                        <th><input type="checkbox" name="allCheck" value="Y" id="ind01"><label for="ind01"><span class="icon_ck" ></span></label></th>
                                        <th><div><spring:message code="settings.shopName" text="쇼핑몰명" /></div></th>
                                        <th><div><spring:message code="shipment.shipmentCode" text="배송바코드" /></div></th>
                                        <th data-sort-name="orderName"><div><spring:message code="order.orderNo" text="주문번호" /></div></th>
                                        <th data-sort-name="goods"><div><spring:message code="settings.itemName" text="상품명" /></div></th>
                                        <th><div><spring:message code="settings.orderInfo" text="주문자 정보" /></div></th>
                                        <th data-sort-name="buyerCountryCode"><div><spring:message code="order.country" text="국가" /></div></th>
                                        <th data-sort-name="companyName"><div><spring:message code="admin.statis.sales.title4" text="배송사" /></div></th>
                                        <th data-sort-name="orderDate"><div><spring:message code="order.orderDate" text="주문일자" /></div></th>
                                        <th data-sort-name="stateStr"><div><spring:message code="settings.status" text="상태" /></div></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    
                                <c:forEach items="${list}" var="item" varStatus="status">
                                    
                                    <tr>
                                        <td><input type="checkbox" name="chkmasterCode" data-statecode="${item.state}" data-state="${item.payState}" data-country="${item.buyerCountryCode}" data-companycode="${item.courierCompany}"  data-companyname="${item.companyName}" value="${item.masterCode}" id="ind02_${status.count}">
                                            <label for="ind02_${status.count}"><span class="icon_ck" ></span></label></td>
                                        <td class="t_center"><p>${item.shopName}</p></td>
                                        <td class="t_center" title="${item.invoice}">
                                            <p>
                                            <c:if test="${item.courierCompany =='B010020' }">${item.masterCode}</c:if>
                                            <c:if test="${item.courierCompany == 'B010030' }">${item.hblNo }</c:if>
                                            <c:if test="${item.combineCode =='Y'}"><strong style="color:red">ⓒ</strong></c:if></p>
                                        </td>
                                        <td class="t_center"><p>${item.orderName}</p></td>
                                        <td><p style="cursor : pointer" class="list-detail" data-code="${item.masterCode}" data-combinecode="${item.combineCode}">${item.goods} <c:if test="${item.goodsCnt > 0}">외 ${item.goodsCnt} 개</c:if></p></td>
                                        <td class="t_center"><p>${item.buyerFirstname}  ${item.buyerLastname}</p></td>
                                        <td class="t_center"><p>${item.buyerCountryCode}</p></td>
                                        <td class="t_center"><p class="ellip">${item.companyName}</p></td>
                                        <td class="t_center"><p>${item.orderDate}</p></td>
                                        <td class="t_center"><div class="btn_state ${item.stateStrCss}">${item.stateStr}</div></td>
                                    </tr>

                                </c:forEach>

                                </tbody>    
                            </table>
                            
                            
                        </c:otherwise>
                    </c:choose>
                             <input type="hidden" name="masterCode" value="">
                    </form>                                  
                  </li>
              </ul>
                        
                    <div class="pager">
                        <c:if test="${fn:length(list) > 0}">
                            <%@ include file="/WEB-INF/views/common/incPaging2.jsp"%>
                        </c:if>
                        
                        <form name="defaultSearchForm" id="defaultSearchForm" method="get">                                
                            <input type="hidden" name="searchDestType" value="${search.searchDestType}">
                            <input type="hidden" name="searchCompany" value="${search.searchCompany}">
                            <input type="hidden" name="searchState" value="${search.searchState}">
                            <input type="hidden" name="searchDateStart" value="${search.searchDateStart}">
                            <input type="hidden" name="searchDateEnd" value="${search.searchDateEnd}">
                            <input type="hidden" name="searchType" value="${search.searchType}">
                            <input type="hidden" name="searchWord" value="${search.searchWord}">
                            
                            <input type="hidden" name="pageSize" value="${search.pageSize}">
                            <input type="hidden" name="currentPage" value="">
                            <input type="hidden" name="sortOrder" value="${search.sortOrder}">
                        </form>
                    </div>

                <!--// #### Content area ############### -->        
                
                </article>
            </div>    
        </div>
    </div>    
   
    <%// footer include %>
    <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>

</body>
</html>