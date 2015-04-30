admin.app.controller('WeChatBasicSettingsCtrl', ['$scope', 'notify', 'weChatBasicSettingsSrv', function ($scope, notify, weChatBasicSettingsSrv) {
    $scope.weChatDeveloperInfo = {};
    $scope.weChatDeveloperInfo.isEditing = false;
    $scope.weChatDeveloperInfo.appId = '';
    $scope.weChatDeveloperInfo.appSecret = '';
    $scope.weChatDeveloperInfo.new_appId = '';
    $scope.weChatDeveloperInfo.new_appSecret = '';

    $scope.weChatServerStatus = {};
    $scope.weChatServerStatus.entryPoint = '';
    $scope.weChatServerStatus.appToken = '';
    $scope.weChatServerStatus.connectionStatus = false;

    $scope.status = {};
    $scope.status.loading = true;
    $scope.status.gettingStatus = false;
    $scope.status.savingDeveloperInfo = false;

    $scope.getServerStatus = function () {
        $scope.status.gettingStatus = true;
        weChatBasicSettingsSrv.getServerStatus().then(
            function (data) {
                $scope.status.gettingStatus = false;
                $scope.weChatServerStatus.entryPoint = data.entry_point;
                $scope.weChatServerStatus.appToken = data.token;
                $scope.weChatServerStatus.connectionStatus = data.connected;
            },
            function (error) {
                $scope.status.gettingStatus = false;
                if (error instanceof AuthorizeFailedException) {
                    notify.danger(error.message);
                }
                else {
                    notify.warning(error.message);
                }
            }
        );
    };

    $scope.startEditDeveloperInfo = function () {
        $scope.weChatDeveloperInfo.isEditing = true;
        $scope.weChatDeveloperInfo.new_appId = $scope.weChatDeveloperInfo.appId;
        $scope.weChatDeveloperInfo.new_appSecret = $scope.weChatDeveloperInfo.appSecret;
    };

    $scope.cancelEditDeveloperInfo = function () {
        if (!$scope.weChatDeveloperInfo.appId || !$scope.weChatDeveloperInfo.appSecret) {
            return;
        }

        $scope.weChatDeveloperInfo.isEditing = false;
        $scope.weChatDeveloperInfo.new_appId = '';
        $scope.weChatDeveloperInfo.new_appSecret = '';
    };

    $scope.saveNewDeveloperInfo = function () {
        if (!$scope.weChatDeveloperInfo.isEditing || !$scope.weChatDeveloperInfo.new_appId || !$scope.weChatDeveloperInfo.new_appSecret) {
            return;
        }

        $scope.status.savingDeveloperInfo = true;
        weChatBasicSettingsSrv.setDeveloperInfo($scope.weChatDeveloperInfo.new_appId, $scope.weChatDeveloperInfo.new_appSecret).then(
            function (data) {
                $scope.status.savingDeveloperInfo = false;
                $scope.weChatDeveloperInfo.isEditing = false;
                $scope.weChatDeveloperInfo.appId = data.app_id;
                $scope.weChatDeveloperInfo.appSecret = data.app_secret;
                $scope.weChatDeveloperInfo.new_appId = '';
                $scope.weChatDeveloperInfo.new_appSecret = '';
            },
            function (error) {
                $scope.status.savingDeveloperInfo = false;
                if (error instanceof AuthorizeFailedException) {
                    notify.danger(error.message);
                }
                else {
                    notify.warning(error.message);
                }
            }
        );
    };

    $scope.getNewToken = function () {
        console.log('new token');
    };

    var init = function () {
        weChatBasicSettingsSrv.getDeveloperInfo().then(
            function (data) {
                if (data.app_id && data.app_secret) {
                    $scope.weChatDeveloperInfo.appId = data.app_id;
                    $scope.weChatDeveloperInfo.appSecret = data.app_secret;
                } else {
                    $scope.weChatDeveloperInfo.isEditing = true;
                }

                $scope.status.loading = false;
                $scope.getServerStatus();
            },
            function (error) {
                $scope.status.loading = false;
                if (error instanceof AuthorizeFailedException) {
                    notify.danger(error.message);
                } else {
                    notify.warning(error.message);
                }
            }
        );


    };

    init();
}]);