<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
 
<!-- 
**************************************************
화면코드 :UI_SYS_321_BAS
기능설명 : AMD 요금 맵핑 관리
Author   Date      Description
 김윤홍     2020-01-20  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 

    <title>셀러별 할인율 조회</title>
    
    <script type="text/javascript" src="/js/admin/seller/admSellerDiscount.js?v=<%=System.currentTimeMillis() %>"></script>
</head>
<body>
    <div class="wrap">
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incAdmGnb.jsp"%>
        <div class="contents">
           <div class="cont_body">
               <article>
                    <div class="spinner">
                        <div class="loading"><img src="/img/common/loading.gif"></div>
                        <div class="dimmed"></div>
                    </div>    
                    <input type="hidden" id="gbn" value="${gbn}" /> 
                    <h2><spring:message code="admin.seller.discount" text="셀러별 할인율 조회"/></h2>
                    <ul class="tab_conts">
                        <li>
                    <form name="submitform" id="submitform" method="get">
                            <div class="module_group pt0">
<!--                                 <div class="sub_tab"> -->
                                    <div class="module_group ">
                                        <div class="btn_group">
                                            <div class="month">
                                                 <input class="date" type="text" name="nowDate" id="nowDate" value="${nowDate}" maxlength="10" 
                                                    data-label="<spring:message code="order.seaech.date.nowdate" text="검색일" />"/>
                                            </div>

                                            <select class="select-ajax" id="partShipCompany" name="partShipCompany"
                                                data-codetype="" data-code="${partShipCompany}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B010000" }'>
                                            </select>
                                            <select class="" id="partDeliveryService" name="partDeliveryService"
                                                data-codetype="etc" data-code="${partDeliveryService}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B040000" }'>
                                            </select>
                                            
                                             <select class="select-ajax" name="searchType"  id="searchType" 
                            				   data-codetype="etc" data-code="${search.searchType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A140000" }'></select>
                            				<div class="searchbox">
				                            
				                                <input type="text" name="searchWord" id="searchWord" value="${search.searchWord}" placeholder="<spring:message code="common.searchWord" text="검색어" />">
				                                </div>
                                            
                                            <button type="button" id="btn_search" class="btn_type2" ><spring:message code="button.search" text="조회" /></button>  
                                            <button type="button" id="downExcel"  class="btn_type1 down" ><span><spring:message code="button.down.all" text="내려받기" /></span></button>
                                        </div>
                                    </div>
<!--                                 </div> -->
                            </div>
                            <div class="scr_x">
                                <div class="h_scroll" style="height:515px;">
                                    <table class="tbtype">
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
                                </div>
                            </div>  
                    </form>
                        </li>
                    </ul>
                    
               </article>
           </div>
        </div>
    </div>
</body>
</html>