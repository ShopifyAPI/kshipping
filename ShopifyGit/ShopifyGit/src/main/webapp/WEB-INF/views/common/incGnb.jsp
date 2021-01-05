<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

            <div class="gnb_menu">
                <ul>
                    <!--활성화시 active 클래스 추가-->
                    <li<c:if test="${pageInfo eq 'dashboard' }"> class="active"</c:if>><a href="/"><span class="menu01"></span><spring:message code="incgnb.dashboard" text="대시보드" /></a></li>  
                    <li<c:if test="${pageInfo eq 'order' }"> class="active"</c:if>><a href="/order"><span class="menu02"></span><spring:message code="incgnb.order" text="주문" /></a></li>
                    <li<c:if test="${pageInfo eq 'shipment' }"> class="active"</c:if>><a href="/shipment"><span class="menu03"></span><spring:message code="incgnb.shipment" text="배송" /></a></li>
                    <li<c:if test="${pageInfo eq 'cssenter' }"> class="active"</c:if>><a href="/cs/backList"><span class="menu04"></span><spring:message code="incgnb.csmanage" text="cs 관리" /></a></li>
                    <li<c:if test="${pageInfo eq 'setting' }"> class="active"</c:if>><a href="/setting/seller"><span class="menu05"></span><spring:message code="incgnb.settings" text="설정" /></a></li>
                    <li<c:if test="${pageInfo eq 'board' }"> class="active"</c:if>><a href="/board/selectQna"><span class="menu06"></span><spring:message code="incgnb.servicecenter" text="고객센터" /></a></li>
                    
                    <c:if test="${empty SHOPIFY_SESSION.email }"> <!-- sessionScopre.id가 없으면 -->
				    <a href="/login">로그인</a><br/>
				    </c:if>
				    <c:if test="${not empty SHOPIFY_SESSION.email }"> <!-- sessionScopre.id가 있으면 -->
				    <!--  
				    (${SHOPIFY_SESSION.email})
				    <a href="/logout">로그아웃</a><br/>
				    -->
				    </c:if>
                </ul>
            </div>
		
		
		
		