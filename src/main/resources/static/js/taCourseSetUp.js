var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var course = splitURL[splitURL.length -1];

var assignments = [];
var labs = [];
var exams = [];
var assignmentCount = 1;


$("#removeAsgnBtn").hide();

function addAsgnHTML() {

	if(addAsgn()) {
		assignmentCount++;
		$("#assignmentFormGroups").append("<div id=\"assignment" + assignmentCount + "\"><hr> <label class=\"col-sm-4\" for=\"assignmentName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"asgnName" + assignmentCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"asgnStartDate" + assignmentCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"asgnEndDate" + assignmentCount + "\" placeholder=\"Bacon\">\r\n\r\n<br></div>");
		$("#removeAsgnBtn").show();
	}
}

function addAsgn() {
	var toReturn = true;
	var n = document.getElementById("asgnName"+assignmentCount).value;
	var s = document.getElementById("asgnStartDate"+assignmentCount).value;
	var e = document.getElementById("asgnEndDate"+assignmentCount).value;

	if(validateFormGroup(n,s,e)) {
		var newAsgn = {name:n, start:s, end:e};
		assignments[assignments.length] = newAsgn;
	} else {
		toReturn = false;
	}
	return toReturn;
}


function saveAssignments() {
	if(assignmentCount > assignments.length) {
		if(addAsgn()) {
			postAsngs();
		}
	} else {
		postAsngs();
	}
}

function postAsngs() {
	for(i = 0; i < assignments.length; i++) {
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
	if(addExam()) {
		examCount++;
		$("#examFormGroups").append("<div id=\"exam" + examCount + "\"><hr> <label class=\"col-sm-4\" for=\"examName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"examName" + examCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"examStartDate" + examCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"examEndDate" + examCount + "\" placeholder=\"Bacon\">\r\n\r\n<br></div>");
		$("#removeExamBtn").show();
	}

}

function addExam() {
	var toReturn = true;
	var n = document.getElementById("examName"+examCount).value;
	var s = document.getElementById("examStartDate"+examCount).value;
	var e = document.getElementById("examEndDate"+examCount).value;
	
	if(validateFormGroup(n,s,e)) {
		var newExam = {name:n, start:s, end:e};
		exams[exams.length] = newExam;
	} else {
		toReturn = false;
	}
	return toReturn;
}


function saveExams() {
	if(examCounts > exams.length) {
		if(addExam()) {
			postExams();
		}
	} else {
		postExams();
	}
}

function postExams() {
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
	if(addLab()){
		labCount++;
		$("#examFormGroups").append("<div id=\"lab" + labCount + "\"><hr> <label class=\"col-sm-4\" for=\"labName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"labName" + labCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"labStartDate" + labCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"labEndDate" + labCount + "\" placeholder=\"Bacon\">\r\n\r\n<br></div>");
		$("#removeLabBtn").show();
	}
}
function addLab() {
	var toReturn = true;
	var n = document.getElementById("labName"+labCount).value;
	var s = document.getElementById("labStartDate"+labCount).value;
	var e = document.getElementById("labEndDate"+labCount).value;
	if(validateFormGroup(n,s,e)) {
		var newLab = {name:n, start:s, end:e};
		labs[lab.length] = newLab;
	} else {
		toReturn = false;
	}
	return toReturn;
}

function saveLabs() {
	if(labCounts > labs.length) {
		if(addLab()) {
			postLabs();
		}
	} else {
		postLabs();
	}
}

function postLabs() {
	for(i = 0; i < labs.length; i++) {
		var postParameters = {"course": course, "name": labs[i].name, "start":labs[i].start, "end":labs[i].end};
		console.log(postParameters);
		$.post("/saveLab", postParameters, function(responseJSON) {
			console.log(responseJSON);
		});
	}
}

function removeLab() {
	labCount--;
	if(labCount < labs.length) {
		labs[labs.length - 1] = null;
		
	}
	var newLabDiv = "#lab" + labCount;
	$(newLabDiv).hide();
	console.log(labCount);
	if(labCount <= 1) {
		$("#removeLabBtn").hide();
	}
}

function highlightField(field) {
	field.style.borderColor = "red";
	field.style.borderWidth = "2px";
}

function validateFormGroup(n,s,e) {
	var toReturn = true;
	
	if(n.length == 0) {
		toReturn = false;
		highlightField(n);
	}
	
	if(s.length == 0) {
		toReturn = false;
		highlightField(s);
	} else {
		var startInput = s.split("/");
		var sMonth = parseInt(startInput[0]);
		var sDay = parseInt(startInput[1]);
		var sYear = parseInt(startInput[2]);
		if(sMonth > 12 || sMonth < 1 || sDay > 31 || sDay < 1 || sYear < 2014 || sYear > 2016) {
			toReturn = false;
			highlightField(s);
		}
	}
	
	if(e.length == 0) {
		toReturn = false;
		highlightField(e);
	} else {
		var endInput = e.split("/");
		var eMonth = parseInt(endInput[0]);
		var eDay = parseInt(endInput[1]);
		var eYear = parseInt(endInput[2]);
		if(eMonth > 12 || eMonth < 1 || eDay > 31 || eDay < 1 || eYear < 2014 || eYear > 2016) {
			toReturn = false;
			highlightField(e);
		}
	}

	return toReturn;
}


function addSubQ() {
	var newQ = $("#newQuestion").val();
	document.getElementById('currSubs').innerHTML = document.getElementById('currSubs').innerHTML + newQ + "<br>";
}

function setTimeLimit() {
	document.getElementById('timeLimMinutes').innerHTML = $("#newTimeLim").val();
}