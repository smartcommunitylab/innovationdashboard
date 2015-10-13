AUI().ready(
/*
 * This function gets loaded when all the HTML, not including the portlets, is
 * loaded.
 */
function() {
});

Liferay.Portlet.ready(
/*
 * This function gets loaded after each and every portlet on the page.
 * 
 * portletId: the current portlet's id node: the Alloy Node object of the
 * current portlet
 */
function(portletId, node) {
	/*
	 * if (portletId == '56_INSTANCE_lEYgoVM6LDfr') { var mapOptions = { zoom :
	 * 8, center : new google.maps.LatLng(46.070151, 11.150627), panControl :
	 * false, zoomControl : true, mapTypeControl : true, scaleControl : false,
	 * streetViewControl : false, overviewMapControl : false };
	 * 
	 * var map = new google.maps.Map(document.getElementById("map-canvas"),
	 * mapOptions); }
	 */
});

Liferay.on('allPortletsReady',
/*
 * This function gets loaded when everything, including the portlets, is on the
 * page.
 */
function() {
});