import { API_URL } from "../api/api.js";
import { mostrarHome } from "./home.js";

export function mostrarLogin(main) {
  document.body.classList.add('auth-screen');
  document.body.classList.remove('home-mode');
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
  const errorBox = document.getElementById("errorBox");
  const goRegister = document.getElementById("goRegister");
  const btnGuest = document.getElementById("btnGuest");
  btnGuest.addEventListener('click', () => {
    // Acceso invitado: rol USUARIO con nombre 'Invitado'
    localStorage.setItem('userRole', 'USUARIO');
    localStorage.setItem('userName', 'Invitado');
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
        localStorage.setItem("userRole", data.rol);
        localStorage.setItem("userName", data.nombre || correo);
        redirigirPorRol(main, data.rol, data.nombre || correo);
        return;
      }
      // Intento proveedor si falla usuario
      const resProv = await fetch(`${API_URL}/proveedores/login?correo=${encodeURIComponent(correo)}&contrasena=${encodeURIComponent(contrasena)}`, { method: "POST" });
      const dataProv = await resProv.json();
      if (resProv.ok && dataProv.mensaje && dataProv.mensaje.toLowerCase().includes("exitoso")) {
        localStorage.setItem("userRole", dataProv.rol);
        const nombreMostrar = dataProv.alias || correo;
        localStorage.setItem("userName", nombreMostrar);
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


