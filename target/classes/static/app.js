const API = 'http://localhost:8000/api';
let currentUser  = null;
let txType       = 'INCOME';
let allTx        = [];
let categories   = [];

const fmt = (n) => 'Rp ' + Number(n).toLocaleString('id-ID');
const today = () => new Date().toISOString().slice(0, 10);

function showAlert(id, msg, type='error') {
  const el = document.getElementById(id);
  el.innerHTML = `<div class="alert alert-${type}">${msg}</div>`;
  setTimeout(() => el.innerHTML = '', 4000);
}

async function api(method, path, body) {
  const opts = { method, headers: { 'Content-Type': 'application/json' } };
  if (body) opts.body = JSON.stringify(body);
  const res = await fetch(API + path, opts);
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || 'Terjadi kesalahan');
  return data;
}

function switchAuthTab(tab) {
  document.querySelectorAll('.auth-tab').forEach((t, i) => {
    t.classList.toggle('active', (i === 0 && tab === 'login') || (i === 1 && tab === 'register'));
  });
  document.getElementById('loginForm').classList.toggle('active', tab === 'login');
  document.getElementById('registerForm').classList.toggle('active', tab === 'register');
}

async function doLogin() {
  const username = document.getElementById('loginUsername').value.trim();
  const password = document.getElementById('loginPassword').value;
  if (!username || !password) return showAlert('loginAlert', 'Username dan password wajib diisi');
  try {
    const user = await api('POST', '/users/login', { username, password });
    loginSuccess(user);
  } catch(e) { showAlert('loginAlert', e.message); }
}

async function doRegister() {
  const username = document.getElementById('regUsername').value.trim();
  const email    = document.getElementById('regEmail').value.trim();
  const password = document.getElementById('regPassword').value;
  if (!username || !email || !password) return showAlert('registerAlert', 'Semua kolom wajib diisi');
  try {
    await api('POST', '/users/register', { username, email, password });
    showAlert('registerAlert', 'Registrasi berhasil! Silakan masuk.', 'success');
    switchAuthTab('login');
    document.getElementById('loginUsername').value = username;
  } catch(e) { showAlert('registerAlert', e.message); }
}

function loginSuccess(user) {
  currentUser = user;
  document.getElementById('authPage').style.display = 'none';
  document.getElementById('appPage').style.display  = 'block';
  document.getElementById('navUsername').textContent = user.username;
  initApp();
}

function doLogout() {
  currentUser = null;
  allTx = []; categories = [];
  document.getElementById('appPage').style.display  = 'none';
  document.getElementById('authPage').style.display = 'flex';
}

async function initApp() {
  await loadCategories();
  await loadTransactions();
  loadDashboard();
  const now = new Date();
  document.getElementById('reportMonth').value = now.getMonth() + 1;
  document.getElementById('reportYear').value  = now.getFullYear();
}

function switchTab(name) {
  document.querySelectorAll('.nav-tab').forEach(t => t.classList.remove('active'));
  document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
  event.currentTarget.classList.add('active');
  document.getElementById('tab-' + name).classList.add('active');
  if (name === 'transactions') renderTxList(allTx, 'allTxList');
  if (name === 'categories')   renderCategories();
}

async function loadTransactions() {
  allTx = await api('GET', `/transactions?userId=${currentUser.id}`);
  renderTxList(allTx.slice(-5).reverse(), 'dashTxList');
  renderTxList(allTx, 'allTxList');
}

async function loadCategories() {
  categories = await api('GET', '/categories');
  populateCategorySelects();
}

function populateCategorySelects() {
  const selects = ['filterCategory', 'txCategory'];
  selects.forEach(id => {
    const el = document.getElementById(id);
    if (!el) return;
    const firstOpt = id === 'filterCategory' ? '<option value="">Semua Kategori</option>' : '';
    el.innerHTML = firstOpt + categories.map(c =>
      `<option value="${c.id}">${c.name}</option>`
    ).join('');
  });
}

function renderTxList(txs, containerId) {
  const el = document.getElementById(containerId);
  if (!txs || txs.length === 0) {
    el.innerHTML = `<div class="empty-state"><div class="icon">💳</div><div>Belum ada transaksi</div></div>`;
    return;
  }
  el.innerHTML = txs.map(t => {
    const isIncome  = t.type === 'INCOME';
    const icon      = isIncome ? '💰' : '💸';
    const cls       = isIncome ? 'income' : 'expense';
    const sign      = isIncome ? '+' : '-';
    const meta      = isIncome
      ? `${t.date} · ${t.source || 'Pemasukan'}`
      : `${t.date} · ${t.categoryName || 'Pengeluaran'}`;
    return `
      <div class="tx-item">
        <div class="tx-icon ${cls}">${icon}</div>
        <div class="tx-info">
          <div class="desc">${t.description || '-'}</div>
          <div class="meta">${meta}</div>
        </div>
        <div class="tx-amount ${cls}">${sign} ${fmt(t.amount)}</div>
        <div class="tx-actions">
          <button class="btn btn-outline btn-sm" onclick="openEditModal(${t.id})">✏️</button>
          <button class="btn btn-danger btn-sm" onclick="deleteTx(${t.id})">🗑️</button>
        </div>
      </div>`;
  }).join('');
}

function loadDashboard() {
  const now   = new Date();
  const month = now.getMonth() + 1;
  const year  = now.getFullYear();
  const monthTx = allTx.filter(t => {
    const d = new Date(t.date);
    return d.getMonth() + 1 === month && d.getFullYear() === year;
  });
  const income  = monthTx.filter(t => t.type === 'INCOME').reduce((s, t) => s + t.amount, 0);
  const expense = monthTx.filter(t => t.type === 'EXPENSE').reduce((s, t) => s + t.amount, 0);
  document.getElementById('dashIncome').textContent  = fmt(income);
  document.getElementById('dashExpense').textContent = fmt(expense);
  document.getElementById('dashBalance').textContent = fmt(income - expense);
  renderTxList([...allTx].reverse().slice(0, 5), 'dashTxList');
}

function applyFilter() {
  const type    = document.getElementById('filterType').value;
  const catId   = document.getElementById('filterCategory').value;
  const start   = document.getElementById('filterStart').value;
  const end     = document.getElementById('filterEnd').value;
  const keyword = document.getElementById('filterKeyword').value.toLowerCase();

  let filtered = [...allTx];
  if (type)    filtered = filtered.filter(t => t.type === type);
  if (catId)   filtered = filtered.filter(t => t.type === 'EXPENSE' && String(t.categoryId) === catId);
  if (start)   filtered = filtered.filter(t => t.date >= start);
  if (end)     filtered = filtered.filter(t => t.date <= end);
  if (keyword) filtered = filtered.filter(t => (t.description || '').toLowerCase().includes(keyword));
  renderTxList(filtered, 'allTxList');
}

function resetFilter() {
  ['filterType','filterCategory'].forEach(id => document.getElementById(id).value = '');
  ['filterStart','filterEnd','filterKeyword'].forEach(id => document.getElementById(id).value = '');
  renderTxList(allTx, 'allTxList');
}

function openAddModal() {
  txType = 'INCOME';
  updateTypeUI();
  document.getElementById('txAmount').value = '';
  document.getElementById('txDate').value   = today();
  document.getElementById('txDesc').value   = '';
  document.getElementById('txSource').value = '';
  document.getElementById('modalAlert').innerHTML = '';
  document.getElementById('addModal').classList.add('open');
}
function closeAddModal() { document.getElementById('addModal').classList.remove('open'); }

function setTxType(type) {
  txType = type;
  updateTypeUI();
}
function updateTypeUI() {
  document.getElementById('incomeBtn').classList.toggle('active', txType === 'INCOME');
  document.getElementById('expenseBtn').classList.toggle('active', txType === 'EXPENSE');
  document.getElementById('sourceGroup').classList.toggle('hidden', txType !== 'INCOME');
  document.getElementById('categoryGroup').classList.toggle('hidden', txType !== 'EXPENSE');
}

async function submitTransaction() {
  const amount = document.getElementById('txAmount').value;
  const date   = document.getElementById('txDate').value;
  const desc   = document.getElementById('txDesc').value.trim();
  if (!amount || !date) return showAlert('modalAlert', 'Jumlah dan tanggal wajib diisi');

  try {
    if (txType === 'INCOME') {
      const source = document.getElementById('txSource').value.trim();
      await api('POST', `/transactions/income?userId=${currentUser.id}`,
                { amount, date, description: desc, source });
    } else {
      const categoryId = document.getElementById('txCategory').value;
      await api('POST', `/transactions/expense?userId=${currentUser.id}`,
                { amount, date, description: desc, categoryId });
    }
    closeAddModal();
    await loadTransactions();
    loadDashboard();
  } catch(e) { showAlert('modalAlert', e.message); }
}

function openEditModal(id) {
  const tx = allTx.find(t => t.id === id);
  if (!tx) return;
  document.getElementById('editId').value     = tx.id;
  document.getElementById('editAmount').value = tx.amount;
  document.getElementById('editDate').value   = tx.date;
  document.getElementById('editDesc').value   = tx.description || '';
  document.getElementById('editAlert').innerHTML = '';
  document.getElementById('editModal').classList.add('open');
}
function closeEditModal() { document.getElementById('editModal').classList.remove('open'); }

async function submitEdit() {
  const id     = document.getElementById('editId').value;
  const amount = document.getElementById('editAmount').value;
  const date   = document.getElementById('editDate').value;
  const desc   = document.getElementById('editDesc').value.trim();
  if (!amount || !date) return showAlert('editAlert', 'Jumlah dan tanggal wajib diisi');
  try {
    await api('PUT', `/transactions/${id}`, { amount, date, description: desc });
    closeEditModal();
    await loadTransactions();
    loadDashboard();
    applyFilter();
  } catch(e) { showAlert('editAlert', e.message); }
}

async function deleteTx(id) {
  if (!confirm('Hapus transaksi ini?')) return;
  try {
    await api('DELETE', `/transactions/${id}`);
    await loadTransactions();
    loadDashboard();
    applyFilter();
  } catch(e) { alert(e.message); }
}

async function loadReport() {
  const month = document.getElementById('reportMonth').value;
  const year  = document.getElementById('reportYear').value;
  try {
    const report = await api('GET', `/reports/monthly?userId=${currentUser.id}&month=${month}&year=${year}`);
    document.getElementById('repIncome').textContent  = fmt(report.totalIncome);
    document.getElementById('repExpense').textContent = fmt(report.totalExpense);
    document.getElementById('repBalance').textContent = fmt(report.balance);
    document.getElementById('reportResult').classList.remove('hidden');
    renderTxList(report.transactions, 'reportTxList');
  } catch(e) { alert(e.message); }
}

function renderCategories() {
  const grid = document.getElementById('catGrid');
  if (categories.length === 0) {
    grid.innerHTML = `<div class="empty-state"><div class="icon">🏷️</div><div>Belum ada kategori</div></div>`;
    return;
  }
  grid.innerHTML = categories.map(c => `
    <div class="cat-card">
      <div class="cat-info">
        <div class="name">🏷️ ${c.name}</div>
        <div class="desc">${c.description || ''}</div>
      </div>
      <button class="btn btn-danger btn-sm" onclick="deleteCategory(${c.id})">🗑️</button>
    </div>`
  ).join('');
}

function openCatModal() {
  document.getElementById('catName').value = '';
  document.getElementById('catDesc').value = '';
  document.getElementById('catAlert').innerHTML = '';
  document.getElementById('catModal').classList.add('open');
}
function closeCatModal() { document.getElementById('catModal').classList.remove('open'); }

async function submitCategory() {
  const name = document.getElementById('catName').value.trim();
  const desc = document.getElementById('catDesc').value.trim();
  if (!name) return showAlert('catAlert', 'Nama kategori wajib diisi');
  try {
    await api('POST', '/categories', { name, description: desc });
    closeCatModal();
    await loadCategories();
    renderCategories();
  } catch(e) { showAlert('catAlert', e.message); }
}

async function deleteCategory(id) {
  if (!confirm('Hapus kategori ini?')) return;
  try {
    await api('DELETE', `/categories/${id}`);
    await loadCategories();
    renderCategories();
  } catch(e) { alert(e.message); }
}
