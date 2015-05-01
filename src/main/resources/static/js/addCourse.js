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
			alert("It appears you're a TA for this class. You can't be a student too!");
		} else if(responseJSON == "Student already in table." || responseJSON == "TA already in table.") {
			alert("You've already signed up for this class.");
		} else if(responseJSON == "TA is a student.") {
			alert("It appears you're a student in this class. You can't be a TA too!");
		} else {
			alert("Some error occured. Please try again.");
		}
	});
}

function createCourse() {
	window.location.href="/createCourse/"+login;
}