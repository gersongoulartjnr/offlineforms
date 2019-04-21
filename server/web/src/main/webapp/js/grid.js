function searchBy(e, div_id, orderBy, orderType, page){
	if(e.which == 13) {
		gridGoTo(div_id, orderBy, orderType, page);
	}
}
function firstPage(div_id, orderBy, orderType, page){
    if(page > 0 && page != 1){
    	var _goToPage = '1';
    	gridGoTo(div_id, orderBy, orderType, _goToPage);
    }
};
function backPage(div_id, orderBy, orderType, page){
    var _goToPage = parseInt(page) - 1;
    if(page > 0 && page != 1 && _goToPage > 0){
    	gridGoTo(div_id, orderBy, orderType, _goToPage);
    }
};
function goToPage(div_id, orderBy, orderType, page, num_rows, num_pages, event){
    var goto_page = $('#goto_page').val();
    if(page != goto_page > 0 && goto_page > 0 && page <= num_pages){
    	//console.log('goToPage > page: ' + page + ' - num_rows: ' + num_rows + ' num_pages: ' + num_pages);
    }
};
function nextPage(div_id, orderBy, orderType, page, num_pages){		
    if(page > 0 && page < num_pages){
    	var _goToPage = parseInt(page) + 1;
    	gridGoTo(div_id, orderBy, orderType, _goToPage);
    }
};
function lastPage(div_id, orderBy, orderType, page, num_pages){
	if(page > 0 && page < num_pages){
		var _goToPage = num_pages;
		gridGoTo(div_id, orderBy, orderType, _goToPage);
    }
};
function gridAscOrder(section, orderBy, page) {
	loadGrid(section, orderBy, M_ASC, page);
}
function gridDescOrder(section, orderBy, page) {
	loadGrid(section, orderBy, M_DESC, page);
}
function gridGoTo(section, orderBy, orderType, page){
	loadGrid(section, orderBy, orderType, page);
}
function loadGrid(section, orderBy, orderType, page, search_words){
	//My Forms
	if(parseInt(section) == G_MY_FORMS){
		var _search_words = $('#searchMyForms').val();
		if(_search_words == undefined || _search_words == null || _search_words == ''){
			myForms(orderBy, orderType, page);
		} else {
			myForms(orderBy, orderType, page, _search_words);
		}		
	}
	//Shared Forms
	else if(parseInt(section) == G_SHARED_FORMS){
		var _search_words = $('#searchSharedForms').val();
		if(_search_words == undefined || _search_words == null || _search_words == ''){
			sharedForms(orderBy, orderType, page);
		} else {
			sharedForms(orderBy, orderType, page, _search_words);
		}		
	}
	else if(parseInt(section) == G_MY_GROUPS){
		myGroups(orderBy, orderType, page);
	}
	else if(parseInt(section) == G_ANSWERS_FORM){
		answersByForm(orderBy, orderType, page);
	}
}
/**/
var MGrid = function MGrid() {
	this.even_color	= 'even';
	this.odd_color	= 'odd';
	this.bg_color	= '#F0F0F0';
	this.div_id		= '';
	
	/*Search*/
	this.div_search_my_forms		= 'search_my_forms';
	this.div_search_shared_forms	= 'search_shared_forms';
	this.input_search_my_forms		= 'searchMyForms';
	this.input_search_shared_forms	= 'searchSharedForms';
	this.search_words;
	
	this.grid_names		= ['myForms', 'sharedForms', 'myGroups', 'answersByForm'];
	this.num_titles		= '';
	this.titles			= [];
	this.key_submenu	= '';
	this.columns_key	= [];
	this.columns_size	= [];
	this.columns_sort	= [];
	this.page;
	this.orderBy;
	this.orderType;
	this.current_rows;
	this.num_pages;
	this.num_rows;
	this.total_rows;
	this.data;	
	this.total_cols;
	
	this.answers_titles;

	this.paintGrid = function paintGrid(titles, columns_key, columns_size, columns_sort, data, div_id, key_with_submenu) {
		var html 			= '';		
		this.div_id			= div_id;
		this.key_submenu 	= key_with_submenu;
		this.titles			= titles;
		this.num_titles		= this.titles.length;
		this.columns_key	= columns_key;
		this.columns_size	= columns_size;
		this.columns_sort	= columns_sort;
		if(parseInt(div_id) == G_ANSWERS_FORM) {
			if(data != undefined && data != null){
				this.data		= data.wrapper.answers;
				this.answers_titles = data.wrapper.questions;
				this.orderBy		= data.orderBy;
				this.orderType		= data.orderType;
				this.page			= data.currentPage;
				this.current_rows 	= data.currentNumRows;
				this.num_pages		= data.numPages;
				this.num_rows		= data.numRows;
				this.total_rows		= data.total;
			}
			this.total_cols	= this.titles.length + this.answers_titles.length;
		}
		else {
			if(data != undefined && data != null){
				this.data		= data.rows;
				this.orderBy		= data.orderBy;
				this.orderType		= data.orderType;
				this.page			= data.currentPage;
				this.current_rows 	= data.currentNumRows;
				this.num_pages		= data.numPages;
				this.num_rows		= data.numRows;
				this.total_rows		= data.total;
				this.search_words	= data.txtSearch;
			}
			this.total_cols	= this.titles.length;
		}		
		
		if(this.data != undefined && this.data != null && this.data.length != 0){
			this.buildSearchBody();
			html += this.openGrid();
			html += this.buildHeader();
			html += this.buildBody();
			html += this.buildFooter();	
			html += this.closeGrid();			
		}
		else {
			html += this.openGrid();
			html += this.buildHeaderNoResults();	
			html += this.buildBodyNoResults();
			html += this.closeGrid();
		}
		$('#'+this.grid_names[div_id]).html(html);
	};
	
	this.openGrid = function openGrid(){
		var html = '<table class="grid">';
		return html;
	};
	this.closeGrid = function closeGrid(){
		var html = '</table>';
		return html;
	};
	this.buildHeaderNoResults = function buildHeaderNoResults(){
		var html = '<thead><tr>';
        var i,
            max_titles,
            max_sizes,
            width = 5;
        for(i = 0, max_titles = this.titles.length, max_sizes = this.columns_size.length; i < max_titles; i++) {
            if(max_titles == max_sizes) {
                width = this.columns_size[i];
            }            
            html += '<th width="'+width+'%">'+this.titles[i]+'</th>';
        }
        html += '</tr></thead>';
        return html;
	};
	this.buildBodyNoResults = function buildBodyNoResults(){		
		var html = '<tbody>';
			html += '<tr>';			
			if(this.key_submenu != null && this.key_submenu != ''){
	        	var total_cols = parseInt(this.total_cols) + 1;
	        	html += '<td colspan="'+total_cols+'">'+MSG_NO_RESULTS+'</td>';
	        }
	        else{
	        	html += '<td colspan="'+this.total_cols+'">'+MSG_NO_RESULTS+'</td>';
	        }
			html += '</tr>';
		html += '</tbody>';
		return html;
	};
	this.buildSearchBody = function buildSearchBoyd(){
		return '';
	};
	this.buildHeader = function buildHeader(){
		var html = '';
        html += '<thead><tr>';
        var i,
            max_titles,
            max_sizes,
            width = 5;
        for(i = 0, max_titles = this.titles.length, max_sizes = this.columns_size.length; i < max_titles; i++) {
            if(max_titles == max_sizes) {
                width = this.columns_size[i];
            }
            var th_class = CSS_SORTABLE;
            if(this.orderBy == this.columns_sort[i]) {
            	if(this.orderType == M_ASC) { th_class = CSS_ASC;}
            	if(this.orderType == M_DESC){ th_class = CSS_DESC;}
            }
            if(this.columns_sort[i] != undefined && this.columns_sort[i] != ''){
            	html += '<th id="title_'+i+'" width="'+width+'%" class="'+th_class+'"';
            	//
            	if(this.columns_key[i] == this.key_submenu){
            		if(this.div_id == G_MY_FORMS || this.div_id == G_SHARED_FORMS){
            			html += ' colspan="3">';
            		} else {
            			html += ' colspan="2">';
            		}
            	} else{
            		html += '>';
            	}

            	if(th_class == CSS_DESC){
                	html += '<a href="#" onclick="gridAscOrder('+"'"+this.div_id+"'"+', '+"'"+this.columns_key[i]+"'"+', '+"'"+this.page+"'"+')">'+this.titles[i]+'</a> ';
                } else{
                	html += '<a href="#" onclick="gridDescOrder('+"'"+this.div_id+"'"+', '+"'"+this.columns_key[i]+"'"+', '+"'"+this.page+"'"+')">'+this.titles[i]+'</a>';
               	}
                html += '</th>';
            } else{
            	html += '<th width="'+width+'%">'+this.titles[i]+'</th>';
            }
        }
        html += '</tr></thead>';
        return html;
	};
	this.buildBody = function buildBody(){        
        return '';
	};
	this.buildFooter = function buildFooter(){
        var html = '';
        if(this.total_rows > this.num_rows){
            html += '<tfoot>';
            html += '<tr>';
            if(this.key_submenu != null && this.key_submenu != ''){
            	if(this.div_id == G_MY_FORMS || this.div_id == G_SHARED_FORMS){
            		var total_cols = parseInt(this.total_cols) + 2;
            		html += '<td colspan="'+total_cols+'">';
            	} else{
            		var total_cols = parseInt(this.total_cols) + 1;
            		html += '<td colspan="'+total_cols+'">';            		
            	}
            }
            else{
            	html += '<td colspan="'+this.total_cols+'">';
            }
            
            html += '<div id="grid_foot">';
            html += '<div id="grid_foot_left"><span class="text_bold">'+MSG_PAGES+'</span> ' + this.num_pages+'</div>';

            html += '<div id="grid_foot_first" onclick="firstPage('+"'"+this.div_id+"'"+', '+"'"+this.orderBy+"'"+', '+"'"+this.orderType+"'"+', '+this.page+')"><< </div>';
			html += '<div id="grid_foot_previous" onclick="backPage('+"'"+this.div_id+"'"+', '+"'"+this.orderBy+"'"+', '+"'"+this.orderType+"'"+', '+this.page+')">< </div>';
			html += '<div id="grid_foot_goto">'+this.page+'</div>';
			html += '<div id="grid_foot_next" onclick="nextPage('+"'"+this.div_id+"'"+', '+"'"+this.orderBy+"'"+', '+"'"+this.orderType+"'"+', '+this.page+', '+this.num_pages+')"> > </div>';
			html += '<div id="grid_foot_last"onclick="lastPage('+"'"+this.div_id+"'"+', '+"'"+this.orderBy+"'"+', '+"'"+this.orderType+"'"+', '+this.page+', '+this.num_pages+')"> >> </div>';

            html += '<div id="grid_foot_right"> <span class="text_bold">'+MSG_SHOWING+'</span> ' + this.current_rows + ' <span class="text_bold">'+MSG_OF+'</span> ' + this.total_rows+'</div>';
            html += '</div>';
            html += '</td>';
            html += '</tr>';
            html += '</tfoot>';
        }
        return html;
    };    
};

var FormGrid = function FormGrid(){
	this.buildSearchBody = function buildSearchBoyd(){
		var search_div;
		var search_id;
		
		if(this.div_id == G_MY_FORMS) { 
			search_div = this.div_search_my_forms;
			search_id = this.input_search_my_forms;
		}
		else if(this.div_id == G_SHARED_FORMS) { 
			search_div = this.div_search_shared_forms;
			search_id = this.input_search_shared_forms;
		}
		
		var html_search = '';
		this.search_words = $('#'+search_id).val();
		if(this.search_words == undefined || this.search_words == null || this.search_words == ''){
			html_search = '';
		} else {
			html_search = 'value="'+this.search_words+'"';
		}
		
		var html = '';
			html = '<input type="text" id="'+search_id+'" '+html_search+' class="search_rounded" placeholder="Search..." onkeypress="searchBy(event, '+"'"+this.div_id+"'"+', '+"'"+this.orderBy+"'"+', '+"'"+this.orderType+"'"+', '+this.page+')" />';

		$('#'+search_div).html(html);
	};	
	
	this.buildBody = function buildBody() {
        var html	= '';
        var bgcolor	= '';
        var even	= this.even_color;
        var odd		= this.odd_color;        
        var i,
        	max;
        
    	html += '<tbody>';
        for (i = 0, max = this.data.length; i < max; i++){
    	    if(i % 2 == 0) bgcolor = even;
    	    else bgcolor = odd;
    	    var obj = this.data[i];
    	    var t;
    	    html += '<tr id="'+obj['url']+'" class="'+bgcolor+'">';
	    	for(t = 0; t < this.num_titles; t++) {
	    		if(this.columns_key[t] == this.key_submenu) {
	    			html += '<td><a href="form.html?id='+obj['url']+'" target="_blank">'+obj['title']+'</a></td>';
	    			html += '<td width="3%" align="center">';
	    				html += '<a href="#" onclick="showQRCode('+"'"+obj['title']+"'"+', '+"'"+obj['url']+"'"+')"><img width="12px" src="static/images/qrcode.png" /></a>';
	    			html += '</td>';
	    			html += '<td width="3%">';
	    				html += '<ul class="form_sub_menu"><li>';
	    				html += '<a href="#"><span class="arrow">&#9660;</span></a>';
		    			html += '<ul>';
		    			html += buildSubMenu(obj['url'], obj['formPermission'], obj['answerPermission'], obj['reportPermission'], obj['reports'], obj['analytics']);
		    			html += '</ul>';
		    			html += '</li>';
		    			html += '</ul>';
    				html += '</td>';
	    		}
	    		else {
	    			var _align = '';
	    			if(this.columns_key[t] == 'numberOfCollects'){ _align = 'align="center"'; }	    			
	    			html += '<td '+_align+'>'+obj[this.columns_key[t]]+'</td>';  	    		
	    		}
	    	}
    	    html += '</tr>';
        }
        html += '</tbody>';
        return html;
	};
};
FormGrid.prototype = new MGrid();

var AnswerGrid = function AnswerGrid(){
	this.buildHeader = function buildHeader(){
		var html = '';
        html += '<thead><tr>';
        var i,
            max_titles,
            t,
            answers_titles_size;
        for(i = 0, max_titles = this.titles.length; i < max_titles; i++) {
        	var th_class = CSS_SORTABLE;
            if(this.orderBy == this.columns_key[i]) {
            	if(this.orderType == M_ASC) { th_class = CSS_ASC;}
            	if(this.orderType == M_DESC){ th_class = CSS_DESC;}
            }
            if(this.columns_sort[i] != undefined && this.columns_sort[i] != ''){
            	html += '<th id="title_'+i+'" class="'+th_class+'">';
                if(th_class == CSS_DESC){
                	html += '<a href="#" onclick="gridAscOrder('+"'"+this.div_id+"'"+', '+"'"+this.columns_key[i]+"'"+', '+"'"+this.page+"'"+')">'+this.titles[i]+'</a> ';
                }
                else{
                	html += '<a href="#" onclick="gridDescOrder('+"'"+this.div_id+"'"+', '+"'"+this.columns_key[i]+"'"+', '+"'"+this.page+"'"+')">'+this.titles[i]+'</a>';
               	}
                html += '</th>';
            }
            else{
            	html += '<th>'+this.titles[i]+'</th>';
            }
        }        
        for(t = 0, answers_titles_size = this.answers_titles.length; t < answers_titles_size; t++){
        	html += '<th>'+this.answers_titles[t]+'</th>';
        }        
        html += '</tr></thead>';
        return html;
	};
	this.buildBody = function buildBody(){
		var html	= '';
        var bgcolor	= '';
        var even	= this.even_color;
        var odd		= this.odd_color;
        
        var i,
    		max;
    	html += '<tbody>';
    	for (i = 0, max = this.data.length; i < max; i++){
    	    if(i % 2 == 0) bgcolor = even;
    	    else bgcolor = odd;
    	    var obj = this.data[i];
    	    var t,
    	    	a,
    	    	answers_size;
    	    html += '<tr class="'+bgcolor+'">';
	    	for(t = 0; t < this.num_titles; t++) {
	    		html += '<td>'+obj[this.columns_key[t]]+'</td>';	    		
	    	}
    		for(a = 0, answers_size = obj['answers'].length; a < answers_size; a++){
    			var answer = obj['answers'][a];
    			html += '<td>';
            	switch(answer['type']) {
            	case 'picture':
            		if (answer['value'] != '') {
            			html += '<div align="center"><img onclick="showPicturePopup(\''+answer['value']+'\')" src="'+answer['thumbnail']+'" /></div>';
            		} else {
            			html += '<div align="center"><img width="90px" src="images/form/not_available.jpeg"/></div>';
            		}
            		break;
            	case 'audio':
            		if (answer['value'] != '') {
            			html += '<div align="center"><img width="20em" onclick="showMultimediaPopup(\'audio\', \''+answer['value']+'\')" src="images/form/audio_icon.png" /></div>';
            		} else {
            			html += '<div align="center"><img width="20em" src="images/form/audio-muted_icon.png"/></div>';
            		}
            		break;
            	case 'video':
            		if (answer['value'] != '') {
            			html += '<div align="center"><img onclick="showMultimediaPopup(\'video\', \''+answer['value']+'\')" src="'+answer['thumbnail']+'" /></div>';
            		} else {
            			html += '<div align="center"><img width="90px" src="images/form/not_available.jpeg"/></div>';
            		}		
            		break;
            	case 'geolocation':
            		html += parseGeolocation(answer['value']);
            		break;
            	case 'barcode':
            	case 'money':
            		html += parseQuestionsWithSubType(answer['subtype'], answer['value']);
            		break;
            	case 'checkbox':
            	case 'radio':
            	case 'combobox':
            		var answer_val = answer['value'];            		
            		if (answer_val.match('^{') && answer_val.match('}$')) {
            			html += parseCheckBox(answer['value']);            			
            		} else {
            			html += answer['value'];
            		}
            		break;
            	default:
            		html += answer['value'];
            		break;            	
            	}
            	html += '</td>';
    		}	    	
    	    html += '</tr>';
        }
        html += '</tbody>';
        return html;
	};
};
AnswerGrid.prototype = new MGrid();

var GroupGrid = function GroupGrid(){
	this.buildBody = function buildBody() {
		var html	= '';
		var bgcolor	= '';
		var even	= this.even_color;
		var odd		= this.odd_color;

		var i,
		max;

		html += '<tbody>';
		for (i = 0, max = this.data.length; i < max; i++){
		    if(i % 2 == 0) bgcolor = even;
		    else bgcolor = odd;
		    var obj = this.data[i];
		    var t;
		    html += '<tr id="'+obj['key']+'" class="'+bgcolor+'">';
			for(t = 0; t < this.num_titles; t++) {
				if(this.columns_key[t] == this.key_submenu) {
					html += '<td><a href="group.html?id='+obj['key']+'">'+obj['name']+'</a></td>';
					html += '<td width="3%">';
						html += '<ul class="form_sub_menu"><li>';
	    				html += '<a href="#"><span class="arrow">&#9660;</span></a>';
		    			html += '<ul>';
		    			html += buildGroupSubMenu(obj['key']);
		    			html += '</ul>';
		    			html += '</li>';
		    			html += '</ul>';
					html += '</td>';
				}
				else{
					html += '<td>'+obj[this.columns_key[t]]+'</td>';
				}
			}
		    html += '</tr>';
		}
		html += '</tbody>';
		return html;
	};
};
GroupGrid.prototype = new MGrid();