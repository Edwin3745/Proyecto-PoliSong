import { renderCompras } from './compras.js';

export function renderPerfilSection(nombre) {
  return `
    <div class="perfil-section">
      <div class="top-bar"><h2>Perfil</h2><p>Edita tu información</p></div>
      <div class="perfil-grid">
        <form id="perfilForm" class="perfil-card">
          <h3>Datos Básicos</h3>
          <label>Nombre<input type="text" id="pfNombre" value="${nombre || ''}"/></label>
          <label>Correo<input type="email" id="pfCorreo" placeholder="correo@ejemplo.com"/></label>
          <label>Contraseña<input type="password" id="pfPass" placeholder="Nueva contraseña"/></label>
          <label>Foto URL<input type="text" id="pfFoto" placeholder="https://..."/></label>
          <button type="submit" class="btn-primary">Guardar Cambios</button>
          <div id="pfMsg" class="muted" style="margin-top:8px;"></div>
        </form>
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
function perfilPage() {
  return `
    <h2> Mi Perfil</h2>
    <p>Bienvenido a tu cuenta.</p>
  `;
}
