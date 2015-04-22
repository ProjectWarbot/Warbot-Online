	var outerLayout; 

	$(document).ready(function () { 

		outerLayout = $('.contenerMapEditor').layout({
			center__paneSelector:	".outer-center",
			south__paneSelector: 	".outer-south", 
			south__size:			100,
			south__minSize:			0,
			south__maxSize:			1000,
			spacing_open:			8,
			spacing_closed:			12
		});
	});