// src/main/resources/static/js/admin-home.js
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('logoutBtn').addEventListener('click', function() {
        fetch('/api/admin/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.text())
            .then(msg => {
                document.getElementById('logoutStatus').innerText = msg;
                window.location.href = '/login';
            });
    });
});
