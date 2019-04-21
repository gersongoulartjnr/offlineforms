package br.unifesp.maritaca.persistence.dao;

import br.unifesp.maritaca.persistence.entity.AnswerByUserForm;

public interface AnswerByUserFormDAO {

	/**
	 * Add an answer by form and user in AnswerByUserForm entity
	 * @param auf
	 */
	void save(AnswerByUserForm auf);
	
	/**
	 * Gets the answers by form and user
	 * @param userKey
	 * @param formKey
	 * @return
	 */
	AnswerByUserForm getAnswerByUserForm(String userKey, String formKey);
}