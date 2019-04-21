<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul id="ul-form-menu">
	<li id="li_form_editor"><spring:message code="form_editor_title"/></li>	
	<c:choose>
		<c:when test="${not empty form.url}">
			<li id="li_form_share"><spring:message code="form_share"/></li>
			<li id="li_view_answers"><spring:message code="form_view_answers"/></li>
		</c:when>
		<c:when test="${empty form.url}">
			<li class="disabled_sub_tab"><spring:message code="form_share"/></li>
			<li class="disabled_sub_tab"><spring:message code="form_view_answers"/></li>
		</c:when>		
	</c:choose>
</ul>