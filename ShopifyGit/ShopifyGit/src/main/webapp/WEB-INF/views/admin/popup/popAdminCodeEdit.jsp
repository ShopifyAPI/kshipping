<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>
<script>
</script>
<!-- 
**************************************************
화면코드 :UI_SYS_512_POP 
기능설명 : 관리자 코드수정 (팝업)
Author   Date      Description
 김윤홍     2020-02-03  First Draft
**************************************************
 -->
<div class="pop_head">
    <h3><spring:message code="code.pop.CodeEdit" text="코드수정" /></h3>
    <button type="button" class="btn_close"></button>
</div>

<div class="pop_body">
	<form name="editAdminCode" id="editAdminCode" method="post">
    
	    <div class="bg_gray mt10">
	        <dl>
	            <dt><c:set var="CodeNameKo"><spring:message code="code.CodeNameKo" text="코드명(한글)" /></c:set>
	            	${CodeNameKo}
	            </dt>
	            <dd>
	                <input type="text" id="codeKname" name="codeKname" data-label="${CodeNameKo}" data-required='Y'
	                value="${adminCodePopupData.codeKname}"/>
	            </dd>
	        </dl>
	        <dl>
	            <dt><c:set var="CodeNameEn"><spring:message code="code.CodeNameEn" text="코드명(영어)" /></c:set>
	            	${CodeNameEn}
	            </dt>
	            <dd>
	            	<input type="text" id="codeEname" name="codeEname" data-label="${CodeNameEn}" data-required='Y'
	            	value="${adminCodePopupData.codeEname}"/>
	            </dd>
	        </dl>
	        <dl class="mainCategory">
	            <dt><c:set var="MainCategory"><spring:message code="code.MainCategory" text="대분류" /></c:set>
	            	${MainCategory}
	            </dt>
	            <dd>
	           		<input type="text" id="codeGroup" name="codeGroup" data-label="${MainCategory}" data-required='Y'
					value="${adminCodePopupData.codeGroup}"/>
	            </dd>
	        </dl>
	        <dl>
	            <dt><c:set var="Category"><spring:message code="code.Category" text="코드" /></c:set>
	            	${Category}
	            </dt>
	            <dd>
					<input type="text" id="codeId" name="codeId" data-label="${Category}" data-required='Y'
					value="${adminCodePopupData.codeId}"/>
	            </dd>
	        </dl>
	        
	        <dl>
	            <dt><c:set var="EtcCode"><spring:message code="code.EtcCode" text="기타코드" /></c:set>
	            	${EtcCode}
	            </dt>
	            
	            <dd>
	            	<input type="text" id="codeEtc" name="codeEtc" data-label="${EtcCode}"
	            	value="${adminCodePopupData.codeEtc}"/>
            	</dd>
	        </dl>
	        <dl>
	            <dt><c:set var="seq"><spring:message code="code.seq" text="순서" /></c:set>
	            	${seq}
	            </dt>
	            <dd>
	            	<input type="text" id="codeSeq" name="codeSeq" data-label="${seq}" data-format="num" data-required='Y'
	            	value="${adminCodePopupData.codeSeq}" />
	            </dd>
	        </dl>
	       
	        <dl>
	            <dt><c:set var="discript"><spring:message code="code.discript" text="설명" /></c:set>
	            	${discript}
	            </dt>
	            <dd>
	            	<input type="text" id="codeDiscript" name="codeDiscript" data-label="${discript}"
	            	value="${adminCodePopupData.codeDiscript}"/>
	            </dd>
	        </dl>
	        <dl>
	            <dt><c:set var="useYn"><spring:message code="code.useYn" text="사용여부" /></c:set>
	            	${useYn}
	            </dt>
	            <dd>
	            	<select class="select-ajax" name="codeUseYn" id="codeUseYn"
	           		        data-codetype="etc" data-code="${adminCodePopupData.codeUseYn}"  data-url="/common/componentDataSet" data-param='{ "codeGroup" : "K010000" }'
	                        data-label="${useYn}" data-required='Y'>
	            	</select>
	            </dd>
	        </dl>
	    </div>
	</form>
    
    <div class="pop_btn">
        <button type="button" class="btn_type2 cper100p" id="btn_edit_pop"><spring:message code="button.save" text="저장" /></button>
        
    </div>
</div><!--pop_body end-->