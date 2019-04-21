<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<header><spring:message code="login_maritaca"/></header>

<div id="container">
	<div id="wraper">
	<br />
	<c:url var="url" value="/mobile-login.html" />
	<form:form id="login" modelAttribute="user" method="POST" action="${url}">
	<form:hidden path="formId" />
	<div class="wrapper-row">
		<div class="wrapper-label"><label for="email"><spring:message code="account_email"/>:</label></div>
		<div class="wrapper-widget">
			<form:input id="email" path="email" maxlength="50" />
		</div>
	</div>
	<div class="wrapper-row">
		<div class="wrapper-label"><label for="password"><spring:message code="account_password"/>:</label></div>
		<div class="wrapper-widget">
			<form:password id="password" path="password" maxlength="50" />
		</div>
	</div>
	<div class="error">${error_message}</div>
	<div class="request">${request_message}</div>
	
	<div class="wrapper-button">
		<form:button class="main_button"><spring:message code="login"/></form:button>
	</div>
	<div id="openid">
		<c:url var="google_opendid" value="/mobile-login-openid.html?op=Google" />
		<c:url var="yahoo_opendid" value="/mobile-login-openid.html?op=Yahoo" />
		<c:url var="facebook_signin" value="/facebook/mobile-login.html" />
		<a href="${google_opendid}"><img class="icon_social" src="<c:url value="/images/icon_google.png"/>" /></a>
		<a href="${yahoo_opendid}"><img class="icon_social" src="<c:url value="/images/icon_yahoo.png"/>" /></a>
		<a href="${facebook_signin}"><img class="icon_social" src="<c:url value="/images/icon_facebook.png"/>" /></a>
	</div>
	<div>
		<a href="<c:url value="/mobile-create-account.html"/>"><spring:message code="account_create"/></a>
	</div>
	</form:form>
	</div>
</div>

<footer>${currentYear}</footer>