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

                if (name instanceof AuthorizeFailedException) {
                    notify.warning('oauth.signIn.failed');
                } else if (name instanceof SystemBadNetworkException) {
                    notify.danger(e.message);
                } else {
                    $scope.name = name;
                }
            });
    };

    $scope.signOut = function () {
        oAuthSrv.signOut();
        $location.path('/');
    };
}])
;