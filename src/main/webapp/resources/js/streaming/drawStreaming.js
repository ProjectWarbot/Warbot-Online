var contener = document.getElementById('stream');
var colorStreamOff = 0x000000;
var stage = new PIXI.Stage(colorStreamOff);
var renderer = new PIXI.autoDetectRenderer(0 , 0);
var camera = new PIXI.DisplayObjectContainer();
var hud = new PIXI.DisplayObjectContainer();
var agentTab = new Array();
var buttonTab = new Array();
var TeamAll = new Array();
var nameTeamRed;
var nameTeamBlue;
var playuttonUI;
var partyInGame = false;
var partyStart = false;
var appM;
var idP;

requestAnimationFrame( animate );
initStreaming();
cameraMove(stage, camera);
addWheelLister();

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

/*
* Traitement des messages JSON recu
*/
function analyseMessageServer(message) {
	if(message.header == "init")
		messageServerInit(message.content);
	else if (message.header == "agent")
		messageServerAgent(message.content);
	else if (message.header == "synchro")
		messageServerSynchro(message.content);
	else if (message.header == "end")
		messageServerEnd(message.content);
	else console.log("bug message");
}


/**
* Traitement du message "init" JSON recu
*/
function messageServerInit(message) {
	initHUD();

	var team1 = message.teams[0];
	var team2 = message.teams[1];
	var team3 = message.teams[2];

	TeamAll.push(team1);
	TeamAll.push(team2);
	TeamAll.push(team3);

	createMapJson();

	for (i = 0; i < message.agents.length; i++) {
		createAgentJson(camera, agentTab, message.agents[i], TeamAll);
	}

	partyStart = false;
	partyInGame = true;
}

/**
* Traitement du message "agent" JSON recu
*/
function messageServerAgent(message) {

	if(typeof(message.state) != "undefined" && (message.state == 1)) {
		createAgentJson(camera, agentTab, message, TeamAll);
	}
	else {

		var index = -1;
		//var i = 0;
		//var contAgentModif = true;

		for(i = 0; i < agentTab.length; i++) {
		//while(i < agentTab.length && contAgentModif) {
			if(agentTab[i].name == message.name) {
				if(typeof(message.state) != "undefined") {
					if(message.state == -1) {
						index = i;
						//contAgentModif = false;
					}
				}
				else {
					agentChangeValue(agentTab[i], message);

				}
			}
			//i++;
		}

		if(index != -1) {
			updateDataAgentMap(agentTab[index]);
			camera.removeChild(agentTab[index].SpriteLife);
			camera.removeChild(agentTab[index].SpritePercept);
			camera.removeChild(agentTab[index]);
			agentTab.splice(index, 1);
		}
	}
}

/**
* Traitement du message "synchro" JSON recu
*/
function messageServerSynchro(message) {
	for(i = 0; i < message.length; i++) {
		if(typeof(message.state) != "undefined" && (message.state == 1)) {
			createAgentJson(camera, agentTab, message, TeamAll);
		}
		else {

			var indexTab = new Array();

			for(i = 0; i < agentTab.length; i++) {
				if(agentTab[i].name == message.name) {
					if(typeof(message.state) != "undefined") {
						if(message.state == -1) {

							indexTab.add(i);
						}
					}
					else {
						//agentSynchroValue(message, index);
					}
				}
			}

			if(indexTab.length != 0) {
				for(j = 0; j < indexTab.length; j++) {
					updateDataAgentMap(agentTab[indexTab[j]]);
					camera.removeChild(agentTab[indexTab[j]].SpriteLife);
					camera.removeChild(agentTab[indexTab[j]].SpritePercept);
					camera.removeChild(agentTab[indexTab[j]]);
					agentTab.splice(indexTab[j], 1);
				}
			}
		}
	}
}

/**
* Traitement du message "end" JSON recu
*/


/**
* Création de la map
**/
function createMapJson() {
	console.log("Create Map")

	var mapWarbot = new PIXI.Sprite(map);

	mapWarbot.position.x = -14;
	mapWarbot.position.y = -14;
	mapWarbot.anchor.x = 0;
	mapWarbot.anchor.y = 0;
	mapWarbot.alpha = 1;

	camera.map = mapWarbot;
	camera.addChild(mapWarbot);

	for(i = 0; i < 3; i++) {
		if(TeamAll[i].color.r == 149 && TeamAll[i].color.g == 149 && TeamAll[i].color.b == 149) {
			var teamName = new PIXI.Text("RED : "+TeamAll[i].name, {font:"25px Arial", fill:"red"});
			teamName.position.x = 30;
			teamName.position.y = -50;
			teamName.alpha = 1;
			camera.teamTextNameRed = teamName;
			camera.addChild(teamName);
			document.getElementById('nameRedTeamConsoleMap').innerHTML = TeamAll[i].name;
			nameTeamRed = TeamAll[i].name;
		}
		else if(TeamAll[i].color.r == 255 && TeamAll[i].color.g == 98 && TeamAll[i].color.b == 255) {
			var teamName = new PIXI.Text("BLUE : "+TeamAll[i].name, {font:"25px Arial", fill:"blue"});
			teamName.position.x = 800;
			teamName.position.y = -50;
			teamName.alpha = 1;
			camera.teamTextNameBlue = teamName;
			camera.addChild(teamName);
			document.getElementById('nameBlueTeamConsoleMap').innerHTML = TeamAll[i].name;
			nameTeamBlue = TeamAll[i].name;
		}
		else {
			//console.log("Bug team");
		}
	}

}

/**
* Création d'un agent
**/
function createAgentJson(scene, tab, json, teams) {
	//console.log("create agent");

	var agent= null;

	var team = getTeamOfAgent(teams, json);

	var colorTeam = team.color;

	if(colorTeam.r == 149 && colorTeam.g == 149 && colorTeam.b == 149) {
        // RED
        agent = new PIXI.Sprite(getSpriteAgent(json.type, 1));
        agent.teamType = 1;
    }
    else if (colorTeam.r == 255 && colorTeam.g == 98 && colorTeam.b == 255) {
        // BLUE
        agent = new PIXI.Sprite(getSpriteAgent(json.type, 2));
        agent.teamType = 2;
    }
    else if (colorTeam.r == 0 && colorTeam.g == 255 && colorTeam.b == 0) {
    	agent = new PIXI.Sprite(getSpriteAgent(json.type, 0));
        agent.teamType = 0;
    }
    else {
        console.log("Color non dispo");
    }

    agent.anchor.x = 0.5;
    agent.anchor.y = 0.5;

	agent.name = json.name;
	agent.teamName = team.name;
	agent.type = json.type;
	agent.position.x = json.x * camera.zoom;
	agent.position.y = json.y * camera.zoom;
	agent.angle = json.angle;
	agent.rotation = Math.PI * (agent.angle / 180);
	agent.scale.x = 0.5 * camera.zoom;
	agent.scale.y = 0.5 * camera.zoom;

    if (typeof(json.lifeP) != "undefined") {
    	agent.lifeP = json.lifeP;
    }
    else {
    	agent.lifeP = 100;
    }

	agent.colorDebug = getColorDebug(json);
	agent.messageDebug = getMessageDebug(json);

	agent.debug = new PIXI.Text(agent.messageDebug, {font:"12px Arial", fill:agent.colorDebug});
	agent.debug.position.x = agent.position.x;
	agent.debug.position.y = agent.position.y;
	agent.debug.anchor.x = -0.1;
    agent.debug.anchor.y = 0.5;

    var indTab = 0;
    var cont = true;

    while(indTab < buttonTab.length && cont) {
    	if(buttonTab[indTab].type == 2 && buttonTab[indTab].isdown) {
    		agent.debug.alpha = 1;
    		cont = false;
    	}
    	indTab++;
    }

    if(cont || agent.type == "WarFood" || agent.type =="Wall") {
    	agent.debug.alpha = -1;
    }

	agent.interactive = true;
	agent.buttonMode = true;
	agent.defaultCursor = "pointer";

	var life = new PIXI.Sprite(getSpriteLife(agent.lifeP));
	life.anchor.x = 0.5;
	life.anchor.y = 0.5;
	life.position.x = agent.position.x;
	life.position.y = agent.position.y - Math.sqrt(agent.height * agent.height) * agent.scale.y;
	life.scale.x = 0.5 * camera.zoom;
	life.scale.y = 0.5 * camera.zoom;

	indTab = 0;
	cont = true;

	while(indTab < buttonTab.length && cont) {
		if(buttonTab[indTab].type == 1 && buttonTab[indTab].isdown) {
			life.alpha = 1;
			cont = false;
		}
		indTab++;
	}

	if(cont || agent.type == "WarFood" || agent.type == "WarRocket") {
		life.alpha = -1;
	}

	agent.SpriteLife = life;

	var percept = new PIXI.Sprite(getSpritePercept(agent));
	percept.position.x = agent.position.x;
	percept.position.y = agent.position.y;
	percept.scale.x = 0.5 * camera.zoom;
	percept.scale.y = 0.5 * camera.zoom;

	indTab = 0;
	cont = true;

	while(indTab < buttonTab.length && cont) {
		if(buttonTab[indTab].type == 3 && buttonTab[indTab].isdown) {
			percept.alpha = 1;
			cont = false;
		}
		indTab++;
	}

	if(cont) {
		percept.alpha = -1;
	}

	agent.SpritePercept = percept;
	changeAnchorPercept(agent);

	changePositionPercept(agent);

	tab.push(agent);

	if(agentTab.length > 0) {
		agent.scale.x = agentTab[0].scale.x;
		agent.scale.y = agentTab[0].scale.y;
		agent.SpriteLife.scale.x = agentTab[0].SpriteLife.scale.x;
		agent.SpriteLife.scale.y = agentTab[0].SpriteLife.scale.y;
		agent.SpriteLife.position.y = agent.position.y - Math.sqrt(agent.height * agentTab[0].scale.x * agent.height * agentTab[0].scale.x);
		agent.SpritePercept.scale.x = agentTab[0].SpritePercept.scale.x;
		agent.SpritePercept.scale.y = agentTab[0].SpritePercept.scale.y;
	}

	addSpriteOfAgentToCamera(agent);
	addSpriteOfAgentToCamera(life);
	addSpriteOfAgentToCamera(percept);
	addSpriteOfAgentToCamera(agent.debug);

	agent.mousedown = function(data) {

//		alert(scene.follow + scene.agentFollow );

   		if (scene.follow) {
   			if(scene.agentFollow == this.name) {
				scene.follow = false;
				scene.agentFollow = -1;
				scene.agentEntityFollow = null;
				document.getElementById('nameOfAgentFollow').innerHTML = "aucun";
				document.getElementById('teamOfAgentFollow').innerHTML = "aucun";
				document.getElementById('typeOfAgentFollow').innerHTML = "aucun";
				document.getElementById('lifeOfAgentFollow').innerHTML = "0 %";
				document.getElementById('debugMessageOfAgentFollow').innerHTML = "aucun";
				document.getElementById('angleOfAgentFollow').innerHTML = "0";
				}

				else {
				   			scene.follow = true;
                   			scene.agentFollow = agent.name;
                   			scene.agentEntityFollow = this;
                   			scene.position.x += (renderer.width / 2) - this.position.x;
                   			scene.position.y += (renderer.height / 2) - this.position.y;
                   			document.getElementById('nameOfAgentFollow').innerHTML = this.name;
                   			document.getElementById('teamOfAgentFollow').innerHTML = this.teamName;
                   			document.getElementById('typeOfAgentFollow').innerHTML = this.type;
                   			document.getElementById('lifeOfAgentFollow').innerHTML = this.lifeP + " %";
                   			document.getElementById('debugMessageOfAgentFollow').innerHTML = this.messageDebug;
                   			document.getElementById('angleOfAgentFollow').innerHTML = this.angle;
				}
   		}
   		else {
   			scene.follow = true;
   			scene.agentFollow = agent.name;
   			scene.agentEntityFollow = this;
   			scene.position.x += (renderer.width / 2) - this.position.x;
   			scene.position.y += (renderer.height / 2) - this.position.y;
   			document.getElementById('nameOfAgentFollow').innerHTML = this.name;
   			document.getElementById('teamOfAgentFollow').innerHTML = this.teamName;
   			document.getElementById('typeOfAgentFollow').innerHTML = this.type;
   			document.getElementById('lifeOfAgentFollow').innerHTML = this.lifeP + " %";
   			document.getElementById('debugMessageOfAgentFollow').innerHTML = this.messageDebug;
   			document.getElementById('angleOfAgentFollow').innerHTML = this.angle;
   		}
    };

}


function getColorDebug(json) {
	if (typeof(json.colorDebug) != "undefined")
		return rgb2hex2(json.colorDebug.r, json.colorDebug.g, json.colorDebug.b);
	else
		return rgb2hex2(0, 0, 0);
}

function getMessageDebug(json) {
	if (typeof(json.messageDebug) != "undefined")
    	return json.messageDebug;
    else
    	return "";
}

function getTeamOfAgent(teams, json) {
	if(teams[0].name == json.team) {
    	return teams[0];
    }
    else if (teams[1].name == json.team) {
    	return teams[1];
    }
    else if (teams[2].name == json.team) {
    	return teams[2];
    }
    else {
    	console.log("Bug team");
    	return null;
    }
}

function addSpriteOfAgentToCamera(sprite) {
	camera.addChild(sprite);
}

function agentChangeValue(agent, json) {
	if (typeof(json.x) != "undefined")
	{
		agent.position.x = json.x * camera.zoom;
		agent.SpriteLife.position.x = agent.position.x;
		changePositionPercept(agent);
		agent.debug.position.x = agent.position.x;
	}

	if (typeof(json.y) != "undefined")
	{
		agent.position.y = json.y * camera.zoom;
		agent.SpriteLife.position.y = agentTab[i].position.y - Math.sqrt(agentTab[i].height * agentTab[i].scale.x * agentTab[i].height * agentTab[i].scale.x);
		changePositionPercept(agent);
		agent.debug.position.y = agent.position.y;
	}

	if (typeof(json.lifeP) != "undefined")
	{
		agent.lifeP = json.lifeP;
		agent.SpriteLife.setTexture(getSpriteLife(agent.lifeP));
	}

	if (typeof(json.angle) != "undefined")
	{
		agent.angle = json.angle;
		agent.rotation = Math.PI * (agent.angle / 180);
		changePositionPercept(agent);
	}

	if(typeof(json.colorDebug) != "undefined")
	{
		agent.colorDebug = json.colorDebug;
		agent.debug.setStyle({fill:agent.colorDebug});
	}

	if(typeof(json.messageDebug) != "undefined")
    {
		agent.messageDebug = json.messageDebug;
		agent.debug.setText(agent.messageDebug);
    }

	if(camera.agentFollow != null) {
    	if(agent.name == camera.agentEntityFollow.name) {
       		document.getElementById('lifeOfAgentFollow').innerHTML = agent.lifeP + " %";
        	document.getElementById('angleOfAgentFollow').innerHTML = agent.angle;
       		document.getElementById('debugMessageOfAgentFollow').innerHTML = agent.messageDebug;
    	}
    }

}

function addButton(scene, form, formDown, formTrans, cX, cY, tab, type) {

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
        		else if (type == 4){
                	// nothing
                }
        	}
   		}
   		else {
   			this.isdown = true;
        	this.setTexture(formDown);
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
        		else if (type == 4){
                	stopGame();
                }
        	}
   		}
    };

	scene.addChild(button);
}

function rgb2hex(r, g, b){
	return '0x'+('0'+r.toString(16)).slice(-2)+('0'+g.toString(16)).slice(-2)+('0'+b.toString(16)).slice(-2);
}

function rgb2hex2(r, g, b){
	return '#'+('0'+r.toString(16)).slice(-2)+('0'+g.toString(16)).slice(-2)+('0'+b.toString(16)).slice(-2);
}

function animate() {

    requestAnimationFrame( animate );

    renderer.resize(contener.offsetWidth-1, contener.offsetHeight-1);

    var coordCenterX = contener.offsetWidth-1 / 2;
    var coordCenterY = contener.offsetHeight-1 / 2;

	hud.gChargement.position.x = coordCenterX / 2;
	hud.gChargement.position.y = coordCenterY / 2;

	hud.playBut.position.x = coordCenterX / 2;
    hud.playBut.position.y = coordCenterY / 2;

    hud.playBut.scale.x = 0.8;
    hud.playBut.scale.y = 0.8;

	if(partyStart) {
		hud.gChargement.rotation += 0.05;
		hud.gChargement.alpha = 1;
		hud.playBut.alpha = -1;
		hud.playBut.interactive = false;
	}
	else {
		hud.gChargement.alpha = -1;
		if(!partyInGame) {
			hud.playBut.alpha = 1;
			hud.playBut.interactive = true;
		}
	}

	if(camera.follow) {
		for (i = 0; i < agentTab.length; i++) {
			if(camera.agentFollow == agentTab[i].name) {
   				camera.position.x = (renderer.width / 2) - agentTab[i].position.x;
   				camera.position.y = (renderer.height / 2) - agentTab[i].position.y;
			}
		}
	}

    renderer.render(stage);
}

function initHUD() {
	camera.position.x = 0;
	camera.position.y = 0;
	stage.setBackgroundColor(0x666666);
	addButton(hud, buttonLife, buttonLifeDown, buttonLifeTrans, 20, 20, buttonTab, 1);
	addButton(hud, buttonMessage, buttonMessageDown, buttonMessageTrans, 60, 20, buttonTab, 2);
	addButton(hud, buttonPercept, buttonPerceptDown, buttonPerceptTrans, 100, 20, buttonTab, 3);
	addButton(hud, buttonStop, buttonStopDown, buttonStopTrans, 20, 60, buttonTab, 4);
}

function initStreaming() {
	stage.interactive = true;
	renderer.view.style.display = "block";
    contener.appendChild(renderer.view);
    camera.follow = false;
    camera.agentFollow = null;
    camera.agentEntityFollow;
    camera.zoom = 1;
    stage.addChild(camera);
    stage.addChild(hud);

    var gifChargement = new PIXI.Sprite(chargementGif);
    gifChargement.position.x = 0;
    gifChargement.position.y = 0;
    gifChargement.alpha = 1;
    gifChargement.anchor.x = 0.5;
    gifChargement.anchor.y = 0.5;
    gifChargement.scale.x = 2;
    gifChargement.scale.y = 2;
    hud.gChargement = gifChargement;
    hud.addChild(gifChargement);

    playuttonUI = new PIXI.Sprite(playButton);
    playuttonUI.position.x = 0;
    playuttonUI.position.y = 0;
    playuttonUI.alpha = 1;
    playuttonUI.anchor.x = 0.5;
    playuttonUI.anchor.y = 0.5;
    playuttonUI.scale.x = 0.2;
    playuttonUI.scale.y = 0.2;

    playuttonUI.interactive = true;
    playuttonUI.buttonMode = true;
    playuttonUI.defaultCursor = "pointer";

    playuttonUI.mousedown = function(data) {
		if(!partyInGame) {
            appM.launchParty(idP,-1);
            partyInGame = true;
            partyStart = true;
        }
        else {

        }
    };

    hud.playBut = playuttonUI;
    hud.addChild(playuttonUI);
}

function cameraMove(stg, cam) {
var prevX;
var prevY;
var isDragging = false;
	stg.mousedown = function (moveData) {
		var pos = moveData.global;
		prevX = pos.x;
		prevY = pos.y;
		isDragging = true;
	};

	stg.mouseup = function (moveDate) {
		isDragging = false;
	};


	stg.mousemove = function (moveData) {
		if (!isDragging) {
			return;
		}

		var pos = moveData.global;
		var dx = pos.x - prevX;
		var dy = pos.y - prevY;

		cam.position.x += dx;
		cam.position.y += dy;

		prevX = pos.x;
		prevY = pos.y;
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

	//if(camera.zoom * factor <= 2 && camera.zoom * factor >= 0.32) {
		camera.zoom *= factor;
		camera.map.scale.x *= factor;
		camera.map.scale.y *= factor;
		camera.map.position.x *= factor;
        camera.map.position.y *= factor;
        camera.teamTextNameBlue.scale.x *= factor;
        camera.teamTextNameBlue.scale.y *= factor;
        camera.teamTextNameBlue.position.x *= factor;
        camera.teamTextNameBlue.position.y *= factor;
        camera.teamTextNameRed.scale.x *= factor;
        camera.teamTextNameRed.scale.y *= factor;
        camera.teamTextNameRed.position.x *= factor;
        camera.teamTextNameRed.position.y *= factor;
	//}

	for (i = 0; i < agentTab.length; i++) {
		//if(agentTab[i].scale.x * factor <= 2 &&  agentTab[i].scale.y * factor <= 2 && agentTab[i].scale.x * factor >= 0.32 &&  agentTab[i].scale.y * factor >= 0.32) {
			agentTab[i].scale.x *= factor;
			agentTab[i].scale.y *= factor;
			agentTab[i].position.x *= factor;
            agentTab[i].position.y *= factor;
			agentTab[i].SpriteLife.scale.x *= factor;
			agentTab[i].SpriteLife.scale.y *= factor;
			agentTab[i].SpriteLife.position.x *= factor;
			agentTab[i].SpriteLife.position.y *= factor;
			agentTab[i].SpritePercept.scale.x *= factor;
			agentTab[i].SpritePercept.scale.y *= factor;
			agentTab[i].SpritePercept.position.x = agentTab[i].position.x;
			agentTab[i].SpritePercept.position.y = agentTab[i].position.y;
			changePositionPercept(agentTab[i]);
			agentTab[i].debug.scale.x *= factor;
			agentTab[i].debug.scale.y *= factor;
			agentTab[i].debug.position.x *= factor;
			agentTab[i].debug.position.y *= factor;

		//}
	}
};

function updateDataAgentMap(agent) {
			if(agent.type == "WarFood") {
				counterAgent.food -= 1;
                document.getElementById('numberOfFoodConsoleMap').innerHTML = counterAgent.food;
			}
			else if(agent.type == "WarBase") {
				if(agent.teamName == nameTeamRed) {
					counterAgent.redBase -= 1;
					document.getElementById('numberOfBaseRed').innerHTML = counterAgent.redBase;
				}
				else {
					counterAgent.blueBase += 1;
        			document.getElementById('numberOfBaseBlue').innerHTML = counterAgent.blueBase;
				}
			}
			else if(agent.type == "WarExplorer") {
				if(agent.teamName == nameTeamRed) {
					counterAgent.redExplorer -= 1;
        			document.getElementById('numberOfExplorerRed').innerHTML = counterAgent.redExplorer;
				}
				else {
					counterAgent.blueExplorer -= 1;
                    document.getElementById('numberOfExplorerBlue').innerHTML = counterAgent.blueExplorer;
				}
			}
			else if(agent.type == "WarEngineer") {
				if(agent.teamName == nameTeamRed) {
					counterAgent.redEngineer -= 1;
                    document.getElementById('numberOfEngineerRed').innerHTML = counterAgent.redEngineer;
				}
				else {
					counterAgent.blueEngineer -= 1;
            		document.getElementById('numberOfEngineerBlue').innerHTML = counterAgent.blueEngineer;
				}
			}
			else if(agent.type == "WarRocketLauncher") {
				if(agent.teamName == nameTeamRed) {
					counterAgent.redRocketLauncher -= 1;
                    document.getElementById('numberOfRocketLauncherRed').innerHTML = counterAgent.redRocketLauncher;
				}
				else {
					counterAgent.blueRocketLauncher -= 1;
                    document.getElementById('numberOfRocketLauncherBlue').innerHTML = counterAgent.blueRocketLauncher;
				}
			}
			else if(agent.type == "WarKamikaze") {
				if(agent.teamName == nameTeamRed) {
					counterAgent.redKamikaze -= 1;
            		document.getElementById('numberOfKamikazeRed').innerHTML = counterAgent.redKamikaze;
				}
				else {
					counterAgent.blueKamikaze -= 1;
                    document.getElementById('numberOfKamikazeBlue').innerHTML = counterAgent.blueKamikaze;
				}
			}
			else if(agent.type == "WarTurret") {
				if(agent.teamName == nameTeamRed) {
					counterAgent.redTurret -= 1;
        			document.getElementById('numberOfTurretRed').innerHTML = counterAgent.redTurret;
				}
				else {
					counterAgent.blueTurret -= 1;
        			document.getElementById('numberOfTurretBlue').innerHTML = counterAgent.blueTurret;
				}
			}
			else if(agent.type == "Wall") {
				if(agent.teamName == nameTeamRed) {
					counterAgent.redWall -= 1;
					document.getElementById('numberOfWallRed').innerHTML = counterAgent.redWall;
				}
				else {
					counterAgent.blueWall -= 1;
					document.getElementById('numberOfWallBlue').innerHTML = counterAgent.blueWall;
				}
			}
			else {
				//

			}
}


function getSpriteAgent(typeAgent, typeColor) {
	if(typeAgent == "WarExplorer") {
		if(typeColor == 1) {
			counterAgent.redExplorer += 1;
        	document.getElementById('numberOfExplorerRed').innerHTML = counterAgent.redExplorer;
			return explorerRed;
		}
		else {
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
		else {
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
		else {
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
		else {
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
		else {
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
		else {
			counterAgent.blueBase += 1;
			document.getElementById('numberOfBaseBlue').innerHTML = counterAgent.blueBase;
			return baseBlue;
		}
	}
	else if(typeAgent == "Wall") {
		if(typeColor == 1) {
			counterAgent.redWall += 1;
			document.getElementById('numberOfWallRed').innerHTML = counterAgent.redWall;
			return wallRed;
		}
		else {
			counterAgent.blueWall += 1;
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
	else if(typeAgent == "WarFood") {
        counterAgent.food += 1;
        document.getElementById('numberOfFoodConsoleMap').innerHTML = counterAgent.food;
		return food;
	}
	else {
		console.log("bug getSpriteAgent")
	}
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

function getSpriteLife(lifeP) {
	if(lifeP == 100) {
		return life001;
	}
	else if (lifeP <= 99 && lifeP > 92.5) {
		return life002;
	}
	else if (lifeP <= 92.5 && lifeP > 85) {
		return life003;
	}
	else if (lifeP <= 85 && lifeP > 77.5) {
		return life004;
	}
	else if (lifeP <= 77.5 && lifeP > 70) {
		return life005;
	}
	else if (lifeP <= 70 && lifeP > 62.5) {
		return life006;
	}
	else if (lifeP <= 62.5 && lifeP > 55) {
		return life007;
	}
	else if (lifeP <= 55 && lifeP > 47.5) {
		return life008;
	}
	else if (lifeP <= 47.5 && lifeP > 40) {
		return life009;
	}
	else if (lifeP <= 40 && lifeP > 32.5) {
		return life010;
	}
	else if (lifeP <= 32.5 && lifeP > 25) {
		return life011;
	}
	else if (lifeP <= 25 && lifeP > 17.5) {
		return life012;
	}
	else if (lifeP <= 17.5 && lifeP > 10) {
		return life013;
	}
	else if (lifeP <= 10 && lifeP > 1.5) {
		return life014;
	}
	else {

		return life015;
	}
}

function changeDebugMessage(agent, json) {


}


function messageServerEnd(message) {

    counterAgent = {
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

    document.getElementById('numberOfExplorerRed').innerHTML = 0;
    document.getElementById('numberOfExplorerBlue').innerHTML = 0;
    document.getElementById('numberOfEngineerRed').innerHTML = 0;
    document.getElementById('numberOfEngineerBlue').innerHTML = 0;
    document.getElementById('numberOfRocketLauncherRed').innerHTML = 0;
    document.getElementById('numberOfRocketLauncherBlue').innerHTML = 0;
    document.getElementById('numberOfKamikazeRed').innerHTML = 0;
    document.getElementById('numberOfKamikazeBlue').innerHTML = 0;
    document.getElementById('numberOfTurretRed').innerHTML = 0;
    document.getElementById('numberOfTurretBlue').innerHTML = 0;
    document.getElementById('numberOfBaseRed').innerHTML = 0;
    document.getElementById('numberOfBaseBlue').innerHTML = 0;
    document.getElementById('numberOfWallRed').innerHTML = 0;
    document.getElementById('numberOfWallBlue').innerHTML = 0;
    document.getElementById('numberOfFoodConsoleMap').innerHTML = 0;

    document.getElementById('nameRedTeamConsoleMap').innerHTML = "aucune";
    document.getElementById('nameBlueTeamConsoleMap').innerHTML = "aucune";

    document.getElementById('nameOfAgentFollow').innerHTML = "aucun";
    document.getElementById('teamOfAgentFollow').innerHTML = "aucun";
    document.getElementById('typeOfAgentFollow').innerHTML = "aucun";
    document.getElementById('lifeOfAgentFollow').innerHTML = "0 %";
    document.getElementById('debugMessageOfAgentFollow').innerHTML = "aucun";
    document.getElementById('angleOfAgentFollow').innerHTML = "0";

	stage.setBackgroundColor(colorStreamOff);

	for (i = 0; i < agentTab.length; i++) {
        camera.removeChild(agentTab[i].SpritePercept);
        camera.removeChild(agentTab[i].SpriteLife);
		camera.removeChild(agentTab[i].debug);
       	camera.removeChild(agentTab[i]);
	}

	for (j = 0; j < buttonTab.length; j++) {
		hud.removeChild(buttonTab[j]);
	}

	camera.removeChild(camera.map);
	camera.removeChild(camera.teamTextNameBlue);
	camera.removeChild(camera.teamTextNameRed);

	agentTab = new Array();
	buttonTab = new Array();
	TeamAll = new Array();

	camera.follow = false;
    camera.agentFollow = null;
    camera.agentEntityFollow;
    camera.zoom = 1;
    camera.position.x = 0;
    camera.position.y = 0;

    //stage.removeChild(camera);
    //stage.removeChild(hud);

    //camera = new PIXI.DisplayObjectContainer();
    //hud = new PIXI.DisplayObjectContainer();

    //stage.addChild(camera);
    //stage.addChild(hud);

    cameraMove(stage, camera);
    addWheelLister();

    partyInGame = false;
    partyStart = false;

    requestAnimationFrame( animate );
}

function chargeAppModel(appModel) {
	stage.appM = appModel;
}

function stopGame() {
	stage.appM.stop();
}