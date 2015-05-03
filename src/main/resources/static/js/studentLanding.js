var windowURL = window.location.href;
var splitURL = windowURL.split("/");
//url contains student
var courseIdAndLogin = splitURL[splitURL.length -1];
var splitCourseAndLogin = courseIdAndLogin.split("~");
var courseId = splitCourseAndLogin[0];
var login = splitCourseAndLogin[1];


console.log("courseId: " + courseId + " , login: " + login);


var SUCCESS_MESSAGE = "You're signed up to get your lab checked off!";
var LINE_CUTOFF_MESSAGE = "The line has been cut off, though you may still" +
		"get called to hours. We'll let you know!";
var OFF_HOURS_MESSAGE = "There are not hours right now. " +
		"We couldn't sign you up.";

function myCourses() {
	window.location.href = "/courses/" + login;
}
$(".checkOffButton").bind('click', function(c) {
	//post to queue
	//success: send alert via email too (?)
	var url = "/labCheckOff/" + login;
	var postParameters = {"course": courseId, "login": login };
	$.post(url, postParameters, function(responseJSON) {
		//TODO : based on response, give message and put into modal.
		if(responseJSON == 1) {
			alert("You're signed up for lab!");
		} else if (responseJSON == 2) {
			alert("Hours haven't started yet!");
		} else {
			alert("looks like we've had some trouble. Try again in a bit.");
		}
		window.location.href= "/studentLanding/" + courseIdAndLogin ;
		

	});
	

});

$(".hoursSignUp").bind('click', function(h) {
	//get questions from server
//	$.get("/signUpForHours");
	var url = "/signUpForHours/" + courseIdAndLogin;
	var postParameters = {"course": courseId, "login": login };
	$.post("/checkQueue", postParameters, function(responseJSON){
		if(responseJSON == 1) {
			window.location.href= url;
		} else {
			alert("The queue for this class isn't running yet! Sorry!");
		}
	});
	
});


function makeAppointment() {
	var url = "/makeAppointment/" + courseIdAndLogin;
	var postParameters = {"course": courseId, "login": login };
	$.post("/checkQueue", postParameters, function(responseJSON){
		if(responseJSON == 1) {
			window.location.href= url;
		} else {
			alert("The queue for this class isn't running yet! Sorry!");
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
	if(!calledToHours) {
	var postParameters = {"course": courseId, "login": login}; 
	$.post("/checkCallStatus", postParameters, function(responseJSON) {
		/*
		 * responseJSON is a boolean, returning true 
		 * if the student has been called to hours
		 * and false otherwise.
		 */
		if(responseJSON == 1) {
			calledToHours = true;
			alert("You've been called up for hours!");
			clearInterval(checkStatus);
		}
	});
	}
}
