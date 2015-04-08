var LoggedInHomepage = function () {
    var LOCATOR_LOGIN_FORM = 'menu_signin_form';
    var LOCATOR_MENU_DASHBOARD = 'menu_dashboard';
    var LOCATOR_MENU_BASIC_SETTINGS = 'menu_basic_settings';
    var LOCATOR_MENU_USER = 'menu_user';

    var Actions = function () {
    };

    var Status = function () {
        this.isLoggedInMenuPresent = function () {
            var menu_dashboard = element(by.id(LOCATOR_MENU_DASHBOARD));
            var menu_basicSettings = element(by.id(LOCATOR_MENU_BASIC_SETTINGS));
            var menu_user = element(by.id(LOCATOR_MENU_USER));

            return menu_dashboard.isPresent()
                && menu_basicSettings.isPresent()
                && menu_user.isPresent();
        };
    };

    var Properties = function () {
    };

    this.actions = new Actions();
    this.status = new Status();
    this.properties = new Properties();
}

module.exports = new LoggedInHomepage();