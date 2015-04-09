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
                    editors[id] = ed;

                    this.style.position = "absolute";
                    this.style.marginTop = "42px";
                    this.style.top = 0;
                    this.style.right = 0;
                    this.style.bottom = 0;
                    this.style.left = 0;

				});
 });