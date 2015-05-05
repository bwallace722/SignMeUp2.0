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
var assStart;
var assEnd;

$(".ass").bind('click', function(e) {
	var cells = this.getElementsByTagName('td');
	
	assName = cells[0].innerHTML;
	assStart = cells[1].innerHTML;
	assEnd = cells[2].innerHTML;
	var asgnNameForm = document.getElementById("assRelease");
	var asgnStartForm = document.getElementById("asgnStartDate");
	var asgnEndForm = document.getElementById("asgnEndDate");
	
	asgnNameForm.innerHTML = asgnNameForm.innerHTML + " " + assName;
	asgnStartForm.value = assStart;
	asgnEndForm.value = assEnd;

});

function changeAss() {
	if(validateFormGroup(assName, assStart, assEnd)) {
	var postParameters = {"courseId": courseId, "assName": assName, "assStart": assStart, "assEnd": assEnd};
	$.post("/changeAss", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	}
}

function removeAss() {
	if(validateFormGroup(assName, assStart, assEnd)) {
	var postParameters = {"courseId": courseId, "assName": assName, "assStart": assStart, "assEnd": assEnd};
	$.post("/removeAss", postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	}
}

function addAss() {
	var newAsgnName = document.getElementById("newAsgnName");
	var newAsgnStart = document.getElementById("newAsgnStartDate");
	var newAsgnEnd = document.getElementById("newAsgnEndDate");
	if(validateFormGroup(newAsgnName, newAsgnStart, newAsgnEnd)) {
	var postParameters = {"courseId": courseId, "assName": newAsgnName, "assStart": newAsgnStart, "assEnd": newAsgnEnd};
	$.post("/addAss", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
	}
}

var labName;
var labStart;
var labEnd;

$(".lab").bind('click', function(e) {
	console.log("here");
	var cells = this.getElementsByTagName('td');
	
	labName = cells[0].innerHTML;
	labStart = cells[1].innerHTML;
	labEnd = cells[2].innerHTML;
	var labNameForm = document.getElementById("labRelease");
	var labStartForm = document.getElementById("labStartDate");
	var labEndForm = document.getElementById("labEndDate");
	
	labNameForm.innerHTML = labNameForm.innerHTML + " " + labName;
	labStartForm.value = labStart;
	labEndForm.value = labEnd;
});

function changeLab() {
	if(validateFormGroup(labName, labStart, labEnd)) {
	var postParameters = {"courseId": courseId, "labName": labName, "labStart": labStart, "labEnd": labEnd};
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
	if(validateFormGroup(labName, labStart, labEnd)) {
	var postParameters = {"courseId": courseId, "labName": labName, "labStart": labStart, "labEnd": labEnd};
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
	var postParameters = {"courseId": courseId, "labName": newLabName, "labStart": newLabStart, "labEnd": newLabEnd};
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
var examStart;
var examEnd;

$(".exam").bind('click', function(e) {
	console.log("here");
	var cells = this.getElementsByTagName('td');
	
	examName = cells[0].innerHTML;
	examStart = cells[1].innerHTML;
	examEnd = cells[2].innerHTML;
	var examNameForm = document.getElementById("examRelease");
	var examStartForm = document.getElementById("examStartDate");
	var examEndForm = document.getElementById("examEndDate");
	
	examNameForm.innerHTML = 	examNameForm.innerHTML + " " + examName;
	examStartForm.value = examStart;
	examEndForm.value = examEnd;

});

function changeExam() {
	if(validateFormGroup(examName, examStart, examEnd)) {
	var postParameters = {"courseId": courseId, "examName": examName, "examStart": examStart, "examEnd": examEnd};
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
	if(validateFormGroup(examName, examStart, examEnd)) {
	var postParameters = {"courseId": courseId, "examName": examName, "examStart": examStart, "examEnd": examEnd};
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


function validateDate(s) {
	if(s.value.length == 0) {
		toReturn = false;
		highlightField(s);
	} else {
		var startInput = s.value.split("/");
		var sMonth = parseInt(startInput[1]);
		var sDay = parseInt(startInput[2]);
		var sYear = parseInt(startInput[0]);
		if(sMonth > 12 || sMonth < 1 || sDay > 31 || sDay < 1 || sYear < 2014 || sYear > 2016) {
			toReturn = false;
			highlightField(s);
		}
	}
}

function validateFormGroup(n,s,e) {
	var toReturn = true;
	
	if(n.value.length == 0) {
		toReturn = false;
		highlightField(n);
	}
	
	if(!validateDate(s) || !validateDate(e)) {
		toReturn = false;
	}
	

	return toReturn;
}


