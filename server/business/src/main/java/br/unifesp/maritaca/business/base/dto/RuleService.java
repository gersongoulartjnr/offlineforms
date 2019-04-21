package br.unifesp.maritaca.business.base.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.persistence.dao.ConfigurationDAO;
import br.unifesp.maritaca.persistence.dao.GroupDAO;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.permission.Accessor;
import br.unifesp.maritaca.persistence.permission.Document;
import br.unifesp.maritaca.persistence.permission.Permission;
import br.unifesp.maritaca.persistence.permission.Policy;
import br.unifesp.maritaca.persistence.permission.Rule;

@Service("ruleService")
public class RuleService {
	
	@Autowired private ConfigurationDAO configurationDAO;
	
	@Autowired private GroupDAO groupDAO;
	
	private Rule rules = Rule.getInstance();
	
	/**
	 * 
	 * @param lstUUID
	 * @param userUUID
	 * @return true if userUUID is into lstUUID
	 */
	protected Boolean isMemberOfTheList(List<UUID> lstUUID, UUID userUUID) {
		if(lstUUID != null && !lstUUID.isEmpty()) {
			for(UUID uuid : lstUUID) {
				MaritacaList mList = groupDAO.getMaritacaListByKey(uuid, false);
				if (mList != null && mList.getUsers() == null && userUUID.equals(mList.getOwner())) {
					return true;
				}
				if(mList != null && mList.getUsers() != null && !mList.getUsers().isEmpty()) {
					// TODO contains ??? 
					for(UUID us : mList.getUsers()) {
						if(us.toString().equals(userUUID.toString())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param obj
	 * @param userKey
	 * @return Permission
	 */
	public Permission getPermission(Form form, UUID userKey, Document doc) {
		if(form != null && (configurationDAO.isRootUser(userKey) || userKey.toString().equals(form.getUser().toString()))) {
			return rules.getPermission(form.getPolicy(), doc, Accessor.OWNER);
		}
		
		else if(form != null && isMemberOfTheList(form.getLists(), userKey)) {
			return rules.getPermission(form.getPolicy(), doc, Accessor.LIST);
		}
		
		else if(form != null && form.isPublic()) {
			return rules.getPermission(form.getPolicy(), doc, Accessor.ALL);
		}
		return null;
	}
	
	public Permission getPublicPermission(Document doc){
		return rules.getPermission(Policy.PUBLIC, doc, Accessor.ALL);
	}
	
	public Permission getSharePermission(Form form, Document doc){
		return rules.getPermission(form.getPolicy(), doc, Accessor.LIST);
	}

	public SetPermissions getPermission(Form form, UUID userKey) {
		SetPermissions permissions = new SetPermissions();
		// root user or owner
		if(form != null && (configurationDAO.isRootUser(userKey) || userKey.toString().equals(form.getUser().toString()))) { 
			permissions.setFormPermission(rules.getPermission(form.getPolicy(), Document.FORM, Accessor.OWNER));
			permissions.setAswPermission(rules.getPermission(form.getPolicy(), Document.ANSWER, Accessor.OWNER));
			permissions.setRepoPermission(rules.getPermission(form.getPolicy(), Document.REPORT, Accessor.OWNER));
			return permissions;
		}
		// shared hierarchical or social
		else if(form != null && isMemberOfTheList(form.getLists(), userKey)) {
			permissions.setFormPermission(rules.getPermission(form.getPolicy(), Document.FORM, Accessor.LIST));
			permissions.setAswPermission(rules.getPermission(form.getPolicy(), Document.ANSWER, Accessor.LIST));
			permissions.setRepoPermission(rules.getPermission(form.getPolicy(), Document.REPORT, Accessor.LIST));
			return permissions;
		}
		// public
		else if(form != null && form.isPublic()) {
			permissions.setFormPermission(rules.getPermission(form.getPolicy(), Document.FORM, Accessor.ALL));
			permissions.setAswPermission(rules.getPermission(form.getPolicy(), Document.ANSWER, Accessor.ALL));
			permissions.setRepoPermission(rules.getPermission(form.getPolicy(), Document.REPORT, Accessor.ALL));
			return permissions;
		}
		return null;
	}
	
	public SetPermissions getPermissionsByPolicy(Policy policy) {
		SetPermissions permissions = new SetPermissions();
		switch (policy) {
			case PUBLIC:
				permissions.setFormPermission(rules.getPermission(policy, Document.FORM, Accessor.ALL));
				permissions.setAswPermission(rules.getPermission(policy, Document.ANSWER, Accessor.ALL));
				permissions.setRepoPermission(rules.getPermission(policy, Document.REPORT, Accessor.ALL));
				return permissions;
			case SHARED_SOCIAL:
			case SHARED_HIERARCHICAL:
				permissions.setFormPermission(rules.getPermission(policy, Document.FORM, Accessor.LIST));
				permissions.setAswPermission(rules.getPermission(policy, Document.ANSWER, Accessor.LIST));
				permissions.setRepoPermission(rules.getPermission(policy, Document.REPORT, Accessor.LIST));
				return permissions;
			default:
				throw new MaritacaException("Invalid Policy to share");
		}
	}
}
