var AnalyticsFilterClass = function AnalyticsFilterClass(){	
	this.left_container 	= 'fs-ac-side-left';
	this.filter_start_date	= 'filter_start_date';
	this.filter_end_date	= 'filter_end_date';
	this.filter_spec_field	= 'filter_spec_field';
	this.filter_collectors	= 'filter_collectors';
	this.filter_process		= 'ac-sb-filter';
	
	this.filter_location = null;
	this.filter_value_location = null;
	this.loc_latitude	= null;
	this.loc_longitude	= null;
	this.filter_from	= null;
	this.filter_to		= null;
	this.filter_users	= [];
	this.filter_fields	= [];
	this.action_method	= 'saveFilterView()';
	this.geo_action_method = 'saveGeolocationFilter()';
	
	this.getDataFromFilterJson = function getDataFromFilterJson(jsonDoc){
		if(jsonDoc.geolocation != undefined){
			this.filter_location = jsonDoc.geolocation.transformation;
			this.filter_value_location = jsonDoc.geolocation.value;
			this.loc_latitude	= jsonDoc.geolocation.latitude;
			this.loc_longitude	= jsonDoc.geolocation.longitude;
		}
		
		this.filter_from	= jsonDoc.date.from;
		this.filter_to		= jsonDoc.date.to;
		
		if(jsonDoc.users.length > 0){//TODO:
			for(var u in jsonDoc.users){
				this.filter_users.push(jsonDoc.users[u]);
			}
		}
		
		if(jsonDoc.fields.length > 0){
			for(var f in jsonDoc.fields){
				this.filter_fields.push((jsonDoc.fields[f]).toString());
			}
		}
	};
	
	this.drawFilterProcess = function drawFilterProcess(){
		var html = '<div class="title">'+jQuery.i18n.prop('msg_a_filter')+'</div>';
		html += '<table>';
			html += '<tr><td>';
			html += '</td></tr>';
			if(this.filter_location != undefined && this.filter_location != ''){
				html += '<tr>';
					html += '<td><span class="text_bold">'+jQuery.i18n.prop('msg_a_ageolocation')+'</span></td>';
					html += '<td>';
						html += this.filter_location;
				html += '</td></tr>';
				if(this.filter_value_location != undefined && this.filter_value_location != ''){
					html += '<tr>';
						html += '<td>&nbsp;</td>';
						html += '<td>';
							html += this.filter_value_location;
							html += ' Kms.';
					html += '</td></tr>';
				}				
				html += '<tr><td colspan="2">&nbsp;</td></tr>';
			}		
			
			html += '<tr>';
				html += '<td><span class="text_bold">'+jQuery.i18n.prop('msg_a_date')+'</span></td>';
				html += '<td>';
				if(this.filter_from != null && this.filter_to != null){
					html += this.filter_from+' - '+this.filter_to;
				}
				html += '</td>';
			html += '</tr>';			
			html += '<tr><td colspan="2">&nbsp;</td></tr>';
			
			if(this.filter_users.length > 0){
				html += '<tr>';
					html += '<td><span class="text_bold">'+jQuery.i18n.prop('msg_a_users')+'</span></td>';
					html += '<td><ul>';
						for(var u in this.filter_users){
							html += '<li>';
								html += this.filter_users[u];
							html += '</li>';
						}
					html += '</ul></td>';
				html += '</tr>';
				html += '<tr><td colspan="2">&nbsp;</td></tr>';
			}
			
			if(this.filter_fields.length > 0){
				html += '<tr>';
					html += '<td><span class="text_bold">'+jQuery.i18n.prop('msg_a_fields')+'</span></td>';
					html += '<td><ul>';
						for(var f in this.filter_fields){
							html += '<li>';
								html += A_FORM_QUESTIONS[parseInt(this.filter_fields[f])].title;
							html += '</li>';
						}
					html += '</ul></td>';
				html += '</tr>';
				html += '<tr><td colspan="2">&nbsp;</td></tr>';
			}
		html += '</table>';		
		$('#'+this.filter_process).html(html);
	};
	
	this.validateData = function validateData(){
		if(HAS_GEOLOCATION == true){
			var _filter_location = $('#'+ID_LOCATION+':checked').val();
			
			if(_filter_location != undefined && _filter_location != ''){
				if(this.loc_latitude == null || this.loc_longitude == null){
					addMessage(jQuery.i18n.prop('err_filter_location'), 'error');
					return false;
				}
				
				if(this.filter_location == T_NEAREST.name){
					var _filter_value_location = $('#'+VALUE_LOCATION).val();
					_filter_value_location = parseInt(_filter_value_location);
					if(_filter_value_location == undefined || _filter_value_location == '' || isNaN(_filter_value_location)){
						addMessage(jQuery.i18n.prop('err_filter_value_location'), 'error');
						return false;
					}
				}
			}
		}
		
		var _filter_from = $('#'+this.filter_start_date).val();
		if(_filter_from == undefined || _filter_from == null){
			addMessage(jQuery.i18n.prop('err_filter_from'), 'error');
			return false;
		}
		var _filter_to = $('#'+this.filter_end_date).val();
		if(_filter_to == undefined || _filter_to == null){
			addMessage(jQuery.i18n.prop('err_filter_to'), 'error');
			return false;
		}
		
		/*var _filter_users = [];
		$('input:checkbox[name='+this.filter_collectors+']:checked').each(function(){
			var pos_u = _filter_users.indexOf(this.value);
			if(pos_u == -1){
				_filter_users.push(this.value);
			}
		});*/
		
		var _filter_fields = [];
		$('input:checkbox[name='+this.filter_spec_field+']:checked').each(function(){
			var pos_v = _filter_fields.indexOf(this.value);
			if(pos_v == -1){
				_filter_fields.push(this.value);
			}
		});
		if(_filter_fields.length == 0){
			addMessage(jQuery.i18n.prop('err_filter_fields'), 'error');
			return false;
		}		
		return true;		
	};
	
	this.saveData = function saveData(){
		if(!this.validateData()){
			return;
		}
		
		var self = this;

		if(HAS_GEOLOCATION == true){
			this.filter_location = $('#'+ID_LOCATION+':checked').val();
			if(this.filter_location == T_NEAREST.name){
				this.filter_value_location = $('#'+VALUE_LOCATION).val();
			}
		}
		
		this.filter_from	= $('#'+this.filter_start_date).val();
		this.filter_to		= $('#'+this.filter_end_date).val();
				
		this.filter_users = [];
		$('input:checkbox[name='+this.filter_collectors+']:checked').each(function(){
			var pos_u = self.filter_users.indexOf(this.value);
			if(pos_u == -1){
				self.filter_users.push(this.value);
			}
		});
		
		this.filter_fields	= [];
		$('input:checkbox[name='+this.filter_spec_field+']:checked').each(function(){
			var pos_v = self.filter_fields.indexOf(this.value);
			if(pos_v == -1){
				self.filter_fields.push(this.value);
			}
		});
		
		this.addItemToView();
	};
	
	this.addItemToView = function addItemToView(){
		//Update A_TRANSFORMATIONS to show the the views
		if(this.filter_location != undefined && this.filter_location != null){
			addTransformationInfo(A_FORM_QUESTIONS[FIELD_GEOLOCATION].type, this.filter_location);
		}
	};
	
	this.validateDate = function validateDate(){		
		if(this.filter_from == undefined || this.filter_from == null || 
				this.filter_from == ''){
			addMessage(jQuery.i18n.prop('err_filter_from',  id), 'error');
			return false;
		}
		if(this.filter_to == undefined || this.filter_to == null || 
				this.filter_to == ''){
			addMessage(jQuery.i18n.prop('err_filter_to',  id), 'error');
			return false;
		}
		if(this.filter_fields.length == 0){
			addMessage(jQuery.i18n.prop('err_filter_fields',  id), 'error');
			return false;
		}
		return true;
	}; 
	
	this.toJson = function toJson(){
		var json = '{';
		var u, 
			f,
			users_size,
			fields_size;
		if(this.filter_location != undefined && this.filter_location != null && this.filter_location != ''){
			json += '"geolocation":';
				json += '{';
					json += '"field":'+FIELD_GEOLOCATION;
					json += ', "transformation":"'+this.filter_location+'"';
					json += ', "value":"'+this.filter_value_location+'"';
					json += ', "latitude":'+this.loc_latitude;
					json += ', "longitude":'+this.loc_longitude;
				json += '}, ';
		}
		json += '"date":{';
			json += '"from":"'+this.filter_from+'"';
			json += ', "to":"'+this.filter_to+'"';
		json += '}';
		json += ', ';
		
		json += '"users":[';
		for(u = 0, users_size = this.filter_users.length; u < users_size; u++){
			json += '"'+this.filter_users[u]+'"';
			if(u < (users_size-1)){
				json += ',';
			}
		}
		json += ']';
		json += ', ';
		
		json += '"fields":[';
		for(f = 0, fields_size = this.filter_fields.length; f < fields_size; f++){
			json += this.filter_fields[f];
			if(f < (fields_size-1)){
				json += ',';
			}
		}
		json += ']';
		json += '}';
		return json;
	};
	
	this.renderFilterView = function renderFilterView(){
		if(ANALYTICS_CONTAINER.item_status == FIRST_ITEM || ANALYTICS_CONTAINER.item_status == NEW_ITEM){
			this.filter_from = $('#form_creation_date').val();
			this.filter_to = setToday();
		}
		
		var html = '<legend>'+jQuery.i18n.prop('msg_a_filter')+'</legend>';
			html += '<table>';
				if(HAS_GEOLOCATION == true){
					var css_class = '';
					if(this.loc_latitude != null && this.loc_longitude != null){
						css_class = CSS_USER_LOCATION;
					}
					var filter_location_values = transformationsByQuestionType(TXT_GEOLOCATION);
					html += getRowAndTextForTable(jQuery.i18n.prop('msg_a_ageolocation'), 'a-title');
					html += '<tr><td>';
						html += fillRadioBox(this.geo_action_method, ID_LOCATION, filter_location_values, filter_location_values, this.filter_location);						
					html += '</td></tr>';
					html += '<tr><td colspan="2">';
						html += '<div id="'+ID_VALUE_LOCATION+'">';
							html += this.specificViewForGeolocation(this.filter_location);
						html += '</div>';
					html += '</td></tr>';
					html += '<tr><td colspan="2">';
						html += '<div id="'+ID_USER_LOCATION+'" class="'+css_class+'">';												
							if(this.loc_latitude != null && this.loc_longitude != null){								
								html += drawUserLocation(this.loc_latitude, this.loc_longitude);
							}
						html += '</div>';
					html += '</td></tr>';					
				}			
				html += '<tr><td colspan="2">';
					html += '<div id="map_canvas" style="width: 50%; height: 50%"></div>';
				html += '</td></tr>';
			
				html += getRowAndTextForTable(jQuery.i18n.prop('msg_a_date'), 'a-title', true);
				html += '<tr>';
					html += '<td>';
						html += getHtmlLabelTag(jQuery.i18n.prop('msg_a_date_from'), 'a-title');
						html += getHtmlInput(this.action_method, 'text', this.filter_start_date, this.filter_from, null, 10, null, true);
						html += '   ';
						html += getHtmlLabelTag(jQuery.i18n.prop('msg_a_date_to'), 'a-title');
						html += getHtmlInput(this.action_method, 'text', this.filter_end_date, this.filter_to, null, 10, null, true);
					html += '</td>';
				html += '</tr>';
				
				if(A_FORM_COLLECTORS.length > 0){
					html += getRowAndTextForTable(jQuery.i18n.prop('msg_a_users'), 'a-title', true);;
					html += '<tr><td>';
						var filter_user_values = [];
						var user_i,
							users_size;
						for(user_i = 0, users_size = A_FORM_COLLECTORS.length; user_i < users_size; user_i++){
							filter_user_values.push(A_FORM_COLLECTORS[user_i]);
						}
						
						if(ANALYTICS_CONTAINER.item_status == FIRST_ITEM || ANALYTICS_CONTAINER.item_status == NEW_ITEM){
							html += fillCheckBox(this.action_method, this.filter_collectors, filter_user_values, filter_user_values, null);
						} else {						
							html += fillCheckBox(this.action_method, this.filter_collectors, filter_user_values, filter_user_values, this.filter_users);
						}
					html += '</td></tr>';
				}
				html += '</ br>';
				html += getRowAndTextForTable(jQuery.i18n.prop('msg_a_fields'), 'a-title', true);
				html += '<tr><td>';
					var filter_field_values = [];
					var filter_field_titles = [];
					var question_i,
						questions_size;
					
					for(question_i = 0, questions_size = A_FORM_QUESTIONS.length; question_i < questions_size; question_i++){
						filter_field_values.push(A_FORM_QUESTIONS[question_i].id);
						filter_field_titles.push(A_FORM_QUESTIONS[question_i].title);
					}					
					
					if(ANALYTICS_CONTAINER.item_status == FIRST_ITEM || ANALYTICS_CONTAINER.item_status == NEW_ITEM){
						html += fillCheckBox(this.action_method, this.filter_spec_field, filter_field_values, filter_field_titles, null);
					} else {						
						html += fillCheckBox(this.action_method, this.filter_spec_field, filter_field_values, filter_field_titles, this.filter_fields);
					}
				html += '</td></tr>';
				
			html += '</table>';
		html += '</legend>';
		
		$('#'+this.left_container).html(html);
		this.drawFilterProcess();
	};
	
	this.specificViewForGeolocation = function specificViewForGeolocation(value){
		var html = '';
		if(value == T_NEAREST.name){
			html += getHtmlInput(this.action_method, 'number', VALUE_LOCATION, this.filter_value_location, null, 5, null, false);
			html += '<span class="a-subtitle"> kms.</span>';
		}
		return html;
	};
};