// READY
$(function() {
	initialize();
});

var initialize = function() {
	initControls();
	bindEvent();
	setDefaultCourier();
};

var initControls = function() {
	var $submitform = $('#searchform');
	bindSelectAjax($submitform); // 검색 select box 세팅
};

// 등록/수정 팝업 select box 세팅
var popupSelectBind = function() {
	var $submitform = $('#submitform');
	bindSelectAjax($submitform);
}

var bindEvent = function() {
	// 팝업 특송저장 > 특송업체 선택
	$(document).on("click", ".courier-area button", function() {
		selectCourier($(this), "click");
	});

	// 팝업 특송저장 > 특송업체 저장
	$(document).on("click", "#saveCourierBtn", function() {
		saveCustomsCourier("save");
	});

};

// 배송사 저장
var saveCustomsCourier = function() {
	
	var count = $(".btn_select.on").length;
	if ( count < 1) {
		alert(getMessage("alert.checkboxChk"));
		return;
	}
	
	var array = [];
	
	$("tr.courierList").each( function() {
		var on = $(this).find("button.btn_select.on");
		if ( on.length == 1 ) {
			var data = $(this).data();
			var boxType = $(this).find("select[name=boxType] option:selected").val();
			var a = $(this).data("shop-shop-idx");
			var b = $(this).data("shopShopIdx");
			var c = $(this).data.shopShopIdx;
			var param = {
					"shopIdx" :	$(this).data("shop-shop-idx"),
					"courierId" :	$(this).data("company-courier-code"),
					"boxType" : $(this).find("select[name=boxType] option:selected").val()
			}
			array.push(param);
		}
	});
	
	var url = "/setting/updateCourier";
	var parameterx = JSON.stringify(array);
	var parameter = JSON.parse(JSON.stringify(array));     
	console.log("rarameter=" + parameter);

	ajaxCall("post", url, parameter, sendFrom);
}

// 특송 선택
var selectCourier = function($obj, mode) {

	var select = $obj.parent().prev().find("select[name=boxType] option:selected").val();
	if (!select) {
		alert(getMessage("alert.checkboxChk.boxsize"));
		return;
	}
	
	// $(".courier-area button").removeClass("on");
	var mySsi = $obj.closest("tr").data("shop-shop-idx");

	$(".courier-area button").each(function() {
		var ssi = $(this).closest("tr").data("shop-shop-idx");
		if (mySsi == ssi) {
			$(this).removeClass("on");
		}
	});

	$obj.addClass("on");
}

// validation 체크
var validationCheck = function() {
	var $submitform = $('#submitform');

	// input type Validation
	if (!fnValidationCheckForInput($submitform)) {
		return false;
	}

	// select type Validation
	if (!fnValidationCheckForSelectbox($submitform)) {
		return false;
	}

	return true;
}

// 프로세스 완료 콜백
var sendFrom = function(data) {
	if (data.errCode == true) {
		alert(getMessage("alert.proc.end", [ data.procCode ])); // 등록(수정)이 완료
																// 되었습니다.
		location.reload();
	} else {
		alert(getMessage("alert.proc.err", [ data.procCode ])); // 등록(수정) 중 오류가
																// 발생했습니다.
	}
}

var setDefaultCourier = function() {
	$("tr.courierList").each(function() {
		var selected = $(this).find("select[name='boxType'] option:selected").val();
		if (selected) {
			$(this).find(".btn_select").addClass("on");
		} else {
			$(this).find(".btn_select").removeClass("on");
		}
		// if ($(this).data().courierid == defaultCourier) {
		// $(this).click() ;
		// }
		
		
	});
}