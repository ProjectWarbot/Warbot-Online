var contener = document.getElementById('stream');

var colorStreamOff = 0x000000;

var stage = new PIXI.Stage(colorStreamOff);
stage.interactive = true;

var renderer = new PIXI.autoDetectRenderer(200 , 200);
renderer.view.style.display = "block";
contener.appendChild(renderer.view);

requestAnimFrame( animate );

var camera = new PIXI.DisplayObjectContainer();
camera.follow = false;
camera.agentFollow = null;

camera.zoom = 1;


var hud = new PIXI.DisplayObjectContainer();

stage.addChild(camera);
stage.addChild(hud);

var agentTab = new Array();

var buttonTab = new Array();

var TeamAll = new Array();

cameraMove(stage, camera);
addWheelLister();

function getSpriteAgent(typeAgent, typeColor) {
	if(typeAgent == "WarExplorer") {
		if(typeColor == 1) {
			return explorerRed;
		}
		else {
			return explorerBlue;
		}
	}
	else if(typeAgent == "WarEngineer") {
		if(typeColor == 1) {
			return engineerRed;
		}
		else {
			return engineerBlue;
		}
	}
	else if(typeAgent == "WarRocketLauncher") {
		if(typeColor == 1) {
			return rocketLauncherRed;
		}
		else {
			return rocketLauncherBlue;
		}
	}
	else if(typeAgent == "WarKamikaze") {
		if(typeColor == 1) {
			return kamikazeRed;
		}
		else {
			return kamikazeBlue;
		}
	}
	else if(typeAgent == "WarTurret") {
		if(typeColor == 1) {
			return turretRed;
		}
		else {
			return turretBlue;
		}
	}
	else if(typeAgent == "WarBase") {
		if(typeColor == 1) {
			return baseRed;
		}
		else {
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
	else if(typeAgent == "WarFood") {
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
	//console.log("LIFE");
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
	else if (lifeP <= 10 && lifeP > 2.5) {
		return life014;
	}
	else {
		return life015;
	}
}

function changeDebugMessage(agent, json) {


}

function createAgentJson(scene, tab, json, teams) {
	//console.log("create agent");

	var agent= null;

	var team = null;
	for (i = 0; i < teams.length; i++) {
		if(teams[i].name == json.team)
			team = teams[i];
	}

	if(team == null)
		console.log("bug team name");
		console.log(team.name);

		var colorTeam = JSON.parse(team.color);
		console.log(colorTeam.r);
		console.log(colorTeam.g);
		console.log(colorTeam.b);

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
	agent.type = json.type;
	agent.position.x = json.x;
	agent.position.y = json.y;

	agent.teamName = team.name;

	agent.lifeP = json.lifeP;
	agent.angle = json.angle;
	agent.rotation = Math.PI * (agent.angle / 180);

	if (typeof(json.colorDebug) != "undefined")
		agent.colorDebug = rgb2hex2(json.colorDebug.r, json.colorDebug.g, json.colorDebug.b);
	else
		agent.colorDebug = rgb2hex2(0, 0, 0);

	if (typeof(json.messageDebug) != "undefined")
		agent.messageDebug = json.messageDebug;
	else
		agent.messageDebug = "";

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

    if(cont || agent.type == "WarFood") {
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

	indTab = 0;
	cont = true;

	while(indTab < buttonTab.length && cont) {
		if(buttonTab[indTab].type == 1 && buttonTab[indTab].isdown) {
			life.alpha = 1;
			cont = false;
		}
		indTab++;
	}

	if(cont || agent.type == "WarFood") {
		life.alpha = -1;
	}
	
	agent.SpriteLife = life;

	var percept = new PIXI.Sprite(getSpritePercept(agent));
	percept.position.x = agent.position.x;
	percept.position.y = agent.position.y;

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

	scene.addChild(agent);
	scene.addChild(life);
	scene.addChild(percept);
	scene.addChild(agent.debug);

	agent.mousedown = function(data) {

   		if (this.isdown) {
   			this.isdown = false;
   			scene.follow = false;
   			scene.agentFollow = 0;
   		}
   		else {
   			this.isdown = true;
   			scene.follow = true;
   			scene.agentFollow = agent.name;
   			scene.position.x += (renderer.width / 2) - this.position.x;
   			scene.position.y += (renderer.height / 2) - this.position.y;
   		}
    };
/*
	agent.movePerceptAgent = function (data) {
		agent.SpritePercept.rotation = agent.rotation - Math.PI / 4;
		agent.SpritePercept.position.x = Math.cos(agent.rotation / 180) + agent.position.x ;
		agent.SpritePercept.position.y = Math.sin(agent.rotation / 180) + agent.position.y ;
	}*/
}


function agentChangeValue(agent, json) {
	if (typeof(json.x) != "undefined")
	{
		agent.position.x = json.x;
		agent.SpriteLife.position.x = agent.position.x;
		changePositionPercept(agent);
		agent.debug.position.x = agent.position.x;
	}

	if (typeof(json.y) != "undefined")
	{
		agent.position.y = json.y;
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
		agent.debug.setStyle({font:"12px Arial", fill:agent.colorDebug});
	}

	if(typeof(json.messageDebug) != "undefined")
    {
		agent.messageDebug = json.messageDebug;
		agent.debug.setText(agent.messageDebug);
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
        	}
   		}
    };

	scene.addChild(button);
}

function analyseMessageServer(message) {

	console.log(message["header"]);
	console.log("Message");
	console.log(message);

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

function messageServerInit(message) {
	initHUD();

	// TODO 
	// createMapJson(message.init.environment)

	var team1 = message.teams[0];
	var team2 = message.teams[1];
	var team3 = message.teams[2];

	console.log(team1.name);
	console.log(team2.name);
	console.log(team3.name);

	TeamAll.push(team1);
	TeamAll.push(team2);
	TeamAll.push(team3);

	// création des agents de la partit 
	for (i = 0; i < message.agents.length; i++) {
		createAgentJson(camera, agentTab, message.agents[i], message.teams);

	}

}

function messageServerAgent(message) {

	if(typeof(message.state) != "undefined" && (message.state == 1)) {
		createAgentJson(camera, agentTab, message, TeamAll);

	}
	else {

		var index = -1;

		for(i = 0; i < agentTab.length; i++) {
			if(agentTab[i].name == message.name) {
				if(typeof(message.state) != "undefined") {
					if(message.state == -1) {

						index = i;
					}
				}
				else {
					agentChangeValue(agentTab[i], message);

				}
			}
		}

		if(index != -1) {
			camera.removeChild(agentTab[index].SpriteLife);
			camera.removeChild(agentTab[index].SpritePercept);
			camera.removeChild(agentTab[index]);
			agentTab.splice(index, 1);
		}
	}
}

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
						// TODO agentSynchroValue
					}
				}
			}

			if(indexTab.length != 0) {
				for(j = 0; j < indexTab.length; j++) {
					camera.removeChild(agentTab[indexTab[j]].SpriteLife);
					camera.removeChild(agentTab[indexTab[j]].SpritePercept);
					camera.removeChild(agentTab[indexTab[j]]);
					agentTab.splice(indexTab[j], 1);
				}
			}
		}
	}
}

function messageServerEnd(message) {

}

function rgb2hex(r, g, b){
	return '0x'+('0'+r.toString(16)).slice(-2)+('0'+g.toString(16)).slice(-2)+('0'+b.toString(16)).slice(-2);
}

function rgb2hex2(r, g, b){
	return '#'+('0'+r.toString(16)).slice(-2)+('0'+g.toString(16)).slice(-2)+('0'+b.toString(16)).slice(-2);
}


function animate() {

    requestAnimFrame( animate );

    renderer.resize(contener.offsetWidth-1, contener.offsetHeight-1);
    
    var coordCenterX = contener.offsetWidth-1 / 2;
    var coordCenterY = contener.offsetHeight-1 / 2;

	for (i = 0; i < agentTab.length; i++) {
		if(camera.follow) {
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
	stage.setBackgroundColor(0xCC9966);
	addButton(hud, buttonLife, buttonLifeDown, buttonLifeTrans, 20, 20, buttonTab, 1);
	addButton(hud, buttonMessage, buttonMessageDown, buttonMessageTrans, 60, 20, buttonTab, 2);
	addButton(hud, buttonPercept, buttonPerceptDown, buttonPerceptTrans, 100, 20, buttonTab, 3);
}

function cameraMove(stg, cam) {

	var isDragging = false;
	var prevX;
	var prevY;

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
/*
	stg.mouseout = function(data) {

		console.log("OUT");
		//for (i = 0; i < buttonTab.length; i++) {
			//buttonTab[i].alpha = -1;
		//}
	}

	stg.mouseover = function(data) {

console.log("on");
	for (i = 0; i < buttonTab.length; i++) {
		buttonTab[i].alpha = 1;
	}
}*/


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

	for (i = 0; i < agentTab.length; i++) {
		if(agentTab[i].scale.x * factor <= 1 &&  agentTab[i].scale.y * factor <= 1 && agentTab[i].scale.x * factor >= 0.32 &&  agentTab[i].scale.y * factor >= 0.32) {
			agentTab[i].scale.x *= factor;
			agentTab[i].scale.y *= factor;
			agentTab[i].SpriteLife.scale.x *= factor;
			agentTab[i].SpriteLife.scale.y *= factor;
			agentTab[i].SpriteLife.position.y = agentTab[i].position.y - Math.sqrt(agentTab[i].height * agentTab[i].scale.x * agentTab[i].height * agentTab[i].scale.x);
			agentTab[i].SpritePercept.scale.x *= factor;
			agentTab[i].SpritePercept.scale.y *= factor;
			agentTab[i].debug.setStyle();
		}
	}
};


function myFunction() {
	analyseMessageServer(message1);
}

function myFunction2() {
	analyseMessageServer (message2);
}

function myFuncFion3() {
	analyseMessageServer (message3);
}

function myFuncFion4() {
	analyseMessageServer (message4);
}

function myFuncFion5() {
	analyseMessageServer (message5);
}

function myFuncFion6() {
	analyseMessageServer (message6);
}

function myFunction7() {
	analyseMessageServer (message7);
}

function myFunction8() {
	analyseMessageServer (message8);
}
