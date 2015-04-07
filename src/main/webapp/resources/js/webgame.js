
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
                     console.log(message.body);
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
    }, function(error) {
      console.log("STOMP protocol error " + error);
    });
  }

  self.launchParty = function() {
      var trade = {
          "idTeam1" : 0,
          "idTeam2" : 0
        };
      console.log(trade);
      stompClient.send("/app/game/start", {}, JSON.stringify(trade));
    }

  self.pushNotification = function(text) {
    self.notifications.push({notification: text});
    if (self.notifications().length > 5) {
      self.notifications.shift();
    }
  }
}