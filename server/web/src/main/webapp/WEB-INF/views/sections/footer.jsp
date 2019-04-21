<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="<c:url value='/js/tutorial.js'/>" type="text/javascript"></script>

<ul id="sub_footer">
	<li>
		<div>
			<span class="sub_footer_title"><spring:message code="project_name"/></span>
			<ul id="sub_footer_item">
				<li><a href="http://www.gnu.org/licenses/gpl-3.0.html" class="sub_footer_item"><spring:message code="project_license"/></a></li>
				<li><a href="<c:url value="/disclaimer.html"/>" class="sub_footer_item"><spring:message code="project_disclaimer"/></a></li>
				<li><a href="#" class="sub_footer_item" onclick="showTutorial();"><spring:message code="project_tutorial"/></a></li>
				<li><a href="<c:url value="/contact-us.html"/>" class="sub_footer_item"><spring:message code="project_contact_us"/></a></li>
			</ul>
		</div>
	</li>
	<li>
		<div>
			<span class="sub_footer_title"><spring:message code="project_development"/></span>
			<ul id="sub_footer_item">
				<li><a href="http://sourceforge.net/p/maritaca/code/ci/e510318fcb2423982069d1f4bca9f6e087f27e57/tree/" class="sub_footer_item" target="_blank">SourceCode</a></li>
				<li><a href="http://sourceforge.net/p/maritaca/wiki/Home/" class="sub_footer_item" target="_blank"><spring:message code="project_documentation"/></a></li>
				<li><a href="http://sourceforge.net/p/maritaca/bug/?source=navbar" class="sub_footer_item"><spring:message code="project_bug_report"/></a></li>				
				<li><a href="<c:url value="/developers.html"/>" class="sub_footer_item"><spring:message code="project_developers"/></a></li>
			</ul>
		</div>
	</li>		
	<li>
		<div>
			<span class="sub_footer_title"><spring:message code="project_extras"/></span>
			<ul id="sub_footer_item">
				<li><a href="http://www.youtube.com/watch?v=1HkHFEQKHaI" class="sub_footer_item" target="_blank">Youtube</a></li>
				<li id="facebook_footer">
					<div id="fb-root"></div>
					<script>(function(d, s, id) {
					  var js, fjs = d.getElementsByTagName(s)[0];
					  if (d.getElementById(id)) return;
					  js = d.createElement(s); js.id = id;
					  js.src = "//connect.facebook.net/en_GB/all.js#xfbml=1";
					  fjs.parentNode.insertBefore(js, fjs);
					}(document, 'script', 'facebook-jssdk'));</script>
					<div class="fb-like" data-show-faces="false" data-href="http://www.facebook.com/pages/Maritaca/551007908245937" data-send="false" data-width="220" data-show-faces="true"></div>
				</li>
			</ul>
			<span class="sub_footer_title"><spring:message code="languages"/></span>
			<div id="divFlagIdioms">		
				<a href="<c:url value="?lang=en"/>"><img src="<c:url value="/images/languages/en.png"/>" /></a>
				<a href="<c:url value="?lang=pt"/>"><img src="<c:url value="/images/languages/br.png"/>" /></a>
				<a href="<c:url value="?lang=es"/>"><img src="<c:url value="/images/languages/es.png"/>" /></a>
			</div>
		</div>
	</li>
</ul>

<div id="dialog-tutorial"></div>
