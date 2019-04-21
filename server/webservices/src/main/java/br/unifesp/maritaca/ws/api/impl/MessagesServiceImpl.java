package br.unifesp.maritaca.ws.api.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.unifesp.maritaca.ws.api.MessagesService;
import br.unifesp.maritaca.ws.api.resp.ErrorResponse;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.api.resp.XmlSavedResponse;
import br.unifesp.maritaca.ws.exceptions.MaritacaWSException;
import br.unifesp.maritaca.ws.resource.messsages.ErrorMobileReport;

@Path("/messages")
public class MessagesServiceImpl implements MessagesService {
	
	private static final Log log = LogFactory.getLog(MessagesServiceImpl.class);
	
	public MessagesServiceImpl() {	
		log.info("in AnswersServiceImpl");
	}

	@Override
	public MaritacaResponse addError(HttpServletRequest request, ErrorMobileReport errorReportDTO) 
			throws MaritacaWSException {
		try {
			if( errorReportDTO != null) {				
				XmlSavedResponse resp = new XmlSavedResponse();
				resp.setType(MaritacaResponse.MESSAGE_TYPE);
				log.error("Mobile: " + errorReportDTO.toString());
				return resp;
			} else {
				ErrorResponse resp = new ErrorResponse(Status.INTERNAL_SERVER_ERROR);
				resp.setMessage("request cannot be processed");
				throw new MaritacaWSException(resp);
			}
		} catch(Exception e) {
			ErrorResponse error = new ErrorResponse(Status.INTERNAL_SERVER_ERROR);
			error.setMessage("unknown error, not possible to save the response");
			throw new MaritacaWSException(error);
		}
	}
}