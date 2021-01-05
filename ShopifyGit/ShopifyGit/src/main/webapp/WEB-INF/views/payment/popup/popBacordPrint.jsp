<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<style>
        body {
            margin: 0;
            padding: 0;
            font: 12pt "Tahoma";
        }
        * {
            box-sizing: border-box;
            -moz-box-sizing: border-box;
        }
        .page {
            width: 21cm;
            min-height: 29.7cm;
            padding: 0cm;
            margin: 1cm auto;
            border-radius: 5px;
            background: white;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
        }
        .subpage {
            padding: 1cm;
            height: 290mm;
        }
        @page {
            size: A4 portrait;
			/*size: A4 landscape;*/
            margin: 0;
            /*size: landscape;*/
        }
        @media print {
            .page {
                margin: 0;
                border: initial;
                border-radius: initial;
                width: initial;
                min-height: initial;
                box-shadow: initial;
                background: initial;
                page-break-after: always;
            }
        }
		
   </style>
<script type="text/javascript">
	$(function () {
		var initBody;
	
	    initialize();
	    $(document).on("click",".btnPrint",function(){
	    	pageprint();
	    });
	});
	
	var initialize = function () {
	    initControls();
	    bindEvent();
	};
	
	var initControls = function () {
	    /* setDate();
	    bindSelectAjax($('#ajax-form')); */
	};
	
	var bindEvent = function () {
	    
	};
	
	var beforePrint = function() {
	    initBody = document.body.innerHTML;
	    document.body.innerHTML = $("#submitform").html();
	}
	var afterPrint = function() {
	    document.body.innerHTML = initBody;
	}
	var printDiv = function() {
	    window.onbeforeprint = beforePrint;
	    window.onafterprint = afterPrint;
	    $("#btn").hide();
	    window.print();
	}
	var pageprint = function()
	{
	  var divToPrint=document.getElementById('submitform');
	  var newWin=window.open('','Print-Window');
	  newWin.document.open();
	  newWin.document.write('<html>');
	  newWin.document.write('<meta charset="UTF-8" />');
	  newWin.document.write('<meta name="_csrf_parameter" content="_csrf" />');
	  newWin.document.write('<meta name="_csrf_header" content="X-XSRF-TOKEN" />');
	  newWin.document.write('<meta name="_csrf" content="469a8e07-0d81-410f-afeb-9cc9acea2a70" />');
	  newWin.document.write('<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />');
	  newWin.document.write('<meta http-equiv="X-UA-Compatible" content="IE=edge" />');
	  newWin.document.write('<meta name="viewport" content="initial-scale=1.0, width=device-width">');
	  newWin.document.write('<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">');
	  newWin.document.write('<link rel="icon" href="/favicon.ico" type="image/x-icon">');
	  newWin.document.write('<link rel="stylesheet" href="https://unpkg.com/@shopify/polaris@4.10.2/styles.min.css"/>');
	  newWin.document.write('<link type="text/css" rel="stylesheet" href="/style/reset.css?v=1586212539427" />');
	  newWin.document.write('<link type="text/css" rel="stylesheet" href="/style/k_shipping.css?v=1586212539427" />');
	  newWin.document.write('<body onload="window.print()">'+divToPrint.innerHTML+'</body></html>');
	  $("#btn").hide();
	  newWin.document.close();
	  setTimeout(function(){newWin.close();},10);
	}
	
</script>
<form name="submitform" id="submitform">
	<div class="book">
		<div class="page">
			<div class="subpage" id="content">
				<table border=1>
					<tr><td height="30px" colspan="2">&nbsp;</td></tr>
					<c:forEach items="${invoiceList}" var="item" varStatus="status">
					<c:if test="${ status.count % 2 == 1 }"><tr height=83><td width=350  align="left"></c:if>
					<c:if test="${ status.count % 2 == 0 }"><td width=450 align="right"></c:if>
					<img src="${pageContext.request.contextPath }/shipment/popup/popCreateBarcode/${item.masterCode },${item.invoice }" width="285" height="83" >
					<c:if test="${ status.count % 2 == 1 }"></td></c:if>
					<c:if test="${ status.count % 2 == 0 }"></td></tr><tr><td height="45px" colspan="2">&nbsp;</td></tr></c:if>
					<c:if test="${ status.count % 16 == 0 }"><div class="endline"></div><br style="height:0; line-height:0"></c:if>
					</c:forEach>
				</table>
				<table border=1 id="btn" style="!important;postion:relative;width:100%;height:20px;text-align:right;">
					<tr><td height="28px"><div class="pop_btn" style="padding-bottom:100px"><button type="button" class="btn_type2 cper100p btnPrint">Print</button></div></td></tr>
				</table>
            </div>
        </div>
    </div >
	
</form>