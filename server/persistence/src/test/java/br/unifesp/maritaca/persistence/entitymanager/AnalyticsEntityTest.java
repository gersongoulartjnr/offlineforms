package br.unifesp.maritaca.persistence.entitymanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import javax.annotation.Resource;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.persistence.EntityManagerHector;
import br.unifesp.maritaca.persistence.cassandra.BaseEmbededServerSetupTest;
import br.unifesp.maritaca.persistence.entity.Analytics;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/persistence-context-test.xml")
public class AnalyticsEntityTest extends BaseEmbededServerSetupTest {

	@Resource
	private EntityManagerHector emHectorImpl;
	
	@Test
	public void testAnalyticsEntity() {
		assertTrue(emHectorImpl.createColumnFamily(Analytics.class));
		assertTrue(emHectorImpl.existColumnFamily(Analytics.class));
		assertFalse(emHectorImpl.createColumnFamily(Analytics.class));
		
		assertTrue(emHectorImpl.dropColumnFamily(Analytics.class));
		assertFalse(emHectorImpl.existColumnFamily(Analytics.class));		
	}
	
	@Test
	public void persistWithoutCreate() {
		Analytics analytics = new Analytics();
		analytics.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		analytics.setUser(UUID.randomUUID());
		analytics.setName("Name");		
		assertFalse(emHectorImpl.persist(analytics));
	}
	
	@Test
	public void persistWithUUIDTest() {
		emHectorImpl.createColumnFamily(Analytics.class);
		
		Analytics analytics = new Analytics();
		analytics.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		analytics.setUser(UUID.randomUUID());
		analytics.setName("Name");
		
		assertTrue(emHectorImpl.persist(analytics));
		emHectorImpl.dropColumnFamily(Analytics.class);
	}
}