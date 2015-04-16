var user = document.getElementById("user").innerHTML;

var windowURL = window.location.href;
var splitURL = windowURL.split("/");
console.log(splitURL);

console.log(user);
function signUp() {
	var courses = document.getElementById("classDropdown");
	
	var courseSelected = courses.options[courses.selectedIndex].value;
	console.log(courseSelected);
	
	var role = $('input[name="inLineRadioOptions"]:checked').val();
	console.log(role);
	
	var postParameters = {"course": courseSelected, "role": role };
	var url = "/updateCourse/" + user;
	console.log(url);
	$.post(url, postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == 1) {
			window.location.href="/courses/" + user;
		}
	});
	
	
	
}