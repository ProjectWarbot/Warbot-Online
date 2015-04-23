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

var counterAgent = {
	food : 0,
	redBase : 0,
	blueBase : 0,
	redExplorer : 0,
	blueExplorer : 0,
	redKamikaze : 0,
	blueKamikaze : 0,
	redRocketLauncher : 0,
	blueRocketLauncher : 0,
	redTurret : 0,
	blueTurret : 0,
	redEngineer : 0,
	blueEngineer : 0,
	redWall : 0,
	blueWall : 0
};

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
    cameraMapEditor.follow = false;
    cameraMapEditor.agentFollow = null;
    cameraMapEditor.agentEntityFollow;
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
        agent.teamName = "My Team";
    }
    else if (nameTeamSelected == "blue") {
        agent = new PIXI.Sprite(getSpriteAgent(type, 2));
        agent.teamType = 2;
        agent.teamName = "Other Team";
    }
    else if (nameTeamSelected == "mother") {
    	agent = new PIXI.Sprite(getSpriteAgent(type, 0));
        agent.teamType = 0;
        agent.teamName = "Mother Team";
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

    var percept = new PIXI.Sprite(getSpritePercept(agent));
    percept.position.x = agent.position.x;
    percept.position.y = agent.position.y;
    percept.scale.x = 0.5;
    percept.scale.y = 0.5;
    percept.alpha = -1;

	var followAgentBorder = new PIXI.Sprite(followAgent);
	followAgentBorder.position.x = agent.position.x;
    followAgentBorder.position.y = agent.position.y;
	followAgentBorder.scale.x = 0.5;
    followAgentBorder.scale.y = 0.5;
    followAgentBorder.anchor.x = 0.5;
    followAgentBorder.anchor.y = 0.5;
    followAgentBorder.alpha = -1;



    // TODO move followAgentBorder

	agent.SpritePercept = percept;
	agent.SpriteFollow = followAgentBorder;

    changeAnchorPercept(agent);
    changePositionPercept(agent);

	scene.addChild(followAgentBorder);
	scene.addChild(percept);
    scene.addChild(agent);
    listAgentEditor.push(agent);

    	agent.mousedown = function(data) {

			if(buttonRemoveAgentME) {
				cameraMapEditor.removeChild(agent.SpriteFollow);
                cameraMapEditor.removeChild(agent.SpritePercept);
                cameraMapEditor.removeChild(agent);
                scene.follow = false;
                scene.agentFollow = -1;
                scene.agentEntityFollow = null;
                document.getElementById('nameOfAgentFollow').innerHTML = "null";
                document.getElementById('teamOfAgentFollow').innerHTML = "null";
                document.getElementById('typeOfAgentFollow').innerHTML = "null";
                document.getElementById('angleOfAgentFollow').innerHTML = "0";

				var i = 0;
				var index = 0;

                while(i < listAgentEditor.length() && listAgentEditor[i].agent.name != this.name) {
              		index++;
                	i++;
                }

                listAgentEditor.splice(index, 1);


			}
			else {
				if (this.isdown) {
					this.isdown = false;
					scene.follow = false;
					scene.agentFollow = -1;
					scene.agentEntityFollow = null;

					this.SpriteFollow.alpha = -1;
					document.getElementById('nameOfAgentFollow').innerHTML = "null";
					document.getElementById('teamOfAgentFollow').innerHTML = "null";
					document.getElementById('typeOfAgentFollow').innerHTML = "null";
					document.getElementById('angleOfAgentFollow').innerHTML = "0";
				}
				else {
					this.isdown = true;
					scene.follow = true;
					if(scene.agentEntityFollow != null)
						scene.agentEntityFollow.SpriteFollow.alpha = -1;
					scene.agentFollow = agent.name;
					scene.agentEntityFollow = this;
					this.SpriteFollow.alpha = 1;
					document.getElementById('nameOfAgentFollow').innerHTML = this.name;
					document.getElementById('teamOfAgentFollow').innerHTML = this.teamName;
					document.getElementById('typeOfAgentFollow').innerHTML = this.type;
					document.getElementById('angleOfAgentFollow').innerHTML = this.angle;
				}
			}
        };
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

function getSpritePercept(agent) {
	if(agent.type == "WarExplorer") {
		if(agent.teamType == 1)
			return perceptExplorerRed;
		else
			return perceptExplorerBlue;
	}
	else if(agent.type == "WarEngineer") {
		if(agent.teamType == 1)
			return perceptEngineerRed;
		else
			return perceptEngineerBlue;
	}
	else if(agent.type == "WarRocketLauncher") {
		if(agent.teamType == 1)
			return perceptRocketLauncherRed;
		else
			return perceptRocketLauncherBlue;
	}
	else if(agent.type == "WarKamikaze") {
		if(agent.teamType == 1)
			return perceptKamikazeRed;
		else
			return perceptKamikazeBlue;
	}
	else if(agent.type == "WarTurret") {
		if(agent.teamType == 1)
    		return perceptTurretRed;
    	else
    		return perceptTurretBlue;
	}
	else if(agent.type == "WarBase") {
		if(agent.teamType == 1)
			return perceptBaseRed;
		else
			return perceptBaseBlue;
	}
	else {
		return perceptOther;
	}
}

function changeAnchorPercept(agent) {

	if(agent.type == "WarExplorer") {
		agent.SpritePercept.anchor.x = 0;
		agent.SpritePercept.anchor.y = 0.5;
	}
	else if(agent.type == "WarEngineer") {
		agent.SpritePercept.anchor.x = 0;
		agent.SpritePercept.anchor.y = 0.51;
	}
	else if(agent.type == "WarRocketLauncher") {
		agent.SpritePercept.anchor.x = 0;
		agent.SpritePercept.anchor.y = 0.5;
	}
	else if(agent.type == "WarKamikaze") {
		agent.SpritePercept.anchor.x = 0;
		agent.SpritePercept.anchor.y = 0.5;
	}
	else if(agent.type == "WarTurret") {
		agent.SpritePercept.anchor.x = 0;
		agent.SpritePercept.anchor.y = 0.5;
	}
	else if(agent.type == "WarBase") {
		agent.SpritePercept.anchor.x = 0.5;
		agent.SpritePercept.anchor.y = 0.5;
	}
	else {
		//console.log("not support this agent");
	}
}

function getSpriteAgent(typeAgent, typeColor) {
	if(typeAgent == "WarExplorer") {
		if(typeColor == 1) {
			counterAgent.redExplorer += 1;
        	document.getElementById('numberOfExplorerRed').innerHTML = counterAgent.redExplorer;
			return explorerRed;
		}
		else if (typeColor == 2){
			counterAgent.blueExplorer += 1;
            document.getElementById('numberOfExplorerBlue').innerHTML = counterAgent.blueExplorer;
			return explorerBlue;
		}
	}
	else if(typeAgent == "WarEngineer") {
		if(typeColor == 1) {
			counterAgent.redEngineer += 1;
            document.getElementById('numberOfEngineerRed').innerHTML = counterAgent.redEngineer;
			return engineerRed;
		}
		else if (typeColor == 2){
			counterAgent.blueEngineer += 1;
            document.getElementById('numberOfEngineerBlue').innerHTML = counterAgent.blueEngineer;
			return engineerBlue;
		}
	}
	else if(typeAgent == "WarRocketLauncher") {
		if(typeColor == 1) {
			counterAgent.redRocketLauncher += 1;
            document.getElementById('numberOfRocketLauncherRed').innerHTML = counterAgent.redRocketLauncher;
			return rocketLauncherRed;
		}
		else if (typeColor == 2){
			counterAgent.blueRocketLauncher += 1;
            document.getElementById('numberOfRocketLauncherBlue').innerHTML = counterAgent.blueRocketLauncher;
			return rocketLauncherBlue;
		}
	}
	else if(typeAgent == "WarKamikaze") {
		if(typeColor == 1) {
			counterAgent.redKamikaze += 1;
            document.getElementById('numberOfKamikazeRed').innerHTML = counterAgent.redKamikaze;
			return kamikazeRed;
		}
		else if (typeColor == 2){
			counterAgent.blueKamikaze += 1;
            document.getElementById('numberOfKamikazeBlue').innerHTML = counterAgent.blueKamikaze;
			return kamikazeBlue;
		}
	}
	else if(typeAgent == "WarTurret") {
		if(typeColor == 1) {
			counterAgent.redTurret += 1;
			document.getElementById('numberOfTurretRed').innerHTML = counterAgent.redTurret;
			return turretRed;
		}
		else if (typeColor == 2){
			counterAgent.blueTurret += 1;
			document.getElementById('numberOfTurretBlue').innerHTML = counterAgent.blueTurret;
			return turretBlue;
		}
	}
	else if(typeAgent == "WarBase") {
		if(typeColor == 1) {
			counterAgent.redBase += 1;
        	document.getElementById('numberOfBaseRed').innerHTML = counterAgent.redBase;
			return baseRed;
		}
		else if (typeColor == 2){
			counterAgent.blueBase += 1;
        	document.getElementById('numberOfBaseBlue').innerHTML = counterAgent.blueBase;
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
		counterAgent.food += 1;
		document.getElementById('numberOfFoodConsoleMap').innerHTML = counterAgent.food;
		return food;
	}
	else {
		console.log("bug getSpriteAgent")
	}
}

function perceptAgentMapEditor(alpha) {
	for (i = 0; i < listAgentEditor.length; i++) {
		listAgentEditor[i].SpritePercept.alpha = alpha;

	}
}

function changePositionPercept(agent) {
	if(agent.type == "WarExplorer") {
		agent.SpritePercept.position.x = agent.position.x;
		agent.SpritePercept.position.y = agent.position.y;
		agent.SpritePercept.rotation = agent.rotation;
	}
	else if(agent.type == "WarEngineer") {
		agent.SpritePercept.position.x = agent.position.x;
		agent.SpritePercept.position.y = agent.position.y;
		agent.SpritePercept.rotation = agent.rotation + Math.PI * (15 / 180);
	}
	else if(agent.type == "WarRocketLauncher") {
		agent.SpritePercept.position.x = agent.position.x;
		agent.SpritePercept.position.y = agent.position.y;
		agent.SpritePercept.rotation = agent.rotation + Math.PI * (30 / 180);
	}
	else if(agent.type == "WarKamikaze") {
		agent.SpritePercept.position.x = agent.position.x;
		agent.SpritePercept.position.y = agent.position.y;
		agent.SpritePercept.rotation = agent.rotation + Math.PI * (15 / 180);
	}
	else if(agent.type == "WarTurret") {
		agent.SpritePercept.position.x = agent.position.x;
		agent.SpritePercept.position.y = agent.position.y;
		agent.SpritePercept.rotation = agent.rotation;
	}
	else if(agent.type == "WarBase") {
		agent.SpritePercept.position.x = agent.position.x;
		agent.SpritePercept.position.y = agent.position.y;
	}
	else {
		//NULL
	}
}

function resetMapEditor(){

	for (i = 0; i < listAgentEditor.length; i++) {
		cameraMapEditor.removeChild(listAgentEditor[i].SpriteFollow);
		cameraMapEditor.removeChild(listAgentEditor[i].SpritePercept);
		cameraMapEditor.removeChild(listAgentEditor[i]);
	}

	while(listAgentEditor.length > 0) {
        listAgentEditor.pop();
    }

    cameraMapEditor.follow = false;
    cameraMapEditor.agentFollow = -1;
    cameraMapEditor.agentEntityFollow = null;
    document.getElementById('nameOfAgentFollow').innerHTML = "null";
    document.getElementById('teamOfAgentFollow').innerHTML = "null";
    document.getElementById('typeOfAgentFollow').innerHTML = "null";
    document.getElementById('angleOfAgentFollow').innerHTML = "0";
}

