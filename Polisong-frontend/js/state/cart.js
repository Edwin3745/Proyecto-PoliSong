const KEY = 'cartItems';

function load() {
  try { return JSON.parse(localStorage.getItem(KEY)) || []; } catch { return []; }
}
function save(items) { localStorage.setItem(KEY, JSON.stringify(items)); }

export function addToCart(item) {
  const items = load();
  items.push(item);
  save(items);
}
export function getCartItems() { return load(); }
export function getCartCount() { return load().length; }
export function clearCart() { save([]); }
