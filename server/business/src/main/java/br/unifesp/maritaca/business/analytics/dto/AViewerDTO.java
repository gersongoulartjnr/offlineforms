package br.unifesp.maritaca.business.analytics.dto;

public class AViewerDTO extends AnalyticsDTO {

	private static final long serialVersionUID = 1L;
	
	private String formUrl;
	private String formXml;
	private String token;
	private String uriServer;
	
	public String getFormUrl() {
		return formUrl;
	}
	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}

	public String getFormXml() {
		return formXml;
	}
	public void setFormXml(String formXml) {
		this.formXml = formXml;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUriServer() {
		return uriServer;
	}
	public void setUriServer(String uriServer) {
		this.uriServer = uriServer;
	}
}