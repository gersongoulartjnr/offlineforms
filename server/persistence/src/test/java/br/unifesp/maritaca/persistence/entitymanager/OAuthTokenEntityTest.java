package br.unifesp.maritaca.persistence.entitymanager;

import static org.junit.Assert.assertNull;
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
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.OAuthToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/persistence-context-test.xml")
public class OAuthTokenEntityTest  extends BaseEmbededServerSetupTest {
	
	@Resource
	private EntityManagerHector emHectorImpl;

	@Test
	public void persistOAuthTokenCreate() {
		emHectorImpl.createColumnFamily(OAuthToken.class);
		
		OAuthToken oauthToken = new OAuthToken();
		oauthToken.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		oauthToken.setRefreshToken(UUID.randomUUID().toString());
		oauthToken.setAccessToken(UUID.randomUUID().toString());
		oauthToken.setUser(UUID.randomUUID());
		oauthToken.setClientId(UUID.randomUUID());
		assertTrue(emHectorImpl.persist(oauthToken));
		
		assertTrue(emHectorImpl.dropColumnFamily(OAuthToken.class));
	}
	
	@Test
	public void findTest() {
		emHectorImpl.createColumnFamily(OAuthToken.class);
		
		OAuthToken oauthToken = new OAuthToken();
		oauthToken.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		oauthToken.setRefreshToken(UUID.randomUUID().toString());
		oauthToken.setAccessToken(UUID.randomUUID().toString());
		oauthToken.setUser(UUID.randomUUID());
		oauthToken.setClientId(UUID.randomUUID());
		assertTrue(emHectorImpl.persist(oauthToken));
		
		OAuthToken oauthResult = emHectorImpl.find(OAuthToken.class, oauthToken.getKey());
		
		assertTrue(oauthToken.getAccessToken().equals(oauthResult.getAccessToken()));
		
		emHectorImpl.dropColumnFamily(Form.class);
	}
	
	@Test
	public void findExpiredAccessTokenTest() {
		emHectorImpl.createColumnFamily(OAuthToken.class);
		
		OAuthToken oauthToken = new OAuthToken();
		oauthToken.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		oauthToken.setRefreshToken(UUID.randomUUID().toString());
		oauthToken.setAccessToken(UUID.randomUUID().toString());
		oauthToken.setUser(UUID.randomUUID());
		oauthToken.setClientId(UUID.randomUUID());
		oauthToken.setAccessTokenTTL(2); // time to live two seconds
		assertTrue(emHectorImpl.persist(oauthToken));
		
		try {
			Thread.sleep(3000); // three seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		OAuthToken oauthResult = emHectorImpl.find(OAuthToken.class, oauthToken.getKey());
		
		assertNull(oauthResult.getAccessToken());
		
		emHectorImpl.dropColumnFamily(Form.class);
	}
	
}
