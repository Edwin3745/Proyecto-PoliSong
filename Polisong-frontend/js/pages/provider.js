
const LS_KEYS = {
  ARTIST_PROFILE: 'providerArtistProfile',
  PRODUCTS: 'providerProducts',
  RATINGS: 'providerRatings',
  ORDERS: 'providerOrders'
};

function loadLocal(key, fallback) {
  try { return JSON.parse(localStorage.getItem(key)) || fallback; } catch { return fallback; }
}
function saveLocal(key, value) { localStorage.setItem(key, JSON.stringify(value)); }

export function mostrarProviderDashboard(container) {
  const profile = loadLocal(LS_KEYS.ARTIST_PROFILE, {
    nombreArtistico: 'Sin nombre',
    bio: 'Agrega una breve biografía o descripción artística.',
    foto: '',
    enlaces: ['https://tu-sitio-web.com'],
    metricas: { reproducciones: 0, productos: 0, valoracionPromedio: 0 }
  });
  const productos = loadLocal(LS_KEYS.PRODUCTS, []);
  const valoraciones = loadLocal(LS_KEYS.RATINGS, []);
  const pedidos = loadLocal(LS_KEYS.ORDERS, []);

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
      <button class="tab-btn" data-tab="publicar">Publicar Producto</button>
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
          content.innerHTML = renderInventario(productos);
          attachInventarioListeners(content, productos, profile);
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
        <div class="fg">
          <label>Foto / portada (URL)</label>
          <input type="text" name="foto" value="${profile.foto}">
        </div>
        <div class="fg full">
          <label>Enlaces externos (uno por línea)</label>
          <textarea name="enlaces" rows="3">${profile.enlaces.join('\n')}</textarea>
        </div>
        <button class="btn btn-primary" type="submit">Guardar Perfil</button>
      </form>
      <div class="metrics-grid">
        <div class="metric-box"><span class="m-label">Reproducciones</span><strong>${profile.metricas.reproducciones}</strong></div>
        <div class="metric-box"><span class="m-label">Productos</span><strong>${profile.metricas.productos}</strong></div>
        <div class="metric-box"><span class="m-label">Valoración</span><strong>${profile.metricas.valoracionPromedio}</strong></div>
      </div>
      <div class="artist-preview">
        <h4>Vista Previa</h4>
        <div class="artist-card">
          ${profile.foto ? `<img src="${profile.foto}" alt="foto"/>` : '<div class="placeholder-img">Sin imagen</div>'}
          <div>
            <h5>${profile.nombreArtistico}</h5>
            <p class="bio-text">${profile.bio}</p>
            <ul class="artist-links">
              ${profile.enlaces.map(e => `<li><a href="${e}" target="_blank">${e}</a></li>`).join('')}
            </ul>
          </div>
        </div>
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
    profile.foto = fd.get('foto');
    profile.enlaces = fd.get('enlaces').split('\n').map(s => s.trim()).filter(Boolean);
    saveLocal(LS_KEYS.ARTIST_PROFILE, profile);
    form.querySelector('button').textContent = 'Guardado';
    setTimeout(()=> form.querySelector('button').textContent = 'Guardar Perfil',1400);
    // Re-render preview
    const preview = scope.querySelector('.artist-preview');
    if(preview){
      preview.innerHTML = renderPerfilArtista(profile, loadLocal(LS_KEYS.PRODUCTS, []), loadLocal(LS_KEYS.RATINGS, [])).match(/<div class="artist-preview">[\s\S]*?<\/div>/)[0];
    }
  });
}

/* =============================
   PUBLICAR PRODUCTO
============================= */
function renderPublicarProducto() {
  return `
    <div class="panel-section">
      <h3>Publicar Producto</h3>
      <form id="publicarForm" class="form-grid">
        <div class="fg">
          <label>Tipo</label>
          <select name="tipo">
            <option value="GRATIS">Canción Gratis</option>
            <option value="PAGO">Canción de Pago</option>
            <option value="VINILO">Vinilo</option>
          </select>
        </div>
        <div class="fg">
          <label>Título</label>
          <input type="text" name="titulo" required>
        </div>
        <div class="fg full">
          <label>Descripción</label>
          <textarea name="descripcion" rows="3"></textarea>
        </div>
        <div class="fg">
          <label>Precio (solo pago/vinilo)</label>
          <input type="number" step="0.01" name="precio">
        </div>
        <div class="fg">
          <label>Stock (solo vinilo)</label>
          <input type="number" name="stock">
        </div>
        <div class="fg full">
          <label>Archivo (audio / imagen) - MOCK</label>
          <input type="file" name="archivo" accept="audio/*,image/*">
        </div>
        <div class="fg full">
          <label>Vista previa</label>
          <div id="previewBox" class="preview-box">Completa el formulario para ver la vista previa.</div>
        </div>
        <div class="fg full actions-row">
          <button type="submit" class="btn btn-primary">Publicar</button>
          <button type="button" id="btnBorrador" class="btn btn-outline">Guardar Borrador</button>
        </div>
      </form>
    </div>
  `;
}
function attachPublicarListeners(scope, productos, profile) {
  const form = scope.querySelector('#publicarForm');
  const preview = scope.querySelector('#previewBox');
  if(!form) return;
  form.addEventListener('input', () => {
    const fd = new FormData(form);
    const tipo = fd.get('tipo');
    const titulo = fd.get('titulo') || '(Sin título)';
    const descripcion = fd.get('descripcion') || '';
    const precio = fd.get('precio');
    const stock = fd.get('stock');
    preview.innerHTML = `
      <div class="product-card-preview">
        <strong>${titulo}</strong><br>
        <small>Tipo: ${tipo}</small><br>
        ${descripcion ? `<p>${descripcion}</p>` : ''}
        ${(tipo === 'PAGO' || tipo === 'VINILO') && precio ? `<div>Precio: $${precio}</div>` : ''}
        ${tipo === 'VINILO' && stock ? `<div>Stock: ${stock}</div>` : ''}
      </div>
    `;
  });
  form.addEventListener('submit', e => {
    e.preventDefault();
    const fd = new FormData(form);
    const nuevo = {
      id: Date.now(),
      tipo: fd.get('tipo'),
      titulo: fd.get('titulo'),
      descripcion: fd.get('descripcion'),
      precio: fd.get('precio'),
      stock: fd.get('stock'),
      estado: 'ACTIVO',
      metricas: { descargas:0, ventas:0, reproducciones:0, likes:0 }
    };
    productos.push(nuevo);
    saveLocal(LS_KEYS.PRODUCTS, productos);
    // Actualizar métricas perfil
    const prof = loadLocal(LS_KEYS.ARTIST_PROFILE, profile);
    prof.metricas.productos = productos.length;
    saveLocal(LS_KEYS.ARTIST_PROFILE, prof);
    form.reset();
    preview.textContent = 'Producto publicado.';
  });
  scope.querySelector('#btnBorrador')?.addEventListener('click', ()=> {
    preview.textContent = 'Borrador guardado (local, sin backend).';
  });
}

/* =============================
   INVENTARIO
============================= */
function renderInventario(productos) {
  if(!productos.length) {
    return `<div class="panel-section"><h3>Inventario</h3><p class="muted">Aún no hay productos publicados.</p></div>`;
  }
  return `
    <div class="panel-section">
      <h3>Inventario</h3>
      <table class="inventario-table">
        <thead><tr><th>Título</th><th>Tipo</th><th>Estado</th><th>Metricas</th><th>Acciones</th></tr></thead>
        <tbody>
          ${productos.map(p => `
            <tr data-id="${p.id}">
              <td>${p.titulo}</td>
              <td>${p.tipo}</td>
              <td><span class="estado ${p.estado === 'ACTIVO' ? 'on' : 'off'}">${p.estado}</span></td>
              <td class="mini-metrics">
                D:${p.metricas.descargas} | V:${p.metricas.ventas} | R:${p.metricas.reproducciones} | L:${p.metricas.likes}
              </td>
              <td>
                <button class="btn btn-outline btn-sm" data-act="toggle">${p.estado === 'ACTIVO' ? 'Desactivar' : 'Activar'}</button>
                <button class="btn btn-primary btn-sm" data-act="edit">Editar</button>
              </td>
            </tr>
          `).join('')}
        </tbody>
      </table>
      <div id="editPanel" class="edit-panel hidden"></div>
    </div>
  `;
}
function attachInventarioListeners(scope, productos, profile) {
  const tbody = scope.querySelector('tbody');
  if(!tbody) return;
  tbody.addEventListener('click', e => {
    const btn = e.target.closest('button[data-act]');
    if(!btn) return;
    const tr = btn.closest('tr');
    const id = Number(tr.dataset.id);
    const producto = productos.find(p => p.id === id);
    if(!producto) return;
    if(btn.dataset.act === 'toggle') {
      producto.estado = producto.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
      saveLocal(LS_KEYS.PRODUCTS, productos);
      tr.querySelector('.estado').textContent = producto.estado;
      tr.querySelector('.estado').className = `estado ${producto.estado === 'ACTIVO' ? 'on' : 'off'}`;
      btn.textContent = producto.estado === 'ACTIVO' ? 'Desactivar' : 'Activar';
    }
    if(btn.dataset.act === 'edit') {
      const panel = scope.querySelector('#editPanel');
      panel.classList.remove('hidden');
      panel.innerHTML = `
        <h4>Editar Producto</h4>
        <form id="editForm">
          <label>Título<input name="titulo" value="${producto.titulo}"></label>
          <label>Descripción<textarea name="descripcion" rows="3">${producto.descripcion}</textarea></label>
          ${(producto.tipo === 'PAGO' || producto.tipo === 'VINILO') ? `<label>Precio<input name="precio" value="${producto.precio || ''}"></label>` : ''}
          ${producto.tipo === 'VINILO' ? `<label>Stock<input name="stock" value="${producto.stock || ''}"></label>` : ''}
          <div class="edit-actions">
            <button class="btn btn-primary" type="submit">Guardar Cambios</button>
            <button class="btn btn-outline" type="button" id="btnCerrarEdit">Cerrar</button>
          </div>
        </form>
      `;
      panel.querySelector('#btnCerrarEdit').addEventListener('click', ()=> panel.classList.add('hidden'));
      panel.querySelector('#editForm').addEventListener('submit', ev => {
        ev.preventDefault();
        const fd = new FormData(ev.target);
        producto.titulo = fd.get('titulo');
        producto.descripcion = fd.get('descripcion');
        if(producto.tipo === 'PAGO' || producto.tipo === 'VINILO') producto.precio = fd.get('precio');
        if(producto.tipo === 'VINILO') producto.stock = fd.get('stock');
        saveLocal(LS_KEYS.PRODUCTS, productos);
        panel.classList.add('hidden');
        // Re-render inventario
        const newHTML = renderInventario(productos);
        scope.innerHTML = newHTML;
        attachInventarioListeners(scope, productos, profile);
      });
    }
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
