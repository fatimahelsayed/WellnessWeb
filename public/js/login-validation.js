function validateForm() {
    const username = document.getElementById('username');
    const password = document.getElementById('password');

    const usernameError = document.getElementById('username-error');
    const passwordError = document.getElementById('password-error');

    let isValid = true;

    var errorElements = document.querySelectorAll('.error-message');
    errorElements.forEach(function(element) {
        element.innerText = "";
    });

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


