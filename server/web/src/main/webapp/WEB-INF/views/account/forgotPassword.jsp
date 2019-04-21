<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="<c:url value='/js/password.js'/>" type="text/javascript"></script>

<div id="forgot-pass" class="form-wrapper">
	<div class="form-header"><spring:message code="account_forgot_pass"/></div>	
	<div class="form-body">
		<c:url var="url" value="/forgot-pass.html" />
   		<form:form id="forgot-password" modelAttribute="user" method="POST" action="${url}">
   			<br />
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="email"><spring:message code="account_email"/>:</label></div>
				<div class="wrapper-widget">
					<form:input id="email" path="email" maxlength="50" cssClass="input-large" />
				</div>
			</div>
			<div class="wrapper-button">
				<div id="forgotPass" class="form-button button-normal"><spring:message code="commons_send" /></div>
			</div>
	</form:form>
	</div>
</div>