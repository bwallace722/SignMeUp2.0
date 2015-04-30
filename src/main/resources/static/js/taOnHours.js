var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];
//var queueHTMLStart = "<div class=\"row studentOnQueue\">" +
//    "<div class=\"col-sm-8 col-sm-push-1\" data-toggle=\"modal\" data-target=\"#queueModal\"><h5>";
var queueHTMLStart = "<div class=\"row studentOnQueue\" data-toggle=\"modal\" data-target=\"#queueModal\">" +
"<div class=\"col-sm-8 col-sm-push-1\"><h5>";
var queueHTMLEnd = "</h5></div><br><hr>";

var emptyQueue = "<h4>There are no students on the Queue!</h4>";

function returnToSetup() {
	window.location.href = "/taHoursSetUp/" + courseId;
}

var currAsign = document.getElementById("currAsign");
console.log(currAsign.innerHTML);
if(currAsign.innerHTML == "none"){
	currAsign.innerHTML = "There is no assignment assigned for today.";
	$("#currQs").hide();
}

function removeStudent() {
	var text = $(this).text();
	var text = text.trim();
	var textList = text.split(" ");
	var login = text.split(" ")[textList.length - 1];
	var postParameters = {"studentLogin": login, 
			"course": courseId};
	$.post("/removeStudent", postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			$(this).hide();
		}
	});
}

var studentToCall;

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
//when prompt is gone, save message

	var text = text.trim();
	var textList = text.split(" ");
	var login = text.split(" ")[textList.length - 1];
	console.log(login);
	
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
