function validateForm() {
    var name = document.getElementById("name").value.trim();
    var age = parseInt(document.getElementById("age").value.trim());
    var gender = document.querySelector('select[name="Gender"]');
    var phonenum = document.getElementById("phonenum").value.trim();
    var email = document.getElementById("email").value.trim();
    var username = document.getElementById("username").value.trim();
    var password = document.getElementById("password").value.trim();
    var i = true;
  
    // Clear existing error messages
    var errorElements = document.querySelectorAll('.error-message');
    errorElements.forEach(function(element) {
      element.innerText = "";
    });
  
    // Validate name
    if (name === "") {
      document.getElementById("name-error").innerText = "Please enter your name";
      i = false;
    }
  
    // Validate age
    if (isNaN(age) || age < 16 || age > 100) {
      document.getElementById("age-error").innerText = "Please enter a valid age between 16 and 100";
      i = false;
    }
  
    // Validate gender
    if (gender.value === "") {
      document.getElementById("gender-error").innerText = "Please select your gender";
      i = false;
    }
  
    // Validate phone number
    if (phonenum === "" || isNaN(phonenum)) {
      document.getElementById("phonenum-error").innerText = "Please enter a valid phone number";
      i = false;
    }
  
    // Validate email
    var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!email.match(emailPattern)) {
      document.getElementById("email-error").innerText = "Please enter a valid email address";
      i = false;
    }
  
    // Validate username
    if (username === "") {
      document.getElementById("username-error").innerText = "Please enter a username";
      i = false;
    }
  
    // Validate password
    if (password === "") {
      document.getElementById("password-error").innerText = "Please enter a password";
      i = false;
    }
  
    return i;
  }