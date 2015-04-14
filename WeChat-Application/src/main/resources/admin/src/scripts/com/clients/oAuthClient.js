admin.app.factory('oAuthClient', ['$q', 'apiHelper', function ($q, apiHelper) {
    var OAuthClient = {};

    OAuthClient.getAccessToken = function (clientId, clientSecret) {
        var deferred = $q.defer();

        apiHelper.post('/uas/oauth/accesstoken', {clientId: clientId, clientSecret: clientSecret}).then(
            function (data) {
                deferred.resolve(data);
            },
            function (error) {
                if (error === 401) {
                    deferred.reject(new AuthorizeFailedException());
                } else if (error instanceof Error) {
                    deferred.reject(error);
                } else {
                    deferred.reject(new UnknownException());
                }
            }
        );

        return deferred.promise;
    };

    OAuthClient.refreshAccessToken = function (access_token, refresh_token) {
        var deferred = $q.defer();

        apiHelper.post('/uas/oauth/refresh', {
            refresh_token: refresh_token,
            access_token: access_token
        }).then(
            function (data) {
                deferred.resolve(data.access_token);
            },
            function (error) {
                if (error === 403) {
                    deferred.reject(new AuthenticateFailedException());
                } else if (error instanceof Error) {
                    deferred.reject(error);
                } else {
                    deferred.reject(new UnknownException());
                }
            }
        );

        return deferred.promise;
    };

    return OAuthClient;
}]);