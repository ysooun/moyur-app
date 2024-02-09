document.addEventListener('DOMContentLoaded', function() {
    loadImages();
});

function loadImages() {
    var username = document.getElementById('username').textContent;

    fetch('/profile/images/' + username)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(imageUrls => {
            var imageCardDiv = document.getElementById('imageCard');
            imageCardDiv.innerHTML = ''; // Clear the div
            imageUrls.forEach(url => {
                var img = document.createElement('img');
                img.src = url;
                img.id = 'uploadedImage'; // Set the id
                imageCardDiv.appendChild(img);
            });
        })
        .catch(error => console.error('Error loading images:', error));
}

function openImageUploadModal() {
    document.getElementById('imageUploadModal').style.display = 'block';
}

function closeImageUploadModal() {
    document.getElementById('imageUploadModal').style.display = 'none';
}

async function uploadImage() {
    var username = document.getElementById('username').textContent;
    var formData = createFormData(username);

    try {
        const response = await fetch('/profile/imageUpload', {
            method: 'POST',
            body: formData,
        });

        if (!response.ok) {
            throw new Error('Image upload failed: ' + response.status);
        }

        const data = await response.json();
        console.log('Image uploaded successfully:', data);

        loadImages(); // Reload images after successful upload
        closeImageUploadModal(); // Close the modal after successful upload
    } catch (error) {
        console.error('Error uploading image:', error);
    }
}

function previewFile() {
    const preview = document.querySelector('#preview');
    const file = document.querySelector('#modalImageUpload').files[0];
    const reader = new FileReader();

    reader.addEventListener("load", function () {
        // convert image file to base64 string
        preview.style.backgroundImage = 'url(' + reader.result + ')';
    }, false);

    if (file) {
        reader.readAsDataURL(file);
    }
}

function createFormData(username) {
    const formData = new FormData();
    const imageType = document.querySelector('#modalImageType').value;
    const file = document.querySelector('#modalImageUpload').files[0];

    // Correctly appending uploadDTO as a JSON blob
    const uploadDTO = {
        username: username,
        imageType: imageType
    };

    formData.append('uploadDTO', new Blob([JSON.stringify(uploadDTO)], {type: 'application/json'}));
    formData.append('file', file);

    return formData;
}

document.getElementById('likeButton').addEventListener('click', function () {
    var likeIcon = document.getElementById('likeIcon');
    if (likeIcon.style.color === 'red') {
        likeIcon.style.color = '';
    } else {
        likeIcon.style.color = 'red';
    }
});