package br.unifesp.maritaca.business.test.answer;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.business.account.ManagementAccount;
import br.unifesp.maritaca.business.answer.ManagementAnswers;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.persistence.EntityManagerFileSystem;
import br.unifesp.maritaca.persistence.EntityManagerHectorImpl;
import br.unifesp.maritaca.persistence.cassandra.BaseEmbededServerSetupTest;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.Answer;
import br.unifesp.maritaca.persistence.entity.AnswerByUserForm;
import br.unifesp.maritaca.persistence.entity.Configuration;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessibleByList;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.Tag;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.entity.UserFormTag;
import br.unifesp.maritaca.persistence.entity.VoteByForm;
import br.unifesp.maritaca.persistence.util.ConstantsTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/business-context-test.xml")
public abstract class AnswerTestBase  extends BaseEmbededServerSetupTest {

	@Resource
	protected EntityManagerHectorImpl emHector;
	@Autowired
	protected EntityManagerFileSystem emFileSystem;
	@Autowired
	protected ManagementAnswers       answerManager;	
	@Autowired
	protected ManagementAccount       accountManager;	
	@Autowired
	protected ManagementForm          formManager;
	@Autowired
	protected UserDAO          userDAO;
	
	protected void setProperties() {
		System.setProperty(ConstantsTest.SYS_PROP_KEY_TEST, ConstantsTest.SYS_PROP_VALUE_TEST);
	}
	protected void clearProperties() {
		System.clearProperty(ConstantsTest.SYS_PROP_KEY_TEST);
	}
	
	@Before
	public void setUp(){
		this.setProperties();
        emHector.createColumnFamily(User.class);
        emHector.createColumnFamily(MaritacaList.class);
        emHector.createColumnFamily(Form.class);
        emHector.createColumnFamily(VoteByForm.class);
        emHector.createColumnFamily(Answer.class);
        emHector.createColumnFamily(FormAccessibleByList.class);
        emHector.createColumnFamily(Configuration.class);
        emHector.createColumnFamily(Tag.class);
		emHector.createColumnFamily(UserFormTag.class);
		emHector.createColumnFamily(AnswerByUserForm.class);
		specificSetup();
	}
		
	@After
	public void cleanUp(){
		this.clearProperties();
		emHector.dropColumnFamily(User.class);
		emHector.dropColumnFamily(MaritacaList.class);
		emHector.dropColumnFamily(Form.class);
		emHector.dropColumnFamily(Answer.class);		
		emHector.dropColumnFamily(VoteByForm.class);
		emHector.dropColumnFamily(FormAccessibleByList.class);
		emHector.dropColumnFamily(Tag.class);
		emHector.dropColumnFamily(UserFormTag.class);
		emHector.dropColumnFamily(AnswerByUserForm.class);
	}

	abstract void specificSetup();
}