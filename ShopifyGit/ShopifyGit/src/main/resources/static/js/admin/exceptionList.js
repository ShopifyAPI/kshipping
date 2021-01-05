$(document).ready(function() {
	
	var initialize = function() {
		bindEvent();
	};
	
	var bindEvent = function() {
		$('#searchDate').click( function(){
			searchDate();
		});
	};
	
	var startTimer = function() {
		timer = setInterval( buildTable, 60000);
		console.log("timer=" + timer);
	};

	var  stopTimer = function()  {
		clearInterval(timer);
	};
	
	var searchDate = function()  {
		stopTimer();
		
		$("table.tbtype tbody").empty();
		timestamp = $('#timestamp').val();
		buildTable(); 
		
		startTimer();
	};

	var buildTable = function() {
		
		var param = {
				"id" : lastId,
				"dateLine" : timestamp
		};
		
		$.ajax({
	        type       : "get"
	        ,async     : true  // true: 비동기, false: 동기
	        ,url       : "/admin/admin/lastExceptionLine?" + $.param(param)
	        ,cache     : false
//	        ,data      : JSON.stringify(param) 
	        ,contentType: "application/json"
	        ,dataType  : "json"
	        ,success   : function(data) {
	        	
				var row = "";
	        	 
	        	$.each(data.list, function(index, value) {
	        		row += "<tr class='meta' id='" + value.id + "' data-date='" + value.dateLine + "'>";
	        		row += "<td>" + value.type + "</td>";
	        		row += "<td>" + value.className + "</td>";
	        		row += "<td>" + value.instName + "</td>";
	        		row += "<td>" + value.fileName + "</td>";
	        		row += "<td>" + value.lineNo + "</td>";
	        		row += "</tr>";
	        		row += "<tr class='exRow'><td colspan='5'><span class='dateLine'>" +  value.content.substring(0,19) + "</span>" + value.content.substring(19) + "</td></tr>";
	        		
	        		lastId = value.id;
	        		timestamp = value.dateLine;
	        	});
	        	
	        	if ( data.list.length ) {
//	        		console.log(row);
	        		
	        		$('#exTable').append(row);
	        		$('#timestamp').val( timestamp );
	        		$(document).scrollTop($(document).height());
	        	}
	        }
	        ,error     : function(error) {
	        	console.log("error");
	        }
		});
	};
	
	
	//------------------------------------------------
	var timer;
	var lastId = "" ;
	var timestamp = "" ;
	
	initialize();
	buildTable();
	startTimer();

});
