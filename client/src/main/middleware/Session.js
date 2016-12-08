import Http from './Http'

let singleton = Symbol();
let singletonEnforcer = Symbol();

class Session {
  constructor(enforcer) {
    if (enforcer !== singletonEnforcer) {
      throw "Cannot construct singleton"
    }
    this.data = {authenticated: false};
    this.$ = $;
    this.http = Http.instance;
    this.login = this.login.bind(this);
    this.logout = this.logout.bind(this);
    this.getData = this.getData.bind(this);
    this.get = this.get.bind(this);
    this.set = this.set.bind(this);
    this.isAuthenticated = this.isAuthenticated.bind(this);
  }

  login(email, password, rememberMe) {
    if (this.authenticated) {
      return Promise.resolve({});
    }
    const data = this.data;

    return this.http.post('/api/accounts/login', {email, password, rememberMe})
        .then((response) => {
          console.log('login success', response);
          data.authenticated = true;
          return response;
        })
        .catch((response) => {
          console.log('login failure', response);
          data.authenticated = false;
          return response;
        });
  }

  logout() {
    const data = this.data;

    if (!data.authenticated) {
      data.authenticated = false;
      return Promise.resolve({ authenticated: false });
    }

    return this.http.post('/api/accounts/logout')
        .then((response) => {
          console.log('logout success', response);
          data.authenticated = false;
          return { authenticated: true };
        })
        .catch((response) => {
          console.log('logout failure', response);
          return { authenticated: false };
        });
  }

  getData() {
    return this.data;
  }

  get(key) {
    return this.data[key];
  }

  set(key, val) {
    this.data[key] = val;
  }

  isAuthenticated() {
    return this.data['authenticated'];
  }

  static get instance() {
    if (!this[singleton]) {
      this[singleton] = new Session(singletonEnforcer);
    }
    return this[singleton];
  }
}

export default Session;
