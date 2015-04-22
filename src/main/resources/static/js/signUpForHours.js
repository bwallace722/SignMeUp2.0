var windowURL = window.location.href;
var splitURL = windowURL.split("/");
//url contains student
var courseIdAndLogin = splitURL[splitURL.length -1];
var splitCourseAndLogin = courseIdAndLogin.split("~");
var courseId = splitCourseAndLogin[0];
var login = splitCourseAndLogin[1];
console.log("course: " + courseId + " , login: " + login);

//var SUCCESS MESSAGE = "You're signed up for hours!";
//var LINE_CUTOFF_MESSAGE = "The line has been cut off, though you may still" +
//		"get called to hours. We'll let you know!";
//var OFF_HOURS_MESSAGE = "There are not hours right now. " +
//		"We couldn't sign you up.";

function getOnQueue() {
	
//	var name = document.getElementById("name").value;
//	var login = document.getElementById("loginSignUp").value;
	var otherQ = document.getElementById("otherQuestion").value;
	//after we decided how these will be displayed,
	//can understand how to best access them and place
	//chosen questions into post parameters.
	var postParameters = {"course": courseId, "login": login, 
			"otherQ": otherQ};
	console.log("before post");
	$.post("/addStudentToQueue", postParameters, function(responseJSON) {
		//TODO : based on response, give message and put into modal.
		//when success received, begin function to check status on queue
		console.log("here");
		if(responseJSON.equals("1")) {
			console.log("lbs");
			updateStatus();
		}
	});
}

//interval set to every second.
function updateStatus() {
	setInterval(checkStatus, 1000);
}

//checks student's call status. If the ta has called them to hours,
//an alert will appear.
var checkStatus = function() {
	var postParameters = {"course": courseId, "login": login}; 
	$.post(url, postParameters, function(responseJSON) {
		/**
		 * responseJSON is a boolean, returning true 
		 * if the student has been called to hours
		 * and false otherwise.
		 */
		if(responseJSON) {
			alert("You've been called up for hours!");
			clearInterval(checkStatus);
		}
	});
}