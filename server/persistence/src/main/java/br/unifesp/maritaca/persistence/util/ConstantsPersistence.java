package br.unifesp.maritaca.persistence.util;

/**
 * This class is to set all the constants used in persistence layer
 * 
 * @author Maritaca team
 */
public class ConstantsPersistence {

	public static final int OAUTH_EXPIRATION_DATE 		= 2592000; //60*60*24*30 (30 days)
	public static final int OAUTH_CODE_TIME_TO_LIVE 		= 600; //60*10 (10 minutes)
	public static final int RESET_PASS_EXPIRATION_DATE	= 2592000; //60*60*24 (24 hours)
	public static final String SHORT_DATE_FORMAT_ISO8601 	= "yyyy-MM-dd";	
	public static final String CLASSPATH = "br.unifesp.maritaca.persistence.entity";	
	public static final String ALL_USERS 		= "all_users"; 	// public group
	
	public static final String USER_DISABLED		= "0";
	public static final String USER_ENABLED 		= "1";
	
	public static final String NEVER_LOGGED			= "0";
	public static final String ALREADY_LOGGED 		= "1";
	
	public static final String TOTAL_USERS 	= "total_users";
	
    /* configuration properties names */
    public static final String MARITACA_MOBILE_PATH 	= "maritaca_mobile_path";
    public static final String ANDROID_SDK_PATH 		= "android_sdk_path";
    public static final String MOB_SCRIPT_LOCATION		= "scripts/maritaca.sh";
    public static final String MARITACA_MOBILE_PROJECTS	= "apps/";
    public static final String MARITACA_URI_SERVER		= "maritaca_uri_server";
    public static final String MOB_RELEASE_SCRIPT_LOCATION	= "scripts/maritaca_release.sh";
    public static final String FILESYSTEM_PATH			= "filesystem_path";
    public static final String HTTP_URI_SERVER			= "http_uri_server";
    public static final String FFMPEG_PATH       		= "ffmpeg_path";
    public static final String TMP_DIRECTORY       		= "tmp_directory";
    public static final String CLUSTER_ADDR       		= "cluster_addr";    
	public static final String POPULATE_TEST_DATABASE   = "populate_test_db";
	
	public static final String FB_WEB_API_KEY   		= "fb_web_api_key";
	public static final String FB_WEB_APP_SECRET   	= "fb_web_app_secret";
	public static final String FB_WEB_REDIRECT_URI   	= "fb_web_redirect_uri";
	public static final String FB_MOB_API_KEY   		= "fb_mob_api_key";
	public static final String FB_MOB_APP_SECRET   	= "fb_mob_app_secret";
	public static final String FB_MOB_REDIRECT_URI   	= "fb_mob_redirect_uri";
	public static final String FB_SCOPE   			= "fb_scope";
	
	public static final String MONGO_HOST   			= "mongo_host";
	public static final String MONGO_PORT   			= "mongo_port";
	public static final String MONGO_TIMEOUT 			= "mongo_timeout";

	public static final String ENVIRONMENT_VAR_CONFIG_PATH = "MARITACA_CONFIG_PATH";
}