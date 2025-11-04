// js/app.js

async function cargarPagina(nombre) {
  const contenedor = document.getElementById("contenido");

  switch (nombre) {
    case "home":
      contenedor.innerHTML = await homePage();
      break;
    case "catalogo":
      contenedor.innerHTML = await catalogoPage();
      break;
    case "carrito":
      contenedor.innerHTML = await carritoPage();
      break;
    case "perfil":
      contenedor.innerHTML = await perfilPage();
      break;
    default:
      contenedor.innerHTML = "<h2>Página no encontrada</h2>";
  }
}

// Cargar la página de inicio por defecto
document.addEventListener("DOMContentLoaded", () => cargarPagina("home"));
