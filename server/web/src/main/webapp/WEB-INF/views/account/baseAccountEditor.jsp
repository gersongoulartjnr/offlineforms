<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form:form id="account" modelAttribute="user" method="POST" action="${url}">
	<div class="wrapper-row">
		<div class="wrapper-label"><label for="firstname"><spring:message code="account_first_name"/>:</label></div>
		<div class="wrapper-widget">
			<form:input id="firstname" path="firstName" maxlength="50" cssClass="large_input" />
		</div>
		<div><form:errors path="firstName" cssClass="error" /></div>
	</div>
	<div class="wrapper-row">
		<div class="wrapper-label"><label for="lastname"><spring:message code="account_last_name"/>:</label></div>
		<div class="wrapper-widget">
			<form:input id="lastname" path="lastName" maxlength="50" cssClass="large_input"/>
		</div>
		<div><form:errors path="lastName" cssClass="error" /></div>
	</div>
	<div class="wrapper-row">
		<div class="wrapper-label"><label for="email"><spring:message code="account_email"/>:</label></div>
		<div class="wrapper-widget">
			<form:input id="email" path="email" maxlength="50" cssClass="large_input" />
		</div>
		<div><form:errors path="email" cssClass="error" /></div>
	</div>
	<div class="wrapper-row">			
		<div class="wrapper-label"><label for="password"><spring:message code="account_password"/>:</label></div>
		<div class="wrapper-widget">
			<form:password id="password" path="password" maxlength="50" cssClass="large_input" />
		</div>
		<div><form:errors path="password" cssClass="error" /></div>
	</div>
	<div class="wrapper-row">			
		<div class="wrapper-label"><label for="retype_password"><spring:message code="account_create_retype_passwd"/>:</label></div>
		<div class="wrapper-widget">
			<form:password id="retype_password" path="passwordConfirmation" maxlength="50" cssClass="large_input" />
		</div>
		<div><form:errors path="passwordConfirmation" cssClass="error" /></div>
	</div>
	<div class="wrapper-row">			
		<div class="wrapper-label"><label for="captcha_code"><spring:message code="account_captcha_code"/>:</label></div>
		<div class="wrapper-widget">
			<form:input id="captcha_code" path="captchaCode" maxlength="5" size="5" />
		</div>
		<div><form:errors path="captchaCode" cssClass="error" /></div>
	</div>
	<div class="wrapper-row">			
		<div class="wrapper-label">&nbsp;</div>
		<div class="wrapper-widget">
			<img id="captchaValue" src="<c:url value="/kaptcha.jpg"/>" />
			<img id="refreshCaptcha" src="<c:url value="/images/refresh.png"/>" />
		</div>
	</div>
	<div class="wrapper-button">
		<form:button class="main_button"><spring:message code="commons_save"/></form:button>
	</div>
</form:form>