import { addToCart } from '../state/cart.js';
import { updateCartBadge } from './navbar.js';

let datosCanciones = [];
let datosVinilos = [];

export function renderRepositorio() {
  return `
    <div class="repositorio-section">
      <div class="top-bar"><h2>Repositorio Musical</h2><p>Explora publicaciones</p></div>
      <div id="repoContent">Cargando contenido...</div>
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
              ${tipo !== 'gratis' ? `<button class='icon-btn mini' data-act='cart' title='Agregar al carrito'>🛒</button>`:''}
            </div>
          </div>`).join('')}
      </div>
    </section>
  `;
}

// 
export async function cargarRepositorio(container){
  try {
    const [cRes, vRes] = await Promise.all([
      fetch('http://localhost:8080/api/canciones'),
      fetch('http://localhost:8080/api/vinilos')
    ]);
    datosCanciones = cRes.ok ? await cRes.json() : [];
    datosVinilos = vRes.ok ? await vRes.json() : [];
    const activosCanciones = datosCanciones.filter(c => c.estado === 'ACTIVO');
    const activosVinilos = datosVinilos.filter(v => v.estado === 'ACTIVO');
    // Ordenar por fechaPublicacion descendente si existe el campo
    activosCanciones.sort((a,b) => (b.fechaPublicacion || '').localeCompare(a.fechaPublicacion||''));
    activosVinilos.sort((a,b) => (b.fechaPublicacion || '').localeCompare(a.fechaPublicacion||''));
    const gratis = activosCanciones.filter(c => !c.precio || c.precio === 0);
    const pago = activosCanciones.filter(c => c.precio && c.precio > 0);
    const vinilos = activosVinilos;
    const html = `
      ${renderCarrusel('Canciones Gratis (Recientes)', gratis.map(m => ({ id: m.idProducto, titulo: m.nombre, artista: m.proveedor ? m.proveedor.aliasContacto : 'Desconocido'})), 'gratis')}
      ${renderCarrusel('Canciones de Pago (Recientes)', pago.map(m => ({ id: m.idProducto, titulo: m.nombre + ' - $' + m.precio, artista: m.proveedor ? m.proveedor.aliasContacto : 'Desconocido'})), 'pago')}
      ${renderCarrusel('Vinilos (Recientes)', vinilos.map(m => ({ id: m.idProducto, titulo: m.nombre + (m.precio? ' - $'+m.precio:''), artista: m.artista || (m.proveedor? m.proveedor.aliasContacto:'Desconocido')})), 'vinilo')}
    `;
    const repoContent = container.querySelector('#repoContent');
    if (repoContent) repoContent.innerHTML = html;
    attachRepositorioEvents(container);
  } catch (err) {
    const repoContent = container.querySelector('#repoContent');
    if (repoContent) repoContent.textContent = 'Error cargando repositorio';
  }
}

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