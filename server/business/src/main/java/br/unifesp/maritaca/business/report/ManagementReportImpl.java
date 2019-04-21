package br.unifesp.maritaca.business.report;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.answer.dto.ZipFileDTO;
import br.unifesp.maritaca.business.base.dto.AbstractBusiness;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.RuleService;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.exception.AuthorizationDenied;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.oauth.ManagementOAuth;
import br.unifesp.maritaca.business.oauth.dto.OAuthTokenDTO;
import br.unifesp.maritaca.business.report.dto.RepData;
import br.unifesp.maritaca.business.report.dto.ReportDTO;
import br.unifesp.maritaca.business.report.dto.SimpleReportDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.persistence.dao.FormDAO;
import br.unifesp.maritaca.persistence.dao.OAuthDAO;
import br.unifesp.maritaca.persistence.dao.ReportDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.OAuthToken;
import br.unifesp.maritaca.persistence.entity.Report;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.permission.Document;
import br.unifesp.maritaca.persistence.permission.Operation;
import br.unifesp.maritaca.persistence.permission.Permission;
import br.unifesp.maritaca.persistence.util.ConstantsPersistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service("managementReport")
public class ManagementReportImpl extends AbstractBusiness implements ManagementReport {
	
	private static Logger logger = Logger.getLogger(ManagementReportImpl.class);

	@Autowired private RuleService ruleService;
	@Autowired private ReportDAO reportDAO;
	@Autowired private FormDAO formDAO;
	@Autowired private UserDAO userDAO;
	@Autowired private OAuthDAO oauthDAO;
	@Autowired private ManagementOAuth managementOAuth;
	@Autowired private ReportCsvFile reportCsvFile;

	@Override
	public List<SimpleReportDTO> findReportsByFormAndByUser(String formKey, UserDTO userDTO) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null){
			throw new MaritacaException(userDTO.getUsername() + " does not exist");
		}
		Form form = formDAO.getFormByKey(UUID.fromString(formKey), false);
		if(form == null){
			throw new MaritacaException(formKey + " form does not exist");
		}
		List<Report> reports = reportDAO.getReportsByFormKey(formKey, true);
		if(reports == null || reports.isEmpty()){
			return new ArrayList<SimpleReportDTO>(0);
		}		
		Permission rpermission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		if(rpermission != null && rpermission.getRead()){
			List<SimpleReportDTO> reportsList = new ArrayList<SimpleReportDTO>(reports.size());
			for(Report r : reports){				
				if(r.getForm().toString().equals(formKey)){
						SimpleReportDTO sReport = new SimpleReportDTO(r.getId(), r.getName(), String.valueOf(r.getCreationDate().getTime()));
						reportsList.add(sReport);
					}
			}
			return reportsList;		
		}
		return null;
	}
	
	@Override
	public List<SimpleReportDTO> findReportsByForm(String formKey, 	UserDTO userDTO) {
		List<Report> reports = reportDAO.getReportsByFormKey(formKey, false);		
		List<SimpleReportDTO> reportsList = new ArrayList<SimpleReportDTO>(reports.size());
		for(Report r : reports){				
			if(r.getForm().toString().equals(formKey)){
					SimpleReportDTO sReport = new SimpleReportDTO(r.getId(), r.getName(), String.valueOf(r.getCreationDate().getTime()));
					reportsList.add(sReport);
				}
		}
		return reportsList;
	}
	
	@Override
	public ReportDTO getReportDataByFormAndByReport(UserDTO userDTO, String formUrl, String reportId) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByUrl(formUrl, false);
		
		if(user == null || form == null){
			throw new MaritacaException("Invalid Form or invalid User");
		}
		
		Permission rpermission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		if(rpermission != null && rpermission.getRead()){
			SimpleDateFormat sdf = new SimpleDateFormat(ConstantsBusiness.SHORT_DATE_ISO8601);
			ReportDTO reportDTO = new ReportDTO();			
			reportDTO.setReportPermission(rpermission);
			reportDTO.setFormName(form.getTitle());
			reportDTO.setFormDescription(form.getDescription());
			reportDTO.setFormUrl(form.getUrl());
			reportDTO.setFormXml(form.getXml());
			reportDTO.setStrStart(sdf.format(form.getCreationDate()));
			reportDTO.setStrFinish(sdf.format(new Date()));
			if(form.getIcon() != null) {
				reportDTO.setFormIcon("data:image/png;base64,"+form.getIcon());
			}
			
			if(reportId != null && !"".equals(reportId)){
				Report report = reportDAO.getReportsById(reportId, false);
				if(report != null){					
					reportDTO.setReportId(report.getId());
					reportDTO.setReportName(report.getName());
					reportDTO.setReportXml(report.getXml());
					if(report.getStart() != null){
						reportDTO.setStrStart(sdf.format(report.getStart()));
					}
					if(report.getFinish() != null){
						reportDTO.setStrFinish(sdf.format(report.getFinish()));
					}
				}
			}
			return reportDTO;	
		}
		throw new AuthorizationDenied(Document.REPORT, form.getKey(), user.getKey(), Operation.READ);
	}
	
	@Override
	public Message saveReport(UserDTO userDTO, ReportDTO reportDTO) {
		if(reportDTO.getReportId() == null || reportDTO.getReportId().equals("")){
			return createReport(userDTO, reportDTO);
		}
		else{
			return updateReport(userDTO, reportDTO);
		}
	}
	
	private Message createReport(UserDTO userDTO, ReportDTO reportDTO) {
		Message message = new Message();
		message.setType(MessageType.ERROR);
		message.setMessage(getText("exception_internal_server_error"));
		
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByUrl(reportDTO.getFormUrl(), true);
		if(user == null || form == null){
			throw new MaritacaException("Invalid Form or invalid User");
		}		

		Permission permission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		if(permission != null && permission.getUpdate()){
			Report report = new Report();
			report.setForm(form.getKey());
			report.setUser(user.getKey());
			report.setName(reportDTO.getReportName());
			report.setXml(reportDTO.getReportXml());
			report.setStart(reportDTO.getStart() == null ? null : reportDTO.getStart().getTime());
			report.setFinish(reportDTO.getFinish() == null ? null : reportDTO.getFinish().getTime());
			report.setId(reportDAO.getUniqueId());				
			reportDAO.saveReport(report);
			
			message.setType(MessageType.SUCCESS);
			message.setMessage(getText("report_edit_save_success"));
			message.setData(report.getId());
			return message;
		}
		throw new AuthorizationDenied(Document.REPORT, form.getKey(), user.getKey(), Operation.UPDATE);
	}
	
	private Message updateReport(UserDTO userDTO, ReportDTO reportDTO){		
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByUrl(reportDTO.getFormUrl(), true);
		if(user == null || form == null){
			throw new MaritacaException("Invalid Form or invalid User");
		}
		
		Permission permission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		if(permission != null && permission.getUpdate()){
			Report report = reportDAO.getReportsById(reportDTO.getReportId(), true);
			if(report != null && form.getKey().toString().equals(report.getForm().toString())){					
				report.setName(reportDTO.getReportName());
				report.setXml(reportDTO.getReportXml());
				report.setStart(reportDTO.getStart() == null ? null : reportDTO.getStart().getTime());
				report.setFinish(reportDTO.getFinish() == null ? null : reportDTO.getFinish().getTime());
				reportDAO.saveReport(report);
				
				Message message = new Message();
				message.setType(MessageType.SUCCESS);
				message.setMessage(getText("report_edit_save_success"));
				message.setData(report.getId());
				return message;
			}
			throw new AuthorizationDenied(Document.REPORT, form.getKey(), user.getKey(), reportDTO.getReportId(), Operation.UPDATE);
		}	
		throw new AuthorizationDenied(Document.REPORT, form.getKey(), user.getKey(), Operation.UPDATE);
	}

	@Override
	public Message deleteReportByReportId(UserDTO userDTO, String formUrl, String reportId) {
		Message message = new Message();
		message.setType(MessageType.ERROR);
		message.setMessage(getText("report_delete_error"));
		
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByUrl(formUrl, true);
		if(user != null && form != null){
			Permission permission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
			if(permission != null && permission.getRemove()){
				Report report = reportDAO.getReportsById(reportId, true);
				if(report != null){
					reportDAO.delete(report);
					message.setType(MessageType.SUCCESS);
					message.setMessage(getText("report_delete_success"));
					return message;
				}				
			}
			else{
				logger.error(userDTO.getUsername() + " cannot delete report " + reportId);
			}
		}		
		return message;
	}

	@Override
	public ReportDTO getReportDataById(UserDTO userDTO, String reportId) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Report report = reportDAO.getReportsById(reportId, true);
		Form form = formDAO.getFormByKey(report.getForm(), false);
		
		if(user == null || report == null || form == null){
			throw new MaritacaException("Invalid Form or invalid User or invalid report");
		}
		
		Permission permission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		if(permission != null && permission.getRead()){
			SimpleDateFormat sdf = new SimpleDateFormat(ConstantsBusiness.SHORT_DATE_ISO8601);
			
			ReportDTO reportDTO = new ReportDTO();
			reportDTO.setReportId(report.getId());
			reportDTO.setReportName(report.getName());
			reportDTO.setReportXml(report.getXml());			
			if(report.getStart() != null && report.getFinish() != null){
				reportDTO.setStrStart(sdf.format(new Date(report.getStart())));
				reportDTO.setStrFinish(sdf.format(new Date(report.getFinish())));
			}
			reportDTO.setToken(generateOAuthToken(user)); //Using oauth authentication to view the reports
			return reportDTO;
		}
		throw new AuthorizationDenied(Document.REPORT, form.getKey(), user.getKey(), reportId, Operation.READ);
	}
	
	@Override
	public ReportDTO getReportDataByTokenAndId(String userOAuthToken, String reportId) {
		OAuthTokenDTO oauthTokenDTO = managementOAuth.findOAuthToken(userOAuthToken);
		if(oauthTokenDTO == null)
			throw new MaritacaException("Invalid token");
		
		User user = userDAO.findUserByEmail(oauthTokenDTO.getUserEmail());
		Report report = reportDAO.getReportsById(reportId, true);
		Form form = formDAO.getFormByKey(report.getForm(), false);
		
		if(user == null || report == null || form == null){
			throw new MaritacaException("Invalid Form or invalid User or invalid report");
		}
		
		Permission permission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		if(permission != null && permission.getRead()){
			SimpleDateFormat sdf = new SimpleDateFormat(ConstantsBusiness.SHORT_DATE_ISO8601);
			
			ReportDTO reportDTO = new ReportDTO();
			reportDTO.setReportId(report.getId());
			reportDTO.setReportName(report.getName());
			reportDTO.setReportXml(report.getXml());			
			if(report.getStart() != null && report.getFinish() != null){
				reportDTO.setStrStart(sdf.format(new Date(report.getStart())));
				reportDTO.setStrFinish(sdf.format(new Date(report.getFinish())));
			}
			reportDTO.setToken(generateOAuthToken(user)); //Using oauth authentication to view the reports
			return reportDTO;
		}
		throw new AuthorizationDenied(Document.REPORT, form.getKey(), user.getKey(), reportId, Operation.READ);
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
	public ZipFileDTO getCsvData(UserDTO userDTO, ReportDTO reportDTO) {
		ZipFileDTO zf = null;
		try{
			Gson gson = new Gson();
			Type collectionType = new TypeToken<RepData>(){}.getType();
			String jsonData = null;
			if(reportDTO.getReportData().startsWith("'") && reportDTO.getReportData().endsWith("'")){
				int l = reportDTO.getReportData().length();
				jsonData = reportDTO.getReportData().substring(1, l-1);
				RepData rd = gson.fromJson(jsonData, collectionType);				
				return reportCsvFile.buildCsvFile(rd);
			}			
		}
		catch(Exception e){
			zf = new ZipFileDTO();
			zf.setMessage(getText("error_unexpected"));
			logger.error("getCsvData: " + e.getMessage());
		}		
		return zf;
	}
}