$("#signIn").hide(0);
$("#signUp").hide(0);

var signUpForm = false;
var logInForm = false;

var account_exist = "account exists";
var account_no_exist = "account does not exist";

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
	if(validateSignUp()) {
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
				} else if (responseJSON == account_exist){
					var problemsDiv = $("#signUpProblems");
					problemsDiv.innerHTML = "<h4>There is already an account under this name and login.</h4>";
				}
			});
		} else {
			alert("make sure your passwords match!");
		}
	} else {
		alert("Please properly fill in the marked fields");
	}

}

function validateSignUp() {
	var toReturn = true;
	var name = document.getElementById("name");
	var inputName = name.value;
	var login = document.getElementById("loginSignUp");
	var inputLogin = login.value;
	var email = document.getElementById("email");
	var inputEmail = email.value;
	var password = document.getElementById("pass");
	var inputPassword = password.value;
	var confirm = document.getElementById("confirmPassword");
	var inputConfirm = confirm.value;

	
	console.log(name + "- name");
	console.log(login + "- login");
	console.log(email + "- email");
	console.log(password + "- password");
	console.log(confirm + "- confirm");
	
	if(inputName.length == 0) {
		toReturn = false;
		name.style.borderColor = "red";
		name.style.borderWidth = "2px";
	}
	if(inputLogin.length == 0) {
		toReturn = false;
		login.style.borderColor = "red";
		login.style.borderWidth = "2px";
	}
	console.log(email.indexOf("@"));
	if(inputEmail.length == 0 || email.indexOf("@") == -1) {
		toReturn = false;
		email.style.borderColor = "red";
		email.style.borderWidth = "2px";
	}
	if(inputEmail.length == 0) {
		toReturn = false;
		password.style.borderColor = "red";
		password.style.borderWidth = "2px";
	}
	if(inputEmail.length == 0) {
		toReturn = false;
		confirm.style.borderColor = "red";
		confirm.style.borderWidth = "2px";
	}
	
	return toReturn;
	
}

function logIn() {
	if(validateLogin()) {
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
			} else if(responseJSON == account_no_exist){
				alert("This account does not exist");
			}
		});
	} else {
		alert("Please properly fill in the marked fields");
	}
}

function validateLogin() {
	var toReturn = true;
	var login = document.getElementById("loginLogIn");
	var password = document.getElementById("passwordLogIn");
	var inputPassword = password.value;
	var inputLogin = login.value;
	if(login.length == 0) {
		toReturn = false;
		login.style.borderColor = "red";
		login.style.borderWidth = "2px";
	}

	if(inputPassword.length == 0) {
		toReturn = false;
		password.style.borderColor = "red";
		password.style.borderWidth = "2px";
	}
	return toReturn;
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



