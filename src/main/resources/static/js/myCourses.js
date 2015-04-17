//myclasses
//requests needed: $("#class_list")
var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var user = splitURL[splitURL.length -1];
console.log(user);

var user_class_list = document.getElementById("user_class_list");
var courseTable = document.getElementById("courseTableBody");

console.log(user_class_list);
var tableRowTagStart = "<tr class=\"clickable-row\">" +
              "<td class=\"courseId\">";
var tableRowTagMidde = "</td><td>";
var tableRowTageEnd = "</td></tr>";


$(".clickable-row").bind('click', function(e) {
	var cells = this.getElementsByTagName('td');
	
	var courseID = cells[0].innerHTML;
	var role = cells[1].innerHTML;
	var url;
	if(role == "TA") {
		url = "/taHoursSetUp/" + courseID;
		console.log(url);
	} else {
		user = user.trim();
		//add course id and user's login to url.
		url = "/studentLanding/" + courseID + "~" + user;
		console.log(url);
	}
	window.location.href=url;
});