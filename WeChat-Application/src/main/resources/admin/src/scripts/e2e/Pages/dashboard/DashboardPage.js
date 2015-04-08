var constants = require('../../constants.js');

var DashboardPage = function () {
    var PAGE_URL = 'http://localhost:3000/admin/html/index.html#/dashboard';

    var Actions = function () {
        this.navigate = function () {
            browser.get(PAGE_URL);
        };
    };

    var Status = function () {
        this.isInThisPage = function () {
            return browser.getLocationAbsUrl().then(function (url) {
                return url === '/dashboard';
            });
        };
    };

    var Properties = function () {

    };

    this.actions = new Actions();
    this.status = new Status();
    this.properties = new Properties();
}

module.exports = new DashboardPage();