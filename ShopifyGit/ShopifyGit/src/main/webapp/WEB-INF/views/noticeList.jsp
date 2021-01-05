<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_ADM_611_TAB 
기능설명 : NOTICE 리스트 조회
Author   Date      Description
 김윤홍     2019-12-23  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incHeader.jsp"%>

    <title>NOTICE 리스트 조회</title>
    
    <script type="text/javascript" src="/js/noticeList.js"></script>

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
                                    <p class="count">${orderData.orderDay} <spring:message code="common.unit.case" text="총" /></p>
                                    <p class="m_total">
                                        <spring:message code="index.nowMonth" text="이번달" /> <span><spring:message code="common.unit.total" text="총" /> <i>${orderData.orderMonth}</i> <spring:message code="common.unit.case" text="총" /></span>
                                    </p>
                                </div>
                            </li>
                            <li>
                                <h4><spring:message code="index.titie02" text="배송 현황" /></h4>
                                <div class="status">
                                    <p class="count">${orderData.shipmentDay} <spring:message code="common.unit.case" text="총" /></p>
                                    <p class="m_total">
                                        <spring:message code="index.nowMonth" text="이번달" /> <span><spring:message code="common.unit.total" text="총" /> <i>${orderData.shipmentMonth}</i> <spring:message code="common.unit.case" text="총" /></span>
                                    </p>
                                </div>
                            </li>
                            <li>
                                <h4><spring:message code="index.titie03" text="매출 현황" /></h4>
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
                            <li id="kshipping" class="on"><a href="/" ><spring:message code="notice.menuKshipping" text="K-Shipping" /></a></li>
                            <li id="post"><a href="/selectPost"><spring:message code="notice.menuPost" text="배송사" /></a></li>
                        </ul>                
<%--                         <div class="sect_right">
                            <p class="bd_count"><spring:message code="common.unit.total" text="총" /> ${paging.countNum}<spring:message code="common.unit.case" text="총" /></p>
                        </div> --%>
                    </div> <!--tit_wrap end-->
<!--                     		
                    		<ul class="tab_conts">
                    			<li> -->
                    				<div class="tit_wrap mt0">
										<div class="sect_right">
										<form name="searchform" id="searchform" method="get">
											
											<select class="select-ajax" id="searchType" name=searchType
											 data-codetype="" data-code="${search.searchType}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "D040000" }'></select>
		                                    
		                                    <div class="searchbox"> <!--인풋박스에 클릭시 active 클래스 추가-->
		                                        <button type="button" class="ic_search" id="btn_search"></button>
		                                        <input type="text" id="searchWord" value="${search.searchWord}" name="searchWord" placeholder="<spring:message code="order.seaech.orderWord.label" text="검색어를 입력해주세요." />">
		                                    </div>
		                                    
		                                    <%-- 페이지 사이즈 --%>
                                            <%@ include file="/WEB-INF/views/common/incPageSize.jsp"%>
										
										</form>
		                                    
                                		</div>
                            		</div>
<!-- 									<form name="submitform" id="submitform" method="get" action="/selectNotice"> -->
 									<input type="hidden" id="noti_svc" name="noti_svc" value="D010003">
									<c:choose>
										<c:when test="${fn:length(list) == 0}">
											<tr>
												<div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
											</tr>
										</c:when>
									<c:otherwise>   
					                   <table class="tbtype mt10">
					                       <colgroup>
					                           <col class="cper0780"/>
					                           <col class="cper1460"/>
					                           <col class="cperauto"/>
					                           <col class="cper1460"/>
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
					                       									                        		
                                             <c:forEach items="${list}" var="list" varStatus="status">
                                                 <c:if test="${list.flagTop eq 'K020010'}">                                            
                                                 <tr style="color:blue;font-weight: bold" data-idx="${list.idx}">
                                                         <td class="t_center list-edit"><p><spring:message code="notice.flag" text="[필독]" /></p></td> 
	                                                     <td class="t_center list-edit">${list.partName}</td>
	                                                     <td style="cursor : pointer" class="list-edit">${list.title}</td>
	                                                     <td class="t_center list-edit">${list.notiFromDate}</td>                                                   
										          </tr>
	 								           </c:if>
                                                 <c:if test="${list.flagTop eq 'K020020'}">                                            
                                                 <tr data-idx="${list.idx}">
                                                         <td class="t_center list-edit"><p>${paging.countNum - status.index-1}</p></td> 
                                                         <td class="t_center list-edit">${list.partName}</td>
                                                         <td style="cursor : pointer" class="list-edit">${list.title}</td>
                                                         <td class="t_center list-edit">${list.notiFromDate}</td>                                                   
                                                  </tr>
                                               </c:if>									        
                                           </c:forEach>									   
										</c:otherwise>
									</c:choose>
                                </tbody>
	                            </table>
	                            </form>
		                            
		                            <div class="pager">
				                        <c:if test="${fn:length(list) > 0}">
				                        <%@ include file="/WEB-INF/views/common/incPaging2.jsp"%>
				                        </c:if>
				                        <form name="defaultSearchForm" id="defaultSearchForm" method="get">
				                        <input type="hidden" name="searchType" value="${search.searchType}">
				                        <input type="hidden" name="searchWord" value="${search.searchWord}">
				                        <input type="hidden" name="pageSize" value="${search.pageSize}">
				                        <input type="hidden" name="currentPage" value="${search.currentPage}">
				                        </form>
				                    </div>
		                            
<%-- 		                            <div class="button">
		                                <button type="button" class="btn_type3" id="btn_write"><span><spring:message code="button.write" text="작성" /></span></button>
		                            </div>		 --%>
<!--                            		</li>
                           </ul> -->
						<!--// tit_wrap end -->
						
					<!--// #### Content area ############### -->

				</article>
			</div>
		
		</div>
	</div>
    
    <%// footer include %>
    <%@ include file="/WEB-INF/views/common/incFooter.jsp"%>
</body>
</html>