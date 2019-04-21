$(document).ready(function() {
	$('#email').focus();
	publicForms();
	
	$('#searchPublicForms').keypress(function (e) {
	  if (e.which == 13) {
		  searchPublicForms();
		  return false;
	  }
	});
});

function showLoadingIcon(div_id){
	var html = '<div class="loader_container"><img src="images/ajax-loader.gif" /></div>';
	$('#'+div_id).append(html);
}

function publicForms() {
	showLoadingIcon('public_forms');
    $.ajax({
        dataType:'json',
        type:'get',
        cache:false,
        url:'public-forms.html',
        success: function(data, textStatus, jqXHR){        	
            showPublicForms(data);
        }
    });    
}

function searchPublicForms() {
	var txt_search = $('#searchPublicForms').val();
	if(txt_search == undefined || txt_search.length == 0) {
		publicForms();
		return;
	}
	showLoadingIcon('public_forms');
    $.ajax({
        dataType:'json',
        type:'get',
        cache:false,
        url:'search-public-forms.html',
        data:{
        	search: txt_search
        },
        success: function(data, textStatus, jqXHR){
            showPublicForms(data);
        }
    });    
}

function showPublicForms(data){
	var html = '';
	$("#public_forms").empty();
	if(data) {
		var title= '';
		if(data.length == 0){
			html += jQuery.i18n.prop('msg_grid_no_results');
		}
		else{
			$.each(data, function(i, v) {
				html += '<div class="public_form">';
				if (v.image != undefined) {
					html += '<div class="form_icon"><img class="form_icon" src="data:image/png;base64,'+ v.image +'" ';
				} else {
					html += '<div class="form_icon"><img class="form_icon" src="' + urlContext() +'/images/project_logo.png" ';
				}
				html += 'title="'+v.title+ ((v.description == undefined) ? '' : ': '+v.description) + '"/>';
				html += '</div>';
		
				title = v.title.length > 10 ? (v.title.substring(0,8) + '...') : v.title;
				html += '<a class="form_title" href="#" onclick="downloadPublicApp('+"'"+v.url+"'"+')">' + title + '</a>';
				html += '<a href="#" onclick="showQRCode('+"'"+v.title+"'"+', '+"'"+v.url+"'"+')"><img width="12px" src="static/images/qrcode.png" /></a>';
				html += '</div>';
			});
		}
	} else {
	html += '<p class="project-slogan_small">' + jQuery.i18n.prop("msg_public_forms_error") + '</p>';
	}
	$("#public_forms").append(html);
}

function downloadPublicApp(id){
	if(id == undefined || id == null || id == '')
		return;
	document.location.href='get-app.html?android-app&id='+id;	
}