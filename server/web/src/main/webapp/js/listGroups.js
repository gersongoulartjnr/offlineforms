$(document).ready(function(){
	setTabSelectedById('tabGroups');
	myGroups();
});

function myGroups(orderBy, orderType, page) {
	var mOrderBy	= 'name';
	var mOrderType	= 'asc';
	var mPage		= 1;
	if(orderBy != '' && orderBy != undefined) {mOrderBy = orderBy;}
	if(orderType != '' && orderType != undefined) {mOrderType = orderType;}
	if(page != '' && page != undefined) {mPage = page;}
	$.ajax({
        dataType:'json',
        type:'get',
        cache:false,
        url:'groups/my-groups.html',
        data:{
        	orderBy: mOrderBy,
        	orderType: mOrderType,
        	page:mPage
        },
        success: function(data, textStatus, jqXHR){
            if(data) {
            	if (data.rows.length > 0) {
            		buildGroupsTable(data);
            	} else {
            		window.location = urlContext() + "group.html";
            	}
            }
            else {
            	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            }
        }
    });    
}

function buildGroupsTable(data){
	var titles = [jQuery.i18n.prop('msg_ttl_name'), jQuery.i18n.prop('msg_ttl_description')];
	var columns_size	= ['40', '60'];
	var columns_key		= ['name', 'description'];
	var columns_sort	= ['name'];
	var myGroups = new GroupGrid();
	myGroups.paintGrid(titles, columns_key, columns_size, columns_sort, data, G_MY_GROUPS, columns_key[0]);
}

function buildGroupSubMenu(id) {
	var url_context = urlContext();
	var html = '';
	html += '<li><a class="sub_menu" href="'+url_context+'group.html?id='+id+'"><img class="sub_menu" src="images/edit.png" />Edit</a></li>';
	html += "<li><a class=\"sub_menu\" href=\"#\" onclick=\"removeGroup("+"'"+id+"'"+");\"><img class=\"sub_menu\" src=\"images/remove.png\" />Delete</a></li>";
	return html;
}

function removeGroup(groupId) {
    $.ajax({
        dataType:'json',
        type:'post',
        cache:false,
        url:'group/delete.html',
        data:{
        	id:groupId
        },
        success: function(data, textStatus, jqXHR){
            if(data) {            	
            	checkRemove(data, groupId);
            }
            else {
            	addMessage(jQuery.i18n.prop('msg_internal_server_error'), 'error');
            }
        }
    });    
}

function checkRemove(data, id) {
	if(data.type == 'SUCCESS') {
		addMessage(data.message, 'info');
		$('#'+id).remove();
	}
	else {
		addMessage(data.message, 'error');
	}
}
function showGroupSubMenu(link){
	clearSubMenus();
	//link.nextElementSibling.style.display = 'block';
}
function clearSubMenus(){
	$('.form_submenu li ul').each(function(){
		this.style.display = 'none';
	});
}