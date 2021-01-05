<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 

<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>
    
    <title>Title</title>
	<script type="text/javascript">
	// READY
    $(function () {
        initialize();
    });
 
    var initialize = function () {
        initControls();
        bindEvent();
    };
    
    var initControls = function () {
    	
    };
    
    var bindEvent = function () {
        
    };
    
    var gotoPage = function(page) {
        $("#defaultSearchForm input[name='currentPage']").val(page);
        defaultSearchForm.submit();
    }
	</script>
</head>


<body>
    <div class="frame">
        <div class="frame_wrap">
            
            <%// gnb include %>
            <%@ include file="/WEB-INF/views/common/incGnb.jsp"%> 
        
            <div class="sub_conts">
            
                <!-- #### Content area ############### -->
                
                <article>
                    <div class="tit_wrap">
                        <h3><spring:message code="incgnb.dashboard" text="대시보드" /></h3>
                        <div class="sect_right">
                            <!-- <div  class="month"> 
                                <input type="text" class="date" />
                                <span>~</span>
                                <input type="text" class="date" />
                            </div> -->
                        </div>
                    </div> <!--tit_wrap end-->
                    <div class="dashbd">
                        <ul>
                            <li>
                                <h4><spring:message code="index.titie01" text="주문 현황" /></h4>
                                <div class="status">
                                    <p class="count">${orderData.orderDay} <spring:message code="common.unit.case" text="총" /></p>
                                    <p class="m_total">
                                        <spring:message code="index.nowMonth" text="이번달" /> <span><spring:message code="common.unit.total" text="총" /> <i>${orderData.orderMonth}</i> <spring:message code="common.unit.case" text="총" /></span>
                                    </p>
                                </div>
                            </li>
                            <li>
                                <h4><spring:message code="index.titie02" text="배송 현황" /></h4>
                                <div class="status">
                                    <p class="count">${orderData.shipmentDay} <spring:message code="common.unit.case" text="총" /></p>
                                    <p class="m_total">
                                        <spring:message code="index.nowMonth" text="이번달" /> <span><spring:message code="common.unit.total" text="총" /> <i>${orderData.shipmentMonth}</i> <spring:message code="common.unit.case" text="총" /></span>
                                    </p>
                                </div>
                            </li>
                            <li>
                                <h4><spring:message code="index.titie03" text="매출 현황" /></h4>
                                <div class="status">
                                    <p class="count">${orderData.salesDay} <spring:message code="common.unit.case" text="총" /></p>
                                    <p class="m_total">
                                        <spring:message code="index.nowMonth" text="이번달" /> <span><spring:message code="common.unit.total" text="총" /> <i>${orderData.salesMonth}</i> <spring:message code="common.unit.case" text="총" /></span>
                                    </p>
                                </div>
                            </li>
                        </ul>
                    </div><!-- dashbd end-->
                    <div class="tit_wrap mt20">
                        <h3><spring:message code="index.titie04" text="공지사항" /></h3>
                    </div>
                    <div class="tit_wrap mt10">
                        <ul class="tab">
                            <li id="kshipping" ><a href="/selectQna" ><spring:message code="notice.menuKshipping" text="K-Shipping" /></a></li>
                            <li id="post" class="on"><a href="/board/selectFaq"><spring:message code="notice.menuPost" text="배송사" /></a></li>
                        </ul>                
                        <div class="sect_right">
                            <p class="bd_count"><spring:message code="common.unit.total" text="총" /> ${paging.countNum}<spring:message code="common.unit.case" text="총" /></p>
                        </div>
                    </div> <!--tit_wrap end-->
                    
                    <table class="tbtype mt10">
                        <colgroup>
                            <col class="cper0780"/>
                            <col class="cper1460"/>
                            <col class="cperauto"/>
                            <col class="cper1460"/>
                        </colgroup>
                        <thead>
                            <tr>
                                <th><spring:message code="board.idx" text="번호" /></th> 
                                <th><spring:message code="board.part" text="구분" /></th>
                                <th><spring:message code="board.title" text="제목" /></th>
                                <th><spring:message code="board.regDate" text="등록일" /></th>
                            </tr>
                        </thead>
                        <tbody>
                        
                        <c:choose>
	                        <c:when test="${fn:length(list) == 0}">
	                        <tr>
                                <td class="t_center" colspan="4"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></td>
                            </tr>
	                        </c:when>
	                        <c:otherwise>
	                        
	                        <c:forEach items="${list}" var="item" varStatus="status">
	                        
                            <tr>
                                <td class="t_center"><p>${item.seq}</p></td>
                                <td class="t_center"><p>${item.group_name}</p></td>
                                <td><a href="#" onclick="return false;">${item.title}</a></td>
                                <td class="t_center"><p>${item.writeday}</p></td>
                            </tr>	    
                            
                            </c:forEach>                    
	                        
	                        </c:otherwise>
                        </c:choose>     
                            
                        </tbody>
                    </table>
                    <div class="pager">
                        <c:if test="${fn:length(list) > 0}">
                        <%@ include file="/WEB-INF/views/common/incPaging2.jsp"%>
                        </c:if>
                        <form name="defaultSearchForm" id="defaultSearchForm" method="get">
                        <input type="hidden" name="pageSize" value="${search.pageSize}">
                        <input type="hidden" name="currentPage" value="">
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
