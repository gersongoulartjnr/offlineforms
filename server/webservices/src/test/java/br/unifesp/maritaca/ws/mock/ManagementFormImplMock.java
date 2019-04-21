package br.unifesp.maritaca.ws.mock;

import java.util.List;

import br.unifesp.maritaca.business.answer.dto.ZipFileDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.base.dto.WrapperGrid;
import br.unifesp.maritaca.business.enums.OrderFormBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.form.dto.FormWSDTO;
import br.unifesp.maritaca.business.form.dto.ImportFormDTO;
import br.unifesp.maritaca.business.form.dto.VoteDTO;
import br.unifesp.maritaca.persistence.entity.Form;

public class ManagementFormImplMock implements ManagementForm {

	@Override
	public Message saveForm(UserDTO userDTO, FormDTO formDTO) {
		return null;
	}

	@Override
	public Message updateFormFromPolicyEditor(FormDTO formDTO, UserDTO userDTO) {
		return null;
	}

	@Override
	public FormDTO getFormDTOByUrl(String url, UserDTO userDTO) {
		return null;
	}

	@Override
	public FormDTO getFormDTOById(String key, UserDTO userDTO) {
		FormDTO formDTO = new FormDTO();
		formDTO.setUser("test former");
		formDTO.setTitle("test form");
		formDTO.setDescription("This is a test form, is it not clear? :D");
		return formDTO;
	}

	@Override
	public Message importForm(ImportFormDTO data) {
		return null;
	}

	@Override
	public Message downloadXML(UserDTO userDTO, String url) {
		return null;
	}
	
	@Override
	public Message downloadAPK(UserDTO userDTO, String url) {
		return null;
	}

	@Override
	public VoteDTO likeForm(UserDTO userDTO, String url) {
		return null;
	}

	@Override
	public VoteDTO dislikeForm(UserDTO userDTO, String url) {
		return null;
	}

	@Override
	public Message deleteForm(UserDTO userDTO, FormDTO formDTO) {
		return null;
	}

	@Override
	public Message deleteFormByUrl(UserDTO userDTO, String url) {
		return null;
	}

	@Override
	public FormWSDTO getUnmarshalledFormFromXML(Form form) {
		return null;
	}

	@Override
	public WrapperGrid<FormDTO> getOwnFormsByUser(UserDTO userDTO, OrderFormBy orderBy,
			OrderType orderType, int page, int numRows) {
		return null;
	}

	@Override
	public WrapperGrid<FormDTO> getSharedFormsByUser(UserDTO userDTO, OrderFormBy orderBy,
			OrderType orderType, int page, int numRows) {
		return null;
	}

	@Override
	public Message sendAppLinkByEmail(UserDTO userDTO, String url,
			String contextPath) {
		return null;
	}

	@Override
	public Message getApkByUrl(String url, UserDTO userDTO) {
		return null;
	}
	
	public void setBuildApk(Boolean dontBuildApk) {
	}

	@Override
	public ZipFileDTO getZipFileWithAnswers(String formKey, UserDTO userDTO) {
		return null;
	}

	@Override
	public boolean isAPKBuilding(String url) {
		return false;
	}

	@Override
	public boolean existTitleDuplication(String title, UserDTO userDTO) {
		return false;
	}

	@Override
	public FormDTO getShareFormDTOByUrl(String url, UserDTO userDTO) {
		return null;
	}

	@Override
	public FormDTO getFormToShareFormByUrl(String url, UserDTO currentUser) {
		return null;
	}

	@Override
	public void verifyPermissions(String formKey, String userKey) {
	}

	@Override
	public Message getAndroidAppByUrl(String url) {
		return null;
	}

	@Override
	public List<FormDTO> getTopPublicForms() {
		return null;
	}

	@Override
	public List<FormDTO> searchPublicForms(String search) {
		return null;
	}

	@Override
	public Message getXMLWithAnswers(String formKey, UserDTO userDTO) {
		return null;
	}

	@Override
	public WrapperGrid<FormDTO> searchOwnForms(UserDTO userDTO, String search,
			OrderFormBy orderBy, OrderType orderType, int page, int numRows) {
		return null;
	}

	@Override
	public WrapperGrid<FormDTO> searchSharedForms(UserDTO userDTO,
			String search, OrderFormBy orderBy, OrderType orderType, int page,
			int numRows) {
		return null;
	}

	@Override
	public WrapperGrid<FormDTO> getOwnForms(UserDTO userDTO,
			OrderFormBy orderBy, OrderType orderType, int page, int numRows) {
		return null;
	}

	@Override
	public WrapperGrid<FormDTO> getSharedForms(UserDTO userDTO,
			OrderFormBy orderBy, OrderType orderType, int page, int numRows) {
		return null;
	}
	
	@Override
	public void populateSolr(UserDTO userDTO) {		
	}
}