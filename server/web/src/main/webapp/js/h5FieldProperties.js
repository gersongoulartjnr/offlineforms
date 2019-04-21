// Functions to create the Field properties
var inputCreator = function(type, id, value, name, size, styleClass) {
	var inputString = '<input ';
	inputString += attribCreator( 'type' , type);
	if(id!=null){
		inputString += attribCreator('id', id);
	}
	if(value!=null || value != ""){
		inputString += attribCreator('value', value);
	}
	inputString += attribCreator('onchange', 'saveField()');
	if(size!=null){
		inputString += attribCreator('size', size);
	}
	if(styleClass!=null){
		inputString += attribCreator('class', styleClass);
	}
	switch (type) {
		case 'radio':
		case 'checkbox':
			inputString += attribCreator('name', name);
			if(value == true)
				inputString += attribCreator('checked', value);
			break;
		case 'number':			
			inputString += attribCreator('step', "any");
			break;
		default:
			break;
	}
	inputString += '/>';
	return inputString;
};

var inputNumberCreator = function inputNumberCreator(type, subtype, id, value, name, size, styleClass) {
	var inputString = '<input ';
	inputString += attribCreator( 'type' , type);
	if(id!=null){
		inputString += attribCreator('id', id);
	}
	if(value!=null || value != ""){
		inputString += attribCreator('value', value);
	}
	inputString += attribCreator('onchange', 'saveField()');
	if(size!=null){
		inputString += attribCreator('size', size);
	}
	if(styleClass!=null){
		inputString += attribCreator('class', styleClass);
	}
	switch (subtype) {
		case 'number':			
			inputString += attribCreator('step', "1");			
			inputString += attribCreator('onkeypress', 'return validDigits(event);');
			break;
		case 'decimal':
			inputString += attribCreator('step', "any");
			inputString += attribCreator('onkeypress', 'return validDecimals(event);');
			break;
		case 'money':
			inputString += attribCreator('step', "0.01");
			inputString += attribCreator('onkeypress', 'return validDecimals(event);');
			break;
		default:
			break;
	}
	inputString += '/>';
	return inputString;
};

var selectCreator = function selectCreator(id, name, multiple){
	var selectString = '<select ';
		selectString += attribCreator('id', id);
		selectString += attribCreator('name', name);
		selectString += attribCreator('onchange', 'saveField()');
		if(multiple){
			selectString += " multiple='multiple'";
		}
		selectString += '>';
	return selectString;
};

var reportInputCreator = function(type, id, value, name, size, styleClass, selected) {
	var inputString = '<input ';
	inputString += attribCreator( 'type' , type);
	if(id!=null){
		inputString += attribCreator('id', id);
	}
	if(value!=null || value != ""){
		inputString += attribCreator('value', value);
	}
	inputString += attribCreator('onchange', 'saveField()');
	if(size!=null){
		inputString += attribCreator('size', size);
	}
	if(styleClass!=null){
		inputString += attribCreator('class', styleClass);
	}
	switch (type) {
		case 'radio':
		case 'checkbox':
			inputString += attribCreator('name', name);
			if(selected == true) {
				inputString += attribCreator('checked', selected);
			}
			break;
		default:
			break;
	}
	inputString += '/>';
	return inputString;
};

var  attribCreator = function (field, value){
	if(field == 'max' || field == 'min') {
		if(value == '' || value == undefined)
			return '';
		else
			return field + '="' + value + '" ';
	}
	else {
		return field + '="' + value + '" ';
	}
};

var  tagCreatorHTML = function (tag, value, styleClass){
	var styleTag = '';
	if(styleClass!=null){
		styleTag = " class=\""+styleClass+"\" ";
	}	
	return '<' + tag + styleTag + '>' + value + '</' + tag + '>';
};

var  tagCreatorXML = function (tag, value){
	return '<' + tag + '>' + replaceXMLEntityByCode(value) + '</' + tag + '>';
};

//general method to create a label
var createLabelProperty = function(id, title){
	var label = '<tr><td colspan="2">' + tagCreatorHTML('label', title) + '</td></tr>';
	return label;
};

//general method to create text component
var createTextProperty = function(id, value, title){
	var label = '<tr><td>' + tagCreatorHTML('label', title) + '</td>';
	var size = null;
	switch (id) {
		case 'fieldLabel':
		case 'fieldHelp':
		case 'fieldDefault':			
			size = 22;
			break;
		case 'fieldTitle':
			//Title for Report properties
			size = 20;
			break;
	}
	label += '<td>' + inputCreator('text', id, value, null, size, null) + '</td></tr>';
	return label;
};

//general method to create number component
var createNumberProperty = function(id, value, title) {
	var label = '<tr><td>' + tagCreatorHTML('label', title) + '</td>';
	label += '<td>' + inputCreator('number', id, value, null, 4, 'numeric') + '</td></tr>';
	return label;
};
var createNumberSpecificProperty = function createNumberSpecificProperty(subtype, id, value, title) {
	var _size = 4;
	if(subtype == 'decimal' || subtype == 'money')
		_size = 10;
	var label = '<tr><td>' + tagCreatorHTML('label', title) + '</td>';
	
	label += '<td>' + inputNumberCreator('number', subtype, id, value, null, _size, 'numeric') + '</td></tr>';
	return label;
};

//general method to create date component
var createDateProperty = function(id, value, title) {
	var label = '<tr><td>' + tagCreatorHTML('label', title) + '</td>';
	label += '<td>' + inputCreator('date', id, value, null, null, 'datepicker') + '</td></tr>';
	return label;
};

var createComboBoxProperty = function createComboBoxProperty(id, options_titles, options_values, selected, title, multiple){
	var i;
	var html = '<tr><td>'+ tagCreatorHTML('label', title) + '</td>';	
		html += '<td>';
			if (multiple) {
				html += selectCreator(id, id, true);
			} else {
				html += selectCreator(id, id, false);
			}
				for(i = 0; i < options_titles.length; i++){
					html += '<option value='+options_values[i]+' ';
					if(options_values[i] == selected){
						html += attribCreator('selected', selected);
					}
					html += '>';
					html += options_titles[i];
					html += '</option>';
				}				
			html += '</select>';
		html += '</td>';
	
	return html;
}; 

var createRadioBoxProperty = function createRadioBoxProperty(id, options_titles, options_values, selected, title){
	var html = '<tr><td>'+ tagCreatorHTML('label', title) + '</td>';
	var i;
	html += '<td>';
	html += '<ul>';
	for(i = 0; i < options_values.length; i++){
		var _title = options_titles[i];
		var _value = options_values[i];
		html += '<li>';
		if(_value == selected){
			html += reportInputCreator('radio', id, _value, id, null, null, true);
		}
		else {
			html += reportInputCreator('radio', id, _value, id, null, null, null);
		}
		html += tagCreatorHTML('label', _title, 'simple_label', _value);
		html += '</li>';
	}
	html += '</ul>';
	html += '</td></tr>';
	return html;
};

var createCheckBoxProperty = function createCheckBoxProperty(id, options_titles, options_values, selected, title){
	var html = '<tr><td>'+ tagCreatorHTML('label', title) +'</td>' ;
	var i;
	html += '<td>';
	html += '<ul>';
	for(i = 0; i < options_values.length; i++){
		var _title	= options_titles[i];
		var _value	= options_values[i];
		var _pos	= selected.indexOf(options_values[i]);
		html += '<li>';
		if(_pos != -1){
			html += reportInputCreator('checkbox', id, _value, id, null, null, true);
		}
		else {
			html += reportInputCreator('checkbox', id, _value, id, null, null, null);
		}
		html += tagCreatorHTML('label', _title, 'simple_label', _value);
		html += '</li>';
	}
	html += '</ul>';
	html += '</td></tr>';
	return html;
}; 

var createRequiredProperty = function(value){
	var required = '<tr><td>' + tagCreatorHTML('label', jQuery.i18n.prop('msg_field_requiredProperty')) + '</td>';
	
	required += '<td>';
	required += inputCreator('checkbox', 'fieldRequired', (value == true) ? true : false, 'fieldRequired',   
			null, null);
	required +=  '</td></tr>';
	
	return required;
};

function replaceXMLEntityByCode(string) {
	string = string.replace(/&/g ,'&amp;');
	string = string.replace(/'/g,'&apos;');
	string = string.replace(/"/g ,'&quot;');	
	string = string.replace(/</g ,'&lt;');
	string = string.replace(/>/g ,'&gt;');
	return string; 
}

function validDigits(e){
	var _key = e.which || e.keyCode;
    var _pattern = /\d/;
    var _keyed = String.fromCharCode(_key);
    return !lockKeys(_keyed, true) && 
    	((_pattern.test(_keyed) || _key == 9 || _key == 8 || _key == 0 || _key == 37 || _key == 39 || _key == 46 || _key == 35 || _key == 36));
}

function validDecimals(e){
	var _key = e.which || e.keyCode;
    var _pattern = /^[0-9,\.]*$/;
    var _keyed = String.fromCharCode(_key);
    return !lockKeys(_keyed, false) && 
    	((_pattern.test(_keyed) || _key == 9 || _key == 8 || _key == 0 || _key == 37 || _key == 39 || _key == 46 || _key == 35 || _key == 36));
}

function lockKeys(value, decimal){
    if(value.toString() == '$')
    	return true;
    else if(value.toString() == '%')
    	return true;
    else if(decimal && (value.toString() == '.'))
    	return true;
    else if(value.toString() == '\'')
    	return true;
    else if(value.toString() == '#')
    	return true;
    return false;
}