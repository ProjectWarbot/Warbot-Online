
function WebGameModel(stompClient) {
  var self = this;

  self.connect = function() {
      stompClient.connect({}, function(frame) {

      console.log('Connected ' + frame);
      self.username(frame.headers['user-name']);

      stompClient.

      stompClient.subscribe("/game/register", function(message) {
      /* Return a message if can save */
        self.portfolio().loadPositions(JSON.parse(message.body));
      });
      stompClient.subscribe("/user/game.agents.*", function(message) {
        self.jimmyHandler().processAgent(JSON.parse(message.body));

      });
      stompClient.subscribe("/user/queue/position-updates", function(message) {
        self.pushNotification("Position update " + message.body);
        self.portfolio().updatePosition(JSON.parse(message.body));
      });
      stompClient.subscribe("/user/queue/errors", function(message) {
        self.pushNotification("Error " + message.body);
      });
    }, function(error) {
      console.log("STOMP protocol error " + error);
    });
  }

  self.executeTrade = function() {
      var trade = {
          "action" : self.action(),
          "ticker" : self.currentRow().ticker,
          "shares" : self.sharesToTrade()
        };
      console.log(trade);
      stompClient.send("/game/start", {}, JSON.stringify(trade));
    }

  self.pushNotification = function(text) {
    self.notifications.push({notification: text});
    if (self.notifications().length > 5) {
      self.notifications.shift();
    }
  }

  self.logout = function() {
    stompClient.disconnect();
    window.location.href = "../logout.html";
  }
}