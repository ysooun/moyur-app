// 팔로우 상태를 확인하고 버튼의 상태를 업데이트하는 함수
function updateFollowButton(btn) {
    var targetUsername = window.location.pathname.split('/profile/')[1];
    var loggedInUsername = document.getElementById('username').textContent;

    // 팔로우 상태 확인
    fetch('/followers/exists/' + targetUsername + '/' + loggedInUsername)
    .then(response => response.json())
    .then(data => {
        if (data.exists) { // 팔로우 중인 경우
            btn.textContent = 'Unfollow'; // 버튼의 텍스트를 'Unfollow'로 변경
            btn.setAttribute('onclick', 'unfollow(this)'); // 클릭 이벤트 핸들러를 unfollow 함수로 변경
        } else { // 팔로우 중이 아닌 경우
            btn.textContent = 'Follow'; // 버튼의 텍스트를 'Follow'로 변경
            btn.setAttribute('onclick', 'follow(this)'); // 클릭 이벤트 핸들러를 follow 함수로 변경
        }
    });
}

// 팔로우 요청을 보내는 함수
function follow(btn) {
    var targetUsername = window.location.pathname.split('/profile/')[1];
    var loggedInUsername = document.getElementById('username').textContent;

    fetch('/followers/' + targetUsername + '/' + loggedInUsername, {
        method: 'POST'
    }).then(response => {
        if (response.ok) {
            updateFollowButton(btn); // 팔로우 요청이 성공하면 버튼 상태 업데이트
        } else {
            console.error('Follow failed');
        }
    });
}

// 언팔로우 요청을 보내는 함수
function unfollow(btn) {
    var targetUsername = window.location.pathname.split('/profile/')[1];
    var loggedInUsername = document.getElementById('username').textContent;

    fetch('/followers/' + targetUsername + '/' + loggedInUsername, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            updateFollowButton(btn); // 언팔로우 요청이 성공하면 버튼 상태 업데이트
        } else {
            console.error('Unfollow failed');
        }
    });
}

// 페이지 로드 시 팔로우 버튼 상태 업데이트
document.addEventListener('DOMContentLoaded', function() {
    var btn = document.getElementById('followButton'); // 팔로우 버튼 엘리먼트
    if (btn) { // 팔로우 버튼이 존재하는지 확인
        updateFollowButton(btn);
    }
});
