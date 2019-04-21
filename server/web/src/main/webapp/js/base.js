var TAB_PREFIX      = 'tabForm_'; //mainMenu.jsp
var TAB_NEW_FORM    = 'tabForm_new_form';

var FORM_EDITOR		= 'form.html';
var FORM_SHARE		= 'form-share.html';
var VIEW_ANSWERS	= 'answers.html';
var msgFormEditor	= '';
var msgFormShare	= '';
var msgViewAnswers	= '';

var MSG_NO_RESULTS;
var MSG_PAGES;
var MSG_SHOWING;
var MSG_OF;

$(document).ready(function() {
	loadMessages();		
});

/**
 * it's for the main menu
 * @param _id
 */
function setTabSelectedById(id){
	if(id == undefined || id == ''){
		var idForm = getParameter('id');
		if(idForm != '') {
			$('#'+TAB_PREFIX+idForm).addClass('selected');
		}
		else {	
			$('#'+TAB_NEW_FORM).addClass('selected');		
		}
	} else {
		$('#'+id).addClass('selected');
	}
}

/**
 * load the file bundle/messages.properties
 */
function loadMessages(){
	jQuery.i18n.properties({
	    name:'messages', 
	    path:'./bundle/', 
	    mode:'both'
	});	
	msgFormEditor	= jQuery.i18n.prop('msg_form_editor_title');
	msgFormShare	= jQuery.i18n.prop('msg_form_share');
	msgViewAnswers	= jQuery.i18n.prop('msg_form_view_answers');
	
	MSG_NO_RESULTS	= jQuery.i18n.prop('msg_grid_no_results');
	MSG_PAGES		= jQuery.i18n.prop('msg_grid_pages')+':';
	MSG_SHOWING		= jQuery.i18n.prop('msg_grid_showing');
	MSG_OF			= jQuery.i18n.prop('msg_grid_of');
}
/**
 * @returns: http://domain/context
 */
function urlContext(){
    var context = document.location.href;
    /*if(context == undefined)
    	context = window.location.href;*/
    context = context.substring(0, context.lastIndexOf('/')) + '/';
    return context;
}

function redirectToIndex() {
	window.location = urlContext() + "index.html";
	return false;
}

/**
 * @param name
 * @returns the param by name
 */
function getParameter(name){
    var regexS  = "[\\?&]"+name+"=([^&#]*)";
    var regex   = new RegExp( regexS );
    var tmpURL  = window.location.href;
    var results = regex.exec( tmpURL );
    if(results == null)
        return "";
    else
        return results[1];
}

function createMenu(msg, subsection, id) {
	if(subsection == undefined) {
		return msg;
	}
	else {		
		var link = urlContext()+subsection+'?id='+id;
		return '<a href="'+link+'" class="main_menu">'+msg+'</a>';
	}
}

function showQRCode(title, url) {
	if(isAPKBuilding(url)) {            	
		addMessage(jQuery.i18n.prop('msg_form_buildingapk_warning'), 'warn');
		return;
	}
	$('#dialog-qr-code').load(urlContext()+'qr-code.html?url='+url).dialog({
		autoOpen : false,
		modal : true,
		title : title,
		width : 'auto',
		position: [10, 10],
	});
	$("#dialog-qr-code").dialog('open');
}

function isAPKBuilding(id) {
	var result = false;
	$.ajax({ // synchronous call
        dataType:'json',
        type:'post',
        cache:false,
        async: false,
        url:'form/valid.html',
        data:{
        	id:id
        },
        success: function(data, textStatus, jqXHR){
            if(data) {            	
            	result = true;
            }
        },
        error: function(data, textStatus, jqXHR){
        	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            result = false;
        }
    });
	return result;
}