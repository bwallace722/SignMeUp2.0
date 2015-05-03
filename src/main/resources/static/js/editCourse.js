var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];

$(".clickable-row").bind('click', function(e) {
	var cells = this.getElementsByTagName('td');
	
	var assName = cells[0].innerHTML;
	var assStart = cells[1].innerHTML;
	var assEnd = cells[2].innerHTML;
	var asgnNameForm = document.getElementById("asgnName");
	var asgnStartForm = document.getElementById("asgnStartDate");
	var asgnEndForm = document.getElementById("asgnEndDate");
	
	asgnNameForm.value = assName;
	asgnStartForm.value = assStart;
	asgnEndForm.value = assEnd;

});

function changeAss() {
	
}

function removeAss() {
	
}