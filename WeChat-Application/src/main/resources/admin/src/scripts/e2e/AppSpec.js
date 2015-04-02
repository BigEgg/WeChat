describe('App generic test', function () {
    it('set title', function () {
        browser.get(constants.URL_HOMEPAGE);
        expect(browser.getTitle()).toBe('ThoughtWorks WeChat Admin');
    });

    it('before sign in user cannot enter dashboard page', function () {
        browser.get(constants.URL_DASHBOARD_PAGE);
        expect(browser.getLocationAbsUrl()).toBe(constants.ABS_URL_HOMEPAGE);
    });

    it('after login home page should be dashboard page', function () {
        browser.get(constants.URL_HOMEPAGE);

        var usernameInput = element(by.model(constants.LOCATOR_LOGIN_USERNAME));
        var passwordInput = element(by.model(constants.LOCATOR_LOGIN_PASSWORD));
        var signInButton = element(by.id(constants.LOCATOR_LOGIN_SUBMIT));

        usernameInput.sendKeys(constants.LOGIN_USERNAME);
        passwordInput.sendKeys(constants.LOGIN_PASSWORD);
        signInButton.click();

        expect(browser.getLocationAbsUrl()).toBe(constants.ABS_URL_DASHBOARD_PAGE);

        browser.get(constants.URL_HOMEPAGE);
        expect(browser.getLocationAbsUrl()).toBe(constants.ABS_URL_DASHBOARD_PAGE);
    });
});
