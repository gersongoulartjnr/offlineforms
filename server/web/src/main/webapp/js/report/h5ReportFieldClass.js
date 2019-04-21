//// Field Factory Method for ReportEditor ////
var fieldFactoryMethod = function fieldFactoryMethod(type, question){
	var field;
	switch (type) {
		case 'text':
			field = new ReportTextBox(type);
			field.title = jQuery.i18n.prop('msg_form_textBoxTitle');
			break;
		case 'number':
			field = new ReportNumber(type);
			field.title = jQuery.i18n.prop('msg_form_numberTitle');
			break;
		case 'decimal':
			field = new ReportDecimal(type);
			field.title = jQuery.i18n.prop('msg_form_decimalTitle');
			break;
		case 'money':
			field = new ReportMoney(type);
			field.title = jQuery.i18n.prop('msg_form_moneyTitle');
			break;
		case 'date':
			field = new ReportDate(type);
			field.title = jQuery.i18n.prop('msg_form_dateTitle');
			break;
		case 'checkbox':
			field = new ReportCheckBox(type);
			field.title = jQuery.i18n.prop('msg_form_checkBoxTitle');
			break;
		case 'radio':
			field = new ReportRadioBox(type);
			field.title = jQuery.i18n.prop('msg_form_radioBoxTitle');
			break;
		case 'combobox':
			field = new ReportComboBox(type);
			field.title = jQuery.i18n.prop('msg_form_comboBoxTitle');
			break;
		case 'picture':
			field = new ReportPicture(type);
			field.title = jQuery.i18n.prop('msg_form_pictureTitle');
			break;
		case 'audio':
			field = new ReportAudio(type);
			field.title = jQuery.i18n.prop('msg_form_audioTitle');
			break;
		case 'video':
			field = new ReportVideo(type);
			field.title = jQuery.i18n.prop('msg_form_videoTitle');
			break;
		case 'geolocation':
			field = new ReportGeoLocation(type);
			field.title = jQuery.i18n.prop('msg_form_geolocationTitle');
			break;
		case 'barcode':
			field = new ReportBarcode(type);
			field.title = jQuery.i18n.prop('msg_form_barcodeTitle');
			break;
		case 'slider':
			field = new ReportSlider(type);
			field.title = jQuery.i18n.prop('msg_form_sliderTitle');
			break;
		case 'draw':
			field = new ReportDraw(type);
			field.title = jQuery.i18n.prop('msg_form_drawTitle');
			break;
		default:
			return null;
	}
	if(field != null && question != undefined && question != null){
		field.question = question;
	}	
	return field;
};

var itemId = function itemId(id){
	var idNumber = ++id;
	return idNumber + '.- ';	
};

var ReportField = function ReportField() {
	this.type	= '';
	this.question = '';
	this.result	= null;
	this.title	= '';
	this.option	= null;
	this.chart	= null;
	this.id		= '';
	
	this.values_ids		= [];
	this.values_titles	= [];//report
	this.values_vtitles	= [];//viewer
	this.values_values	= [];
	
	//this.charts		= [['Table', 'table'], ['Bar', 'bar'], ['Pie','pie']];
	this.charts_titles	= ['Table', 'Bar', 'Pie'];
	this.charts_values	= ['table', 'bar', 'pie'];	
	
	this.field_title	= 'fieldTitle';
	this.field_options	= 'fieldOptions';
	this.field_values	= 'fieldValues';
	this.field_charts	= 'fieldCharts';
	
	//Dont touch this!
	this.txt_id			= 'id';
	this.txt_title		= 'title';
	this.txt_op			= 'operation';
	this.txt_chart		= 'chart';
	this.txt_question	= 'question';
	this.txt_type		= 'type';
	this.txt_simple_res	= 'simple';
	this.txt_map_res	= 'map';
	this.txt_key		= 'key';
	this.txt_value		= 'value';	
	this.txt_label		= 'label';		//form: question title
	this.txt_option		= 'option';		//form: option name(Box)

	this.setFormFromXMLDoc = function setFormFromXMLDoc(xmlDoc){
		var $xmlDoc = $(xmlDoc);
		if(xmlDoc.nodeName != this.type){
			//console.error(xmlDoc.nodeName + " is not a compatible type for " + this.type);
			return;
		}
		this.title	= $xmlDoc.find(this.txt_label).text();
		this.setSpecificFormFromXML(xmlDoc);
	};
	
	this.setReportFromXMLDoc = function setReportFromXMLDoc(xmlDoc){
		var $xmlDoc = $(xmlDoc);
		if(xmlDoc.nodeName != this.type){
			//console.error(xmlDoc.nodeName + " is not a compatible type for " + this.type);
			return;
		}
		this.id		= $xmlDoc.attr(this.txt_id);
		this.title	= $xmlDoc.find(this.txt_title).text();
		this.option	= $xmlDoc.find(this.txt_op).text();
		this.chart	= $xmlDoc.find(this.txt_chart).text();	
		this.question = $xmlDoc.find(this.txt_question).text();
		this.setSpecificReportFromXML(xmlDoc);
	};	
	
	this.toHTMLForm = function toHTMLForm(i) {
		var html = '<label class="lbl_form">';
		html += itemId(i) + this.title;
		html += '</label>';	
		return html;		
	};
	this.toHTMLReport = function toHTMLReport(i) {
		this.id = i;
		var html = '<label class="lbl_report">';
		html += itemId(i) + this.title;
		html += '</label>';
		
		html += '<div class="options">';
		html += '<a href="#" onclick="moveElement(-1); return false;"><img src="images/form/arrow_up.png" /></a>'; 
		html += '<a href="#" onclick="moveElement(1); return false;"><img src="images/form/arrow_down.png" /></a>';
		html += '<a href="#" onclick="removeReportItem('+"'"+i+"'"+')" return false;><img src="images/remove.png" /></a>';
		html += '</div>';
		return html;
	};
	
	this.toXMLReport = function toXMLReport(i){
		var xml = '';
		xml += '<'+ this.type +' '+this.txt_id+'="'+ this.id +'">';
			xml += '<'+this.txt_question+'>'+ this.question +'</'+this.txt_question+'>';
			xml += '<'+this.txt_title+'>'+ this.title +'</'+this.txt_title+'>';
			xml += '<'+this.txt_op+'>'+ this.option +'</'+this.txt_op+'>';
			xml += this.toXMLSpecificReport();
		xml += '</'+ this.type +'>';
		return xml;
	}; 
	
	this.dataToDownload = function dataToDownload(){
		var data = '';
			data += '{';
				data += '"'+this.txt_type+'":"'+this.type+'"';
				data += ', "'+this.txt_id+'":"'+this.id+'"';
				data += ', "'+this.txt_title+'":"'+this.title+'"';
				data += ', "'+this.txt_op+'":"'+this.option+'"';
				data += this.dataSpecificToDownload();
			data += '}';
		return data;
	};
	
	this.showFieldProperties = function showFieldProperties(prop_container_id){
		/*if(this.options_titles == undefined && this.options_values == undefined){
			return;
		}*/
		var html = '<table>';
		var title = jQuery.i18n.prop('msg_field_titleProperty');
		var options = jQuery.i18n.prop('msg_field_optionsProperty');
		html += createTextProperty(this.field_title, this.title, title);
		html += '<tr><td colspan="2">&nbsp;</td></tr>';
		if(this.options_titles != undefined && this.options_values != undefined){
			//set a value by default
			if(this.option == null){
				this.option	= this.options_values[0];
			}
			html += createRadioBoxProperty(this.field_options, this.options_titles, this.options_values, this.option, options);
			html += '<tr><td colspan="2">&nbsp;</td></tr>';
			var extra = this.showExtraFieldProperties();
			if(extra != null && extra != ''){
				html += extra;
			}
		}
		html += '</table>';
		$('#'+prop_container_id).append(html);		
	};
	
	this.validateProperties = function validateProperties(i){
		var title = this.title;
		var id = parseInt(i) + 1;
		if(title == undefined || title == null || title == ''){
			addMessage(jQuery.i18n.prop('msg_report_item_title_required',  id), 'error');
			return false;
		}		
		if(title != '' && (title.length < 0 || title.length > 100)){
			addMessage(jQuery.i18n.prop('msg_report_item_title_error_size', id), 'error');
			return false;
		}
		if(this.option == undefined || this.option == null){
			addMessage(jQuery.i18n.prop('msg_report_item_option_required', id), 'error');
			return false;
		}
		if(!this.validateSpecific(id)){
			return;
		}
		return true;
	};
	
	this.verifyElement = function verifyElement(i){
		var title = this.title;
		var id = parseInt(i) + 1;
		if(title == undefined || title == null || title == ''){
			addMessage(jQuery.i18n.prop('msg_report_item_title_required',  id), 'error');
			return false;
		}		
		if(title != '' && (title.length < 0 || title.length > 100)){
			addMessage(jQuery.i18n.prop('msg_report_item_title_error_size', id), 'error');
			return false;
		}
		if(this.option == undefined || this.option == null || this.option == 'null'){
			addMessage(jQuery.i18n.prop('msg_report_item_option_required', id), 'error');
			return false;
		}
		if(!this.verifySpecificElement(id)){
			return;
		}
		return true;
	};
	
	this.saveProperties = function saveProperties(){	
		this.title	= $('#'+this.field_title).val();
		this.option	= $('input:radio[name='+this.field_options+']:checked').val();
		if(!this.validateProperties(this.id)){
			return;
		}
		this.saveSpecificProperties();
	};
	
	this.toHTMReportViewer = function toHTMReportViewer(){
		//validator ?		
		var html = '<div class="rc-v-item">';
			html += '<label class="r-v-subtitle">'+itemId(this.id) + this.title+'</label>';
			html += this.toHTMSpecificReportViewer();
			html += '</div>';		
		return html;
	};
	
	this.getChartsToShow = function getChartsToShow(charts_to_show){
		var i;
		var charts_keys  = [];
		var charts_names = [];
		for(i = 0; i < charts_to_show.length; i++){
			charts_keys.push(this.charts_values[i]);
			charts_names.push(this.charts_titles[i]);
		}
		return [charts_keys, charts_names];
	}; 
};

var ReportNumeric = function ReportNumeric() {
	this.options_titles = ['Average', 'Maximum', 'Minimum', 'Summation'];
	this.options_values = ['avg', 'max', 'min', 'sum'];
	
	this.dataSpecificToDownload = function dataSpecificToDownload(){
		var data = ', "'+this.txt_simple_res+'":"'+this.result+'"';
		return data;
	};
	this.setSpecificFormFromXML = function setSpecificFormFromXML(xmlDoc){};
	this.setSpecificReportFromXML = function setSpecificReportFromXML(xmlDoc){};
	this.toXMLSpecificReport = function toXMLSpecificReport(){ return ''; };
	this.saveSpecificProperties = function saveSpecificProperties(){};
	this.validateSpecific = function validateSpecific(id){ return true; };
	this.verifySpecificElement = function verifySpecificElement(id){ return true; };
	this.setExtraValues = function setExtraValues(data){};
	this.showExtraFieldProperties = function showExtraFieldProperties(){};	
	
	this.toHTMSpecificReportViewer = function toHTMSpecificReportViewer(){
		var pos_t = this.options_values.indexOf(this.option);
		var html = '<div class="r-v-c-res">';
		if(pos_t != -1){
			html += '<span class="r-v-op">'+ this.options_titles[pos_t] + ': </span>';
			html += '<span class="r-v-res">'+ this.result + '</span>';
		}
		html += '</div>';
		return html;
	};
};
ReportNumeric.prototype = new ReportField();

//Number
var ReportNumber = function ReportNumber(type){
	this.type = type;
};
ReportNumber.prototype = new ReportNumeric();

//Number
var ReportDecimal = function ReportDecimal(type){
	this.type = type;
};
ReportDecimal.prototype = new ReportNumeric();

//Number
var ReportMoney = function ReportMoney(type){
	this.type = type;
};
ReportMoney.prototype = new ReportNumeric();

// TextBox
var ReportTextBox = function ReportTextBox(type) {
	this.type = type;
	
	this.dataSpecificToDownload = function dataSpecificToDownload(){};
	this.setSpecificFormFromXML = function setSpecificFormFromXML(xmlDoc){};
	this.setSpecificReportFromXML = function setSpecificReportFromXML(xmlDoc){};
	this.toXMLSpecificReport = function toXMLSpecificReport(){ return ''; };
	this.saveSpecificProperties = function saveSpecificProperties(){};
	this.validateSpecific = function validateSpecific(id){ return true; };
	this.verifySpecificElement = function verifySpecificElement(id){ return true; };
	this.setExtraValues = function setExtraValues(data){};
	this.showExtraFieldProperties = function showExtraFieldProperties(){};	
};
ReportTextBox.prototype = new ReportField();

//Date
var ReportDate = function ReportDate(type) {
	this.type = type;
	
	this.dataSpecificToDownload = function dataSpecificToDownload(){};
	this.setSpecificFormFromXML = function setSpecificFormFromXML(xmlDoc){};
	this.setSpecificReportFromXML = function setSpecificReportFromXML(xmlDoc){};
	this.toXMLSpecificReport = function toXMLSpecificReport(){ return ''; };
	this.saveSpecificProperties = function saveSpecificProperties(){};
	this.validateSpecific = function validateSpecific(id){ return true; };
	this.verifySpecificElement = function verifySpecificElement(id){ return true; };
	this.setExtraValues = function setExtraValues(data){};
	this.showExtraFieldProperties = function showExtraFieldProperties(){};
};
ReportDate.prototype = new ReportField();

// //Box
var ReportBox = function ReportBox() {
	this.options_titles	= ['Total values'];
	this.options_values	= ['totByVal'];
	this.options_extra	= [0];//index for options_values which has extra properties, 0->totByVal in this case	
	this.options_chart	= [[0, 1, 2]];
	this.options_plot	= [];
	
	this.setSpecificFormFromXML = function setSpecificFormFromXML(xmlDoc){
		var $xmlDoc = $(xmlDoc);
		var options_parsed = [];		
		var options_found  = $xmlDoc.find(this.txt_option);
		for(var i=0; i<options_found.length; i++){
			options_parsed.push(options_found[i].textContent);
		}
		this.values_titles = options_parsed;
	};
	
	this.setExtraValues = function setExtraValues(data){
		if(data != undefined && data != null){
			this.values_titles = data;
		}	
	};
	
	this.setSpecificReportFromXML = function setSpecificReportFromXML(xmlDoc){
		var $xmlDoc = $(xmlDoc);
		this.values_ids		= [];
		this.values_vtitles	= [];
		var values_found	= $xmlDoc.find(this.txt_value);		
		for(var i = 0; i < values_found.length; i++){
			this.values_ids.push(parseInt(values_found[i].attributes[this.txt_id].value));
			this.values_vtitles.push(values_found[i].textContent);
		}
	};
	
	this.toXMLSpecificReport = function toXMLSpecificReport(){
		var xml = '';
		if(this.values_ids.length > 0){
			if(this.chart != null){
				xml += '<'+this.txt_chart+'>'+ this.chart +'</'+this.txt_chart+'>';
			}
			for(var i in this.values_ids){
				xml += '<'+this.txt_value+' id="'+this.values_ids[i]+'">'+this.values_titles[this.values_ids[i]]+'</'+this.txt_value+'>';
			}
		}
		return xml;
	};
	
	this.saveSpecificProperties = function saveSpecificProperties(){
		this.values_ids = [];
		var self = this;
		$('input:checkbox[name='+this.field_values+']:checked').each(function(){
			var pos_v = self.values_ids.indexOf(this.value); 
			if(pos_v == -1){
				self.values_ids.push(parseInt(this.value));
			}
		});
		this.chart = null;
		if(this.values_ids.length > 0){
			this.chart	= $('input:radio[name='+this.field_charts+']:checked').val();
		}
	};
	
	this.validateSpecific = function validateSpecific(id){
		var pos_o = this.options_values.indexOf(this.option);
		if(pos_o == -1)
			return false;			
		var pos_e = this.options_extra.indexOf(pos_o);
		if(pos_e == -1){//Don't need to have extra properties()
			return true;
		}
		
		var values_ids = [];
		$('input:checkbox[name='+this.field_values+']:checked').each(function(){
			var pos_v = values_ids.indexOf(this.value); 
			if(pos_v == -1){
				values_ids.push(parseInt(this.value));
			}
		});
		if(values_ids.length == 0){
			addMessage(jQuery.i18n.prop('msg_report_item_value_required',  id), 'error');
			return false;
		}
		
		var chart = $('input:radio[name='+this.field_charts+']:checked').val();
		if(this.values_ids.length > 0 && (chart == undefined || chart == null)){
			addMessage(jQuery.i18n.prop('msg_report_item_chart_required',  id), 'error');
			return false;
		}
		return true; 
	};
	
	this.verifySpecificElement = function verifySpecificElement(id){
		var pos_o = this.options_values.indexOf(this.option);
		if(pos_o == -1)
			return false;			
		var pos_e = this.options_extra.indexOf(pos_o);
		if(pos_e == -1){
			return true;
		}
		if(this.values_ids.length == 0){
			addMessage(jQuery.i18n.prop('msg_report_item_value_required',  id), 'error');
			return false;
		}
		if(this.values_ids.length > 0 && this.chart == null){
			addMessage(jQuery.i18n.prop('msg_report_item_chart_required',  id), 'error');
			return false;
		}
		return true;
	};
	
	this.setValuesForValuesTitles = function setValuesForValuesTitles() {
		var idx;
		this.values_values = [];
		for(idx = 0; idx < this.values_titles.length; idx++){
			this.values_values.push(idx);
		}
		return this.values_values;
	};
	
	this.shouldShowExtraProperties = function shouldShowExtraProperties(){
		var pos_v = this.options_values.indexOf(this.option);
		if(pos_v != -1){
			var pos_e = this.options_extra.indexOf(pos_v);
			if(pos_e != -1){
				return pos_e;
			}
		}
		return null;
	};
	
	this.showExtraFieldProperties = function showExtraFieldProperties(){
		var values	= jQuery.i18n.prop('msg_field_valuesProperty');
		var charts	= jQuery.i18n.prop('msg_field_chartsProperty');
		var html = '';
		if(this.option != null && this.shouldShowExtraProperties() != null){
			var values_for_val_titles = this.setValuesForValuesTitles();
			this.values_ids = this.values_ids.length > 0 ? this.values_ids : values_for_val_titles;
			html += createCheckBoxProperty(this.field_values, this.values_titles, values_for_val_titles, this.values_ids, values);
			html += '<tr><td colspan="2">&nbsp;</td></tr>';
			var pos_k = this.options_values.indexOf(this.option);
			if(pos_k != -1 && pos_k < this.options_chart.length){
				var charts_arr = this.getChartsToShow(this.options_chart[pos_k]);
				if(charts_arr.length == 2){
					//set a value by default
					if(this.chart == null){
						this.chart	= charts_arr[0][0];
					}
					html += createRadioBoxProperty(this.field_charts, charts_arr[1], charts_arr[0], this.chart, charts);
				}
			}
		}
		else{
			this.values_ids = [];
			this.chart = null;
		}
		return html;
	};	
	
	this.setDataToPlot = function setDataToPlot(){
		this.options_plot = [];
		if(this.result != null){
			var entry = this.result;
			for(var i in entry){
				var pos_k = this.values_ids.indexOf(parseInt(i));
				if(pos_k == -1){
					this.options_plot = [];
					return;
				}
				else{
					this.options_plot.push([this.values_vtitles[pos_k], parseInt(entry[i])]);
				}
			}
		}
	};
	
	this.toHTMSpecificReportViewer = function toHTMSpecificReportViewer(){
		var pos_t;
		pos_t = this.options_values.indexOf(this.option);
		var html = '<div class="r-v-c-res">';
		if(pos_t != -1){
			this.setDataToPlot();
			html += '<span class="r-v-op">'+ this.options_titles[pos_t] + ': </span>';			
			html += '<span class="r-v-res">';
			
			if(this.chart != null && this.options_plot != null && this.chart == this.charts_values[0]){
				html += '<table class="r-v-table">';
				html += '<thead><tr><th>Title</th><th>Value</th></tr></thead>';
				for(var i in this.options_plot){
					html += '<tr>';
						html += '<td>'+this.options_plot[i][0]+'</td><td> '+this.options_plot[i][1]+'</td>';
					html += '</tr>';
				}
				html += '</table>';
			}
			else{
				var div_chart_id = this.type+'_'+this.question+'_'+this.id;
				html += '<div id="'+div_chart_id+'"></div>';
				if(this.options_plot.length > 0){
					addChart(this.chart, div_chart_id, this.options_plot);
				}
			}
			html += '</span>';			
		}
		html += '</div>';
		return html;
	};
	
	this.dataSpecificToDownload = function dataSpecificToDownload(){
		if(this.options_plot != null){
			var total_elems = this.options_plot.length - 1;
			var data = ',';
				data += '"'+this.txt_map_res+'":[';
					for(var i in this.options_plot){
						data += '{';
							data += '"'+this.txt_key+'":"'+this.options_plot[i][0]+'"';
							data += ', "'+this.txt_value+'":"'+this.options_plot[i][1]+'"';
						data += '}';
						//
						if(i == total_elems){ 
							data += ''; 
						}
						else{
							data += ',';
						}
					}
				data += ']';
			return data;
		}
		else{
			var data = ', "'+this.txt_simple_res+'":"'+this.result+'"';
			return data;
		}
	};
};
ReportBox.prototype = new ReportField();

// CheckBox
var ReportCheckBox = function ReportCheckBox(type) {
	this.type = type;	
};
ReportCheckBox.prototype = new ReportBox();

// RadioBox
var ReportRadioBox = function ReportRadioBox(type) {
	this.type = type;
};
ReportRadioBox.prototype = new ReportBox();

//ComboBox
var ReportComboBox = function ReportComboBox(type){
	this.type = type;
};
ReportComboBox.prototype = new ReportBox();

//Geolocation
var ReportGeoLocation = function ReportGeoLocation(type) {
	this.type = type;
	this.options_titles	= ['All', 'The last 10', 'The nearest'];
	this.options_values	= ['list_all', 'list_last_n', 'nearest_points'];
	
	this.dataSpecificToDownload = function dataSpecificToDownload(){};
	this.setSpecificFormFromXML = function setSpecificFormFromXML(xmlDoc){};
	this.setSpecificReportFromXML = function setSpecificReportFromXML(xmlDoc){};
	this.toXMLSpecificReport = function toXMLSpecificReport(){ return ''; };
	this.saveSpecificProperties = function saveSpecificProperties(){};
	this.validateSpecific = function validateSpecific(id){ return true; };
	this.verifySpecificElement = function verifySpecificElement(id){ return true; };
	this.setExtraValues = function setExtraValues(data){};
	this.showExtraFieldProperties = function showExtraFieldProperties(){};
	
	this.toHTMSpecificReportViewer = function toHTMSpecificReportViewer(){
		var points = [],
			i;
		for(i = 0; i < this.result.length; i++){
			points.push(this.result[i]);
		}		
		var pos_t = this.options_values.indexOf(this.option);
		var html = '<div class="r-v-c-res">';
		if(pos_t != -1){
			var div_chart_id = this.type+'_'+this.question+'_'+this.id;
			html += '<span class="r-v-op">'+ this.options_titles[pos_t] + ': </span>';
			html += '<span class="r-v-res">';
			html += '<div id="'+div_chart_id+'" style=\"width:550px; height:470px\"></div>';
				if(points.length > 0){
					addChart(this.option, div_chart_id, points);
				}
			html += '</span>';
		}
		html += '</div>';
		return html;
	};
};
ReportGeoLocation.prototype = new ReportField();

//Basic
var ReportBasic = function ReportBasic() {
	this.dataSpecificToDownload = function dataSpecificToDownload(){};
	this.setSpecificFormFromXML = function setSpecificFormFromXML(xmlDoc){};
	this.setSpecificReportFromXML = function setSpecificReportFromXML(xmlDoc){};
	this.toXMLSpecificReport = function toXMLSpecificReport(){ return ''; };
	this.saveSpecificProperties = function saveSpecificProperties(){};
	this.validateSpecific = function validateSpecific(id){ return true; };
	this.verifySpecificElement = function verifySpecificElement(id){ return true; };
	this.setExtraValues = function setExtraValues(data){};
	this.showExtraFieldProperties = function showExtraFieldProperties(){};
};
ReportBasic.prototype = new ReportField();

//Slider
var ReportSlider = function ReportSlider(type) {
	this.type = type;
};
ReportSlider.prototype = new ReportBasic();

//Barcode
var ReportBarcode = function ReportBarcode(type) {
	this.type = type;
};
ReportBarcode.prototype = new ReportBasic();

//Draw
var ReportDraw = function ReportDraw(type) {
	this.type = type;
};
ReportDraw.prototype = new ReportBasic();

//Picture
var ReportPicture = function ReportPicture(type) {
	this.type = type;
};
ReportPicture.prototype = new ReportBasic();

//Audio
var ReportAudio = function ReportAudio(type) {
	this.type = type;
};
ReportAudio.prototype = new ReportBasic();

//Video
var ReportVideo = function ReportVideo(type) {
	this.type = type;
};
ReportVideo.prototype = new ReportBasic();