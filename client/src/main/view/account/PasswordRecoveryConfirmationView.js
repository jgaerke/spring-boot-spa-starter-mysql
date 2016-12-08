import View from './../View';

class PasswordRecoveryView extends View {
  constructor() {
    super('#viewport', '#password-recovery-confirmation', '/partials/account/password-recovery-confirmation.html');
  }
}

export default PasswordRecoveryView;