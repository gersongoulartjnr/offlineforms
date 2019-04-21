$(document).ready(function(){
	initReportEditor();
	updateReportTitle();
	$('#saveReport').click(function() {
		sendReportToServer();
	});
	$('#clearReport').click(function() {
		clearReport();
	});
	$('#viewReport').click(function() {
		viewReport();
	});
	$('#deleteReport').click(function() {
		deleteReport();
	});
	
	$('#start_date').datepicker({
		maxDate: '0',
        dateFormat: 'yy-mm-dd'
    });
	$('#finish_date').datepicker({
        dateFormat: 'yy-mm-dd'
    });
});

var reportFormClass	= null;
var reportClass		= null;
var reportIdItem	= null;

//Common
function createMessageDialog(messageDialog){
	var message = '<span id="messageDialogConfirm">';
	message += messageDialog; 
	message += '</span>';
	
	return message;
}

function viewReport(){
	var report_id = $('#report_id').val();
	if(report_id == undefined || report_id == '')
		return;
	
	window.location = urlContext() + "report-viewer.html?id="+report_id;
}

function clearReport(){
	var message = createMessageDialog(jQuery.i18n.prop('msg_form_messageDialogClean'));
	$('#dialog-confirm p').append(message);
	
	if(reportClass.elements.length > 0) {
		$( "#dialog-confirm" ).dialog({
			autoOpen: false,
			modal: true,
			title: jQuery.i18n.prop('msg_form_clearDialogTitle'),
			width: 500,
			buttons: {
				Clear : function() {
					reportClass.elements = [];
					reportClass.renderReport();
					$('#rc-properties').hide();
					
					$(this).dialog( "close" );
				},
				Cancel: function() {
					$(this).dialog("close");
				}
			}
		});
		$("#dialog-confirm").dialog('open');
	} else {
		addMessage(jQuery.i18n.prop('msg_form_clearMessageInfo'), 'info');
	}	
	$('#messageDialogConfirm').remove();
}

function deleteReport() {
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
				$(this).dialog("close");
			},
			Cancel: function() {
				$(this).dialog("close");
			}
		}
	});
	$("#dialog-confirm").dialog('open');	
	$('#messageDialogConfirm').remove();
}

function deleteFormAjax(){
	var form = getParameter('form');
	var report = getParameter('report');
	$.ajax({
        dataType:'json',
        type:'post',
        cache:false,
        url:'report.html?delete',
        data:{
        	form: form,
        	report: report
        },
        success: function(data, textStatus, jqXHR){
            if(data) {
            	if(data.type == 'SUCCESS'){
            		window.location = urlContext() + "forms.html";
            		addMessage(data.message, 'info');
            	}
            	else{
            		addMessage(data.message, 'error');
            	}	
            }
            else {
            	addMessage(data.message, 'error');
            	return;
            }
        },
        error: function(data, textStatus, jqXHR){
        	//console.log("Could not retrieve data from API.");
            return;
        }
    });
}

function updateReportTitle(){
	var report_title = $('#report_title').val();
	if(report_title == ''){
		report_title = 'Report';
	}
	reportClass.title = report_title;
	$('#lgd_report').html(report_title);
}

function initReportEditor(){
	reportFormClass = new ReportFormClass();
	reportClass = new ReportClass();
	var form_xml = $("#form_xml").val();
	var report_xml = $("#report_xml").val();
	if(form_xml != undefined && form_xml != '') {
		try{
			loadQuestionsFromXML(form_xml);
			checkReportFormFields(reportFormClass.container);
		} catch(err) {
			addMessage(jQuery.i18n.prop('msg_error_loading_report'), 'error');
		}
	}
	
	if(report_xml != undefined && report_xml != '') {
		try{
			loadReportFromXML(report_xml);
			checkReportFields(reportClass.container);
		} catch(err) {
			addMessage(jQuery.i18n.prop('msg_error_loading_report'), 'error');
		}
	}
}

function loadQuestionsFromXML(xml){
	try{
		var form_xml = $.parseXML(xml);
	}catch(err){
		addMessage(jQuery.i18n.prop('msg_error_xmlprocess'), 'error');
		return;
	}
	var $xml = $( form_xml );
    var size = $xml.find('questions').size();
    try{
    	if(size == 0){
    		throw "no questions";
    	}
    	$xml.find('questions').each(function(){
	    	var elems = this.childNodes;
	    	for(var i = 0; i<elems.length; i++){
	    		var field = fieldFactoryMethod(elems[i].nodeName);
	    		if(field != null){
	    			field.setFormFromXMLDoc(elems[i]);
	    			reportFormClass.elements.push(field);
	    		 }
	    	}
    	});
    	reportFormClass.renderForm();
    }catch(err){
    	//console.log('err: ' + err);
    	addMessage(jQuery.i18n.prop('msg_error_noquestions'), 'warn');
    }	
}

function loadReportFromXML(xml){
	try{
		var report_xml = $.parseXML(xml);
	}catch(err){
		addMessage(jQuery.i18n.prop('msg_error_xmlprocess'), 'error');
		return;
	}
	var $xml = $(report_xml);
	var size = $xml.find('items').size();
	 try{
    	if(size == 0){
    		throw "no items";
    	}
    	$xml.find('items').each(function(){
    		var elems = this.childNodes;	
    		for(var i in elems){
    			var field = fieldFactoryMethod(elems[i].nodeName);
    			if(field != null){
    				field.setReportFromXMLDoc(elems[i]);
    				field.setExtraValues(reportFormClass.elements[field.question].values_titles);
    				reportClass.elements.push(field);    				
    			}
    		}
    	});
    	reportClass.renderReport();
	 }catch(err){
		 	//no items
	    	addMessage(jQuery.i18n.prop('msg_error_noquestions'), 'warn');
	    }	
}

function checkReportFormFields(container_id){
	$('#'+container_id+' ol li').click(function(){
		$('#'+container_id+' ol li').removeClass('selected');
		$(this).addClass('selected');
		var fieldId =  $(this).attr('id');
		var id = parseInt(fieldId.split("_", 2)[1]);
		addReportItem(id);
	});	
}

function checkReportFields(container_id){
	$('#'+container_id+' ol li').click(function(){
		$('#'+container_id+' ol li').removeClass('selected');
		$(this).addClass('selected');
		
		var fieldId =  $(this).attr('id');
		var id = parseInt(fieldId.split("_", 2)[1]);
		editReportField(id);
	});	
}

function addReportItem(formId){
	var form_field		= reportFormClass.elements[parseInt(formId)];
	var type			= form_field.type;	
	var report_field	= fieldFactoryMethod(type, formId);	
	reportClass.elements.push(report_field);
	var lastId	= reportClass.elements.length - 1;
	reportClass.renderReport();
	checkReportFields(reportClass.container);	
	$('#r_'+lastId).addClass('selected');
	editReportField(lastId);
}

function removeReportItem(reportId){
	reportClass.elements.splice(reportId, 1);
	reportClass.renderReport();
	checkReportFields(reportClass.container);
	$('#fs-rc-properties table').remove();
	$("#fs-rc-properties").hide();
}

function editReportField(reportId){
	$("#fs-rc-properties").show();
	$('#fs-rc-properties table').remove();
	reportIdItem = reportId;
	var element = reportClass.elements[reportId];
	element.setExtraValues(reportFormClass.elements[element.question].values_titles);
	element.showFieldProperties(reportClass.prop_container);	
}
/**
 * Save the properties to ReportField
 */
function saveField(){
	var element = reportClass.elements[reportIdItem];
	element.saveProperties();
	//
	$('#fs-rc-properties table').remove();
	element.showFieldProperties(reportClass.prop_container);
	//
	reportClass.renderReport();
	checkReportFields(reportClass.container);
	$('#r_'+reportIdItem).addClass('selected');
}

function moveElement(value) {
	if (Math.abs(value) != 1) {
		return;
	}

	if (value == 1 && reportIdItem == reportClass.elements.length - 1) {
		return;
	} else if (value == -1 && reportIdItem == 0) {
		return;
	}

	var currentElement	= reportClass.elements[reportIdItem];
	var newElement		= reportIdItem + value;

	reportClass.elements[reportIdItem]	= reportClass.elements[newElement];
	reportClass.elements[newElement]	= currentElement;
	reportIdItem = newElement;
	reportClass.renderReport();
	checkReportFields(reportClass.container);
	$('#r_'+reportIdItem).addClass('selected');
	//$('li#r_'+reportIdItem).click();
}

function sendReportToServer(){
	if($('#report_title').val() == ''){
		addMessage(jQuery.i18n.prop('msg_report_title_required'), 'error');
    	return;
	}
	if($('#report_start').val == '' || $('#report_finish').val() == ''){
		addMessage(jQuery.i18n.prop('msg_report_date_required'), 'error');
    	return;
	}
	if(reportClass.elements.length == 0){
    	addMessage(jQuery.i18n.prop('msg_report_xml_required'), 'error');
    	return;
    }	
	if(!reportClass.validateReport()){
		return;
	}
	
	if($('#form_url').val() != ''){
		var xml = reportClass.toXML();
		$('#report_xml').attr("value", xml);
		$('#reportForm').submit();
	}
}