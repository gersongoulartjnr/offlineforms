<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/h5style.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery.tagsinput.css"/>" />
<script src="<c:url value='/js/jquery.tagsinput.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/formCommons.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/formEditor.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/h5Script.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/h5FieldClasses.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/h5FieldProperties.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/h5FormClass.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/si.files.js'/>" type="text/javascript"></script>

<script type="text/javascript">
    SI.Files.stylizeAll();
    var buildApkMessage = "${building_apk}";
</script>

<div id="form_container">
	<div class="formEditorHeader">
		<c:url var="url" value="/form.html" />
		<form:form id="account" modelAttribute="form" method="POST" action="${url}" enctype="multipart/form-data">			

			<div id="containerFormHeader">
				<div id="menuFormEditor">
					<ul id="ul-form-submenu">
						<li><div id="newForm"    class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/new.png"/>" /> <spring:message code="commons_new"/></div></li>
						<li><div id="importForm" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/import.png"/>" /> <spring:message code="commons_import"/></div></li>
						<c:if test="${empty form.url}">
							<li><div id="saveForm"   class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/save.png"/>" /> <spring:message code="commons_save"/></div></li>
							<li><div id="clearForm" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/clear.png"/>" /> <spring:message code="commons_clear"/></div></li>
						</c:if>
						<c:if test="${not empty form.url && not empty form.formPermission}">
							<c:if test="${form.formPermission.update}">
								<li><div id="saveForm"   class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/save.png"/>" /> <spring:message code="commons_save"/></div></li>
							</c:if>					
							<c:if test="${form.formPermission.read}">
								<li><div id="saveAsForm" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/save.png"/>" /> <spring:message code="commons_saveas"/></div></li>
								<li><div id="downloadXML" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/save.png"/>" /> <spring:message code="form_download_xml"/></div></li>						
								<li>
									<div id="downloadApp" class="button-submenu">
										<div id="downloadAppBuilded">
											<img class="img-form-submenu" src="<c:url value="/images/form/app-mobile.png"/>" /> 
											<spring:message code="form_download_mobile_app"/>
										</div>
										<div id="downloadAppBuilding" style="display: none;">
											<img class="img-form-submenu" src="<c:url value="/images/form/ajax-loader.gif"/>" /> 
											<spring:message code="form_building_mobile_app"/>
										</div>	
									</div>
								</li>
								<li><div id="clearForm" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/form/clear.png"/>" /> <spring:message code="commons_clear"/></div></li>
							</c:if>
							<c:if test="${form.formPermission.remove}">
								<li>
									<div id="removeForm" class="button-submenu"><img class="img-form-submenu" src="<c:url value="/images/remove.png"/>" /> <spring:message code="commons_delete"/></div>
								</li>
							</c:if>
						</c:if>
					</ul>
				</div>
	
				<div id="detailsFormEditor">
					<div class="side-left form_header_1 margin_3">
						<div class="wrapper-row">
							<div class="wrapper-label"><label for="form_title"><spring:message code="form_title"/>:</label></div>
							<div class="wrapper-widget">
								<form:input id="form_title" path="title" maxlength="50" onkeyup="updateFormTitle()" />
							</div>
							<div><form:errors path="title" cssClass="error" /></div>
						</div>
						<div class="wrapper-row">
							<div class="wrapper-label"><label for="form_description"><spring:message code="form_description"/>:</label></div>
							<div class="wrapper-widget">
								<form:textarea id="form_description" path="description" rows="1" cols="60" />
							</div>
							<div><form:errors path="description" cssClass="error" /></div>
						</div>
						<div class="wrapper-row">
							<div class="wrapper-label"><label for="tags"><spring:message code="form_tags"/>:</label></div>
							<div class="wrapper-widget">
								<form:input name="tags" id="tags" path="tags" />
							</div>
						</div>
					</div>
					<div class="side-left margin_3 form_icon_div">
						<c:if test="${not empty form.image}"><div>
							<output id="icon_container"><img class="form_icon" src="${form.image}" /></output>							
							<label for="form_icon" class="upload_picture">
								<form:input id="form_icon" type="file" path="iconFile" class="file" />
							</label>
							<div class="form_icon_choose">
								<spring:message code="form_icon_choose"/>
							</div>
						</div></c:if>
						<c:if test="${empty form.image}"><div>
							<output id="icon_container"><img class="form_icon" src="<c:url value="/images/project_logo.png"/>" /></output>							
							<label for="form_icon" class="upload_picture">
								<form:input id="form_icon" type="file" path="iconFile" class="file" />
							</label>
							<div class="form_icon_choose">
								<spring:message code="form_icon_choose"/>
							</div>
						</div></c:if>
						<div><form:errors path="iconFile" cssClass="error" /></div>
					</div>
					<div class="side-right margin_3">
						<c:if test="${not empty form.url}">
							<div id="votes">
								<img id="imgLike" src="<c:url value="/images/form/like.gif"/>" onclick="doLike();"><span id="mLike">${form.numLikes}</span>
								<img id="imgDislike" src="<c:url value="/images/form/dislike.gif"/>" onclick="doDislike();"><span id="mDislike">${form.numDislikes}</span>
								<script type="text/javascript">
									loadVotes("${form.like}", "${form.dislike}", "${form.numLikes}", "${form.numDislikes}");
								</script>
							</div>
						</c:if>
					</div>
				</div>
			</div>	
			
			<form:hidden id="form_url" path="url" />
			<form:hidden id="form_key" path="key" />
			<form:hidden id="form_xml" path="xml" />			
		</form:form>
	</div>
	<div class="formEditorContent">	
		<div id="divComponents" class="divComponents">
			<fieldset id="components">
			<legend id="ldg_components"><spring:message code="form_edit_availableComponents"/></legend>				
				<div class="subsection">
					<h4 class="subtitle"><spring:message code="form_general_comp"/></h4>
					<ol>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/textbox.png"/>" alt="Textbox" id="text"
									width="85px" height="23px" title="Textbox" onclick="addElement('text');" /></a></li>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/combobox.png"/>" alt="Combobox" id="combobox"
									width="85px" height="23px" title="Combobox" onclick="addElement('combobox');" /></a></li>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/date.png"/>" alt="Date" id="date"
								width="85px" height="23px" title="Date" onclick="addElement('date');" /></a></li>
					<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/radiobox.png"/>" alt="Radiobox" id="radio"
								width="85px" height="23px" title="Radiobox" onclick="addElement('radio');" /></a></li>
					<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/checkbox.png"/>" alt="Checkbox" id="checkbox"
								width="85px" height="23px" title="Checkbox" onclick="addElement('checkbox');" /></a></li>			
					</ol>
				</div>
				<div class="subsection">
					<h4 class="subtitle"><spring:message code="form_numeric_comp"/></h4>
					<ol>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/number.png"/>" alt="Number" id="number"
								width="85px" height="23px" title="Number" onclick="addElement('number');" /></a></li>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/decimal.png"/>" alt="Decimal" id="decimal"
								width="85px" height="23px" title="Decimal" onclick="addElement('decimal');" /></a></li>								
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/money.png"/>" alt="Money" id="money"
								width="85px" height="23px" title="Money" onclick="addElement('money');" /></a></li>
					</ol>
				</div>
				<div class="subsection">
					<h4 class="subtitle"><spring:message code="form_multimedia_comp"/></h4>
					<ol>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/picture.png"/>" alt="Picture" id="picture"
								width="85px" height="23px" title="Picture" onclick="addElement('picture');" /></a></li>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/audio.png"/>" alt="Audio" id="audio"
									width="85px" height="23px" title="Audio" onclick="addElement('audio');" /></a></li>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/video.png"/>" alt="Video" id="video"
									width="85px" height="23px" title="Video" onclick="addElement('video');" /></a></li>						
					</ol>
				</div>
				<div class="subsection">
					<h4 class="subtitle"><spring:message code="form_others_comp"/></h4>
					<ol>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/gps.png"/>" alt="Geolocation" id="geolocation"
								width="85px" height="23px" title="Geolocation" onclick="addElement('geolocation');" /></a></li>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/barcode.png"/>" alt="BarCode" id="barcode"
									width="85px" height="23px" title="BarCode" onclick="addElement('barcode');" /></a></li>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/slider.png"/>" alt="Slider" id="slider"
									width="85px" height="23px" title="BarCode" onclick="addElement('slider');" /></a></li>
						<li><a class="component" href="#" onmousemove="showDropBox();"><img class="img_component" src="<c:url value="/images/form/draw.png"/>" alt="Draw" id="draw"
									width="85px" height="23px" title="Draw" onclick="addElement('draw');" /></a></li>
					</ol>
				</div>
			</fieldset>
		</div>
		<div class="divXmlForm">
			<fieldset id="xmlForm">
			<legend style="width: 300px;"></legend>					
			</fieldset>
		</div>
		<div class="divProperties">	
			<fieldset id="properties">
			<legend><spring:message code="form_edit_fieldProperties"/></legend>
			</fieldset>
		</div>			
		<!-- content -->
	</div>
</div>

<div id="dialog-confirm" style="display: none;">
	<p>
		<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>
		<span id="messageDialogConfirm"></span>
	</p>
</div>

<div id="dialog-import" style="display: none;"></div>
<div id="dialog-save-as" style="display: none;"></div>
<div id="dialog-app" style="display: none;"></div>
<div id="dialog-remove" style="display: none;"></div>