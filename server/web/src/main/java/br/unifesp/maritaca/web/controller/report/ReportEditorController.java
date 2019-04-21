package br.unifesp.maritaca.web.controller.report;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
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
import br.unifesp.maritaca.business.report.ManagementReport;
import br.unifesp.maritaca.business.report.dto.ReportDTO;
import br.unifesp.maritaca.business.validator.ReportValidator;
import br.unifesp.maritaca.web.controller.AbstractController;
import br.unifesp.maritaca.web.controller.form.FormEditorController;

@Controller
public class ReportEditorController extends AbstractController {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(FormEditorController.class);	
	private static final String REPORT = "report";
	
	@Autowired private ManagementReport managementReport;
	@Autowired private ReportValidator reportValidator;
	
	@ModelAttribute(REPORT)
	public ReportDTO init() {
		return new ReportDTO();
	}
	
	@RequestMapping(value = "/report", method = RequestMethod.GET)
    public String showReport(
    		@RequestParam(value = "form", required = true) String formUrl, 
    		@RequestParam(value = "report", required = false) String reportId, 
    		ModelMap model, HttpServletRequest request) {
		logger.info("showReport > form: " + formUrl + " report: " + reportId);
		ReportDTO reportDTO = managementReport.getReportDataByFormAndByReport(getCurrentUser(request), 
				formUrl, reportId!=null?reportId:"");
		model.addAttribute(REPORT, reportDTO);
        return "report_editor";
    }
	
	@RequestMapping(value = "/report", method = RequestMethod.POST)
    public String saveReport(@Valid @ModelAttribute(REPORT) ReportDTO report, BindingResult result, RedirectAttributes redirectAttributes, 
    		Model model, HttpServletRequest request) {
		
		reportValidator.validate(report, result);
		if(result.hasErrors()) {
			return "report_editor";
		}
		
		Message message = managementReport.saveReport(getCurrentUser(request), report);
		if(message.getType().equals(MessageType.SUCCESS)){			
			String reportId = (String)message.getData();
			redirectAttributes.addFlashAttribute(MESSAGE, message);
			return "redirect:/report.html?form="+report.getFormUrl()+"&report="+reportId;			
		}
		else{		
			redirectAttributes.addFlashAttribute(MESSAGE, message);
			return "redirect:/report.html?form="+report.getFormUrl();
		}		
	}
	
	@RequestMapping(value = "/report", params = "delete", method = RequestMethod.POST)
    public @ResponseBody Message deleteReport(
    		@RequestParam(value = "form", required = true) String formUrl,
    		@RequestParam(value = "report", required = true) String reportId,
    		HttpServletRequest request) {
		
		return managementReport.deleteReportByReportId(getCurrentUser(request), formUrl, reportId);
	}
}