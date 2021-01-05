    // READY
    $(function () {
        initialize();
    });
 
    var initialize = function () {
        initControls();
        bindEvent();
    };
    
    var initControls = function () {
        var $submitform = $('#searchform');
        bindSelectAjax($submitform); // 검색 select box 세팅
    };
    
    // 등록/수정 팝업 select box 세팅
    var popupSelectBind = function () {
        var $submitform = $('#submitform');
        bindSelectAjax($submitform);
    }
    
    var bindEvent = function () {
        // check box 컨트롤
        $(document).on("click","input[name='allCheck']",function(){
            allCheck("ckBox", this);
        });
        
        // 제품 업로드
        $(document).on("click",".productGet",function(){       
            location.href="/setting/listSku?loadCheck=Y";
        });
        
        
        // 포장재 등록 popup
        $(document).on("click",".add",function(){       
            openPop("/setting/popup/popAddSku", "", 600, "", popupSelectBind, "");
        });

        // 포장재 등록 proc (popup)
        $(document).on("click",".save",function(){  
            var $submitform = $('#submitform');
            
            // Validation check
            if(!validationCheck()){
                return;
            }
            
            if(confirm(getMessage("alert.proc", ["settings.menuSku", "button.insert"]))){  // 포장재를 수정하시겠습니까?
                var type = "post";
                var url = "/setting/insertSku";

                // 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
                var param = $submitform.serializeObject(); 
                ajaxCall(type, url, param, sendFrom);   
            }
        });
        
        // 관세 수정 popup 
        $(document).on("click",".list-edit",function(){  
            var idx = $(this).closest("tr").data("idx");
            var url = "/setting/popup/popAddSku?idx=" + idx;
            openPop(url, "", 600, "", popupSelectBind, "");
        });
        
        // 포장재 수정 proc (popup)
        $(document).on("click",".edit",function(){  
            var $submitform = $('#submitform');
            
            // Validation check
            if(!validationCheck()){
                return;
            }
            
            if(confirm(getMessage("alert.proc", ["settings.menuSku", "button.edit"]))){  // 포장재를 수정하시겠습니까?
                var type = "post";
                var url = "/setting/updateSku";
                
                // 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
                var param = $submitform.serializeObject(); 
                ajaxCall(type, url, param, sendFrom);   
            }
        });
     
     // 저장
        
        $(document).on("click","#btnSave",function(){
        	editBox();
        });

     // 배송정보 팝업 > 관세정보 > 포장재 정보 > 포장재 명 
    	$(document).on("change","#boxSelect", function(){ changeBox($(this)); });
        
        
    // 관세 삭제
	    $(document).on("click",".list-del",function(){   
	        if(!$("input[name='ckBox']").is(":checked")){
	            alert( getMessage("alert.deleteChk") ); // 삭제할 대상을 선택해주세요
	            return;
	        }
	        
	        if(confirm(getMessage("alert.deleteConfirm"))){  // 선택항목을 삭제 하시겠습니까?
	            var $submitform = $('#listform');
	            var type = "post";
	            var url = "/setting/deleteMultiSku";
	            
	            var param = $submitform.serializeObject();
	            ajaxCall(type, url, param, sendFrom);
	        }
	    });

    
        //검색
        $(document).on("click",".ic_search",function(){
            search();
        });
        
        $(document).on("keyup",".ic_search",function(){
            if(e.keyCode == 13){
                search();
            }
        });
        
        
        //다운로드(엑셀)
        $(document).on("click","#down",function(){   
            down();
        });
        //업로드 (엑셀)
        $(document).on("click","#upload",function(){   
            var url = "/setting/popup/popVolumeWeight";
            openPop(url, "", "510", "", popupSelectBindVolume, ""); 
        });
       
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
        
        //엑셀 팝업 저장버튼 클릭시
        $(document).on("click","#btn_setting_pop_weight",function(){
            var $submitform = $('#settingweight');
            //파일
            var file_up = $("#file_up").val();
            if(file_up == null || file_up == ""){
                alert(getMessage("price.addFile")+getMessage("alert.selectCheck"));//첨부파일을 선택해주세요.
                return;
            }
            
            var options = {
                    dataType:"json",//결과
                    success:function(data){
                        console.log("-------------success----------------");
                        console.log(data)
                        alert(getMessage("alert.proc.end", ["price.ExcelUpload"]));  // 엑셀업로드가 완료 되었습니다.
                        location.replace("/setting/listSku");                        // #### 필수 ####
                    },beforeSend:function(){
                        $('.spinner').removeClass('spinner');
                        $('.spinner').addClass('spinner-dimmed');
                        $('.loading').addClass('loading');
                    },complete:function(){
                        $('.spinner').removeClass('spinner-dimmed');
                        $('.spinner').addClass('spinner-show');
                    },error:function(e){                    
                        console.log("-------------error----------------");
                        console.log(e)
                        //alert(getMessage("alert.proc.err", ["price.ExcelUpload"]));  // 등록 중 오류가 발생했습니다. 
                        location.replace("/setting/listSku");                         // #### 필수 ####
                    }
                };
            
            $("#settingweight").ajaxForm(options).submit();
        });
        // 엑셀업로드 팝업 select box 세팅
        var popupSelectBindVolume = function () {
            console.log("---------------popupSelectBindVomume----------------------") 
            var $submitform = $('#settingweight');
            bindSelectAjax($submitform);
            sendServerPop('');
        }
        // ****************************************************************************
        
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
                //alert("success");
            }
            ,error     : function(error) {
                returnData = "error";
                console.log(error);
            }
            ,complete: function (jqXHR, textStatus, errorThrown) {
            }
        }); 
    }
    // 엑셀 다운 로드 
    var down = function(){
        console.log("searchBoxType:" + $("#defaultSearchForm input[name=searchBoxType]").val());
        console.log("searchType:" + $("#defaultSearchForm input[name=searchType]").val());
        console.log("searchWord:" + $("#defaultSearchForm input[name=searchWord]").val());
        console.log("pageSize:" + $("#defaultSearchForm input[name=pageSize]").val());
        console.log("currentPage:" + $("#defaultSearchForm input[name=currentPage]").val());
        var $submitform = $('#defaultSearchForm');
        var type = "post";
        var url = "/setting/saveSkuExcel";
        var param = $submitform.serializeObject();
        ajaxCall(type, url, param, saveExcelEnd);
    }
    
    var saveExcelEnd = function(data) {
        if(data.errCode==true){
            alert( getMessage("settings.down.success") );
        }else{
            if(data.errMsg != null)
               alert(data.errMsg);
            else
               alert( getMessage("settings.down.fail") );
        }
    }
    
    // validation 체크
    var validationCheck = function(){
        var $submitform = $('#submitform');
        
        // input type Validation
        if(!fnValidationCheckForInput($submitform)){
            return false;
        }
        
        // select type Validation
        if(!fnValidationCheckForSelectbox($submitform)){
            return false;
        }
        
        return true;
    }

    // 프로세스 완료 콜백
    var sendFrom = function(data) {
        if(data.errCode == true) {
            alert(getMessage("alert.proc.end", [data.procCode]));  // 등록(수정)이 완료 되었습니다.
            location.reload();
        } else {
            alert(getMessage("alert.proc.err", [data.procCode]));  // 등록(수정) 중 오류가 발생했습니다.
        }
    }
    
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
	// 제풉타입 필터링
    var changeProduct = function(val){
        $("#defaultSearchForm input[name='searchProductType']").val(val);
        $("#defaultSearchForm input[name='currentPage']").val("1");
        
        defaultSearchForm.submit();
    }
    
    
 // selectBox 선택시 가로,세로,높이 변경
    var changeBox = function(optObj) {
    	
    	var obj=optObj.find("option:selected");
    	
    	var boxWidth = obj.closest("tr").find("input[name='itemWidth']");
    	var boxLength = obj.closest("tr").find("input[name='itemLength']");
    	var boxHeight = obj.closest("tr").find("input[name='itemHeight']");
    	var boxTitle = obj.closest("tr").find("input[name='itemTitle']");
    	
    	boxWidth.val(obj.data("boxwidth"));
    	boxLength.val(obj.data("boxlength"));
    	boxHeight.val(obj.data("boxheight"));
    	boxTitle.val(obj.data("boxtitle"));
    }
    
    
  // 제품 정보 업데이트
    var editBox = function() {
    	if(!$("input[name='ckBox']").is(":checked")){
    		alert(getMessage("alert.save.notCheck"));  //삭제할 대상을 선택해 주세요
    		return;
    	}
    	
    	if(confirm(getMessage("alert.editConfirm"))){
    		var $submitform = $('#listform');
    		var type = "post";
    		var url = "/setting/updateSkuBox";
    		
    		// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
    		
    		var param = [];

    		$("table.tbtype tbody tr input[name='ckBox']:checked").each(function() {
    			var tr = $(this).closest('tr');
    			var row = {};
    			row["ckBox"] = $(tr).find("input[name='ckBox']").val();
    			row["selectBox"] = $(tr).find("select[name='selectBox']").val();
    			row["itemCode"] = $(tr).find("input[name='itemCode']").val();
    			row["itemName"] = $(tr).find("input[name='itemName']").val();
    			row["boxTitle"] = $(tr).find("input[name='itemTitle']").val();
    			row["boxWidth"] = $(tr).find("input[name='itemWidth']").val();
    			row["boxHeight"] = $(tr).find("input[name='itemHeight']").val();
    			row["boxLength"] = $(tr).find("input[name='itemLength']").val();
    			row["useYn"] = $(tr).find("select[name='useyn']").val();
    			param.push( row );
    		});
    		
    		
    		// var param = JSON.stringify(formArray);
    		ajaxCall(type,url,param,sendFrom);
    	}
    }
