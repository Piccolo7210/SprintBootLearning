// Management System JavaScript - CRUD Operations
class ManagementSystem {
    constructor() {
        this.API_BASE = '';
        this.currentEditType = '';
        this.currentEditId = '';
        this.editModal = null;
        this.init();
    }

    init() {
        // Initialize Bootstrap modal
        this.editModal = new bootstrap.Modal(document.getElementById('editModal'));
        
        // Bind form submissions
        this.bindFormEvents();
        
        // Bind load buttons
        this.bindLoadEvents();
    }

    bindFormEvents() {
        // Student form
        document.getElementById('studentForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.createStudent();
        });

        // Course form
        document.getElementById('courseForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.createCourse();
        });

        // Department form
        document.getElementById('departmentForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.createDepartment();
        });

        // Enrollment form
        document.getElementById('enrollmentForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.createEnrollment();
        });

        // Edit form save
        document.getElementById('saveEdit').addEventListener('click', () => {
            this.saveEdit();
        });
    }

    bindLoadEvents() {
        document.getElementById('loadStudents').addEventListener('click', () => {
            this.loadStudents();
        });

        document.getElementById('loadCourses').addEventListener('click', () => {
            this.loadCourses();
        });

        document.getElementById('loadDepartments').addEventListener('click', () => {
            this.loadDepartments();
        });

        document.getElementById('loadEnrollments').addEventListener('click', () => {
            this.loadEnrollments();
        });

        document.getElementById('loadGroupedEnrollments').addEventListener('click', () => {
            this.loadGroupedEnrollments();
        });
    }

    // Utility methods
    showLoading(entityType) {
        document.getElementById(`${entityType}Loading`).style.display = 'block';
    }

    hideLoading(entityType) {
        document.getElementById(`${entityType}Loading`).style.display = 'none';
    }

    showMessage(elementId, message, isError = false) {
        const element = document.getElementById(elementId);
        element.innerHTML = `<div class="alert ${isError ? 'alert-danger' : 'alert-success'} mt-2">${message}</div>`;
        setTimeout(() => {
            element.innerHTML = '';
        }, 3000);
    }

    // API calls
    async apiCall(url, method = 'GET', data = null) {
        const options = {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
        };

        if (data) {
            options.body = JSON.stringify(data);
        }

        try {
            const response = await fetch(url, options);
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            // Check if response is JSON or text
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            } else {
                // Return text for string responses (like student/course names)
                return await response.text();
            }
        } catch (error) {
            console.error('API call failed:', error);
            throw error;
        }
    }

    // Student operations
    async createStudent() {
        const studentData = {
            name: document.getElementById('studentName').value,
            age: parseInt(document.getElementById('studentAge').value),
            email: document.getElementById('studentEmail').value,
            cgpa: document.getElementById('studentCgpa').value ? parseFloat(document.getElementById('studentCgpa').value) : null,
            deptId: document.getElementById('studentDeptId').value ? parseInt(document.getElementById('studentDeptId').value) : null
        };

        try {
            await this.apiCall('/api/student', 'POST', studentData);
            this.showMessage('studentMessage', 'Student created successfully!');
            document.getElementById('studentForm').reset();
            // Auto-reload students table if it's already loaded
            if (document.getElementById('students-table').querySelector('table')) {
                this.loadStudents();
            }
        } catch (error) {
            this.showMessage('studentMessage', 'Error creating student: ' + error.message, true);
        }
    }

    async loadStudents() {
        this.showLoading('students');
        try {
            const students = await this.apiCall('/api/student/all');
            this.displayStudents(students);
        } catch (error) {
            document.getElementById('students-table').innerHTML = 
                `<p class="text-danger p-3">Error loading students: ${error.message}</p>`;
        } finally {
            this.hideLoading('students');
        }
    }

    async displayStudents(students) {
        let html = `
            <table class="table table-striped table-hover mb-0">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th><th>Name</th><th>Age</th><th>Email</th><th>CGPA</th><th>Department</th><th>Actions</th>
                    </tr>
                </thead>
                <tbody>`;

        // Use Promise.all to handle all async operations properly
        try {
            const studentRows = await Promise.all(
                students.map(async (student) => {
                    let departmentName = 'N/A';

                    // Fetch department name if deptId exists
                    if (student.deptId) {
                        try {
                            departmentName = await this.apiCall(`/api/student/deptName/${student.deptId}`);
                        } catch (error) {
                            console.error(`Error fetching department name for ID ${student.deptId}:`, error);
                            departmentName = `Dept ID: ${student.deptId}`;
                        }
                    }

                    return `
                        <tr>
                            <td>${student.studentId}</td>
                            <td>${student.name}</td>
                            <td>${student.age}</td>
                            <td>${student.email}</td>
                            <td>${student.cgpa || 'N/A'}</td>
                            <td>${departmentName}</td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="managementSystem.editStudent(${student.studentId}, '${student.name}', ${student.age}, '${student.email}', ${student.cgpa}, ${student.deptId})">
                                    <i class="fas fa-edit"></i> Edit
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="managementSystem.deleteStudent(${student.studentId})">
                                    <i class="fas fa-trash"></i> Delete
                                </button>
                            </td>
                        </tr>`;
                })
            );

            html += studentRows.join('');
        } catch (error) {
            console.error('Error loading student details:', error);
            // Fallback: show original data with department IDs
            students.forEach(student => {
                html += `
                    <tr>
                        <td>${student.studentId}</td>
                        <td>${student.name}</td>
                        <td>${student.age}</td>
                        <td>${student.email}</td>
                        <td>${student.cgpa || 'N/A'}</td>
                        <td>${student.deptId || 'N/A'}</td>
                        <td>
                            <button class="btn btn-sm btn-warning" onclick="managementSystem.editStudent(${student.studentId}, '${student.name}', ${student.age}, '${student.email}', ${student.cgpa}, ${student.deptId})">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <button class="btn btn-sm btn-danger" onclick="managementSystem.deleteStudent(${student.studentId})">
                                <i class="fas fa-trash"></i> Delete
                            </button>
                        </td>
                    </tr>`;
            });
        }

        html += '</tbody></table>';
        document.getElementById('students-table').innerHTML = html;
    }

    editStudent(id, name, age, email, cgpa, deptId) {
        this.currentEditType = 'student';
        this.currentEditId = id;
        
        document.getElementById('editModalLabel').textContent = 'Edit Student';
        document.getElementById('editFormFields').innerHTML = `
            <div class="mb-3">
                <label class="form-label">Name</label>
                <input type="text" id="editName" class="form-control" value="${name}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Age</label>
                <input type="number" id="editAge" class="form-control" value="${age}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" id="editEmail" class="form-control" value="${email}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">CGPA</label>
                <input type="number" step="0.01" id="editCgpa" class="form-control" value="${cgpa || ''}">
            </div>
            <div class="mb-3">
                <label class="form-label">Department ID</label>
                <input type="number" id="editDeptId" class="form-control" value="${deptId || ''}">
            </div>
        `;
        
        this.editModal.show();
    }

    async deleteStudent(id) {
        if (confirm('Are you sure you want to delete this student?')) {
            try {
                await this.apiCall(`/api/student/${id}`, 'DELETE');
                this.loadStudents();
            } catch (error) {
                alert('Error deleting student: ' + error.message);
            }
        }
    }

    // Course operations
    async createCourse() {
        const courseData = {
            courseName: document.getElementById('courseName').value,
            credits: parseInt(document.getElementById('courseCredits').value),
            deptId: document.getElementById('courseDeptId').value ? parseInt(document.getElementById('courseDeptId').value) : null
        };

        try {
            await this.apiCall('/api/course', 'POST', courseData);
            this.showMessage('courseMessage', 'Course created successfully!');
            document.getElementById('courseForm').reset();
            if (document.getElementById('courses-table').querySelector('table')) {
                this.loadCourses();
            }
        } catch (error) {
            this.showMessage('courseMessage', 'Error creating course: ' + error.message, true);
        }
    }

    async loadCourses() {
        this.showLoading('courses');
        try {
            const courses = await this.apiCall('/api/course');
            this.displayCourses(courses);
        } catch (error) {
            document.getElementById('courses-table').innerHTML = 
                `<p class="text-danger p-3">Error loading courses: ${error.message}</p>`;
        } finally {
            this.hideLoading('courses');
        }
    }

    async displayCourses(courses) {
        let html = `
            <table class="table table-striped table-hover mb-0">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th><th>Course Name</th><th>Credits</th><th>Department</th><th>Actions</th>
                    </tr>
                </thead>
                <tbody>`;

        // Use Promise.all to handle all async operations properly
        try {
            const courseRows = await Promise.all(
                courses.map(async (course) => {
                    let departmentName = 'N/A';

                    // Fetch department name if deptId exists
                    if (course.deptId) {
                        try {
                            departmentName = await this.apiCall(`/api/course/deptName/${course.deptId}`);
                        } catch (error) {
                            console.error(`Error fetching department name for ID ${course.deptId}:`, error);
                            departmentName = `Dept ID: ${course.deptId}`;
                        }
                    }

                    return `
                        <tr>
                            <td>${course.courseId}</td>
                            <td>${course.courseName}</td>
                            <td>${course.credits}</td>
                            <td>${departmentName}</td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="managementSystem.editCourse(${course.courseId}, '${course.courseName}', ${course.credits}, ${course.deptId})">
                                    <i class="fas fa-edit"></i> Edit
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="managementSystem.deleteCourse(${course.courseId})">
                                    <i class="fas fa-trash"></i> Delete
                                </button>
                            </td>
                        </tr>`;
                })
            );

            html += courseRows.join('');
        } catch (error) {
            console.error('Error loading course details:', error);
            // Fallback: show original data with department IDs
            courses.forEach(course => {
                html += `
                    <tr>
                        <td>${course.courseId}</td>
                        <td>${course.courseName}</td>
                        <td>${course.credits}</td>
                        <td>${course.deptId || 'N/A'}</td>
                        <td>
                            <button class="btn btn-sm btn-warning" onclick="managementSystem.editCourse(${course.courseId}, '${course.courseName}', ${course.credits}, ${course.deptId})">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <button class="btn btn-sm btn-danger" onclick="managementSystem.deleteCourse(${course.courseId})">
                                <i class="fas fa-trash"></i> Delete
                            </button>
                        </td>
                    </tr>`;
            });
        }

        html += '</tbody></table>';
        document.getElementById('courses-table').innerHTML = html;
    }

    editCourse(id, courseName, credits, deptId) {
        this.currentEditType = 'course';
        this.currentEditId = id;
        
        document.getElementById('editModalLabel').textContent = 'Edit Course';
        document.getElementById('editFormFields').innerHTML = `
            <div class="mb-3">
                <label class="form-label">Course Name</label>
                <input type="text" id="editCourseName" class="form-control" value="${courseName}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Credits</label>
                <input type="number" id="editCredits" class="form-control" value="${credits}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Department ID</label>
                <input type="number" id="editDeptId" class="form-control" value="${deptId || ''}">
            </div>
        `;
        
        this.editModal.show();
    }

    async deleteCourse(id) {
        if (confirm('Are you sure you want to delete this course?')) {
            try {
                await this.apiCall(`/api/course/${id}`, 'DELETE');
                this.loadCourses();
            } catch (error) {
                alert('Error deleting course: ' + error.message);
            }
        }
    }

    // Department operations
    async createDepartment() {
        const departmentData = {
            name: document.getElementById('departmentName').value,
            description: document.getElementById('departmentDescription').value || null
        };

        try {
            await this.apiCall('/api/department', 'POST', departmentData);
            this.showMessage('departmentMessage', 'Department created successfully!');
            document.getElementById('departmentForm').reset();
            if (document.getElementById('departments-table').querySelector('table')) {
                this.loadDepartments();
            }
        } catch (error) {
            this.showMessage('departmentMessage', 'Error creating department: ' + error.message, true);
        }
    }

    async loadDepartments() {
        this.showLoading('departments');
        try {
            const departments = await this.apiCall('/api/department');
            this.displayDepartments(departments);
        } catch (error) {
            document.getElementById('departments-table').innerHTML = 
                `<p class="text-danger p-3">Error loading departments: ${error.message}</p>`;
        } finally {
            this.hideLoading('departments');
        }
    }

    displayDepartments(departments) {
        let html = `
            <table class="table table-striped table-hover mb-0">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th><th>Name</th><th>Description</th><th>Actions</th>
                    </tr>
                </thead>
                <tbody>`;

        departments.forEach(dept => {
            html += `
                <tr>
                    <td>${dept.deptId}</td>
                    <td>${dept.name}</td>
                    <td>${dept.description || 'N/A'}</td>
                    <td>
                        <button class="btn btn-sm btn-warning" onclick="managementSystem.editDepartment(${dept.deptId}, '${dept.name}', '${dept.description || ''}')">
                            <i class="fas fa-edit"></i> Edit
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="managementSystem.deleteDepartment(${dept.deptId})">
                            <i class="fas fa-trash"></i> Delete
                        </button>
                    </td>
                </tr>`;
        });

        html += '</tbody></table>';
        document.getElementById('departments-table').innerHTML = html;
    }

    editDepartment(id, name, description) {
        this.currentEditType = 'department';
        this.currentEditId = id;
        
        document.getElementById('editModalLabel').textContent = 'Edit Department';
        document.getElementById('editFormFields').innerHTML = `
            <div class="mb-3">
                <label class="form-label">Name</label>
                <input type="text" id="editName" class="form-control" value="${name}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Description</label>
                <textarea id="editDescription" class="form-control" rows="3">${description}</textarea>
            </div>
        `;
        
        this.editModal.show();
    }

    async deleteDepartment(id) {
        if (confirm('Are you sure you want to delete this department?')) {
            try {
                await this.apiCall(`/api/department/${id}`, 'DELETE');
                this.loadDepartments();
            } catch (error) {
                alert('Error deleting department: ' + error.message);
            }
        }
    }

    // Enrollment operations
    async createEnrollment() {
        const studentId = document.getElementById('enrollStudentId').value;
        const courseId = document.getElementById('enrollCourseId').value;

        try {
            await this.apiCall(`/api/studentcourse/enroll?studentId=${studentId}&courseId=${courseId}`, 'POST');
            this.showMessage('enrollmentMessage', 'Student enrolled successfully!');
            document.getElementById('enrollmentForm').reset();
            // Auto-reload enrollments table if it's already loaded
            if (document.getElementById('enrollments-table').querySelector('table')) {
                this.loadEnrollments();
            }
        } catch (error) {
            this.showMessage('enrollmentMessage', 'Error enrolling student: ' + error.message, true);
        }
    }

    async loadEnrollments() {
        this.showLoading('enrollments');
        try {
            const enrollments = await this.apiCall('/api/studentcourse/all');
            this.displayEnrollments(enrollments);
        } catch (error) {
            document.getElementById('enrollments-table').innerHTML =
                `<p class="text-danger p-3">Error loading enrollments: ${error.message}</p>`;
        } finally {
            this.hideLoading('enrollments');
        }
    }

    async loadGroupedEnrollments() {
        this.showLoading('enrollments');
        try {
            const groupedData = await this.apiCall('/api/studentcourse/grouped-by-student');
            this.displayGroupedEnrollments(groupedData);
        } catch (error) {
            document.getElementById('enrollments-table').innerHTML =
                `<p class="text-danger p-3">Error loading grouped enrollments: ${error.message}</p>`;
        } finally {
            this.hideLoading('enrollments');
        }
    }

    async displayEnrollments(enrollments) {
        let html = `
            <table class="table table-striped table-hover mb-0">
                <thead class="table-dark">
                    <tr>
                        <th>Student Name</th><th>Course Name</th><th>Actions</th>
                    </tr>
                </thead>
                <tbody>`;

        // Use Promise.all to handle all async operations properly
        try {
            const enrollmentRows = await Promise.all(
                enrollments.map(async (enrollment) => {
                    try {
                        const studentName = await this.apiCall(`/api/studentcourse/studentName/${enrollment.studentId}`);
                        const courseName = await this.apiCall(`/api/studentcourse/courseName/${enrollment.courseId}`);

                        return `
                            <tr>
                                <td>${studentName}</td>
                                <td>${courseName}</td>
                                <td>
                                    <button class="btn btn-sm btn-danger" onclick="managementSystem.removeEnrollment(${enrollment.studentId}, ${enrollment.courseId})">
                                        <i class="fas fa-user-times"></i> Remove
                                    </button>
                                </td>
                            </tr>`;
                    } catch (error) {
                        // Fallback to showing IDs if name fetch fails
                        return `
                            <tr>
                                <td>Student ID: ${enrollment.studentId}</td>
                                <td>Course ID: ${enrollment.courseId}</td>
                                <td>
                                    <button class="btn btn-sm btn-danger" onclick="managementSystem.removeEnrollment(${enrollment.studentId}, ${enrollment.courseId})">
                                        <i class="fas fa-user-times"></i> Remove
                                    </button>
                                </td>
                            </tr>`;
                    }
                })
            );

            html += enrollmentRows.join('');
        } catch (error) {
            console.error('Error loading enrollment details:', error);
            // Fallback: show just the IDs
            enrollments.forEach(enrollment => {
                html += `
                    <tr>
                        <td>Student ID: ${enrollment.studentId}</td>
                        <td>Course ID: ${enrollment.courseId}</td>
                        <td>
                            <button class="btn btn-sm btn-danger" onclick="managementSystem.removeEnrollment(${enrollment.studentId}, ${enrollment.courseId})">
                                <i class="fas fa-user-times"></i> Remove
                        </td>
                    </tr>`;
            });
        }

        html += '</tbody></table>';
        document.getElementById('enrollments-table').innerHTML = html;
    }

    displayGroupedEnrollments(groupedData) {
        let html = `
            <div class="accordion" id="enrollmentAccordion">`;

        let studentIndex = 0;
        for (const [studentName, courses] of Object.entries(groupedData)) {
            const collapseId = `collapse${studentIndex}`;
            const headingId = `heading${studentIndex}`;

            html += `
                <div class="accordion-item">
                    <h2 class="accordion-header" id="${headingId}">
                        <button class="accordion-button ${studentIndex === 0 ? '' : 'collapsed'}" type="button" 
                                data-bs-toggle="collapse" data-bs-target="#${collapseId}" 
                                aria-expanded="${studentIndex === 0 ? 'true' : 'false'}" aria-controls="${collapseId}">
                            <strong>${studentName}</strong> &nbsp;<span class="badge bg-primary">${courses.length} courses</span>
                        </button>
                    </h2>
                    <div id="${collapseId}" class="accordion-collapse collapse ${studentIndex === 0 ? 'show' : ''}" 
                         aria-labelledby="${headingId}" data-bs-parent="#enrollmentAccordion">
                        <div class="accordion-body">
                            <div class="row">`;

            courses.forEach((courseName, courseIndex) => {
                html += `
                    <div class="col-md-6 mb-2">
                        <div class="card border-success">
                            <div class="card-body py-2">
                                <h6 class="card-title mb-1">
                                    <i class="fas fa-book text-success"></i> ${courseName}
                                </h6>
                            </div>
                        </div>
                    </div>`;
            });

            html += `
                            </div>
                        </div>
                    </div>
                </div>`;
            studentIndex++;
        }

        html += `</div>`;

        if (Object.keys(groupedData).length === 0) {
            html = `<p class="text-muted p-3">No enrollments found.</p>`;
        }

        document.getElementById('enrollments-table').innerHTML = html;
    }

    async removeEnrollment(studentId, courseId) {
        if (confirm('Are you sure you want to remove this enrollment?')) {
            try {
                const enrollmentData = {
                    studentId: studentId,
                    courseId: courseId
                };
                await this.apiCall('/api/studentcourse/remove', 'DELETE', enrollmentData);
                this.loadEnrollments();
            } catch (error) {
                alert('Error removing enrollment: ' + error.message);
            }
        }
    }

    // Save edit functionality
    async saveEdit() {
        let updateData = {};
        let url = '';

        switch (this.currentEditType) {
            case 'student':
                updateData = {
                    name: document.getElementById('editName').value,
                    age: parseInt(document.getElementById('editAge').value),
                    email: document.getElementById('editEmail').value,
                    cgpa: document.getElementById('editCgpa').value ? parseFloat(document.getElementById('editCgpa').value) : null,
                    deptId: document.getElementById('editDeptId').value ? parseInt(document.getElementById('editDeptId').value) : null
                };
                url = `/api/student/${this.currentEditId}`;
                break;
            case 'course':
                updateData = {
                    courseName: document.getElementById('editCourseName').value,
                    credits: parseInt(document.getElementById('editCredits').value),
                    deptId: document.getElementById('editDeptId').value ? parseInt(document.getElementById('editDeptId').value) : null
                };
                url = `/api/course/${this.currentEditId}`;
                break;
            case 'department':
                updateData = {
                    name: document.getElementById('editName').value,
                    description: document.getElementById('editDescription').value || null
                };
                url = `/api/department/${this.currentEditId}`;
                break;
        }

        try {
            await this.apiCall(url, 'PUT', updateData);
            this.editModal.hide();
            
            // Reload appropriate table
            switch (this.currentEditType) {
                case 'student': this.loadStudents(); break;
                case 'course': this.loadCourses(); break;
                case 'department': this.loadDepartments(); break;
            }
        } catch (error) {
            alert('Error updating record: ' + error.message);
        }
    }
}

// Initialize the management system when the page loads
document.addEventListener('DOMContentLoaded', function() {
    window.managementSystem = new ManagementSystem();
});
