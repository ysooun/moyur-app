function openImageUploadModal() {
    document.getElementById('imageUploadModal').style.display = 'block';
}

function closeImageUploadModal() {
    document.getElementById('imageUploadModal').style.display = 'none';
}

async function uploadProfilePhoto() {
    var username = document.getElementById('username').textContent;

    // 프로필 이미지 가져오기
    var fileInput = document.getElementById('profileUpload');
    var file = fileInput.files[0];

    // formData 생성
    var formData = new FormData();
    formData.append('profileDTO', new Blob([JSON.stringify({
        username: username
    })], {
        type: "application/json"
    }));
    formData.append('image', file);

    // 프로필 업데이트 요청 보내기
    var updateResponse = await fetch('/profile/update', {
        method: 'POST',
        body: formData
    });

    if (!updateResponse.ok) {
        throw new Error('Network response was not ok');
    }

    var data = await updateResponse.json();
    console.log('Success:', data);

    // 서버에서 새로운 이미지 URL을 반환하면 프로필 이미지를 업데이트합니다.
    document.getElementById('profileImage').src = data.newImageUrl;
}