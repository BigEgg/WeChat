describe('When open the homepage', function () {
    beforeEach(function () {
        browser.get(constants.URL_HOMEPAGE);
    });

    it('should disable the submit button if no username input', function () {
        var signInButton = element(by.id(constants.LOCATOR_LOGIN_SUBMIT));
        signInButton.getAttribute(constants.ATTRIBUTE_CLASS).then(function (classes) {
            expect(classes.indexOf('disabled') > -1).toBeTruthy();
        });
    });

    it('input should have 32 max-length', function () {
        var usernameInput = element(by.model(constants.LOCATOR_LOGIN_USERNAME));
        var passwordInput = element(by.model(constants.LOCATOR_LOGIN_PASSWORD));

        usernameInput.sendKeys('abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz');
        passwordInput.sendKeys('abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz');

        usernameInput.getAttribute(constants.ATTRIBUTE_INPUT_VALUE).then(function (value) {
            expect(value).toBe('abcdefghijklmnopqrstuvwxyzabcdef');
        });
        passwordInput.getAttribute(constants.ATTRIBUTE_INPUT_VALUE).then(function (value) {
            expect(value).toBe('abcdefghijklmnopqrstuvwxyzabcdef');
        });

    });

    it('should enable the submit button if username had input', function () {
        var usernameInput = element(by.model(constants.LOCATOR_LOGIN_USERNAME));
        var signInButton = element(by.id(constants.LOCATOR_LOGIN_SUBMIT));

        usernameInput.sendKeys('username');
        signInButton.getAttribute(constants.ATTRIBUTE_CLASS).then(function (classes) {
            expect(classes.indexOf('disabled') > -1).toBeFalsy();
        });
    });

    it('should keep invalid sign in on this page', function () {
        var usernameInput = element(by.model(constants.LOCATOR_LOGIN_USERNAME));
        var passwordInput = element(by.model(constants.LOCATOR_LOGIN_PASSWORD));
        var signInButton = element(by.id(constants.LOCATOR_LOGIN_SUBMIT));

        usernameInput.sendKeys('WrongUserName');
        passwordInput.sendKeys('');
        signInButton.click().then(function () {
            expect(browser.getLocationAbsUrl()).toBe(constants.ABS_URL_HOMEPAGE);
        });
    });

    it('ensures user can log in', function () {
        var usernameInput = element(by.model(constants.LOCATOR_LOGIN_USERNAME));
        var passwordInput = element(by.model(constants.LOCATOR_LOGIN_PASSWORD));
        var signInButton = element(by.id(constants.LOCATOR_LOGIN_SUBMIT));

        usernameInput.sendKeys(constants.LOGIN_USERNAME);
        passwordInput.sendKeys(constants.LOGIN_PASSWORD);
        signInButton.click().then(function () {
            expect(browser.getLocationAbsUrl()).toBe(constants.ABS_URL_DASHBOARD_PAGE);
        });
    });
});