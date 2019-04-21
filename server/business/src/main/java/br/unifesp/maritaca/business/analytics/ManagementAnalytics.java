package br.unifesp.maritaca.business.analytics;

import java.util.List;

import br.unifesp.maritaca.business.analytics.dto.AEditorDTO;
import br.unifesp.maritaca.business.analytics.dto.AViewerDTO;
import br.unifesp.maritaca.business.analytics.dto.AnalyticsDTO;
import br.unifesp.maritaca.business.analytics.dto.AnalyticsItemDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;

public interface ManagementAnalytics {
	
	/**
	 * Get the Analytics by a form and by user
	 * @param formKey
	 * @param userDTO
	 * @return
	 */
	public List<AnalyticsDTO> findAnalyticsByFormAndByUser(String formKey, UserDTO userDTO);
	
	/**
	 * Get the Analytics by a form
	 * @param formKey
	 * @param userDTO
	 * @return
	 */
	public List<AnalyticsDTO> findAnalyticsByForm(String formKey, UserDTO userDTO);
	
	/**
	 * Get the data for the analytics by its id and form url
	 * @param userDTO
	 * @param formUrl
	 * @param analyticsId
	 * @return
	 */
	public AEditorDTO getDataByFormUrlAndAnalyticsId(UserDTO userDTO, String formUrl, String analyticsId);
	
	/**
	 * Get the data for the analytics by its id
	 * @param userDTO
	 * @param analyticsId
	 * @return
	 */
	public AViewerDTO getDataByAnalyticsId(UserDTO userDTO, String analyticsId);

	/**
	 * Create or update an Analytics
	 * @param userDTO
	 * @param aEditorDTO
	 * @return
	 */
	public Message saveAnalytics(UserDTO userDTO, AEditorDTO aEditorDTO);
	
	/**
	 * Delete an Analytics by id
	 * @param userDTO
	 * @param id
	 * @return
	 */
	public Message deleteAnalytics(UserDTO userDTO, String id);

	/**
	 * 
	 * @param itemId
	 * @return
	 */
	public String processDataByItemId(UserDTO userDTO, AnalyticsItemDTO analyticsItemDTO);
}