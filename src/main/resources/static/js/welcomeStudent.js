$(".labConfirm").hide(0);

$(".labCheckOff").bind('click', function(l){
	$(".labHeader").hide(0);
	$(".labConfirm").show();
});

$(".checkOffButton").bind('click', function(c) {
	//post to queue
	//success: send alert via email too (?)
	
	
	
	alert("You're signed up for lab!");
});

$(".hoursSignUp").bind('click', function(h) {
	//get questions from server
	$.get("/signUpForHours");
	window.location.href="/signUpForHours";

});

//
//$(".hoursSignUp").bind('click', function(h) {
//	//get questions from server
//	$.post("/signUpForHours", function(responseJSON) {
//		responseObject = JSON.parse(responseJSON);
//		$(".questionContainer").innerHTML = responseObject;
//	});
//
//});

