<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>  
<!-- 
**************************************************
화면코드 : UI_ADM_223_POP
기능설명 : 주문 > 팝업 > 관세 정보 선택/등록
Author   Date         Description
jwh      2020-01-29   First Draft
**************************************************
 -->
<c:choose>
    <c:when test="${status == 'no'}">
    
    <script type="text/javascript">
    callPopupError();
    </script>
    
    </c:when>
    <c:otherwise>

                <div class="pop_head">
                    <h3><spring:message code="order.popup.address.title" text="배송정보" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                    <ul class="tab_conts">
                        <form name="frm" method="get" id="ajax-popForm">
                        <li>
                             <h4 class="sub_h4"><spring:message code="order.popup.address.address2" text="" /></h4>
                              <div class="dlv_inf address">
                                  <div class="alignbx fwd departure">
                                      <div class="gridbx cm dpt_r">

                                        <c:set var="sender"><spring:message code="settings.menuSender" text="출고지" /></c:set>
                                        <div class="tit">
                                          <p class="guide_txt">${sender}</p>
                                        </div>
                                        <div class="ipt">
                                            <select name="selectSender" id="selectSender" data-required="Y" data-label="${sender}" data-code="">
                                            <option value="">== ${sender} ==</option>
                                                <c:forEach items="${sellerAddrList}" var="list">
                                                  <option data-phonenumber="${list.phoneNumber}" 
                                                            data-zipcode="${list.zipCode}" 
                                                          data-addr1="${list.addr1}"
                                                          data-addr2="${list.addr2}"
                                                          data-addr1ename="${list.addr1Ename}"
                                                          data-addr2ename="${list.addr2Ename}"
                                                          data-province="${list.province}"
                                                          data-city="${list.city}"
                                                          value="${list.senderIdx}"
                                                      <c:if test="${list.senderIdx == address.selectSender}"> selected</c:if>>${list.senderTitle}</option>
                                              </c:forEach>
                                            </select>
                                            <input class="adr_info" type="text" readonly maxlength="200" name="_senderAddr" value="" >
                                        </div>

                                      </div>
                                
                                      <div class="gridbx cm number_r">
                                           <div class="tit">
                                              <p class="guide_txt"><spring:message code="settings.phoneNumber=" text="전화번호" /></p>
                                          </div>
                                          <div class="ipt">
                                              <input type="text" readonly maxlength="30" name="buyerPhone" value="${address.buyerPhone}" >
                                          </div>

                                      </div>          
                                      <div class="gridbx cm mail_r">
                                            <c:set var="buyerEmail"><spring:message code="settings.email" text="이메일" /></c:set>
                                           <div class="tit">
                                              <p class="guide_txt required">${buyerEmail}</p>
                                          </div>
                                          <div class="ipt">
                                              <input type="text" maxlength="30" name="buyerEmail" id="buyerEmail" data-required="Y" data-format="email" data-label="${buyerEmail}" value="${address.buyerEmail}" >
                                          </div>

                                      </div>


                                  </div>
                                    
                                    
                                    
                                    
                                  <div class="alignbx fwd destination">
                                      <div class="gridbx cm name_r">
                                          <div class="tit">
                                              <p class="guide_txt" style="font-weight: 600;"><spring:message code="shipment.orderInfo" text="주문정보" /></p>
                                          </div>
                                          <div class="ipt">
                                              <input type="text" readonly="" maxlength="60" name="_buyerName" value="${address.buyerFirstname} ${address.buyerLastname}" >
                                          </div>
                                      </div>

                                      <div class="gridbx cm address_r">
                                           <div class="tit">
                                              <p class="guide_txt"><spring:message code="order.popup.address.address2" text="주소" /></p>
                                          </div>
                                          <div class="ipt">
                                              <input type="text" maxlength="200" name="_buyerAddr" value="" >
                                          </div>
                                      </div>

                                      <div class="gridbx cm zipcode_r">
                                           <div class="tit">
                                              <p class="guide_txt"><spring:message code="settings.zipCode" text="우편번호" /></p>
                                          </div>
                                          <div class="ipt">
                                              <input type="text" maxlength="10" name="buyerZipCode" id="buyerZipCode" value="${address.buyerZipCode}" >
                                          </div>
                                      </div>
                                  </div>
                               </div>
                            <div class="tit_wrap mt20">
                                <h4 class="sub_h4"><spring:message code="order.popup.address.title2" text="관세정보" /></h4>
                                <span> 
                                 <div class="mt10 required"><a href="https://unipass.customs.go.kr/clip/index.do?opnurl=/hsinfosrch/openULS0201001Q.do" target="_blank">HS Code Guide</a></div>
                                </span>
                                <div class="sect_right">
                                </div>
                            </div>
                            <div class="scr_x">
                                <table class="tbtype boxlist">
                                    <colgroup>
                                           <col class="cper1200">
                                           <col class="cperauto">
                                            <c:choose>
                                                <c:when test="${address.buyerCountryCode == 'RU' }">
                                                       <col class="cper1200">
                                                       <col class="cper0780">
                                                       <col class="cper1000">
                                                       <col class="cper1000">
                                                       <col class="cper0500">
                                                       <col class="cper0780">
                                                       <col class="cper0780">
                                                       <col class="cper0500">
                                                       <col class="cper0500">
                                                 </c:when>
                                                <c:otherwise>
                                                   <col class="cper1200">
                                                   <col class="cper1200">
                                                   <col class="cper0500">
                                                   <col class="cper0780">
                                                   <col class="cper0780">
                                                   <col class="cper0500">
                                                   <col class="cper0500">
                                                </c:otherwise>
                                            </c:choose>
                                        </colgroup>
                                    <thead>
                                        <tr>
                                            <th><spring:message code="settings.itemCode" text="상품코드" /></span></th>
                                            <th><spring:message code="settings.itemName" text="상품명" /></th>
                                            <th><span><spring:message code="settings.itemSku" text="SKU" /></span></th>
                                            <th><span class="required"><spring:message code="settings.hscode" text="HS CODE" /></span></th>
                                            <c:if test="${address.buyerCountryCode == 'RU' }"> 
                                                <th><span class="required"><spring:message code="settings.repreItemNm" text="대표품명" /></span></th>
                                                <th><span class="required"><spring:message code="settings.repreItemNmRu" text="대표품명(러시아어)" /></span></th>
                                            </c:if>
                                            <th colspan="2"><span class="required"><spring:message code="settings.weight" text="무게" /></span></th>
                                            <th colspan="2"><span class="required"><spring:message code="settings.itemPrice" text="가격" /></span></th>
                                            <th><span class="required"><spring:message code="order.popup.express.box.quantity" text="수량" /></span></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    
                                <c:choose>
                                    <c:when test="${fn:length(skuList) == 0}">
                                        <tr>
                                            <td colspan="9" class="t_center">
                                                <spring:message code="common.title.notData" text="검색된 데이터가 없습니다." />
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${skuList}" var="item" varStatus="status">
                                        <tr>
                                            <td>
                                                <input type="text" class="" readonly name="arrGoodsCode" id="goodsCode_${status.count}" value="${item.goodsCode}" 
                                                    maxlength="20" data-required="Y" data-label="<spring:message code="settings.itemCode" text="상품코드" />" 
                                                    placeholder="<spring:message code="settings.itemCode" text="상품코드" />">
                                                <input type="hidden" name="arrSkuIdx" id="skuIdx_${status.count}" value="${item.skuIdx}">
                                                <input type="hidden" name="arrGoodsItemId" id="goodsItemId_${status.count}" value="${item.goodsItemId}"> 
                                            </td>
                                            <td>
                                                <input type="text" class="" name="arrGoods" id="goods_${status.count}" value="${item.goods}" 
                                                    maxlength="100" data-required="Y" data-label="<spring:message code="settings.itemName" text="상품명" />" 
                                                    placeholder="<spring:message code="settings.itemName" text="상품명" />">
                                            </td>
                                            <td>
                                                <input type="text" class="" name="arrGoodsSku" id="goodsSku_${status.count}" value="${item.goodsSku}" 
                                                    maxlength="100" data-label="<spring:message code="settings.itemSku" text="SKU" />" 
                                                    placeholder="<spring:message code="settings.itemSku" text="SKU" />">
                                            </td>
                                            <td>
                                                <input type="text" class="" name="arrHscode" id="hscode_${status.count}" value="${item.hscode}" 
                                                    maxlength="10" data-required="Y" data-label="<spring:message code="settings.hscode" text="HS CODE" />" 
                                                    placeholder="<spring:message code="settings.hscode" text="HS CODE" />">
                                                <a href="#" class="btn_search"></a>
                                            </td>
                                            <c:if test="${address.buyerCountryCode == 'RU' }"> 
                                               <td>
                                                   <input type="text" class="" name="arrRepreItemNm" id="repreItemNm_${status.count}" value="${item.repreItemNm}" 
                                                       maxlength="100"  data-label="<spring:message code="settings.repreItemNm" text="대표품명" />" 
                                                       placeholder="<spring:message code="settings.repreItemNm" text="대표품명" />">
                                               </td>
                                               
                                               <td>
                                                  <input type="text" class="" name="arrRepreItemNmRu" id="repreItemNmRu_${status.count}" value="${item.repreItemNmRu}" 
                                                       maxlength="100"  data-label="<spring:message code="settings.repreItemNmRu" text="대표품명(러시아어)" />" 
                                                       placeholder="<spring:message code="settings.repreItemNmRu" text="대표품명(러시아어)" />">
                                               </td>
                                            </c:if>
                                            <td class="weight-data" data-weight="${item.weight}" data-unit="${item.weightUnit}" data-quantity="${item.quantity}">
                                                <input type="text" class="t_right refresh" name="arrWeight" id="weight_${status.count}" value="<c:if test="${item.weight != ''}">${item.weight}</c:if>" 
                                                    maxlength="4" data-required="Y" data-format="num" data-label="<spring:message code="settings.weight" text="무게" />" 
                                                    placeholder="<spring:message code="settings.weight" text="무게" />" onchange="setTotalWeight()">
                                            </td>
                                            <td>
                                                <select class="select-ajax" name="arrWeightUnit" id="weightUnit_${status.count}" 
                                                    data-codetype="etc" data-code="${item.weightUnit}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "H030000" }' 
                                                    data-required="Y" data-label="<spring:message code="settings.weightUnit" text="무게단위"  />" onchange="setTotalWeight()"></select>
                                            </td>
                                            <td>
                                                <input type="text" class="t_right" name="arrUnitCost" id="unitCost_${status.count}" value="<c:if test="${item.unitCost gt 0.0}">${item.unitCost}</c:if>" 
                                                    maxlength="7" data-required="Y" data-format="num" data-label="<spring:message code="settings.itemPrice" text="가격" />" 
                                                    placeholder="<spring:message code="settings.itemPrice" text="가격" />">
                                            </td>
                                            <td>
                                                <input type="text" class="t_center" name="arrPriceCurrency" id="priceCurrency_${status.count}" value="${item.priceCurrency}" 
                                                    maxlength="3" data-required="Y" data-format="" data-label="<spring:message code="settings.priceCurrency" text="화폐단위" />" 
                                                    placeholder="<spring:message code="settings.priceCurrency" text="화폐단위" />">
                                            </td>
                                            <td>
                                                <input type="text" class="t_right refresh" name="arrQuantity" id="quantity_${status.count}" value="<c:if test="${item.quantity > 0}">${item.quantity}</c:if>" 
                                                    maxlength="5" data-required="Y" data-format="num" data-label="<spring:message code="order.popup.express.box.quantity" text="수량" />" 
                                                    placeholder="<spring:message code="order.popup.express.box.quantity" text="수량" />" onchange="setTotalWeight()">
                                            </td>
                                        </tr>
                                        
                                        </c:forEach>  
                                    </c:otherwise>
                                </c:choose>   
                                        
                                        
                                    </tbody>
                                </table>
                                
                            </div>
                            <c:if test="${address.buyerCountryCode == 'RU' }"> 
                                <div class="mt10 required">LGL 러시아행인 경우 대표품명(러시아어 포함)을 입력하지 않으면 배송신청이 되지 않습니다.</div>
                            </c:if>
                            
                            
                            <!--  courier start -->
                            <div style="width: 100%; display: table;">

                                <div class="form_side narrow">
                                    <h4 class="sub_h4"><spring:message code="order.popup.express.box.title" text="BOX 정보" /></h4>
                                    <div class="dlv_inf left">
                                        <div class="alignbx name">
                                            <c:set var="boxSelect"><spring:message code="settings.boxSelect" text="포장재선택" /></c:set>
                                            <div class="tit">
                                              <p class="required only">${boxSelect}</p>
                                            </div>
                                            <div class="ipt">
                                                <select class="cper100p" name="selectBox" id="boxSelect" data-required="" data-label="">
                                                <option value="">== ${boxSelect} ==</option>
                                                <c:forEach items="${boxList}" var="item" varStatus="status">
                                                    <option data-boxtype="${item.boxType}"
                                                        data-boxlength="${item.boxLength}"
                                                        data-boxwidth="${item.boxWidth}"
                                                        data-boxheight="${item.boxHeight}"
                                                        data-boxunit="${item.boxUnit}"
                                                        data-boxweight="${item.boxWeight}"
                                                        data-weightunit="${item.weightUnit}"
                                                         value="${item.boxIdx}"
                                                        <c:if test="${item.boxIdx == address.selectBox}"> selected</c:if>>${item.boxTitle}</option>
                                                </c:forEach>
                                                </select>
                                                <input type="text" style="width:auto; margin-left: 10px;" name="boxType" id="boxType" value="<c:if test="${address.boxType != ''}">${address.boxType}</c:if>" 
                                                    maxlength="4" data-required="Y" data-label="${boxType}" data-format="" placeholder="${boxType}">
                                            </div>

                                        </div>
                                                                       
                                        <div class="alignbx bxsize">
                                            <div class="gridbx length">
                                                <c:set var="boxLength"><spring:message code="settings.boxLength" text="가로" /></c:set>
                                                <div class="tit">
                                                    <p class="guide_txt">${boxLength}</p>
                                                </div>
                                                <div class="ipt rt">
                                                    <input type="text" class="refresh cper2500" name="boxLength" id="boxLength" value="<c:if test="${address.boxLength != ''}">${address.boxLength}</c:if>" 
                                                    maxlength="6" data-required="Y" data-label="${boxLength}" data-format="number" placeholder="${boxLength}">
                                                </div>
                                            </div>

                                            <div class="gridbx width">
                                                <c:set var="boxWidth"><spring:message code="settings.boxWidth" text="세로" /></c:set>
                                                <div class="tit">
                                                    <p class="guide_txt">${boxWidth}</p>
                                                </div>
                                                <div class="ipt rt">
                                                    <input type="text" class="refresh cper2500" name="boxWidth" id="boxWidth" value="<c:if test="${address.boxWidth != ''}">${address.boxWidth}</c:if>" 
                                                    maxlength="6" data-required="Y" data-label="${boxWidth}" data-format="number" placeholder="${boxWidth}">
                                                </div>
                                            </div>

                                            <div class="gridbx height">
                                                <c:set var="boxHeight"><spring:message code="settings.boxHeight" text="높이" /></c:set>
                                                <div class="tit">
                                                    <p class="guide_txt">${boxHeight}</p>
                                                </div>
                                                <div class="ipt rt">
                                                    <input type="text" class="refresh cper2500" name="boxHeight" id="boxHeight" value="<c:if test="${address.boxHeight != ''}">${address.boxHeight}</c:if>" 
                                                    maxlength="6" data-required="Y" data-label="${boxHeight}" data-format="number" placeholder="${boxHeight}">
                                                    <input type="text" readonly="" name="boxUnit" id="boxUnit" class="guide_ipt" value="<c:if test="${address.boxUnit != ''}">${address.boxUnit}</c:if>" >
                                                </div>
                                            </div>
                  
                                            <div class="gridbx weight">
                                                <c:set var="weight"><spring:message code="settings.boxWeight" text="포장재무게" /></c:set>
                                                <div class="tit">
                                                    <p class="guide_txt">${weight}</p>
                                                </div>
                                                <div class="ipt rt">
                                                    <input autocomplete="off" type="text" class="refresh cper2500" name="weight" id="weight" value="<c:if test="${address.boxWeight != ''}">${address.boxWeight}</c:if>" 
                                                    maxlength="7" data-required="Y" data-label="${weight}" data-format="commaNumber" placeholder="${weight}" >
                                                    
                                                    <input type="text" readonly="" name="weightUnit" class="guide_ipt" value="<c:if test="${address.weightUnit != ''}">${address.weightUnit}</c:if>" >
                                                </div>
        
                                            </div>          
        
                                        </div>


                                    </div>

                                    <div class="dlv_inf right">

                                        <div class="alignbx" >
                                            <div class="gridbx pck_weight">
                                                <div class="tit"> 
                                                    <p><spring:message code="order.popup.express.gross.weight" text="포장중량" /></p>
                                                </div>
                                                <div class="ipt rt"> 
                                                    <input type="text" class="readonly" readonly name="totalWeight" id="totalWeight" >
                                                    <input type="text" readonly="" name="weightUnitProduct" class="guide_ipt"  value="<c:if test="${address.weightUnit != ''}">${address.weightUnit}</c:if>">
                                                </div>
                                            </div>

                                            <div class="gridbx vlm_weight">
                                                <div class="tit"> 
                                                    <p><spring:message code="order.popup.express.volumetic.weight" text="부피중량" /></p>
                                                </div>
                                                <div class="ipt rt"> 
                                                     <input type="text" class="readonly" readonly name="volumeticWeight" id="volumeticWeight" >
                                                    <input type="text" readonly="" name="weightUnitVolumetic" class="guide_ipt"  value="<c:if test="${address.weightUnit != ''}">${address.weightUnit}</c:if>">
                                                </div>
                                            </div>

                                        </div>

                                    </div>
        
        
                                </div>  

                                <div style="width: 54%; float: right;">
                                    <h4 class="sub_h4 mt20"><spring:message code="order.popup.customer.title1" text="배송 서비스 선택" /></h4>
                                    <table class="tbtype mt10">
                                        <colgroup>
                                            <col class="cperauto">
                                            <col class="cper300">
                                            <col class="cper200">
                                            <col class="cper600">
                                            
                                        </colgroup>
                                        <thead>
                                            <tr>
                                                <th><spring:message code="order.popup.customer.title2" text="서비스" /></th>
                                                <th><spring:message code="order.popup.customer.title5" text="할인 배송료" /></th>
                                                <th><spring:message code="settings.priceCurrency" text="화폐단위" /></th>
                                                <th><spring:message code="order.popup.customer.title4" text="선택" /></th>
                                            </tr>
                                        </thead>
                                        <tbody class="courier-area">
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <!--  courier end -->
                           
                        </li>
                        
                        <input type="hidden" name="sellerPhone" value="${address.sellerPhone}">
                        <input type="hidden" name="sellerZipCode" value="${address.sellerZipCode}">
                        <input type="hidden" name="sellerAddr1" value="${address.sellerAddr1}">
                        <input type="hidden" name="sellerAddr2" value="${address.sellerAddr2}">
                        <input type="hidden" name="sellerAddr1Ename" value="${address.sellerAddr1Ename}">
                        <input type="hidden" name="sellerAddr2Ename" value="${address.sellerAddr2Ename}">
                        <input type="hidden" name="sellerProvince" value="${address.sellerProvince}">
                        <input type="hidden" name="sellerCity" value="${address.sellerCity}">
                        <input type="hidden" name="buyerFirstname" value="${address.buyerFirstname}">
                        <input type="hidden" name="buyerLastname" value="${address.buyerLastname}">
                        <input type="hidden" name="buyerAddr1"  id="buyerAddr1" value="${address.buyerAddr1}">
                        <input type="hidden" name="buyerAddr2"  id="buyerAddr2" value="${address.buyerAddr2}">
                        <input type="hidden" name="buyerCity" value="${address.buyerCity}">
                        <input type="hidden" name="buyerProvince" value="${address.buyerProvince}">
                        <input type="hidden" name="sellerCountry" value="KOREA">
                        <input type="hidden" name="sellerCountryCode" value="KR">
                        
<%--                         <input type="hidden" name="sellerName" value="${address.sellerName}"> --%>
                        <input type="hidden" name="masterCode" value="${address.masterCode}">
                        <input type="hidden" name="orderCode" value="${code}">
                        <input type="hidden" name="shopIdx" value="${idx}">
                        <input type="hidden" name="orderIdx" value="${address.orderIdx}">
                        <input type="hidden" name="orderDate" value="${address.orderDate}">
                        <input type="hidden" name="buyerCountryCode" value="${address.buyerCountryCode}">
                        <input type="hidden" name="shippingLineCode" value="${address.shippingLineCode}">
                        <input type="hidden" name="proc" value="">
                        <input type="hidden" name="courierCompany" value="${courier.courierCompany}">
                        <input type="hidden" name="courier" value="${courier.courier}">
                        <input type="hidden" name="payment" value="${courier.payment}">
                        <input type="hidden" name="courierId" value="${courier.courierId}">
                        <input type="hidden" name="rankPrice" value="${courier.rankPrice}">
                        <input type="hidden" name="paymentCode" value="NA">
                        <input type="hidden" name="payState" value="N">
                        </form>
                        <!-- 저장하지 않으려고 form 외부에 위치시킴 -->
                        <input type="hidden" name="addressBoxLength" value="${address.boxLength}">
                        <input type="hidden" name="addressBoxWidth" value="${address.boxWidth}">
                        <input type="hidden" name="addressBoxHeight" value="${address.boxHeight}">
                        <input type="hidden" name="addressBoxWeight" value="${address.boxWeight}">
                    </ul>
                    <div class="pop_btn">
                        <button style="padding-left: 1em; padding-right: 1em;" type="button" class="btn_type6" id="hscodeBtn" onclick="setHscode('${code}','${idx}'); return false;"><spring:message code="button.hscode" text="HS Code 가져오기" /></button> 
<%--                         <button type="button" class="btn_type3" id="prevBtn" onclick="popupAddress('${code}','${idx}'); return false;"><spring:message code="button.prev" text="이전" /></button>  --%>
                        <button type="button" class="btn_type3" id="saveCustomsBtn"><spring:message code="button.save.and.next" text="저장한후 다음열기" /></button>
                        <button type="button" class="btn_type4" id="addDeliveryBtn"><spring:message code="button.deliveryAdd" text="배송생성" /></button>
                    </div>
                </div><!--pop_body end-->

    </c:otherwise>
</c:choose>   