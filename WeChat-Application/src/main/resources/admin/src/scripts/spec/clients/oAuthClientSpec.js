describe('OAuth Client Test', function () {
    var mockApiHelper;

    beforeEach(angular.mock.module('adminApp', function ($provide, $translateProvider) {
        ignoreTranslate($provide, $translateProvider);
        ignoreRoute($provide);

        mockApiHelper = {
            get: function (url) {
            },
            post: function (url, data) {
            },
            addParameterToURL: function (url, key, value) {
            }
        };
        $provide.value('apiHelper', mockApiHelper);
    }));

    describe('when try to get access token', function () {
        it('should return tokens if success', inject(function ($rootScope, $q, oAuthClient) {
            spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                if (url === '/uas/oauth/accesstoken' && data.clientId === 'username' && data.clientSecret === 'password') {
                    var deferred = $q.defer();
                    deferred.resolve({access_token: 'access', refresh_token: 'refresh'});
                    return deferred.promise;
                }
            });

            var onSuccess = false;
            var onError = false;
            oAuthClient.getAccessToken('username', 'password').then(
                function (data) {
                    expect(data.access_token).toBe('access');
                    expect(data.refresh_token).toBe('refresh');

                    onSuccess = true;
                },
                function (error) {
                    onError = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeTruthy();
            expect(onError).toBeFalsy();
            expect(mockApiHelper.post).toHaveBeenCalledWith('/uas/oauth/accesstoken', {
                clientId: 'username',
                clientSecret: 'password'
            });
        }));

        it('should throw Authorize Failed Error if failed', inject(function ($rootScope, $q, oAuthClient) {
            spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                if (url === '/uas/oauth/accesstoken' && data.clientId === 'username' && data.clientSecret === 'password') {
                    var deferred = $q.defer();
                    deferred.reject(401);
                    return deferred.promise;
                }
            });

            var onSuccess = false;
            var onError = false;
            oAuthClient.getAccessToken('username', 'password').then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof AuthorizeFailedException).toBeTruthy();
                    onError = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
            expect(mockApiHelper.post).toHaveBeenCalledWith('/uas/oauth/accesstoken', {
                clientId: 'username',
                clientSecret: 'password'
            });
        }));

        it('should throw Error if return error', inject(function ($rootScope, $q, oAuthClient) {
            spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                if (url === '/uas/oauth/accesstoken' && data.clientId === 'username' && data.clientSecret === 'password') {
                    var deferred = $q.defer();
                    deferred.reject(new BadNetworkException());
                    return deferred.promise;
                }
            });

            var onSuccess = false;
            var onError = false;
            oAuthClient.getAccessToken('username', 'password').then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof BadNetworkException).toBeTruthy();
                    onError = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
            expect(mockApiHelper.post).toHaveBeenCalledWith('/uas/oauth/accesstoken', {
                clientId: 'username',
                clientSecret: 'password'
            });
        }));

        it('should throw Unknown Error if cannot sure what goes wrong', inject(function ($rootScope, $q, oAuthClient) {
            spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                if (url === '/uas/oauth/accesstoken' && data.clientId === 'username' && data.clientSecret === 'password') {
                    var deferred = $q.defer();
                    deferred.reject(500);
                    return deferred.promise;
                }
            });

            var onSuccess = false;
            var onError = false;
            oAuthClient.getAccessToken('username', 'password').then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof UnknownException).toBeTruthy();
                    onError = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
            expect(mockApiHelper.post).toHaveBeenCalledWith('/uas/oauth/accesstoken', {
                clientId: 'username',
                clientSecret: 'password'
            });
        }));
    });

    describe('when try to refresh access token', function () {
        it('should return new access token if success', inject(function ($rootScope, $q, oAuthClient) {
            spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                if (url === '/uas/oauth/refresh' && data.access_token === 'access' && data.refresh_token === 'refresh') {
                    var deferred = $q.defer();
                    deferred.resolve({access_token: 'new_access', refresh_token: 'refresh'});
                    return deferred.promise;
                }
            });

            var onSuccess = false;
            var onError = false;
            oAuthClient.refreshAccessToken('access', 'refresh').then(
                function (data) {
                    expect(data).toBe('new_access');
                    onSuccess = true;
                },
                function (error) {
                    onError = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeTruthy();
            expect(onError).toBeFalsy();
            expect(mockApiHelper.post).toHaveBeenCalledWith('/uas/oauth/refresh', {
                access_token: 'access',
                refresh_token: 'refresh'
            });
        }));

        it('should throw Authenticate Failed Error if failed', inject(function ($rootScope, $q, oAuthClient) {
            spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                if (url === '/uas/oauth/refresh' && data.access_token === 'access' && data.refresh_token === 'refresh') {
                    var deferred = $q.defer();
                    deferred.reject(403);
                    return deferred.promise;
                }
            });

            var onSuccess = false;
            var onError = false;
            oAuthClient.refreshAccessToken('access', 'refresh').then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof AuthenticateFailedException).toBeTruthy();
                    onError = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
            expect(mockApiHelper.post).toHaveBeenCalledWith('/uas/oauth/refresh', {
                access_token: 'access',
                refresh_token: 'refresh'
            });
        }));

        it('should throw Error if return error', inject(function ($rootScope, $q, oAuthClient) {
            spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                if (url === '/uas/oauth/refresh' && data.access_token === 'access' && data.refresh_token === 'refresh') {
                    var deferred = $q.defer();
                    deferred.reject(new BadNetworkException());
                    return deferred.promise;
                }
            });

            var onSuccess = false;
            var onError = false;
            oAuthClient.refreshAccessToken('access', 'refresh').then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof BadNetworkException).toBeTruthy();
                    onError = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
            expect(mockApiHelper.post).toHaveBeenCalledWith('/uas/oauth/refresh', {
                access_token: 'access',
                refresh_token: 'refresh'
            });
        }));

        it('should throw Unknown Error if cannot sure what goes wrong', inject(function ($rootScope, $q, oAuthClient) {
            spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                if (url === '/uas/oauth/refresh' && data.access_token === 'access' && data.refresh_token === 'refresh') {
                    var deferred = $q.defer();
                    deferred.reject(500);
                    return deferred.promise;
                }
            });

            var onSuccess = false;
            var onError = false;
            oAuthClient.refreshAccessToken('access', 'refresh').then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof UnknownException).toBeTruthy();
                    onError = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
            expect(mockApiHelper.post).toHaveBeenCalledWith('/uas/oauth/refresh', {
                access_token: 'access',
                refresh_token: 'refresh'
            });
        }));
    });
});