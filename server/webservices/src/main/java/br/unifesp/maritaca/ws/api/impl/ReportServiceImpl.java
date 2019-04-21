package br.unifesp.maritaca.ws.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.context.SpringApplicationContext;
import br.unifesp.maritaca.business.enums.ReportItemOpType;
import br.unifesp.maritaca.business.report.ManagementReport;
import br.unifesp.maritaca.business.report.ReportItemCalculator;
import br.unifesp.maritaca.business.report.dto.ReportItemDTO;
import br.unifesp.maritaca.business.report.dto.ReportItemList;
import br.unifesp.maritaca.business.report.dto.ReportItemWParams;
import br.unifesp.maritaca.persistence.util.ConstantsTest;
import br.unifesp.maritaca.ws.api.ReportService;
import br.unifesp.maritaca.ws.api.resp.ErrorResponse;
import br.unifesp.maritaca.ws.api.resp.HashReportItemResponse;
import br.unifesp.maritaca.ws.api.resp.ListReportItemResponse;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.api.resp.ReportListResponse;
import br.unifesp.maritaca.ws.api.resp.SimpleReportItemResponse;
import br.unifesp.maritaca.ws.exceptions.MaritacaWSException;
import br.unifesp.maritaca.ws.util.SpringApplicationContextTest;
import br.unifesp.maritaca.ws.util.UtilsWS;

@Path("/report")
public class ReportServiceImpl implements ReportService {
	
	private static final Log logger = LogFactory.getLog(ReportServiceImpl.class);
	private ReportItemCalculator itemCalculator;
	private ManagementReport managementReport;
	
	public ReportServiceImpl() {	
		logger.info("ResponderReportItemImpl - Constructor");
		if(System.getProperty(ConstantsTest.SYS_PROP_KEY_TEST) == null) {
			itemCalculator	 = (ReportItemCalculator) SpringApplicationContext.getBean("itemCalculator");
			managementReport = (ManagementReport) SpringApplicationContext.getBean("managementReport");
		} else {
			itemCalculator 	 = (ReportItemCalculator) SpringApplicationContextTest.getBean("itemCalculator");
			managementReport = (ManagementReport) SpringApplicationContextTest.getBean("managementReport");
		}
	}
	
	@Override
	public MaritacaResponse getReportsByFormId(HttpServletRequest request, String formKey) {
		try {
			UserDTO userDTO = UtilsWS.createUserDTO(request);
			ReportListResponse response = new ReportListResponse();
			response.setReports(managementReport.findReportsByFormAndByUser(formKey, userDTO));
			response.setSize(response.getReports().size());
			return response;
		} catch (Exception ex) {
			ErrorResponse error = new ErrorResponse(ex);
			error.setMessage(ex.getMessage());
			throw new MaritacaWSException(error);
		}
	}

	public MaritacaResponse getHashAnswerItem(HttpServletRequest request, ReportItemWParams reportItemDTO) {
		try {
			UserDTO userDTO = UtilsWS.createUserDTO(request);
			HashReportItemResponse rir = new HashReportItemResponse();		
			rir.setResult(itemCalculator.computeHashResponse(userDTO, reportItemDTO));
			rir.setOperation(ReportItemOpType.getOpType(reportItemDTO.getOp()).toString());
			rir.setItemId(reportItemDTO.getItemId());
			rir.setQuestionType(reportItemDTO.getQuestionType());
			return rir;
		} catch(Exception e) {
			ErrorResponse error = new ErrorResponse(Status.INTERNAL_SERVER_ERROR);
			error.setMessage(e.getMessage());
			throw new MaritacaWSException(error);
		}
	}
	
	public MaritacaResponse getAnswerItem(HttpServletRequest request, ReportItemDTO reportItemDTO) {
		try {
			UserDTO userDTO = UtilsWS.createUserDTO(request);
			SimpleReportItemResponse rir = new SimpleReportItemResponse();		
			rir.setResult(itemCalculator.computeSimpleResponse(userDTO, reportItemDTO));
			rir.setOperation(ReportItemOpType.getOpType(reportItemDTO.getOp()).toString());
			rir.setItemId(reportItemDTO.getItemId());
			rir.setQuestionType(reportItemDTO.getQuestionType());
			return rir;
		} catch(Exception e) {
			ErrorResponse error = new ErrorResponse(Status.INTERNAL_SERVER_ERROR);
			error.setMessage(e.getMessage());
			throw new MaritacaWSException(error);
		}
	}

	public MaritacaResponse listAnswersItem(HttpServletRequest request, ReportItemList reportItemDTO) {
		try {
			UserDTO userDTO = UtilsWS.createUserDTO(request);
			ListReportItemResponse lrir = new ListReportItemResponse();
			lrir.setResult(itemCalculator.listAnswersResponse(userDTO, reportItemDTO));
			lrir.setOperation(ReportItemOpType.getOpType(reportItemDTO.getOp()).toString());
			lrir.setItemId(reportItemDTO.getItemId());
			lrir.setQuestionType(reportItemDTO.getQuestionType());
			return lrir;
		} catch(Exception e) {
			ErrorResponse error = new ErrorResponse(Status.INTERNAL_SERVER_ERROR);
			error.setMessage(e.getMessage());
			throw new MaritacaWSException(error);
		}
	}

	public ReportItemDTO getReportItemSimple(HttpServletRequest request, String kind) {
		// TODO Auto-generated method stub
		if (kind.equals("simple")) {
			ReportItemDTO item = new ReportItemDTO();
			item.setItemId(0);
			item.setOp(ReportItemOpType.LIST_ALL.toString());
			item.setQuestionId(0);
			item.setQuestionType("number");
			item.setFormId("asdfasfd");
			item.setReportId("qewrty");
			return item;
		}
		if (kind.equals("hash")) {
			ReportItemWParams item = new ReportItemWParams();
			item.setItemId(0);
			item.setOp(ReportItemOpType.LIST_ALL.toString());
			item.setQuestionId(0);
			item.setQuestionType("number");
			item.setFormId("asdfasfd");
			item.setReportId("qewrty");
			List<String> params = new ArrayList<String>();
			params.add("1");
			params.add("2");
			params.add("3");
			item.setParameters(params);
			return item;
		}
		if (kind.equals("list")) {
			ReportItemList item = new ReportItemList();
			item.setItemId(0);
			item.setOp(ReportItemOpType.LIST_ALL.toString());
			item.setQuestionId(0);
			item.setQuestionType("number");
			item.setFormId("asdfasfd");
			item.setReportId("qewrty");
			item.setNumAnswers(0);
			return item;
		}
		return null;
	}

	public MaritacaResponse getReportItemHash(HttpServletRequest request,
			String kind) {
		if (kind.equals("hash")) { 
			SimpleReportItemResponse rir = new SimpleReportItemResponse();		
			rir.setResult("2");
			rir.setOperation("max");
			rir.setItemId(1);
			rir.setQuestionType("number");
			return rir;
		} else {
			HashReportItemResponse rir = new HashReportItemResponse();		
			HashMap<String, String> hashmap = new HashMap<String, String>();
			hashmap.put("0", "0");
			hashmap.put("1", "1");
			hashmap.put("2", "2");
			rir.setResult(hashmap);
			rir.setOperation("count");
			rir.setItemId(0);
			rir.setQuestionType("number");
			return rir;
		}
	}
}