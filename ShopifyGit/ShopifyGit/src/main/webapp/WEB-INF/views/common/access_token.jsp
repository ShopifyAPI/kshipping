<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>    
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <script src="https://cdn.shopify.com/s/assets/external/app.js"></script>
</head>
<body>
    <%-- <h1>access_token : ${orderList} </h1> --%>
    <h1>status : ${status} </h1>
    <h1>message : ${message} </h1>
    <table border = "1" style="width:100%">
		<tr class="text-center">
		<th style="width: 5%">idx</th>
		<th style="width: 70%">token</th>
		<th>email</th>
		<th style="width: 12%">등록날짜</th>
		</tr>
		<c:forEach var="items" items="${access_token}">
			<p><c:out value="${items.id}" /></p>
			<p><c:out value="${items.token}" /></p>
			<p><c:out value="${items.email}" /></p>
			<p><c:out value="${items.updated_at}" /></p>
		</c:forEach>
	</table>
</body>
</html>