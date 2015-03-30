admin.app.controller('AppCtrl', ['$scope', '$location', 'OAuthSrv', function ($scope, $location, OAuthSrv) {
        $scope.isLoggedIn = function () {
            return OAuthSrv.isLoggedIn();
        };

        $scope.logIn = function (username, password) {
            var name = OAuthSrv.logIn(username, password);
            if (name) {
                $scope.username = name;
            }
        };

        $scope.signOut = function () {
            OAuthSrv.signOut();
            $location.path('/');
        };
    }]
);