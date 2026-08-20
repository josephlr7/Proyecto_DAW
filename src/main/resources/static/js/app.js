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
                <td>
                    <div style="display: flex; gap: 8px;">
                        <button class="btn-action-small btn-edit" onclick="editarLaboratorio(${lab.id})">Editar</button>
                        <button class="btn-action-small btn-delete" onclick="eliminarLaboratorio(${lab.id})">Eliminar</button>
                    </div>
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

// Update Laboratory
async function actualizarLaboratorio(id, payload) {
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/laboratorios/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });
        
        if (!response.ok) {
            const errData = await response.json();
            throw new Error(errData.message || 'Error al actualizar laboratorio');
        }
        
        showAlert('Laboratorio actualizado exitosamente');
        closeModal();
        cargarLaboratorios(currentPage);
    } catch (error) {
        showAlert(error.message, true);
    }
}

// Delete Laboratory
async function eliminarLaboratorio(id) {
    if (!confirm('¿Está seguro de que desea eliminar este laboratorio?')) return;
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/laboratorios/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (!response.ok) {
            throw new Error('Error al eliminar laboratorio');
        }
        
        showAlert('Laboratorio eliminado o desactivado correctamente');
        cargarLaboratorios(currentPage);
    } catch (error) {
        showAlert(error.message, true);
    }
}

// Dropdown cascading loaders
let cachedFacultades = [];

async function cargarFacultadesDropdown() {
    const token = getToken();
    if (!token) return;
    try {
        const response = await fetch(`${API_URL}/facultades`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
            cachedFacultades = await response.json();
            const facSelect = document.getElementById('facultad');
            facSelect.innerHTML = '<option value="">Seleccione Facultad</option>';
            cachedFacultades.forEach(f => {
                const opt = document.createElement('option');
                opt.value = f.id;
                opt.textContent = f.nombre;
                facSelect.appendChild(opt);
            });
        }
    } catch (error) {
        console.error('Error al cargar facultades:', error);
    }
}

async function cargarEscuelasDropdown(facultadId, selectedEscuelaNombre = null) {
    const token = getToken();
    if (!token) return;
    try {
        const response = await fetch(`${API_URL}/facultades/${facultadId}/escuelas`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
            const escuelas = await response.json();
            const escSelect = document.getElementById('escuela');
            escSelect.innerHTML = '<option value="">Seleccione Escuela</option>';
            escSelect.disabled = false;
            escuelas.forEach(e => {
                const opt = document.createElement('option');
                opt.value = e.id;
                opt.textContent = e.nombre;
                escSelect.appendChild(opt);
            });
            if (selectedEscuelaNombre) {
                for (let opt of escSelect.options) {
                    if (opt.text.toLowerCase() === selectedEscuelaNombre.toLowerCase()) {
                        escSelect.value = opt.value;
                        break;
                    }
                }
            }
        }
    } catch (error) {
        console.error('Error al cargar escuelas:', error);
    }
}

// Edit Mode Binder
async function editarLaboratorio(id) {
    const token = getToken();
    if (!token) return;
    try {
        const response = await fetch(`${API_URL}/laboratorios/${id}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        if (!response.ok) throw new Error('Error al obtener datos del laboratorio');
        const lab = await response.json();
        
        document.getElementById('labId').value = lab.id;
        document.getElementById('modalTitle').textContent = 'Editar Laboratorio';
        document.getElementById('btnSubmitLab').textContent = 'Actualizar Laboratorio';
        
        document.getElementById('areaInvestigacion').value = lab.areaInvestigacion;
        document.getElementById('lineasInvestigacion').value = lab.lineasInvestigacion;
        document.getElementById('categoria').value = lab.categoria;
        document.getElementById('resolucionNumero').value = lab.resolucionNumero;
        document.getElementById('correoInstitucional').value = lab.correoInstitucional;
        document.getElementById('ods').value = lab.ods;
        document.getElementById('poseeSistemaGestion').value = lab.poseeSistemaGestion.toString();
        
        // Ensure faculties are loaded first
        await cargarFacultadesDropdown();
        
        // Find matching faculty
        const facSelect = document.getElementById('facultad');
        let foundFacId = null;
        for (let opt of facSelect.options) {
            if (opt.text.toLowerCase() === lab.facultad.toLowerCase()) {
                facSelect.value = opt.value;
                foundFacId = opt.value;
                break;
            }
        }
        
        if (foundFacId) {
            await cargarEscuelasDropdown(foundFacId, lab.escuela);
        }
        
        openModalDirect();
    } catch (error) {
        showAlert(error.message, true);
    }
}

// Modal handling
function openModal() {
    document.getElementById('labForm').reset();
    document.getElementById('labId').value = '';
    document.getElementById('modalTitle').textContent = 'Nuevo Laboratorio';
    document.getElementById('btnSubmitLab').textContent = 'Guardar Laboratorio';
    
    const escSelect = document.getElementById('escuela');
    escSelect.innerHTML = '<option value="">Seleccione Escuela</option>';
    escSelect.disabled = true;
    
    cargarFacultadesDropdown();
    openModalDirect();
}

function openModalDirect() {
    const modal = document.getElementById('labModal');
    if (modal) modal.style.display = 'flex';
}

function closeModal() {
    const modal = document.getElementById('labModal');
    if (modal) modal.style.display = 'none';
    const form = document.getElementById('labForm');
    if (form) form.reset();
}

// ==========================================
// CRUD FACULTADES
// ==========================================

async function cargarFacultades() {
    const token = getToken();
    if (!token) return;
    const tableBody = document.getElementById('facultadesTableBody');
    if (!tableBody) return;
    
    try {
        const response = await fetch(`${API_URL}/facultades`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Error al cargar facultades');
        
        const facultades = await response.json();
        tableBody.innerHTML = '';
        
        if (facultades.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="3" class="empty-state">No hay facultades registradas.</td>
                </tr>
            `;
            return;
        }
        
        facultades.forEach(fac => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong># ${fac.id}</strong></td>
                <td>${fac.nombre}</td>
                <td>
                    <div style="display: flex; gap: 8px;">
                        <button class="btn-action-small btn-edit" onclick="editarFacultad(${fac.id})">Editar</button>
                        <button class="btn-action-small btn-delete" onclick="eliminarFacultad(${fac.id})">Eliminar</button>
                    </div>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        showAlert(error.message, true);
    }
}

async function registrarFacultad(payload) {
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/facultades`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            const errData = await response.json();
            throw new Error(errData.message || 'Error al guardar facultad');
        }
        showAlert('Facultad registrada exitosamente');
        closeFacultadModal();
        cargarFacultades();
    } catch (error) {
        showAlert(error.message, true);
    }
}

async function actualizarFacultad(id, payload) {
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/facultades/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            const errData = await response.json();
            throw new Error(errData.message || 'Error al actualizar facultad');
        }
        showAlert('Facultad actualizada exitosamente');
        closeFacultadModal();
        cargarFacultades();
    } catch (error) {
        showAlert(error.message, true);
    }
}

async function eliminarFacultad(id) {
    if (!confirm('¿Está seguro de que desea eliminar esta facultad?')) return;
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/facultades/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) {
            const errData = await response.json();
            throw new Error(errData.message || 'Error al eliminar facultad. Asegúrese de que no tenga escuelas asociadas.');
        }
        showAlert('Facultad eliminada exitosamente');
        cargarFacultades();
    } catch (error) {
        showAlert(error.message, true);
    }
}

function openFacultadModal() {
    document.getElementById('facultadForm').reset();
    document.getElementById('facultadId').value = '';
    document.getElementById('facultadModalTitle').textContent = 'Nueva Facultad';
    document.getElementById('btnSubmitFacultad').textContent = 'Guardar Facultad';
    document.getElementById('facultadModal').style.display = 'flex';
}

function closeFacultadModal() {
    document.getElementById('facultadModal').style.display = 'none';
}

async function editarFacultad(id) {
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/facultades/${id}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Error al obtener facultad');
        const fac = await response.json();
        
        document.getElementById('facultadId').value = fac.id;
        document.getElementById('facultadNombre').value = fac.nombre;
        document.getElementById('facultadModalTitle').textContent = 'Editar Facultad';
        document.getElementById('btnSubmitFacultad').textContent = 'Actualizar Facultad';
        document.getElementById('facultadModal').style.display = 'flex';
    } catch (error) {
        showAlert(error.message, true);
    }
}


// ==========================================
// CRUD ESCUELAS
// ==========================================

async function cargarEscuelas() {
    const token = getToken();
    if (!token) return;
    const tableBody = document.getElementById('escuelasTableBody');
    if (!tableBody) return;
    
    try {
        await fetchFacultadesMap();

        const response = await fetch(`${API_URL}/escuelas`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Error al cargar escuelas');
        
        const escuelas = await response.json();
        tableBody.innerHTML = '';
        
        if (escuelas.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="4" class="empty-state">No hay escuelas registradas.</td>
                </tr>
            `;
            return;
        }
        
        escuelas.forEach(esc => {
            const facNombre = facultadesMap[esc.facultadId] || `Facultad #${esc.facultadId}`;
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong># ${esc.id}</strong></td>
                <td>${esc.nombre}</td>
                <td>${facNombre}</td>
                <td>
                    <div style="display: flex; gap: 8px;">
                        <button class="btn-action-small btn-edit" onclick="editarEscuela(${esc.id})">Editar</button>
                        <button class="btn-action-small btn-delete" onclick="eliminarEscuela(${esc.id})">Eliminar</button>
                    </div>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        showAlert(error.message, true);
    }
}

let facultadesMap = {};
async function fetchFacultadesMap() {
    const token = getToken();
    if (!token) return;
    try {
        const response = await fetch(`${API_URL}/facultades`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
            const list = await response.json();
            facultadesMap = {};
            list.forEach(f => {
                facultadesMap[f.id] = f.nombre;
            });
        }
    } catch (error) {
        console.error('Error loading faculties map:', error);
    }
}

async function registrarEscuela(payload) {
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/escuelas`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            const errData = await response.json();
            throw new Error(errData.message || 'Error al guardar escuela');
        }
        showAlert('Escuela registrada exitosamente');
        closeEscuelaModal();
        cargarEscuelas();
    } catch (error) {
        showAlert(error.message, true);
    }
}

async function actualizarEscuela(id, payload) {
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/escuelas/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            const errData = await response.json();
            throw new Error(errData.message || 'Error al actualizar escuela');
        }
        showAlert('Escuela actualizada exitosamente');
        closeEscuelaModal();
        cargarEscuelas();
    } catch (error) {
        showAlert(error.message, true);
    }
}

async function eliminarEscuela(id) {
    if (!confirm('¿Está seguro de que desea eliminar esta escuela?')) return;
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/escuelas/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) {
            const errData = await response.json();
            throw new Error(errData.message || 'Error al eliminar escuela. Asegúrese de que no esté asociada a laboratorios activos.');
        }
        showAlert('Escuela eliminada exitosamente');
        cargarEscuelas();
    } catch (error) {
        showAlert(error.message, true);
    }
}

async function openEscuelaModal() {
    document.getElementById('escuelaForm').reset();
    document.getElementById('escuelaId').value = '';
    document.getElementById('escuelaModalTitle').textContent = 'Nueva Escuela';
    document.getElementById('btnSubmitEscuela').textContent = 'Guardar Escuela';
    
    await populateEscuelaFacultadDropdown();
    document.getElementById('escuelaModal').style.display = 'flex';
}

function closeEscuelaModal() {
    document.getElementById('escuelaModal').style.display = 'none';
}

async function populateEscuelaFacultadDropdown(selectedId = null) {
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/facultades`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
            const list = await response.json();
            const select = document.getElementById('escuelaFacultadSelect');
            select.innerHTML = '<option value="">Seleccione Facultad</option>';
            list.forEach(f => {
                const opt = document.createElement('option');
                opt.value = f.id;
                opt.textContent = f.nombre;
                select.appendChild(opt);
            });
            if (selectedId) {
                select.value = selectedId;
            }
        }
    } catch (error) {
        console.error('Error populating escuela faculty select:', error);
    }
}

async function editarEscuela(id) {
    const token = getToken();
    try {
        const response = await fetch(`${API_URL}/escuelas/${id}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Error al obtener escuela');
        const esc = await response.json();
        
        document.getElementById('escuelaId').value = esc.id;
        document.getElementById('escuelaNombre').value = esc.nombre;
        
        await populateEscuelaFacultadDropdown(esc.facultadId);
        
        document.getElementById('escuelaModalTitle').textContent = 'Editar Escuela';
        document.getElementById('btnSubmitEscuela').textContent = 'Actualizar Escuela';
        document.getElementById('escuelaModal').style.display = 'flex';
    } catch (error) {
        showAlert(error.message, true);
    }
}
