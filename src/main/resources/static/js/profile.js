async function uploadProfileImage() {
    var username = document.getElementById('username').textContent;
    var userType = document.getElementById('userType').value;
    var biography = document.getElementById('biography').value;

    // 프로필 이미지 가져오기
    var fileInput = document.getElementById('profileUpload');
    var file = fileInput.files[0];
    
    console.log("Username:", username);

    // 파일 선택 여부 검증
    if (!file) {
        alert('Please select a file to upload');
        return;
    }

    // formData 생성
    var formData = new FormData();
    formData.append('profileDTO', new Blob([JSON.stringify({
        username: username,
        userType: userType
    })], {
        type: "application/json"
    }));
    formData.append('image', file);
    formData.append('biography', biography);
    
    // 프로필 업데이트 요청 보내기
    try {
        var updateResponse = await fetch('/profile/upload', {
            method: 'POST',
            body: formData
        });
    
        if (!updateResponse.ok) {
            throw new Error('Network response was not ok');
        }
    
        var data = await updateResponse.json();
    
        // newImageUrl 속성이 존재하는지 확인
        if (!data.newImageUrl) {
            throw new Error('Server response did not include newImageUrl');
        }
    
        console.log('Success:', data);
    
        var profileImage = document.getElementById('profileImage');
        profileImage.src = data.newImageUrl + '?t=' + new Date().getTime();
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to upload profile image');
    }
}