package br.unifesp.maritaca.web.controller.group;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unifesp.maritaca.business.base.dto.WrapperGrid;
import br.unifesp.maritaca.business.enums.OrderGroupBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.group.ManagementGroup;
import br.unifesp.maritaca.business.group.dto.GroupDTO;
import br.unifesp.maritaca.web.controller.AbstractController;
import br.unifesp.maritaca.web.util.ConstantsWeb;

@Controller
public class GroupListerController extends AbstractController {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired private ManagementGroup managementGroup;
	
	@RequestMapping(value = "/groups", method = RequestMethod.GET)
    public String showGroups(HttpServletRequest request) {
		return "group_lister";
    }
	
	@RequestMapping(value = "/groups/my-groups", method = RequestMethod.GET)
	public @ResponseBody WrapperGrid<GroupDTO> myGroups(HttpServletRequest request,
			@RequestParam(value = "orderBy", required = true) String strOrderBy,
			@RequestParam(value = "orderType", required = true) String strOrderType,
			@RequestParam(value = "page", required = true) Integer page) {

		OrderGroupBy orderBy = OrderGroupBy.getOrderBy(strOrderBy);
		OrderType orderType = OrderType.getOrderType(strOrderType);
		return managementGroup.getGroupsByOwner(getCurrentUser(request), orderBy,
				orderType, page, ConstantsWeb.ITEMS_PER_PAGE);		
	}
}