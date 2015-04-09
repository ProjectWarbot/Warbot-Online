





$().ready(function()
{
    $(".webcode-request").click(function(event)
    {
          var child = $(this).children("input");

          var vidAgent = child[0].value;

          var vidParty = $("#idParty")[0].value;
          console.log(vidAgent);
          console.log(vidParty);

        $.get("editor/get/",{ idWebAgent: vidAgent,idParty:vidParty},
        function( data )
        {
          $("#editor1").append(data);
        });
    });
    console.log("end request handler");
});