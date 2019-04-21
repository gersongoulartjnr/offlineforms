<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ul id="ul-main-menu">
	<li id="tabForms"><a class="main_menu" href="<c:url value="/forms.html"/>"><spring:message code="msg_forms"/></a></li>
	<li id="tabGroups"><a class="main_menu" href="<c:url value="/groups.html"/>"><spring:message code="msg_groups"/></a></li>
</ul>
<a id="btn_new_form" class="form-button new-form-button" href="<c:url value="/form.html"/>" target="_blank">New Form</a><span id="info_new_form"></span><br />