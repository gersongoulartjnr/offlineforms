var AnalyticsItemClass = function AnalyticsItemClass(){
	this.item_name		= null;
	this.item_filter	= null;
	this.list_transformation = null;
	this.list_transformation2 = null;
	this.item_view		= null;
	this.item_id		= null;
	
	this.getElementDataFromJson = function getElementDataFromJson(jsonDoc){
		this.item_id	= jsonDoc.id;
		this.item_name	= jsonDoc.name;
		
		this.item_filter = new AnalyticsFilterClass();		
		this.item_filter.getDataFromFilterJson(jsonDoc.filter);
		this.list_transformation = new AnalyticsTransformationClass();
		this.list_transformation.getDataFromTransformationsJson(TRA_MAIN, jsonDoc.transformations);

		this.list_transformation2 = new AnalyticsTransformationClass();
		this.list_transformation2.getDataFromTransformationsJson(TRA_SECOND, jsonDoc.sectransformations);		
		
		this.item_view = new AnalyticsViewClass();
		this.item_view.getDataFromViewJson(jsonDoc.view);
	};
	
	this.renderView = function renderView(){
		$('#item_title').val(this.item_name);
		if(ANALYTICS_CONTAINER.current_side == ITEM_FILTER){ //?
			this.item_filter.renderFilterView();
		}
		if(this.list_transformation == null){
			this.list_transformation = new AnalyticsTransformationClass();
			this.list_transformation.list_type = TRA_MAIN;
		}
		this.list_transformation.renderTransformation();
		
		if(this.list_transformation2 == null){
			this.list_transformation2 = new AnalyticsTransformationClass();
			this.list_transformation2.list_type = TRA_SECOND;
		}
		this.list_transformation2.renderTransformation();
		
		if(this.item_view == null){
			this.item_view = new AnalyticsViewClass();
		}
		this.item_view.renderView();		
	};
	
	this.toJson = function toJson(i){
		var json = '{'+
				'"id":'+i+
				', "name":"'+this.item_name+'"'+
				', "filter":'+this.item_filter.toJson()+
				', "transformations":'+this.list_transformation.toJson()+
				', "sectransformations":'+this.list_transformation2.toJson()+
				', "view":'+this.item_view.toJson()+
			 '}';
		return json;
	};
	
	this.validateData = function validateData(){
		if(this.item_name == undefined || this.item_name == null || "" == this.item_name){
			addMessage(jQuery.i18n.prop('err_item_name_required'), 'error');
			return false;
		}
		
		if(!this.item_filter.validateData()){
			return false;
		}
		if(!this.list_transformation.validateData()){
			return false;
		}
		if(!this.list_transformation2.validateData()){
			return false;
		}
		if(!this.item_view.validateData()){
			return false;
		}
		
		return true;
	};
	
	this.saveData = function saveData(){
		if(ANALYTICS_CONTAINER.current_side == ITEM_FILTER){
			this.item_filter.saveData();
		} else if(ANALYTICS_CONTAINER.current_side == ITEM_TRANSFORMATION){
			this.list_transformation.saveData();
		} else if(ANALYTICS_CONTAINER.current_side == ITEM_TRANSFORMATION_2){
			this.list_transformation2.saveData();
		} else if(ANALYTICS_CONTAINER.current_side == ITEM_VIEW){
			this.item_view.saveData();
		}
	};
	
	this.drawProcess = function drawProcess(){
		this.item_filter.drawFilterProcess();
		this.list_transformation.drawTransformationProcess();
		this.list_transformation2.drawTransformationProcess();		
		this.item_view.drawViewProcess();
	};
};