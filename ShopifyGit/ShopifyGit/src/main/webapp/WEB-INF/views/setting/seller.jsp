<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_ADM_511_BAS
기능설명 : 설정관리 > 계정관리
Author   Date         Description
jwh      2019-12-23   First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%> 
    
    <title>Title</title>
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
    	// 기본 정보 수정 하기 
        $(document).on("click",".edit",function(){	
        	
        	var $submitform = $('#submitform');
        	
        	// input type Validation
        	if(!fnValidationCheckForInput($submitform)){
        		return;
        	}
            
            if(confirm(getMessage("alert.proc", ["settings.seller", "button.edit"]))){  // 기본정보를 수정하시겠습니까?
                var type = "post";
                var url = "/setting/editSeller";

                // 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
                var param = $submitform.serializeObject(); 
                
                console.log(param);
                
                ajaxCall(type, url, param, sendFrom);   
            }
        });
        
    	// 쇼핑몰 추가 팝업 오픈 
        $(document).on("click",".add",function(){   	
        	openPop("/setting/popup/popChannl", "", 450);
        });
    	
        // 쇼핑몰 연동 처리 (popup)
        $(document).on("click",".join",function(){
            var $submitform = $('#authShop');
            
            // input type Validation & submit
            if(fnValidationCheckForInput($submitform)){
                $submitform.submit(); 
            }            
        });
        
        // check box 컨트롤
        $(document).on("click","input[name='allCheck']",function(){
        	allCheck("ckBox", this);
        });
        
    	// 쇼핑몰 삭제 처리 
        $(document).on("click",".del",function(){	
        	
        	if(!$("input[name='ckBox']").is(":checked")){
        		alert( getMessage("alert.deleteChk") ); // 삭제할 대상을 선택해주세요
        		return;
        	}
        	
        	if(confirm(getMessage("alert.deleteConfirm"))){  // 선택항목을 삭제 하시겠습니까?
        		var $submitform = $('#shopform');
        		var type = "post";
        		var url = "/setting/deleteMultiShop";
        		
        		var param = $submitform.serializeObject();
        		ajaxCall(type, url, param, delFrom);
        	}
        });

        // 삽연동 설정 변경
        $(document).on("click",".use-check",function(){
        	var type = "post";
            var url = "/setting/editUseShop";
            
            var useYn = $(this).closest("tr").data("useyn") == "Y" ? "N" : "Y";
            var shopIdx = $(this).closest("tr").data("shopidx");
            
            var param = {"useYn":useYn, "shopIdx":shopIdx}
            
            $(this).addClass("use-edit"); // 수정 데이터 체크
            ajaxCall(type, url, param, useFrom);
        });
    };

    // 연동상태 변경
    var useFrom = function(data) {
    	if(data.errCode == true) {
    		data.useYn == "Y" ? $(".use-edit").addClass("on") : $(".use-edit").removeClass("on") 
    		$(".use-edit").closest("tr").data("useyn", data.useYn);
        } else {
            alert(getMessage("alert.proc.err", ["button.delete"]));  // 수정 중 오류가 발생했습니다.
        }

    	$(".use-edit").removeClass("use-edit"); // 수정 데이터 원복
    }
    
    // 삭제 완료
    var delFrom = function(data) {
        if(data.errCode == true) {
            alert(getMessage("alert.proc.end", ["button.delete"]));  // 수정이 완료 되었습니다.
            location.reload();
        } else {
            alert(getMessage("alert.proc.err", ["button.delete"]));  // 수정 중 오류가 발생했습니다.
        }
    }
    
    // 기본 정보 수정완료
	var sendFrom = function(data) {
		if(data.errCode == true) {
		    alert(getMessage("alert.proc.end", ["button.edit"]));  // 수정이 완료 되었습니다.
            location.reload();
		} else {
			alert(getMessage("alert.proc.err", ["button.edit"]));  // 수정 중 오류가 발생했습니다.
		}
	}
    </script>
</head>
<body>
    <div class="frame">
        <div class="frame_wrap">
        
        
	    <%// gnb include %>
	    <%@ include file="/WEB-INF/views/common/incGnb.jsp"%> 
    
            <div class="sub_conts">
                <article>
                
                <!-- #### Content area ############### -->
                
                    <!-- tit_wrap -->
                    <div class="tit_wrap">
                        <h3><spring:message code="incgnb.settings" text="설정" /></h3>
                        <div class="sect_right">
                        <!-- 기본 검색 영역 -->

                        <!--// 기본 검색 영역 -->
                        </div>
                    </div> 
                    <!--// tit_wrap end --> 
                    
                    <ul class="tab">
                        <li class="on"><a href="/setting/seller"><spring:message code="settings.menuSeller" text="계정관리" /></a></li>
                        <li><a href="/setting/listSender"><spring:message code="settings.menuDelivery" text="배송관리" /></a></li>
                    </ul>
                    
                    <ul class="tab_conts">
                        <li>
                            
                            <!-- 판매 채널 연동 -->
                            
                                <div class="tit_wrap mtb0">
	                                <h4 class="subtit"><spring:message code="settings.channel.title" text="판매 채널 연동" /></h4>
	                            </div>
	                            <div class="module_group">
	                                <div class="btn_group">
	                                    <button type="button" class="btn_type1 add"><span><spring:message code="button.add" text="추가" /></span></button>
                                            <button type="button" class="btn_type1 del"><span><spring:message code="button.delete" text="삭제" /></span></button>
	                                </div>
	                            </div>

                                    
                            <c:choose>
                                <c:when test="${fn:length(list) == 0}">
                                    
                                    <div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
                                
                                </c:when>
                                <c:otherwise>
                                    <form name="shopform" id="shopform">
                                    <table class="tbtype">
                                        <colgroup>
                                            <col class="wp35">
                                            <col class="cperauto"/>
                                            <col class="cperauto"/>
                                            <col class="cperauto"/>
                                            <col class="wp80"/>
                                        </colgroup>
                                        <thead>
                                            <tr>
                                                <th><input type="checkbox" name="allCheck" value="Y" id="ind01"><label for="ind01"><span class="icon_ck" ></span></label></th>
                                                <th><spring:message code="settings.channel" text="채널" /></th>
                                                <th><spring:message code="settings.shopName" text="쇼핑몰명" /></th>
                                                <th><spring:message code="settings.url" text="URL" /></th>
                                                <th><spring:message code="settings.joinstatus" text="연동상태" /></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            
                                        <c:forEach items="${list}" var="item" varStatus="status">
                                            
                                            <tr data-shopidx="${item.shopIdx}" data-useyn="${item.useYn}">
                                                <td><input type="checkbox" name="ckBox" value="${item.shopIdx}" id="ind02_${status.count}"><label for="ind02_${status.count}"><span class="icon_ck" ></span></label></td>
                                                <td class="t_center"><p>${item.ecommerce}</p></td>
                                                <td class="t_center"><p>${item.shopName}</p></td>
                                                <td><a href="https://${item.shopName}.myshopify.com" target="_blank">${item.shopName}.myshopify.com</td>
                                                <td class="t_center"><span class="btn_contect <c:if test="${item.useYn eq 'Y'}">on</c:if> use-check"></span></td>
                                            </tr>

                                        </c:forEach>

                                        </tbody>    
                                    </table>
                                    </form>
                                    
                                </c:otherwise>
                            </c:choose>

                            <!--// 판매 채널 연동 -->
   
                            
                            <!-- 기본 정보 등록 -->
                                <form name="submitform" id="submitform" method="post">
                                <div class="tit_wrap mt20">
                                    <h4 class="subtit"><spring:message code="settings.seller.title" text="기본 정보 등록" /></h4>
                                </div>
                                <table class="tbtype">
                                    <colgroup>
                                        <col class="cper1200">
                                        <col class="cper0780">
                                        <col class="cperauto">
                                        <col class="cper0780">
                                        <col class="cperauto">
                                        <col class="cper1200">
                                        <col class="cperauto">
                                    </colgroup>
                                    <tbody>
                                        <tr>
                                            <th>
                                                <spring:message code="settings.koreanName" text="한글 이름" />
                                            </th>
                                            <th>
                                                <c:set var="lastName"><spring:message code="settings.lastName" text="성" /></c:set>
                                                ${lastName}
                                            </th>
                                            <td>
                                                <input type="text" class="calc50p" name="lastName" id="lastName" value="${seller.lastName}" 
                                                    maxlength="20" data-required="Y" data-label="${lastName}" placeholder="${lastName}">
                                            </td>
                                            <th>
                                                <c:set var="firstName"><spring:message code="settings.firstName" text="이름" /></c:set>
                                                ${firstName}
                                            </th>
                                            <td>
                                                <input type="text"  class="calc50p" name="firstName" id="firstName" value="${seller.firstName}" 
                                                    maxlength="20" data-required="Y" data-label="${firstName}" placeholder="${firstName}">
                                            </td>
                                             <th >
                                                <c:set var="rankName"><spring:message code="settings.sellerGrade" text="등급" /></c:set>
                                                ${rankName}
                                            </th>
                                           
                                            <td class="t_center"><p>${seller.rankName}</p></td>
                                           
                                           
                                        </tr>
                                        
                                        <tr>
                                          <th>
                                                <spring:message code="settings.englishName" text="영문 이름" />
                                            </th>
                                          <th>
                                                <c:set var="lastNameEname"><spring:message code="settings.lastNameEname" text="Last Name" /></c:set>
                                                ${lastNameEname}
                                            </th>
                                            <td>
                                                <input type="text" class="calc50p" name="lastNameEname" id="lastNameEname" value="${seller.lastNameEname}" 
                                                    maxlength="20" data-required="Y" data-label="${lastNameEname}" placeholder="${lastNameEname}">
                                            </td>
                                            <th>
                                                <c:set var="firstNameEname"><spring:message code="settings.firstNameEname" text="First Name" /></c:set>
                                                ${firstNameEname}
                                            </th>
                                            <td>
                                                <input type="text"  class="calc50p" name="firstNameEname" id="firstNameEname" value="${seller.firstNameEname}" 
                                                    maxlength="20" data-required="Y" data-label="${firstNameEname}" placeholder="${firstNameEname}">
                                            </td>
                                             <th >
                                                <c:set var="companyNum"><spring:message code="settings.companyNum" text="사업자등록번호" /></c:set>
                                                ${companyNum}
                                            </th>
                                            <td >
                                                <input type="text" name="companyNum" id="companyNum" value="${seller.companyNum}" 
                                                        maxlength="20" data-format="phone" data-required="Y" data-label="${companyNum}" placeholder="${companyNum}">
                                            
                                           
                                        </tr>
                                        
                                        <tr>
                                            <th>
                                                <c:set var="email"><spring:message code="settings.email" text="이메일" /></c:set>
                                                ${email}
                                            </th>
                                            <td colspan="2">
                                                ${seller.email}
                                                <input type="hidden" class="cper5000" name="email" id="email" value="${seller.email}" 
                                                    readonly maxlength="50" data-format="" data-required="Y" data-label="${email}" placeholder="${email}">
                                            </td>
                                            <th>
                                                <c:set var="phoneNumber"><spring:message code="settings.phoneNumber" text="전화번호" /></c:set>
                                                ${phoneNumber}
                                            </th>
                                            <td colspan="3">
                                            	<div class="phone4">
                                            		<c:set var="phoneText01"><spring:message code="common.phone.01" text="국가번호" /></c:set>
                                					<c:set var="phoneText02"><spring:message code="common.phone.02" text="국번" /></c:set>
                                					<c:set var="phoneText03"><spring:message code="common.phone.03" text="중간자리" /></c:set>
                                					<c:set var="phoneText04"><spring:message code="common.phone.04" text="끝자리" /></c:set>
				                                    <input type="text" name="phoneNumber01" id="phoneNumber01" maxlength="4" value="${seller.phoneNumber01}"
				                                        data-format="number" data-required="Y" data-label="${phoneText01}" placeholder="${phoneText01}">
				                                    <input type="text" name="phoneNumber02" id="phoneNumber02" maxlength="5" value="${seller.phoneNumber02}"
				                                        data-format="number" data-required="Y" data-label="${phoneText02}" placeholder="${phoneText02}">
				                                    <input type="text" name="phoneNumber03" id="phoneNumber03" maxlength="5" value="${seller.phoneNumber03}"
				                                        data-format="number" data-required="Y" data-label="${phoneText03}" placeholder="${phoneText03}">
				                                    <input type="text" name="phoneNumber04" id="phoneNumber04" maxlength="5" value="${seller.phoneNumber04}"
				                                        data-format="number" data-required="Y" data-label="${phoneText04}" placeholder="${phoneText04}">
				                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>
                                                <spring:message code="settings.company" text="회사명" />
                                            </th>
                                            <th>
                                                <spring:message code="settings.korean" text="한글" />
                                            </th>
                                            <td>

                                                <input type="text" class="calc50p" name="company" id="company" value="${seller.company}" 
                                                        maxlength="50" placeholder="<spring:message code="settings.korean" text="한글" />">
                                             </td>
                                            <th>
                                                <spring:message code="settings.English" text="영문" />
                                            </th>
                                             <td colspan="3">           
                                                <input type="text" class="calc50p" name="companyEname" id="companyEname" value="${seller.companyEname}" 
                                                        maxlength="50" placeholder="<spring:message code="settings.English" text="영문" />">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table> 
                                <div class="button">
                                    <button type="button" class="btn_type2 edit"><span><spring:message code="button.edit" text="수정" /></span></button>
                                </div>
                                </form>
                            
<%--                             
                                    <div class="module_group">
                                        <div class="tit_wrap mt20">
                                            <h3><spring:message code="settings.seller.title" text="기본 정보 등록" /></h3>
                                        </div>
                                        
                                         <div class="action">
                                            <button type="button" class="btn_type1 edit"><span><spring:message code="button.edit" text="수정" /></span></button>
                                        </div>
                                    </div>
                                    

                                    <form name="submitform" id="submitform" method="post">

                                    <table class="tbtype">
                                        <colgroup>
                                            <col class="cper1500"/>
                                            <col class="cper3500"/>
                                            <col class="cper1500"/>
                                            <col class="cper3500"/>
                                        </colgroup>
                                        <tbody>
                                            <tr>
                                                <th><spring:message code="settings.name" text="이름" /></th>
                                                <td>
                                                    <c:set var="firstName"><spring:message code="settings.firstName" text="이름" /></c:set>
                                                    <c:set var="lastName"><spring:message code="settings.lastName" text="성" /></c:set>
                                                    <input type="text" class="cper5000" name="firstName" id="firstName" value="${seller.firstName}" 
                                                        maxlength="20" data-required="Y" data-label="${firstName}" placeholder="${firstName}">
                                                    <input type="text" class="cper5000" name="lastName" id="lastName" value="${seller.lastName}" 
                                                        maxlength="20" data-required="Y" data-label="${lastName}" placeholder="${lastName}">
                                                </td>
                                                <th>
                                                    <c:set var="companyNum"><spring:message code="settings.companyNum" text="사업자등록번호" /></c:set>
                                                    ${companyNum}
                                                </th>
                                                <td>
                                                    <input type="text" class="cper5000" name="companyNum" id="companyNum" value="${seller.companyNum}" 
                                                        maxlength="20" data-required="Y" data-label="${companyNum}" placeholder="${companyNum}">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>
                                                    <c:set var="email"><spring:message code="settings.email" text="이메일" /></c:set>
                                                    ${email}
                                                </th>
                                                <td>
                                                    <input type="text" class="cper5000" name="email" id="email" value="${seller.email}" 
                                                        maxlength="20" data-required="Y" data-label="${email}" placeholder="${email}">

                                                </td>
                                                <th>
                                                    <c:set var="phoneNumber"><spring:message code="settings.phoneNumber" text="전화번호" /></c:set>
                                                    ${phoneNumber}
                                                </th>
                                                <td>
                                                    <input type="text" class="cper5000" name="phoneNumber" id="phoneNumber" value="${seller.phoneNumber}" 
                                                        maxlength="20" data-required="Y" data-label="${phoneNumber}" placeholder="${phoneNumber}">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>
                                                    <c:set var="company"><spring:message code="settings.company" text="회사명(국문)" /></c:set>
                                                    ${company}
                                                </th>
                                                <td>
                                                    <input type="text" class="cper5000" name="company" id="company" value="${seller.company}" 
                                                        maxlength="20" data-required="Y" data-label="${company}" placeholder="${company}">
                                                </td>
                                                <th>
                                                    <c:set var="companyEname"><spring:message code="settings.companyEname" text="회사명(영문)" /></c:set>
                                                    ${companyEname}
                                                </th>
                                                <td>
                                                    <input type="text" class="cper5000" name="companyEname" id="companyEname" value="${seller.companyEname}" 
                                                        maxlength="20" data-required="Y" data-label="${companyEname}" placeholder="${companyEname}">
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    </form>  --%>                        
                            
                            <!--// 기본 정보 등록 -->
                        </li>
                    </ul>
                        
                <!--// #### Content area ############### -->        
                
                
                </article>
            </div>
   
        </div>
    </div>    
   
    <%// footer include %>
    <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>

</body>
</html>