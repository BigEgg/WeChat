admin.app.factory('oAuth', ['$window', function ($window) {
    var KEY_ACCESS_TOKEN = "access_token";
    var KEY_REFRESH_TOKEN = "refresh_token";

    var oAuth = {};
    oAuth.isLogin = function () {
        return $window.sessionStorage.getItem(KEY_ACCESS_TOKEN) && $window.sessionStorage.getItem(KEY_REFRESH_TOKEN);
    };
    oAuth.login = function(username, password) {

    };


    return oAuth;
}]);