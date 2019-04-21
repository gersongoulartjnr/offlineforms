package br.unifesp.maritaca.web.controller.analytics;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.unifesp.maritaca.business.analytics.ManagementAnalytics;
import br.unifesp.maritaca.business.analytics.dto.AViewerDTO;
import br.unifesp.maritaca.web.controller.AbstractController;

@Controller
public class AnalyticsViewerController extends AbstractController {

	private static final long serialVersionUID = 1L;
	private static final String ANALYTICS = "analytics";
	
	@Autowired private ManagementAnalytics managementAnalytics;
	
	@ModelAttribute(ANALYTICS)
	public AViewerDTO init() {
		return new AViewerDTO();
	}

	@RequestMapping(value = "/analytics-viewer", method = RequestMethod.GET)
    public String showAnalyticsViewer(@RequestParam(value = "id", required = true) String analyticsId, 
			ModelMap model, HttpServletRequest request){
		
		AViewerDTO aViewerDTO = managementAnalytics.getDataByAnalyticsId(getCurrentUser(request), analyticsId);
		model.addAttribute(ANALYTICS, aViewerDTO);
		
		return "analytics_viewer";
	}
}