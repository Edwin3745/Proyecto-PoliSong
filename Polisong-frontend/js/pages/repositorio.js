import { addToCart } from '../state/cart.js';
import { updateCartBadge } from './navbar.js';

const mockGratis = [
  { id:'g1', titulo:'Canción Libre 1', artista:'Artista A', preview:'#' },
  { id:'g2', titulo:'Canción Libre 2', artista:'Artista B', preview:'#' }
];
const mockVenta = [
  { id:'c1', titulo:'Track Premium 1', artista:'Artista C', precio:1.99 },
  { id:'c2', titulo:'Track Premium 2', artista:'Artista D', precio:2.49 }
];
const mockVinilos = [
  { id:'v1', titulo:'Vinilo Clásico', artista:'Banda X', precio:19.99 },
  { id:'v2', titulo:'Vinilo Épico', artista:'Banda Y', precio:24.99 }
];

export function renderRepositorio() {
  return `
    <div class="repositorio-section">
      <div class="top-bar"><h2>Repositorio Musical</h2><p>Explora categorías</p></div>
      ${renderCarrusel('Canciones Gratis', mockGratis, 'gratis')}
      ${renderCarrusel('Canciones a la Venta', mockVenta, 'venta')}
      ${renderCarrusel('Vinilos', mockVinilos, 'vinilos')}
    </div>
  `;
}

function renderCarrusel(titulo, items, tipo){
  return `
    <section class="repo-block">
      <div class="repo-header"><h3>${titulo}</h3><button class="btn-outline ver-mas" data-tipo="${tipo}">Ver más productos</button></div>
      <div class="repo-carousel">
        ${items.map(i => `
          <div class="repo-item" data-id="${i.id}" data-tipo="${tipo}">
            <div class="ri-info">
              <strong>${i.titulo}</strong><span>${i.artista}</span>
            </div>
            <div class="ri-actions">
              ${i.preview ? `<button class='icon-btn mini' data-act='preview' title='Preview'>▶️</button>`:''}
              <button class='icon-btn mini' data-act='detalle' title='Detalles'>ℹ️</button>
              <button class='icon-btn mini' data-act='cart' title='Agregar al carrito'>🛒</button>
            </div>
          </div>`).join('')}
      </div>
    </section>
  `;
}

// 
export function attachRepositorioEvents(container){
  container.querySelectorAll('.repo-item .icon-btn[data-act="cart"]').forEach(btn => {
    btn.addEventListener('click', () => {
      const parent = btn.closest('.repo-item');
      const id = parent.dataset.id; const tipo = parent.dataset.tipo;
      addToCart({ id, tipo });
      updateCartBadge();
      btn.classList.add('added');
    });
  });
}