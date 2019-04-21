package br.unifesp.maritaca.ws.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import br.unifesp.maritaca.business.analytics.dto.AnalyticsItemDTO;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;

/**
 * Defines RESTFul services to send responses by each analytics item.
 * 
 * @author Jimmy Valverde S.
 *
 */
public interface AnalyticsService {
	
	@POST
	@Path("analyticsItem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	MaritacaResponse getAnswerItem(@Context HttpServletRequest request, AnalyticsItemDTO analyticsItemDTO);
}