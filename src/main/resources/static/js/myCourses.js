//myclasses
//requests needed: $("#class_list")
var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var user = splitURL[splitURL.length -1];
//go through student's courses and display
var user = document.getElementById("user").innerHTML;

$(".clickable-row").bind('click', function(e) {
	var cells = this.getElementsByTagName('td');
	
	var courseID = cells[0].innerHTML;
	var role = cells[1].innerHTML;
	var url;
	if(role == "TA") {
		url = "/taHoursSetUp/" + courseID;
		console.log(url);
	} else {
		//add course id and user's login to url.
		url = "/welcomeStudent/" + courseID + "?" + user;

		console.log(url);
	}
	window.location.href=url;
});