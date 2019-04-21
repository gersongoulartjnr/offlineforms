var MIN_PASSWORD_SIZE = 5;
var MAX_PASSWORD_SIZE = 20;

$(document).ready(function() {
	$('#email').focus();
	$('#password').focus();
	
	$("#forgotPass").click(function(){
		if(!checkEmail())
			return;
		
	    var form	= $(this);
	    var url		= form.attr('action');
	    var email	= $('#email').val();
	    var data	= JSON.stringify({ "email" : email });

	    $.ajax({
	        url : url,
	        type : "POST",
	        traditional : true,
	        contentType : "application/json",
	        dataType : "json",
	        data : data,
	        success : function(data) {
	        	clearEmail();
	        	addMessage(data.message, data.type.toLowerCase());
	        },
	        error : function (response) {
	        	clearEmail();
	            addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
	        },
	    });
	    return false;
	});
	
	$("#updatePass").click(function () {
		if(!checkEmail())
			return;
		if(!checkCode())
			return;
		if(!checkPasswords())
			return;
		var form	= $(this);
	    var url		= form.attr('action');
	    var email	= $('#email').val();
	    var passEncoded	= encodePassword();
	    var resetPassCode= $('#resetPassCode').val();
	    var data	= JSON.stringify({ "passEncoded" : passEncoded, "email" : email, "resetPassCode": resetPassCode });

	    $.ajax({
	        url : url,
	        type : "POST",
	        traditional : true,
	        contentType : "application/json",
	        dataType : "json",
	        data : data,
	        success : function(data) {
	        	clearUpdatePassword();
	        	addMessage(data.message, data.type.toLowerCase());
	        },
	        error : function (response) {
	        	clearUpdatePassword();
	        	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
	        },
	    });
	    return false;
	});
});

function clearEmail(){
	$('#email').attr('value', '');
}
function clearUpdatePassword(){
	$('#email').attr('value', '');
	$('#resetPassCode').attr('value', '');
	$('#password').attr('value', '');
	$('#passwordConfirmation').attr('value', '');
	$('#resetPassCode').attr('value', '');
}

function checkEmail(){
	var email = $('#email').val();
	if(email.trim() == ''){
		addMessage(jQuery.i18n.prop('err_email_required'), 'error');
		return false;
	}
	return true;
}

function checkCode(){
	var resetPassCode = $('#resetPassCode').val();
	if(resetPassCode.trim() == ''){
		addMessage(jQuery.i18n.prop('err_fp_invalid_code'), 'error');
		return false;
	}
	return true;
}

function checkPasswords(){
	var password = $('#password').val();
	var retype_pass = $('#passwordConfirmation').val();
	if(password.trim() == ''){
		addMessage(jQuery.i18n.prop('err_pass_required'), 'error');
		return false;
	}
	if(password.length < MIN_PASSWORD_SIZE || password.length > MAX_PASSWORD_SIZE){
		addMessage(jQuery.i18n.prop('err_pass_size', MIN_PASSWORD_SIZE, MAX_PASSWORD_SIZE), 'error');
		return false;
	}
	if(password.trim() != retype_pass.trim()){
		addMessage(jQuery.i18n.prop('err_pass_dont_match'), 'error');
		return false;
	}
	return true;
}

function encodePassword(){
	var password = $('#password').val();
	return $.sha1(password);
}