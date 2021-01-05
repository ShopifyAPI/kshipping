<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 

<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>
    <title>Title</title>
    <script type="text/javascript">
    </script>
</head>
<body>
    <%// gnb include %>
    <%@ include file="/WEB-INF/views/common/incGnb.jsp"%>
    
    <h1> <spring:message code="index.hello" text="hello text" />shopList.jsp!11</h1>
    
    <table border = "1" style="width:100%">
        <tr class="text-center">
            <th style="width: 5%">idx</th>
            <th style="width: 70%">Shop id</th>
            <th>지역</th>
            <th style="width: 12%">등록날짜</th>
        </tr>
        <tr th:each="list : ${shopList}">
            <td th:text="${list.shop_idx}"></td>
            <td><a th:href="@{/shopView(shop_idx=${list.shop_idx})}" 
                   th:text="${list.shop_id}"></a></td>
            <td th:text="${list.locale}"></td>
            <td th:text="${list.reg_date}"></td>
        </tr>
    </table>
    
    <%// footer include %>
	<%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
</body>
</html>