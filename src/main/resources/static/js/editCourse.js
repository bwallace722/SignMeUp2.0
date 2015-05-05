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
	var asgnNameForm = document.getElementById("asgnName");
	var asgnStartForm = document.getElementById("asgnStartDate");
	var asgnEndForm = document.getElementById("asgnEndDate");
	
	asgnNameForm.value = assName;
	asgnStartForm.value = assStart;
	asgnEndForm.value = assEnd;

});

function changeAss() {
	var asgnStartForm = document.getElementById("asgnStartDate");
	var asgnEndForm = document.getElementById("asgnEndDate");
	var postParameters = {"table": "assignment", "courseId": courseId, "name": assName, "start": assStartForm.value, "end": assEndForm.value};
	$.post("/changeAssessment", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
}

function removeAss() {
	var postParameters = {"table": "assignment", "courseId": courseId, "name": assName};
	$.post("/removeAssessment", postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
}

function addAss() {
	var newAsgnName = document.getElementById("newAsgnName");
	var newAsgnStart = document.getElementById("newAsgnStartDate");
	var newAsgnEnd = document.getElementById("newAsgnEndDate");
	var postParameters = {"table": "assignment", "courseId": courseId, "name": newAsgnName.value, "start": newAsgnStart.value, "end": newAsgnEnd.value};
	console.log(postParameters);
	$.post("/addAssessment", postParameters, function(responseJSON) {
		console.log(responseJSON  + " - here");
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
}

var labName;
var labStart;
var labEnd;

$(".lab").bind('click', function(e) {
	var cells = this.getElementsByTagName('td');
	
	labName = cells[0].innerHTML;
	labStart = cells[1].innerHTML;
	labEnd = cells[2].innerHTML;
	var labNameForm = document.getElementById("labName");
	var labStartForm = document.getElementById("labStartDate");
	var labEndForm = document.getElementById("labEndDate");
	
	labNameForm.value = labName;
	labStartForm.value = labStart;
	labEndForm.value = labEnd;
});

function changeLab() {
	var labStartForm = document.getElementById("labStartDate");
	var labEndForm = document.getElementById("labEndDate");
	var postParameters = {"table": "lab", "courseId": courseId, "name": labName, "start": labStartForm.value, "end": labEndForm.value};
	$.post("/changeAssessment", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
}

function removeLab() {
	var postParameters = {"table": "lab", "courseId": courseId, "name": labName};
	$.post("/removeAssessment", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
}

function addLab() {
	var newLabName = document.getElementById("newLabName");
	var newLabStart = document.getElementById("newLabStartDate");
	var newLabEnd = document.getElementById("newLabEndDate");
	var postParameters = {"table": "lab", "courseId": courseId, "name": newLabName.value, "start": newLabStart.value, "end": newLabEnd.value};
	$.post("/addAssessment", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
}


var examName;
var examStart;
var examEnd;

$(".exam").bind('click', function(e) {
	var cells = this.getElementsByTagName('td');
	
	examName = cells[0].innerHTML;
	examStart = cells[1].innerHTML;
	examEnd = cells[2].innerHTML;
	var examNameForm = document.getElementById("examName");
	var examStartForm = document.getElementById("examStartDate");
	var examEndForm = document.getElementById("examEndDate");
	
	examNameForm.value = examName;
	examStartForm.value = examStart;
	examEndForm.value = examEnd;

});

function changeExam() {
	var examStartForm = document.getElementById("examStartDate");
	var examEndForm = document.getElementById("examEndDate");
	var postParameters = {"table": "exam", "courseId": courseId, "name": examName, "start": examStartForm.value, "end": examEndForm.value};
	$.post("/changeAssessment", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
}

function removeExam() {
	var postParameters = {"table": "exam", "courseId": courseId, "name": examName};
	$.post("/removeAssessment", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
}

function addExam() {
	var newExamName = document.getElementById("newExamName");
	var newExamStart = document.getElementById("newExamStartDate");
	var newExamEnd = document.getElementById("newExamEndDate");
	var postParameters = {"table": "exam", "courseId": courseId, "name": newExamName.value, "start": newExamStart.value, "end": newExamEnd.value};
	$.post("/addAssessment", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			
		} else {
			alert("problems");
		}
	});
}