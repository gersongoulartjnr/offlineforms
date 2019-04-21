package br.unifesp.maritaca.persistence.entitymanager;

import static org.junit.Assert.assertFalse;
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
import br.unifesp.maritaca.persistence.entity.MaritacaList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/persistence-context-test.xml")
public class MaritacaListEntityTest extends BaseEmbededServerSetupTest {
	
	@Resource
	private EntityManagerHector emHectorImpl;
	
	@Test
	public void testUserEntity() {
		assertFalse(emHectorImpl.existColumnFamily(MaritacaList.class));
		
		assertTrue(emHectorImpl.createColumnFamily(MaritacaList.class));
		assertTrue(emHectorImpl.existColumnFamily(MaritacaList.class));
		assertFalse(emHectorImpl.createColumnFamily(MaritacaList.class));
		
		assertTrue(emHectorImpl.dropColumnFamily(MaritacaList.class));
		assertFalse(emHectorImpl.existColumnFamily(MaritacaList.class));
	}
	
	@Test
	public void persistWithUUIDTest() {
		emHectorImpl.createColumnFamily(MaritacaList.class);
		
		MaritacaList maritacaList = new MaritacaList();
		maritacaList.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		maritacaList.setDescription("asdfasdf");
		maritacaList.setOwner(UUID.randomUUID());
		
		assertTrue(emHectorImpl.persist(maritacaList));
		emHectorImpl.dropColumnFamily(MaritacaList.class);
	}

	@Test
	public void findMaritacaListTest() {
		emHectorImpl.createColumnFamily(MaritacaList.class);
		
		MaritacaList maritacaList = new MaritacaList();
		maritacaList.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		maritacaList.setDescription("asdfasdf");
		maritacaList.setOwner(UUID.randomUUID());
		
		List<UUID> list = new ArrayList<UUID>();
		UUID uuid1 = UUID.randomUUID();
		list.add(uuid1);
		UUID uuid2 = UUID.randomUUID();
		list.add(uuid2);
		UUID uuid3 = UUID.randomUUID();
		list.add(uuid3);
		maritacaList.setUsers(list);
		emHectorImpl.persist(maritacaList);
		
		MaritacaList find = emHectorImpl.find(MaritacaList.class, maritacaList.getKey());
		
		assertTrue(find.getUsers().size() == 3);
		assertTrue(find.getUsers().contains(uuid1));
		assertTrue(find.getUsers().contains(uuid2));
		assertTrue(find.getUsers().contains(uuid3));
		
		emHectorImpl.dropColumnFamily(MaritacaList.class);
	}
	
	@Test
	public void findMaritacaListQueryTest() {
		emHectorImpl.createColumnFamily(MaritacaList.class);
		
		MaritacaList maritacaList = new MaritacaList();
		maritacaList.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		maritacaList.setDescription("asdfasdf");
		maritacaList.setOwner(UUID.randomUUID());
		
		List<UUID> uuids = new ArrayList<UUID>();
		UUID uuid1 = UUID.randomUUID();
		uuids.add(uuid1);
		UUID uuid2 = UUID.randomUUID();
		uuids.add(uuid2);
		UUID uuid3 = UUID.randomUUID();
		uuids.add(uuid3);
		maritacaList.setUsers(uuids);
		emHectorImpl.persist(maritacaList);
		
		List<MaritacaList> list = emHectorImpl.cQuery(MaritacaList.class, "owner", maritacaList.getOwner().toString());
		assertTrue(list.size() == 1);
		
		MaritacaList result = list.get(0);		
		assertTrue(result.getUsers().size() == 3);
		assertTrue(result.getUsers().contains(uuid1));
		assertTrue(result.getUsers().contains(uuid2));
		assertTrue(result.getUsers().contains(uuid3));
		
		emHectorImpl.dropColumnFamily(MaritacaList.class);
	}
	
}
