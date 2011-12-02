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

function setType(itemType){
	alert(itemType);
	type = itemType;
}
		
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

function addRadius(){
	circle = new google.maps.Circle({
	  map: map,
	  radius: sizeOfRadius,    								// 250mts
	  fillColor: '#00BFFF',
	  strokeWeight: '1px'
	});
	circle.bindTo('center', marker, 'position');
}

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

function Marker(location, type, value){
	this.location = location;
	this.type = type;
	this.value = value;
}

//function display(){
//	for(var i=0; i<im.getSize(); i++)
//		alert(im.getElementAt(i).location + "     " + im.getElementAt(i).type);
//}

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

function createJSON(){
//	var items = [];
//	var users  = [];
//	var dateTime = document.getElementById("date").value + " " + document.getElementById("time").value;
//	
//	jsonObject.push({"name":document.getElementById("raceName_text")});
//	
//	for(var i=0; i<im.getSize(); i++){
//		var x = im.getElementAt(i);
//		if(x.type == 1){
//			items.push({"location":x.location, "type":x.type, "value":'0'});
//		}
//		else if(x.type == 2){
//			items.push({"location":x.location, "type":x.type, "value":'250'});
//		}
//		else if(x.type == 3){
//			items.push({"location":x.location, "type":x.type, "value":'-250'});
//		}
//	}
//	jsonObject.push({"items":items});
//	for(var i=0; i<userArray.length; i++){
//		if(i==0){
//			users.push({"username":'@ssalazars'});
//		}
//		else{
//			users.push({"username":userArray[i]});
//		}
//	}
//	jsonObject.push({"racers":users});
//	jsonObject.push({"dateTime":dateTime});
	
//	jsonObject = {
//		name: document.getElementById("raceName_text").value,
//		items: [{location: "(12345,67890)", type: "1", value:"0"
//		}],
//		racers: [{name: "@ssalazars"}],
//		dateTime: dateTime
//	};
	
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

function send(){
	//var newRace = JSON.stringify(jsonString);
	var newRace = jsonString;
	var url = "CreateRaceController";
	request = new XMLHttpRequest();
	request.onreadystatechange = handleResponse;
	request.open("POST",url,true);
	request.send(newRace);
}

function handleResponse(){
	if((request.status == 200)&&(request.readyState == 4))
		reDirect("usr_home.html");
//	else
//		alert("There was an error with the application, please restart the app. Error: "+request.status);
}