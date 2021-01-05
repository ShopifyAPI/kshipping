<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>
<script>
</script>
<!-- 
**************************************************
화면코드 :UI_SYS_322_POP 
기능설명 : 부피무게 등록(팝업)
Author   Date      Description
 조한두     2020-09-03  First Draft
**************************************************
 -->
<div class="pop_head">
    <h3><spring:message code="admin.statis.popup.vwtitle" text="부피무게 중량등록" /></h3>
    <button type="button" class="btn_close"></button>
</div>

<div class="pop_body">
    <div class="bg_gray wp420">
        <form name="volumeweight" id="volumeweight" method="post" enctype="multipart/form-data" action="/admin/uploadVolumeWeightExcelFile">
       
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
        <button type="button" class="btn_type2" id="btn_write_pop_weight"><span><spring:message code="button.save" text="저장" /></span></button>
    </div>
</div><!--pop_body end-->