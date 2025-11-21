export function mostrarSplash({ onComplete }) {
  // Crear nodo splash
  const splash = document.createElement('div');
  splash.className = 'splash';
  splash.innerHTML = `
    <div class="splash-logo">POLISONG</div>
    <div class="splash-bar"><span id="splashProgress"></span></div>
    <div class="splash-sub">Cargando...</div>
  `;
  document.body.appendChild(splash);
  console.log('[PoliSong] mostrarSplash() montado');

  let progress = 0;
  const bar = splash.querySelector('#splashProgress');

  const timer = setInterval(() => {
    progress += Math.random() * 18; // avance variable
    if (progress > 100) progress = 100;
    bar.style.width = progress + '%';
    // console.log('[PoliSong] splash progreso:', progress.toFixed(1));
    if (progress >= 100) {
      clearInterval(timer);
      // Marcar que el splash ya se mostró en esta sesión
      localStorage.setItem('splashShown','1');
      setTimeout(() => {
        splash.classList.add('fade-out');
        setTimeout(() => {
          splash.remove();
          console.log('[PoliSong] Splash completado, removido');
          if (typeof onComplete === 'function') onComplete();
        }, 500);
      }, 400);
    }
  }, 250);

  // Fallback adicional: si en 8s no se completó, retirar splash y continuar
  setTimeout(() => {
    if(document.body.contains(splash)){
      console.warn('[PoliSong] Fallback splash: retirada forzada tras tiempo máximo.');
      try { splash.remove(); } catch(e){ /* noop */ }
      if (typeof onComplete === 'function') onComplete();
    }
  }, 8000);
  // Fallback rápido adicional por seguridad (2.5s) si nada avanzó
  setTimeout(() => {
    if(document.body.contains(splash) && progress < 5){
      console.warn('[PoliSong] Fallback rápido: progreso mínimo, forzando login');
      try { splash.remove(); } catch(e){ }
      localStorage.setItem('splashShown','1');
      if (typeof onComplete === 'function') onComplete();
    }
  }, 2500);
}
