admin.app.factory('OAuthSrv', ['$window', '$http', 'notify', function ($window, $http, notify) {
    var KEY_ACCESS_TOKEN = "access_token";
    var KEY_REFRESH_TOKEN = "refresh_token";

    var oAuth = {};
    oAuth.isLoggedIn = function () {
        return $window.sessionStorage.getItem(KEY_ACCESS_TOKEN) && $window.sessionStorage.getItem(KEY_REFRESH_TOKEN);
    };
    oAuth.logIn = function (username, password) {
        $http.post('/api/oauth/admin', {username: username, password: password}).
            success(function (data, status, headers, config) {
                $window.sessionStorage.setItem(KEY_ACCESS_TOKEN, data.access_token);
                $window.sessionStorage.setItem(KEY_REFRESH_TOKEN, data.refresh_token);
                return data.name;
            }).
            error(function (data, status, headers, config) {
                if (status === 404) {
                    notify.danger('system.bad.network');
                } else {
                    notify.warning('oauth.login.failed');
                }
                return ""
            });
    };
    oAuth.signOut = function () {
        $window.sessionStorage.removeItem(KEY_ACCESS_TOKEN);
        $window.sessionStorage.removeItem(KEY_REFRESH_TOKEN);
    };


    return oAuth;
}]);