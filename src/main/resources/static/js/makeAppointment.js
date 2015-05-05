var windowURL = window.location.href;
var splitURL = windowURL.split("/");
//url contains student
var courseIdAndLogin = splitURL[splitURL.length -1];
var splitCourseAndLogin = courseIdAndLogin.split("~");
var courseId = splitCourseAndLogin[0];
var login = splitCourseAndLogin[1];

var successMessage = "You're already on the queue. You can't make an appointment and be signed up for hours.";
var takenMessage = "It looks like this appointment time is taken. Try another";

var chosenTimeString = "Your appointment time <br>";
var currAsign = document.getElementById("currAss");
if(currAsign.innerHTML == "none"){
	currAsign.innerHTML = "There is no assignment assigned for today.";
	$("#questionsForApt").hide();
}

var aptTimes = document.getElementById("aptTimes");
console.log(aptTimes.innerHTML);
if(aptTimes.innerHTML == "") {
	var header = document.getElementById("aptHeader");
	header.innerHTML = "There is no available appointments for today.";
	console.log(header);
	$("#aptTimes").hide();
}

function myCourses() {
	window.location.href = "/courses/" + login;
}

$(".confirmApt").bind('click', function(c) {
	alert("You're signed up for your <time> appointment!");
});

var currAsign = document.getElementById("currAss");
if(currAsign.innerHTML == "none"){
	currAsign.innerHTML = "There is no assignment assigned for today.";
	$("#questions").hide();
}

$(".confirmAptContainer").hide(0);
var aptTime;
$(".aptTime").bind('click', function(a) {
	var timeClicked = $(this)[0].innerHTML;
	var time= timeClicked.split(" ")[0];
	var timeString = chosenTimeString + $(this)[0].innerHTML;
	aptTime = $(this)[0].innerHTML;
	var timeString = chosenTimeString + $(this)[0].innerHTML;
	var chosenTime = document.getElementById("chosenTime");
	chosenTime.innerHTML = timeString;
	$(".confirmAptContainer").show(1000);
});

function confirmApt() {
	
	var checkedQ = "";
	var qs = $('#questionsForApt :checked');
	qs.each(function() {
		checkedQ = checkedQ.concat($(this).val() + "/");
    });
	var otherQ = document.getElementById("otherQ").value;
 	var postParameters = {"time": aptTime, 
 			"login": login, 
 			"courseId": courseId, 
 			"questions": checkedQ,
 			"otherQ": otherQ};
 	$.post("/confirmAppointment", postParameters, function(responseJSON){
		if (responseJSON == 1) {
			alert("You're all set for you appointment! Just head up to hours at "+aptTime+".");
			window.location.href = "/courses/"+login;
			//figure out some animation for showing time
			//show time and reveal questions container
		} else if (responseJSON == 2) {
			alert(successMessage);
 		} else {
			alert(takenMessage);
		}

	});
}
