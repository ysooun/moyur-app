$(document).ready(function() {
    $('.recommend_btn').each(function() {
        $(this).click(function(e) {
            e.preventDefault(); // 기본 링크 이벤트를 중지합니다.
            let page = $(this).data('page');

            // 서버에서 해당 페이지 내용을 비동기적으로 가져옵니다.
            $.ajax({
                url: '/recommend/' + page, // 페이지 URL을 지정해야 합니다.
                type: 'GET',
                success: function(content) {
                    // 서버로부터 받은 content를 파싱하여 .cafe-card 요소를 추출합니다.
                    var cafeCards = $(content).find('.cafe-card');
                    
                    // #dynamicContent 요소의 내부 HTML을 비웁니다.
                    $('#dynamicContent').empty();
                    
                    // 각 .cafe-card 요소를 #dynamicContent 요소에 추가합니다.
                    cafeCards.each(function() {
                        $('#dynamicContent').append(this);
                    });
                },
                error: function(xhr, status, error) {
                    // 실패한 경우 에러 처리를 수행합니다.
                    console.error('Request failed with status:', status);
                    console.error('Error:', error);
                    console.error('Response Text:', xhr.responseText);

                    // 요청 실패 시 처리 (예: 오류 메시지 표시)
                }
            });
        });
    });
});