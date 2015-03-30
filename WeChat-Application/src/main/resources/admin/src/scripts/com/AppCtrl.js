admin.app.controller('AppCtrl', ['$scope', '$location', 'oAuthSrv', function ($scope, $location, oAuthSrv) {
        $scope.isLoggedIn = function () {
            return oAuthSrv.isLoggedIn();
        };

        $scope.logIn = function (username, password) {
            var name = oAuthSrv.logIn(username, password);
            if (name) {
                $scope.username = name;
            }
        };

        $scope.signOut = function () {
            oAuthSrv.signOut();
            $location.path('/');
        };
    }]
);