import { get } from "../api/api.js";

export async function mostrarSongs(container) {
  container.innerHTML = `
    <div class="top-bar">
      <h2>🎵 Songs</h2>
      <p>Loading songs...</p>
    </div>
  `;

  try {
    // Petición real al backend
    const canciones = await get("/vinilos");

    if (canciones.length === 0) {
      container.innerHTML = `
        <div class="top-bar">
          <h2> Songs</h2>
          <p>No songs found in the database.</p>
        </div>
      `;
      return;
    }

    //  Render dinámico
    container.innerHTML = `
      <div class="top-bar">
        <h2>Songs</h2>
        <p>Available songs from the database</p>
      </div>

      <div class="cards-grid">
        ${canciones
          .map(
            (song) => `
          <div class="card">
            <img src="${song.imagen || 'assets/logo.png'}" alt="${song.titulo}">
            <p>${song.titulo}</p>
            <small>${song.artista || 'Unknown Artist'}</small>
          </div>`
          )
          .join("")}
      </div>
    `;
  } catch (error) {
    console.error("Error al conectar con el backend:", error);
    container.innerHTML = `
      <div class="top-bar">
        <h2>🎵 Songs</h2>
        <p style="color:#ef4444;"> Error connecting to backend</p>
      </div>
    `;
  }
}
