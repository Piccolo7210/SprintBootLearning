document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('registerForm');
    const loginForm = document.getElementById('loginForm');

    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            const formData = new FormData();
            formData.append('name', name);
            formData.append('email', email);
            formData.append('password', password);

            try {
                const response = await fetch('/api/auth/register', {
                    method: 'POST',
                    body: formData
                });

                const result = await response.text();

                if (response.ok) {
                    alert('Registration successful! Check your email for activation.');
                    window.location.href = '/login';
                } else {
                    alert('Registration failed: ' + result);
                }
            } catch (error) {
                alert('Registration failed: ' + error.message);
            }
        });
    }

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            const formData = new FormData();
            formData.append('email', email);
            formData.append('password', password);

            try {
                const response = await fetch('/api/auth/login', {
                    method: 'POST',
                    body: formData
                });

                if (response.ok) {
                    window.location.href = '/home';
                } else {
                    const result = await response.text();
                    alert('Login failed: ' + result);
                }
            } catch (error) {
                alert('Login failed: ' + error.message);
            }
        });
    }
});