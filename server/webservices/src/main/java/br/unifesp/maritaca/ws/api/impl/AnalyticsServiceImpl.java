package br.unifesp.maritaca.ws.api.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.unifesp.maritaca.business.analytics.ManagementAnalytics;
import br.unifesp.maritaca.business.analytics.dto.AnalyticsItemDTO;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.context.SpringApplicationContext;
import br.unifesp.maritaca.persistence.util.ConstantsTest;
import br.unifesp.maritaca.ws.api.AnalyticsService;
import br.unifesp.maritaca.ws.api.resp.AnalyticsItemResponse;
import br.unifesp.maritaca.ws.api.resp.ErrorResponse;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.exceptions.MaritacaWSException;
import br.unifesp.maritaca.ws.util.SpringApplicationContextTest;
import br.unifesp.maritaca.ws.util.UtilsWS;

@Path("/analytics")
public class AnalyticsServiceImpl implements AnalyticsService {
	
	private static final Log logger = LogFactory.getLog(AnalyticsServiceImpl.class);
	private ManagementAnalytics managementAnalytics;

	public AnalyticsServiceImpl() {
		logger.info("AnalyticsServiceImpl - Constructor");
		if(System.getProperty(ConstantsTest.SYS_PROP_KEY_TEST) == null) {
			managementAnalytics = (ManagementAnalytics) SpringApplicationContext.getBean("managementAnalytics");
		} else {
			managementAnalytics = (ManagementAnalytics) SpringApplicationContextTest.getBean("managementAnalytics");
		}
	}

	@Override
	public MaritacaResponse getAnswerItem(HttpServletRequest request, AnalyticsItemDTO analyticsItemDTO) {	
		try {
			UserDTO userDTO = UtilsWS.createUserDTO(request);
			AnalyticsItemResponse air = new AnalyticsItemResponse();
			air.setItemId(analyticsItemDTO.getItemId());
			air.setResult(managementAnalytics.processDataByItemId(userDTO, analyticsItemDTO));
			return air;
		} catch(Exception e) {
			ErrorResponse error = new ErrorResponse(Status.INTERNAL_SERVER_ERROR);
			error.setMessage(e.getMessage());
			//throw new MaritacaWSException(error);
			logger.error(e.getMessage());
			return null;
		}
	}
}