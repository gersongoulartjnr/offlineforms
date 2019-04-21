<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<header><spring:message code="account_create"/></header>

<div id="container">
	<div id="wraper">
		<br />
		<c:choose>		
			<c:when test="${empty userRegistered}">				
				<c:url var="url" value="/mobile-create-account.html" />
				<jsp:include page="../account/baseAccountEditor.jsp" flush="true" />			
			</c:when>
			<c:otherwise>
				<div class="info-box">
					${userRegistered}
					<br />
					<br />
					<a href="<c:url value="/${mobLoginUrl}"/>"><spring:message code="login"/></a>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<footer>2013</footer>