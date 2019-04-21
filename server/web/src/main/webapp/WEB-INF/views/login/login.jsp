<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	
<p class="project-slogan_main"><spring:message code="project_slogan_main"/></p><br />
<p class="project-slogan_small"><spring:message code="project_slogan_small"/></p><br />
<div class="project_video">
	<iframe id="video_maritaca" width="480" height="250"  src="http://www.youtube.com/embed/dLFAxnKWBlE" class="iframe_video"></iframe>
	<br /><br />
</div>

<div id="login_public_forms">
	<table>
		<tr>
			<td width="85%"><span class="project-slogan_small"><spring:message code="public_forms_message"/></span></td>
			<td align="right"><span><input type="text" id="searchPublicForms" class="search search_rounded" placeholder="Search..."></span></td>
		</tr>
	</table>
	<div id="public_forms"></div>
</div>

<div id="dialog-qr-code" style="display: none;"></div>