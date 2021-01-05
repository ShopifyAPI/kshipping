<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
						<c:choose>
							<c:when test="${pageSize == 0}">
							<a href="#" class="btn_prev"></a>
							<a href="#" class="btn_next"></a>
							</c:when>
							<c:otherwise>
								<a href="#" class="btn_prev"></a>
								<c:forEach items="${pageList}" var="list">
								<a href="#" onclick="gotoPage('"${list.page}"')"  class="on">${list.page}</a>
								</c:forEach> 
								<a href="#" class="btn_next"></a> 
							</c:otherwise>
						</c:choose>   
