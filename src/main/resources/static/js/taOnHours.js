var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];


	var queueHTMLStart = "<div class=\"row studentOnQueue\">" +
    "<div class=\"col-sm-8 col-sm-push-1\"><h5>";
	var queueHTMLEnd = "</h5></div><br><hr>";

function returnToSetup() {
	window.location.href = "/taHoursSetUp/" + courseId;
}

console.log("here");

$(".studentOnQueue").bind('click', function(s) {
	console.log("hi");	
	var text = $(this).text();

	var message = prompt("Please enter a message to call the student to hours", "You're up for hours!");
//when prompt is gone, save message

	var text = text.trim();
	var textList = text.split(" ");
	var login = text.split(" ")[textList.length - 1];
	console.log(login);
	
	//TODO: find some way of parsing studentlogin from the div
	//that was clicked.
	var url = "/callStudent/" + courseId;
	if(message) {
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
	}
	
});

//updates the queue every 10 seconds.
//setInterval(updateQueue(), 1000);

setInterval(function(t) {
 	var postUrl = "/updateQueue/" + courseId;
	$.post(postUrl, function(responseJSON) {
		console.log(responseJSON);
		var queueString = responseJSON.substring(1,responseJSON.length-1);
		console.log(queueString);
		if(queueString) {
			var queueList = queueString.split(",");
			var queue = document.getElementByClass("queue");
			var studentList = "";
			for(int i=0; i < queueList.length; i++) {
				var student = queueList[i];
				console.log(student);
				studentList.append(queueHTMLStart + student + queueHTMLEnd);
			}
			queue.innerHTML = studentList;
		}
		
	});

}, 4000);

////once hours are on, update queue should run on an interval.
//function updateQueue() {
//
//}
