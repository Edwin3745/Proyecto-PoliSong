export function mostrarAdminPanel(container) {
  container.innerHTML = `
    <div class="top-bar">
      <h2>Admin Panel</h2>
      <p>Manage the platform settings and users.</p>
    </div>

    <div class="admin-dashboard">
      <button class="admin-btn">Manage Users</button>
      <button class="admin-btn">View Reports</button>
      <button class="admin-btn">Settings</button>
    </div>
  `;
}