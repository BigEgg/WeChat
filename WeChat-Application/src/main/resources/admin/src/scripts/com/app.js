var admin = admin || {};
admin.app = angular.module('adminApp', ['ngRoute', 'pascalprecht.translate']);

admin.app.controller('AppCtrl', ['$scope', 'oAuth', function ($scope, oAuth) {
        $scope.oAuth = oAuth;
    }]
);