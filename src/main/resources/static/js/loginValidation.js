function validateForm() {
    const email = document.getElementById('email');
    const username = document.getElementById('username');
    const password = document.getElementById('password');

    const emailError = document.getElementById('email-error');
    const usernameError = document.getElementById('username-error');
    const passwordError = document.getElementById('password-error');

    let isValid = true;
    var validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

    var errorElements = document.querySelectorAll('.error-message');
    errorElements.forEach(function(element) {
        element.innerText = "";
    });

    // Email validation
    if (email.value.trim() === '') {
        email.innerText = 'Email is required';
        isValid = false;
    } 
    else if (email.value.match(validRegex))
    {
        emailError.innerText = '';
    }
    else{
        email.innerText = 'Invalid email';
        isValid = false;
    }
    
    // Username validation
    if (username.value.trim() === '') {
        usernameError.innerText = 'Username is required';
        isValid = false;
    }else {
        usernameError.innerText = '';
    }

    // Password validation
    if(password.value.trim() === ''){
        passwordError.innerText = 'Password is required';
        isValid = false;
    } else {
        passwordError.innerText = '';
    }

    return isValid;
}


