<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_ADM_521_BAS 
기능설명 : 설정관리 > 배송관리 > 출고지설정
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
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
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
    	$(document).on("click",".add",function(){       
            openPop("/setting/popup/popAddSender", "", 600);
        });
    	
    	$(document).on("click",".zipcode-pop",function(){       
            serchZipCode();
        });
    	
    	// 출고지 등록 (popup)
        $(document).on("click",".save",function(){  
            
            var $submitform = $('#submitform');
            
            // input type Validation
            if(!fnValidationCheckForInput($submitform)){
                return;
            }
            
            if(confirm(getMessage("alert.proc", ["settings.menuSender", "button.insert"]))){  // 출고지를 수정하시겠습니까?
                var type = "post";
                var url = "/setting/insertSender";

                // 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
                var param = $submitform.serializeObject(); 
                ajaxCall(type, url, param, sendFrom);   
            }
        });
    	
        // 출고지 수정 
        $(document).on("click",".list-edit",function(){  
        	var idx = $(this).closest("li").data("idx");
        	var url = "/setting/popup/popAddSender?idx=" + idx;
        	openPop(url, "", 600);
        });
    	
        // 출고지 수정 (popup)
        $(document).on("click",".edit",function(){  
            
            var $submitform = $('#submitform');
            
            // input type Validation
            if(!fnValidationCheckForInput($submitform)){
                return;
            }
            
            if(confirm(getMessage("alert.proc", ["settings.menuSender", "button.edit"]))){  // 출고지를 수정하시겠습니까?
                var type = "post";
                var url = "/setting/updateSender";
                
                // 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
                var param = $submitform.serializeObject(); 
                ajaxCall(type, url, param, sendFrom);   
            }
        });
    	
        // 출고지 삭제
        $(document).on("click",".list-del",function(){   
        	if(confirm(getMessage("alert.proc", ["settings.menuSender", "button.delete"]))){  // 출고지를 수정하시겠습니까?
                var type = "post";
                var url = "/setting/deleteSender";
                var senderIdx = $(this).closest("li").data("idx");
                var param = {"senderIdx":senderIdx}
                
                ajaxCall(type, url, param, sendFrom);   
            }
        });
    };

    // 프로세스 완료 콜백
    var sendFrom = function(data) {
        if(data.errCode == true) {
            alert(getMessage("alert.proc.end", [data.procCode]));  // 등록(수정)이 완료 되었습니다.
            location.reload();
        } else {
            alert(getMessage("alert.proc.err", [data.procCode]));  // 등록(수정) 중 오류가 발생했습니다.
        }
    }
    
    // 다음 우편번호 찾기
    var serchZipCode = function() {
    	new daum.Postcode({
            oncomplete: function(data) {
            	 var arr = data.roadAddressEnglish.split(/,\s+/); // 도로명 주소 변수
                 var city;
                 var province;
                 
                 var len = arr.length;
                 if ( arr[len-2].match("-do$") ) {
                     city = arr[len-3];
                     province = arr[len-2];
                 } else {
                     city = arr[len-2];
                     //province = "N/A";
                     province = city;
                 }

                document.getElementById('zipCode').value = data.zonecode;
                document.getElementById("addr1").value = data.roadAddress;
                document.getElementById("addr1Ename").value = data.roadAddressEnglish;
                document.getElementById("province").value = province;
                document.getElementById("city").value = city;
            }
        }).open();
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
                        <li><a href="/setting/seller"><spring:message code="settings.menuSeller" text="계정관리" /></a></li>
                        <li class="on"><a href="/setting/listSender"><spring:message code="settings.menuDelivery" text="배송관리" /></a></li>
                    </ul>
                    
                    <ul class="tab_conts">
                        <li>
                            <div class="sub_tab">
                                <a href="/setting/listSender" class="on"><spring:message code="settings.menuSender" text="출고지" /></a>
                                <a href="/setting/listBox"><spring:message code="settings.menuBox" text="포장재" /></a>
                                <a href="/setting/listSku"><spring:message code="settings.menuSku" text="관세정보" /></a>
                                <a href="/setting/listCourier"><spring:message code="settings.menuCourier" text="배송사" /></a>
                            </div>
                            <div class="sub_tab_conts">
                                <div>
                                    <div class="module_group">
                                        <div class="btn_group">
                                            <button type="button" class="btn_type1 add"><span><spring:message code="button.add" text="추가" /></span></button>
                                        </div>
                                    </div>
                                    
                                    
                            <c:choose>
						        <c:when test="${fn:length(list) == 0}">
						         
						            <div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
						        
						        </c:when>
						        <c:otherwise>
						        
						            <ul class="delivery">
						        
						            <c:forEach items="${list}" var="item">
						            
						                <li <c:if test="${item.useDefault eq 'Y'}">class="basic"</c:if> data-idx="${item.senderIdx}" data-usedefault="${item.useDefault}">
						                    <div class="d_info">
						                        <dl>
                                                    <dt><spring:message code="settings.sender.name" text="출고지명" /></dt>
                                                    <dd>${item.senderTitle}</dd>
                                                </dl>
                                                <dl>
                                                    <dt><spring:message code="settings.phoneNumber" text="전화번호" /></dt>
                                                    <dd>${item.phoneNumber}</dd>
                                                </dl>
                                                <dl>
                                                    <dt><spring:message code="settings.addr1" text="주소" /></dt>
                                                    <dd>
                                                        ${item.addr1} <br>
                                                        ${item.addr2}
                                                    </dd>
                                                </dl>
                                                <dl>
                                                    <dt><spring:message code="settings.addr1Ename" text="영문주소" /></dt>
                                                    <dd>
                                                        ${item.addr1Ename} <br>
                                                        ${item.addr2Ename} <br>
                                                        ${item.province} <br>
                                                        ${item.city} <br>
                                                    </dd>
                                                </dl>
                                                <dl>
                                                    <dt><spring:message code="settings.zipCode" text="우편번호" /></dt>
                                                    <dd>${item.zipCode}</dd>
                                                </dl>
                                                <div class="btn_col col2">
                                                    <button type="button" class="btn_type5 list-del"><spring:message code="button.delete" text="삭제" /></button>
                                                    <button type="button" class="btn_type2 list-edit"><spring:message code="button.edit" text="수정" /></button>
                                                </div>
						                    </div>
						                </li>
						            </c:forEach>

						            </ul>
						           
						        </c:otherwise>
						    </c:choose>
                                   
                                </div>
                            </div>
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