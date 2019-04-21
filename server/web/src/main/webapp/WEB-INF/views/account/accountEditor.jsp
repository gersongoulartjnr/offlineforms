<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="<c:url value='/js/jquery.sha1.js' />" type="text/javascript"></script>
<script src="<c:url value='/js/newAccount.js'/>" type="text/javascript"></script>

<div id="account-form" class="wrapper-form">
	<div class="form-header"><spring:message code="account_create"/></div>
	<div id="newAccountForm" class="form-body">			
		<c:url var="url" value="/create-account.html" />		
		<form:form id="account" modelAttribute="user" method="POST" action="${url}">
			<br />
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="firstName"><spring:message code="account_first_name"/>:</label></div>
				<div class="wrapper-widget">
					<form:input id="firstName" path="firstName" size="20" maxlength="50" cssClass="input-large" />
				</div>
			</div>
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="lastName"><spring:message code="account_last_name"/>:</label></div>
				<div class="wrapper-widget">
					<form:input id="lastName" path="lastName" size="25" maxlength="50" cssClass="input-large"/>
				</div>
			</div>
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="email"><spring:message code="account_email"/>:</label></div>
				<div class="wrapper-widget">
					<form:input id="email" path="email" maxlength="50" cssClass="input-large" />
				</div>
			</div>
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
			<div class="wrapper-row">			
				<div class="wrapper-label"><label for="captchaCode"><spring:message code="account_captcha_code"/>:</label></div>
				<div class="wrapper-widget">
					<form:input id="captchaCode" path="captchaCode" maxlength="5" size="5" />
				</div>
			</div>
			<div class="wrapper-row">
				<div class="wrapper-label">&nbsp;</div>
				<div class="wrapper-widget">
					<img id="captchaValue" src="<c:url value="/kaptcha.jpg"/>" />
					<img id="refreshCaptcha" src="<c:url value="/images/refresh.png"/>" />
				</div>
			</div>
			<div class="wrapper-button">
				<div id="createAccount" class="form-button button-normal"><spring:message code="commons_save" /></div>
			</div>
		</form:form>			
	</div>
</div>