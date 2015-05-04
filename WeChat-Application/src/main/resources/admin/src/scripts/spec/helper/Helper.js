var ignoreTranslate = function ($provide, $translateProvider) {
    $provide.factory('customLoader', function ($q) {
        return function () {
            var deferred = $q.defer();
            deferred.resolve({});
            return deferred.promise;
        };
    });
    $translateProvider.useLoader('customLoader');
};

var ignoreRoute = function ($provide) {
    $provide.factory('$location', function () {
        return {
            path: function (url) {
            }
        };
    });
};