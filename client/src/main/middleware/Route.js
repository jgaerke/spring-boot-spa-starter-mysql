class Route {
  constructor(name, path, view, authenticated = false) {
    this.name = name;
    this.path = path;
    this.view = view;
    this.authenticated = authenticated;
    this.$ = $;
  }

  isAuthenticated() {
    return this.authenticated;
  }

  getPath() {
    return this.path;
  }

  handle(route) {
    return this.view.withRoute((Object.assign({}, {name: this.name}, route))).render();
  }
}

export default Route;
