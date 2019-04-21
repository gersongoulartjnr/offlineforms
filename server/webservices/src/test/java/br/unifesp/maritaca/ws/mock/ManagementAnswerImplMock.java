package br.unifesp.maritaca.ws.mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import br.unifesp.maritaca.business.answer.ManagementAnswers;
import br.unifesp.maritaca.business.answer.dto.AnswerDTO;
import br.unifesp.maritaca.business.answer.dto.AnswerListerDTO;
import br.unifesp.maritaca.business.answer.dto.AnswerWSDTO;
import br.unifesp.maritaca.business.answer.dto.DataCollectedDTO;
import br.unifesp.maritaca.business.answer.dto.LastTimeDataDTO;
import br.unifesp.maritaca.business.answer.dto.WrapperAnswers;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.OrderAnswerBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.util.ConstantsBusiness;

public class ManagementAnswerImplMock implements ManagementAnswers {

	private DataCollectedDTO dataCollectedDTO;

	@Override
	public AnswerListerDTO findAnswersDTO(String formKey, UserDTO userDTO) {
		if (formKey == null) {
			throw new NullPointerException();
		}
		List<AnswerDTO> answers = new ArrayList<AnswerDTO>();
		
		List<AnswerWSDTO> answersPersist = dataCollectedDTO.getAnswers();
		for (AnswerWSDTO answerSaved : answersPersist) {
			AnswerDTO answer = new AnswerDTO();
			answer.setAnswers(answerSaved.getQuestions());
			answer.setCollectDate(new Date(answerSaved.getTimestamp()));		
			answers.add(answer);
		}	
		
		AnswerListerDTO answerListerDTO = new AnswerListerDTO();
		answerListerDTO.setAnswers(answers);
		answerListerDTO.setFormTitle("form title");
		answerListerDTO.setQuestions(new ArrayList<String>());
		
		return answerListerDTO;
	}

	@Override
	public WrapperAnswers getAnswersByUserAndForm(UserDTO userDTO,
			String formUrl, OrderAnswerBy orderBy, OrderType orderType, int page,
			int numRows) {
		return null;
	}

	@Override
	public LastTimeDataDTO getLastTotalAnswers(UserDTO userDTO, String formKey,
			Long date) {
		return new LastTimeDataDTO(12341234, 20);
	}

	@Override
	public void saveAnswers(MultipartFormDataInput input,
			UserDTO userDTO) {
		Map<String, List<InputPart>> mobData = input.getFormDataMap();
		if (mobData.isEmpty()) {
			return;
		}
		try{
			String xml = mobData.get("xml").get(0).getBodyAsString();
			JAXBContext jaxbContext = JAXBContext.newInstance(DataCollectedDTO.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();			
			InputStream is = new ByteArrayInputStream(xml.getBytes(ConstantsBusiness.ENCODING_UTF8));			
			dataCollectedDTO = (DataCollectedDTO)unmarshaller.unmarshal(is);
		} catch(Exception e){
			throw new RuntimeException(e);
		} 
	}

	@Override
	public void updateAnswersWithUserEmail(UserDTO userDTO) {		
	}

	@Override
	public void saveAnswersIOS(UserDTO userDTO, DataCollectedDTO collectedDTO) {
	}
}