$(document).ready(onReady) ;

function onReady() {
    //	셀렉트박스의 옵션 데이터를 불러옵니다.
	getSelectOptions() ;
    //	CKEDITOR 를 화면에 적용합니다.
	CKEDITOR.replace('content', {height: 277}) ;
    //	화면 각 요소에 이벤트 핸들러를 등록합니다.
	setEventHandlers() ;
}

function setEventHandlers() {
    //	버튼 '목록'
    $("button#btn_list").on('click', function() {
        location.href = "/admin/board/selectNotice" ;
    });
    //	버튼 '등록'
	$("button#btn_save").on('click', function() {
		//	입력 항목들의 유효성을 점검합니다.
		if (!checkParams()) { return ; }
		var options = {} ;
		options.dataType = "text" ;
		options.success = function(data) {
			// 등록이 완료 되었습니다.
		    alert(getMessage("alert.proc.end", ["button.insert"])) ;
		    location.href = "/admin/board/selectNotice" ;
        } ;
        options.error = function(e) {
        	// 등록 중 오류가 발생했습니다.
        	alert(getMessage("alert.proc.err", ["button.insert"])) ; 
        }
        $("#searchform").ajaxForm(options).submit() ;
	});
    //	버튼 '파일 선택'
	$("input#file_up").on("change", function(e) {
		var _fileList = $(this)[0].files ;
		if (_fileList.length > 0) {
			$("ul#uplist li").remove() ;
			$("ul#uplist").append("<li id='clear' onclick='clearFileList()'>첨부취소</li>") ;
		}
		for (var i=0; i<_fileList.length; i++) {
			addFileList(_fileList[i]) ;
		}
		var name = $("#file_up")[0].files[0].name ;
        $("#upload_name").val(name) ;
	}) ;
} ;

////////////////////////////////////////////////////////////////////////////////
////
////	입력 항목들의 유효성을 점검합니다.
////
////////////////////////////////////////////////////////////////////////////////

function checkParams() {
	//	공지사항
	var partCode = $("#partCode").val();
    if (partCode == "D040000" || partCode == "") {
        alert("공지 유형을 선택해주세요.") ; // alert(getMessage("notice.partSelect"));
        return false ;
    }
	//	상단 고정여부
	var flagTop = $("#flagTop").val();
	if (flagTop != undefined && flagTop == '') {
        alert("상단 고정여부를 선택해주세요.") ;
        return false ;
	}
	//	공지기간
	var notiFromDate = $("#notiFromDate").val();
    var notiToDate = $("notiToDate").val();
	//	첨부파일
    var file_up = $("#file_up").val();
	//	기타 공통?
    var $submitform = $('#searchform');
    if(!fnValidationCheckForInput($submitform)) {
    	return false ;
    }
	setContent() ;
    if(!fnValidationCheckForTextArea($submitform)) {
    	return false ;
    }
	return true ;
}

////////////////////////////////////////////////////////////////////////////////
////
////	TOOLS : 크게 커스터마이징이 필요 없는 기능들
////
////////////////////////////////////////////////////////////////////////////////

function getSelectOptions() {
	//	data-param 속성의 codeGroup 을 기준으로 데이터를 가져옵니다.
	//	data-codetype 속성이 etc 인 경우 codeId 대신 codeEtc 컬럼을 적용합니다.
	//	data-code 속성의 값과 option.value 값이 같을 경우 선택됨(selected) 옵션을 적용합니다.
	var _selectList = $("select.select-ajax") ;
	$(_selectList).each( function() {
		var _select = $(this) ;
		var _data = $(_select).data() ;
		var _param = new Array() ;
		_param.push({
			codeGroup: _data.param.codeGroup,
			locale: _USER_lang
		}) ;
		var _success = function(data) {
			$(data).each(function(index, item) {
				var _optionValue = item.codeId ;
				var _optionText = item.codeName ;
				var _selected = "" ;
				if (index == 0) { _optionText = ("== "+_optionText+" ==") ; }
				if (data.codetype && dta.codetype == "etc") { _optionValue = item.codeEtc ; }
				if (_data.code == _optionValue) { _selected = "selected" ; }
				var _option = ("<option value='"+_optionValue+"' "+_selected+">"+_optionText+"</option>") ;
				$(_select).append(_option) ;
			}) ;
		}
		doAjax(_data.url, _param, _success) ;
	}) ;
}

function doAjax(_url, _data, _success) {
	var _ajax = {} ;
	//	크게 바뀔 일 없는 기본 설정들
	_ajax.type = "POST" ;
	_ajax.async = true ;
	_ajax.cache = false ;
	_ajax.contentType = "application/json" ;
	_ajax.dataType = "json" ;
	//	사이트간 요청 위조(CSRF) 방지를 위한 CSRF 토큰 설정
	_ajax.beforeSend = function(xhr) {
		var _header = $("meta[name='_csrf_header']").attr("content") ;
		var _token = $("meta[name='_csrf']").attr("content") ;
		xhr.setRequestHeader(_header, _token) ;
	}
	_ajax.error = function(e) { console.log(e) ; }
	//	파라미터로 전달받은 항목들 설정
	_ajax.url = _url ;
	_ajax.data = JSON.stringify(_data) ;
	_ajax.success = _success ;
	$.ajax(_ajax) ;
}

function setContent() {
	//	ckEditor 사용하면 text 앞뒤로 <p>가 들어가서  substring 사용하여 가공한다
	//	ckEditor에서 공백은 db에 &nbsp;로 들어가게 되어 정규포현식으로 제거함
	var content = CKEDITOR.instances.content.getData() ;
	content = content.substring(3, content.length-5) ;
	$("#content").val(content) ;
}

function nPad(nVal, nLen) {
	if (nVal && nLen) {
		var _val = ('0000'+nVal) ;
		var _len = nLen * 1 ;
		if (_len > 0) { _len *= -1 ; }
		return _val.slice(_len) ;
	} else {
		return '' ;
	}
}

function getToday() {
	var _today = new Date() ;
	var _yyyy = _today.getFullYear() ;
	var _mm = _today.getMonth() + 1 ;
	var _dd = _today.getDate() ;
	return (_yyyy+'-'+nPad(_mm, 2)+'-'+nPad(_dd, 2)) ;
}

function getForm() {
	var _form = document.createElement("form") ;
	_form.setAttribute("method", "post") ;
	_form.setAttribute("enctype", "multipart/form-data") ;
	_form.setAttribute("action", "/admin/board/insertNotice") ;
	return _form ;
}

function getInput(_name, _value) {
	var _input = document.createElement("input") ;
	_input.setAttribute("type", "hidden") ;
	_input.setAttribute("name", _name) ;
	_input.setAttribute("value", _value) ;
	return _input ;
}

function addFileList(_file) {
	$("ul#uplist").append("<li title='"+_file.size+"bytes' onclick='removeFileList(this)'>"+_file.name+"</li>") ;
}

function deleteFile(_item) {
	if (confirm("파일을 삭제하시겠습니까? 해당 작업은 복원 할 수 없습니다.")) {
		var _url = '/admin/board/deletefile' ;
		var _data = {idx : $(_item).data().idx} ;
		doAjax(_url, _data, function(data) {
			console
			if (data && data.result == 1) {
				var tr = $(_item).parent().parent() ;
				$(tr).remove() ;
			}
		}) ;
	}
}

function clearFileList() {
	$("input#file_up").val('') ;
	$("ul#uplist li").remove() ;	
}