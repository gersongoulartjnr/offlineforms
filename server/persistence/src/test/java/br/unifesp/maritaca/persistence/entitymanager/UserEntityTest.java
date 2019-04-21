package br.unifesp.maritaca.persistence.entitymanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.persistence.EntityManagerHector;
import br.unifesp.maritaca.persistence.cassandra.BaseEmbededServerSetupTest;
import br.unifesp.maritaca.persistence.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/persistence-context-test.xml")
public class UserEntityTest extends BaseEmbededServerSetupTest {
	
	@Autowired
	private EntityManagerHector entityManager;
	
	@Test
	public void testUserEntity() {
		assertFalse(entityManager.existColumnFamily(User.class));
		
		assertTrue(entityManager.createColumnFamily(User.class));
		assertTrue(entityManager.existColumnFamily(User.class));
		assertFalse(entityManager.createColumnFamily(User.class));
		
		assertTrue(entityManager.dropColumnFamily(User.class));
		assertFalse(entityManager.existColumnFamily(User.class));
	}

	@Test
	public void persistWithoutCreate() {
		UUID uuid = UUID.randomUUID();
		User user = new User();
		user.setKey(uuid);
		user.setEmail("persist@maritaca.com");
		user.setFirstname("mari");
		user.setLastname("taca");
		
		assertFalse(entityManager.persist(user));
	}

	@Test
	public void persistWithUUIDTest() {
		entityManager.createColumnFamily(User.class);
		
		UUID uuid = UUID.randomUUID();
		User user = new User();
		user.setKey(uuid);
		user.setEmail("persistwith@maritaca.com");
		user.setFirstname("mari");
		user.setLastname("taca");
		
		assertTrue(entityManager.persist(user));
		entityManager.dropColumnFamily(User.class);
	}
	
	@Test
	public void persistWithoutUUIDTest() {
		entityManager.createColumnFamily(User.class);
		
		User user = new User();
		user.setEmail("persistwithout@maritaca.com");
		user.setFirstname("mari");
		user.setLastname("taca");
		
		assertTrue(entityManager.persist(user));
		entityManager.dropColumnFamily(User.class);
	}
	
	@Test
	public void findTest() {
		entityManager.createColumnFamily(User.class);
		
		UUID uuid = UUID.randomUUID();
		User user = new User();
		user.setKey(uuid);
		user.setEmail("find@maritaca.com");
		user.setFirstname("mari");
		user.setLastname("taca");		
		entityManager.persist(user);
		
		User userFind = entityManager.find(User.class, uuid);
		
		assertTrue(user.getEmail().equals(userFind.getEmail()));
		entityManager.dropColumnFamily(User.class);
	}
	
	@Test
	public void findNonExistentUserTest() {
		entityManager.createColumnFamily(User.class);
		User userFind = entityManager.find(User.class, UUID.randomUUID());
		assertNull(userFind);
	}
	
	@Test
	public void findUserByEmailTest() {
		entityManager.createColumnFamily(User.class);
		
		UUID uuid = UUID.randomUUID();
		User user = new User();
		user.setKey(uuid);
		user.setEmail("finduserbyemail@maritaca.com");
		user.setFirstname("mari");
		user.setLastname("taca");		
		entityManager.persist(user);
		
		// all columns
		List<User> users = entityManager.cQuery(User.class, "email", user.getEmail());
		
		assertTrue(user.getEmail().equals(users.get(0).getEmail()));
		assertNotNull(users.get(0).getLastname());
		entityManager.dropColumnFamily(User.class);
	}
	
	@Test 
	public void findJustMinimalTest() {
		entityManager.createColumnFamily(User.class);
		
		UUID uuid = UUID.randomUUID();
		User user = new User();
		user.setKey(uuid);
		user.setEmail("findjustminimal@maritaca.com");
		user.setFirstname("mari");
		user.setLastname("taca");
		user.setPassword("123456");
		entityManager.persist(user);
		
		List<User> users = entityManager.cQuery(User.class, "email", user.getEmail(), true);
		
		assertNull(users.get(0).getLastname());
		entityManager.dropColumnFamily(User.class);		
	}
	
	@Test
	public void listAllTest() {
		entityManager.createColumnFamily(User.class);
		
		User user1 = new User();
		user1.setKey(UUID.randomUUID());
		user1.setEmail("maritaca1@maritaca.com");
		user1.setFirstname("mari");
		user1.setLastname("taca");
		entityManager.persist(user1);
		
		User user2 = new User();
		user2.setKey(UUID.randomUUID());
		user2.setEmail("maritaca2@maritaca.com");
		user2.setFirstname("mari");
		user2.setLastname("taca");
		entityManager.persist(user2);
		
		User user3 = new User();
		user3.setKey(UUID.randomUUID());
		user3.setEmail("maritaca3@maritaca.com");
		user3.setFirstname("mari");
		user3.setLastname("taca");
		entityManager.persist(user3);
		
		List<User> users = entityManager.listAll(User.class);
		assertTrue(users.size() == 3);
	}
	
	@Test
	public void listAllWithRangeTest() {
		entityManager.createColumnFamily(User.class);
		
		int total = 105;
		User user;
		for(int i = 0; i < total; i++){
			user = new User();
			user.setEmail("user_"+i+"@maritaca.com");
			user.setFirstname("Mari_"+i);
			user.setLastname("taca_"+i);
			
			assertTrue(entityManager.persist(user));
		}
		
		List<User> users = entityManager.listAll(User.class, total, null);
		assertTrue("There is "+users.size() +" and not "+ total, users.size() == total);
		
		entityManager.dropColumnFamily(User.class);
	}
}