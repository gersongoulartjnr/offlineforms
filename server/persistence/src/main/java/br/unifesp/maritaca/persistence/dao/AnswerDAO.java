package br.unifesp.maritaca.persistence.dao;

import java.util.List;
import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.Answer;

public interface AnswerDAO {

	/**
	 * Find the answers to the questions to the given form. The
	 * answers are returned inside a list in the same order  of
	 * the correspondent questions.
	 * @param formKey
	 * @param rowCount rows to show
	 * @param colCount columns to show
	 * @return A list containing the answers
	 */
	List<Answer> findAnswersByFormKey(UUID formKey, Integer rowCount, Integer colCount);
	
	/**
	 * This method finds an answer by key
	 * 
	 * @param answerKey
	 * @return Answer entity
	 */
	Answer findAnswerByKey(UUID answerKey);

	/**
	 * This method saves a media file in the file system.
	 * 
	 * @param data data file in BASE64.
	 * @param pathFile path to file
	 * @param formPath path formUrl
	 */
	void saveFile(String data, String pathFile, String formPath);
	
	/**
	 * This method saves a media file in the file system.
	 * 
	 * @param data data input stream
	 * @param pathFile path to file
	 * @param formPath path formUrl
	 */
	void saveFileFromByteArray(byte[] data, String pathFile, String formPath);
	
	/**
	 * This method saves an answer in database.
	 * 
	 * @param answer entity
	 */
	void saveAnswer(Answer answer);

	/**
	 * This method creates unique string (10 characters)
	 * 
	 * @return an string (10 characters)
	 */
	String getUniqueUrl();
	
	/**
	 * Delete an answer
	 * @param answer
	 */
	void delete(Answer answer);
	
	/**
	 * Update an answer
	 * @param answer
	 */
	void updateAnswer(Answer answer);
}