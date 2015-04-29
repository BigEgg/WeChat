admin.app.service('weChatBasicSettingsSrv', ['$q', 'weChatBasicSettingsClient', function ($q, weChatBasicSettingsClient) {
    this.getServerStatus = function () {
        var deferred = $q.defer();

        weChatBasicSettingsClient.getServerStatus().then(
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

    this.getDeveloperInfo = function () {
        var deferred = $q.defer();

        weChatBasicSettingsClient.getDeveloperInfo().then(
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

    this.setDeveloperInfo = function (app_id, app_secret) {
        var deferred = $q.defer();

        weChatBasicSettingsClient.setDeveloperInfo(app_id, app_secret).then(
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