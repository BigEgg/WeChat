describe('After logged in', function () {
    var loggedInHomepage = require('../pages/LoggedInHomepage.js');
    var dashboardPage = require('../pages/dashboard/DashboardPage.js');

    beforeEach(function () {
        var constants = require('../constants.js');
        var homepage = require('../pages/Homepage.js');

        homepage.actions.fillSignInInfo(constants.LOGIN_USERNAME, constants.LOGIN_PASSWORD);
        homepage.actions.signIn();
    });
});