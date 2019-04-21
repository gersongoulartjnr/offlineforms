package br.unifesp.maritaca.business.test.form;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.business.BusinessTest;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.ManagementFormAccessRequest;
import br.unifesp.maritaca.business.form.dto.FormAccessRequestDTO;
import br.unifesp.maritaca.persistence.entity.Configuration;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessRequest;
import br.unifesp.maritaca.persistence.entity.FormAccessibleByList;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.permission.RequestStatusType;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/business-context-test.xml")
public class ManagementFormAccessRequestTest extends BusinessTest {

	@Autowired private ManagementFormAccessRequest managFormAccessRequest;
	@Autowired private ManagementForm managementForm;
	
	private Form form;
	private User owner;
	
	@Before
	public void setUp() {
		super.setProperties();
		emHector.createColumnFamily(Configuration.class);
		emHector.createColumnFamily(User.class);
		emHector.createColumnFamily(MaritacaList.class);
		emHector.createColumnFamily(Form.class);
		emHector.createColumnFamily(FormAccessibleByList.class);
		emHector.createColumnFamily(GroupsByUser.class);
		emHector.createColumnFamily(FormAccessRequest.class);
		managementForm.setBuildApk(false);
		
		super.createUserAndList();
		owner = userDAO.findUserByEmail(USER1_EMAIL);
		form = new Form();
		form.setTitle("privateForm");
		form.setUser(owner.getKey());
		form.setUrl(UtilsPersistence.randomString());
		form.setXml("<form>private</form>");
		emHector.persist(form);
	}
	
	@After
	public void cleanUp() {
		super.clearProperties();
		emHector.dropColumnFamily(Configuration.class);
		emHector.dropColumnFamily(User.class);
		emHector.dropColumnFamily(MaritacaList.class);
		emHector.dropColumnFamily(Form.class);
		emHector.dropColumnFamily(FormAccessibleByList.class);
		emHector.dropColumnFamily(GroupsByUser.class);
		emHector.dropColumnFamily(FormAccessRequest.class);
	}
	
	@Test
	public void testAskForPermission() {
		managFormAccessRequest.askForPermission(new UserDTO(USER2_EMAIL), form.getKey().toString());
		
		List<FormAccessRequest> request = emHector.cQuery(FormAccessRequest.class, "owner", owner.getKey().toString());
		Assert.assertTrue(request.size() == 1);
		Assert.assertTrue(request.get(0).getFormUrl().equals(form.getUrl()));
		Assert.assertTrue(request.get(0).getOwner().equals(owner.getKey()));
		
		owner = userDAO.findUserByEmail(USER2_EMAIL);
		Assert.assertTrue(request.get(0).getUser().equals(owner.getKey()));
	}
	
	@Test
	public void testGetFormAccessRequestByUser() {
		
		managFormAccessRequest.askForPermission(new UserDTO(USER2_EMAIL), form.getKey().toString());
		List<FormAccessRequestDTO> farsDTO = managFormAccessRequest.getFormAccessRequestByUser(new UserDTO(USER1_EMAIL));
		
		Assert.assertTrue(farsDTO.size() == 1);
		Assert.assertTrue(farsDTO.get(0).getFormUrl().equals(form.getUrl()));
		
		owner = userDAO.findUserByEmail(USER2_EMAIL);
		Assert.assertTrue(farsDTO.get(0).getUserEmail().equals(owner.getEmail()));
		Assert.assertTrue(farsDTO.get(0).getUserFullName().equals(owner.getFullName()));
	}
	
	@Test
	public void testSendNotificationOfAccepting() {
		User user = userDAO.findUserByEmail(USER2_EMAIL);
		FormAccessRequest far = new FormAccessRequest();
		far.setFormUrl(form.getUrl());
		far.setOwner(owner.getKey());
		far.setStatus(RequestStatusType.PENDING);
		far.setUser(user.getKey());
		emHector.persist(far);
		
		List<String> urls = new ArrayList<String>();
		urls.add(form.getUrl());
		managFormAccessRequest.sendNotificationOfAccepting(new UserDTO(USER1_EMAIL), urls);
		
		FormAccessRequest _far = emHector.find(FormAccessRequest.class, far.getKey());
		
		Assert.assertFalse(_far.getStatus().equals(far.getStatus()));
	}
}
