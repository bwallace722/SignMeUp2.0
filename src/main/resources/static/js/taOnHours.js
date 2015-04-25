var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];

console.log("here");

$(".studentOnQueue").bind('click', function(s) {
	console.log("hi");	
	var text = $(this).text();

	prompt("Please enter a message to call the student to hours", "You're up for hours!");
//when prompt is gone, save message

	var message;
	var text = text.trim();
	var textList = text.split(" ");
	var login = text.split(" ")[textList.length - 1];
	console.log(login);
	
	//TODO: find some way of parsing studentlogin from the div
	//that was clicked.
	var url = "/callStudent/" + courseId;
	var postParameters = {"studentLogin": login, 
			"message": message};
	$.post(url, postParameters, function(responseJSON) {
		//confirmation message
		if(responseJSON == 1) {
			alert(login + " has been called to hours");
		} else {
			alert(login + " cannot be reached. Maybe they signed out");
		}
	});
	
	
});

//updates the queue every 10 seconds.
//setInterval(updateQueue(), 1000);

setInterval(function(t) {
	console.log("running");
 	var postUrl = "/updateQueue/" + courseId;
	$.post(postUrl, function(responseJSON) {
		console.log(responseJSON);
//		console.log(responseJSON[0]);
		//redisplay queue
		//div class="queue" should be updated.
		
	});

}, 4000);

////once hours are on, update queue should run on an interval.
//function updateQueue() {
//
//}
