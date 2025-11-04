import { obtenerCanciones, agregarCancion, actualizarCancion, eliminarCancion } from "../api/canciones.js";

const form = document.getElementById("formAgregar");
const tabla = document.getElementById("tablaCanciones");
const modal = document.getElementById("modal");
const formEditar = document.getElementById("formEditar");
const cancelar = document.getElementById("cancelar");

let cancionActual = null;

// 🔹 Cargar canciones
async function cargarCanciones() {
  tabla.innerHTML = "";
  const canciones = await obtenerCanciones();

  canciones.forEach(c => {
    const fila = document.createElement("tr");
    fila.innerHTML = `
      <td>${c.idCancion}</td>
      <td>${c.nombre}</td>
      <td>${c.artista}</td>
      <td>${c.genero}</td>
      <td>
        <button class="editar" data-id="${c.idCancion}">✏️</button>
        <button class="eliminar" data-id="${c.idCancion}">❌</button>
      </td>
    `;
    tabla.appendChild(fila);
  });

  document.querySelectorAll(".editar").forEach(btn =>
    btn.addEventListener("click", () => abrirModalEdicion(btn.dataset.id))
  );

  document.querySelectorAll(".eliminar").forEach(btn =>
    btn.addEventListener("click", () => borrarCancion(btn.dataset.id))
  );
}

// 🔹 Agregar canción
form.addEventListener("submit", async (e) => {
  e.preventDefault();
  const nuevaCancion = {
    nombre: document.getElementById("nombre").value,
    artista: document.getElementById("artista").value,
    genero: document.getElementById("genero").value
  };
  await agregarCancion(nuevaCancion);
  form.reset();
  await cargarCanciones();
});

// 🔹 Abrir modal de edición
async function abrirModalEdicion(id) {
  const canciones = await obtenerCanciones();
  cancionActual = canciones.find(c => c.idCancion == id);

  document.getElementById("editId").value = cancionActual.idCancion;
  document.getElementById("editNombre").value = cancionActual.nombre;
  document.getElementById("editArtista").value = cancionActual.artista;
  document.getElementById("editGenero").value = cancionActual.genero;

  modal.style.display = "flex";
}

// 🔹 Guardar cambios
formEditar.addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = document.getElementById("editId").value;
  const actualizada = {
    nombre: document.getElementById("editNombre").value,
    artista: document.getElementById("editArtista").value,
    genero: document.getElementById("editGenero").value
  };
  await actualizarCancion(id, actualizada);
  modal.style.display = "none";
  await cargarCanciones();
});

// 🔹 Cancelar edición
cancelar.addEventListener("click", () => (modal.style.display = "none"));

// 🔹 Eliminar canción
async function borrarCancion(id) {
  if (confirm("¿Seguro que deseas eliminar esta canción?")) {
    await eliminarCancion(id);
    await cargarCanciones();
  }
}

cargarCanciones();
