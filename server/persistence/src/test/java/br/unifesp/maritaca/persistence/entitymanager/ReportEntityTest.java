package br.unifesp.maritaca.persistence.entitymanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import br.unifesp.maritaca.persistence.entity.Report;
import br.unifesp.maritaca.persistence.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/persistence-context-test.xml")
public class ReportEntityTest extends BaseEmbededServerSetupTest {

	@Resource
	private EntityManagerHector emHectorImpl;
	
	@Test
	public void testReportEntity() {
		assertTrue(emHectorImpl.createColumnFamily(Report.class));
		assertTrue(emHectorImpl.existColumnFamily(Report.class));
		assertFalse(emHectorImpl.createColumnFamily(Report.class));
		
		assertTrue(emHectorImpl.dropColumnFamily(Report.class));
		assertFalse(emHectorImpl.existColumnFamily(Report.class));		
	}
	
	@Test
	public void persistWithoutCreate() {
		Report report = new Report();
		report.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		report.setUser(UUID.randomUUID());
		report.setName("Name");		
		assertFalse(emHectorImpl.persist(report));
	}
	
	@Test
	public void persistWithUUIDTest() {
		emHectorImpl.createColumnFamily(Report.class);
		
		Report report = new Report();
		report.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		report.setUser(UUID.randomUUID());
		report.setName("Name");	
		report.setId("0001");		
		
		assertTrue(emHectorImpl.persist(report));
		emHectorImpl.dropColumnFamily(Report.class);
	}
	
	@Test
	public void findTest() {
		emHectorImpl.createColumnFamily(Report.class);
		
		Report report = new Report();
		report.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		report.setUser(UUID.randomUUID());
		report.setName("Name");	
		report.setId("0001");
		report.setXml("<xml version=1.0><report></report>");
		
		assertTrue(emHectorImpl.persist(report));
		
		Report findReport = emHectorImpl.find(Report.class, report.getKey());
		
		assertTrue(report.getName().equals(findReport.getName()));
		emHectorImpl.dropColumnFamily(Report.class);
	}
	
	@Test
	public void findReportByIdTest() {
		emHectorImpl.createColumnFamily(Report.class);
		
		Report report = new Report();
		report.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		report.setUser(UUID.randomUUID());
		report.setName("Name");	
		report.setId("0001");
		report.setXml("<xml version=1.0><report></report>");
		
		assertTrue(emHectorImpl.persist(report));
		
		List<Report> reports = emHectorImpl.cQuery(Report.class, "id", report.getId());
		Report findReport = reports.get(0);
		
		assertTrue(report.getId().equals(findReport.getId()));
		emHectorImpl.dropColumnFamily(Report.class);
	}
	
	@Test
	public void findReportByUserTest() {
		emHectorImpl.createColumnFamily(Report.class);
		
		Report report = new Report();
		report.setKey(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
			User user = new User();
			user.setKey(UUID.randomUUID());
		report.setUser(user.getKey());
		report.setName("Name");	
		report.setId("0001");
		report.setXml("<xml version=1.0><report></report>");
		
		assertTrue(emHectorImpl.persist(report));
		
		List<Report> reports = emHectorImpl.cQuery(Report.class, "user", report.getUser().toString());
		Report findReport = reports.get(0);
		
		assertTrue(report.getUser().toString().equals(findReport.getUser().toString()));
		emHectorImpl.dropColumnFamily(Report.class);
	}	
}