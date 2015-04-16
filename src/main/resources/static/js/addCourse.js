function signUp() {
	var courses = document.getElementById("classDropdown");
	
	var courseSelected = courses.options[courses.selectedIndex].value;
	console.log(courseSelected);
	
	var role = $('input[name="inLineRadioOptions"]:checked').val();
	console.log(role);
	
	var postParameters = {"course": courseSelected, "role": role };
	
	$.post("/classes", postParameters, function(responseJSON) {
		console.log(responseJSON);
	});
	
	
	
}