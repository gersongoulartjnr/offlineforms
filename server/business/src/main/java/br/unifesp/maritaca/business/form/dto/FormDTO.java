package br.unifesp.maritaca.business.form.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.gson.Gson;

import br.unifesp.maritaca.persistence.permission.Permission;
import br.unifesp.maritaca.persistence.permission.Policy;
import br.unifesp.maritaca.business.analytics.dto.AnalyticsDTO;
import br.unifesp.maritaca.business.base.dto.BaseDTO;
import br.unifesp.maritaca.business.report.dto.SimpleReportDTO;

@XmlRootElement(name = "form")
public class FormDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private UUID key;
	
	private String xml;
	
	private String title;
	
	private String url;
	
	private Policy policy;
	
	private String strPolicy;
	
	private String owner; // fullname
	
	private String creationDate;
	
	private String user;
	
	private List<UUID> lists;
	private String groupsList;
	
	private Permission formPermission;
	
	private Permission answerPermission;
	
	private Permission reportPermission;
	
	private String description;
	
	private CommonsMultipartFile iconFile;
	
	private String image;
	
	private boolean like;
	
	private boolean dislike;
	
	private int numLikes;
	
	private int numDislikes;

	private Integer numberOfCollects;
	
	private List<SimpleReportDTO> reports;
	
	private List<AnalyticsDTO> analytics;
	
	private String tags;
	
	public FormDTO() { }
	
	public FormDTO(UUID key, String title, String owner, String url, String creationDate, String strPolicy, 
			Integer numberOfCollects) {
		this.key = key;
		this.title = title;
		this.owner = owner;
		this.url = url;
		this.creationDate = creationDate;
		this.strPolicy = strPolicy;
		this.numberOfCollects = (numberOfCollects == null ? 0 : numberOfCollects);
	}

	public FormDTO(UUID key, String title, String owner, String url, String creationDate, String strPolicy, 
					Permission fPermission, Permission aPermission, Permission rPermission, 
					Integer numberOfCollects, List<SimpleReportDTO> reports) {
		setKey(key);
		setTitle(title);
		setOwner(owner);
		setUrl(url);
		setCreationDate(creationDate);
		setStrPolicy(strPolicy);
		setFormPermission(fPermission);
		setAnswerPermission(aPermission);
		setReportPermission(rPermission);
		setNumberOfCollects((numberOfCollects== null) ? 0 : numberOfCollects);
		setReports(reports);
	}
	
	public FormDTO(String url) {
		this.url = url;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}
	
	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@XmlTransient
	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	@XmlElement(name = "id")
	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}

	public Map<String, Policy> getPolicyItems(){
		Map<String, Policy> policyItems = new LinkedHashMap<String, Policy>(); 
		for(Policy p : Policy.values()){
			policyItems.put(p.toString(), p);
		}
		return policyItems;
	}

	public List<UUID> getLists() {
		return lists;
	}

	public void setLists(List<UUID> lists) {
		this.lists = lists;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@XmlTransient
	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

	public boolean isDislike() {
		return dislike;
	}

	public void setDislike(boolean dislike) {
		this.dislike = dislike;
	}

	public int getNumLikes() {
		return numLikes;
	}

	public void setNumLikes(int numLikes) {
		this.numLikes = numLikes;
	}

	public int getNumDislikes() {
		return numDislikes;
	}

	public void setNumDislikes(int numDislikes) {
		this.numDislikes = numDislikes;
	}
	
	@XmlTransient
	public String getStrPolicy() {
		return strPolicy;
	}

	public void setStrPolicy(String strPolicy) {
		this.strPolicy = strPolicy;
	}

	@XmlTransient
	public CommonsMultipartFile getIconFile() {
		return iconFile;
	}

	public void setIconFile(CommonsMultipartFile iconFile) {
		this.iconFile = iconFile;
	}

	public String getGroupsList() {
		return groupsList;
	}

	public void setGroupsList(String groupsList) {
		this.groupsList = groupsList;
	}

	public Integer getNumberOfCollects() {
		return numberOfCollects;
	}

	public void setNumberOfCollects(Integer numberOfCollects) {
		this.numberOfCollects = numberOfCollects;
	}	
	
	@XmlTransient
	public List<SimpleReportDTO> getReports() {
		return reports;
	}

	public void setReports(List<SimpleReportDTO> reports) {
		this.reports = reports;
	}

	@XmlTransient
	public Permission getFormPermission() {
		return formPermission;
	}

	public void setFormPermission(Permission formPermission) {
		this.formPermission = formPermission;
	}

	@XmlTransient
	public Permission getAnswerPermission() {
		return answerPermission;
	}

	public void setAnswerPermission(Permission answerPermission) {
		this.answerPermission = answerPermission;
	}

	@XmlTransient
	public Permission getReportPermission() {
		return reportPermission;
	}

	public void setReportPermission(Permission reportPermission) {
		this.reportPermission = reportPermission;
	}

	public String toJSON() {
		return (new Gson()).toJson(this);
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public List<AnalyticsDTO> getAnalytics() {
		return analytics;
	}
	public void setAnalytics(List<AnalyticsDTO> analytics) {
		this.analytics = analytics;
	}
}