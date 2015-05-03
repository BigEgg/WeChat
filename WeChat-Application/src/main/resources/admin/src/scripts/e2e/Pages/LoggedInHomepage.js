var LoggedInHomepage = function () {
    var LOCATOR_MENU_DASHBOARD = 'menu_dashboard';
    var LOCATOR_MENU_DASHBOARD_GENERAL = 'menu_dashboard_general';

    var LOCATOR_MENU_BASIC_SETTINGS = 'menu_basic_settings';
    var LOCATOR_MENU_BASIC_SETTINGS_WECHAT = 'menu_basic_settings_wechat';
    var LOCATOR_MENU_BASIC_SETTINGS_SYSTEM_MESSAGE = 'menu_basic_settings_system_message';

    var LOCATOR_MENU_USER = 'menu_user';
    var LOCATOR_MENU_USER_USERNAME = 'menu_user_username';
    var LOCATOR_MENU_USER_SIGN_OUT = 'menu_user_signOut';

    var Actions = function () {
        this.clickWeChatBasicSettingsMenu = function () {
            element(by.id(LOCATOR_MENU_BASIC_SETTINGS)).click();
            element(by.id(LOCATOR_MENU_BASIC_SETTINGS_WECHAT)).click();
        };
        this.clickSystemMessageSettingsMenu = function () {
            element(by.id(LOCATOR_MENU_BASIC_SETTINGS)).click();
            element(by.id(LOCATOR_MENU_BASIC_SETTINGS_SYSTEM_MESSAGE)).click();
        };
        this.clickGeneralDashboardMenu = function () {
            element(by.id(LOCATOR_MENU_DASHBOARD)).click();
            element(by.id(LOCATOR_MENU_DASHBOARD_GENERAL)).click();
        }
        this.signOut = function () {
            element(by.id(LOCATOR_MENU_USER)).click();
            element(by.id(LOCATOR_MENU_USER_SIGN_OUT)).click();
        }
    };

    var Status = function () {
        this.isDashboardMenuPresent = function () {
            return element(by.id(LOCATOR_MENU_DASHBOARD)).isPresent()
                && element(by.id(LOCATOR_MENU_DASHBOARD_GENERAL)).isPresent();
        };
        this.isBasicSettingsMenuPresent = function () {
            return element(by.id(LOCATOR_MENU_BASIC_SETTINGS)).isPresent()
                && element(by.id(LOCATOR_MENU_BASIC_SETTINGS_WECHAT)).isPresent();
        };
        this.isUserMenuPresent = function () {
            return element(by.id(LOCATOR_MENU_USER)).isPresent()
                && element(by.id(LOCATOR_MENU_USER_USERNAME)).isPresent()
                && element(by.id(LOCATOR_MENU_USER_SIGN_OUT)).isPresent();
        };
    };

    var Properties = function () {
        this.getLoggedInUsername = function () {
            var usernameElement = element(by.id(LOCATOR_MENU_USER_USERNAME));
            return usernameElement.getText();
        };
    };

    this.actions = new Actions();
    this.status = new Status();
    this.properties = new Properties();
};

module.exports = new LoggedInHomepage();