// READY
$(function() {
	initialize();
});

var initialize = function() {
	initControls();
	bindEvent();
};

var initControls = function() {
	var $submitform = $('#searchform');
	bindSelectAjax($submitform);

};

var bindEvent = function() {


	//NOTICE 조회
	$("#btn_search").on('click',function(){
		search();
	});
	
	$(document).on("keyup","#btn_search",function(){
        if(e.keyCode == 13){
            search();
        }
    });
	
    // 상세보기
    $(document).on("click",".list-edit",function(){  
        var idx = $(this).closest("tr").data("idx");
        var url = "/showNotice?idx=" + idx;  
        
        location.href = url;
    });
	

};

// 페이징 페이지 이동
var gotoPage = function(page) {
    $("#defaultSearchForm input[name='currentPage']").val(page);
    defaultSearchForm.submit();
}

// 페이지 사이즈 변경
var changPageSize = function(val){
    $("#defaultSearchForm input[name='pageSize']").val(val);
    $("#defaultSearchForm input[name='currentPage']").val("1");
    
    defaultSearchForm.submit();
}

// 전체 검색
var search = function(){
    searchform.submit();
}
var cb = function(data){
	console.log("aaaaaaaaa")
	console.log(data);
}