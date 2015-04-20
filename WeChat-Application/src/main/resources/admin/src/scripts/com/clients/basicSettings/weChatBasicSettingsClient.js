admin.app.factory('weChatBasicSettingsClient', ['$q', 'oAuthApiHelper', function ($q, oAuthApiHelper) {
    var WeChatBasicSettingsClient = {};

    WeChatBasicSettingsClient.getServerStatus = function () {
        var deferred = $q.defer();

        oAuthApiHelper.get('/api/admin/wechat/server').then(
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

    WeChatBasicSettingsClient.getDeveloperInfo = function () {
        var deferred = $q.defer();

        oAuthApiHelper.get('/api/admin/wechat/developer').then(
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

    return WeChatBasicSettingsClient;
}]);