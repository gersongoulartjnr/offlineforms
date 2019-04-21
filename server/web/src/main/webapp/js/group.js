$(document).ready(function() {
	setTabSelectedById('tabGroups');
	 $("#form_group").autocomplete({
        source: "group/groups.html", 
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
});

var NONE = "NONE";
var usersgroup = {}; // associative array

function addCurrentGroups(currentGroups) {
	loadMessages();
	var json = JSON.parse(currentGroups);
	$("#form_current_group").append(new Option(jQuery.i18n.prop('msg_group_editor_selectoption'), NONE));
	for(var i in json) {
		$("#form_current_group").append(new Option(json[i], i));
	}
}

function loadUsersGroup(option) {
	if (option.value == NONE) {
		usersgroup = {};
		renderUsersGroup();
		return;
	}
	$.ajax({
        dataType:'json',
        type:'post',
        cache:false,
        url:'group/loadUsersGroup.html',
        data:{
        	key:option.value
        },
        success: function(data, textStatus, jqXHR){
            if(data) {            	
            	for (var i in data) {
            		usersgroup[i] = data[i];
            	}
            	renderUsersGroup();
            	return;
            }
            else {
            	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            	return;
            }
        },
        error: function(data, textStatus, jqXHR){
        	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            return;
        }
    });
}

function renderUsersGroup() {
	$("#groups_list_groups li").remove();
	$("#groups_list_groups_container button").remove();
	for(var k in usersgroup) {
		$("#groups_list_groups").append('<li id='+k+'><img src="images/previus.png" title="'+jQuery.i18n.prop('msg_group_editor_groupadd')+'" onclick="addGroupLists(\''+k+'\')"/>&nbsp;'+usersgroup[k].trim()+'</li>');
	}
	if (Object.keys(usersgroup).length >= 2) {
		$("#groups_list_groups_container").append('<button class="main_button" type="button" onclick="addAllUsers();">Import All</button>');
	}
	if (Object.keys(usersgroup).length == 0) {
		$('#form_current_group option[value='+NONE+']').attr("selected", "selected");
	}
}

function addGroupLists (idUserGroup) {
	addGroup(usersgroup[idUserGroup], idUserGroup);
	delete usersgroup[idUserGroup];
	renderUsersGroup();
}

function addAllUsers () {
	for (i in usersgroup) {
		addGroupLists (i);
	}
	return;
}

function doSubmit() {
	if (retrieveGroupsList()) {
		$('#group').submit();
	}
}
