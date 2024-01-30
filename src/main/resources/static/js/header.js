$(document).ready(function() {
    checkLoginStatus();
});

function checkLoginStatus() {
    $.ajax({
        url: '/check',
        type: 'GET',
        xhrFields: {
            withCredentials: true
        },
        success: function(_, __, xhr){
            if(xhr.status === 200) {
                updateLoginButton('로그아웃');
            } else {
                redirectToLogin();
            }
        },
        error: function(error){
            console.error(error);
            redirectToLogin();
        }
    });
}

function handleLoginLogout(event) {
    event.preventDefault();
    let loginButton = $('#loginButton');
    if(loginButton.text() === '로그아웃') {
        performLogout();
    } else {
        redirectToLogin();
    }
}

function updateLoginButton(text) {
    $('#loginButton').text(text);
}

function performLogout() {
    $.ajax({
        url: '/logout',
        type: 'POST',
        xhrFields: {
            withCredentials: true
        },
        success: function(){
            redirectToLogin();
        },
        error: function(error){
            console.error(error);
        }
    });
}

function redirectToLogin() {
    window.location.href = '/login';
}