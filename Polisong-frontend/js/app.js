import { mostrarLogin } from "./pages/login.js";
import { mostrarSplash } from "./pages/splash.js";

// Flujo inicial: splash -> login
document.addEventListener("DOMContentLoaded", () => {
  mostrarSplash({
    onComplete: () => {
      const main = document.getElementById("contenido");
      mostrarLogin(main);
    }
  });
});
