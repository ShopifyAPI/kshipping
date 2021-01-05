var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
/*
 *  공통 유틸 파일
 * */

/**
 * ajax 통신
 * @param type (post, get)
 * @param url (주소)
 * @param param (파라메터)
 * @param callbackFunction (완료 후 호츨 함수)
 * @param isSpinner (모래시계 출려여부)
 * @param callbackClickTarget (callback function 을 실행한 후, click event 를 발생시킬 target)
 * @returns {string} (success, error)
 */
var g_spinnerCount = 0;
var ajaxCall = function(type, url, param, callbackFunction, isSpinner, callbackClickTarget) {
	var returnData;
	
	isSpinner = isSpinner || true;
	
	$.ajax({
        type       : type
        ,async     : true  // true: 비동기, false: 동기
        ,url       : url
        ,cache     : false
        ,data      : JSON.stringify(param) 
        ,contentType: "application/json"
        ,dataType  : "json"
        ,beforeSend : function(xhr) {
        	//console.log("header");
        	//console.log(header);
        	//console.log("token");
        	//console.log(token)
        	
        	if (isSpinner) {
				g_spinnerCount++;
				$("body").addClass("spinner-show");
			}
        	
        	xhr.setRequestHeader(header, token);
        }
        ,success   : function(data) {
        	returnData = "success";
//            console.log("success");
//            console.log(data);
            
            if(callbackFunction != "") {
            	if ( callbackClickTarget ) {
            		callbackFunction(data, callbackClickTarget);
            	} else {
            		callbackFunction(data);
            	}
            };
        }
        ,error     : function(error) {
        	returnData = "error";
//        	console.log(error);
        	
        	if (isSpinner) {
				g_spinnerCount--;

				if (g_spinnerCount < 1) {
					$("body").removeClass("spinner-show");
				}
			}
        }
        ,complete: function (jqXHR, textStatus, errorThrown) {
			if (isSpinner) {
				g_spinnerCount--;

				if (g_spinnerCount < 1) {
					$("body").removeClass("spinner-show");
				}
			}
		}
	});

	return returnData; 
};

var showSpinner = function() {
	$("body").addClass("spinner-show");
};

var hideSpinner = function() {
	$("body").removeClass("spinner-show");
};

/**
 * serialize 함수 (form 파라미터값 생성 json)
 * @returns json Object
 */
jQuery.fn.serializeObject = function() { 
    var obj = null; 
    try { 
        if(this[0].tagName && this[0].tagName.toUpperCase() == "FORM" ) { 
            var arr = this.serializeArray(); 
            if(arr) { 
            	obj = {}; 
            	jQuery.each(arr, function() { 
            		//console.log(obj[this.name] + "//" + this.name + "//" + this.value);
            		
            		if(obj[this.name] === undefined || obj[this.name] === ""){ // 중복값 "," 구분
            			obj[this.name] = this.value;
            		} else {
            			obj[this.name] = obj[this.name] + "," + this.value;
            		}
                }); 
            } 
        } 
    }catch(e) { 
        alert(e.message); 
    }finally {} 
    
    return obj; 
}

// array 인 경우, value 가 "" 이어도, 추가해야 함
jQuery.fn.serializeForm = function() { 
	var obj = null; 
	try { 
		if(this[0].tagName && this[0].tagName.toUpperCase() == "FORM" ) { 
			var arr = this.serializeArray(); 
			if(arr) { 
				obj = {}; 
				jQuery.each(arr, function() { 
					if(obj[this.name] === undefined ){ // 중복값 "," 구분
						obj[this.name] = this.value;
					} else {
						obj[this.name] = obj[this.name] + "," + this.value;
					}
				}); 
			} 
		} 
	}catch(e) { 
		alert(e.message); 
	}finally {} 
	
	return obj; 
}

/**
 * 3자리마다 ,(콤마) 추가해주는 유틸
 * @param data (숫자형태 , ex. 123123123)
 * @returns {string}	(ex. 123,123,123)
 */
var addComma = function (data) {

	if (data == null || data === '' || data.length === 0) {
		return "";
	}
	var value = data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	return value;
};

var unComma = function (data) {
	
	return data.replace(/,/g, '');
};

/**
 * 3자리마다 ,(콤마) 추가해주는 유틸 * 소수점 버림!
 * @param data
 * @returns {string}
 */
var addCommaNoDeigits = function (data) {
	if (data == null || data === '' || data.length === 0) {
		return "";
	}
	var parts=data.toString().split(".");
    return parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
};

/**
 * 달러화폐단위 .00이여도 보이게끔 추가
 * @param data
 * @returns {string}
 */
var addComma2 = function (data) {
    if (data == null || data === '' || data.length === 0) {
        return "";
    }

    return (data).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');  // 12,345.67

};
/**
 * 날짜 형식 변환
 * @param data (날짜 형태 , ex. 20190801)
 * @returns {string}	(ex. 2019-08-01)
 */
var dateFormat = function (data)
{
	if(data == "00000000" || data == "0001-01-01" || data == null){
		valstr = "";
	}else{
		if(data.length == 8){
			valstr = data.substring(0,4) + "-" + data.substring(4,6) + "-" + data.substring(6,8);
		} else if(data.length == 10) {
			valstr = data.substring(0,4) + "-" + data.substring(5,7) + "-" + data.substring(8,10);
		} else if(data.length == 6) {
			valstr = "20" + data.substring(0,2) + "-" + data.substring(2,4) + "-" + data.substring(4,6);
		} else {
			valstr = data;
		}
	}

	return valstr;
}

/**
 * 시간 형식 변환
 * @param data (시간 형태 , ex. 123525)
 * @returns {string}	(ex. 12:35:25)
 */
var timeFormat = function (data) {
	if(data == "000000"){
		valstr = "";
	}else{
		if(data.length == 4){
			valstr = data.substring(0,2) + ":" + data.substring(2,4);
		} else if(data.length == 6) {
			valstr = data.substring(0,2) + ":" + data.substring(2,4) + ":" + data.substring(4,6);
		} else {
			valstr = data;
		}
	}

	return valstr;
}


/**
 * 날짜세팅
 * @param date1
 * @param date2
 */
var dateSearch = function (date1, date2) {

    var list = {};

    var dif = (date2 - date1);

    if(date1 && date2){

        if (Number(dif) > 10000) {
            Rui.alert(cfnGetMessage('label.express.dateSearch'));
            return list;
        }

        if(date1 > date2){
            Rui.alert(cfnGetMessage('gsm.common.err.003', ["gsm.common.err.008","gsm.common.err.007"]));
            return list;
        }
    }

    if (date1  == '' ) {
        Rui.alert(cfnGetMessage('label.express.dateval1'));
        return list;
    }
    if (date2 == '') {
        Rui.alert(cfnGetMessage('label.express.dateval2'));
        return list;
    }
    if (date1  == '' && date2 == '') {
        Rui.alert(cfnGetMessage('label.express.dateval3'));
        return list;
    }
};

var leftZeroTrim = function (str) {

	if (str == null) {
		return '';
	}

	var temp = str;
	while (temp[0] === '0') {
		temp = temp.replace('0', '');
	}

	return temp;
};

//공백제거
var trimString = function(string) {
	if(string == null) return null;
	if(string == '') return '';
	return string.replace(/(\s*)/g,"");
}

//input  박스 밸리데이션 체크 ==> html 의 input 태그에 data-label의 값으로 메세지 표시 / data-required 가 "Y"인경우만 체크
var fnValidationCheckForInput = function(form) {
	var $inputList = form.find('input');
	var dateFilter = /^(19|20)\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$/; 
	var emailFilter = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
	var phoneFilter = /^[0-9-]*$/;
	var worldPhoneFilter = /^[0-9-()\[\].+ ]*$/;
	var numberFilter = /^[0-9]*$/;
	var commaNumberFilter = /^[0-9,.]*$/;
	var englishFilter = /^[ -~]*$/;
	                   
	
	for(var i = 0 ; i < $inputList.length ; i++){
		var $input = $inputList.eq(i);
		if($input.data("required") == "Y"){
			if(trimString($input.val()) == null || trimString($input.val()) == ""){
				alert($input.data("label") + getMessage("alert.inputCheck"));
				$input.focus();
				return false;
			}
		}
		
		if(trimString($input.val()) != "") {
			//숫자 체크 
			if($input.data("format") == "num" && $input.val() != ''){
				if (!$.isNumeric($input.val())) {  
					alert($input.data("label") + getMessage("alert.checkNumber"));
					$input.focus();
					return false;
				}
			}
			
			//연락처 체크 
			if($input.data("format") == "phone" && $input.val() != ''){
				if(!phoneFilter.test($input.val())){
					alert($input.data("label") + getMessage("alert.checkPhone"));
					$input.focus();
					return false;
				}
			}
			
			//전세계 전화번호 체크 
			if($input.data("format") == "worldPhone" && $input.val() != ''){
				if(!worldPhoneFilter.test($input.val())){
					alert($input.data("label") + getMessage("alert.checkWorldPhone"));
					$input.focus();
					return false;
				}
			}
			
			//숫자 체크(정수) 
			if($input.data("format") == "number" && $input.val() != ''){
				if(!numberFilter.test($input.val())){
					alert($input.data("label") + getMessage("alert.checkNumber"));
					$input.focus();
					return false;
				}
			}
			
			//comma 와 숫자 체크(정수) 
			if($input.data("format") == "commaNumber" && $input.val() != ''){
				if(!commaNumberFilter.test($input.val())){
					alert($input.data("label") + getMessage("alert.checkNumber"));
					$input.focus();
					return false;
				}
			}
			
			//날짜 체크 
			if($input.data("format") == "date" && $input.val() != ''){
				if(!dateFilter.test($input.val())){
					alert($input.data("label") + getMessage("alert.checkDate"));
					$input.focus();
					return false;
				}
			}
			
			//이메일 형식 체크 
			if($input.data("format") == "email"){
				if (!emailFilter.test($input.val())) {
					alert($input.data("label") + getMessage("alert.checkEmail"));
					$input.focus();
					return false;
				}
			}
			
			//영어만 허용  체크 
			if($input.data("format") == "english"){
				if (!englishFilter.test($input.val())) {
					alert($input.data("label") + " : " + getMessage("alert.checkEnglish"));
					$input.focus();
					return false;
				}
			}
		}
	}
	
	return true;
}

//select  박스 밸리데이션 체크 ==> html 의 select 태그에 data-label의 값으로 메세지 표시 / data-required 가 "Y"인경우만 체크
var fnValidationCheckForSelectbox = function(form) {
	var $selectList = form.find('select');
	for(var i = 0 ; i < $selectList.length ; i++){
		var $select = $selectList.eq(i);
		if($select.data("required") == "Y"){
			if(trimString($select.val()) == null || trimString($select.val()) == ""){
				//alert($select.data("label")+"을(를) 선택해 주세요.");
				alert($select.data("label") + getMessage("alert.selectCheck"));
				$select.focus();
				return false;
			}
		}
	}
	return true;
}

//textArea 밸리데이션 체크 ==> html 의 select 태그에 data-label의 값으로 메세지 표시 / data-required 가 "Y"인경우만 체크
var fnValidationCheckForTextArea = function(form) {
	var $textareaList = form.find('textarea');
	for(var i = 0 ; i < $textareaList.length ; i++){
		var $textArea = $textareaList.eq(i);
		if($textArea.data("required") == "Y"){
			if(trimString($textArea.val()) == null || trimString($textArea.val()) == ""){
				alert($textArea.data("label") + getMessage("alert.inputCheck"));
				
				$textArea.focus();
				return false;
			}
		}
	}
	return true;
}

//쿠키생성
var setCookie = function(cookieName, value, exdays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + exdays);
	var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + exdate.toGMTString());
	document.cookie = cookieName + "=" + cookieValue;
}
 
//쿠키삭제
var deleteCookie = function(cookieName) {
	var expireDate = new Date();
	expireDate.setDate(expireDate.getDate() - 1);
	document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
}
 
//쿠키조회
var getCookie = function(cookieName) {
	cookieName = cookieName + '=';
	var cookieData = document.cookie;
	var start = cookieData.indexOf(cookieName);
	var cookieValue = '';
	if(start != -1){
		start += cookieName.length;
		var end = cookieData.indexOf(';', start);
		if(end == -1)end = cookieData.length;
		cookieValue = cookieData.substring(start, end);
	}
	return unescape(cookieValue);
}


//properties 조회하는것 초기화
jQuery.i18n.properties({
	name:'message',
	path:'/messages/',
	mode:'map',
	language:'ko',
	namespace:'ko',
	callback: function () {
		
	}
});

/**
 * 다국어 자바스크립트 call 
 * mcode     : 메세지 코드
 * repCodes  : 치환될 메세지 코드
 * getMessage("alert.proc", ["settings.seller", "button.edit"]);
 */
var getMessage = function(mcode, repCodes) {
	// 기본 정보 받아 오기 
	var args = getMessageText(mcode);
	
	// 내부 텍스트 설정 받아 오기 
	if(repCodes) {
		$.each(repCodes, function(key, value) {
			args = (value == "" ? args.replace("{"+key+"}", "") : args.replace("{"+key+"}", getMessageText(value)) );
		});
	}
	if(args.indexOf("@n@") > 0 ) {
		args = args.replace('@n@',"\n").replace('@n@',"\n");
	}
	return args
}

/**
 * 다국어 텍스트 받아오기 
 * mcode     : 메세지 코드
 * getMessageText("alert.proc");
 */
var getMessageText = function(mcode) {
	var args = "";
	
	for (var i = 1; i < _MESSAGE.length; i++) {
		if(_MESSAGE[i].indexOf(mcode+":") == 0) {
      	   	var argsAry = _MESSAGE[i].split(":");
      	   	
      	   	args = argsAry[1];
      	   	break;
		}
	}
	
	return args
}

/**
 * layer popup open
 * url      : 팝업 주소
 * param    : 파라메터
 * width    : 팝업 크기
 * openFunc : 오픈 전 실행 함수
 * func     : 오픈 후 실행 함수
 * closeFunc: close 실행 함수
 * openPop(url, param, open, func, close);
 */
var openPop = function(url, param, width, openFunc, func, closeFunc, method) {
	if(!method) { method = "GET"; }
	
	$.ajax({
		type       : method
        ,url       : url
        ,data      : JSON.stringify(param) 
        ,contentType: "application/x-www-form-urlencoded; charset=utf-8"
        ,success   : function(data) {
            //console.log("success");
            //console.log(data);
            
        	// 팝업 아이디 생성
        	var cnt = $(".popup").size();
        	if(cnt == 0){	//팝업 중복생성 오류 처리
        		var id = "bpopup_area_" + $(".popup").size();
            	
                if(width && width != "") {
                	$("body").append('<div class="popup" id="' + id + '" style="width:' + width + 'px;">' + data + '</div>');
                }
                else {
                	$("body").append('<div class="popup" id="' + id + '">' + data + '</div>');
                }
        	}

            $(".popup").bPopup({
            	closeClass: "btn_close"
            	,onOpen: function() { if(openFunc && openFunc != "") openFunc(); } 
                ,onClose: function() { 
                	$("#" + id).remove(); // 해당 팝업 삭제
                	if(closeFunc && closeFunc != "") closeFunc(); 
                }
            }, function() {
            	if(func && func != "") func();
            });
        }
        ,error     : function(error) {
            returnData = "error";
//            console.log(error);
        }
    });
}

/**
 * layer popup Close
 * obj : this
 * closePop($(this));
 */
var closePop = function(obj) {
	//$(obj).closest(".popup").find(".btn_close").trigger("click");
	$(obj).closest(".popup").bPopup().close();
	$(obj).closest(".popup").remove();
}

/**
 * layer popup all Close
 * allClosePop();
 */
var allClosePop = function() {
	//$(".popup").bPopup().close();
	//$(".popup").remove();
	
	$(".popup").each(function(key, value) {
		
		//alert($(this).find(".btn_close").text() + $(this).attr("id"));
		
		$(this).bPopup().close();
		//$(this).find(".btn_close").trigger("click");
		$(this).html("");
		$(this).remove();
    });
}
//전월 구해오기
var getMonthTodate = function() {
	var date = new Date();
	var firstDayOfMonth = new Date( date.getFullYear(), date.getMonth() , 1 );

	var lastMonth = new Date ( firstDayOfMonth.setDate( firstDayOfMonth.getDate() - 1 ) );
	return lastMonth.getFullYear() + "-" + ("0"+(lastMonth.getMonth()+1)).slice(-2) + "-" + ("0"+lastMonth.getDate()).slice(-2);
}

//오늘날짜 구해오기
var getTodate = function() {
	var date = new Date();
	return date.getFullYear() + "-" + ("0"+(date.getMonth() + 1)).slice(-2) + "-" + ("0"+date.getDate()).slice(-2);
}

//오늘월 마지막일자로 구하기 
var getTodateLast = function() {
	var date = new Date();
	var lastDay = ( new Date( date.getFullYear(), (date.getMonth()+1), 0) ).getDate();
	return date.getFullYear() + "-" + ("0"+(date.getMonth() + 1)).slice(-2) + "-" + ("0"+lastDay).slice(-2);
}

//오늘날짜-30 구하기
var getOneMonthAgo = function() {
    
    var date = new Date();
	return date.getFullYear() + "-" + ("0"+(date.getMonth())).slice(-2) + "-" + ("0"+date.getDate()).slice(-2);
    
}

//select 에 공통코드 불러오기
var bindSelectAjax = function ($wrapper,$cls, mode) {
	if ( mode === undefined ) {
		mode = true;
	}
	var $clsFn;	
	$clsFn = ($cls ? $wrapper.find("." + $cls) : $wrapper.find(".select-ajax") );
	
	//console.log("_USER_lang : " + _USER_lang);
	
	$($clsFn).each(function(index) {
		var $select;
		$select = $(this);
		var fData = $(this).data();
		var codeType = fData.codetype; // value 컬럼 타입 
		//console.log(fData);
		
		var paramList = new Array() ;
		var data = new Object() ;
	    data.codeGroup = fData.param.codeGroup;
		data.locale = _USER_lang; 
	    paramList.push(data) ;
	    
		//ajaxCall("POST", fData.url, paramList, setSelectView);
		$.ajax({
	        type       : "POST"
	        ,async     : mode  // true: 비동기, false: 동기
	        ,url       : fData.url
	        ,cache     : false
	        ,data      : JSON.stringify(paramList) 
	        ,contentType: "application/json"
	        ,dataType  : "json"
	        ,beforeSend : function(xhr) {
	        	
	        	xhr.setRequestHeader(header, token);
	        }
	        ,success   : function(data) {
	        	//console.log("#########bind######");
	        	//console.log(data);
	        	//console.log("bind : " + fData.param.codeGroup);
	        	//console.log("bind : " + fData.label);
	        	$.each(data, function(index, item) {
	    			var option = "";
	    			
	    			if(fData.param.codeGroup == "F010000") { // 국가 코드 일경우 
	    				if(index == 0){
	    					option = $("<option value=''>== " + fData.label + " ==</option>");
	    					$select.append(option);
	    				}
	    					
	    				var selectedChk = "";
	    				codeId = (codeType && codeType != "") ? item.codeEtc : item.codeId;
	    				
	    				if(fData.code == codeId && fData.code != "") selectedChk = " selected"
	    				option = $("<option value='"+codeId+"'"+selectedChk+">" + "[ " + codeId + " ] " + item.codeName+"</option>");
		    			
	    			} else { // 기타 옵션
	    				if(fData.url == "/common/listCodeGroup"){
	    					if(index == 0) {
//	    						console.log("index!!!!!!!! ");
//	    						console.log(index);
	    						
	    						option = $("<option value=''>== " + "대분류" + " ==</option>");
	    						$select.append(option);
//	    						consol.log(option);
			    			}
	    					
		    				var selectedChk = "";
		    				codeId = (codeType && codeType != "") ? item.codeEtc : item.codeId;
		    				
		    				if(fData.code == codeId && fData.code != "") selectedChk = " selected"
		    				option = $("<option value='"+codeId+"'"+selectedChk+">"+item.codeDiscript+"</option>");
		    				
		    			} else {
		    				if(index == 0) {
			    				option = $("<option value=''>== " + item.codeName + " ==</option>");
//		    					option = $("<option value=''>== " + "대분류" + " ==</option>");
		    				}else{
			    				var selectedChk = "";
			    				codeId = (codeType && codeType != "") ? item.codeEtc : item.codeId;
			    				
			    				if(fData.code == codeId && fData.code != "") selectedChk = " selected"
			    				option = $("<option value='"+codeId+"'"+selectedChk+">"+item.codeName+"</option>");
			    			}
		    			}
	    			}

	    			$select.append(option);
	    		});
	            
	        }
	        ,error     : function(error) {
	        	returnData = "error";
//	        	console.log(error);
	        }
	        ,complete: function (jqXHR, textStatus, errorThrown) {
			}
		});
	});
}

//전체체크처리
/**
 * 체크 박스 전체 체크 
 * ckBox    : 체크박스 이름
 * obj      : this 
 * allCheck("ckBox", this);
 */
var allCheck = function(ckBox, obj){
	//$("input[name='" + ckBox + "']").prop('checked', $(obj).prop("checked"));
	
	$("input[name='" + ckBox + "']").each(function(){
		$(this).is(":disabled") ? $(this).prop('checked', false) : $(this).prop('checked', $(obj).prop("checked"));
	});
}

// 체크여부 확인(1개라도 체크가 있으면 true)
/**
 * 체크여부 확인 
 * chkNm    : 체크박스 이름
 * chkNum  : 최소 개수 (없으면 기본1개)
 * getChecked("ckBox", 2);
 */
var getChecked = function(chkNm, chkNum) {
	if(chkNum == undefined) chkNum = 1;
	var num = 0;
	var $chkObj = eval("$('input:checkbox[name="+chkNm+"]')");
	var returnChk = false;
	$chkObj.each(function() {
		
		 if(this.checked == true && this.value != "on"){
			 num++;
			 if(chkNum == num){
				 returnChk = true;
				 return false;
			 }
		 }
	});
	return returnChk;
}

//우편번호 검색(다음 api) 
//searchPost ('postcode','roadAddress','jibunAddress','detailAddress'); //아이디로 세팅-> 클릭하면 detailAddress로 포커싱
var searchPost = function(postcode, sellerAddr1, sellerAddr1Ename, provinceEle, cityEle, focusEle){
	new daum.Postcode({
        oncomplete: function(data) {

         var arr = data.roadAddressEnglish.split(/,\s+/); // 도로명 주소 변수
         var city;
         var province;
         
         var len = arr.length;
         if ( arr[len-2].match("-do$") ) {
             city = arr[len-3];
             province = arr[len-2];
         } else {
             city = arr[len-2];
             //province = "N/A";
             province = city;
         }

        document.getElementById(postcode).value = data.zonecode;
        document.getElementById(sellerAddr1).value = data.roadAddress;
        document.getElementById(sellerAddr1Ename).value = data.roadAddressEnglish;
        document.getElementById(provinceEle).value = province;
        document.getElementById(cityEle).value = city;
        
      // document.getElementById(focusEle).focus();    
        
        
//        document.getElementById("postcode").value = data.zonecode;
//        document.getElementById("sellerAddr1").value = data.roadAddress;
//        document.getElementById("sellerAddr1Ename").value = data.roadAddressEnglish;
//        document.getElementById("province").value = province;
//        document.getElementById("city").value = city;
    }
    }).open();
}

var setSort = function() {
	var sortOrder = $("input[name='sortOrder']").val();
	if ( sortOrder ) {
		var preFix =sortOrder.substring(0,3);
		var strSort =sortOrder.substring(3);
		$("table.tbtype thead tr th").each(function() {
			var sortName = $(this).data("sort-name");
			if(sortName == strSort && preFix == "DEC")  {
				$(this).find("div").attr("class","desc-sort");
			} else if(sortName == strSort && preFix == "ASC")  {
				$(this).find("div").attr("class","asc-sort");
			} else if(sortName != undefined) {
				$(this).find("div").attr("class","sort-default");
			}
		});
	}
};

var double2int = function (value) {
	if (typeof value == 'string') {
		value = value.replace(/\..*$/, "");     // "34.0" 을 34 로 바꿈
	}
	return value;
}

// unit : g - 0-9의 숫자와 comma 만 가능
// unit : lb - 파운드. 0-9의 숫자와 . comma 만 가능
var validateNumber = function(unit, element) {
	
	var filter = {
		"g" : /^[0-9]*$/,
		"lb" : /^[0-9.]*$/
	};
	
	if( ! filter[unit].test( element.val() )){
		alert(element.data("label") + " : " + getMessage("alert.checkNumber.universal"));
		return false;
	}
	
	return true;
}