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
var checkinManager = new Manager(40);
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
var raceIdSent = false;
var request;
var markerToDelete = -1;
		
function initialize() {
	var myOptions = {
    	zoom: zoomOfMap,
    	center: ubc,
    	mapTypeId: google.maps.MapTypeId.ROADMAP
	};
  
	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	sendRaceId(9);
}

function getGeoLocation(){
	//Try W3C Geolocation (Preferred)
	if(navigator.geolocation) {
	
		browserSupportFlag = true;																			//Browser supports geolocation
		navigator.geolocation.getCurrentPosition(function(position) {
		
			currentLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);	//Get coordinates of user
			//map.setCenter(currentLocation);																	//Center map to location
					
			getUserPinInformation(currentLocation);																//Method to add a marker
								
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

function getUserPinInformation(location){
	var picture = document.getElementById("addPhoto").value;
	var message = document.getElementById("message").value;
	var userLocation = location;
	//var userid = 
	//var raceid =
	createJSON(picture,message,userLocation);
}

function updateUserMarkers(){
	for(var i=0; i<checkinManager.getSize(); i++){	
	
		var location = checkinManager.getElementAt(i).location;
		var userid = checkinManager.getElementAt(i).userid;
		var innerid;
											
		marker = new google.maps.Marker({										
			position: location,													
			map: map,																			
		});
		
		isItInsideRadius(location);
		
		for(var j=0; j<userManager.getSize(); j++){
			if(userid == userManager.getElementAt(j).id){
				innerid = userManager.getElementAt(j).innerId;
				break;
			}
		}
		setUserIcon(innerid); //TODO
		
		pic = checkinManager.getElementAt(i).pic;
		msg = checkinManager.getElementAt(i).msg;
		
		if(pic.length > 0 || msg.length > 0)
			addInfoWindow(pic,msg);
		
		//var object = new Marker(location, userid, pic, msg);
		
		//document.getElementById("addPhoto").value = "";
		//document.getElementById("message").value = "";
		
		google.maps.event.addListener(marker, 'click', function() {
		  infowindow.open(map,marker);											//Evento to open infowindow while a marker is clicked
		});
	}
}

function isItInsideRadius(location){
	var distance;
	
	for(var i=0; i<itemManager.getSize(); i++){
		var item = itemManager.getElementAt(i);
		distance = google.maps.geometry.spherical.computeDistanceBetween(location,item.location);

		if(distance < sizeOfRadius){
			deleteMarkerIcon(itemManager.getElementAt(i));
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

function Item(id,location, type, value){
	this.id = id;
	this.location = location;
	this.type = type;
	this.value = value;
}

function User(id, username, score, innerId){
	this.id = id;
	this.username = username;
	this.score = score;
	this.innerId = innerId;
}

function Checkin(location, msg, pic, userid){
	this.location = location;
	this.msg = msg;
	this.pic = pic;
	this.userid = userid;
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
	markerToDelete = marker.id;
	alert("item id" +marker.id);
	//marker.value = 0;
	//itemManager.removeElementAt(marker);						//Don't know if this is useful
	//removeAllMarkers();										//Or if this is useful
	//updateMarkers();
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

function createJSON(pic,msg,location){
	updateUserMarkers();
	var jsonString = '{"location": "'+location+'", "comment": "'+msg+'", "picture": "'+pic+'", "userId": "1", "raceId": "9", "markerToDelete": "'+markerToDelete+'", "postTweet": "'+document.getElementById("postTwitter").checked+'"}';
	send(jsonString);
}

function send(jsonString){
	request = new XMLHttpRequest();
	var checkin = jsonString;
	var url = "CheckInController";
	request.onreadystatechange = handleResponse;
	request.open("POST",url,true);
	request.send(checkin);
}

function handleResponse(){
	if((request.status == 200)&&(request.readyState == 4)){
		if(raceIdSent){
			raceIdSent = false;+
			alert();
			var jsonString = request.responseText;
			receiveJSON(jsonString);
		}else{
			reDirect("race.html");
		}
	}
	//else
	//	alert(request.status);
}

function sendRaceId(id){
	raceIdSent = true;
	request = new XMLHttpRequest();
	var raceID = id;
	var url = "RaceController?race_id="+raceID;
	request.onreadystatechange = handleResponse;
	request.open("GET",url,true);
	request.send(null);
}

function receiveJSON(jsonString){
	var jsonObject = eval('('+jsonString+')');
	var locationString;
	var lat;
	var lng;
	var position;
	
	for(var i=0; i<jsonObject.items.length; i++){
		
		locationString = jsonObject.items[i].location;
		lat = locationString.substring(1,locationString.indexOf(","));
		lng = locationString.substring(locationString.indexOf(",")+2,locationString.length-1);
		lat = parseFloat(lat);
		lng = parseFloat(lng);
		position = new google.maps.LatLng(lat,lng);
		
		var itemObject = new Item(jsonObject.items[i].id,position,jsonObject.items[i].type,jsonObject.items[i].value);
		itemManager.addElement(itemObject);
	}
	for(var i=0; i<jsonObject.racers.length; i++){
		var userObject = new User(jsonObject.racers[i].userID,jsonObject.racers[i].username,jsonObject.racers[i].score, i+1);
		userManager.addElement(userObject);
	}
	alert("Checkin Length:"+jsonObject.checkin.length);
	for(var i=0; i<jsonObject.checkin.length; i++){
		locationString = jsonObject.checkin[i].location;
		lat = locationString.substring(1,locationString.indexOf(","));
		lng = locationString.substring(locationString.indexOf(",")+2,locationString.length-1);
		lat = parseFloat(lat);
		lng = parseFloat(lng);
		position = new google.maps.LatLng(lat,lng);
		
		var checkinObject = new Checkin(position,jsonObject.checkin[i].comment,jsonObject.checkin[i].picture,jsonObject.checkin[i].userID);
		checkinManager.addElement(checkinObject);
	}
	
	updateMarkers();
	updateUserMarkers();
	
	if(getURL() > 0){
		alert();
		updatePositions();
	}
}

function updatePositions(){
	var positionsDiv = document.getElementById("racersPosition");
	var squareDiv = document.getElementsByTagName("div");
	var sizePerUser = 60;
	var numberOfUsers = 5;
	alert(squareDiv.length);
	for(var i=0; i<squareDiv.length; i++){
		if(squareDiv[i].id == "roundSquare_position")
			squareDiv[i].style.height = sizePerUser * numberOfUsers + 'px';
	}
	
	for(var i=0; i<userManager.getSize(); i++)
		positionsDiv.innerHTML += userManager.getElementAt(i).username + "          " + userManager.getElementAt(i).score + "<br>";
}

function getURL(){
	var url = window.location + "";
	return(url.indexOf("race.html"));
}