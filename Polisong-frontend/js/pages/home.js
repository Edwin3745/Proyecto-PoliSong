export function mostrarHome(main, nombreUsuario) {
  main.innerHTML = `
    <div class="home-wrapper">
      <aside class="sidebar">
        <h1>PoliSong</h1>
        <nav>
          <button class="menu-btn active" data-section="home"> Home</button>
          <button class="menu-btn" data-section="songs"> Songs</button>
          <button class="menu-btn" data-section="playlists"> Playlists</button>
          <button class="menu-btn" data-section="albums"> Albums</button>
          <button class="menu-btn" data-section="artists"> Artists</button>
          <button class="menu-btn" id="btnSalir"> Logout</button>
        </nav>
      </aside>

      <section class="main-content" id="mainContent">
        ${getHomeSection(nombreUsuario)}
      </section>
    </div>
  `;

  const content = document.getElementById("mainContent");
  const buttons = main.querySelectorAll(".menu-btn");

  // Evento de clic en cada botón del menú
  buttons.forEach((btn) => {
    btn.addEventListener("click", () => {
      buttons.forEach((b) => b.classList.remove("active"));
      btn.classList.add("active");

      const section = btn.dataset.section;
      content.classList.add("fade-out");

      setTimeout(() => {
        switch (section) {
          case "home":
            content.innerHTML = getHomeSection(nombreUsuario);
            break;
          case "songs":
            content.innerHTML = getSongsSection();
            break;
          case "playlists":
            content.innerHTML = getPlaylistsSection();
            break;
          case "albums":
            content.innerHTML = getAlbumsSection();
            break;
          case "artists":
            content.innerHTML = getArtistsSection();
            break;
          default:
            break;
        }
        content.classList.remove("fade-out");
        content.classList.add("fade-in");
      }, 200);
    });
  });

  //  Botón de Logout
  const btnSalir = document.getElementById("btnSalir");
  btnSalir.addEventListener("click", () => {
    import("./login.js").then((m) => m.mostrarLogin(main));
  });
}

/* ==========================
   SECCIONES DEL DASHBOARD
========================== */

function getHomeSection(nombreUsuario) {
  return `
    <div class="top-bar">
      <h2>Welcome, ${nombreUsuario} </h2>
      <p>Enjoy your music journey </p>
    </div>

    <div>
      <h3 class="section-title">Quick Picks</h3>
      <div class="cards-grid">
        ${[
          { title: "The Sound of Silence", artist: "Disturbed", img: "https://i.scdn.co/image/ab67616d0000b27309f3dc51a2c146e69ef2e4f4" },
          { title: "Someone Like You", artist: "Adele", img: "https://i.scdn.co/image/ab67616d0000b273e43ebffde6900c8b7b1f8e4d" },
          { title: "Save Your Tears", artist: "The Weeknd", img: "https://i.scdn.co/image/ab67616d0000b273299f6e5a6f4a7c54582e0d2d" },
          { title: "Hotel California", artist: "Eagles", img: "https://i.scdn.co/image/ab67616d0000b273d3d27a15b6e1ed59d3e5fdee" },
        ]
          .map(
            (song) => `
          <div class="card">
            <img src="${song.img}" alt="${song.title}">
            <p>${song.title}</p>
            <small>${song.artist}</small>
          </div>`
          )
          .join("")}
      </div>
    </div>
  `;
}

function getSongsSection() {
  return `
    <div class="top-bar">
      <h2> Songs</h2>
      <p>Browse all available songs in PoliSong</p>
    </div>
    <div class="cards-grid">
      <div class="card"><p>Believer</p><small>Imagine Dragons</small></div>
      <div class="card"><p>Perfect</p><small>Ed Sheeran</small></div>
      <div class="card"><p>Viva La Vida</p><small>Coldplay</small></div>
    </div>
  `;
}

function getPlaylistsSection() {
  return `
    <div class="top-bar">
      <h2> Playlists</h2>
      <p>Your favorite mixes and collections</p>
    </div>
    <div class="cards-grid">
      <div class="card"><p>Chill Vibes</p><small>20 songs</small></div>
      <div class="card"><p>Workout Hits</p><small>15 songs</small></div>
      <div class="card"><p>Latin Party</p><small>25 songs</small></div>
    </div>
  `;
}

function getAlbumsSection() {
  return `
    <div class="top-bar">
      <h2> Albums</h2>
      <p>Explore the latest albums from top artists</p>
    </div>
    <div class="cards-grid">
      <div class="card"><p>Thriller</p><small>Michael Jackson</small></div>
      <div class="card"><p>1989</p><small>Taylor Swift</small></div>
      <div class="card"><p>Random Access Memories</p><small>Daft Punk</small></div>
    </div>
  `;
}

function getArtistsSection() {
  return `
    <div class="top-bar">
      <h2> Artists</h2>
      <p>Meet the creators of great music</p>
    </div>
    <div class="cards-grid">
      <div class="card"><p>Adele</p><small>Pop</small></div>
      <div class="card"><p>Bad Bunny</p><small>Reggaeton</small></div>
      <div class="card"><p>Queen</p><small>Rock</small></div>
    </div>
  `;
}
