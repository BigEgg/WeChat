describe('After logged in', function () {
    var HOMEPAGE_URL = 'http://localhost:3000/admin/html/index.html#/';
    var DASHBOARD_PAGE_URL = 'http://localhost:3000/admin/html/index.html#/dashboard';

    beforeEach(function () {
        browser.get(HOMEPAGE_URL);

        var usernameInput = element(by.model('username'));
        var passwordInput = element(by.model('password'));
        var signInButton = element(by.id('loginSubmit'));

        usernameInput.sendKeys('BigEgg');
        passwordInput.sendKeys('');
        signInButton.click();
    });
});