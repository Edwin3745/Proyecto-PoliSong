const playlistsStore = [];

export function renderPlaylists(){
  return `
    <div class="playlists-section">
      <div class="top-bar"><h2>Playlists</h2><p>Gestiona tus playlists</p></div>
      <form id="plCrear" class="playlist-form">
        <input type="text" id="plNombre" placeholder="Nombre de la playlist" required />
        <button class="btn-primary" type="submit">Crear</button>
      </form>
      <div id="plListado" class="playlist-list">
        ${playlistsStore.length? playlistsStore.map(p => `<div class='playlist-card'>${p.nombre}</div>`).join('') : '<p class="muted">Sin playlists aún.</p>'}
      </div>
    </div>`;
}