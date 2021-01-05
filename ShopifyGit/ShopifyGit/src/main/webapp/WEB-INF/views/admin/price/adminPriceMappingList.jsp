<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/incSys.jsp"%>
 
<!-- 
**************************************************
화면코드 :UI_SYS_321_BAS
기능설명 : AMD 요금 맵핑 관리
Author   Date      Description
 김윤홍     2020-01-20  First Draft
**************************************************
 -->
<!DOCTYPE html>
<html lang="ko">
  <head>
    <%// header include %>
    <%@ include file="/WEB-INF/views/common/incAdmHeader.jsp"%> 

    <title>요금 맵핑 관리 조회</title>
    
    <script type="text/javascript">
    
    // READY
	$(function() {
		initialize();
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
		
		var gbn = $("#gbn").val();
		//alert('gbn:' + gbn);
		if(gbn == "fees"){
			$("#sale").removeClass("on");
			$("#fees").addClass("on");
		}else{
			$("#fees").removeClass("on");
			$("#sale").addClass("on");
		}
				
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
 
		
		//팝업 배송사 select Box 변경시 배송서비스 select Box data set
		$(document).on("change","#shipCompany",function(){
			//배송사 코드
			var shipCompany = this.value;
			alert('shipCompany:' + shipCompany);
			var codeGroup = "";
			//우체국
			if(shipCompany == "B010010"){
				codeGroup = "B020000";
			}else if(shipCompany == "B010020"){
				codeGroup = "B040000";
			}else if(shipCompany == "B010030"){
				codeGroup = "B140000";
			}else if(shipCompany == "B010040"){
				codeGroup = "B170000";
			}
			else{
				//콤보박스 초기화
				$("#deliveryService").empty();
				var option = $("<option value = 'B040000'>== 배송 서비스 ==</option>");
	            $('#deliveryService').append(option);
	            return;
			}
			
			sendServerPop(codeGroup);
		});
		// 테스트 요금 조회 
		$(document).on("click","#btn_search",function(){
			//alert("search");
			sendSearch();
		});
		// 공시요금 클릭 
		$(document).on("click","#fees",function(){
            sendList("fees");
        });
		// 매입요금 클릭 
		$(document).on("click","#sale",function(){
            sendList("sale");
        });
		
		//배송서비스 select Box 변경시 재조회
		$("#partDeliveryService, #nowDate").on("change",function(){
			
			var nowDate = $("#nowDate").val();
			var zoneCodeGroup = $("#partDeliveryService option:selected").val();
			var shipCompany = $("#partShipCompany option:selected").val();
			
			if(gbn == "fees"){
				
				location.href = "/admin/price/feesPriceMappingList?zoneCodeId="+shipCompany+"&zoneCodeGroup="+zoneCodeGroup+"&nowDate="+nowDate;
				
			}else if(gbn == "sale"){
				
				location.href = "/admin/price/salePriceMappingList?zoneCodeId="+shipCompany+"&zoneCodeGroup="+zoneCodeGroup+"&nowDate="+nowDate;
				
			}else{
				
				location.href = "/admin/price/feesPriceMappingList";
				
			}
			
		});
		
		//등록버튼 클릭시
		$(document).on("click","#btn_write",function(){
			
            var shipCompany = $("select[name='partShipCompany'] option:selected").val();
			var code = $("select[name='partDeliveryService'] option:selected").val();
            
			var param = "shipCompany=" + shipCompany + "&code=" + code + "";
            
			if(gbn=="fees"){
				openPop("/admin/popup/feesPriceNewPop?" + param, "", "510", openf, popupSelectBind, closef);	
			}else if(gbn=="sale"){
				openPop("/admin/popup/salePriceNewPop?" + param, "", "510", openf, popupSelectBind, closef);
			}else{
				location.href = "/admin/price/feesPriceMappingList";
			}
	    	
	    });
		
		/*팝업관련*/
		var openf = function(){
	        //alert("open");
	    }
	    
	    var funcf = function(){
	    	//컴퍼넌트 데이타 세팅

	    }
	    
	    var closef = function(){
	        //alert("close");
	    }
	    
		 // 엑셀업로드 팝업 select box 세팅
	    var popupSelectBind = function () {
			 
			 if(gbn == "fees"){
				 var $submitform = $('#feesPrice');
				 bindSelectAjax($submitform);
			 }else{
				 var $submitform = $('#salePrice');
				 bindSelectAjax($submitform);
			 }
			 
			 var shipCompany = $(".pop_body select[name='shipCompany']").data("code");
			 
			 if(shipCompany == "B010010"){
		            codeGroup = "B020000";
		        }else if(shipCompany == "B010020"){
		            codeGroup = "B040000";
		            
		        }else if(shipCompany == "B010030"){
					codeGroup = "B140000";
		        }else if(shipCompany == "B010040"){
					codeGroup = "B170000";
				}
		        else{
		            //콤보박스 초기화
		            $("#partDeliveryService").empty();
		            var option = $("<option value = 'B040000'>== 배송 서비스 ==</option>");
		            $('#partDeliveryService').append(option);
		            return;
		        }
			 
			 sendServerPop(codeGroup);
	    }
		 
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
	    
	    //공시요금 팝업 저장버튼 클릭시
	    $(document).on("click","#btn_write_pop_fees",function(){
	    	
	    	var $submitform = $('#feesPrice');
	           
            // input type Validation & submit
            if(!fnValidationCheckForInput($submitform)){
            	return;
            }
	    	
	    	//배송사
	    	var shipCompany = $("#shipCompany").val();
            if(shipCompany == "B010000" || shipCompany == ""){
            	alert(getMessage("price.deliveryCompany")+getMessage("alert.selectCheck"));
            	return;
            }
            
            //배송서비스
            var deliveryService = $("#deliveryService").val();
            if(deliveryService == "B010020" || deliveryService == "B010010" || deliveryService == 'B010030' || deliveryService == 'B010040' || deliveryService == ""){
            	alert(getMessage("price.deliveryService")+getMessage("alert.selectCheck"));
            	return;
            }
            
	    	//파일
            var file_up = $("#file_up").val();
	    	if(file_up == null || file_up == ""){
	    		
	    		alert(getMessage("price.addFile")+getMessage("alert.selectCheck"));//첨부파일을 선택해주세요.
	    		return;
	    		
	    	}
	    	
	    	var options = {
                    dataType:"text",//결과
                    success:function(data){
                    	console.log(data)
                        alert(getMessage("alert.proc.end", ["price.ExcelUpload"]));  // 엑셀업로드가 완료 되었습니다.
	    	 			location.replace("/admin/price/feesPriceMappingList");
                    },beforeSend:function(){
                        $('.spinner').removeClass('spinner');
                        $('.spinner').addClass('spinner-dimmed');
                        $('.loading').addClass('loading');
                    },complete:function(){
                    	$('.spinner').removeClass('spinner-dimmed');
                        $('.spinner').addClass('spinner-show');
                    },error:function(e){                   	
                    	console.log(e)
                    	alert(getMessage("alert.proc.err", ["price.ExcelUpload"]));  // 등록 중 오류가 발생했습니다.
                    	location.replace("/admin/price/feesPriceMappingList");
                    }
                };
	    	
            $("#feesPrice").ajaxForm(options).submit();
	    });
	    
	    
	    //할인요금 팝업 저장버튼 클릭시
	    $(document).on("click","#btn_write_pop_sale",function(){
	    	
	    	var $submitform = $('#salePrice');
	           
            // input type Validation & submit
            if(!fnValidationCheckForInput($submitform)){
            	return;
            }
	    	
	    	//배송사
	    	var shipCompany = $("#shipCompany").val();
            if(shipCompany == "B010000" || shipCompany == ""){
            	alert(getMessage("price.deliveryCompany")+getMessage("alert.selectCheck"));
            	return;
            }
            
            //배송서비스
            var deliveryService = $("#deliveryService").val();
            if(deliveryService == "B010020" || deliveryService == "B010010" || deliveryService == "B010030" ||deliveryService == "B010040" ||deliveryService == ""){
            	alert(getMessage("price.deliveryService")+getMessage("alert.selectCheck"));
            	return;
            }
	    	
            //파일
            var file_up = $("#file_up").val();
	    	if(file_up == null || file_up == ""){
	    		alert(getMessage("price.addFile")+getMessage("alert.selectCheck"));//첨부파일을 선택해주세요.
	    		return;
	    		
	    	}
	    	
// 	    	 $('#salePrice').submit();

	    	var options = {
                    dataType:"text",//결과
                    success:function(data){
                    	console.log("ok")
                        alert(getMessage("alert.proc.end", ["price.ExcelUpload"]));  // 엑셀업로드가 완료 되었습니다.
	    	 			location.replace("/admin/price/salePriceMappingList");
                    },beforeSend:function(){
                        $('.spinner').removeClass('spinner');
                        $('.spinner').addClass('spinner-dimmed');
                        $('.loading').addClass('loading');
                    },complete:function(){
                    	$('.spinner').removeClass('spinner-dimmed');
                        $('.spinner').addClass('spinner-show');
                    },error:function(e){                   	
                    	alert(getMessage("alert.proc.err", ["price.ExcelUpload"]));  // 등록 중 오류가 발생했습니다.
                    	location.replace("/admin/price/salePriceMappingList");
                    }
                };
	    	
            $("#salePrice").ajaxForm(options).submit();
	    	
	    });

	    var setSelectBoxPop = function(data){
	    	//콤보박스 초기화
			$("#deliveryService").empty();
			var code = $(".pop_body select[name='deliveryService']").data("code");

			for(var i = 0; i < data.length; i++){
			    var option = "";
                
                if(i == 0) {
                    option = $("<option value = ''>== "+data[i].codeKname+" ==</option>");
                } else {
                	if(code == data[i].codeEtc) {
                        option = $("<option value = '"+data[i].codeEtc+"' selected>"+data[i].codeKname+"</option>");    
                    } else {
                        option = $("<option value = '"+data[i].codeEtc+"'>"+data[i].codeKname+"</option>");
                    }
                }
                console.log("option : " + option);
	            $('#deliveryService').append(option);
				
			}
	    	
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
	    }
		
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
 
	// ***************************************************************
	   var sendList = function(choice){
	    var nowDate = $("#nowDate").val();
        var zoneCodeGroup = $("#partDeliveryService option:selected").val();
        var shipCompany = $("#partShipCompany option:selected").val();
        console.log("nowDate:" + nowDate);    
        console.log("shipCompany:" + shipCompany);
        console.log("code:" + zoneCodeGroup);
 
        if(choice == 'fees')
        	location.href = "/admin/price/feesPriceMappingList?zoneCodeId="+shipCompany+"&zoneCodeGroup="+zoneCodeGroup+"&nowDate="+nowDate;
        else
        	location.href = "/admin/price/salePriceMappingList?zoneCodeId="+shipCompany+"&zoneCodeGroup="+zoneCodeGroup+"&nowDate="+nowDate;
    }
	
	var sendSearch = function(shipCompany){
		var codeGroup = "";
		var shipCompany = $("select[name='partShipCompany']").data("code");
        var code = $("select[name='partDeliveryService']").data("code");
		var country = $("#submitform input[name='country']").val();
		var weight = $("#submitform input[name='weight']").val();
		if(shipCompany == "B010010"){
            codeGroup = "B020000";
        }else if(shipCompany == "B010020"){
            codeGroup = "B040000";
        }
        else if(shipCompany == "B010030"){
			codeGroup = "B140000";
        }else if(shipCompany == "B010040"){
			codeGroup = "B150000";
		}
		console.log("shipCompany:" + shipCompany);
		console.log("codeGroup:" + codeGroup);
	    console.log("code:" + code);
	    console.log("country:" + country);
	    console.log("weight:" + weight);
	    var parameter  = {
        		shipCompany: shipCompany
        		,code : code
        		,codeGroup : codeGroup
                ,country : country
                ,weight : weight
            };
        
        ajaxCall("post", "/admin/price/priceSearch", parameter , priceSelect); 
    }
	
 
	
	var priceSelect = function(data) {
		console.log("priceSelect-------------");
		var price = data.price;
		
		console.log("feesPrice:" + price.feesPrice);
		console.log("salesPrice:" + price.salesPrice);
		$("#submitform input[name='fees']").val(price.feesPrice);
		$("#submitform input[name='sales']").val(price.salesPrice);
	};
	
	
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
 
    </script>
</head>
<div class="wrap">
        <%// gnb include %>
        <%@ include file="/WEB-INF/views/common/incAdmGnb.jsp"%>
        <div class="contents">
           <div class="cont_body">
               <article>
               		<div class="spinner">
					    <div class="loading"><img src="/img/common/loading.gif"></div>
					    <div class="dimmed"></div>
					</div>    
                    <form name="submitform" id="submitform" method="get">
                    <input type="hidden" id="gbn" value="${gbn}" /> 
                   	<h2><spring:message code="price.priceMappingMgt" text="요금 매핑 관리"/></h2>
                    <ul class="tab_conts">
                        <li>
                            <div class="module_group pt0">
                                <div class="sub_tab">
                               	 	<!--  
                               	 	<button id="fees" onclick="location.href='/admin/price/feesPriceMappingList'" type="button" class="on"><spring:message code="price.feesPrice" text="공시요금" /></button>
                        			<button id="sale" onclick="location.href='/admin/price/salePriceMappingList'" type="button" ><spring:message code="price.salePrice" text="매입요금" /></button>
                                    -->
                                    <button id="fees" type="button" class="on"><spring:message code="price.feesPrice" text="공시요금" /></button>
                                    <button id="sale" type="button" ><spring:message code="price.salePrice" text="매입요금" /></button>
                                    
                               	 	<div class="module_group ">
                            			<div class="btn_group">
                            			    <div class="month">
				                                 <input class="date" type="text" name="nowDate" id="nowDate" value="${nowDate}" maxlength="10" 
                                                    data-label="<spring:message code="order.seaech.date.nowdate" text="검색일" />"/>
				                            </div>

                            				<select class="select-ajax" id="partShipCompany" name="partShipCompany"
	                                    		data-codetype="" data-code="${partShipCompany}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B010000" }'>
	                                    	</select>
		                                    <select class="" id="partDeliveryService" name="partDeliveryService"
		                                    	data-codetype="etc" data-code="${partDeliveryService}" data-url="/common/componentDataSet" data-param='{ "codeGroup" : "B040000" }'>
	                                    	</select>
                            			</div>
                            		</div>
                                </div>
                            </div>
                            <div class="scr_x">
                            	<div class="h_scroll" style="height:515px;">
                            		<table class="tbtype">
	                                <colgroup>
	                                        <col class="wp140">
	                                        <col class="wp110">
	                                        <col class="wp90">
	                                	<c:forEach items="${headerList}" var="list">
	                                		<col class="wp90">
	                                	</c:forEach>
	                                </colgroup>
	                                <thead>
	                               		<tr>
	                               		   <th><spring:message code="price.deliveryCompany" text="배송사" /></th>
	                               		   <th><spring:message code="price.deliveryService" text="배송 서비스" /></th>
	                               		   <th><spring:message code="price.weight" text="중량_Kg" /></th>
	                               		<c:forEach items="${headerList}" var="list">
	                                   			<th>${list.zoneCodeKname}</th>
	                               		</c:forEach>
	                               		</tr>
	                                </thead>
	                                <tbody>
                        			<c:forEach items="${weightList}" var="list">
                        			     <c:set var="dataKey" value="${list.weightCode}" />
                        			     <c:set var="data" value="${dataList[dataKey]}" />
                        				 <tr>
										     <td class="t_center">${list.shipCompany}</td>
										     <td class="t_center">${list.code}</td>
										     <td class="t_center">${list.weightKg}</td>
					        
											 <c:forEach items="${headerList}" var="list2">
												<td class="t_center"> 
													<c:set var="subData" value="${data[list2.zoneCodeEtc]}" />
													<fmt:formatNumber value="${subData}" pattern="#,###" />
												</td>													
	                       					 </c:forEach>
                       					 </tr>		                               			
                       				</c:forEach>
	                                </tbody>
                            	</table>
                            	</div>
                            </div> 	
                           
                            	
                            
                            <div class="foot_btn">
                            <!-- 요금 테스트 페이지 : 조한두(20.05.26) -->
                                    <a href="https://eminwon.qia.go.kr/common/CountrySP.jsp" target="_blank">국가코드</a>
                                                                국가코드(2)<input type="text" name="country" id="country" value="AU" maxlength="2" /> 
                                                                무게<input type="text" name="weight" id="weight" value="100"  /> (g)
                                     
                                    <input type="text" name="fees" id="fees" placeholder="공시요금" readonly value="${price.feesPrice}"/>
                                    <input type="text" name="sales" id="sales" placeholder="매입요금" readonly value="${price.salesPrice}"/>
                                     
                                    <button type="button" id="btn_search" class="btn_type2" ><spring:message code="button.search" text="조회" /></button>    
                            <!-- 요금 테스트 페이지 : 조한두(20.05.26) -->
                                <button type="button" id="btn_write" class="btn_type2"><spring:message code="button.insert" text="등록" /></button>  
                            </div>
                            
                            <div class="sub_tab">
                                      
                              </div>
                            
                        </li>
                    </ul>
                    </form>
                    
               </article>
           </div>
        </div>
    </div>
</body>
</html>