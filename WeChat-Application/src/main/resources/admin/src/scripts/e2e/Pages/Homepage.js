var Homepage = function () {
    var PAGE_URL = 'http://localhost:3000/admin/html/index.html#/';

    var LOCATOR_LOGIN_FORM = 'menu_signin_form';
    var LOCATOR_LOGIN_USERNAME = 'username';
    var LOCATOR_LOGIN_PASSWORD = 'password';
    var LOCATOR_LOGIN_SUBMIT = 'loginSubmit';

    var Actions = function () {
        this.navigate = function () {
            browser.get(PAGE_URL);
        };
        this.fillSignInInfo = function (username, password) {
            var usernameInput = element(by.model(LOCATOR_LOGIN_USERNAME));
            var passwordInput = element(by.model(LOCATOR_LOGIN_PASSWORD));

            usernameInput.clear();
            passwordInput.clear();
            usernameInput.sendKeys(username);
            passwordInput.sendKeys(password);
        };
        this.signIn = function () {
            return element(by.id(LOCATOR_LOGIN_SUBMIT)).click();
        };
    };

    var Status = function () {
        this.isSignInFormPresent = function () {
            return element(by.id(LOCATOR_LOGIN_FORM)).isPresent();
        };
        this.isSignInButtonDisabled = function () {
            var signInButton = element(by.id(LOCATOR_LOGIN_SUBMIT));

            return signInButton.getAttribute('class').then(function (classes) {
                return classes.indexOf('disabled') > -1;
            });
        };
        this.isInThisPage = function () {
            return browser.getLocationAbsUrl().then(function (url) {
                return url === '/' || url === '';
            });
        };
    };

    var Properties = function () {
        this.getTitle = function () {
            return browser.getTitle();
        };
        this.getUsernameInputValue = function () {
            var usernameInput = element(by.model(LOCATOR_LOGIN_USERNAME));

            return usernameInput.getAttribute('value').then(function (value) {
                return value;
            });
        };
        this.getPasswordInputValue = function () {
            var passwordInput = element(by.model(LOCATOR_LOGIN_PASSWORD));

            return passwordInput.getAttribute('value').then(function (value) {
                return value;
            });
        };
    };

    this.actions = new Actions();
    this.status = new Status();
    this.properties = new Properties();
};

module.exports = new Homepage();