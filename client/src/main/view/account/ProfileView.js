import View from './../View';

class ProfileView extends View {
  constructor() {
    super('#viewport', '#profile', '/partials/account/profile.html');
    this.save = this.save.bind(this);
  }

  getModel() {
    return this.get().then((account) => {
      console.log('current account', account);
      return {
        email: account.email,
        first: account.first,
        last: account.last,
        serverErrors: {
          emailTaken: false,
          accountInfoInvalid: false,
        }
      };
    }).catch((response) => {
      if(response.status == 404) {
        console.log('ah... account does not exist.', response);
        this.session.logout().then(() => {
          this.broker.trigger('onAuthenticationChange', { authenticated: false });
          this.router.navigate('/login');
        });
      }
    });

  }

  setup(template, model) {
    return super.setup(template, model).then((ractive)=> {
      this.$('#profile-form').validator().on('submit', (e)=> this.save(e));
      return ractive;
    });
  }

  get() {
    return this.http.get('/api/accounts/current');
  }

  save(e) {
    console.log('save clicked', this.model);
    if (e.isDefaultPrevented()) {
      return;
    }
    e.preventDefault();


    this.ractive.set('serverErrors', {
      emailTaken: false,
      accountInfoInvalid: false
    });

    return this.http.put('/api/accounts', this.model).then((response) => {
      console.log('profile save success', response);
    }).catch((response) => {
      console.log('profile save error', response);
      if (response.status == 409) {
        this.ractive.set('serverErrors.emailTaken', true);
      }
      if (response.status == 400) {
        this.ractive.set('serverErrors.accountInfoInvalid', true);
      }
    });
  }
}

export default ProfileView;