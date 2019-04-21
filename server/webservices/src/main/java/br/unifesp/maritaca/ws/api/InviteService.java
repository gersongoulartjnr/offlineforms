package br.unifesp.maritaca.ws.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.resource.messsages.Invitation;

/**
 * This Interface defines RESTFul services to send Invitations.
 * 
 * @author alvarohenry
 */
public interface InviteService {
	
	/**
	 * This method sends invitations by email received in invitation object.
	 * HTTP METHOD	: PUT
	 * URI			: http://HOST:PORT/CONTEXT/invite/contactlists
	 * 
	 * @param request
	 * @param invitation 
	 * @return
	 */
	@PUT
	@Path("contactlists")
	@Consumes({MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	MaritacaResponse addInvitation(@Context HttpServletRequest request, Invitation invitation);
	
}
