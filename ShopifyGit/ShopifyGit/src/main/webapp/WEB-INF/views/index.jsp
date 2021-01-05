<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 

<!DOCTYPE html>
<html lang="ko">
<head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>
    
    <title>Title</title>
	<script type="text/javascript">
	// READY
    $(function () {
    	/* var param = {
      		  "rate": {
      			    "origin": {
      			      "country": "CA",
      			      "postal_code": "K2P1L4",
      			      "province": "ON",
      			      "city": "Ottawa",
      			      "name": null,
      			      "address1": "150 Elgin St.",
      			      "address2": "",
      			      "address3": null,
      			      "phone": "16135551212",
      			      "fax": null,
      			      "email": null,
      			      "address_type": null,
      			      "company_name": "Jamie D's Emporium"
      			    },
      			    "destination": {
      			      "country": "CA",
      			      "postal_code": "K1M1M4",
      			      "province": "ON",
      			      "city": "Ottawa",
      			      "name": "Bob Norman",
      			      "address1": "24 Sussex Dr.",
      			      "address2": "",
      			      "address3": null,
      			      "phone": null,
      			      "fax": null,
      			      "email": null,
      			      "address_type": null,
      			      "company_name": null
      			    },
      			    "items": [{
      			      "name": "Short Sleeve T-Shirt",
      			      "sku": "",
      			      "quantity": 1,
      			      "grams": 1000,
      			      "price": 1999,
      			      "vendor": "Jamie D's Emporium",
      			      "requires_shipping": true,
      			      "taxable": true,
      			      "fulfillment_service": "manual",
      			      "properties": null,
      			      "product_id": 48447225880,
      			      "variant_id": 258644705304
      			    }],
      			    "currency": "USD",
      			    "locale": "en"
      			  }
      			} */
    	//alert()
        // CarrierService 테스트
    	//ajaxCall("post", "/shopifyOutApi/apiCarrierServiceSelect", param, cb);
    	
        initialize();
    });
 
    var initialize = function () {
        initControls();
        bindEvent();
    };
    
    var initControls = function () {
    	
    };
    
    var bindEvent = function () {
    	$(document).on("click",".postlist",function(){
    		$(".postlist").removeClass("pop");
    		$(this).addClass("pop");
            openPop("/popup/postNotice", "", 960, "", popupBind, "");
        });
    };
    
    var popupBind = function() {
    	var title = $(".pop").find(".title").html();
    	var content = $(".pop").find(".content").html();
    	
    	content = content.replace(/(?:\r\n|\r|\n)/g, '<br />');
    	
    	$(".pop_body .title").html(title);
    	$(".pop_body .content").html(content);
    }
    
    var gotoPage = function(page) {
        $("#defaultSearchForm input[name='currentPage']").val(page);
        defaultSearchForm.submit();
    }
    
 	var cb = function(data){
    	console.log("aaaaaaaaa")
    	console.log(data);
    } 
	</script>
	
</head>


<body>
    <div class="frame">
        <div class="frame_wrap">
            
            <%// gnb include %>
            <%@ include file="/WEB-INF/views/common/incGnb.jsp"%> 
        
            <div class="sub_conts">
            
                <!-- #### Content area ############### -->
                
                <article>
                    <div class="tit_wrap">
                        <h3><spring:message code="incgnb.dashboard" text="대시보드" /></h3>
                        <div class="sect_right">
                            <!-- <div  class="month"> 
                                <input type="text" class="date" />
                                <span>~</span>
                                <input type="text" class="date" />
                            </div> -->
                        </div>
                    </div> <!--tit_wrap end-->
                    <div class="dashbd">
                        <ul>
                            <li>
                                <h4><spring:message code="index.titie01" text="주문 현황" /></h4>
                                <div class="status">
                                    <p class="count">${orderData.orderDay} <spring:message code="common.unit.case" text="건" /></p>
                                    <p class="m_total">
                                        <spring:message code="index.nowMonth" text="이번달" /> <span><spring:message code="common.unit.total" text="총" /> <i>${orderData.orderMonth}</i> <spring:message code="common.unit.case" text="건" /></span>
                                    </p>
                                </div>
                            </li>
                            <li>
                                <h4><spring:message code="index.titie02" text="배송 현황" /></h4>
                                <div class="status">
                                    <p class="count">${orderData.shipmentDay} <spring:message code="common.unit.case" text="건" /></p>
                                    <p class="m_total">
                                        <spring:message code="index.nowMonth" text="이번달" /> <span><spring:message code="common.unit.total" text="총" /> <i>${orderData.shipmentMonth}</i> <spring:message code="common.unit.case" text="건" /></span>
                                    </p>
                                </div>
                            </li>
                            <li>
                                <h4><spring:message code="index.titie03" text="배송 완료" /></h4>
                                <div class="status">
                                    <p class="count">${orderData.salesDay} <spring:message code="common.unit.case" text="총" /></p>
                                    <p class="m_total">
                                        <spring:message code="index.nowMonth" text="이번달" /> <span><spring:message code="common.unit.total" text="총" /> <i>${orderData.salesMonth}</i> <spring:message code="common.unit.case" text="총" /></span>
                                    </p>
                                </div>
                            </li>
                        </ul>
                    </div><!-- dashbd end-->
                    <div class="tit_wrap mt20">
                        <h3><spring:message code="index.titie04" text="공지사항" /></h3>
                    </div>
                    <div class="tit_wrap mt10">
                        <ul class="tab">
                            <li id="notice" ><a href="/" ><spring:message code="notice.menuKshipping" text="K-Shipping" /></a></li>
                            <li id="shipping" class="on" ><a href="/selectPost"><spring:message code="notice.menuPost" text="배송사" /></a></li>
                        </ul>                      
                        <div class="sect_right">
                            <p class="bd_count"><spring:message code="common.unit.total" text="총" /> ${paging.countNum}<spring:message code="common.unit.case" text="건" /></p>
                        </div>
                    </div> <!--tit_wrap end-->
                    
                    <table class="tbtype mt10">
                        <colgroup>
                            <col class="cper0780"/>
                            <col class="cper1100"/>
                            <col class="cperauto"/>
                            <col class="cper1100"/>
                        </colgroup>
                        <thead>
                            <tr>
                                <th><spring:message code="board.idx" text="번호" /></th> 
                                <th><spring:message code="board.part" text="구분" /></th>
                                <th><spring:message code="board.title" text="제목" /></th>
                                <th><spring:message code="board.regDate" text="등록일" /></th>
                            </tr>
                        </thead>
                        <tbody>
                        
                        <c:choose>
	                        <c:when test="${fn:length(list) == 0}">
	                        <tr>
                                <td class="t_center" colspan="4"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></td>
                            </tr>
	                        </c:when>
	                        <c:otherwise>
	                        
	                        <c:forEach items="${list}" var="item" varStatus="status">
	                        
                            <tr>
                                <td class="t_center"><p>${item.seq}</p></td>
                                <td class="t_center"><p>${item.group_name}</p></td>
                                <td class="postlist">
                                    <a href="#" onclick="return false;" class="title">${item.title}</a>
                                    <div style="display:none" class="content">${item.content}</div>
                                </td>
                                <td class="t_center"><p>${item.writeday}</p></td>
                            </tr>	    
                            
                            </c:forEach>                    
	                        
	                        </c:otherwise>
                        </c:choose>     
                            
                        </tbody>
                    </table>
                    <div class="pager">
                        <c:if test="${fn:length(list) > 0}">
                        <%@ include file="/WEB-INF/views/common/incPaging2.jsp"%>
                        </c:if>
                        <form name="defaultSearchForm" id="defaultSearchForm" method="get">
                        <input type="hidden" name="pageSize" value="10">
                        <input type="hidden" name="currentPage" value="">
                        </form>
                    </div>
                
                <!--// #### Content area ############### -->
                
                </article>
                
            </div> 
        </div>
    </div> 
    
    <%// footer include %>
    <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
           
</body>
</html>
