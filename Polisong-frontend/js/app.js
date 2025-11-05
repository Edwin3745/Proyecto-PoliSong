import { mostrarLogin } from "./pages/login.js";

// Cargar el login al iniciar
document.addEventListener("DOMContentLoaded", () => {
  const main = document.getElementById("contenido");
  mostrarLogin(main);
});
