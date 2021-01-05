$(document).ready(function() {
	
	// function -------------
	function addressChangeSave() {
		
		if ( ! fnValidationCheckForInput($("#addressForm"))) {
			return;
		}
		
		
		var flag = false;
		if ( $(opener.document).find("#buyerAddr1").val() !=  $("#addr1").val() ) {
			$(opener.document).find("#buyerAddr1").val(  $("#addr1").val() ); 
			flag = true;
		}
		
		if ( $(opener.document).find("#buyerAddr2").val() != $("#addr2").val() ) {
			$(opener.document).find("#buyerAddr2").val(  $("#addr2").val() ); 
			flag = true;
		}
		
		
		if ( flag ) {
			window.opener.composeBuyerAddress();
		}
		
		self.close();
	}
	
	function addressChangeCancel() {
		self.close();
	}
	
	
	// run  -----------------------------------------
	
	$(document).on("click", "#addressChangeSave", addressChangeSave);
	$(document).on("click", "#addressChangeCancel", addressChangeCancel);
	
    $("#addr1").val( $(opener.document).find("#buyerAddr1").val() );
    $("#addr2").val( $(opener.document).find("#buyerAddr2").val() );
    
	
}) ;




