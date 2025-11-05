export function mostrarArtists(container) {
  container.innerHTML = `
    <div class="top-bar">
      <h2> Artists</h2>
      <p>Meet the voices behind the music</p>
    </div>

    <div class="cards-grid">
      <div class="card"><p>Adele</p><small>Pop</small></div>
      <div class="card"><p>Bad Bunny</p><small>Reggaeton</small></div>
      <div class="card"><p>Queen</p><small>Rock</small></div>
    </div>
  `;
}
