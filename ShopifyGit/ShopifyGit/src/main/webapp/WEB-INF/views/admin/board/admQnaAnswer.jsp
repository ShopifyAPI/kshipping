<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
<!-- 
**************************************************
화면코드 :UI_SYS_422_BAS 
기능설명 : Q&A 답변등록 화면
Author   Date      Description
 김윤홍     2019-12-23  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 
        
    <title>ADM Q&A 답변작성</title>
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

	};

	var bindEvent = function() {
		

	   CKEDITOR.replace('answer'
	        , {height: 277                                                  
       });
		
	   CKEDITOR.instances.answer.on('change', function(e) {
		 	var answer = CKEDITOR.instances.answer.getData().length;
		    $('#counter').html("("+Number(answer-8)+" / 최대 300자)");    //글자수 실시간 카운팅
	   });
		
// 		$("#ansDate").attr("readonly",true);
// 		$("#upWriter").attr("readonly",true);
		
		//답변등록
		$("#btn_save").on('click',function(){
			
			/*
			json으로 Data 가져가기 위해 hidden 처리된 textArea에 value 입력
			ckEditor 사용하면 text 앞뒤로 <p>가 들어가서  substring 사용하여 가공한다
			*/
			var answer = CKEDITOR.instances.answer.getData();
			answer = answer.substring(3,answer.length-5);
			$("#answer").val(answer);
			
			// form send event
	    	var $submitform = $('#submitform');
		    
	    	// textArea type Validation
	    	if(!fnValidationCheckForTextArea($submitform)){
	    		return;
	    	}
		    	
	    	var type = "post";
			var url = "/admin/board/updateBoardAnswer";

			// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
	        var param = $submitform.serializeObject(); 
	        
			ajaxCall(type, url, param, sendFrom);
			
		});
		
		//목록화면 이동
		$("#btn_list").on('click',function(){
			
			location.href = "/admin/board/selectQna";
			
		});
		
	};
	
	var sendFrom = function(data){
		if(data.errCode == true) {
			alert(getMessage("alert.proc.end", ["button.insert"]));//등록이 완료되었습니다.
		    //목록화면으로 이동	 		
		    location.href = "/admin/board/selectQna";
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
               	    <h2><spring:message code="board.qnamanage" text="Q&A 관리"/></h2>
               	    <form name="submitform" id="submitform" method="post">
               	    <input type="hidden" name="idx" id="idx" value="${adminBoardData.idx}">
                    <input type="hidden" name="status" id="status" value="${adminBoardData.status}">
               	    	<table class="tbtype mt20">         
                            <colgroup>
                                <col class="cper1500">
                                <col class="cperauto">
                                <col class="cper1500">
                                <col class="cperauto">
                            </colgroup>                       
                            <tbody>
                                <tr class="tline">
                                    <th class="t_center">
                                        <p>[ ${adminBoardData.partName} ]</p>
                                    </th>
                                    <td colspan="3">
                                    	<c:set var="title"><spring:message code="board.title" text="제목" /></c:set>
                                        <p>${adminBoardData.title}</p>
                                    </td>
                                 
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <div class="editor view">
                                        	<c:set var="content"><spring:message code="board.content" text="질문" /></c:set>
                                            <p>${adminBoardData.content}</p>
                                        </div>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <th class="t_center">
                                       	<c:set var="answerDate"><spring:message code="board.answerDate" text="답변일" /></c:set>
                                       	${answerDate}
                                    </th>
                                    
                                    <td class="t_center">
                                    	<p>${adminBoardData.ansDate}</p>
                                    </td>
                                    
                                    <th class="t_center">
                                       	<c:set var="answerWriter"><spring:message code="board.answerWriter" text="답변작성자" /></c:set>
                                       	${answerWriter}
                                    </th>
                                    
                                    <td class="t_center">
                                    	<p>${adminBoardData.depart} (${adminBoardData.upWriter})</p>
                                    </td>
                                    
                                </tr>
                                
                                <tr>
                                    <td colspan="4">
                                        <div class="editor">
                                        	<c:set var="answerText"><spring:message code="board.answerText" text="답변내용" /></c:set>
                                            <textarea id="answer" name= "answer" data-label="${answerText}" data-required='Y' placeholder="${answerText}">${adminBoardData.answer}</textarea>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
               	    
               	    </form>
                   
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
    <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
</body>
</html>