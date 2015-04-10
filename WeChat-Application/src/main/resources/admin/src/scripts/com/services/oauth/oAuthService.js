admin.app.service('oAuthSrv', ['$window', '$q', 'apiHelper', function ($window, $q, apiHelper) {
    var KEY_ACCESS_TOKEN = "access_token";
    var KEY_REFRESH_TOKEN = "refresh_token";
    var KEY_USERNAME = "username";

    this.isLoggedIn = function () {
        return $window.sessionStorage.getItem(KEY_ACCESS_TOKEN) && $window.sessionStorage.getItem(KEY_REFRESH_TOKEN);
    };

    this.signIn = function (username, password) {
        var deferred = $q.defer();

        apiHelper.post('/uas/oauth/accesstoken', {clientId: username, clientSecret: password}).then(
            function (data, status, headers, config) {
                $window.sessionStorage.setItem(KEY_ACCESS_TOKEN, data.access_token);
                $window.sessionStorage.setItem(KEY_REFRESH_TOKEN, data.refresh_token);
                var name = username.split('@')[0];
                $window.sessionStorage.setItem(KEY_USERNAME, name);

                deferred.resolve(name);
            },
            function (ex) {
                if (ex instanceof TimeOutException) {
                    deferred.reject(ex);
                }

                $window.sessionStorage.removeItem(KEY_ACCESS_TOKEN);
                $window.sessionStorage.removeItem(KEY_REFRESH_TOKEN);
                $window.sessionStorage.removeItem(KEY_USERNAME);

                if (ex === 404) {
                    deferred.reject(new BadNetworkException());
                } else {
                    deferred.reject(new AuthorizeFailedException());
                }
            }
        );

        return deferred.promise;
    };

    this.signOut = function () {
        $window.sessionStorage.removeItem(KEY_ACCESS_TOKEN);
        $window.sessionStorage.removeItem(KEY_REFRESH_TOKEN);
        $window.sessionStorage.removeItem(KEY_USERNAME);
    };

    this.getUsername = function () {
        return $window.sessionStorage.getItem(KEY_USERNAME) || '';
    }
}]);