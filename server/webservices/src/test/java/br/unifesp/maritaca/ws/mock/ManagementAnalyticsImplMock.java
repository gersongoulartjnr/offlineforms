package br.unifesp.maritaca.ws.mock;

import java.util.List;

import br.unifesp.maritaca.business.analytics.ManagementAnalytics;
import br.unifesp.maritaca.business.analytics.dto.AEditorDTO;
import br.unifesp.maritaca.business.analytics.dto.AViewerDTO;
import br.unifesp.maritaca.business.analytics.dto.AnalyticsDTO;
import br.unifesp.maritaca.business.analytics.dto.AnalyticsItemDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;


public class ManagementAnalyticsImplMock implements ManagementAnalytics {

	@Override
	public List<AnalyticsDTO> findAnalyticsByFormAndByUser(String formKey,
			UserDTO userDTO) {
		return null;
	}

	@Override
	public List<AnalyticsDTO> findAnalyticsByForm(String formKey,
			UserDTO userDTO) {
		return null;
	}

	@Override
	public AEditorDTO getDataByFormUrlAndAnalyticsId(UserDTO userDTO,
			String formUrl, String analyticsId) {
		return null;
	}

	@Override
	public AViewerDTO getDataByAnalyticsId(UserDTO userDTO, String analyticsId) {
		return null;
	}

	@Override
	public Message saveAnalytics(UserDTO userDTO, AEditorDTO aEditorDTO) {
		return null;
	}

	@Override
	public String processDataByItemId(UserDTO userDTO,
			AnalyticsItemDTO analyticsItemDTO) {
		return null;
	}

	@Override
	public Message deleteAnalytics(UserDTO userDTO, String id) {
		return null;
	}
}