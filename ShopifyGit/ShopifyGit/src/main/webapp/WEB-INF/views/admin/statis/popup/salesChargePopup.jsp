<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>
<script>
</script>
<%
    int row = 1;
%>
<!-- 
**************************************************
화면코드 : 
기능설명 : 매출원장>초과요금 사유 상세보기(팝업)
Author   Date      Description
 조한두     2020-08-25  First Draft
**************************************************
 -->
                <div class="pop_head">
                    <h3><spring:message code="admin.statis.popup.title" text="추가요금상세보기" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                            <c:choose>
                                <c:when test="${mode == 'EW'}">
                                                           <h4 class="sub_h4 mt20"><spring:message code="admin.statis.popup.ewtitle" text="매출추가요금" /></h4>
                                </c:when>
                                <c:otherwise>
                                                           <h4 class="sub_h4 mt20"><spring:message code="admin.statis.popup.swtitle" text="매입추가요금" /></h4>
                                </c:otherwise>
                            </c:choose>
                            
                    
                    
                    <!-- 주문/배송 정보 -->
                    <table class="tbtype2 mt10">
                        <colgroup>
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">
                                        <col class="wp100">

                        </colgroup>
                       <thead>
                            <tr>
                                <th rowspan="2"><spring:message code="common.no" text="번호" /></th> 
                                <th><spring:message code="admin.statis.popup.payment" text="추가요금" /></th>
                                <th><spring:message code="admin.statis.popup.regdt" text="등록일" /></th>
                                <th><spring:message code="admin.statis.popup.desc" text="사유" /></th>
                            </tr>
                        </thead> 
                        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(list) == 0}">
                                        <tr>
                                            <td colspan="4" class="t_center"><p><spring:message code="common.title.notData" text="데이터가 없습니다." /></p></td>
                                        </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${list}" var="item" varStatus="status">
                                        
                                        <tr >  
                                            <td class="t_center"><p><%=row++%></p></td>
                                            <td class="t_center"><p>${item.payment}</p></td>
                                            <td class="t_center"><p>${item.regDate}</p></td> 
                                            <td class="t_center"><p>${item.addChargeInfo}</p></td>
 
                                        </tr>
                                                                            
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            
                        </tbody>
                    </table>
                        
                 
                    
                </div>