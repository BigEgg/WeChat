admin.app.controller('AppCtrl', ['$scope', '$location', 'oAuthSrv', 'notify', function ($scope, $location, oAuthSrv, notify) {
    $scope.status = {};
    $scope.status.logging = false;

    $scope.isLoggedIn = function () {
        return oAuthSrv.isLoggedIn();
    };

    $scope.signIn = function (username, password) {
        $scope.status.logging = true;
        oAuthSrv.signIn(username, password).then(
            function (name) {
                $scope.status.logging = false;
                $scope.name = name;
            },
            function (e) {
                $scope.status.logging = false;
                if (e instanceof AuthorizeFailedException) {
                    notify.warning('oauth.signIn.failed');
                } else if (e instanceof SystemBadNetworkException) {
                    notify.danger(e.message);
                } else {
                    notify.danger('error.unknown');
                }
            });
    };

    $scope.signOut = function () {
        oAuthSrv.signOut();
        $location.path('/');
    };
}])
;