package br.unifesp.offlineforms.mobile.dataaccess.model;

public class MaritacaLog {
	
	private String userEmail;
	private String message;
	private Long timestamp;
	private String androidVersion;
	
	public MaritacaLog() {}
	public MaritacaLog(String userEmail, String message, Long timestamp, String androidVersion) {
		this.userEmail = userEmail;
		this.message = message;
		this.timestamp = timestamp;
		this.androidVersion = androidVersion;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAndroidVersion() {
		return androidVersion;
	}
	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}