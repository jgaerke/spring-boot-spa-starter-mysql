import { toPromise } from '../util';

let singleton = Symbol();
let singletonEnforcer = Symbol();

class Http {
  constructor(enforcer) {
    if (enforcer !== singletonEnforcer) {
      throw "Cannot construct singleton"
    }
    this.$ = $;
    this.cookies = cookies;
  }

  dispatch(options) {
    if (options.contentType === undefined) {
      options.contentType = false;
    }
    if (options.data && this.$.isPlainObject(options.data)) {
      options.data = JSON.stringify(options.data);
    }
    options.headers = this.$.extend({
      'Content-Type': 'application/json',
      'X-Requested-With': 'XMLHttpRequest',
      'X-XSRF-TOKEN': this.cookies.get('XSRF-TOKEN')
    }, options.headers);
    return toPromise(this.$.ajax(options));
  }

  get(url, headers) {
    return this.dispatch({
      url: url,
      type: 'GET',
      headers: headers
    });
  }

  post(url, data, headers) {
    return this.dispatch({
      url: url,
      type: 'POST',
      data: data,
      headers: headers
    });
  }

  put(url, data, headers) {
    return this.dispatch({
      url: url,
      type: 'PUT',
      data: data,
      headers: headers
    });
  }

  patch(url, data, headers) {
    return this.dispatch({
      url: url,
      type: 'PATCH',
      data: data,
      headers: headers
    });
  }

  delete(url, headers) {
    return this.dispatch({
      url: url,
      type: 'DELETE',
      headers: headers
    });
  }

  static get instance() {
    if (!this[singleton]) {
      this[singleton] = new Http(singletonEnforcer);
    }
    return this[singleton];
  }
}

export default Http;