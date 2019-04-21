package br.unifesp.maritaca.persistence.dao.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.AnswerDAO;
import br.unifesp.maritaca.persistence.entity.Answer;
import br.unifesp.maritaca.persistence.entity.QuestionAnswer;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

@Service("answerDAO")
public class AnswerDAOImpl extends AbstractDAO implements AnswerDAO {
	
	private static final Log log = LogFactory.getLog(AnswerDAOImpl.class);
	
	@Override
	public List<Answer> findAnswersByFormKey(UUID formKey, Integer rowCount, Integer colCount) {
		return emHector.cQuery(Answer.class, new String[] {"form"}, new String[] {formKey.toString()}, rowCount, colCount, true);
	}

	@Override
	public Answer findAnswerByKey(UUID answerKey) {
		Answer loadedAnswer =  emHector.find(Answer.class, answerKey);
		List<QuestionAnswer> lstQuestionAnswers = loadedAnswer.getQuestions();
		
		// sorting the list values
		Collections.sort(lstQuestionAnswers, new Comparator<QuestionAnswer>() {
			public int compare(QuestionAnswer qa1, QuestionAnswer qa2) {
		        return (Integer.parseInt(qa1.getId()) < Integer.parseInt(qa2.getId()) ? -1 : (Integer.parseInt(qa1.getId()) == Integer.parseInt(qa2.getId()) ? 0 : 1));
		    }
		});
		return loadedAnswer;
	}

	/**
	 * See <b>saveFile</b> in {@link br.unifesp.maritaca.persistence.dao.AnswerDAO}
	 */
	@Override
	public void saveFile(String data, String pathFile, String formUrlPath) {
		emFileSystem.createDirectory(formUrlPath); // checking if directory exist
		byte[] buffer = Base64.decodeBase64(data.getBytes());
		emFileSystem.saveFile(buffer, pathFile); // save file
	}
	
	/**
	 * See <b>saveFileFromInputStream</b> in {@link br.unifesp.maritaca.persistence.dao.AnswerDAO}
	 */
	@Override
	public void saveFileFromByteArray(byte[] data, String pathFile, String formUrlPath) {
		emFileSystem.createDirectory(formUrlPath); // checking if directory exist
		emFileSystem.saveFile(data, pathFile); // save file		
	}

	@Override
	public void saveAnswer(Answer answer) {
		answer.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		emHector.persist(answer);
	}

	@Override
	public String getUniqueUrl() {
		// TODO: check if this random string is enough maybe it is better to generate uuid-based string
		String url = UtilsPersistence.randomString();
		List<Answer> lstAnswer = emHector.cQuery(Answer.class, "url", url, true);
		if (lstAnswer.size() == 0) {
			return url;
		} else {
			return getUniqueUrl();
		}
	}

	@Override
	public void delete(Answer answer) {
		emHector.delete(Answer.class, answer.getKey());		
	}
	
	@Override
	public void updateAnswer(Answer answer) {
		emHector.persist(answer);
	}
}