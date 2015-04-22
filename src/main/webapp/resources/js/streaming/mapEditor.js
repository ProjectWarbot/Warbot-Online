var contener = document.getElementById('mapEditor');

var colorStreamOff = 0x666666;
var stage = new PIXI.Stage(colorStreamOff);
var renderer = new PIXI.autoDetectRenderer(0 , 0);
var cameraMapEditor = new PIXI.DisplayObjectContainer();
var hudMapEditor = new PIXI.DisplayObjectContainer();

var listAgentEditor = new Array();

var buttonTabDebug = new Array();

var buttonAddAgentME = false;
var buttonRemoveAgentME = false;
var buttonPerceptAgentME = false;

var nameTeamSelected = "red";
var nameAgentSelected = "WarBase";
var nameMapSelected = "closed";

requestAnimFrame( animate );
initDebug();
cameraMove(stage, cameraMapEditor);
addWheelLister();

createNewMap();

function initDebug() {
    stage.interactive = true;
    renderer.view.style.display = "block";
    contener.appendChild(renderer.view);
    cameraMapEditor.zoom = 1;
    stage.addChild(cameraMapEditor);
    stage.addChild(hudMapEditor);
    cameraMapEditor.position.x = 0;
    cameraMapEditor.position.y = 0;
}

function createNewMap() {
    var mapWarbot = new PIXI.Sprite(map);

    mapWarbot.position.x = 0;
    mapWarbot.position.y = 0;
    mapWarbot.anchor.x = 0;
    mapWarbot.anchor.y = 0;
    mapWarbot.alpha = 1;

    cameraMapEditor.newMap = mapWarbot;
    cameraMapEditor.addChild(mapWarbot);


}

function createAgentMapEditor(scene, teamName, type , posX, posY) {

    var agent = null;

    if(nameTeamSelected == "red") {
        agent = new PIXI.Sprite(getSpriteAgent(type, 1));
        agent.teamType = 1;
    }
    else if (nameTeamSelected == "blue") {
        agent = new PIXI.Sprite(getSpriteAgent(type, 2));
        agent.teamType = 2;
    }
    else if (nameTeamSelected == "mother") {
    	agent = new PIXI.Sprite(getSpriteAgent(type, 0));
        agent.teamType = 0;
    }
    else {
        console.log("Bug selected team");
    }

    agent.type = type;
    agent.name = type + "-" + listAgentEditor.length;

    agent.position.x = posX;
    agent.position.y = posY;

    agent.anchor.x = 0.5;
    agent.anchor.y = 0.5;

    agent.angle = 0;
    agent.rotation = Math.PI * (agent.angle / 180);
    agent.scale.x = 0.5;
    agent.scale.y = 0.5;

    agent.interactive = true;
    agent.buttonMode = true;
    agent.defaultCursor = "pointer";







    scene.addChild(agent);
    listAgentEditor.push(agent);
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

    if(buttonAddAgentME) {
        contener.style.cursor = "crosshair";
    }
    else if(buttonRemoveAgentME) {
        contener.style.cursor = "no-drop";
    }
    else {
        contener.style.cursor = "default";
    }

    renderer.render(stage);
}

function cameraMove(stg, cam) {

	var isDragging = false;
	var prevX;
	var prevY;

    var dx;
    var dy;

    var tx;
    var ty;

    var vx = 0;
    var vy = 0;

	stg.mousedown = function (moveData) {
		var pos = moveData.global;
		prevX = pos.x;
		prevY = pos.y;
		isDragging = true;


		if(buttonAddAgentME) {
		    createAgentMapEditor(cameraMapEditor, nameTeamSelected, nameAgentSelected, tx - vx, ty-vy);
		}
	};

	stg.mouseup = function (moveDate) {
		isDragging = false;
	};


	stg.mousemove = function (moveData) {

	    var pos = moveData.global;
	    dx = pos.x - prevX;
        dy = pos.y - prevY;

        tx = pos.x;
        ty = pos.y;

		if (!isDragging) {
			return;
		}

		cam.position.x += dx;
		cam.position.y += dy;

		vx += dx;
		vy += dy;

		//tx = cam.position.x - pos.x;
		//ty = cam.position.y - pos.y;
/*
		console.log("BITE");
		console.log(pos.x);
		console.log(pos.y);
		console.log(cam.position.x);
		console.log(cam.position.y);
*/
		prevX = pos.x;
		prevY = pos.y;
	};


}

function nameTeamChange() {
    nameTeamSelected = document.getElementById("selectTeamName").value;
    //console.log(nameTeamSelected);
}

function nameAgentChange() {
    nameAgentSelected = document.getElementById("selectAgentName").value;
    //console.log(nameAgentSelected);
}

function nameMapChange() {
    nameMapSelected = document.getElementById("selectMapName").value;
    //console.log(nameMapSelected);
}

function getSpriteAgent(typeAgent, typeColor) {
	if(typeAgent == "WarExplorer") {
		if(typeColor == 1) {
			return explorerRed;
		}
		else if (typeColor == 2){
			return explorerBlue;
		}
	}
	else if(typeAgent == "WarEngineer") {
		if(typeColor == 1) {
			return engineerRed;
		}
		else if (typeColor == 2){
			return engineerBlue;
		}
	}
	else if(typeAgent == "WarRocketLauncher") {
		if(typeColor == 1) {
			return rocketLauncherRed;
		}
		else if (typeColor == 2){
			return rocketLauncherBlue;
		}
	}
	else if(typeAgent == "WarKamikaze") {
		if(typeColor == 1) {
			return kamikazeRed;
		}
		else if (typeColor == 2){
			return kamikazeBlue;
		}
	}
	else if(typeAgent == "WarTurret") {
		if(typeColor == 1) {
			return turretRed;
		}
		else if (typeColor == 2){
			return turretBlue;
		}
	}
	else if(typeAgent == "WarBase") {
		if(typeColor == 1) {
			return baseRed;
		}
		else if (typeColor == 2){
			return baseBlue;
		}
	}
	else if(typeAgent == "Wall") {
		return wall;
	}
	else if(typeAgent == "WarRocket") {
		return rocket;
	}
	else if(typeAgent == "WarBomb") {
		return bomb;
	}
	else if(typeAgent == "WarFood" && typeColor == 0) {
		return food;
	}
	else {
		console.log("bug getSpriteAgent")
	}
}

function resetMapEditor(){

	for (i = 0; i < listAgentEditor.length; i++) {
		cameraMapEditor.removeChild(listAgentEditor[i]);
	}

	while(listAgentEditor.length > 0) {
        listAgentEditor.pop();
    }

}

