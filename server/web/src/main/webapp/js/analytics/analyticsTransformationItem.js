var AnalyticsFieldClass = function AnalyticsFieldClass(){
	this.question = null;
	this.type = null;
	this.transformation	= null;
	this.call_method	= null;
	this.save_method	= null;
	this.to_xml_method	= null;
	this.from_xml_method = null;
	this.to_json_method	= null;
	this.from_json_method = null;
	this.validate_method = null;
	this.draw_method	= null;
	this.weight_method	= null;
	this.action_method	= 'saveTransformationItem';
	
	this.t_i_id		= null;
	this.is_active	= false;
	this.parent_list	= null;
	this.item_css		= null;
	
	this.txt_active		= 'item_transf_act';
	this.txt_inactive	= 'item_transf';
	
	this.getHtmlIdWithPrefix = function getHtmlIdWithPrefix(html_id){
		return this.parent_list+'_'+html_id;
	};
	this.getActionMethod = function getActionMethod(){
		return this.action_method+'('+"'"+this.parent_list+"'"+')';
	};	
	this.getSpecDataFromTransformationJson = function getSpecDataFromTransformationJson(jsonDoc){
		return this[this.from_json_method](jsonDoc);
	};
	
	this.renderTranItem = function renderTranItem(index){
		if(this.is_active){
			this.item_css = this.txt_active;
		} else{
			this.item_css = this.txt_inactive;
		}

		var tra_prefix = '';
		if(this.parent_list == TRA_MAIN){
			tra_prefix = PREFIX_TRA_MAIN;
		} else if(this.parent_list == TRA_SECOND){
			tra_prefix = PREFIX_TRA_SEC;
		} else {
			return;
		}
		
		this.t_i_id = index;
		var html = '<li id="'+tra_prefix+'_'+this.t_i_id+'" class="'+this.item_css+'">';
			html += '<div><span>' + this.getIdToShow()+'.- </span>'+this.question.title+ ' - '+this.transformation+'</div>';
			if(this.is_active){
				var up = jQuery.i18n.prop('msg_up');
				var down = jQuery.i18n.prop('msg_down');
				var deleteField = jQuery.i18n.prop('msg_delete');
				html += '<div class="transf_options">'+
						'<a href="#" onclick="moveTransformation('+this.t_i_id+', -1, '+"'"+this.parent_list+"'"+'); return false;">' + 
						'<img src="images/form/arrow_up.png" alt="up"' +
						'title="' + up + '"/></a>' + 
					'<a href="#" onclick="moveTransformation('+this.t_i_id+', 1, '+"'"+this.parent_list+"'"+'); return false;">' + 
						'<img src="images/form/arrow_down.png" alt="down"' +
						'title="' + down + '"/></a>' +
					'<a href="#" onclick="deleteTransformation('+this.t_i_id+', '+"'"+this.parent_list+"'"+'); return false;">' + 
						'<img src="images/delete.png" alt="delete"' +
						'title="' + deleteField + '"/></a>' +
				'</div>';
				html += this.renderSpecificItem();
			} else {
				var maximize = 'Maximize';
				html += '<div class="transf_options tra_inactive">' +
						'<a href="#" onclick="editTransformationItem('+this.t_i_id+', '+"'"+this.parent_list+"'"+'); return false;">' + 
						'<img src="images/maximize.png" alt="maximize"' +
						'title="' + maximize + '"/></a>' +
					'</div>';
			}						
		html += '</li>';
		
		this.addItemToView();
		
		return html;
	};
	
	this.addItemToView = function addItemToView(){
		//Update A_TRANSFORMATIONS to show the the views
		addTransformationInfo(this.question.type, this.transformation);
	};
	
	this.drawItem = function drawItem(){
		var html = '<td><span class="a-subtitle">'+this.getIdToShow()+'.- </span>'+this.question.title+ ' - '+this.transformation;
				html += '<br />';
				html += this.drawSpecificItem();
			html += '</td>';
		return html; 
	};	
	this.validateItem = function validateItem(){
		return this.validateSpecificItem();
	};	
	this.saveItem = function saveItem(){
		this.saveSpecificItem();
	};	
	this.itemToJson = function itemToJson(i){
		var json = '{';
				json += '"id":'+i;
				json += ', "field":'+this.question.id;
				json += ', "questionType":"'+this.question.type+'"';
				json += ', "type":"'+this.transformation+'"';
				json += ', "specificData":{'+this.specificItemToJson()+'}';
			json += '}';
		return json;
	};	
	
	this.getIdToShow = function getIdToShow(){
		var _id = parseInt(this.t_i_id);
		return _id + 1;
	};
	
	/* Global methods */
	this.renderSpecificItem = function renderSpecificItem(){
		var html = '';
			html += '<div>';
				html += this[this.call_method]();
			html += '</div>';
		return html;
	};
	this.drawSpecificItem = function drawSpecificItem(){
		return this[this.draw_method]();
	};
	this.validateSpecificItem = function validateSpecificItem(){
		return this[this.validate_method]();
	};
	this.saveSpecificItem = function saveSpecificItem(){
		this[this.save_method]();
	};	
	this.specificItemToJson = function specificItemToJson(){
		return this[this.to_json_method]();
	};
	/* Transformation Methods */
	this.methodToXml = function methodToXml(){
		
	};
	this.methodFromXml = function methodFromXml(){
		
	};
	this.methodToJson = function methodToJson(_value){
		var json = '"values":[';
				json += '"'+_value+'"'; 
			json += ']';
		return json;
	};	
	this.methodFromJson = function methodFromJson(jsonDoc){
		this.unique_value = jsonDoc.values[0];
	};
	this.numberValidate = function numberValidate(_value, _error){
		var unique_value = _value;
		unique_value = parseInt(unique_value);
		if(unique_value == undefined || unique_value == null || isNaN(unique_value)){
			addMessage(jQuery.i18n.prop(_error, this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.methodDraw = function methodDraw(_value){
		if(_value != undefined && _value != null){
			return _value;
		}
		return '';
	};
		
	//SORT
	this.unique_value = null;
	
	this.sort = function sort(){
		if(this.unique_value == null){
			this.unique_value = SORT_ASC;
		}
		return fillComboBox(this.getActionMethod(), this.getHtmlIdWithPrefix(c_unique_value), SORT_TYPE, SORT_TYPE, this.unique_value);
	};
	this.sortSave = function saveSort(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.sortValidate()){
			return;
		}
	};
	this.sortToJson = function sortToJson(){
		return this.methodToJson(this.unique_value);
	};
	this.sortFromJson = function sortFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.sortValidate = function sortValidate(){
		var unique_value = this.unique_value;
		if(unique_value == undefined || unique_value == null){
			addMessage(jQuery.i18n.prop('err_sort_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.sortDraw = function sortDraw(){
		return this.methodDraw(this.unique_value);
	};
	
	//MIN
	this.min = function min(){ return '';	};
	this.minSave = function minSave(){};
	this.minToJson = function minToJson(){ return ''; };
	this.minFromJson = function minFromJson(jsonDoc){};
	this.minValidate = function minValidate(){ return true; };
	this.minDraw = function minDraw(){ return ''; };
	
	//MAX
	this.max = function max(){ return '';	};
	this.maxSave = function maxSave(){};
	this.maxToJson = function maxToJson(){ return ''; };
	this.maxFromJson = function maxFromJson(jsonDoc){};
	this.maxValidate = function maxValidate(){ return true; };
	this.maxDraw = function maxDraw(){ return ''; };
	
	//NMIN
	this.nMin = function nMin(){ 
		return getHtmlInput(this.getActionMethod(), 'number', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 5, null, false);
	};	
	this.nMinSave = function nMinSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.nMinValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.nMinToJson = function nMinToJson(){
		return this.methodToJson(this.unique_value);
	};	
	this.nMinFromJson = function nMinFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.nMinValidate = function nMinValidate(){		
		var unique_value = this.unique_value;
		unique_value = parseInt(unique_value);
		if(unique_value == undefined || unique_value == null || isNaN(unique_value)){
			addMessage(jQuery.i18n.prop('err_nmin_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.nMinDraw = function nMinDraw(){
		return this.methodDraw(this.unique_value);
	};
	
	//NMAX	
	this.nMax = function nMax(){ 
		return getHtmlInput(this.getActionMethod(), 'number', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 5, null, false);
	};	
	this.nMaxSave = function nMaxSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.nMaxValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.nMaxToJson = function nMaxToJson(){
		return this.methodToJson(this.unique_value);
	};	
	this.nMaxFromJson = function nMaxFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.nMaxValidate = function nMaxValidate(){
		var unique_value = this.unique_value;
		unique_value = parseInt(unique_value);
		if(unique_value == undefined || unique_value == null || isNaN(unique_value)){
			addMessage(jQuery.i18n.prop('err_nmax_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;		
	};
	this.nMaxDraw = function nMaxDraw(){
		return this.methodDraw(this.unique_value);
	};
	
	//COUNT
	this.count = function count(){ return '';	};
	this.countSave = function countSave(){};
	this.countToJson = function countToJson(){ return ''; };
	this.countFromJson = function countFromJson(jsonDoc){};
	this.countValidate = function countValidate(){ return true; };
	this.countDraw = function countDraw(){ return ''; };
	
	//SUM
	this.sum = function sum(){ return '';	};
	this.sumSave = function sumSave(){};
	this.sumToJson = function sumToJson(){ return ''; };
	this.sumFromJson = function sumFromJson(jsonDoc){};
	this.sumValidate = function sumValidate(){ return true; };
	this.sumDraw = function sumDraw(){ return ''; };
	
	//AVERAGE
	this.average = function average(){ return '';	};
	this.averageSave = function averageSave(){};
	this.averageToJson = function averageToJson(){ return ''; };
	this.averageFromJson = function averageFromJson(jsonDoc){};
	this.averageValidate = function averageValidate(){ return true; };
	this.averageDraw = function averageDraw(){ return ''; };
	
	//EQUAL	
	this.equal = function equal(){
		return getHtmlInput(this.getActionMethod(), 'number', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 5, null, false);
	};
	this.equalSave = function equalSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.equalValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.equalToJson = function equalToJson(){
		return this.methodToJson(this.unique_value);
	};
	this.equalFromJson = function equalFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.equalValidate = function equalValidate(){
		var unique_value = this.unique_value;
		unique_value = parseInt(unique_value);
		if(unique_value == undefined || unique_value == null || isNaN(unique_value)){
			addMessage(jQuery.i18n.prop('err_equal_number_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.equalDraw = function equalDraw(){
		return this.methodDraw(this.unique_value);
	};

	//NOT EQUAL
	this.notEqual = function notEqual(){//'text'
		return getHtmlInput(this.getActionMethod(), 'number', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 5, null, false);
	};
	this.notEqualSave = function notEqualSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.notEqualValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.notEqualToJson = function notEqualToJson(){
		return this.methodToJson(this.unique_value);
	};
	this.notEqualFromJson = function notEqualFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.notEqualValidate = function notEqualValidate(){
		var unique_value = this.unique_value;
		unique_value = parseInt(unique_value);
		if(unique_value == undefined || unique_value == null || isNaN(unique_value)){
			addMessage(jQuery.i18n.prop('err_not_equal_number_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.notEqualDraw = function notEqualDraw(){
		return this.methodDraw(this.unique_value);
	};
	
	//GREATER THAN
	this.greater = function greater(){
		return getHtmlInput(this.getActionMethod(), 'number', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 5, null, false);
	};
	this.greaterSave = function greaterSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.greaterValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.greaterToJson = function greaterToJson(){
		return this.methodToJson(this.unique_value);
	};
	this.greaterFromJson = function greaterFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.greaterValidate = function greaterValidate(){
		var unique_value = this.unique_value;
		unique_value = parseInt(unique_value);
		if(unique_value == undefined || unique_value == null || isNaN(unique_value)){
			addMessage(jQuery.i18n.prop('err_greater_number_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.greaterDraw = function greaterDraw(){
		return this.methodDraw(this.unique_value);
	};
	
	//LESS THAN
	this.less = function less(){
		return getHtmlInput(this.getActionMethod(), 'number', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 5, null, false);
	};
	this.lessSave = function lessSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.lessValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.lessToJson = function lessToJson(){
		return this.methodToJson(this.unique_value);
	};
	this.lessFromJson = function lessFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.lessValidate = function lessValidate(){
		var unique_value = this.unique_value;
		unique_value = parseInt(unique_value);
		if(unique_value == undefined || unique_value == null || isNaN(unique_value)){
			addMessage(jQuery.i18n.prop('err_less_number_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.lessDraw = function lessDraw(){
		return this.methodDraw(this.unique_value);
	};
	
	//GREATER THAN OR EQUAL
	this.greaterEqual = function greaterEqual(){
		return getHtmlInput(this.getActionMethod(), 'number', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 5, null, false);
	};
	this.greaterEqualSave = function greaterEqualSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.greaterEqualValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.greaterEqualToJson = function greaterEqualToJson(){
		return this.methodToJson(this.unique_value);
	};
	this.greaterEqualFromJson = function greaterEqualFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.greaterEqualValidate = function greaterEqualValidate(){
		var unique_value = this.unique_value;
		unique_value = parseInt(unique_value);
		if(unique_value == undefined || unique_value == null || isNaN(unique_value)){
			addMessage(jQuery.i18n.prop('err_greater_e_number_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.greaterEqualDraw = function greaterEqualDraw(){
		return this.methodDraw(this.unique_value);
	};
	
	//LESS THAN OR EQUAL
	this.lessEqual = function lessEqual(){
		return getHtmlInput(this.getActionMethod(), 'number', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 5, null, false);
	};
	this.lessEqualSave = function lessEqualSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.lessEqualValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.lessEqualToJson = function lessEqualToJson(){
		return this.methodToJson(this.unique_value);
	};
	this.lessEqualFromJson = function lessEqualFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.lessEqualValidate = function lessEqualValidate(){
		var unique_value = this.unique_value;
		unique_value = parseInt(unique_value);
		if(unique_value == undefined || unique_value == null || isNaN(unique_value)){
			addMessage(jQuery.i18n.prop('err_less_e_number_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.lessEqualDraw = function lessEqualDraw(){
		return this.methodDraw(this.unique_value);
	};
	
	//GROUP BY
	this.question_list = [];
	this.group_by_item = null;
	
	this.groupBy = function groupBy(){
		var filter_field_values = [];
		var filter_field_titles = [];
		var question_i,
			questions_size;
		
		for(question_i = 0, questions_size = A_FORM_QUESTIONS.length; question_i < questions_size; question_i++){
			filter_field_values.push(A_FORM_QUESTIONS[question_i].id);
			filter_field_titles.push(A_FORM_QUESTIONS[question_i].title);
		}	
		
		var html = fillRadioBox(this.getActionMethod(), this.getHtmlIdWithPrefix(c_list_group_by), filter_field_values, filter_field_titles, this.group_by_item);
		return html;
	};
	this.groupBySave = function groupBySave(){
		this.group_by_item = $('#'+this.getHtmlIdWithPrefix(c_list_group_by)+':checked').val();
		if(!this.groupByValidate()){
			this.group_by_item = null;
			return;
		}
	};
	this.groupByToJson = function groupByToJson(){
		var json = '"values":[';
				json += '"'+this.group_by_item+'"';
			json += ']';
		return json;
	};
	this.groupByFromJson = function groupByFromJson(jsonDoc){
		this.group_by_item = jsonDoc.values[0];
	};
	this.groupByValidate = function groupByValidate(){
		var group_by_item = this.group_by_item;
		if(group_by_item == undefined || group_by_item == null){
			addMessage(jQuery.i18n.prop('err_group_by_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.groupByDraw = function groupByDraw(){
		if(this.group_by_item != null){
			return A_FORM_QUESTIONS[this.group_by_item].title;
		}
		return '';
	};	
	
	//NFIRSTS
	this.nFirsts = function nFirsts(){ 
		return getHtmlInput(this.getActionMethod(), 'number', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 5, null, false);
	};	
	this.nFirstsSave = function nFirstsSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.nFirstsValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.nFirstsToJson = function nFirstsToJson(){
		return this.methodToJson(this.unique_value);
	};
	this.nFirstsFromJson = function nFirstsFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.nFirstsValidate = function nFirstsValidate(){
		var unique_value = this.unique_value;
		unique_value = parseInt(unique_value);
		if(unique_value == undefined || unique_value == null || isNaN(unique_value)){
			addMessage(jQuery.i18n.prop('err_nfirsts_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;		
	};
	this.nFirstsDraw = function nFirstsDraw(){
		return this.methodDraw(this.unique_value);
	};
	
	//NLASTS
	this.nLasts = function nLasts(){ 
		return getHtmlInput(this.getActionMethod(), 'number', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 5, null, false);
	};	
	this.nLastsSave = function nLastsSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.nLastsValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.nLastsToJson = function nLastsToJson(){
		return this.methodToJson(this.unique_value);
	};
	this.nLastsFromJson = function nLastsFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.nLastsValidate = function nLastsValidate(){
		var unique_value = this.unique_value;
		unique_value = parseInt(unique_value);
		if(unique_value == undefined || unique_value == null || isNaN(unique_value)){
			addMessage(jQuery.i18n.prop('err_nlasts_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;		
	};
	this.nLastsDraw = function nLastsDraw(){
		return this.methodDraw(this.unique_value);
	};	
};
//AnalyticsFieldClass.prototype = new AnalyticsTransformationItemClass();


////////// TextBox:
var AnalyticsTextBoxItem = function AnalyticsTextBoxItem(){
	//EQUAL	
	this.equal = function equal(){
		return getHtmlInput(this.getActionMethod(), 'text', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 20, null, false);
	};
	this.equalSave = function equalSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.equalValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.equalToJson = function equalToJson(){
		return this.methodToJson(this.unique_value);
	};
	this.equalFromJson = function equalFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.equalValidate = function equalValidate(){
		var unique_value = this.unique_value;
		if(unique_value == undefined || unique_value == null || unique_value.trim() == ''){
			addMessage(jQuery.i18n.prop('err_equal_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.equalDraw = function equalDraw(){
		return this.methodDraw(this.unique_value);
	};

	//NOT EQUAL
	this.notEqual = function notEqual(){
		return getHtmlInput(this.getActionMethod(), 'text', this.getHtmlIdWithPrefix(c_unique_value), this.unique_value, null, 20, null, false);
	};
	this.notEqualSave = function notEqualSave(){
		this.unique_value = $('#'+this.getHtmlIdWithPrefix(c_unique_value)).val();
		if(!this.notEqualValidate()){
			this.unique_value = null;
			return;
		}
	};
	this.notEqualToJson = function notEqualToJson(){
		return this.methodToJson(this.unique_value);
	};
	this.notEqualFromJson = function notEqualFromJson(jsonDoc){
		this.methodFromJson(jsonDoc);
	};
	this.notEqualValidate = function notEqualValidate(){
		var unique_value = this.unique_value;
		if(unique_value == undefined || unique_value == null || unique_value.trim() == ''){
			addMessage(jQuery.i18n.prop('err_not_equal_required', this.getIdToShow()), 'error');
			return false;
		}
		return true;
	};
	this.notEqualDraw = function notEqualDraw(){
		return this.methodDraw(this.unique_value);
	};
};
AnalyticsTextBoxItem.prototype = new AnalyticsFieldClass();

//////// Numeric: Number, Decimal, Money, Slider
var AnalyticsNumericItem = function AnalyticsNumericItem(){	
};
AnalyticsNumericItem.prototype = new AnalyticsFieldClass();

//////// Box: ComboBox, CheckBox, RadioBox
var AnalyticsBoxItem = function AnalyticsBoxItem(){
	this.unique_value	= null;
	this.group_values	= [];
	this.option_values	= [];
	this.option_titles	= [];
	
	this.fillOptions = function fillOptions(){
		this.option_values = [];
		this.option_titles = [];
		for (var i in this.question.options) {
			this.option_values.push(this.question.options[i]['key']);
			this.option_titles.push(this.question.options[i]['value']);
		}
	};
	
	//COUNT
	this.count = function count(){
		this.fillOptions();
		var html = fillCheckBox(this.getActionMethod(), this.getHtmlIdWithPrefix(c_box_values), this.option_values, this.option_titles, this.group_values);
		return html;
	};
	this.countSave = function countSave(){
		var self = this;
		this.group_values = [];
		$('input:checkbox[name='+this.getHtmlIdWithPrefix(c_box_values)+']:checked').each(function(){
			var pos_v = self.group_values.indexOf(this.value);
			if(pos_v == -1){
				self.group_values.push(this.value);
			}
		});
	};
	this.countToJson = function countToJson(){
		var i,
			group_size;
		var json = '"values":[';
			for(i = 0, group_size = this.group_values.length; i < group_size; i++){
				json += '"'+this.group_values[i]+'"';
				if(i < (group_size-1)){
					json += ',';
				}
			}	
			json += ']';
		return json;
	};
	this.countFromJson = function countFromJson(jsonDoc){
		for(var i in jsonDoc.values){
			this.unique_value.push(jsonDoc.values[i]);
		}
	};
	this.countValidate = function countValidate(){
		var group_values = this.group_values.length;
		if(group_values <= 0){
			addMessage(jQuery.i18n.prop('err_sort_required', this.getIdToShow()), 'error'); 
			return false;
		}
		return true;
	};
	this.countDraw = function countDraw(){
		var html = '<ul>';
			for(var v in this.group_values){
				html += '<li>'+ this.question.options[this.group_values[v]]['value'] +'</li>';
			}
			html += '</ul>';		
		return html;
	};
	
	//EQUAL	
	this.equal = function equal(){
		this.fillOptions();
		return fillCheckBox(this.getActionMethod(), this.getHtmlIdWithPrefix(c_box_values), this.option_values, this.option_titles, this.group_values);
	};
	this.equalSave = function equalSave(){
		var self = this;
		this.group_values = [];
		$('input:checkbox[name='+this.getHtmlIdWithPrefix(c_box_values)+']:checked').each(function(){
			var pos_v = self.group_values.indexOf(this.value);
			if(pos_v == -1){
				self.group_values.push(this.value);
			}
		});
	};
	this.equalToJson = function equalToJson(){
		var i,
			group_size;
		var json = '"values":[';
			for(i = 0, group_size = this.group_values.length; i < group_size; i++){
				json += '"'+this.group_values[i]+'"';
				if(i < (group_size-1)){
					json += ',';
				}
			}	
			json += ']';
		return json;
	};
	this.equalFromJson = function equalFromJson(jsonDoc){
		var _values = jsonDoc.values;
		for(var i in _values){
			this.group_values.push(_values[i]);
		}
	};
	this.equalValidate = function equalValidate(){
		var group_values = this.group_values.length;
		if(group_values <= 0){
			addMessage(jQuery.i18n.prop('err_equal_required', this.getIdToShow()), 'error'); 
			return false;
		}
		return true;
	};
	this.equalDraw = function equalDraw(){
		var html = '<ul>';
			for(var v in this.group_values){
				html += '<li>'+ this.question.options[this.group_values[v]]['value'] +'</li>';
			}
			html += '</ul>';		
		return html;
	};

	//NOT EQUAL
	this.notEqual = function notEqual(){
		this.fillOptions();
		return fillCheckBox(this.getActionMethod(), this.getHtmlIdWithPrefix(c_box_values), this.option_values, this.option_titles, this.group_values);
	};
	this.notEqualSave = function notEqualSave(){
		var self = this;
		this.group_values = [];
		$('input:checkbox[name='+this.getHtmlIdWithPrefix(c_box_values)+']:checked').each(function(){
			var pos_v = self.group_values.indexOf(this.value);
			if(pos_v == -1){
				self.group_values.push(this.value);
			}
		});
	};
	this.notEqualToJson = function notEqualToJson(){
		var i,
			group_size;
		var json = '"values":[';
			for(i = 0, group_size = this.group_values.length; i < group_size; i++){
				json += '"'+this.group_values[i]+'"';
				if(i < (group_size-1)){
					json += ',';
				}
			}	
			json += ']';
		return json;
	};
	this.notEqualFromJson = function notEqualFromJson(jsonDoc){
		var _values = jsonDoc.values;
		for(var i in _values){
			this.group_values.push(_values[i]);
		}
	};
	this.notEqualValidate = function notEqualValidate(){
		var group_values = this.group_values.length;
		if(group_values <= 0){
			addMessage(jQuery.i18n.prop('err_not_equal_required', this.getIdToShow()), 'error'); 
			return false;
		}
		return true;
	};
	this.notEqualDraw = function notEqualDraw(){
		var html = '<ul>';
			for(var v in this.group_values){
				html += '<li>'+ this.question.options[this.group_values[v]]['value'] +'</li>';
			}
			html += '</ul>';		
		return html;
	};
};
AnalyticsBoxItem.prototype = new AnalyticsFieldClass();

//////// Date: Date
var AnalyticsDateItem = function AnalyticsDateItem(){	
};
AnalyticsDateItem.prototype = new AnalyticsFieldClass();

//////// Multimedia: Picture, Audio, Video, Draw
var AnalyticsMultimediaItem = function AnalyticsMultimediaItem(){	
};
AnalyticsMultimediaItem.prototype = new AnalyticsFieldClass();

//////// Geolocation
var AnalyticsGeolocationItem = function AnalyticsGeolocationItem(){
	
	//NEAREST
	this.nearest = function nearest(){ return '';	};
	this.nearestSave = function nearestSave(){};
	this.nearestToXml = function nearestToXml(){ return ''; };
	this.nearestToJson = function nearestToJson(){ return ''; };	
	this.nearestFromXml = function nearestFromXml(xmlDoc){};	
	this.nearestFromJson = function nearestFromJson(jsonDoc){};
	this.nearestValidate = function nearestValidate(){ return true; };
	this.nearestDraw = function nearestDraw(){ return ''; };
};
AnalyticsGeolocationItem.prototype = new AnalyticsFieldClass();

//////// BarCode:
var AnalyticsBarCodeItem = function AnalyticsBarCodeItem(){	
};
AnalyticsBarCodeItem.prototype = new AnalyticsFieldClass();
////////////////////////////////////////////////////////////////////////////////////////////////////////
var aFieldFactory = function aFieldFactory(question_item, transf_id){
	var field;
	switch (question_item.type) {
	case 'geolocation':
		field = new AnalyticsGeolocationItem();
		break;
	case 'text':
		field = new AnalyticsTextBoxItem();		
		break;
	case 'number':
		field = new AnalyticsNumericItem();
		break;
	case 'decimal':
		field = new AnalyticsNumericItem();
		break;
	case 'money':
		field = new AnalyticsNumericItem();
		break;	
	case 'slider':
		field = new AnalyticsNumericItem();
		break;
	case 'combobox':
		field = new AnalyticsBoxItem();
		break;
	case 'checkbox':
		field = new AnalyticsBoxItem();
		break;
	case 'radio':
		field = new AnalyticsBoxItem();
		break;
	case 'date':
		field = new AnalyticsDateItem();
		break;
	case 'picture':
		field = new AnalyticsMultimediaItem();
		break;
	case 'audio':
		field = new AnalyticsMultimediaItem();
		break;
	case 'video':
		field = new AnalyticsMultimediaItem();
		break;
	case 'draw':
		field = new AnalyticsMultimediaItem();
		break;
	case 'barcode':
		field = new AnalyticsBarCodeItem();
		break;
	default:
		return null;
	}
	
	field.question = question_item;
	field.type = question_item.type;
	field.transformation = transf_id;
	
	field = aFunctionFactory(transf_id, field);
	return field;
};
////////////////////////////////////////////////////////////////////////////////////////////////////////
var aFunctionFactory = function aFunctionFactory(transf_id, field){
	var method;
	var weight;
	switch(transf_id){
		case T_GROUP_BY.name:
			method = T_GROUP_BY.method;
			weight = T_GROUP_BY.weight;
			break;
		case T_SORT.name:
			method = T_SORT.method;
			weight = T_SORT.weight;
			break;
		case T_MIN.name:
			method = T_MIN.method;
			weight = T_MIN.weight;
			break;
		case T_MAX.name:
			method = T_MAX.method;
			weight = T_MAX.weight;
			break;
		case T_NMIN.name:
			method = T_NMIN.method;
			weight = T_NMIN.weight;
			break;
		case T_NMAX.name:
			method = T_NMAX.method;
			weight = T_NMAX.weight;
			break;		
		case T_COUNT.name:
			method = T_COUNT.method;
			weight = T_COUNT.weight;
			break;
		case T_EQUAL.name:
			method = T_EQUAL.method;
			weight = T_EQUAL.weight;
			break;
		case T_NOT_EQUAL.name:
			method = T_NOT_EQUAL.method;
			weight = T_NOT_EQUAL.weight;
			break;
		case T_GREATER.name:
			method = T_GREATER.method;
			weight = T_GREATER.weight;
			break;
		case T_LESS.name:
			method = T_LESS.method;
			weight = T_LESS.weight;
			break;
		case T_GREATER_EQUAL.name:
			method = T_GREATER_EQUAL.method;
			weight = T_GREATER_EQUAL.weight;
			break;
		case T_LESS_EQUAL.name:
			method = T_LESS_EQUAL.method;
			weight = T_LESS_EQUAL.weight;
			break;
		case T_SUM.name:
			method = T_SUM.method;
			weight = T_SUM.weight;
			break;
		case T_AVERAGE.name:
			method = T_AVERAGE.method;
			weight = T_AVERAGE.weight;
			break;
		case T_NFIRSTS.name:
			method = T_NFIRSTS.method;
			weight = T_NFIRSTS.weight;
			break;
		case T_NLASTS.name:
			method = T_NLASTS.method;
			weight = T_NLASTS.weight;
			break;
		case T_NEAREST.name:
			method = T_NEAREST.method;
			weight = T_NEAREST.weight;
			break;
		default:
			return null;
	}
	
	field.call_method		= method;
	field.weight_method		= weight;
	field.save_method		= field.call_method+'Save';
	field.to_xml_method		= field.call_method+'ToXml';
	field.from_xml_method	= field.call_method+'FromXml';
	field.to_json_method	= field.call_method+'ToJson';
	field.from_json_method	= field.call_method+'FromJson';
	field.validate_method	= field.call_method+'Validate';
	field.draw_method		= field.call_method+'Draw';
	return field;	
};