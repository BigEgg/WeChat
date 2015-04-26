describe('After logged in', function () {
    var loggedInHomepage = require('../../pages/LoggedInHomepage.js');
    var generalDashboardPage = require('../../pages/dashboard/DashboardPage.js');

    beforeEach(function () {
        var constants = require('../../constants.js');
        var homepage = require('../../pages/Homepage.js');

        homepage.actions.navigate();
        homepage.actions.fillSignInInfo(constants.LOGIN_USERNAME, constants.LOGIN_PASSWORD);
        homepage.actions.signIn();
    });
});