var FormClass = function() {
	this.title = '';
	this.description = '';
	this.url = '';
	this.container = '';
	this.elements = new Array();
	this.dropBoxId = 'dropBoxId';
	
	this.renderForm = function(deleteAlert) {
		var html = '<legend id="lgd_title">' + this.title + '</legend>';
			html += '<ol id="olXmlForm">';
		for(var i in this.elements){
			html += '<li id="' + i + '">';
			html += this.elements[i].toHTML(i);
			html += '</li>';
		}
		html += '</ol>';
		
		html += this.createDropBox();		
		$('#' + this.container).html(html);
		this.hideDropBox();
		
		initForm();
		
		if(WARNING_H5_FORM_EDITOR != undefined && deleteAlert != true) {
			WARNING_H5_FORM_EDITOR = true;
		}
	};
	
	this.toXML = function() {
		var xml = '<?xml version="1.0" encoding="UTF-8"?>';
			xml += '<form>';
			xml += '<title>' + this.title + '</title>';
			xml += '<questions>';
			for(var i in this.elements) {
				this.elements[i].id		= i;
				// add the next_id value
				if(i < (this.elements.length-1))
					this.elements[i].next	= parseInt(i)+1;
				else 
					this.elements[i].next	= -1;
				
				xml += this.elements[i].toXML(i);
			}
		xml += '</questions>';
		xml += '</form>';
		return xml;
	};
	
	this.toJSON = function() {
		return '{"title":"' + this.title + 
				'","description":"' + this.description + 
				'","url":"' + this.url +
				'","container":"' + this.container +	
				'","elements":' + JSON.stringify(this.elements) + '}';
	};
	
	this.fromJSON = function(json) {
		var jsonObject = JSON.parse(json);
		this.title = jsonObject.title;
		this.container = jsonObject.container;
		for(var i in jsonObject.elements) {
			var elem = jsonObject.elements[i];
			var element = fieldFactory(elem.type);
			if (element == null) {
				return true;
			}
			element.setJSONValues(elem);
			
			this.elements.push(element);
		}
	};
	
	this.validateForm = function() {
		for (var i in this.elements) {
			var element = this.elements[i];
			switch (element.type) {
				// validate element that require verification before save form
				case 'combobox':
				case 'radio':
				case 'checkbox':
					if (!this.elements[i].verifyElement())
						return false;
			}
		}
		return true;
	};
	
	this.createDropBox = function() {
		return '<div id="'+this.dropBoxId+'" class="infoDropBox">'+jQuery.i18n.prop('msg_form_drop_here')+'</div><br /><br /><br /><br /><br />';
	};
	
	this.hideDropBox = function() {
		$('#'+this.dropBoxId).hide();
	};
	
	this.showDropBox = function (message) {
		$('#'+this.dropBoxId).show().animate({opacity: 1.0}, 1300).fadeOut(2000);
	};
};