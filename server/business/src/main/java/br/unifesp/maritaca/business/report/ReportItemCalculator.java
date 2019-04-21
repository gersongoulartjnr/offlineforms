package br.unifesp.maritaca.business.report;

import java.util.HashMap;
import java.util.List;

import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.report.dto.ReportItemDTO;

public interface ReportItemCalculator {

	HashMap<String, String> computeHashResponse(UserDTO userDTO, ReportItemDTO reportItemDTO);

	String computeSimpleResponse(UserDTO userDTO, ReportItemDTO reportItemDTO);

	List<String> listAnswersResponse(UserDTO userDTO, ReportItemDTO reportItemDTO);
	
}