var images = ["/image/1.jpg", "/image/2.jpg", "/image/3.jpg"];
var currentIndex = 0;

function changeSlide(direction) {
  currentIndex += direction;
  if (currentIndex < 0) currentIndex = images.length - 1;
  else if (currentIndex >= images.length) currentIndex = 0;

  document.getElementById("image").src = images[currentIndex];
}

function openModal() {
  document.getElementById("modalImage").src = images[currentIndex];
  document.getElementById("myModal").style.display = "block";
}

function closeModal() {
  document.getElementById("myModal").style.display = "none";
}

function changeSlideInModal(direction) {
  currentIndex += direction;
  if (currentIndex < 0) currentIndex = images.length - 1;
  else if (currentIndex >= images.length) currentIndex = 0;

  document.getElementById("modalImage").src = images[currentIndex];
}
