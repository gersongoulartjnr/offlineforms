package br.unifesp.maritaca.persistence.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class has generic methods that are use in the persistence 
 * layer
 * 
 * @author Maritaca team
 */
public class UtilsPersistence {
	
	private static final int STRING_LENGTH = 10;
	private static ConfigurationFile configFile;

	private static final Log log = LogFactory.getLog(UtilsPersistence.class);
	
	/**
	 * This method generates a random string, default size is 10
	 * @return a random String
	 */
	public static final String randomString() {
		return randomString(STRING_LENGTH);
	}

	/**
	 * This method generates a random string
	 * 
	 * @param length size of the random string
	 * @return the random string
	 */
	public static final String randomString(int length) {
		if (length >= 0)
			return RandomStringUtils.randomAlphanumeric(length);
		else
			return RandomStringUtils.randomAlphanumeric(STRING_LENGTH);
	}

	/**
	 * This method sets all the string's characters in uppercase. 
	 * 
	 * @param value is the string to be set
	 * @return a string with all the elements in uppercase.
	 */
	public static String toUpperFirst(String value) {
		StringBuilder result = new StringBuilder(value);
		result.setCharAt(0, new String(Character.toString(result.charAt(0)))
				.toUpperCase().charAt(0));

		return result.toString();
	}
	
	
	/**
	 * Return the system configuration file. If it is not loaded it is read
	 * and returned.
	 **/
	public static ConfigurationFile retrieveConfigFile() {
		if(configFile == null){
			loadConfigurationProperties();
		}
		return configFile;
	}

	
	public static void loadConfigurationProperties() {
		String path = retrieveMaritacaConfigFilePath();
		loadConfigurationProperties(path);
	}
	
	
	/**
	 * This method loads an ConfigurationFile object with all 
	 * configuration properties.
	 * 
	 * @return
	 */
	public static void loadConfigurationProperties(String path) {
		try {									
			configFile = new ConfigurationFile();		
			Properties prop = new Properties();			
			prop.load(new FileInputStream(path));
			configFile.setMobilePath(prop.getProperty(ConstantsPersistence.MARITACA_MOBILE_PATH));
			configFile.setAndroidSdkPath(prop.getProperty(ConstantsPersistence.ANDROID_SDK_PATH));
			configFile.setScriptLocation(configFile.getMobilePath()+"/"+ConstantsPersistence.MOB_SCRIPT_LOCATION);
			configFile.setMaritacaMobileFolder(configFile.getMobilePath());
			configFile.setMaritacaMobileProjects(configFile.getMobilePath()+ConstantsPersistence.MARITACA_MOBILE_PROJECTS);
			configFile.setUriServer(prop.getProperty(ConstantsPersistence.MARITACA_URI_SERVER));
			configFile.setReleaseScriptLocation(configFile.getMobilePath()+"/"+ConstantsPersistence.MOB_RELEASE_SCRIPT_LOCATION);
			
			configFile.setFilesystemPath(prop.getProperty(ConstantsPersistence.FILESYSTEM_PATH));
			configFile.setHttpUriServer(prop.getProperty(ConstantsPersistence.HTTP_URI_SERVER));
			
			configFile.setFfmpegPath(prop.getProperty(ConstantsPersistence.FFMPEG_PATH));
			configFile.setTmpDir(prop.getProperty(ConstantsPersistence.TMP_DIRECTORY));
			
			configFile.setClusterAddr(prop.getProperty(ConstantsPersistence.CLUSTER_ADDR));
			configFile.setPopulateTestDb(prop.getProperty(ConstantsPersistence.POPULATE_TEST_DATABASE));
			
			configFile.setFbWebApiKey(prop.getProperty(ConstantsPersistence.FB_WEB_API_KEY));
			configFile.setFbWebAppSecret(prop.getProperty(ConstantsPersistence.FB_WEB_APP_SECRET));
			configFile.setFbWebRedirect(prop.getProperty(ConstantsPersistence.FB_WEB_REDIRECT_URI));
			configFile.setFbMobApiKey(prop.getProperty(ConstantsPersistence.FB_MOB_API_KEY));
			configFile.setFbMobAppSecret(prop.getProperty(ConstantsPersistence.FB_MOB_APP_SECRET));
			configFile.setFbMobRedirect(prop.getProperty(ConstantsPersistence.FB_MOB_REDIRECT_URI));
			configFile.setMongoHost(prop.getProperty(ConstantsPersistence.MONGO_HOST));
			configFile.setMongoPort(prop.getProperty(ConstantsPersistence.MONGO_PORT));
			configFile.setMongoTimeout(prop.getProperty(ConstantsPersistence.MONGO_TIMEOUT));
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}

	private static String retrieveMaritacaConfigFilePath() {		
		String propertiesPath = System.getProperty(ConstantsPersistence.ENVIRONMENT_VAR_CONFIG_PATH);
		if (propertiesPath == null || propertiesPath.equals("")) {
			propertiesPath = System.getenv(ConstantsPersistence.ENVIRONMENT_VAR_CONFIG_PATH);
			log.info("Loading configuration properties (getenv) from environment variables!");
			if (propertiesPath == null || propertiesPath.equals("")) {
				throw new RuntimeException("ERROR: Please set the environment variable to Configuration properties file (MARITACA_CONFIG_PATH)");
			}			
		} else {
			log.info("Loading configuration properties (getProperty) from JVM");
		}
		return propertiesPath;				
	}
}