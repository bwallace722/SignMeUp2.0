//myclasses
//requests needed: $("#class_list")
var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var user = splitURL[splitURL.length -1];

document.cooke = "login="+user+";";
console.log(document.cookie);

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

function signOut() {
	var url = "/signOut/" + user;

	$.post(url, function(responseJSON) {
	console.log(responseJSON);
		if(responseJSON == 1) {
			window.location.href = "/home";
		}
	});
}


function removeCourse() {
	var courses = document.getElementById("editCourseDropdown");
	
	var courseSelected = courses.options[courses.selectedIndex].value;
	console.log(courseSelected);
	
	var posturl = "/removeCourse";
	var postParameters = {"course": courseSelected, "login": user};
	$.post("/removeCourse", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			window.location.href = "/courses/" + user;
		} else {
			alert("problems");
		}

	});
}

function addCourses() {
	var url = "/addCourses/"+user;
	window.location.href = url;
}
