<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
                <div class="pop_head">
                    <h3 id="popTitle"><spring:message code="button.state.change" text="상태변경" /></span></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                   <P class="t_center" id="popBody">
                   		<form id="popBodyform">
                   	   <%-- <h4 class="sub_h4"><spring:message code="button.state.change" text="상태변경" /></h4> --%>
                   	   <select class="cper100p" name="shipmentState" id="shipmentState" onchange="shipmentStateView(this.value);">
                   		<option value="A030000"><spring:message code="cs.back" text="반송" /></option>
                   		<option value="A040000"><spring:message code="cs.exchange" text="교환" /></option>
                   		<option value="A050000"><spring:message code="cs.return" text="반품" /></option>
                   		<%-- <option value="A060000"><spring:message code="cs.payment" text="추가요금" /></option> --%>
                   		</select>
                   		<%-- 반송이 필요 하면 위에 주석 제거 --%>
                   		<div id="shipmentStateSubArea" class="mt10" style="display:none"> 
                   		<select class="select-ajax cper100p" name="shipmentStateSub"  id="shipmentStateSub" 
		                    data-codetype="" data-code="" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A030000" }'>
                   		</select>
                   		</div>
                   		<div class="mt20"></div>
                      	<th>
                          <c:set var="changeReason"><spring:message code="settings.changeReason" text="변경사유" /></c:set>
                          <p class="required">${changeReason}</p>
                        </th>
                        <div class="mt10"></div>
                        <td>
                        	<input type="text" class="cper100p" name="changeReason" id="changeReason" value="${change.changeReason}" 
                               		maxlength="20" data-label="${changeReason}" data-required="Y" placeholder="<spring:message code="common.changeReason" text="변경사유를 입력해 주세요" />">
                        </td>
                        <th>
                   		</form>
                   </P>
                   <P class="t_center">&nbsp;</P>
                   <p class="t_center">
                       <button type="button" class="btn_type1" id="btnOk"><span><spring:message code="button.confirm" text="확인" /></span></button>
                       <button type="button" class="btn_type1" id="btnCancel"><span><spring:message code="button.cancel" text="취소" /></span></button>
                   </p>
                </div>