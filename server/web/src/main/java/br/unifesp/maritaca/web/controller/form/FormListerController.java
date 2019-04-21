package br.unifesp.maritaca.web.controller.form;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unifesp.maritaca.business.answer.dto.ZipFileDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.WrapperGrid;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.enums.OrderFormBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.group.ManagementGroup;
import br.unifesp.maritaca.web.controller.AbstractController;
import br.unifesp.maritaca.web.util.ConstantsWeb;

@Controller
public class FormListerController extends AbstractController {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(FormListerController.class);
	private String strPolicy = "strPolicy";
	
	@Autowired private ManagementForm managementForm;
	@Autowired private ManagementGroup managementGroup;
	
	@RequestMapping(value = "/forms", method = RequestMethod.GET)
    public String showForms(HttpServletRequest request) {
		return "form_lister";
    }
	
	@RequestMapping(value = "/forms/my-forms", method = RequestMethod.GET)
	public @ResponseBody WrapperGrid<FormDTO> myOwnForms(HttpServletRequest request,
			@RequestParam(value = "orderBy", required = true) String strOrderBy,
			@RequestParam(value = "orderType", required = true) String strOrderType,
			@RequestParam(value = "page", required = true) Integer page, 
			@RequestParam(value = "searchBy", required = false) String search) {
		
		//TODO: Improve this, should not be strPolicy, but just policy
		OrderFormBy orderBy;
		if(strOrderBy.equals(strPolicy)){
			orderBy = OrderFormBy.POLICY;
		} else {
			orderBy = OrderFormBy.getOrderBy(strOrderBy);
		}
		OrderType orderType = OrderType.getOrderType(strOrderType);
		if(search == null || "".equals(search.trim())){
			return managementForm.getOwnForms(getCurrentUser(request), orderBy, 
				orderType, page, ConstantsWeb.ITEMS_PER_PAGE);
		} else {
			return managementForm.searchOwnForms(getCurrentUser(request), search, orderBy,
				orderType, page, ConstantsWeb.ITEMS_PER_PAGE);
		}
	}
	
	@RequestMapping(value = "/forms/shared-forms", method = RequestMethod.GET)
	public @ResponseBody WrapperGrid<FormDTO> mySharedForms(HttpServletRequest request,
			@RequestParam(value = "orderBy", required = true) String strOrderBy,
			@RequestParam(value = "orderType", required = true) String strOrderType,
			@RequestParam(value = "page", required = true) Integer page, 
			@RequestParam(value = "searchBy", required = false) String search) {
		
		//TODO: Improve this, should not be strPolicy, but just policy
		OrderFormBy orderBy;
		if(strOrderBy.equals(strPolicy)){
			orderBy = OrderFormBy.POLICY;
		} else {
			orderBy = OrderFormBy.getOrderBy(strOrderBy);
		}
		OrderType orderType = OrderType.getOrderType(strOrderType);
		if(search == null || "".equals(search.trim())) {
			return managementForm.getSharedForms(getCurrentUser(request), orderBy, 
				orderType, page, ConstantsWeb.ITEMS_PER_PAGE);
		} else {
			return managementForm.searchSharedForms(getCurrentUser(request), search, orderBy,
				orderType, page, ConstantsWeb.ITEMS_PER_PAGE);
		}			
	}
	
	@RequestMapping(value = "/answers", params = "dwl-csv", method = RequestMethod.GET)
    public String downloadCVS(@RequestParam(value = "id", required = true) String url, 
    		ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		try{ 
			ZipFileDTO zipFileDTO = managementForm.getZipFileWithAnswers(url, getCurrentUser(request));
			if(zipFileDTO != null && zipFileDTO.getData() != null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();  
			    ZipOutputStream zipfile = new ZipOutputStream(bos);
			    String filename = zipFileDTO.getFilename().replaceAll(" ", "");
			    ZipEntry zipentry = new ZipEntry(filename+ConstantsWeb.CSV_EXTENSION);  
		        zipfile.putNextEntry(zipentry);  
		        ByteArrayOutputStream baos = (ByteArrayOutputStream)zipFileDTO.getData(); 
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
			else if(zipFileDTO != null && zipFileDTO.getMessage() != null){				
				response.setContentType(ConstantsWeb.APPLICATION_TEXT);
				response.setHeader("Content-Disposition", "attachment;Filename=\"answers-error"+ConstantsWeb.TXT_EXTENSION + "\"");
				PrintWriter pr = response.getWriter();
				pr.print(zipFileDTO.getMessage());
				pr.flush();
				pr.close();
			} else{
				response.setContentType(ConstantsWeb.APPLICATION_TEXT);
				response.setHeader("Content-Disposition", "attachment;Filename=\"answers-error"+ConstantsWeb.TXT_EXTENSION + "\"");
				PrintWriter pr = response.getWriter();
				pr.print("Error downloading answers.");
				pr.flush();
				pr.close();
			}
		}
		catch(Exception ex) {
			return "form_lister";	
		}
		return "form_lister";
    }
	
	@RequestMapping(value = "/answers", params = "dwl-xml", method = RequestMethod.GET)
    public String downloadXmlAnswers(@RequestParam(value = "id", required = true) String url, 
    		ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		try {	
			Message message = managementForm.getXMLWithAnswers(url, getCurrentUser(request));
			response.setContentType(ConstantsWeb.APPLICATION_XML);			
			if(message != null && message.getType().equals(MessageType.SUCCESS)) {
				String xml = (String)message.getData();
	            response.setHeader("Content-Disposition", "attachment;Filename=\"" + url+ConstantsWeb.XML_EXTENSION + "\"");
				PrintWriter pr = response.getWriter();
				pr.print(xml);
				pr.flush();
				pr.close();
			} else{
				response.setHeader("Content-Disposition", "attachment;Filename=\"answers-error"+ConstantsWeb.XML_EXTENSION + "\"");
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
	
	@RequestMapping(value = "form/listnames", method = RequestMethod.POST)
	public @ResponseBody String getUserListnames(HttpServletRequest request) {
		return managementGroup.getListnamesInJSON(getCurrentUser(request));
	}
}