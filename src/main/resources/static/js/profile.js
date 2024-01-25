$(document).ready(function() {
    var username = 'user1'; // 사용자 이름 설정
    $.ajax({
        url: '/profile/' + username, // URL에 사용자 이름 포함
        type: 'GET',
        success: function(cards) {
            // 가져온 카드 데이터를 화면에 표시
            cards.forEach(function(card) {
                var cardHtml = '<div class="card">' +
                    '<img src="' + card.imageUrl + '" alt="카드 이미지">' +
                    '<p>좋아요: ' + card.likes + '</p>' +
                    '</div>';

                $('#dynamicContent').append(cardHtml);
            });
        },
        error: function(xhr, status, error) {
            console.error('Failed to fetch card data:', error);
        }
    });
});
