<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
 
<!-- 
**************************************************
화면코드 : 
기능설명 : 손익통계
Author   Date      Description
 jwh     2020-03-12  First Draft
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
	        var $submitform = $('#submitform');
	        bindSelectAjax($submitform);
	        
	        setTotalPayment();
	    };
	
	    var bindEvent = function() {            
	        // 검색 클릭
	        $(document).on("click",".btn_search",function(){
	            search();
	        });
	        
	        // 상세보기
	        $(document).on("click",".list-view",function(){
	            viewDaily($(this).data("date"));
	        });
	     
	        // 목록으로
	        $(document).on("click","#btn_List",function(){
	            $("#defaultSearchForm").attr("action", "./statistics");
	            $("#defaultSearchForm").submit();
	        });
	        
	    };
	    
	    // 검색
	    var search = function(){
	        $("#submitform").submit();
	    } 
	    
	    // 상세보기 링크
	    var viewDaily = function(date){
	        $("#defaultSearchForm input[name='searchDate']").val(date);
	        $("#defaultSearchForm").attr("action", "./statisticsDaily");
	        
	        $("#defaultSearchForm").submit();
	    } 
	    
	    // 합계 세팅
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
	<div class="wrap">
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incAdmGnb.jsp"%> 

        <!-- #### Content area ############### -->

        <div class="contents">
           <div class="cont_body">
               <article>
                    <h2><spring:message code="admin.statis.menu.title1" text="손익 통계 조회" /></h2>
                    <form name="submitform" id="submitform" method="get">
                    <div class="module_group mt20">
                        <div class="btn_group" <c:if test="${search.searchDatetype == 'day'}"> style="display:none"</c:if>>
                            <div class="month">
                                <input class="date yymm" type="text" name="searchMonthStart" id="searchMonthStart" value="${search.searchMonthStart}" maxlength="10" 
                                    data-label="<spring:message code="order.seaech.date.start" text="검색일(시작일)" />" readonly/>
                                <span>~</span>
                                <input class="date yymm" type="text" name="searchMonthEnd" id="searchMonthEnd" value="${search.searchMonthEnd}" maxlength="10" 
                                    data-label="<spring:message code="order.seaech.date.end" text="검색일(종료일)" />"/>
                            </div>
                            <select class="select-ajax" id="searchCompany" name="searchCompany"
                                data-codetype="" data-code="${search.searchCompany}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B010000" }'></select>
                                                
                            <button type="button" class="btn_search"></button>
                        </div>
                    </div>
                    </form>
                    <div class="scr_x">
                        <table class="tbtype">
                            <colgroup>
                                <col class="wp80">
                                <col class="wp120">
                                <col class="wp90">
                                <col class="wp120">
                                <col class="wp120">
                                <col class="wp90">
                                <col class="wp120">
                                <col class="wp120">
                                <col class="wp90">
                                <col class="wp120">
                            </colgroup>
                            <thead> 
                                <tr class="multi">
                                    <th rowspan="3" class="back_d1"><spring:message code="admin.statis.total.title1" text="결재월" /></th>
                                    <th colspan="3" class="back_d1"><spring:message code="admin.statis.total.title6" text="매출통계" /></th>
                                    <th colspan="3" class="back_d1"><spring:message code="admin.statis.total.title7" text="매입통계" /></th>
                                    <th colspan="3" class="back_d2"><spring:message code="admin.statis.total.title8" text="손익통계" /></th>                       
                                </tr>
                                <tr class="multi"> 
                                    <th><spring:message code="admin.statis.total.subTitle2" text="금액" /></th>
                                    <th><spring:message code="admin.statis.total.subTitle4" text="부가세" /></th>                             
                                    <th><spring:message code="admin.statis.total.subTitle5" text="합계금액" /></th>
                                    <th><spring:message code="admin.statis.total.subTitle2" text="금액" /></th>
                                    <th><spring:message code="admin.statis.total.subTitle4" text="부가세" /></th>                             
                                    <th><spring:message code="admin.statis.total.subTitle5" text="합계금액" /></th>
                                    <th class="back_d4"><spring:message code="admin.statis.total.subTitle2" text="금액" /></th>
                                    <th class="back_d4"><spring:message code="admin.statis.total.subTitle4" text="부가세" /></th>                             
                                    <th class="back_d4"><spring:message code="admin.statis.total.subTitle5" text="합계금액" /></th>                            
                                </tr>
                                <tr class="multi" id="sumTotalArea">
                                    <c:set var="payment" value="0"  />
	                                <c:set var="paymentVat" value="0"  />
	                                <c:set var="totalPayment" value="0"  />
	                                <c:set var="salePrice" value="0"  />
	                                <c:set var="salePriceVat" value="0"  />
	                                <c:set var="totalSalePrice" value="0"  />
	                                <c:set var="sumPrice" value="0"  />
	                                <c:set var="sumPriceVat" value="0"  />
	                                <c:set var="sumTotalPrice" value="0"  />
                                    
                                    <th class="t_right back_d3" id="payment">0</th>
                                    <th class="t_right back_d3" id="paymentVat">0</th>
                                    <th class="t_right back_d3" id="totalPayment">0</th>
                                    <th class="t_right back_d3" id="salePrice">0</th>
                                    <th class="t_right back_d3" id="salePriceVat">0</th>
                                    <th class="t_right back_d3" id="totalSalePrice">0</th>
                                    <th class="t_right back_d4 bold" id="sumPrice">0</th>
                                    <th class="t_right back_d4 bold" id="sumPriceVat">0</th>
                                    <th class="t_right back_d4 bold" id="sumTotalPrice">0</th>
                                </tr>
                            </thead>
                            <tbody>
                            
                        <c:choose>
	                        <c:when test="${fn:length(list) == 0}">
                               <tr>
                                   <td colspan="10" class="t_center"><p><spring:message code="common.title.notData" text="검색된 데이터가 없습니다." /></p></td>
                               </tr>
	                        </c:when>
	                        <c:otherwise>
	                            <c:forEach items="${list}" var="item" varStatus="status">
                            
                                <tr>  
                                    <td class="t_center"><p>${item.paymentDate}</p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.payment}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.paymentVat}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><strong><fmt:formatNumber value="${item.totalPayment}" pattern="#,###" /></strong></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.salePrice}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.salePriceVat}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><strong><fmt:formatNumber value="${item.totalSalePrice}" pattern="#,###" /></strong></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.sumPrice}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.sumPriceVat}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><strong><fmt:formatNumber value="${item.sumTotalPrice}" pattern="#,###" /></strong></p></td>
                                </tr>
                                
                                <c:set var="payment" value="${payment + item.payment}"  />
                                <c:set var="paymentVat" value="${paymentVat + item.paymentVat}"  />
                                <c:set var="totalPayment" value="${totalPayment + item.totalPayment}"  />
                                <c:set var="salePrice" value="${salePrice + item.salePrice}"  />
                                <c:set var="salePriceVat" value="${salePriceVat + item.salePriceVat}"  />
                                <c:set var="totalSalePrice" value="${totalSalePrice + item.totalSalePrice}"  />
                                <c:set var="sumPrice" value="${sumPrice + item.sumPrice}"  />
                                <c:set var="sumPriceVat" value="${sumPriceVat + item.sumPriceVat}"  />
                                <c:set var="sumTotalPrice" value="${sumTotalPrice + item.sumTotalPrice}"  />
                                
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                                
                            </tbody>
                        </table>
                        
                        <input type="hidden" name="payment" value="<fmt:formatNumber value="${payment}" pattern="#,###" />" />
                        <input type="hidden" name="paymentVat" value="<fmt:formatNumber value="${paymentVat}" pattern="#,###" />" />
                        <input type="hidden" name="totalPayment" value="<fmt:formatNumber value="${totalPayment}" pattern="#,###" />" />
                        <input type="hidden" name="salePrice" value="<fmt:formatNumber value="${salePrice}" pattern="#,###" />" />
                        <input type="hidden" name="salePriceVat" value="<fmt:formatNumber value="${salePriceVat}" pattern="#,###" />" />
                        <input type="hidden" name="totalSalePrice" value="<fmt:formatNumber value="${totalSalePrice}" pattern="#,###" />" />
                        <input type="hidden" name="sumPrice" value="<fmt:formatNumber value="${sumPrice}" pattern="#,###" />" />
                        <input type="hidden" name="sumPriceVat" value="<fmt:formatNumber value="${sumPriceVat}" pattern="#,###" />" />
                        <input type="hidden" name="sumTotalPrice" value="<fmt:formatNumber value="${sumTotalPrice}" pattern="#,###" />" />
                        
                        <form name="defaultSearchForm" id="defaultSearchForm" method="get">
                        <input type="hidden" name="searchMonthStart" value="${search.searchMonthStart}">
                        <input type="hidden" name="searchMonthEnd" value="${search.searchMonthEnd}">
                        <input type="hidden" name="searchCompany" value="${search.searchCompany}">
                        </form>
                    </div>
                    
               </article>
               
           </div>
        </div>
           
        <!--// #### Content area ############### -->

    </div>

	<%// footer include%>
	<%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
	
</body>
</html>