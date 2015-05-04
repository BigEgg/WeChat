admin.app.controller('SystemMessageBasicSettingsCtrl', ['$scope', function ($scope) {
    $scope.modifyingMessage = 'Subscribe';

    $scope.modifyMessage = function (item) {
        $scope.modifyingMessage = item;
    };
}]);