let singleton = Symbol();
let singletonEnforcer = Symbol();

class Cache {
  constructor(enforcer) {
    if (enforcer !== singletonEnforcer) {
      throw "Cannot construct singleton"
    }
    this.data = {};
  }

  get(key) {
    return this.data[key];
  }

  set(key, value) {
    this.data[key] = value;
  }

  static get instance() {
    if (!this[singleton]) {
      this[singleton] = new Cache(singletonEnforcer);
    }
    return this[singleton];
  }
}

export default Cache;
