describe('When navigate to WeChat Basic Settings page', function () {
    var weChatBasicSettingsPage = require('../../pages/basicSettings/WeChatBasicSettingsPage.js');

    beforeEach(function () {
        var constants = require('../../constants.js');
        var homepage = require('../../pages/Homepage.js');

        homepage.actions.navigate();
        homepage.actions.fillSignInInfo(constants.LOGIN_USERNAME, constants.LOGIN_PASSWORD);
        homepage.actions.signIn();

        weChatBasicSettingsPage.actions.navigate();
    });

    it('after open the page', function () {
        expect(weChatBasicSettingsPage.properties.getAppIdInputValue()).toBe('');
        expect(weChatBasicSettingsPage.properties.getAppSecretInputValue()).toBe('');
        expect(weChatBasicSettingsPage.status.isInThisPage()).toBeTruthy();
        expect(weChatBasicSettingsPage.status.isEditingDeveloperInfo()).toBeTruthy();
        expect(weChatBasicSettingsPage.status.isCancelDeveloperInfoButtonDisabled()).toBeTruthy();
        expect(weChatBasicSettingsPage.status.isSaveDeveloperInfoButtonDisabled()).toBeTruthy();
    });

    describe('for developer info', function () {
        it('input should have 32 max-length', function () {
            weChatBasicSettingsPage.actions.fillDeveloperInfo(
                'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz',
                'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz'
            );

            expect(weChatBasicSettingsPage.properties.getAppIdInputValue()).toBe('abcdefghijklmnopqrstuvwxyzabcdef');
            expect(weChatBasicSettingsPage.properties.getAppSecretInputValue()).toBe('abcdefghijklmnopqrstuvwxyzabcdef');
        });

        it('can save new developer info', function () {
            weChatBasicSettingsPage.actions.fillDeveloperInfo('new_app_id', 'new_app_secret');
            expect(weChatBasicSettingsPage.status.isCancelDeveloperInfoButtonDisabled()).toBeTruthy();
            expect(weChatBasicSettingsPage.status.isSaveDeveloperInfoButtonDisabled()).toBeFalsy();

            weChatBasicSettingsPage.actions.clickSaveEditDeveloperInfo();
            expect(weChatBasicSettingsPage.status.isEditingDeveloperInfo()).toBeFalsy();
            expect(weChatBasicSettingsPage.properties.getAppIdLabelValue()).toBe('new_app_id');
            expect(weChatBasicSettingsPage.properties.getAppSecretLabelValue()).toBe('new_app_secret');

            weChatBasicSettingsPage.actions.clickStartEditDeveloperInfo();
            expect(weChatBasicSettingsPage.status.isEditingDeveloperInfo()).toBeTruthy();
            expect(weChatBasicSettingsPage.properties.getAppIdInputValue()).toBe('new_app_id');
            expect(weChatBasicSettingsPage.properties.getAppSecretInputValue()).toBe('new_app_secret');

            weChatBasicSettingsPage.actions.fillDeveloperInfo('', '');
            expect(weChatBasicSettingsPage.status.isSaveDeveloperInfoButtonDisabled()).toBeTruthy();

            weChatBasicSettingsPage.actions.fillDeveloperInfo('app_id', 'app_secret');
            weChatBasicSettingsPage.actions.clickSaveEditDeveloperInfo();
            expect(weChatBasicSettingsPage.status.isEditingDeveloperInfo()).toBeFalsy();
            expect(weChatBasicSettingsPage.properties.getAppIdLabelValue()).toBe('app_id');
            expect(weChatBasicSettingsPage.properties.getAppSecretLabelValue()).toBe('app_secret');
        });

        it('can cancel editing if already have developer info', function () {
            weChatBasicSettingsPage.actions.fillDeveloperInfo('new_app_id', 'new_app_secret');
            expect(weChatBasicSettingsPage.status.isCancelDeveloperInfoButtonDisabled()).toBeTruthy();
            expect(weChatBasicSettingsPage.status.isSaveDeveloperInfoButtonDisabled()).toBeFalsy();

            weChatBasicSettingsPage.actions.clickSaveEditDeveloperInfo();
            weChatBasicSettingsPage.actions.clickStartEditDeveloperInfo();
            expect(weChatBasicSettingsPage.status.isCancelDeveloperInfoButtonDisabled()).toBeFalsy();

            weChatBasicSettingsPage.actions.fillDeveloperInfo('app_id', 'app_secret');
            weChatBasicSettingsPage.actions.clickCancelEditDeveloperInfo();
            expect(weChatBasicSettingsPage.status.isEditingDeveloperInfo()).toBeFalsy();
            expect(weChatBasicSettingsPage.properties.getAppIdLabelValue()).toBe('new_app_id');
            expect(weChatBasicSettingsPage.properties.getAppSecretLabelValue()).toBe('new_app_secret');
        });
    });
});