package br.unifesp.maritaca.ws.mock;

import java.util.HashMap;
import java.util.List;

import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.ReportItemOpType;
import br.unifesp.maritaca.business.report.ReportItemCalculator;
import br.unifesp.maritaca.business.report.dto.ReportItemDTO;

public class ReportItemCalculatorImplMock implements ReportItemCalculator {
	
	@Override
	public String computeSimpleResponse(UserDTO userDTO, ReportItemDTO ariDTO) {
		String result = "";
		switch (ReportItemOpType.getOpType(ariDTO.getOp())) {
			case MAX:
				result = "10.0";
				break;
			case MIN:
				result = "12.0";
				break;
			case AVG:
				result = "14.0";
				break;
		}
		return result;
	}

	@Override
	public HashMap<String, String> computeHashResponse(UserDTO userDTO,
			ReportItemDTO reportItemDTO) {
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("1", "40");
		result.put("2", "50");
		return result;
	}

	@Override
	public List<String> listAnswersResponse(UserDTO userDTO,
			ReportItemDTO reportItemDTO) {
		return null;
	}
}
