admin.app.service('oAuthSrv', ['$window', '$http', function ($window, $http) {
    var KEY_ACCESS_TOKEN = "access_token";
    var KEY_REFRESH_TOKEN = "refresh_token";
    var KEY_USER_NAME = "user_name";

    this.isLoggedIn = function () {
        return $window.sessionStorage.getItem(KEY_ACCESS_TOKEN) && $window.sessionStorage.getItem(KEY_REFRESH_TOKEN);
    };

    this.signIn = function (username, password) {
        return $http.post('/api/oauth/admin', {username: username, password: password}).then(
            function (response) {
                var data = response.data;

                $window.sessionStorage.setItem(KEY_ACCESS_TOKEN, data.access_token);
                $window.sessionStorage.setItem(KEY_REFRESH_TOKEN, data.refresh_token);
                return data.name;
            },
            function (response) {
                if (response.status === 404) {
                    return new SystemBadNetworkException();
                } else {
                    return new AuthorizeFailedException();
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