/*********************************************************************
 * This Javascript class manages everything relevant from a race	 *
 * held by different users. It loads the information stored in the DB*
 * from the race, such as the final destination, positive and 		 *
 * negative items.It also manages the check-in function, which 		 *
 * locates a user in its current position, and stores this info		 *
 * in the DB in order to be displayed in the map for other users.	 *
 *********************************************************************/

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
var raceID;
var numberOfUsers = 0;
var itIsRace = false;
var liveTweets = false;
var tweetManager = new Manager(40);

/*********************************************************************
 * Initial method called when createRace.html is loaded. The 		 *
 * functionality of this method is to set the basic options Google 	 *
 * Maps uses, such as the zoom and the way it is presented (ROADMAP) *
 * as well as the zoom. It also identifies in which page the user is *
 * in, in order to perform specific functions. It sends to the server*
 * the race ID, for it to return all the information needed to 		 *
 * display the information											 *
 *********************************************************************/		
function initialize() {
	var myOptions = {
    	zoom: zoomOfMap,
    	center: ubc,
    	mapTypeId: google.maps.MapTypeId.ROADMAP
	};
  
	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	
	var url = document.location.href;
	var temp = url.substring(url.indexOf('?')+8, url.length);
	raceID = parseInt(temp);
	
	sendRaceId(raceID);
}

/*********************************************************************
 * Method from Google Maps API, that allow if the browser allows it  *
 * to track a user's geolocation. It a user's geolocation is found 	 *
 * getUsersPinInformation() method is called						 *
 *********************************************************************/
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

/*********************************************************************
 * Method for handling an error in geolocation. New York is displayed*
 * in the map.														 *
 *********************************************************************/
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

/*********************************************************************
 * Text entered in the check-in text boxes is retrieved in order to	 *
 * create a JSON object and send that information to the server		 *
 *********************************************************************/
function getUserPinInformation(location){
	var picture = document.getElementById("addPhoto").value;
	var message = document.getElementById("message").value;
	var userLocation = location;
	//var userid = 
	//var raceid =
	createJSON(picture,message,userLocation);
}

/*********************************************************************
 * This method takes the information the server sent and display all *
 * the information from the items, and user's checkins' in the map.	 *
 *********************************************************************/
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
		//alert("MARKER TO BE DELETED: "+markerToDelete);
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

/*********************************************************************
 * Method to check if a user check-in inside a determined radius	 *
 *********************************************************************/
function isItInsideRadius(location){
	var distance;
	
	for(var i=0; i<itemManager.getSize(); i++){
		var item = itemManager.getElementAt(i);
		distance = google.maps.geometry.spherical.computeDistanceBetween(location,item.location);
		
		
		if(itemManager.getElementAt(i).type == 1 && itIsRace)
			document.getElementById("distanceToDestination").innerHTML = "Distance to destination: "+parseInt(distance)+"m";
		
		if(distance < sizeOfRadius){
//			if(itemManager.getElementAt(i).type == 1){
//				alert("Congratulations! You have won the race!");
//			}
			
			deleteMarkerIcon(itemManager.getElementAt(i));
		}				
		
		//if(distance < sizeOfRadius && item.type == 1)
		//	alert("finalDestinationChecked");
			//document.getElementById("distanceToDestination").innerHTML = 0 + "";
	}
	
	//document.getElementById("distanceToDestination").innerHTML = distance + "";
}

/*********************************************************************
 * If a user check-in with a pic url or a message, the dropped pin 	 *
 * will contain an info window, which will display the information	 *
 * the user entered in.												 *
 *********************************************************************/
function addInfoWindow(pic, msg){
	contentString = '<div><img src="'+pic+'"/></br>'+msg+'</div>';									
			
	infowindow = new google.maps.InfoWindow({														
	    content: contentString
	});
}

/*********************************************************************
 * A different color is assigned to each racer in the race			 *
 *********************************************************************/
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

/*********************************************************************
 * This is a class created in order to manage every other class into *
 * a dynamic array. It contains basic methods such as getting the 	 *
 * size of the array, adding and getting an element, and finally 	 *
 * resizing the array.												 *
 *********************************************************************/
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

/*********************************************************************
 * Marker class. It contains the variables necessary to get values	 *
 * from the marker dropped and store them in the DB 				 *
 *********************************************************************/
function Marker(location, user, picture, message){
	this.location = location;
	this.user = user;
	this.picture = picture;
	this.message = message;
}

/*********************************************************************
 * Item class. It contains the variables necessary to get values	 *
 * from the items (final destination, positive and negative) and 	 *
 * display them on the map											 *
 *********************************************************************/
function Item(id,location, type, value){
	this.id = id;
	this.location = location;
	this.type = type;
	this.value = value;
}

/*********************************************************************
 * User class. 														 *
 *********************************************************************/
function User(id, username, score, innerId){
	this.id = id;
	this.username = username;
	this.score = score;
	this.innerId = innerId;
}

/*********************************************************************
 * Checkin class. It contains the variables necessary to get values	 *
 * from the user checkin-in pin dropped and store them in the DB 	 *
 *********************************************************************/
function Checkin(location, msg, pic, userid){
	this.location = location;
	this.msg = msg;
	this.pic = pic;
	this.userid = userid;
}

/*********************************************************************
 * updateMarkers calls iteratively all the items stored in the 		 *
 * itemManager array and adds them to the map					 	 *
 *********************************************************************/
function updateMarkers(){
	for(var i=0; i<itemManager.getSize(); i++){
		addIconMarker(itemManager.getElementAt(i).location, itemManager.getElementAt(i).type);
	}
}

/*********************************************************************
 * AddIconMarker creates and item object and displays it on the map	 *
 *********************************************************************/
function addIconMarker(location,type){
	markerItem = new google.maps.Marker({									
		position: location,													
		map: map															
	});
	markersArray.push(markerItem);
			
	setMarkerIcon(type);
	addRadius();
}

/*********************************************************************
 * SetIcon checks which type of marker was set and based on this info*
 * adds the image to the marker that is dropped on the map.			 *
 *********************************************************************/
function setMarkerIcon(type){
	if(type == goalIcon)
		markerItem.setIcon(goal);
	else if(type == positiveIcon)
		markerItem.setIcon(positive);
	else if(type == negativeIcon)
		markerItem.setIcon(negative);
}

/*********************************************************************
 * AddRadius creates a circle object inside the Google Map. This 	 *
 * object contains attributes such as adding the radius of it. 250mt *
 * is the radius decided to allow users to check-in inside a marker  *
 * for the system to recognize them.								 *
 *********************************************************************/
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

/*********************************************************************
 * This method sets to delete the marker where the user has 		 *
 * checked-in on.													 *
 *********************************************************************/
function deleteMarkerIcon(marker){
	markerToDelete = marker.id;
	//marker.value = 0;
	//itemManager.removeElementAt(marker);						//Don't know if this is useful
	//removeAllMarkers();										//Or if this is useful
	//updateMarkers();
}

/*function removeAllMarkers(){
	if (markersArray) {
		for (i in markersArray) {
	    	markersArray[i].setMap(null);
	    	circlesArray[i].setMap(null);
	    }
	    	markersArray.length = 0;
	}
}*/

/*********************************************************************
 * createJSON() is the method in charge of in formatting all the	 *
 * information gotten and send it to the server as a JSON object.	 *
 *********************************************************************/
function createJSON(pic,msg,location){
	//updateUserMarkers();
	var jsonString = '{"location": "'+location+'", "comment": "'+msg+'", "picture": "'+pic+'", "raceId": "'+raceID+'", "markerToDelete": "'+markerToDelete+'", "postTweet": "'+document.getElementById("postTwitter").checked+'"}';
	send(jsonString);
}

/*********************************************************************
 * Method that sends the JSON object to server. Creates an 			 *
 * XMLHttpRequest, sends it to the server as POST, and handles the 	 *
 * response from the server.										 *
 *********************************************************************/
function send(jsonString){
	request = new XMLHttpRequest();
	var checkin = jsonString;
	var url = "CheckInController";
	request.onreadystatechange = handleResponse;
	request.open("POST",url,true);
	request.send(checkin);
}

/*********************************************************************
 * Method for handling the server's response. It can be if we are 	 *
 * filling up the map or removing an item from the map				 *
 *********************************************************************/
function handleResponse(){
	if((request.status == 200)&&(request.readyState == 4)){
		if(raceIdSent){
			raceIdSent = false;
			var jsonString = request.responseText;
			receiveJSON(jsonString);
		}else{
			//markerToDelete = -1;
			reDirect("race.html?raceId="+raceID.toString());
		}
	}
	//else
	//	alert(request.status);
}

/*********************************************************************
 * Method that sends the raceId to server. Creates an 			 	 *
 * XMLHttpRequest, sends it to the server as GET, and handles the 	 *
 * response from the server.										 *
 *********************************************************************/
function sendRaceId(id){
	raceIdSent = true;
	request = new XMLHttpRequest();
	var raceID = id;
	var url = "RaceController?race_id="+raceID;
	request.onreadystatechange = handleResponse;
	request.open("GET",url,true);
	request.send(null);
}

/*********************************************************************
 * This method receives the JSON object sent by the server and		 *
 * manipulates the information, in this case, setting into different *
 * arrays the information gotten from the server to display on the	 *
 * map																 *
 *********************************************************************/
function receiveJSON(jsonString){
	var jsonObject = eval('('+jsonString+')');
	var locationString;
	var lat;
	var lng;
	var position;
	//var isThereAFinalDestination = false;
	
	for(var i=0; i<jsonObject.items.length; i++){
		
		locationString = jsonObject.items[i].location;
		lat = locationString.substring(1,locationString.indexOf(","));
		lng = locationString.substring(locationString.indexOf(",")+2,locationString.length-1);
		lat = parseFloat(lat);
		lng = parseFloat(lng);
		position = new google.maps.LatLng(lat,lng);
		
		var itemObject = new Item(jsonObject.items[i].id,position,jsonObject.items[i].type,jsonObject.items[i].value);
		itemManager.addElement(itemObject);
		
		if(jsonObject.items[i].type == 1)
			isThereAFinalDestination = true;
	}

//	if(!isThereAFinalDestination){
//		alert("The race has ended");
//		window.location = "usr_home.html";
//	}
	for(var i=0; i<jsonObject.racers.length; i++){
		var userObject = new User(jsonObject.racers[i].userID,jsonObject.racers[i].username,jsonObject.racers[i].score, i+1);
		userManager.addElement(userObject);
		numberOfUsers++;
	}
	
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
	
	if(getURL() > 0){
		itIsRace = true;
		updatePositions();
	}
	
	updateUserMarkers();
}

/*********************************************************************
 * This method updates the position depending on their points on the *
 * race on a div in the race.html page								 *
 *********************************************************************/
function updatePositions(){
	var positionsDiv = document.getElementById("racersPosition");
	var squareDiv = document.getElementsByTagName("div");
	var sizePerUser = 32;
	for(var i=0; i<squareDiv.length; i++){
		if(squareDiv[i].id == "roundSquare_position")
			squareDiv[i].style.height = sizePerUser * numberOfUsers + 'px';
	}
	
	for(var i=0; i<userManager.getSize(); i++)
		positionsDiv.innerHTML += userManager.getElementAt(i).username + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + userManager.getElementAt(i).score + "<br>";

	//getLiveTweets();
}

/*********************************************************************
 * Tells if the url is "race.html"									 *
 *********************************************************************/
function getURL(){
	var url = window.location + "";
	//alert("URL: "+url+" INDEX OF: "+url.indexOf("race.html",0));
	return(url.indexOf("race.html",0));
}

/*********************************************************************
 * When the check-in button is clicked, it calls the checkin.html	 *
 * with the race id of that specific race							 *
 *********************************************************************/
function checkinClicked(){
	window.location = "checkin.html?raceId="+raceID.toString();
}

//function getLiveTweets(){
//	liveTweets = true;
//	request = new XMLHttpRequest();
//	var url = "searchTweets?raceId="+raceID;
//	
//	request.onreadystatechange = handleResponseTweets;
//	request.open("GET",url,true);
//	
//	request.send(null);
//}
//
//function handleResponseTweets(){
//	if((request.status == 200)&&(request.readyState == 4)){
//		
//		var jsonString = request.responseText;
//		displayLiveTweets(jsonString);
//	}
//	
//}
//
//function displayLiveTweets(jsonString){
//	
//	var jsonObject = JSON.stringify(jsonString);
//	var jo = eval(jsonObject);
//	
//	for(var i=0; i<jsonObject.QueryResultJSONImpl.length; i++){
//
//		for(var j=0; j<jsonObject.tweets[i].TweetJSONImpl.length; j++){
//
//			var tweet = jsonObject.tweets[i].TweetJSONImpl[j].text;
//			var user = jsonObject.tweets[i].TweetJSONImpl[j].fromUser;
//		}		
//		var tweetObject = new Tweet(tweet, user);
//		tweetManager.addElement(tweetObject);
//	}
//	
//	showTweets();
//}
//
//function Tweet(tweet,user){
//	this.tweet = tweet;
//	this.user = user;
//}
//function showTweets(){
//	var temp = document.getElementById("liveTweets");
//	
//	for(var i=0; i<tweetManager.getSize(); i++)
//		temp.innerHTML += tweetManager.getElementAt(i).user + "<br>" + tweetManager.getElementAt(i).tweet;
//}
//
function sendTweet(){
	alert("Your tweet has been successfully sent")
	request = new XMLHttpRequest();
	var msg = document.getElementById("tweet").value;
	var url = "tweet?text="+msg;
	request.onreadystatechange = handleResponse;
	request.open("GET",url,true);
	request.send(null);	
}