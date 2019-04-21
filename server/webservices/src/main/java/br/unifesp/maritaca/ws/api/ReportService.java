package br.unifesp.maritaca.ws.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import br.unifesp.maritaca.business.report.dto.ReportItemDTO;
import br.unifesp.maritaca.business.report.dto.ReportItemList;
import br.unifesp.maritaca.business.report.dto.ReportItemWParams;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;

/**
 * This Interface defines RESTFul services to send responses each report item.
 * 
 * @author alvarohenry
 *
 */
public interface ReportService {

	/**
	 * List the reports by form and user
	 * @param request
	 * @param formKey
	 * @return
	 */
	@GET
	@Path("/list/{form}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	MaritacaResponse getReportsByFormId(@Context HttpServletRequest request, @PathParam("form") String formKey);
	
	/**
	 * This method compute the value report item.
	 * HTTP METHOD	: POST
	 * URI			: http://HOST:PORT/CONTEXT/ws/report/answerItem
	 * 
	 * @return
	 */
	@POST
	@Path("answerItem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	MaritacaResponse getAnswerItem(@Context HttpServletRequest request, ReportItemDTO ansRepoItemDTO);
	
	@POST
	@Path("hashAnswerItem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MaritacaResponse getHashAnswerItem(@Context HttpServletRequest request, ReportItemWParams reportItemDTO);	
	
	@POST
	@Path("listAnswersItem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	MaritacaResponse listAnswersItem(@Context HttpServletRequest request, ReportItemList ansRepoItemDTO);
	
	// temporal
	@GET
	@Path("getItem/{kind}")
	@Produces(MediaType.APPLICATION_JSON)
	ReportItemDTO getReportItemSimple(@Context HttpServletRequest request, @PathParam("kind") String kind);
	
	@GET
	@Path("{kind}")
	@Produces(MediaType.APPLICATION_JSON)
	MaritacaResponse getReportItemHash(@Context HttpServletRequest request, @PathParam("kind") String kind);	
}