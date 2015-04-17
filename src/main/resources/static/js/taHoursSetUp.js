var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];

function startHours() {
	var postUrl = "/startHours/" + courseId;
	console.log("")
	$.post(postUrl, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			window.location.href=url;
		}
	});
}

function addQuestion() {
	
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