// Interactive Frontend JavaScript
// API configuration
const API_BASE = '/api/users';

// Load users when page loads
document.addEventListener('DOMContentLoaded', function() {
    loadUsers();
    setupForm();
});

// Setup form submission
function setupForm() {
    const form = document.getElementById('createUserForm');
    const editForm = document.getElementById('editUserForm');

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        createUser();
    });

    editForm.addEventListener('submit', function(e) {
        e.preventDefault();
        updateUser();
    });
}

// Load all users
async function loadUsers() {
    try {
        showMessage('Loading users...', 'info');
        const response = await fetch(API_BASE);

        if (!response.ok) {
            throw new Error('Failed to fetch users');
        }

        const users = await response.json();
        displayUsers(users);
        updateStats(users.length);
        clearMessages();
    } catch (error) {
        showMessage('Error loading users: ' + error.message, 'error');
        document.getElementById('usersList').innerHTML = '<p class="error">Failed to load users</p>';
    }
}

// Display users in the UI
function displayUsers(users) {
    const container = document.getElementById('usersList');

    if (users.length === 0) {
        container.innerHTML = '<p>No users found. Create your first user above!</p>';
        return;
    }

    const usersHTML = users.map(user => `
        <div class="user-card">
            <h3>${escapeHtml(user.name)}</h3>
            <p><strong>Email:</strong> ${escapeHtml(user.email)}</p>
            <p><strong>ID:</strong> ${escapeHtml(user.id)}</p>
            <div class="user-actions">
                <button class="btn btn-warning" onclick="editUser('${user.id}', '${escapeHtml(user.name)}', '${escapeHtml(user.email)}')">Edit</button>
                <button class="btn btn-danger" onclick="deleteUser('${user.id}')">Delete</button>
                <button class="btn btn-primary" onclick="viewUser('${user.id}')">View Details</button>
            </div>
        </div>
    `).join('');

    container.innerHTML = usersHTML;
}

// Create new user
async function createUser() {
    const form = document.getElementById('createUserForm');
    const formData = new FormData(form);

    const userData = {
        name: formData.get('name'),
        email: formData.get('email')
    };

    try {
        const response = await fetch(API_BASE, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            throw new Error('Failed to create user');
        }

        const newUser = await response.json();
        showMessage(`User "${newUser.name}" created successfully!`, 'success');
        form.reset();
        loadUsers(); // Refresh the list
    } catch (error) {
        showMessage('Error creating user: ' + error.message, 'error');
    }
}

// Edit user
function editUser(id, name, email) {
    document.getElementById('editUserId').value = id;
    document.getElementById('editUserName').value = name;
    document.getElementById('editUserEmail').value = email;
    document.getElementById('editModal').style.display = 'block';
}

// Update user
async function updateUser() {
    const id = document.getElementById('editUserId').value;
    const name = document.getElementById('editUserName').value;
    const email = document.getElementById('editUserEmail').value;

    const userData = { name, email };

    try {
        const response = await fetch(`${API_BASE}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            throw new Error('Failed to update user');
        }

        showMessage('User updated successfully!', 'success');
        closeEditModal();
        loadUsers(); // Refresh the list
    } catch (error) {
        showMessage('Error updating user: ' + error.message, 'error');
    }
}

// Delete user
async function deleteUser(id) {
    if (!confirm('Are you sure you want to delete this user?')) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Failed to delete user');
        }

        showMessage('User deleted successfully!', 'success');
        loadUsers(); // Refresh the list
    } catch (error) {
        showMessage('Error deleting user: ' + error.message, 'error');
    }
}

// View user details
async function viewUser(id) {
    try {
        const response = await fetch(`${API_BASE}/${id}`);

        if (!response.ok) {
            throw new Error('Failed to fetch user details');
        }

        const user = await response.json();
        alert(`User Details:\n\nID: ${user.id}\nName: ${user.name}\nEmail: ${user.email}`);
    } catch (error) {
        showMessage('Error fetching user details: ' + error.message, 'error');
    }
}

// Close edit modal
function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
}

// Update statistics
function updateStats(totalUsers) {
    document.getElementById('totalUsers').textContent = totalUsers;
    document.getElementById('lastUpdated').textContent = new Date().toLocaleTimeString();
}

// Show message
function showMessage(message, type) {
    const messagesDiv = document.getElementById('messages');
    const className = type === 'error' ? 'error' : type === 'success' ? 'success' : 'info';
    messagesDiv.innerHTML = `<div class="${className}">${message}</div>`;

    // Auto-clear success messages after 3 seconds
    if (type === 'success') {
        setTimeout(clearMessages, 3000);
    }
}

// Clear messages
function clearMessages() {
    document.getElementById('messages').innerHTML = '';
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Auto-refresh users every 30 seconds
setInterval(loadUsers, 30000);
