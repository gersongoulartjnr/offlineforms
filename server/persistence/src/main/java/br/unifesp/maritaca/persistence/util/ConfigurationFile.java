package br.unifesp.maritaca.persistence.util;

public class ConfigurationFile {
	
	private String mobilePath;
	private String androidSdkPath;
	private String scriptLocation;
	private String maritacaMobileFolder;
	private String maritacaMobileProjects;	
	private String uriServer;
	private String releaseScriptLocation;
	
	private String filesystemPath;
	private String httpUriServer;
	private String ffmpegPath;
	private String tmpDir;
	private String clusterAddr;
	private String populateTestDb; //Default value: Don't populate with test data
	
	private String fbWebApiKey;
	private String fbWebAppSecret;
	private String fbWebRedirect;
	private String fbMobApiKey;
	private String fbMobAppSecret;
	private String fbMobRedirect;
	private String fbScope;
	
	private String mongoHost;
	private String mongoPort;
	private String mongoTimeout;
	
	public String getFbScope() {
		return fbScope;
	}
	public void setFbScope(String fbScope) {
		this.fbScope = fbScope;
	}
	public String getMobilePath() {
		return mobilePath;
	}
	public void setMobilePath(String mobilePath) {
		this.mobilePath = mobilePath;
	}
	public String getAndroidSdkPath() {
		return androidSdkPath;
	}
	public void setAndroidSdkPath(String androidSdkPath) {
		this.androidSdkPath = androidSdkPath;
	}
	public String getScriptLocation() {
		return scriptLocation;
	}
	public void setScriptLocation(String scriptLocation) {
		this.scriptLocation = scriptLocation;
	}
	public String getMaritacaMobileFolder() {
		return maritacaMobileFolder;
	}
	public void setMaritacaMobileFolder(String maritacaMobileFolder) {
		this.maritacaMobileFolder = maritacaMobileFolder;
	}
	public String getMaritacaMobileProjects() {
		return maritacaMobileProjects;
	}
	public void setMaritacaMobileProjects(String maritacaMobileProjects) {
		this.maritacaMobileProjects = maritacaMobileProjects;
	}
	public String getUriServer() {
		return uriServer;
	}
	public void setUriServer(String uriServer) {
		this.uriServer = uriServer;
	}
	public String getReleaseScriptLocation() {
		return releaseScriptLocation;
	}
	public void setReleaseScriptLocation(String releaseScriptLocation) {
		this.releaseScriptLocation = releaseScriptLocation;
	}
	
	public String getFfmpegPath() {
		return ffmpegPath;
	}
	public void setFfmpegPath(String ffmpegPath) {
		this.ffmpegPath = ffmpegPath;
	}
	public String getTmpDir() {
		return tmpDir;
	}
	public void setTmpDir(String tmpDir) {
		this.tmpDir = tmpDir;
	}
	public String getFilesystemPath() {
		return filesystemPath;
	}
	public void setFilesystemPath(String filesystemPath) {
		this.filesystemPath = filesystemPath;
	}
	public String getHttpUriServer() {
		return httpUriServer;
	}
	public void setHttpUriServer(String httpUriServer) {
		this.httpUriServer = httpUriServer;
	}
	public String getClusterAddr() {
		return clusterAddr;
	}
	public void setClusterAddr(String clusterAddr) {
		this.clusterAddr = clusterAddr;
	}
	public String getPopulateTestDb() {
		return populateTestDb;
	}
	public void setPopulateTestDb(String populateTestDb) {
		this.populateTestDb = populateTestDb;
	}
	public String getFbWebApiKey() {
		return fbWebApiKey;
	}
	public void setFbWebApiKey(String fbWebApiKey) {
		this.fbWebApiKey = fbWebApiKey;
	}
	public String getFbWebAppSecret() {
		return fbWebAppSecret;
	}
	public void setFbWebAppSecret(String fbWebAppSecret) {
		this.fbWebAppSecret = fbWebAppSecret;
	}
	public String getFbWebRedirect() {
		return fbWebRedirect;
	}
	public void setFbWebRedirect(String fbWebRedirect) {
		this.fbWebRedirect = fbWebRedirect;
	}
	public String getFbMobApiKey() {
		return fbMobApiKey;
	}
	public void setFbMobApiKey(String fbMobApiKey) {
		this.fbMobApiKey = fbMobApiKey;
	}
	public String getFbMobAppSecret() {
		return fbMobAppSecret;
	}
	public void setFbMobAppSecret(String fbMobAppSecret) {
		this.fbMobAppSecret = fbMobAppSecret;
	}
	public String getFbMobRedirect() {
		return fbMobRedirect;
	}
	public void setFbMobRedirect(String fbMobRedirect) {
		this.fbMobRedirect = fbMobRedirect;
	}
	public String getMongoHost() {
		return mongoHost;
	}
	public void setMongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}
	public String getMongoPort() {
		return mongoPort;
	}
	public void setMongoPort(String mongoPort) {
		this.mongoPort = mongoPort;
	}
	public String getMongoTimeout() {
		return mongoTimeout;
	}
	public void setMongoTimeout(String mongoTimeout) {
		this.mongoTimeout = mongoTimeout;
	}
}