<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
                                  <select name="pageSize" id="pageSize" onchange="changPageSize(this.value)">
                                      <option value="10"<c:if test="${search.pageSize eq '10'}"> selected</c:if>>10<spring:message code="common.ea" text="개" /></option>
                                      <option value="50"<c:if test="${search.pageSize eq '50'}"> selected</c:if>>50<spring:message code="common.ea" text="개" /></option>
                                      <option value="100"<c:if test="${search.pageSize eq '100'}"> selected</c:if>>100<spring:message code="common.ea" text="개" /></option>
                                      <option value="500"<c:if test="${search.pageSize eq '500'}"> selected</c:if>>500<spring:message code="common.ea" text="개" /></option>             
                                  </select>                 
                                  <select name="destType" id="destType" onchange="changeDestType(this.value)">
                                      <option value="all"<c:if test="${search.searchDestType eq 'all'}"> selected</c:if>><spring:message code="common.dest.all" text="전체" /></option>
                                      <option value="overseas"<c:if test="${search.searchDestType eq 'overseas'}"> selected</c:if>><spring:message code="common.dest.overseas" text="해외" /></option>
                                      <option value="domestic"<c:if test="${search.searchDestType eq 'domestic'}"> selected</c:if>><spring:message code="common.dest.domestic" text="국내" /></option>
                                  </select> 
                                  <select class="select-ajax" name="searchCourier" id="searchCourier" onchange="changeCompany(this.value)" data-codetype="" data-code="${search.searchCompany}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B010000" }'>
                                  </select>  
                                  
                                  
                                                  