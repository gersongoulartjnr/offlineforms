package br.unifesp.maritaca.persistence.entitymanager;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.persistence.EntityManagerHector;
import br.unifesp.maritaca.persistence.cassandra.BaseEmbededServerSetupTest;
import br.unifesp.maritaca.persistence.entity.FormAccessRequest;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/persistence-context-test.xml")
public class FormAccessRequestTest extends BaseEmbededServerSetupTest {
	
	@Resource
	private EntityManagerHector entityManager; 
	
	@Test
	public void persistPermissionRequest(){
		entityManager.createColumnFamily(FormAccessRequest.class);
		FormAccessRequest request = new FormAccessRequest(UtilsPersistence.randomString(), UUID.randomUUID(), UUID.randomUUID());
		entityManager.persist(request);
		
		FormAccessRequest request1 = entityManager.find(FormAccessRequest.class, request.getKey());
				
		assertTrue(request1.getStatus().getId() == request.getStatus().getId());
		entityManager.dropColumnFamily(FormAccessRequest.class);
	}
}