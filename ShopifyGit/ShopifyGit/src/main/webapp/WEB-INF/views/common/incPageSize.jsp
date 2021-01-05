<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
                                            <select name="pageSize" id="pageSize" onchange="changPageSize(this.value)">
                                                <option value="10"<c:if test="${search.pageSize eq '10'}"> selected</c:if>>10<spring:message code="common.ea" text="개" /></option>
                                                <option value="50"<c:if test="${search.pageSize eq '50'}"> selected</c:if>>50<spring:message code="common.ea" text="개" /></option>
                                                <option value="100"<c:if test="${search.pageSize eq '100'}"> selected</c:if>>100<spring:message code="common.ea" text="개" /></option>
                                                <option value="500"<c:if test="${search.pageSize eq '500'}"> selected</c:if>>500<spring:message code="common.ea" text="개" /></option>             
                                            </select>                 