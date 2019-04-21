var AnalyticsContainerClass = function AnalyticContainerClass(){
	this.elements		= [];
	this.current_side	= ITEM_FILTER;
	this.item_status	= NORMAL_ITEM; 
	
	this.renderMainView = function renderMainView(element_id){
		if(this.elements.length == 0){
			A_ID_ELEMENT = 0; //CHECK THIS!
			this.current_side	= ITEM_FILTER;
			this.item_status	= FIRST_ITEM;
			var emptyAnalyticsItem = new AnalyticsItemClass();
				emptyAnalyticsItem.item_filter = new AnalyticsFilterClass();
			this.elements.push(emptyAnalyticsItem);
			emptyAnalyticsItem.renderView();
			emptyAnalyticsItem.saveData();
			emptyAnalyticsItem.drawProcess();
		} else{
			if(element_id == undefined || element_id == null) { A_ID_ELEMENT = 0; } 
			else { A_ID_ELEMENT = element_id; }		
			this.elements[parseInt(A_ID_ELEMENT)].renderView();
		}
	};
	
	this.toJson = function toJson(){
		var json = '{"name":"test", ';
				json += '"elements":[';
				for(var i in this.elements){
					json += this.elements[i].toJson(i);
				}
				json += ']';
			json += '}';		
		return json;
	};
	
	this.saveData = function saveData(){
		for(var i in this.elements){
			this.elements[i].saveData();
		}
	};
	
	this.validateData = function validateData(){
		var flag = true;
		for(var i in this.elements){
			flag = this.elements[i].validateData();
			if(flag == false){
				return false;
			}
		}
		return flag;
	};
	
	this.drawProcess = function drawProcess(item_id){
		this.elements[item_id].drawProcess();
	};
};