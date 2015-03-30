"use strict";
var admin = admin || {};
admin.app = angular.module('adminApp', ['ngRoute', 'pascalprecht.translate']);

admin.app.run(function ($rootScope, $location, oAuthSrv, $route) {
    $rootScope.$on("$locationChangeStart", function (event, next, current) {
        var publicAccess = next && next.publicAccess;

        if (!publicAccess && !oAuthSrv.isLoggedIn()) {
            $location.path("/");
        }
    });
});