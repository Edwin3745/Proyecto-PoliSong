import { API_URL } from "../api/api.js";
import { mostrarHome } from "./home.js";

export function mostrarRegistro(main) {
  document.body.classList.add('auth-screen');
  document.body.classList.remove('home-mode');
  main.innerHTML = `
    <section class="register-wrapper">
      <div class="register-card">
        <h1>Crear Cuenta</h1>
        <p class="subtitle">Completa los datos para registrarte</p>
        <form id="formRegistro" autocomplete="off">
          <div class="grid">
            <div class="field full">
              <input type="text" id="nombreUsuario" placeholder="Nombre completo" required />
            </div>
            <div class="field full">
              <input type="email" id="correoPrincipal" placeholder="Correo electrónico" required />
            </div>
            <div class="field">
              <input type="password" id="contrasena" placeholder="Contraseña" required minlength="6" />
            </div>
            <div class="field">
              <input type="password" id="confirmacion" placeholder="Confirmar contraseña" required minlength="6" />
            </div>
            <div class="field full">
              <select id="rol" required>
                <option value="">Selecciona un rol...</option>
                <option value="USUARIO">Usuario</option>
                <option value="PROVEEDOR">Proveedor</option>
                <option value="ADMIN">Administrador</option>
              </select>
            </div>
            <div class="field proveedor extra oculto">
              <input type="text" id="aliasContacto" placeholder="Alias de contacto (Proveedor)" />
            </div>
            <div class="field admin extra oculto">
              <input type="text" id="areaResponsable" placeholder="Área responsable (Admin)" />
            </div>
          </div>
          <button type="submit" class="btn-primary">Registrar</button>
        </form>
        <div id="errorRegistro" class="error-box" style="display:none;"></div>
        <div class="alt-link">¿Ya tienes cuenta? <a id="goLogin" href="#">Iniciar sesión</a></div>
      </div>
    </section>
  `;

  const rolSelect = document.getElementById('rol');
  const proveedorExtras = [...document.querySelectorAll('.proveedor.extra')];
  const adminExtras = [...document.querySelectorAll('.admin.extra')];
  const form = document.getElementById('formRegistro');
  const errorBox = document.getElementById('errorRegistro');
  const goLogin = document.getElementById('goLogin');

  goLogin.addEventListener('click', (e) => {
    e.preventDefault();
    import('./login.js').then(m => m.mostrarLogin(main));
  });

  rolSelect.addEventListener('change', () => {
    const rol = rolSelect.value;
    proveedorExtras.forEach(el => el.classList.toggle('oculto', rol !== 'PROVEEDOR'));
    adminExtras.forEach(el => el.classList.toggle('oculto', rol !== 'ADMIN'));
  });

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    errorBox.style.display = 'none';
    errorBox.textContent = '';
    const nombreUsuario = document.getElementById('nombreUsuario').value.trim();
    const correoPrincipal = document.getElementById('correoPrincipal').value.trim();
    const contrasena = document.getElementById('contrasena').value;
    const confirmacion = document.getElementById('confirmacion').value;
    const rol = rolSelect.value;
    const aliasContacto = document.getElementById('aliasContacto').value.trim();
    const areaResponsable = document.getElementById('areaResponsable').value.trim();

    if (contrasena !== confirmacion) {
      return mostrarError('Las contraseñas no coinciden.');
    }
    if (!rol) {
      return mostrarError('Debes seleccionar un rol.');
    }

    const payload = {
      nombreUsuario,
      correoPrincipal,
      contrasena,
      rol,
      aliasContacto: rol === 'PROVEEDOR' ? aliasContacto : null,
      areaResponsable: rol === 'ADMIN' ? areaResponsable : null
    };

    try {
      const res = await fetch(`${API_URL}/auth/registrar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      const data = await res.json();
      if (res.ok) {
        localStorage.setItem('userRole', data.rol);
        const nombreParaUI = data.rol === 'PROVEEDOR' ? aliasContacto || data.correo : nombreUsuario;
        localStorage.setItem('userName', nombreParaUI);
        redirigirPorRol(main, data.rol, nombreParaUI);
      } else {
        mostrarError(data.mensaje || 'Error al registrar.');
      }
    } catch (err) {
      mostrarError('Error de conexión con el servidor.');
    }
  });

  function mostrarError(msg) {
    errorBox.textContent = msg;
    errorBox.style.display = 'block';
  }
}

function redirigirPorRol(main, rol, nombre) {
  document.body.classList.remove('auth-screen');
  switch ((rol || '').toUpperCase()) {
    case 'PROVEEDOR':
      import('./provider.js').then(m => m.mostrarProviderDashboard(main, nombre));
      break;
    case 'ADMIN':
      import('./admin.js').then(m => m.mostrarAdminDashboard(main, nombre));
      break;
    default:
      mostrarHome(main, nombre);
      break;
  }
}
