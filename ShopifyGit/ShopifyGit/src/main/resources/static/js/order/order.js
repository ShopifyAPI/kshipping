var $submitform = $('#ajax-form');

$(function() {
	initialize();
	openShip();
});

var initialize = function() {
	initControls();
	bindEvent();
};

var initControls = function() {
	bindSelectAjax($('#ajax-form'));
	setSort();
};

var bindEvent = function() {
	// onKeyUp > Enter(13)
	$("input[name]").keyup(function(e){ if (e.keyCode == 13){ search(); } });

	// 주문목록 > 검색 버튼
	$(".ic_search").on("click", function(){ if(fnValidationCheckForInput($submitform)){ search(); } });

	// 주문목록 > 합배송 버튼
	$(document).on("click",".bundle",function(){
		if($("input[name='orderIdxChk']").is(":checked")){
			makeCombine();	
		} else {
			alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
		}
	});
	// 주문목록 > 배송생성 버튼
	$(document).on("click",".new",function(){ createDelivery(); });
	// 주문목록 > 새로고침 버튼
	$(".refresh").on("click", function() { location.href="/order?loadCheck=Y"; });
	// 주문목록 > 내려받기 버튼
	$(".down").on("click", function(){ down(); });
	// 주문목록 > 삭제
	$(".del").on("click", function(){ del(); });
	// 주문목록 > 체크박스 컨트롤
	$(document).on("click", "input[name='allCheck']",function(){ allCheck("orderIdxChk", this); });
	// 주문목록 > 정렬기준
	$(document).on("click", "table.tbtype thead tr th", function() {
		if ($(this).data("sort-name") != null)  {
			var sortName = $(this).data("sort-name");
			var oldSortName = $("input[name='sortOrder']").val();
			if (oldSortName=="DEC"+sortName) {
				$("input[name='sortOrder']").val("ASC"+sortName);
			} else {
				$("input[name='sortOrder']").val("DEC"+sortName);
			}
			gotoPage(1);
		}
	});

	// 주문 팝업 > 주문합치기 버튼 (합배송시작)
	$(document).on("click", "#btnCombineNext", function() {   
		var orderCode = $(".pop_body input[name='arrOrderCode']").val();
		var shopIdx = $(".pop_body input[name='arrShopIdx']").val();
		combineOrderAddrPop(orderCode, shopIdx);
	});
	// 합배송 btnCombineProc click
	$(document).on("click",".btnCombineProc",function(){   
		viewCombineArea($(this));
	});
	// 배송 정보 등록 팝업
	$(document).on("click", ".btn_label", function(){
		popupAddress($(this).closest("tr").data("code"), $(this).closest("tr").data("shopidx"), $(this).closest("tr").data("country"));
	});
	// hscode 의 값이 empty 이면
	$(document).on("change","input[name='arrHscode']",function(){
		if ( $(this).val() == "" ) {
			toggleHsButton(false); 
		}
	});

	var openf = function(){}
	var funcf = function(){}
	var closef = function(){}
};

////////////////////////////////////////////////////////////////////////////////
////	팝업 > 탭이동
////////////////////////////////////////////////////////////////////////////////
// 배송정보 팝업 > 주소등록 탭 이동
// 배송정보 팝업 > 관세정보 > 이전 버튼
var popupAddress = function(code, idx, country){
//	if ( country == "KR" ) {
//		alert( "국내택배는 아직 처리할 수 없습니다." ); 
//		return;
//	}
	
	allClosePop();  
	var url;
	var openFunc;
	
	if (country == "KR") {
		 url = "/order/popup/popOrderCreateDomestic";
		openPop(url + "?code=" + code + "&idx=" + idx, "", 1350, "", initPopup, "");
	} else {
		url = "/order/popup/popOrderCreateExpress";
		openPop(url + "?code=" + code + "&idx=" + idx, "", 1350, "", popupExpressInit, "");
	}
//	openPop(url + "?code=" + code + "&idx=" + idx, "", 1350, "", openFunc, "");
}
//// 배송정보 팝업 > 관세정보 탭 이동
//var popupExpress = function(code, idx){
////	allClosePop();
////	openPop("/order/popup/popOrderCreateExpress?code=" + code + "&idx=" + idx, "", 1350, "", popupExpressInit, "");
//	
//	var url = "/order/popup/popOrderCheckCustoms";
//	var param = { code : code, idx : idx };
//	ajaxSyncCall("post", url, param, customCallBack, code, idx, popupSelectBind);
//	
//}
//// 배송정보 팝업 > 배송사 선택 탭 이동
//var popupCustoms = function(code, idx){
//	var url = "/order/popup/popOrderCheckCustoms";
//	var param = { code : code, idx : idx };
//	ajaxSyncCall("post", url, param, customCallBack, code, idx, popupSelectBind);
//}
//
//
////저장 완료
//var customCallBack = function(data, code, idx, func) {
//	if ( data.msgCode == "SUCCESS") {
//		allClosePop();
////		openPop("/order/popup/popOrderCreateCustoms?code=" + code + "&idx=" + idx, "", 1350, "", func, "");
//		openPop("/order/popup/popOrderCreateExpress?code=" + code + "&idx=" + idx, "", 1350, "", popupExpressInit, "");
//	} else {
//		alert(getMessage(data.msgCode));
//		$('#popupExpress').addClass('off');
//	}
//}

////////////////////////////////////////////////////////////////////////////////

var openShip = function() {
	var params = new window.URLSearchParams(window.location.search);
	var currPosition = params.get('currPosition');
	var flag = false;   // 현재 번호까지는 skip 하기 위하여
	var label;
	if ( currPosition ) {
		$('table.tbtype input[name="orderIdxChk"]').each( function() {
			if ( flag ) {
				if (  $(this).hasClass('order_ing') && $(this).data("country") != "KR" ) {
					label = $(this).closest("tr").find("div.btn_label"); 
					return false;   // break from each loop 
				}
			} else {
				if ( $(this).val() == currPosition ) {
					flag = true;
				}
			}
		});
	}
	if ( label ) {
		$(label).click();
	}
};

var gotoPage = function(page) {
	var $submitform2 = $('#defaultSearchForm');
	$("input[name='currentPage']").val(page);
	$submitform2.submit();
}

var search = function(){
	frm.submit();
}

var setSearchView = function(data){

}

var del = function(){
	var delCnt = 0;
	if($("input[name='orderIdxChk']").is(":checked")){
		delCnt++;
	}
	if(delCnt == 0){
		alert( getMessage("alert.deleteChk") ); // 삭제할 대상을 선택해주세요
		return;
	}
	if(confirm(getMessage("alert.deleteConfirm"))){  // 선택항목을 삭제 하시겠습니까?
		var $submitform = $('#sendForm');
		var type = "post";
		var url = "/order/delete";
		var param = $submitform.serializeObject();
		// console.log(param) ;
		ajaxCall(type, url, param, setDelete);
	}
}

var setDelete = function(){
	alert(getMessage('alert.deleteMessage'));
	search();
}

var down = function(){
	 var param = {
             "searchDateStartValue" : $("input[name=searchDateStart]").val(),
             "searchDateEndValue"   : $("input[name=searchDateEnd]").val(),
             "searchDestType"       : $("input[name=searchDestType]").val(),
             "searchCompany"        : $("input[name=searchCompany]").val()
  };
	var url_="/order/orderExcel?" + $.param(param);
	location.replace(url_);
}

// 국내/해외 변경
var changeDestType = function(val){
    $("#defaultSearchForm input[name='searchDestType']").val(val);
    $("#defaultSearchForm input[name='currentPage']").val("1");
    
    defaultSearchForm.submit();
}

var changeCompany = function(val){
    $("#defaultSearchForm input[name='searchCompany']").val(val);
    $("#defaultSearchForm input[name='currentPage']").val("1");
    
    defaultSearchForm.submit();
}

//배송생성
var createDelivery = function() {
	if(!$("input:checkbox[name='orderIdxChk']").is(":checked")){
		alert(getMessage('alert.checkboxChk')); //대상을 선택해주세요.
		return;
	}
	var check = true;
	var orderName = "";
	// 정보 입력 체크 
	$("input:checkbox[name='orderIdxChk']:checked").each(function() {
		if(check) {
			if( $(this).hasClass("order_ing") ) {
				orderCode = $(this).val();
				orderName = $(this).data("ordername");
				check = false;
			} else {
				//
			}
		}
	});
	if(check == false) {
		alert("[" + orderName + "] " + getMessage('order.payment.err')); //배송 정보를 확인해주세요.
		return;
	}
	//생성
	if(confirm(getMessage('order.popup.customerAlert.creat'))){
		var url = "/order/orderCreateCustomsProc";
		var param = $("#sendForm").serializeObject();
		ajaxCall("post", url, param, sendCreateFrom);
	}
}

var sendCreateFrom = function(data){
	if(data.errCode == true) {
		location.href="/payment";
	} else {
		alert(getMessage("alert.proc.err", ["button.save"]));  // 수정 중 오류가 발생했습니다.
	}
}

var getHsParam = function(){
	var param = [];
	$("table.boxlist tbody tr").each( function(index, row){
		var goodsCode = $(row).find('input[name="arrGoodsCode"]').val();
		var hscode = $(row).find('input[name="arrHscode"]').val();
		var id = $(row).find('input[name="arrHscode"]').attr("id");
		var shopIdx = $('input[name="shopIdx"]').val();
		if ( hscode == null || hscode == "" ) {
			var map = {};
			map.id = id;
			map.goodsCode = goodsCode;
			map.shopIdx = shopIdx;
			param.push(map);
		} 
	});
	return param;
}

var customHsCode = function(data, code, idx, func) {
	$.each(data.mapList, function(idx, obj) {
		var id = "#" + obj.id;
		$(id).val(obj.hscode);
	});
	toggleHsButton(true);
}

var toggleHsButton = function(flag){
	var param = getHsParam();
	if ( param.length == 0 ) {
		$("#hscodeBtn").attr('disabled', true);
		$("#hscodeBtn").removeClass('btn_type5').addClass('btn_type6');
	} else {
		$("#hscodeBtn").attr('disabled', false);
		$("#hscodeBtn").removeClass('btn_type6').addClass('btn_type5');
		if ( flag == true ) {
			alert(getMessage("alert.hscode.not.found"));  // 수정 중 오류가 발생했습니다.
		}
	}
}

var ajaxSyncCall = function(type, url, param, callbackFunction, code, idx, func) {
	var returnData;
	$.ajax({
		type       : type
		,async     : false  // true: 비동기, false: 동기
		,url       : url
		,cache     : false
		,data      : JSON.stringify(param) 
		,contentType: "application/json"
		,dataType  : "json"
		,beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		}
		, success   : function(data) {
			returnData = "success";
			if(callbackFunction != "") {
				callbackFunction(data, code, idx, func);
			};
		}
		, error     : function(error) {
			returnData = "error";
//			console.log(error);
		}
	});
	return returnData; 
};

//팝업 오픈 오류 
var callPopupError = function(){
	alert(getMessage('error.500'));
	allClosePop();
}

// 팝업 > 관세정보등록 화면 세팅
var popupExpressInit =  function () {
	
	composeBuyerAddress();
	
	bindSelectAjax($("#ajax-popForm"));
	
//	if ( $("#boxSelect option:selected").data("boxunit") ) {
//		$("#boxSelect").trigger("change");
//	}
//	
	onSelectSender();
	
	changeBox(true);
	
//	$obj = $(".courier-area").find(".on");
//	var payment = $obj.data("price");
//	var courier = $obj.data("courier");
//	var id = $obj.data("id");
//	var courierId = $obj.data("courierid");
//	var rankPrice = $obj.data("rankprice");
//	$("input[name='courierCompany']").val(id);
//	$("input[name='courier']").val(courier);
//	$("input[name='payment']").val(payment);
//	$("input[name='courierId']").val(courierId);
//	$("input[name='rankPrice']").val(rankPrice);
	
//	setTotalWeight("init");
	
	toggleHsButton(false);
	
}

//팝업 select box 세팅
var popupSelectBind = function () {
	bindSelectAjax($('#ajax-popForm'));
}

//출고지 수정
var setTotalWeight = function(mode) {
	var prodWeight = 0;
	var weight = 0;  
	var unit = ""; 
	var quantity = "";
//	var baxWeight = 0;
	var boxWeight = unComma($("#weight").val());

	$(".pop_body .boxlist tbody").find(".weight-data").each(function(){
		weight = (mode == "init" ? $(this).data("weight") : $(this).closest("tr").find("input[name='arrWeight']").val()); // 중량 확인
		quantity = (mode == "init" ? $(this).data("quantity") : $(this).closest("tr").find("input[name='arrQuantity']").val()); // 중량 단위 확인
		prodWeight += weight * quantity;   
	});
	
//	// 총무게는 kg으로 처리 (상품무게 + 박스무게)
	var totalWeight = Number(prodWeight) + Number(boxWeight);
	$("#totalWeight").val( addComma( totalWeight) );
	
	var divisor = 6;
	var boxUnit = $("#boxUnit").val();
	if ( boxUnit == "cm" ) {
		;
	} else if ( boxUnit == "in" ) {
		 divisor = 166;
	} else {
//		alert("길이 단위에 문제가 있습니다.");
//		divisor = 0;
	}
	
	var volumeticWeight = $("#boxLength").val() * $("#boxWidth").val() * $("#boxHeight").val() / divisor;
	volumeticWeight = Math.ceil(volumeticWeight || 0) ;    // volumetic 이 숫자가 아면 0 로 변환함
	$("#volumeticWeight").val( addComma(volumeticWeight) );
	
	var weightUnit = $("input[name='weightUnit']").val();
	$("input[name='weightUnitProduct']").val( weightUnit );
	$("input[name='weightUnitVolumetic']").val( weightUnit );
}

/////////////////////////////////////////////////////////////////////////
// 합배송 관련 함수 
/////////////////////////////////////////////////////////////////////////

var makeCombine = function(code, idx){
	// 수량 체크 
	var max = 5;
	var min = 2;
	var maxWeight = 30000; // g단원 처리 
	var checkLength = $("input[name='orderIdxChk']:checked").size();

	if(checkLength < min || checkLength > max){ // 합매송 수량 초과
		alert(getMessage('order.popup.combine.maximum'));
	} else { //합배송 체크  
		var zipCode = $("input[name='orderIdxChk']:checked").eq(0).data("zipcode");
		var country = $("input[name='orderIdxChk']:checked").eq(0).data("country");
		var orderCourier = $("input[name='orderIdxChk']:checked").eq(0).data("courier");
		
		var check = "";
		var weight = 0;
		var orderCode = "";
		var shopIdx = "";
		var checkCompany = "";

		if (country == "KR" ) {
			alert( "국내택배는 아직 처리할 수 없습니다." ); 
			return;
		}
		
		$("input[name='orderIdxChk']:checked").each(function(){
			if(checkCompany== "" && check == "") {
				if(orderCourier != $(this).data("courier")) { // 동일 배송사 확인 
					checkCompany = $(this).data("ordername");
				}
				else if(country != $(this).data("country") || zipCode != $(this).data("zipcode")) { // 동일 배송지 확인 (국가/우편번호)
					check = $(this).data("ordername");
				}
			}
			
			orderCode += (orderCode == "") ? $(this).val() : "," + $(this).val();
			shopIdx += (shopIdx == "") ? $(this).data("shopidx") : "," + $(this).data("shopidx");

			weight += ($(this).data("weight") == "") ? 0 : $(this).data("weight"); // 중략 체크
		});

		if(check != "") { // 같은 주소지가 아닌 데이터가 있음
			alert(getMessage('order.popup.combine.error.address') + "["+check+"]");
		} else if (weight > maxWeight) { // 기본 중량 초과
			alert(getMessage('order.popup.combine.error.weight'));
		} else if (checkCompany != "") { // 배송사가 다를 때
			alert(getMessage('order.popup.combine.error.company')); 
		} else { // 합배송 처리용 
			var url = (country == "KR") ? "/order/popup/popOrderCreateDomestic?code=" : "/order/popup/popOrderCombine?arrOrderCode=";
			if(country=="KR"){openPop(url+orderCode+"&idx="+shopIdx+"", "", 1350);}
			else{openPop(url+orderCode+"&idx="+shopIdx+"", "", 500);}
		}
	}
}

// 합배송 > 주소입력 팝업
var combineOrderAddrPop = function(code, idx){
	allClosePop();
	openPop("/order/popup/popOrderCombineInput?arrOrderCode=" + code + "&arrShopIdx=" + idx, "", 1350, "", combineSelectBind, "");
}
// 
var combineSelectBind = function(){
	bindSelectAjax($('#frmCombineStep01'));
	bindSelectAjax($('#frmCombineStep02'));
	setTotalWeight("init");
}

// 합배송 화면 저장
var viewCombineArea = function($obj){
	var area = $obj.data("area");
	var proc = $obj.attr("name");

	var check = true;
	if(area == "popCombineStep02" && proc=="next") { // 주소 => 관세
		if(!fnValidationCheckForSelectbox($("#frmCombineStep01"))) {
			check = false;
		}

		if(!fnValidationCheckForInput($("#frmCombineStep01"))) {
			check = false;
		}

	} else if(area == "popCombineStep03" && proc=="next") { // 관세 => 배송사

		if(!fnValidationCheckForSelectbox($("#frmCombineStep02"))) {
			check = false;
		}

		if(!fnValidationCheckForInput($("#frmCombineStep02"))) {
			check = false;
		}

		if(check) {
			setDeliveryService();
		}

		check = false;
	} else if(area == "popCombineStepProc" && proc=="proc") { // 합 배송 생성
		sendCombineProc();
		check = false;
	}

	if(check) { // 화면 이동
		$obj.closest(".combine-view").removeClass("combine-view-on").addClass("combine-view-off");
		$(".pop_body #" + area).removeClass("combine-view-off").addClass("combine-view-on").scrollTop(0);
	}
}

// 합배송 배송서비스 페이지 처리 
var setDeliveryService = function() {
	var orderCode = $(".pop_body input[name='arrOrderCode']").val();
	var shopIdx = $(".pop_body input[name='arrShopIdx']").val();
	var nationCode = $(".pop_body select[name='buyerCountryCode']").val();
	var weight = $(".pop_body input[name='totalWeight']").val() * 1000;
	var check = false;
	var url = "/order/popup/popOrderCombineDelivery?nationCode=" + nationCode + "&weight=" + weight+"&arrOrderCode=" + orderCode + "&arrShopIdx=" + shopIdx+"";
	var param = {}
	$.get(url, param // 파라메터
			, function(data, status) { 
//		console.log("######## bindInputAddr [status] : " + status);
//		console.log("######## bindInputAddr [data] : " + data);

		if(status == "success"){
			$("#popCombineStep03").html(data); // 전송받은 데이터와 전송 성공 여부를 보여줌.

			$(".combine-view-on").removeClass("combine-view-on").addClass("combine-view-off");
			$(".pop_body #popCombineStep03").removeClass("combine-view-off").addClass("combine-view-on").scrollTop(0);
		} else {
			alert("오류가 발생했습니다. 다시 시도해 주세요.");
			//allClosePop();
		}
	}
	); 
}

// 합배송 배송서비스 생성
var sendCombineProc = function() {
	var courier = $(".courier-area .on").data("courier");
	var company = $(".courier-area .on").data("id");
	var price = $(".courier-area .on").data("price");
	var courierId = $(".courier-area .on").data("courierid");
	var rankPrice = $(".courier-area .on").data("rankprice");

	if(!courier) {
		alert(getMessage("alert.checkboxChk"));
		return;
	}

	$("input[name='courier']").val(courier);
	$("input[name='courierCompany']").val(company);
	$("input[name='price']").val(price);
	$("input[name='rankPrice']").val(rankPrice);
	$("input[name='courierId']").val(courierId);
	//$("input[name='proc']").val(proc);

	var url = "/order/popup/popOrderCombineProc";
	var parameter = $(".pop_body form").serializeObject();

	var pm = JSON.stringify(parameter);
//	console.log("[parameter]" + pm);

	if(confirm(getMessage("order.popup.customerAlert.creat"))){
		ajaxCall("post", url, parameter, saveCombineCourier);  
	}
}

var saveCombineCourier = function(data) {
	if(data.errCode == true) {
		var msg = getMessage("alert.saveMessage");

		alert(msg);  // 저장이 완료 되었습니다.
		location.href = "/payment";    
	} else {
		alert(getMessage("alert.proc.err", ["button.save"]));  // 저장 중 오류가 발생했습니다.
	}
}

//합배송 오류
var bundleError = function(err) {
	alert(getMessage('order.popup.combine.error.already') + "["+err+"]");
	allClosePop();
}
