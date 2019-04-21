package br.unifesp.maritaca.persistence.dao;

import java.util.List;

import br.unifesp.maritaca.persistence.entity.Analytics;

public interface AnalyticsDAO {

	public void saveAnalytics(Analytics analytics);

	public Analytics getAnalyticsById(String analyticsId);

	public List<Analytics> getAnalyticsByForm(String formKey, boolean b);

	public Analytics getAnalyticsByAnalyticsIdAndByFormUrl(String analyticsId, String formUrl);
	
	public void delete(Analytics analytics);
}