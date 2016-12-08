let singleton = Symbol();
let singletonEnforcer = Symbol();


class Broker {
  constructor(enforcer) {
    if (enforcer !== singletonEnforcer) {
      throw "Cannot construct singleton"
    }
    this.$ = $;

    this.on = this.on.bind(this);
    this.off = this.off.bind(this);
    this.trigger = this.trigger.bind(this);
    this.hub = $({});
  }


  on() {
    this.hub.on.apply(this.hub, arguments);
    return Promise.resolve(this);
  }

  off() {
    this.hub.off.apply(this.hub, arguments);
    return Promise.resolve(this);
  }

  trigger() {
    this.hub.trigger.apply(this.hub, arguments);
    return Promise.resolve(this);
  }

  static get instance() {
    if (!this[singleton]) {
      this[singleton] = new Broker(singletonEnforcer);
    }
    return this[singleton];
  }

}

export default Broker;