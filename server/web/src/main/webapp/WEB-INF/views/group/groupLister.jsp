<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="<c:url value='/js/listGroups.js'/>" type="text/javascript"></script>

<div id="container_menu"><jsp:include page="../common/mainMenu.jsp" flush="true"/></div>
<div id="container_body">
	<div>		
		<div class="form-header"><spring:message code="list_header"/></div>
		<div id="myGroups">
			<div class="loader_container"><img src="<c:url value="/images/ajax-loader.gif"/>" /></div>
		</div>
		<hr class="separator"/>
		<a class="form-button" href="<c:url value="/group.html"/>">Create a group</a><br />
	</div>
</div>