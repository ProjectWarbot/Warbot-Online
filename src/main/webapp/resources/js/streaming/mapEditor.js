var contener = document.getElementById('mapEditor');

var colorStreamOff = 0x000000;
var stage = new PIXI.Stage(colorStreamOff);
var renderer = new PIXI.autoDetectRenderer(0 , 0);
var cameraMapEditor = new PIXI.DisplayObjectContainer();
var hudMapEditor = new PIXI.DisplayObjectContainer();

var agentModeDebug = new Array();

var buttonTabDebug = new Array();

requestAnimFrame( animate );
initDebug();
cameraMove(stage, cameraMapEditor);
addWheelLister();

function initDebug() {
    stage.interactive = true;
    renderer.view.style.display = "block";
    contener.appendChild(renderer.view);
    cameraMapEditor.zoom = 1;
    stage.addChild(cameraMapEditor);
    stage.addChild(hudMapEditor);
}

function initHUDDebug() {
		cameraMapEditor.position.x = 0;
    	cameraMapEditor.position.y = 0;
    	stage.setBackgroundColor(0x666666);

}

function createAgentDebug(scene, type , posX, posY) {

    var agent = null;

    agent.type = type;

    agent.name = type + "-" + agentModeDebug.length;



}


function addWheelLister() {
	if (contener.addEventListener) {
	// IE9, Chrome, Safari, Opera
	contener.addEventListener("mousewheel", cameraZoome, false);
	// Firefox
	contener.addEventListener("DOMMouseScroll", cameraZoome, false);
	}
	// IE 6/7/8
	else contener.attachEvent("onmousewheel", cameraZoome);
}

function cameraZoome(e) {



};

function animate() {

    requestAnimFrame( animate );

    renderer.resize(contener.offsetWidth-1, contener.offsetHeight-1);

    var coordCenterX = contener.offsetWidth-1 / 2;
    var coordCenterY = contener.offsetHeight-1 / 2;

    renderer.render(stage);
}