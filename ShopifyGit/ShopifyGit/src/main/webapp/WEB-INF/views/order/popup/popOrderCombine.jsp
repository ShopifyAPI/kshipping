<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>  
    <script type="text/javascript">
    $(function () {
    <c:choose>
        <c:when test="${status == 'ok'}">
            //initialize();
		</c:when>
		<c:when test="${status == 'addr'}">
		   bundleError('addr');
	       return false;
	    </c:when>
	    <c:when test="${status == 'weight'}">
	        bundleError('weight');
            return false;
        </c:when>
		<c:otherwise>
		    bundleError('etc');
		    return false;
		</c:otherwise>
	</c:choose>
        
        
    });
 
    
    </script>
                <div class="pop_head">
                    <h3><spring:message code="incgnb.order" text="주문 합배송 (최대5개)" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                    <h4 class="sub_bg_tit"><spring:message code="order.orderNo" text="주문번호" /></h4>
                   <ul class="order_num">
               <c:choose>
                    <c:when test="${fn:length(orderCodeList) == 0}">
                    <li></li>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${orderCodeList}" var="list">
                        
	                        <li data-ordercode="${ list.orderCode }">${ list.orderName }</li>
                        
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                   </ul>
                   <p class="warn"><spring:message code="order.popup.combine.note" text="* 총 무게가 28kg 이상일 경우 배송 거부 사유가 될수 있습니다." /></p>
                    <div class="pop_btn">
                        <button type="button" class="btn_type2 wp390" id="btnCombineNext"><spring:message code="order.popup.combine.btnSave" text="주문 합치기" /></button>
                        <input type="hidden" name="arrOrderCode" id="arrOrderCode" value="${arrOrderCode}">
                        <input type="hidden" name="arrShopIdx" id="arrShopIdx" value="${arrShopIdx}"> 
                    </div>
                </div>
