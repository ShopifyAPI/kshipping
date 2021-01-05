<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>

        <div class="gnb lnb">
         <a class="fbx_view_btn"></a>
            <div class="aside">
            	<a class="logo_lnb" href="#"></a>
                <ul class="menu icogrp">
                    <li class="I010030"><a href="#" onclick="return false;" class="debth_1">셀러 정보 관리</a>
                        <div class="debth_2">
                            <a id="seller" href="/admin/seller/list" data-subpage01="/admin/seller/viewSeller">셀러 정보 관리</a>
                            <a id="discount" href="/admin/seller/discount" data-subpage01="/admin/seller/viewDiscount">셀러별 할인율 조회</a>
                        </div>
                    </li>
                    <li class="I010040"><a href="#" onclick="return false;" class="debth_1">배송CS 관리</a>
                        <div class="debth_2">
                            <a href="/admin/cs/adminDeliveryList">배송</a>
                            <a href="/admin/cs/adminErrorList">에러</a>
                            <a href="/admin/cs/adminBackList">반송</a>
                            <a href="/admin/cs/adminExchangeList">교환</a>
                            <a href="/admin/cs/adminReturnList">반품</a>
                            <a href="/admin/cs/adminPaymentList">추가요금</a>
                        </div>
                    </li>
                    <li class="I010050"><a href="#" onclick="return false;" class="debth_1">고객센터 관리</a>
                        <div class="debth_2">
                            <a id="qna" href="/admin/board/selectQna" data-subpage01="/admin/board/admQnaAnswer">Q&A 관리</a>
                            <a id="faq" href="/admin/board/selectFaq" data-subpage01="/admin/board/admEditFaq" data-subpage02="/admin/board/admNewFaq">FAQ 관리</a>
                            <a href="/admin/board/selectNotice">공지사항 관리</a>
                        </div>
                    </li>
                    <li class="I010060"><a href="#" onclick="return false;" class="debth_1">특송 요금 관리</a>
                        <div class="debth_2">
                            <a href="/admin/delivery/listShipCompany">배송사 관리</a>
                            <a id="price" href="/admin/price/feesPriceMappingList" data-subpage01="/admin/price/salePriceMappingList">요금 매핑 관리</a>
                            <!--  <a href="#">환율 관리</a> -->
                            <a href="/admin/rank/rankList">할인율 관리</a>
                        </div>
                    </li>
                    <li class="I010090"><a href="#" onclick="return false;" class="debth_1">정산 관리</a>
                        <div class="debth_2">
                            <a id="payment" href="/admin/statis/payment" data-subpage01="/admin/statis/paymentDaily">손익통계 조회</a>
                            <a id="sales" href="/admin/statis/sales" data-subpage01="/admin/statis/salesLocal" data-subpage01="/admin/statis/salesCharge">매출원장 조회</a>
                            <a id="statistics" href="/admin/statis/statistics" data-subpage01="/admin/statis/statisticsDaily">매출통계 조회</a>
                        </div>
                    </li>
                    <li class="I010070"><a href="#" onclick="return false;" class="debth_1">운영 관리</a>
                        <div class="debth_2">
                            <a href="/admin/code/selectCode">코드 관리</a>
                        </div>
                    </li>
                    <li class="I010080"><a href="#" onclick="return false;" class="debth_1">관리자 관리</a>
                        <div class="debth_2">
                            <a id="admin" href="/admin/admin/adminList" data-subpage01="/admin/admin/showAdmin">관리자 관리</a>
<!--                             <a id="exceptionList" onclick="var win = window.open('/admin/admin/exceptionList', '_blank'); win.focus();">Exception List</a> -->
                        </div>
                    </li>
                    <li class="show logout"><a href="/admin/logout" class="debth_1">로그아웃</a>
                        <!-- <div class="debth_2">
                            <a href="/admin/userInfo">개인정보 관리</a>
                            <a href="/admin/logout">로그아웃</a>
                        </div> -->
                    </li>
                </ul>
            </div>
        </div>
		