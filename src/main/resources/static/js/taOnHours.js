var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];
//var queueHTMLStart = "<div class=\"row studentOnQueue\">" +
//    "<div class=\"col-sm-8 col-sm-push-1\" data-toggle=\"modal\" data-target=\"#queueModal\"><h5>";
var queueHTMLStart = "<div class=\"row studentOnQueue\" data-toggle=\"modal\" data-target=\"#queueModal\">" +
"<div class=\"col-sm-8 col-sm-push-1\"><h5>";
var queueHTMLEnd = "</h5></div></div><hr>";

var aptHTMLStart = "<div class=\"row aptOnHrs\" data-toggle=\"modal\" data-target=\"#aptModal\"><div class=\"col-sm-5 col-sm-push-1\"><h5>";
var aptHTMLTime = "</h5></div><div class=\"col-sm-4 col-sm-push-2\"><h5 id=\"aptTime\">";
var aptHTMLEnd = "</h5></div><br><hr></div>";

var clinicHTMLStart = "<div class=\"row clinicQ\"><div class=\"col-sm-4 col-sm-push-1\"><h5>";
var clinicHTMLStudents = "</h5></div><div class=\"col-sm-4 col-sm-push-2\"><h5 id=\"clinicStudents\">";
var clinicHTMLEnd = "</h5></div><br><hr></div>";

var emptyQueue = "<h4>There are no students on the Queue!</h4>";
var emptyApts = "<h4>There are no students with Appointments!</h4>";
var emptyClinics = "<h4>There are no clinic suggestions!</h4>";

function returnToSetup() {
	window.location.href = "/taHoursSetUp/" + courseId;
}

var currAss = document.getElementById("currAss");
if(currAss.innerHTML == "none"){
	currAss.innerHTML = "There is no assignment assigned for today.";
	$("#currQs").hide();
}

var studentToCall;
var studentApt;
var aptTime;

$(document).on('click', '.aptOnHrs', function(e) {

	var text = $(this).text();
	console.log(text);
	var text = text.trim();
	var textList = text.split(" ");
	var time = textList[textList.length - 2];
	var amPm = textList[textList.length - 1];

	aptTime = time + " " + amPm;
});

$(document).on('click', '.studentOnQueue', function(e) {
	console.log($(this).text() + " - student");
	var text = $(this).text();
	var text = text.trim();
	var textList = text.split(" ");
	var login = text.split(" ")[textList.length - 1];
	studentToCall = login;
});

//var clinicToCall;

updateClinic();

$(document).on('click', '.clinicQ', function(e) {
	var text = $(this).text();
	var text = text.trim();
	console.log(text);
	var textList = text.split(" ");
	console.log(textList);
	var question = textList[textList.length - 2];
	var students = textList[textList.length - 1];
	console.log(question + " - questions");
	console.log(students + " - students");
	var postParameters = { "clinicQ": question,
			"students": students,
			"course": courseId};
	callClinic(postParameters);
	
});

function callClinic(postParameters) {
	$.post("/getClinicStudents", postParameters, function(responseJSON) {
		alert("you have just called");
		updateClinic();
	});
}

function checkOffApt() {
	var postParameters = { "time": aptTime,
			"course": courseId};
	console.log(aptTime);
	$.post("/checkOffAppointment", postParameters, function(responseJSON) {
		updateAppointments();
	});
}

function cancelApt() {
	var postParameters = { "time": aptTime,
			"course": courseId};
	$.post("/cancelAppointment", postParameters, function(responseJSON) {
		updateAppointments();
	});
}

function removeStudent() {
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

function updateAppointments() {
 	var postUrl = "/updateAppointments/" + courseId;
	$.post(postUrl, function(responseJSON) {
		console.log(responseJSON);
		var aptString = responseJSON;
		var apt = document.getElementById("appointments");
		var updatedApts;
		if(aptString) {
			var aptList = aptString.split(",");
			var studentList = "<h3>Appointments</h3>";
			for(var i=0; i < aptList.length; i++) {
				var apt = aptList[i];
				if(apt != "") {
				var studentTime = apt.split("~");
				var student = studentTime[0];
				var time = studentTime[1];
				var aptTags = aptHTMLStart + student + aptHTMLTime + time + aptHTMLEnd;
				studentList = studentList.concat(aptTags);
				}
			}
			updatedApts = studentList;
		} else {
			updatedApts = emptyApts;
		}
		document.getElementById("appointments").innerHTML = updatedApts;
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
				var studentTags = queueHTMLStart + student + queueHTMLEnd;
				studentList = studentList.concat(studentTags);
			}
			queue.innerHTML = studentList;
		} else {
			queue.innerHTML = emptyQueue;
		}
	});
}

function updateClinic() {
 	var postUrl = "/updateClinic/" + courseId;
	$.post(postUrl, function(responseJSON) {
		console.log(responseJSON);
		var cString = responseJSON;
		var clinic = document.getElementById("clinicSuggs");
		var updatedClinics;
		if(cString) {
			
			var cList = cString.split("!");
			var studentList = "<h3>Clinic Suggestions</h3>";
			for(var i=0; i < cList.length; i++) {
				var c = cList[i].split("~");
				var cName = c[0];
				if(c != "") {
				var sList = c[1].split(",");
				var students = "";
				for(var j = 0; j < sList.length; j++) {
					students = students.concat(sList[j] + ",");
				}
				var clTags = clinicHTMLStart + cName + clinicHTMLStudents + students + clinicHTMLEnd;
				studentList = studentList.concat(clTags);
				}
			}
			updatedClinics = studentList;
		} else {
			updatedClinics = emptyClinics;
		}
		document.getElementById("clinicSuggs").innerHTML = updatedClinics;
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

	var message = prompt("Please enter a message to call the student to hours", "You're up for hours!");
	var login = studentToCall;
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
//				var emailParameters = {"login": login, "message": message, "taEmail": "signmeuptester@gmail.com"};
//				$.post("/emailStudent", emailParameters, function(responseJSON){
//					if(responseJSON == 1){
//						alert(login + " has been notified via email");
//					}
//				});
				var zwriteParameters = {"login": login, "message": message};
				$.post("/zwriteStudent", zwriteParameters, function(responseJSON){
					if(responseJSON == 1){
						alert(login + " has been notified via zwrite");
					}
				});
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
 	var postUrl = "/updateClinic/" + courseId;
	$.post(postUrl, function(responseJSON) {
		var cString = responseJSON;
		var clinic = document.getElementById("clinicSuggs");
		var updatedClinics;
		if(cString) {
			
			var cList = cString.split("!");
			var studentList = "<h3>Clinic Suggestions</h3>";
			for(var i=0; i < cList.length; i++) {
				var c = cList[i].split("~");
				var cName = c[0];
				if(c != "") {
				var sList = c[1].split(",");
				var clTags = clinicHTMLStart + cName + " " + clinicHTMLStudents + sList + clinicHTMLEnd;
				studentList = studentList.concat(clTags);
				}
			}
			updatedClinics = studentList;
		} else {
			updatedClinics = emptyClinics;
		}
		document.getElementById("clinicSuggs").innerHTML = updatedClinics;
	});
}, 1000);

setInterval(function(t) {
 	var postUrl = "/updateAppointments/" + courseId;
	$.post(postUrl, function(responseJSON) {
		var aptString = responseJSON;
		var apt = document.getElementById("appointments");
		var updatedApts;
		if(aptString) {
			var aptList = aptString.split(",");
			var studentList = "<h3>Appointments</h3>";
			for(var i=0; i < aptList.length; i++) {
				var apt = aptList[i];
				if(apt != "") {
				var studentTime = apt.split("~");
				var student = studentTime[0];
				var time = studentTime[1];
				var aptTags = aptHTMLStart + student  + aptHTMLTime + time + aptHTMLEnd;
				studentList = studentList.concat(aptTags);
				}
			}
			updatedApts = studentList;
		} else {
			updatedApts = emptyApts;
		}
		document.getElementById("appointments").innerHTML = updatedApts;
	});
}, 1000);

var timer;

function startTimer() {
	var timeLimit = document.getElementById("timeLimit").innerHTML;
	console.log(timeLimit);
	var tSeconds = parseInt(timeLimit) * 60000;
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
