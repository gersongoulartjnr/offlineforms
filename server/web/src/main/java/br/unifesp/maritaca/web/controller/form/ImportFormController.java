package br.unifesp.maritaca.web.controller.form;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.dto.ImportFormDTO;
import br.unifesp.maritaca.web.controller.AbstractController;

@Controller
public class ImportFormController extends AbstractController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String IMPORTFORM = "importForm";
	@Autowired private ManagementForm managementForm;
	
	@ModelAttribute(IMPORTFORM)
	public ImportFormDTO init() {
		return new ImportFormDTO();
	}
	
	@RequestMapping(value = "/import", method = RequestMethod.GET)
    public String showView(HttpServletRequest request) {
        return "form_import";
    }	
	
	@RequestMapping(value = "/import", params="xml-file", method = RequestMethod.POST, 
			headers = {"content-type=application/json"})
    public @ResponseBody Message importFormFromFile(HttpServletRequest request, @RequestBody ImportFormDTO importDTO){
		importDTO.setUseFile(true);
		return managementForm.importForm(importDTO);
	}
	
	@RequestMapping(value = "/import", params="xml-url", method = RequestMethod.POST, 
			headers = {"content-type=application/json"})
    public @ResponseBody Message importFormFromUrl(HttpServletRequest request, @RequestBody ImportFormDTO importDTO){
		importDTO.setUseFile(false);
		return managementForm.importForm(importDTO);
	}
}