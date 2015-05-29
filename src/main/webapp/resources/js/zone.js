	var outerLayout, middleLayout;

	$(document).ready(function () {

		outerLayout = $('.box').layout({
			center__paneSelector:	".outer-center",
			west__paneSelector:		".outer-west",
			west__size:				600,
			west__minSize:			1,
			west__maxSize:			1000,
			spacing_open:			8,
			spacing_closed:			12,
			center__onresize:		"middleLayout.resizeAll"
		});

		middleLayout = $('div.outer-west').layout({
			center__paneSelector:	".middle-center",
			south__size:			150,
			spacing_open:			8,
			spacing_closed:			12,
			south__minSize:			45,
			south__maxSize:			2000,
		});

	});