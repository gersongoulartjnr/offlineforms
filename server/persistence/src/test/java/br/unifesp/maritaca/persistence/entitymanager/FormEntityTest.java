package br.unifesp.maritaca.persistence.entitymanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.persistence.EntityManagerHector;
import br.unifesp.maritaca.persistence.cassandra.BaseEmbededServerSetupTest;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/persistence-context-test.xml")
public class FormEntityTest extends BaseEmbededServerSetupTest {
	
	@Resource
	private EntityManagerHector emHectorImpl;
	
	@Test
	public void testUserEntity() {
		assertFalse(emHectorImpl.existColumnFamily(Form.class));
		
		assertTrue(emHectorImpl.createColumnFamily(Form.class));
		assertTrue(emHectorImpl.existColumnFamily(Form.class));
		assertFalse(emHectorImpl.createColumnFamily(Form.class));
		
		assertTrue(emHectorImpl.dropColumnFamily(Form.class));
		assertFalse(emHectorImpl.existColumnFamily(Form.class));
	}

	@Test
	public void persistWithoutCreate() {
		Form form = new Form();
		form.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		form.setUser(UUID.randomUUID());
		form.setTitle("title");
		form.setUrl("asdfasdfasdf");
		assertFalse(emHectorImpl.persist(form));
	}
	
	@Test
	public void persistWithUUIDTest() {
		emHectorImpl.createColumnFamily(Form.class);
		
		Form form = new Form();
		form.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		form.setUser(UUID.randomUUID());
		form.setTitle("title");
		form.setUrl("asdfasdfasdf");
		
		assertTrue(emHectorImpl.persist(form));
		emHectorImpl.dropColumnFamily(Form.class);
	}
	
	@Test
	public void persitMaritacaListsWithJSONTest() {
		emHectorImpl.createColumnFamily(Form.class);
		
		Form form = new Form();
		form.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		form.setUser(UUID.randomUUID());
		form.setTitle("title");
		form.setUrl("asdfasdfasdf");
		
		List<UUID> lists = new ArrayList<UUID>();
		UUID uuid1 = UUID.randomUUID();
		lists.add(uuid1);
		UUID uuid2 = UUID.randomUUID();
		lists.add(uuid2);
		UUID uuid3 = UUID.randomUUID();
		lists.add(uuid3);
		form.setLists(lists);
		
		assertTrue(emHectorImpl.persist(form));
		
		Form formFind = emHectorImpl.find(Form.class, form.getKey());
		
		assertTrue(formFind.getLists().size() == 3);
		assertTrue(formFind.getLists().contains(uuid1));
		assertTrue(formFind.getLists().contains(uuid2));
		assertTrue(formFind.getLists().contains(uuid3));
		
		emHectorImpl.dropColumnFamily(Form.class);
	}
	
	@Test
	public void findTest() {
		emHectorImpl.createColumnFamily(Form.class);
		
		Form form = new Form();
		form.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		form.setUser(UUID.randomUUID());
		form.setTitle("title");
		form.setUrl("asdfasdfasdf");
		
		assertTrue(emHectorImpl.persist(form));
		
		Form formFind = emHectorImpl.find(Form.class, form.getKey());
		
		assertTrue(form.getTitle().equals(formFind.getTitle()));
		emHectorImpl.dropColumnFamily(Form.class);
	}
	
	@Test
	public void findFormByURLTest() {
		emHectorImpl.createColumnFamily(Form.class);
		
		Form form = new Form();
		form.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		form.setUser(UUID.randomUUID());
		form.setTitle("title");
		form.setUrl("asdfasdfasdf");
		
		assertTrue(emHectorImpl.persist(form));
		
		List<Form> forms = emHectorImpl.cQuery(Form.class, "url", form.getUrl());
		Form formResult = forms.get(0);
		
		assertTrue(form.getUrl().equals(formResult.getUrl()));
		emHectorImpl.dropColumnFamily(Form.class);
	}
	
	@Test
	public void findFormByUserTest() {
		emHectorImpl.createColumnFamily(Form.class);
		
		Form form = new Form();
		form.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
			User user = new User();
			user.setKey(UUID.randomUUID());
		form.setUser(user.getKey());
		form.setTitle("title");
		form.setUrl("asdfasdfasdf");
		form.setXml("<xml version=1.0><form></form>");
		
		List<UUID> lists = new ArrayList<UUID>();
		UUID uuid1 = UUID.randomUUID();
		lists.add(uuid1);
		UUID uuid2 = UUID.randomUUID();
		lists.add(uuid2);
		UUID uuid3 = UUID.randomUUID();
		lists.add(uuid3);
		form.setLists(lists);
		
		assertTrue(emHectorImpl.persist(form));
		
		// all columns
		List<Form> forms = emHectorImpl.cQuery(Form.class, "user", form.getUser().toString());
		Form formResult = forms.get(0);
		
		assertTrue(user.getKey().toString().equals(formResult.getUser().toString()));
		assertNotNull(formResult.getXml());
		assertTrue(formResult.getLists().size() == 3);
		assertTrue(formResult.getLists().contains(uuid1));
		emHectorImpl.dropColumnFamily(Form.class);
	}
	
}
