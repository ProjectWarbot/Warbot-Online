$(document).ready(function(){

    			var editor1 = ace.edit("editor1");
			    editor1.setTheme("ace/theme/monokai");
			    editor1.getSession().setMode("ace/mode/python");
				var langTools = ace.require("ace/ext/language_tools");
    			editor1.setOptions({enableBasicAutocompletion: true});


			    var editor2 = ace.edit("editor2");
			    editor2.setTheme("ace/theme/monokai");
			    editor2.getSession().setMode("ace/mode/python");

			    var editor3 = ace.edit("editor3");
			    editor3.setTheme("ace/theme/monokai");
			    editor3.getSession().setMode("ace/mode/python");

			    var editor4 = ace.edit("editor4");
			    editor4.setTheme("ace/theme/monokai");
			    editor4.getSession().setMode("ace/mode/python");

			    var editor5 = ace.edit("editor5");
			    editor5.setTheme("ace/theme/monokai");
			    editor5.getSession().setMode("ace/mode/python");

			    var editor6 = ace.edit("editor6");
			    editor6.setTheme("ace/theme/monokai");
			    editor6.getSession().setMode("ace/mode/python");
 });