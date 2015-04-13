admin.app.service('oAuthSrv', ['$q', 'oAuthHelper', 'apiHelper', function ($q, oAuthHelper, apiHelper) {
    this.isLoggedIn = function () {
        return oAuthHelper.getAccessToken() && oAuthHelper.getRefreshToken();
    };

    this.signIn = function (username, password) {
        var deferred = $q.defer();

        apiHelper.post('/uas/oauth/accesstoken', {clientId: username, clientSecret: password}).then(
            function (data, status, headers, config) {
                oAuthHelper.setAccessToken(data.access_token);
                oAuthHelper.setRefreshToken(data.refresh_token);

                var name = username.split('@')[0];
                oAuthHelper.setUsername(name);

                deferred.resolve(name);
            },
            function (ex) {
                if (ex instanceof Error) {
                    deferred.reject(ex);
                }

                oAuthHelper.clearData()
                deferred.reject(new AuthorizeFailedException());
            }
        );

        return deferred.promise;
    };

    this.signOut = function () {
        oAuthHelper.clearData();
    };

    this.getUsername = function () {
        return oAuthHelper.getUsername();
    }
}]);