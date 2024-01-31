function openImageUploadModal() {
    document.getElementById('imageUploadModal').style.display = 'block';
}

function closeImageUploadModal() {
    document.getElementById('imageUploadModal').style.display = 'none';
}

async function uploadImage() {
    // 폼 데이터 생성
    const formData = new FormData();
    const imageType = document.querySelector('#modalImageType').value;
    
    var username = document.getElementById('username').textContent;
    
    formData.append('username', username); // 사용자 이름
    formData.append('file', document.querySelector('#modalImageUpload').files[0]);  // 파일
    formData.append('imageType', imageType); // 이미지 유형

    try {
        // 서버에 파일 업로드 요청
        const response = await fetch('/profile/imageUpload', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error('Image upload failed: ' + response.status);
        }

        const data = await response.json();
        console.log('Image uploaded successfully: ', data);
    } catch (error) {
        console.error('Error:', error);
    }
}