$("#signIn").hide(0);
$("#signUp").hide(0);

var signUpForm = false;
var logInForm = false;

function showSignUp() {
	logInForm = false;
	signUpForm = true;
	$(".landingHeader").fadeOut(300);
	$("#signInButton").fadeOut(300);
	$("#signUpButton").fadeOut(300);
	$("#signUp").fadeIn(1000);
}

function showSignIn() {
	logInForm = true;
	signUpForm = false;
	$(".landingHeader").fadeOut(300);
	$("#signInButton").fadeOut(300);
	$("#signUpButton").fadeOut(300);
	$("#signIn").fadeIn(1000);
}

$("#switchToSignUp").bind('click', function(s) {
	logInForm = false;
	signUpForm = true;
	$("#signIn").fadeOut(300);
	$("#signUp").fadeIn(1000);
});


$("#switchToSignIn").bind('click', function(s) {
	logInForm = true;
	signUpForm = false;
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
		//console.log(postParameters);
		$.post("/signUp", postParameters, function(responseJSON) {
			console.log(responseJSON);
//			console.log(responseJSON.login);
//			console.log(responseJSON.equals(login));
			console.log(responseJSON == login);
			if(responseJSON == login) {
				var url = "/addCourses/" + login;
				console.log(url);
				window.location.href=url;
			} else {
				console.log("problems");
			}
		});
	} else {
		alert("make sure your passwords match!");
	}

}

function logIn() {
	var login = document.getElementById("loginLogIn").value;
	var password = document.getElementById("passwordLogIn").value;
	console.log(login + " , " + password);
	var postParameters = {"login": login, "password": password,};
	$.post("/login", postParameters, function(responseJSON) {
		console.log(responseJSON);
		if(responseJSON == login) {
			console.log(url);
			var url = "/courses/" + login;
			window.location.href=url;
		} else {
			console.log("problems");
		}
	});
}

document.addEventListener("keydown", keyDownPressed, false);

function keyDownPressed(e) {
    if (e.keyCode == '13') {
		if(signUpForm) {
			signUp();
		} else if (logInForm) {
			logIn();
		} else {
			alert("Please give your credentials for signing up or logging in.");
		}
    }
}



