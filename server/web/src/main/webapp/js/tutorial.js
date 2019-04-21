var title = new Array();
var text  = new Array();
var img   = new Array();

function loadTutorialMsgs(){
	title[0] = jQuery.i18n.prop('tutorial_welcome_title');
	title[1] = jQuery.i18n.prop('tutorial_createform_title');
	title[2] = jQuery.i18n.prop('tutorial_editform_title');
	title[3] = jQuery.i18n.prop('tutorial_downloadapk_title');
	title[4] = jQuery.i18n.prop('tutorial_collect_title');
	title[5] = jQuery.i18n.prop('tutorial_answers_title');
	
	text[0]  = jQuery.i18n.prop('tutorial_welcome_text');
	text[1]  = jQuery.i18n.prop('tutorial_createform_text');
	text[2]  = jQuery.i18n.prop('tutorial_editform_text');
	text[3]  = jQuery.i18n.prop('tutorial_downloadapk_text');
	text[4]  = jQuery.i18n.prop('tutorial_collect_text');
	text[5]  = jQuery.i18n.prop('tutorial_answers_text');
			
	img[0]   = "tutorial_welcome.png";
	img[1]   = "tutorial_createform.png";
	img[2]   = "tutorial_editform.png";
	img[3]   = "tutorial_downloadapk.png";
	img[4]   = "tutorial_collect.png";
	img[5]   = "tutorial_answers.png";
}

function showTutorial(page){
	loadTutorialMsgs();
	
	if(typeof page == 'undefined'){
		page = 0;
	}
	
	if( page < 0 || page >= title.length ){
		return;
	}
		
	buildTutorialTable(page);
	
	closeDialogFunction = function() {
    	$( this ).dialog( "close" );
    }; 
    
    nextPageFunction = function() {
    	$( this ).dialog( "close" );
    	showTutorial(page + 1);
    };
    
    previousPageFunction = function() {
    	$( this ).dialog( "close" );
    	showTutorial(page - 1);
    };
    
    dismiss      = jQuery.i18n.prop('tutorial_nav_dismiss');
    startTour    = jQuery.i18n.prop('tutorial_nav_start');
    finishTour   = jQuery.i18n.prop('tutorial_nav_finish');
    nextPage     = jQuery.i18n.prop('tutorial_nav_next');
    previousPage = jQuery.i18n.prop('tutorial_nav_previous');
    
    buttonsRef = {};
	if(page==0){		
		buttonsRef[dismiss]      = closeDialogFunction;
		buttonsRef[startTour]    = nextPageFunction;
		
	} else if (page == title.length - 1){
		buttonsRef[previousPage] = previousPageFunction;
		buttonsRef[finishTour]   = closeDialogFunction;
		
	} else {
		buttonsRef[previousPage] = previousPageFunction;
		buttonsRef[nextPage]     = nextPageFunction;
	}
		
	$(function() {
	    $( "div#dialog-tutorial" ).dialog({
	      title: title[page],
	      resizable: false,
	      width: 800,
	      height:620,
	      modal: true,
	      buttons: buttonsRef 
	    });
	});
}

function buildTutorialTable(page){	
	var tutorialHtml =	"<table>" +
							"<tr><td><img src='images/" + img[page] + "'></td></tr>" +
							"<tr><td><p>" + text[page] + "</p></td></tr>" +							
						"</table>";	
	
	$( "div#dialog-tutorial" )[0].innerHTML = tutorialHtml;
}

function checkFirstLogin(){
	$(document).ready(function(){
       if(typeof firstLogin != 'undefined' && firstLogin == true){
               showTutorial();
       }
	});
}
