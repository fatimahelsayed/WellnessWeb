function validateForm() {
    var username = document.getElementById("username").value.trim();
    var password = document.getElementById("current-password").value.trim();

    //change password fields
    var currpass = document.getElementById("password").value.trim();
    var newpass = document.getElementById("new-pass").value.trim();
    var confrimpass = document.getElementById("confirm-pass").value.trim();
    var i = true;
  
    // Clear existing error messages
    var errorElements = document.querySelectorAll('.error-message');
    errorElements.forEach(function(element) {
      element.innerText = "";
    });
  
    // Validate username
    if (username === "") {
      document.getElementById("username-error").innerText = "Please enter your username";
      i=false;
    }
  
    // Validate password
    if (password === "") {
      document.getElementById("password-error").innerText = "Please enter your password";
      i=false; 
    }
    if (password != currpass) {
      document.getElementById("password-error").innerText = "Invalid password";
      i=false; 
    }
    if (newpass === "") {
      document.getElementById("new-pass-error").innerText = "Please enter your password";
      i=false; 
    }
    if (confrimpass === "") {
      document.getElementById("confirmed-pass-error").innerText = "Please enter your password";
      i=false; 
    }
    if (confrimpass != newpass) {
      document.getElementById("confirmed-pass-error").innerText = "New password has to match old password";
      i=false; 
    }
  
    return i; 
  }


