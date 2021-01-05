<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_SYS_412_BAS 
기능설명 : ADM NOTICE 등록
Author   Date      Description
 YR    2019-12-23  First Draft
**************************************************
-->
<!DOCTYPE html>
<html lang="ko">
<head>
<%// header include %>
<%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 
<title>ADM NOTICE 등록</title>

<style>
.filebox ul {
	float: left;
	display: inline-block;
	list-style: none;
	min-height: 35px;
	background-color: #F9F9F9;
}
.filebox ul li {
	float: left;
	height: 31px;
	line-height: 30px;
	padding-left: 10px;
	padding-right: 10px;
	margin: 2px 4px 2px 0px;
	border: 1px solid #CCC;
	background-color: #F0F0F0;
	cursor: pointer;
}
.filebox ul li#clear {
	border: none;
	background-color: transparent;
	color: #900;
}
span.deleteFile {
	display: inline-block;
	width: 10px;
	cursor: pointer;
	color: #999;
}
span.deleteFile:hover {
	color: #900;
}
</style>

</head>
<body>

<div class="wrap">
<%// gnb include %>
<%@ include file="/WEB-INF/views/common/incAdmGnb.jsp"%> 
<script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>
<script src="/js/notice.js?v=<%=System.currentTimeMillis() %>"></script>

<!-- #### Content area ############### -->
<div class="contents">
	<div class="cont_body">
		<article>
			<h2><spring:message code="notice.insertNotice" text="공지사항 등록"/></h2>
			<div class="tit_wrap mt0">
				<form name="searchform" id="searchform" method="post" enctype="multipart/form-data" action="/admin/board/insertNotice">
					<input type="hidden" id="division" name="division" value="D010003">
                    <input type="hidden" id="idx" name="idx" value="0">
					<div class="sect_right">
						<%-- 공지사항 유형 --%>
						<select class="select-ajax" name="partCode" id="partCode" data-codetype="" data-code="${adminNoticeData.partCode}" data-url="/common/componentDataSet" data-param='{"codeGroup":"D040000"}'></select>
						<%-- 상단고정 여부 --%>
						<select class="select-ajax" name="flagTop" id="flagTop" data-codetype="" data-code="${adminNoticeData.flagTop}" data-url="/common/componentDataSet" data-param='{"codeGroup":"K020000"}'></select>  
						<%-- 공지기간 --%>
						<div class="month"><input type="text" class="date" id="notiFromDate" name="notiFromDate" value="${adminNoticeData.notiFromDate}" data-label="공지시작" data-required='Y' /></div><span>~</span>
						<div class="month"><input type="text" class="date" id="notiToDate" name="notiToDate" value="${adminNoticeData.notiToDate}" data-label="공지마감" data-required='Y' /></div>
					</div>
					<br/><br/>
					<table class="tbtype mt20">         
						<colgroup>
							<col class="cper100p">
						</colgroup>
						<tbody>
							<%-- 글 제목 --%>
							<tr class="tline">
								<td class="nolline">
									<input type="text" class="cper100p" id="title" name="title" value="${adminNoticeData.title}" maxlength="60" data-label="제목" data-required='Y' placeholder="제목" />
								</td>
							</tr>
							<%-- 글 본문 --%>							
							<tr>
								<td>
									<div class="editor">
										<textarea id="content" name="content" data-label="내용" data-required='Y' placeholder="내용">
											${adminNoticeData.content}
										</textarea>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<%-- 첨부파일 --%>
									<div class="filebox">
										<ul id="uplist" name="uplist"></ul>
										<label for="file_up">파일선택</label>
										<input type="file" class="upfile" id="file_up" name="file" multiple="multiple"
										       accept="application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, text/plain, application/pdf, image/*" />
									</div>
								</td>
							</tr>
							<c:if test="${not empty adminNoticeFileData}">
							<c:forEach items="${adminNoticeFileData}" var="list" varStatus="status">
							<tr>
								<td>
									<span class="deleteFile" data-idx="${list.idx}" onclick="deleteFile(this)">x</span> ${list.fileName} (${list.size}bytes) [<a href="${list.fileDownloadUri}">다운로드</a>]
								</td>
							</tr>
							</c:forEach>
							</c:if>							
						</tbody>
					</table>    
				</form>
				<c:if test="${empty adminNoticeData}">
					<script type="text/javascript">
						$("input#notiFromDate").val(getToday()) ;
						$("input#notiToDate").val(getToday()) ;
					</script>
				</c:if>
				<c:if test="${not empty adminNoticeData}">
					<script type="text/javascript">
						$("form#searchform").attr("action", "/admin/board/updateNotice") ;
						$("input#idx").val("${adminNoticeData.idx}") ;
					</script>
				</c:if>
			</div>    
			<div class="foot_btn">
				<button type="button" class="btn_type5" id="btn_list"><span><spring:message code="button.list" text="목록" /></span></button>
				<button type="button" class="btn_type2" id="btn_save"><span><spring:message code="button.complete" text="완료" /></span></button>
			</div>
		</article>
	</div>
</div>
<!--// #### Content area ############### -->

</div>
<%// footer include %>
<%@ include file="/WEB-INF/views/common/incAdmFooter.jsp"%>
</body>
</html>