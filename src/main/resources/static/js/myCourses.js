//myclasses
//requests needed: $("#class_list")

//go through student's courses and display


$(".clickable-row").bind('click', function(e) {
	var cells = this.getElementsByTagName('td');
	
	var courseID = cells[0].innerHTML;
	var role = cells[1].innerHTML;
	console.log(role);
	var url;
	if(role == "TA") {
		url = "/taHoursSetUp/" + courseID;
	} else {

		url = "/welcomeStudent/" + courseID;
	}
	$.get(url, function(responseJSON) {
		console.log(responseJSON);
	});
});