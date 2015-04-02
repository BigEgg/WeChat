describe('After logged in', function () {
    beforeEach(function () {
        browser.get(constants.URL_HOMEPAGE);

        var usernameInput = element(by.model(constants.LOCATOR_LOGIN_USERNAME));
        var passwordInput = element(by.model(constants.LOCATOR_LOGIN_PASSWORD));
        var signInButton = element(by.id(constants.LOCATOR_LOGIN_SUBMIT));

        usernameInput.sendKeys(constants.LOGIN_USERNAME);
        passwordInput.sendKeys(constants.LOGIN_PASSWORD);
        signInButton.click();
    });
});