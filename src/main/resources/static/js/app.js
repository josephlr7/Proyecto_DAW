const API_URL = 'http://localhost:8080/api';

// Helper: Show Notification Alert
function showAlert(message, isError = false) {
    const alertBox = document.getElementById('alertBox');
    if (!alertBox) return;
    
    alertBox.textContent = message;
    alertBox.className = 'alert-box ' + (isError ? 'alert-error' : 'alert-success');
    alertBox.style.display = 'block';
    
    setTimeout(() => {
        alertBox.style.display = 'none';
    }, 4000);
}

// Session management
function saveSession(username, token, rol) {
    localStorage.setItem('jwtToken', token);
    localStorage.setItem('username', username);
    localStorage.setItem('rol', rol);
}

function clearSession() {
    localStorage.clear();
    window.location.href = 'login.html';
}

function getToken() {
    return localStorage.getItem('jwtToken');
}

function checkSession(requireAuth = true) {
    const token = getToken();
    const currentPath = window.location.pathname;
    
    if (requireAuth && !token) {
        window.location.href = 'login.html';
    } else if (!requireAuth && token) {
        window.location.href = 'index.html';
    }
}

// Authenticate / Login
async function login(username, password) {
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });
        
        if (!response.ok) {
            const errData = await response.json().catch(() => ({ message: 'Credenciales inválidas' }));
            throw new Error(errData.message || 'Credenciales incorrectas');
        }
        
        const data = await response.json();
        saveSession(data.username, data.token, data.rol);
        window.location.href = 'index.html';
    } catch (error) {
        showAlert(error.message, true);
    }
}

// Register User
async function registrarUsuario(username, password, rol) {
    try {
        const response = await fetch(`${API_URL}/auth/registro`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password, rol })
        });
        
        if (!response.ok) {
            const errData = await response.json().catch(() => ({ message: 'Error al registrar usuario' }));
            throw new Error(errData.message || 'Error al registrar');
        }
        
        showAlert('Usuario registrado con éxito. Redirigiendo...');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 1500);
    } catch (error) {
        showAlert(error.message, true);
    }
}

// Fetch Laboratories Paged
let currentPage = 0;
const pageSize = 5;

async function cargarLaboratorios(page = 0) {
    const token = getToken();
    if (!token) return;
    
    const tableBody = document.getElementById('labsTableBody');
    if (!tableBody) return;
    
    try {
        const response = await fetch(`${API_URL}/laboratorios/consulta?page=${page}&size=${pageSize}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.status === 403) {
            clearSession();
            return;
        }
        
        if (!response.ok) {
            throw new Error('Error al cargar laboratorios');
        }
        
        const data = await response.json();
        tableBody.innerHTML = '';
        
        const labs = data.content || [];
        
        if (labs.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="7" class="empty-state">
                        <p>No hay laboratorios registrados.</p>
                    </td>
                </tr>
            `;
            updatePaginationInfo(0, 0, true, true);
            return;
        }
        
        labs.forEach(lab => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong># ${lab.id}</strong></td>
                <td>${lab.facultad}</td>
                <td>${lab.escuela}</td>
                <td>${lab.areaInvestigacion}</td>
                <td>${lab.categoria}</td>
                <td><span class="user-badge">${lab.resolucionNumero}</span></td>
                <td>
                    <span style="color: ${lab.poseeSistemaGestion ? 'var(--success)' : 'var(--danger)'}; font-weight: 600;">
                        ${lab.poseeSistemaGestion ? 'Sí' : 'No'}
                    </span>
                </td>
            `;
            tableBody.appendChild(row);
        });
        
        currentPage = page;
        updatePaginationInfo(page, data.totalPages, data.first, data.last);
        
    } catch (error) {
        showAlert(error.message, true);
    }
}

// Update Pagination buttons
function updatePaginationInfo(page, totalPages, isFirst, isLast) {
    const prevBtn = document.getElementById('btnPrev');
    const nextBtn = document.getElementById('btnNext');
    const pageNumInfo = document.getElementById('pageNumInfo');
    
    if (prevBtn) prevBtn.disabled = isFirst;
    if (nextBtn) nextBtn.disabled = isLast;
    
    if (pageNumInfo) {
        pageNumInfo.textContent = totalPages > 0 ? `Página ${page + 1} de ${totalPages}` : 'Página 0 de 0';
    }
}

// Register Laboratory
async function registrarLaboratorio(payload) {
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/laboratorios`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });
        
        if (!response.ok) {
            const errData = await response.json();
            throw new Error(errData.message || 'Error al guardar laboratorio');
        }
        
        showAlert('Laboratorio registrado exitosamente');
        closeModal();
        cargarLaboratorios(currentPage);
    } catch (error) {
        showAlert(error.message, true);
    }
}

// Modal handling
function openModal() {
    const modal = document.getElementById('labModal');
    if (modal) modal.style.display = 'flex';
}

function closeModal() {
    const modal = document.getElementById('labModal');
    if (modal) modal.style.display = 'none';
    const form = document.getElementById('labForm');
    if (form) form.reset();
}
