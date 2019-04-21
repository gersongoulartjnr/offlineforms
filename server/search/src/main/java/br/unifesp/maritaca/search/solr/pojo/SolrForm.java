package br.unifesp.maritaca.search.solr.pojo;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

public class SolrForm {
	
	@Field("form_url")
	private String url;
	
	@Field("form_title")
	private String title;
	
	@Field("form_description")
	private String description;
	
	@Field("form_policy")
	private int policy;
	
	@Field("form_owner")
	private String owner;
	
	@Field("form_users")
	private String[] users;
	
	@Field("form_tags")
	private String[] tags;
	
	@Field("form_creation_date")
	private Date creationDate;
	
	public SolrForm() {}
	
	public SolrForm(String url, String title, String description, 
			int policy, String owner, String[] users, String[] tags, 
			Date creationDate) {
		this.url = url;
		this.title = title;
		this.description = description;
		this.policy = policy;
		this.owner = owner;
		this.users = users;
		this.tags = tags;
		this.creationDate = creationDate;
	}
	
	@Deprecated
	public SolrForm(String url, String title, String description, int policy) {
		this.url = url;
		this.title = title;
		this.description = description;
		this.policy = policy;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public int getPolicy() {
		return policy;
	}
	public void setPolicy(int policy) {
		this.policy = policy;
	}

	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String[] getUsers() {
		return users;
	}
	public void setUsers(String[] users) {
		this.users = users;
	}

	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}