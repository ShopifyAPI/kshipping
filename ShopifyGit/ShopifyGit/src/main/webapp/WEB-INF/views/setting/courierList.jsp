<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
<!-- 
**************************************************
화면코드 : UI_ADM_541_BAS 
기능설명 : 설정관리 > 배송관리 > 관세정보
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
    <script type="text/javascript" src="/js/setting/courierList.js"></script>
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
                                <h3>
                                    <spring:message code="incgnb.settings" text="설정" />
                                </h3>
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
                                        <a href="/setting/listSender">
                                            <spring:message code="settings.menuSender" text="출고지" />
                                        </a>
                                        <a href="/setting/listBox">
                                            <spring:message code="settings.menuBox" text="포장재" />
                                        </a>
                                        <a href="/setting/listSku">
                                            <spring:message code="settings.menuSku" text="관세정보" />
                                        </a>
                                        <a href="/setting/listCourier" class="on">
                                            <spring:message code="settings.menuCourier" text="배송사" />
                                        </a>
                                    </div>
                                    <div class="sub_tab_conts">
                                        <div>
                                            <div class="module_group">
                                                <div class="btn_group">
                                                    <button type="button" class="btn_type2" id="saveCourierBtn">
                                        <spring:message code="button.save" text="저장" />
                                    </button>
                                                </div>
                                            </div>
                                            <form name="listform" id="listform" method="get">

                                                <table class="tbtype tbsmall mt10">
                                                    <colgroup>
                                                        <col class=cper2000>
                                                        <col class="cperauto">
                                                        <col class=cper3000>
                                                        <col class="cper2000">
                                                    </colgroup>
                                                    <thead>
                                                        <tr>
                                                            <th>
                                                                <spring:message code="settings.shop" text="서비스" />
                                                            </th>
                                                            <th>
                                                                <spring:message code="order.popup.customer.title2" text="서비스" />
                                                            </th>
                                                            <th>
                                                                <spring:message code="shipment.popup.payment.boxsize" text="박스 사이즈" />
                                                            </th>
                                                            <th>
                                                                <spring:message code="order.popup.customer.title4" text="선택" />
                                                            </th>
                                                        </tr>
                                                    </thead>
                                                    <tbody class="courier-area">
                                                        <c:choose>
                                                            <c:when test="${fn:length(companyList) == 0}">
                                                                <tr>
                                                                    <td colspan="2" class="t_center">
                                                                        <spring:message code="order.popup.customer.nolist" text="해당하는 국가의 국제 특송업체가 존재 하지 않습니다." />
                                                                    </td>
                                                                </tr>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:forEach items="${shopList}" var="shop">
                                                                    <c:forEach items="${companyList}" var="company">
                                                                        <tr class="courierList" 
		                                                                        data-shop-shop-idx="${shop.shopIdx}" 
		                                                                        data-company-courier-code="${company.courierCode}"> 
                                                                            <td>
                                                                                <p>${shop.shopName}</p>
                                                                            </td>
                                                                            <td>
                                                                                <p>${company.courierName}</p>
                                                                            </td>
                                                                            <td>
                                                                                <select name="boxType" data-codetype="etc">
		                                                                           <option  data-box-courier-id=""
		                                                                                   value="">== Select option ==</option>
		                                                                           <c:forEach items="${boxList}" var="box">
		                                                                               <c:if test="${company.courierCode == box.courierId}">
		                                                                                   <option data-box-courier-id="${box.courierId}"
		                                                                                      <c:if test="${shop.courierId == box.courierId && shop.boxType == box.zone}"> selected</c:if>
		                                                                                        value="${box.zone}">${box.price} : ${box.boxType}</option>
		                                                                               </c:if>
                                                                           </c:forEach>
                                                                       </select>
                                                                            </td>
                                                                            <td class="t_center">
                                                                                <button type="button" class="btn_select">
                                                                                    <spring:message code="button.select" text="선택" />
                                                                                </button>
                                                                            </td>
                                                                            </tr>
                                                                    </c:forEach>
                                                                </c:forEach>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tbody>
                                                </table>
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