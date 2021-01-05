<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
<script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>

<script>
</script>
<!-- 
**************************************************
화면코드 :UI_SYS_322_POP 
기능설명 : 할인요금 등록(팝업)
Author   Date      Description
 김윤홍     2020-01-28  First Draft
**************************************************
 -->
<div class="pop_head">
    <h3>LOG</h3>
    <button type="button" class="btn_close"></button>
</div>

<div class="pop_body">
	
		<table class="tbtype">
	        
            <colgroup>
                <col class="cper2500">
                <col class="cperauto">
                <col class="cper1100">
                <col class="cperauto">
            </colgroup>
            <thead>
                <tr>
                    <th><spring:message code="admin.seller.regDate" text="변경일자" /> </th>
                    <th><spring:message code="admin.statis.search.title2" text="셀러" /></th>
                    <th><spring:message code="rank.rank" text="등급 rank.rank" /></th>
                    <th><spring:message code="admin.admin" text="관리자" /></th>
                </tr>
            </thead>
            <tbody>
            
            
        <c:choose>
            <c:when test="${fn:length(list) == 0}">
                <tr>
                    <td colspan="4" class="t_center">
                        <spring:message code="common.title.notData" text="검색된 데이터가 없습니다." />
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach items="${list}" var="item" varStatus="status">
            
                <tr>
                    <td class="t_center"><fmt:formatDate value="${item.regDate}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                    <td class="t_center">${item.email}</td>
                    <td class="t_center">${item.rankName}</td>
                    <td class="t_center">${item.editAdmin}</td>
                </tr>
                
                </c:forEach>  
            </c:otherwise>
        </c:choose>   
                
                
            </tbody>
        </table>

	</div>
</div>