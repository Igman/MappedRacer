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


function loginclickhandler(event)
{
        var views = document.getElementById('stackLayout');
		var front = document.getElementById('site-view');
		if (views && views.object && front) {
			views.object.setCurrentView(front, true);
		}
}


function logouthandler(event)
{
        var views = document.getElementById('stackLayout');
		var front = document.getElementById('login-view');
		if (views && views.object && front) {
			views.object.setCurrentView(front, true);
}}


function racebuttonhandler(event)
{
        var views = document.getElementById('application-layout');
		var front = document.getElementById('race-view');
		if (views && views.object && front) {
			views.object.setCurrentView(front, true);
		}
}


function sendcheckinhandler(event)
{
        var views = document.getElementById('application-layout');
		var front = document.getElementById('race-view');
		if (views && views.object && front) {
			views.object.setCurrentView(front, true);
		}
}


function checkinbuttonhandler(event)
{
        var views = document.getElementById('application-layout');
		var front = document.getElementById('checkin-view');
		if (views && views.object && front) {
			views.object.setCurrentView(front, true);
		}
}


function createracehandler(event)
{
        var views = document.getElementById('application-layout');
		var front = document.getElementById('home-view');
		if (views && views.object && front) {
			views.object.setCurrentView(front, true);
		}
}


function newracebuttonhandler(event)
{
        var views = document.getElementById('application-layout');
		var front = document.getElementById('home-view');
		if (views && views.object && front) {
			views.object.setCurrentView(front, true);
		}
}
