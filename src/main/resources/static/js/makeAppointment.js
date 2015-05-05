var windowURL = window.location.href;
var splitURL = windowURL.split("/");
//url contains student
var courseId = splitURL[splitURL.length -1];
var login = getCookie("login");

var chosenTimeString = "Your appointment time <br>";
var currAsign = document.getElementById("currAss");
if(currAsign.innerHTML == "none"){
	currAsign.innerHTML = "There is no assignment assigned for today.";
	$("#questionsForApt").hide();
}
function getCookie(name) {
	  var regexp = new RegExp("(?:^" + name + "|;\s*"+ name + ")=(.*?)(?:;|$)", "g");
	  var result = regexp.exec(document.cookie);
	  return (result === null) ? null : result[1];
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

var successMessage = "You're all set for you appointment! Just head up to hours at "+aptTime+".";
var onQueueMessage = "You're already on the queue. You can't make an appointment and be signed up for hours.";
var takenMessage = "It looks like this appointment time is taken. Try another";

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
			document.getElementById("resultBody").innerHTML = successMessage;
			$("#resultModal").modal('show');
			//figure out some animation for showing time
			//show time and reveal questions container
		} else if (responseJSON == 2) {
			document.getElementById("resultBody").innerHTML = onQueueMessage;
			$("#resultModal").modal('show');
 		} else {
			document.getElementById("resultBody").innerHTML = takenMessage;
			$("#resultModal").modal('show');
		}

	});
}

function returnToCourse() {
	window.location.href = "/studentLanding/"+courseIdAndLogin;
}
function redirect() {
	window.location.href = "/studentLanding/"+courseIdAndLogin;
}


