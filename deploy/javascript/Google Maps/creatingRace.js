var ubc = new google.maps.LatLng(49.263261,-123.253899);
var map;
var marker;
var circle;
var im = new ItemManager(40);
var type = 0;
var finalDestinationMarker = false;
var positive = 'http://mapicons.nicolasmollet.com/wp-content/uploads/mapicons/shape-default/color-2c36f5/shapecolor-color/shadow-1/border-dark/symbolstyle-contrast/symbolshadowstyle-dark/gradient-iphone/star-3.png';
var negative = 'http://mapicons.nicolasmollet.com/wp-content/uploads/mapicons/shape-default/color-c03638/shapecolor-color/shadow-1/border-dark/symbolstyle-white/symbolshadowstyle-dark/gradient-no/bomb.png';
var goal = 'http://mapicons.nicolasmollet.com/wp-content/uploads/mapicons/shape-default/color-66c547/shapecolor-dark/shadow-1/border-white/symbolstyle-white/symbolshadowstyle-no/gradient-no/flag-export.png';

function setType(itemType){
	type = itemType;
}
		
function initialize() {
	var myOptions = {
	   	zoom: 11,
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
	if(type == 1){
		marker.setIcon(goal);
		finalDestinationMarker = true;
	}
	else if(type == 2)
		marker.setIcon(positive);
	else if(type == 3)
		marker.setIcon(negative);
}

function addRadius(){
	circle = new google.maps.Circle({
	  map: map,
	  radius: 1000,    								// 1km
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

function display(){
	for(var i=0; i<im.getSize(); i++)
		alert(im.getElementAt(i).location + "     " + im.getElementAt(i).type);
}