/*********************************************************************
 * This class shows the past races (races raced) from a specific user*
 *********************************************************************/

var raceManager = new Manager(40);
var request;
var sendOnce = true;
var sendRaceId = false;
var sizePerRace = 105;
var numberOfRaces = 5;

/*********************************************************************
 * sizeOfSquare() is a method whose purpose is to dynamically set the*
 * height of the square background where the list of races are 		 *
 * presented.														 *
 *********************************************************************/ 
function sizeOfSquare(){
	
	var div = document.getElementById('roundSquare_races');
	
	div.style.height = sizePerRace * numberOfRaces + 'px';
	
	if(sendOnce){
		sendOnce = false;
		send();
	}
}

/*********************************************************************
 * Method that sends that calls ViewHistoryController.java class	 *
 * in order to display the past races from a certain user.			 *
 *********************************************************************/
function send(){
	request = new XMLHttpRequest();
	var url = "ViewHistoryController";
	request.onreadystatechange = handleResponse;
	request.open("GET",url,true);
	request.send();
}

/*********************************************************************
 * Method for handling the server's response. It receives a JSON 	 *
 * object from the server and calls receiveJSON for its manipulation *
 *********************************************************************/
function handleResponse(){
	if((request.status == 200)&&(request.readyState == 4)){
		var jsonString = request.responseText;
		receiveJSON(jsonString);
	}
}


/*********************************************************************
 * This method receives the JSON object sent by the server and		 *
 * manipulates the information, in this case, the list of races from *
 * a certain user, and calls fillRaces() for display in the web page *
 *********************************************************************/ 
function receiveJSON(jsonString){
	var jsonObject = eval('('+jsonString+')');
	
	for(var i=0; i<jsonObject.races.length; i++){
		var races = new Race(jsonObject.races[i].raceID,jsonObject.races[i].creatorID,jsonObject.races[i].name, jsonObject.races[i].time, jsonObject.races[i].rank, jsonObject.races[i].score);
		raceManager.addElement(races);
	}
	
	fillRaces();
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
 * Race class. It contains the variables a race should contains such *
 * as the race and creator id, name, time, score and the ranking a 	 *
 * user had. Meaning, the position based on the other racer's score	 *
 *********************************************************************/
function Race(raceId, creatorId, name, time, rank, score){
	this.raceId = raceId;
	this.creatorId = creatorId;
	this.name = name;
	this.time = time;
	this.rank = rank;
	this.score = score;
}

/*********************************************************************
 * Method that displays the races from a certain user in a div on the*
 * webpage.														     *
 *********************************************************************/
function fillRaces(){
	var objectDiv = document.getElementById('raceList');
	var i;

	for(i=0; i<raceManager.getSize(); i++){
		objectDiv.innerHTML+= "<div>"+raceManager.getElementAt(i).name + "<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Score: " +raceManager.getElementAt(i).score +" Rank: " +raceManager.getElementAt(i).rank+"<br><br><br></div>";
	}
	numberOfRaces = i;
	sizeOfSquare();
}