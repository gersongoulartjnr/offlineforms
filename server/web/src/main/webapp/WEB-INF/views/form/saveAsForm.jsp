<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script type="text/javascript">
	$('#saveAsMainButton').click(function() {
		saveAsFormToServer();
	});
</script>

<div>
	<c:url var="url" value="/form.html" />
	<form:form id="account" modelAttribute="form" method="POST" action="${url}">
		<form:hidden id="save_as_xml" path="xml" />
		<div class="wrapper-row">
			<div class="wrapper-label"><label for="form_title"><spring:message code="form_title"/>:</label></div>
			<div class="wrapper-widget">
				<form:input id="save_as_title" path="title" />
			</div>
			<div><form:errors path="title" cssClass="error" /></div>
		</div>
		<div>
			<div id="saveAsMainButton" class="form-button button-normal"><spring:message code="commons_save"/></div>
		</div>
	</form:form>
</div>