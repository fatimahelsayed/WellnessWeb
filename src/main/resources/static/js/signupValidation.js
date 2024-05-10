function validateForm() {
  var name = document.getElementById("name").value.trim();
  var age = parseInt(document.getElementById("age").value.trim());
  var gender = document.getElementById("gender");
  var phonenum = document.getElementById("phonenum").value.trim();
  var email = document.getElementById("email").value.trim();
  var username = document.getElementById("username").value.trim();
  var password = document.getElementById("password").value.trim();
  var i = true;

  // Clear existing error messages
  var errorElements = document.querySelectorAll(".error-message");
  errorElements.forEach(function (element) {
    element.innerText = "";
  });

  // Validate name
  if (name === "") {
    document.getElementById("name-error").innerText = "Please enter your name";
    i = false;
  }

  // Validate age
  if (isNaN(age) || age < 16 || age > 100) {
    document.getElementById("age-error").innerText =
      "Please enter a valid age between 16 and 100";
    i = false;
  }

  // Validate gender
  if (gender.value === "") {
    document.getElementById("gender-error").innerText =
      "Please select your gender";
    i = false;
  }

  // Validate phone number
  if (phonenum === "" || isNaN(phonenum)) {
    document.getElementById("phonenum-error").innerText =
      "Please enter a valid phone number";
    i = false;
  }

  // Validate email
  var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!email.match(emailPattern)) {
    document.getElementById("email-error").innerText =
      "Please enter a valid email address";
    i = false;
  }

  // Validate username
  if (username === "") {
    document.getElementById("username-error").innerText =
      "Please enter a username";
    i = false;
  }

  // Validate password
  if (password === "") {
    document.getElementById("password-error").innerText =
      "Please enter a password";
    i = false;
  }

  return i;
}

function alertShow(response) {
  $(".msg").text(response);
  $(".alert").addClass("show");
  $(".alert").removeClass("hide");
  $(".alert").addClass("showAlert");
  setTimeout(function () {
    $(".alert").removeClass("show");
    $(".alert").addClass("hide");
  }, 5000);

  $(".close-btn").click(function () {
    $(".alert").removeClass("show");
    $(".alert").addClass("hide");
  });
}

function signUpAjax() {
  var valid = validateForm();
  // PREPARE FORM DATA
  if (valid) {
    var formData = {
      name: $("#name").val(),
      age: $("#age").val(),
      gender: $("#gender").val(),
      phoneNumber: $("#phonenum").val(),
      email: $("#email").val(),
      username: $("#username").val(),
      password: $("#password").val(),
    };
    console.log(formData);

    // DO POST
    $.ajax({
      type: "POST",
      contentType: "application/json",
      url: "/signup",
      data: JSON.stringify(formData),
      dataType: "json",
      success: function (result) {
        if (result.status == "success") {
          window.location.href = result.data;
          // alertShow("Account created successfully, log in to access it");
        } else if (Array.isArray(result.data)) {
          result.data.forEach(function (error) {
            if (error === "EmailExists") {
              document.getElementById("existing-email-error").innerText =
                "This email already exists, please enter a different email";
            } else if (error === "PhoneNumberExists") {
              document.getElementById("existing-phonenum-error").innerText =
                "This phone number already exists, please enter a different number";
            } else if (error === "UsernameTaken") {
              document.getElementById("existing-username-error").innerText =
                "This username already exists, please enter a different username";
            }
          });
          console.log(result);
        }
      },
      error: function (e) {
        alertShow(result.status);
      },
    });
  }
}
