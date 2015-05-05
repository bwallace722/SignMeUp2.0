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
var queueHTMLStart = "<div class=\"row studentOnQueue\" data-toggle=\"modal\" data-target=\"#queueModal\">" +
"<div class=\"col-sm-8 col-sm-push-1\"><h5>";
var queueHTMLEnd = "</h5></div></div><hr>";
var emptyQueue = "<h4 style=\"text-align:center;\">There are no students on the Queue!</h4>";

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
		console.log(responseJSON);
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


var calledToHours = false;

/*
 * Checks student's call status. If the ta has called them to hours,
 * an alert will appear.
 */
setInterval(function(t) {
	console.log("hi");
	if(!calledToHours) {
	var postParameters = {"course": courseId, "login": login};
	$.post("/checkCallStatus", postParameters, function(responseJSON) {
		console.log(responseJSON);
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
}, 1000);

function updateQueue() {
 	var postUrl = "/updateQueue/" + courseId;
	$.post(postUrl, function(responseJSON) {
		var queueString = responseJSON.substring(1,responseJSON.length-1);
		var queue = document.getElementById("queue");
		if(queueString) {
			var queueList = queueString.split(",");
			var studentList = "";
			for(var i=0; i < queueList.length; i++) {
				var student = queueList[i];
				var studentTags = queueHTMLStart + student + queueHTMLEnd;
				studentList = studentList.concat(studentTags);
			}
			queue.innerHTML = studentList;
		} else {
			queue.innerHTML = emptyQueue;
		}
	});
}

setInterval(function(t) {
 	var postUrl = "/updateQueue/" + courseId;
	$.post(postUrl, function(responseJSON) {
		var queueString = responseJSON.substring(1,responseJSON.length-1);
		var queue = document.getElementById("queue");
		if(queueString) {
			var queueList = queueString.split(",");
			var studentList = "";
			for(var i=0; i < queueList.length; i++) {
				var student = queueList[i];
				var studentTags = queueHTMLStart + student + queueHTMLEnd;
				studentList = studentList.concat(studentTags);
			}
			queue.innerHTML = studentList;
		} else {
			queue.innerHTML = emptyQueue;
		}
	});
}, 1000);
