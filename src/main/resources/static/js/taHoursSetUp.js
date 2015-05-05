var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];
var login = getCookie("login");

console.log(document.cookie);
console.log(login);

function getCookie(name) {
	  var regexp = new RegExp("(?:^" + name + "|;\s*"+ name + ")=(.*?)(?:;|$)", "g");
	  var result = regexp.exec(document.cookie);
	  return (result === null) ? null : result[1];
}

function myCourses() {
	window.location.href = "/courses";
}

function startHours() {
	var postUrl;
	var hoursUrl = "/onHours/" + courseId;
	var hoursLength = document.getElementById("hoursLength");
	var hoursSplit = hoursLength.value.split(" ");
	var checkedQ = "";
	var qs = $('#noApt :checked');
	qs.each(function() {
		checkedQ = checkedQ.concat($(this).val());
    });
	console.log(checkedQ);
	if(checkedQ == "") {
		if (hoursLength.value != "") {
			var hours = hoursSplit[0];
			console.log(hours);
			console.log(isNaN(hours));
			if(!isNaN(hours)) {
				postUrl = "/startHours/" + courseId;
				var postParameters = {"duration": hours};
				$.post(postUrl, postParameters, function(responseJSON) {
					if(responseJSON == 1) {
						window.location.href= hoursUrl;
					} else {
						document.getElementById("resultBody").innerHTML = "Hours haven't been started yet. " +
						"Check your connection and try again";
						$("#resultModal").modal('show');
					}
				});
			} else {
				alert("Please input a valid integer to represent the number of hours for this TA session");
			}
		} else {
			
			alert("Please specify an hours duration or check the box for no appointments.");
		}
	} else {
		console.log(checkedQ);
		postUrl = "/startHoursNoApts/"+courseId;
		$.post(postUrl, function(responseJSON) {
			if(responseJSON == 1) {
				window.location.href= hoursUrl;
			} else {
				document.getElementById("resultBody").innerHTML = "Hours haven't been started yet. " +
				"Check your connection and try again";
				$("#resultModal").modal('show');
			}
		});
		
	}
}

function taCourseSetUp() {
	window.location.href = "/editCourse/"+courseId;
}


var currAsign = document.getElementById("currAsign");
console.log(currAsign.innerHTML);
if(currAsign.innerHTML == "none"){
	currAsign.innerHTML = "There is no assignment assigned for today.";
	$(".questionsDiv").hide();
}

function addQuestion() {
	var currSubs = $("#currSubs");
	var newQuestion = document.getElementById("newQuestion").value;
	var postUrl = "/addQuestionForHours/" + courseId;
	var assignmentName = document.getElementById("currAsign").innerHTML;
	var postParameters = {"newQuestion": newQuestion, "name": assignmentName};
	$.post(postUrl, postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			currSubs.append("<h6>" + newQuestion + "</h6>");
		} else {
			document.getElementById("resultBody").innerHTML = "We couldn't add your question. Refresh this page and try again.";
			$("#resultModal").modal('show');
		}
	});
}

function setTimeLimit() {
	var newTimeLim = document.getElementById("newTimeLim").value;
	
	var postUrl = "/setHoursTimeLimit/" + courseId;
	var postParameters = {"newTimeLimit": newTimeLim};

	$.post(postUrl, postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			document.getElementById("timeLimMinutes").innerHTML = newTimeLim;
		} else {
			document.getElementById("resultBody").innerHTML = "The time limit could not be changed for " + courseId + " hours." +
			"Try again.";
			$("#resultModal").modal('show');
		}
	});
	
}


function startAnalytics() {
	window.location.href="/analytics_home";
}