admin.app.factory('oAuthClient', ['$q', 'apiHelper', function ($q, apiHelper) {
    var OAuthClient = {};

    OAuthClient.getAccessToken = function (clientId, clientSecret) {
        var deferred = $q.defer();

        apiHelper.post('/uas/oauth/accesstoken', {clientId: clientId, clientSecret: clientSecret}).then(
            function (data) {
                if (data.access_token && data.refresh_token) {
                    deferred.resolve(data);
                } else {
                    deferred.reject(new AuthenticateFailedException());
                }
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

    OAuthClient.refreshAccessToken = function (access_token, refresh_token) {
        var deferred = $q.defer();

        apiHelper.post('/uas/oauth/refresh', {
            refresh_token: refresh_token,
            access_token: access_token
        }).then(
            function (data) {
                if (data.access_token && data.refresh_token) {
                    deferred.resolve(data.access_token);
                } else {
                    deferred.reject(new AuthorizeFailedException());
                }
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

    OAuthClient.signOut = function (access_token) {
        var url = apiHelper.addParameterToURL('/uas/oauth/signout', "access_token", access_token);
        apiHelper.post(url, null);
    };

    return OAuthClient;
}]);