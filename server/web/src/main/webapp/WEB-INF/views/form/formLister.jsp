<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="<c:url value='/js/formCommons.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/listForms.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/automaticRefresh.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/tutorial.js'/>" type="text/javascript"></script>

<div id="container_menu"><jsp:include page="../common/mainMenu.jsp" flush="true"/></div>
<div id="container_body">
	<div>
		<div class="form-header"><spring:message code="form_list_my_forms"/>
			<div id="total_requests" class="side-right"></div>
			<div id="search_my_forms" class="side-right"></div>
		</div>
		<div id="myForms">
			<div class="loader_container"><img src="<c:url value="/images/ajax-loader.gif"/>" /></div>
		</div>
		<br />
		<div class="form-header"><spring:message code="form_list_shared_forms"/>
			<div id="search_shared_forms" class="side-right"></div>
		</div>
		<div id="sharedForms">
			<div class="loader_container"><img src="<c:url value="/images/ajax-loader.gif"/>" /></div>
		</div>
		<hr class="separator"/>
	</div>
</div>

<div id="dialog-confirm" style="display: none;">
	<p>
		<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>
		<span id="messageDialogConfirm"></span>
	</p>
</div>
<div id="dialog-qr-code" style="display: none;"></div>

<c:if test="${empty currentUser.loggedBefore}">
	<c:set target="${currentUser}" property="loggedBefore" value="1"/>
	<script>
		firstLogin = true;
		checkFirstLogin();
	</script>
</c:if>