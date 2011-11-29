var currentLocation;
var newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687);
var ubc = new google.maps.LatLng(49.263261,-123.253899);
var browserSupportFlag =  new Boolean();
var marker;
var markerItem;
var map;
var contentString;
var infowindow;
var pic;
var msg;
var itemManager = new Manager(40);
var markerManager = new Manager(40);
var userManager = new Manager(5);
var user;
var positive = 'img/star.png';
var negative = 'img/bomb.png';
var goal = 'img/goal.png';
var itemMarkerCounter = 0;
var markersArray = [];
var circlesArray = [];
var isFinalDestination = false;
		
function initialize() {
	var myOptions = {
    	zoom: 11,
    	center: ubc,
    	mapTypeId: google.maps.MapTypeId.ROADMAP
	};
  
	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
}

function getGeoLocation(){
	//Try W3C Geolocation (Preferred)
	if(navigator.geolocation) {
	
		browserSupportFlag = true;																			//Browser supports geolocation
		navigator.geolocation.getCurrentPosition(function(position) {
		
			currentLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);	//Get coordinates of user
			map.setCenter(currentLocation);																	//Center map to location
					
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

function addUserMarker(location){											//Metodo que agrega un marker al click
	marker = new google.maps.Marker({										//Creacion de marker
		position: location,													//Establecer coordenadas del marker
		map: map,															//En que mapa lo vamos a poner				
	});
	
	for(var i=0; i<itemManager.getSize(); i++)
		obtainDistance(location, itemManager.getElementAt(i).location);				//Metodo para obtener una distancia entre dos markers
	itemMarkerCounter = 0;
	
	setUserIcon(1);
	
	pic = document.getElementById("addPhoto").value;
	msg = document.getElementById("message").value;
	
	if(pic.length > 0 || msg.length > 0)
		addInfoWindow(pic,msg);
	
	var object = new Marker(location, '1', pic, msg);
	
	document.getElementById("addPhoto").value = "";
	document.getElementById("message").value = "";
	
	google.maps.event.addListener(marker, 'click', function() {
	  infowindow.open(map,marker);	//Evento to open infowindow while a marker is clicked
	});
	
	obtainDistance(location, getFinalDestinationLocation());
	var x = google.maps.geometry.spherical.computeDistanceBetween(location,getFinalDestinationLocation());
	alert(x);
}

function getFinalDestinationLocation(){
	for(var i=0; i<itemManager.getSize(); i++)
		if(itemManager.getElementAt(i).type == '1'){
			isFinalDestination  = true;
			return itemManager.getElementAt(i).location;
		}
}

function addInfoWindow(pic, msg){
	contentString = '<div><img src="'+pic+'"/></br>'+msg+'</div>';									//Contenido del infowindow
			
	infowindow = new google.maps.InfoWindow({														//Creacion de infowindow
	    content: contentString																		//Contenido
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

function obtainDistance(currentLocation,markerLocation){
	var obtainDistanceFromTwoPoints = new google.maps.DistanceMatrixService();
	obtainDistanceFromTwoPoints.getDistanceMatrix({
		origins: [currentLocation],
		destinations: [markerLocation],
		travelMode: google.maps.TravelMode.WALKING
	}, callback);
}

function callback(response,status){
	if (status == google.maps.DistanceMatrixStatus.OK) {
		var origins = response.originAddresses;
		var destinations = response.destinationAddresses;
			
		for (var i = 0; i < origins.length; i++) {
			var results = response.rows[i].elements;
			for (var j = 0; j < results.length; j++) {
				var element = results[j];
				var distance = element.distance.value;
				if(isFinalDestination){
					if(distance > 250)
						document.getElementById("distanceToDestination").innerHTML = distance + "";	
					else{
						document.getElementById("distanceToDestination").innerHTML = 0 + "";
					}
					isFinalDestination = false;	
				}
				if(distance < 250)					
					deleteMarkerIcon(itemMarkerCounter);				
			}
		}
	}
	itemMarkerCounter++;
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

function display(){
	for(var i=0; i<itemManager.getSize(); i++)
		alert(itemManager.getElementAt(i).location + "     " + itemManager.getElementAt(i).type);
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
	if(type == 1)
		markerItem.setIcon(goal);
	else if(type == 2)
		markerItem.setIcon(positive);
	else if(type == 3)
		markerItem.setIcon(negative);
}

function addRadius(){
	circle = new google.maps.Circle({
	  map: map,
	  radius: 1000,    								// 1km
	  fillColor: '#00BFFF',
	  strokeWeight: '1px'
	});
	circle.bindTo('center', markerItem, 'position');
	
	circlesArray.push(circle);
}

function deleteMarkerIcon(marker){
	itemManager.getElementAt(marker).value = 0;
	itemManager.removeElementAt(marker);					//Don't know if this is useful
	removeAllMarkers();
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

function initRequest(){
	if(window.XMLHtppRequest)
		return new XMLHttpRequest();
}

function updateMap(){
	
}