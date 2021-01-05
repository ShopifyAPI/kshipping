/**
 * 초기 페이지 세팅
 */
$(document).ready(function(){
	bindDatePicker();
	bindSearchTextBox();
	bindClosePop();
	setTopMenu();

	lnbMenuChk();
	lnbEvent();
	//history.replaceState({}, null, location.pathname); //주소창 파라메터 삭제
});

//검색 텍스트 박스 class 컨트롤
var bindSearchTextBox = function(){
	$(document).on({
		focus : function(e){
//			console.log("focus");
			$(this).closest(".searchbox").addClass("active");
		}
		,blur : function(e){
//			console.log("blur");
			$(this).closest(".searchbox").removeClass("active");
		}
	},".searchbox input[type='text']");
};

//데이터 픽커 세팅
var bindDatePicker = function(){
	setDatepicker();
};

//팝업 닫기
var bindClosePop = function () {
	$(document).on({
		click : function(e){
			$(this).closest(".popup").bPopup().close();
	    	$(this).closest(".popup").remove();
		}
	},'.btn_close');
}

var defaultOption = {
		changeMonth: true
		, changeYear: true
		, showButtonPanel: true
		, showOtherMonths: false
		, dateFormat: "yy-mm-dd"
		, dayNames: ["월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"]
		, dayNamesMin: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
		, monthNames: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"]
		//, minDate: "-1m"
		//, maxDate: "d"
	};


var optionMonth = { 
		dateFormat: "yy-mm" 
		/* //년/월만 나오게 처리 사용성 문제로 주석 처리 
		 * , onClose: function(dateText, inst) { 
			var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val(); 
			var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val(); 
			$(this).datepicker('setDate', new Date(year, month, 1)); 
			$(".ui-datepicker-calendar").css("display","none"); 
		} */
	};


var setDatepicker = function ($obj, option) {
	
	var optionData = $.extend(defaultOption, option);
	var option = optionData;
	
	$obj = !($obj) ? $(".date") : $obj;
	$obj.attr("readonly", "readonly");

	$obj.each(function () {
		var $this = $(this);
		
		if($this.hasClass("yymm")) {
			option = $.extend(optionData, optionMonth);
			$this.datepicker(option);
			
			/* //년/월만 나오게 처리 사용성 문제로 주석 처리 
			 * $this.focus(function () { 
				$(".ui-datepicker-calendar").css("display","none"); 
				$("#ui-datepicker-div").position({ my: "center top", at: "center bottom", of: $(this) });
			}); */
		} else {
			$this.datepicker(optionData);
		}

		var $btn = $this.next("a.btnDate");
		if ($btn && $btn.length > 0) {
			$btn.off("click").on("click", function () {
				$this.datepicker("show");
				return false;
			});
		}
	})
};



 var setTopMenu = function () {
 	$(".tit_wrap").addClass("mt0");
	
 	var page = location.pathname;
 	var index = 0;
	
 	if(page.indexOf("/admin/") != -1) { // 관리자 세팅
 		setAdminTopMenu(page);
 		return;
 	}
	
 	if(parent && parent != this){
 		$(".gnb_menu ul").hide();
 	} else {
 		$(".gnb_menu ul").show();
		
 		if(page == "/") {
 			index = 0;
 		} else if(page.indexOf("/order") != -1) {
 			index = 1;
		} else if(page.indexOf("/shipment") != -1) {
 			index = 2;
 		} else if(page.indexOf("/cs/") != -1) {
 			index = 3;
 		} else if(page.indexOf("/setting") != -1) {
 			index = 4;
 		} else if(page.indexOf("/board") != -1) {
 			index = 5;
 		}
		
 		$(".gnb_menu li").removeClass("active");
 		$(".gnb_menu li").eq(index).addClass("active");
 	}
 }

 var setAdminTopMenu = function (page) {	
 	var check = false;
 	$(".menu .on").removeClass("on");
 	$(".menu li div").find("a").each(function() {		
 		check = false;
		
 		if(page.indexOf($(this).attr("href")) != -1) {
 			check = true;
 		} else if(page.indexOf($(this).data("subpage01")) != -1) {
 			check = true;
 		} else if(page.indexOf($(this).data("subpage02")) != -1) {
 			check = true;
 		}
		
 		if(check) {
 			$(this).addClass("on");
 			$(this).closest("li").addClass("on");
 		}
 	});
 }




function lnbMenuChk () {


    if  (window.location.pathname.split('/')[2] == "seller") {
      //console.log("셀러");
      setTimeout(function () {
      	$(".menu li:eq(0)").click()
      }, 50);

    } else if  (window.location.pathname.split('/')[2] == "cs") {
      //console.log("배송cs");
      setTimeout(function () {
      	$(".menu li:eq(1)").click()
      }, 50);

    } else if  (window.location.pathname.split('/')[2] == "board") {
      //console.log("고객센터");
      setTimeout(function () {
      	$(".menu li:eq(2)").click()
      }, 50);

    } else if  (window.location.pathname.split('/')[2] == "delivery") {
      //console.log("특송요금");
      setTimeout(function () {
      	$(".menu li:eq(3)").click()
      }, 50);

    } else if  (window.location.pathname.split('/')[2] == "price") {
      //console.log("특송요금");
      setTimeout(function () {
          $(".menu li:eq(3)").click()
      }, 50);

    } else if  (window.location.pathname.split('/')[2] == "statis") {
      //console.log("정산관리");
      setTimeout(function () {
      	$(".menu li:eq(4)").click()
      }, 50);

    } else if  (window.location.pathname.split('/')[2] == "code") {
      //console.log("운영관리");
      setTimeout(function () {
      	$(".menu li:eq(5)").click()
      }, 50);

    } else if  (window.location.pathname.split('/')[2] == "admin") {
      //console.log("어드민");
      setTimeout(function () {
      	$(".menu li:eq(6)").click()
      }, 50);

    } 

}


function lnbEvent() {

	//toggle menu UI Event
	var $lnbWrap = $(".gnb.lnb");
	var $lnbTg = $(".gnb ul.menu li");
	var $lnbTgSub = $(".gnb ul.menu li > .debth_2");
	var $fbxViewBtn = $(".fbx_view_btn");

	$lnbTg.click(function () {

		var _thisMenu = $(this);
		var _thisDepth2 = $(this).children(".debth_2");

		//LNB 기본이벤트
		if ( _thisMenu.hasClass("active") ) {

			//이미 열려있으므로 닫아준다.
			_thisMenu.removeClass("active");
			_thisDepth2.slideUp(0);

		}  else {

			//열려있을때라면 다 닫아주고
			$lnbTg.removeClass("active");
 			$lnbTgSub.slideUp(0);

 			//클릭된 요소만 열어준다.
			_thisMenu.addClass("active");
			_thisDepth2.slideDown();
		}

		//LNB 미니사이즈일때 2뎁스를 열때
		//LNB가 'mini'클래스를 가지고 있거나, 2뎁스 메뉴가 액티브를 가지고 있지 않다면,
		if ( ( $lnbWrap.hasClass("mini") ) && (! _thisMenu.hasClass("active") ) ) {
			return false

		} else if  ( ( $lnbWrap.hasClass("mini") ) && ( _thisMenu.hasClass("active") ) ) {
			$lnbWrap.removeClass("mini");
			$fbxViewBtn.removeClass("mini");

		} 

	});


	//WideView Support UI Event

	$fbxViewBtn.click(function () {
		var _thisUiBtn = $(this);
		console.log("사이즈 줄임")

		if ( _thisUiBtn.parent(".gnb").hasClass("mini") ) { 
			_thisUiBtn.parent(".gnb").removeClass("mini")
			_thisUiBtn.removeClass("mini")
		
		} else {
			$(".gnb ul.menu li .debth_2").slideUp();
			$(".gnb ul.menu li").removeClass("active");
			_thisUiBtn.parent(".gnb").addClass("mini")
			_thisUiBtn.addClass("mini")

		}

	});

}


