import View from './View';

class GlobalNavView extends View {
  constructor() {
    super(null, '#global-nav');
    this.logout = this.logout.bind(this);
    this.onUserAuthChange = this.onUserAuthChange.bind(this);
  }

  getModel() {
    return Promise.resolve({
      authenticated: this.session.isAuthenticated(),
      route: this.route
    });
  }

  setup(template, model) {
    return super.setup(template, model).then((ractive)=> {
      this.broker.on('onAuthenticationChange', this.onUserAuthChange);
      this.ractive.on('logout', this.logout)
      return ractive;
    });
  }

  onUserAuthChange(event, message) {
    this.session.set('authenticated', message.authenticated);
    this.ractive.set('authenticated', message.authenticated);
  }

  logout(e) {
    this.session.logout().then(() => {
      this.broker.trigger('onAuthenticationChange', { authenticated: false });
    });
  }

  teardown() {
    super.teardown();
    this.broker.off('onAuthenticationChange', this.onUserAuthChange);
  }
}

export default GlobalNavView;