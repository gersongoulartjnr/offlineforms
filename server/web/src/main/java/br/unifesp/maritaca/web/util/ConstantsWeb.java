package br.unifesp.maritaca.web.util;

public class ConstantsWeb {
	
	public static final String SESSION_TIMEOUT		= "session_timeout";
	public static final String LAST_SESSION_ACCESS	= "last_session_timeout";
	
	public static final String CURRENT_USER 			= "currentUser";
	public static final String MOBILE_CURRENT_USER 	= "mobCurrentUser";
	public static final String FORM_ID_NAME 		= "form_id";
	public static final String USER_NAME 		= "user";
	public static final String CAPTCHA_SESSION_KEY 	= "KAPTCHA_SESSION_KEY";
	public static final String ENCODING_UTF8 			= "UTF-8";
	public static final Integer ITEMS_PER_PAGE   		= 10;
	public static final Integer MIN_PREFIX    		= 3;
	public static final String APPLICATION_JSON 		= "application/json";
	public static final String APPLICATION_XML 		= "application/xml";
	public static final String APPLICATION_TEXT 		= "text/plain";
	public static final String XML_EXTENSION 			= ".xml";
	public static final String CSV_EXTENSION 			= ".csv";
	public static final String TXT_EXTENSION 			= ".txt";
	public static final String DOWNLOAD_APK 			= "download_apk";
	
	//
	public static final String FACES_LOGIN 			= "/views/login.xhtml";
	public static final String FACES_HOME 			= "/views/home.xhtml";
	public static final String ROOT_FOR_SHARING 		= "/ws/form/share/";

	public static final String FACES_OAUTH_LOGIN 	= "mobile-login.html";
	public static final String FACES_OAUTH_CONFIRM 	= "mobile-confirmation.html";
	
	//OpendId
	public static final String ATTR_OP				= "op";
	public static final String ATTR_MAC				= "openid_mac";
	public static final String ATTR_ALIAS				= "openid_alias";
    public static final String ATTR_ENDPOINT			= "openid.op_endpoint";    
    public static final String ATTR_RESPONSE_TYPE		= "code";
    public static final String ATTR_CLIENT_ID			= "maritacamobile";
    public static final String ATTR_REDIRECT_URI		= "http://localhost:8082";
	// oauth service
	public static final String MOBILE_LOGIN_URI 		= "/mobile-login.html";
	public static final String OAUTH_USER_ID 			= "user_id";
	public static final String OAUTH_ERROR_URI 		= "error_uri";
	public static final String OAUTH_ERROR_DESCRIPTION = "error_description";
	public static final String OAUTH_ERROR = "error";
	
	public static final Integer MAX_NUM_MODULES   	= 8;
}
