<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
<!-- 
**************************************************
화면코드 :UI_SYS_542_BAS 
기능설명 : 관리자 목록 상세조회
Author   Date      Description
 김윤홍     2020-01-07  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
<head>
<%
	// header include
%>
<%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%>

<title>관리자 목록 조회</title>
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
    	var $submitform = $('#submitform');
        bindSelectAjax($submitform);
    	
    };
    
    var bindEvent = function () {
    	
    	$("#adminId").attr("readonly",true); 
    	$("#adminName").attr("readonly",true); 
    	$("#adminRegDate").attr("readonly",true); 
	
		//관리자 수정
		$("#btn_edit").on('click',function(){
			
			if(fnValidationCheckForInput($("#submitform"))) {
				//관리자구분 필수값 확인
				var adminDepart = $("#adminDepart").val();
				if(adminDepart == "I010000" || adminDepart == ""){
		        	alert(getMessage("admin.part")+getMessage("alert.selectCheck"));
		        	return;
		        }
				
				//사용여부 필수값 확인
				var adminUseYn = $("#adminUseYn").val();
			       
				if(adminUseYn == "K010000" || adminUseYn == ""){
		        	alert(getMessage("delivery.shipcompanyYn")+getMessage("alert.selectCheck"));
		        	return;
		        }
				
		        // form send event
		    	var $submitform = $('#submitform');
				var type = "post";
				var url = "/admin/admin/editAdminList";
				
				// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
		        var param = $submitform.serializeObject(); 
				
				ajaxCall(type, url, param, sendFrom);
		        
			}
		});
		
		//목록화면 이동
		$("#btn_List").on('click',function(){
			// 목록화면으로 이동
			location.href = '/admin/admin/adminList';
		});
		
		//비밀번호 수정 팝업 창 
        $(document).on("click","#btn_pwd",function(){       
            editPassword();
        });
	
    };
    
    var sendFrom = function(data){
		if(data.errCode == true) {
			alert(getMessage("alert.proc.end", ["button.edit"]));//수정이 완료되었습니다.
		    location.href = '/admin/admin/adminList';
		} else {
			alert(getMessage("alert.proc.err", ["button.edit"]));  // 수정 중 오류가 발생했습니다.
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
						<h2><spring:message code="admin.adminmanage" text="관리자 관리"/></h2>
						<form name="submitform" id="submitform" method="post">
						
						<table class="tbtype mt20">
                            <colgroup>
                                <col class="cper1500">
                                <col class="cperauto">
                            </colgroup>
                            <tbody>
                                <tr class="tline">
                                   <th class="t_center"><spring:message code="board.part" text="구분" /></th>
                                   <td><select class="select-ajax" id="adminDepart" name="adminDepart" data-required="Y" data-label="구분"
                                        data-codetype="" data-code="${adminData.adminDepart}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "I010000" }'></select>
                                   </td>
                                </tr>
                                <tr>
                                    <th class="t_center">
                                        <c:set var="eMail"><spring:message code="board.email" text="이메일" /></c:set>
                                        ${eMail}
                                    </th>
                                    <td>
                                        <p>${adminData.adminId}</p>
                                    </td>
                                    <input type="hidden" name="adminId" id="adminId" value="${adminData.adminId}"/>
                                </tr>
                                 <tr>
                                    <th class="t_center"><spring:message code="admin.pop.password" text="비밀번호" /></th>
                                    <td><input type="password" name="adminPasswd" id="adminPasswd" value=""/></td>
                                </tr>
                                <tr>
                                    <th class="t_center"><c:set var="name"><spring:message code="board.name" text="이름" /></c:set>
                                        ${name}
                                    </th>
                                    <td><p> ${adminData.adminName}</p></td>
                                    
                                </tr>
                                <tr>
                                    <th class="t_center"><c:set var="contact"><spring:message code="admin.pop.contact" text="연락처" /></c:set>
                                        ${contact}
                                    </th>
                                    <td>
                                    	<input type="text" class="cper50p" name="adminPhoneNumber" id="adminPhoneNumber" 
			                                    data-required="Y" data-label="${contact}" value="${adminData.adminPhoneNumber}" data-format="phone"/>
			                        </td>
                                </tr>
                                <tr>
                                    <th class="t_center"><c:set var="startDate"><spring:message code="admin.pop.startDate" text="시작일" /></c:set>
                                         ${startDate}
                                    </th>
                                    <td>
                                    	<div class="month"><input type="text" class="date" id="adminUseSdate" name="adminUseSdate" 
				 							placeholder="${startDate}" value="${adminData.adminUseSdate}" data-label="${startDate}" data-required='Y'></div>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <th class="t_center"><c:set var="endDate"><spring:message code="admin.pop.endDate" text="마감일" /></c:set>
                                         ${endDate}
                                    </th>
                                    <td>
                                    	<div class="month"><input type="text" class="date" id="adminUseEdate" name="adminUseEdate" 
				 							placeholder="${endDate}" value="${adminData.adminUseEdate}" data-label="${endDate}" data-required='Y'></div>
                                    </td>
                                </tr>
                                <tr class="tline">
                                   <th class="t_center"><spring:message code="delivery.shipcompanyYn" text="사용여부" /></th>
                                   <td><select class="select-ajax" id="adminUseYn" name="adminUseYn" data-required="Y" data-label="구분"
                                        data-codetype="etc" data-code="${adminData.adminUseYn}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "K010000" }'></select>
                                   </td>
                                </tr>
                                
                                
<!--                                 <tr> -->
<%--                                     <th class="t_center"><c:set var="auth"><spring:message code="admin.auth" text="권한" /></c:set> --%>
<%--                                     ${auth} --%>
<!--                                     </th> -->
<!--                                     <td class="t_center"> -->
<%-- 										<input type="radio" id="auth1" name="adminScopeId" value='scope1' <c:if test="${adminData.adminScopeId eq 'scope1'}">checked</c:if> /><label for="auth1"><span class="icon_ra"></span><span class="label_txt">관리자 권한1</span></label> --%>
<%--                                         <input type="radio" id="auth2" name="adminScopeId" value='scope2' <c:if test="${adminData.adminScopeId eq 'scope2'}">checked</c:if> /><label for="auth2" class="ml20"><span class="icon_ra"></span><span class="label_txt">관리자 권한2</span></label> --%>
<%--                                         <input type="radio" id="auth3" name="adminScopeId" value='scope3' <c:if test="${adminData.adminScopeId eq 'scope3'}">checked</c:if> /><label for="auth3" class="ml20"><span class="icon_ra"></span><span class="label_txt">관리자 권한3</span></label> --%>
<%--                                         <input type="radio" id="auth4" name="adminScopeId" value='scope4' <c:if test="${adminData.adminScopeId eq 'scope4'}">checked</c:if> /><label for="auth4" class="ml20"><span class="icon_ra"></span><span class="label_txt">관리자 권한4</span></label> --%>
<%--                                         <input type="radio" id="auth5" name="adminScopeId" value='scope5' <c:if test="${adminData.adminScopeId eq 'scope5'}">checked</c:if> /><label for="auth5" class="ml20"><span class="icon_ra"></span><span class="label_txt">관리자 권한5</span></label> --%>
<!--                                     </td> -->
<!--                                 </tr> -->
                            </tbody>
                        </table>
                        </form>
                  
                    <div class="foot_btn"> 
                        <button type="button" class="btn_type5" id="btn_List"><span><spring:message code="button.list" text="목록" /></span></button>
                        <button type="button" class="btn_type2" id="btn_edit"><span><spring:message code="button.edit" text="수정" /></span></button>
                    </div>

				</article>
			</div>
		</div>

		<!--// #### Content area ############### -->

	</div>

	<%// footer include	%>
	<%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
</body>
</html>