package br.unifesp.maritaca.web.controller.group;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.group.ManagementGroup;
import br.unifesp.maritaca.business.group.dto.AutocompleteDTO;
import br.unifesp.maritaca.business.group.dto.GroupDTO;
import br.unifesp.maritaca.business.validator.GroupValidator;
import br.unifesp.maritaca.web.controller.AbstractController;
import br.unifesp.maritaca.web.util.ConstantsWeb;

@Controller
public class GroupEditorController extends AbstractController {

	private static final long serialVersionUID = 1L;
	
	private static final String GROUP = "group";
	@Autowired private ManagementGroup managementGroup;
	@Autowired private GroupValidator groupValidator;
	
	@ModelAttribute(GROUP)
	public GroupDTO init() {
		GroupDTO group = new GroupDTO();
		group.setGroupsList("{}");
		return group;
	}
	
	@RequestMapping(value = "/group", method = RequestMethod.GET)
    public String showForms(@RequestParam(value = "id", required = false) String id, HttpServletRequest request, Model model) {
		GroupDTO groupDTO = new GroupDTO();
		if(id != null) {
			groupDTO = managementGroup.searchGroupByKey(getCurrentUser(request), id);
		}		
		groupDTO.setCurrentGroups(managementGroup.getGroupsUser(getCurrentUser(request)));
		model.addAttribute(GROUP, groupDTO);
		return "group_editor";
    }
	
	@RequestMapping(value = "/group", method = RequestMethod.POST)
    public String saveGroup(@Valid @ModelAttribute(GROUP) GroupDTO group, BindingResult result, RedirectAttributes redirectAttributes, 
    		Model model, HttpServletRequest request) {

		groupValidator.validate(group, result);
		if(result.hasErrors()) {
			return "group_editor";
		}
		Message message = managementGroup.saveGroup(group, getCurrentUser(request));
		if(message.getData() != null) {
			redirectAttributes.addFlashAttribute(MESSAGE, message);
			return "redirect:/group.html?id="+message.getData();
		}
		else {
			model.addAttribute(MESSAGE, message);
			return "group_editor";			
		}
	}
	
	@RequestMapping(value = "group/groups", method = RequestMethod.GET)
	public @ResponseBody List<AutocompleteDTO> getGroupsByName(@RequestParam("term") String prefix, 
			HttpServletRequest request) {
		if(!"".equals(prefix) && prefix.length() > ConstantsWeb.MIN_PREFIX) {
			return managementGroup.getGroupsByName(getCurrentUser(request), prefix);
		}
		else
			return null;
	}
	
	@RequestMapping(value = "/group/delete", method = RequestMethod.POST)
    public @ResponseBody Message removeGroup(@RequestParam(value = "id", required = true) String id, HttpServletRequest request) {
		return managementGroup.removeGroupByKey(getCurrentUser(request), id);	
	}
	
	@RequestMapping(value = "/group/loadUsersGroup", method = RequestMethod.POST)
	public @ResponseBody String loadUsersGroup(@RequestParam(value = "key", required = true) String key, HttpServletRequest request) {
		return managementGroup.loadUsersGroup(key, getCurrentUser(request));
	}
}
