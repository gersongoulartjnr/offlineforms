$(document).ready(function(){
	setTabSelectedById('tabForms');
	myForms();
	sharedForms();
	getTotalRequests();
	
	enableModalDialogs();
});

function getTotalRequests(){
	$.ajax({
        dataType:'json',
        type:'get',
        cache:false,
        url:'form-access-request/requests.html',
        success: function(data, textStatus, jqXHR){
            if(data && parseInt(data) > 0) {
            	showTotalRequests(data);            	          	
            }
        }
    });
}

function showTotalRequests(data){
	$('#total_requests').addClass('requests');
	var txt_requests = jQuery.i18n.prop('msg_number_of_requests');
	var number_requests = data+ ' '+txt_requests;
	var html = '<a class="requests" href="'+urlContext()+'form-access-request.html">'+number_requests+'</a>';
	$('#total_requests').html(html);
}

function buildRecoveryDialog() {
	var url_context = urlContext();
	var html = "<ul>";
	for (var key in localStorage){
		var json = JSON.parse(localStorage[key]);
		if (key == 'newForm') {
			html += '<li class="form_submenu_link"><a href="'+url_context+'form.html?recover=true" target="_blank">' + json.title + ' Form</a></li>';
		} else {
			html += '<li class="form_submenu_link"><a href="'+url_context+'form.html?id='+json.url+'&recover=true" target="_blank">' + json.title + ' Form</a></li>';
		}
	}
	html += "</ul>";
	$('#dialog-recovery p').append(html);
		
    $( "#dialog-recovery" ).dialog({
		resizable: true,
		title:  jQuery.i18n.prop('msg_form_recover_title'),
		width: 500,
		modal: true,
		closeText: "hide",
		closeOnEscape: false,
		open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
		buttons: {
			"Delete all from Local": function() {
				cleanLocalStorage();
				$( this ).dialog( "close" );
			}
		}
    });
}

function cleanLocalStorage() {
	localStorage.clear();
}

function enableModalDialogs(){
	$( function() {
		$( "#modalShareFormDialog" ).dialog({
			autoOpen: false, 
			modal: true
		});
	});

	$( function() {
		$( "#modalViewAnswersDialog" ).dialog({
			autoOpen: false, 
			modal: true
		});
	});
}

function showToolTip_NewForm() {
	$ ('a#btn_new_form').each (function (){
		$(this).qtip ({
			content: { text: jQuery.i18n.prop('msg_help_create_form'), title: { text: jQuery.i18n.prop('msg_help') } },
			show: { when: false, ready: true },
			hide: { when: 'mouseout', fixed: true },
			style: { classes: 'ui-tooltip-blue ui-tooltip-rounded' },
		    position: { my: 'left top', at:"center right", target: $('#btn_new_form') }
		});
	});
}

function buildFormsTable(data, type){	
	if(type == G_MY_FORMS) {
		var titles			= [jQuery.i18n.prop('msg_ttl_title'), jQuery.i18n.prop('msg_ttl_creation'), jQuery.i18n.prop('msg_ttl_policy'), jQuery.i18n.prop('msg_ttl_collects')];
		var columns_key		= ['title', 'creationDate', 'strPolicy', 'numberOfCollects'];
		var columns_size	= ['60', '15', '20', '5'];
		var columns_sort	= ['title', 'creationDate', 'policy'];
		var myForms = new FormGrid();
		myForms.paintGrid(titles, columns_key, columns_size, columns_sort, data, G_MY_FORMS, columns_key[0]);
		
		if (localStorage.length > 0) {
			buildRecoveryDialog();
		}
	}
	if(type == G_SHARED_FORMS) {
		var titles			= [jQuery.i18n.prop('msg_ttl_title'), jQuery.i18n.prop('msg_ttl_owner'), jQuery.i18n.prop('msg_ttl_creation'), jQuery.i18n.prop('msg_ttl_policy'), jQuery.i18n.prop('msg_ttl_collects')];
		var columns_key		= ['title', 'owner', 'creationDate', 'strPolicy', 'numberOfCollects'];
		var columns_size	= ['40', '20', '15', '20', '5'];
		var columns_sort	= ['title', 'owner', 'creationDate', 'policy'];
		var sharedForms = new FormGrid();
		sharedForms.paintGrid(titles, columns_key, columns_size, columns_sort, data, G_SHARED_FORMS, columns_key[0]);
	}	
}

function myForms(orderBy, orderType, page, search_words) {
	var mOrderBy	= 'creationDate';
	var mOrderType	= 'desc';
	var mPage		= 1;
	if(orderBy != '' && orderBy != undefined) {mOrderBy = orderBy;}
	if(orderType != '' && orderType != undefined) {mOrderType = orderType;}
	if(page != '' && page != undefined) {mPage = page;}
    $.ajax({
        dataType:'json',
        type:'get',
        cache:false,
        url:'forms/my-forms.html',
        data:{
        	orderBy: mOrderBy,
        	orderType: mOrderType,
        	page:mPage,
        	searchBy: search_words
        },
        success: function(data, textStatus, jqXHR){         	
        	buildFormsTable(data, G_MY_FORMS);
        }
    });    
}

function sharedForms(orderBy, orderType, page, search_words) {
	var mOrderBy	= 'creationDate';
	var mOrderType	= 'desc';
	var mPage		= 1;
	if(orderBy != '' && orderBy != undefined) {mOrderBy = orderBy;}
	if(orderType != '' && orderType != undefined) {mOrderType = orderType;}
	if(page != '' && page != undefined) {mPage = page;}
	$.ajax({
        dataType:'json',
        type:'get',
        cache:false,
        url:'forms/shared-forms.html',
        data:{
        	orderBy: mOrderBy,
        	orderType: mOrderType,
        	page:mPage,
        	searchBy: search_words
        },
        success: function(data, textStatus, jqXHR){
        	buildFormsTable(data, G_SHARED_FORMS);
        }
    });    
}

function buildSubMenu(id, form_permissions, answer_permissions, report_permissions, reports, analytics) {
	var ttl_edit_form		= jQuery.i18n.prop('msg_menu_edit_form');
	var ttl_share_form		= jQuery.i18n.prop('msg_menu_share_form');
	var ttl_download_xml	= jQuery.i18n.prop('msg_menu_download_xml');
	var ttl_download_app	= jQuery.i18n.prop('msg_menu_download_app');
	var ttl_send_app_link	= jQuery.i18n.prop('msg_menu_send_app_link');	
	var ttl_delete_form		= jQuery.i18n.prop('msg_menu_delete_form');	
	var ttl_view_answers	= jQuery.i18n.prop('msg_menu_view_answers');
	var ttl_download_answers= jQuery.i18n.prop('msg_menu_download_answers');
	var ttl_edit_report		= jQuery.i18n.prop('msg_menu_edit_report');
	var ttl_new_report		= jQuery.i18n.prop('msg_menu_new_report');
	var ttl_view_reports	= jQuery.i18n.prop('msg_menu_view_reports');
	var ttl_edit_analytics	= jQuery.i18n.prop('msg_menu_edit_analytics');
	var ttl_new_analytics	= jQuery.i18n.prop('msg_menu_new_analytics');
	var ttl_view_analytics	= jQuery.i18n.prop('msg_menu_view_analytics');
	var er,
		vr,
		ar;
	var url_context = urlContext();
	var html = '';
	if(form_permissions.update == true) {
		html += '<li><a class="sub_menu" href="'+url_context+'form.html?id='+id+'" target="_blank"><img class="sub_menu" src="images/edit.png" />'+ttl_edit_form+'</a></li>';		
	}
	if(form_permissions.share == true) {
		html += '<li><a class="sub_menu" href="'+url_context+'form-share.html?id='+id+'"><img class="sub_menu" src="images/form/share.png" />'+ttl_share_form+'</a></li>';
	}
	if(form_permissions.read == true) {
		html += '<li><a class="sub_menu" href="#" onclick="downloadXML('+"'"+id+"'"+')"><img class="sub_menu" src="images/form/save.png" />'+ttl_download_xml+'</a></li>';
		html += '<li><a class="sub_menu" href="#" onclick="downloadApp('+"'"+id+"'"+')"><img class="sub_menu" src="images/form/app-mobile.png" />'+ttl_download_app+'</a></li>';
		html += '<li><a class="sub_menu" href="#" onclick="sendAppLink('+"'"+id+"'"+')"><img class="sub_menu" src="images/form/app-mobile.png" />'+ttl_send_app_link+'</a></li>';
	}
	if(form_permissions.remove == true) {
		html += '<li><a class="sub_menu" href="#" onclick="removeForm('+"'"+id+"'"+')"><img class="sub_menu" src="images/remove.png" />'+ttl_delete_form+'</a></li>';
	}
	if(answer_permissions.read == true) {
		html += '<li><a class="sub_menu" href="'+url_context+'answers.html?id='+id+'"><img class="sub_menu" src="images/view_doc.png" />'+ttl_view_answers+'</a></li>';
		html += '<li><a class="sub_menu" href="#"><img class="sub_menu" src="static/images/download_answers.png" />'+ttl_download_answers+' <span class="arrow">&#9658;</span></a>';
			html += '<ul>';
			html += '<li><a class="sub_menu" href="#" onclick="downloadCsvAnswers('+"'"+id+"'"+')"><img class="sub_menu" src="static/images/csv.png" />CSV</a></li>';
			html += '<li><a class="sub_menu" href="#" onclick="downloadXmlAnswers('+"'"+id+"'"+')"><img class="sub_menu" src="static/images/xml.png" />XML</a></li>';
			html += '</ul>';	
		html += '</li>';
	}
	
	if(report_permissions.read == true && report_permissions.update == true) {
		html += '<li><a class="sub_menu" href="#"><img class="sub_menu" src="images/form/edit_report.png" />'+ttl_edit_report+' <span class="arrow">&#9658;</span></a><ul>';
			html += '<li><a class="sub_menu" href="'+url_context+'report.html?form='+id+'" target="_blank"><img class="sub_menu" src="images/form/new_report.png" />'+ttl_new_report+'</a></li>';
			if(reports != undefined && reports != null){
				for(er = 0; er < reports.length; er++){		
					var obj = reports[er];
					html += '<li><a class="sub_menu" href="'+url_context+'report.html?form='+id+'&report='+obj['reportId']+'" target="_blank"><img class="sub_menu" src="images/form/report.png" />'+obj['reportName']+'</a></li>';
				}
			}
		html += '</ul>';	
		html += '</li>';
		
		if(reports != undefined && reports != null && reports.length > 0){
			html += '<li><a class="sub_menu" href="#"><img class="sub_menu" src="images/form/view_reports.png" />'+ttl_view_reports+' <span class="arrow">&#9658;</span></a>';
			html += '<ul>';
			for(vr = 0; vr < reports.length; vr++){
				var obj = reports[vr];
				html += '<li><a class="sub_menu" href="'+url_context+'report-viewer.html?id='+obj['reportId']+'"><img class="sub_menu" src="images/form/view_reports.png" />'+obj['reportName']+'</a></li>';
			}
			html += '</ul>';
			html += '</li>';
		}
	}
	//Analytics
	if(report_permissions.read == true && report_permissions.update == true) {
		html += '<li><a class="sub_menu" href="#"><img class="sub_menu" src="images/form/edit_report.png" />'+ttl_edit_analytics+' <span class="arrow">&#9658;</span></a><ul>';
			html += '<li><a class="sub_menu" href="'+url_context+'analytics-editor.html?form='+id+'" target="_blank"><img class="sub_menu" src="images/form/new_report.png" />'+ttl_new_analytics+'</a></li>';
			if(analytics != undefined && analytics != null){
				for(ar = 0; ar < analytics.length; ar++){		
					var obj = analytics[ar];
					html += '<li><a class="sub_menu" href="'+url_context+'analytics-editor.html?form='+id+'&analytics='+obj['id']+'" target="_blank"><img class="sub_menu" src="images/form/report.png" />'+obj['name']+'</a></li>';
				}
			}
		html += '</ul>';	
		html += '</li>';
		
		if(analytics != undefined && analytics != null && analytics.length > 0){
			html += '<li><a class="sub_menu" href="#"><img class="sub_menu" src="images/form/view_reports.png" />'+ttl_view_analytics+' <span class="arrow">&#9658;</span></a>';
			html += '<ul>';
			for(ar = 0; ar < analytics.length; ar++){
				var obj = analytics[ar];
				html += '<li><a class="sub_menu" href="'+url_context+'analytics-viewer.html?id='+obj['id']+'"><img class="sub_menu" src="images/form/view_reports.png" />'+obj['name']+'</a></li>';
			}
			html += '</ul>';
			html += '</li>';
		}
	}
	return html;
}

function removeForm(formId){
	$('#dialog-confirm p').append(jQuery.i18n.prop('msg_form_messageDialogDeleteForm'));
	$( "#dialog-confirm").dialog({
		autoOpen: false,
		modal: true,
		title: 'Delete',
		width: 500,
		buttons: {
			Remove : function() {
				doRemoveFromList(formId);		
				$(this).dialog( "close" );
			},
			Cancel: function() {
				$(this).dialog( "close" );
			}
		}
	});
	$("#dialog-confirm").dialog('open');
}

function doRemoveFromList(formId) {
	var data = JSON.stringify({ "url" : formId });
	$.ajax({
    	url:'form.html?delete',
        type : "POST",
        traditional : true,
        contentType : "application/json",
        dataType : "json",
        data : data,
        success : function(data) {
        	if('success' == data.type.toLowerCase()){
        		checkRemove(data, formId);
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

function checkRemove(data, id) {
	if(data.type == 'SUCCESS') {
		addMessage(data.message, 'info');
		$('#'+id).remove();
	}
	else {
		addMessage(data.message, 'error');
	}
}