package br.unifesp.maritaca.ws.api.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import br.unifesp.maritaca.business.answer.ManagementAnswers;
import br.unifesp.maritaca.business.answer.dto.AnswerListerDTO;
import br.unifesp.maritaca.business.answer.dto.DataCollectedDTO;
import br.unifesp.maritaca.business.answer.dto.LastTimeDataDTO;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.context.SpringApplicationContext;
import br.unifesp.maritaca.persistence.util.ConstantsTest;
import br.unifesp.maritaca.ws.api.AnswersService;
import br.unifesp.maritaca.ws.api.resp.AnswerListResponse;
import br.unifesp.maritaca.ws.api.resp.ErrorResponse;
import br.unifesp.maritaca.ws.api.resp.GenericResponse;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.api.resp.XmlSavedResponse;
import br.unifesp.maritaca.ws.exceptions.MaritacaWSException;
import br.unifesp.maritaca.ws.util.SpringApplicationContextTest;
import br.unifesp.maritaca.ws.util.UtilsWS;

/**
 * Class to implements AnswerService interface.
 * 
 * @author alvarohenry
 */

@Path("/answer")
public class AnswersServiceImpl implements AnswersService {
	
	private static final Log log = LogFactory.getLog(AnswersServiceImpl.class);

	private ManagementAnswers managementAnswer;
	
	public AnswersServiceImpl() { 
		log.info("AnswersServiceImpl - Constructor");
		if(System.getProperty(ConstantsTest.SYS_PROP_KEY_TEST) == null) {
			managementAnswer = (ManagementAnswers) SpringApplicationContext.getBean("managementAnswers");
		}
		else {
			managementAnswer = (ManagementAnswers) SpringApplicationContextTest.getBean("managementAnswers");
		}
	}
	
	@Override
	public MaritacaResponse list(HttpServletRequest request, String formKey) {
		try {
			UserDTO userDTO = UtilsWS.createUserDTO(request);
			AnswerListerDTO answersDTO = managementAnswer.findAnswersDTO(formKey, userDTO);
			AnswerListResponse response = new AnswerListResponse();
			response.setAnswers(answersDTO.getAnswers());
			response.setQuestions(answersDTO.getQuestions());
			return response;
		} catch (Exception ex) {
			ErrorResponse error = new ErrorResponse(ex);
			error.setMessage(ex.getMessage());
			throw new MaritacaWSException(error);
		}		
	}

	@Override
	public MaritacaResponse getLastAnswers(HttpServletRequest request, String formKey, Long date) {
		try {
			UserDTO userDTO = UtilsWS.createUserDTO(request);
			LastTimeDataDTO lastAnswers = managementAnswer.getLastTotalAnswers(userDTO, formKey, date);
			GenericResponse response = new GenericResponse();
			response.setValue(String.valueOf(lastAnswers.getTotalAnswersCollected()));
			response.setExtra(String.valueOf(lastAnswers.getLastAnswerCreationDate()));
			return response;
		} catch (Exception ex) {
			ErrorResponse error = new ErrorResponse(ex);
			error.setMessage(ex.getMessage());
			throw new MaritacaWSException(error);
		}	
	}

	@Override
	public MaritacaResponse saveAnswer(HttpServletRequest request, MultipartFormDataInput input) {
		log.info("saveAnswer:");
		UserDTO userDTO = UtilsWS.createUserDTO(request);		
		try{
			managementAnswer.saveAnswers(input, userDTO);
			XmlSavedResponse resp = new XmlSavedResponse();
			resp.setType(MaritacaResponse.RESPONSE_TYPE);
			return resp;
		} catch(Exception ex){//TODO: Check 535
			ErrorResponse error = new ErrorResponse(ex);
			error.setMessage(ex.getMessage());
			throw new MaritacaWSException(error);
		}
	}
	
	@Override
    public MaritacaResponse putAnswer(HttpServletRequest request, DataCollectedDTO collectedDTO) {
		log.info("saveAnswer iOS:");
        try {
            UserDTO userDTO = UtilsWS.createUserDTO(request);
            log.info("AnswersServiceImpl : " + userDTO.getUsername());
            managementAnswer.saveAnswersIOS(userDTO, collectedDTO);
            XmlSavedResponse resp = new XmlSavedResponse();
            resp.setType(MaritacaResponse.RESPONSE_TYPE);
            return resp;
        } catch(Exception ex){
			ErrorResponse error = new ErrorResponse(ex);
			error.setMessage(ex.getMessage());
			throw new MaritacaWSException(error);
		} 
        /*catch (MaritacaWSException ex) {
            log.info("AnswersServiceImpl ERROR 1 :" + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.info("AnswersServiceImpl ERROR 2 :" + ex.getMessage());
            ErrorResponse error = new ErrorResponse(ex);
            throw new MaritacaWSException(error);
        }*/
    }
}