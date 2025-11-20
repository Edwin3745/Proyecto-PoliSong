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

  let progress = 0;
  const bar = splash.querySelector('#splashProgress');

  const timer = setInterval(() => {
    progress += Math.random() * 18; // avance variable
    if (progress > 100) progress = 100;
    bar.style.width = progress + '%';
    if (progress >= 100) {
      clearInterval(timer);
      setTimeout(() => {
        splash.classList.add('fade-out');
        setTimeout(() => {
          splash.remove();
          if (typeof onComplete === 'function') onComplete();
        }, 500);
      }, 400);
    }
  }, 250);
}
