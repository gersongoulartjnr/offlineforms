$(document).ready(function() {	
	setTabSelectedById();
	$('#form_title').focus();
	document.getElementById('form_icon').addEventListener('change', handleFileSelect, false);
	disableDownloadAPK(getParameter('id'));	
	
	$('#tags').tagsInput({
		   'height':'30px',
		   'width':'395px',
		   'placeholderColor' : '#666666'
		});
	
	$('#newForm').click(function () {
		newFormDialog();
	});	
	$('#importForm').click(function() {
		showImportForm();
	});
	$('#clearForm').click(function () {
		clearForm();
	});
});

function newFormDialog() {
	var message = createMessageDialog(jQuery.i18n.prop('msg_form_messageDialogNewForm')); 
	$('#dialog-confirm p').append(message);	
	$( "#dialog-confirm" ).dialog({
		autoOpen: false,
		modal: true,
		title: jQuery.i18n.prop('msg_form_newFormDialogTitle'),
		width: 500,
		buttons: {
			"New Form" : function() {
				newForm();				
				WARNING_H5_FORM_EDITOR = false;	
				$( this ).dialog( "close" );
			},
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		}
	});
	$("#dialog-confirm").dialog('open');	
	$('#messageDialogConfirm').remove();
}
function newForm() {
	window.location.href = urlContext()+'form.html';
}

function showImportForm() {
	var title = jQuery.i18n.prop('msg_form_import_title');
	$('#dialog-import').load(urlContext()+'import.html').dialog({
		autoOpen : false,
		modal : true,
		title : title,
		width : 600,
	});
	$("#dialog-import").dialog('open');
}

function showSaveAsForm(thetitle) {
	$('#dialog-save-as').load(urlContext()+'form-save-as.html').dialog({
		autoOpen : false,
		modal : true,
		title : thetitle,
		width : 600,
	});
	$("#dialog-save-as").dialog('open');
}

function clearForm() {
	var message = createMessageDialog(jQuery.i18n.prop('msg_form_messageDialogClean'));
	$('#dialog-confirm p').append(message);	
	if(form.elements.length > 0) {
		$( "#dialog-confirm" ).dialog({
			autoOpen: false,
			modal: true,
			title: jQuery.i18n.prop('msg_form_clearDialogTitle'),
			width: 500,
			buttons: {
				Clear : function() {
					form.elements = new Array();
					$('#properties').hide();					
					form.renderForm();					
					initForm();
					WARNING_H5_FORM_EDITOR = false;					
					$( this ).dialog( "close" );
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			}
		});
		$("#dialog-confirm").dialog('open');
	} else {
		addMessage(jQuery.i18n.prop('msg_form_clearMessageInfo'), 'info');
	}	
	$('#messageDialogConfirm').remove();
}

function disableDownloadAPK(id){
	if(isAPKBuilding(id)) {
		$('#downloadApp').removeClass('button-submenu');
		$('#downloadApp').addClass('button-submenu-disable');
		$('#downloadAppBuilding').css({ display: "block"});
		$('#downloadAppBuilded').css({ display: "none"});
		$('#downloadApp').unbind('click');
		setTimeout(function(){
			disableDownloadAPK(id);
		}, 5000); // 5 seconds
	} else {
		enableDownloadApk();
	} 
}

function enableDownloadApk() {
	$('#downloadApp').removeClass('button-submenu-disable');
	$('#downloadApp').addClass('button-submenu');
	$('#downloadAppBuilded').css({ display: "block"});
	$('#downloadAppBuilding').css({ display: "none"});
	$('#downloadApp').click(function() {
		var id = getParameter('id');
		if(id != '') { downloadApp(id); }
	});
}

function handleFileSelect(evt) {
	var files = evt.target.files;
	var f = files[0];
	if (!f.type.match('image.*')) {
		//console.log('error');
		return;
	}
	var reader = new FileReader();
	reader.onload = (function(theFile) {
		return function(e) {
			var span = document.createElement('span');
			span.innerHTML = ['<img class="form_icon" src="', e.target.result, '" title="', escape(theFile.name), '"/>'].join('');
			$('#icon_container').html(span);
		};
	})(f);
	reader.readAsDataURL(f);
}

/* Votes */
function loadVotes(like, dislike, numLikes, numDislikes) {	
	if(like == 'true') 		{ disableLike(); }
	if(dislike == 'true') 	{ disableDislike(); }
}
function disableLike() {
	$('#imgLike').addClass('vote_disabled');	
	$('#imgDislike').removeClass('vote_disabled');
}
function disableDislike() {
	$('#imgLike').removeClass('vote_disabled');
	$('#imgDislike').addClass('vote_disabled');
}
function setNumLikes(numLikes) {
	$('#mLike').html(numLikes);
}
function setNumDisLikes(numDislikes) {
	$('#mDislike').html(numDislikes);
}
function doLike() {
	var id = getParameter('id');
	$.ajax({
        dataType:'json',
        type:'post',
        cache:false,
        url:'form/like.html',
        data:{
        	id:id
        },
        success: function(data, textStatus, jqXHR){
            if(data) {            	
            	disableLike();
            	setNumLikes(data.numLikes);
            	setNumDisLikes(data.numDislikes);
            }
            else {
            	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            }
        }
    });
}
function doDislike() {
	var id = getParameter('id');
	$.ajax({
        dataType:'json',
        type:'post',
        cache:false,
        url:'form/dislike.html',
        data:{
        	id:id
        },
        success: function(data, textStatus, jqXHR){
            if(data) {            	
            	disableDislike();
            	setNumLikes(data.numLikes);
            	setNumDisLikes(data.numDislikes);
            }
            else {
            	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            }
        }
    });
}
/* Votes */