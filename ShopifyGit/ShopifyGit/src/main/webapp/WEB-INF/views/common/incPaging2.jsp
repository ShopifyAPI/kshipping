<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

                                <c:if test="${paging.prePage > 0}"><a href="#" onclick="gotoPage(${paging.prePage}); return false;" class="btn_prev"></a></c:if>
                                <c:forEach var="item" begin="${paging.startPageNum}" end="${paging.endPageNum}" step="1" varStatus="status">   
                                <a href="?currentPage=${status.index}" onclick="gotoPage(${status.index}); return false;" <c:if test="${status.index == paging.currentPage}">class="on"</c:if>>${status.index}</a>
                                </c:forEach>
                                <c:if test="${paging.nextPage > 0}"><a href="#" onclick="gotoPage(${paging.nextPage}); return false;" class="btn_next"></a></c:if>  
                                       
                                