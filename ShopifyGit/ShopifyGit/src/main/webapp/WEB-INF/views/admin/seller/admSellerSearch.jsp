<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_SYS_111_BAS
기능설명 : 관리자 > 셀러정보관리 > 리스트
Author   Date      Description
 jwh     2020-01-22  First Draft
**************************************************
 -->
                <div class="pop_head">
                    <h3>셀러 찾기</h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                    <div class="searchbox cper100p">
                        <input name="searchWord" type="text" value="${search.searchWord}" placeholder="검색을 통해 셀러를 선택해주세요.">
                        <input name="searchType" type="hidden" value="total">
                        <button type="button" class="ic_search"></button>
                    </div>
                    <div class="overten mt20">
                    <table class="tbtype" id="popSearchArea">
                        <colgroup>
                            <col class="cper2500">
                            <col class="cperauto">
                            <col class="cperauto">
                        </colgroup>
                        <thead>
                            <tr>
                                <th>업체명</th>
                                <th>이름</th>
                                <th>이메일</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                               <td colspan="3" class="t_center">
                                   <p>셀러를 검색해 주세요</p>
                               </td>
                            </tr>
                        </tbody>
                        <tfoot style="display:none">
                            <tr id="sellerList">
                                <td><a href="#" class="link popSeller" data-email="{email}">{company}</a></td>
                                <td><a href="#" class="link popSeller" data-email="{email}">{name}</a></td>
                                <td><a href="#" class="link popSeller" data-email="{email}">{email}</a></td>
                            </tr>
                            <tr id="sellerListNone">
                                <td colspan="3" class="t_center"><p>셀러를 검색해 주세요</p></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div><!--pop_body end-->
