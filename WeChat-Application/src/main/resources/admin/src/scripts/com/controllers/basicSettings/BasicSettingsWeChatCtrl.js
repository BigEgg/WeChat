admin.app.controller('BasicSettingsWeChatCtrl', ['$scope', 'notify', 'weChatBasicSettingsSrv', function ($scope, notify, weChatBasicSettingsSrv) {
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
                $scope.status.loading = false;
                $scope.status.statusGetting = false;
                $scope.weChatServerStatus.entryPoint = data.entry_point;
                $scope.weChatServerStatus.appToken = data.token;
                $scope.weChatServerStatus.connectionStatus = data.connection;
            },
            function (error) {
                $scope.status.loading = false;
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

    $scope.getServerStatus();
}]);