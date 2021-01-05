<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
 
<!-- 
**************************************************
화면코드 : 
기능설명 : 손익통계
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
                                <col class="wp120">
                                <col class="wp90">
                                <col class="wp120">
                                
                                <col class="wp120">
                                <col class="wp120">
                                <col class="wp90">
                                <col class="wp120">
                                
                                <col class="wp120">
                                <col class="wp120">
                                <col class="wp90">
                                <col class="wp120">
                            </colgroup>
                            <thead> 
                                <tr class="multi">
                                    <th rowspan="4" class="back_d1"><spring:message code="admin.statis.total.title1" text="결재월" /></th>
                                    <th colspan="4" class="back_d1"><spring:message code="admin.statis.total.title6" text="매출통계" /></th>
                                    <th colspan="4" class="back_d1"><spring:message code="admin.statis.total.title7" text="매입통계" /></th>
                                    <th colspan="4" class="back_d2"><spring:message code="admin.statis.total.title8" text="손익통계" /></th>                       
                                </tr>
                                <tr class="multi"> 
                                    <th><spring:message code="admin.statis.total.subTitle2" text="금액" /></th>
                                    <th><spring:message code="admin.statis.note.addtitle" text="추가요금" /></th>
                                    <th><spring:message code="admin.statis.total.subTitle4" text="부가세" /></th>                             
                                    <th><spring:message code="admin.statis.total.subTitle5" text="합계금액" /></th>
                                    
                                    <th><spring:message code="admin.statis.total.subTitle2" text="금액" /></th>
                                    <th><spring:message code="admin.statis.note.addtitle" text="추가요금" /></th>
                                    <th><spring:message code="admin.statis.total.subTitle4" text="부가세" /></th>                             
                                    <th><spring:message code="admin.statis.total.subTitle5" text="합계금액" /></th>
                                    
                                    <th class="back_d4"><spring:message code="admin.statis.total.subTitle2" text="금액" /></th>
                                    <th class="back_d4"><spring:message code="admin.statis.note.addtitle" text="추가요금" /></th>
                                    <th class="back_d4"><spring:message code="admin.statis.total.subTitle4" text="부가세" /></th>                             
                                    <th class="back_d4"><spring:message code="admin.statis.total.subTitle5" text="합계금액" /></th>                            
                                </tr>
                                <tr class="multi" id="sumTotalArea">
                                    <c:set var="totalPayment" value="0"  />        
	                                <c:set var="addFeesPrice" value="0"  />
	                                <c:set var="paymentVat" value="0"  />
	                                <c:set var="feesTotal" value="0"  />
	                                
	                                <c:set var="salePrice" value="0"  />
	                                <c:set var="addSalePrice" value="0"  />
	                                <c:set var="saleVat" value="0"  />
	                                <c:set var="saleTotal" value="0"  />
	                               
	                                <c:set var="sumPrice" value="0"  />
	                                <c:set var="sumAddPrice" value="0"  />
	                                <c:set var="sumPriceVat" value="0"  />
	                                <c:set var="sumTotalPrice" value="0"  />
                                    
                                    <th class="t_right back_d3" id="totalPayment">0</th>        <!--  매출통계:금액 -->
                                    <th class="t_right back_d3" id="addFeesPrice">0</th>        <!--  매출통계:추가요금 -->
                                    <th class="t_right back_d3" id="paymentVat">0</th>          <!--  매출통계:부가세 -->    
                                    <th class="t_right back_d3" id="feesTotal">0</th>           <!--  매출통계:합계 -->
                                    
                                    <th class="t_right back_d3" id="salePrice">0</th>           <!--  매입통계:금액 -->
                                    <th class="t_right back_d3" id="addSalePrice">0</th>        <!--  매입통계:추가요금 -->    
                                    <th class="t_right back_d3" id="saleVat">0</th>             <!--  매입통계:부가세 -->
                                    <th class="t_right back_d3" id="saleTotal">0</th>           <!--  매입통계:합계 -->
                                    
                                    <th class="t_right back_d4 bold" id="sumPrice">0</th>       <!--  손익통계:금액 -->
                                    <th class="t_right back_d4 bold" id="sumAddPrice">0</th>    <!--  손익통계:추가요금 -->
                                    <th class="t_right back_d4 bold" id="sumPriceVat">0</th>    <!--  손익통계:부가세 -->
                                    <th class="t_right back_d4 bold" id="sumTotalPrice">0</th>  <!--  손익통계:합계 -->
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
                                    <td class="t_center"><p>${item.paymentDate}</p></td>                                                                <!-- 결제월 -->
                                    <td class="t_right"><p><fmt:formatNumber value="${item.totalPayment}" pattern="#,###" /></p></td>                   <!--  매출통계:금액 -->
                                    <td class="t_right"><p><fmt:formatNumber value="${item.addFeesPrice}" pattern="#,###" /></p></td>                   <!--  매출통계:추가요금 -->
                                    <td class="t_right"><p><fmt:formatNumber value="${item.paymentVat}" pattern="#,###" /></p></td>                     <!--  매출통계:부가세 -->
                                    <td class="t_right"><p><strong><fmt:formatNumber value="${item.feesTotal}" pattern="#,###" /></strong></p></td>     <!--  매출통계:합계 -->
                                    
                                    <td class="t_right"><p><fmt:formatNumber value="${item.salePrice}" pattern="#,###" /></p></td>                      <!--  매입통계:금액 -->    
                                    <td class="t_right"><p><fmt:formatNumber value="${item.addSalePrice}" pattern="#,###" /></p></td>                   <!--  매입통계:추가요금 -->    
                                    <td class="t_right"><p><fmt:formatNumber value="${item.saleVat}" pattern="#,###" /></p></td>                        <!--  매입통계:부가세 -->
                                    <td class="t_right"><p><strong><fmt:formatNumber value="${item.saleTotal}" pattern="#,###" /></strong></p></td>     <!--  매입통계:합계 -->
                                    
                                    <td class="t_right"><p><fmt:formatNumber value="${item.feesTotal-item.saleTotal}" pattern="#,###" /></p></td>       <!--  손익통계:금액 -->
                                    <td class="t_right"><p><fmt:formatNumber value="${item.addFeesPrice-item.addSalePrice}" pattern="#,###" /></p></td> <!--  손익통계:추가요금 -->
                                    <td class="t_right"><p><fmt:formatNumber value="${item.paymentVat-item.saleVat}" pattern="#,###" /></p></td>        <!--  손익통계:부가세 -->
                                                                                                                                                        <!--  손익통계:합계 -->
                                    <td class="t_right"><p><strong><fmt:formatNumber value="${(item.feesTotal-item.saleTotal)+(item.addFeesPrice-item.addSalePrice)+(item.paymentVat-item.saleVat)}" pattern="#,###" /></strong></p></td>                     
                                </tr>
                                
                                <c:set var="totalPayment" value="${totalPayment + item.totalPayment}"  />
                                <c:set var="addFeesPrice" value="${addFeesPrice + item.addFeesPrice}"  />
                                <c:set var="paymentVat" value="${paymentVat + item.paymentVat}"  />
                                <c:set var="feesTotal" value="${feesTotal + item.feesTotal}"  />
                                <c:set var="salePrice" value="${salePrice + item.salePrice}"  />
                                <c:set var="addSalePrice" value="${addSalePrice + item.addSalePrice}"  />
                                <c:set var="saleVat" value="${saleVat + item.saleVat}"  />
                                <c:set var="saleTotal" value="${saleTotal + item.saleTotal}"  />
                                <c:set var="sumPrice" value="${sumPrice + (item.feesTotal-item.saleTotal)}"  />
                                <c:set var="sumAddPrice" value="${sumAddPrice + (item.addFeesPrice-item.addSalePrice)}"  />
                                <c:set var="sumPriceVat" value="${sumPriceVat + (item.paymentVat-item.saleVat)}"  />
                                <c:set var="sumTotalPrice" value="${sumTotalPrice + (sumPrice)+(sumAddPrice)+(sumPriceVat)}"  />
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                                
                            </tbody>
                        </table>
                        
                        <input type="hidden" name="totalPayment" value="<fmt:formatNumber value="${totalPayment}" pattern="#,###" />" />
                        <input type="hidden" name="addFeesPrice" value="<fmt:formatNumber value="${addFeesPrice}" pattern="#,###" />" />
                        <input type="hidden" name="paymentVat" value="<fmt:formatNumber value="${paymentVat}" pattern="#,###" />" />
                        <input type="hidden" name="feesTotal" value="<fmt:formatNumber value="${feesTotal}" pattern="#,###" />" />
                        <input type="hidden" name="salePrice" value="<fmt:formatNumber value="${salePrice}" pattern="#,###" />" />
                        <input type="hidden" name="addSalePrice" value="<fmt:formatNumber value="${addSalePrice}" pattern="#,###" />" />
                        <input type="hidden" name="saleVat" value="<fmt:formatNumber value="${saleVat}" pattern="#,###" />" />
                        <input type="hidden" name="saleTotal" value="<fmt:formatNumber value="${saleTotal}" pattern="#,###" />" />
                        <input type="hidden" name="sumPrice" value="<fmt:formatNumber value="${sumPrice}" pattern="#,###" />" />
                        <input type="hidden" name="sumAddPrice" value="<fmt:formatNumber value="${sumAddPrice}" pattern="#,###" />" />
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