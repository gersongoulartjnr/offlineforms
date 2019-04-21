<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="<c:url value='/js/autocompleteManager.js' />" type="text/javascript"></script>
<script src="<c:url value='/js/group.js' />" type="text/javascript"></script>

<style type="text/css">
	#usersList {
		float: left;
		top: 0;
		left: 0;
		width: 500px;
	}
	#groupList {
		float: left;
		top: 0;
		right: 0;
		width: 400px;
	}
</style>

<div id="container_menu"><jsp:include page="../common/mainMenu.jsp" flush="true"/></div>
<div id="container_body">
<div id="group-form" class="form-wrapper">
	<div id="form-title" class="form-header"><spring:message code="list"/>: ${group.name}</div>
	<div class="form-body">
		<c:url var="url" value="/group.html" />
   		<form:form id="group" modelAttribute="group" method="POST" action="${url}">
   			<br />
   			<form:hidden path="key" />
   			<div class="wrapper-row">
				<div class="wrapper-label"><label for="group_name"><spring:message code="list_name"/>:</label></div>
				<div class="wrapper-widget">
					<form:input id="group_name" path="name" maxlength="50" />
				</div>
				<div><form:errors path="name" cssClass="error" /></div>
			</div>
			<div class="wrapper-row">
				<div class="wrapper-label"><label for="group_description"><spring:message code="list_description"/>:</label></div>
				<div class="wrapper-widget">
					<form:textarea id="group_description" path="description" rows="2" cols="60" />
				</div>
				<div><form:errors path="description" cssClass="error" /></div>
			</div>
			<div id="usersList">
				<div class="wrapper-row">
					<div class="wrapper-label"><label for="form_group"><spring:message code="form_list_of_users"/>:</label></div>
					<div class="wrapper-widget">
						<input type="text" id="form_group" />
						<input type="hidden" id="form_group_value" />
					</div>
				</div>
				<div><form:errors path="groupsList" cssClass="error" /></div>
				<div class="wrapper-row">
					<div class="groups_container">					
						<div><ul id="groups_list"></ul></div>
					</div>
				</div>
			</div>
			<c:if test="${not empty group and not empty group.currentGroups}">
				<div id="groupList">
					<div class="wrapper-row">
						<div class="wrapper-label"><label for="form_group"><spring:message code="form_list_of_lists"/>:</label></div>
						<div class="wrapper-widget">
							<select id="form_current_group" onchange="loadUsersGroup(this);"></select>
						</div>
					</div>
					<div><form:errors path="groupsList" cssClass="error" /></div>
					<div class="wrapper-row">
						<div id="groups_list_groups_container" class="groups_container">					
							<div><ul id="groups_list_groups"></ul></div>
						</div>
					</div>
				</div>
			</c:if>	
			<c:if test="${not empty group and not empty group.currentGroups}">	
				<script type="text/javascript">
					addCurrentGroups('${group.currentGroups}');
				</script>
			</c:if>	
			<c:if test="${not empty group}">
				<script type="text/javascript">
					addGroups('${group.groupsList}');
				</script>
			</c:if>
			<form:hidden id="form_groups" path="groupsList" />
			<div class="wrapper-button">
				<form:button type="button"  class="form-button" onclick="doSubmit();" ><spring:message code="commons_save"/></form:button>
				<form:button type="button"  class="form-button" onclick="return redirectToIndex();"><spring:message code="commons_cancel" /></form:button>
			</div>
   		</form:form>
	</div>
</div>

</div>