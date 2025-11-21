import { API_URL } from "../api/api.js";
import { mostrarHome } from "./home.js";

// Envuelve la construcción real de la pantalla de login
function construirLogin(main) {
  console.log('[PoliSong] construirLogin() inicio');
  document.body.classList.add('auth-screen');
  document.body.classList.remove('home-mode');
  const oldNav = document.getElementById('topNavbar');
  if (oldNav) oldNav.remove();
  main.innerHTML = `
    <section class="login-wrapper">
      <div class="login-card">
        <h1>Bienvenido</h1>
        <p class="subtitle">Ingresa para continuar</p>
        <form id="formLogin" autocomplete="off">
          <div class="field">
            <input type="email" id="correo" placeholder="Correo electrónico" required />
          </div>
          <div class="field">
            <input type="password" id="contrasena" placeholder="Contraseña" required minlength="4" />
          </div>
          <button type="submit" class="btn-primary">Iniciar Sesión</button>
        </form>
        <div id="errorBox" class="error-box" style="display:none;"></div>
        <div class="alt-link">¿Aún no tienes cuenta? <a id="goRegister" href="#">Crear cuenta</a></div>
        <div class="alt-link" style="margin-top:14px;">
          <button id="btnGuest" class="btn-outline" type="button">Entrar sin cuenta</button>
        </div>
      </div>
    </section>
  `;

  const form = document.getElementById("formLogin");
  const prefillEmail = localStorage.getItem('prefillEmail');
  const prefillPassword = localStorage.getItem('prefillPassword');
  if(prefillEmail){
    const correoEl = document.getElementById('correo');
    const passEl = document.getElementById('contrasena');
    if(correoEl) correoEl.value = prefillEmail;
    if(passEl && prefillPassword) passEl.value = prefillPassword;
    localStorage.removeItem('prefillEmail');
    localStorage.removeItem('prefillPassword');
  }
  const errorBox = document.getElementById("errorBox");
  const goRegister = document.getElementById("goRegister");
  const btnGuest = document.getElementById("btnGuest");
  btnGuest.addEventListener('click', () => {
    localStorage.setItem('userRole', 'USUARIO');
    localStorage.setItem('userName', 'Invitado');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('idUsuario');
    localStorage.removeItem('idProveedor');
    redirigirPorRol(main, 'USUARIO', 'Invitado');
  });

  goRegister.addEventListener("click", (e) => {
    e.preventDefault();
    import("./register.js").then(m => m.mostrarRegistro(main));
  });

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const correo = document.getElementById("correo").value.trim();
    const contrasena = document.getElementById("contrasena").value.trim();
    errorBox.style.display = "none";
    errorBox.textContent = "";
    try {
      const res = await fetch(`${API_URL}/usuarios/login?correo=${encodeURIComponent(correo)}&contrasena=${encodeURIComponent(contrasena)}`, { method: "POST" });
      let data = await res.json();
      if (res.ok && data.mensaje && data.mensaje.toLowerCase().includes("exitoso")) {
        localStorage.removeItem('idUsuario');
        localStorage.removeItem('idProveedor');
        localStorage.setItem("userRole", data.rol);
        localStorage.setItem("userName", data.nombre || correo);
        localStorage.setItem("userEmail", data.correo || correo);
        if(data.idUsuario){ localStorage.setItem('idUsuario', String(data.idUsuario)); }
        redirigirPorRol(main, data.rol, data.nombre || correo);
        return;
      }
      const resProv = await fetch(`${API_URL}/proveedores/login?correo=${encodeURIComponent(correo)}&contrasena=${encodeURIComponent(contrasena)}`, { method: "POST" });
      const dataProv = await resProv.json();
      if (resProv.ok && dataProv.mensaje && dataProv.mensaje.toLowerCase().includes("exitoso")) {
        localStorage.setItem("userRole", dataProv.rol);
        const nombreMostrar = dataProv.alias || correo;
        localStorage.setItem("userName", nombreMostrar);
        localStorage.setItem("userEmail", correo);
        if (dataProv.idProveedor) {
          localStorage.setItem('idProveedor', String(dataProv.idProveedor));
        }
        redirigirPorRol(main, dataProv.rol, nombreMostrar);
        return;
      }
      mostrarError(dataProv.mensaje || data.mensaje || "Credenciales inválidas.");
    } catch (err) {
      mostrarError("Error de conexión con el servidor.");
    }
  });

  function mostrarError(msg) {
    errorBox.textContent = msg;
    errorBox.style.display = "block";
  }
  console.log('[PoliSong] construirLogin() renderizado');
}

export function mostrarLogin(main){
  try {
    // Si el splash nunca se mostró en esta sesión, mostrarlo antes del login
    if(!localStorage.getItem('splashShown')){
      import('./splash.js').then(mod => {
        mod.mostrarSplash({
          onComplete: () => {
            // splash.js ya marca splashShown, pero aseguramos
            localStorage.setItem('splashShown','1');
            console.log('[PoliSong] mostrarLogin(): splash onComplete, construyendo login');
            construirLogin(main);
          }
        });
      }).catch(err => {
        console.error('Error cargando splash:', err);
        construirLogin(main);
      });
      return;
    }
    console.log('[PoliSong] mostrarLogin(): splash ya mostrado, construyendo login directo');
    construirLogin(main);
  } catch(e){
    console.error('Fallo inesperado en mostrarLogin:', e);
    // Fallback duro: render mínimo para no dejar pantalla en blanco
    if(main){
      main.innerHTML = '<div style="padding:40px;text-align:center;color:#fff;">Error inicializando login. Reintenta refrescar (Ctrl+Shift+R).</div>';
    }
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


