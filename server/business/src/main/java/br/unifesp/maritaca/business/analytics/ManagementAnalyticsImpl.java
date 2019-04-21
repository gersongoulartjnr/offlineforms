package br.unifesp.maritaca.business.analytics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.analytics.MongoAnalytics;
import br.unifesp.maritaca.business.analytics.dto.AEditorDTO;
import br.unifesp.maritaca.business.analytics.dto.AViewerDTO;
import br.unifesp.maritaca.business.analytics.dto.AnalyticsDTO;
import br.unifesp.maritaca.business.analytics.dto.AnalyticsItemDTO;
import br.unifesp.maritaca.business.base.dto.AbstractBusiness;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.RuleService;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.exception.AuthorizationDenied;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.persistence.dao.AnalyticsDAO;
import br.unifesp.maritaca.persistence.dao.FormDAO;
import br.unifesp.maritaca.persistence.dao.OAuthDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.Analytics;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.OAuthToken;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.permission.Document;
import br.unifesp.maritaca.persistence.permission.Operation;
import br.unifesp.maritaca.persistence.permission.Permission;
import br.unifesp.maritaca.persistence.util.ConfigurationFile;
import br.unifesp.maritaca.persistence.util.ConstantsPersistence;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

import com.google.gson.Gson;

@Service("managementAnalytics")
public class ManagementAnalyticsImpl extends AbstractBusiness implements ManagementAnalytics {

	private static Logger logger = Logger.getLogger(ManagementAnalyticsImpl.class);
	
	@Autowired private RuleService ruleService;
	@Autowired private FormDAO formDAO;
	@Autowired private UserDAO userDAO;
	@Autowired private AnalyticsDAO analyticsDAO;
	@Autowired private OAuthDAO oauthDAO;
	@Autowired private MongoAnalytics mongoAnalytics;

	@Override
	public List<AnalyticsDTO> findAnalyticsByFormAndByUser(String formKey, UserDTO userDTO) {
		List<Analytics> analytics = analyticsDAO.getAnalyticsByForm(formKey, false);		
		List<AnalyticsDTO> aList = new ArrayList<AnalyticsDTO>(analytics.size());
		for(Analytics a : analytics){
			AnalyticsDTO aDTO = new AnalyticsDTO(a.getKey().toString(), a.getName(), String.valueOf(a.getCreationDate().getTime()));			
			aList.add(aDTO);
		}
		return aList;
	}
	
	@Override
	public List<AnalyticsDTO> findAnalyticsByForm(String formKey,
			UserDTO userDTO) {		
		List<Analytics> analytics = analyticsDAO.getAnalyticsByForm(formKey, false);
		List<AnalyticsDTO> aList = new ArrayList<AnalyticsDTO>(analytics.size());
		for(Analytics a : analytics){
			if(a.getForm().toString().equals(formKey)){
				AnalyticsDTO aDTO = new AnalyticsDTO(a.getKey().toString(), a.getName(), String.valueOf(a.getCreationDate().getTime()));
				aList.add(aDTO);
			}
		}
		return aList;
	}	
	
	@Override
	public AEditorDTO getDataByFormUrlAndAnalyticsId(UserDTO userDTO,
			String formUrl, String analyticsId) {
	
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByUrl(formUrl, false);
		
		if(user == null || form == null){
			throw new MaritacaException("Invalid Form or invalid User");
		}
		
		AEditorDTO aEditorDTO = new AEditorDTO();
		Permission rpermission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		if(rpermission != null && rpermission.getRead()){
			aEditorDTO = new AEditorDTO();
			aEditorDTO.setReportPermission(rpermission);
			aEditorDTO.setFormName(form.getTitle());
			aEditorDTO.setFormXml(form.getXml());
			aEditorDTO.setFormUrl(form.getUrl());
			SimpleDateFormat sdf = new SimpleDateFormat(ConstantsPersistence.SHORT_DATE_FORMAT_ISO8601);
			aEditorDTO.setFormCreationDate(sdf.format(form.getCreationDate()));
			aEditorDTO.setFormNumOfCollects(form.getNumberOfCollects());						
			aEditorDTO.setFormCollectors(form.getCollectors() == null ? "" : collectorsToJson(form.getCollectors(), form.getKey()));
			
			if(analyticsId != null && !"".equals(analyticsId)){
				Analytics analytics = analyticsDAO.getAnalyticsById(analyticsId);
				if(analytics != null){
					aEditorDTO.setId(analytics.getKey().toString());
					aEditorDTO.setName(analytics.getName());
					aEditorDTO.setDoc(analytics.getDoc());
				}
			}
		}
		return aEditorDTO;
	}
	
	private String collectorsToJson(List<String> collectors, UUID formKey){
		try {
			Gson gson = new Gson();		
			return gson.toJson(collectors);
		} catch(Exception e) {
			logger.error("Collectors for " + formKey);
			return "";
		}
	}

	@Override
	public Message saveAnalytics(UserDTO userDTO, AEditorDTO aEditorDTO) {
		if(aEditorDTO.getId() == null || "".equals(aEditorDTO.getId())){
			return createAnalytics(userDTO, aEditorDTO);
		} else {
			return updateAnalytics(userDTO, aEditorDTO);
		}
	}

	private Message createAnalytics(UserDTO userDTO, AEditorDTO aEditorDTO) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByUrl(aEditorDTO.getFormUrl(), true);
		if(user == null || form == null){
			throw new MaritacaException("Invalid Form or invalid User");
		}		

		Permission rPermission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		if(rPermission != null && rPermission.getUpdate()){
			Analytics analytics = new Analytics();
			analytics.setForm(form.getKey());
			analytics.setUser(user.getKey());
			analytics.setName(aEditorDTO.getName());
			analytics.setDoc(aEditorDTO.getDoc());			
			analyticsDAO.saveAnalytics(analytics);
			Message message = new Message();
			message.setType(MessageType.SUCCESS);
			message.setMessage(getText("suc_analytics_edit_saved"));
			message.setData(analytics.getKey().toString());
			return message;
		}
		throw new AuthorizationDenied(Document.REPORT, form.getKey(), user.getKey(), Operation.UPDATE);
	}
	
	private Message updateAnalytics(UserDTO userDTO, AEditorDTO aEditorDTO) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByUrl(aEditorDTO.getFormUrl(), true);
		if(user == null || form == null){
			throw new MaritacaException("Invalid Form or invalid User");
		}		

		Permission rPermission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		if(rPermission != null && rPermission.getUpdate()){
			Analytics analytics = analyticsDAO.getAnalyticsById(aEditorDTO.getId());
			if(analytics != null && form.getKey().toString().equals(analytics.getForm().toString())){
				analytics.setName(aEditorDTO.getName());
				analytics.setDoc(aEditorDTO.getDoc());
				analyticsDAO.saveAnalytics(analytics);
				Message message = new Message();
				message.setType(MessageType.SUCCESS);
				message.setMessage(getText("suc_analytics_edit_updated"));
				message.setData(analytics.getKey().toString());
				return message;
			}
		}
		throw new AuthorizationDenied(Document.REPORT, form.getKey(), user.getKey(), Operation.UPDATE);
	}
	
	@Override
	public Message deleteAnalytics(UserDTO userDTO, String id) {
		Message message = new Message();
		User user = (User) userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null){
			throw new IllegalArgumentException("User does not exist in database");
		}
		Analytics analytics = analyticsDAO.getAnalyticsById(id);
		if(analytics == null){
			message.setType(MessageType.ERROR);
			message.setMessage(getText("err_analytics_edit_no_deleted"));
			logger.error("Error deleting analytics: " + id + " user: " + userDTO.getUsername());
		}
		Form form = formDAO.getFormByKey(analytics.getForm(), true);
		if(form == null){
			throw new IllegalArgumentException("Form does not exist in database anymore");
		}

		Permission permission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		if(permission != null && permission.getRemove()) {
			analyticsDAO.delete(analytics);			
			message.setType(MessageType.SUCCESS);
			message.setMessage(getText("suc_analytics_edit_deleted"));
		} else {
			message.setType(MessageType.ERROR);
			message.setMessage(getText("err_analytics_edit_no_deleted"));
			logger.error("Error deleting analytics: " + id + " user: " + userDTO.getUsername());
		}
		return message;
	}

	@Override
	public AViewerDTO getDataByAnalyticsId(UserDTO userDTO, String analyticsId) {
		Analytics analytics = analyticsDAO.getAnalyticsById(analyticsId);
		if(analytics == null){
			throw new MaritacaException("Invalid report");
		}
		Form form = formDAO.getFormByKey(analytics.getForm(), false);
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(form == null || user == null){
			throw new MaritacaException("Invalid user or invalid user");
		}
		Permission permission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		String uriServer = UtilsPersistence.retrieveConfigFile().getHttpUriServer();
		if("".equals(uriServer)){
			throw new MaritacaException("HttpUriServer is required.");
		}
		if(permission != null && permission.getRead()){			
			AViewerDTO aViewerDTO = new AViewerDTO();
			aViewerDTO.setId(analytics.getKey().toString());
			aViewerDTO.setName(analytics.getName());
			aViewerDTO.setDoc(analytics.getDoc());
			aViewerDTO.setFormUrl(form.getUrl());
			aViewerDTO.setFormXml(form.getXml());
			aViewerDTO.setUriServer(uriServer);
			aViewerDTO.setToken(generateOAuthToken(user)); //Using oauth authentication to view the reports
			return aViewerDTO;
		}
		throw new AuthorizationDenied(Document.REPORT, form.getKey(), user.getKey(), analyticsId, Operation.READ);
	}
	private String generateOAuthToken (User user) {
		OAuthToken oauthToken = new OAuthToken();
		oauthToken.setAccessToken(UUID.randomUUID().toString());
		oauthToken.setUser(user.getKey());
		oauthToken.setAccessTokenTTL(ConstantsPersistence.OAUTH_CODE_TIME_TO_LIVE); // ttl 10 minutes
		oauthDAO.persist(oauthToken);
		
		user.setReportToken(oauthToken.getAccessToken());
		userDAO.saveUser(user);
		
		return oauthToken.getAccessToken();
	}

	@Override
	public String processDataByItemId(UserDTO userDTO, AnalyticsItemDTO analyticsItemDTO) {
		logger.info("processDataByItemId");
		
		Form form = formDAO.getFormByUrl(analyticsItemDTO.getFormId(), false);
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(form == null || user == null){
			throw new MaritacaException("Invalid user or invalid user");
		}
		
		//TODO form?
		Analytics analytics = analyticsDAO.getAnalyticsByAnalyticsIdAndByFormUrl(analyticsItemDTO.getAnalyticsId(), form.getKey().toString());
		if(analytics == null){
			throw new MaritacaException("Invalid report");			
		}
				
		//Permissions ?
		
		ConfigurationFile configFileDTO = UtilsPersistence.retrieveConfigFile();
		
		if(!"".equals(configFileDTO.getMongoHost()) && !"".equals(configFileDTO.getMongoPort()) && 
				!"".equals(configFileDTO.getMongoTimeout())){
			String res = mongoAnalytics.processData(configFileDTO.getMongoHost(), configFileDTO.getMongoPort(), configFileDTO.getMongoTimeout(),
					user.getKey().toString(), 
					form.getKey().toString(), 
					form.getUrl(), 
					form.getNumberOfCollects(), 
					analytics.getKey().toString(),
					analytics.getDoc(), 
					analyticsItemDTO.getItemId());
			return res;
		} else{
			logger.info("ERROR: invalid parameters in configuration properties!");
			throw new RuntimeException("ERROR: invalid parameters in configuration properties!");
		}
	}
}