<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div>
	<div class="side-left">
		<a href="<c:url value="/"/>"><img src="<c:url value="/images/project_logo.png"/>" class="project_logo" /></a>
	</div>
	<div class="side-left">
		<div class="project_name"><spring:message code="project_name"/></div>				
		<div class="project_fullname"><spring:message code="project_name_full"/></div>
		<div class="project_version"><spring:message code="project_version"/></div>
	</div>
	<div class="side-right">
		<div id="openid">
			<c:url var="google_opendid" value="/login-openid.html?op=Google" />
			<c:url var="yahoo_opendid" value="/login-openid.html?op=Yahoo" />
			<c:url var="facebook_signin" value="/facebook/login.html" />
			
			<a href="${google_opendid}" id="google_openid"><img class="icon_social" src="<c:url value="/images/icon_google.png"/>" /></a>
			<a href="${yahoo_opendid}" id="google_openid"><img class="icon_social" src="<c:url value="/images/icon_yahoo.png"/>" /></a>
			<a href="${facebook_signin}" id="facebook_signin"><img class="icon_social" src="<c:url value="/images/icon_facebook.png"/>" /></a>
		</div>
		<form:form id="login" modelAttribute="user" method="POST" action="${url}">
		<table>
			<tr>
				<td id="logged_user_name">
						<div class="login-label"><label for="email"><spring:message code="account_email"/>:</label></div>
				</td>
				<td id="logged_user_name">
						<div class="login-label"><label for="password"><spring:message code="account_password"/>:</label></div>
				</td>
				<td>
				</td>
			</tr>
			<tr>	
				<td>		
					<div><form:input id="email" path="email" maxlength="50" /></div>
				</td>						
				<td>
					<div><form:password id="password" path="password" maxlength="50" /></div>
				</td>
				<td>
					<div class="login-wrapper-button">
						<form:button class="form-button"><spring:message code="login"/></form:button>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div id="divCreateAccount">
						<a href="<c:url value="/create-account.html"/>" class="header_link" id="create_account">
							<spring:message code="sign_up"/>
						</a>
					</div>
				</td>
				<td>
					<div class="error">${error_message}</div>
					<div id="divCreateAccount">
						<a href="<c:url value="/forgot-pass.html"/>" class="header_link" id="create_account">
							<spring:message code="forgot_password"/>
						</a>
					</div>
				</td>
				<td>					
				</td>
		</table>
		</form:form>
	</div>
</div>