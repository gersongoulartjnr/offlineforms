package br.unifesp.maritaca.persistence.dao.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.entity.Analytics;
import br.unifesp.maritaca.persistence.entity.Answer;
import br.unifesp.maritaca.persistence.entity.AnswerByUserForm;
import br.unifesp.maritaca.persistence.entity.Configuration;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessibleByList;
import br.unifesp.maritaca.persistence.entity.FormAccessRequest;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.OAuthClient;
import br.unifesp.maritaca.persistence.entity.OAuthCode;
import br.unifesp.maritaca.persistence.entity.OAuthToken;
import br.unifesp.maritaca.persistence.entity.Report;
import br.unifesp.maritaca.persistence.entity.Tag;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.entity.UserFormTag;
import br.unifesp.maritaca.persistence.entity.VoteByForm;
import br.unifesp.maritaca.persistence.util.ConstantsPersistence;
import br.unifesp.maritaca.persistence.util.ConstantsTest;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

/**
 * Pattern DAO to create Maritaca entities and directories in filesystem
 * @author alvarohenry
 */
@Service("maritacaInitDAO")
public class MaritacaInitDAO extends AbstractDAO {
	
	private static final Log    LOG             = LogFactory.getLog(MaritacaInitDAO.class);
	private static final String ROOT_NAME 		= "root";
	private static final String ROOT_PASSWORD 	= "dc76e9f0c0006e8f919e0c515c66dbba3982f785"; //sha1 for 'root'
	private static final String ROOT_EMAIL 		= "root@maritaca.com";
	private static final String CFG_ROOT 		= "root";	
	private static final String MARITACAMOBILE = "maritacamobile";
	private static final String MARITACASECRET = "maritacasecret";
	
	public void createAllEntities() {
		if (!emHector.existColumnFamily(AnswerByUserForm.class)){
			emHector.createColumnFamily(AnswerByUserForm.class);
		}
		if (!emHector.existColumnFamily(Analytics.class)){
			emHector.createColumnFamily(Analytics.class);
		}
			
		User rootUser = null;
		if (!emHector.existColumnFamily(User.class)) {
			LOG.info("Creating Column families ...");
			emHector.createColumnFamily(User.class);
			emHector.createColumnFamily(Configuration.class);
			emHector.createColumnFamily(MaritacaList.class);				
			emHector.createColumnFamily(Form.class);
			emHector.createColumnFamily(Answer.class);
			emHector.createColumnFamily(FormAccessibleByList.class);
			emHector.createColumnFamily(GroupsByUser.class);
			emHector.createColumnFamily(OAuthClient.class);
			emHector.createColumnFamily(OAuthToken.class);
			emHector.createColumnFamily(OAuthCode.class);
			emHector.createColumnFamily(UserFormTag.class);
			emHector.createColumnFamily(Tag.class);
			emHector.createColumnFamily(FormAccessRequest.class);
			emHector.createColumnFamily(GroupsByUser.class);
			emHector.createColumnFamily(VoteByForm.class);
			emHector.createColumnFamily(Report.class);
			emHector.createColumnFamily(Analytics.class);
			
			LOG.info("Creating superuser root ...");
			rootUser = new User();
			rootUser.setFirstname(ROOT_NAME);
			rootUser.setPassword(ROOT_PASSWORD);
			rootUser.setEmail(ROOT_EMAIL);
			rootUser.setEnabled(ConstantsPersistence.USER_ENABLED);
			rootUser.setActivationCode("111");
			if (emHector.persist(rootUser)) {
				LOG.info("Save id root user in Configuration column family ...");
				Configuration config = new Configuration();
				config.setName(CFG_ROOT);
				config.setValue(rootUser.getKey().toString());
				emHector.persist(config);
				
				LOG.info("Creating root user list ...");
				createRootMaritacaList(rootUser);
				
				LOG.info("Creating oauth client data ...");
				OAuthClient oaclient = new OAuthClient();
				oaclient.setClientId(MARITACAMOBILE);
				oaclient.setSecret(MARITACASECRET);
				emHector.persist(oaclient);				
			} else {
				throw new RuntimeException("Error in persist root user");
			}			
		}
		
		LOG.info("Checking: root user ...");
		// get main user
		rootUser = getRootUser();
		if (rootUser == null) {
			throw new RuntimeException("There is not root user");
		}
		
		String populateDb = UtilsPersistence.retrieveConfigFile().getPopulateTestDb();
		if(populateDb != null && populateDb.equals("1")){
			populateTestDataBase();
		}
	}
	
	public void createDirectory(String fsPath, String dirName) {
		String path = fsPath + File.separator + dirName;
		emFileSystem.createDirectory(path);
	}

	public void close() {
		emHector.close();
	}
	
	private void populateTestDataBase() {
		List<User> users = emHector.cQuery(User.class, "email", ConstantsTest.USER1_EMAIL);
		if (users.size() != 0) {
			return;
		}
		LOG.warn("Populating database with test data");
		// add users
		addUser(ConstantsTest.USER1_FIRSTNAME, 
				ConstantsTest.USER1_LASTNAME, 
				ConstantsTest.USER1_EMAIL, 
				ConstantsTest.USER_PASSWORD_ENC);
		addUser(ConstantsTest.USER2_FIRSTNAME, 
				ConstantsTest.USER2_LASTNAME, 
				ConstantsTest.USER2_EMAIL, 
				ConstantsTest.USER_PASSWORD_ENC);
		addUser(ConstantsTest.USER3_FIRSTNAME, 
				ConstantsTest.USER3_LASTNAME, 
				ConstantsTest.USER3_EMAIL, 
				ConstantsTest.USER_PASSWORD_ENC);		
		
		User user1 = emHector.cQuery(User.class, "email", ConstantsTest.USER1_EMAIL).get(0);
		User user2 = emHector.cQuery(User.class, "email", ConstantsTest.USER2_EMAIL).get(0);
		User user3 = emHector.cQuery(User.class, "email", ConstantsTest.USER3_EMAIL).get(0);
		
		addMaritacaList(ConstantsTest.MARITACALIST1_TITLE,
						user1.getKey().toString(),
						user2.getKey().toString(),
						user3.getKey().toString());
		
		addMaritacaList(ConstantsTest.MARITACALIST2_TITLE,
				user2.getKey().toString(),
				user1.getKey().toString(),
				user3.getKey().toString());
		
		// add forms
		addForm(ConstantsTest.FORM1_TITLE,
				ConstantsTest.FORM1_XML,
				user1.getKey().toString());
		addForm(ConstantsTest.FORM2_TITLE,
				ConstantsTest.FORM2_XML,
				user1.getKey().toString());
		addForm(ConstantsTest.FORM3_TITLE,
				ConstantsTest.FORM3_XML,
				user1.getKey().toString());
	}

	private MaritacaList addMaritacaList(String... params) {
		MaritacaList list = new MaritacaList();
		list.setName(params[0]);
		list.setOwner(UUID.fromString(params[1]));
		
		List<UUID> users = new ArrayList<UUID>();
		
		if (params.length > 2) {
			users.add(UUID.fromString(params[2]));
			users.add(UUID.fromString(params[3]));
		} else {
			users.add(UUID.fromString(params[1]));
		}
		list.setUsers(users);
		
		emHector.persist(list);
		
		return list;
	}

	private void addForm(String... params) {
		Form form = new Form();
		form.setTitle(params[0]);
		form.setXml(params[1]);
		form.setUser(UUID.fromString(params[2]));
		
		emHector.persist(form);
	}

	private void addUser(String... params) {
		User user = new User();
		user.setFirstname(params[0]);
		user.setLastname(params[1]);
		user.setEmail(params[2]);
		user.setPassword(params[3]);
		user.setEnabled(ConstantsPersistence.USER_ENABLED);
		user.setActivationCode(params[3]);
		emHector.persist(user);
		
		MaritacaList list = addMaritacaList(user.getEmail(),
											user.getKey().toString());
		
		user.setMaritacaList(list.getKey());
		emHector.persist(user);
	}
	
	private User getRootUser() {
		User rootUser = null;
		List<User> users = emHector.cQuery(User.class, "email", ROOT_EMAIL);
		if (users.size() == 0){
			throw new RuntimeException("There is not root user");
		}
		if (users.size() > 1) {
			throw new RuntimeException("Inconsistence: There are multiple root users");
		}
		rootUser = users.get(0);
		return rootUser;
	}
	
	private void createRootMaritacaList(User rootUser) {
		MaritacaList list = new MaritacaList();
		list.setKey(rootUser.getKey());
		list.setOwner(rootUser.getKey());
		list.setName(rootUser.getEmail());
		emHector.persist(list);
	}
}