package br.unifesp.maritaca.persistence.dao.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.AnswerByUserFormDAO;
import br.unifesp.maritaca.persistence.entity.AnswerByUserForm;

@Service("answerByUserFormDAO")
public class AnswerByUserFormDAOImpl extends AbstractDAO implements AnswerByUserFormDAO {

	@Override
	public void save(AnswerByUserForm auf) {
		emHector.persist(auf);
	}

	@Override
	public AnswerByUserForm getAnswerByUserForm(String userKey, String formKey) {
		List<AnswerByUserForm> result = emHector.cQuery(AnswerByUserForm.class, 
				new String[]{"user", "form"}, new String[]{userKey, formKey}, null, null, false);
		return result.size() == 1 ? result.get(0) : null;
	}
}