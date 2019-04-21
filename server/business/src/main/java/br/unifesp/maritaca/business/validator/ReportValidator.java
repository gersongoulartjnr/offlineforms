package br.unifesp.maritaca.business.validator;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.report.dto.ReportDTO;
import br.unifesp.maritaca.business.util.UtilsBusiness;

@Component
public class ReportValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return AccountDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ReportDTO reportDTO = (ReportDTO)target;
		
		if("".equals(reportDTO.getReportName().trim())){
			errors.rejectValue("reportName", "report_title_required");
		}

		DateTime start	=  UtilsBusiness.getDateFromString(reportDTO.getStrStart());
		DateTime finish	=  UtilsBusiness.getDateFromString(reportDTO.getStrFinish());
		if(start != null && finish != null){
			boolean starDateIsValid = UtilsBusiness.dateIsLessThanToday(start);
			if(starDateIsValid){			
				Days difDays = Days.daysBetween(start, finish);
				if(difDays.getDays() < 0){
					errors.rejectValue("strFinish", "report_start_later_finish");
				}
				else{
					finish = finish.plusDays(1).minusMillis(1);
					reportDTO.setStart(start.toDate());
					reportDTO.setFinish(finish.toDate());
				}
			}
			else{
				errors.rejectValue("strStart", "report_date_later_current");
			}
		}
		else {
			if(start == null){
				errors.rejectValue("strStart", "report_start_required");
			}
			if(finish == null){
				errors.rejectValue("strFinish", "report_finish_required");
			}
		}		
	}
}