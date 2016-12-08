import View from './View';

class CompositeView {
  constructor() {
    this.$ = $;
    this._ = _;
    this.views = Array.prototype.slice.call(arguments);
  }

  withRoute(route) {
    this.views.forEach((view) => view.withRoute(route));
    return this;
  }

  render() {
    const views = this.views;
    const self = this;

    return Promise.all(views.map((view) => view.getTemplate())).then((templates) => {
      return Promise.all(views.map((view) => view.getModel())).then((models) => {
        views.forEach((view, idx) => {
          view.setup(templates[idx], models[idx]);
        });
      }).then(() => {
        return self;
      });
    });
  }

  //render() {
  //  return Promise.all(this.views.map((view) => view.render())).then(()=> this);
  //}

  teardown() {
    return Promise.all(this.views.map((view) => view.teardown())).then(()=> this);
  }
}


export default CompositeView;