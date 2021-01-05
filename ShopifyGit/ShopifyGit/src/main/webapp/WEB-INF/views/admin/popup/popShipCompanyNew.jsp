<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_SYS_312_POP 
기능설명 : ADM 배송사 등록(팝업)
Author   Date      Description
 김윤홍     2019-12-30  First Draft
**************************************************
 -->
<div class="pop_head">
    <h3>배송 상세</h3>
    <button type="button" class="btn_close"></button>
</div>

<div class="pop_body">
    <div>
       <strong> * 신규 생성은 코드관리에서 먼저 설정해 주세요.</strong>
    </div>
	<div class="bg_gray wp420 mt10">
		<form name="shipCompany" id="shipCompany" method="post">
		<dl>
		    <dt><c:set var="shipCompany"><spring:message code="delivery.shipcompany.title1" text="배송사" /></c:set>
            	${shipCompany}
            </dt>
		    <dd>
	            <select class="select-ajax" name="shipId" id="shipId" 
                    data-codetype="" data-code="${delivery.id}" data-url="/common/componentDataSet" data-param='{"codeGroup":"B010000"}' 
                    data-required="Y" data-label="${shipCompany}" placeholder="${shipCompany}" 
                    <c:if test="${delivery.id != '' and delivery.id != null}"> disabled</c:if>>  
                </select>
		    </dd>
		</dl>
		<dl>
		    <dt><c:set var="code"><spring:message code="delivery.shipcompany.title2" text="배송서비스" /></c:set>
                ${code}
            </dt>
		    <dd id="codeArea"> 
		        <select class="" name="code" id="code" 
		        	data-codetype="" data-code="${delivery.code}" data-url="/common/componentDataSet" data-param='' 
		        	data-required="Y" data-label="${code}" placeholder="${code}"
		           <c:if test="${delivery.code != '' and delivery.code != null}"> disabled</c:if>>
		        <option value="">== ${code} ==</option>
		        </select>
		    </dd>
		</dl>
		<dl>
            <dt><c:set var="mindate"><spring:message code="delivery.shipcompany.mindate" text="최소배송일" /></c:set>
                ${mindate}
            </dt>
            <dd>
                <input type="text" class="cper100p" name="minDeliveryDate" id="minDeliveryDate" value="${delivery.minDeliveryDate}" 
                    data-required="Y" data-format="number" data-label="${mindate}" placeholder="${mindate}">
            </dd>
        </dl>
        <dl>
            <dt><c:set var="maxdate"><spring:message code="delivery.shipcompany.maxdate" text="최대배송일" /></c:set>
                ${maxdate}
            </dt>
            <dd>
                <input type="text" class="cper100p" name="maxDeliveryDate" id="maxDeliveryDate" value="${delivery.maxDeliveryDate}"
                    data-required="Y" data-format="number" data-label="${maxdate}" placeholder="${maxdate}">
            </dd>
        </dl>
		<dl>
		    <dt><c:set var="codeYn"><spring:message code="delivery.shipcompanyYn" text="사용여부" /></c:set>
            	${codeYn}
            </dt>
		    <dd>
		        <select name="useYn" id="useYn">
		        <option value="Y"<c:if test="${delivery.useYn == 'Y'}"> selected</c:if>>Y</option>
		        <option value="N"<c:if test="${delivery.useYn == 'N'}"> selected</c:if>>N</option>
		        </select>
		    </dd>
		</dl>
		</form>
	</div>
    <div class="pop_btn">
        <button type="button" class="btn_type2" id="btn_write_pop<c:if test="${delivery.id != '' and delivery.id != null}">_edit</c:if>"><span><spring:message code="button.save" text="저장" /></span></button>
    </div>
</div><!--pop_body end-->