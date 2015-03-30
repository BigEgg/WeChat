admin.app.controller('AppCtrl', ['$scope', '$location', 'oAuthSrv', function ($scope, $location, oAuthSrv) {
    $scope.status = {};
    $scope.status.logging = false;

    $scope.isLoggedIn = function () {
        return oAuthSrv.isLoggedIn();
    };

    $scope.signIn = function (username, password) {
        $scope.status.logging = true;
        try {
            oAuthSrv.signIn(username, password).then(function (name) {
                $scope.name = name;
                $scope.status.logging = false;
            });
        } catch (e) {
            $scope.status.logging = false;
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