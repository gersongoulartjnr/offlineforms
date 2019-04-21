<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script src="<c:url value='/js/tytabs.jquery.min.js' />" type="text/javascript"></script>
<script src="<c:url value='/js/importForm.js' />" type="text/javascript"></script>

<div id="tabsholder">
	<ul class="tabs">
    	<li id="tab1"><spring:message code="form_import_fileUploadTab"/></li>
        <li id="tab2"><spring:message code="form_import_urlTab"/></li>
	</ul>
	<div class="form-body">
		<div id="content1" class="tabscontent">
	    	<div id="byXMLFile">
				<c:url var="url" value="/import.html" />
				<form:form id="importForm_File" modelAttribute="importForm" method="POST" action="${url}" enctype="multipart/form-data">										
					<form:hidden id="fileName" path="fileName" />
					<form:hidden id="fileContent" path="fileContent" />
					<div class="wrapper-row">
						<div class="wrapper-label"><label for="password"><spring:message code="fld_xml_file"/>:</label></div>
						<div class="wrapper-widget">
							<form:input id="xmlFile" type="file" path="url" />
						</div>
					</div>
					<div class="wrapper-button">
						<div id="importXmlFile" class="form-button button-normal"><spring:message code="commons_save" /></div>
					</div>					
				</form:form>
			</div>     	
		</div>
		<div id="content2" class="tabscontent">
	    	<div id="byXMLURL">
				<c:url var="url" value="/import.html" />
				<form:form id="importForm_Url" modelAttribute="importForm" method="POST" action="${url}">
					<div class="wrapper-row">			
						<div class="wrapper-label"><label for="password"><spring:message code="fld_xml_url"/>:</label></div>
						<div class="wrapper-widget">
							<form:input id="xmlUrl" type="text" path="url" cssClass="input-large" />
						</div>
					</div>
					<div class="wrapper-button">
						<div id="importFromUrl" class="form-button button-normal"><spring:message code="commons_save" /></div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>