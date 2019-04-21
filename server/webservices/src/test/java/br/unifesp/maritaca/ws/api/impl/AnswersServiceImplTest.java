package br.unifesp.maritaca.ws.api.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import junit.framework.Assert;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.business.answer.dto.AnswerWSDTO;
import br.unifesp.maritaca.business.answer.dto.DataCollectedDTO;
import br.unifesp.maritaca.business.answer.dto.QuestionAnswerDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.ws.WebServicesTest;
import br.unifesp.maritaca.ws.api.AnswersService;
import br.unifesp.maritaca.ws.api.resp.AnswerListResponse;
import br.unifesp.maritaca.ws.api.resp.GenericResponse;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.exceptions.MaritacaWSException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:webservices-context-test.xml"})
public class AnswersServiceImplTest extends WebServicesTest {

	private static final String USER_EMAIL = "test@maritaca.com";	
	
	private AnswersService answersService;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() {
		super.setProperties();
		answersService = new AnswersServiceImpl();
	}
	
	@After
	public void cleanUp() {
		super.clearProperties();
	}
	
	@Test
	public void testSaveAnswerUnAuthorizedError() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		exception.expect(MaritacaWSException.class);
		@SuppressWarnings("unused")
		MaritacaResponse response = answersService.saveAnswer(request, null);
	}
	
	@Test
	public void testSaveAnswerCollectedDataError() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);
		
		MultipartFormDataInput multipart = null;
		
		exception.expect(MaritacaWSException.class);
		@SuppressWarnings("unused")
		MaritacaResponse response = answersService.saveAnswer(request, multipart);
	}
	
	@Test
	public void testSaveAnswerSuccess() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);
		
		Map<String,List<InputPart>> map = new HashMap<String, List<InputPart>>();
		MultipartFormDataInput multipart = mock(MultipartFormDataInput.class);
		when(multipart.getFormDataMap()).thenReturn(map);
		
		MaritacaResponse response = answersService.saveAnswer(request, multipart);
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getCode());
	}
	
	@Test
	public void testListFormKeyError() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);

		exception.expect(MaritacaWSException.class);
		@SuppressWarnings("unused")
		MaritacaResponse response = answersService.list(request, null);
	}
	
	@Test
	public void testLastAnswers() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);
		
		String formKey = "";
		Long date = 0L;
		GenericResponse resp = (GenericResponse) answersService.getLastAnswers(request, formKey , date);
		Assert.assertTrue(Integer.parseInt(resp.getValue()) == 20);
	}
	
	@Test
	public void testListSuccess() throws JAXBException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);

		DataCollectedDTO collectedDTO = getDataCollectedDTO();
		
		JAXBContext jaxbContext = JAXBContext.newInstance(DataCollectedDTO.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		StringWriter writer = new StringWriter(); 
		marshaller.marshal(collectedDTO, writer);
		
		InputPart inputpart = mock(InputPart.class);
		when(inputpart.getBodyAsString()).thenReturn(writer.toString());
		
		Map<String,List<InputPart>> map = new HashMap<String, List<InputPart>>();
		List<InputPart> value = new ArrayList<InputPart>();
		value.add(inputpart);
		
		map.put("xml", value);
		MultipartFormDataInput multipart = mock(MultipartFormDataInput.class);
		when(multipart.getFormDataMap()).thenReturn(map);
		
		MaritacaResponse resp = answersService.saveAnswer(request, multipart);
		Assert.assertEquals(Status.OK.getStatusCode(), resp.getCode());
		
		resp = answersService.list(request, UUID.randomUUID().toString());
		
		AnswerListResponse answerResponse = (AnswerListResponse) resp;
		Assert.assertTrue(collectedDTO.getAnswers().size() == answerResponse.getAnswers().size());
	}
	
	private DataCollectedDTO getDataCollectedDTO() {
		QuestionAnswerDTO qa = new QuestionAnswerDTO();
		qa.setId("1");
		qa.setType("text");
		qa.setValue("testecito");
		
		List<QuestionAnswerDTO> questions = new ArrayList<QuestionAnswerDTO>();
		questions.add(qa);
		
		AnswerWSDTO answer = new AnswerWSDTO();
		answer.setQuestions(questions);
		answer.setTimestamp(new Date().getTime());
		
		List<AnswerWSDTO> answers = new ArrayList<AnswerWSDTO>();
		answers.add(answer);
		
		DataCollectedDTO collectedDTO = new DataCollectedDTO();
		collectedDTO.setAnswers(answers);
		collectedDTO.setFormId(UUID.randomUUID().toString());
		collectedDTO.setUserId(UUID.randomUUID().toString());
		
		return collectedDTO;
	}
	
}
