admin.app.factory('apiHelper', ['$http', '$window', '$q', '$timeout', function ($http, $window, $q, $timeout) {
    var ApiHelper = {};

    ApiHelper.get = function (url) {
        var deferred = $q.defer();
        $timeout(function () {
            deferred.reject(new TimeOutException());
        }, 3000);

        $http.get(url)
            .success(function (data, status, headers, config) {
                deferred.resolve(data, status, headers, config);
            })
            .error(function (data, status, headers, config) {
                deferred.reject(data, status, headers, config);
            });

        return deferred.promise;
    };

    ApiHelper.post = function (url, data) {
        var deferred = $q.defer();
        $timeout(function () {
            deferred.reject(new TimeOutException());
        }, 3000);

        $http.post(url, data)
            .success(function (data, status, headers, config) {
                deferred.resolve(data, status, headers, config);
            })
            .error(function (data, status, headers, config) {
                deferred.reject(status);
            });

        return deferred.promise;
    };

    return ApiHelper;
}]);