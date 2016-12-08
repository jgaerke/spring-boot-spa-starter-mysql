import View from './../View';

class LoginView extends View {
  constructor() {
    super('#viewport', '#login', '/partials/account/login.html');
    this.login = this.login.bind(this);
  }

  getModel() {
    return Promise.resolve({
      email: '',
      password: '',
      rememberMe: true,
      serverErrors: {
        loginInfoInvalid: false,
        credentialsInvalid: false
      }
    });
  }

  setup(template, model) {
    return super.setup(template, model).then((ractive)=> {
      this.$('#login-form').validator().on('submit', this.login);
      return ractive;
    });
  }

  getEncodedCredentials(credentials) {
    const str = [];
    for (var p in credentials)
      str.push(encodeURIComponent(p) + "=" + encodeURIComponent(credentials[p]));
    return str.join("&");
  }

  login(e) {
    if (e.isDefaultPrevented()) {
      return;
    }
    e.preventDefault();


    this.ractive.set('serverErrors', {
      loginInfoInvalid: false,
      credentialsInvalid: false
    });

    const _ = this._;
    const requestBody = _.omit(this.model, ['serverErrors']);

    console.log('login clicked', requestBody);

    return this.http.post(
        '/api/accounts/login',
        this.getEncodedCredentials(requestBody),
        {'Content-Type': 'application/x-www-form-urlencoded'}
    ).then((response) => {
      console.log('success', response);
      this.broker.trigger('onAuthenticationChange', {authenticated: true});
      this.router.navigate('/');
    }).catch((response) => {
      console.log('error', response);
      if (response.status == 400) {
        this.ractive.set('serverErrors.loginInfoInvalid', true);
        return;
      }
      if (response.status == 401) {
        this.ractive.set('serverErrors.credentialsInvalid', true);
      }
    });
  }
}

export default LoginView;