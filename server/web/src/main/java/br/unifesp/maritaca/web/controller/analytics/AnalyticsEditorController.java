package br.unifesp.maritaca.web.controller.analytics;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unifesp.maritaca.business.analytics.ManagementAnalytics;
import br.unifesp.maritaca.business.analytics.dto.AEditorDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.web.controller.AbstractController;

@Controller
public class AnalyticsEditorController extends AbstractController {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AnalyticsEditorController.class);	

	@Autowired private ManagementAnalytics managementAnalytics;
	private static final String ANALYTICS = "analytics";
	
	@ModelAttribute(ANALYTICS)
	public AEditorDTO init() {
		return new AEditorDTO();
	}
	
	@RequestMapping(value = "/analytics-editor", method = RequestMethod.GET)
    public String showAnalyticsEditor(
    		@RequestParam(value = "form", required = true) String formUrl, 
    		@RequestParam(value = "analytics", required = false) String analyticsId, 
    		ModelMap model, HttpServletRequest request){
		
		logger.info("showAnalyticsEditor >> form: " + formUrl + " >> analytics: " + analyticsId);
		AEditorDTO aEditorDTO = managementAnalytics.getDataByFormUrlAndAnalyticsId(getCurrentUser(request), 
				formUrl, analyticsId != null ? analyticsId : "");
		model.addAttribute(ANALYTICS, aEditorDTO);
		
		return "analytics_editor";
	}
	
	@RequestMapping(value = "/analytics-editor", method = RequestMethod.POST, 
			headers = {"content-type=application/json"})
    public @ResponseBody Message saveAnalyticsEditor(HttpServletRequest request, @RequestBody AEditorDTO aEditorDTO) {
		logger.info("saveAnalyticsEditor >> aEditorDTO: " + aEditorDTO);		
		return managementAnalytics.saveAnalytics(getCurrentUser(request), aEditorDTO);
	}
	
	@RequestMapping(value = "/analytics-editor/delete", method = RequestMethod.POST)
    public @ResponseBody Message deleteAnalytics(@RequestParam(value = "id", required = true) String id, 
    		HttpServletRequest request) {

		return managementAnalytics.deleteAnalytics(getCurrentUser(request), id);	
	}
}