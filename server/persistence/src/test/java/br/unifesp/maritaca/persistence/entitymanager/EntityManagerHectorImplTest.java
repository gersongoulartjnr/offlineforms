package br.unifesp.maritaca.persistence.entitymanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.persistence.EntityManagerHector;
import br.unifesp.maritaca.persistence.cassandra.BaseEmbededServerSetupTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/persistence-context-test.xml")
public class EntityManagerHectorImplTest extends BaseEmbededServerSetupTest {

	@Resource
	private EntityManagerHector emHectorImpl;

	@Test
	public void testCreateExistsDeleteColumnFamily() {
		assertFalse(emHectorImpl.existColumnFamily(CFforTesting.class));
		
		assertTrue(emHectorImpl.createColumnFamily(CFforTesting.class));
		assertTrue(emHectorImpl.existColumnFamily(CFforTesting.class));
		assertFalse(emHectorImpl.createColumnFamily(CFforTesting.class));
		
		assertTrue(emHectorImpl.dropColumnFamily(CFforTesting.class));
		assertFalse(emHectorImpl.existColumnFamily(CFforTesting.class));
	}
	
	public void testComplexCQuery() {
		emHectorImpl.createColumnFamily(CFforTesting.class);
		emHectorImpl.persist(new CFforTesting(UUID.randomUUID(), "column1", "bigData1", null));
		emHectorImpl.persist(new CFforTesting(UUID.randomUUID(), "column2", "bigData2", null));
		emHectorImpl.persist(new CFforTesting(UUID.randomUUID(), "column3", "bigData3", null));
		
		List<CFforTesting> listCF = emHectorImpl.cQuery(CFforTesting.class, new String[]{"name", "bigData"}, new String[]{"column1", "bigData1"}, null, null, false);
		assertEquals(1, listCF.size());
		
		listCF = emHectorImpl.cQuery(CFforTesting.class, new String[]{"name", "bigData"}, new String[]{"column1", "bigData2"}, null, null, false);
		assertEquals(0, listCF.size());
		emHectorImpl.dropColumnFamily(CFforTesting.class);
	}
}