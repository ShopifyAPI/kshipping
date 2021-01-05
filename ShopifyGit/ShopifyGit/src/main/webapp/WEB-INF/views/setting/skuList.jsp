<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_ADM_541_BAS 
기능설명 : 설정관리 > 배송관리 > 관세정보
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
    <script type="text/javascript" src="/js/setting/skuList.js"></script>
    
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
                                <a href="/setting/listSender"><spring:message code="settings.menuSender" text="출고지" /></a>
                                <a href="/setting/listBox"><spring:message code="settings.menuBox" text="포장재" /></a>
                                <a href="/setting/listSku" class="on"><spring:message code="settings.menuSku" text="제품정보" /></a>
                                <a href="/setting/listCourier"><spring:message code="settings.menuCourier" text="배송사" /></a>
                            </div>
                            <div class="sub_tab_conts">
                                <div
                                    <div class="module_group">
                                        <div class="btn_group">
                                        	<button type="button" class="btn_type3" id="btnSave"><spring:message code="button.save" text="저장" /></button>
                                            <button type="button" class="btn_type1 add"><span><spring:message code="button.add" text="추가" /></span></button>
                                            <button type="button" class="btn_type1 del list-del"><span><spring:message code="button.delete" text="삭제" /></span></button>
                                            <button type="button" class="btn_type3 productGet"><span><spring:message code="button.get" text="제품가져오기" /></span></button>
                                            <!--  
                                            <button type="button" class="btn_type1 down" id="down"><span><spring:message code="button.down" text="내려받기" /></span></button>
                                            <button type="button" class="btn_type1 down" id="upload"><span><spring:message code="button.upload" text="업로드" /></span></button>
                                            -->
                                        </div>
                                         <div class="action2">
                                           	<form name="searchform" id="searchform" method="get">
                                   		
                                            <select class="select-ajax" name="searchType"  id="searchType" 
                                               data-codetype="etc" data-code="${search.searchType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A120000" }'></select>
                                            
                                            <div class="searchbox"> <!--인풋박스에 클릭시 active 클래스 추가-->
                                                <button type="button" class="ic_search"></button>
                                                <input type="text" name="searchWord" id="searchWord" value="${search.searchWord}" placeholder="<spring:message code="order.seaech.orderWord.label" text="검색어를 입력해주세요." />">
                                            </div>
                                            
                                            <select name="selectProduct" id="selectProduct" onchange="changeProduct(this.value)" data-label="${productType}" data-code="">
                                                 <option value="">==<spring:message code="settings.productType" text="상품 타입" />==</option>
                                                 <c:if test="${fn:length(productTypeList) > 0}">
                                                     <c:forEach items="${productTypeList}" var="list">
                                                         <option value="${list.productType}"<c:if test="${list.productType == search.searchProductType}"> selected</c:if>>${list.productType}</option>
                                                     </c:forEach>
                                                 </c:if>
                                             </select> 


                                            <%-- 페이지 사이즈 --%>
                                            <%@ include file="/WEB-INF/views/common/incPageSize.jsp"%> 
                                            
  

                                            </form>
                                        </div>
                                    </div>
                                    
                                    <form name="listform" id="listform" method="get">       
                                     
                                        <c:choose>
                                            <c:when test="${fn:length(list) == 0}">
                                                
                                                <div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
                                            
                                            </c:when>
                                            <c:otherwise>
                                                       
                                                <table class="tbtype">
                                                    <colgroup>
                                                        <col class="wp35">          		<!-- checkbox -->
                                                        <col class="cper1000"/>     		<!-- 상품코드 -->
                                                        <col class="cperauto"/>     		<!-- 상품명 -->
                                                       	<col class="cper0600"/>     		<!-- 가격 -->    
                                                        <col class="cper0500"/>     		<!-- 무게 -->
                                                        <col class="cper1100"/>    	 		<!-- box type-->
                                                        <col class="cper0500"/>     		<!-- 부피 길이 -->
                                                        <col class="cper0500"/>     		<!-- 부피 넓이 -->
                                                        <col class="cper0500"/>     		<!-- 부피 높이 -->
                                                        <col class="cper0630">          	<!-- 사용여부 -->
                                                    </colgroup>
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox" name="allCheck" value="Y" id="ind01"><label for="ind01"><span class="icon_ck" ></span></label></th>
                                                            <th><spring:message code="settings.itemCode" text="상품코드" /></th>                                                
                                                            <th><spring:message code="settings.itemName" text="상품명" /></th>
                                                            <th><spring:message code="settings.itemPrice" text="가격" /></th>
                                                            <th><spring:message code="settings.weight" text="무게" /></th>
                                                            <th class="back_d2"><spring:message code="settings.boxtype" text="포장타입" />
                                                            <th class="back_d2"><spring:message code="settings.vwidth" text="폭" /></th>
                                                            <th class="back_d2"><spring:message code="settings.vlength" text="길이" /></th>
                                                            <th class="back_d2"><spring:message code="settings.vheight" text="높이" /></th>
                                                            <th class="back_d2"><spring:message text="사용여부" /></th>                                                            
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        
                                                    <c:forEach items="${list}" var="item" varStatus="status">
                                                        <tr data-idx="${item.skuIdx}">
                                                            <td><input type="checkbox" name="ckBox" value="${item.skuIdx}" id="ind02_${status.count}"><label for="ind02_${status.count}"><span class="icon_ck" ></span></label></td>
                                                            <td class="t_center"><p>${item.itemCode}</p></td>
                                                            <td class="list-edit">${item.itemName}</td>
                                                            <%-- <td class="t_center list-edit"><p><fmt:formatNumber value="${item.itemQty}" pattern="#,###" /></p></td> --%>
                                                            <td class="t_right list-edit"><p><fmt:formatNumber value="${item.itemPrice}" pattern="#,###.##" /><span class="tpu"> ${item.priceCurrency}</span></p></td>
                      
                                                            <td class="t_center list-edit"><p><fmt:formatNumber value="${item.itemWeight}" pattern="#,###" />${item.weightUnit}</p></td>
                                                            <td class="t_center">
                                                            <dt><c:set var="boxSelect"></c:set>${boxSelect}</dt>
                           										<dd><select name="selectBox" id="boxSelect" data-label="${boxSelect}">
								                                        <option value="">==Type==</option>
								                                        <c:forEach items="${boxList}" var="box" varStatus="status">
								                                            <option data-boxtype="${box.boxType}"
								                                                data-boxlength="${box.boxLength}"
								                                                data-boxwidth="${box.boxWidth}"
								                                                data-boxheight="${box.boxHeight}"
								                                                data-boxunit="${box.boxUnit}"
								                                                data-boxweight="${box.boxWeight}"
								                                                data-weightunit="${box.weightUnit}"
								                                                data-boxtitle="${box.boxTitle}"
								                                                value="${box.boxIdx}"
								                                                <c:if test="${box.boxIdx == selectBox or box.boxIdx == item.selectBox}" > selected</c:if>>${box.boxTitle}</option>
								                                        </c:forEach>
								                                     </select> 
                            									</dd>
                            								</td>
                            								<%-- <td class="t_center list-edit"><p>${item.boxType}</p></td> --%>
                                                            <td class="t_center"><p><input type="text" class="cper2000" name="itemWidth" value="${item.boxWidth}"
                                                            							data-format="number" data-required="Y" data-label="0" placeholder="0"/></p></td>	
                                                            <td class="t_center"><p><input type="text" class="cper2000" name="itemLength" value="${item.boxLength}"
                                                            							data-format="number" data-required="Y" data-label="0" placeholder="0"/></p></td>
                                                            <td class="t_center"><p><input type="text" class="cper2000" name="itemHeight"  value="${item.boxHeight}"
                                                            							data-format="number" data-required="Y" data-label="0" placeholder="0"/></p></td>
                                                            <td class="t_center"><select name="useyn" data-code="">
                                                            									<option value="">==</option>
                                                            									<option value="Y" <c:if test="${item.useYn =='Y'}"> selected</c:if>>Y</option>
                                                            									<option value="N"<c:if test="${item.useYn =='N'}"> selected</c:if>>N</option> 
                                                            									</select></td>
                                                            									
                                                           <input type="hidden" name="itemTitle" value="${box.boxTitle}">
                                                           <input type="hidden" name="itemCode" value="${item.itemCode}">
                                                           <input type="hidden" name="itemName" value="${item.itemName}">
                                                      	  
                                                        </tr>
            
                                                    </c:forEach>
            
                                                    </tbody>    
                                                </table>
                                                
                                                
                                            </c:otherwise>
                                        </c:choose>
                                      
                                      </form>   <!-- end listform -->                               
                                </div>
                            </div>
                        
                            <div class="pager">
                                <c:if test="${fn:length(list) > 0}">
                                <%@ include file="/WEB-INF/views/common/incPaging2.jsp"%>
                                </c:if>
                                <form name="defaultSearchForm" id="defaultSearchForm" method="get">
                                <input type="hidden" name="searchBoxType" value="${search.searchBoxType}">
                                <input type="hidden" name="searchType" value="${search.searchType}">
                                <input type="hidden" name="searchWord" value="${search.searchWord}">
                                <input type="hidden" name="searchProductType" value="${search.searchProductType}">
                                <input type="hidden" name="pageSize" value="${search.pageSize}">
                                <input type="hidden" name="currentPage" value="">                         
                                </form>
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