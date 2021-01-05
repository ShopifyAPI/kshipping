<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_SYS_412_BAS 
기능설명 : ADM FAQ 등록
Author   Date      Description
 김윤홍     2019-12-23  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 
     
    <title>ADM FAQ 등록</title>
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
	
		CKEDITOR.replace('answer'
		        , {height: 277                                                  
		         });
		
	   $("#btn_save").on('click',function(){
			
			/*
			json으로 Data 가져가기 위해 hidden 처리된 textArea에 value 입력
			ckEditor 사용하면 text 앞뒤로 <p>가 들어가서  substring 사용하여 가공한다
			ckEditor에서 공백은 db에 &nbsp;로 들어가게 되어 정규포현식으로 제거함
			*/
			
			var answer = CKEDITOR.instances.answer.getData();
			answer = answer.substring(3,answer.length-5);
			$("#answer").val(answer.replace(/&nbsp;/gi,""));
			
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
		 	
	    	// textArea type Validation
	    	if(!fnValidationCheckForTextArea($submitform)){
	    		return;
	    	}
	    	
	    	var type = "post";
			var url = "/admin/board/insertBoard";

			// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
	        var param = $submitform.serializeObject();
	        
			ajaxCall(type, url, param, sendFrom);
			
		});
		
	    //목록화면 이동
		$("#btn_list").on('click',function(){
			
			location.href = "/admin/board/selectFaq";
			
		});
	};
	
	var sendFrom = function(data){
		if(data.errCode == true) {
		    alert(getMessage("alert.proc.end", ["button.insert"]));  // 등록이 완료 되었습니다.
		    //목록화면으로 이동	 		
			location.href = "/admin/board/selectFaq";
		} else {
			
			alert(getMessage("alert.proc.err", ["button.insert"]));  // 등록 중 오류가 발생했습니다.
		}
	}

</script>
</head>

<body>
	<div class="wrap">
        
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incAdmGnb.jsp"%> 

        <!-- #### Content area ############### -->

        <div class="contents">
           <div class="cont_body">
               <article>
                    <h2><spring:message code="board.faqinsert" text="FAQ 등록"/></h2>
                    <form name="submitform" id="submitform" method="post">
                    <input type="hidden" id="division" name="division" value="D010001">
                   	<table class="tbtype mt20">         
                    	<colgroup>
                            <col class="cper1500">
                            <col class="cperauto">
                        </colgroup>
                        <tbody>
                            <tr class="tline">
                                <td>
                                    <c:set var="partName"><spring:message code="board.part" text="구분" /></c:set>
									<select class="select-ajax" id="part" name="part" data-codetype="" data-code="" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "D020000" }' 
									data-label="${partName}" data-required='Y'></select>
                                </td>
                                <td class="nolline">
                                	<c:set var="content"><spring:message code="board.content" text="질문" /></c:set>
                                    <input type="text" class="cper100p" id="content" name="content" maxlength="60" data-label="${content}" data-required='Y' placeholder="${content}">
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <div class="editor">
                                        <c:set var="answerText"><spring:message code="board.answerText" text="답변내용" /></c:set>
										<textarea class="cper100p" style="height:150px;" id="answer" name= "answer" data-label="${answerText}" data-required='Y' placeholder="${answerText}"></textarea></br>
                                    </div>
                                </td>
                            </tr>
                           
                        </tbody>
                       </table>
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