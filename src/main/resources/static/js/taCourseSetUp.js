var assignments = [];
var labs = [];
var exams = [];
var assignmentCount = 1;

$("#removeAsgnBtn").hide();

function addAsgnHTML() {
	assignmentCount++;
	var currAsgn = document.getElementById('assignmentFormGroups').innerHTML;
	currAsgn = currAsgn + "<div id=\"assignment" + assignmentCount + "\"><hr> <label class=\"col-sm-4\" for=\"assignmentName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"name" + assignmentCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"startDate" + assignmentCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"endDate" + assignmentCount + "\" placeholder=\"Bacon\">\r\n\r\n<br></div>";
	document.getElementById('assignmentFormGroups').innerHTML = currAsgn;
	$("#removeAsgnBtn").show();
}

function makeAsgnArray() {
	var asgns = [];
	var i;
	for (i=1; i<=assignmentCount; i++) {
		var n = document.getElementById("name"+i).value;
		var s = document.getElementById("startDate"+i).value;
		var e = document.getElementById("endDate"+i).value;
		var assignment = {name:n, start:s, end:e};
		asgns[i-1] = assignment;
	}
	$.post("/addAssignment", asgns);
		//if response object is null
        //COURSE ID TO BE PASSED IN AS PART OF ASSIGNMENT OBJECT.
}

function saveAssignment() {
	var assignmentFormGroups = document.getElementById("assignmentFormGroups").childNodes;
	console.log(assignmentFormGroups.length);
	
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
	examCount++;
	var currExam = document.getElementById('examFormGroups').innerHTML;
	currExam = currExam + "<div id=\"exam" + examCount + "\"><hr> <label class=\"col-sm-4\" for=\"examName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"name" + examCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"startDate" + examCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"endDate" + examCount + "\" placeholder=\"Bacon\">\r\n\r\n<br></div>";
	document.getElementById('examFormGroups').innerHTML = currExam;
	$("#removeExamBtn").show();
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
	labCount++;
	var currLab = document.getElementById('labFormGroups').innerHTML;
	currLab = currLab + "<div id=\"lab" + labCount + "\"><hr> <label class=\"col-sm-4\" for=\"labName\">Name<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"name" + examCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4\" for=\"start\">Start Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"startDate" + examCount + "\" placeholder=\"Bacon\">\r\n<br>\r\n<br>\r\n<label class=\"col-sm-4 col-push-1\" for=\"end\">End Date<\/label>\r\n<input class=\"col-sm-6\" type=\"text\" class=\"form-control\" id=\"endDate" + examCount + "\" placeholder=\"Bacon\">\r\n\r\n<br></div>";
	document.getElementById('labFormGroups').innerHTML = currLab;
	console.log(labCount);
	$("#removeLabBtn").show();
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