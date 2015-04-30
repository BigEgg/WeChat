admin.app.service('oAuthSrv', ['$q', 'oAuthRepository', 'oAuthClient', function ($q, oAuthRepository, oAuthClient) {
    this.isLoggedIn = function () {
        return oAuthRepository.getAccessToken() && oAuthRepository.getRefreshToken();
    };

    this.signIn = function (username, password) {
        var deferred = $q.defer();

        oAuthClient.getAccessToken(username, password).then(
            function (data) {
                oAuthRepository.setAccessToken(data.access_token);
                oAuthRepository.setRefreshToken(data.refresh_token);

                var name = username.split('@')[0];
                oAuthRepository.setUsername(name);

                deferred.resolve(name);
            },
            function (error) {
                oAuthRepository.clearData();
                deferred.reject(error);
            }
        );

        return deferred.promise;
    };

    this.signOut = function () {
        oAuthClient.signOut(oAuthRepository.getAccessToken());
        oAuthRepository.clearData();
    };

    this.getUsername = function () {
        return oAuthRepository.getUsername();
    };
}]);