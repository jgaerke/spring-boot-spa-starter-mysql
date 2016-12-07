const Browser = require('zombie');

Browser.localhost('localhost', 8080);

describe('submits form', function() {
  const browser = new Browser();

  before(function(done) {
    browser.visit('/', done);
  });

  it('should be successful', function() {
    browser.assert.success();
  });

  it('should see welcome page', function() {
    browser.assert.text('title', 'Starter app');
  });
});