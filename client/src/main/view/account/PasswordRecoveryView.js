import View from './../View';

class PasswordRecoveryView extends View {
  constructor() {
    super('#viewport', '#password-recovery', '/partials/account/password-recovery.html');
    this.recover = this.recover.bind(this);
  }

  getModel() {
    return Promise.resolve({
      email: null,
      serverErrors: {
        unexpectedError: false,
        emailNotFound: false
      }
    });
  }

  setup(template, model) {
    return super.setup(template, model).then((ractive)=> {
      this.$('#password-recovery-form').validator().on('submit', this.recover);
      return ractive;
    });
  }

  recover(e) {
    if (e.isDefaultPrevented()) {
      return;
    }
    e.preventDefault();


    this.ractive.set('serverErrors', {
      unexpectedError: false,
      emailNotFound: false
    });

    const _ = this._;
    const requestBody = _.omit(this.model, ['serverErrors']);

    console.log('recover clicked', requestBody);

    return this.http.post('/api/accounts/password/recover', requestBody).then((response) => {
      console.log('success', response);
      this.router.navigate('/account/password/recovery/confirmation');
    }).catch((response) => {
      if (response.status.toString().indexOf('5') > -1) {
        this.ractive.set('serverErrors.unexpectedError', true);
        return;
      }
      if (response.status == 404) {
        this.ractive.set('serverErrors.emailNotFound', true);
      }
    });
  }
}

export default PasswordRecoveryView;