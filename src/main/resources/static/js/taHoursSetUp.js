var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];

function startHours() {
	var postUrl = "/startHours/" + courseId;
	var hoursUrl = "/onHours/" + courseId;
	$.post(postUrl, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			window.location.href= hoursUrl;
		} else {
			alert("Hours haven't been started yet. " +
					"Check your connection and try again");
		}
	});
}

function addQuestion() {
	var newQuestion = document.getElementById("newQuestion").value;

	var postUrl = "/addQuestionForHours/" + courseId;
	var postParameters {"newQuestion", newQuestion};
	$.post(postUrl, postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			alert("we've added your question");
			//TODO: update questions list
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
			alert("The time limit has been changed for " + courseId + " hours");
		} else {
			alert("The time limit could not be changed for " + courseId + " hours." +
					"Try again.");
		}

	});
	
}