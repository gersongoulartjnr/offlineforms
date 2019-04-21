$(document).ready(function(){
	initReportViewer($('#report_xml').val());
	$('#downloadReport').click(function() {
		var id = getParameter('id');
		if(id != undefined && id != ''){
			sendDataToServer(id);
		}
	});
	
});

var reportViewer = null;

function initReportViewer(xml){
	var report_xml = $.parseXML(xml);	
	var $xml = $(report_xml);
	
	var form, 
		report,
		type,
		operation,
		question,
		item_id,
		params;
	
	reportViewer = new ReportClass();
	
	form = $xml.find('form').text();
	report = $('#report_id').val();
	
	$xml.find('items').each(function(){
		var elems = this.childNodes;
		for(var i = 0; i < elems.length; i++){		
			//			
			var field = fieldFactoryMethod(elems[i].nodeName);
    		if(field != null){
    			field.setReportFromXMLDoc(elems[i]);
    			reportViewer.elements.push(field);
    		}
    		var elem = reportViewer.elements[i];
    		type 		= elem.type;
    		operation	= elem.option;
    		question	= elem.question;
    		item_id		= elem.id;
    		params		= elem.values_ids;
			//			
			if(!checkValue(form) || !checkValue(report) || !checkValue(operation) || 
					!checkValue(question) || !checkValue(item_id)){
				addMessage("Error showing report for item " + i, 'error');
			}
			loadData(form, report, type, operation, question, item_id, params);
		}
	});
}

function checkValue(_value){
	if(_value != undefined && _value != NaN && _value != ''){
		return true;
	}
	return false;
}

function buildReportItemJson(_form, _report, _type, _op, _question, _item, _params){
	var params = '';
	if(_params != null && _params.length > 0){
		params = ',"parameters":['+_params+']'; 
	}
	var obj = '{"op":"' + _op 
			+'","questionId":' + _question
			+',"itemId":' + _item 
			+',"questionType":"' + _type 
			+'","formId":"' + _form 
			+'","reportId":"' + _report + '"'
			+ params
			+ '}';
	return obj;
}

function getServiceMethodName(_type){	
	var methodName = '';
	switch(_type){
		case 'text':
		case 'number':
		case 'decimal':
		case 'money':
			methodName = 'answerItem';
			break;
		case 'checkbox':
		case 'radio':
		case 'combobox':
			methodName = 'hashAnswerItem';
			break;
		case 'geolocation':
			methodName = 'listAnswersItem';
			break;
	}
	return methodName;
}

function loadData(_form, _report, _type, _op, _question, _item, _params){
	var reportItem = buildReportItemJson(_form, _report, _type, _op, _question, _item, _params);
	var methodName = getServiceMethodName(_type);
	var access_token = $('#access_token').val();	
	$.ajax({
		contentType:"application/json; charset=utf-8",
        dataType:'json',
        type:'post',
        cache:false,
        url:'ws/report/'+methodName+'?oauth_token='+access_token,
        data:reportItem,
        success: function(data, textStatus, jqXHR){
            parseReport(data);
        },
        error: function(data, textStatus, jqXHR){
            return;
        }
    });
}

function parseReport(json){
	var jsonObj = json;
	if(jsonObj.code != '200' && jsonObj.status != 'OK'){
		addMessage("Error", 'error');
		return;
	}	
	var item_id	= jsonObj.itemId;
	var res		= jsonObj.result;
	
	//var type 	= jsonObj.reportItem['type'];
	//var op	= jsonObj.reportItem['op'];
	//var title	= reportViewer.elements[item_id].title;	
	reportViewer.elements[item_id].result = res;
	reportViewer.renderViewer();
}

function addChart(chart_type, div_id, chart_data){
	reportViewer.charts.push([chart_type, div_id, chart_data]);
}

function drawChart(chart_type, div_id, chart_data){
	switch(chart_type){
		case 'pie':
			drawPieChart(div_id, chart_data);
			break;
		case 'bar':
			drawBarChart(div_id, chart_data);
			break;
		case 'list_all':
		case 'list_last_n':
			showMap(div_id, chart_data);
			break;
		case 'nearest_points':
			drawMapWithNearestPoints(div_id, chart_data);
			break;
	}
}

function drawMapWithNearestPoints(div_id, map_data){
	var div = document.getElementById(div_id);	
	var mapOptions = setMapOptions(mapOptions);
    var map = new google.maps.Map(document.getElementById(div_id), mapOptions);
    var posLatLng;
    var data_markers = [];
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            posLatLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);            
            map.setCenter(posLatLng);
            var marker = new google.maps.Marker({
                map: map,
                position: map.getCenter(),
                title: 'Current position',
            });
            marker.setMap(map);
            //
            data_markers = getAllPointsSorted(position.coords.latitude, position.coords.longitude, map_data);
            var i;            
            var points_to_show = 15;
            if(data_markers.length > points_to_show){
            	data_markers = data_markers.slice(0, (points_to_show+1));
            }
            for(i = 0; i < data_markers.length; i++){
            	addMarkerToMap(map, new google.maps.LatLng(data_markers[i][1], data_markers[i][2]), '');            	
            }
        }, function() {
            //handleNoGeolocation(true);
        });        
    } else {
        // Browser doesn't support Geolocation
    	div.innerHTML="Geolocation is not supported by this browser.";
        //handleNoGeolocation(false);
    }
    /*google.maps.event.addListener(marker, 'click', function() {
        infowindow.open(map, marker);
    });*/    
}
/**
 * Comparison function by distances, whose index is 0 into the array
 */
function compareDistances(distA, distB){
	if (distA[0] < distB[0])
		return -1;
	if (distA[0] > distB[0])
		return 1;
	return 0;
}
/**
 * Gets all points sorted by its distance
 * @param posLat current position latitude
 * @param posLng current position longitude
 * @param map_data points collected
 * @returns {Array} [][distance, latitude, longitude]
 */
function getAllPointsSorted(posLat, posLng, map_data){
	if(posLat == undefined || posLng == undefined)
		return;
	var i;
	var data_markers = [];
    for(i = 0; i < map_data.length; i++){    
    	var _point = map_data[i].split(";");
    	if(_point.length == 2){
    		data_markers.push([
    				parseFloat(getDistanceBetweenPoints(posLat, posLng, parseFloat(_point[0]), parseFloat(_point[1]))), 
    				parseFloat(_point[0]), 
    				parseFloat(_point[1])
    				]);
    	}
    }    
    data_markers.sort(compareDistances);
    return data_markers;
}

/**
 * Gets the distance between posLatLngA and posLatLngB
 * @param posLatA current position latitude
 * @param posLngA current position longitude
 * @param posLatB marker latitude
 * @param posLngB marker longitude
 * @returns
 */
function getDistanceBetweenPoints(posLatA, posLngA, posLatB, posLngB){
	var posLatLngA = new google.maps.LatLng(posLatA, posLngA);
    var posLatLngB = new google.maps.LatLng(posLatB, posLngB);
    return google.maps.geometry.spherical.computeDistanceBetween(posLatLngA, posLatLngB);
}

function drawPieChart(div_id, chart_data){
	var data = [[]];
	for(var i in chart_data){
		data.push([chart_data[i][0], chart_data[i][1]]);
	}
	var plot1 = jQuery.jqplot (div_id, [data], {
		seriesDefaults: {
	        renderer: jQuery.jqplot.PieRenderer, 
	        rendererOptions: {
	        	showDataLabels: true
	        }
		}, 
		legend: { show:true, location: 'w' }
    });
}
function drawBarChart(div_id, chart_data){
	$.jqplot.config.enablePlugins = true;
	var s1 = [];
	var ticks = [];
	for(var i in chart_data){
		ticks.push(chart_data[i][0]);
		s1.push(chart_data[i][1]);
	}
     
    var plot1 = $.jqplot(div_id, [s1], {
    	animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: true }
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: ticks
            }
        },
        highlighter: { show: false }
    });
}

function showMap(div_id, map_data){
	var div = document.getElementById(div_id);	
	var mapOptions = setMapOptions(mapOptions);
    var map = new google.maps.Map(document.getElementById(div_id), mapOptions);

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var posLatLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);            
            map.setCenter(posLatLng);
            var marker = new google.maps.Marker({
                map: map,
                position: map.getCenter(),
                title: 'My current position',
            });
            marker.setMap(map);

        }, function() {
            //handleNoGeolocation(true);
        });
    } else {
        // Browser doesn't support Geolocation
    	div.innerHTML="Geolocation is not supported by this browser.";
        //handleNoGeolocation(false);
    }
    /*google.maps.event.addListener(marker, 'click', function() {
        infowindow.open(map, marker);
    });*/

    var i;
    for(i = 0; i < map_data.length; i++){
    	var _point = map_data[i].split(";");
    	if(_point.length == 2){
    		addMarkerToMap(map, new google.maps.LatLng(parseFloat(_point[0]), parseFloat(_point[1])), '');
    	}
    }
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

function addMarkerToMap(currentMap, currentLocal, msg) {
    var marker = new google.maps.Marker({
        map: currentMap,
        position: currentLocal,
        title: msg
    });
    marker.setMap(currentMap);
}

function sendDataToServer(report_id){	
	var _data = reportViewer.dataToDownload();
	if(_data != ''){
		_data = "'"+_data+"'";
		document.location.href='report-viewer.html?csv&id='+report_id+'&json='+_data;
	}
	else{
		addMessage("You cannot download the data yet.", 'error');
	}
}