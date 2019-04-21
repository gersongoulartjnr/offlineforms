var AnalyticsViewClass = function AnalyticsViewClass(){
	this.views_value	= null;
	this.list_views		= [];
	
	this.action_method = 'saveView()';
	
	this.getDataFromViewJson = function getDataFromViewJson(jsonDoc){
		this.views_value = jsonDoc.type;
	};
	
	this.getViewsFromTransformations = function getViewsFromTransformations(){
		for(var i in A_TRANSFORMATIONS){
			var another_list = viewsByQuestionType(A_TRANSFORMATIONS[i].question_type);
			for(var a in another_list){
				var pos =  this.list_views.indexOf(another_list[a]);
				if(pos == -1){
					this.list_views.push(another_list[a]);
				}					
			}
		}
	};
	
	this.renderView = function renderView(){//3?
		this.getViewsFromTransformations();		
		var html = fillRadioBox(this.action_method, V_VIEW_VALUES, this.list_views, this.list_views, this.views_value);
		$('#d-ac-views').html(html);
		this.drawViewProcess();
	}; 
	
	this.drawViewProcess = function drawViewProcess(){
		if(this.views_value != undefined && this.views_value != null && this.views_value != ''){
			var html = '<div class="title">'+jQuery.i18n.prop('msg_a_view')+'</div>';
			html += '<table>';
				html += '<tr><td>';
					html += jQuery.i18n.prop('msg_a_view');
				html += '</td></tr>';
				html += '<tr>';
						html += '<td colspan="2">'+this.views_value+'</td>';
					html += '</tr>';
				html += '</table>';		
			$('#ac-sb-view').html(html);
		}
	};
	
	this.validateData = function validateData(){
		var views_value = $('#'+V_VIEW_VALUES+':checked').val();
		if(views_value == undefined || views_value == null || views_value == ''){
			addMessage(jQuery.i18n.prop('err_view_required', 'error'));
			return false;
		}
		return true;
	};
	
	this.saveData = function saveData(){
		if(this.validateData){
			this.views_value = $('#'+V_VIEW_VALUES+':checked').val();			
		}
	};
	
	this.toJson = function toJson(){
		var json = '{'+
				'"type":"'+this.views_value+'"'+
			'}';			
		return json;
	};
}; 