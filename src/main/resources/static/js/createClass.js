console.log("hi");

function createClass() {
	var courseID = document.getElementById("courseID").value;
	var courseName = document.getElementById("courseName").value;
	console.log(courseID);
	console.log(courseName);
	var url = "/addCourse/" + courseID;
	var postParameters = {"courseId": courseID, "courseName": courseName};
	
	$.post(url, postParameters, function(responseJSON) {
		if(responseJSON == 1) {
			window.location.href = "/courseSetUp/" + courseID;
		} else {
			alert("course could not be created. Try again");
		}
	});
	
	
}