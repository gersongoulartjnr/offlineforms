package br.unifesp.maritaca.web.controller.form;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.group.ManagementGroup;
import br.unifesp.maritaca.business.group.dto.AutocompleteDTO;
import br.unifesp.maritaca.persistence.permission.Policy;
import br.unifesp.maritaca.web.controller.AbstractController;
import br.unifesp.maritaca.web.util.ConstantsWeb;

@Controller
public class FormShareController extends AbstractController {
	
private static final long serialVersionUID = 1L;
	
	private static final String FORM = "form";
	
	@Autowired private ManagementForm managementForm;
	
	@Autowired private ManagementGroup managementGroup;
	
	@ModelAttribute(FORM)
	public FormDTO init() {
		return new FormDTO();
	}
	
	@RequestMapping(value = "/form-share", method = RequestMethod.GET)
    public String showShareForm(@RequestParam(value = "id", required = true) String url, 
    		ModelMap model, HttpServletRequest request) {
		
		if(url != null) {
			FormDTO formDTO = managementForm.getShareFormDTOByUrl(url, getCurrentUser(request));
			if(formDTO != null) {
				model.addAttribute(FORM, formDTO);
			}
		}
        return "form_share";
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/form-share", method = RequestMethod.POST)
    public String savePolicy(@Valid @ModelAttribute(FORM) FormDTO form, BindingResult result, 
    		Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Message message = managementForm.updateFormFromPolicyEditor(form, getCurrentUser(request));
		if (message != null && !MessageType.ERROR.equals(message.getType())) {
			form = managementForm.getFormToShareFormByUrl(form.getUrl(), getCurrentUser(request));
			if (form != null){
				if (form.getPolicy().equals(Policy.PUBLIC)) {
					super.setValueInBroadcaster(form.getStrPolicy(), form.toJSON());
				} else {
					List<UUID> newLists = ListUtils.subtract(form.getLists(), (List<UUID>)message.getData());
					for (UUID group : newLists) {
						super.setValueInBroadcaster(group.toString(), form.toJSON());							
					}
				}
			}
			return "form_lister";
		}	
		redirectAttributes.addFlashAttribute(MESSAGE, message);
		return "redirect:form-share.html?id="+form.getUrl();
    }

	@RequestMapping(value = "form-share/groups", method = RequestMethod.GET)
	public @ResponseBody List<AutocompleteDTO> getGroupsByUser(@RequestParam("term") String prefix, 
			HttpServletRequest request) {
		if(!"".equals(prefix) && prefix.length() > ConstantsWeb.MIN_PREFIX) {
			return managementGroup.getGroupsUserByName(getCurrentUser(request), prefix);
		} else {
			return null;
		}
	}
}
