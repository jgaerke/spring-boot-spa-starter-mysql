import { Cache, Http } from '../middleware';

let singleton = Symbol();
let singletonEnforcer = Symbol();

class Loader {
  constructor(enforcer) {
    if (enforcer !== singletonEnforcer) {
      throw "Cannot construct singleton"
    }
    this.http = Http.instance;
    this.cache = Cache.instance;
  }

  load(url) {
    if (this.cache.get(url)) {
      return Promise.resolve({ html: this.cache.get(url) });
    }
    return new Promise((resolve, reject) => {
      this.http.get(url).then((html) => {
        this.cache.set(url, html);
        resolve({html});
      }).catch((response) => {
        reject({html: null, response});
      });
    });
  }

  static get instance() {
    if(!this[singleton]) {
      this[singleton] = new Loader(singletonEnforcer);
    }
    return this[singleton];
  }
}

export default Loader;