var windowURL = window.location.href;
var splitURL = windowURL.split("/");
//url contains student
var courseIdAndLogin = splitURL[splitURL.length -1];
var splitCourseAndLogin = courseIdAndLogin.split("~");
var courseId = splitCourseAndLogin[0];
var login = splitCourseAndLogin[1];
var calledToHours = false;
console.log("course: " + courseId + " , login: " + login);


function getOnQueue() {
	var checkedQ = "";
	var qs = $('#checkbox :checked');
	console.log(qs);
	qs.each(function() {
		checkedQ = checkedQ.concat($(this).val() + "/");
    });
	console.log(qs.length);
	console.log(qs[0].innerText);
	var otherQ = document.getElementById("otherQuestion").value;
//	for(var i = 0; i < qs.length; i++) {
//		console.log(i);
//		if(qs[i].checked) {
//			checkedQ = checkedQ.concat(qs[i].innerText + ",");
//		}
//	}
	console.log(checkedQ);
	var postParameters = {"course": courseId, "login": login, 
			"questions": checkedQ,
			"otherQ": otherQ};
	$.post("/addStudentToQueue", postParameters, function(responseJSON) {
		console.log(responseJSON);
		/*
		 * If responseJSON is 1, there was no error in adding the student to the queue.
		 * Otherwise there was most likely a SQL error.
		 */
		if(responseJSON == 1) {
			alert("You've been added to the queue!");
			updateStatus();
		} else {
			console.log("SQL Error.");
		}
	});
}

//interval set to every second.
function updateStatus() {

	setInterval(checkStatus, 1000);
}

/*
 * Checks student's call status. If the ta has called them to hours,
 * an alert will appear.
 */
var checkStatus = function() {
	if(!calledToHours) {
	var postParameters = {"course": courseId, "login": login}; 
	$.post("/checkCallStatus", postParameters, function(responseJSON) {
		/*
		 * responseJSON is a boolean, returning true 
		 * if the student has been called to hours
		 * and false otherwise.
		 */
		if(responseJSON == 1) {
			calledToHours = true;
			alert("You've been called up for hours!");
			clearInterval(checkStatus);
		}
	});
	}
}