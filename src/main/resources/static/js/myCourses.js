//myclasses
//requests needed: $("#class_list")
var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var user = getCookie("login");


console.log(document.cookie);
console.log(user);

function getCookie(name) {
	  var regexp = new RegExp("(?:^" + name + "|;\s*"+ name + ")=(.*?)(?:;|$)", "g");
	  var result = regexp.exec(document.cookie);
	  return (result === null) ? null : result[1];
}

$(".clickable-row").bind('click', function(e) {
	var cells = this.getElementsByTagName('td');
	
	var courseID = cells[0].innerHTML;
	var role = cells[1].innerHTML;
	var url;
	if(role == "TA") {
		url = "/taHoursSetUp/" + courseID;
		console.log(url);
	} else {
		url = "/studentLanding/" + courseID;
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
			window.location.href = "/courses";
		} else {
			alert("problems");
		}

	});
}

function addCourses() {
	window.location.href = "/addCourses";
}
