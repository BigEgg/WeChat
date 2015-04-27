admin.app.factory('oAuthApiHelper', ['$q', 'apiHelper', 'oAuthClient', 'oAuthRepository', function ($q, apiHelper, OAuthClient, oAuthRepository) {
    var addAccessToken = function (url, access_token) {
        return apiHelper.addParameterToURL(url, "access_token", access_token);
    };

    var OAuthApiHelper = {};

    OAuthApiHelper.get = function (url) {
        var deferred = $q.defer();

        var access_token = oAuthRepository.getAccessToken();
        var refresh_token = oAuthRepository.getRefreshToken();
        if (!access_token || !refresh_token) {
            deferred.reject(new AuthenticateFailedException());
            return deferred.promise;
        }

        var urlWithAccessToken = addAccessToken(url, access_token);
        apiHelper.get(urlWithAccessToken).then(
            function (data) {
                deferred.resolve(data);
            },
            function (error) {
                if (error === 500) {
                    OAuthClient.refreshAccessToken(access_token, refresh_token).then(
                        function (new_access_token) {
                            oAuthRepository.setAccessToken(new_access_token);
                            var urlWithNewAccessToken = addAccessToken(url, new_access_token);
                            apiHelper.get(urlWithNewAccessToken).then(
                                function (data) {
                                    deferred.resolve(data);
                                },
                                function (error) {
                                    deferred.reject(new UnknownException());
                                }
                            );
                        },
                        function (error) {
                            oAuthRepository.clearData();
                            deferred.reject(error);
                        }
                    )
                }
                else {
                    deferred.reject(new UnknownException());
                }
            }
        );

        return deferred.promise;
    };

    OAuthApiHelper.post = function (url, data) {
        var deferred = $q.defer();

        var access_token = oAuthRepository.getAccessToken();
        var refresh_token = oAuthRepository.getRefreshToken();
        if (!access_token || !refresh_token) {
            deferred.reject(new AuthenticateFailedException());
            return deferred.promise;
        }

        var urlWithAccessToken = addAccessToken(url, access_token);
        apiHelper.post(urlWithAccessToken, data).then(
            function (data) {
                deferred.resolve(data);
            },
            function (error) {
                if (error === 500) {
                    OAuthClient.refreshAccessToken(access_token, refresh_token).then(
                        function (new_access_token) {
                            var urlWithNewAccessToken = addAccessToken(url, new_access_token);
                            oAuthRepository.setAccessToken(new_access_token);
                            apiHelper.post(urlWithNewAccessToken, data).then(
                                function (data) {
                                    deferred.resolve(data);
                                },
                                function (error) {
                                    deferred.reject(new UnknownException());
                                }
                            );
                        },
                        function (error) {
                            oAuthRepository.clearData();
                            deferred.reject(error);
                        }
                    )
                }
                else {
                    deferred.reject(new UnknownException());
                }
            }
        );

        return deferred.promise;
    };

    OAuthApiHelper.put = function (url, data) {
        var deferred = $q.defer();

        var access_token = oAuthRepository.getAccessToken();
        var refresh_token = oAuthRepository.getRefreshToken();
        if (!access_token || !refresh_token) {
            deferred.reject(new AuthenticateFailedException());
            return deferred.promise;
        }

        var urlWithAccessToken = addAccessToken(url, access_token);
        apiHelper.put(urlWithAccessToken, data).then(
            function (data) {
                deferred.resolve(data);
            },
            function (error) {
                if (error === 500) {
                    OAuthClient.refreshAccessToken(access_token, refresh_token).then(
                        function (new_access_token) {
                            var urlWithNewAccessToken = addAccessToken(url, new_access_token);
                            oAuthRepository.setAccessToken(new_access_token);
                            apiHelper.put(urlWithNewAccessToken, data).then(
                                function (data) {
                                    deferred.resolve(data);
                                },
                                function (error) {
                                    deferred.reject(new UnknownException());
                                }
                            );
                        },
                        function (error) {
                            oAuthRepository.clearData();
                            deferred.reject(error);
                        }
                    )
                }
                else {
                    deferred.reject(new UnknownException());
                }
            }
        );

        return deferred.promise;
    };

    OAuthApiHelper.patch = function (url, data) {
        var deferred = $q.defer();

        var access_token = oAuthRepository.getAccessToken();
        var refresh_token = oAuthRepository.getRefreshToken();
        if (!access_token || !refresh_token) {
            deferred.reject(new AuthenticateFailedException());
            return deferred.promise;
        }

        var urlWithAccessToken = addAccessToken(url, access_token);
        apiHelper.patch(urlWithAccessToken, data).then(
            function (data) {
                deferred.resolve(data);
            },
            function (error) {
                if (error === 500) {
                    OAuthClient.refreshAccessToken(access_token, refresh_token).then(
                        function (new_access_token) {
                            var urlWithNewAccessToken = addAccessToken(url, new_access_token);
                            oAuthRepository.setAccessToken(new_access_token);
                            apiHelper.patch(urlWithNewAccessToken, data).then(
                                function (data) {
                                    deferred.resolve(data);
                                },
                                function (error) {
                                    deferred.reject(new UnknownException());
                                }
                            );
                        },
                        function (error) {
                            oAuthRepository.clearData();
                            deferred.reject(error);
                        }
                    )
                }
                else {
                    deferred.reject(new UnknownException());
                }
            }
        );

        return deferred.promise;
    };

    return OAuthApiHelper;
}]);