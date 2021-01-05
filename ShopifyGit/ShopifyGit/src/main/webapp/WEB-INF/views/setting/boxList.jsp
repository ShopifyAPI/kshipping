<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_ADM_531_BAS 
기능설명 : 설정관리 > 배송관리 > 포장재관리
Author   Date         Description
jwh      2019-12-23   First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%> 
    <title>Title</title>
    <script type="text/javascript" src="/js/setting/boxList.js">
    </script>
</head>
<body>
    <div class="frame">
        <div class="frame_wrap">
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incGnb.jsp"%> 
            <div class="sub_conts">
                <article>
                <!-- #### Content area ############### -->
                    <!-- tit_wrap -->
                    <div class="tit_wrap">
                        <h3><spring:message code="incgnb.settings" text="설정" /></h3>
                        <div class="sect_right">
                       	 	<!-- 기본 검색 영역 -->
                        	<!--// 기본 검색 영역 -->
                        </div>
                    </div> 
                    <!--// tit_wrap end --> 
                    
                    <ul class="tab">
                                <li>
                                    <a href="/setting/seller">
                                        <spring:message code="settings.menuSeller" text="계정관리" />
                                    </a>
                                </li>
                                <li class="on">
                                    <a href="/setting/listSender">
                                        <spring:message code="settings.menuDelivery" text="배송관리" />
                                    </a>
                                </li>
                    </ul>
                    
                    <ul class="tab_conts">
                        <li>
                            <div class="sub_tab">
                                <a href="/setting/listSender"><spring:message code="settings.menuSender" text="출고지" /></a>
                                <a href="/setting/listBox" class="on"><spring:message code="settings.menuBox" text="포장재" /></a>
                                <a href="/setting/listSku"><spring:message code="settings.menuSku" text="관세정보" /></a>
                                <a href="/setting/listCourier"><spring:message code="settings.menuCourier" text="배송사" /></a>
                            </div>
                            <div class="sub_tab_conts">
                                <div>
                                    <div class="module_group">
                                        <div class="btn_group">
	                        				<button type="button" class="btn_type3" id="btnSave"><spring:message code="button.save" text="저장" /></button>
                                            <button type="button" class="btn_type1 add"><span><spring:message code="button.add" text="추가" /></span></button>
                                            <button type="button" class="btn_type1 del list-del"><span><spring:message code="button.delete" text="삭제" /></span></button>
                                        </div>
                                    </div>
                                    
                            <form name="listform" id="listform" method="get">
                                     
                           		 <c:choose>
                                	<c:when test="${fn:length(list) == 0}">
                                   		 <div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
                                	</c:when>
                                	
                                	<c:otherwise>
                                		<table class="tbtype">
                                			<colgroup>
                                				<col class="wp35">    	<!-- 체크박스 -->
                                				<col class="cper1200"/>		<!-- 포장재명 -->
                                				<col class="cper0500"/>		<!-- 포장재타입 -->
                                				<col class="cper1000"/>		<!-- 무게  -->
                                				<col class="cper0500"/>		<!-- 무게 단위 -->
                                				<col class="cper1000"/>		<!-- 가로 (cm) -->
                                				<col class="cper1000"/>		<!-- 세로 (cm) -->
                                				<col class="cper1000"/>		<!-- 높이 (cm) -->
                                				<col class="cper0500"/>   	<!-- 길이 단위 -->
                                			</colgroup>
                                			<thead>
                                				<tr>
                                						<th><input type="checkbox" name="allCheck" value="Y" id="ind01"><label for="ind01"><span class="icon_ck" ></span></label></th>
                                						<th><spring:message code="settings.boxTitle" text="포장재명" /></th>
                                						<th><spring:message code="settings.boxType" text="포장재 타입" /></th>
                                						<th><spring:message code="settings.boxWeight" text="포장재 무게" /></th>
                                						<th><spring:message code="settings.weightUnit" text="무게단위 " /></th>
                                						<th><spring:message code="settings.boxWidth" text="가로 " /></th>
                                						<th><spring:message code="settings.boxHeight" text="세로 " /></th>
                                						<th><spring:message code="settings.boxLength" text="높이 " /></th>
                                						<th><spring:message code="settings.lengthUnit" text="길이단위 " /></th>
                                				</tr>
                                			</thead>
                                			<tbody>
	                                			
	                                			<c:forEach items="${list}" var="item" varStatus="status">
	                                				<tr data-idx="${item.boxIdx}">
	                                					<td class="t_center"><input type="checkbox" name="ckBox" value="${item.boxIdx}" id="ind02_${status.count}"><label for="ind02_${status.count}">
	                                							<span class="icon_ck"></span></label></td>
	                                					<td class="t_center"><input type="text" name="boxTitle" value="${item.boxTitle}"/><p></p></td>
	                                					<td class="t_center"><p>${item.boxType}</p></td>
	                                					<td class="t_center"><input type="text" name="boxWeight" data-format="number" data-label="포장재무게" value="${item.boxWeight}"/><p></p></td>
	                                					<td class="t_center"><p>${item.weightUnit}</p></td>
	                                					<td class="t_center"><input type="text" name="boxWidth" value="${item.boxWidth}"/><p></p></td>
	                                					<td class="t_center"><input type="text" name="boxHeight" value="${item.boxHeight}"/><p></p></td>
	                                					<td class="t_center"><input type="text" name="boxLength" value="${item.boxLength}"/><p></p></td>
	                                					<td class="t_center"><p>${item.boxUnit}</p></td>
	                                				</tr>
	                                			</c:forEach>
                                			
                                			</tbody>
                                		</table>
                                	</c:otherwise>
                                </c:choose>
                             </form>
                           </div>
                         </div>
                            
                                				
                        </li>
                    </ul>
                    
                      
                <!--// #### Content area ############### -->        
                
                </article>
            </div>    
        </div>
    </div>    
   
    <%// footer include %>
    <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>

</body>
</html>