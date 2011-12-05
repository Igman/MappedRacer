/*********************************************************************
 *	This Javascript class creates a new race by retrieving from the  *
 *	text boxes the required information. It also allow the user drop *
 *	pins (Final destination, positive and negative items). As an 	 *
 *	additional feature, it validates the forms and that the map		 *
 *	contains at least a final destination; information minimum		 *
 *	required to create a race into de database.						 *
 *********************************************************************/

var ubc = new google.maps.LatLng(49.263261,-123.253899);
var positive = 'img/star.png';
var negative = 'img/bomb.png';
var goal = 'img/goal.png';
var im = new ItemManager(40);
var userArray = [];
var jsonObject = [];
var map;
var marker;
var circle;
var request;
var finalDestinationMarker = false;
var string = "";
var type = 0;
var sizeOfRadius = 250;
var zoomOfMap = 10;
var goalIcon = 1;
var positiveIcon = 2;
var negativeIcon = 3;
var jsonString;
var countUsers = 0;

/*********************************************************************
 * This function sets the type of the item:							 *
 * 1. Final destination												 *
 * 2. Positive Item													 *
 * 3. Negative Item													 *
 *********************************************************************/
function setType(itemType){
	type = itemType;
}

/*********************************************************************
 * Initial method called when createRace.html is loaded. The 		 *
 * functionality of this method is to set the basic options Google 	 *
 * Maps uses, such as the zoom and the way it is presented (ROADMAP) *
 * as well as the zoom. After the options are set, the map object is *
 * created and a click listener is added to it.						 *
 *********************************************************************/
function initialize() {
	var myOptions = {
	   	zoom: zoomOfMap,
	   	center: ubc,
	   	mapTypeId: google.maps.MapTypeId.ROADMAP
	};
		  
	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
			
	google.maps.event.addListener(map, 'click', function(event){
		addMarker(event.latLng);						
	});
}

/*********************************************************************
 * This method adds a pin (marker) to the map depending on the type	 *
 * of marker needed. It also makes sure only one final destination is*
 * added to the map. It creates the marker object, and calls 		 *
 * setIcon() that sets the image to the marker and addRadius() that  *
 * adds a circle around the marker to set a radius. At the end, an 	 *
 * array of these markers is stored.								 *
 *********************************************************************/
function addMarker(latlang){
	if((finalDestinationMarker == false && type > 0) || type > 1){
		var location = new google.maps.LatLng(latlang.lat(), latlang.lng());	
		marker = new google.maps.Marker({									
			position: location,													
			map: map,
			draggable: false,													//Missing option of changing the locations
			clickable: false															
		});
			
		setIcon();
		addRadius();
					
		var object = new Marker(location, type, '1');
		im.addElement(object);
	}
}

/*********************************************************************
 * SetIcon checks which type of marker was set and based on this info*
 * adds the image to the marker that is dropped on the map.			 *
 *********************************************************************/
function setIcon(){
	if(type == goalIcon){
		marker.setIcon(goal);
		finalDestinationMarker = true;
	}
	else if(type == positiveIcon)
		marker.setIcon(positive);
	else if(type == negativeIcon)
		marker.setIcon(negative);
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
	circle.bindTo('center', marker, 'position');
}

/*********************************************************************
 * This is a class created in order to manage every other class into *
 * a dynamic array. It contains basic methods such as getting the 	 *
 * size of the array, adding and getting an element, and finally 	 *
 * resizing the array.												 *
 *********************************************************************/ 		
function ItemManager(){
	this.markerArray = new Array(40);
	this.size = 0;
	
	this.getSize = getSize;
	this.getElementAt = getElementAt;
	this.addElement = addElement;
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
function Marker(location, type, value){
	this.location = location;
	this.type = type;
	this.value = value;
}

/*********************************************************************
 * This method validates a name race, at least one friend is invited *
 * as well as a final destination, date and time is inserted. If this*
 * requirements are not addressed, an alert message is popped to tell*
 * the user the required information is missing. When everything is  *
 * as it is supposed to be a JSON object will be created.			 *
 *********************************************************************/
function validate(){
	var total = 0;
	var raceName = document.getElementById("raceName_text");
	var date = document.getElementById("date");
	var time = document.getElementById("time");
	var raceNameError = document.getElementById("raceNameError");
	var friendsError = document.getElementById("friendsError");
	var dateTimeError = document.getElementById("dateTimeError");
	var mapError = document.getElementById("mapError");
	
	if(raceName.value.length > 0){
		total++;
		raceNameError.innerHTML = "";
	}else
		raceNameError.innerHTML = "Missing race name";
		
	if(userArray.length > 0){
		total++;
		friendsError.innerHTML = "";
	}else
		friendsError.innerHTML = "At least add 1 racer";
		
	if(date.value.length > 4){
		total++;
		dateTimeError.innerHTML = "";
	}else
		dateTimeError.innerHTML = "Missing date or time";
		
	if(time.value.length > 4){
		total++;
		dateTimeError.innerHTML = "";
	}else
		dateTimeError.innerHTML = "Missing date or time";
		
	for(var i=0; i<im.getSize(); i++){
		if(im.getElementAt(i).type == 1){
			total++;
			mapError.innerHTML = "";
			break;
		}
	}
	
	if(im.getSize() == 0)
		mapError.innerHTML = "Add at least a final destination";
	
	if(total == 5)
		createJSON();
}

/*********************************************************************
 * This method adds the username inserted in the invite friend text	 *
 * box to ann array. Before it validates the first character is the @*
 * sign, and it restricts the number of friends invited to 4		 *
 *********************************************************************/
function addUser(){
	var username = document.getElementById("addFriends").value;
	if (username[0] == "@" && countUsers < 4){
		userArray.push(username);
		countUsers++;
		for(var i=0; i<userArray.length; i++){
			string += userArray[i] + "<br>";
		}
		document.getElementById("userList").innerHTML = string;
		string = "";
		document.getElementById("addFriends").value = "";
	}
}

/*********************************************************************
 * createJSON() is the method in charge of in formatting all the	 *
 * information gotten and send it to the server as a JSON object.	 *
 *********************************************************************/
function createJSON(){	
	var itemString = '';
	var racersString = '';
	var dateTime = document.getElementById("date").value + " " + document.getElementById("time").value;
	
	for(var i=0; i<im.getSize(); i++){
		var x = im.getElementAt(i);
		if(x.type == 1){
			itemString+='{"location": "' + x.location + '", "type": "' + x.type + '", "value": "0"}, ';
		}
		else if(x.type == 2){
			itemString+='{"location": "' + x.location + '", "type": "' + x.type + '", "value": "250"}, ';
		}
		else if(x.type == 3){
			itemString+='{"location": "' + x.location + '", "type": "' + x.type + '", "value": "-250"}, ';
		}
	}
	
	itemString = itemString.substring(0,itemString.length-2);
	
	//racersString += '{"name": ""}, ';					//TODO it should be usname gotten from a session variable
	for(var i=0; i<userArray.length; i++){
		racersString+='{"name": "' + userArray[i]+'"}, ';
	}
	
	racersString = racersString.substring(0,racersString.length-2);
		
	jsonString = '{"name": "'+document.getElementById('raceName_text').value + '", "dateTime": "' + dateTime + '", "items": [' + itemString + '], "racers": [' + racersString + ']}';
	
	send();
}

/*********************************************************************
 * Method that sends the JSON object to server. Creates an 			 *
 * XMLHttpRequest, sends it to the server as POST, and handles the 	 *
 * response from the server.										 *
 *********************************************************************/
function send(){
	//var newRace = JSON.stringify(jsonString);
	var newRace = jsonString;
	var url = "CreateRaceController";
	request = new XMLHttpRequest();
	request.onreadystatechange = handleResponse;
	request.open("POST",url,true);
	request.send(newRace);
}

/*********************************************************************
 * Method for handling the server's response. It redirects the user  *
 * back to it home page.											 *
 *********************************************************************/
function handleResponse(){
	if((request.status == 200)&&(request.readyState == 4))
		reDirect("usr_home.html");
//	else
//		alert("There was an error with the application, please restart the app. Error: "+request.status);
}