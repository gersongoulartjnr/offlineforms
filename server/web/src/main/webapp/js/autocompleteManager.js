var groups = {}; // associative array

function addGroup(label, key) {
	var _value = groups[key];
	if(_value == undefined && key != "" && label != "") {
		groups[key] = label;
		renderGroups();
	}
}

function renderGroups() {
	$("#groups_list li").remove();
	$('#form_group').attr('value', '');
	for(var k in groups) {
		$("#groups_list").append('<li id='+k+'><img src="images/delete.png" onclick="removeGroup(\''+k+'\')"/>&nbsp;'+groups[k].trim()+'</li>');
	}
}

function removeGroup(idGroup) {
	delete groups[idGroup];
	renderGroups();
}

function addGroups(groupList) {
	var json = JSON.parse(groupList);
	for(var i in json) {
		groups[i] = json[i];
	}
	renderGroups();
}

function retrieveGroupsList() {
	if (Object.keys(groups).length > 0) {
		var groups_list = JSON.stringify(groups);
		$('#form_groups').attr('value', groups_list);
		return true;
	}
	addMessage(jQuery.i18n.prop('msg_group_share_emptylist_error'), 'error');
	return false;
}
