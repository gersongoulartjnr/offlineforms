package br.unifesp.maritaca.business.answer.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This DTO is used to represent one collect of the form
 * containing one answer per question.  
 * @author tiagobarabasz
 */
@XmlRootElement
public class AnswerDTO {
	
	private String key;	
	private String author;	
	private Date collectDate;
	private Long creationDate;
	private String strCreationDate;	
	private String url;
	private List<QuestionAnswerDTO> answers;
	private String userKey;

	@XmlAttribute(name="email")
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public Date getCollectDate() {
		return collectDate;
	}
	public void setCollectDate(Date collectDate) {
		this.collectDate = collectDate;
	}
	
	@XmlElement(name="questionAnswer")
	public List<QuestionAnswerDTO> getAnswers() {
		return answers;
	}
	public void setAnswers(List<QuestionAnswerDTO> answers) {
		this.answers = answers;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@XmlTransient
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@XmlAttribute(name="date")
	public String getStrCreationDate() {
		return strCreationDate;
	}
	public void setStrCreationDate(String strCreationDate) {
		this.strCreationDate = strCreationDate;
	}
	
	@XmlTransient
	public Long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}
	
	@XmlTransient
	public String getUserKey() {
		return userKey;
	}
	
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}	
}