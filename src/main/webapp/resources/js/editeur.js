function buildEditor(nom){




				document.getElementById("myTab").innerHTML += "<li><a data-toggle='tab' href="+nom+">"+nom+"</a></li>";
                document.getElementById("tab-content").innerHTML += "<div id='"+nom+"'class='tab-pane fade'><div class='editor' id="+nom+"editor"+"></div></div>";
/*
                document.getElementById(nom+'editor').style.position = "absolute";
				document.getElementById(nom+'editor').style.marginTop = "42px";
				document.getElementById(nom+'editor').style.top = "0";
				document.getElementById(nom+'editor').style.right = "0";
				document.getElementById(nom+'editor').style.bottom = "0";
				document.getElementById(nom+'editor').style.left = "0";
*/
				var editor = nom;
                editor = ace.edit(editor+'editor');
                editor.setTheme("ace/theme/monokai");
                editor.getSession().setMode("ace/mode/python");
                editor.setOptions({enableBasicAutocompletion: true});



 };