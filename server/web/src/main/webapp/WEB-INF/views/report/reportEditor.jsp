<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div id="r-menu-container">
	<ul id="ul-form-submenu">
		<c:if test="${report.reportPermission.update}">
			<li><div id="saveReport" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/save.png"/>" /><spring:message code="commons_save"/></div></li>
		</c:if>
		<c:if test="${not empty report.reportId}">
			<c:if test="${report.reportPermission.remove}">
				<li><div id="deleteReport" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/remove.png"/>" /><spring:message code="commons_delete"/></div></li>
			</c:if>
			<c:if test="${report.reportPermission.read}">
				<li><div id="viewReport" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/view_reports.png"/>" /><spring:message code="view_report"/></div></li>
			</c:if>
		</c:if>
		<c:if test="${report.reportPermission.update}">
			<li><div id="clearReport" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/clear.png"/>" /><spring:message code="commons_clear"/></div></li>
		</c:if>			
	</ul>
</div>
<div id="r-header-container">
<c:if test="${report.reportPermission.update}">
	<table>
		<tr>
			<td id="r-h-body">
				<c:url var="url" value="/report.html" />
				<form:form id="reportForm" modelAttribute="report" method="POST" action="${url}">
					<form:hidden id="form_xml" path="formXml" />
					<form:hidden id="report_xml" path="reportXml" />
					<form:hidden id="form_url" path="formUrl" />
					<form:hidden id="report_id" path="reportId" />				
					
					<div class="wrapper-row">
						<div class="wrapper-label"><label for="report_title"><spring:message code="report_title"/>:</label></div>
						<div class="wrapper-widget">
							<form:input id="report_title" path="reportName" maxlength="50" onkeyup="updateReportTitle();" />
						</div>
						<div><form:errors path="reportName" cssClass="error" /></div>
					</div>
					<div class="wrapper-row">	
						<div class="wrapper-label"><label for="report_title"><spring:message code="form_title"/>:</label></div>
						<div class="wrapper-widget">
							<div id="r-b-title">${report.formName}</div>
						</div>
					</div>
					<div class="wrapper-row">
						<div class="wrapper-label"><label for="start_date"><spring:message code="report_start"/>:</label></div>
						<div class="wrapper-widget">
							<form:input id="start_date" path="strStart" readonly="true" />
						</div>
						<div><form:errors path="strStart" cssClass="error" /></div>
					</div>
					<div class="wrapper-row">
						<div class="wrapper-label"><label for="finish_date"><spring:message code="report_finish"/>:</label></div>
						<div class="wrapper-widget">
							<form:input id="finish_date" path="strFinish" readonly="true" />
						</div>
						<div><form:errors path="strFinish" cssClass="error" /></div>						
					</div>
				</form:form>
			</td>
			<td id="r-h-separator">&nbsp;</td>			
			<td id="r-h-icon">
				<c:if test="${not empty report.formIcon}">
					<img class="report-icon" src="${report.formIcon}"  />
				</c:if>
				<c:if test="${empty report.formIcon}">
					<img class="report-icon" src="<c:url value="/images/project_logo.png"/>" /> 
				</c:if>
			</td>
		</tr>
	</table>
</c:if>		
</div>
<div id="r-body-container">
	<div id="report-header">
		
	</div>
	<div id="report-container">
		<div id="rc-form">
			<fieldset id="fs-rc-form">
				<legend></legend>
			</fieldset>
		</div>
		<div id="rc-report">
			<fieldset id="fs-rc-report">
				<legend id="lgd_report"></legend>
			</fieldset>
		</div>
		
		<div id="rc-properties">
			<fieldset id="fs-rc-properties">
				<legend>Properties</legend>		
			</fieldset>				
		</div>		
	</div>
</div>

<div id="dialog-confirm" style="display: none;">
	<p>
		<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>
		<span id="messageDialogConfirm"></span>
	</p>
</div>