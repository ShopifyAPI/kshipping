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
        //결제승인
        $(document).on("click",".confirm",function(){
            popPaymentPopOpen('');
        });
        
        // check box 컨트롤
        $(document).on("click","input[name='allCheck']",function(){
            allCheck("chkmasterCode", this);
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
        
        // 배송 삭제
        $(document).on("click",".del",function(){
            var delCnt = 0;
            if($("input[name='chkmasterCode']").is(":checked")){
                delCnt++;
                
            }
            if(delCnt == 0){
                alert( getMessage("alert.deleteChk") ); // 삭제할 대상을 선택해주세요
                return;
            }
            if(confirm(getMessage("alert.deleteConfirm"))){  // 선택항목을 삭제 하시겠습니까?
                console.log("yryryryryryryrtest111")
                var $submitform = $('#listform');
                var type = "post";
                var url = "/payment/deleteMultiPayment";
                var param = $submitform.serializeObject();
                console.log(param)
                ajaxCall(type, url, param, sendFromDelete);
            }
        });
        
        
        $(document).on("click","#btnPayment",function(){
            allClosePop();
            paymentReady('');
        });
        
        // 바코드 출력
        $(document).on("click",".bacord",function(){  
            allClosePop();
            if(!$("input[name='chkmasterCode']").is(":checked")){
                alert( getMessage("alert.checkboxChk") ); // 삭제할 대상을 선택해주세요
                return;
            }
            printBarcode();
        });
        
        //다운로드
        $(document).on("click",".down",function(){   
            downExcel();
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
        
        $(document).on("click",".state_change",function(e){
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
        var searchDateStartSet = $("#defaultSearchForm input[name=searchDateStart]").val();
        var searchDateEndSet =$("#defaultSearchForm input[name=searchDateEnd]").val();
        if( ! searchDateStartSet ) {
        	searchDateStartSet = getMonthTodate();
        }
        if( ! searchDateEndSet ) {
        	searchDateEndSet = getTodateLast();
        }
        $("#searchform input[name=searchDateStart]").val(searchDateStartSet);           
        $("#searchform input[name=searchDateEnd]").val(searchDateEndSet);
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
        var param = {
                   "searchDateStartValue" : $("input[name=searchDateStart]").val(),
                   "searchDateEndValue"   : $("input[name=searchDateEnd]").val(),
                   "searchDestType"       : $("input[name=searchDestType]").val(),
                   "searchCompany"        : $("input[name=searchCompany]").val()
        };
        
        var _url = "/payment/paymentExcel?" + $.param(param) ;
        location.replace(_url);
    }
    
    var sendFromDelete = function() {
        search();
    }
    
    //결제하기
	var paymentReady = function(noData) {
	    var masterCodeList = [];
	    var krCnt = 0;
	    var osCnt = 0;
	    var companyCodeSet = new Set();
	    var companyNameSet = new Set();
	
	    allClosePop();
	    
	    
	    var s = new Set()
	    s.add("hello").add("goodbye").add("hello")
	    console.log(s.size); // === 2
	    console.log(s.has("hello"));   // === true
	    console.log("ss:" + s);
	    
	    
	
	    if (!$("input[name='chkmasterCode']").is(":checked")) {
	        alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
	        return;
	    } else {
	        $('input:checkbox[name="chkmasterCode"]:checked').each(function() {
	            if ($(this).data("state") == "Y") {
	                alert(getMessage("alert.payment.proc")); // 기 결제성공인 주문이 선택되었습니다.
	                $(this).prop('checked', false);
	            } else {
	                if ($(this).data("country").toUpperCase() == "KR") {
	                    krCnt++;
	                } else {
	                    osCnt++;
	                }
	                masterCodeList.push(this.value);
	                companyCodeSet.add($(this).data("companycode"));
	                companyNameSet.add($(this).data("companyname"));
	            }
	        });
	    }
	
	    if (krCnt && osCnt) {
	        alert(getMessage("alert.payment.destination.mixed")); // 국내배송과 해외배송을 동시에 결제할 수 없습니다. 
	        return;
	    }
	    
	    if ( krCnt && companyCodeSet.size != 1 ) {
	    	alert(getMessage("alert.payment.company.mixed"));     // 하나의 배송사만 선택하여 주세요.
	    	return;
	    }
	
	    if (masterCodeList.length > 0) {
	    	var callback;
	    	
	    	if ( krCnt ) {
	    		var code = companyCodeSet.values().next().value;
	    		var name = companyNameSet.values().next().value;
	    		console.log(krCnt + ", " + code + ", " + name);
	    		
	    		callback = function() {
	    			initPopPaymentPayment(krCnt, code, name);
	    		}
	    	} else {
	    		callback = function() {
	    			initPopPaymentPayment();
	    		}
	    	}
//	        openPop("/payment/popup/popPaymentPayment?masterCode=" + masterCodeList + "&krCnt=" + krCnt, "", 1200, "", "", "");
	        openPop("/payment/popup/popPaymentPayment?masterCode=" + masterCodeList + "&krCnt=" + krCnt, "", 1200, "", callback, "");
	    } else {
	        alert(getMessage("alert.checkboxChk")); // 대상을 선택해주세요
	    }
	}
    
    // 상태 변경
    var stateChange = function() {
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
               var check = stateChangeCheck();
               var masterCodeList = check.masterCodeList;
               var payCnt = check.payCnt;
   
               openPop("/payment/popup/popShipmentChange", "", 450, "", popupSelectBind, "");   
           }
           
       }
    }
    
    //가능 여부 확인
    var stateChangeCheck = function() {
        var masterCodeList =  new Array;
        var payCnt = 0;
        
        $("input[name='chkmasterCode']:checked").each(function(){
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
        if(!$("input[name='chkmasterCode']").is(":checked")){
            alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
            return;
        }
        var shipmentState = $("#shipmentState").val();
        var shipmentStateSub = $("#shipmentStateSub").val();
        
        /* if(shipmentState == "A030000" && shipmentStateSub == "") {
            alert( getMessage("cs.backReason") + getMessage("alert.selectCheck") ); // 반송사유를 선택해 주세요
            return;
        } */

        var $submitform = $("#listform");
        var url = "/payment/popup/popShipmentChangeProc";
        var param = $submitform.serializeObject();
        param.stateGroup = shipmentState;
        param.reason = shipmentStateSub;
        
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
        console.log("2)paymentList-----------");
        var $submitform = form;
        var type = "post";
        var url = "/payment/popup/popPaymentPaymentProc";
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
    
    var printBarcode = function() {
        allClosePop();
        var $submitform = $('#listform');
        var param = $submitform.serializeObject();
        openPop("/payment/popup/popBacordPrint?masterCode="+param.masterCode+"&", "", 700);
    }
    
    var detail = function(masterCode) {
        allClosePop();
        openPop("/payment/popup/popDetailShipment?masterCode="+masterCode+"&", "", 1062);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////
    // 결제 팝업 스크립트
    ///////////////////////////////////////////////////////////////////////////////////////
    /*
    $(document).on("click","#payCash", function() {
        alert( getMessage("alert.payment.method") ); // =죄송합니다. 현재 지원되지 않는 결제방법 입니다.
        $("#payPaypal").prop("checked",true);
    });
    */
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
        console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        var courier = $("select[name='courier'] option:selected").val();
        console.log(">>>>>>>>>>courier:" + courier);
        if(courier == 'B010010')
        {
            alert("우체국 서비스는 현재 준비중입니다. 서비스 가능시 공자사항을 통해 알려드리겠습니다. 감사합니다.");
            return;
        }
        /*
        else if(courier == 'B010030')
        {
            alert("판토스 픽업 서비스는 셀러분이 직접 집하지로 배송하셔야 됩니다.\n [인천 서구 갑문1로 20 SKY BOX T3 3층 판토스]");
            return;
        }
        */
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
        
        console.log("--------------");
        console.log( $(".pop_body").html()  )
        // 택배비 계산
        deliveryPayment();
        console.log("--------------");
        console.log( $(".pop_body").html()  )
    }
    
    // 배송리스트 중 다른 배송사 제외
    var setLocalCompanyMapping = function(com) {
    	console.log(">>>com:" + com);
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
        var paymentCode;
        var paymentVat = 0;

        var masterCode = new Array();
        var localData = new Array();
        
        var jsonData = new Object();
        
        // 국내배송이면
        if ( $('#krCnt').val() > 0 ) {
        	 var payCode = getDeliveryHomePayment();
        	 
        	 paymentCode = "DO";		// 국내 택배
             popPayment = payCode.localPayment;		// 바뀜
             popSalePayment = payCode.salePayment;
             popLocalPayment = payCode.payment;     // 바뀜
             popTotalPayment = payCode.totalPayment;
             masterCode = payCode.masterCode;
             localData = payCode.localData;
             
             paymentVat = popPayment * 0.1;
             popPaymentTot = popPayment + paymentVat;
             
//             $(".amountBox .pickupService").show();
//             $(".amountBox .pickupService .amount4").text(addComma(popLocalPayment)); // 픽업 결제 금액
//             $(".amountBox .pickupService .amount4_vat").text(addComma(popLocalPaymentVat)); // 픽업 결제 금액
//             $(".amountBox .amount4_tot").text(addComma(popLocalPayment + popLocalPaymentVat)); // 총 결제예정 금액
             
             $(".amountBox .amount3_sum").text(addComma(popPayment)); // 총 결제예정 금액
             $(".amountBox .amount3_vat").text(addComma(paymentVat)); // 총 결제예정 금액
             $(".amountBox .amount3").text(addComma(popPaymentTot)); // 총 결제예정 금액
                 
        } else if($("input[name='pickupService']").is(":checked")) {
            if($(".pick_list li").size() > 0) { // 국내 배송이 있을경우 처리 
                var payCode = getDeliveryLocalPayment();
                console.log("------------getDeliveryLocalPayment------------");
                console.log("payCode:" + JSON.stringify(payCode));
                /*
                if(deliveryAmount > 0)
                    popPayment = deliveryAmount;
                else 
                    popPayment = payCode.payment;       // 조한두 
                if(rankPrice > 0)   
                    popSalePayment = rankPrice;
                else
                    popSalePayment = payCode.salePayment;        // 조한두 
                */
                popPayment = payCode.payment;
                popSalePayment = payCode.salePayment;
                popLocalPayment = payCode.localPayment;
                popTotalPayment = payCode.totalPayment;
                masterCode = payCode.masterCode;
                localData = payCode.localData;
                
                console.log("ND deliveryPayment[deliveryAmount] : " + deliveryAmount);
                console.log("ND deliveryPayment[rankPrice] : " + rankPrice);
                console.log("ND deliveryPayment[popPayment] : " + popPayment);
                console.log("ND deliveryPayment[popSalePayment] : " + popSalePayment);
                console.log("ND deliveryPayment[popLocalPayment] : " + popLocalPayment);
                console.log("ND deliveryPayment[popTotalPayment] : " + popTotalPayment); 
                
                if(popLocalPayment > 0) {
                    popLocalPaymentVat = Math.round(popLocalPayment * 0.1);
                    popPaymentTot = popLocalPayment + popPayment - popSalePayment;
                    console.log("-------local------------");
                    console.log("ND deliveryPayment[popLocalPayment] : " + popLocalPayment);
                    console.log(">> ND deliveryPayment[popLocalPaymentVat] : " + popLocalPaymentVat); 
                    
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
             
            $(".amountBox .amount1").text(addComma(popPayment-popSalePayment)); // 총 배송 금액
            $(".amountBox .amount2").text(addComma(popSalePayment)); // 등급 할인 금액
            $(".amountBox .amount3_sum").text(addComma(popPaymentTot)); // 총 결제예정 금액
            $(".amountBox .amount3").text(addComma(popTotalPayment)); // 총 결제예정 금액
              
            console.log("------------------------------------------------");
            console.log("ND deliveryPayment[popPayment] : " + popPayment);
            console.log("ND deliveryPayment[popSalePayment] : " + popSalePayment);
            console.log("ND deliveryPayment[popLocalPayment] : " + popLocalPayment);
            console.log("ND deliveryPayment[popTotalPayment] : " + popTotalPayment); 
            // 조한두 2020.05.13
            /*
            $(".amountBox .amount1").text(addComma(deliveryAmount)); // 총 배송 금액
            $(".amountBox .amount2").text(addComma(rankPrice)); // 등급 할인 금액
            var total = deliveryAmount - rankPrice + popLocalPayment;
            //var addTax = total * 0.1;
            //var toalPlusTax = total + addTax ;
            var addTax = popLocalPaymentVat;
            var totalPlusTax = total + addTax;
            $(".amountBox .amount3_sum").text(addComma(total));  
            $(".amountBox .amount3_vat").text(addTax);
            $(".amountBox .amount3").text(addComma(totalPlusTax)); // 총결제 예정금액
            */
        } else {
            var payCode = getDeliveryPayment();      // *******************************************
            console.log("------------getDeliveryPayment------------");
            console.log("payCode:" + payCode);
            /*
            if(deliveryAmount > 0)
                popPayment = deliveryAmount;
            else
                popPayment = payCode.payment;       // 조한두
            if(rankPrice > 0)
                popSalePayment = rankPrice;
            else
                popSalePayment = payCode.salePayment;        // 조한두 
            */
            popPayment = payCode.payment;
            popSalePayment = payCode.salePayment;
            popTotalPayment = payCode.totalPayment;
            masterCode = payCode.masterCode;
            localData = payCode.localData;

            $(".amountBox .pickupService").hide();
            $(".amountBox .pickupService .amount4").text("0"); // 픽업 결제 금액
            $(".amountBox .pickupService .amount4_vat").text("0"); // 픽업 결제 금액
              
            $(".amountBox .amount1").text(addComma(popPayment-popSalePayment)); // 총 배송 금액
            $(".amountBox .amount2").text(addComma(popSalePayment)); // 등급 할인 금액
            $(".amountBox .amount3").text(addComma(popTotalPayment)); // 총 결제예정 금액
            $(".amountBox .amount3_sum").text(addComma(popTotalPayment)); // 총 결제예정 금액
            $(".amountBox .amount3_vat").text("0"); 
             
            // 조한두 2020.05.13
            /* 
            $(".amountBox .amount1").text(addComma(deliveryAmount)); // 총 배송 금액
            $(".amountBox .amount2").text(addComma(rankPrice)); // 등급 할인 금액
            var total = deliveryAmount - rankPrice;
            //var addTax = total * 0.1;
            //var toalPlusTax = total + addTax;
            var addTax = 0;
            var totalPlusTax = total;
            $(".amountBox .amount3_sum").text(addComma(total));  
            $(".amountBox .amount3_vat").text(addTax);
            $(".amountBox .amount3").text(addComma(totalPlusTax)); // 총결제 예정금액 
            */
        }
        
        if(masterCode.size == 1) {
            jsonData.masterCode = masterCode[0];
        }
         
        jsonData.payment = popPayment;
        jsonData.rankPrice = popSalePayment;
        jsonData.localPayment = popLocalPayment;
        jsonData.localPaymentVat = popLocalPaymentVat;
        jsonData.paymentTotal = popTotalPayment;
        jsonData.paymentCode = paymentCode;					// 국내택배 구분자용
        jsonData.paymentVat = paymentVat;					// 국내택배 부가세
        
        // 20.05.14.목 조한두 
        /*
        jsonData.payment = deliveryAmount;
        jsonData.rankPrice = rankPrice;
        jsonData.localPayment = popLocalPayment;
        jsonData.localPaymentVat = popLocalPaymentVat;
        jsonData.paymentTotal = totalPlusTax;
        */
        jsonData.masterCodeList = masterCode;
        if(localData != null && localData != ""){
            jsonData.localData = localData;
        }
        var payMethod = $(".pop_body input[name='paySelect']").val();
        if(payMethod == null || payMethod == "")
            payMethod = "bank";
        jsonData.payMethod = payMethod;  // 조한두 (20.05.12)
        console.log(jsonData)
        
        // String 형태로 변환
        //var jsonText = JSON.stringify(jsonData) ;
        jsonText = jsonData ;
        console.log(JSON.stringify(jsonText))
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
            var rankprice = $(this).data("rankprice");
            
            /////////////////////////////////////////////////////////////////////////
            // 계산
            payment += amount;
            salePayment += rankprice;
            console.log("amount:" + amount);
            console.log("rankprice:" + rankprice);
            console.log("origin:" + origin);
            console.log("courierCom:" + courierCom);
            console.log("courier:" + courierCom);
            console.log("mcode:" + mcode);
          
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
    		var mcode = $obj.data("mcode");
    		var amount = $obj.data("amount");
    		var rankprice = $obj.data("rankprice");
    		
    		console.log("amount:" + amount);
    		console.log("rankprice:" + rankprice);
    		console.log("origin:" + origin);
    		console.log("courierCom:" + courierCom);
    		console.log("courier:" + courierCom);
    		console.log("mcode:" + mcode);
    		
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
    	
    	totalPayment = payment - salePayment + localPayment + Math.round(localPayment * 0.1);
    	
    	return {
    		payment : payment
    		,salePayment : salePayment
    		,totalPayment : totalPayment
    		,localPayment : localPayment
    		,masterCode : masterCode 
    		,localData : localData 
    	};
    };
    
    // 국내택배 only
    var getDeliveryHomePayment = function() {
        var payment = 0;
        var salePayment = 0;
        var totalPayment = 0;
        var localPayment = 0;
        
        var masterCode = new Array();
        var localData = new Array();

        // 특송 요금 계산
        $(".pop_body input[name='pickChk']:checked").each(function(){
            /////////////////////////////////////////////////////////////////////////
//            var courierCom = $(this).data("couriercom");
            var mcode = $(this).data("mcode");
            var amount = $(this).data("amount");
            
            /////////////////////////////////////////////////////////////////////////
            // 계산
            localPayment += amount;
                    
            masterCode.push(mcode);
        })
        
        totalPayment = localPayment + (localPayment * 0.1);
        
        return {
            payment : payment
            ,salePayment : salePayment
            ,totalPayment : totalPayment
            ,localPayment : localPayment
            ,masterCode : masterCode 
            ,localData : localData 
        };
    };
    
    
    //------------------
    $(document).on("click",".pop_body .sendPayment",function(){
        console.log("-----sendPayment-----");
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
        
        if(proc) { // 결제 진행
            popPaymentProc();
        }
    });
    
    var popPaymentProc = function(){
        console.log("-----popPaymentProc-----");
        var url = "/payment/popup/popPaymentPaymentProc";
        // 조한두 20.05.20
        /*     
        console.log("masterCode:" + $("input[name='masterCode']").val());
        var jsonObj = $("input[name='masterCode']").val().replace("{","").replace("}","");
        var payMethod = $(".pop_body input[name='paySelect']").val();
        console.log("payMethod:" + payMethod);
        console.log("jsonObj.payMethod:" + jsonObj.payMethod);
        jsonObj.payMethod = payMethod;
        console.log("jsonObj:" + jsonObj);
        console.log("jsonText:" + jsonText);
        console.log("jsonText.payMethod:" + jsonText.payMethod);
        */
        var payMethod = $(".pop_body input[name='paySelect']").val();
        console.log("payMethod:" + payMethod);
        jsonText.payMethod = payMethod;
        jsonObj = jsonText;
        console.log(jsonObj)
        if(payMethod == "paypal")
            ajaxCall("post", url, jsonObj, paymentBankEnd);
        else
            ajaxCall("post", url, jsonObj, paymentEnd);    
    }
    
    var paymentEnd = function(data){
        if(data.errCode){
            //if(confirm( getMessage("alert.payment.sucess") )){
                if(data.confirmUrl != "" && data.confirmUrl != null){
                    var winpop = window.open(data.confirmUrl,"confirmUrl");
                    // 조한두 수정  (20.05.20) replace 로는 approveCharge 안됨!
                    //location.replace(data.confirmUrl);
                }
            //}
            
            search();
        }else{
            alert( getMessage("alert.payment.faild") );
        }
     }
    // 조한두 
    var paymentBankEnd = function(data){
        allClosePop();
        if(data.errCode == true){
            alert("Success (Firmbanking)");
            location.replace("/shipment");
        }else{
            alert( getMessage("alert.payment.faild") );
        }
     }
    
    //결제 승인창띄우기
    var popPaymentPopOpen = function(masterCode){
        var masterCodeList =  new Array;
        var payCnt = 0;
        var jsonData = new Object();
        allClosePop();
        console.log("1)masterCode:" + masterCode + " payCnt:" + payCnt);
        if(masterCode == ""){
            if(!$("input[name='chkmasterCode']").is(":checked")){
                 alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
                 return;
            }else{
                $('input:checkbox[name="chkmasterCode"]').each(function() {
                      /* if(this.checked){//checked 처리된 항목의 값
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
                                //팝업이 뜨고 프로세스 진행후에 this.value가 전체 코드로 들어가기때문에 masterCode를 뽑아오기위함 처리
                                if(jsonData.masterCode != "" && jsonData.masterCode != undefined) {
                                    jsonData.masterCode = "";
                                    jsonData.masterCodeList = this.value;
                                    console.log("2)jsonData.masterCode:" + jsonData.masterCode + " this.value:" + this.value + " jsonData.masterCodeList:" + jsonData.masterCodeList);
                                }else{
                                    jsonData.masterCode = this.value;
                                    console.log("3)jsonData.masterCode:" + jsonData.masterCode + " this.value:" + this.value );
                                }
                                payCnt++;
                           }else{
                                alert( getMessage("alert.payment.proc.accept") );    //기 결제성공인 주문이 선택되었습니다.
                                $(this).prop('checked',false);
                           }
                        }
                 });
            }
        }else{
            payCnt = 1;
        }
        var url = "/payment/popup/popPaymentPopOpen";
        console.log(jsonData);
        if(confirm( getMessage("alert.payment.accept") )){
            ajaxCall("post", url, jsonData, popPaymentPopOpenEnd);
        }
    }
    
    var popPaymentPopOpenEnd = function(data){
        if(data.errCode){
           
            if(data.confirmUrl != "" && data.confirmUrl != null){
                var winpop = window.open(data.confirmUrl,"confirmUrl");
               }
            search();
        }else{
            alert( getMessage("alert.payment.faild") );
        }

        
    }

    
    
    //----------------------------------------------------------