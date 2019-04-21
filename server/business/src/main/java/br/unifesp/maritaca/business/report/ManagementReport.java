package br.unifesp.maritaca.business.report;

import java.util.List;

import br.unifesp.maritaca.business.answer.dto.ZipFileDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.report.dto.ReportDTO;
import br.unifesp.maritaca.business.report.dto.SimpleReportDTO;

public interface ManagementReport {
	
	public List<SimpleReportDTO> findReportsByFormAndByUser(String formKey, UserDTO userDTO);
	
	public List<SimpleReportDTO> findReportsByForm(String formKey, UserDTO userDTO);
	
	public ReportDTO getReportDataByFormAndByReport(UserDTO userDTO, String formUrl, String reportId);
	
	public Message saveReport(UserDTO userDTO, ReportDTO reportDTO);
	
	public Message deleteReportByReportId(UserDTO userDTO, String formUrl, String reportId);

	/**
	 * Get the report data by username and report id
	 * @param userDTO
	 * @param reportId
	 * @return
	 */
	public ReportDTO getReportDataById(UserDTO userDTO, String reportId);

	/**
	 * Get the report data by user's OAuthToken and report id
	 * @param userOAuthToken
	 * @param reportId
	 * @return
	 */
	public ReportDTO getReportDataByTokenAndId(String userOAuthToken, String reportId);
	
	public ZipFileDTO getCsvData(UserDTO userDTO, ReportDTO reportDTO);
}