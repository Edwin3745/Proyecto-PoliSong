import { API_URL } from "../api/api.js";
import { mostrarHome } from "./home.js";

export function mostrarRegistroProveedor(main) {
  document.body.classList.add('auth-screen');
  document.body.classList.remove('home-mode');
  main.innerHTML = `
    <section class="login-container">
      <div class="login-box">
        <h2>Registro Proveedor</h2>
        <form id="formRegistroProveedor" autocomplete="off">
          <input type="text" id="aliasContacto" placeholder="Alias contacto" required />
          <input type="email" id="correoPrincipal" placeholder="Correo" required />
          <input type="password" id="contrasena" placeholder="Contraseña" required minlength="6" />
          <input type="password" id="confirmacion" placeholder="Confirmar contraseña" required minlength="6" />
          <button type="submit">Registrar</button>
        </form>
        <p id="mensajeProveedor" class="mensaje"></p>
        <div class="alt-links">
          <button id="btnVolverLogin" type="button" class="secondary-btn">Volver al Login</button>
        </div>
      </div>
    </section>
  `;

  const form = document.getElementById("formRegistroProveedor");
  const mensaje = document.getElementById("mensajeProveedor");
  const btnVolver = document.getElementById("btnVolverLogin");

  btnVolver.addEventListener("click", () => {
    import("./login.js").then(m => m.mostrarLogin(main));
  });

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    mensaje.textContent = "Registrando proveedor...";
    mensaje.className = "mensaje";

    const aliasContacto = document.getElementById("aliasContacto").value.trim();
    const correoPrincipal = document.getElementById("correoPrincipal").value.trim();
    const contrasena = document.getElementById("contrasena").value.trim();
    const confirmacion = document.getElementById("confirmacion").value.trim();
    if (contrasena !== confirmacion) {
      mensaje.textContent = "Las contraseñas no coinciden.";
      mensaje.className = "mensaje error";
      return;
    }
    const payload = {
      nombreUsuario: null, // backend ignora para proveedor
      correoPrincipal,
      contrasena,
      rol: 'PROVEEDOR',
      aliasContacto
    };

    try {
      const res = await fetch(`${API_URL}/auth/registrar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.mensaje || "Error en registro");
      mensaje.textContent = data.mensaje;
      mensaje.className = "mensaje exito";
      localStorage.setItem("userRole", data.rol || 'PROVEEDOR');
      localStorage.setItem("userName", aliasContacto);
      setTimeout(() => {
        document.body.classList.remove('auth-screen');
        import('./provider.js').then(m => m.mostrarProviderDashboard(main, aliasContacto));
      }, 600);
    } catch (err) {
      mensaje.textContent = err.message;
      mensaje.className = "mensaje error";
    }
  });
}
