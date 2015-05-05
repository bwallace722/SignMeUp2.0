var windowURL = window.location.href;
var splitURL = windowURL.split("/");
//url contains student
var courseIdAndLogin = splitURL[splitURL.length -1];
var splitCourseAndLogin = courseIdAndLogin.split("~");
var courseId = splitCourseAndLogin[0];
var login = splitCourseAndLogin[1];
var calledToHours = false;
console.log("course: " + courseId + " , login: " + login);

var currAsign = document.getElementById("currAsign");
console.log(currAsign.innerHTML);
if(currAsign.innerHTML == "<label>none</label>"){
	currAsign.innerHTML = "There is no assignment assigned for today.";
	currAsign.style.textAlign = "center";
	$("#checkbox").hide();
}

function signOut() {
	var url = "/signOut/" + login;

	$.post(url, function(responseJSON) {
	console.log(responseJSON);
		if(responseJSON == 1) {
			window.location.href = "/home";
		}
	});
}

function getOnQueue() {
	var checkedQ = "";
	var qs = $('#checkbox :checked');
	console.log(qs);
	qs.each(function() {
		checkedQ = checkedQ.concat($(this).val() + "/");
    });
	console.log(qs.length);
	console.log(document.getElementById("otherQuestion").value);
	var otherQ = document.getElementById("otherQuestion").value;

	console.log(checkedQ);
	var postParameters = {"course": courseId, "login": login, 
			"questions": checkedQ,
			"otherQ": otherQ};
	console.log(postParameters);
	$.post("/addStudentToQueue", postParameters, function(responseJSON) {
		console.log(responseJSON);
		/*
		 * If responseJSON is 1, there was no error in adding the student to the queue.
		 * Otherwise there was most likely a SQL error.
		 */
		if(responseJSON == 1) {
			alert("You've been added to the queue!");
			console.log("redirect");
			window.location.href = "/studentLanding/" + courseIdAndLogin;
			//updateStatus();
		} else if (responseJSON == 2) {
			alert("You are already on the queue!");

			console.log("redirect");
			window.location.href = "/studentLanding/" + courseIdAndLogin;
		} else {
			console.log("SQL Error.");
		}
	});
}

////interval set to every second.
//function updateStatus() {
//	setInterval(checkStatus, 1000);
//}
//
///*
// * Checks student's call status. If the ta has called them to hours,
// * an alert will appear.
// */
//var checkStatus = function() {
//	if(!calledToHours) {
//	var postParameters = {"course": courseId, "login": login}; 
//	$.post("/checkCallStatus", postParameters, function(responseJSON) {
//		/*
//		 * responseJSON is a boolean, returning true 
//		 * if the student has been called to hours
//		 * and false otherwise.
//		 */
//		if(responseJSON == 1) {
//			calledToHours = true;
//			alert("You've been called up for hours!");
//			clearInterval(checkStatus);
//		}
//	});
//	}
//}
