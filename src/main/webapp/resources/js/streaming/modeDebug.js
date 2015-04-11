
var cameraDebug = new PIXI.DisplayObjectContainer();
var hudDebug = new PIXI.DisplayObjectContainer();
var agentModeDebug = new Array();

var buttonTabDebug = new Array();
initDebug();

function initDebug() {
    cameraDebug.zoom = 1;
    stage.addChild(cameraDebug);
    stage.addChild(hudDebug);
}

function initHUDDebug() {
	cameraDebug.position.x = 0;
	cameraDebug.position.y = 0;
	stage.setBackgroundColor(0x666666);
	addButton(hudDebug, buttonTeamRed, buttonTeamRedDown, buttonTeamRedTrans, 20, 20, buttonTabDebug, 11);
    addButton(hudDebug, buttonTeamBlue, buttonTeamBlueDown, buttonTeamBlueTrans, 60, 20, buttonTabDebug, 12);
}
