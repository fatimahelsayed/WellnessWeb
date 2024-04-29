function validateForm() {
    var username = document.getElementById("username").value.trim();
    var password = document.getElementById("password").value.trim();
  
    // Clear existing error messages
    var errorElements = document.querySelectorAll('.error-message');
    errorElements.forEach(function(element) {
      element.innerText = "";
    });
  
    // Validate username
    if (username === "") {
      document.getElementById("username-error").innerText = "Please enter your username";
      return false;
    }
  
    // Validate password
    if (password === "") {
      document.getElementById("password-error").innerText = "Please enter your password";
      return false; 
    }
  
    return true; 
  }


