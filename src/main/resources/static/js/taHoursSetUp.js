var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];

//var params = {"courseId": courseId};
//var url = "/getQuestions/"+courseId;
//$.get(url, function(responseJSON) {
//	console.log(responseJSON);
//	//ADD QUESTIONS
//});

function startHours() {
	var postUrl = "/startHours/" + courseId;
	var hoursUrl = "/onHours/" + courseId;
	var hoursLength = document.getElementById("hoursLength").value;
	console.log(hoursLength);
	var postParameters = {"duration": hoursLength};
	$.post(postUrl, postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			window.location.href= hoursUrl;
		} else {
			alert("Hours haven't been started yet. " +
					"Check your connection and try again");
		}
	});
}

function taCourseSetUp() {
	window.location.href = "/courseSetUp/"+courseId;
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
			alert("The time limit has been changed for " + courseId + " hours");
		} else {
			alert("The time limit could not be changed for " + courseId + " hours." +
					"Try again.");
		}

	});
	
}