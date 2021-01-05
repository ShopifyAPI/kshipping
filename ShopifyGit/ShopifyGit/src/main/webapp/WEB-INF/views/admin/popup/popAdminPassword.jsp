<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
<script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>

<!-- 
**************************************************
화면코드 : UI_SYS_112_POP_A 
기능설명 : 관리자 > 비밀번호 변경(팝업)
Author   Date      Description
 jwh     2020-01-22  First Draft
**************************************************
 -->
                <c:set var="password"><spring:message code=" admin.pop.password" text="비밀번호" /></c:set>
                <div class="pop_head">
                    <h3>${password}</h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                    <p class="t_center bold"><spring:message code="admin.pop.passwordText" text="비밀번호를 입력해 주세요." /></p>
                    
                    <input type="password" class="mt10 cper100p" name="password" id="password" value="" 
                        maxlength="20" readonly data-required="Y" data-label="${password}" placeholder="${password}">
                                        
                    <div class="pop_btn">
                        <button type="button" class="btn_type2">수정</button>
                        <button type="button" class="btn_type6">취소</button>
                        
                    </div>
                </div>