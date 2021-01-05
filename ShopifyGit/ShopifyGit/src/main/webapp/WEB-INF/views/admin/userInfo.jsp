<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
<!-- 
**************************************************
화면코드 :UI_SYS_542_BAS 
기능설명 : 관리자 개인정보
Author   Date      Description
 김윤홍     2020-02-24  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
<head>
<%
    // header include
%>
<%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%>
    <title>관리자 개인정보</title>
    
    <script type="text/javascript">

    // READY
    $(function () {
        initialize();
    });
    
    var initialize = function () {
        initControls();
        bindEvent();   
    };
    
    var initControls = function () {
       
    };
    
    var bindEvent = function () {
    	$(document).on("click",".edit",function(){       
            editPassword();
        });
    };
    
    var editPassword = function(data){
    	var $submitform = $('#submitform');
    	
    	// input type Validation
        if(!fnValidationCheckForInput($submitform)){
            return;
        }
    	
    	if($("#adminPasswd").val() != $("#adminPasswdConf").val()) {
    		alert(getMessage("admin.pop.password.conftext"));
    		$("#adminPasswd").focus();
    		return;
    	}
    	
        var url = "/admin/userInfo/editPassword";
        var param = $submitform.serializeObject(); 
        
        $submitform.action = url;
        $submitform.submit();
        //ajaxCall("post", url, param, sendForm);   
    } 
    
    var sendForm = function(data){
    	if(data.errCode == true) {
            alert(getMessage("alert.proc.end", [data.procCode]));  // 등록(수정)이 완료 되었습니다.
            location.reload();
        } else {
            alert(getMessage("alert.proc.err", [data.procCode]));  // 등록(수정) 중 오류가 발생했습니다.
        }
    }
    </script>
</head>
<body>
    <div class="wrap">
        <%
            // gnb include
        %>
        <%@ include file="/WEB-INF/views/common/incAdmGnb.jsp"%>

        <!-- #### Content area ############### -->
        
        
        <div class="contents">
           <div class="cont_body">
               <article>
                    <h2>개인정보 관리</h2>
                        <form name="submitform" id="submitform" method="post">
                        <table class="tbtype mt20">
                            <colgroup>
                                <col class="cper1500">
                                <col class="cperauto">
                            </colgroup>
                            <tbody>
                                <tr class="tline">
                                   <th><spring:message code="admin.authdate" text="권한기간" /></th> 
                                   <td><p>${adminData.adminUseSdate} ~ ${adminData.adminUseEdate}</p></td>
                                </tr>
                                <tr>
                                    <th><spring:message code="admin.pop.email" text="이메일" /></th>
                                    <td><p>${adminData.adminId}</p></td>
                                </tr>
                                <tr>
                                    <th><spring:message code="board.name" text="이름" /></th>
                                    <td><p>${adminData.adminName}</p></td>
                                </tr>
                                <tr>
                                    <th>
                                        <c:set var="contact"><spring:message code="admin.pop.contact" text="연락처" /></c:set>
                                        ${contact}
                                    </th>
                                    <td>
                                        <input type="text" name="phoneNumber" id="adminPhoneNumber" 
                                                data-required="Y" data-label="${contact}" value="${adminData.adminPhoneNumber}" data-format="phone"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>
                                        <c:set var="password"><spring:message code="admin.pop.password" text="비밀번호" /></c:set>
                                        ${password}
                                    </th> 
                                    <td>
                                        <input type="password" name="passwd" id="adminPasswd" 
                                            data-required="Y" data-label="${password}" placeholder="${password}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>
                                        <c:set var="passwordconf"><spring:message code="admin.pop.passwordconf" text="비밀번호확인" /></c:set>
                                        ${passwordconf}
                                    </th>
                                    <td>
                                        <input type="password" name="passwdConf" id="adminPasswdConf" 
                                            data-required="" data-label="${passwordconf}" placeholder="${passwordconf}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>가입일</th>
                                    <td><p>${adminData.adminRegDate}</p></td>
                                </tr>
                                
                            </tbody>
                        </table>
                        </form>
                    <div class="foot_btn">
                        <button type="button" class="btn_type2 edit"><spring:message code="button.edit" text="수정" /></button>
                    </div>
                    
               </article>
           </div>
        </div>
    </div>
    
    <%// footer include %>
    <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
</body>
</html>