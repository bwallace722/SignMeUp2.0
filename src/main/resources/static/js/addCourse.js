var windowURL = window.location.href;
var splitURL = windowURL.split("/");
var login = splitURL[splitURL.length -1];

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
		if(responseJSON == 1) {
			window.location.href="/courses/" + login;
		}
	});
}

function createCourse() {
	window.location.href="/createCourse";
}