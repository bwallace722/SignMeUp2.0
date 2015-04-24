var windowURL = window.location.href;
var splitURL = windowURL.split("/");
//url contains student
var courseIdAndLogin = splitURL[splitURL.length -1];
var splitCourseAndLogin = courseIdAndLogin.split("~");
var courseId = splitCourseAndLogin[0];
var login = splitCourseAndLogin[1];
console.log("course: " + courseId + " , login: " + login);

function getOnQueue() {
	
	var otherQ = document.getElementById("otherQuestion").value;
	var postParameters = {"course": courseId, "login": login, 
			"otherQ": otherQ};
	$.post("/addStudentToQueue", postParameters, function(responseJSON) {
		console.log(responseJSON);
		/*
		 * If responseJSON is 1, there was no error in adding the student to the queue.
		 * Otherwise there was most likely a SQL error.
		 */
		if(responseJSON == 1) {
			alert("You've been added to the queue!");
			updateStatus();
		} else {
			console.log("SQL Error.");
		}
	});
}

//interval set to every second.
function updateStatus() {
	setInterval(checkStatus, 1000);
}

/*
 * Checks student's call status. If the ta has called them to hours,
 * an alert will appear.
 */
var checkStatus = function() {
	var postParameters = {"course": courseId, "login": login}; 
	$.post("/checkCallStatus", postParameters, function(responseJSON) {
		/*
		 * responseJSON is a boolean, returning true 
		 * if the student has been called to hours
		 * and false otherwise.
		 */
		if(responseJSON == 1) {
			alert("You've been called up for hours!");
			clearInterval(checkStatus);
		}
	});
}