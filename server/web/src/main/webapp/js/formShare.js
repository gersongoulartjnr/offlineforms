$(document).ready(function() {
	setTabSelectedById();
	prepareMenuFormShare();
	urlToShare();
	$("#formShared").hide();
	$("#form_group").autocomplete({
		source: "form-share/groups.html", 
		select: function( event, ui ) {
			$( "#form_group" ).val( ui.item.label );
			$( "#form_group_value" ).val( ui.item.value );
			addGroup(ui.item.label, ui.item.value); // reference to autocompleteManager.js
			return false;
		},
		focus: function( event, ui ) {
			$( "#form_group" ).val( ui.item.label );
			return false;
		}
	});
	toggleGroupsList();
});

function prepareMenuFormShare() {
	var idForm = getParameter('id');
	setMenuStyles();
	$('#li_form_editor').html(createMenu(msgFormEditor, FORM_EDITOR, idForm));
	$('#li_form_share').html(createMenu(msgFormShare));
	$('#li_view_answers').html(createMenu(msgViewAnswers, VIEW_ANSWERS, idForm));
}

function setMenuStyles() {
	$('#li_form_share').addClass('selected');
}

function urlToShare() {
	var form_path	= 'form.html?id=';
	var url 	= $("#form_url").val();
	var url_complete = urlContext()+form_path+url;
	$('#span_url').html(url_complete);
}

function toggleGroupsList() {
	var shared1 = $('#strPolicy2').is(":checked");//shared hierarchical
	var shared2 = $('#strPolicy3').is(":checked");//shared social
	if(shared1 == true || shared2 == true) {
		$("#formShared").show();
	}
	else {
		$("#formShared").hide();
	}
}

function doSubmit() {
	var shared1 = $('#strPolicy1').is(":checked");//private
	if (shared1) {
		return;
	}
	var shared2 = $('#strPolicy2').is(":checked");//shared hierarchical
	var shared3 = $('#strPolicy3').is(":checked");//shared social
	var shared4 = $('#strPolicy4').is(":checked");//public
	if(shared4 || ((shared2 || shared3) && retrieveGroupsList())) {
		$('#account').submit();
	}
}