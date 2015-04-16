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
			
				$.get("/classes", function(response) {
					console.log(response);
				});
			
		});
	} else {
		alert("make sure your passwords match!");
	}

}


