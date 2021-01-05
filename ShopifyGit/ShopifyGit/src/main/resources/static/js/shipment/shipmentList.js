    var jsonText = "";
    var amount = 0;
    var payment = 0;
    //var deliveryAmount = 0;
    //var rankPrice = 0;
    var paymentTotal = 0;
    var amount3 = 0;
    var paymentVat = 0;
    var deliveryAmount = 0;
    var rankPrice = 0;
    
    var deliveryAmountChk = false;
    var paymentVatChk = false;
    var rankPriceChk = false;
    
    // READY
    $(function () {
        var param_masterCode = "";
        initialize();
    });
 
    var initialize = function () {
        initControls();
        bindEvent();
    };
    
    var initControls = function () {
        setDate();
        
        var $submitform = $('#searchform');
        bindSelectAjax($submitform); // 검색 select box 세팅
        bindSelectAjax($('div.action')); // 중간검색창 select box 세팅

        setSort();
    };
    
    // 등록/수정 팝업 select box 세팅
    var popupSelectBind = function () {
        var $submitform = $('#submitform');
        bindSelectAjax($submitform);
    }
    
    var bindEvent = function () {
        // check box 컨트롤
        $(document).on("click","input[name='allCheck']",function(){
            allCheck("masterCode", this);
        });
        
        //상세보기
        $(document).on("click",".list-detail",function(){
            
            var masterCode = $(this).data("code");
            var combineCode = $(this).data("combinecode");
            //var url = "/cs/popup/popDetailShipment?masterCode=" + masterCode + "&combineCode=" + combineCode;
            var url = "/cs/popup/backShowPop?masterCode=" + masterCode + "&combineCode=" + combineCode;
            openPop(url, "", 1200, "", "", "");
            
            //var masterCode = $(this).data("code");
            //detail(masterCode);
        });
        
        
    	// 배송목록 > 정렬기준
    	$(document).on("click", "table.tbtype thead tr th", function() {
    		if ($(this).data("sort-name") != null)  {
    			var sortName = $(this).data("sort-name");
    			var oldSortName = $("input[name='sortShipment']").val();
    			if (oldSortName=="DEC"+sortName) {
    				$("input[name='sortShipment']").val("ASC"+sortName);
    			} else {
    				$("input[name='sortShipment']").val("DEC"+sortName);
    			}
    			gotoPage(1);
    		}
    	});
        
        // 배송 삭제
        $(document).on("click",".del",function(){
            var delCnt = 0;
            if($("input[name='masterCode']").is(":checked")){
                delCnt++;
                
            }
            if(delCnt == 0){
                alert( getMessage("alert.deleteChk") ); // 삭제할 대상을 선택해주세요
                return;
            }
            if(confirm(getMessage("alert.deleteConfirm"))){  // 선택항목을 삭제 하시겠습니까?
                var $submitform = $('#listform');
                var type = "post";
                var url = "/shipment/deleteMultiShipment";
                var param = $submitform.serializeObject();
                console.log(param)
                ajaxCall(type, url, param, sendFromDelete);
            }
        });
        
        
        $(document).on("click","#btnPayment",function(){
            allClosePop();
            if(!$("input[name='masterCode']").is(":checked")){
                alert( getMessage("alert.checkboxChk") ); // 삭제할 대상을 선택해주세요
                console.log("return");
                return;
            }
       		 else{
           		var test= false;
         	   $("input[name='masterCode']:checked").each(function(){
                    if($(this).data("statecode") != "A020020" && $(this).data("statecode") != "A020025") {
                    	console.log("state");
                    	 alert(getMessage("alert.shipment.error"));
                    	 test = true;
                    }
            	});
           		if(test == true)
               		return;
       		 }
            	popPaymentProc('');

        });

        // 바코드 출력
        $(document).on("click",".bacord",function(){ 
    	    var krCnt = 0;
    	    var osCnt = 0;
    	    
            allClosePop();
            if(!$("input[name='masterCode']").is(":checked")){
                alert( getMessage("alert.checkboxChk") ); // 삭제할 대상을 선택해주세요
                console.log("return");
                return;
            }
       		 else{
           		var test= false;
         	   $("input[name='masterCode']:checked").each(function(){
                    if($(this).data("statecode") == "A020020") {
                    	 alert(getMessage("alert.barcode.proc"));
                    	 test = true;
                    } else {
                    	if ($(this).data("country").toUpperCase() == "KR") {
       	                    krCnt++;
       	                } else {
       	                    osCnt++;
       	                }
                    }
            	});
         	   
           		if(test == true) {
           			return;
           		}
           		
	           	if (krCnt && osCnt) {
	     	        alert(getMessage("alert.barcode.destination.mixed")); // 바코드는 용지가 달라서 국내와 해외를 분리해서 발행해야 합니다. 
	     	        return;
	     	    }
       		 }
            printBarcode(krCnt);
        });
        
        //다운로드
        $(document).on("click","#downExcel",function(){   
            downExcel();
        });
        //다운로드(펌뱅킹)
        $(document).on("click","#downExcelBank",function(){   
            downExcelBank();
        });
        //검색
        $(document).on("click",".ic_search",function(){
            search();
        });
        
        $(document).on("keyup","#searchWord",function(e){
            if(e.keyCode == 13){
                search();
            }
        });
        
        $(document).on("click","#btnStateChange",function(e){
            stateChange();
        });
        
        $(document).on("click",".pop_body #btnOk",function(e){
            stateChangeProc();
        });
        
        $(document).on("click",".pop_body #btnCancel",function(e){
            allClosePop();
        }); 

        $(document).on("click", "table.tbtype thead tr th", function() {
            if ( $(this).data("sort-name") )  {
                var sortName = $(this).data("sort-name");
                var oldSortName = $("input[name='sortOrder']").val();
                if (oldSortName=="DEC"+sortName) {
                    $("input[name='sortOrder']").val("ASC"+sortName);
                } else {
                    $("input[name='sortOrder']").val("DEC"+sortName);
                }
                gotoPage(1);
            }
        });
    };
    
    // 검색일자
    var setDate = function(){
        var searchDateStartSet = $("input[name=searchDateStart]").val();     
        var searchDateEndSet =  $("input[name=searchDateEnd]").val();
        var todayStart = getMonthTodate();
        var todayEnd = getTodateLast();
        if(searchDateStartSet != "") todayStart = searchDateStartSet;
        if(searchDateEndSet != "") todayEnd = searchDateEndSet;
        $("input[name=searchDateStart]").val(todayStart);           
        $("input[name=searchDateEnd]").val(todayEnd);
    };
    
    // validation 체크
    var validationCheck = function(){
        var $submitform = $('#submitform');
        return true;
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
    
    // 국내/해외 변경
    var changeDestType = function(val){
        $("#defaultSearchForm input[name='searchDestType']").val(val);
        $("#defaultSearchForm input[name='currentPage']").val("1");
        
        defaultSearchForm.submit();
    }
    
    // 배송사 변경
    var changeCompany = function(val){
        $("#defaultSearchForm input[name='searchCompany']").val(val);
        $("#defaultSearchForm input[name='currentPage']").val("1");
        
        defaultSearchForm.submit();
    }
    
    // 전체 검색
    var search = function(){
        //alert($("#searchState").val());
        document.searchform.submit();
    }
    
    // 엑셀 다운 로드 
    var downExcel = function() {
    	var _url = "/shipment/shipmentExcel" ;
    	searchDateStartValue = $("input[name=searchDateStart]").val() ;
    	searchDateEndValue = $("input[name=searchDateEnd]").val() ;
    	if (searchDateStartValue && searchDateStartValue.length == 10) {
        	if (searchDateEndValue && searchDateEndValue.length == 10) {
        		_url += ("?searchDateStart="+searchDateStartValue) ;
        		_url += ("&searchDateEnd="+searchDateEndValue) ;
        	}    		
    	}
        location.replace(_url);
    }
    // 엑셀 다운 로드 
    var downExcelBank = function() {
    	var _url = "/shipment/shipmentExcelBank" ;
    	searchDateStartValue = $("input[name=searchDateStart]").val() ;
    	searchDateEndValue = $("input[name=searchDateEnd]").val() ;
    	if (searchDateStartValue && searchDateStartValue.length == 10) {
        	if (searchDateEndValue && searchDateEndValue.length == 10) {
        		_url += ("?searchDateStart="+searchDateStartValue) ;
        		_url += ("&searchDateEnd="+searchDateEndValue) ;
        	}    		
    	}
        location.replace(_url);
    }
    var sendFromDelete = function() {
        search();
    }
    
    //결제하기
    var paymentReady = function(masterCode){
        var masterCodeList =  new Array;
        var payCnt = 0;
        allClosePop();
        if(masterCode == ""){
            if(!$("input[name='masterCode']").is(":checked")){
                 alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
                 return;
            }else{
                $('input:checkbox[name="masterCode"]').each(function() {
                    //checked 처리된 항목의 값
                    /* if(this.checked){
                        payCnt++;
                    }
                    
                    if(payCnt > 1){
                        alert( getMessage("alert.checkboxChk.one") );    //1개만 선택해주세요
                        return;
                    }else{
                        if($(this).data("state") == "Y"){
                            payCnt--;
                            alert( getMessage("alert.payment.proc") );    //기 결제성공인 주문이 선택되었습니다.
                            return;  
                        }else{
                            masterCode = this.value;
                        }
                        
                    } */
                    
                      if(this.checked){
                          
                          if($(this).data("state") == "Y"){
                              alert( getMessage("alert.payment.proc") );    //기 결제성공인 주문이 선택되었습니다.
                              $(this).prop('checked',false);
                          }else{
                              masterCodeList.push(this.value);
                              //팝업이 뜨고 프로세스 진행후에 this.value가 전체 코드로 들어가기때문에 masterCode를 뽑아오기위함 처리
                              if(this.value.indexOf("payment") > 0) {
                                  masterCodeList = JSON.parse(masterCodeList[0]);
                                  masterCodeList = masterCodeList.masterCodeList;
                              }
                              payCnt++;
                          }
                      }
                 });
            }
        }else{
            payCnt = 1;
        }
        if(payCnt == 1){
            if(masterCode == "") masterCode = masterCodeList;
            openPop("/shipment/popup/popPaymentShipment?masterCode="+masterCode+"&", "", 1200, "", "", "");
        }else if(payCnt > 1){
            openPop("/shipment/popup/popPaymentShipment?masterCode="+masterCodeList+"&", "", 1200, "", "", "");
            masterCodeList = "";
        }     
    }
    
    // 상태 변경
    var stateChange = function() {
    	//alert("stateChange");
        if(!$("input[name='masterCode']").is(":checked")){
            alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
            return;
       }else{
           var ck = true;
           // 상태변경은 배송완료 건에 대해서만 처리 해달라는 요청
           $("input[name='masterCode']:checked").each(function(){
               if(ck) {
                   if($(this).data("statecode") != "A020099") {
                       alert( getMessage("err.stateChange") ); // 상태변경은 배송완료건 만 가능합니다.
                       ck = false
                   }
               }
           });
           
           if(ck) {
               $("input[name='masterCode']:checked").each(function(){
                       //if($(this).data("statecode") != "A020099") {
                           var check = stateChangeCheck();
                           var masterCodeList = check.masterCodeList;
                           var payCnt = check.payCnt;
                           openPop("/shipment/popup/popShipmentChange", "", 450, "", popupSelectBind, "");   
                       //}
               });
               
           }
           
       }
    }
    
    //가능 여부 확인
    var stateChangeCheck = function() {
        var masterCodeList =  new Array;
        var payCnt = 0;
        
        $("input[name='masterCode']:checked").each(function(){
            /* if($(this).data("state") == "Y"){
                alert( getMessage("alert.payment.proc") );    //기 결제성공인 주문이 선택되었습니다.
                $(this).prop('checked',false);
            }else{
                masterCodeList.push(this.value);
                payCnt++;
            } */
            
            masterCodeList.push(this.value);
            payCnt++;
        })
        
        return {
            payCnt: payCnt,
            masterCodeList: masterCodeList
        };
    }
    
    // 팝업 select box 세팅
    var popupSelectBind = function () {
        var $submitform = $('#popBodyform');
        bindSelectAjax($submitform);
    }
    
    // 상태 변경 반송 사유
    var shipmentStateView = function(val) {
        /* if(val == "A030000") {
            $("#shipmentStateSubArea").show();
        } else {
            $("#shipmentStateSubArea").hide();
        } */
    }
    
    // 상태 변경 프로세스
    var stateChangeProc = function() {
        if(!$("input[name='masterCode']").is(":checked")){
            alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
            return;
        }
        var shipmentState = $("#shipmentState").val();
        var shipmentStateSub = $("#shipmentStateSub").val();
        var changeReason= $("#changeReason").val();
        
        /* if(shipmentState == "A030000" && shipmentStateSub == "") {
            alert( getMessage("cs.backReason") + getMessage("alert.selectCheck") ); // 반송사유를 선택해 주세요
            return;
        } */

        var $submitform = $("#listform");
        var url = "/shipment/popup/popShipmentChangeProc";
        var param = $submitform.serializeObject();
        param.stateGroup = shipmentState;
        param.reason = shipmentStateSub;
        param.changeReason = changeReason;
        
        ajaxCall("post", url, param, stateChangeInfo);  
    }
    
    // 상태 변경 완료
    var stateChangeInfo = function(data) {
        if(data.errCode){
            alert( getMessage("alert.proc.end", ["button.edit"]) );
            location.reload();
        }else{
            alert( getMessage("alert.proc.err", ["button.edit"]) );
        }
        //allClosePop();
        search();
    }
    
    var paymentProc = function(form){
        var $submitform = form;
        var type = "post";
        var url = "/shipment/popup/popPaymentShipmentProc";
        var param = $submitform.serializeObject();
        console.log("payment");
        console.log(param);
        ajaxCall(type, url, param, paymentInfo);    
    }
    
    var paymentInfo = function(data) {
        if(data.errCode){
            alert( getMessage("alert.payment.sucess") );
        }else{
            alert( getMessage("alert.payment.faild") );
        }
        //allClosePop();
        search();
    }
    
    var printBarcode = function(krCnt) {
        allClosePop();
        
        var notice = krCnt ? "alert.payment.barcode.lotteHome" : "alert.payment.barcode";
        
        if(confirm( getMessage(notice) )) {
            var $submitform = $('#listform');
            var param = $submitform.serializeObject();
            
            if ( krCnt ) {
            	var url = "/shipment/popup/popBacordPdfPrint?masterCode="+param.masterCode;
            	var newwindow = window.open(url, "_blank",'height=800,width=500');
            	if ( window.focus ) { 
            		newwindow.focus();
            	}
            } else {
            	openPop("/shipment/popup/popBacordPrint?masterCode="+param.masterCode, "", 700);
            }
            return false;
        }
    }
    
   
    var detail = function(masterCode) {
        allClosePop();
        openPop("/shipment/popup/popDetailShipment?masterCode="+masterCode+"&", "", 1062);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////
    // 결제 팝업 스크립트
    ///////////////////////////////////////////////////////////////////////////////////////
    $(document).on("click","#payCash", function() {
        alert( getMessage("alert.payment.method") ); // =죄송합니다. 현재 지원되지 않는 결제방법 입니다.
        $("#payPaypal").prop("checked",true);
    });
    
    $(document).on("click","#payEtc", function() {
        alert( getMessage("alert.payment.method") ); // =죄송합니다. 현재 지원되지 않는 결제방법 입니다.
        $("#payPaypal").prop("checked",true);
    });
    
    $(document).on("click",".pop_body input[name='pickup']",function(){ // 전체 체크 
        allCheck("pickChk", this);
        deliveryPayment();
    });
    
    //특송 배송 체크박스 선택
    $(document).on("click",".pop_body input[name='pickChk']",function() {
        deliveryPayment();
    });
  
    // 택배 업체 선택
    $(document).on("change",".pop_body select[name='courier']",function() {
        setLocalCompanyMapping($(this).val());
    });
    
    // 택배 업체 선택
    $(document).on("click",".pop_body .addPickup",function() {
        addLocalDeliveryList();
    });
    
    // 택배 박스 사이즈 변경
    $(document).on("change",".pick_list select[name='box_size']",function() {
        deliveryPayment();
    });
    
    var addLocalDeliveryList = function(){
        if(!$("input[name='pickChk']").is(":checked")){
            alert(getMessage("alert.checkboxChk"));
            return false;
        }

        // 국내 택배 생성
        var arrMasterCode = new Array();
        i = 0;
        $("input[name='pickChk']:checked").each(function(){
            var masterCode = $(this).data("mcode");
            ck = checkLocalDelivery(masterCode);
            
            if(ck == true) {
                arrMasterCode[i] = masterCode;
                i++;
            }
        });
        
        if(arrMasterCode.length > 0) {
            makeLocalDelivery(arrMasterCode);   
        }
    }
    
    // 택배에 묶여 있는 특송인지 확인
    var checkLocalDelivery = function(code){
        var check = true;
        $(".pick_list .deliveryAreaZone").find("div").each(function(){
            if($(this).data("mastercode") == code) {
                check = false;
            }
        });
        
        return check;
    }
    
    // 국내 택배 생성
    var makeLocalDelivery = function(arr){
        var len = $(".pick_list li").size();
        var localService = $("select[name='courier'] option:selected").val();
        var localServiceName = $("select[name='courier'] option:selected").text();
        
        var tempArea = $("#tempLocalHtml").html();
        var tempDelivery = $("#tempDeliveryHtml").html();
        var deliveryHtml = "";
        
        // 택배 구간 생성
        tempArea = tempArea.replace("#localService#", localService);
        tempArea = tempArea.replace("#localServiceName#", localServiceName);
        $(".pick_list").append(tempArea);
        
        // 택배 구간에 특송 리스트 적용
        $.each(arr, function(index, item){ 
            var $obj = $("#pickup_" + item);
            var goods = $obj.data("goods"); 
            var price = $obj.data("amount"); 
            var orderDate = $obj.data("orderdate");
            
            deliveryHtml = deliveryHtml + tempDelivery.replace(/#masterCode#/g, item);
            deliveryHtml = deliveryHtml.replace("#goods#", goods);
            deliveryHtml = deliveryHtml.replace("#price#", addComma(price));
            deliveryHtml = deliveryHtml.replace("#orderDate#", orderDate);
        });

        $(".pick_list li").eq(len).find(".deliveryAreaZone").append(deliveryHtml);
        
        $(".pick_list li").eq(len).find("select[name='box_size']").each(function(){
            var comCode = $(this).closest("li").data("localcode");
            $(this).attr("id") == comCode ? $(this).show() : $(this).remove();
        });
        
        // 택배비 계산
        deliveryPayment();
    }
    
    // 배송리스트 중 다른 배송사 제외
    var setLocalCompanyMapping = function(com) {
        if(com == "") {
            $("input[name='pickChk']").attr('disabled', false).removeClass("disabled");
        } else {
            $("input[name='pickChk']").each(function(){
                if($(this).data("couriercom") != com) {
                    $(this).prop("checked", false).attr('disabled', true).addClass("disabled");
                } else {
                    $(this).attr('disabled', false).removeClass("disabled");
                }
            });
        }
    }
    
    var viewDetail = function(obj) {
        var view = $(obj).closest("table").find("tbody").hasClass("pick-off");
        $(".pick_list .deliveryAreaZone").find("tbody").addClass("pick-off");
        
        if(view == true){
            $(obj).closest("table").find("tbody").removeClass("pick-off");
        }
    }
    
    //국내 픽업 서비스 설정
    $(document).on("click",".pop_body input[name='pickupService']",function(){
        if($(this).prop("checked")) {
            if($(".pick_list li").size() <= 0) {
                $(".pop_body .pickupService").show();
                $("input[name='pickup']").prop("checked", false);
                allCheck("pickChk", $("input[name='pickup']"));
                
                deliveryPayment(); // 가격 초기화
            }
        } else {
            if($(".pick_list li").size() > 0) { // 국내 택배가 있을경우 
                if(confirm(getMessage("shipment.popup.payment.check.confirm"))){   //체크 해제시 픽업서비스가 초기화 됩니다. 해제하시겠습니까?
                    $("input[name='pickup']").prop("checked", false);
                    allCheck("pickChk", $("input[name='pickup']")); 
                    
                    cleanLocalDelivery(); // 국내 택배 초기화
                }else{  //픽업서비스가 체크되어있으면 체크해제 취소시 그대로 체크하도록 나둠
                    $(".pop_body input[name='pickupService']").prop("checked", true);
                }
            }
        }
    });
    
    // 국내 택배 초기화
    var cleanLocalDelivery = function(){
        $(".pop_body .pickupService").hide();
        $(".pop_body .pick_list").html("");
 
        
        $(".amountBox .pickupService .amount4").text("0");
        $(".amountBox .pickupService .amount4_vat").text("0");
        $(".amountBox .amount1").text(0);
        $(".amountBox .amount2").text(0);
        $(".amountBox .amount3").text(0);
        $(".amountBox .amount3_vat").text("0");
    }
    
    // 배송비 적용 
    var deliveryPayment = function() {
        var popPayment = 0;
        var popSalePayment = 0;
        var popLocalPayment = 0;
        var popLocalPaymentVat = 0;
        var popTotalPayment = 0;

        var masterCode = new Array();
        var localData = new Array();
        
        var jsonData = new Object();
        
        if($("input[name='pickupService']").is(":checked")) {
            if($(".pick_list li").size() > 0) { // 국내 배송이 있을경우 처리 
                var payCode = getDeliveryLocalPayment();
                popPayment = payCode.payment;
                popSalePayment = payCode.salePayment;
                popLocalPayment = payCode.localPayment;
                popTotalPayment = payCode.totalPayment;
                masterCode = payCode.masterCode;
                localData = payCode.localData;
                
                /* console.log("deliveryPayment[popPayment] : " + popPayment);
                console.log("deliveryPayment[popSalePayment] : " + popSalePayment);
                console.log("deliveryPayment[popLocalPayment] : " + popLocalPayment);
                console.log("deliveryPayment[popTotalPayment] : " + popTotalPayment); */
                
                if(popLocalPayment > 0) {
                    popLocalPaymentVat = popLocalPayment * 0.1;
                    popPaymentTot = popLocalPayment + popPayment - popSalePayment;
                    
                    $(".amountBox .pickupService").show();
                    $(".amountBox .pickupService .amount4").text(addComma(popLocalPayment)); // 픽업 결제 금액
                    $(".amountBox .pickupService .amount4_vat").text(addComma(popLocalPaymentVat)); // 픽업 결제 금액
                    $(".amountBox .amount3_vat").text(addComma(popLocalPaymentVat)); // 총 결제예정 금액
                    $(".amountBox .amount3_sum").text(addComma(popLocalPayment + popPayment)); // 총 결제예정 금액
                    $(".amountBox .amount4_tot").text(addComma(popLocalPayment + popLocalPaymentVat)); // 총 결제예정 금액
                }
            } else {
                popPayment = 0;
                popSalePayment = 0;
                popLocalPayment = 0;
                popTotalPayment = 0;
                popPaymentTot = 0;
                
                $(".amountBox .pickupService").hide();
                $(".amountBox .pickupService .amount4").text(0); // 픽업 결제 금액
                $(".amountBox .pickupService .amount4_vat").text(0); // 픽업 결제 금액
                $(".amountBox .amount3_vat").text(0); // 총 결제예정 금액
                $(".amountBox .amount3_sum").text(0); // 총 결제예정 금액
            }

            $(".amountBox .amount1").text(addComma(popPayment)); // 총 배송 금액
            $(".amountBox .amount2").text(addComma(popSalePayment)); // 등급 할인 금액
            $(".amountBox .amount3_sum").text(addComma(popPaymentTot)); // 총 결제예정 금액
            $(".amountBox .amount3").text(addComma(popTotalPayment)); // 총 결제예정 금액
        } else {
            var payCode = getDeliveryPayment();
            popPayment = payCode.payment;
            popSalePayment = payCode.salePayment;
            popTotalPayment = payCode.totalPayment;
            masterCode = payCode.masterCode;
            localData = payCode.localData;

            $(".amountBox .pickupService").hide();
            $(".amountBox .pickupService .amount4").text("0"); // 픽업 결제 금액
            $(".amountBox .pickupService .amount4_vat").text("0"); // 픽업 결제 금액
            $(".amountBox .amount1").text(addComma(popPayment)); // 총 배송 금액
            $(".amountBox .amount2").text(addComma(popSalePayment)); // 등급 할인 금액
            $(".amountBox .amount3").text(addComma(popTotalPayment)); // 총 결제예정 금액
            $(".amountBox .amount3_sum").text(addComma(popTotalPayment)); // 총 결제예정 금액
            $(".amountBox .amount3_vat").text("0"); 
        }
        
        if(masterCode.size == 1) {
            jsonData.masterCode = masterCode[0];
        }
        jsonData.payment = popPayment;
        jsonData.rankPrice = popSalePayment;
        jsonData.localPayment = popLocalPayment;
        jsonData.localPaymentVat = popLocalPaymentVat;
        jsonData.paymentTotal = popTotalPayment;
        jsonData.masterCodeList = masterCode;
        if(localData != null && localData != "" && localData.size > 0){
            jsonData.localData = localData;
        }
        
         
        console.log("######## jsonData : " + jsonData)
        
        // String 형태로 변환
        //var jsonText = JSON.stringify(jsonData) ;
        jsonText = jsonData ;
        
        // 결제 관련 세팅
        $("input[name='masterCode']").val(JSON.stringify(jsonText));
    }
    
    // 특송 배송비
    var getDeliveryPayment = function() {
        var payment = 0;
        var salePayment = 0;
        var totalPayment = 0;
        var localPayment = 0;
        
        var masterCode = new Array();
        var localData = new Array();
        
        $(".pop_body input[name='pickChk']:checked").each(function(){
            /////////////////////////////////////////////////////////////////////////
            // 변수 선언
            var origin = $(this).data("origin");
            var courierCom = $(this).data("couriercom");
            var courier = $(this).data("courier");
            var mcode = $(this).data("mcode");
            var amount = $(this).data("amount");
            var country = $(this).data("country");
            var rankprice = $(this).data("rankprice");
            
            /////////////////////////////////////////////////////////////////////////
            // 계산
            payment += amount;
            salePayment += rankprice;
            
            masterCode.push(mcode);
        })
        
        totalPayment = payment - salePayment;
        
        return {
            payment : payment
            ,salePayment : salePayment
            ,totalPayment : totalPayment
            ,localPayment : localPayment
            ,masterCode : masterCode 
            ,localData : localData 
        };
    }
    
    // 국내 택배 배송비
    var getDeliveryLocalPayment = function() {
        var payment = 0;
        var salePayment = 0;
        var totalPayment = 0;
        var localPayment = 0;
        
        var masterCode = new Array();
        var localData = new Array();
        
        // 특송 요금 계산
        $(".pick_list .pickon").each(function(){
            var mcode = $(this).data("mastercode");
            var $obj = $("#pickup_" + mcode);
            
            /////////////////////////////////////////////////////////////////////////
            // 변수 선언
            var origin = $obj.data("origin");
            var courierCom = $obj.data("couriercom");
            var courier = $obj.data("courier");
            //var mcode = $obj.data("mcode");
            var amount = $obj.data("amount");
            var rankprice = $obj.data("rankprice");
            
            if(rankprice == "") { // 예외처리
                rankprice = 0;
            }
            
            if(amount == "") { // 예외처리
                amount = 0;
            }
            
            /////////////////////////////////////////////////////////////////////////
            // 계산
            payment += amount;
            salePayment += rankprice;
            
            masterCode.push(mcode);
        });
        
        
        // 택배 요금 계산
        $(".pick_list li select[name='box_size']").each(function(index, item){
            //var $obj = $(".pick_list li select[name='box_size']").eq(index);
            var $obj = $(this);
            var price = ($obj.find("option:selected").data("price") == "") ? 0 : $obj.find("option:selected").data("price");
            localPayment += price
            
            var localObject = new Object();
            var company = $obj.attr("id");
            var boxSize = $obj.val();
            
            localObject.company = company;
            localObject.boxSize = boxSize; 
            localObject.price = price; 
            
            var arrMcode = new Array();
            var code = "";
            $obj.closest("div").find(".pickon").each(function(){
                code = $(this).data("mastercode");              
                arrMcode.push(code);
            });
            
            localObject.mcode = arrMcode; 
            localData.push(localObject);
        });
        
        totalPayment = payment - salePayment + localPayment + (localPayment * 0.1);
        
        return {
            payment : payment
            ,salePayment : salePayment
            ,totalPayment : totalPayment
            ,localPayment : localPayment
            ,masterCode : masterCode 
            ,localData : localData 
        };
    }
    
    $(document).on("click",".pop_body .sendPayment",function(){
        var proc = false;
        
        if($(".pop_body input[name='pickupService']").is(":checked")) { //택배 픽업
            
            var i = 0;
            var boxChk = true;
            $(".pick_list select[name='box_size']").each(function() { // 
                i++;
                if(boxChk) {
                    if($(this).val() == "") {
                        boxChk = false;
                        $(this).focus();
                    }
                }
            });
            
            if(i <= 0){ 
                alert( getMessage("alert.checkboxChk") ); // 대상을 선택해 주세요
            } else if(boxChk){
                if(confirm(getMessage("alert.payment.confirm"))) { // 결제를 진행 하시겠습니까?
                    proc = true;
                }
            }else {
                alert( getMessage("alert.checkboxChk.boxsize") ); // 박스사이즈를 선택해주세요
            }
        } else { //특송
            if($(".pop_body input[name='pickChk']").is(":checked")) {
                if(confirm(getMessage("alert.payment.confirm"))) { // 결제를 진행 하시겠습니까?
                    proc = true;
                }
            } else {
                alert( getMessage("alert.checkboxChk") ); // 대상을 선택해 주세요
            }
        }
        
        if(proc) { // 배송 진행
            popPaymentProc();
        }
    });
    

    var popPaymentProc = function(masterCode) {
		var masterCodeListArr = [];
		var payCnt = 0;
		var jsonDataList = [];
		
		var krCnt = 0;
   	    var osCnt = 0;
		
		allClosePop();
		
		if (masterCode == "") {
			if (!$("input[name='masterCode']").is(":checked")) {
				alert(getMessage("alert.checkboxChk")); // 대상을 선택해주세요
				return;
			} 
			
			$('input:checkbox[name="masterCode"]:checked').each( function() {
				if ($(this).data("country").toUpperCase() == "KR") {
					krCnt++;
				} else {
					osCnt++;
				}

				var jsonData = {};
				jsonData.masterCode = this.value;
				jsonDataList.push(jsonData);
				payCnt++;
			});
		} else {
			payCnt = 1;
		}
		
		if (krCnt && osCnt) {
 	        alert(getMessage("alert.shipment.destination.mixed")); // 국내배송과 해외배송을 동시에 요청할 수 없습니다. 
 	        return;
 	    }
	
		var url = "/shipment/popup/popPaymentShipmentProc";
		console.log(jsonDataList);
		ajaxCall("post", url, jsonDataList, popPaymentPopOpenEnd);
	}
    
    var popPaymentPopOpenEnd = function(data) {
    	console.log("data.errCode : " + data.errCode);
        if(!data.errCode){
        	var msg = getMessage("alert.payment.shipping.faild") + "\n-------\n" + data.errMsg;
            alert( msg );
            search();
        }else{
            alert( getMessage("alert.payment.shipping.sucess") );
            location.reload();
        }
    }