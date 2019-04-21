package br.unifesp.maritaca.business.answer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imgscalr.Scalr;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.unifesp.maritaca.business.answer.dto.AnswerDTO;
import br.unifesp.maritaca.business.answer.dto.AnswerListerDTO;
import br.unifesp.maritaca.business.answer.dto.AnswerWSDTO;
import br.unifesp.maritaca.business.answer.dto.DataCollectedDTO;
import br.unifesp.maritaca.business.answer.dto.LastTimeDataDTO;
import br.unifesp.maritaca.business.answer.dto.QuestionAnswerDTO;
import br.unifesp.maritaca.business.answer.dto.QuestionAnswerMultimediaDTO;
import br.unifesp.maritaca.business.answer.dto.QuestionAnswerSubTypeDTO;
import br.unifesp.maritaca.business.answer.dto.WrapperAnswers;
import br.unifesp.maritaca.business.base.dto.AbstractBusiness;
import br.unifesp.maritaca.business.base.dto.RuleService;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.business.enums.OrderAnswerBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.exception.AuthorizationDenied;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.multimedia.FormatConverter;
import br.unifesp.maritaca.business.multimedia.FrameExtractor;
import br.unifesp.maritaca.business.multimedia.MediaFormat;
import br.unifesp.maritaca.business.multimedia.MultimediaTreater;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.business.util.UtilsBusiness;
import br.unifesp.maritaca.persistence.dao.AnswerByUserFormDAO;
import br.unifesp.maritaca.persistence.dao.AnswerDAO;
import br.unifesp.maritaca.persistence.dao.FormDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.Answer;
import br.unifesp.maritaca.persistence.entity.AnswerByUserForm;
import br.unifesp.maritaca.persistence.entity.AnswerTimestamp;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.MaritacaDate;
import br.unifesp.maritaca.persistence.entity.QuestionAnswer;
import br.unifesp.maritaca.persistence.entity.QuestionAnswerMultimedia;
import br.unifesp.maritaca.persistence.entity.QuestionAnswerSubType;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.permission.Document;
import br.unifesp.maritaca.persistence.permission.Operation;
import br.unifesp.maritaca.persistence.permission.Permission;

@Service("managementAnswers")
public class ManagementAnswersImpl extends AbstractBusiness implements ManagementAnswers {
	
	private static final Log log = LogFactory.getLog(ManagementAnswersImpl.class);
	private static final int THUMBNAIL_MIN_SIZE = 100;
	
	@Autowired private RuleService ruleService;	
	@Autowired private UserDAO userDAO;	
	@Autowired private FormDAO formDAO;	
	@Autowired private AnswerDAO answerDAO;
	@Autowired private AnswerByUserFormDAO answerByUserFormDAO;
	
	private MultimediaTreater multimediaTreater;

	@Override
	public WrapperAnswers getAnswersByUserAndForm(UserDTO userDTO, 	String formUrl, 
			OrderAnswerBy orderBy, OrderType orderType, int page, int numRows) {
		AnswerListerDTO answerListerDTO = findAnswerListerDTO(userDTO, formUrl);
		
		if(answerListerDTO != null) {
			int answersSize = answerListerDTO.getAnswers() == null ? 0 : answerListerDTO.getAnswers().size();
			answerListerDTO.setAnswers(truncateAnswersList(answerListerDTO.getAnswers(), orderBy, orderType, page, numRows));
			return getWrapperAnswers(answerListerDTO, orderBy, orderType, page, numRows, answersSize);
		}	
		return null;
	}
	
	private List<AnswerDTO> truncateAnswersList(List<AnswerDTO> answersDTO, OrderAnswerBy orderBy, OrderType orderType, int page, int numRows){
		if(answersDTO == null)
			return null;
		else {
			UtilsBusiness.sortGrid(answersDTO, orderBy, orderType);
			return UtilsBusiness.pagingGrid(answersDTO, page, numRows);
		}
	}
	
	private WrapperAnswers getWrapperAnswers(AnswerListerDTO answerListerDTO, OrderAnswerBy orderBy, OrderType orderType, 
			int page, int numRows, int totalRows) {		

		WrapperAnswers wrapper = new WrapperAnswers();
		wrapper.setWrapper(answerListerDTO);
		wrapper.setNumRows(numRows);
		wrapper.setCurrentNumRows(answerListerDTO.getAnswers() == null ? 0 : answerListerDTO.getAnswers().size());
		wrapper.setCurrentPage(getPage(page));
		wrapper.setOrderBy(orderBy.getDescription());
		wrapper.setOrderType(orderType.getDescription());
		wrapper.setNumPages(getTotalNumPages(totalRows, numRows));
		wrapper.setTotal(totalRows);
		return wrapper;
	}
	
	@Override
	public void saveAnswers(MultipartFormDataInput input, UserDTO userDTO) {
		log.info("ManagementAnswersImpl > saveAnswersFromMultiPart");
		User user = userDAO.findUserByEmail(userDTO.getUsername());

		Map<String, List<InputPart>> mobData = input.getFormDataMap();
		
		try{
			String xml = mobData.get("xml").get(0).getBodyAsString();
			
			JAXBContext jaxbContext = JAXBContext.newInstance(DataCollectedDTO.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();			
			InputStream is = new ByteArrayInputStream(xml.getBytes(ConstantsBusiness.ENCODING_UTF8));			
			DataCollectedDTO collectedDTO = (DataCollectedDTO)unmarshaller.unmarshal(is);
			//
			validatePermission(collectedDTO.getFormId(), user);
			Answer answer;
			AnswerTimestamp answerTimeStamp;
			Form form = formDAO.getFormByKey(UUID.fromString(collectedDTO.getFormId()), false);
			for (AnswerWSDTO answerDTO : collectedDTO.getAnswers()) {
				// check if the answer was saved
				AnswerByUserForm answerByUserForm = answerByUserFormDAO.getAnswerByUserForm(user.getKey().toString(), form.getKey().toString());
				if(answerByUserForm != null && answerByUserForm.getAnswers().contains(new AnswerTimestamp(null, answerDTO.getTimestamp()))) {
					continue;
				}
				answer = new Answer();
				answer.setForm(UUID.fromString(collectedDTO.getFormId()));
				answer.setUser(user.getKey());
				answer.setUserData(user.getEmail());
				answer.setCreationDate(answerDTO.getTimestamp());
				answer.setUrl(answerDAO.getUniqueUrl());
				answer.setQuestions(getQuestionsToSave(answerDTO, form.getUrl(), answer.getUrl(), mobData));
				answerDAO.saveAnswer(answer);
				
				answerTimeStamp = new AnswerTimestamp(answer.getKey(), answerDTO.getTimestamp());
				if (answerByUserForm == null) {
					List<AnswerTimestamp> list = new ArrayList<AnswerTimestamp>(1);
					list.add(answerTimeStamp);
					answerByUserForm = new AnswerByUserForm(user.getKey(), form.getKey(), list);
				} else {
					answerByUserForm.getAnswers().add(answerTimeStamp);
				}
				answerByUserFormDAO.save(answerByUserForm);
			}
			
			//TODO: Improve this
				Integer numberOfCollects = form.getNumberOfCollects();
				if(numberOfCollects == null) { numberOfCollects = 100; }
				else { numberOfCollects = numberOfCollects + 100; }
			List<Answer> answers = answerDAO.findAnswersByFormKey(form.getKey(), numberOfCollects, null);
			
			form.setCollectors(saveCollectorsByForm(form.getCollectors(), user.getEmail()));
			form.setNumberOfCollects(answers.size());
			formDAO.saveForm(form); //Update number of collects in the form
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private List<String> saveCollectorsByForm(List<String> curCollectors, String curCollector){		
		if(curCollectors == null || curCollectors.isEmpty()){
			List<String> collectors = new ArrayList<String>(1);
			collectors.add(curCollector);
			return collectors;
		} else {
			if(!curCollectors.contains(curCollector)){
				curCollectors.add(curCollector);				
			}
			return curCollectors;
		}		
	}
	
	@Override
	public AnswerListerDTO findAnswersDTO(String formKey, UserDTO userDTO) {
		Form form = formDAO.getFormByKey(UUID.fromString(formKey), true);
		if (form != null) {
			AnswerListerDTO answerListerDTO = findAnswerListerDTO(userDTO, form.getUrl());
			UtilsBusiness.sortGrid(answerListerDTO.getAnswers(), OrderAnswerBy.DATE, OrderType.DESC);
			return answerListerDTO;
		}
		throw new MaritacaException("form doesn't exist!");
	}
	
	private AnswerListerDTO findAnswerListerDTO(UserDTO userDTO, String url) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByUrl(url, false);
		
		if(user != null || form != null) {
			Permission permission = ruleService.getPermission(form, user.getKey(), Document.ANSWER);
			AnswerListerDTO answerListerDTO = new AnswerListerDTO();
			if (permission != null && permission.getRead()) {
					Integer numberOfCollects = form.getNumberOfCollects();
					if(numberOfCollects == null) { numberOfCollects = 100; }
					else { numberOfCollects = numberOfCollects + 100; }
				List<Answer> answers = answerDAO.findAnswersByFormKey(form.getKey(), numberOfCollects, null);
				List<AnswerDTO> listAnswersDTO = new ArrayList<AnswerDTO>();							
				for (Answer answer : answers) {
					AnswerDTO answerDTO = loadAnswerDTO(answer);		
					listAnswersDTO.add(answerDTO);
				}
				sortAnswersByCreationDate(listAnswersDTO);

				answerListerDTO.setQuestions(FormXmlParser.parseQuestionsLabels(form.getXml()));
				answerListerDTO.setFormTitle(form.getTitle());
				answerListerDTO.setAnswers(listAnswersDTO);
			
				return answerListerDTO;
			} else {
				throw new AuthorizationDenied(Document.ANSWER, form.getKey(), user.getKey(), Operation.READ);
			}
		}
		throw new MaritacaException("(findAnswerListerDTO) Invalid: User or Form");
	}
	
	private List<QuestionAnswer> getQuestionsToSave(AnswerWSDTO answerDTO, String formUrl, String answerUrl, Map<String, List<InputPart>> mobData) {
		log.info("ManagementAnswersImpl - getQuestionsToSave:");
		List<QuestionAnswer> questions = new ArrayList<QuestionAnswer>();
		QuestionAnswer qa = null;
		for (QuestionAnswerDTO qaDTO : answerDTO.getQuestions()) {
			log.info("qaDTO.getType(): " + qaDTO.getType());
			if (UtilsBusiness.isMultimediaType(qaDTO.getType())) {
				qa = saveNewMultimediaData(formUrl, answerUrl, qaDTO, mobData);
			} else if (qaDTO.getType().equals(ComponentType.BARCODE.getValue()) || 
					qaDTO.getType().equals(ComponentType.MONEY.getValue())) {
				qa =  new QuestionAnswerSubType(qaDTO.getId(), qaDTO.getValue(), qaDTO.getType(), qaDTO.getSubtype());
			} else {
				qa =  new QuestionAnswer(qaDTO.getId(), qaDTO.getValue(), qaDTO.getType());
			}
			questions.add(qa);				
		}
		return questions;
	}
	
	private QuestionAnswer saveNewMultimediaData(String formUrl, String answerUrl, QuestionAnswerDTO qaDTO, Map<String, List<InputPart>> mobData) {
		QuestionAnswer qa;
		String type 				= qaDTO.getType();
		String id 					= qaDTO.getId();
		String data 				= qaDTO.getValue();
		String mediaPathFilename 	= UtilsBusiness.buildMultimediaPath(formUrl, answerUrl, type, id);		
		String pathFormUrl			= UtilsBusiness.buildFormUrlPath(formUrl, type);
		String filename  			= UtilsBusiness.buildMediaFilename(answerUrl, id);
		String thumbnail			= "";
		String pathInFS				= "";
		boolean dataExist 			= (data == null || "".equals(data)) ? false : true; 
		switch (ComponentType.getComponentType(type)) {				
			case PICTURE:
				String picFilename = ""; // empty string means unavailable data
				if (dataExist) {
					picFilename = mediaPathFilename + "." + MediaFormat.JPEG.getValue();
					pathInFS 	= UtilsBusiness.getFSPath() + picFilename;
					InputStream inputStreamFromInputPart = getInputStreamFromInputPart(mobData, data);
					try {
						byte[] byteArray = IOUtils.toByteArray(inputStreamFromInputPart);
						thumbnail = createThumbnail(byteArray, ComponentType.PICTURE, filename);
						answerDAO.saveFileFromByteArray(byteArray, pathInFS, pathFormUrl);
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage());
					}
				}
				qa =  new QuestionAnswerMultimedia(id, picFilename, ComponentType.PICTURE.getValue(), thumbnail); 
				break;
			case AUDIO:
				if (dataExist) {
					InputStream inputStreamFromInputPart = getInputStreamFromInputPart(mobData, data);
					try{
						byte[] byteArray = IOUtils.toByteArray(inputStreamFromInputPart);
						pathInFS = UtilsBusiness.getFSPath() + mediaPathFilename;
						saveAudioInFSWithByteArray(byteArray, pathInFS, filename, pathFormUrl);						
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage());
					}					
				} else {
					mediaPathFilename = ""; // empty string means unavailable data
				}
				qa =  new QuestionAnswerMultimedia(id, mediaPathFilename, ComponentType.AUDIO.getValue(), null);
				break;
			case VIDEO:
				if (dataExist) {
					pathInFS = UtilsBusiness.getFSPath() + mediaPathFilename;
					try {
						InputStream inputStreamFromInputPart = getInputStreamFromInputPart(mobData, data);
						byte[] byteArray = IOUtils.toByteArray(inputStreamFromInputPart);
						saveVideoInFSWithByteArray(byteArray, pathInFS, filename, pathFormUrl);
						thumbnail = createThumbnail(byteArray, ComponentType.VIDEO, filename);
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage());
					}
				} else {
					mediaPathFilename = ""; // empty string means unavailable data
				}
				qa =  new QuestionAnswerMultimedia(id, mediaPathFilename, ComponentType.VIDEO.getValue(), thumbnail); 
				break;
			default:
				throw new RuntimeException("Invalid multimedia type");
		}
		return qa;
	}
	
	private InputStream getInputStreamFromInputPart(Map<String, List<InputPart>> mobData, String key){
		List<InputPart> file = mobData.get(key);
		try {
			InputStream body = file.get(0).getBody(InputStream.class, null);
			return body;
		} catch (IOException e) {
			throw new RuntimeException("File could not be retrieved");
		}
	}
	
	private void saveVideoInFSWithByteArray(byte[] byteArray, String filenamePath, String filename, String pathFormUrl) {
		multimediaTreater = new FormatConverter(byteArray, ComponentType.VIDEO, filename, MediaFormat._3GPP, MediaFormat.OGG);
		String oggFilename = filenamePath + "." + MediaFormat.OGG.getValue();
		answerDAO.saveFile(multimediaTreater.executeFromByteArray(), oggFilename, pathFormUrl); // converting format 3gp to ogg
		
		multimediaTreater = new FormatConverter(byteArray, ComponentType.VIDEO, filename, MediaFormat._3GPP, MediaFormat.MP4);
		String mp4Filename = filenamePath + "." + MediaFormat.MP4.getValue();
		answerDAO.saveFile(multimediaTreater.executeFromByteArray(), mp4Filename, pathFormUrl); // converting format 3gp to mp4
		
		//Saving the original file
		String originalFileName = filenamePath + "." + MediaFormat._3GPP.getValue();
		answerDAO.saveFileFromByteArray(byteArray, originalFileName, pathFormUrl);
	}

	private void saveAudioInFSWithByteArray(byte[] byteArray, String filenamePath, String filename, String pathFormUrl) {		
		multimediaTreater = new FormatConverter(byteArray, ComponentType.AUDIO, filename, MediaFormat._3GPP, MediaFormat.OGG);
		String oggFilename = filenamePath + "." + MediaFormat.OGG.getValue();
		answerDAO.saveFile(multimediaTreater.executeFromByteArray(), oggFilename, pathFormUrl); // converting format 3gp to ogg
		
		multimediaTreater = new FormatConverter(byteArray, ComponentType.AUDIO, filename, MediaFormat._3GPP, MediaFormat.MP3);
		String mp3Filename = filenamePath + "." + MediaFormat.MP3.getValue();
		answerDAO.saveFile(multimediaTreater.executeFromByteArray(), mp3Filename, pathFormUrl); // converting format 3gp to mp3
	}
	
	private String createThumbnail(byte[] buffer, ComponentType type, String filename) {
		try {
			String data;
			if (type == ComponentType.VIDEO) {
				multimediaTreater = new FrameExtractor(buffer, filename);
				data = multimediaTreater.executeFromByteArray();
				buffer = Base64.decodeBase64(data.getBytes());
			}
			
			InputStream stream = new ByteArrayInputStream(buffer);
			BufferedImage image = ImageIO.read(stream);
			
			int percentage 	= ((100 * THUMBNAIL_MIN_SIZE) / image.getHeight()); 
			int height 		= (image.getHeight() * percentage) / 100;
			int width  		= (image.getWidth()  * percentage) / 100;
			
			BufferedImage thumbnailImg = Scalr.resize(image, width, height);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( thumbnailImg, MediaFormat.JPEG.getValue(), baos);
			baos.flush();
			byte[] thumbnailInByte = baos.toByteArray();
			
			return Base64.encodeBase64String(thumbnailInByte);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void validatePermission(String formId, User user) {
		Form form = formDAO.getFormByKey(UUID.fromString(formId), true);
		
		if(form != null) {
			Permission permission = ruleService.getPermission(form, user.getKey(),	Document.ANSWER);
			if (permission == null || !permission.getUpdate()) {
				throw new AuthorizationDenied(Document.ANSWER, form.getKey(), user.getKey(), Operation.UPDATE);
			}
		} else {
			throw new RuntimeException("No entries");
		}
	}
	
	/**
	 * Loads an answer from the database.
	 * @parm answer
	 * 
	 * @return The loaded answer in a DTO
	 */
	private AnswerDTO loadAnswerDTO(Answer answer) {
		Answer loadedAnswer = answerDAO.findAnswerByKey(answer.getKey());
		AnswerDTO answerDTO = new AnswerDTO();
		
		List<QuestionAnswerDTO> answersDTO = new ArrayList<QuestionAnswerDTO>();		
		
		for(QuestionAnswer qa : loadedAnswer.getQuestions()){
			QuestionAnswerDTO qaDTO;
			if (qa instanceof QuestionAnswerMultimedia) {
				QuestionAnswerMultimedia qam = (QuestionAnswerMultimedia) qa;
				qam.setThumbnail("data:image/png;base64," + qam.getThumbnail());
				String pathHttpUri = "".equals(qam.getValue()) ? "" : UtilsBusiness.getHttpUri() + qam.getValue();
				qam.setValue(pathHttpUri);
				qaDTO = new QuestionAnswerMultimediaDTO(qam);
			} else if (qa instanceof QuestionAnswerSubType) {
				QuestionAnswerSubType qaBar = (QuestionAnswerSubType) qa;
				qaDTO = new QuestionAnswerSubTypeDTO(qaBar);
			} else {
				qaDTO = new QuestionAnswerDTO(qa);
			}
			answersDTO.add(qaDTO);
		}
		
		answerDTO.setAnswers(answersDTO);
		answerDTO.setStrCreationDate((new MaritacaDate(loadedAnswer.getCreationDate())).toString());
		answerDTO.setCreationDate(loadedAnswer.getCreationDate());
		answerDTO.setAuthor(getFullNameByUserKey(loadedAnswer.getUser()));
		answerDTO.setUserKey(loadedAnswer.getUser().toString());
		answerDTO.setUrl(loadedAnswer.getUrl());
		
		return answerDTO;
	}
	
	//TODO: We could create a cache from the users emails in memory in order to optimize this method
	private String getFullNameByUserKey(UUID userKey) {
		User user = userDAO.findUserByKey(userKey);
		if(user != null)
			return user.getFullName();
		else
			return "";
	}
	
	private void sortAnswersByCreationDate(List<AnswerDTO> answers) {
		Collections.sort(answers, new Comparator<AnswerDTO>() {
			public int compare(AnswerDTO answer1, AnswerDTO answer2) {
				if(answer1.getStrCreationDate().compareTo(answer2.getStrCreationDate()) >= 1){
					return -1;
				} else {
					return 1;
				}
			}
		});
	}

	@Override
	public LastTimeDataDTO getLastTotalAnswers(UserDTO userDTO, String formKey, Long date) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByKey(UUID.fromString(formKey), false);
		Integer counter = 0;
		long maxCreationDate = 0;
		if (user != null || form != null) {
			AnswerListerDTO answers = this.findAnswersDTO(form.getKey().toString(), userDTO);
			for (AnswerDTO answer : answers.getAnswers()) {
				if (maxCreationDate < answer.getCreationDate()) {
					maxCreationDate = answer.getCreationDate();
				}
				if (answer.getUserKey().equals(user.getKey().toString())) {
					continue;
				}
				if (answer.getCreationDate() <= date) {
					break;
				}
				counter++;
			}
			return new LastTimeDataDTO(maxCreationDate, counter);
		}
		throw new MaritacaException("(findAnswerListerDTO) Invalid: User or Form");
	}

	@Override
	public void updateAnswersWithUserEmail(UserDTO userDTO) {
		System.out.println("updateAnswersWithUserEmail");
		User root = userDAO.findUserByEmail(userDTO.getUsername());
		if(root == null)
			return;
		
		List<Form> forms = formDAO.getAll();
		for(Form f: forms){
			
			if(f != null){
				System.out.println("formKey: " + f.getKey() + " - NumCollects: " + f.getNumberOfCollects());
				Integer numCollects = f.getNumberOfCollects();
				if(numCollects == null){
					System.out.println("numCollects == null");
					numCollects = 0;
				}
				List<Answer> answers = answerDAO.findAnswersByFormKey(f.getKey(), numCollects+100, null);
				for(Answer a : answers){
					User user = userDAO.findUserByKey(a.getUser());
					if(user != null){
						Answer answ = answerDAO.findAnswerByKey(a.getKey());
						answ.setUserData(user.getEmail());
						System.out.println("Form("+f.getKey()+"): " + f.getTitle() + " - Answer("+answ.getKey()+"): UserEmail: " + user.getEmail() + " UserData: " +answ.getUserData());
						answ.setUserEmail(null);
						answerDAO.updateAnswer(answ);
					}
				}
			}			
		}		
	}

	@Override
	public void saveAnswersIOS(UserDTO userDTO, DataCollectedDTO collectedDTO) {
		log.info("ManagementAnswersImpl:saveAnswersIOS");
        User user = userDAO.findUserByEmail(userDTO.getUsername());
        Answer answer;
		AnswerTimestamp answerTimeStamp;
		Form form = formDAO.getFormByKey(UUID.fromString(collectedDTO.getFormId()), false);
		System.out.println("form.getKey(): " + form.getKey());
        System.out.println("collectedDTO.getFormId(): " + collectedDTO.getFormId());
        System.out.println("user.getKey(): " + user.getKey()); 

        for (AnswerWSDTO answerDTO : collectedDTO.getAnswers()) {
        	System.out.println("foreach "); 
			// check if the answer was saved
			AnswerByUserForm answerByUserForm = answerByUserFormDAO.getAnswerByUserForm(user.getKey().toString(), form.getKey().toString());
			if(answerByUserForm != null && answerByUserForm.getAnswers().contains(new AnswerTimestamp(null, answerDTO.getTimestamp()))) {
				continue;
			}
			answer = new Answer();
			answer.setForm(UUID.fromString(collectedDTO.getFormId()));
			answer.setUser(user.getKey());
			answer.setUserData(user.getEmail());
			answer.setCreationDate(answerDTO.getTimestamp());
			answer.setUrl(answerDAO.getUniqueUrl());
			answer.setQuestions(getQuestionsToSave(answerDTO, form.getUrl(), answer.getUrl(), null));//
			answerDAO.saveAnswer(answer);
			
			answerTimeStamp = new AnswerTimestamp(answer.getKey(), answerDTO.getTimestamp());
			if (answerByUserForm == null) {
				List<AnswerTimestamp> list = new ArrayList<AnswerTimestamp>(1);
				list.add(answerTimeStamp);
				answerByUserForm = new AnswerByUserForm(user.getKey(), form.getKey(), list);
			} else {
				answerByUserForm.getAnswers().add(answerTimeStamp);
			}
			answerByUserFormDAO.save(answerByUserForm);
		}
		
		//TODO: Improve this
			Integer numberOfCollects = form.getNumberOfCollects();
			if(numberOfCollects == null) { numberOfCollects = 100; }
			else { numberOfCollects = numberOfCollects + 100; }
		List<Answer> answers = answerDAO.findAnswersByFormKey(form.getKey(), numberOfCollects, null);
		
		form.setCollectors(saveCollectorsByForm(form.getCollectors(), user.getEmail()));
		form.setNumberOfCollects(answers.size());
		formDAO.saveForm(form); //Update number of collects in the form
              
	}
}