document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    if (!form) return;
    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        const formData = new FormData(form);
        const token = formData.get('token');
        const newPassword = formData.get('newPassword');
        try {
            const response = await fetch('/api/auth/reset-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `token=${encodeURIComponent(token)}&newPassword=${encodeURIComponent(newPassword)}`
            });
            if (response.status === 200) {
                window.location.href = '/login';
            } else {
                const text = await response.text();
                alert('Password reset failed: ' + text);
            }
        } catch (err) {
            alert('Password reset failed: ' + err.message);
        }
    });
});

