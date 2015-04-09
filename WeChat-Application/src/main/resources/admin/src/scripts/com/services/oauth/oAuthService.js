admin.app.service('oAuthSrv', ['$window', '$http', '$q', '$timeout', function ($window, $http, $q, $timeout) {
    var KEY_ACCESS_TOKEN = "access_token";
    var KEY_REFRESH_TOKEN = "refresh_token";
    var KEY_USERNAME = "username";

    this.isLoggedIn = function () {
        return $window.sessionStorage.getItem(KEY_ACCESS_TOKEN) && $window.sessionStorage.getItem(KEY_REFRESH_TOKEN);
    };

    this.signIn = function (username, password) {
        var deferred = $q.defer();
        $timeout(function () {
            deferred.reject(new TimeOutException());
        }, 3000);

        $http.post('/uas/oauth/accesstoken', {clientId: username, clientSecret: password})
            .success(function (data, status, headers, config) {
                $window.sessionStorage.setItem(KEY_ACCESS_TOKEN, data.access_token);
                $window.sessionStorage.setItem(KEY_REFRESH_TOKEN, data.refresh_token);
                var name = username.split('@')[0];
                $window.sessionStorage.setItem(KEY_USERNAME, name);

                deferred.resolve(name);
            })
            .error(function (data, status, headers, config) {
                $window.sessionStorage.removeItem(KEY_ACCESS_TOKEN);
                $window.sessionStorage.removeItem(KEY_REFRESH_TOKEN);
                $window.sessionStorage.removeItem(KEY_USERNAME);

                if (status === 404) {
                    deferred.reject(new BadNetworkException());
                } else {
                    deferred.reject(new AuthorizeFailedException());
                }
            });

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