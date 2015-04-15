admin.app.factory('oAuthRepository', ['$window', function ($window) {
    var KEY_ACCESS_TOKEN = "access_token";
    var KEY_REFRESH_TOKEN = "refresh_token";
    var KEY_USERNAME = "username";
    var oAuthRepository = {};

    oAuthRepository.getAccessToken = function () {
        return $window.sessionStorage.getItem(KEY_ACCESS_TOKEN) || '';
    };

    oAuthRepository.setAccessToken = function (accessToken) {
        $window.sessionStorage.setItem(KEY_ACCESS_TOKEN, accessToken);
    };

    oAuthRepository.getRefreshToken = function () {
        return $window.sessionStorage.getItem(KEY_REFRESH_TOKEN) || '';
    };

    oAuthRepository.setRefreshToken = function (refreshToken) {
        $window.sessionStorage.setItem(KEY_REFRESH_TOKEN, refreshToken);
    };

    oAuthRepository.getUsername = function () {
        return $window.sessionStorage.getItem(KEY_USERNAME) || '';
    };

    oAuthRepository.setUsername = function (username) {
        return $window.sessionStorage.setItem(KEY_USERNAME, username);
    };

    oAuthRepository.clearData = function () {
        $window.sessionStorage.removeItem(KEY_ACCESS_TOKEN);
        $window.sessionStorage.removeItem(KEY_REFRESH_TOKEN);
        $window.sessionStorage.removeItem(KEY_USERNAME);
    };

    return oAuthRepository;
}]);