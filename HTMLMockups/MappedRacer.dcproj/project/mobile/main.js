/* 
 This file was generated by Dashcode.  
 You may edit this file to customize your widget or web page 
 according to the license.txt file included in the project.
 */

//
// Function: load()
// Called by HTML body element's onload event when the web application is ready to start
//
function load()
{
    dashcode.setupParts();
}


function loginbtnOnClickHandler(event)
{
        var views = document.getElementById('stackLayout');
		var front = document.getElementById('new-race-view');
		if (views && views.object && front) {
			views.object.setCurrentView(front, true);
		}
}


function createbtnOnClickHandler(event)
{
        var views = document.getElementById('stackLayout');
		var front = document.getElementById('main-view');
		if (views && views.object && front) {
			views.object.setCurrentView(front, true);
		}
}