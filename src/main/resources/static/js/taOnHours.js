var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];


$(".studentOnQueue").bind('click', function(s) {

prompt("Please enter a message to call the student to hours", "You're up for hours!");
//when prompt is gone, save message
	var text = $(this).text();
	var message;
	console.log(text);
	
	//TODO: find some way of parsing studentlogin from the div
	//that was clicked.
	var studentLogin = "";
	var postParameters = {"course": courseId, 
			"studentLogin": studentLogin, 
			"message": message};
	$.post("/callStudent", postParameters, function(responseJSON) {
		//confirmation message
		if(responseJSON) {
			alert(studentLogin + " has been called to hours");
		} else {
			alert(studentLogin + " cannot be reached. Maybe they signed out");
		}
	});
	
	
});

//updates the queue every 10 seconds.
setInterval(updateQueue(), 10000);

//once hours are on, update queue should run on an interval.
function updateQueue() {
 	var postParameters = {"course": courseId};
	$.post("/updateQueue", postParameters, function(responseJSON) {
		//redisplay queue
		//div class="queue" should be updated.
	});
}
