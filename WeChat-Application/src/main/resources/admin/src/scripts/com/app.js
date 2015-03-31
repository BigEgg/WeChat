"use strict";
var admin = admin || {};
admin.app = angular.module('adminApp', ['ngRoute', 'pascalprecht.translate']);

admin.app.run(function ($rootScope, $location, oAuthSrv, $route, notify) {
    $rootScope.$on('$routeChangeStart', function (event, next, current) {
        var publicAccess = next && next.publicAccess;

        if (!publicAccess && !oAuthSrv.isLoggedIn()) {
            $location.path('/');
            notify.danger('error.route.unAuthorize');
        }
        if (oAuthSrv.isLoggedIn() && next.templateUrl === '../html/views/home.html') {
            $location.path('/dashboard');
        }
    });
});