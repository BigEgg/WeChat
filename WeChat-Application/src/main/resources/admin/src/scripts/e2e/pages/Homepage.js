var Homepage = function () {
    var PAGE_URL = 'http://localhost:3000/admin/html/index.html#/';

    this.usernameInput = element(by.model('username'));
    this.passwordInput = element(by.model('password'));
    this.signInButton = element(by.id('loginSubmit'));

    this.navigate = function () {
        browser.get(PAGE_URL);
    };

    this.getTitle = function () {
        return browser.getTitle();
    };

    this.fillSignInInfo = function (username, password) {
        this.usernameInput.clear();
        this.passwordInput.clear();
        this.usernameInput.sendKeys(username);
        this.passwordInput.sendKeys(password);
    };

    this.isSignInButtonDisabled = function () {
        return this.signInButton.getAttribute('class').then(function (classes) {
            return classes.indexOf('disabled') > -1;
        });
    };

    this.signIn = function () {
        return this.signInButton.click();
    };

    this.isInThisPage = function () {
        return browser.getCurrentUrl().then(function (url) {
            return url === PAGE_URL
        });
    }
};

module.exports = new Homepage();