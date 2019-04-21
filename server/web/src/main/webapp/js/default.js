$(document).ready(function() {
	G_MY_FORMS		= 0;
	G_SHARED_FORMS	= 1;
	G_MY_GROUPS		= 2;
	G_ANSWERS_FORM	= 3;
	M_ASC	= 'asc';
	M_DESC	= 'desc';
	CSS_SORTABLE	= 'sortable';
	CSS_ASC			= 'ascending';
	CSS_DESC		= 'descending';	
});

function showShareForm(thetitle) {
	$('#dialogShareForm').dialog({
		autoOpen : false,
		modal : true,
		title : thetitle,
		width : 600,
	});
	$("#dialogShareForm").dialog('open');
}

function openLoadingProgressBar(token) {
	$.blockUI();
	fileDownloadCheckTimer = window.setInterval(function() {
		var cookieValue = $.cookie('fileDownloadToken');
		if (cookieValue == token)
			closeLoadingProgressBar();
	}, 1000);
}

function closeLoadingProgressBar() {
	window.clearInterval(fileDownloadCheckTimer);
	$.cookie('fileDownloadToken', null); // clears this cookie value
	$.unblockUI();
}

function closePopup(popupId) {
	$(popupId).dialog('close');
}

var MaritacaMessage = function() {
	this.message = '';
	this.id = new Date().getTime();
	this.type = 'default';
	this.render = function() {
		var typeclass = '';
		switch (this.type) {
		case 'error':
			typeclass = 'errMsg';
			break;
		case 'warn':
			typeclass = 'warnMsg';
			break;
		case 'info':
			typeclass = 'infoMsg';
			break;
		case 'success':
			typeclass = 'infoMsg';
			break;
		}
		var html = '<div id=' + this.id + ' class="maritacaMsg ' + typeclass
				+ '">';
		html += '<p>' + this.message + '</p></div>';
		$('.maritacaMessages').append(html);
		$('#' + this.id).fadeIn(400).delay(4000).fadeOut(400);
		window.setTimeout("removeMessage('" + this.id + "')", 4800);

	};
};

function removeMessage(id) {
	$('#' + id).remove();
}

// add a message to be shown for 4 seconds
var addMessage = function(msg, type) {	
	var marMsg = new MaritacaMessage();
	marMsg.message = msg;
	marMsg.type = type;
	marMsg.render();
};