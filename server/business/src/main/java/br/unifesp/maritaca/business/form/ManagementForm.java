package br.unifesp.maritaca.business.form;

import java.util.List;

import br.unifesp.maritaca.business.answer.dto.ZipFileDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.base.dto.WrapperGrid;
import br.unifesp.maritaca.business.enums.OrderFormBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.form.dto.FormWSDTO;
import br.unifesp.maritaca.business.form.dto.ImportFormDTO;
import br.unifesp.maritaca.business.form.dto.VoteDTO;
import br.unifesp.maritaca.persistence.entity.Form;

public interface ManagementForm {
	
	/**
	 * Gets the own forms of one user
	 * @param userDTO: user logged
	 * @param orderBy: field
	 * @param orderType: asc or desc
	 * @param page: show data of a page
	 * @param numRows: num of rows to show
	 * @return
	 */
	@Deprecated
	WrapperGrid<FormDTO> getOwnFormsByUser(UserDTO userDTO,
			OrderFormBy orderBy,
			OrderType orderType,
			int page, 
			int numRows);
	
	/**
	 * Gets the shared forms by user
	 * @param userDTO: user logged
	 * @param orderBy: field
	 * @param orderType: asc or desc
	 * @param page: show data of a page
	 * @param numRows: num of rows to show
	 * @return
	 */
	@Deprecated
	WrapperGrid<FormDTO> getSharedFormsByUser(UserDTO userDTO,
			OrderFormBy orderBy,
			OrderType orderType,
			int page, 
			int numRows);
	
	Message saveForm(UserDTO userDTO, FormDTO formDTO);
	
	Message updateFormFromPolicyEditor(FormDTO formDTO, UserDTO userDTO);	

	/**
	 * Gets a FormDTO by form key
	 * @param key
	 * @param userDTO
	 * @return
	 */
	FormDTO getFormDTOById(String key, UserDTO userDTO);

	/**
	 * Gets a FormDTo by form url
	 * @param url
	 * @param userDTO
	 * @return
	 */
	FormDTO getFormDTOByUrl(String url, UserDTO userDTO);
	
	/**
	 * Allow to import a form from an xml file or an url
	 * @param importDTO
	 * @return
	 */
	Message importForm(ImportFormDTO importDTO);
	
	/**
	 * Download the form as a xml by form url
	 * @param userDTO
	 * @param url
	 * @return
	 */
	Message downloadXML(UserDTO userDTO, String url);
	
	Message downloadAPK(UserDTO userDTO, String url);
	
	/**
	 * User likes a form
	 * @param userDTO
	 * @param url
	 * @return
	 */
	VoteDTO likeForm(UserDTO userDTO, String url);
	
	/**
	 * User dislikes a form
	 * @param userDTO
	 * @param url
	 * @return
	 */
	VoteDTO dislikeForm(UserDTO userDTO, String url);

	Message deleteForm(UserDTO userDTO, FormDTO formDTO);
	
	Message deleteFormByUrl(UserDTO userDTO, String url);
	
	FormWSDTO getUnmarshalledFormFromXML(Form form);

	Message sendAppLinkByEmail(UserDTO userDTO, String url, String contextPath);

	Message getApkByUrl(String url, UserDTO userDTO);
	
	Message getAndroidAppByUrl(String url);	
	
	/**
	 * Allows to downlaod the answers by form, using the CSV format
	 * @param formKey
	 * @param userDTO
	 * @return
	 */
	ZipFileDTO getZipFileWithAnswers(String formKey, UserDTO userDTO);
	
	/**
	 * Allows to downlaod the answers by form, using the XML format
	 * @param formKey
	 * @param userDTO
	 * @return
	 */
	Message getXMLWithAnswers(String formKey, UserDTO userDTO);
	
	void setBuildApk(Boolean dontBuildApk);

	boolean isAPKBuilding(String url);

	/**
	 * Checks if the form title already exists
	 * @param title
	 * @param userDTO
	 * @return
	 */
	boolean existTitleDuplication(String title, UserDTO userDTO);

	FormDTO getShareFormDTOByUrl(String url, UserDTO userDTO);

	FormDTO getFormToShareFormByUrl(String url, UserDTO currentUser);

	void verifyPermissions(String formKey, String userKey);

	List<FormDTO> getTopPublicForms();
	
	/**
	 * Search the public forms to show on the index page
	 * @param search
	 * @return
	 */
	public List<FormDTO> searchPublicForms(String search);

	/**
	 * Get the own forms for the current user
	 * @param userDTO user data logged
	 * @param orderBy key word
	 * @param orderType asc, desc
	 * @param page number to go
	 * @param numRows number of rows to show
	 * @return A WrapperGrid that contains the data to show in the grid
	 */
	public WrapperGrid<FormDTO> getOwnForms(UserDTO userDTO, OrderFormBy orderBy, 
			OrderType orderType, int page, int numRows);
	
	/**
	 * 
	 * @param userDTO
	 * @param orderBy
	 * @param orderType
	 * @param page
	 * @param numRows
	 * @return
	 */
	public WrapperGrid<FormDTO> getSharedForms(UserDTO userDTO, OrderFormBy orderBy, 
			OrderType orderType, int page, int numRows);
	
	/**
	 * Allow to search in the own forms for the current user, by one or more words
	 * @param userDTO user data logged
	 * @param search one or more words
	 * @param orderBy key word
	 * @param orderType asc, desc
	 * @param page number to go
	 * @param numRows number of rows to show
	 * @return A WrapperGrid that contains the data to show in the grid
	 */
	public WrapperGrid<FormDTO> searchOwnForms(UserDTO userDTO, String search, OrderFormBy orderBy, 
			OrderType orderType, int page, int numRows);

	/**
	 * Allow to search in the shared forms for the current user, by one or more words
	 * @param userDTO user data logged
	 * @param search one or more words
	 * @param orderBy key word
	 * @param orderType asc, desc
	 * @param page number to go
	 * @param numRows number of rows to show
	 * @return A WrapperGrid that contains the data to show in the grid
	 */
	public WrapperGrid<FormDTO> searchSharedForms(UserDTO userDTO, String search, OrderFormBy orderBy, 
			OrderType orderType, int page, int numRows);
	
	//TODO: Remove this method
	public void populateSolr(UserDTO userDTO);
}