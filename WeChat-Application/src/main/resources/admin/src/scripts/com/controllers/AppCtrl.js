admin.app.controller('AppCtrl', ['$scope', '$rootScope', '$location', 'oAuthSrv', 'notify', function ($scope, $rootScope, $location, oAuthSrv, notify) {
    $scope.status = $scope.status || {};
    $scope.status.logging = false;

    $scope.isLoggedIn = function () {
        return oAuthSrv.isLoggedIn();
    };

    $scope.signIn = function (username, password) {
        if (!username) {
            return;
        }

        $scope.status.logging = true;
        oAuthSrv.signIn(username, password).then(
            function (name) {
                $scope.status.logging = false;
                $location.path('/dashboard')
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

    $scope.getLoginUsername = function () {
        return oAuthSrv.getUsername();
    }
}]);