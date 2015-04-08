


function requestWebCode()
{
    var vidAgent = $(this).data;
    console.log(vidAgent);
    var vidParty = $("input #idParty").value;


    console.log(vidParty);

    $.get("editor/get",{ idAgent: vidAgent,idParty:vidParty},
    function( data )
    {
      alert( "Data Loaded: " + data );
      $("editor1").html += data;
    });



}



$(document).ready(function()
{
    console.log("start request handler");
    $(".webcode-request").click(requestWebCode());
    console.log("end request handler");
});