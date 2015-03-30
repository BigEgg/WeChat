admin.app.controller('AppCtrl', ['$scope', '$location', 'oAuthSrv', function ($scope, $location, oAuthSrv) {
    $scope.status = {};
    $scope.status.logging = false;

    $scope.isLoggedIn = function () {
        return oAuthSrv.isLoggedIn();
    };

    $scope.logIn = function (username, password) {
        $scope.status.logging = true;
        try {
            $scope.name = oAuthSrv.logIn(username, password);
        } catch (e) {
            if (e instanceof AuthorizeFailedException) {
                notify.warning('oauth.login.failed');
            } else if (e instanceof SystemBadNetworkException) {
                notify.danger(e.message);
            } else {
                notify.danger('error.unknown');
            }
        }
    };

    $scope.signOut = function () {
        oAuthSrv.signOut();
        $location.path('/');
    };
}]);