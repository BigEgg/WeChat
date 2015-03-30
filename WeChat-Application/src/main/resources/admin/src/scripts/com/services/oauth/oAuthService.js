admin.app.service('oAuthSrv', ['$window', '$http', function ($window, $http) {
    var KEY_ACCESS_TOKEN = "access_token";
    var KEY_REFRESH_TOKEN = "refresh_token";
    var KEY_USER_NAME = "user_name";

    this.isLoggedIn = function () {
        return $window.sessionStorage.getItem(KEY_ACCESS_TOKEN) && $window.sessionStorage.getItem(KEY_REFRESH_TOKEN);
    };

    this.signIn = function (username, password) {
        $http.post('/api/oauth/admin', {username: username, password: password})
            .success(function (data, status, headers, config) {
                $window.sessionStorage.setItem(KEY_ACCESS_TOKEN, data.access_token);
                $window.sessionStorage.setItem(KEY_REFRESH_TOKEN, data.refresh_token);
                $window.sessionStorage.setItem(KEY_USER_NAME, data.name);
            })
            .error(function (data, status, headers, config) {
                if (status === 404) {
                    throw new SystemBadNetworkException();
                } else {
                    throw new AuthorizeFailedException();
                }
            });
    };

    this.getUsername = function () {
        return $window.sessionStorage.getItem(KEY_USER_NAME);
    };

    this.signOut = function () {
        $window.sessionStorage.removeItem(KEY_ACCESS_TOKEN);
        $window.sessionStorage.removeItem(KEY_REFRESH_TOKEN);
    };
}]);