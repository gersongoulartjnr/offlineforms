package br.unifesp.maritaca.business.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;

@Component
public class FormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return AccountDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {		
		FormDTO formDTO = (FormDTO)target;
		
		if(formDTO.getTitle().trim().equals("")) {
			errors.rejectValue("title", "form_title_required");
		}
		if(formDTO.getXml().trim().equals("")) {
			errors.rejectValue("xml", "form_xml_required");
		}
		
		long iconSize = formDTO.getIconFile() == null ? 0 : formDTO.getIconFile().getSize();
		if(iconSize > 0) {
			if(iconSize >= ConstantsBusiness.ICON_MAX_SIZE) {
				errors.reject("iconFile", "form_icon_size_exceeded");
			}
			if(!formDTO.getIconFile().getContentType().equals(ConstantsBusiness.PNG_FORMAT) && 
					!formDTO.getIconFile().getContentType().equals(ConstantsBusiness.GIF_FORMAT) && 
					!formDTO.getIconFile().getContentType().equals(ConstantsBusiness.JPG_FORMAT)) {
				errors.reject("iconFile", "form_icon_format_invalid");
			}
		}
	}
}