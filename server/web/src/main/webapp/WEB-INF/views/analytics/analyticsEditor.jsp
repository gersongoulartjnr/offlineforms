<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!--script src="https://maps.googleapis.com/maps/api/js?sensor=false"></script-->
<script src="<c:url value='/js/arrows.js' />" type="text/javascript"></script>		
<script src="<c:url value='/js/analytics/analyticsDictionary.js' />" type="text/javascript"></script>		
<script src="<c:url value='/js/analytics/analyticsCommon.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/analytics/analyticsEditor.js' />" type="text/javascript"></script>
<script src="<c:url value='/js/analytics/analyticsContainer.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/analytics/analyticsItem.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/analytics/analyticsFilter.js'/>" type="text/javascript"></script>		
<script src="<c:url value='/js/analytics/analyticsTransformation.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/analytics/analyticsTransformationItem.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/analytics/analyticsView.js'/>" type="text/javascript"></script>

<div id="a-menu-container">
	<ul id="ul-form-submenu">
			<li><div id="saveAnalytics" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/save.png"/>" /><spring:message code="commons_save"/></div></li>
			<li><div id="deleteAnalytics" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/remove.png"/>" /><spring:message code="commons_delete"/></div></li>
			<li><div id="viewAnalytics" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/view_reports.png"/>" /><spring:message code="view_analytics"/></div></li>
			<li><div id="clearAnalytics" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/clear.png"/>" /><spring:message code="commons_clear"/></div></li>
	</ul>
</div>

<div id="a-header-container">
	<c:url var="url" value="/analytics-editor.html" />
	<form:form id="analyticsForm" modelAttribute="analytics" method="POST" action="${url}">
		<form:hidden id="form_xml" path="formXml" />
		<form:hidden id="form_url" path="formUrl" />
		<form:hidden id="form_collectors" path="formCollectors" />
		<form:hidden id="form_creation_date" path="formCreationDate" />
		<form:hidden id="analytics_doc" path="doc" />
		<form:hidden id="analytics_id" path="id" />			

		<div class="wrapper-row">
			<div class="wrapper-label"><label for="analytics_title"><spring:message code="analytics_title"/>:</label></div>
			<div class="wrapper-widget">
				<form:input id="analytics_title" path="name" size="40" maxlength="50" onkeyup="updateAnalyticsTitle();" />
				<div class="side-right num_collects"><span class="text_bold">${analytics.formNumOfCollects} </span>collects.</div>
			</div>
		</div>
		
	</form:form>	
</div>

<div id="a-body-container">
	<div id="analytics-container">	
		<fieldset id="fs-ac-content">
			<legend id="ac-l-item-name"></legend>
				<label for="item_title"><spring:message code="analytics_name"/>:</label>
				<input type="text" id="item_title" onkeyup="updateItemName();" />
				<div id="addAnalyticsItem" class="btn-analytics side-right"><spring:message code="commons_new"/></div>
			<br />
			<div id="ac-side-left">
				<fieldset id="fs-ac-side-left">
					<legend></legend>			
				</fieldset>
				<br />
				<fieldset id="fs-ac-transformations">
					<legend>Transformations</legend>			
					<div id="d-ac-transformations"></div>					
				</fieldset>
				<br />
				<fieldset id="fs-ac-s-transformations">
					<legend>Transformations 2</legend>			
					<div id="d-ac-sectransformations"></div>					
				</fieldset>
				<br />
				<fieldset id="fs-ac-views">
					<legend>Views</legend>			
					<div id="d-ac-views"></div>					
				</fieldset>
				
			</div>
			
			<div id="ac-side-body">
				<fieldset id="fs-ac-side-body">
					<legend id="ac-l-side-body"></legend>
					<div id="ac-sb-filter"></div>
					<br />
					<div id="ac-sb-transformations"></div>
					<br />
					<div id="ac-sb-sectransformations"></div>
					<br />
					<div id="ac-sb-view"></div>
				</fieldset>
			</div>
		</fieldset>
	</div>
</div>

<div id="dialog-confirm" style="display: none;">
	<p>
		<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>
		<span id="messageDialogConfirm"></span>
	</p>
</div>