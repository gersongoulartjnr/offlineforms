package br.unifesp.maritaca.business.test.form;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.business.BusinessTest;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.persistence.dao.FormDAO;
import br.unifesp.maritaca.persistence.entity.Configuration;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessibleByList;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.Tag;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.entity.UserFormTag;
import br.unifesp.maritaca.persistence.permission.Policy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/business-context-test.xml")
public class FormEditorTest extends BusinessTest {
	
	@Autowired private ManagementForm managementForm;
	@Autowired private FormDAO formDAO; 
	
	@Before
	public void setUp() {
		super.setProperties();
		emHector.createColumnFamily(Configuration.class);
		emHector.createColumnFamily(User.class);
		emHector.createColumnFamily(MaritacaList.class);
		emHector.createColumnFamily(Form.class);
		emHector.createColumnFamily(FormAccessibleByList.class);
		emHector.createColumnFamily(GroupsByUser.class);
		emHector.createColumnFamily(Tag.class);
		emHector.createColumnFamily(UserFormTag.class);
		managementForm.setBuildApk(false);
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
		emHector.dropColumnFamily(Tag.class);
		emHector.dropColumnFamily(UserFormTag.class);
	}
	
	@Test
	public void createPrivateForm() {
		super.createUserAndList();
		String title = "privateForm";
		String xml = "<form>private</form>";
		FormDTO privateDTO = new FormDTO();
		privateDTO.setTitle(title);
		privateDTO.setXml(xml);
		Message message = managementForm.saveForm(new UserDTO(USER1_EMAIL), privateDTO);
		Assert.assertNotNull(message);
		Assert.assertNotNull(message.getData());
		Form form = getFormByMessage(message);
		Assert.assertNotNull(form);
		Assert.assertEquals(Policy.PRIVATE.getId(), form.getPolicy().getId());
		Assert.assertEquals(0, form.getLists()!=null?form.getLists().size():0);
	}

	@Test
	public void createPublicForm() {
		super.createUserAndList();
		String title = "publicForm";
		String xml = "<form>public</form>";
		FormDTO publicFormDTO = new FormDTO();
		publicFormDTO.setTitle(title);
		publicFormDTO.setXml(xml);
		Message message = managementForm.saveForm(new UserDTO(USER1_EMAIL), publicFormDTO);
		Assert.assertNotNull(message);
		Assert.assertNotNull(message.getData());
		Form form = getFormByMessage(message);
		Assert.assertNotNull(form);
		Assert.assertEquals(Policy.PRIVATE.getId(), form.getPolicy().getId());		
		
		publicFormDTO.setUrl(form.getUrl());
		publicFormDTO.setStrPolicy(Policy.PUBLIC.toString());
		managementForm.updateFormFromPolicyEditor(publicFormDTO, new UserDTO(USER1_EMAIL));
		form = getFormByMessage(message);
		Assert.assertNotNull(form);
		Assert.assertEquals(Policy.PUBLIC.getId(), form.getPolicy().getId());
		Assert.assertEquals(0, form.getLists()!=null?form.getLists().size():0);
	}
	
	@Test
	public void createSharedHierarchicalForm() {
		super.createUserAndList();
		String title = "shareHierarchicalForm";
		String xml = "<form>shareHierarchical</form>";
		FormDTO shaHieFormDTO = new FormDTO();
		shaHieFormDTO.setTitle(title);
		shaHieFormDTO.setXml(xml);
		Message message = managementForm.saveForm(new UserDTO(USER1_EMAIL), shaHieFormDTO);
		Assert.assertNotNull(message);
		Assert.assertNotNull(message.getData());
		Form form = getFormByMessage(message);
		Assert.assertNotNull(form);
		Assert.assertEquals(Policy.PRIVATE.getId(), form.getPolicy().getId());		
		
		shaHieFormDTO.setUrl(form.getUrl());
		shaHieFormDTO.setStrPolicy(Policy.SHARED_HIERARCHICAL.toString());
		shaHieFormDTO.setGroupsList(getGroupsList(COD_GROUP1));
		managementForm.updateFormFromPolicyEditor(shaHieFormDTO, new UserDTO(USER1_EMAIL));
		form = getFormByMessage(message);
		Assert.assertNotNull(form);
		Assert.assertEquals(Policy.SHARED_HIERARCHICAL.getId(), form.getPolicy().getId());
		Assert.assertEquals(1, form.getLists()!=null?form.getLists().size():0);
	}
	
	@Test
	public void createSharedSocialForm() {
		super.createUserAndList();
		String title = "shareSocialForm";
		String xml = "<form>shareSocial</form>";
		FormDTO shaHieFormDTO = new FormDTO();
		shaHieFormDTO.setTitle(title);
		shaHieFormDTO.setXml(xml);
		Message message = managementForm.saveForm(new UserDTO(USER1_EMAIL), shaHieFormDTO);
		Assert.assertNotNull(message);
		Assert.assertNotNull(message.getData());
		Form form = getFormByMessage(message);
		Assert.assertNotNull(form);
		Assert.assertEquals(Policy.PRIVATE.getId(), form.getPolicy().getId());		
		
		shaHieFormDTO.setUrl(form.getUrl());
		shaHieFormDTO.setStrPolicy(Policy.SHARED_SOCIAL.toString());
		shaHieFormDTO.setGroupsList(getGroupsList(COD_GROUP1));
		managementForm.updateFormFromPolicyEditor(shaHieFormDTO, new UserDTO(USER1_EMAIL));
		form = getFormByMessage(message);
		Assert.assertNotNull(form);
		Assert.assertEquals(Policy.SHARED_SOCIAL.getId(), form.getPolicy().getId());
		Assert.assertEquals(1, form.getLists()!=null?form.getLists().size():0);
	}	
	
	private Form getFormByMessage(Message message) {
		Form form;
		if (message.getData() instanceof FormDTO) {
			FormDTO tmpDTO = (FormDTO) message.getData();
			form = formDAO.getFormByUrl(tmpDTO.getUrl(), false);
		} else {
			form = formDAO.getFormByUrl(message.getData().toString(), false);;
		}
		return form;
	}
}
