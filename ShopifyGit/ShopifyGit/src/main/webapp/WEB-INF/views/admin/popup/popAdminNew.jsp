<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
<script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>

<!-- 
**************************************************
화면코드 :UI_SYS_542_POP 
기능설명 : ADM 관리자 등록(팝업)
Author   Date      Description
 김윤홍     2020-01-07  First Draft
**************************************************
 -->
<div class="pop_head">
   <h3><spring:message code="admin.adminInsert" text="관리자 등록"/></h3>
<button type="button" class="btn_close"></button>
</div> 	
		
<div class="pop_body">
	<div class="bg_gray wp380">
		<form name="insertAdmin" id="insertAdmin" action="/admin/admin/insertAdminListPop">
			<dl>
                <dt><c:set var="adminDepart"><spring:message code="board.part" text="구분" /></c:set>
            		${adminDepart}
            	</dt>
                <dd>
					<select class="select-ajax" id="adminScopeId" name="adminScopeId" data-required="Y" data-label="구분"
                     data-codetype="" data-code="" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "I010000" }'></select>        
				</dd>
            </dl>
			
			<dl>
                <dt><c:set var="startDate"><spring:message code="admin.pop.startDate" text="시작일" /></c:set>
            		${startDate}
            	</dt>
                <dd>
					<div class="month"><input type="text" class="date" id="adminUseSdate" name="adminUseSdate" 
				         placeholder="${startDate}" data-label="${startDate}" data-required='Y'></div>
				</dd>
            </dl>
            
            <dl>
                <dt><c:set var="endDate"><spring:message code="admin.pop.endDate" text="마감일" /></c:set>
            		${endDate}
            	</dt>
                <dd>
					<div class="month"><input type="text" class="date" id="adminUseEdate" name="adminUseEdate" 
				     placeholder="${endDate}" data-label="${endDate}" data-required='Y'></div>
				</dd>
            </dl>
		
			<dl>
            	<dt><c:set var="eMail"><spring:message code="board.email" text="이메일" /></c:set>
            		${eMail}
            	</dt>
	            <dd>
	            	<input type="text" class="mt10 cper100p" name="adminId" id="adminId" 
			               data-required="Y" data-label="${eMail}" data-format="email" placeholder="${eMail}">
	            </dd>
            </dl>
            <dl>
                <dt><c:set var="password"><spring:message code="admin.pop.password" text="비밀번호" /></c:set>
            		${password}
            	</dt>
                <dd><input type="password" class="mt10 cper100p" name="adminPasswd" id="adminPasswd" 
			               data-required="Y" data-label="${password}" placeholder="${password}"/></dd>
            </dl>
            <dl>
                <dt><c:set var="name"><spring:message code="settings.name" text="이름" /></c:set>
            		${name}
            	</dt>
                <dd><input type="text" class="mt10 cper100p" name="adminName" id="adminName" 
			               data-required="Y" data-label="${name}" placeholder="${name}"/></dd>
            </dl>
            <dl>
                <dt><c:set var="contact"><spring:message code="order.popup.address.phone" text="전화번호" /></c:set>
            		${contact}
            	</dt>
                <dd><input type="text" class="mt10 cper100p" name="adminPhoneNumber" id="adminPhoneNumber" 
			               data-required="Y" data-label="${contact}" placeholder="${contact}" data-format="phone" /></dd>
            </dl>       
            
            

            <dl>
                <dt><c:set var="useYn"><spring:message code="delivery.shipcompanyYn" text="사용여부" /></c:set>
            		${useYn}
            	</dt>
                <dd>
					<select class="select-ajax" name="adminUseYn" id="adminUseYn"
				data-codetype="etc" data-code="" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "K010000" }'>
				</select>        
				</dd>
            </dl>
            
		</form>
	</div>
	
       
	<div class="pop_btn">
	    <button type="button" class="btn_type2" id="btn_send_pop"><span><spring:message code="button.save" text="저장" /></span></button>
<%-- 		<button type="button" class="btn_type6" id="btn_cancle_pop"><span><spring:message code="button.cancel" text="취소" /></span></button> --%>
	</div>
</div><!--pop_body end-->
