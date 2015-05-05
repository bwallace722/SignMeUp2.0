var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var login = splitURL[splitURL.length -1];


function signOut() {
	var url = "/signOut/" + login;

	$.post(url, function(responseJSON) {
	console.log(responseJSON);
		if(responseJSON == 1) {
			window.location.href = "/home";
		}
	});
}

function signUp() {
	var courses = document.getElementById("classDropdown");
	
	var courseSelected = courses.options[courses.selectedIndex].value;
	console.log(courseSelected);
	
	var role = $('input[name="inLineRadioOptions"]:checked').val();
	console.log(role);
	
	var postParameters = {"courseSelected": courseSelected, "role": role };
	var url = "/updateCourse/" + login;
	console.log(url);
	$.post(url, postParameters, function(responseJSON) {
		console.log(responseJSON);
		console.log("fsfd");
		if(responseJSON == "Success.") {
			window.location.href="/courses/" + login;
		} else if(responseJSON == "Student is a TA.") {
			document.getElementById("resultBody").innerHTML = "It appears you're a TA for this class. You can't be a student too!";
			$("#resultModal").modal('show');
		} else if(responseJSON == "Student already in table." || responseJSON == "TA already in table.") {
			document.getElementById("resultBody").innerHTML = "You've already signed up for this class.";
			$("#resultModal").modal('show');
		} else if(responseJSON == "TA is a student.") {
			document.getElementById("resultBody").innerHTML = "It appears you're a student in this class. You can't be a TA too!";
			$("#resultModal").modal('show');
		} else {
			document.getElementById("resultBody").innerHTML = "Some error occured. Please try again.";
			$("#resultModal").modal('show');
		}
	});
}

function createCourse() {
	window.location.href="/createCourse/"+login;
}