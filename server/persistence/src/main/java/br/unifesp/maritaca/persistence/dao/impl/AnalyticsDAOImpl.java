package br.unifesp.maritaca.persistence.dao.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.AnalyticsDAO;
import br.unifesp.maritaca.persistence.entity.Analytics;

@Service("analyticsDAO")
public class AnalyticsDAOImpl extends AbstractDAO implements AnalyticsDAO {

	@Override
	public void saveAnalytics(Analytics analytics) {
		emHector.persist(analytics);		
	}

	@Override
	public Analytics getAnalyticsById(String analyticsId) {
		return emHector.find(Analytics.class, UUID.fromString(analyticsId));
	}

	@Override
	public List<Analytics> getAnalyticsByForm(String formKey, boolean b) {
		return emHector.cQuery(Analytics.class, "form", formKey);
	}

	@Override
	public Analytics getAnalyticsByAnalyticsIdAndByFormUrl(String analyticsId,
			String formKey) {
		
		return emHector.find(Analytics.class, UUID.fromString(analyticsId));
	}

	@Override
	public void delete(Analytics analytics) {
		emHector.delete(Analytics.class, analytics.getKey());
	}
}