import { API_URL } from "../api/api.js";
import { mostrarHome } from "./home.js";

export function mostrarLogin(main) {
  main.innerHTML = `
    <section class="login-container">
      <div class="login-box">
        <h2>Iniciar Sesión</h2>
        <form id="formLogin">
          <input type="email" id="correo" placeholder="Correo electrónico" required />
          <input type="password" id="contrasena" placeholder="Contraseña" required />
          <button type="submit">Entrar</button>
        </form>
        <p id="mensajeLogin" class="mensaje"></p>
      </div>
    </section>
  `;

  const form = document.getElementById("formLogin");
  const mensaje = document.getElementById("mensajeLogin");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const correo = document.getElementById("correo").value.trim();
    const contrasena = document.getElementById("contrasena").value.trim();

    mensaje.textContent = "Verificando...";
    mensaje.className = "mensaje";

    try {
      const res = await fetch(
        `${API_URL}/usuarios/login?correo=${correo}&contrasena=${contrasena}`,
        { method: "POST" }
      );

      const data = await res.json();

      if (res.ok && data.mensaje.includes("exitoso")) {
        mensaje.textContent = "Inicio de sesión exitoso.";
        mensaje.className = "mensaje exito";

        setTimeout(() => {
          mostrarHome(main, data.nombre);
        }, 1000);
      } else {
        mensaje.textContent = data.mensaje;
        mensaje.className = "mensaje error";
      }
    } catch (err) {
      mensaje.textContent = " Error de conexión con el servidor.";
      mensaje.className = "mensaje error";
    }
  });
}


