package br.unifesp.maritaca.ws.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.resource.messsages.ErrorMobileReport;

/**
 * This Interface defines RESTFul services to Messages.
 * 
 * @author alvarohenry
 * @author Jimmy Valverde S&aacute;nchez
 */
public interface MessagesService {

	/**
	 * This method logs some errors that happened in mobile devices.
	 * HTTP METHOD	: PUT
	 * URI			: http://HOST:PORT/CONTEXT/messages/mobilerror
	 * 
	 * @param request
	 * @param errorReportDTO
	 * @return
	 */
	@PUT
	@Path("mobileerror")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	MaritacaResponse addError(@Context HttpServletRequest request, ErrorMobileReport errorReportDTO);
	
}