package br.unifesp.maritaca.analytics;

public interface MongoAnalytics {
	
	public String processData(String mongoHost, String mongoPort, String mongoTimeout, 
			String userKey, String formKey, String formUrl, Integer numberOfCollects, String analyticsKey, String json, int itemId);

}