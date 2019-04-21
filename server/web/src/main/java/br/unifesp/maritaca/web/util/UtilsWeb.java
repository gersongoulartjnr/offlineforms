package br.unifesp.maritaca.web.util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;

public class UtilsWeb {
	/**
	 * This method makes responses in JSON format. Generally is use to 
	 * treat exceptions and send back to the user an specific message. 
	 * @param response
	 * @param params
	 */
	public static void makeResponseInJSON(HttpServletResponse response, 
													String... params) {
		HttpServletResponse response1 = response;
		response1.setContentType(ConstantsWeb.APPLICATION_JSON);
		sendValuesInJson(response1, params);
		response1.setStatus(HttpURLConnection.HTTP_OK);
    }
	
	/**
	 * This method writes, in JSON format, params in the HttpServletResponse.
	 * @param response
	 * @param params
	 * @throws IOException
	 */
	public static void sendValuesInJson(HttpServletResponse response, String... params) {
		PrintWriter printWriter;
		try {
			printWriter = response.getWriter();
			if (params.length % 2 != 0) {
				throw new IllegalArgumentException("Arguments should be name=value*");
			} 
			
			printWriter.append('{');
			for (int i = 0; i < params.length; i+=2) {
				if (i > 0) {
					printWriter.append(',');
				}
				printWriter.append('"');
				printWriter.append(params[i]);
				printWriter.append('"');
				printWriter.append(':');
				printWriter.append('"');
				printWriter.append(params[i+1]);
				printWriter.append('"');
			}
			printWriter.append('}');
		} catch (IOException e) {			
			throw new RuntimeException(e);
		}
	}
	
	public static String buildServerAddressUrl(HttpServletRequest request) {
        //return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		return request.getScheme() + "://" + request.getServerName();// + ":" + request.getServerPort();
    }
	
	public static String buildContextUrl(HttpServletRequest request) {
		return buildServerAddressUrl(request) + request.getContextPath();
	}
}
