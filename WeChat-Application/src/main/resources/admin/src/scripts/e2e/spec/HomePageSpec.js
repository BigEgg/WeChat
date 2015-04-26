describe('When open the homepage', function () {
    var constants = require('../constants.js');
    var homepage = require('../pages/Homepage.js');
    var loggedInHomepage = require('../pages/LoggedInHomepage.js');
    var generalDashboardPage = require('../pages/dashboard/DashboardPage.js');

    beforeEach(function () {
        homepage.actions.navigate();
    });

    it('should disable the submit button if no username input', function () {
        expect(homepage.status.isSignInFormPresent()).toBeTruthy();
        expect(homepage.status.isSignInButtonDisabled()).toBeTruthy();
        expect(loggedInHomepage.status.isDashboardMenuPresent()).toBeFalsy();
        expect(loggedInHomepage.status.isBasicSettingsMenuPresent()).toBeFalsy();
        expect(loggedInHomepage.status.isUserMenuPresent()).toBeFalsy();
    });

    it('input should have 32 max-length', function () {
        homepage.actions.fillSignInInfo(
            'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz',
            'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz'
        );

        expect(homepage.properties.getUsernameInputValue()).toBe('abcdefghijklmnopqrstuvwxyzabcdef');
        expect(homepage.properties.getPasswordInputValue()).toBe('abcdefghijklmnopqrstuvwxyzabcdef');
    });

    it('should enable the submit button if username had input', function () {
        homepage.actions.fillSignInInfo('username', '');

        expect(homepage.status.isSignInButtonDisabled()).toBeFalsy();
    });

    it('should keep invalid sign in on this page', function () {
        homepage.actions.fillSignInInfo('WrongUserName', '');
        homepage.actions.signIn();

        expect(homepage.status.isInThisPage()).toBeTruthy();
    });

    it('ensures user can sign in', function () {
        homepage.actions.fillSignInInfo(constants.LOGIN_USERNAME, constants.LOGIN_PASSWORD);
        homepage.actions.signIn();

        expect(generalDashboardPage.status.isInThisPage()).toBeTruthy();
        expect(homepage.status.isSignInFormPresent()).toBeFalsy();
        expect(loggedInHomepage.properties.getLoggedInUsername()).toBe(constants.USERNAME);
        expect(loggedInHomepage.status.isDashboardMenuPresent()).toBeTruthy();
        expect(loggedInHomepage.status.isBasicSettingsMenuPresent()).toBeTruthy();
        expect(loggedInHomepage.status.isUserMenuPresent()).toBeTruthy();
    });
});
