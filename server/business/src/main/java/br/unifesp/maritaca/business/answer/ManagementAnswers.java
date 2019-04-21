package br.unifesp.maritaca.business.answer;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import br.unifesp.maritaca.business.answer.dto.AnswerListerDTO;
import br.unifesp.maritaca.business.answer.dto.DataCollectedDTO;
import br.unifesp.maritaca.business.answer.dto.LastTimeDataDTO;
import br.unifesp.maritaca.business.answer.dto.WrapperAnswers;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.OrderAnswerBy;
import br.unifesp.maritaca.business.enums.OrderType;

public interface ManagementAnswers {
	
	/**
	 * Gets the answers by form, and checks if the user has permissions to read them
	 * @param userDTO user data of the current user logged
	 * @param formUrl form url
	 * @param orderBy field name
	 * @param orderType asc or desc
	 * @param page page to go
	 * @param numRows number of rows to show
	 * @return
	 */
	WrapperAnswers getAnswersByUserAndForm(UserDTO userDTO, 
			String formUrl, 
			OrderAnswerBy orderBy,
			OrderType orderType,
			int page, 
			int numRows);
	
	AnswerListerDTO findAnswersDTO(String formKey, UserDTO userDTO);

	/**
	 * Gets the total of the collects done since date
	 * @param userDTO
	 * @param formKey 
	 * @param date
	 * @return
	 */
	LastTimeDataDTO getLastTotalAnswers(UserDTO userDTO, String formKey, Long date);

	/**
	 * 
	 * @param input contains XML with the answers, a JSON file with the list 
	 * of name files, and the multimedia files 
	 * @param userDTO
	 */
	void saveAnswers(MultipartFormDataInput input, UserDTO userDTO);
	
	/**
	 * TEMP Delete it
	 */
	void updateAnswersWithUserEmail(UserDTO userDTO);

	/**
	 * Allow save the answers (do not save multimedia files) 
	 * @param userDTO
	 * @param collectedDTO
	 */
	void saveAnswersIOS(UserDTO userDTO, DataCollectedDTO collectedDTO);
}