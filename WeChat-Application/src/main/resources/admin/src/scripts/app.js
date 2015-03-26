var admin = admin || {};
admin.app = angular.module('adminApp', ['pascalprecht.translate']);

admin.app.controller('AppController', ['$scope', 'oAuth', function ($scope, oAuth) {
        $scope.oAuth = oAuth;
    }]
);