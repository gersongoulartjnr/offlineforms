package br.unifesp.maritaca.persistence.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;

import br.unifesp.maritaca.persistence.annotation.Minimal;
import br.unifesp.maritaca.persistence.converter.UUIDConverter;
import br.unifesp.maritaca.persistence.util.ConstantsPersistence;

@Entity
@Table(name="User")
public class User implements Serializable, Cloneable{

	private static final long serialVersionUID = 1L;

	@Id
	private UUID key;
	
	@Minimal
	@Column(name="firstname")
	private String firstname;
	
	@Column(name="lastname")
	private String lastname;
	
	@Minimal
	@Column(name="maritacaList", converter=UUIDConverter.class)
	private UUID maritacaList;
	
	@Minimal(indexed=true)
	@Column(name="email")
	private String email;
	
	@Column(name="password")
	private String password;
	
	@Column(name="enabled")	
	private String enabled;
	
	@Column(name="loggedBefore")
	private String loggedBefore;
	
	@Column(name="reportToken")
	private String reportToken;
	
	@Minimal(indexed=true)
	@Column(name="activationCode")
	private String activationCode;
	
	//@Minimal(ttl=true)
	@Column(name="resetPassCode")
	private String resetPassCode;
	
	private Integer resetPassCodeTTL = ConstantsPersistence.RESET_PASS_EXPIRATION_DATE;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}
	
	public void setKey(String ks){
		this.key = UUID.fromString(ks);
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}	

	@Deprecated
	public UUID getMaritacaList() {
		return maritacaList;
	}

	@Deprecated
	public void setMaritacaList(UUID maritacaList) {
		this.maritacaList = maritacaList;
	}
	
	public void setMaritacaList(String maritacaList){
		this.maritacaList = UUID.fromString(maritacaList);
	}
	
	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
	@Override
	public String toString() {
		if(getKey()!=null){
			return getKey().toString();
		}
		return super.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(obj instanceof User){
			User user = (User)obj;
			return user.getKey().equals(getKey());
		}
		return(obj.equals(getKey()));
	}
	
	@Override
	public int hashCode() {
		if(key==null){
			return super.hashCode();
		}else{
			return key.hashCode();
		}
	}
	
	public String getFullName() {
		return getFirstname() + " " + (getLastname()!=null?getLastname():"");
	}

	public String getLoggedBefore() {
		return loggedBefore;
	}

	public void setLoggedBefore(String loggedBefore) {
		this.loggedBefore = loggedBefore;
	}

	public String getReportToken() {
		return reportToken;
	}

	public void setReportToken(String reportToken) {
		this.reportToken = reportToken;
	}

	public Integer getResetPassCodeTTL() {
		return resetPassCodeTTL;
	}

	public void setResetPassCodeTTL(Integer resetPassCodeTTL) {
		this.resetPassCodeTTL = resetPassCodeTTL;
	}

	public String getResetPassCode() {
		return resetPassCode;
	}

	public void setResetPassCode(String resetPassCode) {
		this.resetPassCode = resetPassCode;
	}
}