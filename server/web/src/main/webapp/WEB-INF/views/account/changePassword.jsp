<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="<c:url value='/js/jquery.sha1.js' />" type="text/javascript"></script>
<script src="<c:url value='/js/myAccount.js'/>" type="text/javascript"></script>

<div id="change-pass" class="form-wrapper">
	<div class="form-header"><spring:message code="account_change_pass"/></div>	
	<div class="form-body">
		<c:url var="url" value="/change-pass.html" />
   		<form:form id="change-pass" modelAttribute="user" method="POST" action="${url}">
			<br />
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="currentPassword"><spring:message code="current_account_password"/>:</label></div>
				<div class="wrapper-widget">
					<form:password id="currentPassword" path="currentPassword" maxlength="50" />
				</div>
			</div>
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="password"><spring:message code="account_password"/>:</label></div>
				<div class="wrapper-widget">
					<form:password id="password" path="password" maxlength="50" />
				</div>
			</div>
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="passwordConfirmation"><spring:message code="account_create_retype_passwd"/>:</label></div>
				<div class="wrapper-widget">
					<form:password id="passwordConfirmation" path="passwordConfirmation" maxlength="50" />
				</div>
			</div>
			<div class="wrapper-button">
				<div id="changePass" class="form-button button-normal"><spring:message code="commons_save" /></div>
			</div>
		</form:form>
	</div>
</div>