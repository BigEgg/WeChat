describe('App generic test', function () {
    var HOMEPAGE_URL = 'http://localhost:3000/admin/html/index.html#/';
    var DASHBOARD_PAGE_URL = 'http://localhost:3000/admin/html/index.html#/dashboard';

    it('set title', function () {
        browser.get(HOMEPAGE_URL);
        expect(browser.getTitle()).toBe('ThoughtWorks WeChat Admin');
    });

    it('before sign in user cannot enter dashboard page', function () {
        browser.get(DASHBOARD_PAGE_URL);
        expect(browser.getLocationAbsUrl()).toBe('/');
    });

    it('after login home page should be dashboard page', function () {
        browser.get(HOMEPAGE_URL);

        var usernameInput = element(by.model('username'));
        var passwordInput = element(by.model('password'));
        var signInButton = element(by.id('loginSubmit'));

        usernameInput.sendKeys('BigEgg');
        passwordInput.sendKeys('');
        signInButton.click();

        expect(browser.getLocationAbsUrl()).toBe('/dashboard');
    });
});
