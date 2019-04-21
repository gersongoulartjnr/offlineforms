package br.unifesp.maritaca.web.controller.account;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unifesp.maritaca.business.answer.ManagementAnswers;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.persistence.dao.AnswerDAO;
import br.unifesp.maritaca.persistence.dao.ConfigurationDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.Answer;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.web.controller.AbstractController;

@Controller
public class AdminController extends AbstractController {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired ManagementForm managementForm;
	@Autowired ManagementAnswers managementAnswers;
	@Autowired AnswerDAO answerDAO;
	@Autowired UserDAO userDAO;
	@Autowired ConfigurationDAO configurationDAO;

	@RequestMapping(value = "/m-admin", method = RequestMethod.GET)
    public String showView(HttpServletRequest request) {
		System.out.println("admin - get");
		managementForm.populateSolr(getCurrentUser(request));
        return "m_admin";
    }
	
	private boolean isRoot(UserDTO userDTO){
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user != null){
			boolean isRoot = configurationDAO.isRootUser(user.getKey());
			return isRoot;
		}
		return false;
	}
	
	@RequestMapping(value = "/list-answers-by-form", method = RequestMethod.GET)
    public @ResponseBody List<Answer> showAnswersByForm(HttpServletRequest request, Model model, 
    		@RequestParam(value = "form", required = true) String formKey) {
		System.out.println("admin - answers by form: " + formKey);
		List<Answer> answers = null;
		if(isRoot(getCurrentUser(request))){
			answers = answerDAO.findAnswersByFormKey(UUID.fromString(formKey), 100000, 100000);
		}
		return answers;
    }
	
	@RequestMapping(value = "/answer-by-key", method = RequestMethod.GET)
    public @ResponseBody Answer showAnswerByKey(HttpServletRequest request, 
    		@RequestParam(value = "answer", required = true) String answerKey) {
		System.out.println("admin - answer by key: " + answerKey);
		Answer answer = null;
		if(isRoot(getCurrentUser(request))){	
			answer = answerDAO.findAnswerByKey(UUID.fromString(answerKey));
		}
		return answer;
    }
	
	@RequestMapping(value = "/delete-answer", method = RequestMethod.GET)
    public @ResponseBody Answer deleteAnswer(HttpServletRequest request,  
    		@RequestParam(value = "answer", required = true) String answerKey) {
		System.out.println("admin - answer: " + answerKey);
		Answer ans = null;
		if(isRoot(getCurrentUser(request))){
			ans = new Answer();
			ans.setKey(answerKey);
			answerDAO.delete(ans);
		}
		return ans;
    }
	
	@RequestMapping(value = "/update-collectors", method = RequestMethod.GET)
    public @ResponseBody String updateCollectors(HttpServletRequest request) {
		System.out.println("admin - update-collectors");
		managementAnswers.updateAnswersWithUserEmail(getCurrentUser(request));
		System.out.println("Done");
		return "";
    }
}