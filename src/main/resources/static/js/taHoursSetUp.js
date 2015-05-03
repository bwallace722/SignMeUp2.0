var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];

function startHours() {
	var postUrl = "/startHours/" + courseId;
	var hoursUrl = "/onHours/" + courseId;
	var hoursLength = document.getElementById("hoursLength");
	var hoursSplit = hoursLength.value.split(" ");
	if (hoursLength.value != "") {
		var hours = hoursSplit[0];
		console.log(hours);
		console.log(isNaN(hours));
		if(!isNaN(hours)) {
			var postParameters = {"duration": hours};
			$.post(postUrl, postParameters, function(responseJSON) {
				if(responseJSON == 1) {
					window.location.href= hoursUrl;
				} else {
					alert("Hours haven't been started yet. " +
							"Check your connection and try again");
				}
			});
		} else {
			alert("Please input a valid integer to represent the number of hours for this TA session");
		}
	} else {
		alert("Please specify an hours duration.");
	}
}

function taCourseSetUp() {
	window.location.href = "/editCourse/"+courseId;
}


var currAsign = document.getElementById("currAsign");
console.log(currAsign.innerHTML);
if(currAsign.innerHTML == "none"){
	currAsign.innerHTML = "There is no assignment assigned for today.";
	$(".onHoursBlock").hide();
}

function addQuestion() {
	var currSubs = $("#currSubs");
	var newQuestion = document.getElementById("newQuestion").value;
	var postUrl = "/addQuestionForHours/" + courseId;
	var assignmentName = document.getElementById("currAsign").innerHTML;
	var postParameters = {"newQuestion": newQuestion, "name": assignmentName};
	$.post(postUrl, postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			alert("we've added your question");
			currSubs.append("<h6>" + newQuestion + "</h6>");
		} else {
			alert("We couldn't add your question. Try again.");
		}
	});
}

function setTimeLimit() {
	var newTimeLim = document.getElementById("newTimeLim").value;
	
	var postUrl = "/setHoursTimeLimit/" + courseId;
	var postParameters = {"newTimeLimit": newTimeLim};
	$.post(postUrl, postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			
			var timeLimMinutes = document.getElementById("timeLimMinutes").innerHTML;
			timeLimMinutes = newTimeLim;
		} else {
			alert("The time limit could not be changed for " + courseId + " hours." +
					"Try again.");
		}

	});
	
}


function startAnalytics() {
	window.location.href="/analytics_home";
}