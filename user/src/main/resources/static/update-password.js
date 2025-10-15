document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('updatePasswordForm');
    const messageDiv = document.getElementById('updatePasswordMessage');
    if (!form) return;
    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        const oldPassword = document.getElementById('oldPassword').value;
        const newPassword = document.getElementById('newPassword').value;
        try {
            const response = await fetch('/api/auth/update-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `oldPassword=${encodeURIComponent(oldPassword)}&newPassword=${encodeURIComponent(newPassword)}`
            });
            if (response.ok) {
                messageDiv.textContent = 'Password updated successfully!';
                messageDiv.style.color = 'green';
                form.reset();
            } else {
                const text = await response.text();
                if (text.includes('Invalid Old Password')) {
                    messageDiv.textContent = 'Old password is incorrect.';
                } else {
                    messageDiv.textContent = text;
                }
                messageDiv.style.color = 'red';
            }
        } catch (err) {
            messageDiv.textContent = 'Error: ' + err.message;
            messageDiv.style.color = 'red';
        }
    });
});

