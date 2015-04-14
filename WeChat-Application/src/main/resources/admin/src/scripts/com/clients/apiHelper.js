admin.app.factory('apiHelper', ['$http', '$window', '$q', '$timeout', function ($http, $window, $q, $timeout) {
    var ApiHelper = {};

    ApiHelper.get = function (url) {
        var deferred = $q.defer();
        $timeout(function () {
            deferred.reject(new TimeOutException());
        }, 3000);

        $http.get(url)
            .success(function (data) {
                deferred.resolve(data);
            })
            .error(function (data, status, headers, config) {
                if (status === 404) {
                    deferred.reject(new BadNetworkException());
                } else {
                    deferred.reject(status);
                }
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
                deferred.resolve(data);
            })
            .error(function (data, status, headers, config) {
                if (status === 404) {
                    deferred.reject(new BadNetworkException());
                } else {
                    deferred.reject(status);
                }
            });

        return deferred.promise;
    };

    ApiHelper.addParameterToURL = function (url, key, value) {
        key = encodeURI(key);
        value = encodeURI(value);

        var param = key + "=" + value;
        url += (url.split('?')[1] ? '&' : '?') + param;
        return url;
    };

    return ApiHelper;
}]);