/* ================================================================
   DMAS — Shared JS Utilities
   ================================================================ */

const API = 'http://localhost:8080/api';

/* ── TOKEN / SESSION ─────────────────────────────────────── */
function getToken()   { return localStorage.getItem('dmas_token'); }
function getUser()    { try { return JSON.parse(localStorage.getItem('dmas_user')); } catch { return null; } }
function getRole()    { const u = getUser(); return u ? u.role : null; }
function isLoggedIn() { return !!getToken(); }

function saveSession(data) {
  localStorage.setItem('dmas_token', data.token);
  localStorage.setItem('dmas_user', JSON.stringify(data));
}

function logout() {
  localStorage.removeItem('dmas_token');
  localStorage.removeItem('dmas_user');
  window.location.href = '/';
}

/* ── FETCH WRAPPER ────────────────────────────────────────── */
async function apiFetch(path, options = {}) {
  const token = getToken();
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  if (token) headers['Authorization'] = 'Bearer ' + token;
  const res = await fetch(API + path, { ...options, headers });
  if (res.status === 401) { logout(); return null; }
  const text = await res.text();
  try { return { ok: res.ok, status: res.status, data: JSON.parse(text) }; }
  catch { return { ok: res.ok, status: res.status, data: text }; }
}

/* ── TOAST ────────────────────────────────────────────────── */
function showToast(msg, type = 'success') {
  let t = document.getElementById('toast');
  if (!t) {
    t = document.createElement('div');
    t.id = 'toast';
    document.body.appendChild(t);
  }
  t.className = `toast-${type}`;
  t.textContent = msg;
  t.classList.add('show');
  setTimeout(() => t.classList.remove('show'), 3200);
}

/* ── GUARDS ───────────────────────────────────────────────── */
function requireAuth()  { if (!isLoggedIn()) { window.location.href = '/'; } }
function requireAdmin() { requireAuth(); if (getRole() !== 'ADMIN') { window.location.href = '/pages/dashboard.html'; } }

/* ── RENDER TOPBAR ────────────────────────────────────────── */
function renderTopbar() {
  const u = getUser();
  const el = document.getElementById('topbar-user');
  if (el && u) {
    el.innerHTML = `
      <span class="topbar-user">👤 ${u.name}</span>
      <span class="topbar-role">${u.role}</span>
      <button class="btn-logout" onclick="logout()">⏻ Logout</button>`;
  }
}

/* ── RENDER SIDEBAR ───────────────────────────────────────── */
function renderSidebar(active) {
  const role = getRole();
  const u    = getUser();
  const el   = document.getElementById('sidebar');
  if (!el) return;

  let links = `
    <div class="sidebar-section">Navigation</div>
    <a href="/pages/dashboard.html" class="${active==='dashboard'?'active':''}">
      <span>🏠</span> Dashboard</a>
    <a href="/pages/disasters.html" class="${active==='disasters'?'active':''}">
      <span>🌊</span> Disasters</a>
    <a href="/pages/alerts.html" class="${active==='alerts'?'active':''}">
      <span>🔔</span> Alerts</a>
    <a href="/pages/rescue.html" class="${active==='rescue'?'active':''}">
      <span>🚑</span> Rescue Requests</a>`;

  if (role === 'ADMIN') {
    links += `
    <div class="sidebar-section">Admin</div>
    <a href="/pages/analytics.html" class="${active==='analytics'?'active':''}">
      <span>📊</span> Analytics</a>`;
  }

  el.innerHTML = links;
}

/* ── BADGE HELPERS ────────────────────────────────────────── */
function severityBadge(s) {
  const map = { CRITICAL:'badge-red', HIGH:'badge-orange', MEDIUM:'badge-yellow', LOW:'badge-green' };
  return `<span class="badge ${map[s]||'badge-gray'}">${s}</span>`;
}
function statusBadge(s) {
  const map = {
    ACTIVE:'badge-red', MONITORING:'badge-yellow', RESOLVED:'badge-green',
    PENDING:'badge-yellow', ASSIGNED:'badge-blue', ONGOING:'badge-orange',
    COMPLETED:'badge-green', CANCELLED:'badge-gray'
  };
  return `<span class="badge ${map[s]||'badge-gray'}">${s}</span>`;
}
function categoryIcon(c) {
  const map = { FLOOD:'🌊', CYCLONE:'🌀', EARTHQUAKE:'🏔️', FIRE:'🔥', TSUNAMI:'🌊', LANDSLIDE:'⛰️', OTHER:'⚡' };
  return map[c] || '⚡';
}
function formatDate(dt) {
  if (!dt) return '—';
  return new Date(dt).toLocaleString('en-IN', { day:'2-digit', month:'short', year:'numeric', hour:'2-digit', minute:'2-digit' });
}
function urgencyBar(level) {
  const colors = ['','#27ae60','#f1c40f','#e67e22','#e74c3c','#8e1000'];
  const w = level * 20;
  return `<div style="display:flex;align-items:center;gap:6px">
    <div style="width:80px;height:8px;background:#eee;border-radius:4px;overflow:hidden">
      <div style="width:${w}%;height:100%;background:${colors[level]||'#ccc'};border-radius:4px"></div>
    </div>
    <span style="font-size:0.8rem;font-weight:700;color:${colors[level]}">${level}/5</span>
  </div>`;
}

/* ── MODAL HELPERS ────────────────────────────────────────── */
function openModal(id)  { document.getElementById(id).classList.add('open'); }
function closeModal(id) { document.getElementById(id).classList.remove('open'); }
