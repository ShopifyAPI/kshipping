// READY
    $(function () {
        initialize();
    });
 
    var initialize = function () {
        initControls();
        bindEvent();
    };
    
    var initControls = function () {
        
    };
    
    var popupSelectBind = function () {
    	var $submitform = $('#submitform');
    	bindSelectAjax($submitform);
    }
    
    var bindEvent = function () {
    	// 포장재 등록 popup
        $(document).on("click",".add",function(){       
            openPop("/setting/popup/popAddBox", "", 600, "", popupSelectBind, "");
        });
        
        $(document).on("click","#btnSave",function(){
        	editBox();
        });

        // 포장재 등록 proc (popup)
        $(document).on("click",".save",function(){
        	
        	var boxWeight = $("#boxWeight").val();
        	
        	var boxWeightNumber = Number(boxWeight);
        	
        	$("#boxWeight").val(boxWeightNumber);
        	
            var $submitform = $('#submitform');
            
            // Validation check
            if(!validationCheck()){
                return;
            }
            
            if(confirm(getMessage("alert.proc", ["settings.menuBox", "button.insert"]))){  // 포장재를 수정하시겠습니까?
                var type = "post";
                var url = "/setting/insertBox";

                // 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
                var param = $submitform.serializeObject(); 
                ajaxCall(type, url, param, sendFrom);   
            }
        });
//        
//        // 포장재 수정 popup 
//        $(document).on("click",".list-edit",function(){  
//            var idx = $(this).closest("li").data("idx");
//            var url = "/setting/popup/popAddBox?idx=" + idx;
//            openPop(url, "", 600, "", popupSelectBind, "");
//        });
//        
//        // 포장재 수정 proc (popup)
//        $(document).on("click",".edit",function(){  
//            var $submitform = $('#submitform');
//            
//            // Validation check
//            if(!validationCheck()){
//                return;
//            }
//            
//            if(confirm(getMessage("alert.proc", ["settings.menuBox", "button.edit"]))){  // 포장재를 수정하시겠습니까?
//                var type = "post";
//                var url = "/setting/updateBox";
//                
//                // 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
//                var param = $submitform.serializeObject(); 
//                ajaxCall(type, url, param, sendFrom);   
//            }
//        });
        
        //포장재 수정
        var editBox = function() {
        	if(!$("input[name='ckBox']").is(":checked")){
        		alert(getMessage("alert.save.notCheck"));  //삭제할 대상을 선택해 주세요
        		return;
        	}
        	
        	if (! fnValidationCheckForInput($("#listform"))) {
    			return;
    		}
        	
        	
        	if(confirm(getMessage("alert.editConfirm"))){
        		var $submitform = $('#listform');
        		var type = "post";
        		var url = "/setting/updateBox";
        		
        		// 입력된 모든Element(을)를 문자열의 데이터에 serialize 한다.
        		
        		var param = [];

        		$("table.tbtype tbody tr input[name='ckBox']:checked").each(function() {
        			var tr = $(this).closest('tr');
        			var row = {};
        			row["ckBox"] = $(tr).find("input[name='ckBox']").val();
        			row["boxTitle"] = $(tr).find("input[name='boxTitle']").val();
        			row["boxWeight"] = $(tr).find("input[name='boxWeight']").val();
        			row["boxWidth"] = $(tr).find("input[name='boxWidth']").val();
        			row["boxHeight"] = $(tr).find("input[name='boxHeight']").val();
        			row["boxLength"] = $(tr).find("input[name='boxLength']").val();
        			row["weightUnit"] = $(tr).find("input[name='weightUnit']").val();
        			row["boxUnit"] = $(tr).find("input[name='boxUnit']").val();
        			param.push( row );
        		});
        		
        		
        		// var param = JSON.stringify(formArray);
        		ajaxCall(type,url,param,sendFrom);
        	}
        }
        
        // 포장재 삭제
        $(document).on("click",".list-del",function(){
        	if(!$("input[name='ckBox']").is(":checked")){
        		alert(getMessage("alert.deleteChk"));  //삭제할 대상을 선택해 주세요
        		return;
        	}
        	if(confirm(getMessage("alert.deleteConfirm"))){  // 선택항목을 삭제 하시겠습니까?
        		var $submitform = $('#listform');
        		var type = "post";
        		var url = "/setting/deleteBox";
        		
        		var param = [];

        		$("table.tbtype tbody tr input[name='ckBox']:checked").each(function() {
        			var tr = $(this).closest('tr');
        			var row = {};
        			row["ckBox"] = $(tr).find("input[name='ckBox']").val();
        			row["boxTitle"] = $(tr).find("input[name='boxTitle']").val();
        			row["boxWeight"] = $(tr).find("input[name='boxWeight']").val();
        			row["boxWidth"] = $(tr).find("input[name='boxWidth']").val();
        			row["boxHeight"] = $(tr).find("input[name='boxHeight']").val();
        			row["boxLength"] = $(tr).find("input[name='boxLength']").val();
        			row["weightUnit"] = $(tr).find("input[name='weightUnit']").val();
        			row["boxUnit"] = $(tr).find("input[name='boxUnit']").val();
        			param.push( row );
        		});
        		ajaxCall(type,url,param,sendFrom);
        	
//            if(confirm(getMessage("alert.proc", ["settings.menuBox", "button.delete"]))){  // 포장재를 수정하시겠습니까?
//                var type = "post";
//                var url = "/setting/deleteBox";
//                var idx = $(this).closest("li").data("idx");
//                var param = {"boxIdx":idx}
//                
//                ajaxCall(type, url, param, sendFrom);   
            }
        });
        
    };
    
    var validationCheck = function(){
    	var $submitform = $('#submitform');
        
        // input type Validation
        if(!fnValidationCheckForInput($submitform)){
            return false;
        }
        
        if($("#boxLength").val() == 0){
        	var $input = $("#boxLength");
        	alert($("#boxLength").data("label") + getMessage("alert.inputCheck"));
        	$("#boxLength").focus();
        	return false;
        }
        
        if($("#boxWidth").val() == 0){
            var $input = $("#boxWidth");
            alert($input.data("label") + getMessage("alert.inputCheck"));
            $input.focus();
            return false;
        }
        
        if($("#boxHeight").val() == 0){
            var $input = $("#boxHeight");
            alert($input.data("label") + getMessage("alert.inputCheck"));
            $input.focus();
            return false;
        }
        
        if($("#boxWeight").val() == 0){
            var $input = $("#boxWeight");
            alert($input.data("label") + getMessage("alert.inputCheck"));
            $input.focus();
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