var A_FORM_QUESTIONS	= [];
var MULTIMEDIA_URL		= '';
var A_VIEWER = [];

/**
 * List of Views
 */
var V_GRID		= { 'name' : 'GRID' };
var V_BARCHART	= { 'name' : 'BARCHART' };
var V_PIE		= { 'name' : 'PIE' };
var V_SLIDER	= { 'name' : 'SLIDER' };
var V_MAP		= { 'name' : 'MAP' };


$(document).ready(function(){
	MULTIMEDIA_URL = $('#uri_server').val();
	loadQuestionsFromXml($('#form_xml').val());
	initAnalyticsViewer($('#a_doc').val());
});

function loadQuestionsFromXml(doc_xml){
	loadFormFields();
}
/****************/ 
function loadFormFields(){
	var form_xml = $("#form_xml").val();
	if(form_xml != undefined && form_xml != '') {
		try{
			loadQuestionsFromXML(form_xml);
		} catch(err) {
			addMessage(jQuery.i18n.prop('error_analyst_load_questions'), 'error');
		}
	}
}
function loadQuestionsFromXML(xml){
	try{
		var form_xml = $.parseXML(xml);	
	} catch(err){
		addMessage(jQuery.i18n.prop('msg_error_xmlprocess'), 'error');
		return;
	}
	var $xml = $( form_xml );
    var size = $xml.find('questions').size();    
    
    try{
    	if(size == 0){
    		throw "No questions";
    	}
    	$xml.find('questions').each(function(){
	    	var elems = this.childNodes;
	    	for(var i = 0; i<elems.length; i++){
	    		var field = analyticsFieldFactoryMethod(elems[i].nodeName);
	    		if(field != null){
	    			field.setDataFromXmlDoc(elems[i]);
	    			A_FORM_QUESTIONS.push(field);
	    		}
	    	}
    	});
    } catch(err){
    	addMessage(jQuery.i18n.prop('msg_error_noquestions'), 'error');
    }	
}
/* Questions */
var analyticsFieldFactoryMethod = function analyticsFieldFactoryMethod(type){	
	var field = new AnalyticsFormFieldClass(type);	
	return field;
};

var AnalyticsFormFieldClass = function AnalyticsFormFieldClass(type){
	this.id		= '';
	this.type	= type;
	this.title	= '';
	this.options = [];
	
	this.txt_id		= 'id';
	this.txt_label	= 'label';
	
	this.setDataFromXmlDoc = function setDataFromXmlDoc(xmlDoc){		
		var $xmlDoc = $(xmlDoc);
		if(xmlDoc.nodeName != this.type){
			return;
		}
		this.id		= $xmlDoc.attr(this.txt_id);
		this.title	= $xmlDoc.find(this.txt_label).text();
		if(this.type == 'combobox' || this.type == 'checkbox' || this.type == 'radio'){
			var options_parsed = [];		
			var options_found  = $xmlDoc.find('option');
			for(var i=0; i<options_found.length; i++){
				var _key = null;
				for(var a in options_found[i].attributes){
					if(options_found[i].attributes[a].nodeName == 'value'){
						_key = options_found[i].attributes[a].value;						
					}
				}
				options_parsed.push({'key': _key, 'value': options_found[i].textContent});
			}
			this.options = options_parsed;
		}
	}; 
};

/**
 * @param item id
 * @returns {Array}: {latitude, longitude}
 */
function getPositionFromDoc(id){
	var a_doc = $('#a_doc').val();
	var json_doc = JSON.parse(a_doc);
	var position = [];
	var element = json_doc.elements[id];
	if(element != undefined){
		position.push(element.filter.geolocation.latitude);
		position.push(element.filter.geolocation.longitude);
	}
	return position;
}

function initAnalyticsViewer(aDoc){
	var json_doc = JSON.parse(aDoc);
	var elements_size = json_doc.elements.length;
	var i;
	if(elements_size > 0){
		for(i = 0; i < elements_size; i++){
			var item = aViewerOptionFactory(i, json_doc.elements[i].view.type);
			for(var f in json_doc.elements[i].filter.fields){
				item.fields.push(f);
			}
			//
			A_VIEWER.push(item);
			loadData(i);
		}
	}
}

function buildAnalyticsItemJson(_item){
	var form_id = $('#form_id').val();
	var analytics_id = $('#a_id').val();
	
	var obj = '{"formId":"' + form_id
		+'", "analyticsId":"' + analytics_id
		+'", "itemId":'+ _item
		+'}';
	return obj;
}

function loadData(item_id){
	var access_token = $('#access_token').val();
	
	var analyticsItem = buildAnalyticsItemJson(item_id);
	
	$.ajax({
		contentType:"application/json; charset=utf-8",
        dataType:'json',
        type:'post',
        cache:false,
        url:'ws/analytics/analyticsItem?oauth_token='+access_token,
        data:analyticsItem,
        success: function(data, textStatus, jqXHR){
            drawItemView(item_id, data);
        },
        error: function(data, textStatus, jqXHR){
            return;
        }
    });	
}

function drawItemView(item_id, data){
	var json_doc = JSON.parse(data.result);
	var item = A_VIEWER[item_id];
	item.drawView(json_doc);	
}

/**/
function parseDateComponent(value){
	var _value = new Date(value);
	if(_value != undefined || _value != null){
		var _date_parsed = getDateParsed(_value);
	    return _date_parsed;
	} else {
		return '';
	}
}
function parseCreationDate(value){
	var _value = new Date(value);
	if(_value != undefined || _value != null){
		return getDateParsed(_value) +' '+getTimeParsed(_value);
	} else {
		return '';
	}
}
function getDateParsed(_date){
	var _days	= _date.getDate();
	var _months	= _date.getMonth() + 1;
	var _years	= _date.getFullYear();
    return (_days <= 9 ? '0' + _days : _days)+'/'+(_months<=9 ? '0' + _months : _months)+'/'+ _years;
}
function getTimeParsed(_date){
	var _hours	= _date.getHours();
	var _minutes = _date.getMinutes();
	return (_hours <= 9 ? '0' + _hours : _hours)+':'+(_minutes <= 9 ? '0' + _minutes : _minutes);
}
function parseBoxComponentForMap(box_text) {
	var html = '';	
	var checkbox = JSON.parse(box_text);
	$.each(checkbox, function(i, v) {
		html += v+'<br />';
	}); 
	return html;
}
function parseBoxComponent(box_text) {
	var html = '';
	html += '<ul>';
	var checkbox = JSON.parse(box_text);
	$.each(checkbox, function(i, v) {
		html += '<li>'+v+'</li>';
	}); 
	html += '</ul>';
	return html;
}
function parseBoxComponentFromMongo(box_text, questionId) {
	var tokens = box_text.split(',');
	var html = '';
	html += '<ul>';
	for(var t in tokens){
		html += '<li>'+A_FORM_QUESTIONS[parseInt(questionId)].options[parseInt((tokens[t]).trim())].value+'</li>';
	}
	html += '</ul>';
	return html;
}
function parseBoxComponentForMapFromMongo(box_text, questionId) {
	var tokens = box_text.split(',');
	var html = '';
	for(var t in tokens){
		html += A_FORM_QUESTIONS[parseInt(questionId)].options[parseInt((tokens[t]).trim())].value+'<br />';
	}
	return html;
}

function parseGeolocation(value) {
	var html = '';
	if(value!= null && value.length == 2){
		html += '<span class="underline">'+jQuery.i18n.prop("msg_form_gps_latitude") +'</span>: '+ value[0];
		html += '<br />';
		html += '<span class="underline">'+jQuery.i18n.prop("msg_form_gps_longitude") +'</span>: '+ value[1];
	}
	return html;
}
function parseBarcode(value){
	var html = '';
	if(value == null){
		return html;
	}		
	if(value.length == 2){
		html += '<span class="underline">'+value[1]+'</span>: ' +value[0];
	}
	return html;
}
function aShowMultimedia(m_type, m_value){
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
    	$("#dialog-answer").append('<audio controls="controls"><source src="'+MULTIMEDIA_URL+m_value+'.ogg" type="audio/ogg">'+'<source src="'+MULTIMEDIA_URL+m_value+'.mp3" type="audio/mp3"></audio>');
    else if(m_type === 'video') 
    	$("#dialog-answer").append('<video controls="controls"><source src="'+MULTIMEDIA_URL+m_value+'.ogg" type="audio/ogg">'+'<source src="'+MULTIMEDIA_URL+m_value+'.mp4" type="audio/mp4"></video>');
}

function aShowPicture(img){
    $('#dialog-picture').dialog({
        width : 800,
        height : 600
    });
    $("#dialog-picture").dialog('open');
    $("#dialog-picture").empty();
    $("#dialog-picture").append('<img src="'+MULTIMEDIA_URL+img+'"/>');
}

function setMapOptions(mapOptions) {
	mapOptions = {
        zoom: 10,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        disableDefaultUI: true,
        scaleControl: true,
        scaleControlOptions: {
            position: google.maps.ControlPosition.TOP_LEFT
        },
        streetViewControl: true,
        zoomControl: true,
        panControl: true,
        panControlOptions: {
            position: google.maps.ControlPosition.TOP_RIGHT
        }
    };
	return mapOptions;
}

function displayAddress(_latitude, _longitude) {
	var html = $.ajax({
		  url: 'http://nominatim.openstreetmap.org/reverse?format=json&lat='+_latitude+'&lon='+_longitude,
		  async: false
		 }).responseText;	
	
	var res = JSON.parse(html);
	var _road	= res.address.road;
	var _suburb	= res.address.suburb;
	var _city	= res.address.city;
	   
	var ret = _road + ', ' + _suburb + ', ' + _city;
	return ' '+ret;
}

function drawMap(div_id, position, data_by_row){
	var markers = [];
	//TODO:
	for(var i in data_by_row){
		var _val = data_by_row[i][0];
		markers.push([' ' + _val, data_by_row[i][1], data_by_row[i][2]]);
	}

	var myOptions = {
			zoom: 1,
            mapTypeId: google.maps.MapTypeId.SATELLITE,
        };
        var map = new google.maps.Map(document.getElementById(div_id), myOptions);
        var infowindow = new google.maps.InfoWindow(); 
        var marker, i;
        var bounds = new google.maps.LatLngBounds();
        
        for (i = 0; i < markers.length; i++) { 
            var pos = new google.maps.LatLng(markers[i][1], markers[i][2]);
            bounds.extend(pos);
            marker = new google.maps.Marker({
                position: pos,
                map: map,
                title : displayAddress(markers[i][1], markers[i][2])
            });
            
            google.maps.event.addListener(marker, 'click', (function(marker, i) {
                return function() {
                    infowindow.setContent(markers[i][0]);
                    infowindow.open(map, marker);
                };
            })(marker, i));
        }
        map.fitBounds(bounds);   
}