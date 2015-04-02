describe('When open the homepage', function () {
    var HOMEPAGE_URL = 'http://localhost:3000/admin/html/index.html#/';

    beforeEach(function () {
        browser.get(HOMEPAGE_URL);
    });

    it('should disable the submit button if no username input', function () {
        var signInButton = element(by.id('loginSubmit'));
        signInButton.getAttribute('class').then(function (classes) {
            expect(classes.indexOf('disabled') > -1).toBeTruthy();
        });
    });

    it('should enable the submit button if username had input', function () {
        var usernameInput = element(by.model('username'));
        var signInButton = element(by.id('loginSubmit'));

        usernameInput.sendKeys('BigEgg');
        signInButton.getAttribute('class').then(function (classes) {
            expect(classes.indexOf('disabled') > -1).toBeFalsy();
        });
    });

    it('should keep invalid sign in on this page', function () {
        var usernameInput = element(by.model('username'));
        var passwordInput = element(by.model('password'));
        var signInButton = element(by.id('loginSubmit'));

        usernameInput.sendKeys('wrong user name');
        passwordInput.sendKeys('');
        signInButton.click();

        expect(browser.getLocationAbsUrl()).toBe('/');
    });

    it('ensures user can log in', function () {
        var usernameInput = element(by.model('username'));
        var passwordInput = element(by.model('password'));
        var signInButton = element(by.id('loginSubmit'));

        usernameInput.sendKeys('BigEgg');
        passwordInput.sendKeys('');
        signInButton.click();

        expect(browser.getLocationAbsUrl()).toBe('/dashboard');
    });
});