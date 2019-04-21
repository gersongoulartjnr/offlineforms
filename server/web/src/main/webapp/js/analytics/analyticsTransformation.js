/* Transformation - Parent */
var AnalyticsTransformationClass = function AnalyticsTransformationClass(transformation_id){
	
	this.list_type	= TRA_MAIN;
	this.t_items	= [];	
	this.filter_fields = [];
	this.filter_field_values = [];
	this.filter_field_titles = [];
	
	this.transformation_container	= 'd-ac-';
	this.transformation_process		= 'ac-sb-';
	
	this.getDataFromTransformationsJson = function getDataFromTransformationsJson(list_type, jsonDoc){
		if(list_type == TRA_MAIN){
			this.list_type = TRA_MAIN;
		} else if(list_type == TRA_SECOND){
			this.list_type = TRA_SECOND;
		} else{
			return;
		}
		for(var t in jsonDoc){
			var id			= jsonDoc[t].id;
			var field		= jsonDoc[t].field;
			var type		= jsonDoc[t].type;
			var spec_data	= jsonDoc[t].specificData;
			var question_item = A_FORM_QUESTIONS[field];
			
			var t_item = aFieldFactory(question_item, type, this.list_type);
			t_item.t_i_id = id;
			t_item.parent_list = this.list_type;
			
			if(parseInt(id) == 0){
				t_item.is_active = true;
			}
			t_item.getSpecDataFromTransformationJson(spec_data);		
			this.t_items.push(t_item);
		}
	};
	
	this.allItemsAreNotActive = function allItemsAreNotActive(){
		for(var i in this.t_items){
			this.t_items[i].is_active = false;
		}
	};
		
	this.addTransformation = function addTransformation(question_elem, transf_sel){
		if(this.validateData()){
			var t_item = aFieldFactory(question_elem, transf_sel);
			this.allItemsAreNotActive();
			t_item.is_active = true;
			t_item.parent_list = this.list_type;
			t_item.t_i_id = this.t_items.length;
			
			this.t_items.push(t_item);
			ANALYTICS_CONTAINER.item_status = NORMAL_ITEM;
			this.renderTransformation();		
		}
	};
	this.editTransformation = function editTransformation(id){
		var items_length = this.t_items.length;
		if(items_length == 0){
			this.renderTransformation();
		}
		if(items_length >= 0 && items_length > id){
			this.allItemsAreNotActive();
			this.t_items[id].is_active = true;
			this.renderTransformation();
		}		
	};
	
	this.renderTransformation = function renderTransformation(){
		this.filter_field_values = [];
		this.filter_field_titles = [];
		var item_filter = ANALYTICS_CONTAINER.elements[A_ID_ELEMENT].item_filter;
		if(ANALYTICS_CONTAINER.item_status == FIRST_ITEM || ANALYTICS_CONTAINER.item_status == NEW_ITEM){
			var question_i,
			questions_size;		
			for(question_i = 0, questions_size = A_FORM_QUESTIONS.length; question_i < questions_size; question_i++){
				this.filter_field_values.push(A_FORM_QUESTIONS[question_i].id);
				this.filter_field_titles.push(A_FORM_QUESTIONS[question_i].title);
			}			
		} else {
			this.filter_fields = item_filter.filter_fields;
			for(var i in this.filter_fields){
				this.filter_field_values.push(A_FORM_QUESTIONS[this.filter_fields[i]].id);
				this.filter_field_titles.push(A_FORM_QUESTIONS[this.filter_fields[i]].title);
			}
		}
		
		var html = '<br /><div>';
		html += '<table><tr>';
		html += '<td><div>'+fillComboBox('getTransfByType('+"'"+this.list_type+"'"+')', getTransformationPrefix(this.list_type)+TRA_FIELDS, this.filter_field_values, this.filter_field_titles, null, 'select_tr_question_size')+'</div></td>';
		
		html += '<td><div>'+fillComboBox(null, getTransformationPrefix(this.list_type)+TRA_TRAS, ['-1'], ['Choose one'], null, 'select_tr_option_size')+'</div></td>';
		html += '<td><div id="add_transformation" class="btn-analytics" onclick="addTransformation('+"'"+this.list_type+"'"+')">Add</div></td>';
		html += '</tr></table>';		
		html += '</div>';
		
		html += '<ol>';		
		
		for(var i in this.t_items){
			this.t_items[i].t_i_id = i;
			html += this.t_items[i].renderTranItem(i);
		}
		html += '</ol>';
		$('#'+this.transformation_container+this.list_type).html(html);
		getTransfByType(this.list_type);
		this.drawTransformationProcess();
	};
	
	this.drawTransformationProcess = function drawTransformationProcess(){
		if(this.t_items.length > 0){
			var html = '';			
				if(this.list_type == TRA_MAIN){
					html += '<div class="title">'+jQuery.i18n.prop('msg_a_transformation')+'</div>';
					html += '<table>';
				} else if(this.list_type == TRA_SECOND){
					html += '<div class="title">'+jQuery.i18n.prop('msg_a_last_transformation')+'</div>';
					html += '<table>';
				} else {
					return;
				}		
				
				for(var t in this.t_items){
					html += '<tr>';
						html += this.t_items[t].drawItem();
					html += '</tr>';
				}
				html += '</table>';		
			$('#'+this.transformation_process+this.list_type).html(html);
		}
	};
	
	this.validateData = function validateData(){		
		var flag = true;
		/*if(this.t_items.length > 1){
			//error message
			return false;
		}*/
		
		if(this.list_type == TRA_MAIN){
			if(this.t_items.length > MAX_TRANSFORMATIONS){
				addMessage(jQuery.i18n.prop('err_transformation_max_exceeded', MAX_TRANSFORMATIONS), 'error');
				return false;
			}
		} else if(this.list_type == TRA_SECOND){
			if(this.t_items.length > MAX_SECTRANSFORMATIONS){
				addMessage(jQuery.i18n.prop('err_last_transformation_max_exceeded', MAX_SECTRANSFORMATIONS), 'error');
				return false;
			}
		} else {
			//error general
			return false;
		}
		
		
		for(var t in this.t_items){
			flag = this.t_items[t].validateItem();
			if(flag == false){
				return false;
			}
		}
		return flag;
	};
	
	this.saveData = function saveData(){
		var id_to_save = null;
		for(var i in this.t_items){
			if(this.t_items[i].is_active == true){
				id_to_save = this.t_items[i].t_i_id;
			}
		}
		if(id_to_save != null){
			this.t_items[parseInt(id_to_save)].saveItem();
		} else {
			//ERROR
		}
	};
	
	this.addItemToView = function addItemToView(){
		if(this.filter_location != undefined && this.filter_location != null){
			addTransformationInfo(A_FORM_QUESTIONS[FIELD_GEOLOCATION].type, this.filter_location);
		}
	};
	
	this.toJson = function toJson(){
		var i,
			items_size;
		var json = '[';
			for(i = 0, items_size = this.t_items.length; i < items_size; i++){
				json += this.t_items[i].itemToJson(i);
				if(i != (items_size-1)){
					json += ',';
				}
			}		
			json += ']';
		return json;
	};
};