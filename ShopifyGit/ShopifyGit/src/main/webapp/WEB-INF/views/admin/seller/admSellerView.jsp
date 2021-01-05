<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_SYS_112_BAS
기능설명 : 관리자 > 셀러정보 > 상세보기
Author   Date      Description
 jwh     2020-01-22  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 

    <title>셀러 정보 관리</title>
    
     <style type="text/css">
    #newRow td.discount input { text-align: right; }
    .tbtype tbody tr.historyRow td {height:20px;  font-size: 80%;  background-color: lightgrey;  color: blue;}
    </style>
    
    
    <script type="text/javascript" src="/js/admin/seller/admSellerView.js"></script>
    
</head>
<body>
    <div class="wrap">
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incAdmGnb.jsp"%> 

        <!-- #### Content area ############### -->

        <div class="contents">
           <div class="cont_body">
               <article>
                    <h2><spring:message code="admin.seller" text="셀러 정보 관리" /></h2>
                    
                    <!-- 기본 정보 등록 -->
                    <form name="submitform" id="submitform" method="post">
                    
	                    <div class="tit_wrap mt20">
	                        <h4 class="subtit"><spring:message code="admin.seller.subtitle1" text="기본 정보" /></h4>
	                    </div>
	                    
	                    <table class="tbtype">
	                        <colgroup>
	                            <col class="cper1250">
	                            <col class="cperauto">
	                            <col class="cper1250">
	                            <col class="cperauto">
	                        </colgroup>
	                        <tbody>
	                            <tr class="tline">
	                                <th>
	                                    <spring:message code="settings.name" text="이름" />
	                                </th>
	                                <td>
	                                    <p>${seller.firstName} ${seller.lastName}</p>
	                                    <c:set var="firstName"><spring:message code="settings.firstName" text="이름" /></c:set>
	                                    <c:set var="lastName"><spring:message code="settings.lastName" text="성" /></c:set>
	                                </td>
	                                <th>
	                                    <c:set var="companyNum"><spring:message code="settings.companyNum" text="사업자등록번호" /></c:set>
	                                    ${companyNum}
	                                </th>
	                                <td>
	                                    <p>${seller.companyNum}</p>
	                                </td>
	                            </tr>
	                            <tr>
	                                <th>
	                                    <c:set var="email"><spring:message code="settings.email" text="이메일" /></c:set>
	                                    ${email}
	                                </th>
	                                <td>
	                                    <p>${seller.email}</p>
	                                    <input type="hidden" class="cper5000" name="email" id="email" value="${seller.email}" 
	                                        readonly maxlength="50" data-required="Y" data-label="${email}" placeholder="${email}">
	                                </td>
	                                <th>
	                                    <c:set var="phoneNumber"><spring:message code="settings.phoneNumber" text="전화번호" /></c:set>
	                                    ${phoneNumber}
	                                </th>
	                                <td>
	                                    <p>${seller.phoneNumber}</p>
	                                </td>
	                            </tr>
	                            <tr>
	                                <th>
	                                    <spring:message code="settings.companyKname" text="회사명(국문)" />
	                                </th>
	                                <td>
	                                    <p>${seller.company}</p>
	                                </td>
	                                <th>
	                                    <spring:message code="settings.companyEname" text="회사명(영문)" />
	                                </th>
	                                <td>
	                                    <p>${seller.companyEname}</p>
	                                </td>
	                            </tr>
	                            <tr>
	                                <th>
	                                    <spring:message code="settings.paymentStatus" text="결제 방법" />
	                                </th>
	                                <td>
	                                     <select class="cper100p select-ajax changemon" name="paymentMethod" id="paymentMethod" 
							                        data-codetype="" data-code="${seller.paymentMethod}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "E030000" }' 
							                        data-required="Y" data-label="${paymentMethod}" >
							             </select>
	                                </td>                                
	                                <th>
	                                    <spring:message code="settings.shopStatus" text="셀러활동상태" />
	                                </th>
	                                <td>
	                                    <select class="cper100p select-ajax changemon" name="shopStatus" id="shopStatus" 
						                        data-codetype="" data-code="${seller.shopStatus}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "E020000" }' 
						                        data-required="Y" data-label="${shopStatus}" >
						                </select>      
	                                </td>                                
	                            </tr>
	                        </tbody>
	                    </table> 
	                    
                         <div class="scr_x">
                                <div class="h_scroll">
                                    <table class="tbtype">
                                    <colgroup>
                                            <col class="wp120">
                                            <col class="wp100">
                                            <c:forEach items="${courierList}" var="list">
                                                <col class="wp60">
                                            </c:forEach>
                                    </colgroup>
                                    <thead>
                                        <tr>
                                           <th rowspan="2"><spring:message code="price.startDate" text="적용시작일" /></th>
                                           <th rowspan="2"><spring:message code="price.endDate" text="적용종료일" /></th>
                                            <c:forEach items="${courierList}" var="courier">
                                                <c:if test="${courier.carrierCnt != 0}">
                                                    <th colspan="${courier.carrierCnt}">${courier.comName}</th>
                                                </c:if>
                                            </c:forEach>
                                        </tr>
                                        <tr id="courierRow">
	                                        <c:forEach items="${courierList}" var="courier" varStatus="status">
	                                                <th data-id="${courier.id}" data-code="${courier.code}">${courier.codeName}</th>
	                                        </c:forEach>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:if test="${discountListSize != 0 }">
	                                         <tr class="discountRow">
	                                             <td class="t_left"> ${sellerDiscount.startDate} </td>
	                                             <td class="t_left"> ${sellerDiscount.endDate} </td>
	                                             <c:forEach items="${sellerDiscount.array}" var="array">
	                                                <td class="t_right">${array}</td>
	                                             </c:forEach>
	                                         </tr>                                              
                                        </c:if>
                                         <tr id="newRow" style="display: none;">
                                             <td colspan="2">
                                                 <div class="month">
	                                                 <input class="date" type="text" name="startDate" id="startDate" value="${nowDate}" maxlength="10" 
	                                                       data-label="<spring:message code="order.seaech.date.nowdate" text="검색일" />"/>
                                                 </div>
                                             </td>
                                             <c:forEach items="${sellerDiscount.array}" var="array">
                                                <td class="discount">
<%--                                                     <input type="text" value="${array}" data-old="${array}"/> --%>
                                                    <input type="text" value="" data-old=""/>
                                                </td>
                                             </c:forEach>
                                         </tr> 
                                         <c:if test="${fn:length(discountHistory) > 0}">
                                            <c:forEach items="${discountHistory}" var="history">
		                                         <tr class="historyRow">
		                                             <td class="t_left"> ${history.startDate} </td>
		                                             <td class="t_left"> ${history.endDate} </td>
		                                             <c:forEach items="${history.array}" var="array">
		                                                <td class="t_right">${array}</td>
		                                             </c:forEach>
		                                         </tr>
	                                         </c:forEach>   
                                         </c:if>                                              
                                    </tbody>
                                </table>
                                </div>
                            </div>  
	                            
	                            
	                            
	                            
	                            
	                    <div class="foot_btn">
	                        <button type="button" class="btn_type2" id="btnAdd"><spring:message code="button.add" text="추가" /></button>
	                        <button type="button" class="btn_type3" id="btnSave"><spring:message code="button.save" text="저장" /></button>
	                    </div>
	
	                    <!--// 기본 정보 등록 -->
                    
                    </form>
                    
                    <!-- 판매 채널 연동 -->
                    <div class="tit_wrap mt20">
                        <h4 class="subtit"><spring:message code="admin.seller.subtitle2" text="쇼핑몰 정보" /></h4>
                    </div>
                    
                    
                            <table class="tbtype">
                                <colgroup>
                                    <col class="wp50"/>
		                            <col class="cper0780"/>
		                            <col class="cper0950"/>
		                            <col class="cperauto"/>
		                            <col class="cper0780"/>
		                            <col class="cper0780"/>
		                            <col class="cper0780"/>
		                            <col class="cper0780"/>
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th><spring:message code="settings.idx" text="번호" /></th>
                                        <th><spring:message code="settings.channel" text="이커머스" /></th>
                                        <th><spring:message code="settings.shopName" text="쇼핑몰명" /></th>
                                        <th><spring:message code="settings.url" text="URL" /></th>
                                        <th><spring:message code="settings.joinstatus" text="연동상태" /></th>
                                        <th><spring:message code="settings.billingtatus" text="결제모드" /></th>
                                        <th><spring:message code="settings.contactStatus" text="계약상태" /></th>
                                        <th><spring:message code="admin.seller.link" text="이동" /></th>
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
                                    <tr data-shopidx="${item.shopIdx}" data-useyn="${item.useYn}" data-billingyn="${item.billingYn}" data-activeyn="${item.activeYn}">
                                        <td class="t_center"><p>${status.count}</p></td>
                                        <td class="t_center"><p>${item.ecommerce}</p></td>
                                        <td class="t_center"><p>${item.shopName}</p></td>
                                        <td><a href="https://${item.shopName}.myshopify.com" target="_blank">${item.shopName}.myshopify.com</td>
                                        <td class="t_center"><p>${item.useYn}</p></td>
                                        
                                        <c:choose>	                                       
	                                        <c:when test="${item.billingYn == 'Y'}">
	                                        	<td class="t_center"><span class="btn_contect on use-check"><span></td>
	                                         </c:when>
	                                        <c:otherwise>
	                                        	<td class="t_center"><span class="btn_contect use-check"><span></td>
	                                         </c:otherwise>
                                        </c:choose>
                                        
                                        <c:choose>	                                       
	                                        <c:when test="${item.activeYn == 'Y'}">
	                                        	<td class="t_center"><span class="btn_label on contact-check"><span></td>
	                                         </c:when>
	                                        <c:otherwise>
	                                        	<td class="t_center"><span class="btn_label contact-check"><span></td>
	                                         </c:otherwise>
                                        </c:choose>                                        
                                    	<%-- <td class="t_center"><p>${item.activeYn}</p></td> --%>                                           
                                        <td class="t_center"><a href="https://${item.shopName}.myshopify.com" target="_blank" class="btn_type1"><spring:message code="admin.seller.link" text="이동" /></a></td>
                                    
                                    </tr>

                                </c:forEach>

                            </c:otherwise>
                        </c:choose>

                                </tbody>    
                            </table>
                            
                            
                        

                    <!--// 판매 채널 연동 -->
                    
                    <div class="foot_btn">
                        <button type="button" class="btn_type5" id="btn_list"><span><spring:message code="button.list" text="목록" /></span></button>
                        <form name="defaultSearchForm" id="defaultSearchForm" method="get" action="/admin/seller/list">
	                        <input type="hidden" name="searchRankId" value="${search.searchRankId}"/> 
	                        <input type="hidden" name="searchType" value="${search.searchType}"/>
	                        <input type="hidden" name="searchWord" value="${search.searchWord}"/>
	                        
	                        <input type="hidden" name="pageSize" value="${search.pageSize}"/>
	                        <input type="hidden" name="currentPage" value="${search.currentPage}"/>
	                        
	                        <input type="hidden" name="paymentMethodInitData" value="${seller.paymentMethod}"/>
	                        <input type="hidden" name="shopStatusInitData" value="${seller.shopStatus}"/>
                        </form>
                    </div>

               </article>
           </div>
        </div>
           
        <!--// #### Content area ############### -->

    </div>
                
   <%// footer include %>
   <%@ include file="/WEB-INF/views/common/incAdmFooter.jsp"%>
    
</body>
</html>
