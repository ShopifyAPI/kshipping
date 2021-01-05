<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>
<script>
</script>
<!-- 
**************************************************
화면코드 :UI_SYS_322_POP 
기능설명 : 공시요금 등록(팝업)
Author   Date      Description
 김윤홍     2020-01-28  First Draft
**************************************************
 -->
<div class="pop_head">
    <h3><spring:message code="price.popupFeesPriceNew" text="공시요금 매핑 등록" /></h3>
    <button type="button" class="btn_close"></button>
</div>

<div class="pop_body">
	<div class="bg_gray wp420">
		<form name="feesPrice" id="feesPrice" method="post" enctype="multipart/form-data" action="/admin/uploadPriceExcelFile">
		<input type="hidden" value="fees" name="gbn">
		<dl>
		    <dt><c:set var="startDate"><spring:message code="price.startDate" text="적용시작일" /></c:set>
            	${startDate}
            </dt>
		    <dd>
	            <div class="month"><input type="text" class="date" id="startDate" name="startDate" 
				 placeholder="${startDate}" data-label="${startDate}" data-required='Y'></div>
		    </dd>
		</dl>
		<dl>
		    <dt><c:set var="courier"><spring:message code="price.deliveryCompany" text="배송사" /></c:set>
            	${courier}
            </dt>
		    <dd>
	            <select class="select-ajax" name="shipCompany" id="shipCompany"
	            data-codetype="" data-code="${shipCompany}"  data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B010000" }'
	            data-label="${courier}" data-required='Y'>
	            </select>
		    </dd>
		</dl>
		<dl>
		    <dt><c:set var="deliveryService"><spring:message code="price.deliveryService" text="배송 서비스" /></c:set>
                ${deliveryService}
            </dt>
		    <dd>
		        <select name="deliveryService" data-code="${code}" id="deliveryService" data-label="${deliveryService}" data-required='Y'>
		        	<option value="">== 배송 서비스 ==</option>
		        </select>
		    </dd>
		</dl>
		<dl>
           	<dt><spring:message code="price.fileUpload" text="파일업로드" /></dt>
            <dd>
                <div class="filebox">
                    <input class="upload_name" id="upload_name" type="text" vlaue="파일선택" disabled="disabled">
                    <label for="file_up">파일 선택</label>
                    <input type="file" class="upfile" id="file_up" name="file" />
                </div>
            </dd>
        </dl>
		</form>
	</div>
    <div class="pop_btn">
        <button type="button" class="btn_type2" id="btn_write_pop_fees"><span><spring:message code="button.save" text="저장" /></span></button>
    </div>
</div><!--pop_body end-->