package br.unifesp.maritaca.web.controller.answers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unifesp.maritaca.business.answer.ManagementAnswers;
import br.unifesp.maritaca.business.answer.dto.WrapperAnswers;
import br.unifesp.maritaca.business.enums.OrderAnswerBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.web.controller.AbstractController;
import br.unifesp.maritaca.web.util.ConstantsWeb;

@Controller
public class AnswersListerController extends AbstractController {

	private static final long serialVersionUID = 1L;
	private static final String FORM = "form";
	
	@Autowired private ManagementAnswers managementAnswers;
	
	@RequestMapping(value = "/answers", method = RequestMethod.GET)
    public String showView(@RequestParam(value = "id", required = true) String url, 
    		ModelMap model, HttpServletRequest request) {
		model.addAttribute(FORM, new FormDTO(url));
        return "answers_lister";
    }
	
	@RequestMapping(value = "/answers/form", method = RequestMethod.GET)
	public @ResponseBody WrapperAnswers answersByForms(HttpServletRequest request, 
			@RequestParam(value = "id", required = true) String formUrl,
			@RequestParam(value = "orderBy", required = true) String strOrderBy,
			@RequestParam(value = "orderType", required = true) String strOrderType,
			@RequestParam(value = "page", required = true) Integer page) {
		
		OrderAnswerBy orderBy = OrderAnswerBy.getOrderBy(strOrderBy);
		OrderType orderType = OrderType.getOrderType(strOrderType);
		return managementAnswers.getAnswersByUserAndForm(getCurrentUser(request), formUrl, 
				orderBy, orderType, page, ConstantsWeb.ITEMS_PER_PAGE);		
	}
}