package br.unifesp.maritaca.business.test.answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.junit.Test;

import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.answer.dto.AnswerDTO;
import br.unifesp.maritaca.business.answer.dto.AnswerListerDTO;
import br.unifesp.maritaca.business.answer.dto.AnswerWSDTO;
import br.unifesp.maritaca.business.answer.dto.DataCollectedDTO;
import br.unifesp.maritaca.business.answer.dto.QuestionAnswerDTO;
import br.unifesp.maritaca.business.answer.dto.QuestionAnswerMultimediaDTO;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.business.exception.AuthorizationDenied;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.util.FileDataManager;
import br.unifesp.maritaca.business.util.UtilsBusiness;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.permission.Policy;

import com.google.gson.Gson;

public class ManagemenAnswersImplTest extends AnswerTestBase {
	
	private String formXml = 	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
								"<form>"+
								"	<title>MultiMediaTestForm</title>"+
								"	<questions>"+
								"		<text id=\"0\" next=\"1\" required=\"false\">"+
								"			<label>TextBox title</label>"+
								"			<help></help>"+
								"			<size>100</size>"+
								"			<default></default>"+
								"		</text>"+
								"		<barcode id=\"1\" next=\"2\" required=\"false\">"+
								"			<label>Bar Code title</label>"+
								"			<help></help>"+
								"		</barcode>"+
								"		<audio id=\"2\" next=\"3\" required=\"false\">"+
								"			<label>Audio title</label>"+
								"			<help></help>"+
								"		</audio>"+
								"		<picture id=\"3\" next=\"4\" required=\"false\">"+
								"			<label>Picture title</label>"+
								"			<help></help>"+
								"		</picture>"+
								"		<video id=\"4\" next=\"-1\" required=\"false\">"+
								"			<label>Video title</label>"+
								"			<help></help>"+
								"		</video>"+
								"	</questions>"+
								"</form>";
	
	private AccountDTO        accountUserA;
	private UserDTO           userADto;
	private FormDTO           testForm;
	
	private QuestionAnswerDTO           qa;
	private QuestionAnswerDTO    		qaBar;
	private QuestionAnswerMultimediaDTO qaAudio;
	private QuestionAnswerMultimediaDTO qaPicture;
	private QuestionAnswerMultimediaDTO qaVideo;
	
	@Override
	void specificSetup() {
		accountUserA = new AccountDTO();
		accountUserA.setEmail("userA@mail.com");
		accountUserA.setPassword("pass");
		
		userADto = new UserDTO(accountUserA.getEmail());
		
		accountManager.saveNewAccount(accountUserA);
		
		testForm = new FormDTO();
		testForm.setXml(formXml.replaceAll("(\\t*|\\n*)", ""));
		testForm.setTitle("MultimediaTestForm");
		testForm.setOwner(accountUserA.getKey().toString());
		formManager.setBuildApk(false);
		formManager.saveForm(new UserDTO(accountUserA.getEmail()) , testForm);

		testForm = formManager.getFormDTOByUrl(testForm.getUrl(), userADto);
	}
	
	@Test(expected=MaritacaException.class)
	public void testGetAnswerWithNonexistentForm() throws Exception {
		createAnswer("", "", "", "", "");		
		answerManager.findAnswersDTO(UUID.randomUUID().toString(), userADto);		
	}
	
	@Test
	public void testGetEmptyAnswers() throws Exception {
		FormDTO form = new FormDTO();
		form.setXml(formXml.replaceAll("(\\t*|\\n*)", ""));
		form.setTitle("TestForm");
		form.setOwner(accountUserA.getKey().toString());
		formManager.setBuildApk(false);
		formManager.saveForm(new UserDTO(accountUserA.getEmail()) , form);
		
		form = formManager.getFormDTOByUrl(form.getUrl(), userADto);
		
		createAnswer("", "", "", "", "");		
		AnswerListerDTO   answerListerDTO = answerManager.findAnswersDTO(form.getKey().toString(), userADto);
		assertTrue(answerListerDTO.getAnswers().isEmpty());
	}
	
	@Test
	public void testAnswerWithWrongValues() throws Exception {
		createAnswer(null, null, null, null, null);
		
		AnswerListerDTO   answerListerDTO = answerManager.findAnswersDTO(testForm.getKey().toString(), userADto);		
		AnswerDTO         firstCollect         = answerListerDTO.getAnswers().get(0);		
		
		QuestionAnswerDTO answQa               = firstCollect.getAnswers().get(0);
		QuestionAnswerDTO answQaBar            = firstCollect.getAnswers().get(1);
		QuestionAnswerDTO answQaAudio          = firstCollect.getAnswers().get(2);
		QuestionAnswerDTO answQaPicture        = firstCollect.getAnswers().get(3);
		QuestionAnswerDTO answQaVideo          = firstCollect.getAnswers().get(4);
		
		assertEquals(qa.getValue(),    answQa.getValue());
		assertEquals(qaBar.getValue(), answQaBar.getValue());
	
		assertEquals("", answQaPicture.getValue());
		assertEquals("", answQaAudio.getValue());
		assertEquals("", answQaVideo.getValue());
	}

	@Test
	public void testAnswerWithEmptyValues() throws Exception {
		createAnswer("answer1", "132451234", "", "", "");
		
		AnswerListerDTO   foundAnswerListerDTO = answerManager.findAnswersDTO(testForm.getKey().toString(), userADto);		
		AnswerDTO         firstCollect         = foundAnswerListerDTO.getAnswers().get(0);		
		
		QuestionAnswerDTO answQa               = firstCollect.getAnswers().get(0);
		QuestionAnswerDTO answQaBar            = firstCollect.getAnswers().get(1);
		QuestionAnswerDTO answQaAudio          = firstCollect.getAnswers().get(2);
		QuestionAnswerDTO answQaPicture        = firstCollect.getAnswers().get(3);
		QuestionAnswerDTO answQaVideo          = firstCollect.getAnswers().get(4);
		
		assertEquals(qa.getValue(),    answQa.getValue());
		assertEquals(qaBar.getValue(), answQaBar.getValue());
		
		assertEquals("", answQaPicture.getValue());
		assertEquals("", answQaAudio.getValue());
		assertEquals("", answQaVideo.getValue());
	}
	
	@Test
	public void testMultimediaAnswers() throws Exception{
		
		createAnswer("answer1", "132451234", "audio.3gpp", "picture.jpg", "video.3gpp");
		
		AnswerListerDTO   foundAnswerListerDTO = answerManager.findAnswersDTO(testForm.getKey().toString(), userADto);		
		AnswerDTO         firstCollect         = foundAnswerListerDTO.getAnswers().get(0);		
		String            firstUrl             = firstCollect.getUrl();
		
		QuestionAnswerDTO answQa               = firstCollect.getAnswers().get(0);
		QuestionAnswerDTO answQaBar            = firstCollect.getAnswers().get(1);
		QuestionAnswerDTO answQaAudio          = firstCollect.getAnswers().get(2);
		QuestionAnswerDTO answQaPicture        = firstCollect.getAnswers().get(3);
		QuestionAnswerDTO answQaVideo          = firstCollect.getAnswers().get(4);
		
		assertEquals(qa.getValue(),    answQa.getValue());
		assertEquals(qaBar.getValue(), answQaBar.getValue());
		
		String pathFilename;
		
		pathFilename = UtilsBusiness.buildMultimediaPath(testForm.getUrl(), firstUrl, answQaPicture.getType(), answQaPicture.getId());
		// testing PICTURE
		assertEquals(answQaPicture.getValue(),  UtilsBusiness.getHttpUri() + pathFilename + ".jpeg");
		assertNotNull(emFileSystem.readFile("/tmp/" + answQaPicture.getValue()));
		
		// testing AUDIO in ogg format
		pathFilename = UtilsBusiness.buildMultimediaPath(testForm.getUrl(), firstUrl, answQaAudio.getType(), answQaAudio.getId());
		assertEquals(answQaAudio.getValue(),    UtilsBusiness.getHttpUri() + pathFilename);
		assertNotNull(emFileSystem.readFile("/tmp/" + answQaAudio.getValue() + ".ogg"));
		// testing AUDIO in mp3 format
		assertNotNull(emFileSystem.readFile("/tmp/" + answQaAudio.getValue() + ".mp3"));
		
		pathFilename = UtilsBusiness.buildMultimediaPath(testForm.getUrl(), firstUrl, answQaVideo.getType(), answQaVideo.getId());
		// testing VIDEO in ogg format
		assertEquals(answQaVideo.getValue(),    UtilsBusiness.getHttpUri() + pathFilename);
		assertNotNull(emFileSystem.readFile("/tmp/" + answQaVideo.getValue() + ".ogg"));
		// testing VIDEO in mp4 format
		assertNotNull(emFileSystem.readFile("/tmp/" + answQaVideo.getValue() + ".mp4"));
	}
	
	@Test(expected=AuthorizationDenied.class)
	public void testGetAnwersWithoutPermissions() throws Exception {
		createAnswer("answer1", "132451234", "", "", "");
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setEmail("attackerA@mail.com");
		accountDTO.setPassword("attacker");		
		accountManager.saveNewAccount(accountDTO);
		
		UserDTO attacker = new UserDTO(accountDTO.getEmail());
		
		answerManager.findAnswersDTO(testForm.getKey().toString(), attacker);
	}
	
	@Test
	public void testGetAnswersWithPublicAccessPermissions() throws Exception {
		testForm.setStrPolicy(Policy.PUBLIC.toString());
		formManager.updateFormFromPolicyEditor(testForm, userADto);
		
		createAnswer("answer1", "132451234", "", "", "");
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setEmail("publicUser@mail.com");
		accountDTO.setPassword("publicUser");		
		accountManager.saveNewAccount(accountDTO);
		
		UserDTO publicUser = new UserDTO(accountDTO.getEmail());
		
		AnswerListerDTO answersDTO = answerManager.findAnswersDTO(testForm.getKey().toString(), publicUser);
		assertTrue(answersDTO.getAnswers().size() > 0);
	}
	
	@Test
	public void testGetAnswersWithShareSocialPermissionRightUsers() throws Exception {
		createAnswer("answer1", "132451234", "", "", "");
		// creating a new account new maritacaList to share 
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setEmail("publicUser@mail.com");
		accountDTO.setPassword("publicUser");		
		accountManager.saveNewAccount(accountDTO);
		User user = userDAO.findUserByEmail(accountDTO.getEmail());
		// sharing form
		testForm.setStrPolicy(Policy.SHARED_SOCIAL.toString());
		Map<String, String> list = new HashMap<String, String>();
		list.put(user.getKey().toString(), user.getEmail());
		
		testForm.setGroupsList((new Gson()).toJson(list));
		
		formManager.updateFormFromPolicyEditor(testForm, userADto);
		
		UserDTO sharedSocialUser = new UserDTO(accountDTO.getEmail());
		
		AnswerListerDTO answersDTO = answerManager.findAnswersDTO(testForm.getKey().toString(), sharedSocialUser);
		assertTrue(answersDTO.getAnswers().size() > 0);
		
		answersDTO = answerManager.findAnswersDTO(testForm.getKey().toString(), userADto);
		assertTrue(answersDTO.getAnswers().size() > 0);
	}
	
	@Test(expected=AuthorizationDenied.class)
	public void testGetAnswersWithShareSocialPermissionWrongUser() throws Exception {
		createAnswer("answer1", "132451234", "", "", "");
		// creating a new account new maritacaList to share 
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setEmail("publicUser@mail.com");
		accountDTO.setPassword("publicUser");		
		accountManager.saveNewAccount(accountDTO);
		User user = userDAO.findUserByEmail(accountDTO.getEmail());
		// sharing form
		testForm.setStrPolicy(Policy.SHARED_SOCIAL.toString());
		Map<String, String> list = new HashMap<String, String>();
		list.put(user.getKey().toString(), user.getEmail());
		
		testForm.setGroupsList((new Gson()).toJson(list));
		
		formManager.updateFormFromPolicyEditor(testForm, userADto);
		
		accountDTO = new AccountDTO();
		accountDTO.setEmail("unknown@mail.com");
		accountDTO.setPassword("unknown");		
		accountManager.saveNewAccount(accountDTO);
		UserDTO unknown = new UserDTO(accountDTO.getEmail());
		
		answerManager.findAnswersDTO(testForm.getKey().toString(), unknown);
	}
	
	@Test
	public void testGetAnswersWithShareHierarquicalPermissionRightUsers() throws Exception {
		createAnswer("answer1", "132451234", "", "", "");
		// creating a new account new maritacaList to share 
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setEmail("publicUser@mail.com");
		accountDTO.setPassword("publicUser");		
		accountManager.saveNewAccount(accountDTO);
		User user = userDAO.findUserByEmail(accountDTO.getEmail());
		// sharing form
		testForm.setStrPolicy(Policy.SHARED_HIERARCHICAL.toString());
		Map<String, String> list = new HashMap<String, String>();
		list.put(user.getKey().toString(), user.getEmail());
		
		testForm.setGroupsList((new Gson()).toJson(list));
		
		formManager.updateFormFromPolicyEditor(testForm, userADto);
		
		AnswerListerDTO answersDTO = answerManager.findAnswersDTO(testForm.getKey().toString(), userADto);
		assertTrue(answersDTO.getAnswers().size() > 0);
	}
	
	@Test(expected=AuthorizationDenied.class)
	public void testGetAnswersWithShareHierarquicalPermissionWrongUser() throws Exception {
		createAnswer("answer1", "132451234", "", "", "");
		// creating a new account new maritacaList to share 
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setEmail("publicUser@mail.com");
		accountDTO.setPassword("publicUser");		
		accountManager.saveNewAccount(accountDTO);
		User user = userDAO.findUserByEmail(accountDTO.getEmail());
		// sharing form
		testForm.setStrPolicy(Policy.SHARED_HIERARCHICAL.toString());
		Map<String, String> list = new HashMap<String, String>();
		list.put(user.getKey().toString(), user.getEmail());
		
		testForm.setGroupsList((new Gson()).toJson(list));
		
		formManager.updateFormFromPolicyEditor(testForm, userADto);
	
		UserDTO sharedHierarquicalUser = new UserDTO(accountDTO.getEmail());
		answerManager.findAnswersDTO(testForm.getKey().toString(), sharedHierarquicalUser);
	}
	
	private void createAnswer(String textValue, String barcodeValue, String audioFileName, String picFileName, String videoFileName) throws Exception {
		DataCollectedDTO dataCollected = new DataCollectedDTO();
		dataCollected.setFormId(testForm.getKey().toString());
		dataCollected.setUserId(accountUserA.getKey().toString());
		
		List<AnswerWSDTO> answers = new ArrayList<AnswerWSDTO>();
		AnswerWSDTO answer1 = new AnswerWSDTO();
		long now = new Date().getTime();
		answer1.setTimestamp(now);
		
		List<QuestionAnswerDTO> questions = new ArrayList<QuestionAnswerDTO>();
		
		qa        = new QuestionAnswerDTO("0", ComponentType.TEXT, textValue);
		qaBar     = new QuestionAnswerDTO("1", ComponentType.BARCODE, barcodeValue, "subtype");
		qaAudio   = new QuestionAnswerMultimediaDTO("2", ComponentType.AUDIO,   audioFileName, null);
		qaPicture = new QuestionAnswerMultimediaDTO("3", ComponentType.PICTURE, picFileName, null);
		qaVideo   = new QuestionAnswerMultimediaDTO("4", ComponentType.VIDEO,   videoFileName, null);
		
		questions.add(qa);
		questions.add(qaBar);
		questions.add(qaAudio);
		questions.add(qaPicture);
		questions.add(qaVideo);

		answer1.setQuestions(questions);		
		answers.add(answer1);				
		dataCollected.setAnswers(answers);
		
		JAXBContext jaxbContext = JAXBContext.newInstance(DataCollectedDTO.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		StringWriter writer = new StringWriter(); 
		marshaller.marshal(dataCollected, writer);
		
		InputPart inputpart = mock(InputPart.class);
		when(inputpart.getBodyAsString()).thenReturn(writer.toString());
		List<InputPart> lstXmlInputPart = new ArrayList<InputPart>();
		lstXmlInputPart.add(inputpart);
		
		Map<String,List<InputPart>> map = new HashMap<String, List<InputPart>>();
		map.put("xml", lstXmlInputPart);
		
		if (picFileName != null && !"".equals(picFileName)) {
			InputPart inputpartPic = mock(InputPart.class);
			InputStream pictureAsInputStream = fetchFileAsInputStream(picFileName);
			when(inputpartPic.getBody(InputStream.class, null)).thenReturn(pictureAsInputStream);
			List<InputPart> lstPicInputPart = new ArrayList<InputPart>();
			lstPicInputPart.add(inputpartPic);
		
			map.put(picFileName, lstPicInputPart);
		}
		
		if (audioFileName != null && !"".equals(audioFileName)) {
			InputPart inputpartAudio = mock(InputPart.class);
			InputStream audioAsInputStream = fetchFileAsInputStream(audioFileName);
			when(inputpartAudio.getBody(InputStream.class, null)).thenReturn(audioAsInputStream);
			List<InputPart> lstAudioInputPart = new ArrayList<InputPart>();
			lstAudioInputPart.add(inputpartAudio);
		
			map.put(audioFileName, lstAudioInputPart);
		}
		
		if (videoFileName != null && !"".equals(videoFileName)) {
			InputPart inputpartVideo = mock(InputPart.class);
			InputStream videoAsInputStream = fetchFileAsInputStream(videoFileName);
			when(inputpartVideo.getBody(InputStream.class, null)).thenReturn(videoAsInputStream);
			List<InputPart> lstVideoInputPart = new ArrayList<InputPart>();
			lstVideoInputPart.add(inputpartVideo);
			
			map.put(videoFileName, lstVideoInputPart);
		}
		
		MultipartFormDataInput multipart = mock(MultipartFormDataInput.class);
		when(multipart.getFormDataMap()).thenReturn(map);
		
		answerManager.saveAnswers(multipart, userADto);
	}
	
	private String fetchFileAsBase64(String filename) {
		return FileDataManager.read("src/test/resources/"+filename);
	}
	
	private InputStream fetchFileAsInputStream(String filename) throws IOException {
		File file = new File("src/test/resources/"+filename);
		if (file.exists()) {
			byte[] buffer = new byte[(int) file.length()];
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.read(buffer, 0, buffer.length);
			return new ByteArrayInputStream(buffer);
		}		
		return null;
	}
}