<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/h5style.css"/>" />
<script src="<c:url value='/js/autocompleteManager.js' />" type="text/javascript"></script>
<script src="<c:url value='/js/formShare.js' />" type="text/javascript"></script>

<div id="container_menu"><jsp:include page="../common/mainMenu.jsp" flush="true"/></div>
<div id="container_body">
<div id="group-form" class="form-wrapper">
	<div id="form-title" class="form-header"><spring:message code="msg_share"/>: ${form.title}</div>
		<div class="form-body">			
			<c:url var="url" value="/form-share.html" />
			<form:form id="account" modelAttribute="form" method="POST" action="${url}">
				<br />
				<div class="wrapper-row">
					<div class="wrapper-label"><label for="form_url"><spring:message code="form_share_url"/>:</label></div>
					<div class="wrapper-widget wrapper-text">
						<span id="span_url" class="data"></span>
						<form:hidden id="form_url" path="url" />
					</div>
				</div>
				<div class="wrapper-row">
					<div class="wrapper-label"><label for="form_policy"><spring:message code="form_policy"/>:</label></div>
					<div class="wrapper-widget">
						<c:choose>
							<c:when test="${form.strPolicy=='private'}">
								<form:radiobuttons path="strPolicy" items="${form.policyItems}" class="data" onclick="toggleGroupsList();" />
							</c:when>
							<c:otherwise>
								<form:radiobuttons path="strPolicy" items="${form.policyItems}" class="data" onclick="toggleGroupsList();" disabled="true" />
							</c:otherwise>
						</c:choose>								
					</div>
				</div>
				<div id="formShared">
					<div class="wrapper-row">
						<div class="wrapper-label"><label for="form_group"><spring:message code="form_list_of_users"/>:</label></div>
						<div class="wrapper-widget">
							<input type="text" id="form_group" />
							<input type="hidden" id="form_group_value" />
						</div>
					</div>
					<div class="wrapper-row">
						<div class="groups_container">				
							<div><ul id="groups_list"></ul></div>
						</div>
					</div>
				</div>
				<c:if test="${not empty form}">						
					<script type="text/javascript">							
						addGroups('${form.groupsList}');
					</script>
				</c:if>
				<form:hidden id="form_groups" path="groupsList" />
				<div class="wrapper-button">
					<form:button type="button" onclick="doSubmit();" class="form-button"><spring:message code="commons_save" /></form:button>
					<form:button type="button" onclick="return redirectToIndex();" class="form-button"><spring:message code="commons_cancel" /></form:button>
				</div>			
			</form:form>		
		</div>
	</div>
</div>