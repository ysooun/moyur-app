$(document).ready(function() {
    $('.sidebar_btn').each(function() {
        $(this).click(function() {
            let page = $(this).data('page');

            // 페이지를 비동기적으로 로드합니다.
            $.ajax({
                url: '/' + page,
                type: 'GET',
                async: true,
                success: function(content) {
                    // 성공한 경우
                    $('#mainContent').html(content);
                },
                error: function(xhr, status, error) {
                    // 실패한 경우
                    console.error('Request failed with status:', status);
                    console.error('Error:', error);
                    console.error('Response Text:', xhr.responseText);
                }
            });
        });
    });
});