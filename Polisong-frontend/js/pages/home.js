import { mostrarAdminPanel } from "./admin.js";
import { mostrarProviderDashboard } from "./provider.js";
import { renderPerfilSection } from "./perfil.js";
import { renderRepositorio } from "./repositorio.js";
import { renderPlaylists } from "./playlists.js";
import { renderDescargas } from "./descargas.js";
import { renderCompras } from "./compras.js";
import { ensureNavbar } from "./navbar.js";

export function mostrarHome(main, nombreUsuario) {
  document.body.classList.add('home-mode');
  document.body.classList.remove('auth-screen');
  const userRole = (localStorage.getItem("userRole") || "USUARIO").toUpperCase();

  main.innerHTML = `
    <div class="home-wrapper">
      <aside class="sidebar">
        <h1>PoliSong</h1>
        <nav>
          ${buildMenu(userRole)}
        </nav>
      </aside>
      <section class="main-content" id="mainContent">
        ${getHomeSection(nombreUsuario, userRole)}
      </section>
    </div>
  `;

  ensureNavbar(nombreUsuario);

  const content = document.getElementById("mainContent");
  const buttons = main.querySelectorAll(".menu-btn");

  // Evento de clic en cada botón del menu
  buttons.forEach((btn) => {
    btn.addEventListener("click", () => {
      buttons.forEach((b) => b.classList.remove("active"));
      btn.classList.add("active");

      const section = btn.dataset.section;
      content.classList.add("fade-out");

      setTimeout(() => {
        switch (section) {
          case "home":
            content.innerHTML = getHomeSection(nombreUsuario, userRole);
            break;
          case "perfil":
            content.innerHTML = renderPerfilSection(nombreUsuario);
            break;
          case "repositorio":
            content.innerHTML = renderRepositorio();
            break;
          case "playlists":
            content.innerHTML = renderPlaylists();
            break;
          case "descargas":
            content.innerHTML = renderDescargas();
            break;
          case "compras":
            content.innerHTML = renderCompras();
            break;
          case "admin":
            content.innerHTML = getAdminSection();
            break;
          case "provider":
            // Render proveedor como nodo para conservar listeners internos
            const providerContainer = document.createElement('div');
            mostrarProviderDashboard(providerContainer);
            content.innerHTML = '';
            content.appendChild(providerContainer);
            break;
          case "songs":
            content.innerHTML = getSongsSection();
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

//DASHBOARD secciones

function getHomeSection(nombreUsuario, role) {
  // Tarjetas de ejemplo, placeholders.
  const exampleCards = Array.from({ length: 4 }).map((_, i) => ({ title: `Ejemplo ${i + 1}`, artist: "Demo" }));

  let extraBlock = "";
  if (role === "ADMIN") {
    extraBlock = `<div class="admin-alert">\n        <h3>Panel Administrativo</h3>\n        <p>Accede a gestión de usuarios y reportes desde el menú.</p>\n      </div>`;
  } else if (role === "PROVIDER") {
    extraBlock = `<div class="provider-alert">\n        <h3>Resumen Proveedor</h3>\n        <p>Administra tus productos y revisa estadísticas de ventas.</p>\n      </div>`;
  } else {
    extraBlock = `<div class="user-alert">\n        <h3>Descubre Música</h3>\n        <p>Explora canciones, playlists y artistas recomendados.</p>\n      </div>`;
  }

  return `
    <div class="top-bar">
      <h2>Bienvenido, ${nombreUsuario}</h2>
      <p>Rol activo: ${role}</p>
    </div>
    ${extraBlock}
    <div>
      <h3 class="section-title">Quick pick</h3>
      <div class="cards-grid">
        ${exampleCards.map(card => `
          <div class="card placeholder">
            <p>${card.title}</p>
            <small>${card.artist}</small>
          </div>`).join("")}
      </div>
    </div>
  `;
}

function buildMenu(role) {
  const base = `
    <button class="menu-btn active" data-section="home">Inicio</button>
    <button class="menu-btn" data-section="perfil">Perfil</button>
    <button class="menu-btn" data-section="repositorio">Repositorio</button>
    <button class="menu-btn" data-section="playlists">Playlists</button>
    <button class="menu-btn" data-section="descargas">Descargas</button>
    <button class="menu-btn" data-section="compras">Compras</button>
  `;
  if (role === "ADMIN") {
    return base + `
      <button class="menu-btn" data-section="admin">Admin</button>
      <button class="menu-btn" id="btnSalir">Salir</button>
    `;
  }
  if (role === "PROVIDER") {
    return base + `
      <button class="menu-btn" data-section="provider">Proveedor</button>
      <button class="menu-btn" id="btnSalir">Salir</button>
    `;
  }
  return base + `
    <button class="menu-btn" id="btnSalir">Salir</button>
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

function getAdminSection() {
  const container = document.createElement("div");
  mostrarAdminPanel(container);
  return container.innerHTML;
}

function getProveedorSection() {
  const container = document.createElement("div");
  mostrarProveedorDashboard(container);
  return container.innerHTML;
}
