var raceManager = new Manager(40);
var request;
var sendOnce = true;
var sendRaceId = false;
var sizePerRace = 110;
var numberOfRaces = 1;

function sizeOfSquare(){
	
	var div = document.getElementById('roundSquare_races');
	
	div.style.height = sizePerRace * numberOfRaces + 'px';
	
	if(sendOnce){
		sendOnce = false;
		send();
	}
}

function send(){
	request = new XMLHttpRequest();
	var url = "PreRaceController?";
	request.onreadystatechange = handleResponse;
	request.open("GET",url,true);
	request.send();
}

function handleResponse(){
	if((request.status == 200)&&(request.readyState == 4)){
		var jsonString = request.responseText;
		receiveJSON(jsonString);
	}
}

function receiveJSON(jsonString){
	var jsonObject = eval('('+jsonString+')');
	
	for(var i=0; i<jsonObject.races.length; i++){
		var races = new Race(jsonObject.races[i].raceID,jsonObject.races[i].creatorID,jsonObject.races[i].name, jsonObject.races[i].time, jsonObject.races[i].score);
		raceManager.addElement(races);
	}
	
	fillRaces();
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

function Race(raceId, creatorId, name, time, score){
	this.raceId = raceId;
	this.creatorId = creatorId;
	this.name = name;
	this.time = time;
	this.score = score;
}

function fillRaces(){
	var objectDiv = document.getElementById('raceList');
	var i;

	for(i=0; i<raceManager.getSize(); i++){
		objectDiv.innerHTML+= "<div onclick=raceClicked('"+raceManager.getElementAt(i).raceId+"')>"+raceManager.getElementAt(i).name + "<br><br><br></div>";
	}
	numberOfRaces = i;
	sizeOfSquare('raceList');
}

function raceClicked(raceId){
	window.location = "race.html?raceId="+raceId.toString();
}