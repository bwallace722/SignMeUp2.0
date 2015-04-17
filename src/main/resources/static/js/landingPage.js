$("#signIn").hide(0);
$("#signUp").hide(0);

function showSignUp() {
	$(".landingHeader").fadeOut(300);
	$("#signInButton").fadeOut(300);
	$("#signUpButton").fadeOut(300);
	$("#signUp").fadeIn(1000);
}

function showSignIn() {
	$(".landingHeader").fadeOut(300);
	$("#signInButton").fadeOut(300);
	$("#signUpButton").fadeOut(300);
	$("#signIn").fadeIn(1000);
}

$("#switchToSignUp").bind('click', function(s) {
	$("#signIn").fadeOut(300);
	$("#signUp").fadeIn(1000);
});


$("#switchToSignIn").bind('click', function(s) {
	$("#signUp").fadeOut(300);
	$("#signIn").fadeIn(1000);
});

function signUp() {
	
	var name = document.getElementById("name").value;
	var login = document.getElementById("loginSignUp").value;
	var email = document.getElementById("email").value;
	var password = document.getElementById("pass").value;
	var confirm = document.getElementById("confirmPassword").value;
	
	if(password == confirm) {
		var postParameters = {"name": name, "login": login,
				"email":email, "password": password, "confirm_password": confirm};
		console.log(postParameters);
		$.post("/signUp", postParameters, function(responseJSON) {
			console.log(responseJSON);
			if(responseJSON == login) {
				var url = "/addCourses/" + login;
				console.log(url);
				window.location.href=url;
			} else {
				console.log("problems");
			}
		});

//		$.get(url, function(response) {
//			console.log(response);
//			response.redirect(url);
//		});
		
	} else {
		alert("make sure your passwords match!");
	}

}

function logIn() {
	//TODO
	

	var login = document.getElementById("loginLogIn").value;
	var password = document.getElementById("name").value;
	console.log(login + " , " + password);
	var postParameters = {}
}


