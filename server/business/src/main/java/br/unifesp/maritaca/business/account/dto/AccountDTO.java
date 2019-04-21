package br.unifesp.maritaca.business.account.dto;

import java.io.Serializable;

public class AccountDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;	
	private String group;	
	private String email;	
	private String firstName;	
	private String lastName;
	private String password;	
	private String passwordConfirmation;	
	private String captchaCode;
	private String captchaValue;	
	private String currentPassword;
	private String loggedBefore;
	private String formId;
	
	private String contextPath;
	private String mobClientId;
	
	private String curPassEncoded;
	private String passEncoded;
	private String resetPassCode;	

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCaptchaCode() {
		return captchaCode;
	}
	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}	
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}
	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
	public String getCaptchaValue() {
		return captchaValue;
	}
	public void setCaptchaValue(String captchaValue) {
		this.captchaValue = captchaValue;
	}
	public String getMobClientId() {
		return mobClientId;
	}
	public void setMobClientId(String mobClientId) {
		this.mobClientId = mobClientId;
	}	

	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public String getLoggedBefore() {
		return loggedBefore;
	}
	public void setLoggedBefore(String loggedBefore) {
		this.loggedBefore = loggedBefore;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getResetPassCode() {
		return resetPassCode;
	}
	public void setResetPassCode(String resetPassCode) {
		this.resetPassCode = resetPassCode;
	}
	public String getPassEncoded() {
		return passEncoded;
	}
	public void setPassEncoded(String passEncoded) {
		this.passEncoded = passEncoded;
	}
	public String getCurPassEncoded() {
		return curPassEncoded;
	}
	public void setCurPassEncoded(String curPassEncoded) {
		this.curPassEncoded = curPassEncoded;
	}
	public String retrieveFullName() {
		String fullName = "";
		if(getFirstName()!=null){
			fullName += getFirstName();
		}
		if(getLastName()!=null){
			fullName += " " + getLastName();
		}
		return fullName;
	}
}