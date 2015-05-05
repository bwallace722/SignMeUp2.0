var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];

var exams = document.getElementById("examsCoursePanel").innerHTML;
if(exams.trim() == "none") {
	document.getElementById("examsCoursePanel").innerHTML = "<h4>There are no exams for this course.</h4>";

}

var assignments = document.getElementById("assignmentsCoursePanel").innerHTML;
if(assignments.trim() == "none") {
	document.getElementById("assignmentsCoursePanel").innerHTML = "<h4>There are no assignments for this course.</h4>";
}

var labs = document.getElementById("labsCoursePanel").innerHTML;
if(labs.trim() == "none") {
	document.getElementById("labsCoursePanel").innerHTML = "<h4>There are no labs for this course.</h4>";
}


function startAnalytics() {
	window.location.href="/analytics_home";
}

var assName;

$(".ass").bind('click', function(e) {
	var cells = this.getElementsByTagName('td');
	
	assName = cells[0].innerHTML;
	var assStart = cells[1].innerHTML;
	var assEnd = cells[2].innerHTML;
	var asgnNameForm = document.getElementById("assRelease");
	
	asgnNameForm.innerHTML = "Edit " + assName;
	asgnStartForm.value = assStart;
	asgnEndForm.value = assEnd;

});

function changeAss() {
	var asgnStartForm = document.getElementById("asgnStartDate");
	var asgnEndForm = document.getElementById("asgnEndDate");
	if(validateDate(asgnStartForm, "asgnStartDate") && validateDate(asgnEndForm, "asgnEndDate")) {
	var postParameters = {"courseId": courseId, "assName": assName, "assStart": asgnStartForm.value, "assEnd": asgnEndForm.value};
	$.post("/changeAss", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	} else {
		alert("There was a problem with your form, please attempt to edit the assignment again.");
	}
}

function removeAss() {
	var asgnStartForm = document.getElementById("asgnStartDate");
	var asgnEndForm = document.getElementById("asgnEndDate");
	if(validateDate(asgnStartForm, "asgnStartDate") && validateDate(asgnEndForm, "asgnEndDate")) {
	var postParameters = {"courseId": courseId, "assName": assName, "assStart": asgnStartForm.value, "assEnd": asgnEndForm.value};
	$.post("/removeAss", postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	} else {
		alert("There was a problem with your form, please attempt to edit the assignment again.");
	}
}

function addAss() {
	var newAsgnName = document.getElementById("newAsgnName");
	var newAsgnStart = document.getElementById("newAsgnStartDate");
	var newAsgnEnd = document.getElementById("newAsgnEndDate");
	if(validateFormGroup(newAsgnName, newAsgnStart, newAsgnEnd)) {
	var postParameters = {"courseId": courseId, "assName": newAsgnName.value, "assStart": newAsgnStart.value, "assEnd": newAsgnEnd.value};
	$.post("/addAss", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	} else {
		alert("There was a problem with your form, please attempt to create the assignment again.");
	}
}

var labName;

$(".lab").bind('click', function(e) {
	console.log("here");
	var cells = this.getElementsByTagName('td');
	
	labName = cells[0].innerHTML;
	var labStart = cells[1].innerHTML;
	var labEnd = cells[2].innerHTML;
	var labNameForm = document.getElementById("labRelease");
	var labStartForm = document.getElementById("labStartDate");
	var labEndForm = document.getElementById("labEndDate");
	
	labNameForm.innerHTML = "Edit " + labName;
	labStartForm.value = labStart;
	labEndForm.value = labEnd;
});

function changeLab() {
	var labStartForm = document.getElementById("labStartDate");
	var labEndForm = document.getElementById("labEndDate");
	if(validateDate(labStartForm, "labStartDate") && validateDate(labEndForm, "labEndDate")) {
	var postParameters = {"courseId": courseId, "labName": labName, "labStart": labStartForm.value, "labEnd": labEndForm.value};
	$.post("/changeLab", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	}
}

function removeLab() {
	var labStartForm = document.getElementById("labStartDate");
	var labEndForm = document.getElementById("labEndDate");
	if(validateDate(labStartForm, "labStartDate") && validateDate(labEndForm, "labEndDate")) {
		var postParameters = {"courseId": courseId, "labName": labName, "labStart": labStartForm.value, "labEnd": labEndForm.value};
	$.post("/removeLab", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	}
}

function addLab() {
	var newLabName = document.getElementById("newLabName");
	var newLabStart = document.getElementById("newLabStartDate");
	var newLabEnd = document.getElementById("newLabEndDate");
	if(validateFormGroup(newLabName, newLabStart, newLabEnd)) {
	var postParameters = {"courseId": courseId, "labName": newLabName.value, "labStart": newLabStart.value, "labEnd": newLabEnd.value};
	$.post("/addLab", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	}
}


var examName;

$(".exam").bind('click', function(e) {
	console.log("here");
	var cells = this.getElementsByTagName('td');
	
	examName = cells[0].innerHTML;
	var examStart = cells[1].innerHTML;
	var examEnd = cells[2].innerHTML;
	var examNameForm = document.getElementById("examRelease");
	var examStartForm = document.getElementById("examStartDate");
	var examEndForm = document.getElementById("examEndDate");
	
	examNameForm.innerHTML = 	"Edit " + examName;
	examStartForm.value = examStart;
	examEndForm.value = examEnd;

});

function changeExam() {
	var examStartForm = document.getElementById("examStartDate");
	var examEndForm = document.getElementById("examEndDate");
	if(validateDate(examStartForm, "examStartDate") && validateDate(examEndForm, "examEndDate")) {
	var postParameters = {"courseId": courseId, "examName": examName, "examStart": examStartForm.value, "examEnd": examEndForm.value};
	$.post("/changeExam", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	}
}

function removeExam() {
	var examStartForm = document.getElementById("examStartDate");
	var examEndForm = document.getElementById("examEndDate");
	if(validateDate(examStartForm, "examStartDate") && validateDate(examEndForm, "examEndDate")) {
	var postParameters = {"courseId": courseId, "examName": examName, "examStart": examStartForm.value, "examEnd": examEndForm.value};
	$.post("/removeExam", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	}
}

function addExam() {
	var newExamName = document.getElementById("newExamName");
	var newExamStart = document.getElementById("newExamStartDate");
	var newExamEnd = document.getElementById("newExamEndDate");
	if(validateFormGroup(newExamName, newExamStart, newExamEnd)) {
	var postParameters = {"courseId": courseId, "examName": newExamName, "examStart": newExamStart, "examEnd": newExamEnd};
	$.post("/addExam", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	}
}
function highlightField(field, id) {
	field.style.borderColor = "red";
	field.style.borderWidth = "2px";
	console.log(field);
	field.title = "Please check this format.";
	id = "#" + id;
	$(id).tooltip('show');
}

function validateDate(s) {
	console.log(s.value);
	if(s.value.length == 0) {
		toReturn = false;
		highlightField(s);
	} else {
		var startInput = s.value.split("-");
		var sMonth = parseInt(startInput[1]);
		console.log(sMonth);
		var sDay = parseInt(startInput[2]);
		console.log(sDay);
		var sYear = parseInt(startInput[0]);
		console.log(sYear);
		if(sMonth > 12 || sMonth < 1 || sDay > 31 || sDay < 1 || sYear < 2014 || sYear > 2016) {
			toReturn = false;
			highlightField(s);
		}
	}
}



function validateFormGroup(n,s,e) {
	var toReturn = true;
	console.log()
	if(n.value.length == 0) {
		toReturn = false;
		highlightField(n);
	}
	
	if(!validateDate(s) || !validateDate(e)) {
		toReturn = false;
	}
	

	return toReturn;
}

function returnToHours() {
	window.location.href = "/taHoursSetUp/"+courseId;
}

