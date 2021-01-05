$(document).ready(function() {
	// 배송정보 팝업 > 주소등록 > 보내는사람 > 출고지 선택
	$(document).on("change", "#selectSender", onSelectSender);
	$(document).on("change", "#selectSenderKr", onSelectSenderKr);
	// 배송정보 팝업 > 주소등록 > 보내는사람 > 주소찾기 (KAKAO MAP API)
	$(document).on("click", ".postcode", function() {
		// util.js > searchPost()
		searchPost('postcode', 'sellerAddr1', 'sellerAddr1Ename', 'sellerProvince', 'sellerCity', 'sellerAddr2');
	});
	// 배송정보 팝업 > 주소 등록 > 저장 버튼
	$(document).on("click","#saveAddrBtn", saveAddr);
	// 배송정보 팝업 > 관세정보 > 포장재 정보 > 포장재 명 
	$(document).on("change","#boxSelect", function(){ changeBox(false); });
	// 가로, 세로, 높이, 포장재 무게
	$(document).on("change",".refresh", function(){ changeWeight( $(this) ); });
	// 배송정보 팝업 > 관세정보 > 저장 버튼
	$(document).on("click", "#saveExpressBtn", popExpressSave);
	// 배송정보 팝업 > 배송사 선택 > 선택
	$(document).on("click",".courier-area button",function(){ selectCourier($(this), "click"); });
	// 배송정보 팝업 > 배송사 선택 > 저장
	$(document).on("click", "#saveCustomsBtn", function(){ saveCustomsCourier("save"); });
	// 배송정보 팝업 > 배송사 선택 > 배송생성
	$(document).on("click", "#addDeliveryBtn", function(){ saveCustomsCourier("create"); });
	// 배송정보 팝업 > 국내 전용 > 저장 버튼
	$(document).on("click","#saveDomesticBtn", function(){ saveDomestic(); });
	
	$(document).on("change", "select[name='selectBoxType']", onBoxType);
	
	$(document).on("click", "input[name='_buyerAddr']", openChangeAddr);

}) ;

function initPopup () {
	
	popupSelectBind();
	
	var defCourierId = $("input[name=defCourierId]").val() ;
	var defBoxType = $("input[name=defBoxType]").val() ;
	
	$("tr.courier").each( function() {
		var $row = $(this);
		
		$row.find("select[name='selectBoxType'] option").each( function() {
			if ( $(this).val() == defBoxType && $(this).data("courier-id") == defCourierId ) {
				$(this).prop('selected', true);
				$row.find("select[name='selectBoxType']").change();
				$row.find(".btn_select").addClass("on");
			}
		});
		
	});
}


function openChangeAddr() {
	
	//window size       
//	var $widthv = $(window).width();
//	var $heightv = $(window).height();

	//popup size
	var popupWinWidth = 650;
	var popupWinHeight = 300;

	var leftPs = (screen.width - popupWinWidth) / 2; 
	var topPs = (screen.height - popupWinHeight) / 4 + 100; 
	
	window.open( "/order/popup/popupAddressChange","_blank", "width=650,height=300,left=" + leftPs + ",top=" + topPs + ",location=no,toolbar=1,status=1");
}

function composeBuyerAddress() {
	var addrs = [ $("input[name='buyerAddr1']").val() ];
	
	if ( $("input[name='buyerAddr2']").val() ) {
		addrs.push( $("input[name='buyerAddr2']").val() );
	}
	
	addrs.push( $("input[name='buyerCity']").val() );
	addrs.push( $("input[name='buyerProvince']").val() );
	addrs.push( $("input[name='buyerCountryCode']").val() );
	
	$("input[name='_buyerAddr']").val(  addrs.join(", ") );
}

function onBoxType() {
	var _option = $(this).find("option:selected") ;
	var _data = $(_option).data() ;
	
	$("input[name='boxType']").val(_option.val());
	
	$(this).closest("tr").find("input[name='boxPrice']").val(_data.price);
}

function onSelectSender() {
	
	var option = $("select[name='selectSender'] option:selected") ;
	var fullAddr = "(" + option.data("zipcode") + ") " + option.data("addr1") + " " + option.data("addr2");
	
	$("input[name='_senderAddr']").val(fullAddr);
	
	$("input[name='sellerPhone']").val(option.data("phonenumber"));
	$("input[name='sellerZipCode']").val(option.data("zipcode"));
	$("input[name='sellerAddr1']").val(option.data("addr1"));
	$("input[name='sellerAddr2']").val(option.data("addr2"));
	$("input[name='sellerAddr1Ename']").val(option.data("addr1ename"));
	$("input[name='sellerAddr2Ename']").val(option.data("addr2ename"));
	$("input[name='sellerProvince']").val(option.data("province"));
	$("input[name='sellerCity']").val(option.data("city"));
	
}

function onSelectSenderKr() {
	var _option = $(this).find("option:selected") ;
	var _data = $(_option).data() ;
	console.log("price_selected");
	$("input[name='sellerPhone01']").val(_data.phonenumber01);
	$("input[name='sellerPhone02']").val(_data.phonenumber02);
	$("input[name='sellerPhone03']").val(_data.phonenumber03);
	$("input[name='sellerPhone04']").val(_data.phonenumber04);
	$("input[name='sellerZipCode']").val(_data.zipcode);
	$("input[name='sellerAddr1']").val(_data.addr1);
	$("input[name='sellerAddr2']").val(_data.addr2);
	$("input[name='sellerAddr1Ename']").val(_data.addr1ename);
	$("input[name='sellerAddr2Ename']").val(_data.addr2ename);
	$("input[name='sellerProvince']").val(_data.province);
	$("input[name='sellerCity']").val(_data.city);
	// SET DISPLAY LABEL // 국내전용
	if ($("div#sellerPhone").length > 0) {
		$("div#sellerPhone").text(_data.phonenumber);
	}
	if ($("div#sellerAddr").length > 0) {
		$("div#sellerAddr").text("("+_data.zipcode+") "+_data.addr1);
	}
	if ($("div#sellerAddrDetail").length > 0) {
		$("div#sellerAddrDetail").text(_data.addr2);
	}
}

// 배송정보 팝업 > 주소 등록 > 저장 버튼 > onSuccess
function sendFrom(data, target) {
	if (data.errCode == true) {
		if (target) {
			$(target)[0].click();
		} else {
			alert(getMessage("alert.proc.end", ["button.save"])); // 수정이 완료되었습니다.
		}
	} else {
		alert(getMessage("alert.proc.err", [ "button.save" ])); // 수정 중 오류가 발생했습니다.
	}
}

//배송정보 팝업 > 주소 등록 > 저장 버튼
function saveAddr() {
	if (fnValidationCheckForSelectbox($("#ajax-popForm"))) {
		if (fnValidationCheckForInput($("#ajax-popForm"))) {
			var url = "/order/popup/popOrderCreateAddressProc";
			var param = $("#ajax-popForm").serializeObject(); 
            ajaxCall("post", url, param, sendFrom, false, "#popupExpress");
		}
	}
}

var changeWeight = function(element) {
	
	var unit = $("input[name='weightUnit']").val();
	
	//unit = "lb";
	
	if ( validateNumber(unit, element) == false ) {
		element.focus();
		return;
	}
	
//	var value = element.val();
//	// 무게단위가 "g"이고 포장재무게에 "." 가 있으면, integer 로 변환한다.
//	if (  value.indexOf(".") > -1 && element.attr("name") == "weight" && unit == "g")  {
//		element.val( double2int(value) );
//	}
//	
	setTotalWeight("");
	refreshCarrier();
}

// 배송정보 팝업 > 관세정보 > 포장재 정보 > 포장재 명 
var changeBox = function(flag) {
	
	var obj = $("select[name='selectBox'] option:selected");
	
	$("input[name='boxType']").val(obj.data("boxtype")); 
	$("input[name='boxUnit']").val(obj.data("boxunit"));
	
	var weightUnit = $("input[name='weightUnit']").val();
	
	if ( flag == true && $("input[name='addressBoxWeight']").val() == "init" ) {
		flag = false;
	}

	var addressBoxLength = flag ? $("input[name='addressBoxLength']").val() : obj.data("boxlength");
	var addressBoxWidth = flag ? $("input[name='addressBoxWidth']").val() : obj.data("boxwidth");
	var addressBoxHeight = flag ? $("input[name='addressBoxHeight']").val() : obj.data("boxheight");
	var addressBoxWeight = flag ? $("input[name='addressBoxWeight']").val() : obj.data("boxweight");
	
	if ( weightUnit == "g" ) {
		addressBoxWeight = double2int(addressBoxWeight);
	}
	
	$("input[name='weightUnit']").val(weightUnit);
	$("input[name='boxLength']").val(addressBoxLength);
	$("input[name='boxWidth']").val(addressBoxWidth);
	$("input[name='boxHeight']").val(addressBoxHeight);
	$("input[name='weight']").val( addComma(addressBoxWeight) );
	
	setTotalWeight("");
	
	refreshCarrier();
	
	$("#saveCustomsBtn").removeClass("btn_type3").addClass("btn_type2");
}

//배송정보 팝업 > 관세정보 > HS Code 가져오기
var refreshCarrier = function(){
	var url = "/order/popup/selectVolumeticCourierList";
	
	// value check 를 위하여 === 를 사용함
	if ( $("#boxLength").val() === "" || $("#boxWidth").val() === "" || $("#boxHeight").val() === "" || $("#boxUnit").val() === "" ) {
		return;
	}
	
	var param = {
		"totalWeight" : $("#totalWeight").val(),
		"boxLength" : $("#boxLength").val(),	
		"boxWidth" : $("#boxWidth").val(),	
		"boxHeight" : $("#boxHeight").val(),	
		"boxUnit" : $("#boxUnit").val(),	
		"buyerCountryCode" : $("input[name='buyerCountryCode']").val()
	};
//	console.log(param);
	
	ajaxSyncCall("post", url, param, redrawCarrier, null, null, popupSelectBind);
}

var redrawCarrier = function (data) {

//	console.log(document.body);
	$("tbody.courier-area").empty();

	var shippingLineCode = $("input[name='shippingLineCode']").val();
	var courierCompany = $("input[name='courierCompany']").val();
	var courier =  $("input[name='courier']").val();
	
	var checkCustom = false;
	var checkSeller = false;
	var content = "";
	
	if ( data.list.length ) {
		
		$(data.list).each( function(index, obj) {
			if ( shippingLineCode == obj.courierId) {
				obj["buttonClass"] = "on";
				checkCustom = obj;
			} else if (courierCompany == obj.comCode && courier == obj.courierId) {
				obj["buttonClass"] = "on";
				checkSeller = obj;
			} else {
				obj["buttonClass"] = "";
			}
		});
		
		// customer 와 seller 가 둘다 선택했으면, customer 가 선택한 것은 무시
		if ( checkSeller && checkCustom ) {
			checkCustom["buttonClass"] = "";
		}
		
		$(data.list).each( function(index, obj) {
			
			var tr = "<tr data-courier='" + obj["code"] + "'";
			tr += " data-id='" + obj["comCode"] + "'";
			tr += " data-courierid='" + obj["courierId"] + "'";
			tr += " data-price='" + obj["price"] + "'";
			tr += " data-rankprice='" + obj["rankPrice"] + "'>";
			
			var netprice = obj.price - obj.rankPrice;
			var td = "<td>" + obj["codeName"] + "</td><td class='t_right'>" + addComma(netprice) + "</td><td class='t_center'>KRW</td><td class='t_center'><button type='button' class='btn_select " + obj["buttonClass"] + "'>선택</button></td>";
			
			content += tr + td + "</tr>\n";
		});
	} else {
		content = '<tr><td colspan="5" class="t_center"> <spring:message code="order.popup.customer.nolist" text="해당하는 국가의 국제 특송업체가 존재 하지 않습니다." /> </td></tr>';
	}
	
//	console.log(content);
	
	$("tbody.courier-area").append(content);
}

//배송정보 팝업 > 관세정보 > HS Code 가져오기
var setHscode = function(){
	var url = "/order/popup/getHscode";
	var param = getHsParam();
	ajaxSyncCall("post", url, param, customHsCode, null, null, popupSelectBind);
}

//배송정보 팝업 > 관세정보 > 저장 버튼
function popExpressSave() {
	if (fnValidationCheckForSelectbox($("#ajax-popForm"))) {
		if (fnValidationCheckForInput($("#ajax-popForm"))) {
			var url = "/order/popup/popOrderCreateExpressProc";
			var param = $("#ajax-popForm").serializeObject(); 
			if(param.buyerCountryCode == "RU" && param.arrHscode.length < 10){
				alert("HS Code는 10자리로 기입 부탁드립니다.");
				return;
			}
			ajaxCall("post", url, param, sendExpress, false, "#popupCustoms");
		}
	}
}


//배송정보 팝업 > 관세 등록 > 저장 버튼 > onSuccess
function sendExpress(data, target) {
	if (data.errCode == "SUCCESS") {
		if (target) {
			$(target)[0].click();
		} else {
			alert(getMessage("alert.proc.end", ["button.save"])); // 수정이 완료되었습니다.
		}
	} else {
		//alert(getMessage("alert.proc.err", [ "button.save" ])); // 수정 중 오류가 발생했습니다.
		alert(getMessage(data.errCode));
//		console.log(data.errCode);
	}
}


// 배송정보 팝업 > 배송사 선택 > 선택
var selectCourier = function($obj, mode){
	
	var nation = $("input[name='buyerCountryCode']").val();
	var price;
	var flag = true;
	
	if ( nation == "KR" ) {
		price = $obj.closest("tr").find("select[name='selectBoxType'] option:selected").val();
		if (!price) {
			alert(getMessage("alert.checkboxChk.boxsize"));
			return;
		}
	} else {
		
		$(".courier-area").find(".orderCourier").each(function(){
			alert("고객이 선택한 배송서비스가 있어 배송서비스 수정이 불가능 합니다.");
			flag = false;
			return false;	
		});
		
		if ( flag ) {
			price = $obj.closest("tr").data("price");
		}
	}
	
	if ( flag ) {
		$(".courier-area button").each(function() {
			$(this).removeClass("on");
		});
		$obj.addClass("on");
	}
	
}

//배송정보 팝업 > 배송사 선택 > 저장
//배송정보 팝업 > 배송사 선택 > 배송생성
//- 특송업체 저장 및 배송리스트 이동
var saveCustomsCourier = function(proc){
	if ( proc == "save" && $("#saveCustomsBtn").hasClass("btn_type2") == false ) {
		alert(getMessage("alert.checkboxChk.boxinfo"));
		return;
	}
	
	if ( ! fnValidationCheckForInput($("#ajax-popForm"))) {
		return;
	}
	
	if ( ! fnValidationCheckForSelectbox($("#ajax-popForm"))) {
		return;
	}
	
	var courier;
	var company;
	var price;
	var courierId;
	var rankPrice;
	
	$("tbody.courier-area tr").each( function(index, tr) {
		if ( $(tr).find("button").hasClass("on") ) {
			courier = $(tr).data("courier");
			company = $(tr).data("id");
			price = $(tr).data("price");
			courierId = $(tr).data("courierid");
			rankPrice = $(tr).data("rankprice");
		}
	});
	
	if (!courier) {
		alert(getMessage("alert.checkboxChk.courier"));
		return;
	}
	
	$("input[name='courier']").val(courier);
	$("input[name='courierCompany']").val(company);
	$("input[name='price']").val(price);
	$("input[name='rankPrice']").val(rankPrice);
	$("input[name='courierId']").val(courierId);
	$("input[name='proc']").val(proc);
	$("input[name='payment']").val(price);  // 조한두 (20.05.19)
	
	var url = "/order/popup/popOrderCreateCustomsProc";
	var parameter = $("#ajax-popForm").serializeForm(); 
	
//	if( parameter.buyerCountryCode == "RU" ){
//		var flag = true;
//		$.each( parameter.arrHscode.split(","), function(index, hscode) {
//			if ( hscode.length < 10 ) {
//				flag = false;
//				return false;   // break
//			}
//		});
//		if ( flag == false ) {
//			alert("HS Code는 10자리로 기입 부탁드립니다.");
//			return;
//		}
//	}
	
	ajaxCall("post", url, parameter, saveCourier);  
}

//배송사 저장 완료
function saveCourier(data) {
	if (data.errCode ==  "SUCCESS") {
		if (data.proc == "save") {
			var search = window.location.search; 
			var re = /(currPosition)=\w+/;
			if ( search ) {
				if ( re.test(search) ) {
					search = search.replace(re, "$1=" + data.currPosition);
				} else {
					search += "&currPosition=" + data.currPosition;
				}
			} else {
				search = "?currPosition=" + data.currPosition;
			}
			var url = window.location.pathname + search;
			window.location.replace(url);
		} else {
			window.location.reload();
		}
	} else {
//		alert(getMessage("alert.proc.err", ["button.save"]));  // 수정 중 오류가 발생했습니다.
		alert(getMessage(data.errCode));
	}
}


// 배송정보 팝업 > 국내 전용 > 저장 버튼
function saveDomestic() {
	
	// param.buyerZipCode = "" ;
//	console.log('saveDomestic() > Step 3/3') ;
	if (fnValidationCheckForSelectbox($("#ajax-popForm"))) {
		if (fnValidationCheckForInput($("#ajax-popForm"))) {
			
			var $tr = $(".courier-area .on").closest("tr");
			if ( $tr.length != 1 ) {
				alert(getMessage("alert.checkboxChk.boxsize"));
				return;
			}
			
			var price;
			
			var nation = $("input[name='buyerCountryCode']").val();
			if ( nation == "KR" ) {
				price = $tr.find("select[name='selectBoxType'] option:selected").data("price");
			} else {
				price = $tr.data("price");
			}
			
			var data = $tr.data();
			var courier = data.courier;
			var company = data.id;
			var courierId = data.courierid;
			var rankPrice = data.rankprice;
			
			if (! price ) {
				alert(getMessage("alert.checkboxChk"));
				return;
			}
			$("input[name='courier']").val(data.courier);
			$("input[name='courierCompany']").val(data.id);
			$("input[name='price']").val(price);
			$("input[name='rankPrice']").val(data.rankprice);
			$("input[name='courierId']").val(data.courierid);
			$("input[name='proc']").val("create");
			$("input[name='payment']").val(price);  // 조한두 (20.05.19)
			
			var url = "/order/popup/popOrderCreateDomesticProc";
//			var param = $("#ajax-popForm").serializeObject();
			var param = $("#ajax-popForm").serializeForm();
//			console.log(param) ;
			var onSuccess = function(data) {
//				console.log(data) ;
				if (data.errCode == true) {
					alert(getMessage("alert.proc.end", ["button.save"])); // 수정이 완료되었습니다.
					location.reload();
//					var pathname = window.location.pathname;
//					var origin = window.location.origin; 
//					var url ;
//					if (data.proc == "save") {
//						url = origin + pathname + "?currPosition=" + data.currPosition;
//					} else {
//						url = origin + pathname ;
//					}
//					console.log("url:" + url);
//					window.open(url, '_self');
				} else {
					alert(getMessage("alert.proc.err", ["button.save"]));  // 수정 중 오류가 발생했습니다.
				}
			}
			doAjax(url, param, onSuccess);  
		}
	}
}

function doAjax(url, param, onSuccess) {
    var _ajax = {} ;
    // DEFAULT SETTING
    _ajax.type = 'POST' ;
    _ajax.async = true ;
    _ajax.cache = false ;
    _ajax.contentType = "application/json" ;
    _ajax.dataType = "json" ;
    _ajax.beforeSend = onBeforeSend ;
    _ajax.complete = onComplete ;
    _ajax.error = onError ;
    // CUSTOM
    _ajax.url = url ;
    _ajax.data = JSON.stringify(param) ;
    _ajax.success = onSuccess ;
//    console.log(_ajax) ;
    $.ajax(_ajax) ;
}

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

function onBeforeSend(xhr) {
	var _header = $("meta[name='_csrf_header']").attr("content") ;
	var _token = $("meta[name='_csrf']").attr("content") ;
	xhr.setRequestHeader(_header, _token) ;
}

function onComplete(jqXHR, textStatus, errorThrown) {
//	console.log('onComplete()') ;
	offLoading() ;
}

function onError(error) {
//	console.log('onError()', error) ;
	offLoading() ;
	alert(getMessage("alert.proc.err", [ "button.save" ])); // 수정 중 오류가 발생했습니다.
}

function onLoading() {
	if ($("body").hasClass("spinner-show")) {
		return false ;
	} else {
	    $("body").addClass("spinner-show");
	    return true ;
	}
}

function offLoading() {
	if ($("body").hasClass("spinner-show")) {
		$("body").removeClass("spinner-show");	
	}
}
