const feedbackEl = document.getElementById('feedback');

function showFeedback(message, type) {
    feedbackEl.className = 'visible';
    feedbackEl.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
    setTimeout(() => {
        feedbackEl.className = '';
        feedbackEl.innerHTML = '';
    }, 4000);
}

async function apiFetch(endpoint, options = {}) {
    const url = `${API_BASE}${endpoint}`;
    const config = {
        headers: { 'Content-Type': 'application/json' },
        ...options,
    };
    const res = await fetch(url, config);
    const text = await res.text();
    if (!res.ok) {
        throw new Error(text || `Error ${res.status}`);
    }
    return text;
}

function renderUsers(users) {
    const tbody = document.getElementById('usersTableBody');
    if (!users || users.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="loading">No hay usuarios registrados</td></tr>';
        return;
    }
    tbody.innerHTML = users.map(u => `
        <tr>
            <td>${u.id}</td>
            <td>${u.name}</td>
            <td>${u.email}</td>
            <td><span class="badge badge-${u.role.toLowerCase()}">${u.role}</span></td>
        </tr>
    `).join('');
}

function renderSpaces(spaces) {
    const tbody = document.getElementById('spacesTableBody');
    if (!spaces || spaces.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="loading">No hay espacios registrados</td></tr>';
        return;
    }
    tbody.innerHTML = spaces.map(s => `
        <tr>
            <td>${s.id}</td>
            <td>${s.name}</td>
            <td>${s.location}</td>
            <td>${s.type}</td>
            <td>$${Number(s.price).toFixed(2)}</td>
        </tr>
    `).join('');
}

async function loadUsers() {
    const tbody = document.getElementById('usersTableBody');
    try {
        const text = await apiFetch('/users/all');
        const users = JSON.parse(text);
        renderUsers(users);
    } catch {
        tbody.innerHTML = '<tr><td colspan="4" class="loading">Error al cargar usuarios</td></tr>';
    }
}

async function loadSpaces() {
    const tbody = document.getElementById('spacesTableBody');
    try {
        const text = await apiFetch('/spaces/all');
        const spaces = JSON.parse(text);
        renderSpaces(spaces);
    } catch {
        tbody.innerHTML = '<tr><td colspan="5" class="loading">Error al cargar espacios</td></tr>';
    }
}

document.getElementById('userForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    const data = {
        name: form.name.value.trim(),
        email: form.email.value.trim(),
        password: form.password.value,
        role: form.role.value,
    };

    const btn = form.querySelector('button');
    btn.disabled = true;
    btn.textContent = 'Registrando...';

    try {
        await apiFetch('/users/add', {
            method: 'POST',
            body: JSON.stringify(data),
        });
        showFeedback('Usuario registrado exitosamente', 'success');
        form.reset();
        loadUsers();
    } catch (err) {
        showFeedback(err.message, 'error');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Registrar Usuario';
    }
});

document.getElementById('spaceForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    const data = {
        name: form.name.value.trim(),
        location: form.location.value.trim(),
        type: form.type.value,
        price: parseFloat(form.price.value),
    };

    const btn = form.querySelector('button');
    btn.disabled = true;
    btn.textContent = 'Registrando...';

    try {
        await apiFetch('/spaces/add', {
            method: 'POST',
            body: JSON.stringify(data),
        });
        showFeedback('Espacio registrado exitosamente', 'success');
        form.reset();
        loadSpaces();
    } catch (err) {
        showFeedback(err.message, 'error');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Registrar Espacio';
    }
});

// ===== RESERVATIONS =====

function renderReservations(reservations) {
    const tbody = document.getElementById('reservationsTableBody');
    if (!reservations || reservations.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="loading">No hay reservaciones registradas</td></tr>';
        return;
    }
    tbody.innerHTML = reservations.map(r => `
        <tr>
            <td>${r.id}</td>
            <td>${r.user ? r.user.name + ' (' + r.user.email + ')' : '-'}</td>
            <td>${r.space ? r.space.name : '-'}</td>
            <td>${r.startDate ? new Date(r.startDate).toLocaleString() : '-'}</td>
            <td>${r.endDate ? new Date(r.endDate).toLocaleDateString() : '-'}</td>
            <td><span class="badge badge-${r.status.toLowerCase()}">${r.status}</span></td>
        </tr>
    `).join('');
}

async function loadReservations() {
    const tbody = document.getElementById('reservationsTableBody');
    try {
        const text = await apiFetch('/reservations/all');
        const reservations = JSON.parse(text);
        renderReservations(reservations);
    } catch {
        tbody.innerHTML = '<tr><td colspan="6" class="loading">Error al cargar reservaciones</td></tr>';
    }
}

document.getElementById('reservationForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    const data = {
        userEmail: form.userEmail.value.trim(),
        spaceId: parseInt(form.spaceId.value),
        startDate: form.startDate.value,
        endDate: form.endDate.value || null,
    };

    const btn = form.querySelector('button');
    btn.disabled = true;
    btn.textContent = 'Reservando...';

    try {
        await apiFetch('/reservations/add', {
            method: 'POST',
            body: JSON.stringify(data),
        });
        showFeedback('Reservación creada exitosamente', 'success');
        form.reset();
        loadReservations();
    } catch (err) {
        showFeedback(err.message, 'error');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Crear Reservación';
    }
});

loadUsers();
loadSpaces();
loadReservations();
