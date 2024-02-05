document.addEventListener('DOMContentLoaded', function() {
    let sidebarButtons = document.querySelectorAll('.sidebar_btn');

    sidebarButtons.forEach(function(button) {
        button.addEventListener('click', async function() {
            let page = this.getAttribute('data-page');

            // 로그인한 사용자의 username을 사용하여 URL을 구성할 필요 없이 직접 경로 사용
            let dynamicUrl = '/' + page;

            console.log('Requested page:', dynamicUrl);

            try {
                const response = await fetch(dynamicUrl, {
                    credentials: 'include' // 쿠키를 포함시키기 위해 필요
                });
                if (!response.ok) {
                    throw new Error("HTTP error " + response.status);
                }
                const content = await response.text();

                document.getElementById('mainContent').innerHTML = content;
                history.pushState(null, '', dynamicUrl);
            } catch (error) {
                console.error('Error:', error);
            }
        });
    });
});