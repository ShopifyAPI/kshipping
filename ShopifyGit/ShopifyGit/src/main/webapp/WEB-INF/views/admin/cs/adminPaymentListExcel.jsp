<%@ page language="java"
    contentType="application/vnd.ms-excel; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
    String today = format.format(new Date());
    String days = request.getParameter("days");
    String fileName = today + ".추가요금";
      response.setHeader("Content-Type", "application/vnd.ms-xls");
    response.setHeader("Content-Disposition",
            "attachment; filename=" + new String((fileName).getBytes("KSC5601"), "8859_1") + ".xls");
%>
<%@ include file="/WEB-INF/views/common/incSysExcel.jsp"%>  
<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeaderExcel.jsp"%>
    <title><spring:message code="cs.payment" text="추가요금" /></title>
</head>
<body>
    <div class="frame"> <!--프레임 영역 이 div 영역부터 붙여서 넣어주세요.-->
        <div class="frame_wrap">
            
            <%// gnb include %>

            <div class="sub_conts">
            <form name="frm" method="get" id="ajax-form" action="/order">
                <!-- #### Content area ############### -->
				<article>
					<div class="tit_wrap">
						<h3><spring:message code="cs.payment" text="추가요금" /></h3>
					</div> <!--tit_wrap end-->
					<table class="tbtype">
	                    <colgroup>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
                            <col class="cperauto"/>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>

<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>

<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
<%--                             <col class="cperauto"/> --%>
	                    </colgroup>
	                    <thead>
	                        <tr class=".list-detail">
                                <th><spring:message code="" text="상품구분" /></th>
                                <th><spring:message code="" text="배송사" /></th>
                                <th><spring:message code="" text="배송 서비스" /></th>
<%--                                 <th><spring:message code="" text="종류" /></th> --%>
                                <th><spring:message code="" text="배송 신청일자" /></th>
                                <th><spring:message code="" text="배송상태" /></th>
                                <th><spring:message code="" text="배송번호" /></th>
                                <th><spring:message code="" text="송장번호" /></th>
                                <th><spring:message code="" text="총중량(g)" /></th>
                                <th><spring:message code="" text="배송비" /></th>
                                <th><spring:message code="" text="주문번호" /></th>
                                <th><spring:message code="" text="상품코드" /></th>
                                <th><spring:message code="" text="상품명" /></th>
                                <th><spring:message code="" text="SKU" /></th>
                                <th><spring:message code="" text="개수" /></th>
                                <th><spring:message code="" text="크기(cm)" /></th>
                                <th><spring:message code="" text="순중량(g)" /></th>
                                <th><spring:message code="" text="HS CODE" /></th>
                                <th><spring:message code="" text="생산지" /></th>
                                <th><spring:message code="" text="가격(￦)" /></th>
                                <th><spring:message code="" text="쇼핑몰 명" /></th>
                                <th><spring:message code="" text="주문자 명" /></th>
<%--                                 <th><spring:message code="" text="주문자 연락처" /></th> --%>
<%--                                 <th><spring:message code="" text="주문자 주소" /></th> --%>
<%--                                 <th><spring:message code="" text="주문자 국가" /></th> --%>
                                <th><spring:message code="" text="결제금액" /></th>
<%--                                 <th><spring:message code="" text="결제방식" /></th> --%>
<%--                                 <th><spring:message code="" text="추가금액" /></th> --%>
<%--                                 <th><spring:message code="" text="추가 결제 상태" /></th> --%>
<%--                                 <th><spring:message code="" text="추가 결제 방식" /></th> --%>
<%--                                 <th><spring:message code="" text="추가 결제 일자" /></th> --%>
                                <th><spring:message code="" text="수취인 명" /></th>
                                <th><spring:message code="" text="수취인 국가 코드" /></th>
                                <th><spring:message code="" text="수취인 주소1" /></th>
                                <th><spring:message code="" text="수취인 주소2" /></th>
                                <th><spring:message code="" text="수취인 우편번호" /></th>
                                <th><spring:message code="" text="수취인 시/군" /></th>
                                <th><spring:message code="" text="수취인 주/도" /></th>
                                
                                <th><spring:message code="" text="이름 또는 상호명" /></th>
                                <th><spring:message code="" text="사업자 번호" /></th>
                                <th><spring:message code="" text="출고지 주소" /></th>
                                <th><spring:message code="" text="출고지 우편번호(숫자 6자리)" /></th>
                                <th><spring:message code="" text="출고지 담당자 이메일" /></th>
                                <th><spring:message code="" text="출고지 담장자 연락처" /></th>
                                <th><spring:message code="" text="수출이행등록여부(Y/N)" /></th>
                                <th><spring:message code="" text="수출 신고번호1" /></th>
                                <th><spring:message code="" text="전량/분할 발송 여부" /></th>
                                <th><spring:message code="" text="선기적 포장개수" /></th>
                                
                                <th><spring:message code="" text="수출 신고번호2" /></th>
                                <th><spring:message code="" text="전량/분할 발송 여부" /></th>
                                <th><spring:message code="" text="선기적 포장개수" /></th>
                                <th><spring:message code="" text="수출 신고번호3" /></th>
                                <th><spring:message code="" text="전량/분할 발송 여부" /></th>
                                <th><spring:message code="" text="선기적 포장개수" /></th>
                                <th><spring:message code="" text="수출 신고번호4" /></th>
                                <th><spring:message code="" text="전량/분할 발송 여부" /></th>
                                <th><spring:message code="" text="선기적 포장개수" /></th>
	                        </tr>
	                    </thead>
	                    <tbody>
	                        
	                    <c:forEach items="${list}" var="item" varStatus="status">
	                        
	                        <tr>
								<td><p>${item.goodsType}</p></td>     <%-- 상품구분 --%>
								<td><p>${item.courierCompany}</p></td><%-- 배송사 --%>
								<td><p>${item.courier}</p></td>       <%-- 배송 서비스 --%>
<%-- 								<td><p>${item.종류}</p></td>           종류 --%>
								<td><p>${item.regDateStr}</p></td>    <%-- 배송 신청일자 --%>
								<td><p>${item.stateStr}</p></td>      <%-- 배송상태 --%>
								<td><p>${item.masterCode}</p></td>    <%-- 배송번호 --%>
								<td><p>${item.invoice}</p></td>       <%-- 송장번호 --%>
								
								<td><p>${item.singlePayWeight}${item.singlePayWeightUnit}</p></td> <%-- 총중량(g)  --%>
								<td><p>${item.payment}</p></td> <%-- 배송비  --%>
								<td style="mso-number-format:'\@'"><p>${item.orderCode}</p></td> <%-- 주문번호  --%>
								<td style="mso-number-format:'\@'"><p>${item.goodsCode}</p></td> <%-- 상품코드  --%>
								<td><p>${item.goods}</p></td> <%-- 상품명  --%>
								<td><p>${item.goodsSku}</p></td> <%-- SKU  --%>
								<td><p>${item.quantityStr}</p></td> <%-- 개수  --%>
								<td><p>${item.boxLength}${item.boxUnit} , ${item.boxWeight}${item.boxUnit} , ${item.boxHeight}${item.boxUnit}</p></td> <%-- 크기(cm)  --%>
								<td><p>${item.goodsWeight}${item.goodsWeightUnit}</p></td> <%-- 순중량(g)  --%>
								<td><p>${item.hsCode}</p></td> <%-- HSCODE  --%>
								
								<td><p>${item.origin}</p></td> <%-- 생산지  --%>
								<td><p>${item.goodSPriceStr}</p></td> <%-- 가격(￦)  --%>
								<td><p>${item.shopName}</p></td> <%-- 쇼핑몰 명  --%>
								<td><p>${item.customerName}</p></td> <%-- 주문자 명  --%>
<%-- 								<td><p>${item.}</p></td> 주문자 연락처  --%>
<%-- 								<td><p>${item.}</p></td> 주문자 주소  --%>
<%-- 								<td><p>${item.}</p></td> 주문자 국가  --%>
								<td><p>${item.orderPrice}</p></td> <%-- 결제금액  --%>
<%-- 								<td><p>${item.}</p></td> 결제방식 --%>
                                
<%--                                 <td><p>${item.}</p></td> 추가금액  --%>
<%--                                 <td><p>${item.}</p></td> 추가 결제 상태  --%>
<%--                                 <td><p>${item.}</p></td> 추가 결제 방식  --%>
<%--                                 <td><p>${item.}</p></td> 추가 결제 일자  --%>

								<td><p>${item.buyerFirstname} ${item.buyerLastname}</p></td> <%-- 수취인 명  --%>
								
								<td><p>${item.buyerCountryCode}</p></td> <%--수취인 국가 코드  --%>
								<td><p>${item.buyerAddr1}</p></td> <%-- 수취인 주소1 --%>
								<td><p>${item.buyerAddr2}</p></td> <%-- 수취인 주소2 --%>
								<td><p>${item.buyerZipCode}</p></td> <%-- 수취인 우편번호 --%>
								<td><p>${item.buyerCity}</p></td> <%-- 수취인 시/군 --%>
								<td><p>${item.buyerProvince}</p></td> <%-- 수취인 주/도 --%>
								
								<%-- 								<td><p>${item.}</p></td> 이름 또는 상호명 --%>
<%-- 								<td><p>${item.}</p></td> 사업자 번호 --%>
<%-- 								<td><p>${item.}</p></td> 출고지 주소 --%>
<%-- 								<td><p>${item.}</p></td> 출고지 우편번호(숫자 6자리) --%>
<%-- 								<td><p>${item.}</p></td> 출고지 담당자 이메일 --%>
<%-- 								<td><p>${item.}</p></td> 출고지 담장자 연락처 --%>
<%-- 								<td><p>${item.}</p></td> 수출이행등록여부(Y/N) --%>
<%-- 								<td><p>${item.}</p></td> 수출 신고번호1 --%>
<%-- 								<td><p>${item.}</p></td> 전량/분할 발송 여부 --%>
<%-- 								<td><p>${item.}</p></td> 선기적 포장개수 --%>
								
<%-- 								<td><p>${item.}</p></td> 수출 신고번호2 --%>
<%-- 								<td><p>${item.}</p></td> 전량/분할 발송 여부 --%>
<%-- 								<td><p>${item.}</p></td> 선기적 포장개수 --%>
<%-- 								<td><p>${item.}</p></td> 수출 신고번호3 --%>
<%-- 								<td><p>${item.}</p></td> 전량/분할 발송 여부 --%>
<%-- 								<td><p>${item.}</p></td> 선기적 포장개수 --%>
<%-- 								<td><p>${item.}</p></td> 수출 신고번호4 --%>
<%-- 								<td><p>${item.}</p></td> 전량/분할 발송 여부 --%>
<%-- 								<td><p>${item.}</p></td> 선기적 포장개수 --%>
								
							</tr>
	
	                    </c:forEach>
	
	                    </tbody>    
	                </table>
				</article>
			</form>
			</div> 
		</div>
	</div> 
   
</body>
</html>
