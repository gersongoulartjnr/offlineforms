package br.unifesp.maritaca.analytics.util;

public class ConstantsPipeline {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	/*
	 * @pipeline
	 */
	public static final String PROJECT = "$project";
	public static final String MATCH = "$match";
	public static final String LIMIT = "$limit";
	public static final String SKIP = "$skip";
	public static final String UNWIND = "$unwind";
	public static final String GROUP = "$group";
	public static final String SORT = "$sort";
	public static final String ELEM_MATCH = "$elemMatch";
	public static final String AGGREGATE = "aggregate";

	/*
	 * @group
	 */
	public static final String ADD_TO_SET = "$addToSet";
	public static final String FIRST = "$first";
	public static final String LAST = "$last";
	public static final String MAX = "$max";
	public static final String MIN = "$min";
	public static final String AVG = "$avg";
	public static final String PUSH = "$push";
	public static final String SUM = "$sum";

	/*
	 * @boolean
	 */
	public static final String AND = "$and";
	public static final String OR = "$or";
	public static final String NOT = "$not";

	/*
	 * @comparison
	 */
	public static final String CMP = "$cmp";
	public static final String EQ = "$eq";
	public static final String GT = "$gt";
	public static final String GTE = "$gte";
	public static final String LT = "$lt";
	public static final String LTE = "$lte";
	public static final String NE = "$ne";
	public static final String IN = "$in";

	/*
	 * @JSON_Constants
	 */
	public static final String AUTHOR = "author";	
	public static final String CREATION_DATE = "creationDate";
	public static final String URL = "url";
	public static final String FORM_TITLE = "formTitle";
	public static final String USER_KEY = "userKey";
	public static final String _ID = "_id";
	public static final String FORM_KEY = "formKey";

	/*
	 * @numbers
	 */
	public static final String ADD = "$add";

	/*
	 * @String
	 */
	public static final String REGEX = "$regex";
	public static final String OPTIONS = "$options";

	/*
	 * @gps
	 */
	public static final String NEAR = "near";
	public static final String DISTANCE_FIELD = "distanceField";
	public static final String MAX_DISTANCE = "maxDistance";
	public static final String GEONEAR = "$geoNear";
}