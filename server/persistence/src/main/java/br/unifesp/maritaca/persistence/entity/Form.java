package br.unifesp.maritaca.persistence.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hom.annotations.Column;

import br.unifesp.maritaca.persistence.annotation.Minimal;
import br.unifesp.maritaca.persistence.converter.BooleanConverter;
import br.unifesp.maritaca.persistence.converter.IntegerConverter;
import br.unifesp.maritaca.persistence.converter.JSONUUIDConverter;
import br.unifesp.maritaca.persistence.converter.PolicyConverter;
import br.unifesp.maritaca.persistence.converter.ListStringConverter;
import br.unifesp.maritaca.persistence.converter.UUIDConverter;
import br.unifesp.maritaca.persistence.permission.Policy;

@Entity
@Table(name="Form")
public class Form {
	
	@Id
	private UUID key;

	@Column(name="lists", converter=JSONUUIDConverter.class)
	private List<UUID> lists; //lists of MaritacaList
	
	@Column(name="tags", converter=JSONUUIDConverter.class)
	private List<UUID> tags; //lists of Tags
	
	@Column(name="xml")
	private String xml;

	@Minimal(indexed=true)
	@Column(name="user", converter = UUIDConverter.class)
	private UUID user;

	@Minimal
	@Column(name="title")
	private String title;
	
	@Minimal(indexed=true)
	@Column(name="url")
	private String url;
	
	@Minimal
	@Column(name="description")
	private String description;
	
	@Minimal
	@Column(name="icon")
	private String icon;
	
	@Minimal(indexed=true)
	@Column(name="policy", converter=PolicyConverter.class)
	private Policy policy = Policy.PRIVATE;

	@Column(name="numberOfCollects", converter=IntegerConverter.class)
	private Integer numberOfCollects;
	
	@Column(name="collectors", converter=ListStringConverter.class)
	private List<String> collectors; //lists of collectors
	
	@Column(name="buildingApk", converter=BooleanConverter.class)
	private Boolean buildingApk;
	
	public UUID getKey() {
		return key;
	}
	
	private MaritacaDate creationDate;
	
	public Form() {	}

	public Form(UUID key, String xml, String title, String description) {
		this.key = key;
		this.xml = xml;
		this.title = title;
		this.description = description;
	}

	public void setKey(UUID key) {
		this.key = key;
		if(key!=null){
			long dl = TimeUUIDUtils.getTimeFromUUID(getKey());
			MaritacaDate date = new MaritacaDate();
			date.setTime(dl);
			creationDate = date;
		}
	}

	public void setKey(String ks) {
		setKey(UUID.fromString(ks));		
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
	
	public MaritacaDate getCreationDate() {
		return creationDate;
	}

	@Override
	public String toString() {
		if (getKey() != null) {
			return getKey().toString();
		}
		return super.toString();
	}

	public String getTitle() {
		if (title == null && getKey() != null) {
			return getKey().toString();
		}
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
	
	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = Policy.getPolicyFromString(policy);
	}
	
	public void setPolicy(Policy policy) {
		this.policy = policy;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public UUID getUser() {
		return user;
	}

	public void setUser(UUID user) {
		this.user = user;
	}
	
	public Boolean isPublic() {
        return policy.getId() == Policy.PUBLIC.getId();
    }
	
	/**
	 * Policy can be changed only when it is PRIVATE
	 * @return
	 */
	public Boolean isPrivate() {
        return policy.getId() == Policy.PRIVATE.getId();
    }
	
	public Boolean isShared() {
        return policy.getId() == Policy.SHARED_HIERARCHICAL.getId() || 
        		policy.getId() == Policy.SHARED_SOCIAL.getId();
    }
	
	public Map<String, Policy> getPolicyItems(){
		Map<String, Policy> policyItems = new LinkedHashMap<String, Policy>(); 
		for(Policy p : Policy.values()){
			policyItems.put(p.toString(), p);
		}		
		return policyItems;
	}
	
	public Integer getNumberOfCollects() {
		return numberOfCollects;
	}
	
	public void setNumberOfCollects(Integer numberOfCollects) {
		this.numberOfCollects = numberOfCollects;
	}
	
	public void setBuildingApk(Boolean buildingApk) {
		this.buildingApk = buildingApk;
	}
	
	public Boolean getBuildingApk() {
		return buildingApk;
	}
	
	@Override
	public int hashCode() {
		if(getKey() == null){
			return super.hashCode();
		}
		return getKey().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Form){
			Form f = (Form)obj;
			if(getKey()!=null && f.getKey()!=null){
				return getKey().equals(f.getKey());
			}
		}
		return false;
	}

	public List<UUID> getTags() {
		return tags;
	}
	public void setTags(List<UUID> tags) {
		this.tags = tags;
	}

	public List<String> getCollectors() {
		return collectors;
	}
	public void setCollectors(List<String> collectors) {
		this.collectors = collectors;
	}	
}