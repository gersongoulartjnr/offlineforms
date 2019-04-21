package br.unifesp.maritaca.persistence.dao;

import java.util.List;
import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.Report;

public interface ReportDAO {
	
	public List<Report> getReportsByFormKey(String formKey, boolean minimal);
	
	public List<Report> getReportsByUserKey(UUID userKey, boolean minimal);
	
	public Report getReportsById(String reportId, boolean minimal);
	
	public void saveReport(Report report);
	
	public void delete(Report report);
	
	/**
	 * This method creates an unique string (10 characters)
	 * @return an string (10 characters)
	 */
	public String getUniqueId();
}