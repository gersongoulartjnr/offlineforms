$(document).ready(function() {
	showTimeoutMessage();
});

function showTimeoutMessage(time) {
	var delay 			= 500; 						// 0.5 milliseconds
	var minTimeout 		= 120000;					// two minutes before session expire.
	
	var _sessionTimeout = SESSION_TIMEOUT * 1000; 	// global variable
	var remainderTime	= (SESSION_TIMEOUT * 1000) - minTimeout;
	var timeout 		= (_sessionTimeout - minTimeout);
	
	if(time != undefined) {		
		_sessionTimeout = time * 1000;
		timeout 		= (_sessionTimeout - minTimeout) > 0 ? (_sessionTimeout - minTimeout) : _sessionTimeout;
	}	
	
	setTimeout( function() {		
		$.ajax({
	        dataType:'json',
	        type:'post',
	        cache:false,
	        url:'getinactivetime.html',
	        success: function(data, textStatus, jqXHR){
	        	var inactiveTimeServer = (data + delay);
	        	if((SESSION_TIMEOUT * 1000) - inactiveTimeServer <= 0) {
	        		WARNING_H5_FORM_EDITOR = false;
	        		window.location = urlContext() + "logout.html";
	        	}
	        	if(remainderTime <= inactiveTimeServer) {
	        		var count = Math.ceil(((SESSION_TIMEOUT * 1000) - inactiveTimeServer) / 1000);
	        		startCountdown(count);
	        	} else {
	        		var _time = Math.ceil((remainderTime - inactiveTimeServer) / 1000);
	        		showTimeoutMessage(_time);
	        	}
	            return;
	        },
	        error: function(data, textStatus, jqXHR){
	        	WARNING_H5_FORM_EDITOR = false;
	        	window.location = urlContext() + "logout.html";
	            return;
	        }
	    });		
	}, timeout);
}

function startCountdown(wsCount) {
	if((wsCount - 1) >= 0){
		wsCount = wsCount - 1;
		$("#dialog-recovery").html(jQuery.i18n.prop("msg_session_timeout_message_part1") + ' <font style="font-size:125%;"> <b>' + wsCount + '</b></font> ' + jQuery.i18n.prop("msg_session_timeout_message_part2"));	 
		$("#dialog-recovery" ).dialog({
			resizable: true,
			title: jQuery.i18n.prop("msg_session_timeout_boxtitle"),
			width: 500,
			modal: true,
			closeText: "hide",
			closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons: {
				"Logout": function() {					
//					showTimeoutMessage(1);
					$( this ).dialog( "close" );
					WARNING_H5_FORM_EDITOR = false;
					window.location = urlContext() + "logout.html";
				},
				"Ok": function() {
					wsCount = -1;
				}
			},
		});
		setTimeout(function(){
			$("#dialog-recovery").dialog("close");
			startCountdown(wsCount);
		}, 1000);
	} else if(wsCount == -1) {
		$("#dialog-recovery").dialog("close");
		showTimeoutMessage(7);
	} else {	
		showTimeoutMessage(1);
	}	 
}