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
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 
    
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
    	console.log(getMessage("index.hello"));  // 다국어 적용
    };
    
    var bindEvent = function () {
        
    };
    </script>
</head>
<body>
    <div class="wrap">
        
        
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incAdmGnb.jsp"%> 

        <!-- #### Content area ############### -->

        <div class="contents">
           <div class="cont_body">
               <article>
                    <h2><spring:message code="index.test" text="테스트" /></h2>
                    <div class="module_group mt20">
                        <div class="action">
                            <select><option>이메일</option></select>
                            <div class="searchbox"> <!--인풋박스에 클릭시 active 클래스 추가-->
                                <input type="text" placeholder="SearchBox">
                                <button type="button" class="ic_search"></button>
                            </div>
                            <select><option>10개</option></select>
                        </div>
                    </div>
                    <table class="tbtype">
                        <colgroup>
                            <col class="wp50">
                            <col class="cperauto">
                            <col class="cperauto">
                            <col class="cper1100">
                            <col class="cper0780">
                            <col class="cper0780">
                        </colgroup>
                        <thead>
                            <tr>
                                <th>번호</th>
                                <th>이메일</th>
                                <th>쇼핑몰명</th>
                                <th>이름</th>
                                <th>등급</th>
                                <th>활동상태</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td class="t_center"><p>68</p></td>
                                <td><p>abcdef@hanmail.net</p></td>
                                <td><a>다팔자쇼핑몰</a></td>
                                <td class="t_center"><p>조수아</p></td>
                                <td class="t_center"><p>A</p></td>
                                <td class="t_center"><p>활동</p></td>
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
               </article>
           </div>
        </div>
           
        <!--// #### Content area ############### -->

    </div>
 
                
   <%// footer include %>
   <%@ include file="/WEB-INF/views/common/incAdmFooter.jsp"%>

         
</body>
</html>