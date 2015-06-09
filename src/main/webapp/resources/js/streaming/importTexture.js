/**
* Constant values used for Warbot and Pixi JS Texture loading
*
* @lends Jimmy Lopez, Sébastien Beugnon
**/

//NEW SYSTEM
var TEXTURE_CONSTANTS = {
	/**
	* @static
	* @constant
	* @property {object} AGENTS - Agents texture names
	**/
	AGENTS : {
		/**
		* @static
		* @constant
		* @property {string} EXPLORER - Explorer texture name
		**/
		EXPLORER : "explorer",
		/**
		* @static
		* @constant
		* @property {string} P_EXPLORER - Explorer Perception Field texture name
		**/
		P_EXPLORER : "p_explorer",
		ENGINEER : "engineer",
		ROCKET_LAUNCHER :"rocketLauncher",
		KAMIKAZE : "kamikaze",
		TURRET : "turret",
		BASE : "base",
		WALL : "wall",
		ROCKET : "rocket",
		BOMB : "bomb",
		FOOD : "food",

		EXPLOSION : "",
	},
	HUD :{},
	MAP: {1 : "map001",2:"map002",3:"map003",4:"map004"},

	TEAM: "team",

	QUALITY: {
		FAST: "-fast.png",
		NORMAL: ".png"
	}

};

function initTex(path,quality) {
	return PIXI.Texture.fromImage(path+quality);
}

/**
* Create an instance of TeamTexture with a specific path
*
**/
function TeamTexture(pathToTextureWithTeam,quality)
{
	this.explorer = initTex
	(pathToTextureWithTeam+TEXTURE_CONSTANTS.AGENTS.EXPLORER,quality);
	this.engineer = initTex
	(pathToTextureWithTeam+TEXTURE_CONSTANTS.AGENTS.ENGINEER,quality);
	this.rocketLauncher = initTex
	(pathToTextureWithTeam+TEXTURE_CONSTANTS.AGENTS.ROCKET_LAUNCHER,quality);
	this.kamikaze = initTex
	(pathToTextureWithTeam+TEXTURE_CONSTANTS.AGENTS.KAMIKAZE,quality);
	this.turret = initTex
	(pathToTextureWithTeam+TEXTURE_CONSTANTS.AGENTS.TURRET,quality);
	this.base = initTex
	(pathToTextureWithTeam+TEXTURE_CONSTANTS.AGENTS.BASE,quality);
	this.wall = initTex
	(pathToTextureWithTeam+TEXTURE_CONSTANTS.AGENTS.WALL,quality);
}

function HUDTexture(pathToTexture)
{
	this.buttonLife = {
		ON :initTex
		(pathToTexture+TEXTURE_CONSTANTS.HUD.LIFE.ON),
		OFF:initTex
		(pathToTexture+TEXTURE_CONSTANTS.HUD.LIFE.OFF),
		TRANSPARENT:initTex
		(pathToTexture+TEXTURE_CONSTANTS.HUD.LIFE.TRANSPARENT)
	};
	this.buttonStop = {
		ON : "",
		OFF :"",
		TRANSPARENT:""
	};
}

/**
* Create an instance of TextureFolder containing all textures for teams, maps, hud
**/
function TextureFolder(pathToTexture,nbTeam,quality = null)
{
	this.quality = quality || TEXTURE_CONSTANTS.QUALITY.NORMAL;
	this.teams = [];
	for (i = 0;i<nbTeam;i++) {
		console.log(pathToTexture+"/"+TEXTURE_CONSTANTS.TEAM+""+i+"/");
		this.teams.push(new TeamTexture(pathToTexture+"/"+TEXTURE_CONSTANTS.TEAM+""+i+"/",this.quality));
	}
	this.rocket = initTex
	(pathToTexture+"/"+TEXTURE_CONSTANTS.AGENTS.ROCKET,this.quality);
	this.map =
	(pathToTexture+"/"+TEXTURE_CONSTANTS.AGENTS.ROCKET,this.quality);

}

TextureFolder.prototype.reloadTexture = function() {};
TextureFolder.prototype.reloadTexture = function(pathToTexture) {};
TextureFolder.prototype.reloadTexture = function(pathToTexture,quality) {};


// OLD SYSTEM

var explorerRed = PIXI.Texture.fromImage("/resources/assetWarbot/RedTeam/explorer.png");
var explorerBlue = PIXI.Texture.fromImage("/resources/assetWarbot/BlueTeam/explorer.png");
var engineerRed = PIXI.Texture.fromImage("/resources/assetWarbot/RedTeam/engineer.png");
var engineerBlue = PIXI.Texture.fromImage("/resources/assetWarbot/BlueTeam/engineer.png");
var rocketLauncherRed = PIXI.Texture.fromImage("/resources/assetWarbot/RedTeam/rocketLauncher.png");
var rocketLauncherBlue = PIXI.Texture.fromImage("/resources/assetWarbot/BlueTeam/rocketLauncher.png");
var kamikazeRed = PIXI.Texture.fromImage("/resources/assetWarbot/RedTeam/kamikaze.png");
var kamikazeBlue = PIXI.Texture.fromImage("/resources/assetWarbot/BlueTeam/kamikaze.png");
var turretRed = PIXI.Texture.fromImage("/resources/assetWarbot/RedTeam/turret.png");
var turretBlue = PIXI.Texture.fromImage("/resources/assetWarbot/BlueTeam/turret.png");
var baseRed = PIXI.Texture.fromImage("/resources/assetWarbot/RedTeam/base.png");
var baseBlue = PIXI.Texture.fromImage("/resources/assetWarbot/BlueTeam/base.png");
var wallRed = PIXI.Texture.fromImage("/resources/assetWarbot/RedTeam/wall.png");
var wallBlue = PIXI.Texture.fromImage("/resources/assetWarbot/BlueTeam/wall.png");
var rocket = PIXI.Texture.fromImage("/resources/assetWarbot/MotherTeam/rocket2.png");;
var bomb;
var food = PIXI.Texture.fromImage("/resources/assetWarbot/MotherTeam/food02.png");

var map = PIXI.Texture.fromImage("/resources/assetWarbot/MotherTeam/map004.png");

var perceptOther = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/perceptOther.png");
var perceptBaseRed = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/RED/perceptBase.png");
var perceptBaseBlue = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/BLUE/perceptBase.png");
var perceptExplorerRed = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/RED/perceptExplorer.png");
var perceptExplorerBlue = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/BLUE/perceptExplorer.png");
var perceptTurretRed = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/RED/perceptTurret.png");
var perceptTurretBlue = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/BLUE/perceptTurret.png");
var perceptRocketLauncherRed = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/RED/perceptRocketLauncher.png");
var perceptRocketLauncherBlue = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/BLUE/perceptRocketLauncher.png");
var perceptEngineerRed = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/RED/perceptEngeneer.png");
var perceptEngineerBlue = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/BLUE/perceptEngeneer.png");
var perceptKamikazeRed = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/RED/perceptKamikaze.png");
var perceptKamikazeBlue = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/percept/BLUE/perceptKamikaze.png");

var life001 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life001.png");
var life002 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life002.png");
var life003 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life003.png");
var life004 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life004.png");
var life005 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life005.png");
var life006 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life006.png");
var life007 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life007.png");
var life008 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life008.png");
var life009 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life009.png");
var life010 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life010.png");
var life011 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life011.png");
var life012 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life012.png");
var life013 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life013.png");
var life014 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life014.png");
var life015 = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/life/life015.png");

var buttonLife = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonLifeOff.png");
var buttonLifeDown = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonLifeOn.png");
var buttonLifeTrans = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonLifeTrans.png");;
var buttonMessage = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonMessageDebugOff.png");
var buttonMessageDown = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonMessageDebugOn.png");
var buttonMessageTrans = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonMessageDebugTrans.png");
var buttonPercept = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonPerceptOff.png");
var buttonPerceptDown = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonPerceptOn.png");
var buttonPerceptTrans = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonPerceptTrans.png");

var buttonStop = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonStopGameOff.png");
var buttonStopDown = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonStopGameOn.png");
var buttonStopTrans = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonStopGameTrans.png");

var buttonStats = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonStatsOff.png");
var buttonStatsDown = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonStatsOn.png");
var buttonStatsTrans = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/buttonStatsTrans.png");

var buttonTeamRed = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/RED/buttonSelectTeamOff.png");
var buttonTeamRedDown = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/RED/buttonSelectTeamOn.png");
var buttonTeamRedTrans = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/RED/buttonSelectTeamTrans.png");
var buttonTeamBlue = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/BLUE/buttonSelectTeamOff.png");
var buttonTeamBlueDown = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/BLUE/buttonSelectTeamOn.png");
var buttonTeamBlueTrans = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/BLUE/buttonSelectTeamTrans.png");

var buttonCreateBaseRed = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/RED/buttonCreateBaseOff.png");
var buttonCreateBaseRedDown = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/RED/buttonCreateBaseOn.png");
var buttonCreateBaseRedTrans = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/RED/buttonCreateBaseTrans.png");
var buttonCreateBaseBlue = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/BLUE/buttonCreateBaseOff.png");
var buttonCreateBaseBlueDown = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/BLUE/buttonCreateBaseOn.png");
var buttonCreateBaseBlueTrans = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/debug/BLUE/buttonCreateBaseTrans.png");

var followAgent = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/followAgent.png");
var followWall = PIXI.Texture.fromImage("/resources/assetWarbot/InfoAgent/followWall.png");

var chargementGif = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/chargement.gif");
var playButton = PIXI.Texture.fromImage("/resources/assetWarbot/HUD/playButton.png");