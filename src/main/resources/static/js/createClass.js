var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var login = splitURL[splitURL.length -1];


function createClass() {
	var courseID = document.getElementById("courseID").value;
	var courseName = document.getElementById("courseName").value;
	console.log(courseID);
	console.log(courseName);
	var url = "/addCourse/" + courseID;
	var postParameters = {"courseId": courseID, "courseName": courseName};
	
	$.post(url, postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			window.location.href = "/courseSetUp/" + courseID;
		} else {
			alert("course could not be created. Try again");
		}
	});
}


function signOut() {
	var url = "/signOut/" + login;

	$.post(url, function(responseJSON) {
	console.log(responseJSON);
		if(responseJSON == 1) {
			window.location.href = "/home";
		} else {
			alert("We had problems signing you out. Try again.");
		}
	});
}