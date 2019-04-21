<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/reports.css"/>" />
<script src="<c:url value='/js/report/reportViewer.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/report/h5ReportClass.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/report/h5ReportFieldClass.js'/>" type="text/javascript"></script>

<div id="container_body">

	<div>
		<div id="form_container">
			<div id="form_body">
				<!-- content -->
				<form:form id="reportForm" modelAttribute="report" method="POST">
					<form:hidden id="report_xml" path="reportXml" />
					<form:hidden id="report_id" path="reportId" />
					<form:hidden id="report_data" path="reportData" />
					<form:hidden id="access_token" path="token" />
				</form:form>
				<div id="r-v-container">
					<table>
						<tr><td colspan="2"><div class="r-v-title" id="r-v-title">${report.reportName}</div></td></tr>
						<tr>
							<td>
								<c:if test="${not empty report.strStart && not empty report.strFinish}">
									<span class="r-v-bold"><spring:message code="report_start"/>: </span><span class="r-v-text">${report.strStart}</span>
									<span class="r-v-bold"><spring:message code="report_finish"/>: </span><span class="r-v-text">${report.strFinish}</span>
								</c:if>
							</td>
							<td width=" 8%">
								<div id="downloadReport" class="hidden">
									<img src="<c:url value="/images/download.png"/>" />
								</div>
							</td>
						</tr>					
					</table>					
					<div id="r-v-items"></div>
				</div>
								
				<!-- content -->	
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyDNAJRu-y2ge6WsSxUbmaOitQ1NNpxS1fo&libraries=geometry&sensor=true"></script>
<script src="<c:url value='/js/report/jquery.jqplot.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/report/jqplot.pieRenderer.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/report/jqplot.donutRenderer.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/report/jqplot.barRenderer.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/report/jqplot.categoryAxisRenderer.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/report/jqplot.pointLabels.min.js'/>" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery.jqplot.min.css"/>" />