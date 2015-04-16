$(".container .labConfirm").hide(0);

//taQueueOnHours
//post or get requests needed: $("#studentsOnQueue"), $("#currQs"), $("#clinicSuggs"), $("#popularQs")
//click on each student --> open modal

//myclasses
//requests needed: $("#class_list")

//taOffHours landing
//timelimit --> $(".timeLimit")
//current subs --> populate


$(".labCheckOff").bind('click', function(l){
	$(".labHeader").hide(0);
	$(".labConfirm").show();
});

$(".hoursSignUp").bind('click', function(h) {
	//get questions from server
	$.post("/hoursSignUp", function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		$(".questionContainer").innerHTML = responseObject;
	});

});

// $(".allCategories").hide(0);

// $(".allCategoriesBtn").bind('click', function(a) {
// 	//populate all categories div and show
// 	$(".allCategories").show();
// });



$(".checkOffButton").bind('click', function(c) {
	//post to queue
	//success: send alert via email too (?)
	alert("You're signed up for lab!");
});

$(".confirmApt").bind('click', function(c) {
	//post to queue
	//success: send alert via email too (?)
	alert("You're signed up for your <time> appointment!");
});

$(".confirmAptContainer").hide(0);
$(".aptTime").bind('click', function(a) {
	$(".confirmAptContainer").show(1000);
});

$(".studentOnQueue").bind('click', function(s) {
prompt("Please enter a message to call the student to hours", "You're up for hours!");
});

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
	$.post("/addAssignment", asgns)
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

$(".time").bind('click', function(e) {


 	var postParameters = {"time": $(this).text()};
 	//to confirm appointment: send time and receive true from server
 	$.post("/confirmAppointment", postParameters, function(responseJSON){
		responseObject = JSON.parse(responseJSON);
		resultWords = responseObject.results;
		questionsList = resultWords.split("!");
		var success = questionsList[0];
		if(resultWords.equals("true")) {
			//figure out some animation for showing time
			//show time and reveal questions container

			$(".appointmentHeaderMessage").fadeOut(1000);
			$(".appointmentHeaderMessage").innerHTML = "Appointment Time";
			$(".appointmentHeaderMessage").fadeIn(1000);
			//this.val() = time selection
			$(".appointmentTimeHeader").fadeOut(1000);
			$(".appointmentTimeHeader").innerHTML = this.val();
			$(".appointmentTimeHeader").fadeIn(1000);

			$(".availAppointments").fadeOut(1000);

			//in resultWords, get questions and update questionsContainer
			//category divs must have their questions already written in
			var categories = $(".categories").innerHTML
			//$(".questionsContainer")
			// for(i = 1; i < questionsList.length; i++) {
			// 	categories +=
			// }
			$(".questionsContainer").innerHTML = categories;
			$(".confirmAptContainer").fadeIn(1000);
		} else {
			//show
		}

	});
});

