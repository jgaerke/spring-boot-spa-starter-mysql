import View from './../View';

class PasswordResetView extends View {
  constructor() {
    super('#viewport', '#password-reset', '/partials/account/password-reset.html');
    this.reset = this.reset.bind(this);
  }

  getModel() {
    return Promise.resolve({
      password: '',
      serverErrors: {
        resetInfoInvalid: false,
        unexpectedError: false,
        invalidToken: false
      }
    });
  }

  setup(template, model) {
    return super.setup(template, model).then((ractive)=> {
      this.$('#password-reset-form').validator().on('submit', this.reset);
      return ractive;
    });
  }

  reset(e) {
    if (e.isDefaultPrevented()) {
      return;
    }
    e.preventDefault();


    this.ractive.set('serverErrors', {
      resetInfoInvalid: false,
      unexpectedError: false,
      emailNotFound: false
    });

    const _ = this._;
    const requestBody = _.extend(
        {passwordResetToken: this.route.params.passwordResetToken},
        _.omit(this.model, ['serverErrors'])
    );

    console.log('reset clicked', requestBody);

    return this.http.post('/api/accounts/password/reset', requestBody).then((response) => {
      console.log('success', response);
      this.router.navigate('/login');
    }).catch((response) => {
      if (response.status.toString().indexOf('5') > -1) {
        this.ractive.set('serverErrors.unexpectedError', true);
        return;
      }
      if (response.status == 400) {
        this.ractive.set('serverErrors.resetInfoInvalid', true);
      }
      if (response.status == 404) {
        this.ractive.set('serverErrors.invalidToken', true);
      }
    });
  }
}

export default PasswordResetView;