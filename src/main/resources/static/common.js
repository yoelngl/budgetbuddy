const API = '/api';

const fmt   = (n) => 'Rp ' + Number(n).toLocaleString('id-ID');
const today = ()  => new Date().toISOString().slice(0, 10);

// ── Toast notification system ──────────────────────────────────────────
const _toastIcons = { success: '✅', error: '❌', warning: '⚠️', info: 'ℹ️' };

function toast(message, type = 'info', duration = 3500) {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }
  const el = document.createElement('div');
  el.className = `toast toast-${type}`;
  el.innerHTML = `<span class="toast-icon">${_toastIcons[type] || 'ℹ️'}</span>`
               + `<span class="toast-msg">${message}</span>`
               + `<button class="toast-close" onclick="this.parentElement._dismiss()">✕</button>`;
  el._dismiss = () => {
    el.classList.add('toast-out');
    setTimeout(() => el.remove(), 300);
  };
  el.container = container;
  container.appendChild(el);
  setTimeout(() => el._dismiss && el._dismiss(), duration);
}

// Legacy compat — redirect old inline alerts to toast
function showAlert(id, msg, type = 'error') {
  toast(msg, type === 'success' ? 'success' : 'error');
}

async function api(method, path, body) {
  const opts = {
    method,
    headers: { 'Content-Type': 'application/json' },
    credentials: 'same-origin'
  };
  if (body) opts.body = JSON.stringify(body);
  const res = await fetch(API + path, opts);
  const data = await res.json().catch(() => ({}));
  if (res.status === 401) {
    if (window.location.pathname !== '/login') { window.location.href = '/login'; return null; }
    throw new Error(data.error || 'Username atau password salah');
  }
  if (!res.ok) throw new Error(data.error || 'Terjadi kesalahan');
  return data;
}

async function doLogout() {
  await fetch('/api/users/logout', { method: 'POST', credentials: 'same-origin' });
  window.location.href = '/login';
}

// Highlight active nav link based on current URL
(function () {
  const path = window.location.pathname;
  document.querySelectorAll('.nav-link').forEach(a => {
    a.classList.toggle('active', a.getAttribute('href') === path);
  });
})();
