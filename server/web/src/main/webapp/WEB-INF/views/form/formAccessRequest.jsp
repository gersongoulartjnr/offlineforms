<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="<c:url value='/js/requestList.js'/>" type="text/javascript"></script>

<div id="container_menu"><jsp:include page="../common/mainMenu.jsp" flush="true"/></div>
<div id="container_body">
	<div>
		<div class="form-header"><spring:message code="list_form_access_request"/></div>
			<div id="formAccRequest">
				<div class="loader_container"><img src="<c:url value="/images/ajax-loader.gif"/>" /></div>
			</div>
		<hr class="separator"/>
		<div class="wrapper-button">
			<div id="idAcceptAll" onclick="acceptAll();" class="form-button button-big side-left"><spring:message code="form_access_request_acceptall" /></div>
			<div id="idRejectAll" onclick="rejectAll();"  class="form-button button-big side-left"><spring:message code="form_access_request_rejectall" /></div>
		</div>
	</div>
</div>	