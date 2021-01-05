<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_ADM_612_TAB 
기능설명 : Q&A 상세보기
Author   Date      Description
 김윤홍     2019-12-23  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <jsp:include page="/WEB-INF/views/common/incHeader.jsp" flush="false" />

    <title>셀러 Q&A 작성</title>
    <script type="text/javascript">

	// READY
	$(function() {
		initialize();
	});

	var initialize = function() {
		initControls();
		bindEvent();
	};

	var initControls = function() {
		var $submitform = $('#submitform');
    	bindSelectAjax($submitform);
	};

	var bindEvent = function() {
	
		CKEDITOR.replace('content'
		        , {height: 277                                                  
		         });
		
		
		$("#btn_list").on('click',function(){
			//목록화면으로 이동 
			location.href = "/board/selectQna";
		});	
		
		$("#btn_save").on('click',function(){
			
			/*
				json으로 Data 가져가기 위해 hidden 처리된 textArea에 value 입력
				ckEditor 사용하면 text 앞뒤로 <p>가 들어가서  substring 사용하여 가공한다
			*/
			
			var content = CKEDITOR.instances.content.getData();
			
			content = content.substring(3,content.length-5);

			$("#content").val(content);
				
			// form send event
			var $submitform = $('#submitform');
			
			var part = $("#part").val();
			
			if(part == "D020000" || part == ""){
				alert(getMessage("board.partSelect")); //머릿글 구분을 선택해주세요
				return;
			}
			
			// input type Validation
	    	if(!fnValidationCheckForInput($submitform)){
	    		return;
	    	}
			
	    	// input type Validation
	    	if(!fnValidationCheckForTextArea($submitform)){
	    		return;
	    	}
			
			var type = "post";
			var url = "/board/insertQna";

			// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
			var param = $submitform.serializeObject();
			
			ajaxCall(type, url, param, sendFrom);
			
		});
		
	};

	var sendFrom = function(data){
		if(data.errCode == true) {
			alert(getMessage("alert.proc.end", ["button.insert"]));//등록이 완료되었습니다.
		    //목록화면으로 이동
			location.href = "/board/selectQna";
            
		} else {
			alert(getMessage("alert.proc.err", ["button.insert"]));  // 등록 중 오류가 발생했습니다.
		}
	}
	
</script>
</head>
<body>
    <div class="frame">
		<div class="frame_wrap">
			<%// gnb include%>
			<%@ include file="/WEB-INF/views/common/incGnb.jsp"%>

			<div class="sub_conts">
				<article>
				<div class="tit_wrap">
					<h3><spring:message code="board.menuServicecenter" text="고객센터" /></h3>
				</div>
				
					<ul class="tab">
                        <li class="on"><a href="/board/selectQna"><spring:message code="board.menuQna" text="Q&A" /></a></li>
                        <li><a href="/board/selectFaq"><spring:message code="board.menuFaq" text="FAQ" /></a></li>
               		</ul>
                  		
                  	<ul class="tab_conts">
                        <li>
                            <div class="tit_wrap mt0">
                                <h4 class="subtit"><spring:message code="board.qnainsert" text="문의 작성" /></h4>
                            </div>
                            <form name="submitform" id="submitform" method="post">
							<input type="hidden" id="division" name="division" value="D010002">
							
                            <table class="tbtype">         
                                <colgroup>
                                    <col class="cper1500">
                                    <col class="cperauto">
                                </colgroup>                       
                                <tbody>
                                    <tr>
                                        <td>
                                            <c:set var="partName"><spring:message code="board.part" text="구분" /></c:set>
											<select class="select-ajax" name="part" id="part"
											data-codetype="" data-code="" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "D020000" }'></select>
                                        </td>
                                        <td class="nolline">
                                        	<c:set var="title"><spring:message code="board.title" text="제목" /></c:set>
                                            <input type="text" class="cper100p" name="title" id="title" 
		                                    maxlength="100" data-required="Y" data-label="${title}" placeholder="${title}">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <div class="editor">
                                            	<c:set var="content"><spring:message code="board.content" text="문의내용" /></c:set>
                                                <textarea class="cper100p" style="height:150px;" id="content" name="content"
												data-required="Y" data-label="${content}" placeholder="${content}"></textarea>
                                            </div>
                                        </td>
                                    </tr>
                                   
                                </tbody>
                            </table>
                            </form>
                            
                            <div class="button">
                                <button type="button" class="btn_type5" id="btn_list"><span><spring:message code="button.list" text="목록" /></span></button>
                                <button type="button" class="btn_type2" id="btn_save"><span><spring:message code="button.complete" text="완료" /></span></button>
                            </div>
                        </li>
                    </ul>	
				</article>
			</div>
		
		</div>
	</div>

    <%// footer include %>
    <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
    
</body>
</html>