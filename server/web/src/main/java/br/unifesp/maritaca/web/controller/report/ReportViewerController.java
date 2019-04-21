package br.unifesp.maritaca.web.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.unifesp.maritaca.business.answer.dto.ZipFileDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.report.ManagementReport;
import br.unifesp.maritaca.business.report.dto.ReportDTO;
import br.unifesp.maritaca.web.controller.AbstractController;
import br.unifesp.maritaca.web.util.ConstantsWeb;

@Controller
public class ReportViewerController extends AbstractController {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ReportViewerController.class);
	private static final String REPORT = "report";
	
	@Autowired private ManagementReport managementReport;
	
	@ModelAttribute(REPORT)
	public ReportDTO init() {
		return new ReportDTO();
	}
	
	@RequestMapping(value = "/report-viewer", method = RequestMethod.GET)
	public String showReportViewer(@RequestParam(value = "id", required = true) String reportId, 
			ModelMap model, HttpServletRequest request){
				
		ReportDTO reportDTO = managementReport.getReportDataById(getCurrentUser(request), reportId);
		model.addAttribute(REPORT, reportDTO);
		return "report_viewer";	
	}
	
	@RequestMapping(value = "/report-viewer", params = "csv", method = RequestMethod.GET)
	public String downloadCVS(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "json", required = true) String json,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		ReportDTO r = new ReportDTO();
		r.setReportData(json);
		ZipFileDTO zf = managementReport.getCsvData(getCurrentUser(request), r);
		try{
			if(zf != null && zf.getData() != null){
				ByteArrayOutputStream bos = new ByteArrayOutputStream();  
			    ZipOutputStream zipfile = new ZipOutputStream(bos);
			    String filename = zf.getFilename().replaceAll(" ", "");
			    ZipEntry zipentry = new ZipEntry(filename+ConstantsWeb.CSV_EXTENSION);  
		        zipfile.putNextEntry(zipentry);  
		        ByteArrayOutputStream baos = (ByteArrayOutputStream)zf.getData(); 
				byte[] cvsBytes = baos.toByteArray();
		        zipfile.write((byte[]) cvsBytes); 
				zipfile.close();
				response.setContentType("application/zip");
	            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename+".zip"+ "\"");
				response.setContentLength((int) bos.toByteArray().length);					
				baos.close();
				bos.close();
				OutputStream os = response.getOutputStream();
				os.write(bos.toByteArray());
				os.flush(); 
				os.close();				
			}
			else{
				Message message = new Message();
				message.setType(MessageType.ERROR);
				message.setMessage("Error downloading file");
				redirectAttributes.addFlashAttribute(MESSAGE, message);
				return "redirect:/report-viewer.html?id="+id;		
			}
		}
		catch(Exception e){
			Message message = new Message();
			message.setType(MessageType.ERROR);
			message.setMessage(zf.getMessage());
			return "redirect:/report-viewer.html?id="+id;	
		}
		return "report_viewer";
	}
}