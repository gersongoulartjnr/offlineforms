package br.unifesp.maritaca.business.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.unifesp.maritaca.business.answer.FormXmlParser;
import br.unifesp.maritaca.business.answer.ManagementAnswers;
import br.unifesp.maritaca.business.answer.dto.AnswerDTO;
import br.unifesp.maritaca.business.answer.dto.AnswerListerDTO;
import br.unifesp.maritaca.business.answer.dto.QuestionAnswerDTO;
import br.unifesp.maritaca.business.base.dto.RuleService;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.ReportItemOpType;
import br.unifesp.maritaca.business.exception.AuthorizationDenied;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.report.dto.ReportItemDTO;
import br.unifesp.maritaca.business.report.dto.ReportItemList;
import br.unifesp.maritaca.business.report.dto.ReportItemWParams;
import br.unifesp.maritaca.persistence.dao.FormDAO;
import br.unifesp.maritaca.persistence.dao.ReportDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.Report;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.permission.Document;
import br.unifesp.maritaca.persistence.permission.Operation;
import br.unifesp.maritaca.persistence.permission.Permission;

@Service("itemCalculator")
public class ReportItemCalculatorImpl implements ReportItemCalculator {

	@Autowired private FormDAO formDAO;
	@Autowired private UserDAO userDAO;
	@Autowired private ReportDAO reportDAO;
	
	@Autowired private ManagementAnswers managementAnswers;
	@Autowired private RuleService ruleService;
	

	@Override
	public String computeSimpleResponse(UserDTO userDTO, ReportItemDTO ritemDTO) {
		// TODO compute when answers are inserted
		Form form = formDAO.getFormByUrl(ritemDTO.getFormId(), false);
		AnswerListerDTO answerListerDTO = this.validateInputs(userDTO, ritemDTO, form);
		if (answerListerDTO.getAnswers() == null || answerListerDTO.getAnswers().isEmpty() ) {
			return String.valueOf(Double.NaN);			
		}
		
		String result = "";		
		switch (ReportItemOpType.getOpType(ritemDTO.getOp())) {
			case MAX:
				result = String.valueOf(this.getMax(ritemDTO, answerListerDTO.getAnswers()));
				break;
			case MIN:
				result = String.valueOf(this.getMin(ritemDTO, answerListerDTO.getAnswers()));
				break;
			case AVG:
				result = String.valueOf(this.getAverage(ritemDTO, answerListerDTO.getAnswers()));
				break;
			case SUM:
				result = String.valueOf(this.getSum(ritemDTO, answerListerDTO.getAnswers()));
				break;
			default:
				throw new MaritacaException("Invalid Report Item Type for this operation");
		}
		return result;
	}

	@Override
	public HashMap<String, String> computeHashResponse(UserDTO userDTO, ReportItemDTO ritemDTO) {
		Form form = formDAO.getFormByUrl(ritemDTO.getFormId(), false);
		AnswerListerDTO answerListerDTO = this.validateInputs(userDTO, ritemDTO, form);
		HashMap<String, String> result = null;
		if (answerListerDTO.getAnswers() == null || answerListerDTO.getAnswers().isEmpty() ) {
			return result;			
		}
		
		switch (ReportItemOpType.getOpType(ritemDTO.getOp())) {
			case TOTALBYVALUE:
				result = this.getTotalByValues((ReportItemWParams)ritemDTO, answerListerDTO.getAnswers(), form);
				break;
			default:
				throw new MaritacaException("Invalid Report Item Type for this operation");
		}
		return result;
	}

	@Override
	public List<String> listAnswersResponse(UserDTO userDTO, ReportItemDTO ritemDTO) {
		Form form = formDAO.getFormByUrl(ritemDTO.getFormId(), false);
		if(form == null){
			throw new MaritacaException(ritemDTO.getFormId() + " form does not exist");
		}
		
		AnswerListerDTO answerListerDTO = this.validateInputs(userDTO, ritemDTO, form);
		List<String> values = new ArrayList<String>();
		if (answerListerDTO.getAnswers() == null || answerListerDTO.getAnswers().isEmpty() ) {
			return values;			
		}
		
		switch (ReportItemOpType.getOpType(ritemDTO.getOp())) {
			case LIST_LAST_N:
				values = this.listLastNAnswers((ReportItemList)ritemDTO, answerListerDTO.getAnswers());
				break;
			case LIST_ALL:
			case NEAREST_POINTS:
				values = this.listAllAnswers((ReportItemList)ritemDTO, answerListerDTO.getAnswers());
				break;
			default:
				throw new MaritacaException("Invalid Report Item Type for this operation");
		}
		return values;
	}
	
	private List<String> listAllAnswers(ReportItemList ariDTO, List<AnswerDTO> answers) {
		List<String> values = new ArrayList<String>();
		for (AnswerDTO answer : answers) {
			List<QuestionAnswerDTO> questions = answer.getAnswers();
			QuestionAnswerDTO qa = questions.get(ariDTO.getQuestionId());
			if("".equals(qa.getValue()) || qa.getValue() == null){
				continue;
			} else {
				values.add(qa.getValue());
			}
		}
		return values;
	}
	
	private List<String> listLastNAnswers(ReportItemList ariDTO, List<AnswerDTO> answers) {
		List<String> values = new ArrayList<String>();
		int counter = 0;
		for (AnswerDTO answer : answers) {
			if (counter >= ariDTO.getNumAnswers()) {
				break;
			}
			List<QuestionAnswerDTO> questions = answer.getAnswers();
			QuestionAnswerDTO qa = questions.get(ariDTO.getQuestionId());
			if("".equals(qa.getValue()) || qa.getValue() == null){
				continue;
			} else {
				values.add(qa.getValue());
			}
			counter++;
		}
		return values;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, String> getTotalByValues(ReportItemWParams ritemDTO, List<AnswerDTO> answers, Form form) {
		HashMap<String, String> map = FormXmlParser.getValuesFromMultiChoiceQuestion(form.getXml(), ritemDTO.getQuestionId());
		// filtering result
		List<String> keys = new ArrayList<String>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (!ritemDTO.getParameters().contains(entry.getKey())) {
				keys.add(entry.getKey());
			}
		}
		for (String key : keys) {
			map.remove(key);
		}
		HashMap<String, Integer> totalValues = new HashMap<String, Integer>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			totalValues.put(entry.getValue(), 0);
		}
		for(AnswerDTO answer : answers) {
			List<QuestionAnswerDTO> questions = answer.getAnswers();
			QuestionAnswerDTO qa = questions.get(ritemDTO.getQuestionId());
			if(qa.getValue() == null || (!qa.getValue().startsWith("{") && !qa.getValue().endsWith("}"))){
				continue;
			}
			HashMap<String, String> dbValues = (new Gson()).fromJson(qa.getValue(), HashMap.class);
			for (Map.Entry<String, String> entry : dbValues.entrySet()) {
				if (totalValues.get(entry.getValue()) == null) {
					continue;
				}
				int total = totalValues.get(entry.getValue());			
				totalValues.put(entry.getValue(), total+1);
			}
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {			
			map.put(entry.getKey(), String.valueOf(totalValues.get(entry.getValue())));
		}
		return map;
	}

	private Double getMax(ReportItemDTO ariDTO, List<AnswerDTO> answers) {
		Double max = 0.0;
		boolean flag = false;
		for(AnswerDTO answer : answers) {
			List<QuestionAnswerDTO> questions = answer.getAnswers();
			QuestionAnswerDTO qa = questions.get(ariDTO.getQuestionId());
			if("".equals(qa.getValue()) || qa.getValue() == null){
				continue;
			} else {
				double value = Double.parseDouble(qa.getValue());
				max = (value > max) ? value : max;
				flag = true;
			}
		}
		return flag ? max : Double.NaN;
	}
	
	private Double getMin(ReportItemDTO ariDTO, List<AnswerDTO> answers) {
		Double min = Double.MAX_VALUE;
		boolean flag = false;
		for(AnswerDTO answer : answers) {
			List<QuestionAnswerDTO> questions = answer.getAnswers();
			QuestionAnswerDTO qa = questions.get(ariDTO.getQuestionId());
			if("".equals(qa.getValue()) || qa.getValue() == null){
				continue;
			} else {
				double value = Double.parseDouble(qa.getValue());			
				min = (value < min) ? value : min;
				flag = true;
			}
		}
		return flag ? min : Double.NaN;
	}

	private Double getAverage(ReportItemDTO ariDTO, List<AnswerDTO> answers) {
		Double average = 0.0;
		int totalValues = 0;
		boolean flag = false;
		for(AnswerDTO answer : answers) {
			List<QuestionAnswerDTO> questions = answer.getAnswers();
			QuestionAnswerDTO qa = questions.get(ariDTO.getQuestionId());
			if("".equals(qa.getValue()) || qa.getValue() == null){
				continue;
			} else {
				average += Double.parseDouble(qa.getValue());
				totalValues++;
				flag = true;
			}
		}
		return (flag && totalValues > 0) ? (average / totalValues) : Double.NaN;
	}
	
	private Double getSum(ReportItemDTO ariDTO, List<AnswerDTO> answers) {
		Double total = 0.0;
		boolean flag = false;
		for(AnswerDTO answer : answers) {
			List<QuestionAnswerDTO> questions = answer.getAnswers();
			QuestionAnswerDTO qa = questions.get(ariDTO.getQuestionId());
			if("".equals(qa.getValue()) || qa.getValue() == null){
				continue;
			} else {
				total += Double.parseDouble(qa.getValue());
				flag = true;
			}
		}
		return flag ? total : Double.NaN;
	}

	private AnswerListerDTO validateInputs(UserDTO userDTO, ReportItemDTO ritemDTO, Form form) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if (form == null || user == null) {
			throw new MaritacaException("Invalid Form or invalid User!");
		}
		Permission rpermission = ruleService.getPermission(form, user.getKey(), Document.REPORT);
		if (rpermission != null && rpermission.getRead()) {
			AnswerListerDTO answerListerDTO = managementAnswers.findAnswersDTO(form.getKey().toString(), userDTO);
			answerListerDTO.setAnswers(limitAnswersByDate(answerListerDTO.getAnswers(), ritemDTO.getReportId()));
			return answerListerDTO;
		}
		throw new AuthorizationDenied(Document.REPORT, form.getKey(), user.getKey(), Operation.READ);
	}

	private List<AnswerDTO> limitAnswersByDate(List<AnswerDTO> answers, String reportUrl) {
		Report report = reportDAO.getReportsById(reportUrl, false);
		if (answers.isEmpty() || 
				(report.getStart() == null && report.getFinish() == null) || 
				(answers.get(answers.size()-1).getCreationDate().floatValue() >= report.getStart().floatValue() && 
						answers.get(0).getCreationDate().floatValue() <= report.getFinish().floatValue())){
			return answers;
		}
		if (report.getStart() != null && report.getFinish() == null) {
			return cutAnswersBetween(answers, report.getStart(), answers.get(answers.size()-1).getCreationDate());
		}
		if (report.getStart() == null && report.getFinish() != null) {
			return cutAnswersBetween(answers, answers.get(0).getCreationDate(), report.getFinish());
		}		
		return cutAnswersBetween(answers, report.getStart(), report.getFinish());
	}

	private List<AnswerDTO> cutAnswersBetween(List<AnswerDTO> answers, Long start, Long finish) {
		List<AnswerDTO> result = new ArrayList<AnswerDTO>();
		int startPosition = searchClosestStartPosition(answers, finish);
		int finishPosition = searchClosestFinishPosition(answers, start);
		for (int i = startPosition; i <= finishPosition; i++) {
			result.add(answers.get(i));
		}
		return result;
	}

	private int searchClosestFinishPosition(List<AnswerDTO> answers, Long start) {
		for (int i = answers.size()-1; i >= 0; i--) {
			if (answers.get(i).getCreationDate().floatValue() >= start.floatValue() ) {
				return i;
			}
		}
		return -1;
	}

	private int searchClosestStartPosition(List<AnswerDTO> answers, Long finish) {
		for (int i = 0; i < answers.size(); i++) {
			if (answers.get(i).getCreationDate().floatValue() <= finish.floatValue() ) {
				return i;
			}
		}
		return 0;
	}
}