package br.unifesp.maritaca.business.test.report;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.business.BusinessTest;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.report.ManagementReport;
import br.unifesp.maritaca.business.report.dto.ReportDTO;
import br.unifesp.maritaca.persistence.dao.ReportDAO;
import br.unifesp.maritaca.persistence.entity.Configuration;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.Report;
import br.unifesp.maritaca.persistence.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/business-context-test.xml")
public class ManagementReportTest extends BusinessTest {
	
	@Autowired private ManagementReport managementReport;
	@Autowired private ReportDAO reportDAO;
	private UserDTO userDTO;
	private User user;
	private Form form;
	private String formUrl;
	private ReportDTO reportDTO;
	private String reportName;
	private String reportXml;
	
	@Before
	public void setUp () {
		super.setProperties();
		emHector.createColumnFamily(Configuration.class);
		emHector.createColumnFamily(User.class);
		emHector.createColumnFamily(Form.class);
		emHector.createColumnFamily(Report.class);
		emHector.createColumnFamily(MaritacaList.class);
		emHector.createColumnFamily(GroupsByUser.class);
		userDTO = new UserDTO(USER1_EMAIL);
		formUrl	= "oGfTi8SJPz";
		reportName	= "TestReport";
		reportXml	= "<report></report>";	
		super.createUserAndList();
		this.createForm();
		this.getReportDTO();
	}
	
	@After
	public void cleanUp () {
		super.clearProperties();
		emHector.dropColumnFamily(Configuration.class);
		emHector.dropColumnFamily(User.class);
		emHector.dropColumnFamily(Form.class);
		emHector.dropColumnFamily(Report.class);
		emHector.dropColumnFamily(MaritacaList.class);
		emHector.dropColumnFamily(GroupsByUser.class);
	}
	
	@Test
	public void saveReport(){
		Message message = managementReport.saveReport(userDTO, reportDTO);
		Assert.assertNotNull("message should'nt be null", message);
		Assert.assertNotNull("messageType should'nt be null", message.getType());
		Assert.assertNotNull("data should'nt be null", message.getData());
		Assert.assertEquals("messageType should be SUCCESS", message.getType(), MessageType.SUCCESS);		
		Assert.assertNotNull("id should'nt be null", message.getData());
		
		Report report = reportDAO.getReportsById((String)message.getData(), true);
		Assert.assertNotNull("report key shouldn't be null", report.getKey());
	}
	
	@Test
	public void updateReport(){
		Message cMessage = managementReport.saveReport(userDTO, reportDTO);
		Assert.assertNotNull("message should'nt be null", cMessage);
		Assert.assertNotNull("messageType should'nt be null", cMessage.getType());
		Assert.assertNotNull("data should'nt be null", cMessage.getData());
		Assert.assertEquals("messageType should be SUCCESS", cMessage.getType(), MessageType.SUCCESS);		
		Assert.assertNotNull("id should'nt be null", cMessage.getData());
		
		String reportId = (String)cMessage.getData();
		String reportName = "newReportName";
		reportDTO.setReportId(reportId);		
		reportDTO.setReportName(reportName);		
		Message uMessage = managementReport.saveReport(userDTO, reportDTO);
		Report report = reportDAO.getReportsById((String)uMessage.getData(), true);
		Assert.assertEquals(report.getName(), reportName);
	}

	@Test
	public void deleteReport(){
		Message cMessage = managementReport.saveReport(userDTO, reportDTO);
		String reportId = (String)	cMessage.getData();
		Report cReport = reportDAO.getReportsById(reportId, true);
		Assert.assertNotNull("report key shouldn't be null", cReport.getKey());
		
		managementReport.deleteReportByReportId(userDTO, formUrl, reportId);
		Report dReport = reportDAO.getReportsById(reportId, true);
		Assert.assertNull("report should be null", dReport);
	}
	
	private void createForm() {
		user = userDAO.findUserByEmail(userDTO.getUsername());
		form = new Form();
		form.setUser(user.getKey());
		form.setTitle("formtest");
		form.setXml("<xml></xml>");
		form.setUrl(formUrl);
		emHector.persist(form);
	}
	
	private void getReportDTO(){
		reportDTO = new ReportDTO();
		reportDTO.setFormUrl(formUrl);
		reportDTO.setReportName(reportName);
		reportDTO.setReportXml(reportXml);		
	}
}