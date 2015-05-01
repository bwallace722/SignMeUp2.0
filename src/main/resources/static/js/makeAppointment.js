var windowURL = window.location.href;
var splitURL = windowURL.split("/");
//url contains student
var courseIdAndLogin = splitURL[splitURL.length -1];
var splitCourseAndLogin = courseIdAndLogin.split("~");
var courseId = splitCourseAndLogin[0];
var login = splitCourseAndLogin[1];

var chosenTimeString = "Your appointment time <br>";

$(".confirmApt").bind('click', function(c) {
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
	console.log(qs);
	qs.each(function() {
		checkedQ = checkedQ.concat($(this).val() + "/");
    });
	var otherQ = document.getElementById("otherQ").value;
	console.log(checkedQ);
	console.log(aptTime);
 	var postParameters = {"time": aptTime, 
 			"login": login, 
 			"courseId": courseId, 
 			"questions": checkedQ,
 			"otherQ": otherQ};
 	$.post("/confirmAppointment", postParameters, function(responseJSON){
		console.log(responseJSON + " = response");
		if(responseJSON == 1) {
			alert("You're all set for you appointment! Just head up to hours at "+aptTime+".");
			window.location.href = "/courses/"+login;
		} else {

			alert("It looks like this appointment time is taken. Try another");
		}

	});
}
