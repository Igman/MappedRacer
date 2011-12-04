/*********************************************************************
 * This is the general.js Javascript class. The only purpose of this *
 * class is to use the following 2 methods in all classes. reDirect()*
 * redirects the current page to the desired one, and goback() simply*
 * goes back to the past page the user had open						 *
 *********************************************************************/
function reDirect(url){
	window.location=url;
}

function goback(){
	history.go(-1);
}