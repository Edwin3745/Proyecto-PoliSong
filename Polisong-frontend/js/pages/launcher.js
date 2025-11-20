export function mostrarLauncher(main, nombre, rol) {
  main.innerHTML = `
    <section class="launcher-wrapper">
      <div class="launcher-card">
        <div class="launcher-icon">
          <img src="assets/icono.png" alt="Abrir Dashboard" onerror="this.replaceWith(document.createElement('div'));" />
        </div>
        <h1>PoliSong</h1>
        <p class="subtitle">Hola ${nombre || ''} (${(rol||'').toUpperCase()})</p>
        <button id="btnAbrirDashboard" class="btn-primary">Entrar al Dashboard</button>
      </div>
    </section>
  `;
  const btn = document.getElementById('btnAbrirDashboard');
  const icon = main.querySelector('.launcher-icon');
  const abrir = () => {
    import('./home.js').then(m => m.mostrarHome(main, nombre || '')); 
  };
  btn.addEventListener('click', abrir);
  icon.addEventListener('click', abrir);
}
