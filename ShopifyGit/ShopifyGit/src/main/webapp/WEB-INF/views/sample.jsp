<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<!-- 
**************************************************
화면코드 :UI_SYS_412_BAS 
기능설명 : /setting/seller - 기본정보
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
        // form send event
        var $submitform = $('#submitform');
        
        $(".ic_search").on('click', function () {
            alert("aaa");    
        });
        
        $("#testButton").on('click', function () {
            alert();
        });
    };
    
    var sendFrom = function(data){
        if(data.errCode == true) {
            alert("수정이 완료 되었습니다.");
            location.reload();
        } else {
            alert("수정 중 오류가 발생했습니다.");
        }
    }
    </script>
</head>
<body>
    <div class="frame">
       <div class="frame_wrap">
        
        
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incGnb.jsp"%> 

    
        
        <!-- <spring:message code="index.hello" text="hello text" /> -->
    
            <div class="sub_conts">
                <article>
                
                <!-- #### Content area ############### -->
                
                    <!-- tit_wrap -->
                    <div class="tit_wrap">
                        <h3><spring:message code="index.test" text="테스트" /></h3>
                        <div class="sect_right">
                        <!-- 기본 검색 영역 -->
                            <select><option>결제 상태</option></select>
                            <div  class="month"> <!--아이콘이 버튼으로 별도작업되야하면 말씀해주세요. 다시 전달해드리겠습니다.-->
                                <input type="text" value="" class="date" />
                                <span>~</span>
                                <input type="text" class="date" />
                            </div>
                            
                            <select><option>주문 번호</option></select>
                            <div class="searchbox active"> <!--인풋박스에 클릭시 active 클래스 추가-->
                                <input type="text" placeholder="SearchBox">
                                <button type="button" class="ic_search"></button>
                            </div>
                        <!--// 기본 검색 영역 -->
                        </div>
                    </div> 
                    <!--// tit_wrap end --> 
                    
                    <div class="module_group">  
                        <div class="btn_group">
                            <button type="button" class="btn_type1 bundle"><span>계정관리</span></button>
                            <button type="button" class="btn_type1 new"><span>배송관리</span></button>
                        </div>
                        <div class="action">
                            <select><option>10개</option></select>
                        </div>
                    </div>
                    
                    <table class="tbtype">
                        <colgroup>
                            <col class="wp35">
                            <col class="cper0860"/>
                            <col class="cper1300"/>
                            <col class="cperauto"/>
                            <col class="cper1300"/>
                            <col class="cper0900"/>
                            <col class="cper1250"/>
                            <col class="wp50"/>
                            <col class="cper0950"/>
                        </colgroup>
                        <thead>
                            <tr>
                                <th><input type="checkbox" id="ind01"><label for="ind01"><span class="icon_ck" ></span></label></th>
                                <th>주문번호</th>
                                <th>주문일자</th>
                                <th>상품명</th>
                                <th>주문자정보</th>
                                <th>택배사</th>
                                <th>결제상태</th>
                                <th>라벨</th>
                                <th>결제금액</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                            <td><input type="checkbox" id="ind02"><label for="ind02"><span class="icon_ck" ></span></label></td>
                                <td class="t_center"><p>103458</p></td>
                                <td class="t_center"><p>2019-11-30</p></td>
                                <td><p>크리스마스 머그컵</p></td>
                                <td class="t_center"><p>(KR) 조수아</p></td>
                                <td class="t_center"><p>EMS</p></td>
                                <td class="t_center"><div class="btn_state cancel">환불완료</div></td>
                                <td class="t_center"><a href="#" class="btn_label "></a></td>
                                <td class="t_right"><p>25,000</p></td>
                            </tr>
                        </tbody>
                    </table>
                    
                    <div class="pager">
                        <a href="#" class="btn_prev"></a>
                        <a href="#" class="on">1</a>
                        <a href="#">2</a>
                        <a href="#">3</a>
                        <a href="#">4</a>
                        <a href="#">5</a>
                        <a href="#">6</a>
                        <a href="#">7</a>
                        <a href="#">8</a>
                        <a href="#">9</a>
                        <a href="#">10</a>
                        <a href="#" class="btn_next"></a>
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