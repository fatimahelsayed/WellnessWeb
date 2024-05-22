
function validateForm() {
  var username = document.getElementById("username").value.trim();

  // Clear existing error messages
  var errorElements = document.querySelectorAll(".error-message");
  errorElements.forEach(function (element) {
    element.innerText = "";
  });

  // Validate username
  if (username === "") {
    document.getElementById("username-error").innerText =
      "Please enter your username";
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

function validatePasswordForm() {
  var password = document.getElementById("password").value.trim();
  var newpass = document.getElementById("new-pass").value.trim();
  var confrimpass = document.getElementById("confirm-pass").value.trim();
  var i = true;

  // Validate password
  if (password === "") {
    document.getElementById("password-error").innerText =
      "Please enter your old password";
    i = false;
  }
  
  if (newpass === "") {
    document.getElementById("new-pass-error").innerText =
      "Please enter your new password";
    i = false;
  }
  if (confrimpass === "") {
    document.getElementById("confirmed-pass-error").innerText =
      "Please confirm your new password";
    i = false;
  }
  if (confrimpass !== newpass) {
    document.getElementById("confirmed-pass-error").innerText =
      "The passwords don't match";
    i = false;
  }

  return i;
}
function changePasswordAjax() {
  var validForm = validatePasswordForm();
  console.log("In ajax function")
  if (validForm) {
    var formData = {
      oldPassword: $("#password").val(),
      newPassword: $("#confirm-pass").val(),
    };

    // Perform AJAX request
    $.ajax({
      type: "POST",
      contentType: "application/json",
      url: "/therapistdashboard/changepassword",
      data: JSON.stringify(formData),
      dataType: "json",
      success: function (response) {
        if (response.status == "success") {
          window.location.href = response.data;
        }else{
          document.getElementById("password-error").innerText = response.data;

        }
      },
      error: function (xhr, status, error) {
        console.error(xhr.responseText);
      },
    });
  }
}
