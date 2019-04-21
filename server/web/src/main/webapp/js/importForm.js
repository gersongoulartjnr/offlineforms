$(document).ready(function() {
	
	prepareLoadXmlFile();
	$("#importXmlFile").click(function(){
		importXmlFile();
	});
	$("#importFromUrl").click(function(){
		importFromUrl();
	});
	
	$(function() {
		$("#tabsholder").tytabs({
			tabinit:"1",
			fadespeed:"fast"
		});
	});
});

function prepareLoadXmlFile(){
    if (window.File && window.FileList && window.FileReader) {
        var filesInput = document.getElementById("xmlFile");
        filesInput.addEventListener("change", function(event) {
            var files = event.target.files;
            for (var i = 0; i < files.length; i++) {
                var file = files[i];
                if (!file.type.match('xml')) continue;
                var picReader = new FileReader();
                picReader.addEventListener("load", function(event) {
                    var textFile = event.target;
                    $('#fileName').attr('value', file.name);
                    $('#fileContent').attr('value', textFile.result);
                });
                picReader.readAsText(file);
            }
        });
    }
    else {
    	addMessage('Please update your browser, it does not support File API', 'warn');
    }
}

function importXmlFile(){
	var file_name = $('#fileName').val();
	var file_content = $('#fileContent').val();
	if(file_name.trim() == ''){
		addMessage(jQuery.i18n.prop('err_xml_filename_required'), 'error');
		return false;
	}
	if(file_content.trim() == ''){
		addMessage(jQuery.i18n.prop('err_xml_filecontent_required'), 'error');
		return false;
	}
    var data = JSON.stringify({ "fileName" : "'"+file_name+"'", "fileContent" : "'"+file_content+"'" });
    $.ajax({
    	url:'import.html?xml-file',
        type : "POST",
        traditional : true,
        contentType : "application/json",
        dataType : "json",
        data : data,
        success : function(data) {
        	if('success' == data.type.toLowerCase()){
        		$('#dialog-import').dialog("close");
        		$('#form_title').attr('value', file_name);
        		$("#form_xml").attr('value', file_content);
        		initFormEditor(file_name);
        		addMessage(jQuery.i18n.prop('suc_form_was_imported'), 'success');
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

function importFromUrl(){
	var xml_url = $('#xmlUrl').val();
	if(xml_url.trim() == ''){
		addMessage(jQuery.i18n.prop('err_xml_url_required'), 'error');
		return false;
	}
    var data = JSON.stringify({ "url" : xml_url });
    $.ajax({
    	url:'import.html?xml-url',
        type : "POST",
        traditional : true,
        contentType : "application/json",
        dataType : "json",
        data : data,
        success : function(data) {
        	if('success' == data.type.toLowerCase()){
        		$('#dialog-import').dialog("close");
        		$('#form_title').attr('value', 'new Form');
        		$("#form_xml").attr('value', data.message);
        		initFormEditor('new Form');
        		addMessage(jQuery.i18n.prop('suc_form_was_imported'), 'success');
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