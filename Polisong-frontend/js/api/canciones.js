const API_URL = "http://localhost:8080/api/canciones";

export async function obtenerCanciones() {
  const response = await fetch(API_URL);
  return await response.json();
}

export async function agregarCancion(cancion) {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(cancion),
  });
  return await response.json();
}

export async function actualizarCancion(id, cancion) {
  const response = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(cancion),
  });
  return await response.json();
}

export async function eliminarCancion(id) {
  await fetch(`${API_URL}/${id}`, { method: "DELETE" });
}
