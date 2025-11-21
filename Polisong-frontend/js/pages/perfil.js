import { renderCompras } from './compras.js';
import { API_URL } from '../api/api.js';

export function renderPerfilSection(nombre) {
  const role = (localStorage.getItem('userRole') || '').toUpperCase();
  const correoActual = localStorage.getItem('userEmail') || '';
  const idUsuario = localStorage.getItem('idUsuario');
  // Invitado solo si es rol USUARIO, sin correo y sin idUsuario persistido
  const invitado = role === 'USUARIO' && !correoActual && !idUsuario;
  return `
    <div class="perfil-section">
      <div class="top-bar"><h2>Perfil</h2><p>${invitado ? 'Crea una cuenta para guardar tus datos' : 'Edita tu información'}</p></div>
      <div class="perfil-grid">
        ${invitado ? renderInvitadoCTA() : renderFormularioPerfil(nombre, correoActual)}
        <div class="perfil-card compras-mini">
          <h3>Historial Compras (Resumen)</h3>
          <ul class="mini-list" id="pfCompras">
            <li>Sin datos (placeholder)</li>
          </ul>
          <button class="btn-outline" data-section="compras">Ver completo</button>
        </div>
      </div>
    </div>`;
}// perfil.js

function renderInvitadoCTA(){
  return `
    <div class="perfil-card invitado-box" id="invitadoBox">
      <h3>Usuario Invitado</h3>
      <p>No has creado una cuenta permanente. Para editar correo y contraseña, primero crea tu cuenta.</p>
      <button id="btnCrearCuenta" class="btn-primary">CREAR CUENTA</button>
    </div>
  `;
}

function renderFormularioPerfil(nombre, correo){
  return `
    <form id="perfilForm" class="perfil-card">
      <h3>Datos de Perfil</h3>
      <label>Nombre
        <input type="text" id="pfNombre" value="${nombre || ''}" required />
      </label>
      <label>Correo
        <input type="email" id="pfCorreo" value="${correo}" placeholder="correo@ejemplo.com" required />
      </label>
      <fieldset class="pass-group">
        <legend>Cambiar Contraseña</legend>
        <label>Contraseña Actual
          <input type="password" id="pfPassActual" autocomplete="current-password" placeholder="••••" />
        </label>
        <label>Nueva Contraseña
          <input type="password" id="pfPassNueva" autocomplete="new-password" placeholder="Nueva contraseña" />
        </label>
        <label>Confirmar Nueva
          <input type="password" id="pfPassConfirm" autocomplete="new-password" placeholder="Repite nueva contraseña" />
        </label>
      </fieldset>
      <button type="submit" class="btn-primary">Guardar Cambios</button>
      <div id="pfMsg" class="muted" style="margin-top:8px;"></div>
    </form>
  `;
}
function perfilPage() {
  return `
    <h2> Mi Perfil</h2>
    <p>Bienvenido a tu cuenta.</p>
  `;
}

// Carga de contenido publicado (global, no filtrado por usuario comprador)
export async function initPerfilEdicion(scope){
  const invitado = (localStorage.getItem('userRole')||'').toUpperCase() === 'USUARIO'
    && !localStorage.getItem('userEmail')
    && !localStorage.getItem('idUsuario');
  if(invitado){
    const btn = scope.querySelector('#btnCrearCuenta');
    if(btn){
      btn.addEventListener('click', () => {
        import('./register.js').then(m => {
          const main = document.getElementById('contenido')||document.querySelector('main');
          m.mostrarRegistro(main);
        });
      });
    }
    return;
  }
  const form = scope.querySelector('#perfilForm');
  if(!form) return;
  form.addEventListener('submit', async e => {
    e.preventDefault();
    const msg = form.querySelector('#pfMsg');
    msg.textContent='Guardando...';
    const nombre = form.querySelector('#pfNombre').value.trim();
    const correo = form.querySelector('#pfCorreo').value.trim();
    const passActual = form.querySelector('#pfPassActual').value;
    const passNueva = form.querySelector('#pfPassNueva').value;
    const passConfirm = form.querySelector('#pfPassConfirm').value;
    if(passNueva || passConfirm){
      if(passNueva.length < 4){ msg.textContent='La nueva contraseña debe tener al menos 4 caracteres.'; return; }
      if(passNueva !== passConfirm){ msg.textContent='La confirmación no coincide.'; return; }
      if(!passActual){ msg.textContent='Debes ingresar la contraseña actual.'; return; }
    }
    try {
      // Obtener ID usuario (si se guarda en localStorage; si no, intentar buscar por correo)
      let idUsuario = localStorage.getItem('idUsuario');
      if(!idUsuario && correo){
        const resLookup = await fetch(`${API_URL}/usuarios/email?correo=${encodeURIComponent(correo)}`);
        if(resLookup.ok){ const data = await resLookup.json(); idUsuario = data.idUsuario; localStorage.setItem('idUsuario', idUsuario); }
      }
      if(idUsuario){
        // Actualizar nombre/correo
        await fetch(`${API_URL}/usuarios/${idUsuario}/editarPerfil`, { method:'PUT', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ nombreUsuario: nombre, correoPrincipal: correo }) });
        localStorage.setItem('userName', nombre);
        localStorage.setItem('userEmail', correo);
        // Cambiar contraseña si procede
        if(passNueva){
          const respPass = await fetch(`${API_URL}/usuarios/${idUsuario}/cambiarContrasena?contrasenaActual=${encodeURIComponent(passActual)}&nuevaContrasena=${encodeURIComponent(passNueva)}`, { method:'PUT' });
          if(!respPass.ok){ msg.textContent='Error cambiando contraseña.'; return; }
        }
        msg.textContent='Tus datos han sido actualizados correctamente.';
        form.querySelector('#pfPassActual').value='';
        form.querySelector('#pfPassNueva').value='';
        form.querySelector('#pfPassConfirm').value='';
      } else {
        msg.textContent='No se pudo determinar el usuario para actualizar.';
      }
    } catch(err){
      msg.textContent='Error actualizando perfil.';
    }
  });
}

// Compatibilidad: función antigua ya no muestra publicaciones (el requerimiento las eliminó).
export function cargarPublicadosPerfil(){ /* Intencionalmente vacío */ }
