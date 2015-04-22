//myclasses
//requests needed: $("#class_list")
var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var user = splitURL[splitURL.length -1];


var userCourseList = document.getElementById("userCourseList");
console.log(userCourseList);
//if(userCourseList.innerHTML == "") {
//	$("#courseTable").hide(0);
//	var coursePanel = document.getElementById("coursePanel");
//	coursePanel.innerHTML = "<h3>You've got no courses! Try adding a course!</h3>" +
//			"<a class=\"btn btn-primary btn-lg\" onclick=\"addCourses()\"" +
//					"id=\"addCourseBtn\">Add a Course</a>";
//	var addBtn = document.getElementById("addCourseBtn");
//	addBtn.style.float = "left";
//	addBtn.style.margin = "5% 0% 0% 38%";
//}

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

function addCourses() {
	var url = "/addCourses/"+user;
	window.location.href = url;
}