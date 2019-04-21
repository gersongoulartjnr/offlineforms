<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script src="<c:url value='/js/listAnswers.js'/>" type="text/javascript"></script>

<div id="container_menu"><jsp:include page="../common/mainMenu.jsp" flush="true"/></div>
<div id="container_body">
	<div>		
		<div id="listAnswers" class="form-header"><spring:message code="msg_answers"/></div>
		<div id="form_body">				
			<div id="answersByForm">
				<div class="loader_container"><img src="<c:url value="/images/ajax-loader.gif"/>" /></div>
			</div>
			<hr class="separator"/>			
			<div id="dialogPictureAnswer"></div>				
			<div id="dialog-answer"></div>			
		</div>
	</div>
</div>