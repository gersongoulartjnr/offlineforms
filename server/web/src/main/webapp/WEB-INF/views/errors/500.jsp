<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
		<title><spring:message code="commons_title"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">		
		<link rel="shortcut icon" type="image/x-icon" href="<c:url value="/favicon.ico"/>"/>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/reset.css"/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>" />		
	</head>
	<body>
		<div id="header"><div class="container left"><jsp:include page="../sections/header.jsp" flush="true"/></div></div>
		<div id="content"><div class="container left"><div id="error">Internal server error</div></div></div>
		<div id="banners"><div class="container left"><jsp:include page="../sections/banners.jsp" flush="true"/></div></div>
	</body>
</html>