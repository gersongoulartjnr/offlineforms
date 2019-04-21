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
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.form.dto.FormWSDTO;
import br.unifesp.maritaca.persistence.dao.FormDAO;
import br.unifesp.maritaca.persistence.entity.Configuration;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessibleByList;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/business-context-test.xml")
public class ApkBuilderTest extends BusinessTest {

	@Autowired private ManagementForm managementForm;
	@Autowired private FormDAO formDAO;
	
	private static final String FORM_XML = 
				"<form>" +
					"<title>testForm</title>" +
					"<questions>" +
						"<text id=\"0\" next=\"1\" required=\"true\">" +
							"<label>label text</label>" +
							"<help>help text</help>" +
							"<size>100</size>" +
							"<default>default text</default>" +
						"</text>" +
						"<number id=\"4\" next=\"5\" required=\"true\" min=\"0\" max=\"10\">" + 
							"<label>label number</label>" +
							"<help>help number</help>" +
							"<default>0</default>" +
						"</number>" +						
					"</questions>" +
				"</form>";
	
	@Before
	public void setUp() {
		super.setProperties();
		emHector.createColumnFamily(Configuration.class);
		emHector.createColumnFamily(User.class);
		emHector.createColumnFamily(MaritacaList.class);
		emHector.createColumnFamily(Form.class);
		emHector.createColumnFamily(FormAccessibleByList.class);
		emHector.createColumnFamily(GroupsByUser.class);
		super.createUserAndList();
	}
	
	@After
	public void cleanUp(){
		super.clearProperties();
		emHector.dropColumnFamily(Configuration.class);
		emHector.dropColumnFamily(User.class);
		emHector.dropColumnFamily(MaritacaList.class);
		emHector.dropColumnFamily(Form.class);
		emHector.dropColumnFamily(FormAccessibleByList.class);
		emHector.dropColumnFamily(GroupsByUser.class);
	}
	
	@Test
	public void testGetUnmarshalledFormFromXML() {
		Form form = new Form();
		form.setTitle("testForm");
		form.setUrl(UtilsPersistence.randomString());
		form.setXml(FORM_XML);
		formDAO.saveForm(form);
		
		FormDTO formDTO = new FormDTO();
		formDTO.setKey(form.getKey());
		
		FormWSDTO formWSDTO = managementForm.getUnmarshalledFormFromXML(form);
		
		Assert.assertEquals(form.getTitle(), formWSDTO.getTitle());
	}
}