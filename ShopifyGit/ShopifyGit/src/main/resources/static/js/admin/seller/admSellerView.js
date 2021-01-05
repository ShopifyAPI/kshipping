// READY
$(function() {
	initialize();
});

var initialize = function() {
	initControls();
	bindEvent();
};

var initControls = function() {
	var $submitform = $('#submitform');
	bindSelectAjax($submitform); // 검색 select box 세팅
	
	initSelect();
};

var initSelect = function() {
	$("#paymentMethod").val( $("input[name='paymentMethodInitData']").val() );
	$("#shopStatus").val( $("input[name='shopStatusInitData']").val() );
}



var bindEvent = function () {
	
	$("#newRow td input").change(function() {
	     $('#btnSave').removeClass('btn_type3').addClass('btn_type2');
	});
	
	$("select.changemon").change(function() {
		$('#btnSave').removeClass('btn_type3').addClass('btn_type2');
	});
	
	
    //목록
    $(document).on("click","#btn_list",function(){
    	gotoPage();
    });
    
    //랭크 수정
//    $(document).on("click","#btnEdit",function(){
//    	var url = "/admin/popup/seller/popSellerRank?email=${seller.email}&rankId=${seller.rankId}";
//        openPop(url, "", 350, "", popupSelectBind, "");
//    });
    
//    //랭크 수정 로그
//    $(document).on("click","#btnLog",function(){
//        var url = "/admin/popup/seller/popSellerRankLog?email=${seller.email}";
//        openPop(url, "", 900, "", "", "");
//    });
    
    //랭크 수정 (popup)
    $(document).on("click","#btnSave",function(){
    	saveDiscount();
    });
    
    $(document).on("click","#btnAdd",function(){
    	newRow();
    });
    
    $(document).on("click",".cancel",function(){
        $(".popup .btn_close").trigger("click");
    });

    // 결제 모드 변경
    $(document).on("click",".use-check",function(){
    	var type = "post";
        var url = "/admin/seller/updateShopBilling";
        
        var billingYn = $(this).closest("tr").data("billingyn") == "Y" ? "N" : "Y";
        var shopIdx = $(this).closest("tr").data("shopidx");
        
        var param = {"billingYn":billingYn, "shopIdx":shopIdx}
        
        $(this).addClass("use-edit"); // 수정 데이터 체크
            ajaxCall(type, url, param, useFrom);
    }); 
   
        // 계약 상태 변경
    $(document).on("click",".contact-check",function(){
    	var type = "post";
        var url = "/admin/seller/updateActive";
        
        var activeYn = $(this).closest("tr").data("activeyn") == "Y" ? "N" : "Y";
        var shopIdx = $(this).closest("tr").data("shopidx");
        
        var param = {"activeYn":activeYn, "shopIdx":shopIdx}
        
        $(this).addClass("use-contact"); // 수정 데이터 체크
        ajaxCall(type, url, param, useFrom);
    }); 
     
};

var newRow = function() {
	$('#newRow').show();
	$('#btnAdd').removeClass('btn_type2').addClass('btn_type3');
};

var saveDiscount = function() {
	var $submitform = $('#submitform');
	
	// select type Validation
	if(!fnValidationCheckForSelectbox($submitform)){
		return false;
	}
	
	// 수정된 내용이 있는지 모든 field 를 확인함
	var changed = false;
	
	// post 할 parameter 정리
	var param = $submitform.serializeObject();
	
	if ( param.paymentMethod == $('input[name="paymentMethodInitData"]').val() ) {
		param.paymentMethod = "";
	} else {
		changed = true;
	}
	
	if ( param.shopStatus == $('input[name="shopStatusInitData"]').val() ) {
		param.shopStatus = "";
	} else {
		changed = true;
	}
	
	// seller 별 할인율 항목에 수정된 내용을 수집한다.		
	var discountList = [];
	
	$("tr#newRow td.discount input").each( function() {
		var value =  $(this).val();
		var old = $(this).data("old");
		var useYn;
		
		if (  value == old ) {
			useYn = "N";
		} else {
			useYn = "Y";
			changed = true;
		}
		
		var courier = { 
				"discount" : value,
				"useYn" : useYn
		};
		discountList.push(courier);   

	});
	
	if ( changed ) {
		var url = "/admin/seller/updateSellerDiscount";
		
		
		$("tr#courierRow th").each( function(index, item) {
			var courier = discountList[index];
			courier["id"] = $(item).data("id");	   
			courier["code"] = $(item).data("code");	   
		});
		

		param["discountList"] = discountList;
		
		
		ajaxCall("post", url, param, sendFrom);   
	} else {
		confirm(getMessage("alert.nothing.changed"));
		$('#btnSave').removeClass('btn_type2').addClass('btn_type3');
	}
};

// 연동상태 변경
var useFrom = function(data) {
	if(data.errCode == true) {
		data.billingYn == "Y" ? $(".use-edit").addClass("on") : $(".use-edit").removeClass("on");
		$(".use-edit").closest("tr").data("billingyn", data.billingYn);
		data.activeYn == "Y" ? $(".use-contact").addClass("on") : $(".use-contact").removeClass("on");
		$(".use-contact").closest("tr").data("activeyn", data.activeYn);    		
    } else {
        alert(getMessage("alert.proc.err", ["button.delete"]));  // 수정 중 오류가 발생했습니다.
    }

	$(".use-edit").removeClass("use-edit"); // 수정 데이터 원복
	$(".use-contact").removeClass("use-contact");
}


var popupSelectBind = function () {
    var $submitform = $('#submitform');
    bindSelectAjax($submitform);
}

// 페이징 페이지 이동
var gotoPage = function() {
    defaultSearchForm.submit();
}

// 프로세스 완료 콜백
var sendFrom = function(data) {
    if(data.errCode == true) {
        alert(getMessage("alert.proc.end", [data.procCode]));  // 등록(수정)이 완료 되었습니다.
        location.reload();
    } else {
//        alert(getMessage("alert.proc.err", [data.procCode]));  // 등록(수정) 중 오류가 발생했습니다.
        alert(data.errMsg);  // 등록(수정) 중 오류가 발생했습니다.
    }
}
    