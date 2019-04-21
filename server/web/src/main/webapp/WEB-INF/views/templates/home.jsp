<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<title><spring:message code="commons_title"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			
		<link rel="shortcut icon" type="image/x-icon" href="<c:url value="/favicon.ico"/>"/>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/reset.css"/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>" />
		
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery-ui-1.8.18.custom.css"/>" />
		<script src="<c:url value='/js/jquery-1.7.1.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/jquery-ui-1.8.18.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/json.min.js' />" type="text/javascript"></script>	
		<script src="<c:url value='/js/grid.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/default.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/base.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/jquery.blockUI.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/jquery.cookie.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/jquery.i18n.properties.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/jquery.maskedinput-1.3.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/jquery.ui.datepicker-pt-BR.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/jquery.atmosphere.js' />" type="text/javascript"></script>
		
		<script src="<c:url value='/js/jquery.qtip.js' />" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery.qtip.css"/>" />

		<script src="<c:url value='/js/sessionTimeout.js' />" type="text/javascript"></script>
		<script type="text/javascript">
			var SESSION_TIMEOUT = "${session_timeout}";
		</script>		
		
	</head>
	<body>
		<div id="header"><div class="container left"><tiles:insertAttribute name="header" /></div></div>		
		<div id="containerMessages"><tiles:insertAttribute name="messages" /></div>		
		<div id="content"><div class="container left"><div id="body"><tiles:insertAttribute name="content" /></div></div></div>
		<div id="footer"><div class="container left"><tiles:insertAttribute name="footer" /></div></div>
		<div id="banners"><div class="container left"><tiles:insertAttribute name="banners" /></div></div>
		
		<div id="dialog-recovery" style="display: none;"><p></p></div>
	</body>
</html>