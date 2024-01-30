function saveImageDataToDatabase(file, imageType, username) {
    var formData = new FormData();
    formData.append('image', file); // 이미지 파일 추가
    formData.append('profileDTO', JSON.stringify({ username, imageType })); // ProfileDTO JSON 추가

    // FormData 내용을 로그로 찍어보기
    for (let [key, value] of formData.entries()) {
        console.log(key, value);
    }

    fetch('/profile/saveImage', {
        method: 'POST',
        body: formData
    })
    .then(response => response.text())
    .then(text => {
        console.log('Server response:', text);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}