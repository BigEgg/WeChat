describe('Test the navigate', function () {
    describe('After logged in', function () {
        var loggedInHomepage = require('../pages/LoggedInHomepage.js');

        beforeEach(function () {
            var constants = require('../constants.js');
            var homepage = require('../pages/Homepage.js');

            homepage.actions.navigate();
            homepage.actions.fillSignInInfo(constants.LOGIN_USERNAME, constants.LOGIN_PASSWORD);
            homepage.actions.signIn();
        });

        it('can navigate to WeChat Basic Settings page', function () {
            var weChatBasicSettingsPage = require('../pages/basicSettings/WeChatBasicSettingsPage.js');

            expect(weChatBasicSettingsPage.status.isInThisPage()).toBeFalsy();
            loggedInHomepage.actions.clickWeChatBasicSettingsMenu();
            expect(weChatBasicSettingsPage.status.isInThisPage()).toBeTruthy();
        });

        it('can navigate to System Message Basic Settings page', function () {
            var systemMessageBasicSettingsPage = require('../pages/basicSettings/SystemMessageBasicSettingsPage.js')

            expect(systemMessageBasicSettingsPage.status.isInThisPage()).toBeFalsy();
            loggedInHomepage.actions.clickSystemMessageSettingsMenu();
            expect(systemMessageBasicSettingsPage.status.isInThisPage()).toBeTruthy();
        });

        it('can navigate to General Dashboard page', function () {
            var generalDashboardPage = require('../pages/dashboard/DashboardPage.js');

            loggedInHomepage.actions.clickWeChatBasicSettingsMenu();
            expect(generalDashboardPage.status.isInThisPage()).toBeFalsy();
            loggedInHomepage.actions.clickGeneralDashboardMenu();
            expect(generalDashboardPage.status.isInThisPage()).toBeTruthy();
        });


        it('ensures user can sign out', function () {
            var homepage = require('../pages/Homepage.js');

            expect(homepage.status.isInThisPage()).toBeFalsy();
            loggedInHomepage.actions.signOut();

            browser.getLocationAbsUrl().then(function (url) {
                expect(homepage.status.isInThisPage()).toBeTruthy();
            });
        });
    });
});