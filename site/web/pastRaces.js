function sizeOfSquare(theDiv){
	var sizePerRace = 60;
	var numberOfRaces = 4;
	
	var div = document.getElementById(theDiv);
	
	div.style.height = sizePerRace * numberOfRaces + 'px';
}