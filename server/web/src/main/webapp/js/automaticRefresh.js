$(document).ready(function() {
	openWebsocket('public'); // 'public' identifies public forms
	
	$.ajax({
		dataType:'json',
		type:'post',
		cache:false,
		url:'form/listnames.html',
		success: function(data, textStatus, jqXHR){
			if(data) {
				for(var i in data) {
					openWebsocket(data[i]);
				}				
			}
		}
	});
});

function openWebsocket(name) {
//	var detectedTransport = null;
	var socket = $.atmosphere;
//	var subSocket;
	
	var url = urlContext() + name + "/websocket.html";
	var request = { url : url, 
					contentType: "application/json",
					transport: "websocket"};

    request.onMessage = function (response) {
        detectedTransport = response.transport;
        if (response.status == 200) {
            var data = response.responseBody;
            var name = response.request.url;
	            name = name.replace(urlContext(), '');
	            name = name.replace("/websocket.html", '');
            var json = JSON.parse(data);
            
            var contains = $('a[href$="'+json.url+'"]'); // url is unique
            if (isMyForms(name) && contains.length == 0) {
            	addMessage(json.title, 'info');
            	$(buildRow(json,false)).insertBefore('#myForms table > tbody > tr:first');            	
            } 
            if (!isMyForms(name) && contains.length == 0) {
            	addMessage(json.title + " " + json.strPolicy, 'info');
            	$(buildRow(json,true)).insertBefore('#sharedForms table > tbody > tr:first');            	
            }            
        }
    };
    subSocket = socket.subscribe(request);
}

function buildRow(jsonObj,isShare) {
	var title			= jsonObj.title;
	var url				= jsonObj.url;	
	var creationDate	= jsonObj.creationDate;
	var policy			= jsonObj.strPolicy;
	var numberOfCollects= jsonObj.numberOfCollects;
	var reports			= jsonObj.reports;
	var analytics		= jsonObj.analytics;
	
	var html = '<tr bgcolor="#EBFAF0">';
	html += getTitle(title, url);	
	html += '<td width="3%" align="center">';
		html += '<a href="#" onclick="showQRCode('+"'"+title+"'"+', '+"'"+url+"'"+')"><img width="12px" src="static/images/qrcode.png" /></a>';
	html += '</td>';
	
	html += getMenu(url, jsonObj.formPermission, jsonObj.answerPermission, jsonObj.reportPermission, reports, analytics);
	if (isShare) {
		html += '<td>'+jsonObj.owner+'</td>';
	}
	html += '<td>'+creationDate+'</td>';
	html += '<td>'+policy+'</td>';
	html += '<td align="center">'+numberOfCollects+'</td>';
	html += '</tr>';
	
	return html;
}

function getTitle(title, url) {
	var html = '<td><a href="form.html?id='+url+'" target="_blank">'+title+'</a>';
    html += '<span class="form_list_new">'+jQuery.i18n.prop('msg_form_new')+'</span></td>';
    return html;
}

function getMenu(url, formPermission, answerPermission, reportPermission, reports, analytics) {    
	var html = '<td width="3%">';
		html += '<ul class="form_sub_menu"><li>';
		html += '<a href="#"><span class="arrow">&#9660;</span></a>';
		html += '<ul>';
		html += buildSubMenu(url, formPermission, answerPermission, reportPermission, reports, analytics);
		html += '</ul>';
		html += '</li>';
		html += '</ul>';
	html += '</td>';
    
    
    return html;
}

function isMyForms(name){
	var re = /\S+@\S+\.\S+/;
    return re.test(name);
}