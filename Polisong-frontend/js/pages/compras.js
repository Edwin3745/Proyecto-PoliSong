export function renderCompras(){
  return `
    <div class="compras-section">
      <div class="top-bar"><h2>Mis Compras</h2><p>Historial general (placeholder)</p></div>
      <table class="compras-table">
        <thead><tr><th>Fecha</th><th>Producto</th><th>Estado</th><th>Acciones</th></tr></thead>
        <tbody>
          <tr><td>-</td><td>No hay registros</td><td>-</td><td><button class='btn-outline' disabled>Soporte</button></td></tr>
        </tbody>
      </table>
    </div>`;
}