import { API_URL } from '../api/api.js';

// Versión simplificada del panel admin (revertida)
export function mostrarAdminDashboard(container, nombreAdmin){
  container.innerHTML = `
    <div style="margin-bottom:22px;">
      <h2 style="margin:0 0 6px; font-size:24px;">Panel Admin (Simple)</h2>
      <p style="margin:0; font-size:13px; color:#94a3b8;">Bienvenido ${nombreAdmin||'Administrador'} - vista básica.</p>
    </div>
    <div id="adminUsuarios"><h3>Usuarios</h3><div>Cargando...</div></div>
    <div id="adminProveedores" style="margin-top:30px;"><h3>Proveedores</h3><div>Cargando...</div></div>
    <div id="adminProductos" style="margin-top:30px;"><h3>Productos</h3><div>Cargando...</div></div>
    <div style="margin-top:30px; text-align:right;"><button id="btnAdminLogout" class="btn-outline">Cerrar sesión</button></div>
  `;
  cargarUsuarios(container.querySelector('#adminUsuarios div'));
  cargarProveedores(container.querySelector('#adminProveedores div'));
  cargarProductos(container.querySelector('#adminProductos div'));
  const btnLogout = container.querySelector('#btnAdminLogout');
  if(btnLogout){ btnLogout.addEventListener('click',()=>{ localStorage.clear(); import('./login.js').then(m=> m.mostrarLogin(document.getElementById('contenido')||document.querySelector('main'))); }); }
}

async function cargarUsuarios(target){
  try {
    const res = await fetch(`${API_URL}/usuarios`);
    const data = res.ok? await res.json():[];
    const usuarios = data.filter(u => (u.rol||'').toUpperCase()==='USUARIO');
    if(!usuarios.length){ target.innerHTML = '<div style="font-size:12px; color:#94a3b8;">No hay usuarios.</div>'; return; }
    target.innerHTML = `<table class="data-table"><thead><tr><th>Nombre</th><th>Correo</th><th>Rol</th></tr></thead><tbody>${usuarios.map(u=>`<tr><td>${esc(u.nombreUsuario)}</td><td>${esc(u.correoPrincipal)}</td><td>${u.rol||'USUARIO'}</td></tr>`).join('')}</tbody></table>`;
  } catch(e){ target.textContent='Error usuarios'; }
}
async function cargarProveedores(target){
  try {
    const res = await fetch(`${API_URL}/proveedores`);
    const proveedores = res.ok? await res.json():[];
    if(!proveedores.length){ target.innerHTML = '<div style="font-size:12px; color:#94a3b8;">No hay proveedores.</div>'; return; }
    target.innerHTML = `<table class="data-table"><thead><tr><th>Alias</th><th>Correo</th><th>ID</th></tr></thead><tbody>${proveedores.map(p=>`<tr><td>${esc(p.aliasContacto)}</td><td>${esc(p.correo)}</td><td>${p.idProveedor}</td></tr>`).join('')}</tbody></table>`;
  } catch(e){ target.textContent='Error proveedores'; }
}
async function cargarProductos(target){
  try {
    const [cRes, vRes] = await Promise.all([
      fetch(`${API_URL}/canciones`),
      fetch(`${API_URL}/vinilos`)
    ]);
    const canciones = cRes.ok? await cRes.json():[];
    const vinilos = vRes.ok? await vRes.json():[];
    const productos = [
      ...canciones.map(c=>({ titulo:c.nombre, tipo:'Canción', precio:c.precio||0 })),
      ...vinilos.map(v=>({ titulo:v.nombre, tipo:'Vinilo', precio:v.precio||0 }))
    ];
    if(!productos.length){ target.innerHTML='<div style="font-size:12px; color:#94a3b8;">No hay productos.</div>'; return; }
    target.innerHTML = `<table class="data-table"><thead><tr><th>Título</th><th>Tipo</th><th>Precio</th></tr></thead><tbody>${productos.map(p=>`<tr><td>${esc(p.titulo)}</td><td>${p.tipo}</td><td>${p.precio ? '$'+p.precio : 'Gratis'}</td></tr>`).join('')}</tbody></table>`;
  } catch(e){ target.textContent='Error productos'; }
}

function esc(str){ return (str||'').replace(/[<>&]/g, c=>({'<':'&lt;','>':'&gt;','&':'&amp;'}[c])); }

export const mostrarAdminPanel = mostrarAdminDashboard;