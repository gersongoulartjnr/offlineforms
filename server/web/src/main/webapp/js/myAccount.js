var MIN_FIRSTNAME_SIZE = 2;
var MAX_FIRSTNAME_SIZE = 25;
var MIN_LASTNAME_SIZE = 2;
var MAX_LASTNAME_SIZE = 40;
var MIN_PASSWORD_SIZE = 5;
var MAX_PASSWORD_SIZE = 20;

$(document).ready(function() {
	$('#firstName').focus();
	$('#currentPassword').focus();
	
	$("#updateAccount").click(function(){		
		if(!checkFirstName())
			return;
		if(!checkLastName())
			return;
		var form	= $(this);
	    var url		= form.attr('action');
	    var firstName = $('#firstName').val();
	    var lastName = $('#lastName').val();
	    var data	= JSON.stringify({ "firstName" : firstName, "lastName" : lastName });

	    $.ajax({
	        url : url,
	        type : "POST",
	        traditional : true,
	        contentType : "application/json",
	        dataType : "json",
	        data : data,
	        success : function(data) {
	        	addMessage(data.message, data.type.toLowerCase());
	        },
	        error : function (response) {
	            addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
	        },
	    });
	    return false;
	});
	
	$("#changePass").click(function(){		
		if(!checkPasswords())
			return;
		var form	= $(this);
	    var url		= form.attr('action');
	    var curPassEncoded = encodePassword('currentPassword');
	    var passEncoded	= encodePassword('password');
	    var data	= JSON.stringify({ "curPassEncoded" : curPassEncoded, "passEncoded" : passEncoded });

	    $.ajax({
	        url : url,
	        type : "POST",
	        traditional : true,
	        contentType : "application/json",
	        dataType : "json",
	        data : data,
	        success : function(data) {
	        	addMessage(data.message, data.type.toLowerCase());
	        },
	        error : function (response) {
	            addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
	        },
	    });
	    return false;
	});
});

function checkFirstName(){
	var firstName = $('#firstName').val();
	if(firstName.trim() == ''){
		addMessage(jQuery.i18n.prop('err_firstname_required'), 'error');
		return false;
	}
	if(firstName.length < MIN_FIRSTNAME_SIZE || firstName.length > MAX_FIRSTNAME_SIZE){
		addMessage(jQuery.i18n.prop('err_firstname_size', MIN_FIRSTNAME_SIZE, MAX_FIRSTNAME_SIZE), 'error');
		return false;
	}
	return true;
}

function checkLastName(){
	var lastName = $('#lastName').val();
	if(lastName.trim() == ''){
		addMessage(jQuery.i18n.prop('err_lastname_required'), 'error');
		return false;
	}
	if(lastName.length < MIN_LASTNAME_SIZE || lastName.length > MAX_LASTNAME_SIZE){
		addMessage(jQuery.i18n.prop('err_lastname_size', MIN_LASTNAME_SIZE, MAX_LASTNAME_SIZE), 'error');
		return false;
	}
	return true;
}

function checkPasswords(){
	var cur_password = $('#currentPassword').val();
	var password = $('#password').val();
	var retype_pass = $('#passwordConfirmation').val();
	if(cur_password.trim() == ''){
		addMessage(jQuery.i18n.prop('err_current_pass_required'), 'error');
		return false;
	}	
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

function encodePassword(id_field){
	return $.sha1($('#'+id_field).val());
}