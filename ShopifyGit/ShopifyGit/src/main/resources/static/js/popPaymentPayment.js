    var masterCode = "${datail.masterCode}";
    var masterCodeList = "${datail.masterCodeList}";
    var amount = 0;
    var payment = 0;
    var paymentTotal = 0;
    var amount3 = 0;
    var paymentVat = 0;
    var deliveryAmount = $("#deliveryAmount").val();
    var rankPrice = $("#rankPrice").val();
    console.log("deliveryAmount>" + deliveryAmount);
    console.log("rankPrice>" + rankPrice);
    var deliveryAmountChk = false;
    var paymentVatChk = false;
    var rankPriceChk = false;
    var deliveryChk = 0;

    var initPopPaymentPayment = function (krCnt, code, name) {
    	
        if ( $('#krCnt').val() == 0 ) {
        	$('#trOverseas').show();
        	$('#trDiscount').show();
//        	$('#trLocal').show();
        }
    	
        var chkCnt = 0; //주문전체 체크 개수
        var $deliveryform = $('#deliveryform');
//        bindSelectAjax($deliveryform);  // 검색 select box 세팅
        bindSelectAjax($deliveryform, "", false);  // 검색 select box 세팅
        deliveryAmount = $("#deliveryAmount").val();
        rankPrice = $("#rankPrice").val();

        /* $(document).on("change",".box_size", function() {
            console.log("box_size==>");
            console.log(this);
            console.log(this.value);
            console.log(this.data("etc"));
        }); */
        
        
        //pashCash 결제하기
        $("label[for = 'bank' ]").click(function () {
            //alert("payCash");
            $("#payCash").prop("checked",true);
            $("#paySelect").val("bank");
            var payChoice1 = document.getElementById("payChoice");
            payChoice1.src = "/img/common/firm_banking.png";
        });

       //paypal 결제하기
       $("label[for = 'credit' ]").click(function () {
             //alert("paypal");
             $("#payPaypal").prop("checked",true);
             $("#paySelect").val("paypal");
             var payChoice2 = document.getElementById("payChoice");
             payChoice2.src = "/img/common/paypal.png";
       });
     
        
        //결제하기
        $(document).on("click",".btnPaymentProc",function() {
            var payChk = 0;
            var boxChk = true;

            
            if($("input[name='pickupService']").is(":checked")) {
                //국내 택배 픽업시 만들어진 레이어가 있는지 확인
                var payChkHtml = $("#tempPickup").html();
                //console.log("#######payChkHtml########");
                //console.log(payChkHtml);
                if(payChkHtml != null && payChkHtml != "" && payChkHtml != undefined) {
                    payChk++;
                }
            }else{
                payChk = $('input:checkbox[name="pickChk"]').length;    //픽업 체크 안했을때는 제품의 체크박스로 데이터 전달
                //console.log("#######payChk########1=>"+payChk);
                $('input:checkbox[name="pickChk"]').each(function() {   
                    if(this.checked){
                        //payChk++;
                    }else{
                        payChk--;
                    }
                });
                //console.log("#######payChk########2=>"+payChk);
            }
            
            //결제 픽업박스가 있다면 박스 확인
            $('.pick_list .box_size').each(function() {
                if($(this).val() == "") {
                    boxChk = false;
                }
            });
            if(payChk > 0) {
                if(boxChk){
                    btnPaymentProc();
                }else {
                    alert( getMessage("alert.checkboxChk.boxsize") ); // 박스사이즈를 선택해주세요
                }
            }else {
                alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
            }
                
        });
        
        //픽업서비스 체크
        /* $(document).on("click","#pick_service",function() {
            pickUpService();
        }); */
        
        //픽업서비스 추가
       /*  $(document).on("click",".addPickup",function() {
            var pickCnt = 0;
            var pickVal = "";
            var pickObj = $('#courier option:selected');
            pickVal = pickObj.val();
            if(pickVal == ""){
                alert( getMessage("alert.checkboxChk.service") ); //서비스 대상을 선택해주세요
                return;
            }
            addPickUpService(pickObj);
        }); */
        
        //추가된 픽업서비스 상세펼치기..
        $(document).on('click', '.masterCode',function(e) {
            //console.log(e.toElement.innerText);
            
            //console.log(e.toElement.innerText);
            var mcode = e.toElement.innerText;
            var $addpickDetail = eval($(".d"+mcode));
            var $addpickDetailOn = eval($(".p"+mcode));
            if($addpickDetail.is(':visible')) {
                $addpickDetail.hide();
                $addpickDetailOn.removeClass("on");
            }else{
                $(".addPickUpDetail").hide();
                $(".pickon").removeClass("on");
                
                $addpickDetail.show();
                $addpickDetailOn.addClass("on");
                //console.log("bbbbbbbbbbb");
                
                //console.log("ccccccccccc");
            }
        }); 
        
        if ( krCnt ) {
//        	$("input[name='pickup']").prop("checked", true);
//        	allCheck("pickChk", $("input[name='pickup']"));
        	
        	$("#deliveryform").hide();   
        	$("#blankLine").hide();
        	$(".pop_body .pickupService").hide();        	
//        	$("select[name='courier']").val(code);
//            var localService = $("select[name='courier'] option:selected").val();
//            var localServiceName = $("select[name='courier'] option:selected").text();
//            console.log("= localService     === " + localService);
//            console.log("= localServiceName === " + localServiceName);
//        	
//        	addLocalDeliveryList();
        }
        
        
       
    };
    
    var deliveryCheckBox = function(obj) {
    	alert('deliveryCheckBox');
        var masterCodeform = $("#masterCode").val();
        if(!$("input[name='pickupService']").is(":checked")) {
            rankPrice = 0;
        }
        if(obj.checked) {
            deliveryChk++;
            //deliveryAmount = $("#deliveryAmount").val();
            //paymentVat = $("#paymentVat").val();
            if(paymentVat == ""){
                paymentVat = 0;
            }
            if(payment == "") {
                payment = 0;
            }
            if(paymentTotal == "") {
                paymentTotal = 0;
            }
            if(deliveryAmount == ""){
                deliveryAmount = 0;
            }
            if(rankPrice == ""){
                rankPrice = 0;
            }
            payment         = parseInt(payment) + parseInt(obj.dataset.amount);
            console.log("######this.dataset.amount=>"+obj.dataset.amount);
            console.log("######paymentTotal=>"+paymentTotal);
            //paymentTotal  = parseInt(paymentTotal) - parseInt(deliveryAmount) - parseInt(paymentVat) + parseInt(rankPrice);
            paymentTotal    = parseInt(paymentTotal) + parseInt(obj.dataset.amount);
            console.log(paymentTotal);
            if(!rankPriceChk) {
                paymentTotal    = parseInt(paymentTotal) + parseInt(rankPrice)
                rankPriceChk = true;
            }
            /* if($("input[name='pickupService']").is(":checked")) {    
                if(!deliveryAmountChk) {
                    paymentTotal    = parseInt(paymentTotal) + parseInt(deliveryAmount);
                    deliveryAmountChk = true;
                }
                if(!paymentVatChk) {
                    if(deliveryAmount != null && deliveryAmount != "") {
                        paymentVat = parseInt(deliveryAmount) * 0.1;
                    }
                    paymentTotal    = parseInt(paymentTotal) + parseInt(paymentVat)
                    paymentVatChk = true;
                    $(".amount4").text(addComma(parseInt(deliveryAmount)+" + (VAT "+parseInt(paymentVat)+")")+getMessage("common.unit"));
                }
            } */
            $("#payment").val(payment);
            $("#paymentTotal").val(paymentTotal);
            $("#paymentVat").val(paymentVat);
            $(".amount1").text(addComma(payment)+getMessage("common.unit"));                //원
            $(".amount3").text(addComma(paymentTotal)+getMessage("common.unit"));       //원
        }else{
            deliveryChk--;
            if($("input[name='pickupService']").is(":checked")) {   //픽업서비스가 체크되어있으면 체크 해제시 초기화 처리
                if(confirm(getMessage("shipment.popup.payment.check.confirm"))){    //체크 해제시 픽업서비스가 초기화 됩니다. 해제하시겠습니까?
                    formClean();
                }else{  //픽업서비스가 체크되어있으면 체크해제 취소시 그대로 체크하도록 나둠
                    console.log("######111");
                    $(obj).parents("td").prop("checked",true);
                }
            }else{  //픽업서비스가 체크 안되어있으면 가격 부분 처리
                if(payment == "") {
                    payment = 0;
                }
                if(paymentTotal == "") {
                    paymentTotal = 0;
                }
                payment         = parseInt(payment) - parseInt(obj.dataset.amount);
                if(obj.dataset.amount == 0){
                    rankPrice = 0;
                }

                if(rankPriceChk) {
                    paymentTotal    = parseInt(paymentTotal) + parseInt(rankPrice)
                    rankPriceChk = false;
                }
                paymentTotal    = parseInt(paymentTotal) - parseInt(obj.dataset.amount);
                $("#payment").val(payment);
                $("#paymentTotal").val(paymentTotal);
                $(".amount1").text(addComma(payment)+getMessage("common.unit"));                //원
                $(".amount3").text(addComma(paymentTotal)+getMessage("common.unit"));       //원
            }
        }
    }
    
    //초기화
    var formClean = function() {
        payment = 0;
        paymentTotal = 0;
        amount3 = 0;
        deliveryAmountChk = false;
        rankPriceChk = false;
        paymentVatChk = false;  
        
        $("#payment").val("0");
        $("#paymentTotal").val("0");
        $(".amount1").text("0"+getMessage("common.unit"));              //원
        $(".amount3").text("0"+getMessage("common.unit"));      //원
        $("input[name='pickupService']").prop("checked",false);
        $(".pickChk").prop("checked",false);
        $("#pickup").prop("checked",false);
        $("#courier").val("");
        $("#deliveryCompany").val("");
        $("#deliveryCompanyCode").val("");
        $(".pickupService").hide();
        $("#pickupService").html("");
    }
    //결제하기
    var btnPaymentProc = function () {
        var $payfrm = $("#payfrm");
        var masterCodeList = "";
        var boxSizeList = "";
        var intC = 0;
        var courier = "";
        var courierCode = $("#deliveryCompanyCode").val();
        var courierArry = "";
        if(courier.indexOf(",") > 0){
            courierArry = courierCode.split(",");
        }
        if($("input[name='pickupService']").is(":checked")) {   //픽업서비스가 체크되어있으면 레이어에서 masterCode 가져옴
            //박스사이즈 담기
            $('.pick_list .box_size').each(function() {
                //console.log("vvvvvvvvvvvv");
                //console.log($(this).val());
                if($(this).val() == "") {
                } else {
                    if(boxSizeList == "") {
                        boxSizeList = $(this).val();
                    }else{
                        boxSizeList = boxSizeList + "," + $(this).val();
                    }
                }
            });
            //주문 masterCode 담기
            $('.pick_list > li').each(function() {
                $(this).find(".masterCode").each(function(i,v) {
                    if(masterCodeList == "") {
                        masterCodeList = v.innerHTML;
                    }else{
                        masterCodeList = masterCodeList + "," + v.innerHTML;
                    }
                });
                if(courierCode.indexOf(",") > 0){
                    courier = courierArry[intC];
                }
                paymentPopProc($payfrm,masterCodeList, boxSizeList, courier);
                intC++;
            });
            
        }else {     //픽업서비스가 체크 해제 되어있으면 주문 체크건으로 masterCode 가져옴
            $('input:checkbox[name="pickChk"]').each(function() {
                if(this.checked) {
                    if(masterCodeList == "") masterCodeList = this.dataset.mcode;
                    else masterCodeList = masterCodeList + "," + this.dataset.mcode;
                }
            });
            paymentPopProc($payfrm,masterCodeList,'','');
        }
    }
    
    //결제 ajax처리
    var paymentPopProc = function($payfrm,masterCodeList, boxSizeList, courier){
        $("#masterCode").val(masterCodeList);
        $("#boxSize").val(boxSizeList);
        $("#deliveryCompany").val(courier);
        console.log("1)popPaymentPayment-------------");
        console.log("$payfrm:" + $payfrm);  // 조한두
        if($("input[name='pickupService']").is(":checked")) {
            if ( $("#pickupService").is($("#pickupService").show()) ) {
                paymentProc($payfrm);
            }else { 
                alert(getMessage("alert.error.request.payment.service"));         //요청하신 서비스에 해당되는 택배사가 없습니다.
            } 
        }else{
            paymentProc($payfrm);   
        }
    }
    
    //픽업서비스 체크
    var pickUpService = function() {
        if($("input[name='pickupService']").is(":checked")){
            $(".pickupService").show();
            $(".amountBox").height("241px");
        }else{
            if(confirm(getMessage("shipment.popup.payment.check.confirm"))){    //체크 해제시 픽업서비스가 초기화 됩니다. 해제하시겠습니까?
                /* $("#payment").val("0");
                $("#paymentTotal").val("0");
                $(".amount1").text("0"+getMessage("common.unit"));              //원
                $(".amount3").text("0"+getMessage("common.unit"));      //원
                $("input[name='pickupService']").prop("checked",false);
                $(".pickChk").prop("checked",false);
                $("#pickup").prop("checked",false);
                $("#courier").val("");
                $("#deliveryCompany").val("");
                $("#deliveryCompanyCode").val("");
                $(".pickupService").hide();
                $("#pickupService").html("");
                payment = 0;
                paymentTotal = 0;
                amount3 = 0;
                deliveryAmountChk = false;
                rankPriceChk = false;
                paymentVatChk = false;
                //console.log("######1122this.dataset.amount=>"+this.dataset.amount);
                console.log("######2222paymentTotal=>"+paymentTotal); */
                formClean();
            }

        }
    }
    
    //픽업서비스 추가
    var addPickUpService = function(obj) {
        var masterCodeAry = new Array();
        var masterCode = "";
        var masterCodeList = "";
        var pickCnt = 0;
        var deliveryCompanyCode = "";
        var deliveryCompanyList = "";
        $('input:checkbox[name="pickChk"]').each(function() {
            if(this.checked){
                //console.log("pickChk");
                //console.log(this.dataset.mcode);
                if(masterCodeList == ""){
                    masterCodeList = this.dataset.mcode;
                }else{
                    masterCodeList = masterCodeList + "," + this.dataset.mcode;
                }
                if(deliveryCompanyList == ""){
                    deliveryCompanyList = obj.val();
                }else{
                    deliveryCompanyList = deliveryCompanyList + "," + obj.val();
                }
                masterCodeAry.push(this.dataset.mcode);
                pickCnt++;
            }
        });
        var type = "post";
        var url = "/shipment/popup/apiPaymentShipment";
        
        if(pickCnt >= 1 && masterCodeList != "" ){
            masterCode = masterCodeList;
            masterCodeList = "";
            deliveryCompanyCode = deliveryCompanyList;
            deliveryCompanyList = "";
        }else{
            alert( getMessage("alert.checkboxChk") ); // 대상을 선택해주세요
            return;
        }
        var cComp = obj.val();
        var param = {
                masterCode : masterCode
                ,courier : cComp
                ,courierChk : 'search'
        }
        $("#masterCode").val(masterCode);
        $("#deliveryCompanyCode").val(deliveryCompanyCode);
        var addFlag = true;
        $.each(masterCodeAry, function(index, value){
            $.each($("#pickupService .masterCode"), function(i,v){
                if(v.innerText == value){
                    addFlag = false;
                }
            });
        });
        if(addFlag){
            ajaxCall(type, url, param, addPickUpCallBack);
        }
        
    }
    
    var addPickUpCallBack = function(data) {
        var aryData = data.list;
        if(aryData != undefined){
            if(aryData.length > 0){
                $.each(aryData, function (index, el) {
                    var tmp;
                    var pickObj = $('#courier option:selected');
                    var pickService = pickObj.text();
                    var pickSelect = $("#boxSize").html();
                    if(index == 0) {
                        tmp = $("#tempPickup").html();
                        tmp = tmp.replace("@pickup.select@",pickSelect);
                    }else{
                        tmp = $("#tempPickupDiv").html();
                    }
                    tmp = tmp.replace("@pickup.service@",pickService);
                    tmp = tmp.replace("p@pickup.masterCode@","p"+el.masterCode);
                    tmp = tmp.replace("d@pickup.masterCode@","d"+el.masterCode);
                    tmp = tmp.replace("@pickup.masterCode@",el.masterCode);
                    tmp = tmp.replace("@pickup.buyerLastname@",el.buyerLastname);
                    tmp = tmp.replace("@pickup.buyerFirstname@",el.buyerFirstname);
                    tmp = tmp.replace("@pickup.courier@",el.courier);
                    tmp = tmp.replace("@pickup.payment@",el.paymentStr);
                    tmp = tmp.replace("@pickup.orderDate@",el.orderDate);
                    tmp = tmp.replace("@pickup.goods@",el.goods);
                    tmp = tmp.replace("@pickup.masterCode@",el.masterCode);
                    
                    //$("#pickupService").append(tmp);
                     if(index == 0) {
                        $("#pickupService").append(tmp);    
                    }else{
                        $(".subDiv").append(tmp);
                    }
                });
                
                if(deliveryChk > 0) {   
                    if(!deliveryAmountChk) {
                        paymentTotal    = parseInt(paymentTotal) + parseInt(deliveryAmount);
                        deliveryAmountChk = true;
                    }
                    if(!paymentVatChk) {
                        if(deliveryAmount != null && deliveryAmount != "") {
                            paymentVat = parseInt(deliveryAmount) * 0.1;
                        }
                        paymentTotal    = parseInt(paymentTotal) + parseInt(paymentVat);
                        paymentVatChk = true;
                        $(".amount4").text(addComma(parseInt(deliveryAmount)+" + (VAT "+parseInt(paymentVat)+")")+getMessage("common.unit"));
                        paymentTotal    = parseInt(paymentTotal) - parseInt(payment);
                        $("#payment").val(payment);
                        $("#paymentTotal").val(paymentTotal);
                        $(".amount1").text(addComma(payment)+getMessage("common.unit"));                //원
                        $(".amount3").text(addComma(paymentTotal)+getMessage("common.unit"));       //원
                    }
                }
                
            }
        }else{
            alert(getMessage("alert.error.search.service"));   //검색된 서비스가 없습니다.
        }
        
    }
    
    var getDeliveryAmount = function(masterCode) {
        var type = "post";
        var url = "/shipment/popup/popPaymentDeliveryAmount";
        var param = {
                weightTotal : weightTotal
                ,courier : courier
                ,courierCompany : courierCompany
                ,origin : origin
        }
        ajaxCall(type, url, param, getDeliveryAmountCallBack);
    }
    var getDeliveryAmountCallBack = function(data) {
        var deliveryAmount = data.deliveryAmount;
        var deliveryAmountVat = data.deliveryAmountVat;
    }
    
 
