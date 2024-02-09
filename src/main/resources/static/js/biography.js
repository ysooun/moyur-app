async function uploadBiography(event) {
	event.preventDefault();
	
    var username = document.getElementById('username').textContent;
    var biography = document.getElementById('biography').value;
    var userType = document.getElementById('userType').value;

    // 서버로 데이터 전송
    try {
        var formData = new FormData();
        formData.append('username', username);
        formData.append('biography', biography);
        formData.append('userType', userType);

        var profileDTO = {
		    username: username,
		    userType: userType
		};
		var profileDTOBlob = new Blob([JSON.stringify(profileDTO)], {type: 'application/json'});
		formData.append('profileDTO', profileDTOBlob);

        var response = await fetch('/profile/upload', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        var data = await response.json();
        console.log('Success:', data);
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to upload biography');
    }
}

document.getElementById('submitBioBtn').addEventListener('click', uploadBiography);

async function fetchBiography() {
    var username = document.getElementById('username').textContent;

    // 서버에서 자기소개 정보 가져오기
    try {
        var response = await fetch('/profile/getBio?username=' + username, {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        var data = await response.json();

        // 자기소개 업데이트
        var biography = document.getElementById('biography');
        biography.value = data.biography;
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to fetch biography');
    }
}

// 페이지 로딩 시 자기소개 정보 가져오기
window.onload = fetchBiography;
