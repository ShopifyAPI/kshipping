<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 : UI_ADM_512_POP 
기능설명 : 설정관리 > 계정설정 popup
Author   Date         Description
jwh      2019-12-23   First Draft
**************************************************
 -->
                <div class="pop_head">
                    <h3><spring:message code="settings.pop.channel" text="채널 연동" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                    <form name="authShop" id="authShop" action="/shopifyInstall">
                    <ul class="tab_conts">
                        <li>
                            <div class="wrap_col">
                                
                                <spring:message code="settings.pop.channelText" text="쇼핑몰 명을 입력해주세요." /> <br>
                                
                                <c:set var="shopName"><spring:message code="settings.shop" text="쇼핑몰명" /></c:set>
                                <input type="text" class="cper100p mt5" name="shopName" id="shopName" 
                                    data-required="Y" data-label="${shopName}" placeholder="${shopName}">
                            </div>
                        </li>
                    </ul>
                    <div class="pop_btn">
                        <button type="button" class="btn_type2 join"><spring:message code="button.join" text="연동" /></button>
                    </div>
                    </form>
                </div>