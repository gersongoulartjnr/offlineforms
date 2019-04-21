var MIN_FIRSTNAME_SIZE = 2;
var MAX_FIRSTNAME_SIZE = 25;
var MIN_LASTNAME_SIZE = 2;
var MAX_LASTNAME_SIZE = 40;
var MIN_PASSWORD_SIZE = 5;
var MAX_PASSWORD_SIZE = 20;
var MAX_CAPTCHA_SIZE = 5;

$(document).ready(function() {
	$('#firstName').focus();
	$('#refreshCaptcha').click(function () {
    	var url_context = urlContext();
        $('#captchaValue').attr('src', url_context+'/kaptcha.jpg?' + Math.floor(Math.random()*100) );
    });
	
	$("#createAccount").click(function(){
		if(!checkFields())
			return;
		var form	= $(this);
	    var url		= form.attr('action');
	    var firstName = $('#firstName').val();
	    var lastName = $('#lastName').val();	    
	    var email = $('#email').val();
	    var passEncoded	= encodePassword();
	    var captchaCode = $('#captchaCode').val();
	    
	    var data = JSON.stringify({ "firstName" : firstName, "lastName" : lastName, "email" : email, "passEncoded" : passEncoded, "captchaCode" : captchaCode });

	    $.ajax({
	        url : url,
	        type : "POST",
	        traditional : true,
	        contentType : "application/json",
	        dataType : "json",
	        data : data,
	        success : function(data) {
	        	if('success' == data.type.toLowerCase()){
	        		userHasBeenCreated();
	        	} else{
	        		addMessage(data.message, data.type.toLowerCase());
	        	}
	        },
	        error : function (response) {
	        	clearPasswords();
	        	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
	        },
	    });
	    return false;
	});
});

function userHasBeenCreated(){
	var html = '<div id="newAccountForm">';
		html += '<div class="info-box">';
			html += '<p>'+jQuery.i18n.prop('suc_account_created')+'</p>';
			html += '<p>'+jQuery.i18n.prop('suc_account_created_sent_email')+'</p>';
			html += '<br/><a href="index.html">'+jQuery.i18n.prop('msg_login_page')+'</a>';
		html += '</div>';
	html += '</div>';	
	$('#newAccountForm').html(html);
}

function checkFields(){
	if(!checkFirstName())
		return false;
	if(!checkLastName())
		return false;
	if(!checkEmail())
		return false;
	if(!checkPasswords())
		return false;
	if(!checkCaptchaCode())
		return false;
	return true;
}

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

function checkEmail(){
	var email = $('#email').val();
	if(email.trim() == '' || !validateEmail()){
		addMessage(jQuery.i18n.prop('err_email_invalid'), 'error');
		return false;
	}		
	return true;
}

function validateEmail(){
	
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

function checkCaptchaCode(){
	var captchaCode = $('#captchaCode').val();
	if(captchaCode.trim() == '' || captchaCode.length < MAX_CAPTCHA_SIZE){
		addMessage(jQuery.i18n.prop('err_captcha_required'), 'error');
		return false;
	}
	return true;
}

function encodePassword(){
	var password = $('#password').val();
	return $.sha1(password);
}

function clearPasswords(){
	$('#password').attr('value', '');
	$('#passwordConfirmation').attr('value', '');
}