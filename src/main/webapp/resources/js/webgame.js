
function WebGameModel(stompClient) {
  var self = this;

  self.connect = function() {
      stompClient.connect({}, function(frame) {

      console.log('Connected ' + frame);

      stompClient.subscribe("/app/game/register", function(message) {
        /* Return a message if can save */
        // NOT NEED
      });

      stompClient.subscribe("/user/queue/game.agents.*", function(message)
      {
        //UPDATE AGENT
                    // console.log(message.body);
                    analyseMessageServer(JSON.parse(message.body));
                     
      });
       stompClient.subscribe("/user/queue/game", function(message)
            {
              //UPDATE AGENT
              //console.log(message.body);
              analyseMessageServer(JSON.parse(message.body));

            });

      stompClient.subscribe("/user/queue/errors", function(message) {
        self.pushNotification("Error " + message.body);
      });

      stompClient.subscribe("/app/editor/register", function(message) {
              /* Return a message if can save */
              // NOT NEED
            });

            stompClient.subscribe("/user/editor/code",function (message)
            {
              var m = JSON.parse(message.body);
              var editor = $("#"+m.webAgentId+"-editor");

              if (editor.length==0)
              {
                  console.log("none");
              }
              else
              {
                  console.log(editors);
                  editors[m.webAgentId+"-editor"].getSession().setValue(m.content);
              }

            });
    }, function(error) {
      console.log("STOMP protocol error " + error);
    });
  }

  self.launchParty = function(idParty1,idParty2) {
      var trade = {
          "idTeam1" : idParty1,
          "idTeam2" : idParty2
        };
      console.log(trade);
      stompClient.send("/app/game/start.against.ia", {}, JSON.stringify(trade));
    };

     self.get = function(idParty1,idAgent1) {
          var trade = {
              "idParty" : idParty1,
              "idWebAgent" : idAgent1
          };

          stompClient.send("/app/editor/get",{},JSON.stringify(trade));
        };

      self.save = function(idParty1,idAgent1,content) {
      	var trade = {
      	 	"idParty" : idParty1,
        	"idWebAgent" : idAgent1,
            "content" : content
        };
        stompClient.send("/app/editor/save", {}, JSON.stringify(trade));
      }

  self.pushNotification = function(text) {
    self.notifications.push({notification: text});
    if (self.notifications().length > 5) {
      self.notifications.shift();
    }
  };
}