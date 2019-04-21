$(document).ready(function() {
	$('ul#usermenu li ul').hide();
	$('ul#usermenu li').hover(
		function() {
			$('ul#usermenu li').not($('ul', this)).stop();
			$('ul', this).slideDown('fast');
		},
		function() {
			$('ul', this).slideUp('fast');
		}
	);
	$('#refreshCaptcha').click(function () {
    	var url_context = urlContext();
        $('#captchaValue').attr('src', url_context+'/kaptcha.jpg?' + Math.floor(Math.random()*100) );
    });
});

var MIN_PASSWORD_SIZE = 6;
var MAX_PASSWORD_SIZE = 20;

function checkPassword() {
	var password        = $('#password').val();
	var passwordConfirm = $('#retype_password').val();

	if (!checkPasswordSize(password)) {
		return;
	}

	if (!checkPasswordsMatches(password, passwordConfirm)) {
		return;
	}
}

function checkPasswordSize(password) {
	var msgPassShort = $('span[id="span_pass"]')[0];
	var msgPassLong  = $('span[id="span_passconf"]')[0];

	if (password.length < MIN_PASSWORD_SIZE){
		msgPassShort.style.display = 'block';
		msgPassLong.style.display  = 'none';
		disableSaveAccount();
		return false;
	} else if(password.length > MAX_PASSWORD_SIZE){
		msgPassShort.style.display = 'none';
		msgPassLong.style.display  = 'block';
		disableSaveAccount();
		return false;
	} else {
		msgPassShort.style.display = 'none';
		msgPassLong.style.display  = 'none';
		enableSaveAccount();
		return true;
	}	
}

function checkPasswordsMatches(password, passwordConfirm) {
	var message = $('span[id$=":passMatchError"]')[0];
	
	if (password == passwordConfirm && password != "") {
		message.style.display = 'none';
		enableSaveAccount();
		return true;
	} else {
		message.style.display = 'block';
		disableSaveAccount();
		return false;
	}
}

function encryptPassword() {
	var password = $('#password').val();
	var newpass = $.sha1(password);
	$('#password').attr("value", newpass); 
}