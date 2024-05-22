
document.addEventListener("DOMContentLoaded", function() {
  var today = new Date().toISOString().split('T')[0];
  document.getElementById("date").setAttribute('min', today);
});

function validateAndSubmitForm() {
  var date = document.getElementById("date").value.trim();
  var startTime = document.getElementById("startTime").value.trim();
  var endTime = document.getElementById("endTime").value.trim();
  var errorMessage = "";
  var errorElements = document.querySelectorAll(".error-message");
  
  // Clear previous error messages
  errorElements.forEach(function(element) {
      element.innerText = "";
  });

  if (!date) {
      errorMessage += "Please enter a date.\n";
  }

  if (!startTime) {
      errorMessage += "Please enter a start time.\n";
  }

  if (!endTime) {
      errorMessage += "Please enter an end time.\n";
  }

  if (startTime && endTime) {
      var start = new Date("1970-01-01T" + startTime + "Z");
      var end = new Date("1970-01-01T" + endTime + "Z");
      var diff = (end - start) / (1000 * 60 * 60); // difference in hours

      if (diff < 1) {
          errorMessage += "The end time must be at least one hour after the start time.\n";
      }
  }

  if (errorMessage) {
      errorElements.forEach(function(element) {
          element.innerText = errorMessage;
      });
  } else {
      document.getElementById("sessionForm").submit();
  }
}