function checkUsername(username) {
    // AJAX 요청을 통해 아이디 중복 검사
    fetch(`/check-username?username=${username}`)
        .then(response => response.json())
        .then(data => {
            if (data.available) {
                document.getElementById('usernameError').textContent = '';
            } else {
                document.getElementById('usernameError').textContent = '이미 사용중인 아이디입니다.';
            }
        });
}

function checkName(name) {
    // AJAX 요청을 통해 이름 중복 검사
    fetch(`/check-name?name=${name}`)
        .then(response => response.json())
        .then(data => {
            if (data.available) {
                document.getElementById('nameError').textContent = '';
            } else {
                document.getElementById('nameError').textContent = '이미 사용중인 이름입니다.';
            }
        });
}

function checkEmail(email) {
    // AJAX 요청을 통해 이메일 중복 검사
    fetch(`/check-email?email=${email}`)
        .then(response => response.json())
        .then(data => {
            if (data.available) {
                document.getElementById('emailError').textContent = '';
            } else {
                document.getElementById('emailError').textContent = '이미 사용중인 이메일입니다.';
            }
        });
}

function checkPassword() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    if (password !== confirmPassword) {
        document.getElementById('passwordError').textContent = '비밀번호가 일치하지 않습니다.';
    } else {
        document.getElementById('passwordError').textContent = '';
    }
}
