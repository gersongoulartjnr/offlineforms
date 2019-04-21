var ReportFormClass = function ReportFormClass(){
	this.title		= '';
	this.elements	= [];
	this.container	= 'fs-rc-form';
	
	this.renderForm = function renderForm(){
		var html = '<legend id="lgd_form_report">'+'Form'+'</legend>';
		html += '<ol id="ol_form_report">';
		for(var i in this.elements){			
			html += '<li id="f_'+i+'">';
			html += this.elements[i].toHTMLForm(i);
			html += '</li>';
		}
		html += '</ol>';
		$('#'+this.container).html(html);
	};
};