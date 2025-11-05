export function mostrarPlaylists(container) {
  container.innerHTML = `
    <div class="top-bar">
      <h2>🎧 Playlists</h2>
      <p>Your curated playlists</p>
    </div>

    <div class="cards-grid">
      <div class="card"><p>Chill Vibes</p><small>20 songs</small></div>
      <div class="card"><p>Workout Mix</p><small>15 songs</small></div>
      <div class="card"><p>Latin Beats</p><small>30 songs</small></div>
    </div>
  `;
}
