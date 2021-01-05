<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
 
<!-- 
**************************************************
화면코드 : 
기능설명 : 요약보기
Author   Date      Description
 조한두     2020-09-09  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
<head>
<%// header include %>
<%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%>

<title>관리자 손익 통계</title>
    <script type="text/javascript">
        $(function() {
            initialize();
        });
    
        var initialize = function() {
            initControls();
            bindEvent();
        };
    
        var initControls = function() {
            setTotalPayment();
        };
    
        var bindEvent = function() {            
            
            
        };
        
        var setTotalPayment = function() {
            
            $("#sumTotalArea th").each(function(){
                console.log($(this).attr("id"));
                num = $("input[name='"  + $(this).attr("id") + "']").val();
                $(this).text(num);
            });
        }
    </script> 
</head>
<body>    
 

                <div class="contents">
                    <div class="pop_head">
                    <h3><spring:message code="admin.statis.sale.popup.title" text="매출원장요약" /></h3>
                    <button type="button" class="btn_close"></button>
                    </div>
                <div class="pop_body">
                
                    <h4 class="sub_h4 mt20"><spring:message code="admin.statis.sale.popup.summary" text="매출정산/미정산" /></h4>
                    
                        
                    <div class="scr_x">    
                    <!-- 정산 정보 -->
                    <table class="tbtype">
                        
                        <colgroup>
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                        </colgroup>
                        <thead> 
                            <tr class="multi">
                                <th><spring:message code="admin.statis.sales.title2" text="쇼핑몰 명" /></th>
                                <th><spring:message code="admin.statis.sale.popup.paycount" text="실결제건수" /></th>
                                <th><spring:message code="admin.statis.total.subTitle7" text="실배송금액" /></th>
                                <th><spring:message code="admin.statis.sale.popup.company.price" text="택배사정산금액" /></th>
                                <th><spring:message code="admin.statis.sale.popup.seller.price" text="셀러정산금액" /></th>
                                <th><spring:message code="admin.statis.sale.popup.company.count" text="택배사정산건수" /></th>
                                <th><spring:message code="admin.statis.sale.popup.seller.count" text="셀러정산건수" /></th>
                            </tr>
                            <tr class="multi" id="sumTotalArea">
                                    <c:set var="payCount" value="0"  />        
                                    <c:set var="realPayment" value="0"  />
                                    <c:set var="calculCompanyPrice" value="0"  />
                                    <c:set var="calculSellerPrice" value="0"  />
                                    <c:set var="calculCompanyCount" value="0"  />
                                    <c:set var="calculSellerCount" value="0"  />
                                    
                                    <th class="t_center back_d4 bold" id="shop_name">TOT</th>              
                                    <th class="t_right back_d3" id="payCount">0</th>            
                                    <th class="t_right back_d3" id="realPayment">0</th>       
                                    <th class="t_right back_d3" id="calculCompanyPrice">0</th>          
                                    <th class="t_right back_d3" id="calculSellerPrice">0</th>       
                                    <th class="t_right back_d3" id="calculCompanyCount">0</th>       
                                    <th class="t_right back_d3" id="calculSellerCount">0</th>     
                            </tr>
                        </thead> 
                        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(list) == 0}">
                                        <tr>
                                            <td colspan="7" class="t_center"><p><spring:message code="common.title.notData" text="데이터가 없습니다." /></p></td>
                                        </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${list}" var="item" varStatus="status">
                                        
                                        <tr >  
                                            <td class="t_center"><p>${item.shopName}</p></td>
                                            <td class="t_right"><p>${item.payCount}</p></td>
                                            <td class="t_right"><p><fmt:formatNumber value="${item.realPayment}" pattern="#,###" /></p></td>  
                                            <td class="t_right"><p><fmt:formatNumber value="${item.calculCompanyPrice}" pattern="#,###" /></p></td>  
                                            <td class="t_right"><p><fmt:formatNumber value="${item.calculSellerPrice}" pattern="#,###" /></p></td>  
                                            <td class="t_right"><p>${item.calculCompanyCount}</p></td> 
                                            <td class="t_right"><p>${item.calculSellerCount}</p></td>
                                        </tr>
                                            <c:set var="payCount" value="${payCount+item.payCount}"  />
                                            <c:set var="realPayment" value="${realPayment+item.realPayment}"  />
                                            <c:set var="calculCompanyPrice" value="${calculCompanyPrice+item.calculCompanyPrice}"  />
                                            <c:set var="calculSellerPrice" value="${calculSellerPrice+item.calculSellerPrice}"  />
                                            <c:set var="calculCompanyCount" value="${calculCompanyCount+item.calculCompanyCount}"  />
                                            <c:set var="calculSellerCount" value="${calculSellerCount+item.calculSellerCount}"  />                                    
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            
                        </tbody>
                    </table>
                        <input type="hidden" name="payCount" value="<fmt:formatNumber value="${payCount}" pattern="#,###" />" />
                        <input type="hidden" name="realPayment" value="<fmt:formatNumber value="${realPayment}" pattern="#,###" />" />
                        <input type="hidden" name="calculCompanyPrice" value="<fmt:formatNumber value="${calculCompanyPrice}" pattern="#,###" />" />
                        <input type="hidden" name="calculSellerPrice" value="<fmt:formatNumber value="${calculSellerPrice}" pattern="#,###" />" />
                        <input type="hidden" name="calculCompanyCount" value="<fmt:formatNumber value="${calculCompanyCount}" pattern="#,###" />" />
                        <input type="hidden" name="calculSellerCount" value="<fmt:formatNumber value="${calculSellerCount}" pattern="#,###" />" /> 
                 </div>
                </div>
                </div>
</body>
</html>                