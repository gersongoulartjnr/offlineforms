<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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
		<br />
		<a href="<c:url value="?lang=en"/>"><img src="<c:url value="/images/languages/en.png"/>" /></a>
		<a href="<c:url value="?lang=pt"/>"><img src="<c:url value="/images/languages/br.png"/>" /></a>
		<a href="<c:url value="?lang=es"/>"><img src="<c:url value="/images/languages/es.png"/>" /></a>
		<br /><br />
		<table>
			<tr>
				<td id="logged_user_name">
					<c:if test="${not empty currentUser.username}">			
						<ul id="user_menu">
							<li><span class="user_info">${currentUser.fullName}</span>
								<ul>
									<li><a href="<c:url value="/my-account.html"/>"><spring:message code="msg_my_profile"/></a></li>
									<li><a href="<c:url value="/change-pass.html"/>"><spring:message code="msg_change_pass"/></a></li>		
								</ul>						
							</li>
						</ul>
					</c:if>
				</td>						
				<td>
					<c:if test="${not empty currentUser.username}">			
						<a id="logout_button" href="<c:url value="/logout.html"/>"><img class="logout_button" src="<c:url value="/images/logout_button.png"/>" /></a>
					</c:if>					
				</td>
			</tr>
		</table>
	</div>
</div>