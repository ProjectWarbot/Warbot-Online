$(document).ready(function(){

				$(".editor").each(function( index )
				{
					if(index==0)
    	               	$(this).parent().addClass("in active");
					var id = $(this).attr("id");
					var ed = ace.edit(id);
					ed.setTheme("ace/theme/monokai");
                    ed.getSession().setMode("ace/mode/python");
                    ed.setOptions({enableBasicAutocompletion: true});

                    this.style.position = "absolute";
                    this.style.marginTop = "42px";
                    this.style.top = 0;
                    this.style.right = 0;
                    this.style.bottom = 0;
                    this.style.left = 0;


                   	$(this).html += "def toto :\n\t pass";

				});



//    			var editor1 = ace.edit("editor1");
//
//
//			    var editor2 = ace.edit("editor2");
//			    editor2.setTheme("ace/theme/monokai");
//			    editor2.getSession().setMode("ace/mode/python");
//			    editor2.setOptions({enableBasicAutocompletion: true});
//
//			    var editor3 = ace.edit("editor3");
//			    editor3.setTheme("ace/theme/monokai");
//			    editor3.getSession().setMode("ace/mode/python");
//			    editor3.setOptions({enableBasicAutocompletion: true});
//
//			    var editor4 = ace.edit("editor4");
//			    editor4.setTheme("ace/theme/monokai");
//			    editor4.getSession().setMode("ace/mode/python");
//			    editor4.setOptions({enableBasicAutocompletion: true});
//
//			    var editor5 = ace.edit("editor5");
//			    editor5.setTheme("ace/theme/monokai");
//			    editor5.getSession().setMode("ace/mode/python");
//			    editor5.setOptions({enableBasicAutocompletion: true});
//
//			    var editor6 = ace.edit("editor6");
//			    editor6.setTheme("ace/theme/monokai");
//			    editor6.getSession().setMode("ace/mode/python");
//			    editor6.setOptions({enableBasicAutocompletion: true});
 });


function EditorModel(stompClient) {
  var self = this;

  self.connect = function() {
      stompClient.connect({}, function(frame) {

      console.log('Connected ' + frame);

      stompClient.subscribe("/app/editor/register", function(message) {
        /* Return a message if can save */
        // NOT NEED
      });

      stompClient.subscribe("/user/editor/code",function (message)
      {
        var m = JSON.parse(message.body);

        $("#"+m.idWebAgent+"-editor").append(m.content);
      });

    }, function(error) {
      console.log("STOMP protocol error " + error);
    });
  }

  self.get = function(idParty1,idAgent1) {
      var trade = {
          "idParty" : idParty1,
          "idWebAgent" : idAgent1
      };

      stompClient.send("/app/editor/get",{},JSON.stringify(trade));
    }

  self.save = function(idParty1,idAgent1,content) {
  	var trade = {
  	 	"idParty" : idParty1,
    	"idWebAgent" : idAgent1,
        "content" : content
    };
    stompClient.send("/app/editor/save", {}, JSON.stringify(trade));
  }

}