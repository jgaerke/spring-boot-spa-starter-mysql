import View from './View';

class IndexView extends View {
  constructor() {
    super('#viewport', '#index', '/partials/index.html');
  }
}

export default IndexView;