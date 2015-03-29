admin.app.controller('AppCtrl', ['$scope', '$location', 'oAuth', function ($scope, $location, oAuth) {
        $scope.isLoggedIn = function () {
            return oAuth.isLoggedIn();
        };

        $scope.logIn = function (username, password) {
            var name = oAuth.logIn(username, password);
            if (name) {
                $scope.username = name;
            }
        };

        $scope.signOut = function () {
            oAuth.signOut();
            $location.path('/');
        };
    }]
);