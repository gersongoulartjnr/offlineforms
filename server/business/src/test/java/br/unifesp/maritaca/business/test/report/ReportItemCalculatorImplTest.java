package br.unifesp.maritaca.business.test.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;

import br.unifesp.maritaca.business.BusinessTest;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.business.enums.ReportItemOpType;
import br.unifesp.maritaca.business.exception.AuthorizationDenied;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.exception.ParameterException;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.report.ReportItemCalculator;
import br.unifesp.maritaca.business.report.dto.ReportItemDTO;
import br.unifesp.maritaca.business.report.dto.ReportItemList;
import br.unifesp.maritaca.business.report.dto.ReportItemWParams;
import br.unifesp.maritaca.persistence.entity.Answer;
import br.unifesp.maritaca.persistence.entity.Configuration;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessibleByList;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.QuestionAnswer;
import br.unifesp.maritaca.persistence.entity.Report;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.permission.Policy;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/business-context-test.xml")
public class ReportItemCalculatorImplTest extends BusinessTest {

	private static final String RADIO_FIRST_OPTION = "Yes";
	private static final String RADIO_SECOND_OPTION = "No";
	
	private static final String CHECKBOX_FIRST_OPTION = "Family";
	private static final String CHECKBOX_SECOND_OPTION = "Person";
	private static final String CHECKBOX_THIRD_OPTION = "With contact";
	
	private static final String TEN 	= "10.0";
	private static final String FIFTEEN = "15.0";
	private static final String TWELVE 	= "12.0";

	private static final String RADIOBOX_FIRST_ANSWER = "{\"0\":\""+RADIO_FIRST_OPTION+"\"}";
	private static final String RADIOBOX_SECOND_ANSWER = "{\"0\":\""+RADIO_FIRST_OPTION+"\"}";
	
	private static final String CHECKBOX_FIRST_ANSWER = "{\"0\":\""+CHECKBOX_FIRST_OPTION+"\",\"1\":\""+CHECKBOX_SECOND_OPTION+"\"}";
	private static final String CHECKBOX_SECOND_ANSWER = "{\"1\":\""+CHECKBOX_SECOND_OPTION+"\",\"2\":\""+CHECKBOX_THIRD_OPTION+"\"}";
	
	private User user;
	private Form form;
	private Report report;
	private Answer answer_1;
	private Answer answer_2;
	
	private UserDTO userDTO;
	
	private Date firstAnswerDate;
	private Date secondAnswerDate;
	private Date thirdAnswerDate;
	
	private static final String FORM_XML = 
			"<form>" +
				"<title>testForm</title>" +
				"<questions>" +
					"<number id=\"0\" next=\"1\" required=\"true\" min=\"0\" max=\"10\">" + 
						"<label>label number</label>" +
						"<help>help number</help>" +
						"<default>0</default>" +
					"</number>" +						
					"<number id=\"1\" next=\"2\" required=\"true\" min=\"0\" max=\"10\">" + 
						"<label>label number</label>" +
						"<help>help number</help>" +
						"<default>0</default>" +
					"</number>" +
					"<radio id=\"2\" next=\"3\" required=\"false\">" +
						"<label>Options</label>" +
						"<help></help>" +
						"<default></default>" +
						"<option value=\"0\">" + RADIO_FIRST_OPTION + "</option>" +
						"<option value=\"1\">" + RADIO_SECOND_OPTION + "</option>" +
					"</radio>" +						
					"<checkbox id=\"3\" next=\"4\" required=\"false\">" +
						"<label>label checkbox</label>" +
						"<help></help>" +
						"<default></default>" +
						"<option value=\"0\" checked=\"false\">" + CHECKBOX_FIRST_OPTION + "</option>" +
						"<option value=\"1\" checked=\"false\">" + CHECKBOX_SECOND_OPTION + "</option>" +
						"<option value=\"2\" checked=\"false\">" + CHECKBOX_THIRD_OPTION + "</option>" +
					"</checkbox>" +
					"<location id=\"4\" next=\"-1\" required=\"true\" >" +
						"<label>GPS title</label>" +
						"<help></help>" +
					"</location>" +	
				"</questions>" +
			"</form>";
	
	@Autowired protected ManagementForm formManager;
	@Autowired private ReportItemCalculator itemCalculator;
	
	@Before
	public void setUp () {
		super.setProperties();
		emHector.createColumnFamily(User.class);
		emHector.createColumnFamily(Answer.class);
		emHector.createColumnFamily(Report.class);
		emHector.createColumnFamily(Form.class);
		emHector.createColumnFamily(MaritacaList.class);
		emHector.createColumnFamily(FormAccessibleByList.class);
		emHector.createColumnFamily(GroupsByUser.class);
		emHector.createColumnFamily(Configuration.class);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
			firstAnswerDate = sdf.parse("01/01/2013"); 
			secondAnswerDate = sdf.parse("15/01/2013");
			thirdAnswerDate = sdf.parse("15/02/2013");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		super.createUserAndList();
		this.createForm();
		this.createAnswers();
		this.createReport();
		userDTO = new UserDTO(USER1_EMAIL);
	}

	@After
	public void cleanUp () {
		emHector.delete(Form.class, form.getKey());
		emHector.delete(Answer.class, answer_1.getKey());
		emHector.delete(Answer.class, answer_2.getKey());

		super.clearProperties();
		emHector.dropColumnFamily(User.class);
		emHector.dropColumnFamily(Answer.class);
		emHector.dropColumnFamily(Report.class);
		emHector.dropColumnFamily(Form.class);
		emHector.dropColumnFamily(MaritacaList.class);
		emHector.dropColumnFamily(GroupsByUser.class);
		emHector.dropColumnFamily(Configuration.class);
		emHector.dropColumnFamily(FormAccessibleByList.class);
	}
	
	@Test(expected=ParameterException.class)
	public void testComputeBasicOperationParameteerException () {
		itemCalculator.computeSimpleResponse(userDTO, null);
	}
	
	@Test(expected=MaritacaException.class)
	public void testComputeBasicOperationInvalidForm() {
		ReportItemDTO ariDTO = new ReportItemDTO();
		ariDTO.setFormId("INVALIDURI");
		ariDTO.setReportId(report.getId());
		
		itemCalculator.computeSimpleResponse(userDTO, ariDTO);
	}
	
	@Test(expected=MaritacaException.class)
	public void testComputeBasicOperationInvalidUser () {
		ReportItemDTO ariDTO = new ReportItemDTO();
		ariDTO.setFormId(form.getUrl());
		ariDTO.setReportId(report.getId());
		userDTO.setUsername("invaliduser@nothing.com");
		itemCalculator.computeSimpleResponse(userDTO, ariDTO);
	}
	
	@Test(expected=MaritacaException.class)
	public void testComputeBasicOperationInvalidReport () {
		ReportItemDTO ariDTO = new ReportItemDTO();
		ariDTO.setFormId(form.getUrl());
		ariDTO.setReportId(report.getId());
		userDTO.setUsername("invaliduser@nothing.com");
		itemCalculator.computeSimpleResponse(userDTO, ariDTO);
	}
	
	@Test
	public void testComputeBasicOperationByOwnerReport () {
		ReportItemDTO ariDTO = new ReportItemDTO();
		ariDTO.setFormId(form.getUrl());
		ariDTO.setReportId(report.getId());
		ariDTO.setOp(ReportItemOpType.MAX.toString());
		ariDTO.setQuestionId(0);
		String max = itemCalculator.computeSimpleResponse(userDTO, ariDTO);
		assertTrue(max.equals(TWELVE));
	}
	
	@Test(expected=AuthorizationDenied.class)
	public void testComputeBasicOperationByUnauthorizedUser () {
		ReportItemDTO ariDTO = new ReportItemDTO();
		ariDTO.setFormId(form.getUrl());
		ariDTO.setReportId(report.getId());
		ariDTO.setOp(ReportItemOpType.MAX.toString());
		ariDTO.setQuestionId(0);
		userDTO.setUsername(USER2_EMAIL);
		itemCalculator.computeSimpleResponse(userDTO, ariDTO);
	}
	
	@Test(expected=AuthorizationDenied.class)
	public void testComputeBasicOperationSharedHierarquical() {
		ReportItemDTO ariDTO = new ReportItemDTO();
		ariDTO.setFormId(form.getUrl());
		ariDTO.setReportId(report.getId());
		ariDTO.setOp(ReportItemOpType.MAX.toString());
		ariDTO.setQuestionId(0);
		
		User user2 = userDAO.findUserByEmail(USER2_EMAIL);
		Map<String, String> list = new HashMap<String, String>();
		list.put(user2.getKey().toString(), user2.getEmail());
		FormDTO formDTO = new FormDTO();
		formDTO.setUrl(form.getUrl());
		formDTO.setStrPolicy(Policy.SHARED_HIERARCHICAL.toString());
		formDTO.setGroupsList((new Gson()).toJson(list));		
		formManager.updateFormFromPolicyEditor(formDTO, (new UserDTO(USER1_EMAIL)));
		
		userDTO.setUsername(USER2_EMAIL);
		itemCalculator.computeSimpleResponse(userDTO, ariDTO);
	}
	
	@Test
	public void testComputeBasicOperationSharedSocial() {
		ReportItemDTO ariDTO = new ReportItemDTO();
		ariDTO.setFormId(form.getUrl());
		ariDTO.setReportId(report.getId());
		ariDTO.setOp(ReportItemOpType.MAX.toString());
		ariDTO.setQuestionId(0);
		
		User user2 = userDAO.findUserByEmail(USER2_EMAIL);
		Map<String, String> list = new HashMap<String, String>();
		list.put(user2.getKey().toString(), user2.getEmail());
		FormDTO formDTO = new FormDTO();
		formDTO.setUrl(form.getUrl());
		formDTO.setStrPolicy(Policy.SHARED_SOCIAL.toString());
		formDTO.setGroupsList((new Gson()).toJson(list));		
		formManager.updateFormFromPolicyEditor(formDTO, (new UserDTO(USER1_EMAIL)));
		
		userDTO.setUsername(USER2_EMAIL);
		String max = itemCalculator.computeSimpleResponse(userDTO, ariDTO);
		assertTrue(max.equals(TWELVE));
	}
	
	@Test
	public void testComputeBasicOperationPublic() {
		ReportItemDTO ariDTO = new ReportItemDTO();
		ariDTO.setFormId(form.getUrl());
		ariDTO.setReportId(report.getId());
		ariDTO.setOp(ReportItemOpType.MAX.toString());
		ariDTO.setQuestionId(0);
		
		FormDTO formDTO = new FormDTO();
		formDTO.setUrl(form.getUrl());
		formDTO.setStrPolicy(Policy.PUBLIC.toString());
		formManager.updateFormFromPolicyEditor(formDTO, (new UserDTO(USER1_EMAIL)));
		
		userDTO.setUsername(USER2_EMAIL);
		String max = itemCalculator.computeSimpleResponse(userDTO, ariDTO);
		assertTrue(max.equals(TWELVE));
	}
	
	@Test
	public void testComputeHashResponseRadiobox () {
		ReportItemWParams reportItemDTO = new ReportItemWParams();
		reportItemDTO.setFormId(form.getUrl());
		reportItemDTO.setReportId(report.getId());
		reportItemDTO.setOp(ReportItemOpType.TOTALBYVALUE.toString());
		reportItemDTO.setQuestionId(2);
		List<String> params = new ArrayList<String>();
		params.add("0");
		params.add("1");		
		reportItemDTO.setParameters(params);
		
		HashMap<String, String> expected = new HashMap<String, String>();
		expected.put("0", "2");
		expected.put("1", "0");
		
		HashMap<String, String> result = itemCalculator.computeHashResponse(userDTO, reportItemDTO);
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testComputeHashResponseCheckbox () {
		ReportItemWParams reportItemDTO = new ReportItemWParams();
		reportItemDTO.setFormId(form.getUrl());
		reportItemDTO.setReportId(report.getId());
		reportItemDTO.setOp(ReportItemOpType.TOTALBYVALUE.toString());
		reportItemDTO.setQuestionId(3);
		List<String> params = new ArrayList<String>();
		params.add("0");
		params.add("1");		
		params.add("2");
		reportItemDTO.setParameters(params);
		
		HashMap<String, String> expected = new HashMap<String, String>();
		expected.put("0", "1");
		expected.put("1", "2");
		expected.put("2", "1");
		
		HashMap<String, String> result = itemCalculator.computeHashResponse(userDTO, reportItemDTO);
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testComputeHashResponseCheckboxWithParameters () {
		ReportItemWParams reportItemDTO = new ReportItemWParams();
		reportItemDTO.setFormId(form.getUrl());
		reportItemDTO.setReportId(report.getId());
		reportItemDTO.setOp(ReportItemOpType.TOTALBYVALUE.toString());
		reportItemDTO.setQuestionId(3);
		List<String> params = new ArrayList<String>();
		params.add("0");
		params.add("1");		
		reportItemDTO.setParameters(params);
		
		HashMap<String, String> expected = new HashMap<String, String>();
		expected.put("0", "1");
		expected.put("1", "2");
		
		HashMap<String, String> result = itemCalculator.computeHashResponse(userDTO, reportItemDTO);
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testComputeHashResponseRadioCheckboxEmptyAnswer () {
		this.createAnswer("{}", "{}", "");
		
		/////////////////// test radio
		ReportItemWParams reportItemDTO = new ReportItemWParams();
		reportItemDTO.setFormId(form.getUrl());
		reportItemDTO.setReportId(report.getId());
		reportItemDTO.setOp(ReportItemOpType.TOTALBYVALUE.toString());
		reportItemDTO.setQuestionId(2);
		List<String> params = new ArrayList<String>();
		params.add("0");
		params.add("1");		
		reportItemDTO.setParameters(params);
		
		HashMap<String, String> expected = new HashMap<String, String>();
		expected.put("0", "2");
		expected.put("1", "0");
		
		HashMap<String, String> result = itemCalculator.computeHashResponse(userDTO, reportItemDTO);
		
		assertEquals(expected, result);
		
		/////////////// test check		
		reportItemDTO.setQuestionId(3);
		params = new ArrayList<String>();
		params.add("0");
		params.add("1");		
		params.add("2");
		reportItemDTO.setParameters(params);
		
		expected = new HashMap<String, String>();
		expected.put("0", "1");
		expected.put("1", "2");
		expected.put("2", "1");		
		result = itemCalculator.computeHashResponse(userDTO, reportItemDTO);		
		assertEquals(expected, result);
	}
	
	@Test
	public void testComputeLimitByDate () {
		String radioValue = "{\"0\":\""+RADIO_FIRST_OPTION+"\"}";
		String checkValues = "{\"0\":\""+CHECKBOX_FIRST_OPTION+"\",\"1\":\""+CHECKBOX_SECOND_OPTION+"\"}";
		this.createAnswer(radioValue, checkValues, "-16.3713789;-71.5279748");

		/////////////////// test radio
		ReportItemWParams reportItemDTO = new ReportItemWParams();
		reportItemDTO.setFormId(form.getUrl());
		reportItemDTO.setReportId(report.getId());
		reportItemDTO.setOp(ReportItemOpType.TOTALBYVALUE.toString());
		reportItemDTO.setQuestionId(2);
		List<String> params = new ArrayList<String>();
		params.add("0");
		params.add("1");		
		reportItemDTO.setParameters(params);		
		HashMap<String, String> expected = new HashMap<String, String>();
		expected.put("0", "2");
		expected.put("1", "0");		
		HashMap<String, String> result = itemCalculator.computeHashResponse(userDTO, reportItemDTO);		
		assertEquals(expected, result);
		
		/////////////// test check		
		reportItemDTO.setQuestionId(3);
		params = new ArrayList<String>();
		params.add("0");
		params.add("1");		
		params.add("2");
		reportItemDTO.setParameters(params);		
		expected = new HashMap<String, String>();
		expected.put("0", "1");
		expected.put("1", "2");
		expected.put("2", "1");		
		result = itemCalculator.computeHashResponse(userDTO, reportItemDTO);		
		assertEquals(expected, result);		
	}
	
	@Test
	public void testListAllAnswerResponse() {
		ReportItemList reportItemDTO = new ReportItemList();
		reportItemDTO.setFormId(form.getUrl());
		reportItemDTO.setReportId(report.getId());
		reportItemDTO.setOp(ReportItemOpType.LIST_ALL.toString());
		reportItemDTO.setQuestionId(4);
				
		List<String> listAnswers = itemCalculator.listAnswersResponse(userDTO, reportItemDTO);
		assertTrue(listAnswers.size() == 2);
		
		this.createAnswer("{}", "{}", "-16.3713789;-71.5279748");
		
		listAnswers = itemCalculator.listAnswersResponse(userDTO, reportItemDTO);
		assertTrue(listAnswers.size() == 2);
	}
	
	@Test
	public void testListNAnswerResponse() {
		ReportItemList reportItemDTO = new ReportItemList();
		reportItemDTO.setFormId(form.getUrl());
		reportItemDTO.setReportId(report.getId());
		reportItemDTO.setOp(ReportItemOpType.LIST_LAST_N.toString());
		reportItemDTO.setNumAnswers(1);
		reportItemDTO.setQuestionId(4);
				
		List<String> listAnswers = itemCalculator.listAnswersResponse(userDTO, reportItemDTO);
		assertTrue(listAnswers.size() == 1);
		
		this.createAnswer("{}", "{}", "-16.3713789;-71.5279748");
		
		listAnswers = itemCalculator.listAnswersResponse(userDTO, reportItemDTO);
		assertTrue(listAnswers.size() == 1);
	}
	
	private void createAnswer(String radioValue, String checkValues, String location) {
		Answer answer = new Answer();
		QuestionAnswer question;
		List<QuestionAnswer> questions = new ArrayList<QuestionAnswer>();
		answer.setCreationDate(thirdAnswerDate.getTime());
		answer.setForm(form.getKey());
		answer.setKey(UUID.randomUUID());
			question = new QuestionAnswer("0", String.valueOf(TEN), ComponentType.NUMBER.getValue());
			questions.add(question);
			question = new QuestionAnswer("1", String.valueOf(FIFTEEN), ComponentType.NUMBER.getValue());
			questions.add(question);
			question = new QuestionAnswer("2", radioValue, ComponentType.RADIOBOX.getValue());
			questions.add(question);
			question = new QuestionAnswer("3", checkValues, ComponentType.CHECKBOX.getValue());
			questions.add(question);
			question = new QuestionAnswer("4", location, ComponentType.GEOLOCATION.getValue());
			questions.add(question);
		answer.setQuestions(questions);
		answer.setUrl(RandomStringUtils.randomAlphanumeric(10));
		answer.setUser(user.getKey());
		
		emHector.persist(answer);
	}

	private void createAnswers() {
		List<QuestionAnswer> questions = new ArrayList<QuestionAnswer>();
		QuestionAnswer question;
		
		answer_1 = new Answer();		
		answer_1.setCreationDate(firstAnswerDate.getTime());
		answer_1.setForm(form.getKey());
		answer_1.setKey(UUID.randomUUID());
			question = new QuestionAnswer("0", String.valueOf(TWELVE), ComponentType.NUMBER.getValue());
			questions.add(question);
			question = new QuestionAnswer("1", String.valueOf(FIFTEEN), ComponentType.NUMBER.getValue());
			questions.add(question);
			question = new QuestionAnswer("2", RADIOBOX_FIRST_ANSWER, ComponentType.RADIOBOX.getValue());
			questions.add(question);
			question = new QuestionAnswer("3", CHECKBOX_FIRST_ANSWER, ComponentType.CHECKBOX.getValue());
			questions.add(question);
			question = new QuestionAnswer("4", "-16.3713789;-71.5279748", ComponentType.GEOLOCATION.getValue());
			questions.add(question);
		answer_1.setQuestions(questions);
		answer_1.setUrl(RandomStringUtils.randomAlphanumeric(10));
		answer_1.setUser(user.getKey());
		
		emHector.persist(answer_1);
		
		answer_2 = new Answer();
		questions = new ArrayList<QuestionAnswer>();
		answer_2.setCreationDate(secondAnswerDate.getTime());
		answer_2.setForm(form.getKey());
		answer_2.setKey(UUID.randomUUID());
			question = new QuestionAnswer("0", String.valueOf(TEN), ComponentType.NUMBER.getValue());
			questions.add(question);
			question = new QuestionAnswer("1", String.valueOf(FIFTEEN), ComponentType.NUMBER.getValue());
			questions.add(question);
			question = new QuestionAnswer("2", RADIOBOX_SECOND_ANSWER, ComponentType.RADIOBOX.getValue());
			questions.add(question);
			question = new QuestionAnswer("3", CHECKBOX_SECOND_ANSWER, ComponentType.CHECKBOX.getValue());
			questions.add(question);
			question = new QuestionAnswer("4", "-16.3713789;-71.5279748", ComponentType.GEOLOCATION.getValue());
			questions.add(question);
		answer_2.setQuestions(questions);
		answer_2.setUrl(RandomStringUtils.randomAlphanumeric(10));
		answer_2.setUser(user.getKey());
		
		emHector.persist(answer_2);
	}
	
	private void createReport() {
		report = new Report();
		report.setForm(form.getKey());
		report.setUser(user.getKey());
		report.setId(UtilsPersistence.randomString());
		report.setStart(firstAnswerDate.getTime());
		report.setFinish(secondAnswerDate.getTime());
		emHector.persist(report);
	}
	
	private void createForm() {
		user = userDAO.findUserByEmail(USER1_EMAIL);
		form = new Form();
		form.setTitle("formtest");
		form.setUrl(UtilsPersistence.randomString());
		form.setXml(FORM_XML);
		form.setUser(user.getKey());
		emHector.persist(form);
	}
	
}
