var admin = admin || {};
admin.app = angular.module('adminApp', ['ngRoute', 'pascalprecht.translate']);

admin.app.run(function ($rootScope, $location, oAuth, $route) {
    $rootScope.$on("$locationChangeStart", function (event, next, current) {
        var publicAccess = next && next.publicAccess;

        if (!publicAccess && !oAuth.isLoggedIn()) {
            $location.path("/");
        }
    });
});