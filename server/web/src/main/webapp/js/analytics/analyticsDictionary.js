var A_FORM_COLLECTORS	= [];
var A_ID_ELEMENT		= null;
var A_ID_TRANSFORMATION	= null;
var ANALYTICS_CONTAINER	= null;

var FIELD_GEOLOCATION	= null;
var HAS_GEOLOCATION		= false;
var A_FORM_QUESTIONS	= [];
var A_TRANSFORMATIONS	= [];

const MAX_TRANSFORMATIONS	= 20;
const MAX_SECTRANSFORMATIONS= 1;
//Constants
const TXT_LATITUDE		= 'latitude';
const TXT_LONGITUDE		= 'longitude';
const LBL_LATITUDE		= 'Latitude';
const LBL_LONGITUDE		= 'Longitude';

const TXT_ELEMENTS		= 'elements';
const TXT_ELEMENT		= 'element';
const TXT_FILTER		= 'filter';
const TXT_ID			= 'id';
const TXT_DATE			= 'date';
const TXT_FROM			= 'from';
const TXT_TO			= 'to';
const TXT_USERS			= 'users';
const TXT_USER			= 'user';
const TXT_FIELDS		= 'fields';
const TXT_FIELD			= 'field';
const TXT_GEOLOCATION	= 'geolocation';
const TXT_VALUE			= 'value';
const TXT_TYPE			= 'type';
const TXT_VIEW			= 'view';

//const TXT_TRANSFORMATIONS	= 'transformations';
const TXT_TRANSFORMATION	= 'transformation';

const CSS_USER_LOCATION	= 'user_location';
const ID_USER_LOCATION	= 'id_user_location';
const ID_VALUE_LOCATION	= 'id_value_location';
const ID_LOCATION		= 'id_location'; //nearest
const VALUE_LOCATION	= 'value_location';//10

const ITEM_FILTER	= 'item_filter';
const ITEM_TRANSFORMATION	= 'item_transformation';
const ITEM_TRANSFORMATION_2	= 'item_transformation_2';
const ITEM_VIEW		= 'item_view';

const NORMAL_ITEM	= 'NORMAL';
const FIRST_ITEM	= 'FIRST';
const NEW_ITEM		= 'NEW';

const TRA_MAIN 		= 'transformations';
const TRA_SECOND	= 'sectransformations';
const PREFIX_TRA_MAIN	= 'tra';
const PREFIX_TRA_SEC	= 'sectra';
const TRA_FIELDS	= 'list_fields';
const TRA_TRAS		= 'list_tras';
/***********************************************************************************************************/

/**
 * List of components (15) 
 */
var C_TEXT 		= 'text';
var C_DATE 		= 'date'; 
var C_COMBOBOX	= 'combobox';
var C_RADIOBOX	= 'radio';
var C_CHECKBOX	= 'checkbox';
var C_NUMBER	= 'number';
var C_DECIMAL	= 'decimal';
var C_MONEY		= 'money';
var C_PICTURE	= 'picture';
var C_AUDIO		= 'audio';
var C_VIDEO		= 'video';
var C_GEOLOCATION = 'geolocation';
var C_BARCODE	= 'barcode';
var C_SLIDER	= 'slider';
var C_DRAW 		= 'draw';

/**
 * List of transformations
 */
var T_SORT		= { 
		'name'	: 'SORT',
		'method': 'sort',
		'weight': 10
		};
var T_MIN		= {
		'name'	: 'MIN',
		'method': 'min',
		'weight': 1			
		};
var T_MAX		= {
		'name'	: 'MAX',
		'method': 'max',
		'weight': 1
		};
var T_NMIN		= {
		'name'	: 'NMIN',
		'method': 'nMin',
		'weight': 2			
		};
var T_NMAX		= {
		'name'	: 'NMAX',
		'method': 'nMax',
		'weight': 2
		};
var T_COUNT		= {
		'name'	: 'COUNT',
		'method': 'count',
		'weight': 1
		};
var T_EQUAL		= {
		'name'	: 'EQUAL',
		'method': 'equal',
		'weight': 9
		};
var T_NOT_EQUAL	= {
		'name'	: 'NOTEQUAL',
		'method': 'notEqual',
		'weight': 9
		};
var T_GREATER	= {
		'name'	: 'GREATER',
		'method': 'greater',
		'weight': 9
		};
var T_LESS		= {
		'name'	: 'LESS',
		'method': 'less',
		'weight': 9
		};
var T_GREATER_EQUAL	= {
		'name'	: 'GREATER_EQUAL',
		'method': 'greaterEqual',
		'weight': 9
		};
var T_LESS_EQUAL	= {
		'name'	: 'LESS_EQUAL',
		'method': 'lessEqual',
		'weight': 9
		};
var T_SUM		= {
		'name'	: 'SUM',
		'method': 'sum',
		'weight': 1
		};
var T_AVERAGE	= {
		'name'	: 'AVERAGE',
		'method': 'average',
		'weight': 1			
		};
var T_NFIRSTS	= {
		'name'	: 'NFIRSTS',
		'method': 'nFirsts',
		'weight': 10
		};
var T_NLASTS	= {
		'name'	: 'NLASTS',
		'method': 'nLasts',
		'weight': 10
		};
var T_NEAREST	= {
		'name'	: 'NEAREST',
		'method': 'nearest',
		'weight': 2
		};
var T_GROUP_BY	= {
		'name'	: 'GROUP_BY',
		'method': 'groupBy',
		'weight': 10
		};

/**
 * List of Views
 */
var V_GRID		= { 'name' : 'GRID' };
var V_BARCHART	= { 'name' : 'BARCHART' };
var V_PIE		= { 'name' : 'PIE' };
var V_SLIDER	= { 'name' : 'SLIDER' };
var V_MAP		= { 'name' : 'MAP' };

//const
const TXT_TRANSFORMATIONS = 'transformations';
const TXT_VIEWS = 'views';
/**
 * Rules: Transformations by components 
 */
var A_RULES = {
	'text': {
		'transformations': [
		    { "name": T_SORT.name }, { "name": T_EQUAL.name }, { "name": T_NOT_EQUAL.name }, { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 
		],
		'views': [
		    { "name": V_GRID.name } 
		]
	}, 	
	'date': {
		'transformations': [
		    { "name": T_SORT.name }, { "name": T_MIN.name }, { "name": T_MAX.name }, { "name": T_NMIN.name }, { "name": T_NMAX.name }, 
		    { "name": T_EQUAL.name }, { "name": T_NOT_EQUAL.name }, { "name": T_GREATER.name }, { "name": T_LESS.name }, { "name": T_GREATER_EQUAL.name }, { "name": T_LESS_EQUAL.name }, 
		    { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 
		],
		'views': [
		    { "name": V_GRID.name }
		]
	}, 
	'combobox': {
		'transformations': [
		    { "name": T_SORT.name }, { "name": T_EQUAL.name }, { "name": T_NOT_EQUAL.name }, { "name": T_COUNT.name }, { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 
		],
		'views': [
		    { "name": V_GRID.name }
		]
	}, 
	'radio': {
		'transformations': [
		    { "name": T_SORT.name }, { "name": T_EQUAL.name }, { "name": T_NOT_EQUAL.name }, { "name": T_COUNT.name }, { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 	                    
		],
		'views': [
		    { "name": V_GRID.name }
		]	
	}, 
	'checkbox': {
		'transformations': [
		    { "name": T_SORT.name }, { "name": T_EQUAL.name }, { "name": T_NOT_EQUAL.name }, { "name": T_COUNT.name }, { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 
		],
		'views': [
		    { "name": V_GRID.name }
		]
	}, 	
	'number': {
		'transformations': [		
		    { "name": T_SORT.name }, { "name": T_MIN.name }, { "name": T_MAX.name }, { "name": T_NMIN.name }, { "name": T_NMAX.name }, { "name": T_SUM.name }, { "name": T_AVERAGE.name }, 
		    { "name": T_EQUAL.name }, { "name": T_NOT_EQUAL.name }, { "name": T_GREATER.name }, { "name": T_LESS.name }, { "name": T_GREATER_EQUAL.name }, { "name": T_LESS_EQUAL.name }, 
		    { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 
		],
		'views': [
		    { "name": V_GRID.name }
		]
	}, 
	'decimal': {
		'transformations': [		
		    { "name": T_SORT.name }, { "name": T_MIN.name }, { "name": T_MAX.name }, { "name": T_NMIN.name }, { "name": T_NMAX.name }, { "name": T_SUM.name }, { "name": T_AVERAGE.name }, 
		    { "name": T_EQUAL.name }, { "name": T_NOT_EQUAL.name }, { "name": T_GREATER.name }, { "name": T_LESS.name }, { "name": T_GREATER_EQUAL.name }, { "name": T_LESS_EQUAL.name }, 
		    { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 
		],
		'views': [
		    { "name": V_GRID.name }
		]
	},
	'money': {
		'transformations': [		
		    { "name": T_SORT.name }, { "name": T_MIN.name }, { "name": T_MAX.name }, { "name": T_NMIN.name }, { "name": T_NMAX.name }, { "name": T_SUM.name }, { "name": T_AVERAGE.name }, 
		    { "name": T_EQUAL.name }, { "name": T_NOT_EQUAL.name }, { "name": T_GREATER.name }, { "name": T_LESS.name }, { "name": T_GREATER_EQUAL.name }, { "name": T_LESS_EQUAL.name }, 
		    { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 
		],
		'views': [
		    { "name": V_GRID.name }
		]
	}, 
	'picture': {
		'transformations': [
		    { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 
		],
		'views': [
		    { "name": V_GRID.name }
		]
	}, 
	'audio': {
		'transformations': [
		    { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name }
		],
		'views': [
		    { "name": V_GRID.name }
		]
	},
	'video': {
		'transformations': [
		    { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name }
		],
		'views': [
		    { "name": V_GRID.name }
		]
	},
	'geolocation': {
		'transformations': [
		    { "name": T_NEAREST.name }
		],
		'views': [
		    { "name": V_GRID.name }, { "name": V_MAP.name }
		]
	},
	'barcode': {
		'transformations': [
            { "name": T_SORT.name }, { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 
		],
		'views': [
		    { "name": V_GRID.name }
		]
	},
	'slider': {
		'transformations': [
		    { "name": T_SORT.name }, { "name": T_MIN.name }, { "name": T_MAX.name }, { "name": T_NMIN.name }, { "name": T_NMAX.name }, { "name": T_SUM.name }, { "name": T_AVERAGE.name }, 
		    { "name": T_EQUAL.name }, { "name": T_NOT_EQUAL.name }, { "name": T_GREATER.name }, { "name": T_LESS.name }, { "name": T_GREATER_EQUAL.name }, { "name": T_LESS_EQUAL.name }, 
		    { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 
		],
		'views': [
		    { "name": V_GRID.name }
		]
	},
	'draw': {
		'transformations': [
		    { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name }, { "name": T_NFIRSTS.name }, { "name": T_NLASTS.name } 
		],
		'views': [
		    { "name": V_GRID.name }, { "name": V_SLIDER.name }
		]
	}	
};

var transformationsByQuestionType = function transformationsByQuestionType(questionType){
	var transfs = [];
	for(var i in A_RULES[questionType][TXT_TRANSFORMATIONS]){
		transfs.push(A_RULES[questionType][TXT_TRANSFORMATIONS][i].name);
	}
	return transfs;
};
var viewsByQuestionType = function viewsByQuestionType(questionType){
	var views = [];
	for(var i in A_RULES[questionType][TXT_VIEWS]){
		views.push(A_RULES[questionType][TXT_VIEWS][i].name);
	}
	return views;
};
/**
 * Constants
 */
const v_filter_users	= 'filter_users';
const c_filter_users_list = 'filter_users_list';
//XML tags
const txt_specific_data = 'specific-data';

const txt_value = 'value';
const txt_group_by = 'group_by';
//IDs
const c_unique_value	= 'unique_value';
const c_list_group_by	= 'list_group_by';
const c_box_values		= 'box_values';
const V_VIEW_VALUES		= 'view_values';

//Transformations
var SORT_ASC	= 'asc';
var SORT_DESC	= 'desc';

/**
 * List of methods by transformation type
 */
//
var SORT_TYPE = [SORT_ASC, SORT_DESC];

var getTransformationsByQuestionType = function getTransformationsByQuestionType(questionType){
	var transfs = [];
	for(var i in A_RULES[questionType][TXT_TRANSFORMATIONS]){
		var weight = aHelperFactory(A_RULES[questionType][TXT_TRANSFORMATIONS][i].name);
		if(weight > 5){
			transfs.push(A_RULES[questionType][TXT_TRANSFORMATIONS][i].name);			
		}		
	}
	return transfs;
};
var getSecTransformationsByQuestionType = function getSecTransformationsByQuestionType(questionType){
	var transfs = [];
	for(var i in A_RULES[questionType][TXT_TRANSFORMATIONS]){
		var weight = aHelperFactory(A_RULES[questionType][TXT_TRANSFORMATIONS][i].name);
		if(weight < 5){
			transfs.push(A_RULES[questionType][TXT_TRANSFORMATIONS][i].name);			
		}		
	}
	return transfs;
};
var aHelperFactory = function aHelperFactory(_transformation){
	var weight = '';
	switch(_transformation){
		case T_GROUP_BY.name:			
			weight = T_GROUP_BY.weight;
			break;
		case T_SORT.name:
			weight = T_SORT.weight;
			break;
		case T_MIN.name:
			weight = T_MIN.weight;
			break;
		case T_MAX.name:
			weight = T_MAX.weight;
			break;
		case T_NMIN.name:
			weight = T_NMIN.weight;
			break;
		case T_NMAX.name:
			weight = T_NMAX.weight;
			break;		
		case T_COUNT.name:
			weight = T_COUNT.weight;
			break;
		case T_EQUAL.name:
			weight = T_EQUAL.weight;
			break;
		case T_NOT_EQUAL.name:
			weight = T_NOT_EQUAL.weight;
			break;
		case T_GREATER.name:
			weight = T_GREATER.weight;
			break;
		case T_LESS.name:
			weight = T_LESS.weight;
			break;
		case T_GREATER_EQUAL.name:
			weight = T_GREATER_EQUAL.weight;
			break;
		case T_LESS_EQUAL.name:
			weight = T_LESS_EQUAL.weight;
			break;
		case T_SUM.name:
			weight = T_SUM.weight;
			break;
		case T_AVERAGE.name:
			weight = T_AVERAGE.weight;
			break;
		case T_NFIRSTS.name:
			weight = T_NFIRSTS.weight;
			break;
		case T_NLASTS.name:
			weight = T_NLASTS.weight;
			break;
		case T_NEAREST.name:
			weight = T_NEAREST.weight;
			break;
		default:
			return null;		
	}
	return weight;
};