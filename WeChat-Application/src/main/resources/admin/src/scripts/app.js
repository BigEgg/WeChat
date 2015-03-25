var admin = admin || {};
admin.app = angular.module('adminApp', []);

admin.app.controller('AppController', ['$scope', 'i18n', function ($scope, i18n) {
        $scope.i18n = i18n;
    }]
);