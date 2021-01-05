<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
 
<!-- 
**************************************************
화면코드 :UI_SYS_541_BAS 
기능설명 : 관리자 목록 조회
Author   Date      Description
 김윤홍     2020-01-06  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
<head>
        <meta charset="UTF-8" />

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="initial-scale=1.0, width=device-width" />

        <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
        <link rel="icon" href="/favicon.ico" type="image/x-icon" />

        <link type="text/css" rel="stylesheet" href="/style/reset.css" />
        <link type="text/css" rel="stylesheet" href="/style/admin.css" />
        
        <style type="text/css">
		    tbody tr.meta td {height:20px;  font-size: 80%;  background-color: lightgrey;  color: blue;}
		    tbody tr.exRow td { font-family: Consolas,monaco,monospace; }
		    .foot_btn { float: left; }
            span.dateLine { color:red; }
	    </style>

        <script src="//code.jquery.com/jquery-1.12.4.js"></script>
        <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" />

<!--         <link type="text/css" rel="stylesheet" href="/style/spinner.css" /> -->

<!--         <script src="/js/common.js"></script> -->
<!--         <script src="/js/util.js"></script> -->

        <title>Exception List</title>

        <script type="text/javascript" src="/js/admin/exceptionList.js"></script>
</head>
<body>
    <h2>Exception List</h2>
	<table class="tbtype">
	    <colgroup>
	        <col class="cper1250" />
	        <col class="cperauto" />
	        <col class="cper1250" />
	        <col class="cper2500" />
	        <col class="cper1250" />
	    </colgroup>
	    <thead>
	        <tr>
	            <th>Type</th>
	            <th>Class Name</th>
	            <th>Instance</th>
	            <th>File Name</th>
	            <th>Line No</th>
	        </tr>
	    </thead>
	    <tbody id="exTable">
	    </tbody>
	</table>
	<div>
		<div class="btn_group foot_btn">
		           TimeStamp
		         <input type="text" name="timestamp" id="timestamp" value="" maxlength="20"/>
		    
		    <button type="button" id="searchDate"  class="btn_type2" >검색</button>
		</div>
	</div>
</body>
</html>