import { mostrarLogin } from "./pages/login.js";
import { mostrarSplash } from "./pages/splash.js";

// Flujo inicial: splash -> login (evitar doble splash si ya se marcó)
document.addEventListener("DOMContentLoaded", () => {
  const main = document.getElementById("contenido");
  // Instrumentación básica para depurar
  window.__polisongDebug = window.__polisongDebug || {};
  console.log('[PoliSong] DOMContentLoaded');
  if(!localStorage.getItem('splashShown')){
    mostrarSplash({
      onComplete: () => {
        console.log('[PoliSong] Splash completo, cargando login');
        mostrarLogin(main);
      }
    });
  } else {
    console.log('[PoliSong] Splash ya mostrado en sesión, directo al login');
    mostrarLogin(main);
  }

  // Watchdog: si a los 6s sigue el splash o no hay login, forzar
  setTimeout(() => {
    const splash = document.querySelector('.splash');
    const loginCard = main && main.querySelector('.login-card');
    if(splash || !loginCard){
      console.warn('[PoliSong] Watchdog activado. Forzando carga de login y retiro de splash.');
      if(splash) splash.remove();
      if(main && !loginCard){
        import('./pages/login.js').then(m => m.mostrarLogin(main)).catch(err => {
          console.error('[PoliSong] Error al forzar login:', err);
          main.innerHTML = '<div style="padding:40px;text-align:center;color:#fff;">Error cargando login (watchdog). Refresca (Ctrl+Shift+R).</div>';
        });
      }
    }
  }, 6000);
  // Fallback extra aún más temprano (3.5s) si no hay login
  setTimeout(() => {
    const loginCard = main && main.querySelector('.login-card');
    if(!loginCard){
      console.warn('[PoliSong] Fallback 3.5s: login ausente, forzando.');
      import('./pages/login.js').then(m => m.mostrarLogin(main)).catch(err => console.error('[PoliSong] Error fallback 3.5s:', err));
    }
  }, 3500);

  // Captura global de errores para diagnóstico rápido
  window.addEventListener('error', (ev) => {
    console.error('[PoliSong][GlobalError]', ev.message, ev.error);
  });
});
