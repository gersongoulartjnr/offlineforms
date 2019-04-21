$(document).ready(function(){	
	loadFormFields();
	loadFormCollectors();
	initAnalyticsEditor();	
	
	$('#addAnalyticsItem').click(function() {
		addAnalyticsItem();
	});
	
	$('#saveAnalytics').click(function() {
		sendAnalyticsDataToServer();
	});
	$('#deleteAnalytics').click(function() {
		deleteAnalytics();
	});	
	$('#viewAnalytics').click(function() {
		console.log('view ... ');
		viewAnalytics();
	});
	$('#clearAnalytics').click(function() {
		clearAnalytics();
	});
	
	$('#filter_start_date').datepicker({
		maxDate: '0',
        dateFormat: 'yy-mm-dd'
    });
	$('#filter_end_date').datepicker({
        dateFormat: 'yy-mm-dd'
    });
});

function viewAnalytics(){
	var analytics_id = $('#analytics_id').val();
	if(analytics_id == undefined || analytics_id == '')
		return;
	
	window.location = urlContext() + "analytics-viewer.html?id="+analytics_id;
}

function deleteAnalytics(){
	$('#dialog-confirm p').append(jQuery.i18n.prop('msg_remove_analytics'));

	$( "#dialog-confirm").dialog({
			autoOpen: false,
			modal: true,
			title: 'Delete',
			width: 500,
			buttons: {
				Remove : function() {
					doDelete();		
					$(this).dialog( "close" );
				},
				Cancel: function() {
					$(this).dialog( "close" );
				}
			}
		});
		$("#dialog-confirm").dialog('open');
	
	$('#messageDialogConfirm').remove();
}

function doDelete(){
	var analytics_id = getParameter('analytics');
	$.ajax({
        dataType:'json',
        type:'post',
        cache:false,
        url:'analytics-editor/delete.html',
        data:{
        	id: analytics_id
        },
        success: function(data, textStatus, jqXHR){
        	if('success' == data.type.toLowerCase()){        		
        		window.location =  urlContext() + 'forms.html';
        	}
        	else{
        		addMessage(data.message, data.type.toLowerCase());
        	}
        }
    }); 
    return false;
}

////

function clearAnalytics(){
	ANALYTICS_CONTAINER = null;
	A_FORM_COLLECTORS	= [];
	A_ID_ELEMENT		= null;
	A_ID_TRANSFORMATION	= null;
	FIELD_GEOLOCATION	= null;
	HAS_GEOLOCATION		= false;
	A_FORM_QUESTIONS	= [];
	A_TRANSFORMATIONS	= [];
	
	$('#analytics_title').val('');
	$('#item_title').val('');	
	$('#ac-l-item-name').html('');
	$('#ac-l-side-body').html('');
	
	loadFormFields();
	loadFormCollectors();
	initAnalyticsEditor();
}
/**
 * Add a new analytics item
 */
function addAnalyticsItem(){	
}

function initAnalyticsEditor(){
	var analytics_doc = $("#analytics_doc").val();
//console.log('json: ' + analytics_doc);
	
	if(analytics_doc != undefined && analytics_doc != '') {
		try{
			loadAnalyticsFromJSON(analytics_doc);
		} catch(err) {
			addMessage(jQuery.i18n.prop('msg_error_loading_report'), 'error');
		}
	} else {
		ANALYTICS_CONTAINER = new AnalyticsContainerClass();
		ANALYTICS_CONTAINER.renderMainView();
	}
	
	updateAnalyticsTitle();
	updateItemName();
}

/*function initTransformations(){
	$('#d-ac-transformation li').click(function() {
		$('#d-ac-transformation ol li').removeClass('item_transf_act');
		$('#d-ac-transformation ol li').addClass('item_transf');
		$(this).addClass('item_transf_act');

		var id = $(this).attr('id');
		editTransformationItem(id);
		$(this).click(function(){ return false; });
	});
}*/

/**
 * Fill the combobox with the form collectors
 */
function loadFormCollectors(){
	var collectors_list = $('#form_collectors').val();
	if(collectors_list != ''){
		var form_collectors = JSON.parse($('#form_collectors').val());
		if(form_collectors.length > 0){
			for(var i in form_collectors){
				A_FORM_COLLECTORS.push(form_collectors[i]);
			}
		}		
	}
}

function loadAnalyticsFromJSON(json){
	var json_doc = JSON.parse(json);
	if(json_doc.elements.length > 0){
		ANALYTICS_CONTAINER = new AnalyticsContainerClass();
		for(var e in json_doc.elements){
			var analyticsItem = new AnalyticsItemClass();
			analyticsItem.getElementDataFromJson(json_doc.elements[e]);
			ANALYTICS_CONTAINER.elements.push(analyticsItem);
		}
	}
	ANALYTICS_CONTAINER.renderMainView();
}

/**
 * Update the analytics title
 */
function updateAnalyticsTitle(){
	var var_title = $('#analytics_title').val();
	if(var_title == ''){
		var_title = 'Title';
	}
	$('#ac-l-item-name').html(var_title);
}
/**
 * Update the name for the current analytics item
 */
function updateItemName(){
	var item_title = $('#item_title').val();
	if(item_title == ''){
		item_title = 'Name';
	}
	ANALYTICS_CONTAINER.elements[A_ID_ELEMENT].item_name = item_title;
	$('#ac-l-side-body').html(item_title);
}

/**
 * Save the filter for the current analytics item
 */
function saveFilterView(){
	if(ANALYTICS_CONTAINER != null){
		var element = ANALYTICS_CONTAINER.elements[A_ID_ELEMENT];
		ANALYTICS_CONTAINER.current_side = ITEM_FILTER;
		element.saveData();
		element.drawProcess();
	} else {
		addMessage(jQuery.i18n.prop('msg_error_noquestions'), 'error');
	}
}
/**
 * Save the transformation for the current analytics item
 */
function saveTransformationItem(parent_list){
	if(ANALYTICS_CONTAINER != null){
		var element = ANALYTICS_CONTAINER.elements[A_ID_ELEMENT];
		if(parent_list == TRA_MAIN){
			ANALYTICS_CONTAINER.current_side = ITEM_TRANSFORMATION;
		} else if(parent_list == TRA_SECOND){
			ANALYTICS_CONTAINER.current_side = ITEM_TRANSFORMATION_2;
		}
		element.saveData();
		element.drawProcess();
	} else {
		addMessage(jQuery.i18n.prop('msg_error_noquestions'), 'error');
	}
}

/**
 * Add a transformation for the current analytics item
 */
function addTransformation(list_type){
	var element = ANALYTICS_CONTAINER.elements[A_ID_ELEMENT];
	var question_sel = $('#'+getTransformationPrefix(list_type)+TRA_FIELDS).val();
	if(question_sel == undefined || question_sel == null || question_sel == '-1'){
		//ADD AN ERROR MESSAGE: select 1 question item
		return;
	}
	var transf_sel = $('#'+getTransformationPrefix(list_type)+TRA_TRAS).val();
	if(transf_sel == undefined || transf_sel == null || transf_sel == '-1'){
		//ADD AN ERROR MESSAGE: select 1 transformation item/element
		return;
	}
	
	var question_elem = A_FORM_QUESTIONS[parseInt(question_sel)];
	if(question_elem == undefined || question_elem == null){
		//ADD AN ERROR MESSAGE: That question cannot be 
		return;	
	}
	//
	if(list_type == TRA_MAIN){
		element.list_transformation.addTransformation(question_elem, transf_sel);
	} else if(list_type == TRA_SECOND){
		element.list_transformation2.addTransformation(question_elem, transf_sel);
	}
}
/**
 * Add a view for the current analytics item
 */
function saveView(){
	if(ANALYTICS_CONTAINER != null){
		var element = ANALYTICS_CONTAINER.elements[A_ID_ELEMENT];
		ANALYTICS_CONTAINER.current_side = ITEM_VIEW;
		element.saveData();
		element.drawProcess();
	} else {
		addMessage(jQuery.i18n.prop('msg_error_noquestions'), 'error');
	}
}

/**
 * Edit a transformation item
 */
function editTransformationItem(tra_id, list_type){
	if(tra_id == undefined || tra_id == null){
		return;
	}
	var element = ANALYTICS_CONTAINER.elements[A_ID_ELEMENT];
	element.list_transformation.editTransformation(tra_id);
}

function moveTransformation(id, num, list_type){
	if (Math.abs(num) != 1) {
		return;
	}
	var element = ANALYTICS_CONTAINER.elements[A_ID_ELEMENT];
	if(element == null){
		return;
	}	
	if (num == 1 && id == (element.list_transformation.t_items.length-1)) {
		return;
	} else if (num == -1 && id == 0) {
		return;
	}	
	
	id = parseInt(id);
	num = parseInt(num);
	var newId = id + num;
	element.list_transformation.t_items.splice(newId, 0, element.list_transformation.t_items.splice(id, 1)[0]);
	editTransformationItem(newId, list_type);
}
function deleteTransformation(id, list_type){
	var element = ANALYTICS_CONTAINER.elements[A_ID_ELEMENT];
	if(element == null){
		return;
	}
	element.list_transformation.t_items.splice(id, 1);
	var newId = parseInt(id);
	editTransformationItem(newId, list_type);
}

/**
 * Geolocation
 */
function saveGeolocationFilter(){
	var _filter_location = $('#'+ID_LOCATION+':checked').val();
	
	if(_filter_location != undefined && _filter_location != ''){
		if(navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(showUserLocation);
		} else{
			//ERROR
			//"Geolocation is not supported by this browser.";
		}
	}
}
function showUserLocation(position){	
	var element = ANALYTICS_CONTAINER.elements[A_ID_ELEMENT];
	if(element == null){
		return;
	}
	
	var value_location = element.item_filter.specificViewForGeolocation($('#'+ID_LOCATION+':checked').val());
	$('#'+ID_VALUE_LOCATION).html(value_location);
	
	element.item_filter.loc_latitude	= position.coords.latitude;
	element.item_filter.loc_longitude	= position.coords.longitude;
	$('#'+ID_USER_LOCATION).html(drawUserLocation(element.item_filter.loc_latitude, element.item_filter.loc_longitude));
	$('#'+ID_USER_LOCATION).addClass(CSS_USER_LOCATION);
	
	ANALYTICS_CONTAINER.current_side = ITEM_FILTER;
	element.saveData();
	element.drawProcess();
	
	//showMap(element.item_filter.loc_latitude, element.item_filter.loc_longitude)
}
function drawUserLocation(_latitude, _longitude){
	var html = '<ol>';
		html += '<li><span class="text_bold">'+LBL_LATITUDE+':</span> '+_latitude+'</li>';
		html += '<li><span class="text_bold">'+LBL_LONGITUDE+':</span> '+_longitude+'</li>';
	html += '</ol>';
	return html;
}
function showMap(_latitude, _longitude){
	var map;
            var mapOptions = {
                      zoom: 12,
                      center: new google.maps.LatLng(_latitude, _longitude),
                      mapTypeId: google.maps.MapTypeId.ROADMAP
                    };
                                    
            map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);
}

/**
 * Get the transformation type BY a question type
 * By the list type(see AnalyticsTransformationClass)
 */
function getTransfByType(list_type){
	var val_field = $('#'+getTransformationPrefix(list_type)+TRA_FIELDS).val();	
	if(val_field != undefined && val_field != null){
		var transfs = [];
		if(list_type == TRA_MAIN){
			transfs = getTransformationsByQuestionType(A_FORM_QUESTIONS[parseInt(val_field)].type);
		} else if(list_type == TRA_SECOND){
			transfs = getSecTransformationsByQuestionType(A_FORM_QUESTIONS[parseInt(val_field)].type);
		}
		else{
			return;
		}
			
		//var transfs = transformationsByQuestionType(A_FORM_QUESTIONS[parseInt(val_field)].type);
		var html = '';
		if(transfs.length > 0){
			for(var i in transfs){
				html += '<option value="'+transfs[i]+'">'+transfs[i]+'</option>';
			}
		} else {
			html += '<option value="-1">Choose one</option>';
		}
		$('#'+getTransformationPrefix(list_type)+TRA_TRAS).html(html);
	}	
}

function getTransformationPrefix(list_type){
	if(list_type == TRA_MAIN){
		return PREFIX_TRA_MAIN;
	} else if(list_type == TRA_SECOND){
		return PREFIX_TRA_SEC;
	}
	return '';
}

/**
 * Save the Analytics in the server
 * @returns {Boolean}
 */
function sendAnalyticsDataToServer(){	
	var a_title = $('#analytics_title').val();
	if(a_title == undefined || a_title == null || "" == a_title){
		addMessage(jQuery.i18n.prop('err_analytics_title_required'), 'error');
		return;
	}
	if(!ANALYTICS_CONTAINER.validateData()){
		return;
	}
	var analytics_doc = ANALYTICS_CONTAINER.toJson();
	var form_url = $('#form_url').val();
	
	var analytics_id = $('#analytics_id').val();
	var data;
	if(analytics_id == ''){
		data = JSON.stringify({ "formUrl": form_url, "name": a_title, "doc": analytics_doc });
	} else {
		data = JSON.stringify({ "formUrl": form_url, "name": a_title, "doc": analytics_doc, "id": analytics_id });
	}
	
    $.ajax({
    	url: 'analytics-editor.html',
        type : "POST",
        traditional : true,
        contentType : "application/json",
        dataType : "json",
        data : data,
        success : function(data) {
        	if('success' == data.type.toLowerCase()){
        		addMessage(data.message, data.type.toLowerCase());
        		updateUrl(form_url, data.data);
        		updateAnalyticsIdField(data.data);
        	} else{
        		addMessage(data.message, data.type.toLowerCase());
        	}
        },
        error : function (response) {
            addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
        },
    });
    return false;
}

function updateUrl(form_url, analytics_id){
	window.history.pushState('analytics-editor', '', 'analytics-editor.html?form='+form_url+'&analytics='+analytics_id);
}
function updateAnalyticsIdField(analytics_id){
	$('#analytics_id').val(analytics_id);	
}

function drawArrows(){
	arrow_initialize('ac-side-body', 'a_main_process');
	var element = ANALYTICS_CONTAINER.elements[A_ID_ELEMENT];
	//if(element.list_transformation != null && element.list_transformation.length > 0){
		arrow('a_main_process', 'ac-sb-filter', 4, 'ac-sb-transformation', 2, '#4A5B5D', 2, '#C0C0C0', 2);
	//}
}