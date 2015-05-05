var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var courseId = splitURL[splitURL.length -1];

$(".ass").bind('click', function(e) {
	console.log("here");
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



$(".lab").bind('click', function(e) {
	console.log("here");
	var cells = this.getElementsByTagName('td');
	
	var labName = cells[0].innerHTML;
	var labStart = cells[1].innerHTML;
	var labEnd = cells[2].innerHTML;
	var labNameForm = document.getElementById("labName");
	var labStartForm = document.getElementById("labStartDate");
	var labEndForm = document.getElementById("labEndDate");
	
	labNameForm.value = labName;
	labStartForm.value = labStart;
	labEndForm.value = labEnd;
});

function changeLab() {
	
}

function removeLab() {
	
}

$(".exam").bind('click', function(e) {
	console.log("here");
	var cells = this.getElementsByTagName('td');
	
	var examName = cells[0].innerHTML;
	var examStart = cells[1].innerHTML;
	var examEnd = cells[2].innerHTML;
	var examNameForm = document.getElementById("examName");
	var examStartForm = document.getElementById("examStartDate");
	var examEndForm = document.getElementById("examEndDate");
	
	examNameForm.value = examName;
	examStartForm.value = examStart;
	examEndForm.value = examEnd;

});

function changeExam() {
	
}

function removeExam() {
	
}