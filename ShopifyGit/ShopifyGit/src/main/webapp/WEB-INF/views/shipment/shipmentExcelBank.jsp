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
    String fileName = today + ".펌뱅킹";
	response.setHeader("Content-Type", "application/vnd.ms-xls");
	response.setHeader("Content-Disposition", "attachment; filename=" + new String((fileName).getBytes("KSC5601"), "8859_1") + ".xls");
%>
<%@ include file="/WEB-INF/views/common/incSysExcel.jsp"%>  
<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeaderExcel.jsp"%>
    <title><spring:message code="incgnb.order" text="주문(펌뱅킹)" /></title>
</head>
<body>
    <div class="frame"> <!--프레임 영역 이 div 영역부터 붙여서 넣어주세요.-->
        <div class="frame_wrap">
            
            <%// gnb include %>

            <div class="sub_conts">
                <!-- #### Content area ############### -->
                <article>
                    <div class="tit_wrap">
                        <!-- <h3><spring:message code="incgnb.order" text="주문" /></h3> -->
                        <h3>펌뱅킹</h3>
                    </div> <!--tit_wrap end-->
                    <table class="tbtype" border="1">
                        <colgroup>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1200"/>
                            <col class="cper1300"/>
                            <col class="cper1300"/>
                        </colgroup>
                        <thead>
                            <tr class=".list-detail">
                                <th>쇼핑몰 명</th>
                                <th>주문명칭</th>
                                <th>주문번호</th>
                                <th>배송바코드</th>
                                <th>결제방법</th>
                                <th>입금금액</th>
                                <th>상품명</th>
                                <th>주문자정보</th>
                                <th>배송국가</th>
                                <th>배송접수일</th>
                                <th>배송상태</th>
                                
                                <th>송하인 우편번호</th>
                                <th>송하인 기본주소</th>
                                <th>송하인 상세주소</th>
                                <th>송하인 이름</th>
                                <th>송하인 연락처</th>
                                <th>송하인 핸드폰</th>
                                
                                <th>수하인 우편번호</th>
                                <th>수하인 기본주소</th>
                                <th>수하인 이름</th>
                                <th>수하인 연락처</th>
                                <th>수하인 핸드폰</th>
                            </tr>
                        </thead>
                        <tbody>
                            
                        <c:forEach items="${list}" var="item" varStatus="status">
                            
                            <tr>
                                <td style="mso-number-format:'\@'"><p>${item.shopName}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.orderName}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.orderCode}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.masterCode}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.payMethod}</p></td>
                                <td class="t_center"><p><fmt:formatNumber value="${item.totalAmount}" pattern="#,###" /></p></td>
                                <td style="mso-number-format:'\@'"><p>${item.goods} <c:if test="${item.goodsCnt > 0}">외 ${item.goodsCnt} 개</c:if></p></td>
                                <td style="mso-number-format:'\@'"><p>${item.customerName}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.buyerCountryCode}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.regDate}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.stateStr}</p></td>
                                
                                <td style="mso-number-format:'\@'"><p>${item.buyerZipCode}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.buyerAddr1}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.buyerAddr2}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.buyerFirstname}  ${item.buyerLastname}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.buyerPhone}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.buyerPhone}</p></td>
                                
                                <td style="mso-number-format:'\@'"><p>${item.consigneeZipcode}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.consigneeAddr1}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.consigneeName}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.consigneeTel}</p></td>
                                <td style="mso-number-format:'\@'"><p>${item.consigneeCpno}</p></td>
                            </tr>
    
                        </c:forEach>
    
                        </tbody>    
                    </table>
                </article>
            </div> 
        </div>
    </div> 
   
</body>
</html>
