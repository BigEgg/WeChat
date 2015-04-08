ddescribe('App generic test', function () {
    var homepage = require('../pages/Homepage.js');
    var dashboardPage = require('../pages/dashboard/DashboardPage.js');
    var constants = require('../constants.js');

    it('set title', function () {
        homepage.actions.navigate();
        expect(homepage.properties.getTitle()).toBe('ThoughtWorks WeChat Admin');
    });

    it('before sign in user cannot enter dashboard page', function () {
        dashboardPage.actions.navigate();
        expect(homepage.status.isInThisPage()).toBeTruthy();
    });

    it('after login home page should be dashboard page', function () {
        homepage.actions.navigate();
        homepage.actions.fillSignInInfo(constants.LOGIN_USERNAME, constants.LOCATOR_LOGIN_PASSWORD);
        homepage.actions.signIn().then(function () {
            expect(dashboardPage.status.isInThisPage()).toBeTruthy();

            homepage.actions.navigate();
            expect(dashboardPage.status.isInThisPage()).toBeTruthy();
        });
    });
});
