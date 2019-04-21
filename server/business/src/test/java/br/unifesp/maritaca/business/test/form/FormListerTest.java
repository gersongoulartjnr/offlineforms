package br.unifesp.maritaca.business.test.form;

import java.util.List;

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
import br.unifesp.maritaca.business.base.dto.WrapperGrid;
import br.unifesp.maritaca.business.enums.OrderFormBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.persistence.dao.FormDAO;
import br.unifesp.maritaca.persistence.entity.Analytics;
import br.unifesp.maritaca.persistence.entity.Configuration;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessibleByList;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.Report;
import br.unifesp.maritaca.persistence.entity.Tag;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.entity.UserFormTag;
import br.unifesp.maritaca.persistence.permission.Policy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/business-context-test.xml")
public class FormListerTest extends BusinessTest {
	
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
		emHector.createColumnFamily(Report.class);
		emHector.createColumnFamily(Analytics.class);
		emHector.createColumnFamily(GroupsByUser.class);
		emHector.createColumnFamily(Tag.class);
		emHector.createColumnFamily(UserFormTag.class);
	}
	
	@After
	public void cleanUp(){
		super.clearProperties();
		emHector.dropColumnFamily(Configuration.class);
		emHector.dropColumnFamily(User.class);
		emHector.dropColumnFamily(MaritacaList.class);
		emHector.dropColumnFamily(Form.class);
		emHector.dropColumnFamily(FormAccessibleByList.class);
		emHector.dropColumnFamily(Report.class);
		emHector.dropColumnFamily(Analytics.class);
		emHector.dropColumnFamily(GroupsByUser.class);
		emHector.dropColumnFamily(Tag.class);
		emHector.dropColumnFamily(UserFormTag.class);
	}
	
	@Test
	public void listFormsForUser1() {
		super.createUserAndList();
		createForm(USER1_EMAIL, "privateForm", null, null, null);
		createForm(USER1_EMAIL, "publicForm", Policy.PUBLIC, null, null);
		createForm(USER1_EMAIL, "shaHierarchicalForm", Policy.SHARED_HIERARCHICAL, COD_GROUP1, null);
		createForm(USER1_EMAIL, "shaSocialForm", Policy.SHARED_SOCIAL, COD_GROUP1, null);
		//
		OrderFormBy orderBy = OrderFormBy.DATE;
		OrderType orderType = OrderType.ASC;
		WrapperGrid<FormDTO> ownForms = managementForm.getOwnFormsByUser(new UserDTO(USER1_EMAIL),  orderBy, orderType, page, numRows);
		
		Assert.assertNotNull(ownForms);
		Assert.assertNotNull(ownForms.getRows());
		Assert.assertEquals(4, ownForms.getRows().size());
		
		List<FormDTO> forms = ownForms.getRows();
		for(FormDTO f : forms) {
			Assert.assertTrue("read(true)",   f.getFormPermission().getRead());
			Assert.assertTrue("update(true)", f.getFormPermission().getUpdate());
			Assert.assertTrue("delete(true)", f.getFormPermission().getRemove());
			Assert.assertTrue("share(true)",  f.getFormPermission().getShare());
		}
		
		WrapperGrid<FormDTO> sharedForms	= managementForm.getSharedFormsByUser(new UserDTO(USER1_EMAIL),  orderBy, orderType, page, numRows);
		
		Assert.assertNotNull(sharedForms);
		Assert.assertNotNull(sharedForms.getRows());
		Assert.assertEquals(0, sharedForms.getRows().size());
	}
	
	@Test
    public void listFormsForUser2() {
        super.createUserAndList();
        createForm(USER1_EMAIL, "privateForm", null, null, null);
        createForm(USER1_EMAIL, "publicForm", Policy.PUBLIC, null, null);
        createForm(USER1_EMAIL, "shaHierarchicalForm", Policy.SHARED_HIERARCHICAL, COD_GROUP1, null);
        createForm(USER1_EMAIL, "shaSocialForm", Policy.SHARED_SOCIAL, COD_GROUP1, null);
        
        OrderFormBy orderBy = OrderFormBy.DATE; // TODO
		OrderType orderType = OrderType.ASC; // TODO
        WrapperGrid<FormDTO> ownForms = managementForm.getOwnFormsByUser(new UserDTO(USER2_EMAIL),  orderBy, orderType, page, numRows);
		Assert.assertNull(ownForms);
		//Assert.assertNull(ownForms.getRows());
        
        WrapperGrid<FormDTO> sharedForms = managementForm.getSharedFormsByUser(new UserDTO(USER2_EMAIL), orderBy, orderType, page, numRows);
        Assert.assertNotNull(sharedForms);
		Assert.assertNotNull(sharedForms.getRows());
		List<FormDTO> forms = sharedForms.getRows();
		for(FormDTO f : forms) {
			Assert.assertTrue("read(true)",     f.getFormPermission().getRead());
			Assert.assertFalse("update(false)", f.getFormPermission().getUpdate());
			Assert.assertFalse("delete(false)", f.getFormPermission().getRemove());
			Assert.assertFalse("share(false)",  f.getFormPermission().getShare());	
		}
	}
	
	private void createForm(String userEmail, String formTitle, Policy policy, String group1, String group2) {		
		String title = formTitle;
		String xml = "<form>"+formTitle+"</form>";
		FormDTO formDTO = new FormDTO();
		formDTO.setTitle(title);
		formDTO.setXml(xml);
		Message message = managementForm.saveForm(new UserDTO(userEmail), formDTO);
		Form form;
		if (message.getData() instanceof FormDTO) {
			FormDTO tmpDTO = (FormDTO) message.getData();
			form = formDAO.getFormByUrl(tmpDTO.getUrl(), false);		
		} else {
			form = formDAO.getFormByUrl(message.getData().toString(), false);
		}
		if(policy != null) {
			formDTO.setUrl(form.getUrl());
			if(group1 != null) {
				formDTO.setGroupsList(getGroupsList(COD_GROUP1));
			}
			formDTO.setStrPolicy(policy.toString());
			managementForm.updateFormFromPolicyEditor(formDTO, new UserDTO(userEmail));
		}
	}
}