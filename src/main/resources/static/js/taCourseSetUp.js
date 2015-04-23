var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var course = splitURL[splitURL.length -1];

var assignments = [];
var labs = [];
var exams = [];
var assignmentCount = 1;


$("#removeAsgnBtn").hide();

function addAsgnHTML() {
	addAsgn();
	assignmentCount++;
	$("#assignmentFormGroups").append("<div id=\"assignment" + assignmentCount + "\"><hr> <label class=\"col-sm-4\" for=\"assignmentName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"asgnName" + assignmentCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"asgnStartDate" + assignmentCount + "\" placeholder=\"MM/DD/YYYY\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"asgnEndDate" + assignmentCount + "\" placeholder=\"MM/DD/YYYY\">\r\n\r\n<br></div>");
	$("#removeAsgnBtn").show();
}

function addAsgn() {
	var n = document.getElementById("asgnName"+assignmentCount).value;
	var s = document.getElementById("asgnStartDate"+assignmentCount).value;
	var e = document.getElementById("asgnEndDate"+assignmentCount).value;
	//TODO check if values are empty
	var newAsgn = {name:n, start:s, end:e};
	assignments[assignments.length] = newAsgn;
}


function saveAssignments() {
	if (assignments.length < assignmentCount) {
		addAsgn();
	}
	//TODO save last assignment
	console.log(assignments.length);
	for (i = 0; i < assignments.length; i++) {
		//console.log("inside");
		var postParameters = {"course": course, "name": assignments[i].name, "start":assignments[i].start, "end":assignments[i].end};
		console.log(postParameters);
		$.post("/saveAssignment", postParameters, function(responseJSON) {
			console.log(responseJSON);
		});
	}

}

function removeAsgn() {
	assignmentCount--;
	var newAssignmentDiv = "#assignment" + assignmentCount;
	$(newAssignmentDiv).hide();
	if(assignmentCount <= 1) {
		$("#removeAsgnBtn").hide();
	}
}

$("#examForm").hide();

$("#yesExamRadio").bind('click', function(y) {
 $("#examForm").fadeIn(1000);
});
$("#noExamRadio").bind('click', function(y) {
$("#examForm").fadeOut(200);
});

$("#removeExamBtn").hide();
var examCount = 1;

function addExamHTML() {
	addExam();
	examCount++;
	$("#examFormGroups").append("<div id=\"exam" + examCount + "\"><hr> <label class=\"col-sm-4\" for=\"examName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"examName" + examCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"examStartDate" + examCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"examEndDate" + examCount + "\" placeholder=\"Bacon\">\r\n\r\n<br></div>");
	$("#removeExamBtn").show();
}

function addExam() {
	var n = document.getElementById("examName"+examCount).value;
	var s = document.getElementById("examStartDate"+examCount).value;
	var e = document.getElementById("examEndDate"+examCount).value;
	//TODO check if values are empty
	var newExam = {name:n, start:s, end:e};
	exams[exams.length] = newExam;
}


function saveExams() {
	if(examCounts > exams.length) {
		addExam();
	}
	//TODO save last assignment
	for(i = 0; i < exams.length; i++) {
		var postParameters = {"course": course, "name": exams[i].name, "start":exams[i].start, "end":exams[i].end};
		console.log(postParameters);
		$.post("/saveExam", postParameters, function(responseJSON) {
			console.log(responseJSON);
		});
	}
}


function removeExam() {
	examCount--;
	var newExamDiv = "#exam" + examCount;
	$(newExamDiv).hide();
	if(examCount <= 1) {
		$("#removeExamBtn").hide();
	}
}

$("#labForm").hide();

$("#yesLabRadio").bind('click', function(y) {
 $("#labForm").fadeIn(1000);
});
$("#noLabRadio").bind('click', function(y) {
$("#labForm").fadeOut(200);
});

var labCount = 1;
$("#removeLabBtn").hide();

function addLabHTML() {
	addLab();
	labCount++;
	$("#examFormGroups").append("<div id=\"lab" + labCount + "\"><hr> <label class=\"col-sm-4\" for=\"labName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"labName" + labCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"labStartDate" + labCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"labEndDate" + labCount + "\" placeholder=\"Bacon\">\r\n\r\n<br></div>");
	$("#removeLabBtn").show();
}
function addLab() {
	var n = document.getElementById("labName"+labCount).value;
	var s = document.getElementById("labStartDate"+labCount).value;
	var e = document.getElementById("labEndDate"+labCount).value;
	//TODO check if values are empty
	var newLab = {name:n, start:s, end:e};
	labs[lab.length] = newLab;
}


function saveLabs() {
	if(labCounts > labs.length) {
		addLab();
	}
	//TODO save last assignment
	for(i = 0; i < lasb.length; i++) {
		var postParameters = {"course": course, "name": labs[i].name, "start":labs[i].start, "end":labs[i].end};
		console.log(postParameters);
		$.post("/saveLab", postParameters, function(responseJSON) {
			console.log(responseJSON);
		});
	}
}


function removeLab() {
	labCount--;
	var newLabDiv = "#lab" + labCount;
	$(newLabDiv).hide();
	console.log(labCount);
	if(labCount <= 1) {
		$("#removeLabBtn").hide();
	}
}

function addSubQ() {
	var newQ = $("#newQuestion").val();
	document.getElementById('currSubs').innerHTML = document.getElementById('currSubs').innerHTML + newQ + "<br>";
}

function setTimeLimit() {
	document.getElementById('timeLimMinutes').innerHTML = $("#newTimeLim").val();
}