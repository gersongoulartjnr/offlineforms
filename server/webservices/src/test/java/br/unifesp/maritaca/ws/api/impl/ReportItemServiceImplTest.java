package br.unifesp.maritaca.ws.api.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.business.enums.ReportItemOpType;
import br.unifesp.maritaca.business.report.dto.ReportItemDTO;
import br.unifesp.maritaca.business.report.dto.ReportItemWParams;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.ws.WebServicesTest;
import br.unifesp.maritaca.ws.api.ReportService;
import br.unifesp.maritaca.ws.api.resp.HashReportItemResponse;
import br.unifesp.maritaca.ws.api.resp.SimpleReportItemResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:webservices-context-test.xml"})
public class ReportItemServiceImplTest extends WebServicesTest {

	private static final String USER_EMAIL = "test@maritaca.com";
	private ReportService reportItemService;

	@Before
	public void setUp() {
		super.setProperties();
		reportItemService = new ReportServiceImpl();
	}
	
	@After
	public void cleanUp() {
		super.clearProperties();
	}
	
	@Test
	public void testGetAnswerItemMAX() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);		
		ReportItemDTO ansRepoItemDTO = getAnswerReportItem(ReportItemOpType.MAX);
		
		SimpleReportItemResponse response = (SimpleReportItemResponse) reportItemService.getAnswerItem(request, ansRepoItemDTO);
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getCode());
		Assert.assertEquals("10.0", response.getResult());
	}
	
	@Test
	public void testGetAnswerItemMIN() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);		
		ReportItemDTO ansRepoItemDTO = getAnswerReportItem(ReportItemOpType.MIN);		
		
		SimpleReportItemResponse response = (SimpleReportItemResponse) reportItemService.getAnswerItem(request, ansRepoItemDTO);
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getCode());
		Assert.assertEquals("12.0", response.getResult());
	}

	
	@Test
	public void testGetAnswerItemAVG() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);		
		ReportItemDTO ansRepoItemDTO = getAnswerReportItem(ReportItemOpType.AVG);
		
		SimpleReportItemResponse response = (SimpleReportItemResponse) reportItemService.getAnswerItem(request, ansRepoItemDTO);
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getCode());
		Assert.assertEquals("14.0", response.getResult());
	}
	
	@Test
	public void getGetAnswerItemHash() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);		
		ReportItemDTO ansRepoItemDTO = getAnswerReportItem(ReportItemOpType.TOTALBYVALUE);
		ReportItemWParams riparams = new ReportItemWParams();
		riparams.setFormId(ansRepoItemDTO.getFormId());
		riparams.setItemId(ansRepoItemDTO.getItemId());
		riparams.setOp(ansRepoItemDTO.getOp());
		riparams.setQuestionId(ansRepoItemDTO.getQuestionId());
		riparams.setQuestionType(ansRepoItemDTO.getQuestionType());
		riparams.setReportId(ansRepoItemDTO.getReportId());
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		riparams.setParameters(list);
		HashReportItemResponse response = (HashReportItemResponse) reportItemService.getHashAnswerItem(request, riparams);
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getCode());
		Assert.assertEquals("40", response.getResult().get("1"));
	}

	private ReportItemDTO getAnswerReportItem(ReportItemOpType type) {
		ReportItemDTO ansRepoItemDTO = new ReportItemDTO();
		ansRepoItemDTO.setOp(type.toString());
		ansRepoItemDTO.setQuestionId(0);
		ansRepoItemDTO.setQuestionType("number");
		ansRepoItemDTO.setFormId(UUID.randomUUID().toString());
		ansRepoItemDTO.setReportId(UUID.randomUUID().toString());
		return ansRepoItemDTO;
	}
}
