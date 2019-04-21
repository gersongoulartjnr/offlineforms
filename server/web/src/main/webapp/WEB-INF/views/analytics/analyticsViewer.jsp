<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/analytics.css"/>" />
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
<script src="<c:url value='/js/analytics/analyticsCommon.js' />" type="text/javascript"></script>
<script src="<c:url value='/js/analytics/analyticsViewerOption.js' />" type="text/javascript"></script>
<script src="<c:url value='/js/analytics/analyticsViewer.js' />" type="text/javascript"></script>

<div id="analytics-viewer">

	<form:form id="analyticsViewer" modelAttribute="analytics" method="POST">
		<form:hidden id="a_doc" path="doc" />
		<form:hidden id="a_id" path="id" />
		<form:hidden id="a_name" path="name" />
		<form:hidden id="form_id" path="formUrl" />
		<form:hidden id="form_xml" path="formXml" />
		<form:hidden id="uri_server" path="uriServer" />
		
		<form:hidden id="access_token" path="token" />
	</form:form>
		
	<div id="a-v-title">${analytics.name}</div>
	
	<center><div id="a-v-items"></div></center>
	
	<div id="dialog-picture"></div>				
	<div id="dialog-answer"></div>
</div>