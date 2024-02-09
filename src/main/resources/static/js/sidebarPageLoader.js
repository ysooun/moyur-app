document.addEventListener('DOMContentLoaded', function() {
    let sidebarButtons = document.querySelectorAll('.sidebar_btn');

    sidebarButtons.forEach(function(button) {
        button.addEventListener('click', async function() {
            let page = this.getAttribute('data-page');

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