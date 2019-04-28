package br.unifesp.maritaca.mobile.util;

import java.util.List;

import br.unifesp.maritaca.mobile.model.Question;

public class Constants {

	//public static final String FORM_ID="7421fb10-564e-11e5-874f-52540043a0d4"; //Don't touch	
	public static String FORM_ID="";
	public static final String FORM_NAME_ID = "form_id"; //Don't touch
	
	public static final String M_ENCODING = "UTF-8";
	
	public static final String MODEL_PARCELABLE		= "MODEL_PARCELABLE";
	public static final String IS_LIST_MODE_ACTIVE	= "IS_LIST_MODE_ACTIVE";
	public static final String QUESTION_ID 			= "QUESTION_ID";
	public static final String QUESTIONS_DATA			= "QUESTIONS_DATA";
	
	// SQLite
	public static final String DATABASE_NAME = "maritaca.sqlite";
	public static final int DATABASE_VERSION = 1;
	public static final String USER_DATA = "USER_DATA";
	public static final String ANSWER_GROUP_ID = "ANSWER_GROUP_ID";
	// Table: user
	public static final String USER_TABLE_NAME = "user";
	public static final String USER_ID = "user_id";
	public static final String USER_EMAIL = "email";
	public static final String USER_ACCESSTOKEN = "access_token";
	public static final String USER_REFRESHTOKEN = "refresh_token";
	public static final String USER_INIT_DATE = "init_date";
	public static final String USER_EXP_DATE = "expiration_date";
	public static final String USER_FORM_ID = "form_id";

	// Table: answers_group
	public static final String GROUP_TABLE_NAME = "answers_group";
	public static final String GROUP_ID = "group_id";
	public static final String GROUP_TIMESTAMP = "timestamp";
	public static final String GROUP_VALID = "group_valid";
	// Table: answer
	public static final String ANSWER_TABLE_NAME = "answer";
	public static final String ANSWER_ID = "id";
	public static final String ANSWER_TYPE = "type";
	public static final String ANSWER_SUBTYPE = "subtype";
	public static final String ANSWER_VALUE = "value";
	// Table: error_log
	public static final String LOG_TABLE_NAME = "errors_log";
	public static final String LOG_ID = "log_id";
	public static final String LOG_MESSAGE = "message";
	public static final String LOG_DATE = "timestamp";
	public static final String LOG_ANDROIDVERSION = "android_version";	
	// Table: settings
	public static final String SETTINGS_TABLE_NAME = "settings";
	public static final String SETTINGS_ID = "id";
	public static final String SETTINGS_SYNC_ACTIVE = "sync_active";
	public static final String SETTINGS_SYNC_INTERVAL = "sync_interval";
	public static final String SETTINGS_COLLECT_DATA_MODE = "collect_data_mode";;
	public static final String SETTINGS_SENDING_DATA_AUTO = "sending_data_auto";
	public static final String SETTINGS_LAST_SYNC_LOCAL_TIME = "last_sync_local_data";
	public static final String SETTINGS_LAST_SYNC_SERVER_TIME = "last_sync_server_data";

	// Diaglos ids
	public static final int SAVE_DIALOG 				= 1;
	public static final int DIALOG_INVITE_OFFLINE_OK_CANCEL 	= 10;
	public static final int DIALOG_OFFLINE 			= 11;
	public static final int DIALOG_LOGOUT 			= 12;
	public static final int DIALOG_ERROR_REPORT 		= 13;
	public static final int DIALOG_TIME_EXPIRED 		= 14;
	public static final int CANCEL_QUESTION 			= 15;
	public static final int CANCEL_QUESTION_LIST_MODE	= 16;
	public static final int BACK_LIST_QUESTIONS		= 17;
	public static final int SAVE_LIST_QUESTIONS		= 18;
	public static final int CANCEL_LIST_QUESTIONS		= 19;
	public static final int CANCEL_LIST_QUESTION		= 20;

	// Exceptions
	public static final String UNCAUGHT_EXCEPTION_CODE = "UNCAUGHT_EXCEPTION_CODE";
	public static final String UNCAUGHT_EXCEPTION_ERROR = "UNCAUGHT_EXCEPTION_ERROR";

	public static final String FORM_FILENAME	= "form.xml";
	public static final String FORM_BIN		= "form.bin";

	public static final String DATE_ISO8601FORMAT = "yyyy-MM-dd";
	public static final String PROGRESSBAR_COLOR = "#006666";

	public static final String FORM_URL_DATA = "FORM_URL_DATA";
	public static final String DATE_FILE_FORMAT = "ddMMyyyy_HHmmss";
	public static final String FILES_DIR		= "maritaca";
	public static final int AUDIO_REQUEST		= 70;
	public static final int PICTURE_REQUEST	= 71;
	public static final int VIDEO_REQUEST		= 72;
	public static final int VOICE_RECOGNITION_REQUEST	= 1234;
	public static final int BARCODE_REQUEST	= 195543262;
	
	public static final int MAX_PICTURE_SIZE	= 1468006;  //1024*1024*1.4
	public static final int MAX_AUDIO_SIZE	= 1048576;  //1024*1024*1
	public static final int MAX_VIDEO_SIZE	= 15728640; //1024*1024*15
	
	public static final String MAP_XML = "xml";
	public static final String MAP_FILES = "files";

	public static final String XML_ID = "id";
	public static final String XML_NAME = "name";
	public static final String XML_REPORT = "report";
	public static final String XML_TYPE = "type";
	public static final String XML_SUBTYPE = "subtype";
	public static final String XML_VALUE = "value";
	public static final String XML_TIMESTAMP = "timestamp";
	
	// xml answer elements
	public static final String ANSWER_XML = "xml";
	
	public static final String ANSWER_RESPONSE = "collecteddata";
	public static final String ANSWER_FORMID = "formId";
	public static final String ANSWER_USERID = "userId";
	public static final String ANSWER_ANSWERS = "answers";
	public static final String ANSWER_ANSWER = "answer";
	public static final String ANSWER_QUESTION = "question";

	// json response names
	public static final String JSON_GENERIC_RESPONSE_VALUE = "value";
	public static final String JSON_GENERIC_RESPONSE_EXTRA = "extra";
	
	// xml list answers elements
	public static final String ANSW_LIST_EMAIL = "email";
	public static final String ANSW_LIST_DATE = "date";

	// OAuth constants
	public static final String URI_PROJECT = "http://maritaca.unifesp.br"; // "http://10.0.2.2";
	public static final String URI_SERVER = "http://maritaca.unifesp.br/maritaca";
	public static final String URI_OAUTH = URI_SERVER + "/oauth/";
	public static final String URI_WS_ANSWER = URI_SERVER + "/ws/answer/add/";
	public static final String URI_WS_POST_ANSWER = URI_SERVER + "/ws/answer/save/";
	public static final String URI_WS_POST_SAVE_ANSWERS = URI_SERVER + "/ws/answer/saveAnswers/";
	public static final String URI_WS_GET_LAST_ANSWERS = URI_SERVER + "/ws/answer/lastAnswers/";
	public static final String URI_WS_LIST_ANSWER = URI_SERVER + "/ws/answer/list/";
	public static final String URI_WS_LIST_REPORT = URI_SERVER + "/ws/report/list/";
	public static final String URI_WS_INVITE_FRIENDS = URI_SERVER + "/ws/invite/contactlists/";
	public static final String URI_WS_ERRORREPORT = URI_SERVER + "/ws/messages/mobileerror/";
	public static final String URI_AUTH_REQUEST = URI_OAUTH + "authorizationRequest";
	public static final String URI_ACCESSTOKEN_REQUEST = URI_OAUTH + "accessTokenRequest";

	public static final String OAUTH_REDIRECT_URI = "http://localhost:8082/";
	public static final String OAUTH_CLIENT_ID = "maritacamobile";
	public static final String OAUTH_RESPONSE_TYPE = "response_type";
	public static final String OAUTH_CLIENT_SECRET = "maritacasecret";
	
	public static final String URL_WEB_SERVICE = "http://192.168.1.34:8080/axis/";
	public static String PATH_FORM = "/data/data/br.unifesp.maritaca.mobile.activities/form.xml";
	public static String PATH_CSV = "/data/data/br.unifesp.maritaca.mobile.activities/form.csv";
	public static List<Question> LIST_QUESTIONS = null;

	public static final String URL_FORM="URL_FORM";
	public static final String URL_FORM_URL="URL";

	public static final String NAME_FORM="NAME_FORM";
	public static final String NAME_FORM_NAME="NAME";
	
}