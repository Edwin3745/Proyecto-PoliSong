
const LS_KEYS_BASE = {
  ARTIST_PROFILE: 'providerArtistProfile',
  PRODUCTS: 'providerProducts',
  RATINGS: 'providerRatings',
  ORDERS: 'providerOrders'
};

function ns(key){
  const id = localStorage.getItem('idProveedor');
  return id ? `${key}_${id}` : key;
}

function loadLocal(key, fallback) {
  try { return JSON.parse(localStorage.getItem(ns(key))) || fallback; } catch { return fallback; }
}
function saveLocal(key, value) { localStorage.setItem(ns(key), JSON.stringify(value)); }

import { API_URL } from '../api/api.js';

export function mostrarProviderDashboard(container) {
  const profile = loadLocal(LS_KEYS_BASE.ARTIST_PROFILE, {
    nombreArtistico: 'Sin nombre',
    bio: 'Agrega una breve biografía o descripción artística.',
    metricas: { reproducciones: 0, productos: 0, valoracionPromedio: 0 }
  });
    const productos = loadLocal(LS_KEYS_BASE.PRODUCTS, []);
  const valoraciones = loadLocal(LS_KEYS_BASE.RATINGS, []);
    const pedidos = loadLocal(LS_KEYS_BASE.ORDERS, []);

  const idProveedor = localStorage.getItem('idProveedor');
  if(idProveedor){
    fetch(`${API_URL}/proveedores/${idProveedor}`)
      .then(r => r.ok ? r.json() : null)
      .then(data => { if(data){ profile.nombreArtistico = data.aliasContacto || profile.nombreArtistico; saveLocal(LS_KEYS_BASE.ARTIST_PROFILE, profile); const contentEl = container.querySelector('#providerContent'); if(contentEl){ contentEl.innerHTML = renderPerfilArtista(profile, productos, valoraciones); attachPerfilListeners(contentEl, profile); } }})
      .catch(()=>{});
  }

  container.innerHTML = `
    <div class="top-bar">
      <div>
        <h2>Panel del Proveedor</h2>
        <p>Gestiona tu perfil artístico y tus productos.</p>
      </div>
      <button id="btnSalirProveedor" class="btn btn-outline">Cerrar sesión</button>
    </div>
    <div class="provider-tabs">
      <button class="tab-btn active" data-tab="perfil-artista">Perfil Artista</button>
      <button class="tab-btn" data-tab="publicar">Publicar Contenido</button>
      <button class="tab-btn" data-tab="inventario">Inventario</button>
      <button class="tab-btn" data-tab="valoraciones">Valoraciones</button>
      <button class="tab-btn" data-tab="pedidos">Pedidos Activos</button>
    </div>
    <div id="providerContent" class="provider-content">
      ${renderPerfilArtista(profile, productos, valoraciones)}
    </div>
  `;

  const tabButtons = container.querySelectorAll('.tab-btn');
  const content = container.querySelector('#providerContent');

  tabButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      tabButtons.forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      const tab = btn.dataset.tab;
      switch(tab) {
        case 'perfil-artista':
          content.innerHTML = renderPerfilArtista(profile, productos, valoraciones);
          attachPerfilListeners(content, profile);
          break;
        case 'publicar':
          content.innerHTML = renderPublicarProducto();
          attachPublicarListeners(content, productos, profile);
          break;
        case 'inventario':
          cargarInventarioBackend(content);
          break;
        case 'valoraciones':
          content.innerHTML = renderValoraciones(valoraciones);
          attachValoracionesListeners(content, valoraciones);
          break;
        case 'pedidos':
          content.innerHTML = renderPedidos(pedidos);
          attachPedidosListeners(content, pedidos);
          break;
      }
    });
  });

  // Listeners iniciales para perfil
  attachPerfilListeners(container.querySelector('#providerContent'), profile);

  // Botón logout proveedor
  const logoutBtn = container.querySelector('#btnSalirProveedor');
  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
      const mainEl = document.getElementById('contenido') || document.querySelector('main');
      localStorage.removeItem('userRole');
      localStorage.removeItem('userEmail');
      localStorage.removeItem('idProveedor');
      import('./login.js').then(m => m.mostrarLogin(mainEl));
    });
  }
}

/* =============================
   PERFIL ARTISTA
============================= */
function renderPerfilArtista(profile, productos, valoraciones) {
  const avgRating = valoraciones.length
    ? (valoraciones.reduce((a, v) => a + v.puntuacion, 0) / valoraciones.length).toFixed(1)
    : 0;
  profile.metricas.productos = productos.length;
  profile.metricas.valoracionPromedio = avgRating;
  return `
    <div class="panel-section">
      <h3>Perfil de Artista</h3>
      <form id="artistProfileForm" class="form-grid">
        <div class="fg">
          <label>Nombre artístico</label>
          <input type="text" name="nombreArtistico" value="${profile.nombreArtistico}">
        </div>
        <div class="fg full">
          <label>Biografía / descripción</label>
          <textarea name="bio" rows="4">${profile.bio}</textarea>
        </div>
        <button class="btn btn-primary" type="submit">Guardar Perfil</button>
      </form>
      <div class="metrics-grid">
        <div class="metric-box"><span class="m-label">Reproducciones</span><strong>${profile.metricas.reproducciones}</strong></div>
        <div class="metric-box"><span class="m-label">Productos</span><strong>${profile.metricas.productos}</strong></div>
        <div class="metric-box"><span class="m-label">Valoración</span><strong>${profile.metricas.valoracionPromedio}</strong></div>
      </div>
    </div>
  `;
}
function attachPerfilListeners(scope, profile) {
  const form = scope.querySelector('#artistProfileForm');
  if(!form) return;
  form.addEventListener('submit', e => {
    e.preventDefault();
    const fd = new FormData(form);
    profile.nombreArtistico = fd.get('nombreArtistico');
    profile.bio = fd.get('bio');
    saveLocal(LS_KEYS_BASE.ARTIST_PROFILE, profile);
    form.querySelector('button').textContent = 'Guardado';
    setTimeout(()=> form.querySelector('button').textContent = 'Guardar Perfil',1400);
  });
}

/* =============================
   PUBLICAR PRODUCTO
============================= */
function renderPublicarProducto() {
  return `
    <div class="panel-section">
      <h3>Publicar Contenido</h3>
      <form id="publicarUnicoForm" class="form-grid">
        <div class="fg full">
          <label>Tipo de Publicación</label>
          <select name="tipo" id="tipoPublicacion">
            <option value="GRATIS" selected>Canción Gratis</option>
            <option value="PAGO">Canción con Precio</option>
            <option value="VINILO">Vinilo</option>
          </select>
        </div>
        <div class="fg">
          <label id="labelTitulo">Título / Nombre</label>
          <input type="text" name="titulo" required>
        </div>
        <div class="fg" id="grupoArtista">
          <label>Artista</label>
          <input type="text" name="artista" placeholder="Alias o nombre" required>
        </div>
        <div class="fg" id="grupoPrecio" style="display:none;">
          <label>Precio</label>
          <input type="number" step="0.01" name="precio">
        </div>
        <div class="fg" id="grupoStock" style="display:none;">
          <label>Stock</label>
          <input type="number" name="stock">
        </div>
        <div class="fg full">
          <label>Vista previa</label>
          <div id="previewBox" class="preview-box">Selecciona tipo y completa campos.</div>
        </div>
        <div class="fg full actions-row">
          <button type="submit" class="btn btn-primary">Publicar</button>
        </div>
      </form>
    </div>
  `;
}
function attachPublicarListeners(scope, productos, profile) {
  const form = scope.querySelector('#publicarUnicoForm');
  if(!form) return;
  const tipoSelect = form.querySelector('#tipoPublicacion');
  const grupoPrecio = form.querySelector('#grupoPrecio');
  const grupoStock = form.querySelector('#grupoStock');
  const preview = form.querySelector('#previewBox');
  const idProveedor = localStorage.getItem('idProveedor');

  function actualizarVisibilidadCampos() {
    const tipo = tipoSelect.value;
    if (tipo === 'GRATIS') {
      grupoPrecio.style.display = 'none';
      grupoStock.style.display = 'none';
      form.querySelector('input[name="precio"]').value = '';
      form.querySelector('input[name="stock"]').value = '';
    } else if (tipo === 'PAGO') {
      grupoPrecio.style.display = '';
      grupoStock.style.display = 'none';
      form.querySelector('input[name="stock"]').value = '';
    } else { // VINILO
      grupoPrecio.style.display = '';
      grupoStock.style.display = '';
    }
  }

  tipoSelect.addEventListener('change', () => { actualizarVisibilidadCampos(); actualizarPreview(); });
  form.addEventListener('input', actualizarPreview);

  function actualizarPreview() {
    const fd = new FormData(form);
    const tipo = fd.get('tipo');
    const titulo = fd.get('titulo') || '(Sin título)';
    const precio = fd.get('precio');
    const stock = fd.get('stock');
    preview.innerHTML = `<div class="product-card-preview"><strong>${titulo}</strong><br><small>Tipo: ${tipo}</small>${(tipo==='PAGO' || tipo==='VINILO') && precio? `<div>Precio: $${precio}</div>`:''}${tipo==='VINILO' && stock? `<div>Stock: ${stock}</div>`:''}</div>`;
  }

  actualizarVisibilidadCampos();
  actualizarPreview();

  form.addEventListener('submit', async e => {
    e.preventDefault();
    const fd = new FormData(form);
    const tipo = fd.get('tipo');
    const titulo = fd.get('titulo');
    const artista = fd.get('artista');
    const precio = fd.get('precio');
    const stock = fd.get('stock');
    if(!titulo || !titulo.trim()) { preview.textContent='Título requerido'; return; }
    if(tipo === 'PAGO' && (!precio || Number(precio) <= 0)) { preview.textContent='Precio > 0 requerido'; return; }
    if(tipo === 'VINILO') {
      if(!precio || Number(precio) <= 0) { preview.textContent='Precio > 0 requerido'; return; }
      if(stock === '' || Number(stock) < 0) { preview.textContent='Stock inválido'; return; }
    }
    if(!idProveedor){ preview.textContent='No idProveedor (inicia sesión).'; return; }
    try {
      if(tipo === 'VINILO') {
        const payloadVinilo = { nombre: titulo, artista: artista || titulo, precio: Number(precio), cantidadDisponible: Number(stock), estado:'ACTIVO' };
        const res = await fetch(`http://localhost:8080/api/proveedores/${idProveedor}/vinilos`, { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(payloadVinilo) });
        const data = await res.json(); if(!res.ok) throw new Error(data.mensaje || 'Error publicando vinilo');
        productos.push({ id: Date.now(), tipo:'VINILO', titulo, precio:Number(precio), stock:Number(stock), estado:'ACTIVO', metricas:{descargas:0,ventas:0,reproducciones:0,likes:0} });
        preview.textContent='Vinilo publicado.';
      } else { // Canción
        const precioFinal = (tipo === 'PAGO') ? Number(precio) : 0;
        const payloadCancion = { nombre: titulo, precio: precioFinal, estado:'ACTIVO' };
        const res = await fetch(`http://localhost:8080/api/proveedores/${idProveedor}/canciones`, { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(payloadCancion) });
        const data = await res.json(); if(!res.ok) throw new Error(data.mensaje || 'Error publicando canción');
        productos.push({ id: Date.now(), tipo: tipo, titulo, precio: precioFinal, estado:'ACTIVO', metricas:{descargas:0,ventas:0,reproducciones:0,likes:0} });
        preview.textContent = tipo==='GRATIS' ? 'Canción gratis publicada.' : 'Canción de pago publicada.';
      }
      saveLocal(LS_KEYS.PRODUCTS, productos);
      const prof = loadLocal(LS_KEYS.ARTIST_PROFILE, profile); prof.metricas.productos = productos.length; saveLocal(LS_KEYS.ARTIST_PROFILE, prof);
      form.reset(); tipoSelect.value='GRATIS'; actualizarVisibilidadCampos(); actualizarPreview();
    } catch(err){ preview.textContent='Error: '+ err.message; }
  });
}

/* =============================
   INVENTARIO
============================= */
async function cargarInventarioBackend(scope){
  const idProveedor = localStorage.getItem('idProveedor');
  if(!idProveedor){ scope.innerHTML = '<div class="panel-section"><h3>Inventario</h3><p class="muted">No hay proveedor activo.</p></div>'; return; }
  try {
    // Intento 1: endpoint combinado
    const res = await fetch(`${API_URL}/proveedores/${idProveedor}/inventario`);
    console.log('[Inventario] status', res.status);
    if(res.ok){
      const data = await res.json();
      console.log('[Inventario] data', data);
      if(data && data.canciones && data.vinilos){
        const listado = [
          ...data.canciones.map(c => ({ ...c, estado: c.estado || 'ACTIVO', tipo: (c.precio && c.precio>0) ? 'PAGO' : 'GRATIS' })),
          ...data.vinilos.map(v => ({ ...v, estado: v.estado || 'ACTIVO', tipo: 'VINILO' }))
        ];
        scope.innerHTML = renderInventario(listado);
        attachInventarioListeners(scope, listado, {});
        return;
      }
    }
    // Fallback: pedir listados separados
    console.warn('[Inventario] usando fallback separado');
    const [cancRes, vinRes] = await Promise.all([
      fetch(`${API_URL}/proveedores/${idProveedor}/canciones`),
      fetch(`${API_URL}/proveedores/${idProveedor}/productos`)
    ]);
    const canciones = cancRes.ok ? await cancRes.json() : [];
    const vinilos = vinRes.ok ? await vinRes.json() : [];
    const listado = [
      ...canciones.map(c => ({ ...c, estado: c.estado || 'ACTIVO', tipo: (c.precio && c.precio>0) ? 'PAGO' : 'GRATIS' })),
      ...vinilos.map(v => ({ ...v, estado: v.estado || 'ACTIVO', tipo: 'VINILO' }))
    ];
    scope.innerHTML = renderInventario(listado);
    attachInventarioListeners(scope, listado, {});
  } catch(err){
    console.error('[Inventario] error', err);
    scope.innerHTML = `<div class="panel-section"><h3>Inventario</h3><p class="muted">Error cargando inventario: ${err.message}</p></div>`;
  }
}
function renderInventario(productos) {
  if(!productos.length) {
    return `<div class="panel-section"><h3>Inventario</h3><p class="muted">Aún no hay productos publicados.</p></div>`;
  }
  return `
    <div class="panel-section">
      <h3>Inventario</h3>
      <table class="inventario-table">
        <thead><tr><th>Título</th><th>Tipo</th><th>Estado</th><th>Precio</th><th>Stock</th><th>Acciones</th></tr></thead>
        <tbody>
          ${productos.map(p => `
            <tr data-id="${p.idProducto}" data-tipo="${p.tipo}">
              <td>${p.nombre || p.titulo}</td>
              <td>${p.tipo}</td>
              <td><span class="estado ${p.estado === 'ACTIVO' ? 'on' : 'off'}">${p.estado}</span></td>
              <td>${p.precio != null ? p.precio : '-'}</td>
              <td>${p.cantidadDisponible != null ? p.cantidadDisponible : '-'}</td>
              <td>
                <button class="btn btn-outline btn-sm" data-act="toggle">${p.estado === 'ACTIVO' ? 'Desactivar' : 'Activar'}</button>
              </td>
            </tr>
          `).join('')}
        </tbody>
      </table>
    </div>
  `;
}
function attachInventarioListeners(scope, productos, profile) {
  const tbody = scope.querySelector('tbody');
  if(!tbody) return;
  tbody.addEventListener('click', async e => {
    const btn = e.target.closest('button[data-act="toggle"]');
    if(!btn) return;
    const tr = btn.closest('tr');
    const idProducto = tr.dataset.id;
    const tipo = tr.dataset.tipo;
    const idProveedor = localStorage.getItem('idProveedor');
    if(!idProveedor) return;
    const estadoActual = tr.querySelector('.estado').textContent;
    const nuevoEstado = estadoActual === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
    try {
      if(tipo === 'VINILO') {
        await fetch(`http://localhost:8080/api/proveedores/${idProveedor}/vinilos/${idProducto}`, { method:'PUT', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ estado: nuevoEstado }) });
      } else {
        await fetch(`http://localhost:8080/api/proveedores/${idProveedor}/canciones/${idProducto}`, { method:'PUT', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ estado: nuevoEstado }) });
      }
      tr.querySelector('.estado').textContent = nuevoEstado;
      tr.querySelector('.estado').className = `estado ${nuevoEstado === 'ACTIVO' ? 'on' : 'off'}`;
      btn.textContent = nuevoEstado === 'ACTIVO' ? 'Desactivar' : 'Activar';
    } catch(err){ console.error('Error cambiando estado', err); }
  });
}

/* =============================
   VALORACIONES
============================= */
function renderValoraciones(valoraciones) {
  if(!valoraciones.length) {
    return `<div class="panel-section"><h3>Valoraciones</h3><p class="muted">Sin valoraciones todavía.</p></div>`;
  }
  return `
    <div class="panel-section">
      <h3>Valoraciones</h3>
      <div class="sort-bar">
        <select id="sortValoraciones">
          <option value="recent">Más recientes</option>
          <option value="best">Mejor puntuadas</option>
          <option value="worst">Peor puntuadas</option>
        </select>
      </div>
      <ul class="valoraciones-list">
        ${valoraciones.map(v => `
          <li>
            <div class="v-head"><strong>${v.puntuacion}★</strong> por ${v.usuario} - <small>${new Date(v.fecha).toLocaleDateString()}</small></div>
            <p>${v.comentario}</p>
          </li>
        `).join('')}
      </ul>
    </div>
  `;
}
function attachValoracionesListeners(scope, valoraciones) {
  const sel = scope.querySelector('#sortValoraciones');
  if(!sel) return;
  sel.addEventListener('change', () => {
    let sorted = [...valoraciones];
    if(sel.value === 'recent') sorted.sort((a,b)=> b.fecha - a.fecha);
    if(sel.value === 'best') sorted.sort((a,b)=> b.puntuacion - a.puntuacion);
    if(sel.value === 'worst') sorted.sort((a,b)=> a.puntuacion - b.puntuacion);
    scope.innerHTML = renderValoraciones(sorted);
    attachValoracionesListeners(scope, sorted);
  });
}

/* =============================
   PEDIDOS
============================= */
function renderPedidos(pedidos) {
  if(!pedidos.length) return `<div class="panel-section"><h3>Pedidos Activos</h3><p class="muted">No hay pedidos aún.</p></div>`;
  return `
    <div class="panel-section">
      <h3>Pedidos Activos</h3>
      <table class="pedidos-table">
        <thead><tr><th>Cliente</th><th>Producto</th><th>Fecha</th><th>Estado</th><th>Acción</th></tr></thead>
        <tbody>
          ${pedidos.map(p => `
            <tr data-id="${p.id}">
              <td>${p.cliente}</td>
              <td>${p.producto}</td>
              <td>${new Date(p.fecha).toLocaleDateString()}</td>
              <td>${p.estado}</td>
              <td>${p.tipo === 'VINILO' ? `<button class="btn btn-outline btn-sm" data-act="avance">Avanzar Estado</button>` : '-'}</td>
            </tr>
          `).join('')}
        </tbody>
      </table>
    </div>
  `;
}
function attachPedidosListeners(scope, pedidos) {
  scope.querySelector('tbody')?.addEventListener('click', e => {
    const btn = e.target.closest('button[data-act="avance"]');
    if(!btn) return;
    const tr = btn.closest('tr');
    const id = Number(tr.dataset.id);
    const pedido = pedidos.find(p => p.id === id);
    if(!pedido) return;
    const flow = ['PENDIENTE','PREPARANDO','ENVIADO','ENTREGADO'];
    const idx = flow.indexOf(pedido.estado);
    pedido.estado = flow[Math.min(idx+1, flow.length-1)];
    saveLocal(LS_KEYS.ORDERS, pedidos);
    tr.children[3].textContent = pedido.estado;
    if(pedido.estado === 'ENTREGADO') btn.remove();
  });
}
