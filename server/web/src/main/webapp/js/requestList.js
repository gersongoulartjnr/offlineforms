$(document).ready(function(){
	showRequestsList();
});

function showRequestsList(){
	$.ajax({
        dataType:'json',
        type:'get',
        cache:false,
        url:'form-access-request/list.html',
        success: function(data, textStatus, jqXHR){
            if(data) {            	
            	drawRequestsListTable(data);
            }
            else {
            	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            }
        }
    }); 
}
var USERS = new Array();
function drawRequestsListTable(data){
	var i;
	var data_length = data.length;
	var html = '<table class="grid">';
		html += '<thead><tr>';
			html += '<th>Title</th>';
			html += '<th>Name</th>';
			html += '<th>Email</th>';
			html += '<th>Date</th>';
			html += '<th>Accept</th>';
			html += '<th>Reject</th>';
		html += '</tr></thead>';
		html += '<tbody>';
		if(data_length > 0){
			for(i = 0; i < data_length; i++){
				html += '<tr>';
					html += '<td>'+data[i].formTitle+'</td>';
					html += '<td>'+data[i].userFullName+'</td>';
					html += '<td>'+data[i].userEmail+'</td>';
					html += '<td>'+data[i].creationDate+'</td>';
					html += '<td align="center"><a href="#" onclick="acceptItem('+i+')"><img src="images/accept.png"/></a></td>';
					html += '<td align="center"><a href="#" onclick="rejectItem('+i+')"><img src="images/reject.png"/></a></td>';
				html += '</tr>';
				USERS[i] = {user: data[i].userEmail, form: data[i].formUrl}; // using object literals
			}
		}
		else{
			html += '<tr><td colspan="6">'+jQuery.i18n.prop('msg_grid_no_results')+'</td></tr>';
		}
		html += '</tbody>';
	html += '</table>';
	$('#formAccRequest').html(html);
}

function acceptAll() {
	accept(JSON.stringify(USERS));
}

function acceptItem(i) {
	accept('['+JSON.stringify(USERS[i])+']');
}

function rejectItem(i) {
	reject('['+JSON.stringify(USERS[i])+']');
}

function rejectAll() {
	reject(JSON.stringify(USERS));
}

function accept(value) {
	$.ajax({
        dataType:'json',
        type:'post',
        cache:false,
        url: 'form-access-request/accept.html',
        data:{
        	users: value
        },
        success: function(data, textStatus, jqXHR){
            if(data) {            	
            	addMessage(data.message, data.type.toLowerCase());          	
            }
            else {
            	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            }
        }
    });
}

function reject(value) {
	$.ajax({
        dataType:'json',
        type:'post',
        cache:false,
        url: 'form-access-request/reject.html',
        data:{
        	users: value
        },
        success: function(data, textStatus, jqXHR){
            if(data) {            	
            	addMessage(data.message, data.type.toLowerCase());          	
            }
            else {
            	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            }
        }
    });	
}