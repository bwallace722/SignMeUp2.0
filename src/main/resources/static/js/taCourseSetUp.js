var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var course = splitURL[splitURL.length -1];

var assignments = [];
var labs = [];
var exams = [];
var assignmentCount = 1;
var examCount = 1;
var labCount = 1;


$("#removeAsgnBtn").hide();

function addAsgnHTML() {
	if(addAsgn()) {
		assignmentCount++;
		$("#assignmentFormGroups").append("<div id=\"assignment" + assignmentCount + "\"><hr> <label class=\"col-sm-4\" for=\"assignmentName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"asgnName" + assignmentCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"asgnStartDate" + assignmentCount + "\" placeholder=\"MM/DD/YYYY\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"asgnEndDate" + assignmentCount + "\" placeholder=\"MM/DD/YYYY\">\r\n\r\n<br></div>");
		$("#removeAsgnBtn").show();
	}
}

function addAsgn() {
	var toReturn = true;
	var n = document.getElementById("asgnName"+assignmentCount);
	var s = document.getElementById("asgnStartDate"+assignmentCount);
	var e = document.getElementById("asgnEndDate"+assignmentCount);

	if(validateFormGroup(n,s,e)) {
		var newAsgn = {name:n.value, start:s.value, end:e.value};
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

function addExamHTML() {
	if(addExam()) {
		examCount++;
		$("#examFormGroups").append("<div id=\"exam" + examCount + "\"><hr> <label class=\"col-sm-4\" for=\"examName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"examName" + examCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"examStartDate" + examCount + "\" placeholder=\"MM/DD/YYYY\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"examEndDate" + examCount + "\" placeholder=\"MM/DD/YYYY\">\r\n\r\n<br></div>");
		$("#removeExamBtn").show();
	}

}

function addExam() {
	var toReturn = true;
	var n = document.getElementById("examName"+examCount);
	var s = document.getElementById("examStartDate"+examCount);
	var e = document.getElementById("examEndDate"+examCount);
	
	if(validateFormGroup(n,s,e)) {
		var newExam = {name:n.value, start:s.value, end:e.value};
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

$("#removeLabBtn").hide();		console.log("attempting to add html");


function addLabHTML() {
	if(addLab()){
		labCount++;
		$("#labFormGroups").append("<div id=\"lab" + labCount + "\"><hr> <label class=\"col-sm-4\" for=\"labName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"labName" + labCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"labStartDate" + labCount + "\" placeholder=\"MM/DD/YYYY\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"labEndDate" + labCount + "\" placeholder=\"MM/DD/YYYY\">\r\n\r\n<br></div>");
		$("#removeLabBtn").show();
	}
}
function addLab() {
	var toReturn = true;
	var n = document.getElementById("labName"+labCount);
	var s = document.getElementById("labStartDate"+labCount);
	var e = document.getElementById("labEndDate"+labCount);
	if(validateFormGroup(n,s,e)) {
		var newLab = {name:n.value, start:s.value, end:e.value};
		labs[labs.length] = newLab;
	} else {
		toReturn = false;
	}
	return toReturn;
}

function saveLabs() {
	console.log("saving labs");
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
	
	if(n.value.length == 0) {
		toReturn = false;
		highlightField(n);
	}
	
	if(s.value.length == 0) {
		toReturn = false;
		highlightField(s);
	} else {
		var startInput = s.value.split("/");
		var sMonth = parseInt(startInput[0]);
		var sDay = parseInt(startInput[1]);
		var sYear = parseInt(startInput[2]);
		if(sMonth > 12 || sMonth < 1 || sDay > 31 || sDay < 1 || sYear < 2014 || sYear > 2016) {
			toReturn = false;
			highlightField(s);
		}
	}
	
	if(e.value.length == 0) {
		toReturn = false;
		highlightField(e);
	} else {
		var endInput = e.value.split("/");
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