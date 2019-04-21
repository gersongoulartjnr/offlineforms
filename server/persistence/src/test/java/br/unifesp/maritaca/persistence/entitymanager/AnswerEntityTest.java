package br.unifesp.maritaca.persistence.entitymanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.persistence.EntityManagerHector;
import br.unifesp.maritaca.persistence.cassandra.BaseEmbededServerSetupTest;
import br.unifesp.maritaca.persistence.dao.AnswerDAO;
import br.unifesp.maritaca.persistence.entity.Answer;
import br.unifesp.maritaca.persistence.entity.QuestionAnswer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/persistence-context-test.xml")
public class AnswerEntityTest extends BaseEmbededServerSetupTest {
	
	@Resource private EntityManagerHector emHectorImpl;
	@Resource private AnswerDAO answerDAO;
	
	@Test
	public void testUserEntity() {
		assertFalse(emHectorImpl.existColumnFamily(Answer.class));
		
		assertTrue(emHectorImpl.createColumnFamily(Answer.class));
		assertTrue(emHectorImpl.existColumnFamily(Answer.class));
		assertFalse(emHectorImpl.createColumnFamily(Answer.class));
		
		assertTrue(emHectorImpl.dropColumnFamily(Answer.class));
		assertFalse(emHectorImpl.existColumnFamily(Answer.class));
	}
	
	@Test
	public void persistWithoutCreate() {
		Answer answer = new Answer();
		answer.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		answer.setUser(UUID.randomUUID());
		answer.setForm(UUID.randomUUID());
		assertFalse(emHectorImpl.persist(answer));
	}
	
	@Test
	public void persistAnswersTest() {
		emHectorImpl.createColumnFamily(Answer.class);
		
		Answer answer = new Answer();
		answer.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		answer.setUser(UUID.randomUUID());
		answer.setForm(UUID.randomUUID());
		
		List<QuestionAnswer> qas = new ArrayList<QuestionAnswer>();
			QuestionAnswer qa = new QuestionAnswer();
			qa.setId("1");
			qa.setValue("value1");
			qas.add(qa);		
			qa = new QuestionAnswer();
			qa.setId("2");
			qa.setValue("value2");
			qas.add(qa);		
			qa = new QuestionAnswer();
			qa.setId("3");
			qa.setValue("value3");
			qas.add(qa);		
			qa = new QuestionAnswer();
			qa.setId("4");
			qa.setValue("value4");
			qas.add(qa);
		
		answer.setQuestions(qas);
		
		assertTrue(emHectorImpl.persist(answer));
	}
	
	@Test
	public void answersSorting(){
		emHectorImpl.createColumnFamily(Answer.class);
		Answer answer = new Answer();
		answer.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		answer.setUser(UUID.randomUUID());
		answer.setForm(UUID.randomUUID());
		List<QuestionAnswer> ans = new ArrayList<QuestionAnswer>(5);
		QuestionAnswer q0 = new QuestionAnswer("0",  "cero", "texto");
		QuestionAnswer q1 = new QuestionAnswer("1",  "uno", "texto");
		QuestionAnswer q10 = new QuestionAnswer("10", "diezz", "texto");
		QuestionAnswer q2 = new QuestionAnswer("2",  "dos", "texto");
		QuestionAnswer q3 = new QuestionAnswer("3",  "tres", "texto");
		QuestionAnswer q4 = new QuestionAnswer("4",  "cuatro", "texto");
		QuestionAnswer q5 = new QuestionAnswer("5",  "cinco", "texto");
		QuestionAnswer q6 = new QuestionAnswer("6",  "seis", "texto");
		QuestionAnswer q7 = new QuestionAnswer("7",  "siete", "texto");
		QuestionAnswer q8 = new QuestionAnswer("8",  "ocho", "texto");
		QuestionAnswer q9 = new QuestionAnswer("9",  "nueve", "texto");
		
		ans.add(q0); 
		ans.add(q1); 
		ans.add(q10); 
		ans.add(q2); 
		ans.add(q3); 
		ans.add(q4); 
		ans.add(q5); 
		ans.add(q6); 
		ans.add(q7); 
		ans.add(q8); 
		ans.add(q9);
		answer.setQuestions(ans);
		emHectorImpl.persist(answer);
		
		Answer a = answerDAO.findAnswerByKey(answer.getKey());
		List<QuestionAnswer> qas = a.getQuestions();
		Assert.assertEquals(qas.get(2).getId(), q2.getId());
	}
}