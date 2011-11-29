var newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687);
var ubc = new google.maps.LatLng(49.263261,-123.253899);
var currentLocation;
var browserSupportFlag =  new Boolean();
var marker;
var markerItem;
var map;
var contentString;
var infowindow;
var pic;
var msg;
var user;
var itemManager = new Manager(40);
var markerManager = new Manager(40);
var userManager = new Manager(5);
var positive = 'img/star.png';
var negative = 'img/bomb.png';
var goal = 'img/goal.png';
var markersArray = [];
var circlesArray = [];
var jsonObject = [];
var sizeOfRadius = 500;
var goalIcon = 1;
var positiveIcon = 2;
var negativeIcon = 3;
var zoomOfMap = 10;
		
function initialize() {
	var myOptions = {
    	zoom: zoomOfMap,
    	center: ubc,
    	mapTypeId: google.maps.MapTypeId.ROADMAP
	};
  
	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	
	fillMapWithMarkers();
}

function getGeoLocation(){
	//Try W3C Geolocation (Preferred)
	if(navigator.geolocation) {
	
		browserSupportFlag = true;																			//Browser supports geolocation
		navigator.geolocation.getCurrentPosition(function(position) {
		
			currentLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);	//Get coordinates of user
			//map.setCenter(currentLocation);																	//Center map to location
					
			addUserMarker(currentLocation);																		//Method to add a marker
								
	}, function() {
		handleNoGeolocation(browserSupportFlag);
	});
	//Browser doesn't support Geolocation
	} else {
		browserSupportFlag = false;
		handleNoGeolocation(browserSupportFlag);
	}
}

function handleNoGeolocation(errorFlag) {
	if (errorFlag == true) {
		alert("Geolocation service failed.");
		currentLocation = newyork;
	} else {
		alert("Your browser doesn't support geolocation. We've placed you in Siberia.");
		currentLocation = newyork;
	}
	map.setCenter(currentLocation);
}

function addUserMarker(location){											
	marker = new google.maps.Marker({										
		position: location,													
		map: map,																			
	});
	
	isItInsideRadius(location);
	
	setUserIcon(1);
	
	pic = document.getElementById("addPhoto").value;
	msg = document.getElementById("message").value;
	
	if(pic.length > 0 || msg.length > 0)
		addInfoWindow(pic,msg);
	
	var object = new Marker(location, '1', pic, msg);
	
	document.getElementById("addPhoto").value = "";
	document.getElementById("message").value = "";
	
	google.maps.event.addListener(marker, 'click', function() {
	  infowindow.open(map,marker);											//Evento to open infowindow while a marker is clicked
	});
}

function isItInsideRadius(location){
	var distance;
	
	for(var i=0; i<itemManager.getSize(); i++){
		var item = itemManager.getElementAt(i);
		distance = google.maps.geometry.spherical.computeDistanceBetween(location,item.location);
				
		if(distance < sizeOfRadius){
			deleteMarkerIcon(i);
		}
		
		if(distance < sizeOfRadius && item.type == 1)
			alert("finalDestinationChecked");
			//document.getElementById("distanceToDestination").innerHTML = 0 + "";
	}
	
	//document.getElementById("distanceToDestination").innerHTML = distance + "";
}

function addInfoWindow(pic, msg){
	contentString = '<div><img src="'+pic+'"/></br>'+msg+'</div>';									
			
	infowindow = new google.maps.InfoWindow({														
	    content: contentString
	});
}

function setUserIcon(id){
	if(id == 1)
		marker.setIcon('http://maps.google.com/mapfiles/ms/micons/blue-dot.png');
	else if(id == 3)
		marker.setIcon('http://maps.google.com/mapfiles/ms/micons/green-dot.png');
	else if(id == 4)
		marker.setIcon('http://maps.google.com/mapfiles/ms/micons/yellow-dot.png');
	else if(id == 5)
		marker.setIcon('http://maps.google.com/mapfiles/ms/micons/pink-dot.png');
}

function Manager(){
	this.markerArray = new Array(40);
	this.size = 0;
	
	this.getSize = getSize;
	this.getElementAt = getElementAt;
	this.addElement = addElement;
	this.removeElementAt = removeElementAt;
	this.resize = resize;
	
	function getSize(){
		return this.size;
	}
	
	function getElementAt(i){
		return this.markerArray[i];
	}
	
	function addElement(marker){
		if(this.getSize() == this.markerArray.length)
			this.resize()
		this.markerArray[this.size++] = marker;
	}
	
	function removeElementAt(position) {
		var theMarker = this.markerArray[position];
	    
	    for(var i=position; i<(this.getSize()-1); i++) {
			this.markerArray[i] = this.markerArray[i+1];
		}

		this.markerArray[getSize()-1] = null;
		this.size--;
		return theMarker;
	}
	
	function resize(){
		newArray = new Array(this.markerArray.length + 40);
		
		for(var i=0; i<this.markerArray.length; i++)
			newArray[i] = this.markerArray[i];
	}
}

function Marker(location, user, picture, message){
	this.location = location;
	this.user = user;
	this.picture = picture;
	this.message = message;
}

function Item(location, type, value){
	this.location = location;
	this.type = type;
	this.value = value;
}

function User(id, innerId){
	this.id = id;
	this.innerId = innerId;
}

function fillMapWithMarkers(){
	//Fill map with Markers and Items
	var position;
	var position2;
	var position3;
	var position4;
	var position5;
	var position6;
	var position7;

	position = new google.maps.LatLng(49.26303695241871, -123.23941507131957);
	var marker1 = new Item(position,'1','1');
	itemManager.addElement(marker1);
	position2 = new google.maps.LatLng(49.29887162686631, -123.1447008961083);
	var marker2 = new Item(position2,'2','1');
	itemManager.addElement(marker2);
	position3 = new google.maps.LatLng(49.240627056995336, -123.14744747813955);
	var marker3 = new Item(position3,'2','1');
	itemManager.addElement(marker3);
	position4 = new google.maps.LatLng(49.24286850433255, -123.02110470470205);
	var marker4 = new Item(position4,'2','1');
	itemManager.addElement(marker4);
	position5 = new google.maps.LatLng(49.24757521259346, -123.10524024755858);
	var marker5 = new Item(position5,'3','1');
	itemManager.addElement(marker5);
	position6 = new google.maps.LatLng(49.261244535147405, -123.1941393726708)
	var marker6 = new Item(position6,'3','1');
	itemManager.addElement(marker6);
	position7 = new google.maps.LatLng(49.289282139759524, -123.01006893810049);
	var marker7 = new Item(position7,'3','1');
	itemManager.addElement(marker7);

	updateMarkers();
}

function updateMarkers(){
	for(var i=0; i<itemManager.getSize(); i++){
		addIconMarker(itemManager.getElementAt(i).location, itemManager.getElementAt(i).type);
	}
}

function addIconMarker(location,type){
	markerItem = new google.maps.Marker({									
		position: location,													
		map: map															
	});
	markersArray.push(markerItem);
			
	setMarkerIcon(type);
	addRadius();
}

function setMarkerIcon(type){
	if(type == goalIcon)
		markerItem.setIcon(goal);
	else if(type == positiveIcon)
		markerItem.setIcon(positive);
	else if(type == negativeIcon)
		markerItem.setIcon(negative);
}

function addRadius(){
	circle = new google.maps.Circle({
	  map: map,
	  radius: sizeOfRadius,    								// 250mts
	  fillColor: '#00BFFF',
	  strokeWeight: '1px'
	});
	circle.bindTo('center', markerItem, 'position');
	
	circlesArray.push(circle);
}

function deleteMarkerIcon(marker){
	itemManager.getElementAt(marker).value = 0;
	itemManager.removeElementAt(marker);					//Don't know if this is useful
	removeAllMarkers();										//Or if this is useful
	updateMarkers();
}

function removeAllMarkers(){
	if (markersArray) {
		for (i in markersArray) {
	    	markersArray[i].setMap(null);
	    	circlesArray[i].setMap(null);
	    }
	    	markersArray.length = 0;
	}
}

function createJSON(){
	var items = [];
	var users  = [];
	var dateTime = document.getElementById("date").value + " " + document.getElementById("time").value;
	
	jsonObject.push({"name":document.getElementById("raceName_text")});
	for(var i=0; i<im.getSize(); i++){
		var x = im.getElementAt(i);
		if(x.type == 0)
			items.push({"location":x.location, "type":x.type, "value":'0'});
		else if(x.type == 1)
			items.push({"location":x.location, "type":x.type, "value":'250'});
		else if(x.type == 3)
			items.push({"location":x.location, "type":x.type, "value":'-250'});
	}
	jsonObject.push({"items":items});
	for(var i=0; i<userArray.length; i++){
		if(i==0)
			users.push({"username":'@ssalazars'});
		else
			users.push({"username":userArray[i]});
	}
	jsonObject.push({"racers":users});
	jsonObject.push({"dateTime":dateTime});
	
	send();
}

function send(){
	var request = new XMLHttpRequest();
	var newRace = JSON.stringify(jsonObject);
	var url = "CreateRaceController?";
	request.onreadystatechange = handleResponse;
	request.open("POST",url,true);
	request.send(newRace);
}

function handleResponse(){
	if((request.status == 200)&&(request.readyState == 4))
		alert(":D");
	else
		alert(request.status);
}

function reDirect(url){
	window.location = url;
}

function goback(){
	history.go(-1);
}