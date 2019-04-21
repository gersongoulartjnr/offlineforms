package br.unifesp.maritaca.business.util;

/**
 * This class is to set all the constants used in the business layer
 * 
 * @author Maritaca team
 */
public class ConstantsBusiness {
	public static final Integer N_PUBLIC_FORMS = 8;
	public static final String LOCALE_DEFAULT		= "EN";
	public final static String SHORT_DATE_ISO8601	= "yyyy-MM-dd";
	public static final String XML_CONTENT_TYPE	= "text/xml";
	
	public static final String WS_USER_KEY 	= "userKey";
	public static final String EMAIL_REG_EXP 	= "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$";
	public static final String ENCODING_UTF8 	= "UTF-8";
	
	public static final int MIN_FIRSTNAME_SIZE	= 2;
	public static final int MAX_FIRSTNAME_SIZE	= 25;
	public static final int MIN_LASTNAME_SIZE		= 2;
	public static final int MAX_LASTNAME_SIZE		= 40;
	public static final int MIN_PASSWORD_SIZE		= 5;
	public static final int MAX_PASSWORD_SIZE		= 20;
	
	public final static int ICON_MAX_SIZE		= 1048576;
	public final static String JPG_FORMAT		= "image/jpeg";
    public final static String PNG_FORMAT		= "image/png";
    public final static String GIF_FORMAT		= "image/gif";
    
    /* Search module*/
    public final static int SEARCH_PUBLIC_FORMS	= 0;
    public final static int SEARCH_MY_FORMS		= 1;
    public final static int SEARCH_SHARED_FORMS	= 2;
    
	/* App Mobile */
    public static final String APK_DIRNAME 			= "apk";
    public static final String APK_EXTENSION 			= ".apk";
    public static final String PREFIX_FORM_NAME		= "form_";
    public static final String APK_NAME				= "maritaca-mobile-release.apk";
	public static final String CONF_FILE_NAME			= "configuration.properties";
	public static final String MARITACA_MOBILE_FOLDER	= "maritaca-mobile";	
	public static final String MOB_FORM_XML_PATH		= "/maritaca-mobile/assets/form.xml";
	public static final String MOB_FORM_LOGO_PATH		= "/maritaca-mobile/res/drawable-hdpi/form_logo.png";
	public static final String MOB_BIN_PATH			= "/maritaca-mobile/bin/";	
	public static final String MOB_MIMETYPE			= "application/vnd.android.package-archive";
	
	/* Initial system values  */
	public static final String ROOT 			= "root";
	public static final String PASSROOT 		= "dc76e9f0c0006e8f919e0c515c66dbba3982f785"; //sha1 for 'root'
	public static final String ROOTEMAIL 		= "root@maritaca.com";
	public static final String CFG_ROOT 		= "root";
	public static final String MARITACAMOBILE	= "maritacamobile";
	public static final String MARITACASECRET	= "maritacasecret";
}