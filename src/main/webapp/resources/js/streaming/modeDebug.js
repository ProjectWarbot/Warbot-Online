
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
	addButtonDebug(hudDebug, buttonTeamRed, buttonTeamRedDown, buttonTeamRedTrans, 20, 20, buttonTabDebug, 11);
    addButtonDebug(hudDebug, buttonTeamBlue, buttonTeamBlueDown, buttonTeamBlueTrans, 60, 20, buttonTabDebug, 12);

    addButtonDebug(hudDebug, buttonCreateBaseRed, buttonCreateBaseRedDown, buttonCreateBaseRedTrans, 20, 60, buttonTabDebug, 13);
    addButtonDebug(hudDebug, buttonCreateBaseBlue, buttonCreateBaseBlueDown, buttonCreateBaseBlueTrans, 60, 60, buttonTabDebug, 14);
}


function createAgentDebug(scene, type , posX, posY) {

    var agent = null;

    agent.type = type;

    agent.name = type + "-" + agentModeDebug.length;



}

function addButtonDebug (scene, form, formDown, formTrans, cX, cY, tab, type) {

	var button = new PIXI.Sprite(formTrans);

	button.position.x = cX;
	button.position.y = cY;

	button.anchor.x = 0.5;
	button.anchor.y = 0.5;

	button.interactive = true;
	button.buttonMode = true;
	button.defaultCursor = "pointer";
	button.type = type;

	button.alpha = 1;
	button.isdown = false;

	tab.push(button);

	button.mouseover = function(data) {
        this.isOver = true;
        if (this.isdown)
            return;
        this.setTexture(form);
    };

    button.mouseout = function(data) {
        this.isOver = false;
        if (this.isdown)
            return;
        this.setTexture(formTrans);
   	};

   	button.mousedown = function(data) {
   		if (this.isdown) {
   			this.isdown = false;
        	this.setTexture(form);

        	/*

        	for (i = 0; i < agentTab.length; i++) {
        		if(type == 1) {
        			// life
        			if(agentTab[i].type != "WarFood") {
        				agentTab[i].SpriteLife.alpha = -1;
        			}
        		}
        		else if (type == 2){
        			if(agentTab[i].type != "WarFood") {
                    	agentTab[i].debug.alpha = -1;
                    }
        		}
        		else if (type == 3){
        			agentTab[i].SpritePercept.alpha = -1;
        		}
        	}*/
   		}
   		else {
   			this.isdown = true;
        	this.setTexture(formDown);
        	/*
        	for (i = 0; i < agentTab.length; i++) {
        		if(type == 1) {
        			if(agentTab[i].type != "WarFood") {
        				agentTab[i].SpriteLife.alpha = 1;
        			}
        		}
        		else if (type == 2){
        			if(agentTab[i].type != "WarFood") {
        				agentTab[i].debug.alpha = 1;
        			}
        		}
        		else if (type == 3){
        			agentTab[i].SpritePercept.alpha = 1;
        		}
        	}*/
   		}
    };

	scene.addChild(button);


}