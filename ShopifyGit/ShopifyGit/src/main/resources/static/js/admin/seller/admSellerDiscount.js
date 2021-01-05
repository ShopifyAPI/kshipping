// READY
$(function() {
    initialize();
//    $("table tr th, table tr td").resizable({handles: 'e'});
});

var initialize = function() {
    initControls();
    bindEvent();
};

var initControls = function() {
    var $submitform = $('#submitform');
    bindSelectAjax($submitform);
    
    var shipCompany = $("select[name='partShipCompany']").data("code");
    setShipService(shipCompany);
};

var onChange = false; 
var bindEvent = function() {
    
    //배송사 select Box 변경시 배송서비스 select Box data set 
    $("#partShipCompany").on("change",function(){
        //alert('change1');
        //배송사 코드
        if($(this).val() != ""){
                onChange = true;
                var shipCompany = this.value;
                setShipService(shipCompany);    
            }
        });
 
        
    // 테스트 요금 조회 
    $(document).on("click","#btn_search",function(){
        //alert("search");
        sendSearch();
    });

//    //배송서비스 select Box 변경시 재조회
//    $("#partDeliveryService, #nowDate").on("change",function(){
//    	sendSearch();
//    });
    
    $(document).on("change","#file_up",function(e){
        
        var name = $("#file_up")[0].files[0].name; //파일이름
        
        //확장자를 체크(xls xlsx 형식만 지원함)
        var extension = name.slice(name.lastIndexOf(".") + 1).toLowerCase(); //파일 확장자를 잘라내고, 비교를 위해 소문자로 만듬.
        if(extension != "xls" && extension != "xlsx"){ 
            alert(getMessage("price.fileUploadExtension"));
            $("#file_up").val("");
            return false;
        };
        
        $("#upload_name").val(name);
        
    });
    
 // 엑셀 다운 로드 
    $(document).on("click","#downExcel",function(){
    	
        var nowDate = $("#nowDate").val();
        var zoneCodeGroup = $("#partDeliveryService option:selected").val();
        var shipCompany = $("#partShipCompany option:selected").val();
            
        location.href = "/admin/seller/discountExcel?zoneCodeId="+shipCompany+"&zoneCodeGroup="+zoneCodeGroup+"&nowDate="+nowDate;
    });
    
};


var sendServerPop = function(codeGroup){
	
	var paramList = new Array() ;
	var data = new Object() ;
	data.codeGroup = codeGroup
	data.locale = _USER_lang; 
	paramList.push(data) ;
	
	//ajaxCall("POST", fData.url, paramList, setSelectView);
	$.ajax({
		type       : "POST"
			,async     : true  // true: 비동기, false: 동기
			,url       : "/common/componentDataSet"
				,cache     : false
				,data      : JSON.stringify(paramList) 
				,contentType: "application/json"
					,dataType  : "json"
						,beforeSend : function(xhr) {
							
							xhr.setRequestHeader(header, token);
						}
	,success   : function(data) {
		setSelectBoxPop(data)
	}
	,error     : function(error) {
		returnData = "error";
		console.log(error);
	}
	,complete: function (jqXHR, textStatus, errorThrown) {
	}
	}); 
};

var setShipService = function(ship){
    var shipCompany = ship;
    //alert('shipCompany:' + shipCompany);
    
    var codeGroup = "";
    //alert("----test----");
    //우체국
    if(shipCompany == "B010010"){
        codeGroup = "B020000";
    }else if(shipCompany == "B010020"){
        codeGroup = "B040000";
    }else if(shipCompany == "B010030"){
        codeGroup = "B140000";
    }else if(shipCompany == "B010040"){
        codeGroup = "B150000";
    }
    else{
        //콤보박스 초기화
         
        $("#partDeliveryService").empty();
        var option = $("<option value = ''>== 배송 서비스 ==</option>");
        $('#partDeliveryService').append(option);
        return;
        
    }
    //$("#partDeliveryService").empty();
    sendServer(shipCompany);
}
 

var sendServer = function(shipCompany){
    var param = {"shipCompany":shipCompany}        
    ajaxCall("post", "/admin/price/selectShipService", param, setSelectBox); 
}
 

var sendSearch = function(){
    var nowDate = $("#nowDate").val();
    var zoneCodeGroup = $("#partDeliveryService option:selected").val();
    var shipCompany = $("#partShipCompany option:selected").val();
    var searchWord = $("#searchWord").val()
    var searchType = $("#searchType option:selected").val();
        
    location.href = "/admin/seller/discount?zoneCodeId="+shipCompany+"&zoneCodeGroup="+zoneCodeGroup+"&nowDate="+nowDate+"&searchWord="+searchWord+"&searchType="+searchType;
}
    
var setSelectBox = function(data){
    //콤보박스 초기화
    $("#partDeliveryService").empty();
    var code = $("select[name='partDeliveryService']").data("code");
    var company = $("select[name='partShipCompany'] option:selected").text();

    var list = data.list;
    
    for(var i = 0; i < list.length; i++){
        var option = "";
        
        if(i == 0) {
            option = $("<option value = ''>== " + company + " ==</option>");
            $('#partDeliveryService').append(option);
        }
        
        if(code == list[i].codeEtc && onChange == false) {
            option = $("<option value = '"+list[i].codeEtc+"' selected>"+list[i].codeKname+"</option>");    
        } else {
            option = $("<option value = '"+list[i].codeEtc+"'>"+list[i].codeKname+"</option>");
        }
        
        $('#partDeliveryService').append(option);
    }
};
 
