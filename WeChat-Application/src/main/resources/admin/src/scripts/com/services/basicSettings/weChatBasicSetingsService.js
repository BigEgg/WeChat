admin.app.service('weChatBasicSettingsSrv', ['$q', 'weChatBasicSettingsClient', function ($q, weChatBasicSettingsClient)  {
    this.getWeChatServerStatus = function () {
        var deferred = $q.defer();

        weChatBasicSettingsClient.getWeChatServerStatus().then(
            function (data) {
                deferred.resolve(data);
            },
            function (error) {
                if (error instanceof Error) {
                    deferred.reject(error);
                } else {
                    deferred.reject(new UnknownException());
                }
            }
        );

        return deferred.promise;
    };
}]);