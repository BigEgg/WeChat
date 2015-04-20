admin.app.controller('BasicSettingsWeChatCtrl', ['$scope', 'notify', 'weChatBasicSettingsSrv', function ($scope, notify, weChatBasicSettingsSrv) {
    $scope.weChatDeveloperInfo = {};
    $scope.weChatDeveloperInfo.appId = '';
    $scope.weChatDeveloperInfo.appSecret = '';

    $scope.weChatServerStatus = {};
    $scope.weChatServerStatus.entryPoint = '';
    $scope.weChatServerStatus.appToken = '';
    $scope.weChatServerStatus.connectionStatus = false;

    $scope.status = {};
    $scope.status.loading = true;
    $scope.status.statusGetting = false;

    $scope.getServerStatus = function () {
        $scope.status.statusGetting = true;
        weChatBasicSettingsSrv.getServerStatus().then(
            function (data) {
                $scope.status.statusGetting = false;
                $scope.weChatServerStatus.entryPoint = data.entry_point;
                $scope.weChatServerStatus.appToken = data.token;
                $scope.weChatServerStatus.connectionStatus = data.connection;
            },
            function (error) {
                $scope.status.statusGetting = false;
                if (error instanceof AuthenticateFailedException) {
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
                $scope.weChatDeveloperInfo.appId = data.app_id;
                $scope.weChatDeveloperInfo.appSecret = data.app_secret;

                $scope.status.loading = false;
                $scope.getServerStatus();
            },
            function (error) {
                if (error instanceof AuthenticateFailedException) {
                    notify.danger(error.message);
                }
                else {
                    notify.warning(error.message);
                }
            }
        );
    };

    init();
}]);