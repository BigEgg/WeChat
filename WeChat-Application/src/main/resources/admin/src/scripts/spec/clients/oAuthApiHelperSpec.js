describe('OAuth API Helper Test', function () {
    var mockApiHelper, mockOAuthClient;

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

        mockOAuthClient = {
            getAccessToken: function (clientId, clientSecret) {
            },
            refreshAccessToken: function (access_token, refresh_token) {
            }
        };
        $provide.value('oAuthClient', mockOAuthClient);
    }));

    describe('When access token valid', function () {
        describe('for GET method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'get').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.get('access', 'refresh', '/api/test').then(
                    function (data) {
                        expect(data.data.test).toBe('value');
                        expect(data.access_token).toBe('');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=access');
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'get').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.get('access', 'refresh', '/api/test').then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error).toBe(500);
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=access');
            }));
        });

        describe('for POST method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'post').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.post('access', 'refresh', '/api/test', {test: 'value'}).then(
                    function (data) {
                        expect(data.data.test).toBe('value');
                        expect(data.access_token).toBe('');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'post').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.post('access', 'refresh', '/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error).toBe(500);
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
            }));
        });
    });

    describe('when access token invalid and can refresh token', function () {
        describe('for GET method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'get').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.get('access', 'refresh', '/api/test').then(
                    function (data) {
                        expect(data.data.test).toBe('value');
                        expect(data.access_token).toBe('new_access');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=new_access');
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'get').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.get('access', 'refresh', '/api/test').then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error.error).toBe(500);
                        expect(error.access_token).toBe('new_access');
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=new_access');
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });

        describe('for POST method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.post('access', 'refresh', '/api/test', {test: 'value'}).then(
                    function (data) {
                        expect(data.data.test).toBe('value');
                        expect(data.access_token).toBe('new_access');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=new_access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.post('access', 'refresh', '/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error.error).toBe(500);
                        expect(error.access_token).toBe('new_access');
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=new_access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });
    });

    describe('when both access token and refresh token are invalid ', function () {
        describe('for GET method', function () {
            it('should throw authenticate failed error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                });
                spyOn(mockApiHelper, 'get').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.reject(new AuthenticateFailedException());
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.get('access', 'refresh', '/api/test').then(
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
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=access');
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });

        describe('for POST method', function () {
            it('should throw authenticate failed error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                });
                spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.reject(new AuthenticateFailedException());
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.post('access', 'refresh', '/api/test', {test: 'value'}).then(
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
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });
    });
});