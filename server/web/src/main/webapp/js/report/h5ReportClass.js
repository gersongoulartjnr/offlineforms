var ReportClass = function ReportClass(){
	this.title		= '';//
	this.elements	= [];
	this.charts		= [];
	this.container	= 'fs-rc-report';
	this.v_container= 'r-v-items';
	this.prop_container	= 'fs-rc-properties';
	this.form_url	= null;
	
	this.txt_form		= 'form';
	this.txt_report		= 'report';
	this.txt_title		= 'title';
	this.txt_items		= 'items';
	
	this.renderReport = function renderReport(){
		this.form_url = $('#form_url').val();
		if(this.form_url == null || this.form_url == '')
			return;
		var html = '<legend id="lgd_report">'+'Report Editor'+'</legend>';
		html += '<ol id="ol_report">';
		for(var i in this.elements){			
			html += '<li id="r_'+i+'">';
			html += this.elements[i].toHTMLReport(i);
			html += '</li>';
		}
		html += '</ol>';		
		$('#'+this.container).html(html);
	};
	
	this.validateReport = function validateReport(){
		for (var i in this.elements) {
			if(!this.elements[i].verifyElement(i))
				return false;
		}
		return true;
	};
	
	this.toXML = function toXML(){
		var report_title = $('#report_title').val(); 
		var xml = '<?xml version="1.0" encoding="UTF-8"?>';
		xml += '<'+this.txt_report+'>';
			xml += '<'+this.txt_form+'>'+this.form_url+'</'+this.txt_form+'>';
			xml += '<'+this.txt_title+'>'+report_title+'</'+this.txt_title+'>';
			xml += '<'+this.txt_items+'>';
				for(var i in this.elements){
					xml += this.elements[i].toXMLReport(i);
				}
			xml += '</'+this.txt_items+'>';
		xml += '</'+this.txt_report+'>';
		return xml;
	};
	
	this.toJSON = function toJSON(){};
	
	this.renderViewer = function renderViewer(){
		var html = '';
		html += '<ol id="ol_viewer">';
		for(var i in this.elements){
			html += '<li id="v_'+i+'">';
			html += this.elements[i].toHTMReportViewer();
			html += '</li>';
		}
		html += '</ol>';
		$('#'+this.v_container).html(html);
		//[chart_type, div_id, chart_data[]]
		for(var i in this.charts){
			drawChart(this.charts[i][0], this.charts[i][1], this.charts[i][2]);
		}
		$('#downloadReport').removeClass('hidden');
	};
	
	this.dataToDownload = function dataToDownload(){
		var t = this.elements.length;
		if(t == 0)
			return '';
		var total_elems = t - 1;
		var report_title = $('#r-v-title').text();
		var data = '{';
				data += '"'+this.txt_title+'":"'+report_title+'"';
				data += ', "lstReport":[';
				for(var i in this.elements){
					data += this.elements[i].dataToDownload();
					if(i == total_elems) 
						data += '';
					else
						data += ',';
				}
				data += ']';
			data += '}';
		return data;
	};
};