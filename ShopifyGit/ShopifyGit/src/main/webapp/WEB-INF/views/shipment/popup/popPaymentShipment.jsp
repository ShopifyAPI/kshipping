<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%> 
<script type="text/javascript">
    
    
    

    var masterCode = "${datail.masterCode}";
    var masterCodeList = "${datail.masterCodeList}";
    var amount = 0;
    var payment = 0;
    //var deliveryAmount = 0;
    //var rankPrice = 0;
    var paymentTotal = 0;
    var amount3 = 0;
    var paymentVat = 0;
    var deliveryAmount = $("#deliveryAmount").val();
    var rankPrice = $("#rankPrice").val();
    
    var deliveryAmountChk = false;
    var paymentVatChk = false;
    var rankPriceChk = false;
    var deliveryChk = 0;

    $(function () {
        var chkCnt = 0; //주문전체 체크 개수
        var $deliveryform = $('#deliveryform');
        bindSelectAjax($deliveryform);  // 검색 select box 세팅
        deliveryAmount = $("#deliveryAmount").val();
        rankPrice = $("#rankPrice").val();

        /* $(document).on("change",".box_size", function() {
            console.log("box_size==>");
            console.log(this);
            console.log(this.value);
            console.log(this.data("etc"));
        }); */
        
        
        
        
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
        
    });
    
    var deliveryCheckBox = function(obj) {
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

 
</script>
                <div class="pop_head">
                    <h3><spring:message code="order.popup.delivary" text="배송정보" /></h3>
                    <button type="button" class="btn_close"></button>
                </div>
                <div class="pop_body">
                        <div class="tit_wrap mt0">
                            <h4 class="sub_h4"><spring:message code="incgnb.shipment" text="배송" /></h4>
                            <div class="sect_right">
                                <p class="bd_count"><spring:message code="common.unit.total" text="총" /> ${listCnt}<spring:message code="common.unit.case" text="건" /></p>
                            </div>
                        </div>
                        <div class="pick_service" id="deliveryform">
                            <input type="checkbox" id="pick_service" name="pickupService"><label for="pick_service"><span class="icon_ck"></span><span class="label_txt"><spring:message code="shipment.popup.payment.pickup" text="택배 픽업 서비스" /></span></label>
                            <div class="right pickupService" style="display:none">
                                <div id="boxSize" style="display:none"><select class="select-ajax box_size" name="box_size" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "A170000" }' ></select></div>
                                <select class="select-ajax" name="courier" id="courier" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B010000" }'></select>
                                <button type="button" class="btn_type2 addPickup"><spring:message code="button.add" text="추가"/></button>
                            </div>
                        </div>
                        <div class="h_scroll" style="height:auto">
                        <c:choose>
                        <c:when test="${fn:length(list) == 0}">
                           <div class="nolist"><spring:message code="settings.nolist" text="등록된 정보가 없습니다." /></div>
                        </c:when>
                        <c:otherwise>
                            <table class="tbtype">
                                <colgroup>
                                    <col class="wp35">
                                    <col class="wp150">
                                    <col class="wp100">
                                    <col class="cperauto">
                                    <col class="wp100">
                                    <col class="wp150">
                                    <col class="wp100">
                                    <col class="wp80">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th><input type="checkbox" id="pickup" name="pickup"><label for="pickup"><span class="icon_ck"></span></label></th>
                                        <th><spring:message code="shipment.shipmentCode" text="배송번호"/></th>
                                        <th><spring:message code="order.orderDate" text="주문일자"/></th>
                                        <th><spring:message code="order.productName" text="상품명"/></th>
                                        <th><spring:message code="order.orderInfo" text="주문자 정보"/></th>
                                        <th><spring:message code="admin.statis.sales.title5" text="특송서비스"/></th>
                                        <th><spring:message code="order.paymentAmount" text="결제 금액"/></th>
                                        <th><spring:message code="settings.priceCurrency" text="화폐단위" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${list}" var="item" varStatus="status">
                                    <tr>
                                        <td class="t_center">
                                            <input type="checkbox" name="pickChk" class="pickChk" 
                                                data-origin="${item.origin}" 
                                                data-courierCom="${item.courierCompany}" 
                                                data-courier="${item.courier}" 
                                                data-mcode="${item.masterCode}" 
                                                data-amount="${item.payment}"
                                                data-goods="${item.goods}"
                                                data-orderdate="${item.orderDate}"
                                                data-rankprice="${item.rankPrice}" id="pickup_${item.masterCode}">
                                            <label for="pickup_${item.masterCode}"><span class="icon_ck" ></span></label>
                                        </td>
                                        <td class="t_center"><p>${item.masterCode}</p></td>
                                        <td class="t_center"><p>${item.orderDate}</p></td>
                                        <td><p>${item.goods}</p></td>
                                        <td class="t_center"><p>${item.buyerLastname} ${item.buyerFirstname}</p></td>
                                        <td class="t_center"><p>${item.courier}</p></td>
                                        <td class="t_right"><p>${item.paymentStr}</p></td>
                                        <td class="t_center"><p>KRW</p></td>
                                    </tr>
                                </c:forEach>   
                                </tbody>
                            </table>
                            </c:otherwise>
                        </c:choose>
                        </div>
                        <p>&nbsp;</p>
                        <ul class="pick_list pickupService" id="pickupService" style="display:none"></ul> <!-- 픽업 박스 보이는 곳 -->
                        <div id="tempLocalHtml" style="display:none">
                            <li data-localcode="#localService#">
                                <h4 class="sub_h4">#localServiceName#</h4>
                                <div class="h_scroll subDiv">
                                    <c:set var="boxsize"><spring:message code="shipment.popup.payment.boxsize" text="박스사이즈" /></c:set>
                                    <b>${boxsize} :</b>  
                                    <select class="box_size" name="box_size" id="B010020" style="display:none">
                                        <option value="" data-price="0" data-comcode="">== ${boxsize} ==</option>
                                    <c:forEach items="${lglPickup}" var="item" varStatus="status" >
                                        <option value="${item.zone}" data-price="${item.price}" data-comcode="${item.id}">${item.codeName}</option>
                                    </c:forEach>
                                    </select>
                                    <select class="box_size" name="box_size" id="B010010" style="display:none">
                                        <option value="" data-price="0" data-comcode="">== ${boxsize} ==</option>
                                    <c:forEach items="${postPickup}" var="item" varStatus="status">
                                        <option value="${item.zone}" data-price="${item.price}" data-comcode="${item.id}">${item.codeName}</option>
                                    </c:forEach>
                                    </select>
                                    <div class="deliveryAreaZone">
                                    </div>
                                </div>
                            </li>
                        </div>
                        <div id="tempDeliveryHtml" style="display:none">
                                <div class="pickon" data-mastercode="#masterCode#">
                                    <table class="tbtype">
                                    <colgroup>
                                        <col class="cper2000">
                                        <col class="cperauto">
                                        <col class="cper2000">
                                        <col class="cperauto">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th><spring:message code="shipment.shipmentCode" text="배송번호" /></th>
                                            <td colspan="3" onclick="viewDetail(this)" class="list-edit"><a href="#" onclick="return false;">#masterCode#</a></td>
                                        </tr>
                                    <tbody class="off pick-off">
                                        <tr>
                                            <th><spring:message code="shipment.productName" text="상품명" /></th>
                                            <td colspan="3"><p>#goods#</p></td>
                                        </tr>
                                        <tr>
                                            <th><spring:message code="shipment.popup.delivery.price" text="배송비" /></th>
                                            <td><p>#price#</p></td>
                                            <th><spring:message code="order.orderDate" text="주문일자" /></th>
                                            <td><p>#orderDate#</p></td>
                                        </tr>
                                    </tbody>
                                    </thead>
                                    </table>
                                </div>
                            </div>
                            
                        <form name="payfrm" id="payfrm" method="post">
                        <input type="hidden" id="masterCode" name="masterCode" value="${detail.masterCode}"> <!-- 222,2222 -->
                        <input type="hidden" id="masterCodeList" name="masterCodeList" value="${detail.masterCode}"> <!-- 222,2222 -->
                        <input type="hidden" id="payment" name="payment" value="${detail.payment}">  <!-- payment -->
                        <input type="hidden" id="deliveryAmount" name=deliveryAmount value="3000">   <!-- localPayment -->
                        <input type="hidden" id="paymentVat" name="paymentVat">                 <!-- popLocalPaymentVat -->
                        <input type="hidden" id="rankPrice" name="rankPrice" value="${detail.rankPrice}">  <!--salePayment-->
                        <input type="hidden" id="paymentTotal" name="paymentTotal" value="${detail.paymentTotal}"> <!--totalPayment-->
                        <input type="hidden" id="deliveryCompany" name="deliveryCompany">    <!-- 택배 111,1111 -->
                        <input type="hidden" id="deliveryCompanyCode" name="deliveryCompanyCode">  <!-- 택배 111,1111 -->
                        <input type="hidden" id="boxSize" name="boxSize">                      <!-- 택배 A,B -->
                        <div class="wrap_col cper100p mt45">
                            <div class="colA">
                                <h4 class="sub_h4"><spring:message code="shipment.list.payment.type" text="결제수단" /></h4>
                                <div class="paybox">
                                    <div class="boxbg">
                                        <input type="radio" id="payPaypal" name="payMethod" checked><label for="credit"><span class="icon_ra"></span><span class="label_txt bold"><spring:message code="shipment.popup.payment.method.paypal" text="페이팔" /></span></label>
                                        <span style="display:none">
                                        <input type="radio" id="payCash" name="payMethod"><label for="bank" class=" ml20"><span class="icon_ra"></span><span class="label_txt bold"><spring:message code="shipment.popup.payment.method.cash" text="무통장 입금" /></span></label>
                                        <input type="radio" id="payEtc" name="payMethod"><label for="paypal" class=" ml20"><span class="icon_ra"></span><span class="label_txt bold"><spring:message code="shipment.popup.payment.method.etc" text="기타" /></span></label>
                                        </span>
                                    </div>
                                    <dl class="t_center mt20">
                                        <img src="/img/common/paypal.png" width="317" height="54" />
                                    </dl>
                                    
                                    <%-- <dl>
                                        <dt><spring:message code="shipment.popup.payment.select.card" text="카드선택" /></dt>
                                        <dd><select><option>카드를 선택하세요.</option></select></dd>
                                    </dl>
                                    <dl>
                                        <dt><spring:message code="shipment.popup.payment.select.installment.period" text="할부 기간" /></dt>
                                        <dd><select><option>일시불</option></select></dd>
                                    </dl> --%>
                                </div>
                            </div>
                            <div class="colB">
                                <h4 class="sub_h4"><spring:message code="shipment.popup.payment.total.price" text="결제 내역" /></h4>
                                <table class="tbtype mt10">
                                    <colgroup>
                                        <col class="cper2500">
                                        <col class="cper2500">
                                        <col class="cper2500">
                                        <col class="cper2500">
                                    </colgroup>
                                <thead>
                                    <tr>
                                        <th><strong><spring:message code="shipment.payment.part" text="구분" /></strong></th>
                                        <th><strong><spring:message code="shipment.payment.price" text="금액" /></strong></th>
                                        <th><strong><spring:message code="admin.statis.total.subTitle4" text="부가세" /></strong></th>
                                        <th><strong><spring:message code="shipment.payment.total" text="합계" /></strong></th>
                                    </tr>
                                </thead>
                                <tbody class="paybox amountBox">
                                    <tr>
                                        <td><spring:message code="shipment.popup.payment.total.price.delivery" text="해외특송요금" /></td>
                                        <td class="t_right"><span class="cost amount1">0</span></td>
                                        <td class="t_right"><span class="cost">0</span></td>
                                        <td class="t_right"><span class="cost amount1">0</span></td>
                                    </tr>
                                    <tr>
                                        <td><spring:message code="shipment.popup.payment.total.price.discount" text="등급할인요금" /></td>
                                        <td class="t_right" style="color:red">-<span class="cost amount2">0</span></td>
                                        <td class="t_right" style="color:red"><span class="cost">0</span></td>
                                        <td class="t_right" style="color:red">-<span class="cost amount2">0</span></td>
                                    </tr>
                                    <tr class="pickupService" style="display:none">
                                        <td><spring:message code="shipment.popup.payment.total.price.pickup" text="픽업 택배요금" /></td>
                                        <td class="t_right"><span class="cost amount4">0</span></td>
                                        <td class="t_right"><span class="cost amount4_vat">0</span></td>
                                        <td class="t_right"><span class="cost amount4_tot">0</span></td>
                                    </tr>
                                    <tr class="totalServicePayment">
                                        <td><strong><spring:message code="shipment.popup.payment.total.price.plan" text="총결제금액" /></strong></td>
                                        <td class="t_right"><strong><span class="cost amount3_sum">0</span></strong></td>
                                        <td class="t_right"><strong><span class="cost amount3_vat">0</span></strong></td>
                                        <td class="t_right"><strong><span class="cost amount3">0</span></strong></td>
                                    </tr>
                                </tbody>
                                </table>
                                
                                
                                <%-- <div class="paybox amountBox">
                                    <ul>
                                        <li>
                                            <span><spring:message code="shipment.popup.payment.total.price.delivery" text="총 배송 금액" /></span>
                                            <span class="cost amount1">0<spring:message code="common.unit" text="원" /></span>
                                        </li>
                                        <li class="pickupService" style="display:none">
                                            <span><spring:message code="shipment.popup.payment.total.price.pickup" text="픽업 결제 금액" /></span>
                                            <span class="cost amount4">0<spring:message code="common.unit" text="원" /></span>
                                        </li>
                                        <li>
                                            <span><spring:message code="shipment.popup.payment.total.price.discount" text="등급 할인 금액" /></span>
                                            <span class="cost amount2">0<spring:message code="common.unit" text="원" /></span>
                                        </li>
                                        <li>
                                            <span><spring:message code="shipment.popup.payment.total.price.plan" text="총 결제예정 금액" /></span>
                                            <span class="cost amount3">0<spring:message code="common.unit" text="원" /></span>
                                        </li>
                                    </ul>
                                </div> --%>
                                
                                
                                
                                
                            </div>
                        </div>
                        </form>
                    <div class="pop_btn">
                        <!-- <button type="button" class="btn_type2 btnPaymentProc sendPayment">결제</button> -->
                        <button type="button" class="btn_type2 sendPayment">결제</button>
                    </div>
                </div>