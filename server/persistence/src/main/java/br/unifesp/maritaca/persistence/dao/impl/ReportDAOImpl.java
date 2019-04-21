package br.unifesp.maritaca.persistence.dao.impl;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.ReportDAO;
import br.unifesp.maritaca.persistence.entity.Report;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

@Service("reportDAO")
public class ReportDAOImpl extends AbstractDAO implements ReportDAO {
	
	private static Logger logger = Logger.getLogger(ReportDAOImpl.class);

	@Override
	public List<Report> getReportsByFormKey(String formKey, boolean minimal) {
		return emHector.cQuery(Report.class, "form", formKey);
	}
	
	@Override
	public List<Report> getReportsByUserKey(UUID userKey, boolean minimal) {
		return emHector.cQuery(Report.class, "user", userKey.toString());
	}
	
	@Override
	public Report getReportsById(String reportId, boolean minimal) {
		List<Report> report = emHector.cQuery(Report.class, "id", reportId);
		if (report == null || report.size() == 0) {
			return null;
		} else if (report.size() == 1) {
			return report.get(0);
		} else {
			//TODO:
			logger.error(reportId + " " + report.size());
			return null;
		}
	}	
	
	@Override
	public void saveReport(Report report) {
		emHector.persist(report);		
	}

	@Override
	public String getUniqueId() {
		String id = UtilsPersistence.randomString();
		List<Report> lstReport = emHector.cQuery(Report.class, "id", id, true);
		if(lstReport.size() == 0){
			return id;
		} else{
			return getUniqueId();
		}
	}

	@Override
	public void delete(Report report) {
		emHector.delete(Report.class, report.getKey());		
	}
}