package br.unifesp.maritaca.ws.mock;

import java.util.List;

import br.unifesp.maritaca.business.answer.dto.ZipFileDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.report.ManagementReport;
import br.unifesp.maritaca.business.report.dto.ReportDTO;
import br.unifesp.maritaca.business.report.dto.SimpleReportDTO;

public class ManagementReportImplMock implements ManagementReport {

	@Override
	public List<SimpleReportDTO> findReportsByFormAndByUser(String formKey,
			UserDTO userDTO) {
		return null;
	}

	@Override
	public ReportDTO getReportDataByFormAndByReport(UserDTO userDTO,
			String formUrl, String reportId) {
		return null;
	}

	@Override
	public Message saveReport(UserDTO userDTO, ReportDTO reportDTO) {
		return null;
	}

	@Override
	public Message deleteReportByReportId(UserDTO userDTO, String formUrl,
			String reportId) {
		return null;
	}

	@Override
	public ReportDTO getReportDataById(UserDTO userDTO, String reportId) {
		return null;
	}

	@Override
	public ZipFileDTO getCsvData(UserDTO userDTO, ReportDTO reportDTO) {
		return null;
	}

	@Override
	public ReportDTO getReportDataByTokenAndId(String userOAuthToken, String reportId) {
		return null;
	}

	@Override
	public List<SimpleReportDTO> findReportsByForm(String formKey,	UserDTO userDTO) {
		return null;
	}
}