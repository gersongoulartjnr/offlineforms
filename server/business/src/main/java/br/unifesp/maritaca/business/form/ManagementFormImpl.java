package br.unifesp.maritaca.business.form;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.analytics.ManagementAnalytics;
import br.unifesp.maritaca.business.analytics.dto.AnalyticsDTO;
import br.unifesp.maritaca.business.answer.CreateAnswersXml;
import br.unifesp.maritaca.business.answer.ManagementAnswers;
import br.unifesp.maritaca.business.answer.dto.AnswerDTO;
import br.unifesp.maritaca.business.answer.dto.AnswerListerDTO;
import br.unifesp.maritaca.business.answer.dto.QuestionAnswerDTO;
import br.unifesp.maritaca.business.answer.dto.ZipFileDTO;
import br.unifesp.maritaca.business.base.dto.AbstractBusiness;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.RuleService;
import br.unifesp.maritaca.business.base.dto.SetPermissions;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.base.dto.WrapperGrid;
import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.enums.OrderFormBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.exception.AuthorizationDenied;
import br.unifesp.maritaca.business.exception.AuthorizationDeniedAlternative;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.form.dto.FormWSDTO;
import br.unifesp.maritaca.business.form.dto.ImportFormDTO;
import br.unifesp.maritaca.business.form.dto.VoteDTO;
import br.unifesp.maritaca.business.group.ManagementGroup;
import br.unifesp.maritaca.business.messaging.ManagementMessage;
import br.unifesp.maritaca.business.messaging.dto.EmailDTO;
import br.unifesp.maritaca.business.report.ManagementReport;
import br.unifesp.maritaca.business.report.dto.SimpleReportDTO;
import br.unifesp.maritaca.business.tag.ManagementTag;
import br.unifesp.maritaca.business.tag.TagsWrapper;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.business.util.UtilsBusiness;
import br.unifesp.maritaca.persistence.dao.ConfigurationDAO;
import br.unifesp.maritaca.persistence.dao.FormAccessRequestDAO;
import br.unifesp.maritaca.persistence.dao.FormAccessibleByListDAO;
import br.unifesp.maritaca.persistence.dao.FormDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.dao.VoteByFormDAO;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessRequest;
import br.unifesp.maritaca.persistence.entity.Tag;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.entity.VoteByForm;
import br.unifesp.maritaca.persistence.permission.Document;
import br.unifesp.maritaca.persistence.permission.Operation;
import br.unifesp.maritaca.persistence.permission.Permission;
import br.unifesp.maritaca.persistence.permission.Policy;
import br.unifesp.maritaca.persistence.permission.RequestStatusType;
import br.unifesp.maritaca.persistence.util.ConfigurationFile;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;
import br.unifesp.maritaca.search.ManagementSearch;
import br.unifesp.maritaca.search.solr.pojo.SolrForm;
import br.unifesp.maritaca.search.solr.pojo.WrapperForms;

import com.google.gson.Gson;

@Service("managementForm")
public class ManagementFormImpl extends AbstractBusiness implements ManagementForm {
	
	private static Logger logger = Logger.getLogger(ManagementFormImpl.class);
	
	@Autowired private ManagementAnswers managementAnswers;
	@Autowired private ManagementGroup 	 managementGroup;
	@Autowired private ManagementMessage managementMessage;
	@Autowired private ApkBuilder 		 apkBuilder;	
	@Autowired private RuleService 		 ruleService;
	@Autowired private ManagementReport  managementReport;
	@Autowired private ManagementAnalytics managementAnalytics;
	@Autowired private ManagementTag  	 managementTag;
	@Autowired private ManagementSearch<SolrForm> managementSolrSearch;
	
	@Autowired private UserDAO 					userDAO;
	@Autowired private FormDAO 					formDAO;
	@Autowired private FormAccessibleByListDAO 	formByListDAO;
	@Autowired private VoteByFormDAO 			voteByFormDAO;
	@Autowired private FormAccessRequestDAO 	formAccessRequestDAO;	
	@Autowired private ConfigurationDAO configurationDAO;
	
	@Deprecated private Boolean buildApk = true;
		
	@Override
	public WrapperGrid<FormDTO> getOwnFormsByUser(UserDTO userDTO, OrderFormBy orderBy,
			OrderType orderType, int page, int numRows) {
		
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null) {
			logger.error(userDTO.getUsername() + " User does not exist");
			return null;
		}		
		List<Form> forms = formDAO.getListOwnFormsByUserKey(user.getKey().toString());
		if(forms == null || forms.isEmpty()) {
			return null;
		}
		List<FormDTO> formsDTO = new ArrayList<FormDTO>();        	
        for(Form form : forms) {
        	FormDTO formDTO = new FormDTO(form.getKey(), form.getTitle(), user.getFullName(), form.getUrl(), 
					                    form.getCreationDate().toString(), form.getPolicy().toString(), form.getNumberOfCollects());
        	formsDTO.add(formDTO);
        }
		
		int formsSize = formsDTO.size();
		formsDTO = truncateFormsList(formsDTO, orderBy, orderType, page, numRows);
		formsDTO = getListCompleteOwnForms(formsDTO, userDTO);
		return getWrapperForms(formsDTO, orderBy, orderType, page, numRows, formsSize, null);
	}

	@Override
	public WrapperGrid<FormDTO> getSharedFormsByUser(UserDTO userDTO, OrderFormBy orderBy,
			OrderType orderType, int page,	int numRows) {
		List<FormDTO> listSharedForms = getListSharedForms(userDTO);
		int formsSize = (listSharedForms == null ? 0 : listSharedForms.size());
		listSharedForms = truncateFormsList(listSharedForms, orderBy, orderType, page, numRows);
		return getWrapperForms(listSharedForms, orderBy, orderType, page, numRows, formsSize, null);
	}
	
	private List<FormDTO> truncateFormsList(List<FormDTO> formsDTO, OrderFormBy orderBy, OrderType orderType, int page, int numRows){
		if(formsDTO == null) {
			return null;
		} else {
			UtilsBusiness.sortGrid(formsDTO, orderBy, orderType);
			return UtilsBusiness.pagingGrid(formsDTO, page, numRows);
		}
	}
	
	private WrapperGrid<FormDTO> getWrapperForms(List<FormDTO> forms, OrderFormBy orderBy,
			OrderType orderType, int page, int numRows, int totalRows, String search) {
		
		WrapperGrid<FormDTO> wrapper = new WrapperGrid<FormDTO>();
		wrapper.setRows(forms);
		wrapper.setNumRows(numRows);
		wrapper.setCurrentNumRows((forms == null ? 0 : forms.size()));
		wrapper.setCurrentPage(getPage(page));
		wrapper.setOrderBy(orderBy.getDescription());
		wrapper.setOrderType(orderType.getDescription());
		wrapper.setNumPages(getTotalNumPages(totalRows, numRows));
		wrapper.setTotal(totalRows);
		wrapper.setTxtSearch(search);
		
		return wrapper;		
	}
	
	private List<FormDTO> getListCompleteOwnForms(List<FormDTO> listOwnForms, UserDTO userDTO){
		Permission permission = new Permission(Operation.READ, Operation.UPDATE, Operation.DELETE, Operation.SHARE); 
		for(FormDTO dto : listOwnForms){
			dto.setFormPermission(permission);
			dto.setAnswerPermission(permission);
			dto.setReportPermission(permission);
			dto.setReports(getReportsByFormAndByUser(dto.getKey().toString(), userDTO));
			dto.setAnalytics(getAnalyticsByFormAndByUser(dto.getKey().toString(), userDTO));
		}		
		return listOwnForms;
	}
	
	private List<AnalyticsDTO> getAnalyticsByFormAndByUser(String formKey, UserDTO userDTO) {
		return managementAnalytics.findAnalyticsByFormAndByUser(formKey, userDTO);
	}
	
	private List<AnalyticsDTO> getAnalyticsByForm(String formKey, UserDTO userDTO) {
		return managementAnalytics.findAnalyticsByForm(formKey, userDTO);
	}
	
	private List<SimpleReportDTO> getReportsByFormAndByUser(String formKey, UserDTO userDTO) {
		return managementReport.findReportsByFormAndByUser(formKey, userDTO);
	}
	
	private List<SimpleReportDTO> getReportsByForm(String formKey, UserDTO userDTO) {
		return managementReport.findReportsByForm(formKey, userDTO);
	}

	private List<FormDTO> getListSharedForms(UserDTO userDTO) {
		List<FormDTO> formsDTO = null;
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null) {
			logger.error(userDTO.getUsername() + " User does not exist");
			return formsDTO;
		}
		
		List<Form> forms = formDAO.getAllSharedFormsByUserKey(user.getKey().toString());
		if(forms == null || forms.isEmpty()) {
			return formsDTO;
		}

		formsDTO = new ArrayList<FormDTO>();
        for(Form form : forms) {
        	if(form != null && !user.getKey().toString().equals(form.getUser().toString())) {
        		SetPermissions permissions = ruleService.getPermission(form, user.getKey());
            	if(permissions.getFormPermission() != null && permissions.getFormPermission().getRead()) {
            		User owner = userDAO.findUserByKey(form.getUser());
            		List<SimpleReportDTO> reports = null;
            		if(permissions.getRepoPermission().getRead() == true){
            			reports = getReportsByForm(form.getKey().toString(), userDTO);
            		}
            		FormDTO formDTO = new FormDTO(
                            form.getKey(),
                            form.getTitle(), 
                            owner.getFullName(), 
                            form.getUrl(), 
                            form.getCreationDate().toString(), 
                            form.getPolicy().toString(),
                            permissions.getFormPermission(),
                            permissions.getAswPermission(),
                            permissions.getRepoPermission(),
                            form.getNumberOfCollects(),
                            reports);
                	formsDTO.add(formDTO);
            	}
        	}
        }
        return formsDTO;
	}
	
	/**
	 * Get an unique URL for a form 
	 * @return String
	 */
	private String getUniqueUrl() {
		// TODO: check if this random string is enough
		// maybe it is better to generate uuid-based string
		String url = UtilsPersistence.randomString();
		if (!formDAO.verifyIfUrlExist(url))
			return url;
		else
			return getUniqueUrl();
	}
	
	/**
	 * 
	 * @param obj
	 * @return Form or User
	 */
	private <T> Object verifyReturnNullValuesInDB(T obj) {
		if(obj instanceof FormDTO) {
			FormDTO formDTO = (FormDTO)obj;
			Form form = formDAO.getFormByKey(formDTO.getKey(), true);
			if(form == null) {
				throw new IllegalArgumentException("Form " +formDTO.getKey()+ " does not exist in database");
			}
			return form;
		}
		else if(obj instanceof UserDTO) {
			UserDTO userDTO = (UserDTO)obj;
			User user = userDAO.findUserByEmail(userDTO.getUsername());
			if(user == null) {
				throw new IllegalArgumentException("User does not exist in database");
			}
			return user;
		}
		return null;
	}
	
	@Override 
	public Message saveForm(UserDTO userDTO, FormDTO formDTO) {
		if(formDTO.getKey() == null) {
			return createForm(userDTO, formDTO);
		}
		else {
			return updateForm(userDTO, formDTO);
		}
	}
	
	private void mergeSolrForm(Form form, String username, List<String> users, List<String> tags){		
		SolrForm solrForm = new SolrForm();
		solrForm.setUrl(form.getUrl());
		
		solrForm.setTitle(form.getTitle());
		solrForm.setDescription(form.getDescription());
		solrForm.setPolicy(form.getPolicy().getId());
		solrForm.setOwner(username);
		
		if(form.getPolicy().equals(Policy.PUBLIC) || form.getPolicy().equals(Policy.PRIVATE) 
				|| users == null || users.isEmpty()){			
			solrForm.setUsers(null);
		} else{
			String[] usersArray = users.toArray(new String[users.size()]);
			solrForm.setUsers(usersArray);
		}
		
		if(tags == null || tags.isEmpty()){
			solrForm.setTags(null);
		} else{			
			String[] tagsArray = tags.toArray(new String[tags.size()]);
			solrForm.setTags(tagsArray);
		}
		solrForm.setCreationDate(form.getCreationDate());
		managementSolrSearch.saveForm(solrForm);		
	}
	
	private void deleteSolrForm(String idForm){
		managementSolrSearch.deleteForm(idForm);
	}

	private Message createForm(UserDTO userDTO, FormDTO formDTO) {
		Message message = new Message();
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null){
			message.setType(MessageType.ERROR);
			message.setMessage(getText("exception_internal_server_error"));
			return message;
		}
		
		Form form = new Form();
		form.setTitle(formDTO.getTitle());
		form.setXml(formDTO.getXml());
		form.setUrl(this.getUniqueUrl());
		form.setNumberOfCollects(0);
		String description = formDTO.getDescription();
		if(description != null && !"".equals(description.trim())) {
			form.setDescription(formDTO.getDescription());
		}
		if(formDTO.getIconFile() != null && formDTO.getIconFile().getSize() > 0) {
			form.setIcon(new String(Base64.encodeBase64(formDTO.getIconFile().getBytes())));
		}
		
		TagsWrapper tagsWrapper = managementTag.retrieveTagsWrapper(formDTO.getTags() == null ? "" : formDTO.getTags());		
		form.setTags(tagsWrapper.getKeys());
		
		form.setUser(user.getKey());
		formDAO.saveForm(form);
		managementTag.saveUserFormTag(form.getTags(), user.getKey(), form.getKey());
		
		if(form.getKey() != null) {
			formDTO.setUrl(form.getUrl());
			formDTO.setCreationDate(form.getCreationDate().toString());
			formDTO.setNumberOfCollects(form.getNumberOfCollects());
			Permission permission = new Permission(Operation.READ, Operation.UPDATE, Operation.DELETE, Operation.SHARE);     
			formDTO.setFormPermission(permission);
			formDTO.setAnswerPermission(permission);
			formDTO.setReportPermission(permission);
			formDTO.setStrPolicy(form.getPolicy().toString());
			message.setType(MessageType.SUCCESS);
			message.setMessage(getText("form_edit_save_success"));
			message.setData(formDTO);
			message.setExtra(true); // it means apk is building
			
			if(!isRunningTestEnvironment()){
				this.buildAPK(user, form);
				this.mergeSolrForm(form, user.getEmail(), null, tagsWrapper.getTexts());
			}
		}
		else {
			message.setType(MessageType.ERROR);
			message.setMessage(getText("form_edit_save_error"));
		}		
		return message;	
	}

	private boolean hasFormChanged(Form original, Form clone) {
		boolean result = false;
		if(!original.getTitle().equals(clone.getTitle())) {	
			result = true; 
		}
		if(!original.getXml().equals(clone.getXml())) { 
			result = true;
		}		
		String oriDescription = original.getDescription() == null ? "":original.getDescription();
		String cloDescription = clone.getDescription() == null ? "":clone.getDescription();		
		if(!oriDescription.equals(cloDescription)) { 
			result = true;
		}
		if(original.getIcon() != null && !"".equals(original.getIcon())) { 
			result = true;
		}
		return result;
	}
	
	private Message updateForm(UserDTO userDTO, FormDTO formDTO) {
		Message message = new Message();
		Form originalForm = (Form) verifyReturnNullValuesInDB(formDTO);
		Form cloneForm = new Form(originalForm.getKey(), originalForm.getXml(), originalForm.getTitle(), originalForm.getDescription());
		User user = (User) verifyReturnNullValuesInDB(userDTO);
		if(user != null && originalForm != null) {
			Permission permission = ruleService.getPermission(originalForm, user.getKey(), Document.FORM);
			if(permission != null && permission.getUpdate()) {
				originalForm.setUser(user.getKey());
				originalForm.setTitle(formDTO.getTitle());
				originalForm.setXml(formDTO.getXml());
				String description = formDTO.getDescription();
				if(description != null && !"".equals(description.trim())) {
					originalForm.setDescription(formDTO.getDescription());
				}
				if(formDTO.getIconFile() != null && formDTO.getIconFile().getSize() > 0) {
					originalForm.setIcon(new String(Base64.encodeBase64(formDTO.getIconFile().getBytes())));
				}
				
				TagsWrapper tagsWrapper = managementTag.retrieveTagsWrapper(formDTO.getTags() == null ? "" : formDTO.getTags());		
				managementTag.checkTagModifications(originalForm, tagsWrapper.getKeys());
				originalForm.setTags(tagsWrapper.getKeys());
				
				formDAO.saveForm(originalForm);
				if(!isRunningTestEnvironment()){
					this.mergeSolrForm(originalForm, user.getEmail(), getUsernamesList(originalForm.getLists()), tagsWrapper.getTexts());
				}
				
				if(originalForm.getKey() != null) {
					message.setType(MessageType.SUCCESS);
					message.setMessage(getText("form_edit_save_success"));
					message.setData(originalForm.getUrl());
					message.setExtra(false); // means apk is not building
					if(permission.getRead() && hasFormChanged(originalForm, cloneForm)) {
						if(!isRunningTestEnvironment()){
							this.buildAPK(user, originalForm);
							message.setExtra(true); // means apk is building
						}
					}					
				}
			}
			else {
				message.setType(MessageType.ERROR);
				message.setMessage(getText("exception_internal_server_error"));
				throw new AuthorizationDenied(Document.FORM, originalForm.getKey(), user.getKey(), Operation.UPDATE);
			}
		}
		else {
			message.setType(MessageType.ERROR);
			message.setMessage(getText("exception_internal_server_error"));
		}
		return message;
	}
	
	/**
	 * Update the Form with the new Policy
	 * @param formDTO
	 * @param userDTO
	 * @return
	 */
	public Message updateFormFromPolicyEditor(FormDTO formDTO, UserDTO userDTO) {
		Form originalForm = formDAO.getFormByUrl(formDTO.getUrl(), false);
		Message message = new Message();
		if (formDTO.getStrPolicy() == null && !originalForm.isShared()) {
			message.setType(MessageType.ERROR);
			message.setMessage(getText("form_share_error"));
			return message;
		}
		// setting policy
		if (formDTO.getStrPolicy() != null) {
			formDTO.setPolicy(Policy.getPolicyFromString(formDTO.getStrPolicy()));		
		}
		if (Policy.PRIVATE.equals(formDTO.getPolicy())) {
			return null;
		}
		// changing policy
		if(originalForm.getPolicy().equals(Policy.PRIVATE)) { // change Policy
			originalForm.setPolicy(formDTO.getPolicy());
		}
		// if policy is shared
		List<UUID> originalLists = new ArrayList<UUID>();
		if (originalForm.isShared() || Policy.SHARED_HIERARCHICAL.equals(formDTO.getPolicy())
				|| Policy.SHARED_SOCIAL.equals(formDTO.getPolicy())) {
			List<UUID> groupsFromDTO = managementGroup.getGroupsFromString(formDTO.getGroupsList());
			if (groupsFromDTO.isEmpty()) {
				message.setType(MessageType.ERROR);
				message.setMessage(getText("form_share_emptylist_error"));
				return message;
			}
			if(originalForm.getLists() != null) {
				originalLists.addAll(originalForm.getLists());
			}		
			if(originalForm.isShared()) {
				originalForm.setLists(groupsFromDTO);
			}
			message.setData(originalLists);
		}
		
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Permission permission = ruleService.getPermission(originalForm, user.getKey(), Document.FORM);
		if(permission != null && permission.getUpdate() && permission.getShare()) {
			if(originalForm.isShared()) {
				formByListDAO.createOrUpdateFormAccessible(originalForm, user, checkDeletedLists(originalLists, originalForm.getLists()));
			}
			formDAO.saveForm(originalForm);
			if(!isRunningTestEnvironment()){
				this.mergeSolrForm(originalForm, user.getEmail(), getUsernamesList(originalForm.getLists()), getTagsList(originalForm.getTags()));
			}
			message.setType(MessageType.SUCCESS);
			message.setMessage(getText("form_share_successful"));
			return message;
		} else {
			throw new AuthorizationDenied(Document.FORM, originalForm.getKey(), user.getKey(), Operation.UPDATE);
		}
	}
	
	private List<String> getUsernamesList(List<UUID> uuidUsersList) {
		if(uuidUsersList == null || uuidUsersList.isEmpty())
			return null;
		
		List<String> usernames = new ArrayList<String>();
		for(UUID groupKey: uuidUsersList){
			List<String> users = managementGroup.getUsernamesByGroupKey(groupKey);
			if(users != null && !users.isEmpty()){
				usernames.addAll(users);
			}
		}		
		return usernames;
	}
	
	private List<String> getTagsList(List<UUID> uuidTagsList){
		if(uuidTagsList == null || uuidTagsList.isEmpty())
			return null;
		
		List<String> tags = new ArrayList<String>();
		for(UUID tagKey : uuidTagsList){
			Tag tag = managementTag.getTagsByTagKey(tagKey);
			if(tag != null){
				tags.add(tag.getText());
			}
		}
		return tags;
	}

	/**
	 * Delete a Form
	 * @param formDTO
	 * @param userDTO
	 */
	//TODO delete answers?
	@Override
	public Message deleteForm(UserDTO userDTO, FormDTO formDTO) {
		if(formDTO == null || formDTO.getUrl() == null || "".equals(formDTO.getUrl())){ 
			return new Message(getText("form_edit_delete_error"), MessageType.ERROR);
		}
		return deleteFormByUrl(userDTO, formDTO.getUrl().toString()); 
	}
	
	@Override
	public Message deleteFormByUrl(UserDTO userDTO, String url) {
		Message message = new Message();
		User user = (User) verifyReturnNullValuesInDB(userDTO);
		Form originalForm = formDAO.getFormByUrl(url, false);
		
			Permission permission = ruleService.getPermission(originalForm, user.getKey(), Document.FORM);
			if(permission != null && permission.getRemove()) {
				formDAO.delete(originalForm);
				if(originalForm.getLists()!=null){
					formByListDAO.deleteFormAccessible(originalForm, user);
				}
				this.deleteSolrForm(originalForm.getUrl());
				message.setType(MessageType.SUCCESS);
				message.setMessage(getText("form_edit_delete_success"));
			} else {
				message.setType(MessageType.ERROR);
				message.setMessage(getText("form_edit_delete_error"));
				logger.error("Error deleting form: " + url + " user: " + userDTO.getUsername());
				//throw new AuthorizationDenied(Document.FORM, formKey, userKey, Operation.DELETE);
			}
		return message;
	}
	
	private List<UUID> checkDeletedLists(List<UUID> currentLists, List<UUID> newLists) {
		List<UUID> deletedLists = new ArrayList<UUID>();
		for(UUID current : currentLists) {
			if(!newLists.contains(current)) {
				deletedLists.add(current);
			}
		}
		return deletedLists;
	}


	@Override
	public FormDTO getFormDTOById(String key, UserDTO userDTO) {
		FormDTO formDTO = null;
		User user =  userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByKey(UUID.fromString(key), true);			
		if(user != null && form != null) {						
			Permission permission = ruleService.getPermission(form, user.getKey(), Document.FORM);
			if(permission != null && permission.getRead()) {
				formDTO = new FormDTO();
				formDTO.setKey(form.getKey());
				formDTO.setTitle(form.getTitle());
				formDTO.setUrl(form.getUrl());
				formDTO.setXml(form.getXml());
				formDTO.setUser(user.getFirstname() + " " + user.getLastname());
				return formDTO;
			}
			else {
				throw new AuthorizationDenied(Document.FORM, form.getKey(), user.getKey(), Operation.READ);
			}
		}
		throw new MaritacaException(userDTO.getUsername()+" doesn't exist or " + form.getKey() + " doesn't exist!!");
	}
	
	@Override
	public FormDTO getFormDTOByUrl(String url, UserDTO userDTO) {
		FormDTO formDTO = null;
		User user =  userDAO.findUserByEmail(userDTO.getUsername());
		if(user != null) {						
			Form form = formDAO.getFormByUrl(url, false);			
			SetPermissions permissions = ruleService.getPermission(form, user.getKey());
			if(permissions.getFormPermission() != null && permissions.getFormPermission().getRead()) {
				Integer[] voteByFormAndUser = this.getLikeAndDislikeByFormAndUser(form.getKey(), user.getKey());
				formDTO = new FormDTO();
				formDTO.setKey(form.getKey());
				formDTO.setTitle(form.getTitle());
				formDTO.setUrl(form.getUrl());
				formDTO.setXml(form.getXml());
				formDTO.setStrPolicy(form.getPolicy().toString());
				formDTO.setFormPermission(permissions.getFormPermission());
				formDTO.setAnswerPermission(permissions.getAswPermission());
				formDTO.setReportPermission(permissions.getRepoPermission());
				formDTO.setDescription(form.getDescription());
				formDTO.setLike(voteByFormAndUser[0] == 0?false:true);
				formDTO.setDislike(voteByFormAndUser[1] == 0?false:true);
				formDTO.setNumLikes(voteByFormAndUser[2]);
				formDTO.setNumDislikes(voteByFormAndUser[3]);
				formDTO.setTags(managementTag.retriveTagTextList(form.getTags() == null ? (new ArrayList<UUID>()) : form.getTags()));
				if(form.getIcon() != null) {
					formDTO.setImage("data:image/png;base64,"+form.getIcon());
				}
				return formDTO;
			}
			else {
				throw new AuthorizationDenied(Document.FORM, form.getKey(), user.getKey(), Operation.READ);
			}
		}
		else {
			logger.error(userDTO.getUsername()+" doesn't exist");
		}
		return formDTO;
	}
	
	/**
	 * This method returns the number of likes/dislikes
	 * 
	 * @param formKey form's key
	 * @param userKey user's key
	 * @return integer array, where: 	integer[0] represents like;
	 * 									integer[1] represents dislike;
	 *									integer[2] represents the number of likes;
	 *									integer[3] represents the number of dislikes. 
	 */
	private Integer[] getLikeAndDislikeByFormAndUser(UUID formKey, UUID userKey) {
		Integer[] vote = new Integer[4];
		vote[0] = vote[1] = vote[2] = vote[3] = 0;
		VoteByForm voteByForm = voteByFormDAO.findVoteFormByKey(formKey);
		if(voteByForm != null) {
			if(voteByForm.getUsersLike() != null) {
				vote[2] = voteByForm.getUsersLike().size();
				if(voteByForm.getUsersLike().contains(userKey)) {
					vote[0] = 1;
				}
			}
			if(voteByForm.getUsersDislike() != null) {
				vote[3] = voteByForm.getUsersDislike().size();
				if(voteByForm.getUsersDislike().contains(userKey)) {
					vote[1] = 1;
				}
			}
		}
		return vote;
	}

	@Override
	public Message importForm(ImportFormDTO importDTO) {
		//Using a XML File
		if(importDTO.isUseFile()){
			if("".equals(importDTO.getFileName()))
				return new Message(getText("err_xml_filename_required"), MessageType.ERROR);
			if("".equals(importDTO.getFileContent()))
				return new Message(getText("err_xml_filecontent_required"), MessageType.ERROR);
			
			return new Message(importDTO.getFileContent(), MessageType.SUCCESS);
			
		}
		//Using an URL
		else {
			if("".equals(importDTO.getUrl()))
				return new Message(getText("err_xml_url_required"), MessageType.ERROR);
			
			try {
				URL url = new URL(importDTO.getUrl());
				BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
				StringBuilder sb = new StringBuilder();
				String inputLine;
				while((inputLine = br.readLine()) != null) {
					sb.append(inputLine);
				}					
				br.close();
				return new Message(sb.toString(), MessageType.SUCCESS);
			} catch (IOException e) {
				logger.error("URL: " + importDTO.getUrl());
				return new Message(getText("exception_internal_server_error"), MessageType.ERROR);
			}
		}
	}

	@Override
	public Message downloadXML(UserDTO userDTO, String url) {
		User user =  userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null){
			return new Message();
		}
		Form form = formDAO.getFormByUrl(url, true);
		if(form == null){
			return new Message(getText("err_download_xml"), MessageType.ERROR);
		}
		Permission permission = ruleService.getPermission(form, user.getKey(), Document.FORM);
		if(permission != null && permission.getRead()) {
			Message message = new Message();
			message.setType(MessageType.SUCCESS);
			message.setData(form);
			return message;
		} else {
			return new Message(getText("err_download_xml"), MessageType.ERROR);
		}
	}
	
	@Override
	public Message downloadAPK(UserDTO userDTO, String url) {
		Message message = new Message();
		User user =  userDAO.findUserByEmail(userDTO.getUsername());
		if(user != null) {
			Form form = formDAO.getFormByUrl(url, true);
			if(form != null) {
				Permission permission = ruleService.getPermission(form, user.getKey(), Document.FORM);
				if (form.getBuildingApk()) {
					message.setType(MessageType.WARN);
					message.setMessage(getText("warn_building_apk"));
					return message;
				}
				if(permission != null && permission.getRead()) {
					String pathFileName = UtilsPersistence.retrieveConfigFile().getFilesystemPath() + File.separator + UtilsBusiness.buildApkPathFS(url);
					byte[] apkBytes = formDAO.getFileFromFilesystem(pathFileName);					
					if(apkBytes != null) {
						message.setType(MessageType.SUCCESS);
						message.setData(apkBytes);
					} else {
						message.setType(MessageType.ERROR);
						message.setMessage(getText("error_download_apk"));
						throw new MaritacaException("Error sending apk. File not found: " + pathFileName);
					}
				}
			}
			else {
				message.setType(MessageType.ERROR);
				message.setMessage(getText("error_download_apk"));
			}			
		}
		else {
			message.setType(MessageType.ERROR);
			message.setMessage(getText("error_download_apk"));
		}
		return message;
	}
	
	private void buildAPK(User user, Form form) {
		if(getBuildApk() == false){
			return;
		}				
		ConfigurationFile configFileDTO = UtilsPersistence.retrieveConfigFile();
	
		if(!"".equals(configFileDTO.getScriptLocation()) && !"".equals(configFileDTO.getMaritacaMobileFolder()) && 
				!"".equals(configFileDTO.getMaritacaMobileProjects()) && !"".equals(configFileDTO.getAndroidSdkPath()) &&
				!"".equals(configFileDTO.getUriServer()) && !"".equals(configFileDTO.getReleaseScriptLocation())) {
		
			FormDTO formDTO = this.getXmlAndFormDetails(form);
			apkBuilder.setFormDTO(formDTO);
			apkBuilder.setConfigFileDTO(configFileDTO);
			SimpleAsyncTaskExecutor sate = new SimpleAsyncTaskExecutor();
			sate.execute(apkBuilder);
		} else {
			logger.info("ERROR: invalid parameters in configuration properties!");
			throw new RuntimeException("ERROR: invalid parameters in configuration properties!");			
		}
	}	

	private FormDTO getXmlAndFormDetails(Form form) {
		FormDTO formDTO = new FormDTO();
		formDTO.setKey(form.getKey());
		formDTO.setUrl(form.getUrl());
		formDTO.setDescription(form.getDescription());
		formDTO.setTitle(form.getTitle());
		formDTO.setImage(form.getIcon());
		formDTO.setXml(getMarshalledFormFromXML(form));
		return formDTO;
	}
	
	private String getMarshalledFormFromXML(Form form) {
		try {
			FormWSDTO formWSDTO = getUnmarshalledFormFromXML(form);
			JAXBContext jaxbContext = JAXBContext.newInstance(FormWSDTO.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			StringWriter writer = new StringWriter(); 
			marshaller.marshal(formWSDTO, writer);
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public FormWSDTO getUnmarshalledFormFromXML(Form form) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(FormWSDTO.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();			
			InputStream is = new ByteArrayInputStream(form.getXml().getBytes(ConstantsBusiness.ENCODING_UTF8));			
			FormWSDTO formWSDTO = (FormWSDTO)unmarshaller.unmarshal(is);
			//formWSDTO.setUrl(form.getUrl());
			//formWSDTO.setKey(form.getKey());			
			return formWSDTO;			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public VoteDTO likeForm(UserDTO userDTO, String url) {//(UUID formKey, UUID userKey) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByUrl(url, true);
		if(user == null || form == null)
			return null;
			
		VoteByForm voteByForm = voteByFormDAO.findVoteFormByKey(form.getKey());
		VoteDTO vote = null;
		if(voteByForm != null) {
			if(voteByForm.getUsersLike() != null && !voteByForm.getUsersLike().contains(user.getKey())) {
				if(voteByForm.getUsersDislike() != null && voteByForm.getUsersDislike().contains(user.getKey())) {
					voteByForm.getUsersDislike().remove(user.getKey());
				}
				voteByForm.getUsersLike().add(user.getKey());
				voteByFormDAO.persist(voteByForm);
			}
			else if(voteByForm.getUsersLike() == null) {
				if(voteByForm.getUsersDislike() != null && voteByForm.getUsersDislike().contains(user.getKey())) {
					voteByForm.getUsersLike().remove(user.getKey());
				}
				List<UUID> likesByUser = new ArrayList<UUID>(1);
				likesByUser.add(user.getKey());
				voteByForm.setUsersLike(likesByUser);
				voteByFormDAO.persist(voteByForm);
			}
		} 
		else {			
			voteByForm = new VoteByForm();
			List<UUID> likesByUser = new ArrayList<UUID>(1);
			likesByUser.add(user.getKey());
			voteByForm.setKey(form.getKey());
			voteByForm.setUsersLike(likesByUser);
			voteByFormDAO.persist(voteByForm);
		}
		int numLikes = 0;
		int numDislikes = 0;
		if(voteByForm.getUsersLike() != null)
			numLikes = voteByForm.getUsersLike().size();
		if(voteByForm.getUsersDislike() != null)
			numDislikes = voteByForm.getUsersDislike().size();	
		vote = new VoteDTO(true, false, numLikes, numDislikes);
		return vote;
	}
	
	@Override
	public VoteDTO dislikeForm(UserDTO userDTO, String url) { //(UUID formKey, UUID userKey) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByUrl(url, true);
		if(user == null || form == null)
			return null;
		
		VoteByForm voteByForm = voteByFormDAO.findVoteFormByKey(form.getKey());
		VoteDTO vote = null;
		if(voteByForm != null) {
			if(voteByForm.getUsersDislike() != null && !voteByForm.getUsersDislike().contains(user.getKey())) {
				if(voteByForm.getUsersLike() != null && voteByForm.getUsersLike().contains(user.getKey())) {
					voteByForm.getUsersLike().remove(user.getKey());
				}				
				voteByForm.getUsersDislike().add(user.getKey());
				voteByFormDAO.persist(voteByForm);
			}
			else if(voteByForm.getUsersDislike() == null) {			
				if(voteByForm.getUsersLike() != null && voteByForm.getUsersLike().contains(user.getKey())) {
					voteByForm.getUsersLike().remove(user.getKey());
				}				
				List<UUID> dislikesByUser = new ArrayList<UUID>(1);
				dislikesByUser.add(user.getKey());
				voteByForm.setUsersDislike(dislikesByUser);
				voteByFormDAO.persist(voteByForm);
			}
		} 
		else {			
			voteByForm = new VoteByForm();
			List<UUID> dislikesByUser = new ArrayList<UUID>(1);
			dislikesByUser.add(user.getKey());
			voteByForm.setKey(form.getKey());
			voteByForm.setUsersDislike(dislikesByUser);
			voteByFormDAO.persist(voteByForm);
		}
		int numLikes = 0;
		int numDislikes = 0;
		if(voteByForm.getUsersLike() != null)
			numLikes = voteByForm.getUsersLike().size();
		if(voteByForm.getUsersDislike() != null)
			numDislikes = voteByForm.getUsersDislike().size();	
		vote = new VoteDTO(false, true, numLikes, numDislikes);
		return vote;
	}
	
	private EmailDTO getEmailData(String email, String url, String contextPath) {
		EmailDTO emailDTO = new EmailDTO();
		List<String> emails = new ArrayList<String>(1);
		emails.add(email);
		
		emailDTO.setSubject(getText("email_app_link_subject"));
		//email_app_link_content
		emailDTO.setContent("You can download the apk from: " + contextPath + "/form.html?get-app&id=" + url);
		emailDTO.setEmails(emails);
		
		return emailDTO;
	}

	@Override
	public Message sendAppLinkByEmail(UserDTO userDTO, String url, String contextPath) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByUrl(url, true);
		if(user == null || form == null)
			return null;
		else {
			Message message = new Message();
			message.setType(MessageType.SUCCESS);
			message.setMessage(getText("msg_download_link_sent"));
			managementMessage.sendMessage(getEmailData(user.getEmail(), url, contextPath));			
			return message;
		}
	}

	@Override
	public Message getApkByUrl(String url, UserDTO userDTO) {
		Message message = new Message();
		Form form = formDAO.getFormByUrl(url, true);
		String pathFileName = UtilsPersistence.retrieveConfigFile().getFilesystemPath() + File.separator + UtilsBusiness.buildApkPathFS(url);
		if(form.getPolicy().getId() == Policy.PUBLIC.getId()) {
			byte[] apkBytes = formDAO.getFileFromFilesystem(pathFileName);					
			if(apkBytes != null) {
				message.setType(MessageType.SUCCESS);
				message.setData(apkBytes);
			}
		}
		else {
			if(userDTO != null && userDTO.getUsername() != null && !"".equals(userDTO.getUsername().trim())) {
				User user =  userDAO.findUserByEmail(userDTO.getUsername());
				Permission permission = ruleService.getPermission(form, user.getKey(), Document.FORM);
				if(permission != null && permission.getRead()) {
					byte[] apkBytes = formDAO.getFileFromFilesystem(pathFileName);					
					if(apkBytes != null) {
						message.setType(MessageType.SUCCESS);
						message.setData(apkBytes);
					}
				}
				else {
					message.setType(MessageType.ERROR);
					message.setData(null);
				}
			}
			else {
				message.setType(MessageType.SUCCESS);
				message.setData(null);
			}
		}		
		return message;
	}
	
	@Override
	public Message getAndroidAppByUrl(String url) {
		Message message = new Message();
		String pathFileName = UtilsPersistence.retrieveConfigFile().getFilesystemPath() + File.separator + UtilsBusiness.buildApkPathFS(url);
		byte[] apkBytes = formDAO.getFileFromFilesystem(pathFileName);					
		if(apkBytes != null) {
			message.setType(MessageType.SUCCESS);
			message.setData(apkBytes);
		}
		else{
			message.setType(MessageType.ERROR);
			message.setData(null);
			message.setMessage(getText("error_download_apk"));
		}		
		return message;
	}

	public Boolean getBuildApk() {
		return buildApk;
	}

	public void setBuildApk(Boolean dontBuildApk) {
		this.buildApk = dontBuildApk;
	}

	@Override
	public ZipFileDTO getZipFileWithAnswers(String url, UserDTO userDTO) {
		ZipFileDTO zipFileDTO = new ZipFileDTO();
		Form form = formDAO.getFormByUrl(url, true);
		if(form != null) {
			try {
				AnswerListerDTO wrapper = managementAnswers.findAnswersDTO(form.getKey().toString(), userDTO);
				
		        if(!wrapper.getAnswers().isEmpty()) {		        	
		        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        	Writer w = new OutputStreamWriter(baos);
		        	w.append(getText("answer_list_author"));
		        	w.append(",");
		        	w.append(getText("answer_list_date"));
		        	w.append(",");
		        	for(String s : wrapper.getQuestions()) {
		        		w.append(s);
		        		w.append(",");		        		
		        	}
		        	w.append("\n");		        	
		        	for(AnswerDTO ans : wrapper.getAnswers()) {
		        		w.append(ans.getAuthor());
		        		w.append(",");
		        		w.append(ans.getStrCreationDate());
		        		w.append(",");
		        		for(QuestionAnswerDTO qa : ans.getAnswers()) {
		        			if(qa.getType().equals(ComponentType.CHECKBOX.getValue()) || qa.getType().equals(ComponentType.COMBOBOX.getValue()) || 
		        					qa.getType().equals(ComponentType.RADIOBOX.getValue())){
		        				
		        				HashMap<String, String> jsonValues = (new Gson()).fromJson(qa.getValue(), HashMap.class);
		        				for (Map.Entry<String, String> entry : jsonValues.entrySet()) {		        					
		        					w.append(entry.getValue());
		        					w.append("; ");
		        				}
		        			} else {
		        				w.append(qa.getValue());
		        			}
		        			w.append(",");
		        		}
		        		w.append("\n");
		        	}
		        	w.flush();        
		        	w.close();
		        	baos.flush();
		        	baos.close();
		        	zipFileDTO.setFilename(wrapper.getFormTitle());
		        	zipFileDTO.setData(baos);
		        }
		        else {
		        	zipFileDTO.setMessage(getText("answer_no_results"));
		        }
			} catch (IOException e) {
				zipFileDTO.setMessage(getText("error_unexpected"));
				logger.error("CSV file for form " + form.getKey() + " cannot be generated.");
			}
		}		
		return zipFileDTO;
	}
	
	@Override
	public Message getXMLWithAnswers(String url, UserDTO userDTO) {
		Message message = new Message();
		message.setType(MessageType.ERROR);
		
		Form form = formDAO.getFormByUrl(url, true);
		if(form == null){
			message.setMessage("This form does not exist");
			return message;
		}
					
		AnswerListerDTO wrapper = managementAnswers.findAnswersDTO(form.getKey().toString(), userDTO);
		if(wrapper.getAnswers().isEmpty()){
			message.setMessage("There is no answers yet");
			return message;
		}		
		
		String res = CreateAnswersXml.getAnswersXML(wrapper);
		if(res != null){
			message.setType(MessageType.SUCCESS);
			message.setData(res);
			return message;
		}
		message.setMessage("Error downloading XML: " + url);
		return message;
	}

	@Override
	public boolean isAPKBuilding(String url) {
		Form form = formDAO.getFormByUrl(url, false);
		return form.getBuildingApk();
	}

	@Override
	public boolean existTitleDuplication(String title, UserDTO userDTO) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		List<Form> forms = formDAO.getListOwnFormsByUserKey(user.getKey().toString());
		for (Form form : forms) {
			if (title.equals(form.getTitle())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public FormDTO getShareFormDTOByUrl(String url, UserDTO userDTO) {
		User user =  userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null) {
			logger.error(userDTO.getUsername()+" doesn't exist");
			return null;
		}
		Form form = formDAO.getFormByUrl(url, false);			
		Permission permission = ruleService.getPermission(form, user.getKey(), Document.FORM);
		if(permission != null && permission.getUpdate()) {
			FormDTO formDTO = new FormDTO();
			formDTO.setKey(form.getKey());
			formDTO.setTitle(form.getTitle());
			formDTO.setUrl(form.getUrl());
			formDTO.setStrPolicy(form.getPolicy().toString());
			formDTO.setFormPermission(permission);
			List<UUID> lists = form.getLists() != null ? form.getLists() : (new ArrayList<UUID>());
			Map<String, String> groupsList = managementGroup.getUsersListByKeys(lists);
			formDTO.setGroupsList(groupsList == null ? "{}" : (new Gson()).toJson(groupsList));				
			return formDTO;
		} else {
			throw new AuthorizationDenied(Document.FORM, form.getKey(), user.getKey(), Operation.READ);
		}
	}

	@Override
	public FormDTO getFormToShareFormByUrl(String url, UserDTO userDTO) {
		User owner = userDAO.findUserByEmail(userDTO.getUsername());
		if(owner == null) {
			logger.error(userDTO.getUsername() + " User does not exist");
			return null;
		}
		
		FormDTO formDTO = null;        
		Form form = formDAO.getFormByUrl(url, false);
		if(form != null) {
			SetPermissions permissions = ruleService.getPermissionsByPolicy(form.getPolicy());
        	if(permissions.getFormPermission() != null && permissions.getFormPermission().getRead()) {
        		formDTO = new FormDTO(
                        form.getKey(),
                        form.getTitle(), 
                        owner.getFullName(), 
                        form.getUrl(), 
                        form.getCreationDate().toString(), 
                        form.getPolicy().toString(),
                        permissions.getFormPermission(),
                        permissions.getAswPermission(),
                        permissions.getRepoPermission(),
                        form.getNumberOfCollects(),
                        getReportsByFormAndByUser(form.getKey().toString(), userDTO));
        		formDTO.setAnalytics(getAnalyticsByForm(form.getKey().toString(), userDTO));
        		formDTO.setPolicy(form.getPolicy());
        		formDTO.setLists(form.getLists());
        	}
    	}
        return formDTO;
	}

	@Override
	public void verifyPermissions(String formKey, String userKey) {
		Form form = formDAO.getFormByKey(UUID.fromString(formKey), true);			
		if (form != null) {
			Permission permission = ruleService.getPermission(form, UUID.fromString(userKey), Document.FORM);
			if(permission != null && permission.getRead()) {
				return;
			} else if (form.getPolicy().isShared()) {
				List<FormAccessRequest> fars = formAccessRequestDAO.getFormAccessRequestsByUser(UUID.fromString(userKey));
				FormAccessRequest _far = null;
				for (FormAccessRequest far : fars) {
					if (far.getFormUrl().equals(form.getUrl())) {
						_far = far;
						break;
					}
				}
				AuthorizationDeniedAlternative ads = new AuthorizationDeniedAlternative(Document.FORM, form.getKey(), UUID.fromString(userKey), Operation.READ);
				ads.setUserMessage(getText("error_authorization_denied"));
				if (_far == null) { // there was not requests
					ads.setExtraMessage(getText("error_auth_alternative_request"));
					throw ads;
				} else if (_far.getStatus().equals(RequestStatusType.PENDING)) {
					ads.setExtraMessage(getText("error_auth_alternative_pending"));
					ads.setStatus(RequestStatusType.PENDING);
					throw ads;
				} else {
					ads.setStatus(RequestStatusType.REJECTED);
					throw ads;
				}
			} else {
				AuthorizationDenied ad = new AuthorizationDenied(Document.FORM, form.getKey(), UUID.fromString(userKey), Operation.READ);
				ad.setUserMessage(getText("error_authorization_denied"));
				throw ad;				
			}
		}
		throw new MaritacaException("Current form doesn't exist or was deleted.");
	}
	
	@Override
	public List<FormDTO> searchPublicForms(String search){
		String[] params = null;
		if(search != null || "".equals(search)){
			params = search.split(" ");			
		}
		//By default it is going to show just 10 forms
		WrapperForms wrapperForms = managementSolrSearch.searchForms(ConstantsBusiness.SEARCH_PUBLIC_FORMS, params, 
				OrderFormBy.DATE.getDescription(), OrderType.DESC.getDescription(), 1, ConstantsBusiness.N_PUBLIC_FORMS, null);
		return fillPublicForms(wrapperForms);
	}

	@Override
	public List<FormDTO> getTopPublicForms() {
		//By default it is going to show just 10 forms
		WrapperForms wrapperForms = managementSolrSearch.listForms(ConstantsBusiness.SEARCH_PUBLIC_FORMS, 
				OrderFormBy.DATE.getDescription(), OrderType.DESC.getDescription(), 1, ConstantsBusiness.N_PUBLIC_FORMS, null);
		return fillPublicForms(wrapperForms);
	}
	
	private List<FormDTO> fillPublicForms(WrapperForms wrapperForms){
		List<FormDTO> formsDTO = new ArrayList<FormDTO>();
		if(wrapperForms != null && wrapperForms.getSolrForms() != null){
			Permission permission = new Permission(Operation.READ, Operation.UPDATE, Operation.DELETE, Operation.SHARE);
			for(SolrForm sf : wrapperForms.getSolrForms()){
				Form form = formDAO.getFormByUrl(sf.getUrl(), false);
				if(form != null){
					FormDTO formDTO = new FormDTO(
	                        form.getKey(),
	                        form.getTitle(), 
	                        "", 
	                        form.getUrl(), 
	                        form.getCreationDate().toString(), 
	                        form.getPolicy().toString(),
	                        permission,
	                        permission,
	                        permission,
	                        form.getNumberOfCollects(),
	                        null);
	        		formDTO.setImage(form.getIcon());
	        		formDTO.setDescription(form.getDescription());
					formsDTO.add(formDTO);
				}
			}
		}
		return formsDTO;
	}
	
	@Override
	public WrapperGrid<FormDTO> getOwnForms(UserDTO userDTO,
			OrderFormBy orderBy, OrderType orderType, int page, int numRows) {
	
		return getOwnFormsGeneric(userDTO, null, orderBy, orderType, page, numRows);
	}
	
	@Override
	public WrapperGrid<FormDTO> searchOwnForms(UserDTO userDTO, String search, OrderFormBy orderBy, 
			OrderType orderType, int page, int numRows) {
		
		if(search == null || "".equals(search.trim()))
			return null;	
		return getOwnFormsGeneric(userDTO, search, orderBy, orderType, page, numRows);
	}
	
	private WrapperGrid<FormDTO> getOwnFormsGeneric(UserDTO userDTO, String search, OrderFormBy orderBy, 
			OrderType orderType, int page, int numRows) {
		
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null) {
			logger.error(userDTO.getUsername() + " User does not exist");
			return null;
		}
		
		WrapperForms wrapperForms;
		if(search == null){
			wrapperForms = managementSolrSearch.listForms(ConstantsBusiness.SEARCH_MY_FORMS, 
				orderBy.getDescription(), orderType.getDescription().toLowerCase(), page, numRows, userDTO.getUsername());
		} else {
			String[] params = null;
			if(search != null || "".equals(search)){
				params = search.split(" ");
			}
			wrapperForms = managementSolrSearch.searchForms(ConstantsBusiness.SEARCH_MY_FORMS, 
				params, orderBy.getDescription(), orderType.getDescription().toLowerCase(), page, numRows, userDTO.getUsername());
		}		
		
		List<FormDTO> formsDTO = new ArrayList<FormDTO>();
		if(wrapperForms.getSolrForms() != null){
			for(SolrForm sf : wrapperForms.getSolrForms()){
				Form form = formDAO.getFormByUrl(sf.getUrl(), false);
				if(form != null){
					FormDTO formDTO = new FormDTO(
							form.getKey(), form.getTitle(), user.getFullName(), form.getUrl(), 
				                    form.getCreationDate().toString(), form.getPolicy().toString(), form.getNumberOfCollects());				
					formsDTO.add(formDTO);
				}
			}
		}
		formsDTO = getListCompleteOwnForms(formsDTO, userDTO);
		
		return getWrapperForms(formsDTO, orderBy, orderType, page, numRows, wrapperForms.getTotalFormsByQuery(), search);
	}

	@Override
	public WrapperGrid<FormDTO> getSharedForms(UserDTO userDTO,
			OrderFormBy orderBy, OrderType orderType, int page, int numRows) {

		return getSharedFormsGeneric(userDTO, null, orderBy, orderType, page, numRows);
	}

	@Override
	public WrapperGrid<FormDTO> searchSharedForms(UserDTO userDTO, String search, OrderFormBy orderBy, 
			OrderType orderType, int page, int numRows) {
		
		if(search == null || "".equals(search.trim()))
			return null;	
		return getSharedFormsGeneric(userDTO, search, orderBy, orderType, page, numRows);		
	}
	
	private WrapperGrid<FormDTO> getSharedFormsGeneric(UserDTO userDTO, String search, OrderFormBy orderBy, 
			OrderType orderType, int page, int numRows) {
		
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null) {
			logger.error(userDTO.getUsername() + " User does not exist");
			return null;
		}
		
		WrapperForms wrapperForms;
		if(search == null){
			wrapperForms = managementSolrSearch.listForms(ConstantsBusiness.SEARCH_SHARED_FORMS, 
				orderBy.getDescription(), orderType.getDescription().toLowerCase(), page, numRows, userDTO.getUsername());
		} else {
			String[] params = null;
			if(search != null || "".equals(search)){
				params = search.split(" ");
			}
			wrapperForms = managementSolrSearch.searchForms(ConstantsBusiness.SEARCH_SHARED_FORMS, 
				params, orderBy.getDescription(), orderType.getDescription().toLowerCase(), page, numRows, userDTO.getUsername());
		}
		
		List<FormDTO> formsDTO = new ArrayList<FormDTO>();
		if(wrapperForms.getSolrForms() != null){
			for(SolrForm sf : wrapperForms.getSolrForms()){
				Form form = formDAO.getFormByUrl(sf.getUrl(), false);
				if(form != null && !user.getKey().toString().equals(form.getUser().toString())) {
					SetPermissions permissions = ruleService.getPermission(form, user.getKey());
					if(permissions.getFormPermission() != null && permissions.getFormPermission().getRead()) {
						User owner = userDAO.findUserByKey(form.getUser());
						List<SimpleReportDTO> reports = null;
						List<AnalyticsDTO> analytics = null;
						if(permissions.getRepoPermission().getRead() == true){
							reports = getReportsByForm(form.getKey().toString(), userDTO);
							analytics = getAnalyticsByForm(form.getKey().toString(), userDTO);
						}
						FormDTO formDTO = new FormDTO(
				                form.getKey(),
				                form.getTitle(), 
				                owner.getFullName(), 
				                form.getUrl(), 
				                form.getCreationDate().toString(), 
				                form.getPolicy().toString(),
				                permissions.getFormPermission(),
				                permissions.getAswPermission(),
				                permissions.getRepoPermission(),
				                form.getNumberOfCollects(),
				                reports);
							formDTO.setAnalytics(analytics);
				    	formsDTO.add(formDTO);
					}
				}
			}
		}		
		return getWrapperForms(formsDTO, orderBy, orderType, page, numRows, wrapperForms.getTotalFormsByQuery(), search);
	}

	////////////////////////////////////////Remove!
	@Override
	public void populateSolr(UserDTO userDTO) {
		System.out.println("populateSolr: " + userDTO.getUsername());
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null)
			return;
		Boolean isRoot = configurationDAO.isRootUser(user.getKey());
		if(!isRoot)
			return;
		
		System.out.println("Populating Solr NOW");
		//
		List<Form> forms = formDAO.getAll();
		for(Form f : forms){
			User formOwner = userDAO.findUserByKey(f.getUser());
			if(formOwner != null){
				List<String> users = null;
				if(f.isShared()){
					users = getUsernamesList(f.getLists());					
				}
				List<String> tags = null;
				if(f.getTags() != null){
					tags = getTagsList(f.getTags());
				}
				mergeSolrForm(f, formOwner.getEmail(), users, tags);
			}
		}
	}
}