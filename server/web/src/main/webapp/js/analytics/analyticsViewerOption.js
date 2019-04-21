var AnalyticsViewerOptionClass = function AnalyticsViewerOptionClass(){
	this.id		= null;
	this.type	= null;
	this.fields	= [];
	
	this.drawView = function drawView(json_doc){
		var secTransf = this.lastTransformation();
		var html = '';
		if(secTransf != null){
			html = '<div class="a-grid">';
				html += '<span class="bold">'+secTransf+': </span>';
				html += json_doc[0].result;
			html += '</div>';
			$('#a-v-items').html(html);
		} else {
			this.drawItemView(json_doc);
		}		
	};
	
	this.lastTransformation = function lastTransformation(){
		var aDoc = $('#a_doc').val();
		var json_doc = JSON.parse(aDoc);
		if(json_doc.elements[this.id].sectransformations.length > 1){
			addMessage(jQuery.i18n.prop('error_analyst_sec_transformations'), 'error');
			return null;
		} else if(json_doc.elements[this.id].sectransformations.length == 1){
			return json_doc.elements[this.id].sectransformations[0].type;
		} else{
			return null;
		}
	};
};

/* Map */
var MapViewerOption = function MapViewerOption(){

	this.drawItemView = function drawItemView(json_doc){
		var position = getPositionFromDoc(this.id);
		var geolocation_id = null;
		var pos_data = []; 
		var author = json_doc[0].author;
		var creationDate = json_doc[0].creationDate.$date;
		
		if(position.length == 2){
			for(var v in A_FORM_QUESTIONS){
				if(A_FORM_QUESTIONS[v].type == 'geolocation'){
					geolocation_id = A_FORM_QUESTIONS[v].id;
					continue;
				}
			}
			if(geolocation_id == null)
				return;
			
				
			for(var a in json_doc){
				var pos = json_doc[a][geolocation_id];
				var data_by_row = '';
				for(var d in this.fields){
					var title = '<span>'+A_FORM_QUESTIONS[this.fields[d]].title+':</span> ';
					switch(A_FORM_QUESTIONS[this.fields[d]].type){
						case 'text':
						case 'number':
						case 'decimal':
						case 'slider':
						case 'money':
							var answer_val = json_doc[a][d]; 
							if(answer_val != null){
								data_by_row += '<span>'+title+'</span>'+json_doc[a][d]+'<br />';
							}
							break;
						case 'date':
							var answer_val = json_doc[a][d]; 
							if(answer_val != null){
								data_by_row += '<span>'+title+'</span>'+parseDateComponent(json_doc[a][d].$date)+'<br />';
							}
							break;
						case 'geolocation':
							//				
		            		break;
						case 'checkbox':
		            	case 'radio':
		            	case 'combobox':
		            		var answer_val = json_doc[a][d];
		            		if (answer_val.match('^{') && answer_val.match('}$')) {
		            			data_by_row += '<span>'+title+'</span><br />'+parseBoxComponentForMap(answer_val);
		            		} else {
		            			data_by_row += '<span>'+title+'</span>'+parseBoxComponentForMapFromMongo(answer_val, this.fields[d])+'<br />';
		            		}
		            		break;
		            	case 'picture':
		            	case 'draw':
		            		var answer_val = json_doc[a][d];
		            		if(answer_val != null && answer_val != '') {
		            			data_by_row += '<span>'+title+'</span><br /><img onclick="aShowPicture(\''+json_doc[a][d][0]+'\')" src="data:image/png;base64,'+json_doc[a][d][1]+'" /><br />';
		            		}
		            		break;				            		
		            	case 'audio':
		            		var answer_val = json_doc[a][d];
		            		if(answer_val != null && answer_val != '') {
		            			data_by_row += '<span>'+title+'</span><br /><img width="20em" onclick="aShowMultimedia(\'audio\', \''+answer_val+'\')" src="images/form/audio_icon.png" /><br />';
		            		}
		            		break;
		            	case 'video':
		            		var answer_val = json_doc[a][d];
		            		if(answer_val != null && answer_val != '') {
		            			data_by_row += '<span>'+title+'</span><br /><img onclick="aShowMultimedia(\'video\', \''+answer_val[0]+'\')"src="data:image/png;base64,'+answer_val[1]+'" /><br />';
		            		}
		            		break;
		            	case 'barcode':
		            		data_by_row += '<span>'+title+'</span>'+parseBarcode(json_doc[a][d])+'<br />';
		            		break;
						default:
							//
					}
				}

				var _geolocation = json_doc[a][geolocation_id];
				if(_geolocation != null){
					pos_data.push([data_by_row, json_doc[a][geolocation_id][0], json_doc[a][geolocation_id][1]]);
				}
			}
			$('#a-v-items').width(800);
			$('#a-v-items').height(400);
			drawMap('a-v-items', position, pos_data);
		} else{
			return;
		}	
	};
};
MapViewerOption.prototype = new AnalyticsViewerOptionClass();

/* Grid */
var GridViewerOption = function GridViewerOption(){

	this.drawItemView = function drawItemView(json_doc){
		var author = json_doc[0].author;
		var creationDate = json_doc[0].creationDate.$date;
		
		html = '<div class="a-grid"><table class="grid">';
		html += '<thead><tr>';
			if(author != undefined){
				html += '<th>Author</th>';
			}
			if(creationDate != undefined){
				html += '<th>Creation Date</th>';
			}
			for(var t in this.fields){
				html += '<th width="5%">';
					html += A_FORM_QUESTIONS[this.fields[t]].title;
				html += '</th>';
			}
			html += '</tr/></thead>';
			html += '<tbody>';
				for(var a in json_doc){
					html += '<tr>';
					if(author != undefined){
						html += '<td>'+json_doc[a].author+'</td>';
					}
					if(creationDate != undefined){
						html += '<td>'+parseCreationDate(json_doc[a].creationDate.$date)+'</td>';
					}
					
					for(var d in this.fields){
						html += '<td>';
							switch(A_FORM_QUESTIONS[this.fields[d]].type){
								case 'text':
								case 'number':
								case 'decimal':
								case 'slider':
								case 'money':								
									if(json_doc[a][d] != null){
										html += json_doc[a][d];
									} else {
										html += '';
									}
									break;
								case 'date':
									var answer_val = json_doc[a][d]; 
									if(answer_val != null){
										html += parseDateComponent(json_doc[a][d].$date);
									} else {
										html += '';
									}									
									break;									
								case 'geolocation':
									html += parseGeolocation(json_doc[a][d]);
				            		break;
								case 'checkbox':
				            	case 'radio':
				            	case 'combobox':
				            		var answer_val = json_doc[a][d];
				            		if (answer_val.match('^{') && answer_val.match('}$')) {
				            			html += parseBoxComponent(answer_val);            			
				            		} else {
				            			html += parseBoxComponentFromMongo(answer_val, this.fields[d]);
				            		}
				            		break;
				            	case 'picture':
				            	case 'draw':
				            		var answer_val = json_doc[a][d];
				            		if(answer_val != null && answer_val != '') {
				            			html += '<div align="center"><img onclick="aShowPicture(\''+answer_val[0]+'\')" src="data:image/png;base64,'+answer_val[1]+'" /></div>';
				            		} else {
				            			html += '<div align="center"><img width="90px" src="images/form/not_available.jpeg"/></div>';
				            		}
				            		break;				            		
				            	case 'audio':
				            		var answer_val = json_doc[a][d];
				            		if (answer_val != null && answer_val != '') {
				            			html += '<div align="center"><img width="20em" onclick="aShowMultimedia(\'audio\', \''+answer_val+'\')" src="images/form/audio_icon.png" /></div>';
				            		} else {
				            			html += '<div align="center"><img width="20em" onclick="aShowMultimedia(\'audio\', \''+answer_val+'\')" src="images/form/audio-muted_icon.png"/></div>';
				            		}
				            		break;
				            	case 'video':
				            		var answer_val = json_doc[a][d];
				            		if(answer_val != null && answer_val != '') {
				            			html += '<div align="center"><img onclick="aShowMultimedia(\'video\', \''+answer_val[0]+'\')"src="data:image/png;base64,'+answer_val[1]+'" /></div>';
				            		} else {
				            			html += '<div align="center"><img width="90px" src="images/form/not_available.jpeg"/></div>';
				            		}
				            		break;
				            	case 'barcode':
				            		html += parseBarcode(json_doc[a][d]);
				            		break;
								default:
									html += json_doc[a][d];
							}
						html += '</td>';
					}
					html += '</tr>';
				}
				html += '</tbody>';
				
			html += '</table></div>';
		$('#a-v-items').html(html);
	};
};
GridViewerOption.prototype = new AnalyticsViewerOptionClass();

/* Barchart */
var BarchartViewerOption = function BarchartViewerOption(){

	this.drawView = function drawView(){
	};
};
BarchartViewerOption.prototype = new AnalyticsViewerOptionClass();

/* Pie */
var PieViewerOption = function PieViewerOption(){

	this.drawView = function drawView(){
	};
};
PieViewerOption.prototype = new AnalyticsViewerOptionClass();

/* Slider */
var SliderViewerOption = function SliderViewerOption(){

	this.drawView = function drawView(){
	};
};
SliderViewerOption.prototype = new AnalyticsViewerOptionClass();

/**/
var aViewerOptionFactory = function aViewerOptionFactory(id, type){
	var viewer_option;
	switch(type){
		case V_GRID.name:
			viewer_option = new GridViewerOption();			
			break;
		case V_BARCHART.name:
			viewer_option = new BarchartViewerOption();
			break;
		case V_PIE.name:
			viewer_option = new PieViewerOption();
			break;
		case V_SLIDER.name:
			viewer_option = new SliderViewerOption();
			break;
		case V_MAP.name:
			viewer_option = new MapViewerOption();
			break;
		default:
			return null;
	}
	viewer_option.id = id;
	viewer_option.type = type;
	
	return viewer_option;	
};
/**/
var getDataForGrid = function getDataForGrid(){
	
}; 