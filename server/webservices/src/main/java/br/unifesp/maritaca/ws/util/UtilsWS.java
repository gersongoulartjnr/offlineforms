package br.unifesp.maritaca.ws.util;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response.Status;

import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.ws.api.resp.ErrorResponse;
import br.unifesp.maritaca.ws.exceptions.MaritacaWSException;

public class UtilsWS {
	
	public static String buildApplicationUrl(HttpServletRequest request, String url){
		String contextPath = request.getContextPath();
		String serverName = request.getServerName();
		String port = String.valueOf(request.getServerPort());
		String scheme = request.getScheme();
		String queryStr = request.getQueryString();
        return scheme + "://" + serverName + ":" + port + contextPath + url + "?" + queryStr;
    }
	
	public static UserDTO createUserDTO(HttpServletRequest request) {
		String userEmail = (String) request.getAttribute(ConstantsBusiness.WS_USER_KEY);
		if(userEmail == null){
			ErrorResponse error = new ErrorResponse(Status.UNAUTHORIZED);
			error.setMessage("Invalid User");
			throw new MaritacaWSException(error);
		}
		return new UserDTO(userEmail);
	}
}
