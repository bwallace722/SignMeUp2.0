var windowURL = window.location.href;
var splitURL = windowURL.split("/");
//url contains student
var courseIdAndLogin = splitURL[splitURL.length -1];
var splitCourseAndLogin = courseIdAndLogin.split("~");
var courseId = splitCourseAndLogin[0];
var login = splitCourseAndLogin[1];

var chosenTimeString = "Your appointment time <br>";

$(".confirmApt").bind('click', function(c) {
	//post to queue
	//success: send alert via email too (?)
	alert("You're signed up for your <time> appointment!");
});

var currAsign = document.getElementById("currAss");
console.log(currAsign.innerHTML);
if(currAsign.innerHTML == "none"){
	currAsign.innerHTML = "There is no assignment assigned for today.";
	$("#questions").hide();
}

$(".confirmAptContainer").hide(0);
var aptTime;
$(".aptTime").bind('click', function(a) {
	var timeClicked = $(this)[0].innerHTML;
	//NIFEMI - DO YOU WANT "PM" OR NO?
	var time= timeClicked.split(" ")[0];
	//var time = timeClicked;
	console.log(time);
	var timeString = chosenTimeString + $(this)[0].innerHTML;
	aptTime = time;
	console.log($(this)[0].innerHTML);
	var timeString = chosenTimeString + $(this)[0].innerHTML;
	aptTime = $(this).val();
	var chosenTime = document.getElementById("chosenTime");
	console.log(timeString);
	chosenTime.innerHTML = timeString;
	$(".confirmAptContainer").show(1000);
});

function confirmApt() {
	
	var checkedQ = "";
	var qs = $('#questionsForApt :checked');
	console.log(qs);
	qs.each(function() {
		checkedQ = checkedQ.concat($(this).val() + "/");
    });
	var otherQ = document.getElementById("otherQ").value;
	console.log(checkedQ);
 	var postParameters = {"time": aptTime, 
 			"login": login, 
 			"courseId": courseId, 
 			"questions": checkedQ,
 			"otherQ": otherQ};
 	$.post("/confirmAppointment", postParameters, function(responseJSON){
		console.log(responseJSON + " = response");
		if(responseJSON == 1) {
			alert("You're all set for you appointment! Just head up to hours at "+aptTime+".");
			//figure out some animation for showing time
			//show time and reveal questions container
		} else if (responseJSON == 2) {
			alert("You're already on the queue. You can't make an appointment and be signed up for hours.");
 		} else {

			alert("It looks like this appointment time is taken. Try another");
		}

	});
}
