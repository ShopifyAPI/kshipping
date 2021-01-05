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
<!DOCTYPE html>
<html lang="ko">
<head>
<%// header include %>
<%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%>

<title>관리자 매출 통계</title>
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
                    <h2><spring:message code="admin.statis.menu.title3" text="매출 통계 조회" /></h2>
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
                                <col class="wp100">
                                <col class="wp60">
                                <col class="wp120">
                                <col class="wp80">
                                <col class="wp120">
                                <col class="wp60">
                                <col class="wp120">
                                <col class="wp70">
                                <col class="wp120">
                                <col class="wp60">
                                <col class="wp100">
                                <col class="wp80">
                                <col class="wp100">
                                <col class="wp60">
                                <col class="wp110">
                                <col class="wp70">
                                <col class="wp110">
                            </colgroup>
                            <thead> 
                                <tr class="multi">
                                    <th rowspan="3" class="back_d1"><spring:message code="admin.statis.total.title1" text="결제월" /></th>
                                    <th colspan="4" class="back_d1"><spring:message code="admin.statis.total.title2" text="총 매출합계" /></th>
                                    <th colspan="4" class="back_d2"><spring:message code="admin.statis.total.title3" text="해외 특송비" /></th>
                                    <th colspan="4" class="back_d5"><spring:message code="admin.statis.total.title4" text="국내 택배비" /></th>                       
                                    <th colspan="4" class="back_d6"><spring:message code="admin.statis.total.title5" text="추가요금" /></th>                       
                                </tr>
                                <tr class="multi">
                                    <th><spring:message code="admin.statis.total.subTitle1" text="건수" /></th>
                                    <th><spring:message code="admin.statis.total.subTitle2" text="판매금액" /></th>
                                    <th><spring:message code="admin.statis.total.subTitle4" text="부가세" /></th>
                                    <th><spring:message code="admin.statis.total.subTitle5" text="매출금액" /></th>
                                    <th class="back_d4"><spring:message code="admin.statis.total.subTitle1" text="건수" /></th>
                                    <th class="back_d4"><spring:message code="admin.statis.total.subTitle2" text="금액" /></th>
                                    <th class="back_d4"><spring:message code="admin.statis.total.subTitle4" text="부가세" /></th>
                                    <th class="back_d4"><spring:message code="admin.statis.total.subTitle5" text="합계금액" /></th>
                                    <th class="back_d52"><spring:message code="admin.statis.total.subTitle1" text="건수" /></th>
                                    <th class="back_d52"><spring:message code="admin.statis.total.subTitle2" text="금액" /></th>
                                    <th class="back_d52"><spring:message code="admin.statis.total.subTitle4" text="부가세" /></th>
                                    <th class="back_d52"><spring:message code="admin.statis.total.subTitle5" text="합계금액" /></th>
                                    <th class="back_d62"><spring:message code="admin.statis.total.subTitle1" text="건수" /></th>
                                    <th class="back_d62"><spring:message code="admin.statis.total.subTitle2" text="금액" /></th>
                                    <th class="back_d62"><spring:message code="admin.statis.total.subTitle4" text="부가세" /></th>
                                    <th class="back_d62"><spring:message code="admin.statis.total.subTitle5" text="합계금액" /></th>                          
                                </tr>
                                <tr class="multi" id="sumTotalArea">
                                    <c:set var="sumCount" value="0" />
                                    <c:set var="sumPayment" value="0" />
                                    <c:set var="sumRankPrice" value="0" />
                                    <c:set var="sumPaymentVat" value="0" />
                                    <c:set var="sumTotalPayment" value="0" />
                                    <c:set var="naCount" value="0" />
                                    <c:set var="naPayment" value="0" />
                                    <c:set var="naRankPrice" value="0" />
                                    <c:set var="naPaymentVat" value="0" />
                                    <c:set var="naTotalPayment" value="0" />
                                    <c:set var="ndCount" value="0" />
                                    <c:set var="ndPayment" value="0" />
                                    <c:set var="ndRankPrice" value="0" />
                                    <c:set var="ndPaymentVat" value="0" />
                                    <c:set var="ndTotalPayment" value="0" />
                                    <c:set var="ewCount" value="0" />
                                    <c:set var="ewPayment" value="0" />
                                    <c:set var="ewRankPrice" value="0" />
                                    <c:set var="ewPaymentVat" value="0" />
                                    <c:set var="ewTotalPayment" value="0" />
                                    
                                    <th class="t_right back_d3" id="sumCount">0</th>
                                    <th class="t_right back_d3" id="sumPayment">0</th>
                                    <th class="t_right back_d3" id="sumPaymentVat">0</th>
                                    <th class="t_right back_d3" id="sumTotalPayment">0</th>
                                    <th class="t_right back_d4 bold" id="naCount">0</th>
                                    <th class="t_right back_d4 bold" id="naPayment">0</th>
                                    <th class="t_right back_d4 bold" id="naPaymentVat">0</th>
                                    <th class="t_right back_d4 bold" id="naTotalPayment">0</th>
                                    <th class="t_right back_d52 bold" id="ndCount">0</th>
                                    <th class="t_right back_d52 bold" id="ndPayment">0</th>
                                    <th class="t_right back_d52 bold" id="ndPaymentVat">0</th>
                                    <th class="t_right back_d52 bold" id="ndTotalPayment">0</th>
                                    <th class="t_right back_d62 bold" id="ewCount">0</th>
                                    <th class="t_right back_d62 bold" id="ewPayment">0</th>
                                    <th class="t_right back_d62 bold" id="ewPaymentVat">0</th>
                                    <th class="t_right back_d62 bold" id="ewTotalPayment">0</th>
                                </tr>
                            </thead>
                            <tbody>
                            
                    <c:choose>
                        <c:when test="${fn:length(list) == 0}">
                                <tr>
                                    <td colspan="17" class="t_center"><p><spring:message code="common.title.notData" text="검색된 데이터가 없습니다." /></p></td>
                                </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${list}" var="item" varStatus="status">                            
                    
                                <tr>  
                                    <td class="t_center<c:if test="${search.searchDatetype != 'day'}"> list-view</c:if>" data-date="${item.paymentDate}">
                                        ${item.paymentDate}
                                    </td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.totalCount}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.sumPayment - item.sumRankPrice}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.sumPaymentVat}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><strong><fmt:formatNumber value="${item.sumTotalPayment}" pattern="#,###" /></strong></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.naCount}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.naPayment - item.naRankPrice}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.naPaymentVat}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><strong><fmt:formatNumber value="${item.naTotalPayment}" pattern="#,###" /></strong></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.ndCount}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.ndPayment - item.ndRankPrice}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.ndPaymentVat}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><strong><fmt:formatNumber value="${item.ndTotalPayment}" pattern="#,###" /></strong></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.ewCount}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.ewPayment - item.ewRankPrice}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><fmt:formatNumber value="${item.ewPaymentVat}" pattern="#,###" /></p></td>
                                    <td class="t_right"><p><strong><fmt:formatNumber value="${item.ewTotalPayment}" pattern="#,###" /></strong></p></td>
                                    
                                    <c:set var="sumCount" value="${sumCount + item.totalCount}"  />
                                    <c:set var="sumPayment" value="${sumPayment + item.sumPayment}"  />
                                    <c:set var="sumRankPrice" value="${sumRankPrice + item.sumRankPrice}"  />
                                    <c:set var="sumPaymentVat" value="${sumPaymentVat + item.sumPaymentVat}"  />
                                    <c:set var="sumTotalPayment" value="${sumTotalPayment + item.sumTotalPayment}"  />
                                    <c:set var="naCount" value="${naCount + item.naCount}"  />
                                    <c:set var="naPayment" value="${naPayment + item.naPayment}"  />
                                    <c:set var="naRankPrice" value="${naRankPrice + item.naRankPrice}"  />
                                    <c:set var="naPaymentVat" value="${naPaymentVat + item.naPaymentVat}"  />
                                    <c:set var="naTotalPayment" value="${naTotalPayment + item.naTotalPayment}"  />
                                    <c:set var="ndCount" value="${ndCount + item.ndCount}"  />
                                    <c:set var="ndPayment" value="${ndPayment + item.ndPayment}"  />
                                    <c:set var="ndRankPrice" value="${ndRankPrice + item.ndRankPrice}"  />
                                    <c:set var="ndPaymentVat" value="${ndPaymentVat + item.ndPaymentVat}"  />
                                    <c:set var="ndTotalPayment" value="${ndTotalPayment + item.ndTotalPayment}"  />
                                    <c:set var="ewCount" value="${ewCount + item.ewCount}"  />
                                    <c:set var="ewPayment" value="${ewPayment + item.ewPayment}"  />
                                    <c:set var="ewRankPrice" value="${ewRankPrice + item.ewRankPrice}"  />
                                    <c:set var="ewPaymentVat" value="${ewPaymentVat + item.ewPaymentVat}"  />
                                    <c:set var="ewTotalPayment" value="${ewTotalPayment + item.ewTotalPayment}"  />
                                </tr>
                                
                            </c:forEach>
                        </c:otherwise>
                    </c:choose> 
                                                  
                            </tbody>
                        </table>
                        
                        <input type="hidden" name="sumCount" value="<fmt:formatNumber value="${sumCount}" pattern="#,###" />" />
                        <input type="hidden" name="sumPayment" value="<fmt:formatNumber value="${sumPayment-sumRankPrice}" pattern="#,###" />" />
                        <input type="hidden" name="sumPaymentVat" value="<fmt:formatNumber value="${sumPaymentVat}" pattern="#,###" />" />
                        <input type="hidden" name="sumTotalPayment" value="<fmt:formatNumber value="${sumTotalPayment}" pattern="#,###" />" />
                        <input type="hidden" name="naCount" value="<fmt:formatNumber value="${naCount}" pattern="#,###" />" />
                        <input type="hidden" name="naPayment" value="<fmt:formatNumber value="${naPayment-naRankPrice}" pattern="#,###" />" />
                        <input type="hidden" name="naPaymentVat" value="<fmt:formatNumber value="${naPaymentVat}" pattern="#,###" />" />
                        <input type="hidden" name="naTotalPayment" value="<fmt:formatNumber value="${naTotalPayment}" pattern="#,###" />" />
                        <input type="hidden" name="ndCount" value="<fmt:formatNumber value="${ndCount}" pattern="#,###" />" />
                        <input type="hidden" name="ndPayment" value="<fmt:formatNumber value="${ndPayment-ndRankPrice}" pattern="#,###" />" />
                        <input type="hidden" name="ndPaymentVat" value="<fmt:formatNumber value="${ndPaymentVat}" pattern="#,###" />" />
                        <input type="hidden" name="ndTotalPayment" value="<fmt:formatNumber value="${ndTotalPayment}" pattern="#,###" />" />
                        <input type="hidden" name="ewCount" value="<fmt:formatNumber value="${ewCount}" pattern="#,###" />" />
                        <input type="hidden" name="ewPayment" value="<fmt:formatNumber value="${ewPayment-ewRankPrice}" pattern="#,###" />" />
                        <input type="hidden" name="ewPaymentVat" value="<fmt:formatNumber value="${ewPaymentVat}" pattern="#,###" />" />
                        <input type="hidden" name="ewTotalPayment" value="<fmt:formatNumber value="${ewTotalPayment}" pattern="#,###" />" />
                        
                        <form name="defaultSearchForm" id="defaultSearchForm" method="get">
                        <input type="hidden" name="searchMonthStart" value="${search.searchMonthStart}">
                        <input type="hidden" name="searchMonthEnd" value="${search.searchMonthEnd}">
                        <input type="hidden" name="searchCompany" value="${search.searchCompany}">
                        <input type="hidden" name="searchDate" value="">
                        </form>
                    </div>
                    
                    <div class="foot_btn"> 
	                    <c:if test="${search.searchDatetype == 'day'}">
	                        <button type="button" class="btn_type5" id="btn_List"><span><spring:message code="button.list" text="목록" /></span></button>
	                    </c:if> 
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