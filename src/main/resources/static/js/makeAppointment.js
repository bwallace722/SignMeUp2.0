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
	var qs = $('#checkbox :checked');
	console.log(qs);
	qs.each(function() {
		checkedQ = checkedQ.concat($(this).val() + "/");
    });

	var otherQ = document.getElementById("otherQuestion").value;
	console.log(checkedQ);
 	var postParameters = {"time": aptTime, 
 			"login": login, 
 			"courseId": courseId, 
 			"questions": checkedQ,
 			"otherQ": otherQ};
 	$.post("/confirmAppointment", postParameters, function(responseJSON){
		responseObject = JSON.parse(responseJSON);
		console.log(responseJSON + " = response");
		resultWords = responseObject.results;
		console.log(resultWords + " - hi");
		questionsList = resultWords.split("!");
		var success = questionsList[0];
		if(resultWords == 1) {
			//figure out some animation for showing time
			//show time and reveal questions container


		} else {
			//show
		}

	});
}
