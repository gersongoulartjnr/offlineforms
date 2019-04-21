package br.unifesp.maritaca.web.controller.form;

import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.form.dto.VoteDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.business.validator.FormValidator;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.web.controller.AbstractController;
import br.unifesp.maritaca.web.util.ConstantsWeb;

@Controller
public class FormEditorController extends AbstractController {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(FormEditorController.class);	
	private static final String FORM = "form";
	
	@Autowired
	private ManagementForm managementForm;
	
	@Autowired
	private FormValidator formValidator;
	
	@ModelAttribute(FORM)
	public FormDTO initFormEditor() {
		return new FormDTO();
	}
	
	@RequestMapping(value = "/form", method = RequestMethod.GET)
    public String showForm(@RequestParam(value = "id", required = false) String url, 
    		ModelMap model, HttpServletRequest request) {

		if(url != null) {
			FormDTO formDTO = managementForm.getFormDTOByUrl(url, getCurrentUser(request));
			if(formDTO != null) {
				model.addAttribute(FORM, formDTO);
			}
		} 
        return "form_editor";
    }
	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
    public String saveForm(@Valid @ModelAttribute(FORM) FormDTO form, BindingResult result, RedirectAttributes redirectAttributes, 
    		Model model, HttpServletRequest request) {

		formValidator.validate(form, result);
		if(result.hasErrors()) {
			return "form_editor";
		}
		UserDTO currentUser = getCurrentUser(request);
		Message message = managementForm.saveForm(currentUser, form);
		if(message.getData() != null) {
			redirectAttributes.addFlashAttribute(MESSAGE, message);
			redirectAttributes.addFlashAttribute(APK_BUILDING, message.getExtra());
	
			if (message.getData() instanceof FormDTO) {
				form = (FormDTO) message.getData();
				super.setValueInBroadcaster(currentUser.getUsername(), form.toJSON());
				return "redirect:/form.html?id="+form.getUrl();
			}		
			return "redirect:/form.html?id="+message.getData();			
		}
		else {
			return "form_editor";
		}
	}	
	
	/*** Like and Dislike ***/
	@RequestMapping(value = "/form/like", method = RequestMethod.POST)
    public @ResponseBody VoteDTO doLike(@RequestParam(value = "id", required = true) String url, 
    		HttpServletRequest request, HttpServletResponse response) {
		return managementForm.likeForm(getCurrentUser(request), url);
    }
	
	@RequestMapping(value = "/form/dislike", method = RequestMethod.POST)
    public @ResponseBody VoteDTO doDisLike(@RequestParam(value = "id", required = true) String url, 
    		HttpServletRequest request, HttpServletResponse response) {
		return managementForm.dislikeForm(getCurrentUser(request), url);
    }
	
	/*** Save as***/	
	@RequestMapping(value = "/form-save-as", method = RequestMethod.GET)
	public String showView(HttpServletRequest request) {		
        return "form_save_as";
    }
	
	@RequestMapping(value = "/form/validtitle", method = RequestMethod.POST)
	public @ResponseBody String validTitleDuplication(@RequestParam(value = "title", required = true) String title, HttpServletRequest request) {
		// TODO
		if (managementForm.existTitleDuplication(title, getCurrentUser(request))){
			return "true";
		}
		return "false";
	}
	
	/*** Download XML***/
	@RequestMapping(value = "/form", params = "xml", method = RequestMethod.GET)
    public String downloadXML(@RequestParam(value = "id", required = true) String url, 
    		ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		try {	
			Message message = managementForm.downloadXML(getCurrentUser(request), url);
			response.setContentType(ConstantsWeb.APPLICATION_XML);
			if(message != null && message.getType().equals(MessageType.SUCCESS)) {
				Form form = (Form)message.getData();
	            response.setHeader("Content-Disposition", "attachment;Filename=\"" + form.getTitle()+ConstantsWeb.XML_EXTENSION + "\"");
				PrintWriter pr = response.getWriter();
				pr.print(form.getXml());
				pr.flush();
				pr.close();
			}
			else{
				response.setHeader("Content-Disposition", "attachment;Filename=\"error"+ConstantsWeb.XML_EXTENSION + "\"");
				PrintWriter pr = response.getWriter();
				pr.print(message.getMessage());
				pr.flush();
				pr.close();
			}
		}
		catch(Exception e) {
			logger.error("Error downloading XML: " + url);
		}
		return null;
    }
	
	/*** Delete (form list and form editor) ***/
	@RequestMapping(value = "/form", params="delete", method = RequestMethod.POST, 
			headers = {"content-type=application/json"})
    public @ResponseBody Message deleteFormJ(HttpServletRequest request, @RequestBody FormDTO form, 
    		RedirectAttributes redirectAttributes){
		//Message message = managementForm.deleteForm(getCurrentUser(request), form);		
		//redirectAttributes.addFlashAttribute(MESSAGE, message);
		return managementForm.deleteForm(getCurrentUser(request), form);
	}
	
	@RequestMapping(value = "/form/delete", method = RequestMethod.POST)
    public @ResponseBody Message deleteFormFromList(@RequestParam(value = "id", required = true) String id, 
    		HttpServletRequest request) {

		return managementForm.deleteFormByUrl(getCurrentUser(request), id);	
	}
	
	/*** ***/
	@RequestMapping(value = "/form", params = "mobile-app", method = RequestMethod.GET)
    public String downloadAPK(@RequestParam(value = "id", required = true) String url, 
    		ModelMap model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		
		try {	
			Message message = managementForm.downloadAPK(getCurrentUser(request), url);
			if(message != null && message.getType().equals(MessageType.SUCCESS)) {
				byte[] apkBytes = (byte[])message.getData();
				response.setContentType(ConstantsBusiness.MOB_MIMETYPE);
	            response.setHeader("Content-Disposition", "attachment;Filename=\"" + url+".apk"+ "\"");
	            response.setContentLength((int) apkBytes.length);
	            			
				OutputStream os = response.getOutputStream();
				os.write(apkBytes);
	            os.flush(); 
	            os.close();
			} else if (message != null && message.getType().equals(MessageType.WARN)) {
				redirectAttributes.addFlashAttribute(MESSAGE, message);
				return "redirect:/index.html#";
			} else {
				redirectAttributes.addFlashAttribute(MESSAGE, message);
				return "redirect:/form.html?id="+url;
			}
		}
		catch(Exception e) {
			redirectAttributes.addFlashAttribute(MESSAGE, new Message("Error downloading Mobile App", MessageType.ERROR));
			logger.error("Error downloading Mobile App: " + url);
			return "redirect:/form.html?id="+url;
		}
		return null;
    }
	
	@RequestMapping(value = "form/send-app-link",  method = RequestMethod.POST)
	public @ResponseBody Message sendApkLinkByEmail(@RequestParam(value = "id", required = true) String url, 
			HttpServletRequest request) {
		
		String contextPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + request.getContextPath();
		return managementForm.sendAppLinkByEmail(getCurrentUser(request), url, contextPath);
	}
	
	@RequestMapping(value = "/form", params = "get-app", method = RequestMethod.GET)
	public String getApk(@RequestParam(value = "id", required = true) String url, 
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		
		try {
			UserDTO userDTO = getCurrentUser(request);
			if(userDTO == null) { userDTO = new UserDTO(); }
			
			Message message = managementForm.getApkByUrl(url, userDTO);
			if(message != null && message.getType().equals(MessageType.SUCCESS)) {
				if(message.getData() != null) {
					byte[] apkBytes = (byte[])message.getData();
					response.setContentType(ConstantsBusiness.MOB_MIMETYPE);
					response.setHeader("Content-Disposition", "attachment;Filename=\"" + url+".apk"+ "\"");
					response.setContentLength((int) apkBytes.length);					
					OutputStream os = response.getOutputStream();
					os.write(apkBytes);           
					os.flush(); 
					os.close();
				}
				else {
					message.setMessage("You need to login first.");
					redirectAttributes.addFlashAttribute(MESSAGE, message);
					request.getSession().setAttribute(ConstantsWeb.DOWNLOAD_APK, url);
					return REDIRECT_LOGIN_HTML_VIEW;
				}				
			} else {
				message.setMessage("You cannot download this APK.");
				redirectAttributes.addFlashAttribute(MESSAGE, message);
				return "redirect:/forms.html";				
			}
		}
		catch(Exception e) {
			redirectAttributes.addFlashAttribute(MESSAGE, new Message("Error downloading Mobile App", MessageType.ERROR));
			logger.error("Error downloading Mobile App: " + url);
			return "redirect:/form.html?id="+url;
		}
		return null;
	}		
}