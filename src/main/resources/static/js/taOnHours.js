var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];
//var queueHTMLStart = "<div class=\"row studentOnQueue\">" +
//    "<div class=\"col-sm-8 col-sm-push-1\" data-toggle=\"modal\" data-target=\"#queueModal\"><h5>";
var queueHTMLStart = "<div class=\"row studentOnQueue\" data-toggle=\"modal\" data-target=\"#queueModal\">" +
"<div class=\"col-sm-8 col-sm-push-1\"><h5>";
var queueHTMLEnd = "</h5></div><br><hr>";

var aptHTMLStart = "<div class=\"row apt\">div class=\"col-sm-4 col-sm-push-1\"><h5>";
var aptHTMLEnd = "</h5></div><br><hr></div>";

var emptyQueue = "<h4>There are no students on the Queue!</h4>";

function returnToSetup() {
	window.location.href = "/taHoursSetUp/" + courseId;
}

var currAss = document.getElementById("currAss");
if(currAss.innerHTML == "none"){
	currAss.innerHTML = "There is no assignment assigned for today.";
	$("#currQs").hide();
}

var studentToCall;

$(document).on('click', '.studentOnQueue', function(e) {
	var text = $(this).text();
	var text = text.trim();
	var textList = text.split(" ");
	var login = text.split(" ")[textList.length - 1];
	studentToCall = login;
});

function removeStudent() {
	console.log(studentToCall);
	var postParameters = {"studentLogin": studentToCall, 
			"course": courseId};
	$.post("/removeStudent", postParameters, function(responseJSON) {
		console.log(responseJSON + " = response");
		console.log(responseJSON == 1);
		if (responseJSON == 1) {
			updateQueue();
		}
	});
}

var studentToCall;

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
				console.log(student);
				var studentTags = queueHTMLStart + student + queueHTMLEnd;
				studentList = studentList.concat(studentTags);
			}
			queue.innerHTML = studentList;
			console.log("updated");
		} else {
			queue.innerHTML = emptyQueue;
		}
	});
}

//function removeStudent() {
//var url = "/callStudent/" + courseId;
//	var postParameters = {"studentLogin": studentToCall, 
//			"message": message};
//	$.post(url, postParameters, function(responseJSON) {
//		//confirmation message
//		if(responseJSON == 1) {
//			alert(login + " has been called to hours");
//		} else {
//			alert(login + " cannot be reached. Maybe they signed out");
//		}
//	});

//}
function callStudent() {
	var text = $(this).text();
	var message = prompt("Please enter a message to call the student to hours", "You're up for hours!");
	var text = text.trim();
	var textList = text.split(" ");
	var login = text.split(" ")[textList.length - 1];
	//TODO: find some way of parsing studentlogin from the div
	//that was clicked.
	var url = "/callStudent/" + courseId;
	if(message) {
		var postParameters = {"studentLogin": login, 
				"message": message};
		$.post(url, postParameters, function(responseJSON) {
			//confirmation message
			if(responseJSON == 1) {
				alert(login + " has been called to hours");
			} else {
				alert(login + " cannot be reached. Maybe they signed out");
			}
		});
	}
}

//updates the queue every 10 seconds.

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


setInterval(function(t) {
 	var postUrl = "/updateAppointments/" + courseId;
	$.post(postUrl, function(responseJSON) {
		var aptString = responseJSON.substring(1,responseJSON.length-1);
		var apt = document.getElementById("appointments");
		if(aptString) {
			var aptList = aptString.split(",");
			var studentList = "";
			for(var i=0; i < aptList.length; i++) {
				var student = aptList[i];
				var studentTags = aptHTMLStart + student + aptHTMLEnd;
				studentList = studentList.concat(studentTags);
			}
			apt.innerHTML = studentList;
		} else {
			apt.innerHTML = emptyQueue;
		}
	});
}, 1000);

var timer;

function startTimer() {
	var timeLimit = document.getElementById("timeLimit").innerHTML;
	console.log(timeLimit);
	var tSeconds = parseInt(timeLimit) * 10000;
	console.log(tSeconds + " seconds");
	timer = window.setTimeout(timerLimit, tSeconds);
}

function timerLimit() {
	var r = confirm("Time's up! Move onto the next Student. Click Cancel to extend time.");
	if (r != true) {
		window.clearTimeout(timer);
	    startTimer();
	}
}

function stopTimer() {
	window.clearTimeout(timer);
}

function endHours() {
	var postURL = "/endHours/" + courseId;
	$.post(postURL, function(responseJSON) {
		if(responseJSON == 1) {
			alert("hours have ended. redirecting to hours setup.");
			window.location.href = "/taHoursSetUp/" + courseId;
		} else {
			alert("unable to end hours. Please try again");
		}
	});
}
