<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="<c:url value='/js/myAccount.js'/>" type="text/javascript"></script>

<div id="my-account" class="form-wrapper">
	<div class="form-header"><spring:message code="account_my_account"/></div>
	<div class="form-body">
		<c:url var="url" value="/my-account.html" />
   		<form:form id="account" modelAttribute="user" method="POST" action="${url}">
			<br />
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="firstName"><spring:message code="account_first_name"/>:</label></div>
				<div class="wrapper-widget">
					<form:input id="firstName" path="firstName" maxlength="50" cssClass="input-large" />
				</div>
			</div>
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="lastName"><spring:message code="account_last_name"/>:</label></div>
				<div class="wrapper-widget">
					<form:input id="lastName" path="lastName" maxlength="50" cssClass="input-large" />
				</div>
			</div>
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="email"><spring:message code="account_email"/>:</label></div>
				<div class="wrapper-widget wrapper-text">
					<span class="data">${user.email}</span>
				</div>
			</div>
			<div class="wrapper-button">
				<div id="updateAccount" class="form-button button-normal"><spring:message code="commons_save" /></div>
			</div>
		</form:form>
	</div>
</div>