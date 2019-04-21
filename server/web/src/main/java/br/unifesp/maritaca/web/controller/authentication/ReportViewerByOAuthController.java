package br.unifesp.maritaca.web.controller.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.unifesp.maritaca.business.report.ManagementReport;
import br.unifesp.maritaca.business.report.dto.ReportDTO;
import br.unifesp.maritaca.web.controller.AbstractController;

@Controller
public class ReportViewerByOAuthController extends AbstractController {
	
	private static final long serialVersionUID = 1L;
	private static final String REPORT = "report";
	
	@Autowired private ManagementReport managementReport;
	
	@ModelAttribute(REPORT)
	public ReportDTO init() {
		return new ReportDTO();
	}
	
	@RequestMapping(value = "/report-viewer-mob", method = RequestMethod.GET)
	public String showReportViewer(@RequestParam(value = "id", required = true) String reportId, 
			@RequestParam(value = "token", required = true) String userOAuthToken,
			ModelMap model, HttpServletRequest request){
				
		ReportDTO reportDTO = managementReport.getReportDataByTokenAndId(userOAuthToken, reportId);
		model.addAttribute(REPORT, reportDTO);
		return "report_viewer_mob";	
	}
}