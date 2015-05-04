var WeChatBasicSettingsPage = function () {
    var PAGE_URL = 'http://localhost:3000/admin/html/index.html#/basicsettings/wechat';

    var LOCATOR_SERVER_STATUS = 'serverStatus';
    var LOCATOR_API_STATUS = 'apiStatus';
    var LOCATOR_APP_ID_LABEL = 'labelAppId';
    var LOCATOR_APP_ID_INPUT = 'inputAppId';
    var LOCATOR_APP_SECRET_LABEL = 'labelAppSecret';
    var LOCATOR_APP_SECRET_INPUT = 'inputAppSecret';
    var LOCATOR_EDIT_DEVELOPER_INFO_BUTTON = 'editDeveloperInfoBtn';
    var LOCATOR_CANCEL_DEVELOPER_INFO_BUTTON = 'cancelDeveloperInfoBtn';
    var LOCATOR_SAVE_DEVELOPER_INFO_BUTTON = 'saveDeveloperInfoBtn';

    var Actions = function () {
        this.navigate = function () {
            browser.get(PAGE_URL);
        };
        this.fillDeveloperInfo = function (app_id, app_secret) {
            var appIdInput = element(by.id(LOCATOR_APP_ID_INPUT));
            var appSecretInput = element(by.id(LOCATOR_APP_SECRET_INPUT));

            appIdInput.clear();
            appSecretInput.clear();
            appIdInput.sendKeys(app_id);
            appSecretInput.sendKeys(app_secret);
        };
        this.clickStartEditDeveloperInfo = function () {
            element(by.id(LOCATOR_EDIT_DEVELOPER_INFO_BUTTON)).click();
        };
        this.clickCancelEditDeveloperInfo = function () {
            element(by.id(LOCATOR_CANCEL_DEVELOPER_INFO_BUTTON)).click();
        };
        this.clickSaveEditDeveloperInfo = function () {
            element(by.id(LOCATOR_SAVE_DEVELOPER_INFO_BUTTON)).click();
        };
    };

    var Status = function () {
        this.isInThisPage = function () {
            return browser.getLocationAbsUrl().then(function (url) {
                return url === '/basicsettings/wechat';
            });
        };
        this.isEditingDeveloperInfo = function () {
            return element(by.id(LOCATOR_APP_ID_INPUT)).isPresent() &&
                element(by.id(LOCATOR_APP_SECRET_INPUT)).isPresent() &&
                element(by.id(LOCATOR_CANCEL_DEVELOPER_INFO_BUTTON)).isPresent() &&
                element(by.id(LOCATOR_SAVE_DEVELOPER_INFO_BUTTON)).isPresent();
        };
        this.isCancelDeveloperInfoButtonDisabled = function () {
            return element(by.id(LOCATOR_CANCEL_DEVELOPER_INFO_BUTTON)).getAttribute('class').then(function (classes) {
                return classes.indexOf('disabled') > -1;
            });
        };
        this.isSaveDeveloperInfoButtonDisabled = function () {
            return element(by.id(LOCATOR_SAVE_DEVELOPER_INFO_BUTTON)).getAttribute('class').then(function (classes) {
                return classes.indexOf('disabled') > -1;
            });
        };
    };

    var Properties = function () {
        this.getAppIdInputValue = function () {
            return element(by.id(LOCATOR_APP_ID_INPUT)).getAttribute('value').then(function (value) {
                return value;
            });
        };
        this.getAppSecretInputValue = function () {
            return element(by.id(LOCATOR_APP_SECRET_INPUT)).getAttribute('value').then(function (value) {
                return value;
            });
        };
        this.getAppIdLabelValue = function () {
            return element(by.id(LOCATOR_APP_ID_LABEL)).getText();
        };
        this.getAppSecretLabelValue = function () {
            return element(by.id(LOCATOR_APP_SECRET_LABEL)).getText();
        };
        this.getServerStatus = function () {
            return element(by.id(LOCATOR_SERVER_STATUS)).getAttribute('class').then(function (classes) {
                console.log("server" + classes);
                if (classes.indexOf('fui-check-circle status-ok') > -1) {
                    return 'ok';
                } else if (classes.indexOf('fui-cross-circle status-failed') > -1) {
                    return 'failed';
                } else {
                    return 'unknown';
                }
            });
        };
        this.getAPIStatus = function () {
            return element(by.id(LOCATOR_API_STATUS)).getAttribute('class').then(function (classes) {
                console.log("api" + classes);
                if (classes.indexOf('fui-check-circle status-ok') > -1) {
                    return 'ok';
                } else if (classes.indexOf('fui-cross-circle status-failed') > -1) {
                    return 'failed';
                } else {
                    return 'unknown';
                }
            });
        };
    };

    this.actions = new Actions();
    this.status = new Status();
    this.properties = new Properties();
};

module.exports = new WeChatBasicSettingsPage();