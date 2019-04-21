package br.unifesp.maritaca.business.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.group.dto.GroupDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;

@Component
public class GroupValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return AccountDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		GroupDTO groupDTO = (GroupDTO)target;
		String groupName  = groupDTO.getName();
		String[] errGroupNameArgs	= {	String.valueOf(ConstantsBusiness.MIN_FIRSTNAME_SIZE), 
				String.valueOf(ConstantsBusiness.MAX_FIRSTNAME_SIZE)};
		
		if("".equals(groupName.trim()) || groupName.length() <= ConstantsBusiness.MIN_FIRSTNAME_SIZE || 
				groupName.length() >= ConstantsBusiness.MAX_FIRSTNAME_SIZE) {
			errors.rejectValue("name", "error_group_name_size", errGroupNameArgs, null);
		}
		if(groupDTO.getGroupsList().equals("") || groupDTO.getGroupsList().equals("[]") 
				 || groupDTO.getGroupsList().equals("[\"\"]")) {
			errors.rejectValue("groupsList", "error_group_groupslits_required");
		}
	}
}