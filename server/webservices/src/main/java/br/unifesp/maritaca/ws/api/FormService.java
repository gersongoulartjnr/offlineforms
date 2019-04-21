package br.unifesp.maritaca.ws.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;

public interface FormService {

	@GET
	@Path("/access/{form}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) 
	MaritacaResponse askForPermission (@Context HttpServletRequest request,
			@PathParam("form") String formKey);
	
}