$(document).ready(function() {
	initFormEditor($('#form_title').val());
});

var editElement = null;
var form = null;
var WARNING_H5_FORM_EDITOR = false;
// localstorage key
var keyStorage; // = $("#form_url").val() == "" ? "newForm" : $("#form_url").val();
var currentForm = localStorage[keyStorage];
var currentTarget = null;

jQuery.fn.exists = function(){return this.length>0;};

function setPositionForNewElement(e) {
	var total = document.querySelectorAll('#xmlForm ol > li').length;
	$('#hr_').remove();
	//if(total > 0) {	
		var nodeId = e.target.id;
		var parentId = $(e.target).closest("li").attr("id");
		
		if(nodeId != null && nodeId != undefined && nodeId != '' && nodeId != 'xmlForm' && nodeId != 'hr_' && nodeId != 'olXmlForm' && nodeId != 'dropBoxId') {
			currentTarget = nodeId;
			if($('#hr_').exists() == false) {
				$('<hr id="hr_" class="addElement" />').insertAfter('#'+currentTarget);
			}
		}
		else if(parentId != null && parentId != undefined && parentId != '' && parentId != 'xmlForm' && parentId != 'hr_' && parentId != 'olXmlForm' && parentId != 'dropBoxId') {			
			currentTarget = parentId;
			if($('#hr_').exists() == false) {
				$('<hr id="hr_" class="addElement" />').insertAfter('#'+currentTarget);
			}
		}
		else if(nodeId == 'dropBoxId') 		{ currentTarget = total-1; }
		else if(parentId == 'dropBoxId') 	{ currentTarget = total-1; }
		else {
			currentTarget = null;			
		}
	//}
}

function addElement(type) {
    var elem = fieldFactory(type);
    if (elem == null) {
        return true;
    }
    form.elements.push(elem);
    saveLocalStorage();
    form.renderForm();
    $('li#' + (form.elements.length - 1)).click();
}
//

function initFormEditor(formtitle) {	
	keyStorage = $("#form_url").val() == "" ? "newForm" : $("#form_url").val();	
	
	if(formtitle == undefined || formtitle == ''){
		formtitle = jQuery.i18n.prop('msg_form_title');
	}
	form = new FormClass();
	
	var xml = $("#form_xml").val();
	if(xml != undefined && xml != '') {
		try{
			loadFormFromXML(xml);
		} catch( err ) {
			alert('#{msgs.form_edit_error_loading}, ' + err);
		}
	}

	WARNING_H5_FORM_EDITOR = false;
	// localStorage
	currentForm = localStorage[keyStorage];
	if (currentForm && getParameter('recover') == 'true') {
		loadLocalStorage();
		deleteLocalStorageItem();
	} else {
		form.title = formtitle;
		$('input[id$=":titleForm"]').val(formtitle);
		form.container = 'xmlForm';
	}

	
	form.renderForm(true);

	window.onbeforeunload = function() {
		if (WARNING_H5_FORM_EDITOR) {
			deleteLocalStorageItem();
			return jQuery.i18n.prop('msg_warning_onbeforeunload');
		}
	};

	var field = document.querySelectorAll('#components ol > li');

	for ( var i = 0; i < field.length; i++) {
		var elem = field[i];
		elem.setAttribute('draggable', 'true');
		elem.addEventListener('dragstart', function(e) {
			e.dataTransfer.effectAllowed = 'copy';
			e.dataTransfer.setData('Text', $(this).find('img').attr('id'));
		});
	}

	var destiny = document.querySelector('#xmlForm');

	destiny.addEventListener('dragover', function(e) {
		if (e.preventDefault) {
			e.preventDefault();
		}
		setPositionForNewElement(e);
		e.dataTransfer.dropEffect = 'copy';
		return false;
	});

	destiny.addEventListener('drop', function(e) {
		// this stops the redirect, I don't know why, but
		// it works.
		if (e.preventDefault) {
			e.preventDefault();
		}
		
		type = e.dataTransfer.getData('Text');
			
		// fieldFactory Method, located in h5FieldClasses.js
		$('#hr_').remove();
		var total = document.querySelectorAll('#xmlForm ol > li').length;
		if(total == 0) {
			addElement(type);
		}
		else if(total > 0 && currentTarget != null) {
			var elem = fieldFactory(type);
			if (elem == null) {
				return true;
			}
			var relTarget= parseInt(currentTarget)+1;
			form.elements.splice(relTarget, 0, elem);
			form.renderForm();
			$('li#' + relTarget).click();
		}
		return true;
	});
	initForm();
};

// save localStorage
function saveLocalStorage() {
	if (form.elements.length < 1) { // threee elements at least to local storage
		return;
	}
	form.title = $('#form_title').val() == "" ? "UNNAMED" : $('#form_title').val();
	form.description = $('#form_description').val();
	form.url = $('#form_url').val();
	form.renderForm();
	$('li#' + editElement).click();
	var _form = form.toJSON();
	localStorage[keyStorage] = _form;
	WARNING_H5_FORM_EDITOR = false;
}

// load localStorage
function loadLocalStorage() {
	form.elements = new Array();
	form.fromJSON(currentForm);
	$('#form_title').val(form.title);
	$('#form_description').val(form.description);
	form.renderForm();
}

// delete localStorage
function deleteLocalStorageItem() {
	localStorage.removeItem(keyStorage); 
};

function initForm() {
	$('#xmlForm ol li').click(function() {
		$('#xmlForm ol li').removeClass('editing');
		$(this).addClass('editing');

		var id = parseInt($(this).attr('id'));
		editField(id);
	});
}

// save method
function sendFormToServer(){
    
    if ($('#form_title').val() === ''){
    	// invalid form title, here can be check the title with expression language
    	addMessage(jQuery.i18n.prop('msg_help_type_title'), 'error');
    	return;
    }
    if(form.elements.length == 0) {
    	closePopup('#dialogSaveFormAs');
    	addMessage(jQuery.i18n.prop('msg_error_emptyForm'), 'error');
    	return;
    }
    if (!form.validateForm()){
    	// if there is an error in validation return false
    	return;
    }
    if ($("#form_url").val() != "" ) {
        var xml = form.toXML();
        $("#form_xml").attr("value", xml);
        $('#account').submit(); // use the native submit method of the form element
        deleteLocalStorageItem();
        WARNING_H5_FORM_EDITOR = false;
    	return;
    }
    
    $.ajax({
        dataType:'json',
        type:'post',
        cache:false,
        url:'form/validtitle.html',
        data:{
        	title:$("#form_title").val()
        },
        success: function(data, textStatus, jqXHR){
            if(data) {            	
            	addMessage(jQuery.i18n.prop('msg_error_form_title_duplicate'), 'warn');
            	return;
            }
            else {
                var xml = form.toXML();
                $("#form_xml").attr("value", xml);	
                $('#account').submit(); // use the native submit method of the form element
                deleteLocalStorageItem();
                WARNING_H5_FORM_EDITOR = false;
            	return;
            }
        },
        error: function(data, textStatus, jqXHR){
        	//console.log("Could not retrieve data from API.");
            return;
        }
    });

}

function saveAsFormToServer(){	
    if ($('#save_as_title').val() === ''){
    	closePopup('#dialog-save-as');
    	addMessage(jQuery.i18n.prop('msg_help_type_title'), 'error');
    	return;
    }    
    $("#form_title").val( $("#save_as_title").val() );    
	if(form.elements.length == 0) {
		closePopup('#dialog-save-as');
		addMessage(jQuery.i18n.prop('msg_error_emptyForm'), 'error');
		return;
	}
	if (!form.validateForm()){
    	return;
    }
    
    var url = $("#form_url").val(); // tmp url
    var key = $("#form_key").val(); // tmp key
    $("#form_url").val(""); // reset url to be the form a new form
    $("#form_key").val(""); // reset key to be the form a new form
    
    $.ajax({
        dataType:'json',
        type:'post',
        cache:false,
        url:'form/validtitle.html',
        data:{
        	title:$("#form_title").val()
        },
        success: function(data, textStatus, jqXHR){
        	closePopup('#dialog-save-as');
            if(data) {            	
            	$("#form_url").val(url);
            	$("#form_key").val(key);
            	addMessage(jQuery.i18n.prop('msg_error_form_title_duplicate'), 'warn');
            	return;
            }
            else {
                var xml = form.toXML();
                $("#form_xml").attr("value", xml);	
                $('#account').submit();
                deleteLocalStorageItem();
                WARNING_H5_FORM_EDITOR = false;
            	return;
            }
        },
        error: function(data, textStatus, jqXHR){
            return;
        }
    });
}

function deleteFormDialog() {
	var message = createMessageDialog(jQuery.i18n.prop('msg_form_messageDialogDeleteForm')); 
	$('#dialog-confirm p').append(message);
	
	$( "#dialog-confirm" ).dialog({
		autoOpen: false,
		modal: true,
		title: jQuery.i18n.prop('msg_form_deleteDialogTitle'),
		width: 500,
		buttons: {
			"Delete Form" : function() {
				deleteFormAjax();
				deleteLocalStorageItem();
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

function collectFormDialog() {
	var message = createMessageDialog(jQuery.i18n.prop('msg_form_messageDialogCollect')); 
	$('#dialog-confirm p').append(message);
	
	$( "#dialog-confirm" ).dialog({
		autoOpen: false,
		modal: true,
		title: jQuery.i18n.prop('msg_form_collectDialogTitle'),
		width: 500,
		buttons: {
			"Set to Collect" : function() {
				setFormAsCollectableAjax();
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

function createMessageDialog(messageDialog){
	var message = '<span id="messageDialogConfirm">';
	message += messageDialog; 
	message += '</span>';
	
	return message;
}

function updateFormTitle() {
	form.title = $('#form_title').val();
	$('#lgd_title').html($('#form_title').val());
	saveLocalStorage();
}

function showDropBox() {
	form.showDropBox();
}

function loadFormFromXML(xml){
	try{
	var xmlDoc = $.parseXML( xml );
	}catch(err){
		addMessage(jQuery.i18n.prop('msg_error_xmlprocess'), 'error');
		return;
	}
    var $xml = $( xmlDoc );
    var size = $xml.find('questions').size();
    //TODO: verify xml structure
    try{
    	if(size == 0){//temporal while verification
    		throw "no questions";
    	}
    $xml.find('questions').each(function(){
    	 var elems = this.childNodes;
    	 for(var i = 0; i<elems.length; i++){
    		 var field = fieldFactory(elems[i].nodeName);
    		 if(field!=null){
    			field.setFromXMLDoc(elems[i]);
    			form.elements.push(field);
    		 }
    	 }
    });
    form.renderForm(true);
    }catch(err){
    	addMessage(jQuery.i18n.prop('msg_error_noquestions'), 'warn');
    }
}

//////////////////// Field Operations /////////////////////
function editField(id) {
	editElement = id;
	var element = form.elements[id];
	
	$('#properties').show();
	$('#properties table').remove();
	element.showProperties();
}

function deleteField() {
	form.elements.splice(editElement, 1);
	$('#properties').hide();
	saveLocalStorage();
	form.renderForm();
	if (form.elements.length == 0) {
		deleteLocalStorageItem();
		WARNING_H5_FORM_EDITOR = false;
	}
}

function idToQuestionNumber(id){
	var idNumber = id.split("_")[1];
	idNumber++;
	
	return jQuery.i18n.prop('msg_form_question') + " " + idNumber;	
};

function questionNumberToId(questionNumber){
	var idNumber = questionNumber.split(" ")[1];
	idNumber--;
	return idNumber;
};

function saveField() {
	var element = form.elements[editElement];
	element.saveProperties();
	saveLocalStorage();
	form.renderForm();
	$('li#' + editElement).click();
	//addMessage(jQuery.i18n.prop('msg_saveautomatic'), 'info');
}
///////////////////////////////////////////////////////////

//move the field up and down
function move(value) {
	$('#save').click();

	// only values -1 and 1
	if (Math.abs(value) != 1) {
		return;
	}

	if (value == 1 && editElement == form.elements.length - 1) {
		return;
	} else if (value == -1 && editElement == 0) {
		return;
	}

	var currentField = form.elements[editElement];
	var newElement = editElement + value;

	form.elements[editElement] = form.elements[newElement];
	form.elements[newElement] = currentField;
	editElement = newElement;
	saveLocalStorage();
	form.renderForm();
	$('li#' + editElement).click();
}

function textAreaMaxLength(textareaId, max) {
	if(document.getElementById(textareaId).value.length > max) {
		document.getElementById(textareaId).value = document.getElementById(textareaId).value.substring(0,max);
     }
}