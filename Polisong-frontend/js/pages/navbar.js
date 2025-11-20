import { getCartCount } from '../state/cart.js';

export function ensureNavbar(nombre) {
  if (document.getElementById('topNavbar')) return;
  const bar = document.createElement('header');
  bar.id = 'topNavbar';
  bar.className = 'top-navbar';
  bar.innerHTML = `
    <div class="nav-left">
      <span class="logo-mini">PS</span>
      <div class="search-box">
        <input type="text" id="globalSearch" placeholder="Buscar..." />
      </div>
    </div>
    <div class="nav-right">
      <button id="btnCart" class="icon-btn" title="Carrito">
        <span class="icon">🛒</span><span id="cartCount" class="badge">${getCartCount()}</span>
      </button>
      <button id="btnNoti" class="icon-btn" title="Notificaciones">🔔</button>
      <button id="btnSettings" class="icon-btn" title="Configuración">⚙️</button>
      <div class="user-chip">${nombre || 'Usuario'}</div>
    </div>`;
  document.body.appendChild(bar);
  updateCartBadge();
}

export function updateCartBadge() {
  const badge = document.getElementById('cartCount');
  if (badge) badge.textContent = getCartCount();
}
