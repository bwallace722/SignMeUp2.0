//myclasses
//requests needed: $("#class_list")

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
		url = "/welcomeStudent/" + courseID;
		console.log(url);
	}
	window.location.href=url;
});