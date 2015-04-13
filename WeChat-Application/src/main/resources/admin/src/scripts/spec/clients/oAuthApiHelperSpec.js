describe('OAuth API Helper Test', function () {
    beforeEach(angular.mock.module('adminApp', function ($provide, $translateProvider) {
        $provide.factory('customLoader', function ($q) {
            return function () {
                var deferred = $q.defer();
                deferred.resolve({});
                return deferred.promise;
            };
        });
        $translateProvider.useLoader('customLoader');

        $provide.factory('$location', function () {
            return {
                path: function (url) {
                }
            }
        });
    }));

    describe('When access token valid', function () {

    });

    describe('when access token invalid', function () {

    });
});