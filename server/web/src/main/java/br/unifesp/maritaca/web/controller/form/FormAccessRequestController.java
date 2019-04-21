package br.unifesp.maritaca.web.controller.form;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.form.ManagementFormAccessRequest;
import br.unifesp.maritaca.business.form.dto.AccessRequestDTO;
import br.unifesp.maritaca.business.form.dto.FormAccessRequestDTO;
import br.unifesp.maritaca.web.controller.AbstractController;

@Controller
public class FormAccessRequestController extends AbstractController {

	private static final long serialVersionUID = 1L;
	@Autowired private ManagementFormAccessRequest managFormAccessRequest;
	private static Logger logger = Logger.getLogger(FormAccessRequestController.class);

	@RequestMapping(value = "/form-access-request", method = RequestMethod.GET)
	public String showForms(HttpServletRequest request) {
		return "form_access_request";
	}

	@RequestMapping(value = "/form-access-request/list", method = RequestMethod.GET)
	public @ResponseBody
	List<FormAccessRequestDTO> requestList(HttpServletRequest request) {
		return managFormAccessRequest.getFormAccessRequestByUser(getCurrentUser(request));
	}

	@RequestMapping(value = "/form-access-request/requests", method = RequestMethod.GET)
	public @ResponseBody 
	String getTotalForm(HttpServletRequest request) {
		return String.valueOf(managFormAccessRequest.getTotalRequestByUser(getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/form-access-request/accept", method = RequestMethod.POST)
	public @ResponseBody Message acceptAllRequest(
			@RequestParam(value = "users", 	 required = true) String jsonUsers,
			HttpServletRequest request) {
		logger.info(jsonUsers);
		Type type = new TypeToken<ArrayList<AccessRequestDTO>>(){}.getType();
		List<AccessRequestDTO> acceptList = (new Gson()).fromJson(jsonUsers, type); 
		return managFormAccessRequest.acceptRequest(getCurrentUser(request), acceptList);
	}

	@RequestMapping(value = "/form-access-request/reject", method = RequestMethod.POST)
	public @ResponseBody Message rejectAllRequest(
			@RequestParam(value = "users", 	 required = true) String jsonUsers,
			HttpServletRequest request) {
		Type type = new TypeToken<ArrayList<AccessRequestDTO>>(){}.getType();
		List<AccessRequestDTO> acceptList = (new Gson()).fromJson(jsonUsers, type); 
		return managFormAccessRequest.rejectRequest(getCurrentUser(request), acceptList);
	}
}