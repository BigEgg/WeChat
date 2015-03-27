admin.app.factory('oAuth', ['$window', '$http', function ($window, $http) {
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
            }).
            error(function (data, status, headers, config) {

            });
    };
    oAuth.signOut = function () {
        $window.sessionStorage.removeItem(KEY_ACCESS_TOKEN);
        $window.sessionStorage.removeItem(KEY_REFRESH_TOKEN);
    };


    return oAuth;
}]);