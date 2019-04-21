package br.unifesp.maritaca.ws.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import br.unifesp.maritaca.business.answer.dto.DataCollectedDTO;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.exceptions.MaritacaWSException;

/**
 * This Interface defines RESTFul services to Answer.
 * 
 * @author alvarohenry
 * @author emigueltriana
 */
public interface AnswersService {

	/**
	 * List the answers belong to formKey
	 * @param request
	 * @param formKey
	 * @return
	 * @throws MaritacaWSException
	 */
	@GET
	@Path("/list/{form}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) 
	MaritacaResponse list(@Context HttpServletRequest request,
						  @PathParam("form") String formKey);
	
	/**
	 * Returns the number of last answers
	 * @param request
	 * @param formKey
	 * @return
	 */
	@GET
	@Path("/lastAnswers/{form}")
	@Produces(MediaType.APPLICATION_JSON)
	MaritacaResponse getLastAnswers(@Context HttpServletRequest request, @PathParam("form") String formKey, @QueryParam("date") Long date);
	
	/**
	 * Save an Answer for a form
	 * @param request
	 * @param input
	 * @return
	 */
	@POST
	@Path("save")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public MaritacaResponse saveAnswer(@Context HttpServletRequest request, MultipartFormDataInput input);
	
	@POST
    @Path("saveios")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    MaritacaResponse putAnswer(@Context HttpServletRequest request,
                                DataCollectedDTO collectedDTO);
}