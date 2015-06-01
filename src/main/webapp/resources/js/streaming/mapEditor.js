var contener = document.getElementById('mapEditor');

var colorStreamOff = 0x666666;
var stage = new PIXI.Stage(colorStreamOff);
var renderer = new PIXI.autoDetectRenderer(0 , 0);
var cameraMapEditor = new PIXI.DisplayObjectContainer();
var hudMapEditor = new PIXI.DisplayObjectContainer();

//La liste des agents que l'on crée sur la map
var listAgentEditor = new Array();

var buttonAddAgentME = false;
var buttonRemoveAgentME = false;
var buttonPerceptAgentME = false;
var buttonMoveAgentME = false;
var buttonRotateAgentME = false;

var mapVector = 14;

var modeMap = "Duel";
var timerMap = 0;

//les variables utilisées pour
//avoir les vrai coordonnées sur
//la map par rapport au coordonnées
//relative de la souris
var tx;
var ty;
var vx = 0;
var vy = 0;

//On sauvegarde les anciennes position de l'agent en mouvement
var oldPositionAgentMoveX = 0;
var oldPositionAgentMoveY = 0;

var nameTeamSelected = "red";
var nameAgentSelected = "WarBase";
var nameMapSelected = "closed";

var numberMaxAgentByTeamUser = 30;
var numberMaxFood = 100;

var counterAgentRed    = 0;
var counterAgentBlue   = 0;

var mapHeigth = 600;
var mapWigth = 1000;

var counterAgent = {
	food               : 0,
	redBase            : 0,
	blueBase           : 0,
	redExplorer        : 0,
	blueExplorer       : 0,
	redKamikaze        : 0,
	blueKamikaze       : 0,
	redRocketLauncher  : 0,
	blueRocketLauncher : 0,
	redTurret          : 0,
	blueTurret         : 0,
	redEngineer        : 0,
	blueEngineer       : 0,
	redWall            : 0,
	blueWall           : 0
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
    cameraMapEditor.agentInMove = false;
    cameraMapEditor.agentMove = null;

    document.getElementById('numberOfFoodConsoleMap').innerHTML = 0 + " / " + numberMaxFood;
    document.getElementById('totalRedTeamAgent').innerHTML = 0 + " / " + numberMaxAgentByTeamUser;
    document.getElementById('totalBlueTeamAgent').innerHTML = 0 + " / " + numberMaxAgentByTeamUser;
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

function resetResumeAgentFollow(){
	document.getElementById('nameOfAgentFollow').innerHTML = "null";
    document.getElementById('teamOfAgentFollow').innerHTML = "null";
    document.getElementById('typeOfAgentFollow').innerHTML = "null";
    document.getElementById('angleOfAgentFollow').innerHTML = "0";
    document.getElementById('distanceViewAgentFollow').innerHTML = "0";
    document.getElementById('angleViewAgentFollow').innerHTML = "0";
    document.getElementById('lifeAgentFollow').innerHTML = "0 / 0";
}

function updateResumeAgentFollow(agent) {
	document.getElementById('nameOfAgentFollow').innerHTML = agent.name;
	document.getElementById('teamOfAgentFollow').innerHTML = agent.teamName;
	document.getElementById('typeOfAgentFollow').innerHTML = agent.type;
	document.getElementById('angleOfAgentFollow').innerHTML = agent.angle;
	document.getElementById('distanceViewAgentFollow').innerHTML = getViewDistance(agent);
    document.getElementById('angleViewAgentFollow').innerHTML = getViewAngle(agent);
	document.getElementById('lifeAgentFollow').innerHTML = getLifeMaxAgent(agent) + " / " + getLifeMaxAgent(agent);
}

function updateResumeCounterAgent(agent) {
	if(agent.type == "WarExplorer") {
		if(agent.teamType == 1) {
			counterAgent.redExplorer -= 1;
			counterAgentRed -= 1;
        	document.getElementById('numberOfExplorerRed').innerHTML = counterAgent.redExplorer;
        	document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
		}
		else if (agent.teamType == 2){
			counterAgent.blueExplorer -= 1;
			counterAgentBlue -= 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfExplorerBlue').innerHTML = counterAgent.blueExplorer;
		}
	}
	else if(agent.type == "WarEngineer") {
		if(agent.teamType == 1) {
			counterAgent.redEngineer -= 1;
			counterAgentRed -= 1;
			document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfEngineerRed').innerHTML = counterAgent.redEngineer;
		}
		else if (agent.teamType == 2){
			counterAgent.blueEngineer -= 1;
			counterAgentBlue -= 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfEngineerBlue').innerHTML = counterAgent.blueEngineer;
		}
	}
	else if(agent.type == "WarRocketLauncher") {
		if(agent.teamType == 1) {
			counterAgent.redRocketLauncher -= 1;
			counterAgentRed -= 1;
			document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfRocketLauncherRed').innerHTML = counterAgent.redRocketLauncher;
		}
		else if (agent.teamType == 2){
			counterAgent.blueRocketLauncher -= 1;
			counterAgentBlue -= 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfRocketLauncherBlue').innerHTML = counterAgent.blueRocketLauncher;
		}
	}
	else if(agent.type == "WarKamikaze") {
		if(agent.teamType == 1) {
			counterAgent.redKamikaze -= 1;
			counterAgentRed -= 1;
			document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfKamikazeRed').innerHTML = counterAgent.redKamikaze;
		}
		else if (agent.teamType == 2){
			counterAgent.blueKamikaze -= 1;
			counterAgentBlue -= 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfKamikazeBlue').innerHTML = counterAgent.blueKamikaze;
		}
	}
	else if(agent.type == "WarTurret") {
		if(agent.teamType == 1) {
			counterAgent.redTurret -= 1;
			counterAgentRed -= 1;
			document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
			document.getElementById('numberOfTurretRed').innerHTML = counterAgent.redTurret;
		}
		else if (agent.teamType == 2){
			counterAgent.blueTurret -= 1;
			counterAgentBlue -= 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
			document.getElementById('numberOfTurretBlue').innerHTML = counterAgent.blueTurret;
		}
	}
	else if(agent.type == "WarBase") {
		if(agent.teamType == 1) {
			counterAgent.redBase -= 1;
			counterAgentRed -= 1;
			document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
        	document.getElementById('numberOfBaseRed').innerHTML = counterAgent.redBase;
		}
		else if (agent.teamType == 2){
			counterAgent.blueBase -= 1;
			counterAgentBlue -= 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
        	document.getElementById('numberOfBaseBlue').innerHTML = counterAgent.blueBase;
		}
	}
	else if(agent.type == "Wall") {
		if(agent.teamType == 1) {
        	counterAgent.redWall -= 1;
        	counterAgentRed -= 1;
        	document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfWallRed').innerHTML = counterAgent.redWall;
        }
        else if (agent.teamType == 2){
        	counterAgent.blueWall -= 1;
        	counterAgentBlue -= 1;
        	document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfWallBlue').innerHTML = counterAgent.blueWall;
        }
	}
	else if(agent.type == "WarFood" && agent.teamType == 0) {
		counterAgent.food -= 1;
		document.getElementById('numberOfFoodConsoleMap').innerHTML = counterAgent.food + " / " + numberMaxFood;
	}
	else {
		console.log("bug getSpriteAgent")
	}
}

function resetResumeCounterAgent() {
	document.getElementById('numberOfFoodConsoleMap').innerHTML = counterAgent.food + " / " + numberMaxFood;
	document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
    document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;

	document.getElementById('numberOfExplorerRed').innerHTML = counterAgent.redExplorer;
    document.getElementById('numberOfExplorerBlue').innerHTML = counterAgent.blueExplorer;
    document.getElementById('numberOfEngineerRed').innerHTML = counterAgent.redEngineer;
    document.getElementById('numberOfEngineerBlue').innerHTML = counterAgent.blueEngineer;
    document.getElementById('numberOfRocketLauncherRed').innerHTML = counterAgent.redRocketLauncher;
    document.getElementById('numberOfRocketLauncherBlue').innerHTML = counterAgent.blueRocketLauncher;
    document.getElementById('numberOfKamikazeRed').innerHTML = counterAgent.redKamikaze;
    document.getElementById('numberOfKamikazeBlue').innerHTML = counterAgent.blueKamikaze;
    document.getElementById('numberOfTurretRed').innerHTML = counterAgent.redTurret;
    document.getElementById('numberOfTurretBlue').innerHTML = counterAgent.blueTurret;
    document.getElementById('numberOfBaseRed').innerHTML = counterAgent.redBase;
    document.getElementById('numberOfBaseBlue').innerHTML = counterAgent.blueBase;
    document.getElementById('numberOfWallRed').innerHTML = counterAgent.redWall;
    document.getElementById('numberOfWallBlue').innerHTML = counterAgent.blueWall;
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





    agent.anchor.x = 0.5;
    agent.anchor.y = 0.5;

    agent.angle = 0;
    agent.rotation = Math.PI * (agent.angle / 180);
    agent.scale.x = 0.5 * cameraMapEditor.zoom;
    agent.scale.y = 0.5 * cameraMapEditor.zoom;

    agent.position.x = posX;
    agent.position.y = posY;

    agent.interactive = true;
    agent.buttonMode = true;
    agent.defaultCursor = "pointer";

    var percept = new PIXI.Sprite(getSpritePercept(agent));
    percept.position.x = agent.position.x;
    percept.position.y = agent.position.y;
    percept.scale.x = 0.5 * cameraMapEditor.zoom;
    percept.scale.y = 0.5 * cameraMapEditor.zoom;

    if(buttonPerceptAgentME)
    	percept.alpha = 1;
	else
		percept.alpha = -1;

	var followAgentBorder;

	if(agent.type != "Wall")
		followAgentBorder = new PIXI.Sprite(followAgent);
	else
		followAgentBorder = new PIXI.Sprite(followWall);

	followAgentBorder.position.x = agent.position.x;
    followAgentBorder.position.y = agent.position.y;
	followAgentBorder.scale.x = 0.5 * cameraMapEditor.zoom;
    followAgentBorder.scale.y = 0.5 * cameraMapEditor.zoom;
    followAgentBorder.anchor.x = 0.5;
    followAgentBorder.anchor.y = 0.5;
    followAgentBorder.alpha = -1;

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
                if(scene.follow = true) {
                	if(scene.agentFollow == agent.name) {
                		scene.follow = false;
                		scene.agentFollow = -1;
                		scene.agentEntityFollow.SpriteFollow.alpha = -1;
                		scene.agentEntityFollow = null;
                		resetResumeAgentFollow();
                	}
                }

				updateResumeCounterAgent(this);

				var i = 0;
				var index = 0;

                while(i < listAgentEditor.length && listAgentEditor[i].name != this.name) {
              		index++;
                	i++;
                }

                listAgentEditor.splice(index, 1);

				cameraMapEditor.removeChild(agent.SpriteFollow);
                cameraMapEditor.removeChild(agent.SpritePercept);
                cameraMapEditor.removeChild(agent);
			}
			else {
				if (this.isdown) {
					if(buttonMoveAgentME) {
						cameraMapEditor.agentInMove = true;
						this.defaultCursor = "grabbing";
						cameraMapEditor.agentMove = agent;
						oldPositionAgentMoveX = agent.position.x;
                        oldPositionAgentMoveY = agent.position.y;
					}
					else {
						this.defaultCursor = "default";
						this.isdown = false;
                    	scene.follow = false;
                    	scene.agentFollow = -1;
                    	if(scene.agentEntityFollow != null)
                    		scene.agentEntityFollow.SpriteFollow.alpha = -1;
                    	scene.agentEntityFollow = null;
                    	this.SpriteFollow.alpha = -1;
                    	resetResumeAgentFollow();
                    }
				}
				else {
					this.isdown = true;
					scene.follow = true;
					if(scene.agentEntityFollow != null)
						scene.agentEntityFollow.SpriteFollow.alpha = -1;
					scene.agentFollow = agent.name;
					scene.agentEntityFollow = this;
					this.SpriteFollow.alpha = 1;

					updateResumeAgentFollow(this);
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

	var e = window.event || e; // old IE support
	var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));
  	var x = e.clientX;
  	var y = e.clientY;
  	var isZoomIn = delta > 0;
  	var direction = isZoomIn ? 1 : -1;
	var factor = (1 + direction * 0.1);

	cameraMapEditor.zoom *= factor;

	cameraMapEditor.newMap.scale.x *= factor;
	cameraMapEditor.newMap.scale.y *= factor;

	for (i = 0; i < listAgentEditor.length; i++) {
		listAgentEditor[i].scale.x *= factor;
    	listAgentEditor[i].scale.y *= factor;
    	listAgentEditor[i].SpritePercept.scale.x *= factor;
        listAgentEditor[i].SpritePercept.scale.y *= factor;
        listAgentEditor[i].SpriteFollow.scale.x *= factor;
        listAgentEditor[i].SpriteFollow.scale.y *= factor;

		listAgentEditor[i].position.x *= factor;
		listAgentEditor[i].position.y *= factor;
    	listAgentEditor[i].SpritePercept.position.x *= factor;
        listAgentEditor[i].SpritePercept.position.y *= factor;
        listAgentEditor[i].SpriteFollow.position.x *= factor;
        listAgentEditor[i].SpriteFollow.position.y *= factor;
	}
};

function changeCursorAgentMapEditor() {



}

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
    else if(buttonMoveAgentME) {
    	contener.style.cursor = "grab";
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



	stg.mousedown = function (moveData) {
		var pos = moveData.global;
		prevX = pos.x;
		prevY = pos.y;

		isDragging = true;

		if(buttonAddAgentME) {

			if(checkPossibleCreateAgent(tx - vx, ty - vy, nameAgentSelected)) {

				if(nameTeamSelected == "mother") {
					if(nameAgentSelected == "WarFood") {
						if(counterAgent.food < numberMaxFood) {
							if((tx - vx) > (10 + mapVector) * cameraMapEditor.zoom && (tx - vx) < (mapWigth - (10 - mapVector))* cameraMapEditor.zoom && (ty - vy) > (10 + mapVector) * cameraMapEditor.zoom && (ty - vy) < (mapHeigth - (10 - mapVector)) * cameraMapEditor.zoom ) {
								createAgentMapEditor(cameraMapEditor, nameTeamSelected, nameAgentSelected, tx - vx, ty-vy);
							}
						}
						else {
							console.log("Impossible create food because number max is already max")
						}
					}
					else {
						console.log("Mother can't create : " + nameAgentSelected);
					}
				}
				else {
					if(nameAgentSelected != "WarFood") {

						if(nameTeamSelected == "red") {
							if(counterAgentRed < numberMaxAgentByTeamUser) {
								if((tx - vx) > (10 + mapVector) * cameraMapEditor.zoom && (tx - vx) < (mapWigth - (10 - mapVector))* cameraMapEditor.zoom && (ty - vy) > (10 + mapVector) * cameraMapEditor.zoom && (ty - vy) < (mapHeigth - (10 - mapVector)) * cameraMapEditor.zoom ) {
									createAgentMapEditor(cameraMapEditor, nameTeamSelected, nameAgentSelected, tx - vx, ty-vy);
								}
							}
							else {

								console.log("number max for red team is ok");
							}
						}
						else if (nameTeamSelected == "blue") {
							if(counterAgentBlue < numberMaxAgentByTeamUser) {
								if((tx - vx) > (10 + mapVector) * cameraMapEditor.zoom && (tx - vx) < (mapWigth - (10 - mapVector))* cameraMapEditor.zoom && (ty - vy) > (10 + mapVector) * cameraMapEditor.zoom && (ty - vy) < (mapHeigth - (10 - mapVector)) * cameraMapEditor.zoom ) {
									createAgentMapEditor(cameraMapEditor, nameTeamSelected, nameAgentSelected, tx - vx, ty-vy);
								}
							}
							else {
								console.log("number max for blue team is ok");
							}
						}
					}
					else {
						console.log("Team can't create food");
					}
				}

			}
			else {
				console.log("Impossible create agent to ")
			}
		}
	};

	stg.mouseup = function (moveDate) {
		isDragging = false;

		if(cameraMapEditor.agentInMove) {
			cameraMapEditor.agentInMove = false;

			if(!checkPossibleCreateAgent(tx - vx, ty - vy, nameAgentSelected)) {
				cameraMapEditor.agentMove.position.x = oldPositionAgentMoveX;
				cameraMapEditor.agentMove.position.y = oldPositionAgentMoveY;
				cameraMapEditor.agentMove.SpritePercept.position.x = cameraMapEditor.agentMove.position.x;
               	cameraMapEditor.agentMove.SpritePercept.position.y = cameraMapEditor.agentMove.position.y;
                cameraMapEditor.agentMove.SpriteFollow.position.x = cameraMapEditor.agentMove.position.x;
               	cameraMapEditor.agentMove.SpriteFollow.position.y = cameraMapEditor.agentMove.position.y;
			}

			cameraMapEditor.agentMove.defaultCursor = "default";
			cameraMapEditor.agentMove = null;
		}

	};


	stg.mousemove = function (moveData) {

	    var pos = moveData.global;
	    dx = pos.x - prevX;
        dy = pos.y - prevY;

        tx = pos.x;
        ty = pos.y;

        if(cameraMapEditor.agentInMove)
        	isDragging = false;

		if (!isDragging) {
			if(cameraMapEditor.agentInMove) {
				if((tx - vx) > (10 + mapVector) * cameraMapEditor.zoom && (tx - vx) < (mapWigth - (10 - mapVector))* cameraMapEditor.zoom && (ty - vy) > (10 + mapVector) * cameraMapEditor.zoom && (ty - vy) < (mapHeigth - (10 - mapVector)) * cameraMapEditor.zoom ) {
					cameraMapEditor.agentMove.position.x = tx - vx;
					cameraMapEditor.agentMove.position.y = ty - vy;
					cameraMapEditor.agentMove.SpritePercept.position.x = cameraMapEditor.agentMove.position.x;
					cameraMapEditor.agentMove.SpritePercept.position.y = cameraMapEditor.agentMove.position.y;
					cameraMapEditor.agentMove.SpriteFollow.position.x = cameraMapEditor.agentMove.position.x;
					cameraMapEditor.agentMove.SpriteFollow.position.y = cameraMapEditor.agentMove.position.y;
				}
				return;
			}
			else {
				return;
			}
		}

		cam.position.x += dx;
		cam.position.y += dy;

		vx += dx;
		vy += dy;

		prevX = pos.x;
		prevY = pos.y;
	};
}

function getViewAngle(agent) {
	if(agent.type == "WarExplorer") {
    	return 180;
    }
    else if(agent.type == "WarEngineer") {
    	return 150;
    }
    else if(agent.type == "WarRocketLauncher") {
    	return 120;
    }
    else if(agent.type == "WarKamikaze") {
    	return 150;
    }
    else if(agent.type == "WarTurret") {
    	return 180;
    }
    else if(agent.type == "WarBase") {
    	return 360;
    }
    else {
    	return 0;
    }
}

function getViewDistance(agent) {
	if(agent.type == "WarExplorer") {
    	return 200;
    }
    else if(agent.type == "WarEngineer") {
    	return 120;
    }
    else if(agent.type == "WarRocketLauncher") {
    	return 80;
    }
    else if(agent.type == "WarKamikaze") {
    	return 80;
    }
    else if(agent.type == "WarTurret") {
    	return 200;
    }
    else if(agent.type == "WarBase") {
    	return 360;
    }
    else {
    	return 0;
    }
}

function getLifeMaxAgent(agent) {
	if(agent.type == "WarExplorer") {
    	return 3000;
    }
    else if(agent.type == "WarEngineer") {
    	return 3000;
    }
    else if(agent.type == "WarRocketLauncher") {
    	return 8000;
    }
    else if(agent.type == "WarKamikaze") {
    	return 3000;
    }
    else if(agent.type == "WarTurret") {
    	return 4000;
    }
    else if(agent.type == "WarBase") {
    	return 12000;
    }
    else if(agent.type == "Wall") {
        return 15000;
    }
    else {
    	return 1;
    }
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
			counterAgentRed += 1;
        	document.getElementById('numberOfExplorerRed').innerHTML = counterAgent.redExplorer;
        	document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
			return explorerRed;
		}
		else if (typeColor == 2){
			counterAgent.blueExplorer += 1;
			counterAgentBlue += 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfExplorerBlue').innerHTML = counterAgent.blueExplorer;
			return explorerBlue;
		}
	}
	else if(typeAgent == "WarEngineer") {
		if(typeColor == 1) {
			counterAgent.redEngineer += 1;
			counterAgentRed += 1;
			document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfEngineerRed').innerHTML = counterAgent.redEngineer;
			return engineerRed;
		}
		else if (typeColor == 2){
			counterAgent.blueEngineer += 1;
			counterAgentBlue += 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfEngineerBlue').innerHTML = counterAgent.blueEngineer;
			return engineerBlue;
		}
	}
	else if(typeAgent == "WarRocketLauncher") {
		if(typeColor == 1) {
			counterAgent.redRocketLauncher += 1;
			counterAgentRed += 1;
			document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfRocketLauncherRed').innerHTML = counterAgent.redRocketLauncher;
			return rocketLauncherRed;
		}
		else if (typeColor == 2){
			counterAgent.blueRocketLauncher += 1;
			counterAgentBlue += 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfRocketLauncherBlue').innerHTML = counterAgent.blueRocketLauncher;
			return rocketLauncherBlue;
		}
	}
	else if(typeAgent == "WarKamikaze") {
		if(typeColor == 1) {
			counterAgent.redKamikaze += 1;
			counterAgentRed += 1;
			document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfKamikazeRed').innerHTML = counterAgent.redKamikaze;
			return kamikazeRed;
		}
		else if (typeColor == 2){
			counterAgent.blueKamikaze += 1;
			counterAgentBlue += 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfKamikazeBlue').innerHTML = counterAgent.blueKamikaze;
			return kamikazeBlue;
		}
	}
	else if(typeAgent == "WarTurret") {
		if(typeColor == 1) {
			counterAgent.redTurret += 1;
			counterAgentRed += 1;
			document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
			document.getElementById('numberOfTurretRed').innerHTML = counterAgent.redTurret;
			return turretRed;
		}
		else if (typeColor == 2){
			counterAgent.blueTurret += 1;
			counterAgentBlue += 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
			document.getElementById('numberOfTurretBlue').innerHTML = counterAgent.blueTurret;
			return turretBlue;
		}
	}
	else if(typeAgent == "WarBase") {
		if(typeColor == 1) {
			counterAgent.redBase += 1;
			counterAgentRed += 1;
			document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
        	document.getElementById('numberOfBaseRed').innerHTML = counterAgent.redBase;
			return baseRed;
		}
		else if (typeColor == 2){
			counterAgent.blueBase += 1;
			counterAgentBlue += 1;
			document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
        	document.getElementById('numberOfBaseBlue').innerHTML = counterAgent.blueBase;
			return baseBlue;
		}
	}
	else if(typeAgent == "Wall") {
		if(typeColor == 1) {
    		counterAgent.redWall += 1;
    		counterAgentRed += 1;
    		document.getElementById('totalRedTeamAgent').innerHTML = counterAgentRed + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfWallRed').innerHTML = counterAgent.redWall;
    		return wallRed;
    	}
    	else if (typeColor == 2){
    		counterAgent.blueWall += 1;
    		counterAgentBlue += 1;
    		document.getElementById('totalBlueTeamAgent').innerHTML = counterAgentBlue + " / " + numberMaxAgentByTeamUser;
            document.getElementById('numberOfWallBlue').innerHTML = counterAgent.blueWall;
    		return wallBlue;
    	}
	}
	else if(typeAgent == "WarRocket") {
		return rocket;
	}
	else if(typeAgent == "WarBomb") {
		return bomb;
	}
	else if(typeAgent == "WarFood" && typeColor == 0) {
		counterAgent.food += 1;
		document.getElementById('numberOfFoodConsoleMap').innerHTML = counterAgent.food + " / " + numberMaxFood;
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

	counterAgentBlue = 0;
	counterAgentRed = 0;

    counterAgent.food = 0;
    counterAgent.redBase = 0;
    counterAgent.blueBase = 0;
    counterAgent.redExplorer = 0;
    counterAgent.blueExplorer = 0;
    counterAgent.redKamikaze = 0;
    counterAgent.blueKamikaze = 0;
    counterAgent.redRocketLauncher = 0;
    counterAgent.blueRocketLauncher = 0;
    counterAgent.redTurret = 0;
    counterAgent.blueTurret = 0;
    counterAgent.redEngineer = 0;
    counterAgent.blueEngineer = 0;
    counterAgent.redWall = 0;
    counterAgent.blueWall = 0;

	while(listAgentEditor.length > 0) {
        listAgentEditor.pop();
    }

    cameraMapEditor.follow = false;
    cameraMapEditor.agentFollow = -1;
    cameraMapEditor.agentEntityFollow = null;

    resetResumeAgentFollow();
    resetResumeCounterAgent();
}

function checkPossibleCreateAgent(posX, posY, agentType) {

	var minDistance = 13;

	var cont = true;
	var i = 0;
	while (i < listAgentEditor.length && cont) {
		if(agentType == "Wall")
    		minDistance = 65;
    	else if (agentType == "WarFood")
    		minDistance = 5;
    	else
    		minDistance = 13;

		if(listAgentEditor[i].name != cameraMapEditor.agentFollow) {
			var x = (posX - listAgentEditor[i].position.x) * (posX - listAgentEditor[i].position.x);
			var y = (posY - listAgentEditor[i].position.y) * (posY - listAgentEditor[i].position.y);
			var dist = Math.sqrt(x + y);

			if(dist < minDistance * cameraMapEditor.zoom)
				return false;
		}
		else {
			if(buttonAddAgentME) {
				var x = (posX - listAgentEditor[i].position.x) * (posX - listAgentEditor[i].position.x);
            	var y = (posY - listAgentEditor[i].position.y) * (posY - listAgentEditor[i].position.y);
            	var dist = Math.sqrt(x + y);

            	if(dist < minDistance * cameraMapEditor.zoom)
            		return false;
			}
		}


		i++;
	}

    return true;
}

function modeMapChange() {

}

function sendListAgent() {
	var agents = [];

	for (i = 0; i < listAgentEditor.length; i++) {
    	agents.push({
    		"name"     : listAgentEditor[i].name,
    		"x"        : listAgentEditor[i].position.x,
    		"y"        : listAgentEditor[i].position.y,
    		"angle"    : listAgentEditor[i].angle,
    		"teamName" : listAgentEditor[i].teamName,
    		"type"     : listAgentEditor[i].type,
    		"life"     : getLifeMaxAgent(listAgentEditor[i]),
    	});
    }

    return agents;
}

function sendMessageForSaveTrainingConfiguration() {

	var map = {
		"id" : "tutu",
		"mode" : "",
		"type" : "closed",
		"timer" : ""
	};

	var agents = [];
	for (i = 0; i < listAgentEditor.length; i++) {
		agents.push({
			"name"     : listAgentEditor[i].name,
			"x"        : listAgentEditor[i].position.x,
			"y"        : listAgentEditor[i].position.y,
			"angle"    : listAgentEditor[i].angle,
			"teamName" : listAgentEditor[i].teamName,
			"type"     : listAgentEditor[i].type,
			"life"     : getLifeMaxAgent(listAgentEditor[i]),
		});
	}

	var contentTrainingConfiguration = {
		"map" : map,
		"agents" : agents
	};

	var trainingConfiguration = {
		"header" : "trainingConfiguration",
		"content" : contentTrainingConfiguration
	};

	return trainingConfiguration;
}

function saveTrainingConfiguration() {

	var listAgentForSave = new Array();

	for (i = 0; i < listAgentEditor.length; i++) {
		var agent = {
			name : listAgentEditor[i].name,
			x : listAgentEditor[i].position.x,
			y : listAgentEditor[i].position.y,
			angle : listAgentEditor[i].angle,
			teamName : listAgentEditor[i].teamName,
			type : listAgentEditor[i].type,
			life : getLifeMaxAgent(listAgentEditor[i])
		};

		listAgentForSave.push(agent);
	}

	return listAgentForSave;
}

function incrementAngleAgentFollow() {
	if(cameraMapEditor.follow) {
		cameraMapEditor.agentEntityFollow.angle += 1;
		if(cameraMapEditor.agentEntityFollow.angle >= 361)
			cameraMapEditor.agentEntityFollow.angle = 0;

        cameraMapEditor.agentEntityFollow.rotation = Math.PI * (cameraMapEditor.agentEntityFollow.angle / 180);

        if(cameraMapEditor.agentEntityFollow.type == "Wall")
        	cameraMapEditor.agentEntityFollow.SpriteFollow.rotation = cameraMapEditor.agentEntityFollow.rotation;

       	changePositionPercept(cameraMapEditor.agentEntityFollow);
        document.getElementById('angleOfAgentFollow').innerHTML = cameraMapEditor.agentEntityFollow.angle;
	}
}

function decrementAngleAgentFollow() {
	if(cameraMapEditor.follow) {
		cameraMapEditor.agentEntityFollow.angle -= 1;

		if(cameraMapEditor.agentEntityFollow.angle <= -1)
        	cameraMapEditor.agentEntityFollow.angle = 360;

        cameraMapEditor.agentEntityFollow.rotation = Math.PI * (cameraMapEditor.agentEntityFollow.angle / 180);

        if(cameraMapEditor.agentEntityFollow.type == "Wall")
        	cameraMapEditor.agentEntityFollow.SpriteFollow.rotation = cameraMapEditor.agentEntityFollow.rotation;

		changePositionPercept(cameraMapEditor.agentEntityFollow);
		document.getElementById('angleOfAgentFollow').innerHTML = cameraMapEditor.agentEntityFollow.angle;
	}
}
