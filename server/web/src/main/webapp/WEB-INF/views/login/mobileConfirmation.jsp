<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<header>${clientId}</header>
<div id="container">
	<div id="wrapper">
		<br />
		<c:url var="url" value="/mobile-confirmation.html" />
	  		<form:form id="login" modelAttribute="oAuthParams" method="POST" action="${url}">  			
			<span class="mobile-text"><spring:message code="login_mobile_message_confirm"/></span>
			<div class="wrapper-button">
				<button type="submit" name="allow" class="main_button"><spring:message code="commons_allow"/></button>
				<button type="submit" name="cancel" name="cancel" class="main_button"><spring:message code="commons_cancel"/></button>
			</div>			
		</form:form>
	</div>
</div>

<footer>${currentYear}</footer>