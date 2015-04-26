var windowURL = window.location.href;
var splitURL = windowURL.split("/");
//url contains student
var courseIdAndLogin = splitURL[splitURL.length -1];
var splitCourseAndLogin = courseIdAndLogin.split("~");
var courseId = splitCourseAndLogin[0];
var login = splitCourseAndLogin[1];

$(".confirmApt").bind('click', function(c) {
	//post to queue
	//success: send alert via email too (?)
	alert("You're signed up for your <time> appointment!");
});

$(".confirmAptContainer").hide(0);
$(".aptTime").bind('click', function(a) {
	$(".confirmAptContainer").show(1000);
});

$(".time").bind('click', function(e) {


 	var postParameters = {"time": $(this).text(), "login": login, "courseId": courseId};
 	$.post("/confirmAppointment", postParameters, function(responseJSON){
		responseObject = JSON.parse(responseJSON);
		resultWords = responseObject.results;
		questionsList = resultWords.split("!");
		var success = questionsList[0];
		if(resultWords == 1) {
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
