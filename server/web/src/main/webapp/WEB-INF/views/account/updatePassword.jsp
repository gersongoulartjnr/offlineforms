<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="<c:url value='/js/jquery.sha1.js' />" type="text/javascript"></script>
<script src="<c:url value='/js/password.js'/>" type="text/javascript"></script>

<div id="update-pass" class="form-wrapper">
	<div class="form-header"><spring:message code="account_update_pass"/></div>	
	<div class="form-body">
		<c:url var="url" value="/update-pass.html" />
   		<form:form id="update-password" modelAttribute="user" method="POST" action="${url}">
			<br />
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="password"><spring:message code="account_password"/>:</label></div>
				<div class="wrapper-widget">
					<form:password id="password" path="password" maxlength="50" cssClass="input-large" />
				</div>
			</div>
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="passwordConfirmation"><spring:message code="account_create_retype_passwd"/>:</label></div>
				<div class="wrapper-widget">
					<form:password id="passwordConfirmation" path="passwordConfirmation" maxlength="50" cssClass="input-large" />
				</div>
			</div>			
			<form:hidden id="email" path="email"/>
			<form:hidden id="resetPassCode" path="resetPassCode"/>
			<div class="wrapper-button">
				<div id="updatePass" class="form-button button-normal"><spring:message code="commons_save" /></div>
			</div>
	</form:form>
	</div>
</div>