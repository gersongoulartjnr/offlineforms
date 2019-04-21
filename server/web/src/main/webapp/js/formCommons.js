$(document).ready(function() {	
	var id = getParameter('id');
	prepareMenuFormEditor();
	
	$('#downloadXML').click(function() {
		if(id != '') { downloadXML(id); }
	});
	
	$('#downloadApp').click(function() {
		if(id != '') { downloadApp(id); }
	});
	
	$('#sendAppLink').click(function() {
		if(id != '') { sendAppLinkPopup(id); }
	});
	
	$('#saveForm').click(function() {
		sendFormToServer();
	});
	
	$('#saveAsForm').click(function() {
		showSaveAsForm(jQuery.i18n.prop('msg_form_save_as_title'));
	});
	
	$('#removeForm').click(function() {
		deleteForm();
	});
});

function prepareMenuFormEditor() {
	var idForm = getParameter('id');
	setMenuStyles();
	$('#li_form_editor').html(createMenu(msgFormEditor));
	$('#li_form_share').html(createMenu(msgFormShare, FORM_SHARE, idForm));
	$('#li_view_answers').html(createMenu(msgViewAnswers, VIEW_ANSWERS, idForm));
}
function setMenuStyles() {
	$('#li_form_editor').addClass('selected');
}
function downloadXML(id) {
	document.location.href='form.html?xml&id='+id;
}

function downloadApp(id) {
	if(isAPKBuilding(id)) {            	
		addMessage(jQuery.i18n.prop('msg_form_buildingapk_warning'), 'warn');
		return;
	} else {
		document.location.href='form.html?mobile-app&id='+id;
		return;
	}
}

/**
 * Download a file with the answers(csv format)
 * @param id
 */
function downloadCsvAnswers(id) {
	document.location.href='answers.html?dwl-csv&id='+id;
}
/**
 * Download a file with the answers(xml format)
 * @param id
 */
function downloadXmlAnswers(id) {
	document.location.href='answers.html?dwl-xml&id='+id;
}
function sendAppLinkPopup(id){
	showApkPopup('Apk Download');
}
function showApkPopup(title) {
	$('#dialog-app').load(urlContext()+'send-apk-link.html').dialog({
		autoOpen : false,
		modal : true,
		title : title,
		width : 600,
	});
	$("#dialog-app").dialog('open');
}
function sendAppLink(id){
	//var id = getParameter('id');
	$.ajax({
        dataType:'json',
        type:'post',
        cache:false,
        url:'form/send-app-link.html',
        data:{
        	id:id
        },
        success: function(data, textStatus, jqXHR){
            if(data) {
            	addMessage(data.message);
            }
            else {
            	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            }
        }
    });
}

function deleteForm(){
	$('#dialog-confirm p').append(jQuery.i18n.prop('msg_form_messageDialogDeleteForm'));
	if(form.elements.length > 0) {
		$( "#dialog-confirm").dialog({
			autoOpen: false,
			modal: true,
			title: 'Delete',
			width: 500,
			buttons: {
				Remove : function() {
					doRemove();		
					$(this).dialog( "close" );
				},
				Cancel: function() {
					$(this).dialog( "close" );
				}
			}
		});
		$("#dialog-confirm").dialog('open');
	} else {
		addMessage(jQuery.i18n.prop('msg_form_clearMessageInfo'), 'info');
	}	
	$('#messageDialogConfirm').remove();
}
function doRemove(){
	var url = getParameter('id');
	var data = JSON.stringify({ "url" : url });
	$.ajax({
    	url:'form.html?delete',
        type : "POST",
        traditional : true,
        contentType : "application/json",
        dataType : "json",
        data : data,
        success : function(data) {
        	if('success' == data.type.toLowerCase()){        		
        		window.location =  urlContext() + 'forms.html';
        		//addMessage(data.message, 'info');
        	}
        	else{
        		addMessage(data.message, data.type.toLowerCase());
        	}
        },
        error : function (response) {
        	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
        },
    });
    return false;
}