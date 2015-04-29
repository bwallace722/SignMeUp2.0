var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];
updateQueue();
//var queueHTMLStart = "<div class=\"row studentOnQueue\">" +
//    "<div class=\"col-sm-8 col-sm-push-1\" data-toggle=\"modal\" data-target=\"#queueModal\"><h5>";
var queueHTMLStart = "<div class=\"row studentOnQueue\" onclick=\"callStudent()\">" +
"<div class=\"col-sm-8 col-sm-push-1\"><h5>";
var queueHTMLEnd = "</h5></div><br><hr>";

function returnToSetup() {
	window.location.href = "/taHoursSetUp/" + courseId;
}

var studentToCall;

//$(".studentOnQueue").bind('click', function(s) {
//	var text = $(this).text();
	//
//		var message = prompt("Please enter a message to call the student to hours", "You're up for hours!");
	////when prompt is gone, save message
	//
//		var text = text.trim();
//		var textList = text.split(" ");
//		studentToCall = text.split(" ")[textList.length - 1];
//		console.log(login);
//});

//function callStudent() {
//
//	var message = prompt("Please enter a message to call the student to hours", "You're up for hours!");
////when prompt is gone, save message
//	
//	//TODO: find some way of parsing studentlogin from the div
//	//that was clicked.
//	var url = "/callStudent/" + courseId;
//	if(message) {
//		var postParameters = {"studentLogin": studentToCall, 
//				"message": message};
//		$.post(url, postParameters, function(responseJSON) {
//			//confirmation message
//			if(responseJSON == 1) {
//				alert(login + " has been called to hours");
//			} else {
//				alert(login + " cannot be reached. Maybe they signed out");
//			}
//		});
//	}
//	
//}

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
	console.log("here");
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
//setInterval(updateQueue(), 1000);

function updateQueue() {
 	var postUrl = "/updateQueue/" + courseId;
	$.post(postUrl, function(responseJSON) {
		var queueString = responseJSON.substring(1,responseJSON.length-1);
		console.log(queueString);
		if(queueString) {
			var queueList = queueString.split(",");
			console.log(queueList);
			var queue = document.getElementById("queue");
			var studentList = "";
			for(var i=0; i < queueList.length; i++) {
				var student = queueList[i];				
				var studentTags = queueHTMLStart + student + queueHTMLEnd;
				studentList = studentList.concat(studentTags);
			}
			queue.innerHTML = studentList;
		}
		
	});
}

setInterval(function(t) {
 	var postUrl = "/updateQueue/" + courseId;
	$.post(postUrl, function(responseJSON) {
		var queueString = responseJSON.substring(1,responseJSON.length-1);
		console.log(queueString);
		if(queueString) {
			var queueList = queueString.split(",");
			console.log(queueList);
			var queue = document.getElementById("queue");
			var studentList = "";
			for(var i=0; i < queueList.length; i++) {
				var student = queueList[i];
				var studentTags = queueHTMLStart + student + queueHTMLEnd;
				studentList = studentList.concat(studentTags);
			}
			queue.innerHTML = studentList;
		}
		
	});
}, 4000);

var timer;

function startTimer() {
	var timeLimit = document.getElementById("timeLim");
	var tSeconds = parseInt(timeLimit) * 1000;
	timer = window.setTimeout(timerLimit(), tSeconds);
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

////once hours are on, update queue should run on an interval.
//function updateQueue() {
//
//}
