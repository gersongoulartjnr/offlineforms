function loadFormFields(){
	var form_xml = $("#form_xml").val();
	//console.log('common: ' + form_xml);
	if(form_xml != undefined && form_xml != '') {
		try{
			loadQuestionsFromXML(form_xml);
		} catch(err) {
			addMessage(jQuery.i18n.prop('error_analyst_load_questions'), 'error');
		}
	}
}
function loadQuestionsFromXML(xml){
	try{
		var form_xml = $.parseXML(xml);		
	} catch(err){
		addMessage(jQuery.i18n.prop('msg_error_xmlprocess'), 'error');
		return;
	}
	var $xml = $( form_xml );
    var size = $xml.find('questions').size();    
    try{
    	if(size == 0){
    		throw "no questions";
    	}
    	$xml.find('questions').each(function(){
	    	var elems = this.childNodes;
	    	for(var i = 0; i<elems.length; i++){
	    		var field = analyticsFieldFactoryMethod(elems[i].nodeName);
	    		if(field != null){
	    			field.setDataFromXmlDoc(elems[i]);
	    			A_FORM_QUESTIONS.push(field);
	    			
	    			if(elems[i].nodeName == TXT_GEOLOCATION){
	    				FIELD_GEOLOCATION = A_FORM_QUESTIONS[i].id;
	    				HAS_GEOLOCATION = true;
	    			}
	    		}
	    	}
    	});
    } catch(err){
    	addMessage(jQuery.i18n.prop('msg_error_noquestions'), 'warn');
    }	
}
/******************************************/
function setToday(){
	var currentDate = new Date();
	var day = currentDate.getDate();
		if(day < 10) { day = '0'+day; }
	var month = currentDate.getMonth() + 1;
		if(month < 10) { month = '0'+month; }
	var year = currentDate.getFullYear();	
	return year+'-'+month+'-'+day;
}
/**
 * Compare the weights, so after that the array can be sorted by its weights
 * @param weight1
 * @param weight2
 * @returns {Number}
 */
function compareByWeight(weight1, weight2){
	if (weight1.weight_method < weight2.weight_method)
		return 1;
	if (weight1.weight_method > weight2.weight_method)
		return -1;
	return 0;
}

/* Questions */
var analyticsFieldFactoryMethod = function analyticsFieldFactoryMethod(type){	
	var field = new AnalyticsFormFieldClass(type);	
	return field;
};

var AnalyticsFormFieldClass = function AnalyticsFormFieldClass(type){
	this.id		= '';
	this.type	= type;
	this.title	= '';
	this.options = [];
	
	this.txt_id		= 'id';
	this.txt_label	= 'label';
	
	this.setDataFromXmlDoc = function setDataFromXmlDoc(xmlDoc){		
		var $xmlDoc = $(xmlDoc);
		if(xmlDoc.nodeName != this.type){
			return;
		}
		this.id		= $xmlDoc.attr(this.txt_id);
		this.title	= $xmlDoc.find(this.txt_label).text();
		if(this.type == 'combobox' || this.type == 'checkbox' || this.type == 'radio'){
			var options_parsed = [];		
			var options_found  = $xmlDoc.find('option');
			for(var i=0; i<options_found.length; i++){
				var _key = null;
				for(var a in options_found[i].attributes){
					if(options_found[i].attributes[a].nodeName == 'value'){
						_key = options_found[i].attributes[a].value;						
					}
				}
				options_parsed.push({'key': _key, 'value': options_found[i].textContent});
			}
			this.options = options_parsed;
		}
	}; 
};

/* Transformations */
var AnalyticsTransformationInfo = function AnalyticsTransformationInfo(question_type, transformation){
	this.question_type	= question_type;
	this.transformation	= transformation;
};
var addTransformationInfo = function addTransformationInfo(question_type, transformation){
	var flag = false;
	for(var i in A_TRANSFORMATIONS){
		//Only by type?
		if(A_TRANSFORMATIONS[i].question_type == question_type){
			flag = true;
			continue;
		}
	}
	if(flag == false){
		A_TRANSFORMATIONS.push(new AnalyticsTransformationInfo(question_type, transformation));
		var element = ANALYTICS_CONTAINER.elements[A_ID_ELEMENT];
		if(element.item_view != null) {
			element.item_view.renderView();
		}
	}	
}; 

/* Views components*/
var ViewComponent = function ViewComponent(){
	this.type = null;
	
	this.drawComponent = function drawComponent(){}; 
};

/* Basic */
var ViewBasicComponent = function ViewBasicComponent(){
	this.type = null;
	
	this.drawComponent = function drawComponent(){}; 
};
ViewBasicComponent.prototype = new ViewComponent();

/* Box */
var ViewBoxComponent = function ViewBasicComponent(){
	this.type = null;
	
	this.drawComponent = function drawComponent(){}; 
};
ViewBoxComponent.prototype = new ViewComponent();

/* Image */
var ViewImageComponent = function ViewBasicComponent(){
	this.type = null;
	
	this.drawComponent = function drawComponent(){}; 
};
ViewImageComponent.prototype = new ViewComponent();

/* Audio */
var ViewAudioComponent = function ViewBasicComponent(){
	this.type = null;
	
	this.drawComponent = function drawComponent(){}; 
};
ViewAudioComponent.prototype = new ViewComponent();

/* Video */
var ViewVideoComponent = function ViewBasicComponent(){
	this.type = null;
	
	this.drawComponent = function drawComponent(){}; 
};
ViewVideoComponent.prototype = new ViewComponent();

/* Subtype */
var ViewSubtypeComponent = function ViewBasicComponent(){
	this.type = null;
	
	this.drawComponent = function drawComponent(){}; 
};
ViewSubtypeComponent.prototype = new ViewComponent();

/*** ***/

var itemId = -1;
var generateUUID = function generateUUID(){
	itemId++;
	if(itemId >= 1000000){
		itemId = -1;
	}
	return itemId;
};

var getRowAndTextForTable = function getRowAndTextForTable(title, cssClass, pre_space){
	var css_class = '';
	if(cssClass != null){
		css_class = getHtmlAttrib('class', cssClass);
	}
	var br_before_sth = '';
	if(pre_space){
		br_before_sth = '<br />';
	}
	return '<tr><td>'+br_before_sth+'<span '+css_class+'>'+title+'</span></td></tr>'; 
};
var getHtmlLabelTag = function getHtmlLabelTag(title, cssClass){
	var css_class = '';
	if(cssClass != null){
		css_class = getHtmlAttrib('class', cssClass);
	}
	return '<span '+css_class+'>'+title+'</span>'; 
}; 

var openXmlTag = function openXmlTag(xml_tag){ return '<'+xml_tag+'>'; };
var closeXmlTag = function closeXmlTag(xml_tag){ return '</'+xml_tag+'>'; };

var getHtmlTag = function getHtmlTag(tag, value, cssClass){
	var css_class = '';
	if(css_class != null){
		css_class = getHtmlAttrib('class', cssClass);
	}	
	return '<'+tag+' '+css_class+'>'+ value +'</'+tag+'>';
};

var getHtmlAttrib = function getHtmlAttrib(field, value){
	return field + '="' + value + '" ';
};

var getHtmlInput = function getHtmlInput(method, type, id, value, name, size, cssClass, readonly, selected){
	var html = '<input ';
		html += getHtmlAttrib('type', type);
		if(id != null){
			html += getHtmlAttrib('id', id);
		}
		if(method != null){
			html += getHtmlAttrib('onchange', method);
		}
		if(value != null){
			html += getHtmlAttrib('value', value);
		}
		if(size != null){
			html += getHtmlAttrib('size', size);
		}
		if(cssClass != null){
			html += getHtmlAttrib('class', cssClass);
		}
		if(readonly == true){
			html += getHtmlAttrib('readonly', 'readonly');
		}
		
		switch(type){
			case 'checkbox':
			case 'radio':
				html += getHtmlAttrib('name', name);
				if(selected == true){
					html += getHtmlAttrib('checked', selected);
				}
				break;
			default:
				break;
		}
		html += ' />';
	
	return html;
};

var openSelectTag = function openSelectTag(method, id, name, cssClass){
	var html = '<select ';
		html += getHtmlAttrib('id', id);
		html += getHtmlAttrib('name', name);
		if(method != undefined && method != null){
			html += getHtmlAttrib('onchange', method);
		}
		if(cssClass != undefined && cssClass != null){
			html += getHtmlAttrib('class', cssClass);
		}
	html += '>';	
	return html;
};

var getHtmlButton = function getHtmlButton(id, title, do_method, cssClass){
	var do_action = '';
	if(do_method != undefined && do_method != null){
		do_action = 'onclick="'+do_method+'"';
		do_action = getHtmlAttrib('onclick', do_method);
	}
	var css_class = '';
	if(cssClass != undefined && cssClass != null){
		css_class = getHtmlAttrib('class', cssClass);
	}
	
	var html = '<button id="'+id+'" type="button" '+ css_class +' '+do_action+'>';
		html += title;
	html += '</button>';
	return html;
}; 

var fillComboBox = function fillComboBox(method, id, value_options, title_options, selected, cssClass){
	var html = openSelectTag(method, id, id, cssClass);
		var i, 
			options_size;
		for(i = 0, options_size = value_options.length; i < options_size; i++){
			html += '<option value='+value_options[i]+' ';
			if(value_options[i] == selected){
				html += getHtmlAttrib('selected', selected);
			}
			html += '>';
			html += title_options[i];
			html += '</option>';
		}
	html += '</select>';
	return html;
};

var fillCheckBox = function fillCheckBox(method, id, value_options, title_options, selected){
	var i,
		options_size;
	var html = '<ul>';
		for(i = 0, options_size = value_options.length; i < options_size; i++){
			html += '<li>';
			var _value = value_options[i];
			var _title = title_options[i];			
			if(selected == undefined || selected == null){				
				html += getHtmlInput(method, 'checkbox', id, _value, id, null, null, null, true);				
			} else {
				var pos = selected.indexOf(value_options[i]);
				if(pos != -1){					
					html += getHtmlInput(method, 'checkbox', id, _value, id, null, null, null, true);
											
				} else {
					html += getHtmlInput(method, 'checkbox', id, _value, id, null, null, null, false);						
				}
			}
			html += getHtmlTag('label', _title, 'normal');
			html += '</li>';
		}
	html += '</ul>';	
	return html;
};

var fillRadioBox = function fillRadioBox(method, id, value_options, title_options, selected){
	var i,
		options_size;
	var html = '<ul>';
		for(i = 0, options_size = value_options.length; i < options_size; i++){
			html += '<li>';
			var _value = value_options[i];
			var _title = title_options[i];			
			if(selected == undefined || selected == null){				
				html += getHtmlInput(method, 'radio', id, _value, id, null, null, null, false);				
			} else {
				if(selected == value_options[i]){
					html += getHtmlInput(method, 'radio', id, _value, id, null, null, null, true);
											
				} else {
					html += getHtmlInput(method, 'radio', id, _value, id, null, null, null, false);						
				}
			}
			html += getHtmlTag('label', _title, 'normal');
			html += '</li>';
		}
	html += '</ul>';	
	return html;
};