$(document).ready(function() {
	setTabSelectedById();
	prepareMenuFormAnswers();
	answersByForm();
});

function prepareMenuFormAnswers() {
	var idForm = getParameter('id');
	setMenuStyles();
	$('#li_form_editor').html(createMenu(msgFormEditor, FORM_EDITOR, idForm));
	$('#li_form_share').html(createMenu(msgFormShare, FORM_SHARE, idForm));
	$('#li_view_answers').html(createMenu(msgViewAnswers));
}
function setMenuStyles() {
	$('#li_view_answers').addClass('selected');
}

function answersByForm(orderBy, orderType, page) {
	var idForm = getParameter('id');
	var mOrderBy	= 'strCreationDate';
	var mOrderType	= 'desc';
	var mPage		= 1;
	if(orderBy != '' && orderBy != undefined) {mOrderBy = orderBy;}
	if(orderType != '' && orderType != undefined) {mOrderType = orderType;}
	if(page != '' && page != undefined) {mPage = page;}
    $.ajax({
        dataType:'json',
        type:'get',
        cache:false,
        url:'answers/form.html',
        data:{
        	id: idForm, 
        	orderBy: mOrderBy,
        	orderType: mOrderType,
        	page:mPage
        },
        success: function(data, textStatus, jqXHR){
            if(data) {
            	showFormTitle(data.wrapper.formTitle);
            	buildAnswersTable(data);
            }
            else {
            	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            }
        }
    });    
}

function showFormTitle(title){
	if(title != ''){
		$('#listAnswers').html(title);
	}
}

function buildAnswersTable(data){
	var titles			= [jQuery.i18n.prop('msg_ttl_author'), jQuery.i18n.prop('msg_ttl_date')];
	var columns_key		= ['author', 'strCreationDate'];
	var columns_size	= ['10', '8'];
	var columns_sort	= ['author', 'strCreationDate'];		
	var answersByForms = new AnswerGrid();
	answersByForms.paintGrid(titles, columns_key, columns_size, columns_sort, data, G_ANSWERS_FORM);
}

function parseCheckBox(checkbox_text) {
	var html = '';
	html += '<ul>';
	var checkbox = JSON.parse(checkbox_text);
	$.each(checkbox, function(i, v) {
		html += '<li>'+v+'</li>';
	}); 
	html += '</ul>';
	return html;
}

function parseGeolocation(geolocation_text) {
	var html = '';
	var geolocation = geolocation_text.split(';');
	if(geolocation.length == 2 && geolocation[0] != ''){
		html += '<span class="underline">'+jQuery.i18n.prop("msg_form_gps_latitude") +'</span>: '+ geolocation[0];
		html += '<br />';
		html += '<span class="underline">'+jQuery.i18n.prop("msg_form_gps_longitude") +'</span>: '+ geolocation[1];
	}
	return html;
}

function parseQuestionsWithSubType(subtype, value){
	var html = '';
	if(subtype != '' && value != ''){
		html += '<span class="underline">'+subtype+'</span>: ' +value;
	}
	return html;
}

function showMultimediaPopup(m_type, m_value){
	var m_height=150;
	if(m_type == 'video') 
		m_height = 240;
	$('#dialog-answer').dialog({
        width : 330,
        height : m_height
    });
    $("#dialog-answer").dialog('open');
    $("#dialog-answer").empty();
    if(m_type === 'audio') 
    	$("#dialog-answer").append('<audio controls="controls"><source src="'+m_value+'.ogg" type="audio/ogg">'+'<source src="'+m_value+'.mp3" type="audio/mp3"></audio>');
    else if(m_type === 'video') 
    	$("#dialog-answer").append('<video controls="controls"><source src="'+m_value+'.ogg" type="audio/ogg">'+'<source src="'+m_value+'.mp4" type="audio/mp4"></video>');
}

function showPicturePopup(img){
    $('#dialogPictureAnswer').dialog({
        width : 800,
        height : 600
    });
    $("#dialogPictureAnswer").dialog('open');
    $("#dialogPictureAnswer").empty();
    $("#dialogPictureAnswer").append('<img src="'+img+'"/>');
}